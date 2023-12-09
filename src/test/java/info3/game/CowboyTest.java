package info3.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import info3.game.assets.AssetServer;
import info3.game.entities.Cowboy;

public class CowboyTest {

	@Test
	public void testCowboy() {
		AssetServer.init(false);
		LocalController co = new LocalController();
		View view = new RemoteView();
		view.setDimensions(new Vec2(0));
		co.addView(view);

		Cowboy c = new Cowboy(co, 1);
		assertEquals(0, c.getPosition().x);
		c.tick(10);
		assertEquals(0, c.getPosition().y);
		assertEquals(0, c.getPosition().x);
		c.tick(20);
		assertEquals(2, c.getPosition().x);
	}
}
