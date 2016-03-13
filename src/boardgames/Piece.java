package boardgames;

public class Piece {

	private Coordinate coord;
	private String id, playerId;
	private String imageLocation;

	public Piece(Coordinate c) {
		setCoord(c);
		setId(null);
		setPlayerId(null);
		setImageLocation("");
	}

	public Piece(Coordinate c, String id, String PlayerId, String imageLocation) {
		setCoord(c);
		setId(id);
		setPlayerId(playerId);
		setImageLocation(imageLocation);
	}

	public Coordinate getCoord() {
		return coord;
	}

	public void setCoord(Coordinate coord) {
		this.coord = coord;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
	public String toString() {
		return id/* + "@" + coord*/;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
}
