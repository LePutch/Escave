#!/usr/bin/env bash

declare -A hue
hue[vert]=30,240,30
hue[bleu]=80,110,230
hue[rouge]=210,50,50
hue[violet]=180,50,190
hue[noir]=30,30,30
hue[blanc]=248,248,248
hue[orange]=230,120,20

for color in "${!hue[@]}"; do
    if [ ! -d $color ]; then
        mkdir $color 
    fi

    for img in jaune/*.png; do
        echo Generation de $(basename $img .png) pour $color
        dest=$color/$(basename $img)
        hue-changer $img "${hue[$color]}" $dest
    done
done
