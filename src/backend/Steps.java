package backend;

public class Steps {

	// ATTRIBUTS :
	private Coord actuel;
	private Coord dest;
	private Coord right;
	private Coord top;
	private Coord bot;
	private Coord left;

	
	// CONSTRUCTEUR
	public Steps(Coord actuel, Coord dest) {
		this.actuel = actuel;
		this.dest = dest;
		association();
		bestCoord();
	}

	
	private void bestCoord() {
		pythagore(right);
		
	}


	private void pythagore(Coord future) {
		
		
		
	}


	private void association() {
		this.right = actuel.getRight();
		this.left = actuel.getLeft();
		this.top = actuel.getTop();
		this.bot = actuel.getBot();
	}
	
	
}
