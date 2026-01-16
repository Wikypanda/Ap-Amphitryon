package com.restaurant.amphitryon.domain.model

enum class Service {
    DEJEUNER,
    DINER
}

enum class CategoriePlat {
    ENTREE,
    PLAT_PRINCIPAL,
    DESSERT
}

enum class EtatPlat {
    COMMANDE,
    SERVI,
    DEBARRASSE
}

enum class RoleUtilisateur {
    CHEF_DE_SALLE,
    CHEF_CUISINIER,
    SERVEUR
}
