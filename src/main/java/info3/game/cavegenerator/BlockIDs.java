package info3.game.cavegenerator;

import java.util.HashMap;

import info3.game.Vec2;

/*
 *
 * 
 * 
 * [1] [2] 	[2]  [2]  [3]
 * [8] [9] 	[10] [11] [4]
 * [8] [16]	[1] [12] [4]
 * [8] [15]	[14] [13] [4]
 * [7] [6] 	[6]  [6]  [5]
 * 
 * 
 * 
 * 
 * 
 * 
 * 0 : "vide"
 * 	*** Blocs normaux
 * 1 : "noir"
 * 2 : "sol"
 * 3 : "coin_droit_haut"
 * 4 : "mur_droit"
 * 5 : "coin_droit_bas"
 * 6 : "plafond"
 * 7 : "coin_gauche_bas"
 * 8 : "mur_gauche"
 * 9 : "trans_coin_gauche_haut"
 * 10 : "trans_sol"
 * 11 : "trans_coin_droit_haut"
 * 12 : "trans_mur_droit"
 * 13 : "trans_coin_droit_bas"
 * 14 : "trans_plafond"
 * 15 : "trans_coin_gauche_bas"
 * 16 : "trans_mur_gauche"
 * 17 : "volant_seul"
 * 18 : "volant_horizontal_gauche"
 * 19 : "volant_horizontal_droit"
 * 20 : "volant_vertical_haut"
 * 21 : "volant_vertical_bas"
 * 22 : "coin_gauche_haut"
 * 23 : "seul_gris_clair"
 * 24 : "volant_horizontal_milieu"
 * 25 : "volant_vertical_milieu"
 * 26 : "trans_herbe_coin_haut_gauche"
 * 27 : "trans_herbe_coin_haut_droit"
 * 28 : "trans_herbe_coin_bas_gauche"
 * 29 : "trans_herbe_coin_bas_droit"
 * 30 : "trans_3_haut_gauche"
 * 31 : "trans_3_haut_droit"
 * 32 : "trans_3_bas_gauche"
 * 33 : "trans_3_bas_droit"
 * 34 : "gris_herbe_haut_droit"
 * 35 : "gris_herbe_haut_gauche"
 * 36 : "gris_herbe_bas_droit"
 * 37 : "gris_herbe_bas_gauche"
 * 38 : "noir_herbe_haut_droit"
 * 39 : "noir_herbe_haut_gauche"
 * 40 : "noir_herbe_bas_droit"
 * 41 : "noir_herbe_bas_gauche"
 * 42 : "noir_milieu_horizontal"
 * 43 : "noir_milieu_vertical"
 * 
 *  *** Blocs spéciaux
 * 100 : "noir_blanc"
 * 101 : "noir_noir"
 * 102 : "noir_bleu"
 * 103 : "noir_rose"
 * 104 : "noir_violet"
 * 105 : "noir_vert"
 * 150 : "t_rex_1"
 * 151 : "t_rex_2"
 * 152 : "t_rex_3"
 * 153 : "t_rex_4"
 * 154 : "t_rex_5"
 * 155 : "t_rex_6"
 * 156 : "t_rex_7"
 * 160 : "steg_1"
 * 161 : "steg_2"
 * 162 : "steg_3"
 * 163 : "steg_4"
 * 164 : "steg_5"
 * 165 : "steg_6"
 * 166 : "steg_7"
 * 167 : "steg_8"
 * 170 : "tric_1"
 * 171 : "tric_2"
 * 172 : "tric_3"
 * 173 : "tric_4"
 * 174 : "tric_5"
 * 175 : "tric_6"
 * 
 * 
 * 
 * 
 *  *** Blocs spéciaux sol
 *  	*** Cristaux
 * 200 : "cristal_sol_blanc"
 * 201 : "cristal_sol_vert"
 * 202 : "cristal_sol_noir"
 * 203 : "cristal_sol_bleu"
 * 204 : "cristal_sol_rose"
 * 205 : "cristal_sol_violet"
 * 
 *  	*** Champignons
 * 250 : "champi_sol_rose"
 * 251 : "champi_sol_double_bleu"
 * 252 : "champi_sol_vert"
 * 253 : "champi_sol_escargot_bleu"
 * 254 : "champi_sol_escargot_violet"
 * 255 : "champi_sol_bleu_rose"
 * 256 : "champi_sol_double_orange"
 * 257 : "champi_sol_rouge"

 * 
 *  *** Blocs spéciaux plafond
 * 300 : "cristal_plafond_blanc"
 * 301 : "cristal_plafond_bleu"
 * 302 : "cristal_plafond_vert"
 * 303 : "cristal_plafond_violet"
 * 304 : "cristal_plafond_rose"
 * 305 : "cristal_plafond_noir"
 * 
 * 
 * 350: "plafond_liane"
 * 351 : "plafond_lanterne_courte"
 * 352 : "plafond_lanterne_longue"

 *  *** Blocs spéciaux mur_gauche
 * 400 : "cristal_mur_gauche_blanc"
 * 401 : "cristal_mur_gauche_bleu"
 * 402 : "cristal_mur_gauche_vert"
 * 403 : "cristal_mur_gauche_violet"
 * 404 : "cristal_mur_gauche_rose"
 * 405 : "cristal_mur_gauche_noir"
 * 
 * 450 : "arbre_montant_mur_gauche"
 * 450 : "arbre_tombant_mur_gauche"
 * 
 *  *** Blocs spéciaux mur_droit
 * 400 : "cristal_mur_droit_blanc"
 * 501 : "cristal_mur_droit_bleu"
 * 502 : "cristal_mur_droit_vert"
 * 503 : "cristal_mur_droit_violet"
 * 504 : "cristal_mur_droit_rose"
 * 505 : "cristal_mur_droit_noir"
 * 
 * 550 : "arbre_montant_mur_droit"
 * 551 : "arbre_tombant_mur_droit"
 * 
 * 600 : "water_sol"
 * 601 : "socle"
 * 602 : "stalactite"
 */

public class BlockIDs {
	public static HashMap<Integer, String> IDs = new HashMap<Integer, String>();
	public static HashMap<Integer, Vec2> IDsToVec2 = new HashMap<Integer, Vec2>();
	public static HashMap<Integer[][], Integer> PatternCouche1ToIDs = new HashMap<Integer[][], Integer>();
	public static HashMap<Integer[][], Integer> PatternCouche2ToIDs = new HashMap<Integer[][], Integer>();
	public static HashMap<Integer[][], Integer> PatternCouche3ToIDs = new HashMap<Integer[][], Integer>();

	static {
		IDs.put(0, "vide");
		IDs.put(1, "noir");
		IDs.put(2, "sol");
		IDs.put(3, "coin_droit_haut");
		IDs.put(4, "mur_droit");
		IDs.put(5, "coin_droit_bas");
		IDs.put(6, "plafond");
		IDs.put(7, "coin_gauche_bas");
		IDs.put(8, "mur_gauche");
		IDs.put(9, "trans_coin_gauche_haut");
		IDs.put(10, "trans_sol");
		IDs.put(11, "trans_coin_droit_haut");
		IDs.put(12, "trans_mur_droit");
		IDs.put(13, "trans_coin_droit_bas");
		IDs.put(14, "trans_plafond");
		IDs.put(15, "trans_coin_gauche_bas");
		IDs.put(16, "trans_mur_gauche");
		IDs.put(17, "volant_seul");
		IDs.put(18, "volant_horizontal_gauche");
		IDs.put(19, "volant_horizontal_droit");
		IDs.put(20, "volant_vertical_haut");
		IDs.put(21, "volant_vertical_bas");
		IDs.put(22, "coin_gauche_haut");
		IDs.put(23, "seul_gris_clair");
		IDs.put(24, "volant_horizontal_milieu");
		IDs.put(25, "volant_vertical_milieu");
		IDs.put(26, "trans_herbe_coin_haut_gauche");
		IDs.put(27, "trans_herbe_coin_haut_droit");
		IDs.put(28, "trans_herbe_coin_bas_gauche");
		IDs.put(29, "trans_herbe_coin_bas_droit");
		IDs.put(30, "trans_3_haut_gauche");
		IDs.put(31, "trans_3_haut_droit");
		IDs.put(32, "trans_3_bas_gauche");
		IDs.put(33, "trans_3_bas_droit");
		IDs.put(34, "gris_herbe_haut_droit");
		IDs.put(35, "gris_herbe_haut_gauche");
		IDs.put(36, "gris_herbe_bas_droit");
		IDs.put(37, "gris_herbe_bas_gauche");
		IDs.put(38, "noir_herbe_haut_droit");
		IDs.put(39, "noir_herbe_haut_gauche");
		IDs.put(40, "noir_herbe_bas_droit");
		IDs.put(41, "noir_herbe_bas_gauche");
		IDs.put(42, "noir_milieu_horizontal");
		IDs.put(43, "noir_milieu_vertical");
		IDs.put(100, "noir_blanc");
		IDs.put(101, "noir_noir");
		IDs.put(102, "noir_bleu");
		IDs.put(103, "noir_rose");
		IDs.put(104, "noir_violet");
		IDs.put(105, "noir_vert");
		IDs.put(150, "t_rex_1");
		IDs.put(151, "t_rex_2");
		IDs.put(152, "t_rex_3");
		IDs.put(153, "t_rex_4");
		IDs.put(154, "t_rex_5");
		IDs.put(155, "t_rex_6");
		IDs.put(156, "t_rex_7");
		IDs.put(160, "steg_1");
		IDs.put(161, "steg_2");
		IDs.put(162, "steg_3");
		IDs.put(163, "steg_4");
		IDs.put(164, "steg_5");
		IDs.put(165, "steg_6");
		IDs.put(166, "steg_7");
		IDs.put(167, "steg_8");
		IDs.put(170, "tric_1");
		IDs.put(171, "tric_2");
		IDs.put(172, "tric_3");
		IDs.put(173, "tric_4");
		IDs.put(174, "tric_5");
		IDs.put(175, "tric_6");
		IDs.put(200, "cristal_sol_blanc");
		IDs.put(201, "cristal_sol_vert");
		IDs.put(202, "cristal_sol_noir");
		IDs.put(203, "cristal_sol_bleu");
		IDs.put(204, "cristal_sol_rose");
		IDs.put(205, "cristal_sol_violet");
		IDs.put(250, "champi_sol_rose");
		IDs.put(251, "champi_sol_double_bleu");
		IDs.put(252, "champi_sol_vert");
		IDs.put(253, "champi_sol_escargot_bleu");
		IDs.put(254, "champi_sol_escargot_violet");
		IDs.put(255, "champi_sol_bleu_rose");
		IDs.put(256, "champi_sol_double_orange");
		IDs.put(257, "champi_sol_rouge");
		IDs.put(300, "cristal_plafond_blanc");
		IDs.put(301, "cristal_plafond_bleu");
		IDs.put(302, "cristal_plafond_vert");
		IDs.put(303, "cristal_plafond_violet");
		IDs.put(304, "cristal_plafond_rose");
		IDs.put(305, "cristal_plafond_noir");
		IDs.put(350, "plafond_liane");
		IDs.put(351, "plafond_lanterne_courte");
		IDs.put(352, "plafond_lanterne_longue");
		IDs.put(400, "cristal_mur_gauche_blanc");
		IDs.put(401, "cristal_mur_gauche_bleu");
		IDs.put(402, "cristal_mur_gauche_vert");
		IDs.put(403, "cristal_mur_gauche_violet");
		IDs.put(404, "cristal_mur_gauche_rose");
		IDs.put(405, "cristal_mur_gauche_noir");
		IDs.put(450, "arbre_montant_mur_gauche");
		IDs.put(451, "arbre_tombant_mur_gauche");
		IDs.put(500, "cristal_mur_droit_blanc");
		IDs.put(501, "cristal_mur_droit_bleu");
		IDs.put(502, "cristal_mur_droit_vert");
		IDs.put(503, "cristal_mur_droit_violet");
		IDs.put(504, "cristal_mur_droit_rose");
		IDs.put(505, "cristal_mur_droit_noir");
		IDs.put(550, "arbre_montant_mur_droit");
		IDs.put(551, "arbre_tombant_mur_droit");
		IDs.put(600, "water_sol");
		IDs.put(601, "socle");
		IDs.put(602, "stalactite");
		IDs.put(700, "player_jaune");
		IDs.put(701, "player_rouge");
		IDs.put(702, "player_vert");
		IDs.put(703, "player_bleu");
		IDs.put(704, "player_orange");
		IDs.put(705, "player_violet");
		IDs.put(706, "player_blanc");
		IDs.put(707, "player_noir");
	}

	static {
		// Champignons sols
		IDsToVec2.put(250, new Vec2(0, 1));
		IDsToVec2.put(251, new Vec2(0, 1));
		IDsToVec2.put(252, new Vec2(0, 1));
		IDsToVec2.put(253, new Vec2(0, 1));
		IDsToVec2.put(254, new Vec2(0, 1));
		IDsToVec2.put(255, new Vec2(0, 1));
		IDsToVec2.put(256, new Vec2(0, 1));
		IDsToVec2.put(257, new Vec2(0, 1));
		// Cristaux sols
		IDsToVec2.put(200, new Vec2(0, 1));
		IDsToVec2.put(201, new Vec2(0, 1));
		IDsToVec2.put(202, new Vec2(0, 1));
		IDsToVec2.put(203, new Vec2(0, 1));
		IDsToVec2.put(204, new Vec2(0, 1));
		IDsToVec2.put(205, new Vec2(0, 1));
		// Cristaux plafond
		IDsToVec2.put(300, new Vec2(0, 0));
		IDsToVec2.put(301, new Vec2(0, 0));
		IDsToVec2.put(302, new Vec2(0, 0));
		IDsToVec2.put(303, new Vec2(0, 0));
		IDsToVec2.put(304, new Vec2(0, 0));
		IDsToVec2.put(305, new Vec2(0, 0));
		// Cristaux gauche
		IDsToVec2.put(400, new Vec2(1, 0));
		IDsToVec2.put(401, new Vec2(1, 0));
		IDsToVec2.put(402, new Vec2(1, 0));
		IDsToVec2.put(403, new Vec2(1, 0));
		IDsToVec2.put(404, new Vec2(1, 0));
		IDsToVec2.put(405, new Vec2(1, 0));
		// Cristaux droite
		IDsToVec2.put(500, new Vec2(0, 0));
		IDsToVec2.put(501, new Vec2(0, 0));
		IDsToVec2.put(502, new Vec2(0, 0));
		IDsToVec2.put(503, new Vec2(0, 0));
		IDsToVec2.put(504, new Vec2(0, 0));
		IDsToVec2.put(505, new Vec2(0, 0));
		// Arbres droite
		IDsToVec2.put(550, new Vec2(0, 2));
		IDsToVec2.put(551, new Vec2(0, 0));
		// Arbre Gauche
		IDsToVec2.put(450, new Vec2(4, 2));
		IDsToVec2.put(451, new Vec2(3, 0));
		// Liane
		IDsToVec2.put(350, new Vec2(0, 0));
		// Lanternes
		IDsToVec2.put(351, new Vec2(0, 0));
		IDsToVec2.put(352, new Vec2(0, 0));
	}

	static {
		// Sol
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 1, 1, 1), 2);
		// Coin haut droit
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 1, 0, 1), 3);
		// mur droit
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 1, 0, 1), 4);
		// coin bas droit
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 1, 0, 0), 5);
		// plafond
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 1, 1, 0), 6);
		// Coin gauche bas
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 0, 1, 0), 7);
		// mur gauche
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 0, 1, 1), 8);
		// volant seul
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 0, 0, 0), 17);
		// volant horizontal gauche
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 0, 1, 0), 18);
		// volant horizontal droit
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 1, 0, 0), 19);
		// volant vertical haut
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 0, 0, 1), 20);
		// volant vertical bas
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 0, 0, 0), 21);
		// coin gauche haut
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 0, 1, 1), 22);
		// volant horizontal milieu
		PatternCouche1ToIDs.put(patternCreatorCroix(0, 1, 1, 0), 24);
		// volant vertical milieu
		PatternCouche1ToIDs.put(patternCreatorCroix(1, 0, 0, 1), 25);
	}

	static {
		// Transition coin gauche haut
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 8, 1, 1), 9);
		// Transition sol
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 1, 1), 10);

		// Transition coin droit haut
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 4, 1), 11);
		// Transition mur droit
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 4, 1), 12);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 5, 5), 12);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 1, 19, 5), 12);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 1, 19, 4), 12);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 19, 4), 12);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 19, 5), 12);

		// Transition coin droit bas
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 4, 6), 13);
		// Transition plafond
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 1, 6), 14);
		// Transition coin gauche bas
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 1, 6), 15);
		// Transition mur gauche
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 1, 1), 16);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 22, 1, 6), 16);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 18, 1, 7), 16);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 18, 1, 8), 16);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 7, 1, 8), 16);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 18, 1, 8), 16);

		// Transition seul gris clair
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 8, 4, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 7, 4, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 7, 4, 8), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 5, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 6, 5, 21), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 7, 5, 21), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 7, 2, 8), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 19, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 7, 2, 8), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 2, 5, 4), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 6, 2, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 5, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 5, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 2, 5, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(25, 2, 2, 4), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(25, 2, 2, 19), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 2, 1, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 6, 2, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(25, 2, 3, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 6, 1, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 5, 4), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 18, 5, 21), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 6, 7, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 22, 6, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 6, 2, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 6, 2, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 6, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 22, 5, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(26, 8, 6, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 6, 1, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 6, 5), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(20, 2, 3, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(20, 2, 2, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(20, 22, 3, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(20, 22, 2, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 2, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 22, 4, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 8, 2, 1), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 24, 1, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 24, 1, 6), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 7, 1, 8), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 6, 3, 7), 23);
		PatternCouche2ToIDs.put(patternCreatorCroix(20, 2, 2, 1), 23);

		// Transition herbe coin haut gauche
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 2, 1, 1), 26);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 22, 1, 1), 26);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 22, 1, 1), 26);
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 2, 1, 1), 26);

		// Transition herbe coin haut droit
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 3, 1), 27);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 1, 3, 1), 27);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 1, 2, 1), 27);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 2, 1), 27);
		// Transition herbe coin bas gauche
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 7, 1, 7), 28);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 7, 1, 8), 28);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 6, 1, 8), 28);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 6, 1, 7), 28);
		// Transition herbe coin bas droit
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 5, 5), 29);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 5, 4), 29);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 6, 5), 29);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 5, 6), 29);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 1, 6, 4), 29);

		// noir_herbe_haut_gauche
		PatternCouche2ToIDs.put(patternCreatorCroix(8, 2, 4, -2), 39);
		// noir_herbe_bas_droit
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 8, 5, 4), 40);
		// noir_herbe_haut_droit
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 8, 2, 1), 38);
		PatternCouche2ToIDs.put(patternCreatorCroix(4, 8, 3, 1), 38);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 8, 3, 1), 38);

		// gris_herbe_bas_gauche
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 7, 1, 7), 37);
		PatternCouche2ToIDs.put(patternCreatorCroix(1, 6, 4, 7), 37);

		// noir-milieu-vertical
		PatternCouche2ToIDs.put(patternCreatorCroix(-2, 8, 4, 1), 43);
		// noir-milieu-vertical
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 3, 6), 42);
		PatternCouche2ToIDs.put(patternCreatorCroix(3, 1, 1, 6), 42);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 1, 6), 42);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 3, 6), 42);
		PatternCouche2ToIDs.put(patternCreatorCroix(2, 1, 3, 6), 42);
		PatternCouche2ToIDs.put(patternCreatorCroix(22, 1, 1, 6), 42);

	}

	static {
		// Transition 3 coin haut gauche
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 10, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 26, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(9, 26, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 9, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(16, 26, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(16, 26, 1, 10), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 9, 1, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 9, 12, 1), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(16, 26, 12, 33), 30);
		PatternCouche3ToIDs.put(patternCreatorCroix(26, 10, 1, 14), 30);

		// Transition 3 coin haut droit
		PatternCouche3ToIDs.put(patternCreatorCroix(12, 1, 27, 1), 31);
		PatternCouche3ToIDs.put(patternCreatorCroix(27, 1, 27, 1), 31);
		PatternCouche3ToIDs.put(patternCreatorCroix(27, 1, 10, 1), 31);
		PatternCouche3ToIDs.put(patternCreatorCroix(27, 1, 11, 1), 31);
		PatternCouche3ToIDs.put(patternCreatorCroix(27, 16, 10, 1), 31);

		// Transition 3 coin bas gauche
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 1, 16), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 1, 28), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 14, 1, 28), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 14, 12, 28), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(10, 28, 1, 16), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 1, 15), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 12, 16), 32);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 15, 1, 28), 32);

		// Transition 3 coin bas droit
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 1, 29, 29), 33);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 1, 29, 12), 33);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 1, 14, 29), 33);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 16, 29, 12), 33);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 16, 29, 29), 33);
		PatternCouche3ToIDs.put(patternCreatorCroix(10, 1, 29, 29), 33);

		// trans plafond fix
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 29, 23), 14);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 14, 14, 23), 14);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 28, 14, 23), 14);
		PatternCouche3ToIDs.put(patternCreatorCroix(1, 14, 29, 23), 14);

		// trans sol fix
		PatternCouche3ToIDs.put(patternCreatorCroix(23, 26, 10, 1), 10);

	}

	private static Integer[][] patternCreatorCroix(int a, int b, int c, int d) {
		return patternCreatorMatrice(-2, a, -2, b, 1, c, -2, d, -2);
	}

	private static Integer[][] patternCreatorMatrice(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		Integer[][] pattern = { { a, b, c }, { d, e, f }, { g, h, i } };
		return pattern;
	}
}
