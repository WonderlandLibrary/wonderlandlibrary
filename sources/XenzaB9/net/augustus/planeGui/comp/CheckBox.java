// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.planeGui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;
import net.augustus.planeGui.Clickgui;

public class CheckBox extends Comp
{
    public CheckBox(final double x, final double y, final Clickgui parent, final Module module, final BooleanValue setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Gui.drawRect(this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x + 10.0 - 70.0, this.parent.posY + this.y + 10.0, ((BooleanValue)this.setting).getBoolean() ? new Color(230, 10, 230).getRGB() : new Color(30, 30, 30).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(this.setting.getName(), (int)(this.parent.posX + this.x - 55.0), (int)(this.parent.posY + this.y + 1.0), new Color(200, 200, 200).getRGB());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isInside(mouseX, mouseY, this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x + 10.0 - 70.0, this.parent.posY + this.y + 10.0) && mouseButton == 0) {
            ((BooleanValue)this.setting).setBoolean(!((BooleanValue)this.setting).getBoolean());
        }
    }
}
