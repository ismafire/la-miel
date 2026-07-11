@echo off
title La Miel - Starting Store Server
cd /d "%~dp0"

REM Point directly at the local Maven install (no PATH setup needed)
set "MAVEN_BIN=%USERPROFILE%\Desktop\Apache-Maven\apache-maven-3.9.16\bin\mvn.cmd"

echo ============================================
echo   Starting La Miel store server...
echo ============================================
echo.

REM Check Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not on PATH.
    echo Please install Java 17+ and try again.
    pause
    exit /b 1
)

REM Check Maven exists at the expected location
if not exist "%MAVEN_BIN%" (
    echo [ERROR] Maven not found at:
    echo    %MAVEN_BIN%
    echo If you moved the Apache Maven folder, update MAVEN_BIN in this file.
    pause
    exit /b 1
)

echo Java and Maven found. Launching server...
echo Once it says "Started LaMielApplication", open this in your browser:
echo    http://localhost:8080
echo.
echo Press CTRL+C in this window to stop the server.
echo ============================================
echo.

call "%MAVEN_BIN%" spring-boot:run

echo.
echo Server stopped.
pause
