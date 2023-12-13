// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements;

import java.io.IOException;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.xulu.newgui.util.FontUtil;
import com.elementars.eclient.module.Category;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.render.NewGui;
import java.awt.Color;
import dev.xulu.newgui.util.ColorUtil;
import java.util.Iterator;
import dev.xulu.newgui.elements.menu.ElementTextBox;
import dev.xulu.newgui.elements.menu.ElementKeyBind;
import dev.xulu.newgui.elements.menu.ElementComboBoxEnum;
import dev.xulu.newgui.elements.menu.ElementComboBox;
import dev.xulu.newgui.elements.menu.ElementSlider;
import dev.xulu.newgui.elements.menu.ElementCheckBox;
import dev.xulu.settings.Value;
import com.elementars.eclient.Xulu;
import net.minecraft.client.Minecraft;
import dev.xulu.newgui.Panel;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class ModuleButton
{
    public Module mod;
    public ArrayList<Element> menuelements;
    public Panel parent;
    public double x;
    public double y;
    public double width;
    public double height;
    public double height2;
    public boolean extended;
    public boolean listening;
    
    public ModuleButton(final Module imod, final Panel pl) {
        this.extended = false;
        this.listening = false;
        this.mod = imod;
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2;
        this.height2 = Xulu.cFontRenderer.getHeight() + 2.0f;
        this.parent = pl;
        this.menuelements = new ArrayList<Element>();
        if (Xulu.VALUE_MANAGER.getSettingsByMod(imod) != null) {
            for (final Value s : Xulu.VALUE_MANAGER.getSettingsByMod(imod)) {
                if (s.isToggle()) {
                    this.menuelements.add(new ElementCheckBox(this, s));
                }
                else if (s.isNumber()) {
                    this.menuelements.add(new ElementSlider(this, s));
                }
                else if (s.isMode()) {
                    this.menuelements.add(new ElementComboBox(this, s));
                }
                else if (s.isEnum()) {
                    this.menuelements.add(new ElementComboBoxEnum(this, s));
                }
                else if (s.isBind() && !(this.mod instanceof com.elementars.eclient.guirewrite.Element)) {
                    this.menuelements.add(new ElementKeyBind(this, s));
                }
                else {
                    if (!s.isText()) {
                        continue;
                    }
                    this.menuelements.add(new ElementTextBox(this, s));
                }
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
        if (NewGui.rainbowgui.getValue()) {
            color = ColorUtils.changeAlpha(this.parent.rgb, 200);
        }
        int textcolor = -5263441;
        if (this.mod.isToggled() && !NewGui.moduleSetting.getValue().equalsIgnoreCase("Text")) {
            if (NewGui.moduleSetting.getValue().equalsIgnoreCase("MiniButton")) {
                Gui.drawRect((int)this.x, (int)this.y + 1, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(color, 100));
                textcolor = -1052689;
            }
            else {
                Gui.drawRect((int)this.x - 2, (int)this.y, (int)(this.x + this.width + 2.0), (int)(this.y + this.height + 1.0), color);
                textcolor = -1052689;
            }
        }
        if (this.isHovered(mouseX, mouseY) && !NewGui.moduleSetting.getValue().equalsIgnoreCase("Text")) {
            if (NewGui.moduleSetting.getValue().equalsIgnoreCase("MiniButton")) {
                Gui.drawRect((int)this.x, (int)this.y + 1, (int)(this.x + this.width), (int)(this.y + this.height), (this.mod.isToggled() && !this.mod.getCategory().equals(Category.HUD)) ? ColorUtils.changeAlpha(1427181841, 30) : ColorUtils.changeAlpha(ColorUtils.Colors.GRAY, 30));
            }
            else {
                Gui.drawRect((int)(this.x - 2.0), (int)this.y, (int)(this.x + this.width + 2.0), (int)(this.y + this.height + 1.0), (this.mod.isToggled() && !this.mod.getCategory().equals(Category.HUD)) ? ColorUtils.changeAlpha(1427181841, 30) : ColorUtils.changeAlpha(ColorUtils.Colors.GRAY, 30));
            }
        }
        if (NewGui.moduleSetting.getValue().equalsIgnoreCase("MiniButton")) {
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(this.mod.getName(), (float)(this.x + 2.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), textcolor);
                if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                    Xulu.cFontRenderer.drawStringWithShadow(this.extended ? "." : "...", (float)(this.x + this.width - 10.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), textcolor);
                }
            }
            else {
                FontUtil.drawStringWithShadow(this.mod.getName(), this.x + 2.0, this.y + 1.0 + this.height / 2.0 - 4.0, textcolor);
                if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                    FontUtil.drawStringWithShadow(this.extended ? "." : "...", this.x + this.width - 7.0, this.y + 1.0 + this.height / 2.0 - 4.0, textcolor);
                }
            }
        }
        else if (NewGui.moduleSetting.getValue().equalsIgnoreCase("Text")) {
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow((this.isHovered(mouseX, mouseY) ? ChatFormatting.BOLD : "") + this.mod.getName(), (float)(this.x + 2.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), (this.mod.isToggled() && !this.mod.getCategory().equals(Category.HUD)) ? ColorUtil.getClickGUIColor().getRGB() : textcolor);
                if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                    Xulu.cFontRenderer.drawStringWithShadow(this.extended ? ">" : "V", (float)(this.x + this.width - 6.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), textcolor);
                }
            }
            else {
                FontUtil.drawStringWithShadow((this.isHovered(mouseX, mouseY) ? ChatFormatting.BOLD : "") + this.mod.getName(), this.x + 2.0, this.y + 1.0 + this.height / 2.0 - 4.0, (this.mod.isToggled() && !this.mod.getCategory().equals(Category.HUD)) ? ColorUtil.getClickGUIColor().getRGB() : textcolor);
                if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                    FontUtil.drawStringWithShadow(this.extended ? ">" : "V", this.x + this.width - 5.0, this.y + 1.0 + this.height / 2.0 - 4.0, textcolor);
                }
            }
        }
        else if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(this.mod.getName(), (float)(this.x + 2.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), textcolor);
            if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                Xulu.cFontRenderer.drawStringWithShadow(this.extended ? ">" : "V", (float)(this.x + this.width - 6.0), (float)(this.y + 1.0 + this.height2 / 2.0 - 4.0), textcolor);
            }
        }
        else {
            FontUtil.drawStringWithShadow(this.mod.getName(), this.x + 2.0, this.y + 1.0 + this.height / 2.0 - 4.0, textcolor);
            if (Xulu.VALUE_MANAGER.getValuesByMod(this.mod) != null) {
                FontUtil.drawStringWithShadow(this.extended ? ">" : "V", this.x + this.width - 5.0, this.y + 1.0 + this.height / 2.0 - 4.0, textcolor);
            }
        }
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.isHovered(mouseX, mouseY)) {
            return false;
        }
        if (mouseButton == 0) {
            this.mod.toggle();
        }
        else if (mouseButton == 1 && this.menuelements != null && this.menuelements.size() > 0) {
            final boolean b = !this.extended;
            Xulu.newGUI.closeAllSettings();
            this.extended = b;
        }
        return true;
    }
    
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final Element e : this.menuelements) {
            if (!e.set.isVisible()) {
                continue;
            }
            if (e.keyTyped(typedChar, keyCode)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
