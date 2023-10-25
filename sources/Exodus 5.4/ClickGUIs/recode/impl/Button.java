/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl;

import ClickGUIs.recode.impl.Panel;
import ClickGUIs.recode.impl.set.Checkbox;
import ClickGUIs.recode.impl.set.Mode;
import ClickGUIs.recode.impl.set.SetComp;
import ClickGUIs.recode.impl.set.Slider;
import de.Hero.settings.Setting;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Button {
    public boolean extended = false;
    public Panel panel;
    public ArrayList<SetComp> settings = new ArrayList();
    public double setHeight = 0.0;
    private double sussy = 0.0;
    public double height = 13.0;
    public Module mod;
    public double y;

    public void mouseReleased(int n, int n2, int n3) {
        for (SetComp setComp : this.settings) {
            setComp.mouseReleased(n, n2, n3);
        }
    }

    public void drawScreen(int n, int n2, float f, double d) {
        this.height = 14.0;
        Minecraft minecraft = Minecraft.getMinecraft();
        Gui.drawRect(this.panel.x + 1.0, this.panel.y + this.y + d, this.panel.x + this.panel.width - 1.0, this.panel.y + this.y + this.height + d, -1);
        FontUtil.normal.drawString(this.mod.getDisplayName(), (float)(this.panel.x + 5.0), (float)(this.panel.y + this.y + (this.height - (double)FontUtil.normal.getHeight()) / 2.0 + d), -1);
        this.setHeight = 0.0;
        this.sussy = d;
        if (this.extended) {
            int n3 = 0;
            for (SetComp setComp : this.settings) {
                this.setHeight += setComp.drawScreen(n, n2, this.panel.x, this.panel.y + this.y + this.height + d + (double)n3 * setComp.getHeight());
                ++n3;
            }
        }
    }

    public double getWidth() {
        return this.panel.width;
    }

    public void mouseClicked(int n, int n2, int n3) throws IOException {
        if (this.isHovered(n, n2)) {
            switch (n3) {
                case 0: {
                    this.mod.toggle();
                    break;
                }
                case 1: {
                    this.extended = !this.extended;
                }
            }
        }
        for (SetComp setComp : this.settings) {
            setComp.mouseClicked(n, n2, n3);
        }
    }

    public boolean isHovered(int n, int n2) {
        return (double)n > this.panel.x && (double)n < this.panel.x + this.panel.width && (double)n2 > this.panel.y + this.y + this.sussy && (double)n2 < this.panel.y + this.y + this.height + this.sussy;
    }

    public Button(double d, Module module, Panel panel) {
        this.y = d;
        this.panel = panel;
        this.mod = module;
        if (Exodus.INSTANCE.settingsManager.getSettingsByMod(module) != null) {
            for (Setting setting : Exodus.INSTANCE.getSettingsManager().getSettingsByMod(module)) {
                if (setting.isSlider()) {
                    this.settings.add(new Slider(setting, this));
                }
                if (setting.isCheck()) {
                    this.settings.add(new Checkbox(setting, this));
                }
                if (!setting.isCombo()) continue;
                this.settings.add(new Mode(setting, this));
            }
        }
    }

    public void keyTyped(char c, int n) {
    }
}

