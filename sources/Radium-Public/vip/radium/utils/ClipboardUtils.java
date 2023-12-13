package vip.radium.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class ClipboardUtils {

    private ClipboardUtils() {
    }


    public static String getClipboardContents() {
        try {
            return (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ignored) {
            return null;
        }
    }

    public static void setClipboardContents(String contents) {
        StringSelection selection = new StringSelection(contents);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, selection);
    }
}
