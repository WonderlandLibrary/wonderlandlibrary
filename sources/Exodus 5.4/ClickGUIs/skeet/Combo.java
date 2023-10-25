/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.skeet;

import ClickGUIs.skeet.Comp;
import ClickGUIs.skeet.Skeet;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Combo
extends Comp {
    public Combo(double d, double d2, Skeet skeet, Module module, Setting setting) {
        this.x = d;
        this.y = d2;
        this.parent = skeet;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int n, int n2) {
        super.drawScreen(n, n2);
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
        Gui.drawRect(this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x, this.parent.posY + this.y + 10.0, this.setting.getValBoolean() ? new Color(230, 10, 230).getRGB() : new Color(0, 0, 0).getRGB());
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawString(String.valueOf(this.setting.getName()) + ": " + this.setting.getValString(), (int)(this.parent.posX + this.x - 69.0), (int)(this.parent.posY + this.y + 1.0), new Color(200, 200, 200).getRGB());
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

