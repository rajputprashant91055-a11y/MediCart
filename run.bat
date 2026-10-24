@echo off
title MediCart Application
echo ==========================================
echo    MediCart - Medicine Ordering App
echo ==========================================
echo.

cd /d "%~dp0"

echo [1/3] Checking Java...
java -version 2>&1 | findstr /i "version" >nul
if errorlevel 1 (
    echo ERROR: Java not found! Install JDK from https://adoptium.net
    pause
    exit /b 1
)
echo      Java found.

echo [2/3] Compiling...
javac -cp ".;lib\mysql-connector-j-26.7.0.jar" Main.java ui\*.java dao\*.java model\*.java service\*.java database\*.java 2>compile_errors.txt
if errorlevel 1 (
    echo ERROR: Compilation failed! See errors:
    type compile_errors.txt
    pause
    exit /b 1
)
del compile_errors.txt 2>nul
echo      Compiled successfully.

echo [3/3] Starting MediCart...
echo.
java -cp ".;lib\mysql-connector-j-26.7.0.jar" Main
if errorlevel 1 (
    echo.
    echo App exited with an error. Make sure MySQL is running.
    pause
)
