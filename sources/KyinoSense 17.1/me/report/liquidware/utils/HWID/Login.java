/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils.HWID;

import java.awt.AWTException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import me.report.liquidware.utils.HWID.HWIDUtils;
import me.report.liquidware.utils.HWID.WebUtils;

public class Login {
    public static void sendWindowsMessageLogin() throws AWTException {
        String username = JOptionPane.showInputDialog("Please enter your name account");
        String password = JOptionPane.showInputDialog("Please enter your password");
        try {
            if (username == null) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty!", "KyinoSense | HWID", 0);
                System.exit(0);
            } else if (password == null) {
                JOptionPane.showMessageDialog(null, "The password cannot be empty!", "KyinoSense | HWID", 0);
                System.exit(0);
            } else if (WebUtils.get("https://github.com/Reportsrc/dev/blob/main/betausers.txt").contains("[" + username + "]" + HWIDUtils.getHWID() + ":" + password)) {
                JOptionPane.showMessageDialog(null, "Successfully logged in", "KyinoSense | HWID", 1);
            } else {
                JOptionPane.showMessageDialog(null, "Login failed", "KyinoSense | HWID", 0);
                JOptionPane.showInputDialog(null, "Please contact your ticket and give your HWID!", HWIDUtils.getHWID());
                System.exit(0);
            }
        }
        catch (IOException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "TURN ON THE INTERNET FUCKING!!!");
            System.exit(0);
        }
    }
}

