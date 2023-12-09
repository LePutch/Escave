# Description du projet Escave

## Scénario

Escave est un jeu d’évasion dans lequel les joueurs, incarnés par des taupes, sont plongés dans un monde en deux dimensions. Celui-ci est de type “bac à sable”, c’est-à-dire que les joueurs peuvent interagir avec le monde, en cassant ou en plaçant des blocs.
L’objectif est de s’échapper. Pour cela, il faut rejoindre la sortie après l’avoir débloquée en réalisant plusieurs étapes. Chaque joueur a en sa possession une amulette d'une certaine couleur. Sur la carte, chaque amulette a une statue qui lui est associée. Devant la sortie, des blocs spéciaux seront placés. Le but étant d'emmener les statues sur ces blocs. Pour déplacer les statues, il faut, par exemple, que le joueur possédant l'amulette jaune, trouve la statue jaune, et l'emmène sur un bloc spécial à coté de la sortie. Une fois que chaque joueur a emmené sa statue, la sortie est "débloquée". Les joueurs peuvent donc s'echapper.
Malheureusement, des monstres ne simplifieront pas la tâche...

## Génération du monde

La carte est générée aléatoirement à chaque début de partie. Elle est constituée de salles plus ou moins grandes, de tunnels, de lieux d'apparition pour nos joueurs/nos statues et de la sortie. Le monde est un cylindre : les bords latéraux se recouvrent sur eux-même.

## Utilisation des automates

Chaque entité a son propre automate et chacune d'elle a son action pop() et wizz(). La **doublure** du joueur est une taupe immobile cachée dans le sol. Elle devient alors invisible aux yeux des ennemis.
Concernant les statues, pour les déplacer, il faut les activer avec son amulette. Il y a alors un **transfert d'automate** entre la statue et le joueur. Le joueur ne peut plus bouger et est ignoré aux yeux des ennemis tandis que la statue peut uniquement se déplacer, devenant très exposée aux attaques des monstres. 

## Multiplication des monstres

Certains monstres ont la capacité de se dupliquer un certain nombre de fois avant de mourir. Par exemple, si on tue un monstre A une première fois, deux nouveaux monstres B et C du même type apparaissent. Ils pourront à leur tour être dupliqués avant de disparaitre définitivement. Il s'agit là de notre application de la fonction egg().

## Multijoueur

Le multijoueur est une partie essentielle du jeu. Afin de terminer une partie, les joueurs doivent atteindre la sortie. Celle-ci est verrouillée au début. Pour la débloquer, tous les joueurs doivent contrôler les statues jusqu’aux emplacements réservés à l’activation de la sortie. Toutefois, lorsqu'un joueur prend le contrôle d'une statue, il ne peut plus ni attaquer, ni se défendre. Il devient donc une proie facile pour les monstres de la grotte. La coopération est de mise et l’entraide est vivement conseillée pour se sortir des situations les plus fâcheuses.


