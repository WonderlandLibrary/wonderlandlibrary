/*
 * Decompiled with CFR 0.152.
 */
package Verify1.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class SystemUtils {
    public static boolean main(String Title2, String Text3, TrayIcon.MessageType type) throws AWTException {
        if (SystemTray.isSupported()) {
            new SystemUtils();
            SystemUtils.displayTray(Title2, Text3, type);
            return false;
        }
        System.err.println("LiquidBounce By CCBlueX");
        return false;
    }

    public static void displayTray(String Title2, String Text3, TrayIcon.MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image2 = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image2, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(Title2, Text3, type);
    }
}

