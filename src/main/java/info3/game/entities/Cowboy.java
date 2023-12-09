/*
 * Copyright (C) 2020  Pr. Olivier Gruber
 * Educational software for a basic game development
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Created on: March, 2020
 *      Author: Pr. Olivier Gruber
 */
package info3.game.entities;

import info3.game.AvatarBuilder;
import info3.game.LocalController;
import info3.game.Vec2;
import info3.game.assets.AnimatedImage;
import info3.game.physics.RigidBody;

/**
 * A simple class that holds the images of a sprite for an animated cowbow.
 *
 */
public class Cowboy extends RigidBody {
	int moveElapsed;
	int maxX = 500;
	int moveSpeed = 2;

	public Cowboy(LocalController controller, int points) {
		super(5, controller, points);
		this.setPosition(new Vec2(0.0f, 0.0f));
		this.avatar = new AvatarBuilder(new AnimatedImage("cowboy.png", 24, 200, true)).position(this.getPosition())
				.build(this.controller);
	}

	@Override
	public void tick(long elapsed) {
		super.tick(elapsed);
		this.moveElapsed += elapsed;
		if (this.moveElapsed > 24) {
			this.moveElapsed = 0;
			this.setPosition(new Vec2((this.getPosition().getX() + this.moveSpeed) % maxX, this.getPosition().getY()));
		}
	}
}
