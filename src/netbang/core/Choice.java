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
	public final int playerid;
	public int choice;
}
