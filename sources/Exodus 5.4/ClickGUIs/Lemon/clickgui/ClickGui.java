/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.Lemon.clickgui;

import ClickGUIs.Lemon.clickgui.component.Component;
import ClickGUIs.Lemon.clickgui.component.Frame;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ColorUtils;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui
extends GuiScreen {
    public static ArrayList<Frame> frames;
    public static float count;
    public static int color;

    @Override
    public void initGui() {
    }

    @Override
    protected void keyTyped(char c, int n) {
        for (Frame frame : frames) {
            if (!frame.isOpen() || n == 1 || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.keyTyped(c, n);
            }
        }
        if (n == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    public ClickGui() {
        frames = new ArrayList();
        int n = 5;
        Category[] categoryArray = Category.values();
        int n2 = categoryArray.length;
        int n3 = 0;
        while (n3 < n2) {
            Category category = categoryArray[n3];
            if (!category.name().equalsIgnoreCase("HIDDEN")) {
                for (Module module : Exodus.INSTANCE.moduleManager.getModulesByCategory(category)) {
                    count += 1.0f;
                }
                Frame object2 = new Frame(category);
                object2.setX(n);
                frames.add(object2);
                n += object2.getWidth() + 1;
            }
            ++n3;
        }
    }

    static {
        color = new Color(0, 0, 0).getRGB();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        ColorUtils.getRainbowWave(8.0f, 0.4f, 1.0f, 100L);
        this.drawDefaultBackground();
        for (Frame frame : frames) {
            frame.renderFrame(this.fontRendererObj);
            frame.updatePosition(n, n2);
            for (Component component : frame.getComponents()) {
                component.updateComponent(n, n2);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        for (Frame frame : frames) {
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseReleased(n, n2, n3);
            }
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        for (Frame frame : frames) {
            if (frame.isWithinHeader(n, n2) && n3 == 0) {
                frame.setDrag(true);
                frame.dragX = n - frame.getX();
                frame.dragY = n2 - frame.getY();
            }
            if (frame.isWithinHeader(n, n2) && n3 == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseClicked(n, n2, n3);
            }
        }
    }
}

