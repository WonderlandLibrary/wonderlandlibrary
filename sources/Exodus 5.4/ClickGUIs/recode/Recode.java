/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode;

import ClickGUIs.recode.impl.Panel;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.module.Category;
import net.minecraft.client.gui.GuiScreen;

public class Recode
extends GuiScreen {
    public ArrayList<Panel> panels = new ArrayList();

    public static Color getMainColor() {
        return new Color(21, 21, 21);
    }

    @Override
    protected void keyTyped(char c, int n) {
        if (n == 1) {
            this.mc.displayGuiScreen(null);
        } else {
            for (Panel panel : this.panels) {
                panel.keyTyped(c, n);
            }
        }
    }

    public static Color getThirdColor() {
        return new Color(31, 31, 31);
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        for (Panel panel : this.panels) {
            panel.mouseReleased(n, n2, n3);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        for (Panel panel : this.panels) {
            panel.drawScreen(n, n2, f);
        }
    }

    public static Color getSecondaryColor() {
        return new Color(25, 25, 25);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        for (Panel panel : this.panels) {
            panel.mouseClicked(n, n2, n3);
        }
    }

    public Recode() {
        int n = 0;
        Category[] categoryArray = Category.values();
        int n2 = categoryArray.length;
        int n3 = 0;
        while (n3 < n2) {
            Category category = categoryArray[n3];
            this.panels.add(new Panel(5 + n * 105, category, this));
            ++n;
            ++n3;
        }
    }
}

