$ErrorActionPreference = 'Stop'
$state = Join-Path (Split-Path $PSScriptRoot -Parent) '.local-services/processes.json'
if (!(Test-Path $state)) { Write-Host 'No managed services.'; exit }
foreach ($entry in (Get-Content $state -Raw | ConvertFrom-Json)) {
    $process = Get-Process -Id $entry.id -ErrorAction SilentlyContinue
    if ($process -and $process.StartTime.ToUniversalTime().Ticks -eq $entry.started) {
        Stop-Process -Id $process.Id
    }
}
Remove-Item -LiteralPath $state
Write-Host 'Services stopped.'
