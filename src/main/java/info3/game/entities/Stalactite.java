package info3.game.entities;

import info3.game.AvatarBuilder;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.Image;
import info3.game.automata.Category;
import info3.game.automata.behaviors.StalactiteBehaviour;
import info3.game.physics.RbState;
import info3.game.physics.RigidBody;

public class Stalactite extends RigidBody {

	public Stalactite(LocalController c, Vec2 pos, int points) {
		super(1, c, points);
		this.setPosition(pos);
		this.setCategory(Category.SOMETHING);
		this.setAutomata(Model.getAutomata("Stalactite"));
		this.setBehaviour(new StalactiteBehaviour());
		this.avatar = new AvatarBuilder(new Image("classic_block/stalactite.png")).position(this.getPosition())
				.build(this.controller);
		this.setState(RbState.STATIC);
	}
}