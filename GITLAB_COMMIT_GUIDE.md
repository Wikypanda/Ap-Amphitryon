# Guide de Commit du Projet Amphitryon sur GitLab

## 📋 Contexte
Le projet a eu des problèmes lors du `git add .`:
- Chemins de fichiers trop longs dans le cache Gradle
- Sous-répertoire `ap-amphitryon/` avec son propre dépôt Git

## ✅ Solution

### Étape 1: Suppression du sous-dépôt problématique
```powershell
Remove-Item -Path "F:\Ap-Amphitryon\ap-amphitryon\.git" -Recurse -Force
```

### Étape 2: Mise à jour du .gitignore
Le fichier `.gitignore` a été mis à jour pour exclure:
- `caches/` - Cache Gradle avec chemins trop longs
- `daemon/` - Daemon Gradle
- `wrapper/dists/` - Distribution Gradle
- `native/` - Répertoires natifs
- `jdks/` - JDKs Android
- `.tmp/` - Fichiers temporaires
- `ap-amphitryon/.git/` - Sous-dépôt

### Étape 3: Exécution du script de commit

#### Option A: PowerShell (recommandé)
```powershell
cd F:\Ap-Amphitryon
.\commit_to_gitlab.ps1
```

#### Option B: Batch
```cmd
F:\Ap-Amphitryon\fix_git.bat
```

#### Option C: Commandes manuelles
```powershell
cd F:\Ap-Amphitryon

# Configurer Git
git config user.name "Votre Nom"
git config user.email "votre.email@example.com"

# Ajouter les fichiers (le .gitignore sera respecté)
git add .

# Créer le commit
git commit -m "Initial commit: Projet Amphitryon - Gestion de restaurant Android"

# Ajouter la remote
git remote add origin https://gitlab.com/eliasperez2006/ap-amphitryon.git

# Push vers GitLab
git push -u origin main
```

## 🔐 Authentification GitLab

Vous avez deux options:

### Option 1: Token d'accès personnel (recommandé)
1. Allez sur https://gitlab.com/profile/personal_access_tokens
2. Créez un nouveau token avec les scopes `api` et `write_repository`
3. Lors du push, utilisez:
   ```
   URL: https://gitlab.com/eliasperez2006/ap-amphitryon.git
   Username: <any>
   Password: <votre_token>
   ```

### Option 2: Clé SSH
1. Générez une clé SSH: `ssh-keygen -t ed25519 -C "votre.email@example.com"`
2. Ajoutez la clé à GitLab: https://gitlab.com/profile/keys
3. Modifiez l'URL du dépôt:
   ```powershell
   git remote set-url origin git@gitlab.com:eliasperez2006/ap-amphitryon.git
   ```

## 📊 Fichiers ignorés

Les fichiers suivants ne seront **pas** pushés:
- `.gradle/` - Cache Gradle
- `build/` - Résultats de compilation
- `.idea/` - Configuration IntelliJ
- `app/build/` - Build de l'application
- `*.apk`, `*.aab` - Applications compilées
- `*.log` - Fichiers de log
- `local.properties` - Configuration locale

## ✨ Résultat

Après exécution du script:
- ✅ Dépôt Git créé avec commit initial
- ✅ Branche `main` créée
- ✅ Remote `origin` pointant vers GitLab
- ✅ Code poussé vers GitLab

## 🔍 Vérification

Vérifiez que le commit a été poussé:
```
https://gitlab.com/eliasperez2006/ap-amphitryon
```

## ⚠️ Notes

- Le premier push peut être lent (transfert du projet entier)
- Les identifiants GitLab seront demandés lors du push
- En cas d'erreur 502, attendez quelques minutes et réessayez
