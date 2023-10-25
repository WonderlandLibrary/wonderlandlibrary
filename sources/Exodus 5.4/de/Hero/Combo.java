/*
 * Decompiled with CFR 0.152.
 */
package de.Hero;

import de.Hero.Clickgui;
import de.Hero.Comp;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Combo
extends Comp {
    public Combo(double d, double d2, Clickgui clickgui, Module module, Setting setting) {
        this.x = d;
        this.y = d2;
        this.parent = clickgui;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int n, int n2) {
        super.drawScreen(n, n2);
        Gui.drawRect(this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x, this.parent.posY + this.y + 10.0, this.setting.getValBoolean() ? new Color(230, 10, 230).getRGB() : new Color(30, 30, 30).getRGB());
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawString("Mode: " + this.setting.getValString(), (int)(this.parent.posX + this.x - 69.0), (int)(this.parent.posY + this.y + 1.0), new Color(200, 200, 200).getRGB());
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isInside(n, n2, this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x, this.parent.posY + this.y + 10.0) && n3 == 0) {
            int n4 = this.setting.getOptions().size();
            this.parent.modeIndex = this.parent.modeIndex + 1 >= n4 ? 0 : ++this.parent.modeIndex;
            this.setting.setValString(this.setting.getOptions().get(this.parent.modeIndex));
        }
    }
}

