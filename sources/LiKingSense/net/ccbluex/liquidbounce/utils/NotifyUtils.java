/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import javax.swing.JOptionPane;

public class NotifyUtils {
    public static void notice(String title, String message) {
        JOptionPane.getRootFrame().setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(null, message, title, 2);
    }

    public static String showInputDialog(String message) {
        JOptionPane.getRootFrame().setAlwaysOnTop(true);
        return JOptionPane.showInputDialog(message);
    }
}

