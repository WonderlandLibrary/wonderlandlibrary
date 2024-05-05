package space.lunaclient.luna.impl.alert;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.PrintStream;

public class DisplayTray
{
  public DisplayTray() {}
  
  public static void displayTray(String sub, String msg, TrayIcon.MessageType type)
    throws AWTException
  {
    if (SystemTray.isSupported())
    {
      SystemTray tray = SystemTray.getSystemTray();
      
      Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
      TrayIcon trayIcon = new TrayIcon(image, "");
      trayIcon.setImageAutoSize(true);
      trayIcon.setToolTip("");
      tray.add(trayIcon);
      
      trayIcon.displayMessage(sub, msg, type);
    }
    else
    {
      System.out.println("Can't display notification! Not supported on this OS version!");
    }
  }
}
