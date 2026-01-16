# Amphitryon - Restaurant Management Application

A full-featured Android application built with Kotlin for managing restaurant operations with three distinct user roles: Chef de Salle, Chef Cuisinier, and Serveur.

## Architecture

This application follows **MVVM Clean Architecture** principles with the following technologies:

- **Room Database** - Local data persistence
- **Hilt** - Dependency injection
- **Kotlin Coroutines & Flow** - Asynchronous programming
- **Material Design 3** - Modern UI components
- **Navigation Component** - Screen navigation
- **ViewBinding** - Type-safe view access

## Project Structure

```
app/
├── data/
│   ├── local/database/
│   │   ├── RestaurantDatabase.kt        # Main Room database
│   │   ├── Converters.kt                # Type converters for enums
│   │   ├── dao/                         # Data Access Objects
│   │   │   ├── TableDao.kt
│   │   │   ├── ServeurDao.kt
│   │   │   ├── PlatDao.kt
│   │   │   ├── CommandeDao.kt
│   │   │   └── AffectationDao.kt
│   │   └── entities/                    # Room entities
│   │       ├── TableEntity.kt
│   │       ├── ServeurEntity.kt
│   │       ├── PlatEntity.kt
│   │       ├── PlatServiceEntity.kt
│   │       ├── CommandeEntity.kt
│   │       ├── LigneCommandeEntity.kt
│   │       └── AffectationEntity.kt
│   └── repository/                      # Repository layer
│       ├── TableRepository.kt
│       ├── PlatRepository.kt
│       ├── CommandeRepository.kt
│       ├── ServeurRepository.kt
│       └── AffectationRepository.kt
├── domain/model/
│   └── Enums.kt                         # Domain enums (Service, CategoriePlat, EtatPlat, RoleUtilisateur)
├── presentation/                        # UI layer
│   ├── common/
│   │   └── MainActivity.kt              # Role selection screen
│   ├── chefsalle/
│   │   ├── ChefSalleActivity.kt
│   │   ├── ChefSalleViewModel.kt
│   │   └── tables/
│   │       └── TableAdapter.kt
│   ├── chefcuisinier/
│   │   ├── ChefCuisinierActivity.kt
│   │   ├── ChefCuisinierViewModel.kt
│   │   └── plats/
│   │       └── PlatAdapter.kt
│   └── serveur/
│       ├── ServeurActivity.kt
│       ├── ServeurViewModel.kt
│       └── commandes/
│           └── CommandeAdapter.kt
├── di/                                  # Dependency injection
│   ├── AppModule.kt
│   └── DatabaseModule.kt
└── RestaurantApplication.kt             # Application class
```

## Features

### Chef de Salle
- Create, update, delete, and view restaurant tables
- Assign tables to servers for each service (lunch and dinner)
- Remove or modify table assignments
- View tables and assignments for a given service

### Chef Cuisinier
- Create, update, delete, and view dishes
- Propose dishes for a given service with variable selling prices
- Classify dishes into 3 categories: Starter, Main Course, Dessert
- Remove a dish from a given service
- Define, modify, and view available quantities for each service
- View the list of dishes with available and sold quantities for a day or service

### Serveurs (Servers)
- Create, update, delete, and view orders
- Associate orders with a table and add a timestamp
- Add a list of dishes with additional information per order (e.g., "one rare steak and two cooked medium")
- Modify the status of each dish (ordered, served, cleared)
- Record payment of an order (set status to "paid")

## Database Schema

### Tables
- **tables**: id, numero, nombrePlaces, estOccupee
- **serveurs**: id, nom, prenom
- **plats**: id, numero, nom, descriptif, categorie, prixBase
- **plat_services**: id, platId, date, service, prixVente, quantiteDisponible, quantiteVendue, estPropose
- **affectations**: id, tableId, serveurId, date, service
- **commandes**: id, tableId, dateHeure, service, estReglee
- **lignes_commande**: id, commandeId, platId, quantite, informationsComplementaires, etat

## Building the Project

### Requirements
- Android Studio Arctic Fox or newer
- JDK 17
- Android SDK with minimum API 24 (Android 7.0)
- Gradle 8.2

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Wait for Gradle to sync
4. Run the app on an emulator or physical device

### Build Commands
```bash
# Build the project
./gradlew build

# Install debug APK
./gradlew installDebug

# Run tests
./gradlew test
```

## Dependencies

### Core
- Kotlin 1.9.20
- AndroidX Core KTX 1.12.0
- AppCompat 1.6.1

### UI
- Material Design 3: 1.11.0
- ConstraintLayout 2.1.4
- RecyclerView 1.3.2

### Architecture Components
- Lifecycle ViewModel KTX 2.7.0
- Lifecycle LiveData KTX 2.7.0
- Navigation Fragment KTX 2.7.6

### Database
- Room Runtime 2.6.1
- Room KTX 2.6.1

### Dependency Injection
- Hilt Android 2.48

### Async
- Kotlinx Coroutines Android 1.7.3

## Configuration

The app is configured with:
- **Namespace**: com.restaurant.amphitryon
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## License

This project is developed as part of an academic assignment.

## Authors

Developed for restaurant management operations.
