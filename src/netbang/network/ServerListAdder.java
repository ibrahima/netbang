package netbang.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerListAdder {
    String address = "http://cardgameservers.appspot.com/";
    String hash;
    final String type = "bang";
    private String name;
    int players=1;
    boolean started=false;
    int failcount = 0;
    public ServerListAdder(String name) {
        this.name = name;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(name.getBytes());
            md.update(type.getBytes());
            byte[] bar = md.digest();
            hash = getHexString(bar);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public void setPlayers(int pl){
        players=pl;
    }
    public void setStarted(boolean value){
        started=value;
    }
    public void setName(String name) {
        this.name = name;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(name.getBytes());
            md.update(type.getBytes());
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
        if(failcount>5) {
            System.out.println("Not trying to add to serverlist since it failed 5 times.");
            return;
        }
        try {
            URL url = new URL(address + "add");

            HttpURLConnection hConnection = (HttpURLConnection) url
                    .openConnection();
            HttpURLConnection.setFollowRedirects(true);

            hConnection.setDoOutput(true);
            hConnection.setRequestMethod("POST");

            PrintStream ps = new PrintStream(hConnection.getOutputStream());
            ps.print("gamename=" + name + "&amp;type=" + type + "&amp;hash="
                    + hash + "&amp;players="+ players + "&amp;started="+ started);
            ps.close();
            hConnection.connect();

            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                BufferedReader is = new BufferedReader(new InputStreamReader(
                        hConnection.getInputStream()));
                while (is.ready())
                    System.out.println(is.readLine());
                is.close();
                hConnection.disconnect();
            } else {
                System.out.println("Ugh, something bad happened when trying to connect to " +
                        url);
            }
        } catch(UnknownHostException uh){
            System.out.println("Couldn't find "+address);
        } catch (Exception ex) {
            failcount++;
            ex.printStackTrace();
        }
    }

    public void RemoveFromServerList() {
        try {
            URL url = new URL(address + "remove");

            HttpURLConnection hConnection = (HttpURLConnection) url
                    .openConnection();
            HttpURLConnection.setFollowRedirects(true);

            hConnection.setDoOutput(true);
            hConnection.setRequestMethod("POST");

            PrintStream ps = new PrintStream(hConnection.getOutputStream());
            ps.print("gamename=" + name + "&amp;type=" + type + "&amp;hash="
                    + hash);
            ps.close();
            hConnection.connect();

            if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
                BufferedReader is = new BufferedReader(new InputStreamReader(
                        hConnection.getInputStream()));
                System.out.println(is.readLine());
                is.close();
                hConnection.disconnect();
            } else {
                System.out.println("Ugh, something bad happened when trying to connect to " +
                url);
            }
        } catch(UnknownHostException uh){
            System.out.println("Couldn't find "+address);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}