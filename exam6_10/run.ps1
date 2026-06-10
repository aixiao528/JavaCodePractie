$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$outputDir = Join-Path $projectRoot "out"

if (-not (Test-Path $outputDir)) {
    throw "请先执行 .\build.ps1 编译项目。"
}

$classpath = ".;out;lib\*"

java -cp $classpath com.exam.course.Main
