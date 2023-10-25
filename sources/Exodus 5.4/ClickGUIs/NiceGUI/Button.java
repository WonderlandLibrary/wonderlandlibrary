/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.NiceGUI;

import ClickGUIs.NiceGUI.SettingsWindow;
import java.awt.Color;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.RenderingUtil;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;

public class Button {
    Category category;
    int x;
    Module mod;
    int y;
    Minecraft mc = Minecraft.getMinecraft();
    public boolean open = false;
    int mouseTicks = 0;
    public SettingsWindow settingsWindow = null;
    int lastSlide = 0;
    int last = 500;
    int slide;
    public int start = 0;

    public void setLast(int n) {
        this.last = n;
    }

    public boolean isWithinButton(int n, int n2) {
        return n > this.x && n < this.x + 550 && n2 > this.y && n2 < this.y + 35;
    }

    public void draw(int n, int n2) {
        if (this.isWithinButton(n, n2)) {
            if (this.mouseTicks < 2) {
                ++this.mouseTicks;
            }
        } else if (this.mouseTicks > 0) {
            --this.mouseTicks;
        }
        int n3 = this.mod.isToggled() ? 518 : 500;
        float f = n3 - this.last;
        this.last = (int)((float)this.last + f / 4.0f);
        int n4 = 0;
        while (n4 < 15) {
            RenderingUtil.drawFilledCircle(this.x + 500 + n4, this.y + 17, 4.0f, this.mod.isToggled() ? new Color(0, 255, 0, 255) : new Color(150, 150, 150, 255));
            ++n4;
        }
        RenderingUtil.drawFilledCircle(this.x + this.last, this.y + 17, 6.0f, new Color(255, 255, 255, 255));
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setMc(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public int getLast() {
        return this.last;
    }

    public Button(Category category, int n, int n2, Module module) {
        this.category = category;
        this.x = n;
        this.y = n2;
        this.start = n2;
        this.mod = module;
    }

    public int getX() {
        return this.x;
    }

    public void drawString(FontUtil fontUtil) {
        FontUtil.normal.drawString(this.mod.getName(), this.x + 20, this.y + 17 - FontUtil.normal.getHeight() / 2 - 2, -1);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setOpen(boolean bl) {
        this.open = bl;
    }

    public void setMouseTicks(int n) {
        this.mouseTicks = n;
    }

    public Minecraft getMc() {
        return this.mc;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setStart(int n) {
        this.start = n;
    }

    public Module getMod() {
        return this.mod;
    }

    public void setMod(Module module) {
        this.mod = module;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getMouseTicks() {
        return this.mouseTicks;
    }

    public void setX(int n) {
        this.x = n;
    }

    public int getStart() {
        return this.start;
    }

    public void mouseClicked(int n, int n2, int n3) {
        if (this.isWithinButton(n, n2)) {
            if (n3 == 0) {
                if (!this.open) {
                    this.mod.toggle();
                }
            } else {
                this.settingsWindow = new SettingsWindow(this);
            }
        }
    }

    public int getY() {
        return this.y;
    }
}

