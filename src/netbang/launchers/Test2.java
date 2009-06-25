package netbang.launchers;

import netbang.network.Client;
import netbang.network.Server;

public class Test2 {
    public static void main(String args[]) {
        new Server(12345, false);
        Client a = new Client("localhost", true, "1");
        a.gui.setLocation(0, 0);
        Client a1 = new Client("localhost", true, "2");
        a1.gui.setLocation(a.gui.getWidth(), 0);
    }
}