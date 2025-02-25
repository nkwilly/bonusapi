# API de Bonification

## Présentation de l'API

L'API de Bonification est un service backend conçu pour gérer et appliquer des règles de bonification dans un système de récompenses ou de fidélité. Elle permet aux administrateurs de définir des règles de bonification personnalisées qui peuvent être appliquées automatiquement en fonction des actions des utilisateurs ou des transactions effectuées.

### Fonctionnalités principales :
- **Création et gestion des règles de bonification** : Les administrateurs peuvent créer, modifier et supprimer des règles de bonification via l'API.
- **Application des règles** : L'API applique automatiquement les règles de bonification en fonction des critères définis (par exemple, montant d'une transaction, fréquence d'utilisation, etc.).
- **Suivi des bonifications** : L'API permet de suivre les bonifications attribuées à chaque utilisateur et de générer des rapports détaillés.

### Endpoints principaux :
- `POST /rules` : Crée une nouvelle règle de bonification.
- `GET /rules` : Récupère la liste des règles de bonification existantes.
- `PUT /rules/{id}` : Met à jour une règle de bonification existante.
- `DELETE /rules/{id}` : Supprime une règle de bonification.
- `POST /transactions` : Initie une transaction qui est automatiquement analysée par l'API et décide si oui ou non il y a besoin d'appliquer la bonification en fonction des entrées. 

## Frontend de Configuration des Règles de Bonification

Le frontend est une interface utilisateur intuitive qui permet aux administrateurs de configurer et de gérer les règles de bonification de manière simple et efficace. Il est conçu pour offrir une expérience utilisateur fluide et permettre une configuration rapide des règles.

### Fonctionnalités principales :
- **Création de règles** : Les administrateurs peuvent créer de nouvelles règles de bonification en spécifiant les critères et les actions à appliquer.
- **Modification des règles** : Les règles existantes peuvent être modifiées à tout moment pour s'adapter aux besoins changeants.
- **Suppression des règles** : Les règles obsolètes ou inutiles peuvent être supprimées facilement.
- **Visualisation des règles** : Les règles sont affichées dans une liste claire et organisée, avec des détails sur les critères et les actions associées.

### Règles de configuration :
1. **Critères** :
   - **Montant de la transaction** : Définir un seuil minimum ou maximum pour déclencher la bonification.
   - **Fréquence d'utilisation** : Appliquer une bonification après un certain nombre de transactions ou d'actions.
   - **Catégorie de produit/service** : Limiter la bonification à certaines catégories de produits ou services.

2. **Actions** :
   - **Points de fidélité** : Ajouter un nombre spécifique de points de fidélité à l'utilisateur.
   - **Réduction sur la prochaine transaction** : Offrir une réduction en pourcentage ou en montant fixe sur la prochaine transaction.
   - **Cadeaux** : Offrir un cadeau ou un produit gratuit après un certain nombre de transactions.

3. **Conditions supplémentaires** :
   - **Période de validité** : Définir une période pendant laquelle la règle est active.
   - **Limite d'utilisation** : Limiter le nombre de fois qu'une règle peut être appliquée par utilisateur.

### Exemple de configuration :
- **Règle** : "Bonus de bienvenue"
  - **Critère** : Première transaction de l'utilisateur.
  - **Action** : Ajouter 100 points de fidélité.
  - **Condition** : Valable uniquement pendant les 30 premiers jours après l'inscription.

- **Règle** : "Réduction fréquente"
  - **Critère** : 5 transactions dans un mois.
  - **Action** : Offrir une réduction de 10% sur la prochaine transaction.
  - **Condition** : Limite de 1 utilisation par utilisateur par mois.

## Installation et Utilisation

### Backend (API) :
1. Clonez le dépôt : `git clone https://github.com/Momo-azangue/bonusapi.git`
2. Installez les dépendances : `mvn install`
3. Configurez les variables d'environnement dans `.env`.
4. Lancez le serveur : `mvn spring-boot:run`

### Frontend :
1. Clonez le dépôt : `git clone https://github.com/votre-repo/frontend-bonification.git](https://github.com/nkwilly/bonusFront.git`
2. Installez les dépendances : `npm install`
3. Configurez l'URL de l'API dans `src/store`.
4. Lancez l'application : `npm run dev`

## Contribution


Ce projet a été développé et maintenu par :

- **[Momo Azangue](https://github.com/Momo-azangue)**  
- **[Willy Watcho](https://github.com/nkwilly)**  
- **[Danga Blonde](https://github.com/BlondeQueen)**  
- **[Noudjeu Franck](https//github.com/franckettheofranckettheo)**
- **[Temgoua Kros](https//github.com/krostemgoua)**
- **[Kengne Fresnel](https//github.com/237Fresnel)**

## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

---

Pour toute question ou support, veuillez contacter [contact support](watchowilly@gmail.com).
