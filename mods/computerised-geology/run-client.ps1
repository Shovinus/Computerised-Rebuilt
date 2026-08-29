$ErrorActionPreference = "Stop"

$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
if (-not (Test-Path -LiteralPath (Join-Path $javaHome "bin\java.exe"))) {
    throw "Java 17 was not found at $javaHome"
}

$env:JAVA_HOME = $javaHome
& "$PSScriptRoot\gradlew.bat" runClient
exit $LASTEXITCODE
