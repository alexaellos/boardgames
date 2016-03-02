package boardgames;

public class Piece {

	private Coordinate coord;
	private String id, playerId;

	public Piece(Coordinate c) {
		setCoord(c);
		setId(null);
		setPlayerId(null);
	}

	public Piece(Coordinate c, String id, String PlayerId) {
		setCoord(c);
		setId(id);
		setPlayerId(playerId);
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

}
