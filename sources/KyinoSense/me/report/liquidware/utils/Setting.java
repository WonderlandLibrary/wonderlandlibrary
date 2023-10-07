/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ColorValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;

public abstract class Setting {
    protected final GameFontRenderer font = Fonts.font35;

    protected final boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public abstract void drawListValue(boolean var1, int var2, int var3, float var4, float var5, ListValue var6);

    public abstract void drawTextValue(float var1, float var2, TextValue var3);

    public abstract void drawFloatValue(int var1, float var2, float var3, boolean var4, boolean var5, FloatValue var6);

    public abstract void drawIntegerValue(int var1, float var2, float var3, boolean var4, boolean var5, IntegerValue var6);

    public abstract void drawBoolValue(boolean var1, int var2, int var3, float var4, float var5, BoolValue var6);

    public abstract void drawColorValue(float var1, float var2, float var3, int var4, int var5, ColorValue var6);
}

