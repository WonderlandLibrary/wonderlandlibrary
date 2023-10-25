/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package de.Hero.clickgui.elements;

import de.Hero.clickgui.Panel;
import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.menu.ElementCheckBox;
import de.Hero.clickgui.elements.menu.ElementComboBox;
import de.Hero.clickgui.elements.menu.ElementSlider;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class ModuleButton {
    public double x;
    public ArrayList<Element> menuelements;
    public double width;
    public Module mod;
    public double height;
    public boolean extended = false;
    public Panel parent;
    public double y;
    public boolean listening = false;

    public boolean isHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= this.y && (double)n2 <= this.y + this.height;
    }

    public ModuleButton(Module module, Panel panel) {
        this.mod = module;
        Minecraft.getMinecraft();
        this.height = Minecraft.fontRendererObj.FONT_HEIGHT + 2;
        this.parent = panel;
        this.menuelements = new ArrayList();
        if (Exodus.INSTANCE.settingsManager.getSettingsByMod(module) != null) {
            for (Setting setting : Exodus.INSTANCE.settingsManager.getSettingsByMod(module)) {
                if (setting.isCheck()) {
                    this.menuelements.add(new ElementCheckBox(this, setting));
                    continue;
                }
                if (setting.isSlider()) {
                    this.menuelements.add(new ElementSlider(this, setting));
                    continue;
                }
                if (!setting.isCombo()) continue;
                this.menuelements.add(new ElementComboBox(this, setting));
            }
        }
    }

    public boolean mouseClicked(int n, int n2, int n3) {
        if (!this.isHovered(n, n2)) {
            return false;
        }
        if (n3 == 0) {
            this.mod.toggle();
            if (Exodus.INSTANCE.settingsManager.getSettingByName("Sound").getValBoolean()) {
                Minecraft.getMinecraft();
                Minecraft.thePlayer.playSound("random.click", 0.5f, 0.5f);
            }
        } else if (n3 == 1) {
            if (this.menuelements != null && this.menuelements.size() > 0) {
                boolean bl = !this.extended;
                Exodus.INSTANCE.clickGui.closeAllSettings();
                this.extended = bl;
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Sound").getValBoolean()) {
                    if (this.extended) {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.out", 1.0f, 1.0f);
                    } else {
                        Minecraft.getMinecraft();
                        Minecraft.thePlayer.playSound("tile.piston.in", 1.0f, 1.0f);
                    }
                }
            }
        } else if (n3 == 2) {
            this.listening = true;
        }
        return true;
    }

    public void drawScreen(int n, int n2, float f) {
        Color color = ColorUtil.getClickGUIColor();
        int n3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150).getRGB();
        int n4 = -5263441;
        int n5 = 0;
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            if (!module.isToggled()) continue;
            ++n5;
        }
        if (this.mod.isToggled()) {
            Gui.drawRect(this.x - 2.5, this.y, this.x + this.width + 2.0, this.y + this.height + 1.0, CustomIngameGui.getColorInt((int)(this.y / 8.0)));
            n4 = -1052689;
        }
        if (this.isHovered(n, n2)) {
            Gui.drawRect(this.x - 2.0, this.y, this.x + this.width + 2.0, this.y + this.height + 1.0, 0x55111111);
        }
        FontUtil.normal.drawStringWithShadow(this.mod.getName(), this.x + this.width / 100.0 - 1.0, (float)(this.y + 1.0 + this.height / 5.0), n4);
    }

    public boolean keyTyped(char c, int n) throws IOException {
        if (this.listening) {
            if (n != 1) {
                Exodus.addChatMessage("Bound '" + this.mod.getName() + "'" + " to '" + Keyboard.getKeyName((int)n) + "'");
                this.mod.setKey(n);
            } else {
                Exodus.addChatMessage("Unbound '" + this.mod.getName() + "'");
                this.mod.setKey(0);
            }
            this.listening = false;
            return true;
        }
        return false;
    }
}

