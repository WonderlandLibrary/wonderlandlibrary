/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ElementComboBox
extends Element {
    public boolean isButtonHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= this.y && (double)n2 <= this.y + 15.0;
    }

    @Override
    public boolean mouseClicked(int n, int n2, int n3) {
        if (n3 == 0) {
            if (this.isButtonHovered(n, n2)) {
                this.comboextended = !this.comboextended;
                return true;
            }
            if (!this.comboextended) {
                return false;
            }
            double d = this.y + 15.0;
            for (String string : this.set.getOptions()) {
                if ((double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= d && (double)n2 <= d + (double)FontUtil.normal.getHeight() + 2.0) {
                    if (Exodus.INSTANCE.settingsManager.getSettingByName("Sound").getValBoolean()) {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.in", 20.0f, 20.0f);
                    }
                    if (this.clickgui != null && this.clickgui.setmgr != null) {
                        this.clickgui.setmgr.getSettingByName(this.set.getName()).setValString(string);
                    }
                    return true;
                }
                d += (double)(FontUtil.normal.getHeight() + 2);
            }
        }
        return super.mouseClicked(n, n2, n3);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        Color color = ColorUtil.getClickGUIColor();
        int n3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150).getRGB();
        Gui.drawRect(this.x + 2.25, this.y, this.x + this.width, this.y + this.height, new Color(64, 64, 64).darker().getRGB());
        FontUtil.normal.drawStringWithShadow(this.setstrg, this.x + this.width / 11.5, (float)(this.y + 5.0), -1);
        int n4 = CustomIngameGui.color;
        int n5 = CustomIngameGui.color;
        if (this.comboextended) {
            Gui.drawRect(this.x + 1.25, this.y + 15.0, this.x + this.width, this.y + this.height, -1441656302);
            double d = this.y + 15.0;
            for (String string : this.set.getOptions()) {
                String string2 = String.valueOf(string.substring(0, 1).toUpperCase()) + string.substring(1, string.length());
                FontUtil.normal.drawString(string2, this.x + this.width / 10.0, (float)(d + 2.0), -1);
                if (string.equalsIgnoreCase(this.set.getValString())) {
                    Gui.drawRect(this.x, d, this.x + 1.5, d + (double)FontUtil.normal.getHeight() + 2.0, n4);
                }
                if ((double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= d && (double)n2 < d + (double)FontUtil.normal.getHeight() + 2.0) {
                    Gui.drawRect(this.x + this.width - 1.2, d, this.x + this.width, d + (double)FontUtil.normal.getHeight() + 2.0, n5);
                }
                d += (double)(FontUtil.normal.getHeight() + 2);
            }
        }
    }

    public ElementComboBox(ModuleButton moduleButton, Setting setting) {
        this.parent = moduleButton;
        this.set = setting;
    }
}

