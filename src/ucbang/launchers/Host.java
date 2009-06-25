package ucbang.launchers;

import ucbang.network.Client;
import ucbang.network.Server;

public class Host {
    public static void main(String args[]) {
        new Server(12345, false);
        new Client("localhost", true, "Host");
    }
}