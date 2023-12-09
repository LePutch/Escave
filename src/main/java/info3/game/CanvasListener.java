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
package info3.game;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import info3.game.graphics.GameCanvasListener;
import info3.game.network.KeyPress;
import info3.game.network.KeyRelease;
import info3.game.network.MouseClick;
import info3.game.network.WheelScroll;

public class CanvasListener implements GameCanvasListener {
	LocalView view;

	CanvasListener(LocalView lv) {
		this.view = lv;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.view.controller.mouseClick(this.view.getPlayer(), new MouseClick(e.getPoint().x, e.getPoint().y));
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.view.mousePos = new Vec2(e.getX(), e.getY());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 114) { // F3: toggle debug mode
			this.view.debug = !this.view.debug;
		} else if (code == 27) { // Echap: exit full screen
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		} else {
			KeyPress kp = new KeyPress(e.getKeyCode());
			this.view.controller.keyPressed(this.view.getPlayer(), kp);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyRelease kr = new KeyRelease(e.getKeyCode());
		this.view.controller.keyReleased(this.view.getPlayer(), kr);
	}

	@Override
	public void tick(long elapsed) {
		this.view.tick(elapsed);
		this.view.controller.tick(elapsed);
	}

	@Override
	public void paint(Graphics g) {
		this.view.paint(g);
	}

	@Override
	public void windowOpened() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void endOfPlay(String name) {

	}

	@Override
	public void expired() {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		WheelScroll ws = new WheelScroll(e.getWheelRotation() > 0);
		this.view.controller.mouseScroll(this.view.getPlayer(), ws);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.view.controller.windowResize(this.view.getPlayer(),
				new Vec2(this.view.canvas.getWidth(), this.view.canvas.getHeight()));
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
