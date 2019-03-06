package backend;

public class Winner extends Coord {

	//attribut
	private double score;
	
	public Winner(int x, int y, double score) {
		super(x, y);
		this.score = score;
	}

	
	public double getScore() {
		return score;
	}
}
