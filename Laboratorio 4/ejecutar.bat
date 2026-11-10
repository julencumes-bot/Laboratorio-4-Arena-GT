@echo off
setlocal
cd /d "%~dp0"
chcp 65001 >nul
if not exist bin mkdir bin
javac --release 17 -encoding UTF-8 -d bin src\*.java
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -cp bin Aplicacion
exit /b %errorlevel%
