/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class SystemUtils {
    public static boolean main(String Title2, String Text2, TrayIcon.MessageType type) throws AWTException {
        if (!SystemTray.isSupported()) {
            System.err.println("KyinoClient client By: Report.");
            return false;
        }
        SystemUtils nd = new SystemUtils();
        SystemUtils.displayTray(Title2, Text2, type);
        return false;
    }

    public static void displayTray(String Title2, String Text2, TrayIcon.MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image2 = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image2, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(Title2, Text2, type);
    }
}

