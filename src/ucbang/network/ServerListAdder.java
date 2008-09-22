package ucbang.network;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerListAdder {
	String address = "http://cardgameservers.appspot.com/";
	String ip;
	String hash;
	final String type = "bang";
	String name="TestServer";
	public ServerListAdder(String ip) {
		this.ip = ip;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(ip.getBytes());
			byte[] bar = md.digest();
			hash = getHexString(bar);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public void addToServerList() {
		try {
			URL url = new URL(address);

			HttpURLConnection hConnection = (HttpURLConnection) url
					.openConnection();
			HttpURLConnection.setFollowRedirects(true);

			hConnection.setDoOutput(true);
			hConnection.setRequestMethod("POST");

			PrintStream ps = new PrintStream(hConnection.getOutputStream());
			ps.print("ip="+ip+"&amp;name="+name+"&amp;type="+type+"&amp;hash="+hash);
			ps.close();

			hConnection.connect();

			/*if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
				InputStream is = hConnection.getInputStream();
				OutputStream os = new FileOutputStream("output.html");
				int data;
				while ((data = is.read()) != -1) {
					os.write(data);
				}
				is.close();
				os.close();
				hConnection.disconnect();
			}*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}