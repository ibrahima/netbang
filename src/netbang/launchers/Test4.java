package netbang.launchers;

import netbang.network.Client;
import netbang.network.Server;

public class Test4 {
	public static void main(String args[]) {
		new Server(12345, false);
		Client a = new Client("localhost", true, "1");
		a.gui.setLocation(0, 0);
		Client a1 = new Client("localhost", true, "2");
		a1.gui.setLocation(a.gui.getWidth(), 0);
		Client a2 = new Client("localhost", true, "3");
		a2.gui.setLocation(0, a.gui.getHeight());
		Client a3 = new Client("localhost", true, "4");
		a3.gui.setLocation(a.gui.getWidth(), a.gui.getHeight());

	}
}