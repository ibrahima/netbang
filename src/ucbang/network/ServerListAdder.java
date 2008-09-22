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
	String address = "http://cardgameservers.appspot.com/process";
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
			ps.print("ip="+ip+"&amp;gamename="+name+"&amp;type="+type+"&amp;hash="+hash);
			ps.close();
			System.out.println("Should have added server");
			hConnection.connect();

			if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
				InputStream is = hConnection.getInputStream();
				byte data;
				while ((data = (byte) is.read()) != -1) {
					System.out.print(data);
				}
				is.close();
				hConnection.disconnect();
			}else{
				System.out.println("Ugh, something bad happened");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}