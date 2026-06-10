$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$outputDir = Join-Path $projectRoot "out"

if (Test-Path $outputDir) {
    Remove-Item -Recurse -Force $outputDir
}

New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$javaFiles = Get-ChildItem -Path (Join-Path $projectRoot "src\main\java") -Recurse -Filter *.java |
    Select-Object -ExpandProperty FullName

if (-not $javaFiles) {
    throw "未找到 Java 源码文件。"
}

$classpath = ".;lib\*"

javac -encoding UTF-8 -cp $classpath -d $outputDir $javaFiles

Write-Host "编译完成，输出目录: $outputDir"
