// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class GuiButtonLanguage extends GuiButton
{
    public GuiButtonLanguage(final int buttonID, final int xPos, final int yPos) {
        super(buttonID, xPos, yPos, 20, 20, "");
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final ResourceLocation rl = new ResourceLocation("fluidclient/languageButton.png");
            mc.getTextureManager().bindTexture(rl);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = new Color(255, 255, 255, 100).getRGB();
            final int j = new Color(255, 255, 255, 50).getRGB();
            final int k = new Color(150, 150, 150, 50).getRGB();
            final int l = this.enabled ? (this.hovered ? i : j) : k;
            final int i2 = this.enabled ? (this.hovered ? new Color(255, 255, 255, 150).getRGB() : new Color(255, 255, 255, 100).getRGB()) : new Color(150, 150, 150, 100).getRGB();
            final int i3 = 106;
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, l);
            Gui.drawRect(this.xPosition, this.yPosition + this.height, this.xPosition + this.width, this.yPosition + 1 + this.height, i2);
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0.0f, 0.0f, 20, 20, 20.0f, 20.0f);
        }
    }
}
