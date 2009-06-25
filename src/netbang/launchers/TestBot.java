package netbang.launchers;

import netbang.bots.RandomBot;
import netbang.network.Client;
import netbang.network.Server;

public class TestBot {

    public static void main(String args[]) {
        new Server(12345, false);
        Client a = new Client("localhost", true, "1");
        a.gui.setLocation(0, 0);
        RandomBot a1 = new RandomBot("localhost", "2");
    }

}
