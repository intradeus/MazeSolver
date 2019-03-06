package backend;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
	private static final int IMG_WIDTH = 200;
	private static final int IMG_HEIGHT = 200;

	public static void main(String[] args) throws IOException {

		launch();

	}

	private static void launch() throws IOException {

		// code pour resizer l'image tiré de
		// https://www.mkyong.com/java/how-to-resize-an-image-in-java/
		BufferedImage originalImage = ImageIO.read(new File("src\\ressources\\maze.jpg"));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		ImageIO.write(resizeImageJpg, "jpg", new File("src\\ressources\\maze_resize.jpg"));

		// le chemin du fichier est localisé
		File maze = new File("src\\ressources\\maze_resize.jpg");

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

			// TEST VISUEL : imprimé sur fichier "output.txt"
			// PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			// System.setOut(out);

			for (Coord i : coordImage) {
				System.out.println("pixel trouvé à " + i.toString());
			}
			System.out.println("total pixels = " + coordImage.size());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// code pour resizer l'image tiré de
	// https://www.mkyong.com/java/how-to-resize-an-image-in-java/
	private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	// code pour resizer l'image tiré de
	// https://www.mkyong.com/java/how-to-resize-an-image-in-java/
	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {

		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		return resizedImage;
	}	

	/**
	 * @author Lucas Roland, inspiré de :
	 *         https://stackoverflow.com/questions/15553122/finding-pixel-position
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
