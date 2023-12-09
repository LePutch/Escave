package info3.game.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import info3.game.automata.Automata;
import info3.game.automata.BotBuilder;
import info3.game.automata.ast.AST;
import info3.game.automata.parser.AutomataParser;
import info3.game.automata.parser.ParseException;

public class AutomataSelectionPage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7749010576833684899L;
	public final String TITLE = "Association des automates";
	public final Color BTN_COLOR = Color.white;
	public JFrame caller;

	public static String galURL = "src/main/resources/automatas.gal";

	public AutomataSelectionPage(JFrame caller) {
		this.caller = caller;
		setup();
		makeInterface();
	}

	private void loadModelAutomatas(JComboBox<String> playerCombo, JComboBox<String> statueCombo,
			JComboBox<String> blockCombo, JComboBox<String> mushroomCombo, JComboBox<String> socleCombo,
			JComboBox<String> waterCombo, JComboBox<String> foodCombo) {
		GameOptions.instance.automates.put("Player", (String) playerCombo.getSelectedItem());
		GameOptions.instance.automates.put("Statue", (String) statueCombo.getSelectedItem());
		GameOptions.instance.automates.put("Block", (String) blockCombo.getSelectedItem());
		GameOptions.instance.automates.put("Mushroom", (String) mushroomCombo.getSelectedItem());
		GameOptions.instance.automates.put("Socle", (String) socleCombo.getSelectedItem());
		GameOptions.instance.automates.put("Water", (String) waterCombo.getSelectedItem());
		GameOptions.instance.automates.put("Food", (String) foodCombo.getSelectedItem());
	}

	private void setup() {
		this.setTitle("Escave");
		this.setSize(500, 400);
		this.setUndecorated(true);

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(800, 850));
		// this.setIconImage(new
		// ImageIcon(getClass().getResource("image.jgp)).getImage());
	}

	private JPanel genPanel(Color color) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(color);
		panel.setOpaque(true);
		return panel;
	}

	private void makeInterface() {
		JFrame frame = this;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		JPanel bgPanel = new JPanel(new GridBagLayout());
		bgPanel.setBackground(Color.lightGray);
		bgPanel.setOpaque(false);
		this.setContentPane(bgPanel);

		GridBagConstraints grid = new GridBagConstraints();
		grid.fill = GridBagConstraints.BOTH;
		JPanel panel1 = genPanel(Color.white);
		panel1.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		JPanel panel2 = genPanel(Color.white);
		JPanel panel4 = genPanel(Color.white);

		grid.weightx = 1;
		grid.weighty = 0;
		grid.gridwidth = 2;
		grid.gridy = 1;
		grid.gridx = 1;
		bgPanel.add(panel1, grid);
		grid.weighty = 1;
		grid.gridwidth = 1;
		grid.gridx = 1;
		grid.gridy = 2;
		bgPanel.add(panel2, grid);
		grid.gridx = 1;
		grid.gridy = 3;
		grid.gridwidth = 2;
		bgPanel.add(panel4, grid);

		GridBagConstraints gridPanel1 = new GridBagConstraints();
		gridPanel1.fill = GridBagConstraints.BOTH;
		gridPanel1.insets = new Insets(20, 0, 20, 0);
		JButton selectFileBtn = new JButton("Selectionnez un fichier .gal");
		selectFileBtn.setBackground(BTN_COLOR);
		selectFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					AutomataSelectionPage.galURL = chooser.getSelectedFile().toString();
				}
			}
		});

		panel1.add(selectFileBtn, gridPanel1);
		ArrayList<String> automatasNames = new ArrayList<String>();
		try {
			automatasNames = getAutomatasName(galURL);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		GridBagConstraints gridPanel2 = new GridBagConstraints();
		Font text = new Font("Text", Font.BOLD, 30);
		gridPanel2.fill = GridBagConstraints.BOTH;
		JLabel playerLbl = new JLabel("Player");
		playerLbl.setFont(text);
		JLabel statueLbl = new JLabel("Statue");
		statueLbl.setFont(text);
		JLabel blockLbl = new JLabel("Block");
		blockLbl.setFont(text);
		JLabel mushroomLbl = new JLabel("Mushroom");
		mushroomLbl.setFont(text);
		JLabel socleLbl = new JLabel("Socle");
		socleLbl.setFont(text);
		JLabel waterLbl = new JLabel("Water");
		waterLbl.setFont(text);
		JLabel foodLbl = new JLabel("Food");
		foodLbl.setFont(text);

		gridPanel2.insets = new Insets(20, 100, 20, 100);
		gridPanel2.gridy = 1;
		panel2.add(playerLbl, gridPanel2);
		gridPanel2.gridy = 2;
		panel2.add(statueLbl, gridPanel2);
		gridPanel2.gridy = 3;
		panel2.add(blockLbl, gridPanel2);
		gridPanel2.gridy = 4;
		panel2.add(mushroomLbl, gridPanel2);
		gridPanel2.gridy = 5;
		panel2.add(socleLbl, gridPanel2);
		gridPanel2.gridy = 6;
		panel2.add(waterLbl, gridPanel2);
		gridPanel2.gridy = 7;
		panel2.add(foodLbl, gridPanel2);

		String[] arr = automatasNames.toArray(new String[0]);
		JComboBox<String> playerCombo = new JComboBox<String>(arr);
		playerCombo.setFont(text);
		JComboBox<String> statueCombo = new JComboBox<String>(arr);
		statueCombo.setFont(text);
		JComboBox<String> blockCombo = new JComboBox<String>(arr);
		blockCombo.setFont(text);
		JComboBox<String> mushroomCombo = new JComboBox<String>(arr);
		mushroomCombo.setFont(text);
		JComboBox<String> socleCombo = new JComboBox<String>(arr);
		socleCombo.setFont(text);
		JComboBox<String> waterCombo = new JComboBox<String>(arr);
		waterCombo.setFont(text);
		JComboBox<String> foodCombo = new JComboBox<String>(arr);
		foodCombo.setFont(text);
		gridPanel2.gridx = 2;
		gridPanel2.gridy = 1;
		panel2.add(playerCombo, gridPanel2);
		gridPanel2.gridy = 2;
		panel2.add(statueCombo, gridPanel2);
		gridPanel2.gridy = 3;
		panel2.add(blockCombo, gridPanel2);
		gridPanel2.gridy = 4;
		panel2.add(mushroomCombo, gridPanel2);
		gridPanel2.gridy = 5;
		panel2.add(socleCombo, gridPanel2);
		gridPanel2.gridy = 6;
		panel2.add(waterCombo, gridPanel2);
		gridPanel2.gridy = 7;
		panel2.add(foodCombo, gridPanel2);

		GridBagConstraints gridPanel4 = new GridBagConstraints();
		JButton validateBtn = new JButton("Valider");
		validateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Charger les automates dans le Model
				loadModelAutomatas(playerCombo, statueCombo, blockCombo, mushroomCombo, socleCombo, waterCombo,
						foodCombo);
				caller.requestFocus();
				caller.toFront();
				caller.setState(Frame.NORMAL);
				frame.dispose();
			}
		});
		validateBtn.setBackground(BTN_COLOR);
		panel4.add(validateBtn, gridPanel4);

		this.setVisible(true);
	}

	private String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	public ArrayList<String> getAutomatasName(String url) throws FileNotFoundException, ParseException {

		AST ast = new AutomataParser(new BufferedReader(new FileReader(url))).Run();
		try {
			GameOptions.instance.fichierGal = readFile(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BotBuilder bot_builder = new BotBuilder();
		ast.accept(bot_builder);
		ArrayList<String> res = new ArrayList<String>();
		for (Automata aut : bot_builder.getAutomatas()) {
			res.add(aut.getName());
		}
		return res;
	}

}
