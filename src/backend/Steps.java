package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Steps {

	// attributs donnés
	private Coord actuel, dest;

	// attributs calculés
	private Coord right, left, top, bot;

	// attributs utilisés
	private Coord winner;
	private ArrayList<Winner> winners;

	// CONSTRUCTEUR
	public Steps(Coord actuel, Coord dest) {
		this.actuel = actuel;
		this.dest = dest;
		association();

		bestCoord();

	}
	
	private void bestCoord() {

		winners.add(new Winner(right.getX(), right.getY(), pythagore(right)));
		winners.add(new Winner(left.getX(), left.getY(), pythagore(left)));
		winners.add(new Winner(top.getX(), top.getY(), pythagore(top)));
		winners.add(new Winner(bot.getX(), bot.getY(), pythagore(bot)));

		Collections.sort(winners, (o1, o2) -> Double.compare(o1.getScore(), o2.getScore()));

		this.winner = winners.get(0);
	}

	private double pythagore(Coord future) {
		
		double lx = Math.abs(future.getX() - dest.getX());
		double ly = Math.abs(future.getY() - dest.getY());
		lx = Math.pow(lx, 2);
		ly = Math.pow(ly, 2);

		return Math.sqrt(lx + ly);
	}
	
	
	private void association() {
		this.right = actuel.getRight();
		this.left = actuel.getLeft();
		this.top = actuel.getTop();
		this.bot = actuel.getBot();
	}

}
