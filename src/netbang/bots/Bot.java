package netbang.bots;

import netbang.network.Client;

public abstract class Bot extends Client {
    public Bot(String host, String name) {
        super(host, false, name);
    }

    /**
     * This method will be overridden by various bot classes to define different
     * bot behaviors.
     * 
     * @return
     */
    public abstract int choose();

    /**
     * All bots will always agree to start
     * 
     * @return returns 0 to signify that the bot agrees to start
     */
    protected int promptStart() {
        print("HI");
        return 0;
    }
}
