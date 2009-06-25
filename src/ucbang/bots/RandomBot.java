package ucbang.bots;

import java.util.Random;

/**
 * The Randombot is a bot that randomly responds to prompts to play a card.
 * 
 * @author Ibrahim
 * 
 */
public class RandomBot extends Bot {

	/**
	 * @param host
	 * @param name
	 */
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
		Random r = new Random();
		int choice = r.nextInt(this.player.hand.size());
		outMsgs.add("Prompt:" + choice);
	}

}
