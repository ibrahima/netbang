package ucbang.network;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import updater.Updater;

public class ServerBrowser extends JFrame implements ActionListener, MouseListener{
    /**
     * 
     */
    private static final long serialVersionUID = 773307472628459263L;
    ArrayList<ServerInfo> servers = new ArrayList<ServerInfo>();
    JTable servertable;
    JScrollPane scrollPane;
    JButton join, create, refresh;
    JButton joinlan, createlan, foobar;
    ServerTableModel tm;
    /**
     * Constructs a ServerBrowser
     */
    public ServerBrowser(){
        setPreferredSize(new Dimension(480, 320));
        setSize(new Dimension(480, 320));
        this.setTitle("Server Browser");
        tm = new ServerTableModel(servers);
        servertable=new JTable(tm);
        scrollPane = new JScrollPane(servertable);
        servertable.setFillsViewportHeight(true);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy=1;
        gbc.weightx=1;
        gbc.weighty=1;
        gbc.gridx=1;
        gbc.gridheight=4;
        gbc.gridwidth=3;
        gbc.fill=GridBagConstraints.BOTH;
        this.add(scrollPane,gbc);
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.weighty=0;
        gbc.gridy=5;

        gbc.gridx=1;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        
        join = new JButton("Join");
        join.setMnemonic(java.awt.event.KeyEvent.VK_J);
        this.add(join,gbc);
        join.addActionListener(this);
        
        create = new JButton("Create Server");
        create.setMnemonic(java.awt.event.KeyEvent.VK_C);
        gbc.gridx=2;
        create.addActionListener(this);
        this.add(create,gbc);
        gbc.gridx=3;
        
        refresh = new JButton("Refresh");
        refresh.setMnemonic(java.awt.event.KeyEvent.VK_R);
        refresh.addActionListener(this);
        this.add(refresh,gbc);
        
        gbc.gridy = 6;
        gbc.gridx = 1;
        joinlan = new JButton("Join LAN");
        joinlan.setMnemonic(java.awt.event.KeyEvent.VK_O);
        joinlan.addActionListener(this);
        this.add(joinlan, gbc);
        
        gbc.gridx = 2;
        createlan = new JButton("Create LAN");
        createlan.setMnemonic(java.awt.event.KeyEvent.VK_L);
        createlan.addActionListener(this);
        this.add(createlan, gbc);
        
        gbc.gridx = 3;
        foobar = new JButton("FOOBAR");
        foobar.setMnemonic(java.awt.event.KeyEvent.VK_F);
                
        this.add(foobar, gbc);    
        this.pack();
        this.setVisible(true);
        downloadList();
        tm.setData(servers);
        tm.fireTableDataChanged();
        servertable.addMouseListener(this);
        int current = currentRevision();
        int latest = latestRevision();
        System.out.println(current);
        System.out.println(latest);
        if(current<latest){
            JOptionPane.showMessageDialog(this, "Your game is out of date. Automatically updating...");
            long localUpdaterSize = Updater.getLocalFileSize("updater.jar");
            long remoteUpdaterSize = Updater.getRemoteFileSize("http://inst.eecs.berkeley.edu/~ibrahima/bang/updater.jar");
            //simple/lazy check to see if updater has been updated
            if(localUpdaterSize<0||(remoteUpdaterSize>0&&localUpdaterSize!=remoteUpdaterSize)){
                Updater.downloadFile("http://inst.eecs.berkeley.edu/~ibrahima/bang/updater.jar", "updater.jar");
            }
            try {
                Process foo = Runtime.getRuntime().exec("java -jar updater.jar");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("You are up to date!");
        }
    }
    public static void main(String args[]) {
        new ServerBrowser();
    }

    /**
     * Downloads the list of Bang servers from the serverlist website
     */
    public void downloadList() {
        servers.clear();
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
                                
                hConnection.disconnect();
                Iterator<ServerInfo> iter = servers.iterator();
                while (iter.hasNext()) {
                    System.out.println(iter.next());
                }
            } else {
                System.out.println("Ugh, something bad happened");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ServerInfo processServerNode(Node n) {
        NodeList ns = n.getChildNodes();
                String dateNode = ns.item(11).getTextContent();
                String dateLocal = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return new ServerInfo(ns.item(1).getTextContent(), ns.item(3)
                .getTextContent(), ns.item(5).getTextContent(),
                Integer.parseInt(ns.item(7).getTextContent()), Boolean.parseBoolean(ns.item(9).getTextContent()), 
                                    (dateNode.substring(0,dateLocal.length()).equals(dateLocal)?dateNode.substring(dateLocal.length()+1):dateNode)); 
                                    //(created today?)time only:add date;
    }
    public int currentRevision(){
        BufferedReader is;
        try {
            is = new BufferedReader(
                    new InputStreamReader(ClassLoader.getSystemResource("revision.txt").openStream()));
            int rev = Integer.valueOf(is.readLine());
            return rev;
        } catch(NullPointerException e){
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 1<<31-1;
    }
    public int latestRevision(){
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
    void recPrint(Node n) {
        if (!n.getNodeName().equals("#text"))
            System.out.println(n.getNodeName() + ":" + n.getTextContent());
        NodeList ns = n.getChildNodes();
        for (Node a = ns.item(0); a != null; a = a.getNextSibling()) {
            recPrint(a);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(refresh)){
            downloadList();
            tm.setData(servers);
            tm.fireTableDataChanged();
        }else if(e.getSource().equals(join)){
            int i=servertable.getSelectedRow();
            if(i>=0){
                System.out.println("Joining "+servers.get(i).ip);
                new Client(servers.get(i).ip, true);
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Choose a server!");
            }
        }else if(e.getSource().equals(create)){
            new Server(12345, false);
            new Client("localhost", true, "Host");
            this.dispose();
        }else if(e.getSource().equals(createlan)){
            new Server(12345, true);
            new Client("localhost", true, "Host");
            this.dispose();
        }else if(e.getSource().equals(joinlan)){
            String host = JOptionPane
            .showInputDialog("Input server IP");
            new Client(host, true, "Host");
            this.dispose();
        }else if(e.getSource().equals(foobar)){
            this.dispose();
            System.exit(-1);
        }
        
    }

    private class ServerInfo {
        String name;
        String ip;
        String type;
        int players;
        boolean started;
                String time;
        public ServerInfo(String name, String ip, String type, int players, boolean started, String time) {
            this.name = name;
            this.ip = ip;
            this.type = type;
            this.players = players;
            this.started = started;
                        this.time = time;
        }

        public String toString() {
            return type + " server \"" + name + "\" on " + ip;
        }
    }
    class ServerTableModel extends AbstractTableModel {
        /**
         * 
         */
        private static final long serialVersionUID = -5523627394901068270L;
        String[] columns = {"Name","IP Address", "Game Type", "Players", "Started", "Time Last Seen"};
        private String[][] data;
        public ServerTableModel(ArrayList<ServerInfo> list){
            data = new String[list.size()][6];
            Iterator<ServerInfo> iter = list.iterator();
            int i=0;
            while(iter.hasNext()){
                ServerInfo temp = iter.next();
                data[i][0]=temp.name;
                data[i][1]=temp.ip;
                data[i][2]=temp.type;
                data[i][3]=temp.players+"";
                data[i][4]=temp.started+"";
                        data[i][5]=temp.time+"";
                i++;
            }
        }
        public void setData(ArrayList<ServerInfo> list){
            data = new String[list.size()][6];
            Iterator<ServerInfo> iter = list.iterator();
            int i=0;
            while(iter.hasNext()){
                ServerInfo temp = iter.next();
                data[i][0]=temp.name;
                data[i][1]=temp.ip;
                data[i][2]=temp.type;
                data[i][3]=temp.players+"";
                data[i][4]=temp.started+"";
                        data[i][5]=temp.time+"";
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
    }
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==2&&e.getSource().equals(servertable)){
            int i=servertable.getSelectedRow();
            if(i>=0){
                System.out.println("Joining "+servers.get(i).ip);
                new Client(servers.get(i).ip, true);
                this.dispose();
            }
        }
        
    }
    public void mouseEntered(MouseEvent e) {
        
    }
    public void mouseExited(MouseEvent e) {
        
    }
    public void mousePressed(MouseEvent e) {
        
    }
    public void mouseReleased(MouseEvent e) {
        
    }


}