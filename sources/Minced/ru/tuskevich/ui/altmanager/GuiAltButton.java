// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager;

import net.minecraft.client.gui.FontRenderer;
import ru.tuskevich.util.render.BlurUtility;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.util.math.MathUtility;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiAltButton extends GuiButton
{
    private int opacity;
    public float size;
    
    public GuiAltButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiAltButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.opacity = 40;
        this.size = 0.0f;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiAltButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.size = MathUtility.lerp(this.size, this.hovered ? 1.0f : 0.0f, 1.0f);
            RenderUtility.drawRound((float)this.x, (float)this.y, (float)this.width, (float)this.height, 3.0f, new Color(25, 25, 25, 200));
            this.mouseDragged(mc, mouseX, mouseY);
            final int j = 14737632;
            BlurUtility.drawShadow(this.size * 7.0f, 2.0f, () -> Fonts.MONTSERRAT16.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2 + 1.5, -1), Color.WHITE);
            Fonts.MONTSERRAT16.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2 + 1.5, -1);
        }
    }
}
