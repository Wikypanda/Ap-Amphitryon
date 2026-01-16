# Amphitryon - Application Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          AMPHITRYON RESTAURANT APP                           │
│                         Full Android MVVM Clean Architecture                 │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER (UI)                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐        │
│  │   MainActivity   │   │ ChefSalleActivity│   │ChefCuisinierActvty│        │
│  │  (Role Select)   │──▶│   (Tables &      │   │   (Dishes &      │        │
│  │                  │   │   Assignments)   │   │   Services)      │        │
│  └──────────────────┘   └────────┬─────────┘   └────────┬─────────┘        │
│           │                       │                       │                  │
│           │              ┌────────▼─────────┐   ┌────────▼─────────┐        │
│           │              │ ChefSalleViewModel│   │ChefCuisinierVM   │        │
│           │              │  - tables: Flow   │   │  - plats: Flow   │        │
│           │              │  - affectations   │   │  - platServices  │        │
│           │              └───────────────────┘   └──────────────────┘        │
│           │                                                                   │
│  ┌────────▼─────────┐   ┌──────────────────┐                                │
│  │ ServeurActivity  │   │  RecyclerView    │                                │
│  │   (Orders &      │   │   Adapters       │                                │
│  │   Payments)      │   │  - TableAdapter  │                                │
│  └────────┬─────────┘   │  - PlatAdapter   │                                │
│           │              │  - CommandeAdapter│                               │
│  ┌────────▼─────────┐   └──────────────────┘                                │
│  │  ServeurViewModel│                                                        │
│  │  - commandes     │                                                        │
│  │  - lignesCommande│                                                        │
│  └──────────────────┘                                                        │
│                                                                               │
└───────────────────────────────────────┬───────────────────────────────────────┘
                                        │ Uses (Dependency Injection via Hilt)
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DOMAIN LAYER (Business Logic)                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌─────────────────────────────────────────────────────────────────┐        │
│  │                         Enums.kt                                 │        │
│  │  • Service (DEJEUNER, DINER)                                    │        │
│  │  • CategoriePlat (ENTREE, PLAT_PRINCIPAL, DESSERT)              │        │
│  │  • EtatPlat (COMMANDE, SERVI, DEBARRASSE)                       │        │
│  │  • RoleUtilisateur (CHEF_DE_SALLE, CHEF_CUISINIER, SERVEUR)    │        │
│  └─────────────────────────────────────────────────────────────────┘        │
│                                                                               │
└───────────────────────────────────────┬───────────────────────────────────────┘
                                        │ Used by
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DATA LAYER (Repositories)                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐          │
│  │ TableRepository  │  │ PlatRepository   │  │ServeurRepository │          │
│  │  (CRUD Tables)   │  │ (CRUD Dishes &   │  │ (CRUD Servers)   │          │
│  └────────┬─────────┘  │  Services)       │  └──────────────────┘          │
│           │             └────────┬─────────┘                                 │
│           │                      │                                           │
│  ┌────────▼─────────┐  ┌────────▼─────────┐                                │
│  │ AffectationRepo  │  │ CommandeRepository│                               │
│  │ (Assignments)    │  │ (Orders & Lines)  │                               │
│  └──────────────────┘  └───────────────────┘                               │
│                                                                               │
└───────────────────────────────────────┬───────────────────────────────────────┘
                                        │ Uses
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DATA LAYER (Room Database - Local)                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────┐           │
│  │                  RestaurantDatabase.kt                        │           │
│  │              (Room Database with Converters)                  │           │
│  └────┬─────────────────────────────────────────────────────┬───┘           │
│       │                                                      │               │
│       ▼                                                      ▼               │
│  ┌───────────────────────┐                      ┌───────────────────────┐   │
│  │  DAOs (5)             │                      │  Entities (7)         │   │
│  │  ┌─────────────────┐  │                      │  ┌─────────────────┐  │   │
│  │  │ TableDao        │  │◀─────────────────────│  │ TableEntity     │  │   │
│  │  │ ServeurDao      │  │◀─────────────────────│  │ ServeurEntity   │  │   │
│  │  │ PlatDao         │  │◀─────────────────────│  │ PlatEntity      │  │   │
│  │  │ AffectationDao  │  │◀─────────────────────│  │ PlatServiceEnt  │  │   │
│  │  │ CommandeDao     │  │◀─────────────────────│  │ AffectationEnt  │  │   │
│  │  └─────────────────┘  │                      │  │ CommandeEntity  │  │   │
│  │                       │                      │  │ LigneCommandeEnt│  │   │
│  │  CRUD Operations      │                      │  └─────────────────┘  │   │
│  │  + Flow Queries       │                      │                       │   │
│  └───────────────────────┘                      │  Foreign Keys         │   │
│                                                  │  + Indices            │   │
│                                                  └───────────────────────┘   │
│                                                                               │
└───────────────────────────────────────┬───────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DEPENDENCY INJECTION (Hilt)                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────┐           │
│  │             RestaurantApplication.kt                          │           │
│  │                @HiltAndroidApp                                │           │
│  └────┬─────────────────────────────────────────────────────────┘           │
│       │                                                                       │
│       ▼                                                                       │
│  ┌────────────────────┐          ┌────────────────────┐                     │
│  │  DatabaseModule    │          │    AppModule       │                     │
│  │  Provides:         │          │   (Additional      │                     │
│  │  • Database        │          │    dependencies)   │                     │
│  │  • DAOs (5)        │          └────────────────────┘                     │
│  └────────────────────┘                                                      │
│                                                                               │
└─────────────────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════════════════
                            KEY TECHNOLOGIES
═══════════════════════════════════════════════════════════════════════════════

 • Room Database 2.6.1        → Local data persistence with SQLite
 • Hilt 2.48                   → Dependency injection framework
 • Kotlin Coroutines 1.7.3     → Asynchronous programming
 • Flow                        → Reactive data streams
 • Material Design 3           → Modern UI components
 • ViewBinding                 → Type-safe view access
 • RecyclerView + DiffUtil     → Efficient list rendering
 • Gradle 8.2 + KSP            → Build system with annotation processing

═══════════════════════════════════════════════════════════════════════════════
                            FEATURE COVERAGE
═══════════════════════════════════════════════════════════════════════════════

┌────────────────────┬──────────────────────────────────────────────────────┐
│ Chef de Salle      │ • Manage tables (CRUD)                                │
│                    │ • Assign tables to servers by service                │
│                    │ • View assignments by service                        │
├────────────────────┼──────────────────────────────────────────────────────┤
│ Chef Cuisinier     │ • Manage dishes (CRUD)                                │
│                    │ • Propose dishes for services with prices            │
│                    │ • Manage quantities (available & sold)               │
│                    │ • Categorize dishes (Starter, Main, Dessert)         │
├────────────────────┼──────────────────────────────────────────────────────┤
│ Serveur            │ • Manage orders (CRUD)                                │
│                    │ • Add dishes to orders with notes                    │
│                    │ • Track dish status (Ordered, Served, Cleared)       │
│                    │ • Mark orders as paid                                │
└────────────────────┴──────────────────────────────────────────────────────┘
