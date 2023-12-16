// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import net.minecraft.client.gui.Gui;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.function.Consumer;
import moonsense.features.SCModule;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementModuleSettings2 extends Element
{
    private final ResourceLocation ICON;
    private final int textureIndex;
    private final SCModule module;
    private int fadeIcon;
    
    public ElementModuleSettings2(final int x, final int y, final int width, final int height, final String iconLocation, final int textureIndex, final boolean shouldScissor, final SCModule module, final Consumer<Element> consumer) {
        super(x, y, width, height, true, consumer);
        this.textureIndex = textureIndex;
        this.ICON = new ResourceLocation("streamlined/" + iconLocation);
        this.module = module;
        this.fadeIcon = 175;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        this.mc.getTextureManager().bindTexture(this.ICON);
        final int b = this.textureIndex;
        GlStateManager.enableBlend();
        GuiUtils.setGlColor(new Color(0, 0, 0, 75).getRGB());
        GuiUtils.drawScaledCustomSizeModalRect(this.getX() + (this.width - b) / 2.0f + 0.75f, this.getY() + (this.height - 2 * b) / 2.0f + 7.0f + 0.75f, 0.0f, 0.0f, b, b, b, b, (float)b, (float)b);
        GuiUtils.setGlColor(this.hovered ? new Color(255, 255, 255, this.fadeIcon).getRGB() : new Color(120, 120, 120, this.fadeIcon).getRGB());
        Gui.drawScaledCustomSizeModalRect(this.getX() + (this.width - b) / 2, this.getY() + (this.height - 2 * b) / 2 + 7, 0.0f, 0.0f, b, b, b, b, (float)b, (float)b);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public void update() {
        if (this.hovered && this.fadeIcon + 5 <= 255) {
            this.fadeIcon += 5;
        }
        else if (!this.hovered && this.fadeIcon - 5 >= 175) {
            this.fadeIcon -= 5;
        }
    }
}
