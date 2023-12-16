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

public class ElementButtonIcon extends Element
{
    protected final ResourceLocation ICON;
    private final int textureIndex;
    private double backgroundFade;
    private boolean hasTooltip;
    private String tooltipText;
    
    public ElementButtonIcon(final int width, final int height, final String tooltipText, final String iconLocation, final Consumer<Element> consumer) {
        this(0, 0, width, height, tooltipText, iconLocation, -1, false, consumer);
    }
    
    public ElementButtonIcon(final int x, final int y, final int width, final int height, final String tooltipText, final String iconLocation, final int textureIndex, final Consumer<Element> consumer) {
        this(x, y, width, height, tooltipText, iconLocation, textureIndex, false, consumer);
    }
    
    public ElementButtonIcon(final int x, final int y, final int width, final int height, final String tooltipText, final String iconLocation, final int textureIndex, final boolean shouldScissor, final Consumer<Element> consumer) {
        super(x, y, width, height, shouldScissor, consumer);
        this.textureIndex = textureIndex;
        this.ICON = new ResourceLocation("streamlined/" + iconLocation);
        if (tooltipText == null) {
            this.hasTooltip = false;
        }
        else {
            this.hasTooltip = true;
            this.tooltipText = tooltipText;
        }
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
        if (this.hasTooltip && this.enabled && this.hovered) {
            final float nameInt1 = this.getX() + this.width / 2 - MoonsenseClient.titleRenderer.getWidth(this.tooltipText) / 2.0f - 2.0f;
            final int n = this.getY() - 4;
            MoonsenseClient.titleRenderer.getClass();
            GuiUtils.drawRoundedRect(nameInt1, (float)(n - 9), this.getX() + this.width / 2 + MoonsenseClient.titleRenderer.getWidth(this.tooltipText) / 2.0f + 3.0f, (float)(this.getY() - 2), 2.0f, new Color(0, 0, 0, (int)(this.backgroundFade / 3.0)).getRGB());
            final CustomFontRenderer titleRenderer = MoonsenseClient.titleRenderer;
            final String tooltipText = this.tooltipText;
            final float x = (float)(this.getX() + this.width / 2);
            final int n2 = this.getY() - 3;
            MoonsenseClient.titleRenderer.getClass();
            titleRenderer.drawCenteredString(tooltipText, x, (float)(n2 - 9), -1);
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
