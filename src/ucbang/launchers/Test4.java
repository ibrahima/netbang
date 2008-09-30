package ucbang.launchers;

import ucbang.network.Client;
import ucbang.network.Server;

public class Test4 {
	public static void main(String args[]) {
		new Server(12345);
		new Client("localhost", true, "Host");
		new Client("localhost", true, "Client2");
		new Client("localhost", true, "Client3");
		new Client("localhost", true, "Client4");
	}
}