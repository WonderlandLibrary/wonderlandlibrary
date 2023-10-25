/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.intellij;

import ClickGUIs.intellij.Comp;
import ClickGUIs.intellij.Intellij;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;

public class CheckBox
extends Comp {
    @Override
    public void drawScreen(int n, int n2) {
        super.drawScreen(n, n2);
        FontUtil.normal.drawString("\ufffd6public boolean \ufffd5" + this.setting.getName() + "\ufffdf = " + "\ufffd6" + this.setting.getValBoolean() + "\ufffd6;", (int)(this.parent.posX + this.x - 59.0), (int)(this.parent.posY + this.y + 1.0), new Color(200, 200, 200).getRGB());
    }

    public CheckBox(double d, double d2, Intellij intellij, Module module, Setting setting) {
        this.x = d;
        this.y = d2;
        this.parent = intellij;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isInside(n, n2, this.parent.posX + this.x - 59.0, this.parent.posY + this.y, this.parent.posX + this.x + 10.0 - 59.0, this.parent.posY + this.y + 10.0) && n3 == 0) {
            this.setting.setValBoolean(!this.setting.getValBoolean());
        }
    }
}

