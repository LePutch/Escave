package info3.game.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342949181734714588L;

	public GameWindow(GamePanel gamePanel) {

		super();

		setLayout(new BorderLayout());

		JLabel background = new JLabel();

		this.add(background);
		background.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel.setOpaque(false);

		this.add(gamePanel);

		this.setUndecorated(true);
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f));

		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

}
