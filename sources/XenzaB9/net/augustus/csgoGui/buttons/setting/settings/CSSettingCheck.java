// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons.setting.settings;

import java.io.IOException;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.augustus.settings.Setting;
import net.augustus.csgoGui.buttons.setting.CSSetting;

public class CSSettingCheck extends CSSetting
{
    private int animation;
    
    public CSSettingCheck(final int x, final int y, final int width, final int height, final Setting s) {
        super(x, y, width, height, s);
        this.animation = 20;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.fr.drawString(this.displayString, this.x, this.y, Integer.MAX_VALUE);
        final int stringwidth = this.fr.getStringWidth(this.displayString);
        Gui.drawRect(this.x + stringwidth + 20, this.y, this.x + stringwidth + 30, this.y + 10, -16777216);
        RenderUtil.drawCircle(this.x + stringwidth + 20, this.y + 5, 5.0, -16777216);
        RenderUtil.drawCircle(this.x + stringwidth + 30, this.y + 5, 5.0, -16777216);
        RenderUtil.drawCircle(this.x + stringwidth + this.animation, this.y + 5, 5.0, -2);
        if (((BooleanValue)this.set).getBoolean()) {
            if (this.animation < 30) {
                ++this.animation;
            }
        }
        else if (this.animation > 20) {
            --this.animation;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            ((BooleanValue)this.set).setBoolean(!((BooleanValue)this.set).getBoolean());
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private boolean isHovered(final int mouseX, final int mouseY) {
        final int stringwidth = this.fr.getStringWidth(this.displayString);
        final boolean hoveredx = mouseX > this.x + stringwidth + 15 && mouseX < this.x + stringwidth + 35;
        final boolean hoveredy = mouseY > this.y && mouseY < this.y + 10;
        return hoveredx && hoveredy;
    }
}
