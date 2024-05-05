package slavikcodd3r.rainbow.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class PushUtils {
	
	public static boolean sendPush(String title, String message) throws AWTException {
        if (SystemTray.isSupported()) {
        	PushUtils td = new PushUtils();
            td.displayTray(title, message);
            return true;
        } else {
            return false;
        }
    }

    public void displayTray(String title, String message) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(title, message, MessageType.INFO);
    }

}
