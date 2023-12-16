// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements;

import java.math.BigDecimal;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.utils.MathUtil;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;

public class Slider extends Element
{
    public final float min;
    public final float max;
    public final float step;
    public final String displayString;
    private boolean dragging;
    public float sliderValue;
    private final BiConsumer<Element, Float> consumer;
    
    public Slider(final int x, final int y, final int width, final int height, final float min, final float max, final float step, final float current, final String displayText, final BiConsumer<Element, Float> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, true, null, parent);
        this.displayString = displayText;
        this.consumer = consumer;
        this.min = min;
        this.max = max;
        this.step = step;
        this.sliderValue = MathUtil.normalizeValue(current, min, max, step);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)(this.getY() + 2), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 1.0f, new Color(50, 50, 50, 255).getRGB());
        GuiUtils.drawRoundedRect(this.getX() + this.sliderValue * (this.width - 8), this.getY() - 0.5f, this.getX() + this.sliderValue * (this.width - 8) + 8.0f, this.getY() + this.height + 2.5f, 4.0f, MoonsenseClient.getMainColor(255));
        float value = MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step);
        value = this.getRoundedValue(value);
        MoonsenseClient.titleRenderer.drawString(this.displayString, this.getX() - MoonsenseClient.titleRenderer.getWidth(this.displayString) - 2.0f, (float)(this.getY() - 1), 16777215);
        MoonsenseClient.titleRenderer.drawString(String.valueOf(value), this.getX() + this.width + 2, this.getY() - 1, 16777215);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered) {
            this.sliderValue = (mouseX - (this.getX() + 4.0f)) / (this.width - 8.0f);
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseDragged(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.sliderValue = (mouseX - (this.getX() + 4.0f)) / (this.width - 8.0f);
            this.sliderValue = MathUtil.normalizeValue(MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step), this.min, this.max, this.step);
            if (this.consumer != null) {
                this.consumer.accept(this, this.sliderValue);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
        if (this.consumer != null) {
            this.consumer.accept(this, this.sliderValue);
        }
    }
    
    public float getDenormalized() {
        return MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step);
    }
    
    protected float getRoundedValue(final float value) {
        return new BigDecimal(String.valueOf(value)).setScale(2, 4).floatValue();
    }
}
