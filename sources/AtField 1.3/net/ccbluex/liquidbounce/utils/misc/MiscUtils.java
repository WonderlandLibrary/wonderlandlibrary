/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class MiscUtils
extends MinecraftInstance {
    public static void showErrorPopup(String string, String string2) {
        JOptionPane.showMessageDialog(null, string2, string, 0);
    }

    public static File saveFileChooser() {
        if (mc.isFullScreen()) {
            mc.toggleFullscreen();
        }
        JFileChooser jFileChooser = new JFileChooser();
        JFrame jFrame = new JFrame();
        jFileChooser.setFileSelectionMode(0);
        jFrame.setVisible(true);
        jFrame.toFront();
        jFrame.setVisible(false);
        int n = jFileChooser.showSaveDialog(jFrame);
        jFrame.dispose();
        return n == 0 ? jFileChooser.getSelectedFile() : null;
    }

    public static File openFileChooser() {
        if (mc.isFullScreen()) {
            mc.toggleFullscreen();
        }
        JFileChooser jFileChooser = new JFileChooser();
        JFrame jFrame = new JFrame();
        jFileChooser.setFileSelectionMode(0);
        jFrame.setVisible(true);
        jFrame.toFront();
        jFrame.setVisible(false);
        int n = jFileChooser.showOpenDialog(jFrame);
        jFrame.dispose();
        return n == 0 ? jFileChooser.getSelectedFile() : null;
    }

    public static void showURL(String string) {
        try {
            Desktop.getDesktop().browse(new URI(string));
        }
        catch (IOException | URISyntaxException exception) {
            exception.printStackTrace();
        }
    }
}

