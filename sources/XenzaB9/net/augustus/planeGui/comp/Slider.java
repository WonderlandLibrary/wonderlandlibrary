// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.planeGui.comp;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;
import net.augustus.planeGui.Clickgui;

public class Slider extends Comp
{
    private boolean dragging;
    private double renderWidth;
    private double renderWidth2;
    
    public Slider(final double x, final double y, final Clickgui parent, final Module module, final DoubleValue setting) {
        this.dragging = false;
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isInside(mouseX, mouseY, this.parent.posX + this.x - 70.0, this.parent.posY + this.y + 10.0, this.parent.posX + this.x - 70.0 + this.renderWidth2, this.parent.posY + this.y + 20.0) && mouseButton == 0) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        final double min = ((DoubleValue)this.setting).getMinValue();
        final double max = ((DoubleValue)this.setting).getMaxValue();
        final double l = 90.0;
        this.renderWidth = l * (((DoubleValue)this.setting).getValue() - min) / (max - min);
        this.renderWidth2 = l * (((DoubleValue)this.setting).getMaxValue() - min) / (max - min);
        final double diff = Math.min(l, Math.max(0.0, mouseX - (this.parent.posX + this.x - 70.0)));
        if (this.dragging) {
            if (diff == 0.0) {
                ((DoubleValue)this.setting).setValue(((DoubleValue)this.setting).getMinValue());
            }
            else {
                final double newValue = this.roundToPlace(diff / l * (max - min) + min, 1);
                ((DoubleValue)this.setting).setValue(newValue);
            }
        }
        Gui.drawRect((int)(this.parent.posX + this.x - 70.0), (int)(this.parent.posY + this.y + 10.0), (int)(this.parent.posX + this.x - 70.0 + this.renderWidth2), (int)(this.parent.posY + this.y + 20.0), new Color(230, 10, 230).darker().getRGB());
        Gui.drawRect((int)(this.parent.posX + this.x - 70.0), (int)(this.parent.posY + this.y + 10.0), (int)(this.parent.posX + this.x - 70.0 + this.renderWidth), (int)(this.parent.posY + this.y + 20.0), new Color(230, 10, 230).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(this.setting.getName() + ": " + ((DoubleValue)this.setting).getValue(), (int)(this.parent.posX + this.x - 70.0), (int)(this.parent.posY + this.y), -1);
    }
    
    private double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
