/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.other;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import me.wintware.client.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class ChangeLogUtils {
    private static final List<ChangeLogEntry> ENTRIES;
    public static Minecraft mc;

    public static void drawChangeLog() {
        ScaledResolution sr = new ScaledResolution(mc);
        Minecraft.getMinecraft().arraylist.drawStringWithShadow(Main.build + " Build", sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(Main.build + " Build") - 4, 7.0f, -1);
        Minecraft.getMinecraft().arraylist.drawStringWithShadow("Change Log", sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth("Change Log") - 4, 20.0f, -1);
        int y = 13;
        for (ChangeLogEntry entry : ENTRIES) {
            Minecraft.getMinecraft().arraylist.drawStringWithShadow(entry.type.prefix + " \u00a7f" + entry.text, sr.getScaledWidth() - Minecraft.getMinecraft().arraylist.getStringWidth(entry.type.prefix + " \u00a7f" + entry.text) - 4, y + 20, entry.type.color);
            y += 11;
        }
    }

    static {
        mc = Minecraft.getMinecraft();
        ENTRIES = Arrays.asList(new ChangeLogEntry("Fixed crash when loading the world", ChangeType.FIX));
    }

    private static class ChangeLogEntry {
        private final String text;
        private final ChangeType type;

        public ChangeLogEntry(String text, ChangeType type) {
            this.text = text;
            this.type = type;
        }
    }

    private enum ChangeType {
        ADD("[+] ", -16711936),
        FIX("[?] ", -256),
        REMOVE("[-] ", Color.red.getRGB());

        private final String prefix;
        private final int color;

        ChangeType(String prefix, int color) {
            this.prefix = prefix;
            this.color = color;
        }
    }
}

