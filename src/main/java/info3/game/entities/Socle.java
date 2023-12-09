package info3.game.entities;

import info3.game.AvatarBuilder;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.Image;
import info3.game.automata.Category;
import info3.game.automata.behaviors.SocleBehaviour;

public class Socle extends Block {
	PlayerColor color;

	public boolean isActivated = false;

	public Socle(LocalController c, Vec2 position) {
		super(c, position, 1000, 1);
		this.setCategory(Category.JUMPABLE);
		this.position = position;
		this.setAutomata(Model.getAutomata("Socle"));
		this.setBehaviour(new SocleBehaviour());
		this.avatar = new AvatarBuilder(new Image("classic_block/socle.png")).position(this.getPosition())
				.build(this.controller);
	}
}
