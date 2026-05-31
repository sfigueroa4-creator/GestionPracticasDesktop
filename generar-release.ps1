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

# --- Limpiar release anterior ---
if (Test-Path $release) {
    # Borrar solo el JAR anterior para evitar problemas si la carpeta está abierta
    if (Test-Path $fatJar) { Remove-Item $fatJar -Force }
} else {
    New-Item -ItemType Directory -Path $release | Out-Null
}

Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

# --- Construir el fat JAR copiando entradas ZIP directamente en memoria ---
Write-Host "  Construyendo fat JAR..."

$fatStream   = [System.IO.File]::Open($fatJar, [System.IO.FileMode]::Create)
$fatArchive  = New-Object System.IO.Compression.ZipArchive($fatStream, [System.IO.Compression.ZipArchiveMode]::Create)

$entradas = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::OrdinalIgnoreCase)

function Copiar-Zip($zipPath, $esDriver) {
    $stream  = [System.IO.File]::OpenRead($zipPath)
    $archive = New-Object System.IO.Compression.ZipArchive($stream, [System.IO.Compression.ZipArchiveMode]::Read)

    foreach ($entry in $archive.Entries) {
        # Saltar el MANIFEST del driver; usaremos el del proyecto
        if ($esDriver -and $entry.FullName -eq "META-INF/MANIFEST.MF") { continue }
        # Saltar directorios vacíos y duplicados
        if ($entry.FullName.EndsWith("/")) { continue }
        if ($entradas.Contains($entry.FullName)) { continue }

        $entradas.Add($entry.FullName) | Out-Null

        $nuevaEntrada = $fatArchive.CreateEntry($entry.FullName, [System.IO.Compression.CompressionLevel]::Fastest)
        $srcStream    = $entry.Open()
        $dstStream    = $nuevaEntrada.Open()
        $srcStream.CopyTo($dstStream)
        $dstStream.Close()
        $srcStream.Close()
    }

    $archive.Dispose()
    $stream.Dispose()
}

# Primero el driver (sus entradas van primero, excepto el MANIFEST)
Copiar-Zip "$lib\ojdbc17.jar" $true

# Luego el JAR del proyecto (sobreescribe con su MANIFEST)
Copiar-Zip $dist $false

$fatArchive.Dispose()
$fatStream.Dispose()

Write-Host "  Copiando archivos..."
Copy-Item "$base\config.properties" "$release\config.properties"

if (Test-Path "$base\MANUAL_USUARIO.md") {
    Copy-Item "$base\MANUAL_USUARIO.md" "$release\MANUAL_USUARIO.md"
}

# --- Lanzador Windows ---
Set-Content -Path "$release\iniciar.bat" -Encoding ASCII -Value "@echo off`r`njava -jar GestionPracticasDesktop.jar`r`nif %ERRORLEVEL% NEQ 0 (`r`n    echo.`r`n    echo Error al iniciar. Verifique que Java 17 o superior este instalado.`r`n    pause`r`n)"

# --- Lanzador Linux/macOS ---
Set-Content -Path "$release\iniciar.sh" -Encoding UTF8 -Value "#!/bin/bash`njava -jar GestionPracticasDesktop.jar`n"

# --- Verificar el JAR generado ---
Write-Host "  Verificando JAR..."
$verificacion = java -jar $fatJar --version 2>&1
# No importa el resultado, solo que no diga "corrupt"
if ($verificacion -match "corrupt") {
    Write-Host "ADVERTENCIA: El JAR puede estar corrupto." -ForegroundColor Yellow
}

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
