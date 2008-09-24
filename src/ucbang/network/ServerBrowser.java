package ucbang.network;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;

import java.awt.Dimension;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServerBrowser extends JFrame{
	ArrayList<ServerInfo> servers = new ArrayList<ServerInfo>();
	JTable servertable;
	public ServerBrowser(){
		downloadList();
		setPreferredSize(new Dimension(480, 320));
		setSize(new Dimension(480, 320));
		this.setVisible(true);
		this.setTitle("Server Browser");
		ServerTableModel tm = new ServerTableModel(servers);
		servertable=new JTable(tm);
		this.add(servertable);

	}
	public String chooseServer(){
		//TODO: Work out a choosing thing.
		return servers.get(0).ip;
	}
	public static void main(String args[]) {
		new ServerBrowser();
	}

	public void downloadList() {
		URL url;
		try {
			url = new URL("http://cardgameservers.appspot.com/xml");
			HttpURLConnection hConnection = (HttpURLConnection) url
					.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			if (HttpURLConnection.HTTP_OK == hConnection.getResponseCode()) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				dbf.setIgnoringElementContentWhitespace(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(hConnection.getInputStream());
				for (Node server = doc.getFirstChild().getChildNodes().item(1); server != null; server = server
						.getNextSibling()) {
					if (server.getNodeName().equals("server")) {
						servers.add(processServerNode(server));
					}
				}
				/*
				 * BufferedReader is = new BufferedReader(new
				 * InputStreamReader(hConnection.getInputStream()));
				 * while(is.ready()) System.out.println(is.readLine());
				 */
				hConnection.disconnect();
				Iterator<ServerInfo> iter = servers.iterator();
				while (iter.hasNext()) {
					System.out.println(iter.next());
				}
			} else {
				System.out.println("Ugh, something bad happened");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ServerInfo processServerNode(Node n) {
		NodeList ns = n.getChildNodes();
		return new ServerInfo(ns.item(1).getTextContent(), ns.item(3)
				.getTextContent(), ns.item(5).getTextContent());
	}

	void recPrint(Node n) {
		if (!n.getNodeName().equals("#text"))
			System.out.println(n.getNodeName() + ":" + n.getTextContent());
		NodeList ns = n.getChildNodes();
		for (Node a = ns.item(0); a != null; a = a.getNextSibling()) {
			recPrint(a);
		}
	}

	private static class MyErrorHandler implements ErrorHandler {

		private PrintWriter out;

		MyErrorHandler(PrintWriter out) {
			this.out = out;
		}

		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
			if (systemId == null) {
				systemId = "null";
			}
			String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
					+ ": " + spe.getMessage();
			return info;
		}

		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	}
	private class ServerInfo {
		String name;
		String ip;
		String type;

		public ServerInfo(String name, String ip, String type) {
			this.name = name;
			this.ip = ip;
			this.type = type;
		}

		public String toString() {
			return type + " server \"" + name + "\" on " + ip;
		}
	}
	class ServerTableModel extends AbstractTableModel {
		String[] columns = {"Name","IP Address", "Game Type"};
	    private String[][] data;
	    public ServerTableModel(ArrayList<ServerInfo> list){
	    	data = new String[list.size()][3];
	    	Iterator<ServerInfo> iter = list.iterator();
	    	int i=0;
	    	while(iter.hasNext()){
	    		ServerInfo temp = iter.next();
	    		data[i][0]=temp.name;
	    		data[i][1]=temp.ip;
	    		data[i][2]=temp.type;
	    		i++;
	    	}
	    }

	    public int getColumnCount() {
	        return columns.length;
	    }

	    public int getRowCount() {
	        return data.length;
	    }

	    public String getColumnName(int col) {
	        return columns[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }


	    /*
	     * Don't need to implement this method unless your table's
	     * editable.
	     */
	    public boolean isCellEditable(int row, int col) {
	        //Note that the data/cell address is constant,
	        //no matter where the cell appears onscreen.
            return false;
	    }
    }

}