package io.github.nevalackin.client.util.misc;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public final class ClipboardUtil {

    private ClipboardUtil() {
    }

    public static void setClipboardContents(final String data) {
        final StringSelection selection = new StringSelection(data);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static String getClipboardContents() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            // TODO :: Error log
            return null;
        }
    }

}
