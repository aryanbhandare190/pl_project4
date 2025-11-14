# run_tests.ps1 - compile and run tests for project4
# Usage: Open PowerShell in this folder and run: .\run_tests.ps1

$javac = Get-Command javac -ErrorAction SilentlyContinue
$java = Get-Command java -ErrorAction SilentlyContinue

if (-not $javac -or -not $java) {
    Write-Host "javac or java not found in PATH. Please install a JDK (e.g., OpenJDK) and ensure 'javac' and 'java' are on PATH." -ForegroundColor Yellow
    Write-Host "Helpful links: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/"
    exit 1
}

if (-not (Test-Path bin)) { New-Item -ItemType Directory bin | Out-Null }

Write-Host "Compiling sources..."
javac -d bin src\*.java
if ($LASTEXITCODE -ne 0) { Write-Host "Compilation failed" -ForegroundColor Red; exit $LASTEXITCODE }

$tests = @("TestPart1","TestPart2","TestPart3","TestPart4")
foreach ($t in $tests) {
    Write-Host "\nRunning $t" -ForegroundColor Cyan
    java -cp bin $t
}

Write-Host "\nDone." -ForegroundColor Green
