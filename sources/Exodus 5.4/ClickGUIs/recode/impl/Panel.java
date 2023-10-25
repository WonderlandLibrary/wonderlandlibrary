/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl;

import ClickGUIs.recode.Recode;
import ClickGUIs.recode.impl.Button;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Panel {
    public Recode clickGUI;
    private double offsetX = 0.0;
    public double width = 100.0;
    public double height = 14.5;
    private double offsetY = 0.0;
    public boolean dragging;
    public ArrayList<Button> buttons = new ArrayList();
    public boolean open = true;
    public Category category;
    public double y = 5.0;
    public double x;

    public void keyTyped(char c, int n) {
        if (this.open) {
            for (Button button : this.buttons) {
                button.keyTyped(c, n);
            }
        }
    }

    public void mouseClicked(int n, int n2, int n3) throws IOException {
        if (this.isHovered(n, n2)) {
            switch (n3) {
                case 0: {
                    this.dragging = true;
                    this.offsetX = (double)n - this.x;
                    this.offsetY = (double)n2 - this.y;
                    break;
                }
                case 1: {
                    boolean bl = this.open = !this.open;
                }
            }
        }
        if (this.open) {
            for (Button button : this.buttons) {
                button.mouseClicked(n, n2, n3);
            }
        }
    }

    public void mouseReleased(int n, int n2, int n3) {
        if (this.open) {
            for (Button button : this.buttons) {
                button.mouseReleased(n, n2, n3);
            }
        }
        this.dragging = false;
    }

    public void drawScreen(int n, int n2, float f) {
        if (this.dragging) {
            this.x = (double)n - this.offsetX;
            this.y = (double)n2 - this.offsetY;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Recode.getMainColor().getRGB());
        FontUtil.normal.drawString(this.category.name, (float)(this.x + 5.0), (float)(this.y + (this.height - (double)FontUtil.normal.getHeight()) / 2.0), -1);
        if (this.open) {
            int n3 = 0;
            double d = 0.0;
            for (Button button : this.buttons) {
                if (n3 > 0) {
                    d += this.buttons.get((int)(n3 - 1)).setHeight;
                }
                Gui.drawRect(this.x, button.y + this.y + d, this.x + this.width, button.y + this.y + button.height + 1.0 + d, Recode.getMainColor().getRGB());
                button.drawScreen(n, n2, f, d);
                ++n3;
            }
        }
    }

    public boolean isHovered(int n, int n2) {
        return (double)n > this.x && (double)n < this.x + this.width && (double)n2 > this.y && (double)n2 < this.y + this.height;
    }

    public Panel(int n, Category category, Recode recode) {
        this.x = n;
        this.clickGUI = recode;
        this.category = category;
        int n2 = 0;
        for (Module module : Exodus.INSTANCE.getModuleManager().getModulesByCategory(category)) {
            this.buttons.add(new Button(this.height + (double)(n2 * 15), module, this));
            ++n2;
        }
    }
}

