package info3.game.entities;

import info3.game.AvatarBuilder;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.AnimatedImage;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.automata.behaviors.MushroomBehaviour;
import info3.game.physics.BoxCollider;
import info3.game.physics.RigidBody;

public class Mushroom extends RigidBody {

	public int childRemain;

	public Mushroom(LocalController c, Vec2 pos, int remain) {
		super(1, c, 50 * (remain + 1));
		int level = remain + 1;
		this.childRemain = remain;
		this.setPosition(pos);
		this.setCategory(Category.ADVERSAIRE);
		this.setAutomata(Model.getAutomata("Mushroom"));
		this.setBehaviour(new MushroomBehaviour());
		this.speedFactor = 5 - (level * 2);
		this.degatMob = level;
		this.collider = new BoxCollider(Block.SIZE, Block.SIZE, 0, 0);
		this.avatar = new AvatarBuilder(new AnimatedImage(this.avatarPath(), 4, 200, true)).scale(new Vec2(level))
				.position(this.getPosition()).build(this.controller);
		this.playAnimation("spawn-right", 4, 100, 0, -10, false);
	}

	@Override
	public void tick(long el) {
		super.tick(el);
		if (this.getPointsDeVie() <= 0) {
			this.getSpeed().setX(0);
			return;
		}
		AnimatedImage anim = (AnimatedImage) this.getPaintable();
		if (anim.isFinished()) {
			if (this.getDirection() == Direction.EST)
				this.playAnimation("idle-right", 4, 200, 0, -64, true);
			else
				this.playAnimation("idle-left", 4, 200, 0, -64, true);
		}
	}

	private String avatarPath() {
		return "mushroom/idle-right.png";
	}

	@Override
	public String animationDir() {
		return "mushroom";
	}

	@Override
	public void playAnimation(String name, int fc, int delay, int offsetX, int offsetY, boolean loop) {
		int scale = this.childRemain;
		super.playAnimation(name, fc, delay, offsetX * scale, offsetY * scale, loop);
	}
}
