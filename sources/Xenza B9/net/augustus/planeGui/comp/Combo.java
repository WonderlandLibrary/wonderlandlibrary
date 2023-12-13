// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.planeGui.comp;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;
import net.augustus.planeGui.Clickgui;

public class Combo extends Comp
{
    public Combo(final double x, final double y, final Clickgui parent, final Module module, final StringValue setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isInside(mouseX, mouseY, this.parent.posX + this.x - 70.0, this.parent.posY + this.y, this.parent.posX + this.x, this.parent.posY + this.y + 10.0) && mouseButton == 0) {
            final int max = ((StringValue)this.setting).getStringList().length;
            if (this.parent.modeIndex + 1 >= max) {
                this.parent.modeIndex = 0;
            }
            else {
                final Clickgui parent = this.parent;
                ++parent.modeIndex;
            }
            ((StringValue)this.setting).setString(((StringValue)this.setting).getStringList()[this.parent.modeIndex]);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        Minecraft.getMinecraft().fontRendererObj.drawString(this.setting.getName() + ": " + ((StringValue)this.setting).getSelected(), (int)(this.parent.posX + this.x - 69.0), (int)(this.parent.posY + this.y + 1.0), new Color(200, 200, 200).getRGB());
    }
}
