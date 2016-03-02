package boardgames;

public class Command {

	private Coordinate coord1, coord2;
	private String playerId;

	public Command(String playerId, Coordinate c1, Coordinate c2) {
		setPlayerId(playerId);
		setCoord1(c1);
		setCoord2(c2);
	}

	public Command(String playerId, Coordinate c1) {
		setPlayerId(playerId);
		setCoord1(c1);
		setCoord2(null);
	}

	public Coordinate getCoord1() {
		return coord1;
	}

	public void setCoord1(Coordinate coord1) {
		this.coord1 = coord1;
	}

	public Coordinate getCoord2() {
		return coord2;
	}

	public void setCoord2(Coordinate coord2) {
		this.coord2 = coord2;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

}
