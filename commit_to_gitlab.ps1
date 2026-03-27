#!/usr/bin/env pwsh

# Script pour préparer et commiter le projet Amphitryon sur GitLab

$projectPath = "F:\Ap-Amphitryon"
$gitlabUrl = "https://gitlab.com/eliasperez2006/ap-amphitryon.git"

Write-Host "🚀 Nettoyage du répertoire Git problématique..." -ForegroundColor Cyan

# Supprimer le .git du sous-répertoire
if (Test-Path "$projectPath\ap-amphitryon\.git") {
    Remove-Item -Path "$projectPath\ap-amphitryon\.git" -Recurse -Force
    Write-Host "✅ Répertoire .git supprimé" -ForegroundColor Green
}

# Aller dans le répertoire du projet
Set-Location $projectPath

Write-Host "`n📦 Configuration de Git..." -ForegroundColor Cyan

# Configurer Git
git config user.name "Elias Perez"
git config user.email "eliasperez2006@example.com"

Write-Host "✅ Configuration Git complète" -ForegroundColor Green

Write-Host "`n📝 Ajout des fichiers..." -ForegroundColor Cyan

# Ajouter tous les fichiers (respecte le .gitignore)
git add .

Write-Host "✅ Fichiers ajoutés" -ForegroundColor Green

Write-Host "`n💾 Création du commit..." -ForegroundColor Cyan

# Créer le commit
git commit -m "Initial commit: Projet Amphitryon - Gestion de restaurant Android"

Write-Host "✅ Commit créé" -ForegroundColor Green

Write-Host "`n🔗 Ajout de la remote GitLab..." -ForegroundColor Cyan

# Ajouter la remote
git remote add origin $gitlabUrl

Write-Host "✅ Remote ajoutée" -ForegroundColor Green

Write-Host "`n🚀 Push vers GitLab..." -ForegroundColor Cyan
Write-Host "⚠️  Vous devrez entrer vos identifiants GitLab" -ForegroundColor Yellow

# Push vers GitLab
git push -u origin main

Write-Host "`n✅ Projet commité et poussé avec succès sur GitLab!" -ForegroundColor Green
Write-Host "📍 Repository: $gitlabUrl" -ForegroundColor Cyan
