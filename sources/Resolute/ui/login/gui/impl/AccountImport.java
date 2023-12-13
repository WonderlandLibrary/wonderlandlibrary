// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.gui.impl;

import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import javax.swing.JOptionPane;
import vip.Resolute.ui.login.system.Account;
import vip.Resolute.Resolute;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AccountImport extends JPanel implements ActionListener
{
    public JButton openButton;
    private JFileChooser fc;
    
    public AccountImport() {
        (this.fc = new JFileChooser()).setFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
        (this.openButton = new JButton("Open a File...")).addActionListener(this);
        this.add(this.openButton);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.openButton) {
            final int returnVal = this.fc.showOpenDialog(this);
            if (returnVal == 0) {
                try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fc.getSelectedFile()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        final String[] arguments = line.split(":");
                        for (int i = 0; i < 2; ++i) {
                            arguments[i].replace(" ", "");
                        }
                        Resolute.getAccountManager().getAccounts().add(new Account(arguments[0], arguments[1], ""));
                    }
                    Resolute.getAccountManager().save();
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "An error happened.", "ERROR", 1);
                    Resolute.getNotificationManager().add(new Notification("Error", "An error happened", 5000L, NotificationType.ERROR));
                }
            }
        }
    }
}
