// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons.setting.settings;

import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.augustus.settings.StringValue;
import net.augustus.settings.Setting;
import java.util.ArrayList;
import net.augustus.csgoGui.buttons.setting.CSSetting;

public class CSSettingCombo extends CSSetting
{
    public ArrayList<CSSettingComboValue> values;
    
    public CSSettingCombo(final int x, final int y, final int width, final int height, final Setting s) {
        super(x, y, width, height, s);
        this.values = new ArrayList<CSSettingComboValue>();
        this.initValues();
    }
    
    private void initValues() {
        final int x = this.x;
        int y = this.y + this.height;
        for (final String s : ((StringValue)this.set).getStringList()) {
            final CSSettingComboValue value = new CSSettingComboValue(x, y, 70, this.mc.fontRendererObj.FONT_HEIGHT + 2, this.set, s);
            this.values.add(value);
            y += this.mc.fontRendererObj.FONT_HEIGHT + 2;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
        this.fr.drawString(this.displayString, this.x + this.width / 2 - this.fr.getStringWidth(this.displayString) / 2, this.y + 1, Integer.MAX_VALUE);
        for (final CSSettingComboValue value : this.values) {
            value.drawScreen(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final CSSettingComboValue value : this.values) {
            value.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
