// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.button.values;

import java.awt.Font;
import org.lwjgl.input.Keyboard;
import net.lenni0451.eventapi.events.IEvent;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.events.EventClickGui;
import org.lwjgl.input.Mouse;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import net.augustus.utils.skid.tomorrow.ColorUtils;
import net.augustus.material.Main;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.material.Tab;
import net.augustus.settings.Setting;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.button.Button;

public class BNumbers extends Button
{
    private static UnicodeFontRenderer arial16B;
    private static UnicodeFontRenderer arial16;
    
    public BNumbers(final float x, final float y, final Setting v, final Tab moduleTab) {
        super(x, y, v, moduleTab);
    }
    
    @Override
    public void drawButton(final float mouseX, final float mouseY) {
        BNumbers.arial16.drawString(this.v.getName() + ":" + this.round(((DoubleValue)this.v).getValue(), ((DoubleValue)this.v).getDecimalPlaces()), this.x, this.y - 4.0f, new Color(255, 255, 255).getRGB());
        final double visibleRange = ((DoubleValue)this.v).getMaxValue() - ((DoubleValue)this.v).getMinValue();
        final double percentage = (((DoubleValue)this.v).getValue() - ((DoubleValue)this.v).getMinValue()) / visibleRange;
        RenderUtil.drawRect(this.x + 5.0f, this.y + 5.0f, this.x + 65.0f, this.y + 6.0f, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.6f));
        RenderUtil.drawRect(this.x + 5.0f, this.y + 5.0f, this.x + 5.0f + (int)(percentage * 60.0), this.y + 6.0f, Main.clientColor.getRGB());
        RenderUtil.drawCircle((float)(this.x + 5.0f + percentage * 60.0), this.y + 5.5f, 3.0f, Main.clientColor.getRGB());
        if (Main.isHovered(this.x + 5.0f, this.y + 4.0f, this.x + 65.0f, this.y + 7.0f, mouseX, mouseY)) {
            if (Mouse.isButtonDown(0)) {
                this.drag = true;
            }
            else {
                this.drag = false;
            }
            if (this.drag) {
                final double reach = mouseX - (this.x + 5.0f);
                final double percent = reach / 60.0;
                double newValue = ((DoubleValue)this.v).getMinValue() + percent * visibleRange;
                newValue = Math.max(((DoubleValue)this.v).getMinValue(), Math.min(newValue, ((DoubleValue)this.v).getMaxValue()));
                ((DoubleValue)this.v).setValue(this.round(newValue, ((DoubleValue)this.v).getDecimalPlaces()));
                EventManager.call(new EventClickGui());
            }
            final double stepSize = 1.0 / Math.pow(10.0, ((DoubleValue)this.v).getDecimalPlaces());
            if (Keyboard.isKeyDown(205)) {
                ((DoubleValue)this.v).setValue(Math.min(((DoubleValue)this.v).getValue() + stepSize, ((DoubleValue)this.v).getMaxValue()));
            }
            else if (Keyboard.isKeyDown(203)) {
                ((DoubleValue)this.v).setValue(Math.max(((DoubleValue)this.v).getValue() - stepSize, ((DoubleValue)this.v).getMinValue()));
            }
        }
    }
    
    @Override
    public void mouseClicked(final float mouseX, final float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
    
    private double round(final double value, final int decimalPoints) {
        if (decimalPoints == 0) {
            return (int)value;
        }
        final double d = Math.pow(10.0, decimalPoints);
        return Math.round(value * d) / d;
    }
    
    static {
        try {
            BNumbers.arial16B = new UnicodeFontRenderer(Font.createFont(0, BNumbers.class.getResourceAsStream("/ressources/ArialB.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            BNumbers.arial16 = new UnicodeFontRenderer(Font.createFont(0, BNumbers.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
