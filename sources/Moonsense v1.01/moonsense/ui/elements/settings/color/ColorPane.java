// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.color;

import net.minecraft.util.MathHelper;
import java.awt.Color;
import moonsense.ui.utils.GuiUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

public class ColorPane extends SettingElementPane
{
    public int saturation;
    public int value;
    
    public ColorPane(final int x, final int y, final int width, final int height) {
        super(x, y, width, height, -1, -1);
    }
    
    public void renderPane(final int hue, final int mouseX, final int mouseY) {
        this.renderPane(mouseX, mouseY);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glBegin(9);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f((float)this.getX(), (float)(this.getY() + this.height));
        GL11.glVertex2f((float)(this.getX() + this.width), (float)(this.getY() + this.height));
        GuiUtils.setGlColor(GuiUtils.hsvToRgb(hue, 100, 100), 1.0f);
        GL11.glVertex2f((float)(this.getX() + this.width), (float)this.getY());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f((float)this.getX(), (float)this.getY());
        GL11.glEnd();
        GuiUtils.drawRoundedOutline(this.getX() - 0.5f, this.getY() - 0.2f, this.getX() + this.width + 0.4f, this.getY() + this.height + 0.4f, 2.0f, 1.5f, new Color(200, 200, 200, 255).getRGB());
        GuiUtils.drawPartialCircle((float)(this.getX() + this.saturation), this.getY() + this.height - this.value * this.height / 100.0f, 2.0f, 0, 360, 0.5f, -1, true);
    }
    
    @Override
    public void dragging(final int mouseX, final int mouseY) {
        this.saturation = MathHelper.clamp_int(mouseX - this.getX(), 0, 100);
        this.value = MathHelper.clamp_int((this.height - (mouseY - this.getY())) * 100 / this.height, 0, 100);
    }
}
