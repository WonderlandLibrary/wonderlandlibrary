// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import java.math.BigDecimal;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.MoonsenseClient;
import moonsense.utils.MathUtil;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;

public class SettingElementSlider extends SettingElement
{
    private final float min;
    private final float max;
    private final float step;
    private boolean dragging;
    public float sliderValue;
    private boolean focusedOnTextbox;
    private String newTBValue;
    private final SettingElementSliderAdjustButton decreaseValue;
    private final SettingElementSliderAdjustButton increaseValue;
    
    public SettingElementSlider(final int x, final int y, final int width, final int height, final float min, final float max, final float step, final float current, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, setting, consumer, parent);
        this.focusedOnTextbox = false;
        this.newTBValue = "";
        this.min = min;
        this.max = max;
        this.step = step;
        this.sliderValue = MathUtil.normalizeValue(current, min, max, step);
        this.front = true;
        this.decreaseValue = new SettingElementSliderAdjustButton(10, 10, false, this.setting, (st, elem) -> {
            this.sliderValue -= step / max;
            if (this.sliderValue < 0.0f) {
                this.sliderValue = 0.0f;
            }
            this.sliderValue = MathUtil.normalizeValue(MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step), this.min, this.max, this.step);
            return;
        });
        this.increaseValue = new SettingElementSliderAdjustButton(10, 10, true, this.setting, (st, elem) -> {
            this.sliderValue += step / max;
            if (this.sliderValue > 1.0f) {
                this.sliderValue = this.max;
            }
            this.sliderValue = MathUtil.normalizeValue(MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step), this.min, this.max, this.step);
            return;
        });
        this.decreaseValue.setX((int)(this.getX() + MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 3.0f + 7.0f));
        this.decreaseValue.setY(this.getY() - 3);
        this.increaseValue.setX((int)(this.getX() + MoonsenseClient.titleRenderer.getWidth(setting.getDescription()) + 18.0f + 7.0f + this.width));
        this.increaseValue.setY(this.getY() - 3);
        parent.elements.add(this.decreaseValue);
        parent.elements.add(this.increaseValue);
    }
    
    public void setVisibilityOfSliders(final boolean render) {
        this.decreaseValue.setVisibility(render);
        this.increaseValue.setVisibility(render);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)(this.getX() + 16), (float)(this.getY() + 1), (float)(this.getX() + 16 + this.width), (float)(this.getY() + this.height - 1), 1.0f, new Color(50, 50, 50, 255).getRGB());
        GuiUtils.drawRoundedRect(this.getX() + 16 + this.sliderValue * (this.width - 8), this.getY() - 1.5f, this.getX() + 16 + this.sliderValue * (this.width - 8) + 8.0f, this.getY() + this.height + 1.5f, 4.0f, MoonsenseClient.getMainColor(255));
        float value = MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step);
        value = this.getRoundedValue(value);
        if (this.focusedOnTextbox) {
            MoonsenseClient.titleRenderer.drawString(String.valueOf(this.newTBValue), (float)(this.getX() + this.width + 2), this.getY() - 2.5f, 16777215);
            final float left = (float)(this.getX() + this.width + 1);
            final float n = this.getY() - 2.5f;
            MoonsenseClient.titleRenderer.getClass();
            final float top = n + 9.0f + 1.0f;
            final float right = this.getX() + this.width + 4 + MoonsenseClient.titleRenderer.getWidth(String.valueOf(this.newTBValue));
            final float n2 = this.getY() - 2.5f;
            MoonsenseClient.titleRenderer.getClass();
            GuiUtils.drawRect(left, top, right, n2 + 9.0f + 2.0f, -1);
        }
        else if (this.setting.getType() == Setting.Type.INT_SLIDER) {
            MoonsenseClient.titleRenderer.drawString(new StringBuilder(String.valueOf((int)value)).toString(), (float)(this.getX() + 30 + this.width + 2), this.getY() - 2.5f, 16777215);
        }
        else {
            MoonsenseClient.titleRenderer.drawString(String.valueOf(value), (float)(this.getX() + 30 + this.width + 2), this.getY() - 2.5f, 16777215);
        }
    }
    
    @Override
    public boolean hovered(final int mouseX, final int mouseY) {
        return this.hovered = (mouseX >= this.getX() + 16 && mouseX <= this.getX() + 16 + this.width && mouseY >= this.getY() && mouseY <= this.getY() + this.height);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered) {
            this.sliderValue = (mouseX - (this.getX() + 16 + 4.0f)) / (this.width - 8.0f);
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseDragged(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.sliderValue = (mouseX - (this.getX() + 16 + 4.0f)) / (this.width - 8.0f);
            this.sliderValue = MathUtil.normalizeValue(MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step), this.min, this.max, this.step);
            this.consumer.accept(this.setting, this);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.focusedOnTextbox) {
            if ((Character.isDigit(typedChar) || typedChar == '.') && (!this.newTBValue.contains(".") || typedChar != '.')) {
                this.newTBValue = String.valueOf(this.newTBValue) + typedChar;
            }
            if (typedChar == '\b') {
                int newEnd = this.newTBValue.length() - 2;
                if (newEnd < 0) {
                    newEnd = 0;
                }
                this.newTBValue = this.newTBValue.substring(0, newEnd);
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
        this.consumer.accept(this.setting, this);
    }
    
    protected float getRoundedValue(final float value) {
        return new BigDecimal(String.valueOf(value)).setScale(2, 4).floatValue();
    }
    
    private boolean numberHovered(final int mouseX, final int mouseY) {
        float value = MathUtil.denormalizeValue(this.sliderValue, this.min, this.max, this.step);
        value = this.getRoundedValue(value);
        if (mouseX >= this.getX() + this.width + 2 && mouseX <= this.getX() + this.width + 2 + MoonsenseClient.titleRenderer.getWidth(String.valueOf(value)) && mouseY >= this.getY() - 2.5f) {
            final float n = (float)mouseY;
            final float n2 = this.getY() - 2.5f;
            MoonsenseClient.titleRenderer.getClass();
            if (n <= n2 + 9.0f) {
                return true;
            }
        }
        return false;
    }
}
