@echo off
start "" javaw -jar GestionPracticasDesktop.jar
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error al iniciar. Verifique que Java 17 o superior este instalado.
    pause
)
