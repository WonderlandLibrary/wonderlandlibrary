package slavikcodd3r.rainbow.gui.hacktools;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;
import java.io.File;
import java.net.UnknownHostException;
import java.net.InetAddress;
import javax.swing.LayoutStyle;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.util.List;
import javax.swing.JFrame;

public class GuiSubdomainBrute extends JFrame
{
    private List<String> subs;
    private int count;
    private JButton jButton1;
    private JCheckBox jCheckBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private TextArea textArea1;
    
    public GuiSubdomainBrute() {
        this.subs = new ArrayList<String>();
        this.count = 0;
        this.initComponents();
    }
    
    private void initComponents() {
        this.jTextField1 = new JTextField();
        this.jLabel1 = new JLabel();
        this.textArea1 = new TextArea();
        this.jButton1 = new JButton();
        this.jLabel2 = new JLabel();
        this.jTextField2 = new JTextField();
        this.jCheckBox1 = new JCheckBox();
        this.jTextField3 = new JTextField();
        this.jLabel3 = new JLabel();
        this.jTextField4 = new JTextField();
        this.setTitle("Subdomains Brute");
        this.jTextField1.setToolTipText("");
        this.jLabel1.setText("Path to list with Subdomains");
        this.jButton1.setText("Start");
        this.jButton1.setToolTipText("");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiSubdomainBrute.this.jButton1ActionPerformed(evt);
            }
        });
        this.jLabel2.setText("Target");
        this.jTextField2.setToolTipText("");
        this.jTextField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiSubdomainBrute.this.jTextField2ActionPerformed(evt);
            }
        });
        this.jCheckBox1.setText("HTTP Responce code");
        this.jCheckBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiSubdomainBrute.this.jCheckBox1ActionPerformed(evt);
            }
        });
        this.jTextField3.setToolTipText("");
        this.jLabel3.setText("Threads");
        this.jTextField4.setText("0/0");
        this.jTextField4.setToolTipText("");
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTextField2).addComponent(this.jButton1, -1, -1, 32767).addGroup(layout.createSequentialGroup().addComponent(this.textArea1, -2, 341, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jCheckBox1, -1, -1, 32767).addComponent(this.jTextField3).addComponent(this.jLabel3, -2, 55, -2).addComponent(this.jTextField4))).addComponent(this.jTextField1).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel1)).addGap(0, 0, 32767))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(13, 32767).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField2, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jButton1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.textArea1, -2, 205, -2).addGroup(layout.createSequentialGroup().addComponent(this.jCheckBox1).addGap(18, 18, 18).addComponent(this.jLabel3).addGap(3, 3, 3).addComponent(this.jTextField3, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.jTextField4, -2, -1, -2))).addContainerGap()));
        this.pack();
    }
    
    private boolean isSubExist(final String sub) {
        try {
            InetAddress.getByName(sub);
        }
        catch (UnknownHostException ex) {
            return false;
        }
        return true;
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        this.subs.clear();
        this.count = 0;
        new Thread() {
            @Override
            public void run() {
                GuiSubdomainBrute.this.textArea1.append("Start!\n");
                try {
                    final Scanner txtscan = new Scanner(new File(GuiSubdomainBrute.this.jTextField1.getText()));
                    while (txtscan.hasNextLine()) {
                        GuiSubdomainBrute.this.subs.add(txtscan.nextLine());
                    }
                    final ForkJoinPool forkJoinPool = new ForkJoinPool(Integer.parseInt(GuiSubdomainBrute.this.jTextField3.getText()));
                    forkJoinPool.submit(() -> Arrays.stream(GuiSubdomainBrute.this.subs.toArray()).parallel().forEach(sub -> GuiSubdomainBrute.this.go(String.valueOf(sub.toString()) + "." + GuiSubdomainBrute.this.jTextField2.getText())));
                }
                catch (Exception ex) {}
            }
        }.start();
    }
    
    private void go(final String str) {
        if (this.isSubExist(str)) {
            if (this.jCheckBox1.isSelected()) {
                try {
                    this.textArea1.append(String.valueOf(str) + " : " + this.sendGET(str) + "\n");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                this.textArea1.append(String.valueOf(str) + "\n");
            }
        }
        ++this.count;
        this.jTextField4.setText(String.valueOf(this.count) + "/" + this.subs.size());
    }
    
    private String sendGET(final String dom) throws Exception {
        final String url = "http://" + dom;
        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.connect();
        final int responseCode = con.getResponseCode();
        return "Code - " + responseCode;
    }
    
    private void jTextField2ActionPerformed(final ActionEvent evt) {
    }
    
    private void jCheckBox1ActionPerformed(final ActionEvent evt) {
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
            Logger.getLogger(GuiSubdomainBrute.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(GuiSubdomainBrute.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(GuiSubdomainBrute.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(GuiSubdomainBrute.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiSubdomainBrute().setVisible(true);
            }
        });
    }
}
