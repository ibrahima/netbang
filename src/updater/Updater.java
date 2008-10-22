package updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Updater {
	public static void main(String[] args){
		System.out.println("FOOBAR");
	}
	int latestRevision(){
		URL url;
		try {
			url = new URL("http://inst.eecs.berkeley.edu/~ibrahima/bang/revision.txt");
			HttpURLConnection hConnection = (HttpURLConnection) url
					.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
				BufferedReader is = new BufferedReader(new
						InputStreamReader(hConnection.getInputStream()));
				int rev = Integer.valueOf(is.readLine());
				return rev;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return -1;
	}
}
