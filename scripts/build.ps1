$ErrorActionPreference = 'Stop'

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$packDirectory = Join-Path $repositoryRoot 'pack'
$buildDirectory = Join-Path $repositoryRoot 'build'
$packwizExecutable = Join-Path $repositoryRoot '.tools/bin/packwiz.exe'
$output = Join-Path $buildDirectory 'computerised-rebuilt-0.1.0-prototype.mrpack'

if (-not (Test-Path -LiteralPath $packwizExecutable)) {
    $packwizExecutable = & (Join-Path $PSScriptRoot 'bootstrap-packwiz.ps1')
}

New-Item -ItemType Directory -Force -Path $buildDirectory | Out-Null
Push-Location $packDirectory
try {
    & $packwizExecutable refresh
    if ($LASTEXITCODE -ne 0) { throw 'packwiz refresh failed' }
    & $packwizExecutable modrinth export -o $output
    if ($LASTEXITCODE -ne 0) { throw 'packwiz export failed' }
}
finally {
    Pop-Location
}

Get-FileHash $output -Algorithm SHA256
