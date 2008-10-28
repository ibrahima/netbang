package ucbang.bots;

public class RandomBot extends Bot {

	public RandomBot(String host, String name) {
		super(host, name);
	}

	public int choose() {
		return 0;
	}
	/**
	 * Prompts the player to play a card
	 */
	protected void promptPlayCard() {
		outMsgs.add("Prompt:0");
	}

}
