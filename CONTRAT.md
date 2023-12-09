
# Contrat

## Escave

### 1 Univers

### Phase de jeu classique : jeu d'action

* Une grotte sur une planète torique
* Vue en coupe
* Viewport : 40 blocs autour du joueur, qui suit le joueur quand il se déplace
* Des ennemis (champignons vivants), des ressources

### Objectif

* S'échapper : rejoindre la sortie avec l'autre joueur.

### Phase de jeu

* Le joueur incarne une taupe. 
* Pour se déplacer dans la grotte, en plus de marcher, il peut casser et placer des blocs.
* Il devra se défendre contre des monstres qui le poursuivront.
* Pour pouvoir utiliser la sortie, il doit d'abord trouver la statue qui lui est associée, en prendre le contrôle et la placer sur un bloc d'activation (situé devant la sortie). Pour cela, il ne doit pas subir de dégat. Si la statue est frappée, la joueur en perd le contrôle, et doit retourner la rejoindre à l'endroit où elle se trouve à présent.
* En plus de ses points de vie, le joueur a des jauges de satiété qu'il doit maintenir en consommant des ressources récoltées dans la grotte et sur les monstres.

### Inventaire

* Le joueur possède un inventaire lui permettant de stocker : 
    * Des armes (épée, pioche) qui sont fixes dans l'inventaire
    * Une gourde fixe qui permet au joueur de récolter de l'eau dans la grotte et la boire
    * Des consommables: les champignons récupérés en tuant les monstres permettant au joueur de se nourrir
    * Des blocs récupérés en les cassant dans la grotte et pouvant être placés
* Le joueur ne peut sélectionner qu'un seul objet à la fois (il ne peut donc pas manger et mettre un coup d'épée en même temps par exemple)

### Doublure

* La doublure du joueur est une taupe immobile cachée dans le sol. Elle ne peut rien faire à part ressortir du sol. Lorsqu'on est enterrés, les monstres ne nous voient plus.

### Transfert d'automate

* Cela concerne les statues: pour les déplacer, il faut les activer avec son amulette. Il y a alors un transfert d'automate entre la statue et le joueur. Le joueur ne peut plus bouger et est ignoré aux yeux des ennemis tandis que la statue peut uniquement se déplacer, devenant très exposée aux attaques des monstres.

## Extensions : quelques idées

### :one: Menu principal

* Le menu permet au joueur de lancer une partie et de rentrer l'adresse IP du serveur s'il veut jouer en réseau
* Le joueur ne peut pas lancer de serveur depuis ce menu, il doit lancer un autre exécutable

### :two: Graphismes et bruitages

* Inclure des bruitages enregistrés par nos soins

###  :three: Variété des monstres

* Créer des nouveaux monstres avec des comportements/designs différents

### :four: Ombres (lumière autour du joueur)

* Le joueur tient une torche, il ne peut donc voir qu'à une certaine distance, le reste des blocs est donc sombre

### :five: Ombres (lumière dirigée par la souris)

* La torche est placée sur le curseur de la souris au lieu d'être sur le joueur, l'utilisateur peut donc choisir quelle partie de l'écran éclairer

### :six: Grappin

* Ajouter un objet qui permet de s'accrocher aux murs et de se balancer

### :seven: Taguer les murs
* Écrire des indications sur les murs pour mieux se repérer sur la carte 

### :eight: Biomes
* Inclure des petites variations dans l'environnement (graphiques uniquement)
