/**
 * 
 */
package netbang.core;

/**
 * @author Ibrahim
 *
 */
public class Choice {
	/**
	 * @param player
	 * @param choice
	 */
	public Choice(int player, int choice) {
		this.player = player;
		this.choice = choice;
	}
	private int player;
	private int choice;
	/**
	 * @return the player
	 */
	public int getPlayer() {
		return player;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	/**
	 * @return the choice
	 */
	public int getChoice() {
		return choice;
	}
	/**
	 * @param choice the choice to set
	 */
	public void setChoice(int choice) {
		this.choice = choice;
	}
}
