package backend;

//CETTE CLASSE EST UNE COORDONNEE
public class Coord {

	
	//Attributs
	private int x;
	private int y;
	private boolean start = false;
	private boolean end = false;
	
	//Constructeur
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//accesseurs
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String toString() {
		return "coordonnée x= " + x + " et y= " + y;
	}
	
	public Coord getRight() {
		return new Coord(x+1,y);	
	}
	
	public Coord getLeft() {
		return new Coord(x-1,y);	
	}
	
	public Coord getTop() {
		return new Coord(x,y+1);	
	}
	
	public Coord getBot() {
		return new Coord(x,y-1);	
	}
	
	
	
	//mutateurs
	public void setStart() {
		this.start = true;
	}
	
	public void setEnd() {
		this.end = true;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	
}
