package netbang.launchers;

import netbang.network.Client;
import netbang.network.Server;

public class Host {
	public static void main(String args[]) {
		new Server(12345, false);
		new Client("localhost", true, "Host");
	}
}