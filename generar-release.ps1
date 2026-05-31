# =============================================================
# generar-release.ps1
# Genera la carpeta release/ con el fat JAR listo para distribuir
#
# Uso: click derecho en este archivo > "Ejecutar con PowerShell"
#      O desde terminal: .\generar-release.ps1
#
# Requisito previo: haber compilado el proyecto en NetBeans
#                   (Shift+F11 / Clean and Build)
# =============================================================

$ErrorActionPreference = "Stop"
$base    = $PSScriptRoot
$dist    = "$base\dist\GestionPracticasDesktop.jar"
$lib     = "$base\lib"
$release = "$base\release"
$tmp     = "$base\build\fat-jar-tmp"
$fatJar  = "$release\GestionPracticasDesktop.jar"

# --- Verificaciones previas ---
if (-not (Test-Path $dist)) {
    Write-Host ""
    Write-Host "ERROR: No se encontro $dist" -ForegroundColor Red
    Write-Host "Compila el proyecto en NetBeans primero (Shift+F11) y vuelve a ejecutar este script." -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Pulsa Enter para salir"
    exit 1
}

if (-not (Test-Path "$lib\ojdbc17.jar")) {
    Write-Host ""
    Write-Host "ERROR: No se encontro lib\ojdbc17.jar" -ForegroundColor Red
    Write-Host "Copia el driver ojdbc17.jar a la carpeta lib\ del proyecto." -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Pulsa Enter para salir"
    exit 1
}

Write-Host ""
Write-Host "Generando release..." -ForegroundColor Cyan

# --- Limpiar directorios temporales y release anterior ---
if (Test-Path $tmp)     { Remove-Item $tmp     -Recurse -Force }
if (Test-Path $release) { Remove-Item $release -Recurse -Force }
New-Item -ItemType Directory -Path $tmp     | Out-Null
New-Item -ItemType Directory -Path $release | Out-Null

# --- Descomprimir el driver ojdbc en el directorio temporal ---
Write-Host "  Descomprimiendo ojdbc17.jar..."
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory("$lib\ojdbc17.jar", $tmp)

# --- Descomprimir el JAR compilado encima (clases del proyecto) ---
Write-Host "  Descomprimiendo clases del proyecto..."
[System.IO.Compression.ZipFile]::ExtractToDirectory($dist, $tmp)

# --- Eliminar el MANIFEST del driver para usar el nuestro ---
$manifestDir = "$tmp\META-INF"
if (Test-Path "$manifestDir\MANIFEST.MF") {
    Remove-Item "$manifestDir\MANIFEST.MF" -Force
}

# --- Escribir el MANIFEST correcto ---
$manifest = "Manifest-Version: 1.0`r`nMain-Class: com.practicas.view.Main`r`nBuilt-By: GestionPracticas`r`n`r`n"
Set-Content -Path "$manifestDir\MANIFEST.MF" -Value $manifest -NoNewline

# --- Empaquetar todo en el fat JAR ---
Write-Host "  Empaquetando fat JAR..."
[System.IO.Compression.ZipFile]::CreateFromDirectory($tmp, $fatJar)

# --- Copiar archivos de la release ---
Write-Host "  Copiando archivos..."
Copy-Item "$base\config.properties" "$release\config.properties"

if (Test-Path "$base\MANUAL_USUARIO.md") {
    Copy-Item "$base\MANUAL_USUARIO.md" "$release\MANUAL_USUARIO.md"
}

# --- Crear lanzador Windows ---
$bat = "@echo off`r`njava -jar GestionPracticasDesktop.jar`r`nif %ERRORLEVEL% NEQ 0 (`r`n    echo.`r`n    echo Error al iniciar. Verifique que Java 17 o superior este instalado.`r`n    pause`r`n)`r`n"
Set-Content -Path "$release\iniciar.bat" -Value $bat -NoNewline

# --- Crear lanzador Linux/macOS ---
$sh = "#!/bin/bash`njava -jar GestionPracticasDesktop.jar`n"
Set-Content -Path "$release\iniciar.sh" -Value $sh -NoNewline

# --- Limpiar temporal ---
Remove-Item $tmp -Recurse -Force

# --- Resultado ---
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host " Release generado correctamente en:" -ForegroundColor Green
Write-Host " $release" -ForegroundColor White
Write-Host "--------------------------------------------" -ForegroundColor Green
Write-Host " Contenido:" -ForegroundColor Green
Get-ChildItem $release | ForEach-Object {
    $size = if ($_.Length -gt 1MB) { "{0:N1} MB" -f ($_.Length / 1MB) }
            elseif ($_.Length -gt 1KB) { "{0:N1} KB" -f ($_.Length / 1KB) }
            else { "$($_.Length) B" }
    Write-Host ("   {0,-40} {1}" -f $_.Name, $size) -ForegroundColor White
}
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Read-Host "Pulsa Enter para salir"
