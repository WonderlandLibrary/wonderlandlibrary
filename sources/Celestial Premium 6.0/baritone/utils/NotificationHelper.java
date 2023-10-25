/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.IOException;
import org.apache.commons.lang3.SystemUtils;

public class NotificationHelper {
    private static TrayIcon trayIcon;

    public static void notify(String text, boolean error) {
        if (SystemUtils.IS_OS_WINDOWS) {
            NotificationHelper.windows(text, error);
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            NotificationHelper.mac(text);
        } else if (SystemUtils.IS_OS_LINUX) {
            NotificationHelper.linux(text);
        }
    }

    private static void windows(String text, boolean error) {
        if (SystemTray.isSupported()) {
            try {
                if (trayIcon == null) {
                    SystemTray tray = SystemTray.getSystemTray();
                    Image image = Toolkit.getDefaultToolkit().createImage("");
                    trayIcon = new TrayIcon(image, "Baritone");
                    trayIcon.setImageAutoSize(true);
                    trayIcon.setToolTip("Baritone");
                    tray.add(trayIcon);
                }
                trayIcon.displayMessage("Baritone", text, error ? TrayIcon.MessageType.ERROR : TrayIcon.MessageType.INFO);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("SystemTray is not supported");
        }
    }

    private static void mac(String text) {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        processBuilder.command("osascript", "-e", "display notification \"" + text + "\" with title \"Baritone\"");
        try {
            processBuilder.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void linux(String text) {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        processBuilder.command("notify-send", "-a", "Baritone", text);
        try {
            processBuilder.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

