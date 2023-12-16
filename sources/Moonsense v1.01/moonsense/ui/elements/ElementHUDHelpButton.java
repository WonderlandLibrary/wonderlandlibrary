// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements;

import moonsense.utils.CustomFontRenderer;
import net.minecraft.client.gui.Gui;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.function.Consumer;
import net.minecraft.util.ResourceLocation;

public class ElementHUDHelpButton extends Element
{
    protected final ResourceLocation ICON;
    private double backgroundFade;
    private String tooltipText;
    
    public ElementHUDHelpButton() {
        super(2, 0, 18, 18, true, null);
        this.ICON = new ResourceLocation("streamlined/icons/help.png");
        this.tooltipText = "HUD Config Screen Help\nTo move mods, simply click and drag them around the screen.\nTo disable snapping, press the left shift key while dragging.\nKey bindings:\n - Module movement (2px): Arrow keys\n - Module movement (1px): Left Shift + Arrow keys";
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        this.mc.getTextureManager().bindTexture(this.ICON);
        final int b = 12;
        GlStateManager.enableBlend();
        GuiUtils.setGlColor(new Color(0, 0, 0, 75).getRGB());
        GuiUtils.drawScaledCustomSizeModalRect(this.getX() + (this.width - 12) / 2.0f + 0.75f, this.getY() + (this.height - 12) / 2.0f + 0.75f, 0.0f, 0.0f, 12, 12, 12, 12, 12.0f, 12.0f);
        GuiUtils.setGlColor(this.enabled ? (this.hovered ? new Color(255, 255, 255, 255).getRGB() : MoonsenseClient.getMainColor(255)) : new Color(120, 120, 120, 255).getRGB());
        Gui.drawScaledCustomSizeModalRect(this.getX() + (this.width - 12) / 2, this.getY() + (this.height - 12) / 2, 0.0f, 0.0f, 12, 12, 12, 12, 12.0f, 12.0f);
        if (this.enabled && this.hovered) {
            final float nameInt1 = (float)this.getX();
            final int n = this.getY() - 6;
            MoonsenseClient.titleRenderer.getClass();
            GuiUtils.drawRoundedRect(nameInt1, (float)(n - 9 * 6), (float)(this.getX() + 240), (float)(this.getY() - 2), 2.0f, new Color(0, 0, 0, (int)(this.backgroundFade / 3.0)).getRGB());
            final CustomFontRenderer titleRenderer = MoonsenseClient.titleRenderer;
            final String tooltipText = this.tooltipText;
            final int x = this.getX() + 1;
            final int n2 = this.getY() - 5;
            MoonsenseClient.titleRenderer.getClass();
            titleRenderer.drawString(tooltipText, x, n2 - 9 * 6, -1);
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        if (this.backgroundFade > 0.0 && this.enabled) {
            GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 3.0f, MoonsenseClient.getMainColor((int)this.backgroundFade));
        }
    }
    
    @Override
    public void update() {
        if (this.hovered && this.backgroundFade + 12.25 <= 255.0) {
            this.backgroundFade += 12.25;
        }
        else if (!this.hovered && this.backgroundFade - 12.25 >= 0.0) {
            this.backgroundFade -= 12.25;
        }
    }
}
