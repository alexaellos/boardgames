package boardgames;

public class Protocol {

	private static final int WAITING = 0;
	private static final int NOT_MATCHED = 1;
	private static final int MATCHED = 2;
	private int state = WAITING;

	public String processInput(String theInput) {
		String theOutput = null;
		if (state == WAITING) {
			theOutput = "Waiting for another player. Send me what game you want to play?";
			// state =
		}
		return theOutput;
	}
}
