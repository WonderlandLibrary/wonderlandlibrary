// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import java.awt.Color;
import moonsense.features.modules.type.hud.CrosshairModule;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import moonsense.animations.Easing;
import moonsense.features.SettingsManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import moonsense.animations.Animation;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementCrosshairPreview extends Element
{
    public static final ResourceLocation[] previews;
    private int lastMouseX;
    private int lastMouseY;
    private Animation smoothAnimation;
    
    static {
        previews = new ResourceLocation[] { new ResourceLocation("streamlined/preview 1.png"), new ResourceLocation("streamlined/preview 4.png"), new ResourceLocation("streamlined/preview 2.png"), new ResourceLocation("streamlined/preview 3.png") };
    }
    
    public ElementCrosshairPreview(final int x, final int y, final int width, final int height, final boolean shouldScissor, final AbstractGuiScreen parent) {
        super(x, y, width, height, shouldScissor, null, parent);
        this.smoothAnimation = null;
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseX < this.x + this.width - 85.0f || mouseX > this.x + this.width) {
            return false;
        }
        if (mouseY < this.y || mouseY > this.y + this.height) {
            return false;
        }
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
        SettingsManager.INSTANCE.previewBG.setValue((int)Math.floor((mouseY - this.y) * 4.0f / this.height));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.smoothAnimation == null) {
            this.smoothAnimation = new Animation(1.0f, 1.0f, 0.001f, Easing.EASE_IN_OUT_SINE);
        }
        final int index = SettingsManager.INSTANCE.previewBG.getInt();
        final int n = Math.min(3, Math.max(0, index));
        GuiUtils.setGlColor(-1);
        final int f3 = this.width - 85;
        int offX = 0;
        int offY = 0;
        if (this.hovered) {
            offX = -(this.getX() + this.width / 2 - this.mouseX);
            offX /= 2;
            offY = -(this.getY() + this.height / 2 - this.mouseY);
            offY /= 2;
        }
        GuiUtils.drawImage(ElementCrosshairPreview.previews[n], this.x, this.y, f3, this.height);
        for (int i = 0; i < 4; ++i) {
            GuiUtils.drawImage(ElementCrosshairPreview.previews[i], this.x + this.width - 85.0f, this.y + i * (float)this.height / 4.0f, 85.0f, this.height / 4.0f);
            if (n == i) {
                GuiUtils.drawRoundedOutline((float)(this.x + f3), this.y + i * (float)this.height / 4.0f, (float)(this.x + this.width), this.y + (i + 1) * (float)this.height / 4.0f, 0.1f, 2.0f, MoonsenseClient.getMainColor(255));
            }
            GuiUtils.setGlColor(-1);
        }
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        CrosshairModule.INSTANCE.render(this.mc.ingameGUI, this.x + f3 / 2, this.y + this.height / 2, this.x + f3 / 2, this.y + this.height / 2);
        GuiUtils.drawRoundedOutline(this.x - 0.8f, this.y - 0.8f, this.x + this.width + 0.8f, this.y + this.height + 0.8f, 0.0f, 2.0f, Color.black.getRGB());
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        super.render(this.lastMouseX = mouseX, this.lastMouseY = mouseY, partialTicks);
    }
    
    public void drawRects(final float f, final float f2, final float f3, final float f4, final float f5, final int n) {
        GuiUtils.drawRect(f - f5, f2 - f5, f3 + f5 * 2.0f, f5, n);
        GuiUtils.drawRect(f - f5, f2 + f4, f3 + f5 * 2.0f, f5, n);
        GuiUtils.drawRect(f - f5, f2, f5, f4, n);
        GuiUtils.drawRect(f + f3, f2, f5, f4, n);
    }
}
