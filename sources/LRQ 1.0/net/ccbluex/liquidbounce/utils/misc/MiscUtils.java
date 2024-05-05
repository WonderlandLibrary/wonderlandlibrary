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
    public static void showErrorPopup(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, 0);
    }

    public static void showURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static File openFileChooser() {
        if (mc.isFullScreen()) {
            mc.toggleFullscreen();
        }
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame();
        fileChooser.setFileSelectionMode(0);
        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);
        int action = fileChooser.showOpenDialog(frame);
        frame.dispose();
        return action == 0 ? fileChooser.getSelectedFile() : null;
    }

    public static File saveFileChooser() {
        if (mc.isFullScreen()) {
            mc.toggleFullscreen();
        }
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame();
        fileChooser.setFileSelectionMode(0);
        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);
        int action = fileChooser.showSaveDialog(frame);
        frame.dispose();
        return action == 0 ? fileChooser.getSelectedFile() : null;
    }
}

