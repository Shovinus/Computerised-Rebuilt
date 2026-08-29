$ErrorActionPreference = 'Stop'

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$toolsRoot = Join-Path $repositoryRoot '.tools'
$goExecutable = Join-Path $toolsRoot 'go/bin/go.exe'
$packwizDirectory = Join-Path $toolsRoot 'bin'
$packwizExecutable = Join-Path $packwizDirectory 'packwiz.exe'

New-Item -ItemType Directory -Force -Path $toolsRoot, $packwizDirectory | Out-Null

if (-not (Test-Path -LiteralPath $goExecutable)) {
    $release = (Invoke-RestMethod 'https://go.dev/dl/?mode=json')[0]
    $asset = $release.files |
        Where-Object { $_.os -eq 'windows' -and $_.arch -eq 'amd64' -and $_.kind -eq 'archive' } |
        Select-Object -First 1
    $archive = Join-Path $toolsRoot $asset.filename

    Invoke-WebRequest ("https://go.dev/dl/" + $asset.filename) -OutFile $archive
    $actualHash = (Get-FileHash $archive -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($actualHash -ne $asset.sha256) {
        throw "Go archive checksum mismatch: expected $($asset.sha256), got $actualHash"
    }
    Expand-Archive -LiteralPath $archive -DestinationPath $toolsRoot -Force
}

$env:GOBIN = $packwizDirectory
& $goExecutable install github.com/packwiz/packwiz@latest

if (-not (Test-Path -LiteralPath $packwizExecutable)) {
    throw 'Packwiz compilation completed without producing packwiz.exe'
}

Write-Output $packwizExecutable
