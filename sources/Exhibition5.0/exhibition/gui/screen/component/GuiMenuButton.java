// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.screen.component;

import exhibition.Client;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.management.ColorManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiMenuButton extends GuiButton
{
    float scale;
    float targ;
    
    public GuiMenuButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.scale = 1.0f;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition - 2 && mouseX < this.xPosition + this.width && mouseY < this.yPosition + mc.fontRendererObj.FONT_HEIGHT + 2);
            this.targ = (this.hovered ? 1.8f : 1.0f);
            final float diff = (this.targ - this.scale) * 0.6f;
            this.scale = 1.0f + diff;
            GlStateManager.pushMatrix();
            GlStateManager.scale(this.scale, this.scale, this.scale);
            this.mouseDragged(mc, mouseX, mouseY);
            final int text = this.hovered ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : -1;
            GlStateManager.pushMatrix();
            final float offset = (this.xPosition + this.width / 2) / this.scale;
            GlStateManager.translate(offset, this.yPosition / this.scale, 1.0f);
            RenderingUtil.drawFancy(-31.0, -2.0, 31.0, 14.0, Colors.getColor(21));
            RenderingUtil.drawFancy(-30.0, -1.0, 30.0, 13.0, Colors.getColor(28));
            RenderingUtil.rectangle(-29.0, -1.0f / this.scale, 31.0, -0.5f / this.scale, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255));
            Client.f.drawStringWithShadow(this.displayString, -(Client.f.getWidth(this.displayString) / 2.0f), -0.5f, text);
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }
}
