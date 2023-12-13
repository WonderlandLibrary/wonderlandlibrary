package slavikcodd3r.rainbow.gui.hacktools;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.concurrent.ForkJoinPool;
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

public class GuiHttpGetBrute extends JFrame
{
    private List<String> words;
    private List<String> urlList;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JTextField jTextField5;
    private JTextField jTextField6;
    private TextArea textArea1;
    
    public GuiHttpGetBrute() {
        this.words = new ArrayList<String>();
        this.urlList = new ArrayList<String>();
        this.initComponents();
    }
    
    private void initComponents() {
        this.jTextField1 = new JTextField();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jTextField2 = new JTextField();
        this.textArea1 = new TextArea();
        this.jLabel3 = new JLabel();
        this.jButton1 = new JButton();
        this.jTextField3 = new JTextField();
        this.jTextField4 = new JTextField();
        this.jLabel4 = new JLabel();
        this.jTextField5 = new JTextField();
        this.jLabel5 = new JLabel();
        this.jButton2 = new JButton();
        this.jButton3 = new JButton();
        this.jLabel6 = new JLabel();
        this.jTextField6 = new JTextField();
        this.jComboBox1 = new JComboBox<String>();
        this.setTitle("HTTP/HTTPS GET Method Brute");
        this.jTextField1.setToolTipText("");
        this.jTextField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jTextField1ActionPerformed(evt);
            }
        });
        this.jLabel1.setText("List of targets");
        this.jLabel2.setText("Path to list with words");
        this.jTextField2.setToolTipText("");
        this.jLabel3.setText("Log");
        this.jButton1.setText("Start");
        this.jButton1.setToolTipText("");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jButton1ActionPerformed(evt);
            }
        });
        this.jTextField3.setText("0/0");
        this.jTextField4.setText("200,403");
        this.jTextField4.setToolTipText("");
        this.jLabel4.setText("Threads");
        this.jTextField5.setText("1");
        this.jTextField5.setToolTipText("");
        this.jLabel5.setText("Allowed Responce Codes");
        this.jButton2.setText("...");
        this.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jButton2ActionPerformed(evt);
            }
        });
        this.jButton3.setText("...");
        this.jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jButton3ActionPerformed(evt);
            }
        });
        this.jLabel6.setText("Allowed context on page");
        this.jTextField6.setToolTipText("");
        this.jTextField6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jTextField6ActionPerformed(evt);
            }
        });
        this.jComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] { "By responce codes", "By context on page" }));
        this.jComboBox1.setToolTipText("");
        this.jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiHttpGetBrute.this.jComboBox1ActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jTextField3).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jButton1, -2, 382, -2)).addGroup(layout.createSequentialGroup().addComponent(this.textArea1, -2, 357, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTextField4).addComponent(this.jTextField5, GroupLayout.Alignment.TRAILING).addComponent(this.jTextField6).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel4).addComponent(this.jLabel5).addComponent(this.jLabel6)).addGap(0, 0, 32767)).addComponent(this.jComboBox1, 0, -1, 32767))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.jTextField2, GroupLayout.Alignment.LEADING, -1, 464, 32767).addComponent(this.jLabel1, GroupLayout.Alignment.LEADING).addComponent(this.jLabel2, GroupLayout.Alignment.LEADING).addComponent(this.jLabel3, GroupLayout.Alignment.LEADING).addComponent(this.jTextField1, GroupLayout.Alignment.LEADING)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton2).addComponent(this.jButton3)).addGap(0, 0, 32767))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(7, 7, 7).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jTextField1, -2, -1, -2).addComponent(this.jButton3)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jTextField2, -2, -1, -2).addComponent(this.jButton2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel3).addGap(6, 6, 6).addComponent(this.textArea1, -2, 192, -2)).addGroup(layout.createSequentialGroup().addComponent(this.jComboBox1, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel6).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField6, -2, -1, -2).addGap(18, 18, 18).addComponent(this.jLabel5).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField4, -2, -1, -2).addGap(17, 17, 17).addComponent(this.jLabel4).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextField5, -2, -1, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton1, -1, 54, 32767).addComponent(this.jTextField3, -2, -1, -2))));
        this.pack();
    }
    
    private void jTextField1ActionPerformed(final ActionEvent evt) {
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        this.words.clear();
        this.urlList.clear();
        new Thread() {
            int count = 0;
            
            @Override
            public void run() {
                try {
                    final File file = new File(GuiHttpGetBrute.this.jTextField2.getText());
                    final BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        GuiHttpGetBrute.this.words.add(line);
                    }
                    final File file2 = new File(GuiHttpGetBrute.this.jTextField1.getText());
                    final BufferedReader br2 = new BufferedReader(new FileReader(file2));
                    String line2;
                    while ((line2 = br2.readLine()) != null) {
                        GuiHttpGetBrute.this.urlList.add(line2);
                    }
                    final List<String> urls = new ArrayList<String>();
                    for (final String s1 : GuiHttpGetBrute.this.urlList) {
                        for (final String s2 : GuiHttpGetBrute.this.words) {
                            final String url3 = s1.replace("{text}", s2);
                            urls.add(url3);
                        }
                    }
                    GuiHttpGetBrute.this.jTextField3.setText("0/" + urls.size());
                    final ForkJoinPool forkJoinPool = new ForkJoinPool(Integer.parseInt(GuiHttpGetBrute.this.jTextField5.getText()));
                    if ("By responce codes".equals(GuiHttpGetBrute.this.jComboBox1.getSelectedItem().toString())) {
                        forkJoinPool.submit(() -> Arrays.stream(urls.toArray()).parallel().forEach(url2 -> this.go(url2.toString())));
                    }
                    else if ("By context on page".equals(GuiHttpGetBrute.this.jComboBox1.getSelectedItem().toString())) {
                        forkJoinPool.submit(() -> Arrays.stream(urls.toArray()).parallel().forEach(url2 -> this.goByContext(url2.toString())));
                    }
                }
                catch (Exception ex) {}
            }
            
            private void go(final String url) {
                try {
                    if (this.pageExist(url)) {
                        GuiHttpGetBrute.this.textArea1.append(String.valueOf(url) + "\n");
                    }
                }
                catch (Exception ex) {}
                ++this.count;
                GuiHttpGetBrute.this.jTextField3.setText(String.valueOf(this.count) + "/" + GuiHttpGetBrute.this.words.size() * GuiHttpGetBrute.this.urlList.size());
            }
            
            private void goByContext(final String url) {
                try {
                    if (this.pageContainsContext(url)) {
                        GuiHttpGetBrute.this.textArea1.append(String.valueOf(url) + "\n");
                    }
                }
                catch (Exception ex) {}
                ++this.count;
                GuiHttpGetBrute.this.jTextField3.setText(String.valueOf(this.count) + "/" + GuiHttpGetBrute.this.words.size() * GuiHttpGetBrute.this.urlList.size());
            }
            
            private boolean pageContainsContext(final String url) throws Exception {
                final StringBuffer response = new StringBuffer();
                final URL obj = new URL(url);
                if (url.startsWith("https://")) {
                    final HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                else {
                    final HttpURLConnection con2 = (HttpURLConnection)obj.openConnection();
                    con2.setRequestMethod("GET");
                    con2.setRequestProperty("User-Agent", "Mozilla/5.0");
                    final BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                return response.toString().contains(GuiHttpGetBrute.this.jTextField6.getText());
            }
            
            private boolean pageExist(final String url) throws Exception {
                int responseCode = 0;
                final URL obj = new URL(url);
                if (url.startsWith("https://")) {
                    final HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con.connect();
                    responseCode = con.getResponseCode();
                }
                else {
                    final HttpURLConnection con2 = (HttpURLConnection)obj.openConnection();
                    con2.setRequestMethod("GET");
                    con2.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con2.connect();
                    responseCode = con2.getResponseCode();
                }
                final String[] codes = GuiHttpGetBrute.this.jTextField4.getText().replace(" ", "").split(",");
                return Arrays.asList(codes).contains(String.valueOf(responseCode));
            }
        }.start();
    }
    
    private void jButton3ActionPerformed(final ActionEvent evt) {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        final int returnValue = jfc.showOpenDialog(null);
        if (returnValue == 0) {
            final File selectedFile = jfc.getSelectedFile();
            this.jTextField1.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void jButton2ActionPerformed(final ActionEvent evt) {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        final int returnValue = jfc.showOpenDialog(null);
        if (returnValue == 0) {
            final File selectedFile = jfc.getSelectedFile();
            this.jTextField2.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void jTextField6ActionPerformed(final ActionEvent evt) {
    }
    
    private void jComboBox1ActionPerformed(final ActionEvent evt) {
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
            Logger.getLogger(GuiHttpGetBrute.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(GuiHttpGetBrute.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(GuiHttpGetBrute.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(GuiHttpGetBrute.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiHttpGetBrute().setVisible(true);
            }
        });
    }
}
