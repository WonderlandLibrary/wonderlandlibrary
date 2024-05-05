package slavikcodd3r.rainbow.gui.hacktools;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GuiOfflineCheckerDB extends JFrame
{
    private JButton jButton1;
    private JCheckBox jCheckBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextPane jTextPane1;
    private JTextPane jTextPane2;
    private TextArea textArea1;
    
    public GuiOfflineCheckerDB() {
        this.initComponents();
    }
    
    private void initComponents() {
        this.jTextField1 = new JTextField();
        this.jLabel1 = new JLabel();
        this.jTextField2 = new JTextField();
        this.jLabel2 = new JLabel();
        this.textArea1 = new TextArea();
        this.jButton1 = new JButton();
        this.jLabel3 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.jTextPane1 = new JTextPane();
        this.jTextField3 = new JTextField();
        this.jLabel4 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jCheckBox1 = new JCheckBox();
        this.jLabel6 = new JLabel();
        this.jScrollPane2 = new JScrollPane();
        this.jTextPane2 = new JTextPane();
        this.setTitle("CheckedDB");
        this.jTextField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiOfflineCheckerDB.this.jTextField1ActionPerformed(evt);
            }
        });
        this.jLabel1.setText("Path to list of databases");
        this.jTextField2.setToolTipText("");
        this.jTextField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiOfflineCheckerDB.this.jTextField2ActionPerformed(evt);
            }
        });
        this.jLabel2.setText("What search?");
        this.jButton1.setText("Search");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiOfflineCheckerDB.this.jButton1ActionPerformed(evt);
            }
        });
        this.jLabel3.setText("Current file:");
        this.jScrollPane1.setViewportView(this.jTextPane1);
        this.jTextField3.setToolTipText("");
        this.jTextField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                GuiOfflineCheckerDB.this.jTextField3ActionPerformed(evt);
            }
        });
        this.jLabel4.setText("Path to list of words");
        this.jLabel5.setText("for search");
        this.jCheckBox1.setText("Search of list");
        this.jLabel6.setText("Current nick:");
        this.jScrollPane2.setViewportView(this.jTextPane2);
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.textArea1, -1, -1, 32767).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.jLabel4).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel1).addComponent(this.jTextField1, -2, 142, -2)).addGap(53, 53, 53).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jTextField2, -2, 132, -2)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(this.jTextField3, -2, 124, -2)).addGroup(layout.createSequentialGroup().addGap(11, 11, 11).addComponent(this.jLabel5)))).addGroup(layout.createSequentialGroup().addComponent(this.jLabel3).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -2, 132, -2).addGap(18, 18, 18).addComponent(this.jLabel6).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane2, -2, 132, -2))).addGap(11, 11, 11))).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton1, -1, -1, 32767).addGroup(layout.createSequentialGroup().addComponent(this.jCheckBox1).addGap(0, 71, 32767))))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(15, 15, 15).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jLabel2).addComponent(this.jLabel5))).addComponent(this.jLabel4)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jTextField1, -2, -1, -2).addComponent(this.jTextField2, -2, -1, -2).addComponent(this.jTextField3, -2, -1, -2))).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(this.jButton1, -2, 49, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, -2, -1, -2).addComponent(this.jLabel3, -2, 14, -2).addComponent(this.jCheckBox1).addComponent(this.jScrollPane2, -2, -1, -2).addComponent(this.jLabel6, -2, 14, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 25, 32767).addComponent(this.textArea1, -2, 367, -2)));
        this.pack();
    }
    
    private void jTextField1ActionPerformed(final ActionEvent evt) {
    }
    
    private void jTextField2ActionPerformed(final ActionEvent evt) {
    }
    
    public void findFile(final File file) {
        final File[] list = file.listFiles();
        if (list != null) {
            File[] array;
            for (int length = (array = list).length, i = 0; i < length; ++i) {
                final File fil = array[i];
                if (fil.isDirectory()) {
                    this.findFile(fil);
                }
                else {
                    try {
                        this.jTextPane1.setText(fil.getName());
                        this.findTextInFile(fil);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.jTextPane1.setText("Finish!");
                    this.jTextPane2.setText("Finish!");
                }
            }
        }
    }
    
    void findTextInFile(final File file) throws FileNotFoundException, IOException {
        if (this.jCheckBox1.isSelected()) {
            final Scanner txtscan = new Scanner(new File(this.jTextField3.getText()));
            while (txtscan.hasNextLine()) {
                final String str = txtscan.nextLine();
                this.jTextPane2.setText(str);
                final Scanner txtscan2 = new Scanner(file);
                while (txtscan2.hasNextLine()) {
                    final String str2 = txtscan2.nextLine();
                    if (str2.contains(str)) {
                        this.textArea1.append(String.valueOf(str) + " ||||||||| " + file.getName() + " ||||||||| " + str2 + "\n");
                    }
                }
            }
        }
        else {
            final Scanner txtscan = new Scanner(file);
            final String words = this.jTextField2.getText();
            while (txtscan.hasNextLine()) {
                final String str3 = txtscan.nextLine();
                if (str3.contains(words)) {
                    this.textArea1.append(String.valueOf(this.jTextField2.getText()) + " ||||||||| " + file.getName() + " ||||||||| " + str3 + "\n");
                }
            }
        }
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        new Thread() {
            @Override
            public void run() {
                GuiOfflineCheckerDB.this.findFile(new File(GuiOfflineCheckerDB.this.jTextField1.getText()));
            }
        }.start();
    }
    
    private void jTextField3ActionPerformed(final ActionEvent evt) {
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
            Logger.getLogger(GuiOfflineCheckerDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(GuiOfflineCheckerDB.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(GuiOfflineCheckerDB.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(GuiOfflineCheckerDB.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiOfflineCheckerDB().setVisible(true);
            }
        });
    }
}
