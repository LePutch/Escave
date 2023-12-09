package info3.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import info3.game.assets.AnimatedImage;
import info3.game.automata.Automata;
import info3.game.automata.BotBuilder;
import info3.game.automata.ast.AST;
import info3.game.automata.parser.AutomataParser;
import info3.game.cavegenerator.DecorationGenerator;
import info3.game.cavegenerator.SpawnGenerator4D;
import info3.game.entities.Block;
import info3.game.entities.Entity;
import info3.game.entities.Player;
import info3.game.entities.PlayerColor;
import info3.game.entities.Socle;
import info3.game.entities.Stalactite;
import info3.game.entities.Statue;
import info3.game.menu.GameOptions;
import info3.game.physics.PhysicsWorld;
import info3.game.physics.RigidBody;
import info3.game.torus.IntTorus;
import info3.game.torus.Map;

/**
 * Représente l'ensemble des données du jeu.
 */
public class Model {
	/**
	 * Le controlleur associé à ce modèle.
	 */
	public static LocalController controller;

	/**
	 * La liste de toutes les entités dynamiques dans le monde.
	 */
	static List<RigidBody> entities;

	/**
	 * La liste des entités dynamiques à spawner au prochain tick
	 * 
	 * On ajoute pas directement dans entities pour éviter des accès concurrents par
	 * plusieurs threads #réseau #parallélisme
	 */
	static ArrayList<RigidBody> spawnQueue = new ArrayList<RigidBody>();

	/**
	 * La liste des blocs de la carte.
	 * 
	 * Les élements de ce tableau sont aussi dans le tableau `entities`. Cette
	 * duplication permet d'accéder précisément à un bloc à une position donnée. En
	 * réalité, il n'y a pas de duplication, juste de l'aliasing.
	 * 
	 * On peut voir la carte comme une matrice, dont on peut accéder à un élément
	 * précis avec la méthode getBlock(x, y) de cette classe.
	 */
	private static Map map;

	static ArrayList<Vec2> spawnPoints;
	static ArrayList<Avatar> spawnPointsBackground;

	public static Vec2 exitPoint;
	public static Avatar exitAvatar;

	public static GameOptions options;

	static AtomicInteger playerCount = new AtomicInteger(0);
	private static int activatedSocles = 0;

	static ArrayList<Automata> automatas;

	private static boolean started() {
		// TODO: memoization
		return Model.options != null && Model.playerCount.get() == Model.options.playerCount
				&& Model.getPlayers().size() == Model.options.playerCount;
	}

	private static PhysicsWorld physics;

	private static List<Vec2> statuesSpawns;
	private static List<Vec2> stalactiteSpawns;

	public static boolean exitOpened = false;

	public static void init(LocalController controller) {
		System.out.println("init model");
		Model.controller = controller;
		Model.entities = Collections.synchronizedList(new ArrayList<RigidBody>());
		Model.physics = new PhysicsWorld();
	}

	public static void configure(GameOptions options) {
		if (Model.options == null) {
			Model.options = options;
			Model.loadAutomatas();
		}
	}

	public static void deleteEntity(RigidBody e) {
		// TODO: sync?
		entities.remove(e);
		Model.controller.deleteAvatar(e.getAvatar().getId());
	}

	/**
	 * Ajoute une entité dans le monde.
	 * 
	 * @param e L'entité à faire apparaître
	 */
	public static void spawn(RigidBody e) {
		Model.spawnQueue.add(e);
	}

	public static Player spawnPlayer(int playerNum) {
		// TODO: throw exception if there are more players than expected
		Player p = new Player(Model.controller, Player.colorFromInt(playerNum),
				Model.spawnPoints.get(playerNum).multiply(Block.SIZE), 10);
		Model.spawn(p);
		return p;
	}

	public static void deleteBlock(int x, int y) {
		if (Model.map.get(x, y) == null) {
			return;
		}
		int avatarId = Model.map.get(x, y).getAvatar().getId();
		Model.map.set(x, y, null);
		Model.controller.deleteAvatar(avatarId);
	}

	static void generateMapIfNeeded() {
		if (Model.map == null) {
			// génération de la map
			SpawnGenerator4D generationMap = new SpawnGenerator4D();
			int[][] values = generationMap.spawnStatueTotal(Model.options.playerCount);
			Model.spawnPoints = generationMap.listSpawnPlayer;
			Model.exitPoint = generationMap.exit;
			List<Vec2> blocs = generationMap.listSpawnBlocsStatues;
			Model.statuesSpawns = generationMap.listSpawnStatues;
			Model.stalactiteSpawns = DecorationGenerator.listSpawnStalactites;
			IntTorus torus = DecorationGenerator.decorate(values);
			int[][] blocks = torus.toArray();

			Model.map = new Map(blocks.length, blocks[0].length);
			for (int i = 0; i < blocks.length; i++) {
				for (int j = 0; j < blocks[i].length; j++) {
					if (blocks[i][j] != 0) {
						Model.map.set(i, j, new Block(Model.controller, new Vec2(i * 64, j * 64), blocks[i][j], 1));
					}
				}
			}
			AnimatedImage exit = new AnimatedImage("exit/exit.png", 6, 200, true);
			exitPoint.setX(exitPoint.getX() - 4);
			exitPoint.setY(exitPoint.getY() - 4);
			exitPoint = exitPoint.multiply(Block.SIZE);
			exitAvatar = new AvatarBuilder(exit).position(exitPoint).scale(new Vec2(1)).layer(-1)
					.build(Controller.controller);
			spawnPointsBackground = generateSpawnBackground(spawnPoints);
			for (Vec2 socle : blocs) {
				Model.map.set((int) socle.getX(), (int) socle.getY(),
						new Socle(Model.controller, socle.multiply(Block.SIZE)));
			}

		}
	}

	public static ArrayList<Avatar> generateSpawnBackground(ArrayList<Vec2> spawnBackground) {
		ArrayList<Avatar> res = new ArrayList<>();
		AnimatedImage sprite = new AnimatedImage("spawn-area.png", 7, 100, true);
		for (Vec2 posBackground : spawnBackground) {
			posBackground = posBackground.add(new Vec2(-4, -3)).multiply(Block.SIZE);
			posBackground = posBackground.add(new Vec2(0, 1));
			Avatar bg_avatar = new AvatarBuilder(sprite).position(posBackground).scale(new Vec2(1)).layer(-1)
					.build(Controller.controller);
			res.add(bg_avatar);
		}
		return res;
	}

	public static void tick(long elapsed) {
		activatedSocles = 0;
		Model.entities.addAll(Model.spawnQueue);
		Model.spawnQueue.clear();
		if (!Model.started()) {
			return;
		}
		if (Model.statuesSpawns != null) {
			Iterator<Player> players = Model.getPlayers().iterator();
			for (Vec2 s : Model.statuesSpawns) {
				float x = s.getX();
				float y = s.getY();
				Model.spawn(new Statue(Model.controller, players.next(), new Vec2(x * Block.SIZE, y * Block.SIZE), 1));
			}
			Model.statuesSpawns = null;

			for (Vec2 posStalactite : Model.stalactiteSpawns) {
				Model.spawn(new Stalactite((LocalController) Model.controller, posStalactite.multiply(Block.SIZE), 10));
			}
			MobSpawner.init(30, 0.00003f);
		}

		if (elapsed > 200) {
			System.out.println("[WARN] Tick ignored in model");
			return;
		}
		MobSpawner.tick();
		Model.physics.tick(elapsed);
		for (Entity e : Model.allEntities()) {
			e.tick(elapsed);
		}
		if (activatedSocles == playerCount.get() && !exitOpened) {
			AnimatedImage newAnim = new AnimatedImage("exit/exit-destroy.png", 5, 200, false);
			exitAvatar.setPaintable(newAnim);
			Model.controller.updateAvatar(exitAvatar);
			System.out.println("Sortie activée");
			exitOpened = true;
			for (View v : Model.controller.views) {
				v.playSound(10);
			}
		}
	}

	/**
	 * Renvoie le bloc aux coordonnées (x, y) sur la carte, ou `null` si cet
	 * emplacement est vide.
	 * 
	 * Les indices peuvent être plus grands que la taille de la carte, le fait que
	 * le monde est circulaire est pris en compte.
	 * 
	 * @param x Coordonée x
	 * @param y Coordonée y
	 * @return Le bloc à cette position
	 */
	public static Block getBlock(int x, int y) {
		return Model.map.get(x, y);
	}

	/**
	 * 
	 * @param baseX  abscisse du coin hg de la zone de vision
	 * @param baseY  ordonnée (croissant vers le bas) du coin hg de la zone de
	 *               vision
	 * @param width  en px
	 * @param height en px
	 * @return
	 */
	public static ArrayList<Entity> getNearEntities(int baseX, int baseY, int width, int height) {
		ArrayList<Entity> nearEntities = new ArrayList<>();
		// On parcours d'abord la map pour avoir les blocs
		for (int i = 0; i < width / Block.SIZE; i++) {
			for (int j = 0; j < height / Block.SIZE; j++) {
				Block block = Model.getBlock(baseX / Block.SIZE + i, baseY / Block.SIZE + j);
				if (block != null) {
					nearEntities.add(block);
				}
			}
		}
		// Puis on parcours les entités "dynamiques"
		ArrayList<RigidBody> entities = new ArrayList<>(Model.entities);
		for (Entity e : entities) {
			Vec2 pos = e.getPosition();
			if (pos.getX() >= baseX && pos.getX() <= baseX + width && pos.getY() >= baseY
					&& pos.getY() <= baseY + height) {
				nearEntities.add(e);
			}
		}
		return nearEntities;
	}

	public static ArrayList<Entity> allEntities() {
		ArrayList<Entity> all;
		synchronized (Model.entities) {
			all = new ArrayList<Entity>(Model.entities);
		}
		if (Model.map != null) {
			synchronized (Model.map) {
				for (int i = 0; i < Model.map.width; i++) {
					for (int j = 0; j < Model.map.height; j++) {
						Block block = Model.map.get(i, j);
						if (block != null) {
							all.add(block);
						}
					}
				}
			}
		}

		return all;
	}

	public static ArrayList<Entity> getNearBlocks(int x, int y, int width, int height) {
		ArrayList<Entity> nearBlocks = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				nearBlocks.add(Model.getBlock(x + i, y + j));
			}
		}
		return nearBlocks;
	}

	public static List<RigidBody> getEntities() {
		return Model.entities;
	}

	public static Map getMap() {
		return Model.map;
	}

	public static void incrementActivatedSocles() {
		activatedSocles++;
	}

	private static void loadAutomatas() {
		AST ast = null;
		try {
			Reader gal;
			if (Model.options.fichierGal != null) {
				gal = new StringReader(Model.options.fichierGal);
			} else {
				gal = new BufferedReader(new FileReader("src/main/resources/automatas.gal"));
			}
			ast = new AutomataParser(gal).Run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BotBuilder botBuilder = new BotBuilder();
		ast.accept(botBuilder);
		Model.automatas = botBuilder.getAutomatas();
		System.out.println("[DEBUG] Loaded " + Model.automatas.size() + " automatas");
	}

	public static Automata getAutomata(String name) {
		for (Automata automata : Model.automatas) {
			if (automata.getName().equals(name))
				return automata;
		}
		System.out.println("[WARNING] Automata " + name + " not found");
		return null;
	}

	public static Block[][] getMapZone(int x, int y, int width, int height) {
		Block[][] resMap = new Block[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				resMap[i][j] = map.get(x + i, y + j);
			}
		}
		return resMap;
	}

	public static ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (RigidBody rb : Model.entities) {
			if (rb instanceof Player) {
				players.add((Player) rb);
			}
		}
		return players;
	}

	public static Player getPlayer(PlayerColor color) {
		for (RigidBody rb : Model.entities) {
			if (rb instanceof Player) {
				if (((Player) rb).getColor() == color) {
					return (Player) rb;
				}
			}
		}
		return null;
	}
}
