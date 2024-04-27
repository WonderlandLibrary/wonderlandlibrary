/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import javax.swing.JOptionPane;

public class NotifyUtils {
    public static void notice(String string, String string2) {
        JOptionPane.getRootFrame().setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(null, string2, string, 2);
    }

    public static String showInputDialog(String string) {
        JOptionPane.getRootFrame().setAlwaysOnTop(true);
        return JOptionPane.showInputDialog(string);
    }
}

