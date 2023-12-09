package info3.game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IPSelectionPage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4387000242423850271L;

	public JFrame caller;

	private static Color BTN_COLOR = Color.white;

	IPSelectionPage(JFrame caller) {
		this.caller = caller;
		setup();
		makeInterface();
	}

	private void setup() {
		this.setTitle("Escave");
		this.setSize(300, 300);
		this.setUndecorated(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		// this.setIconImage(new
		// ImageIcon(getClass().getResource("image.jgp)).getImage());
		this.setResizable(false);
	}

	private JPanel genPanel(Color color) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(color);
		panel.setOpaque(true);
		return panel;
	}

	private void makeInterface() {
		JFrame frame = this;
		JPanel bgPanel = new JPanel(new GridBagLayout());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		this.setContentPane(bgPanel);
		bgPanel.setOpaque(true);
		bgPanel.setBackground(Color.lightGray);

		JPanel panel05 = genPanel(Color.white);
		JPanel panel1 = genPanel(Color.white);
		JPanel panel15 = genPanel(Color.white);
		JPanel panel2 = genPanel(Color.white);
		panel05.setOpaque(false);
		panel1.setOpaque(false);
		panel15.setOpaque(false);
		panel2.setOpaque(false);

		GridBagConstraints grid = new GridBagConstraints();
		grid.fill = GridBagConstraints.BOTH;
		grid.weightx = 1;
		grid.weighty = 0.5;
		grid.gridy = 1;
		bgPanel.add(panel05, grid);
		grid.weightx = 1;
		grid.weighty = 0;
		grid.gridy = 2;
		bgPanel.add(panel1, grid);
		grid.weightx = 1;
		grid.weighty = 0;
		grid.gridy = 3;
		bgPanel.add(panel15, grid);
		grid.gridy = 4;
		grid.weighty = 0.3;
		bgPanel.add(panel2, grid);

		grid.fill = GridBagConstraints.NONE;
		JLabel label = new JLabel("Rentrer l'IP du serveur !");
		label.setFont(new Font("Serif", Font.BOLD, 60));
		JTextField IPTextField = new JTextField("");
		IPTextField.setPreferredSize(new Dimension(150, 30));
		grid.weighty = 200;
		grid.weightx = 1;
		grid.gridy = 1;
		panel05.add(label, grid);
		grid.weighty = -100;
		grid.weightx = 1;
		grid.gridy = 2;
		panel05.add(IPTextField, grid);

		JButton joinGameBtn = new JButton("Valider");
		joinGameBtn.setPreferredSize(new Dimension(170, 30));
		joinGameBtn.setBackground(BTN_COLOR);
		joinGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Lancer le jeu en r�cup�rant l'IP
				caller.requestFocus();
				caller.toFront();
				caller.setState(Frame.NORMAL);
				frame.dispose();
				// Sauver l'IP dans le fichier d'Options
				Options.ip = IPTextField.getText();
			}
		});
		panel2.add(joinGameBtn, grid);

		JButton createGameBtn = new JButton("Cr�er une partie");
		createGameBtn.setPreferredSize(new Dimension(200, 80));
		createGameBtn.setBackground(BTN_COLOR);
		createGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				// Sauver l'IP dans le fichier d'Options
			}
		});
		grid.gridy = 2;
		// panel2.add(createGameBtn, grid);

		this.setVisible(true);
	}

}
