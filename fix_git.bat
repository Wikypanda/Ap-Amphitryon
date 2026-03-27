@echo off
REM Nettoyer le répertoire .git problématique
cd F:\Ap-Amphitryon
rmdir /s /q ap-amphitryon\.git
REM Ajouter les fichiers à Git
git add .
REM Créer le commit initial
git commit -m "Initial commit: Projet Amphitryon - Gestion de restaurant Android"
REM Ajouter la remote GitLab
git remote add origin https://gitlab.com/eliasperez2006/ap-amphitryon.git
REM Push vers le dépôt distant
git push -u origin main
echo Commit et push terminés avec succès!
pause
