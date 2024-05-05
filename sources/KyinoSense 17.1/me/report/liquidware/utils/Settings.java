/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.report.liquidware.utils;

import java.awt.Color;
import me.report.liquidware.utils.AnimationHelper;
import me.report.liquidware.utils.Setting;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ColorValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.input.Mouse;

public class Settings
extends Setting {
    private final AnimationHelper alphaAnim;

    public Settings(AnimationHelper alphaAnim) {
        this.alphaAnim = alphaAnim;
    }

    @Override
    public void drawListValue(boolean previousMouse, int mouseX, int mouseY, float mY, float startX, ListValue listValue) {
        float x = startX + 295.0f;
        this.font.drawString(listValue.getName(), startX + 210.0f, mY + 1.0f, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRect(x, mY - 5.0f, x + 80.0f, mY - 4.0f, new Color(0, 100, 255, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRect(x, mY + 10.0f, x + 80.0f, mY + 11.0f, new Color(0, 100, 255, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRect(x + 80.0f, mY - 5.0f, x + 81.0f, mY + 11.0f, new Color(0, 100, 255, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRect(x, mY - 5.0f, x + 1.0f, mY + 10.0f, new Color(0, 100, 255, this.alphaAnim.getAlpha()).getRGB());
        this.font.drawString((String)listValue.get(), x + 10.0f, mY + 4.0f, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        if (this.isHovered(x, mY - 5.0f, x + 80.0f, mY + 11.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !previousMouse) {
            String current = (String)listValue.get();
            int next = listValue.getModeListNumber(current) + 1 >= listValue.getValues().length ? 0 : listValue.getModeListNumber(current) + 1;
            listValue.set(listValue.getValues()[next]);
        }
    }

    @Override
    public void drawTextValue(float startX, float mY, TextValue textValue) {
        this.font.drawString(textValue.getName() + ": " + (String)textValue.get(), startX + 210.0f, mY, new Color(80, 80, 80).getRGB());
    }

    public String toString() {
        return "Light Client Settings";
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object obj) {
        return obj == this;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void drawFloatValue(int mouseX, float mY, float startX, boolean previousMouse, boolean buttonDown, FloatValue floatValue) {
        float x = startX + 300.0f;
        double render = 68.0f * (((Float)floatValue.get()).floatValue() - floatValue.getMinimum()) / (floatValue.getMaximum() - floatValue.getMinimum()) + 1.0f;
        RenderUtils.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + 75.0), mY + 3.0f, new Color(200, 200, 200, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + render + 6.5), mY + 3.0f, new Color(61, 141, 255, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.circle((float)((double)x + render + 4.0), mY + 2.5f, 2.0f, new Color(61, 141, 255, this.alphaAnim.getAlpha()));
        this.font.drawString(String.valueOf(floatValue.get()), (float)((double)x + render - 5.0), mY - 7.0f, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        this.font.drawString(floatValue.getName(), startX + 210.0f, mY, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        if (buttonDown && Mouse.isButtonDown((int)0) && !previousMouse && Mouse.isButtonDown((int)0)) {
            render = floatValue.getMinimum();
            double max = floatValue.getMaximum();
            double inc = 0.01;
            double valAbs = (double)mouseX - ((double)x + 1.0);
            double perc = valAbs / 68.0;
            perc = Math.min(Math.max(0.0, perc), 1.0);
            double valRel = (max - render) * perc;
            double val = render + valRel;
            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
            floatValue.set(Float.valueOf((float)val));
        }
    }

    @Override
    public void drawIntegerValue(int mouseX, float mY, float startX, boolean previousMouse, boolean buttonDown, IntegerValue integerValue) {
        float x = startX + 300.0f;
        double render = 68.0f * (float)((Integer)integerValue.get() - integerValue.getMinimum()) / (float)(integerValue.getMaximum() - integerValue.getMinimum()) + 1.0f;
        RenderUtils.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + 75.0), mY + 3.0f, new Color(200, 200, 200).getRGB());
        RenderUtils.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + render + 6.5), mY + 3.0f, new Color(61, 141, 255).getRGB());
        RenderUtils.circle((float)((double)x + render + 4.0), mY + 2.5f, 2.0f, new Color(61, 141, 255, this.alphaAnim.getAlpha()));
        this.font.drawString(String.valueOf(integerValue.get()), (float)((double)x + render - 5.0), mY - 7.0f, new Color(80, 80, 80).getRGB());
        this.font.drawString(integerValue.getName(), startX + 210.0f, mY, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        if (buttonDown && Mouse.isButtonDown((int)0) && !previousMouse && Mouse.isButtonDown((int)0)) {
            render = integerValue.getMinimum();
            double max = integerValue.getMaximum();
            double inc = 1.0;
            double valAbs = (double)mouseX - ((double)x + 1.0);
            double perc = valAbs / 68.0;
            perc = Math.min(Math.max(0.0, perc), 1.0);
            double valRel = (max - render) * perc;
            double val = render + valRel;
            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
            integerValue.set((int)val);
        }
    }

    @Override
    public void drawBoolValue(boolean mouse, int mouseX, int mouseY, float startX, float mY, BoolValue boolValue) {
        float x = startX + 325.0f;
        this.font.drawString(boolValue.getName(), startX + 210.0f, mY, new Color(80, 80, 80, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.drawRoundedRect2(x + 30.0f, mY - 2.0f, x + 50.0f, mY + 8.0f, 4.0f, (Boolean)boolValue.get() != false ? new Color(66, 134, 245, this.alphaAnim.getAlpha()).getRGB() : new Color(114, 118, 125, this.alphaAnim.getAlpha()).getRGB());
        RenderUtils.circle(x + 40.0f + boolValue.getAnimation().getAnimationX(), mY + 3.0f, 4.0f, (Boolean)boolValue.get() != false ? new Color(255, 255, 255, this.alphaAnim.getAlpha()).getRGB() : new Color(164, 168, 175, this.alphaAnim.getAlpha()).getRGB());
        if (boolValue.getAnimation().getAnimationX() > -5.0f && !((Boolean)boolValue.get()).booleanValue()) {
            boolValue.getAnimation().animationX -= 1.0f;
        } else if (boolValue.getAnimation().getAnimationX() < 5.0f && ((Boolean)boolValue.get()).booleanValue()) {
            boolValue.getAnimation().animationX += 1.0f;
        }
        if (this.isHovered(x + 30.0f, mY + 2.0f, x + 50.0f, mY + 12.0f, mouseX, mouseY) && mouse) {
            boolValue.set((Boolean)boolValue.get() == false);
        }
    }

    @Override
    public void drawColorValue(float startX, float mY, float x, int mouseX, int mouseY, ColorValue colorValue) {
        Color rainbowColor;
        this.font.drawString(colorValue.getName(), startX + 210.0f, mY - 2.0f, new Color(80, 80, 80).getRGB());
        float y = mY - 8.0f;
        double aDouble = ((double)((float)mouseX - x) / 50.0 + Math.sin(1.6)) % 1.0;
        for (int ticks = 0; ticks < 50; ++ticks) {
            rainbowColor = new Color(Color.HSBtoRGB((float)((double)ticks / 50.0 + Math.sin(1.6)) % 1.0f, 1.0f, 1.0f));
            if ((float)mouseX > x && (float)mouseX < x + 50.0f && (float)mouseY > y && (float)mouseY < y + 13.0f && Mouse.isButtonDown((int)0)) {
                colorValue.set(new Color(Color.HSBtoRGB((float)aDouble, 1.0f, 1.0f)).getRGB());
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            if ((float)mouseX > x && (float)mouseX < x + 50.0f && (float)mouseY > y && (float)mouseY < y + 13.0f) {
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            RenderUtils.drawRect(x + (float)ticks, y, x + (float)ticks + 1.0f, y + 13.0f, rainbowColor.getRGB());
        }
        for (int shits = 0; shits < 50; ++shits) {
            rainbowColor = new Color(Color.HSBtoRGB((float)((double)shits / 50.0 + Math.sin(1.6)) % 1.0f, 0.5f, 1.0f));
            if ((float)mouseX > x && (float)mouseX < x + 100.0f && (float)mouseY > y && (float)mouseY < y + 13.0f && Mouse.isButtonDown((int)0)) {
                colorValue.set(new Color(Color.HSBtoRGB((float)aDouble, 0.5f, 1.0f)).getRGB());
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            if ((float)mouseX > x && (float)mouseX < x + 100.0f && (float)mouseY > y && (float)mouseY < y + 13.0f) {
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            RenderUtils.drawRect(x + (float)shits + 50.0f, y, x + (float)shits + 51.0f, y + 13.0f, rainbowColor.getRGB());
        }
        RenderUtils.drawRect(x, y + 16.0f, x + 50.0f, y + 20.0f, (int)((Integer)colorValue.get()));
    }
}

