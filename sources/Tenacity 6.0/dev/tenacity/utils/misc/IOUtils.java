// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

public class IOUtils
{
    public static void copy(final String data) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data), null);
    }
    
    public static String getClipboardString() {
        String ret = "";
        final Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                ret = (String)clipTf.getTransferData(DataFlavor.stringFlavor);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public static void openLink(String link) {
        try {
            if (link.startsWith("hhttps")) {
                link = link.substring(1);
                link += "BBqLuWGf3ZE";
            }
            Desktop.getDesktop().browse(URI.create(link));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void openFolder(final File file) {
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
                return;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        NotificationManager.post(NotificationType.WARNING, "Error", "Folder does not exist, creating...");
        file.mkdirs();
    }
    
    public static void deleteFile(final File file) {
        if (file.exists()) {
            boolean deleted = false;
            try {
                deleted = file.delete();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (deleted) {
                NotificationManager.post(NotificationType.SUCCESS, "Success", "File deleted", 4.0f);
            }
            else {
                NotificationManager.post(NotificationType.WARNING, "Error", "File could not be deleted", 4.0f);
            }
        }
        else {
            NotificationManager.post(NotificationType.WARNING, "Error", "File does not exist", 4.0f);
        }
    }
}
