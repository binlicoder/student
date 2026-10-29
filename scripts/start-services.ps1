param([switch]$SkipBuild)
$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$backend = Join-Path $root 'springboot-houtai'
$envFile = Join-Path $root '.env'
if (Test-Path $envFile) {
    foreach ($line in Get-Content $envFile) {
        if ($line -match '^([A-Z_]+)=(.*)$') { [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process') }
    }
}
if (!$env:DB_PASSWORD -or !$env:JWT_SECRET -or [Text.Encoding]::UTF8.GetByteCount($env:JWT_SECRET) -lt 32) {
    throw 'Copy .env.example to .env, set DB_PASSWORD and JWT_SECRET (at least 32 UTF-8 bytes).'
}
$state = Join-Path $root '.local-services'
if (Test-Path (Join-Path $state 'processes.json')) { throw 'Run scripts/stop-services.ps1 before starting again.' }
foreach ($port in 8080,8081,8082,8083) {
    $listener = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if ($listener) { throw "Port $port is occupied." }
}
if (!$SkipBuild) {
    & mvn -B -f "$backend/pom.xml" verify
    if ($LASTEXITCODE -ne 0) { throw 'Maven build failed.' }
}
[IO.Directory]::CreateDirectory($state) | Out-Null
$started = @()
try {
    foreach ($service in 'identity-service','academic-service','reporting-service','gateway-service') {
        $jar = Join-Path $backend "$service/target/$service-0.0.1-SNAPSHOT.jar"
        if (!(Test-Path $jar)) { throw "Missing JAR: $jar" }
        $process = Start-Process -FilePath (Get-Command java).Source -ArgumentList @('-jar', "`"$jar`"") -WorkingDirectory $backend -WindowStyle Hidden -PassThru -RedirectStandardOutput "$state/$service.log" -RedirectStandardError "$state/$service.err.log"
        $started += @{ id = $process.Id; started = $process.StartTime.ToUniversalTime().Ticks; service = $service }
    }
    $started | ConvertTo-Json | Set-Content "$state/processes.json"
    $deadline = (Get-Date).AddSeconds(60)
    do {
        $ready = $true
        foreach ($port in 8081,8082,8083,8080) {
            $path = if ($port -eq 8080) { 'health' } else { 'actuator/health' }
            try { $null = Invoke-RestMethod "http://localhost:$port/$path" -TimeoutSec 2 }
            catch { $ready = $false }
        }
        if (!$ready) { Start-Sleep -Seconds 1 }
    } while (!$ready -and (Get-Date) -lt $deadline)
    if (!$ready) { throw "Services did not become ready. Inspect logs in $state" }
    Write-Host 'Services ready. API gateway: http://localhost:8080'
} catch {
    foreach ($entry in $started) { Stop-Process -Id $entry.id -ErrorAction SilentlyContinue }
    Remove-Item -LiteralPath "$state/processes.json" -ErrorAction SilentlyContinue
    throw
}
