package info3.game.entities;

import info3.game.AvatarBuilder;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.AnimatedImage;
import info3.game.automata.Category;
import info3.game.automata.behaviors.StatueBehaviour;
import info3.game.physics.BoxCollider;
import info3.game.physics.RigidBody;

public class Statue extends RigidBody {
	PlayerColor color;
	Player player;

	public Statue(LocalController c, Player p, Vec2 pos, int points) {
		super(1, c, points);
		this.setPosition(pos);
		this.player = p;
		this.color = this.player.getColor();

		this.avatar = new AvatarBuilder(new AnimatedImage("statue/blanc/statue-idle.png", 4, 100, false))
				.offset(new Vec2(0, -52)).position(this.getPosition()).build(this.controller);
		this.setAutomata(Model.getAutomata("Statue"));
		this.setBehaviour(new StatueBehaviour());
		this.setCategory(Category.TEAM);
		this.collider = new BoxCollider(Block.SIZE - 3, Block.SIZE - 3, 1.5f, 1.5f);
	}

	public Player getPlayer() {
		return this.player;
	}

	@Override
	public void tick(long el) {
		super.tick(el);
		AnimatedImage anim = (AnimatedImage) this.getPaintable();
		if (anim.isFinished()) {
			if (this.getCategory() == Category.TEAM)
				this.playAnimation("statue-idle", 4, 1000, -16, -64, true);
			else if (this.getCategory() == Category.PLAYER)
				this.playAnimation("statue-levitation", 16, 200, -16, -104, true);
		}
	}

	@Override
	public String animationDir() {
		return "statue/" + this.name().toLowerCase();
	}

	public String name() {
		switch (this.color) {
		case BLUE:
			return "Bleu";
		case RED:
			return "Rouge";
		case GREEN:
			return "Vert";
		case YELLOW:
			return "Jaune";
		case ORANGE:
			return "Orange";
		case PURPLE:
			return "Violet";
		case WHITE:
			return "Blanc";
		case BLACK:
			return "Noir";
		}
		return "_";
	}
}
