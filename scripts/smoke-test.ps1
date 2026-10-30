$ErrorActionPreference = 'Stop'
$gateway = 'http://localhost:8080'
$login = Invoke-RestMethod "$gateway/api/auth/login" -Method Post -ContentType 'application/json' -Body '{"username":"admin","password":"admin123"}'
if ($login.code -ne 200 -or !$login.data.token) { throw 'Login failed (requires initialized demo database).' }
$headers = @{ Authorization = "Bearer $($login.data.token)" }
foreach ($path in 'auth/userinfo','students','teachers','classes','courses','grades','course-selections') {
    $result = Invoke-RestMethod "$gateway/api/$path" -Headers $headers
    if ($result.code -ne 200) { throw "API failed: $path" }
}
$stats = Invoke-RestMethod "$gateway/api/stats"
foreach ($key in 'studentCount','teacherCount','classCount','courseCount') {
    if ($null -eq $stats.data.$key) { throw "Missing statistics field: $key" }
}
Write-Host 'Login, authenticated reads and aggregate statistics passed.'
