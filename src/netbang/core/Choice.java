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
	 * @param playerid
	 * @param choice
	 */
	public Choice(int playerid, int choice) {
		this.playerid = playerid;
		this.choice = choice;
	}
	private final int playerid;
	private int choice;
	/**
	 * @return the playerid
	 */
	public int getPlayerid() {
		return playerid;
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
