@echo off
echo Nettoyage du projet Amphitryon...
echo.

cd /d F:\Ap-Amphitryon

echo 1. Suppression des dossiers build...
rmdir /s /q app\build 2>nul
rmdir /s /q build 2>nul
rmdir /s /q .gradle 2>nul

echo 2. Nettoyage Gradle...
call gradlew.bat clean

echo 3. Reconstruction du projet...
call gradlew.bat assembleDebug

echo.
echo Terminé! Le projet devrait maintenant compiler correctement.
pause
