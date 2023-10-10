/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.ui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import tk.rektsky.Client;
import tk.rektsky.module.Module;

public class HUD {
    private Minecraft mc = Minecraft.getMinecraft();
    private HyperiumFontRenderer fr = Client.getFont();
    private ScaledResolution sr = new ScaledResolution(this.mc);

    public static String getClassNameSpliter() {
        try {
            return (Minecraft.getMinecraft().getSession().getSessionID() + Minecraft.getMinecraft().getClass().getClassLoader().loadClass("Start").getName()).substring(0, 0);
        }
        catch (ClassNotFoundException e2) {
            return null;
        }
    }

    public void draw(GuiIngame gui) {
        this.sr = new ScaledResolution(this.mc);
        this.ArrayList(gui);
        this.Notification();
    }

    public void ArrayList(GuiIngame gui) {
    }

    public void Notification() {
    }

    public static class ModuleComparator
    implements Comparator<Module> {
        @Override
        public int compare(Module first, Module second) {
            boolean firstHasAdditionalText = false;
            if (!first.getSuffix().equals("")) {
                firstHasAdditionalText = true;
            }
            boolean secondHasAdditionalText = false;
            if (!second.getSuffix().equals("")) {
                secondHasAdditionalText = true;
            }
            String f2 = first.name + (firstHasAdditionalText ? " " + first.getSuffix() : "");
            String s2 = second.name + (secondHasAdditionalText ? " " + second.getSuffix() : "");
            if (Client.hud.fr.getWidth(f2) < Client.hud.fr.getWidth(s2)) {
                return 1;
            }
            if (Client.hud.fr.getWidth(f2) > Client.hud.fr.getWidth(s2)) {
                return -1;
            }
            return 0;
        }
    }
}

