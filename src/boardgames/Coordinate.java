package boardgames;

public class Coordinate {

	private int x, y;

	public Coordinate(int x, int y) {
		setX(x);
		setY(y);
	}

	public Coordinate(Coordinate c) {
		setX(c.getX());
		setY(c.getY());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinate))
			return false;
		if (obj == this)
			return true;
		Coordinate c = (Coordinate) obj;
		if (this.getX() == c.getX() && this.getY() == c.getY())
			return true;
		else
			return false;
			
	}
}
