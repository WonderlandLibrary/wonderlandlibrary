/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles;

import java.awt.Color;
import java.io.IOException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Skeet2Style
extends GuiScreen {
    boolean previousmouse = true;
    public float moveX = 0.0f;
    public float moveY = 0.0f;
    boolean bind = false;
    public static ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
    public static ModuleCategory currentModuleType = ModuleCategory.COMBAT;
    public static Module currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
    public static float startX = (float)sr.func_78326_a() / 2.0f - 225.0f;
    public static float startY = (float)sr.func_78328_b() / 2.0f - 175.0f;
    public static int moduleStart = 0;
    public static int valueStart = 0;
    boolean mouse;
    float hue;
    public static int alpha;

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(this.field_146297_k);
        if (alpha < 255) {
            alpha += 5;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float hue = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (hue > 255.0f) {
            hue = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor(hue / 255.0f, 0.9f, 1.0f);
        Color color34 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        Color color35 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        int color36 = color33.getRGB();
        int color37 = color34.getRGB();
        int color38 = color35.getRGB();
        int color39 = new Color((Integer)ClickGUI.colorRedValue.get(), (Integer)ClickGUI.colorGreenValue.get(), (Integer)ClickGUI.colorBlueValue.get(), alpha).getRGB();
        this.hue += 0.1f;
        RenderUtils.rectangleBordered(startX, startY, startX + 450.0f, startY + 350.0f, 0.01, Colors.getColor(90, alpha), Colors.getColor(0, alpha));
        RenderUtils.rectangleBordered(startX + 1.0f, startY + 1.0f, startX + 450.0f - 1.0f, startY + 350.0f - 1.0f, 1.0, Colors.getColor(90, alpha), Colors.getColor(61, alpha));
        RenderUtils.rectangleBordered((double)startX + 2.5, (double)startY + 2.5, (double)(startX + 450.0f) - 2.5, (double)(startY + 350.0f) - 2.5, 0.01, Colors.getColor(61, alpha), Colors.getColor(0, alpha));
        RenderUtils.rectangleBordered(startX + 3.0f, startY + 3.0f, startX + 450.0f - 3.0f, startY + 350.0f - 3.0f, 0.01, Colors.getColor(27, alpha), Colors.getColor(61, alpha));
        if (alpha >= 55) {
            RenderUtils.drawGradientSideways(startX + 3.0f, startY + 3.0f, startX + 225.0f, (double)startY + 3.6, color36, color37);
            RenderUtils.drawGradientSideways(startX + 225.0f, startY + 3.0f, startX + 450.0f - 3.0f, (double)startY + 3.6, color37, color38);
        }
        RenderUtils.drawRect(startX + 98.0f, startY + 48.0f, startX + 432.0f, startY + 318.0f, new Color(30, 30, 30, alpha).getRGB());
        RenderUtils.drawRect(startX + 100.0f, startY + 50.0f, startX + 430.0f, startY + 315.0f, new Color(35, 35, 35, alpha).getRGB());
        RenderUtils.drawRect(startX + 200.0f, startY + 50.0f, startX + 430.0f, startY + 315.0f, new Color(37, 37, 37, alpha).getRGB());
        RenderUtils.drawRect(startX + 202.0f, startY + 50.0f, startX + 430.0f, startY + 315.0f, new Color(40, 40, 40, alpha).getRGB());
        Fonts.font40.drawCenteredString("LA MIERDA", startX + 50.0f, startY + 12.0f, new Color(255, 255, 255, alpha).getRGB());
        Fonts.font35.drawCenteredString("ClickGUI", startX + 50.0f, startY + 32.0f, color39);
        Fonts.font35.drawString("", startX + 15.0f, startY + 330.0f, new Color(180, 180, 180, alpha).getRGB());
        int dWheel = Mouse.getDWheel();
        if (this.isCategoryHovered(startX + 100.0f, startY + 40.0f, startX + 200.0f, startY + 315.0f, mouseX, mouseY)) {
            if (dWheel < 0 && moduleStart < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size() - 1) {
                ++moduleStart;
            }
            if (dWheel > 0 && moduleStart > 0) {
                --moduleStart;
            }
        }
        if (this.isCategoryHovered(startX + 200.0f, startY + 50.0f, startX + 430.0f, startY + 315.0f, mouseX, mouseY)) {
            if (dWheel < 0 && valueStart < currentModule.getValues().size() - 1) {
                ++valueStart;
            }
            if (dWheel > 0 && valueStart > 0) {
                --valueStart;
            }
        }
        float mY = startY + 12.0f;
        for (int i = 0; i < LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).size(); ++i) {
            Module module = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(i);
            if (mY > startY + 250.0f) break;
            if (i < moduleStart) continue;
            if (!module.getState()) {
                RenderUtils.drawRect(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? new Color(60, 60, 60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (float)(this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 112 : 110), mY + 58.0f, 3.0, new Color(70, 70, 70, alpha).getRGB(), 5);
                Fonts.font35.drawString(module.getName(), startX + (float)(this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 122 : 120), mY + 55.0f, new Color(175, 175, 175, alpha).getRGB());
            } else {
                RenderUtils.drawRect(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? new Color(60, 60, 60, alpha).getRGB() : new Color(35, 35, 35, alpha).getRGB());
                RenderUtils.drawFilledCircle(startX + (float)(this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 112 : 110), mY + 58.0f, 3.0, new Color(100, 255, 100, alpha).getRGB(), 5);
                Fonts.font35.drawString(module.getName(), startX + (float)(this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) ? 122 : 120), mY + 55.0f, new Color(255, 255, 255, alpha).getRGB());
            }
            if (this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY)) {
                if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                    if (module.getState()) {
                        module.setState(false);
                    } else {
                        module.setState(true);
                    }
                    this.previousmouse = true;
                }
                if (!this.previousmouse && Mouse.isButtonDown((int)1)) {
                    this.previousmouse = true;
                }
            }
            if (!Mouse.isButtonDown((int)0)) {
                this.previousmouse = false;
            }
            if (this.isSettingsButtonHovered(startX + 100.0f, mY + 45.0f, startX + 200.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown((int)1)) {
                for (int j = 0; j < currentModule.getValues().size(); ++j) {
                    Value<?> value = currentModule.getValues().get(j);
                }
                currentModule = module;
                valueStart = 0;
            }
            mY += 25.0f;
        }
        mY = startY + 12.0f;
        GameFontRenderer font = Fonts.font35;
        for (int k = 0; k < currentModule.getValues().size() && mY <= startY + 250.0f; ++k) {
            float val;
            double valRel;
            double perc;
            double valAbs;
            double inc;
            double max;
            double render;
            float x;
            if (k < valueStart) continue;
            Value<?> value2 = currentModule.getValues().get(k);
            if (value2 instanceof TextValue) {
                TextValue textValue = (TextValue)value2;
                Fonts.font40.drawString(textValue.getName() + ": " + (String)textValue.get(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                mY += 20.0f;
            }
            if (value2 instanceof BlockValue) {
                BlockValue blockValue = (BlockValue)value2;
                Fonts.font40.drawString(blockValue.getName() + ": " + blockValue.get(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                mY += 20.0f;
            }
            if (value2 instanceof FontValue) {
                FontValue fontValue = (FontValue)value2;
                Fonts.font40.drawString(fontValue.getName() + ": " + fontValue.get(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                mY += 20.0f;
            }
            if (value2 instanceof IntegerValue) {
                IntegerValue floatValue = (IntegerValue)value2;
                x = startX + 320.0f;
                render = 68.0f * (float)((Integer)floatValue.getValue() - floatValue.getMinimum()) / (float)(floatValue.getMaximum() - floatValue.getMinimum());
                RenderUtils.drawRect(x + 2.0f, mY + 52.0f, (float)((double)x + 75.0), mY + 53.0f, this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) ? new Color(80, 80, 80, alpha).getRGB() : new Color(30, 30, 30, alpha).getRGB());
                RenderUtils.drawRect(x + 2.0f, mY + 52.0f, (float)((double)x + render + 6.5), mY + 53.0f, new Color(35, 35, 255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float)((double)x + render + 2.0) + 3.0f, (double)mY + 52.25, 1.5, new Color(35, 35, 255, alpha).getRGB(), 5);
                Fonts.font40.drawString(floatValue.getName(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                Fonts.font40.drawString(((Integer)floatValue.getValue()).toString(), startX + 320.0f - (float)font.func_78256_a(value2.getValue().toString()), mY + 50.0f, new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                        render = floatValue.getMinimum();
                        max = floatValue.getMaximum();
                        inc = 1.0;
                        valAbs = (double)((float)mouseX - x) + 1.0;
                        perc = valAbs / 68.0;
                        perc = Math.min(Math.max(0.0, perc), 1.0);
                        valRel = (max - render) * perc;
                        val = (float)(render + valRel);
                        val = (float)((double)Math.round((double)val * 1.0) / 1.0);
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20.0f;
            }
            if (value2 instanceof FloatValue) {
                FloatValue floatValue2 = (FloatValue)value2;
                x = startX + 320.0f;
                render = 68.0f * (((Float)floatValue2.getValue()).floatValue() - floatValue2.getMinimum()) / (floatValue2.getMaximum() - floatValue2.getMinimum());
                RenderUtils.drawRect(x + 2.0f, mY + 52.0f, (float)((double)x + 75.0), mY + 53.0f, this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) ? new Color(80, 80, 80, alpha).getRGB() : new Color(30, 30, 30, alpha).getRGB());
                RenderUtils.drawRect(x + 2.0f, mY + 52.0f, (float)((double)x + render + 6.5), mY + 53.0f, new Color(35, 35, 255, alpha).getRGB());
                RenderUtils.drawFilledCircle((float)((double)x + render + 2.0) + 3.0f, (double)mY + 52.25, 1.5, new Color(35, 35, 255, alpha).getRGB(), 5);
                Fonts.font40.drawString(floatValue2.getName(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                Fonts.font40.drawString(((Float)floatValue2.getValue()).toString(), startX + 320.0f - (float)font.func_78256_a(value2.getValue().toString()), mY + 50.0f, new Color(255, 255, 255, alpha).getRGB());
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(x, mY + 45.0f, x + 100.0f, mY + 57.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                        render = floatValue2.getMinimum();
                        max = floatValue2.getMaximum();
                        inc = 0.01f;
                        valAbs = (double)((float)mouseX - x) + 1.0;
                        perc = valAbs / 68.0;
                        perc = Math.min(Math.max(0.0, perc), 1.0);
                        valRel = (max - render) * perc;
                        val = (float)(render + valRel);
                        val = (float)((double)Math.round((double)val * 100.00000223517424) / 100.00000223517424);
                        floatValue2.setValue(Float.valueOf(val));
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 20.0f;
            }
            if (value2 instanceof BoolValue) {
                BoolValue boolValue = (BoolValue)value2;
                x = startX + 320.0f;
                int xx = 50;
                int x2x = 65;
                Fonts.font40.drawString(boolValue.getName(), startX + 220.0f, mY + 50.0f, new Color(175, 175, 175, alpha).getRGB());
                if (((Boolean)boolValue.getValue()).booleanValue()) {
                    RenderUtils.drawRect(x + 50.0f, mY + 50.0f, x + 65.0f, mY + 59.0f, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + 50.0f, (double)mY + 54.5, 4.5, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + 65.0f, (double)mY + 54.5, 4.5, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + 65.0f, (double)mY + 54.5, 5.0, new Color(35, 35, 255, alpha).getRGB(), 10);
                } else {
                    RenderUtils.drawRect(x + 50.0f, mY + 50.0f, x + 65.0f, mY + 59.0f, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB());
                    RenderUtils.drawFilledCircle(x + 50.0f, (double)mY + 54.5, 4.5, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + 65.0f, (double)mY + 54.5, 4.5, this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(20, 20, 20, alpha).getRGB(), 10);
                    RenderUtils.drawFilledCircle(x + 50.0f, (double)mY + 54.5, 5.0, new Color(56, 56, 56, alpha).getRGB(), 10);
                }
                if (this.isCheckBoxHovered(x + 50.0f - 5.0f, mY + 50.0f, x + 65.0f + 6.0f, mY + 59.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                        this.previousmouse = true;
                        this.mouse = true;
                    }
                    if (this.mouse) {
                        boolValue.setValue((Boolean)boolValue.getValue() == false);
                        this.mouse = false;
                    }
                }
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousmouse = false;
                }
                mY += 20.0f;
            }
            if (!(value2 instanceof ListValue)) continue;
            ListValue listValue = (ListValue)value2;
            x = startX + 320.0f;
            Fonts.font40.drawString(listValue.getName(), startX + 220.0f, mY + 52.0f, new Color(175, 175, 175, alpha).getRGB());
            RenderUtils.drawRect(x + 5.0f, mY + 45.0f, x + 75.0f, mY + 65.0f, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
            RenderUtils.drawRect(x + 2.0f, mY + 48.0f, x + 78.0f, mY + 62.0f, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
            RenderUtils.drawFilledCircle(x + 5.0f, mY + 48.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
            RenderUtils.drawFilledCircle(x + 5.0f, mY + 62.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
            RenderUtils.drawFilledCircle(x + 75.0f, mY + 48.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
            RenderUtils.drawFilledCircle(x + 75.0f, mY + 62.0f, 3.0, this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
            Fonts.font40.drawString((String)listValue.get(), x + 40.0f - (float)font.func_78256_a((String)listValue.get()) / 2.0f, mY + 53.0f, new Color(255, 255, 255, alpha).getRGB());
            if (this.isStringHovered(x, mY + 45.0f, x + 75.0f, mY + 65.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousmouse) {
                if (listValue.getValues().length <= listValue.getModeListNumber((String)listValue.get()) + 1) {
                    listValue.set(listValue.getValues()[0]);
                } else {
                    listValue.set(listValue.getValues()[listValue.getModeListNumber((String)listValue.get()) + 1]);
                }
                this.previousmouse = true;
            }
            mY += 25.0f;
        }
        float x2 = startX + 320.0f;
        float yyy = startY + 240.0f;
        Fonts.font40.drawString("Bind", startX + 220.0f, yyy + 50.0f, new Color(170, 170, 170, alpha).getRGB());
        RenderUtils.drawRect(x2 + 5.0f, yyy + 45.0f, x2 + 75.0f, yyy + 65.0f, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawRect(x2 + 2.0f, yyy + 48.0f, x2 + 78.0f, yyy + 62.0f, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB());
        RenderUtils.drawFilledCircle(x2 + 5.0f, yyy + 48.0f, 3.0, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x2 + 5.0f, yyy + 62.0f, 3.0, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x2 + 75.0f, yyy + 48.0f, 3.0, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        RenderUtils.drawFilledCircle(x2 + 75.0f, yyy + 62.0f, 3.0, this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY) ? new Color(80, 80, 80, alpha).getRGB() : new Color(56, 56, 56, alpha).getRGB(), 5);
        Fonts.font40.drawString(Keyboard.getKeyName((int)currentModule.getKeyBind()), x2 + 40.0f - (float)font.func_78256_a(Keyboard.getKeyName((int)currentModule.getKeyBind())) / 2.0f, yyy + 53.0f, new Color(255, 255, 255, alpha).getRGB());
        if ((this.isHovered(startX, startY, startX + 450.0f, startY + 50.0f, mouseX, mouseY) || this.isHovered(startX, startY + 315.0f, startX + 450.0f, startY + 350.0f, mouseX, mouseY) || this.isHovered(startX + 430.0f, startY, startX + 450.0f, startY + 350.0f, mouseX, mouseY)) && Mouse.isButtonDown((int)0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)mouseX - startX;
                this.moveY = (float)mouseY - startY;
            } else {
                startX = (float)mouseX - this.moveX;
                startY = (float)mouseY - this.moveY;
            }
            this.previousmouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        if (this.isHovered((float)sr.func_78326_a() / 2.0f - 40.0f, 0.0f, (float)sr.func_78326_a() / 2.0f + 40.0f, 20.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            startX = (float)sr.func_78326_a() / 2.0f - 225.0f;
            startY = (float)sr.func_78328_b() / 2.0f - 175.0f;
            alpha = 0;
        }
        RenderUtils.drawRect((float)sr.func_78326_a() / 2.0f - 39.0f, 0.0f, (float)sr.func_78326_a() / 2.0f + 39.0f, 19.0f, new Color(0, 0, 0, alpha / 2).getRGB());
        RenderUtils.drawRect((float)sr.func_78326_a() / 2.0f - 40.0f, 0.0f, (float)sr.func_78326_a() / 2.0f + 40.0f, 20.0f, new Color(0, 0, 0, alpha / 2).getRGB());
        int l = 60;
        int l2 = 45;
        float k2 = startY + 10.0f;
        float xx2 = startX + 5.0f;
        for (int i2 = 0; i2 < ModuleCategory.values().length; ++i2) {
            ModuleCategory[] iterator2 = ModuleCategory.values();
            if (iterator2[i2] == currentModuleType) {
                RenderUtils.drawRect(xx2 + 8.0f, k2 + 12.0f + 60.0f + (float)(i2 * 45), xx2 + 30.0f, k2 + 13.0f + 60.0f + (float)(i2 * 45), color39);
            }
            float y2 = k2 + 20.0f + 60.0f + (float)(i2 * 45);
            Fonts.font40.drawString(iterator2[i2].toString(), xx2 + (float)(this.isCategoryHovered(xx2 + 8.0f, k2 - 10.0f + 60.0f + (float)(i2 * 45), xx2 + 80.0f, k2 + 20.0f + 60.0f + (float)(i2 * 45), mouseX, mouseY) ? 27 : 25), k2 + 56.0f + (float)(45 * i2), new Color(255, 255, 255, alpha).getRGB());
            try {
                if (!this.isCategoryHovered(xx2 + 8.0f, k2 - 10.0f + 60.0f + (float)(i2 * 45), xx2 + 80.0f, k2 + 20.0f + 60.0f + (float)(i2 * 45), mouseX, mouseY) || !Mouse.isButtonDown((int)0)) continue;
                currentModuleType = iterator2[i2];
                currentModule = LiquidBounce.moduleManager.getModuleInCategory(currentModuleType).get(0);
                moduleStart = 0;
                valueStart = 0;
                for (int x3 = 0; x3 < currentModule.getValues().size(); ++x3) {
                    Value<?> value = currentModule.getValues().get(x3);
                }
                continue;
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void func_73866_w_() {
        for (int i = 0; i < currentModule.getValues().size(); ++i) {
            Value<?> value = currentModule.getValues().get(i);
        }
        super.func_73866_w_();
    }

    public void func_73869_a(char typedChar, int keyCode) {
        if (this.bind) {
            currentModule.setKeyBind(keyCode);
            if (keyCode == 1) {
                currentModule.setKeyBind(0);
            }
            this.bind = false;
        } else if (keyCode == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            ((ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class)).setState(false);
            if (this.field_146297_k.field_71462_r == null) {
                this.field_146297_k.func_71381_h();
            }
        }
    }

    public void func_73864_a(int mouseX, int mouseY, int button) throws IOException {
        float x = startX + 220.0f;
        float mY = startY + 30.0f;
        for (int i = 0; i < currentModule.getValues().size() && mY <= startY + 350.0f; ++i) {
            if (i < valueStart) continue;
            Value<?> value = currentModule.getValues().get(i);
            if (value instanceof FloatValue) {
                mY += 20.0f;
            }
            if (value instanceof BoolValue) {
                mY += 20.0f;
            }
            if (!(value instanceof ListValue)) continue;
            mY += 25.0f;
        }
        float x2 = startX + 320.0f;
        float yyy = startY + 240.0f;
        if (this.isHovered(x2 + 2.0f, yyy + 45.0f, x2 + 78.0f, yyy + 65.0f, mouseX, mouseY)) {
            this.bind = true;
        }
        super.func_73864_a(mouseX, mouseY, button);
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public void func_146281_b() {
        alpha = 0;
    }
}

