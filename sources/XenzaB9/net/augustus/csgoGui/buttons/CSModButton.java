// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons;

import java.io.IOException;
import java.util.Iterator;
import net.augustus.csgoGui.buttons.setting.settings.CSSettingCombo;
import net.augustus.settings.StringValue;
import net.augustus.csgoGui.buttons.setting.settings.CSSettingDouble;
import net.augustus.settings.DoubleValue;
import net.augustus.csgoGui.buttons.setting.settings.CSSettingCheck;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.Setting;
import net.augustus.Augustus;
import net.augustus.csgoGui.buttons.setting.CSSetting;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class CSModButton extends CSButton
{
    public Module mod;
    public ArrayList<CSSetting> settings;
    
    public CSModButton(final int x, final int y, final int width, final int height, final int color, final String displayString, final Module mod) {
        super(x, y, width, height, color, displayString);
        this.settings = new ArrayList<CSSetting>();
        this.mod = mod;
        this.initSettings();
    }
    
    private void initSettings() {
        int y = 110;
        int x = this.x + 100;
        for (int i = 0; i < Augustus.getInstance().getSettingsManager().getStgs().size(); ++i) {
            final Setting s = Augustus.getInstance().getSettingsManager().getStgs().get(i);
            if (s.getParent() == this.mod) {
                if (s instanceof BooleanValue) {
                    final CSSettingCheck check = new CSSettingCheck(x, y, y, x, s);
                    this.settings.add(check);
                    y += 13;
                }
                if (s instanceof DoubleValue) {
                    final CSSettingDouble doubleset = new CSSettingDouble(x, y, 0, 0, s);
                    this.settings.add(doubleset);
                    y += 15;
                }
                if (s instanceof StringValue) {
                    final int yplus = y;
                    final CSSettingCombo combo = new CSSettingCombo(x, y, 70, this.mc.fontRendererObj.FONT_HEIGHT + 2, s);
                    this.settings.add(combo);
                    for (int i2 = 0; i2 < ((StringValue)s).getStringList().length; ++i2) {
                        y += this.fr.FONT_HEIGHT + 2;
                        if (y > 100 + this.width - 220) {
                            y = 0;
                            x += this.mc.fontRendererObj.getStringWidth(s.getName()) + 50;
                        }
                    }
                    y += this.fr.FONT_HEIGHT + 5;
                }
                if (y > 100 + this.width - 220) {
                    y = 0;
                    x += this.mc.fontRendererObj.getStringWidth(s.getName()) + 50;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        int color = this.isHovered(mouseX, mouseY) ? Augustus.getInstance().getClientColor().getRGB() : -1;
        if (this.mod.isToggled()) {
            color = Augustus.getInstance().getClientColor().darker().getRGB();
        }
        if (this.isCurrentMod()) {
            color = Augustus.getInstance().getClientColor().getRGB();
        }
        this.fr.drawString(this.displayString, this.x, this.y, color);
        for (final CSSetting cs : this.settings) {
            if (this.isCurrentMod()) {
                cs.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY)) {
            if (mouseButton == 0 && this.isHovered(mouseX, mouseY) && Augustus.getInstance().csgogui.currentCategory != null && Augustus.getInstance().csgogui.currentCategory.Categorys == this.mod.getCategory()) {
                this.mod.toggle();
            }
            else if (mouseButton == 1) {}
        }
        for (final CSSetting cs : this.settings) {
            if (this.isCurrentMod()) {
                cs.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        final boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
        final boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
        return hoveredx && hoveredy;
    }
    
    private boolean isCurrentMod() {
        return Augustus.getInstance().csgogui.currentCategory != null && Augustus.getInstance().csgogui.currentCategory.currentMod != null && Augustus.getInstance().csgogui.currentCategory.currentMod == this;
    }
    
    @Override
    public void initButton() {
        this.initSettings();
        super.initButton();
    }
}
