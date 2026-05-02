param(
    [string]$Destination = "github-ready\EtherHack-B42"
)

$ErrorActionPreference = "Stop"

$root = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$target = Join-Path $root $Destination
$resolvedRoot = (Resolve-Path $root).Path

if (Test-Path $target) {
    $resolvedTarget = (Resolve-Path $target).Path
    if (-not $resolvedTarget.StartsWith($resolvedRoot) -or $resolvedTarget -eq $resolvedRoot) {
        throw "Refusing to clean unsafe export path: $resolvedTarget"
    }
    Remove-Item -LiteralPath $resolvedTarget -Recurse -Force
}

New-Item -ItemType Directory -Path $target | Out-Null

$items = @(
    ".github",
    "demo",
    "docs",
    "gradle",
    "src",
    ".gitignore",
    "build.gradle.kts",
    "gradlew",
    "gradlew.bat",
    "LICENSE.txt",
    "qodana.yaml",
    "README.md",
    "README.en.md",
    "README.zh-CN.md",
    "README.ru.md",
    "settings.gradle.kts"
)

foreach ($item in $items) {
    $source = Join-Path $root $item
    if (Test-Path $source) {
        Copy-Item -LiteralPath $source -Destination $target -Recurse -Force
    }
}

New-Item -ItemType Directory -Path (Join-Path $target "lib") | Out-Null
Copy-Item -LiteralPath (Join-Path $root "lib\README.md") -Destination (Join-Path $target "lib\README.md") -Force

Write-Host "GitHub-ready export written to: $target"
