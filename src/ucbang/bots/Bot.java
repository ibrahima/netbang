package ucbang.bots;

import ucbang.network.Client;

public abstract class Bot extends Client{
	public Bot(String host, String name){
		super(host, false, name);
	}
	/**
	 * This method will be overridden by various bot classes to define different bot behaviors.
	 * @return
	 */
	public abstract int choose();
}
