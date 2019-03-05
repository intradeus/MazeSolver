package backend;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;




public class Launch {

	public static int largeur;
	public static int hauteur;

	public static void main(String[] args) {

		launch();

	}

	private static void launch() {

		// le chemin du fichier est localisé
		File maze = new File(
				"C:\\Users\\Lucas Roland\\eclipse-workspace\\MazeSolver\\src\\ressources\\maze.jpg");

		// on travaille sur BufferedImage
		BufferedImage mazeBuff;

		try {
			mazeBuff = ImageIO.read(maze);	
			// TODO : demander à l'utilisateur la couleur des "murs" du
			// labyrinthe
			// pour l'instant, on travaille avec du noir complet
			// ( #000000 ou RGB(0,0,0) )
			Color couleur = new Color(0, 0, 0);
			
			largeur = mazeBuff.getWidth();
			hauteur = mazeBuff.getHeight();
			
			ArrayList<Coord> coordImage = cherchePixels(couleur, mazeBuff);

			// TEST VISUEL :
			PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
			
			for (Coord i : coordImage) {
				System.out.println("pixel trouvé à " + i.toString());
			}
			System.out.println("total pixels = " + coordImage.size());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author Lucas Roland, inspiré de :
	 *         https://stackoverflow.com/questions/15553122/finding-pixel-position
	 * @param c
	 *            , la couleur recherchée
	 * @param pic
	 *            , l'image dans lequel chercher la couleur
	 * @return , un tableau de coordonnée des pixels de couleur trouvés
	 */
	private static ArrayList<Coord> cherchePixels(Color c, BufferedImage maze) {
		int cVal = c.getRGB(); // get integer value of color we are trying to
								// find

		int x1 = 0;
		int y1 = 0;
		int x2 = maze.getWidth();
		int y2 = maze.getHeight();
		ArrayList<Coord> pixels = new ArrayList<Coord>();

		// créer un tableau contenant toutes les X de l'image (de 0 à width)
		int[] XArray = new int[x2];
		for (int i = 0; i < x2; i++) {
			XArray[i] = i;
		}

		// créer un tableau contenant toutes les Y de l'image (de 0 à height)
		int[] YArray = new int[y2];
		for (int j = 0; j < y2; j++) {
			YArray[j] = j;
		}

		// on passe à travers tous les pixels de l'image pour vérifier la
		// couleur
		for (int yVal : YArray) {

			for (int xVal : XArray) {

				// couleur du pixel actuel
				int color = maze.getRGB(xVal, yVal);
				if (color == cVal) {
					pixels.add(new Coord(xVal, yVal));
				}
			}
			
		}
		return pixels;
	}
}
