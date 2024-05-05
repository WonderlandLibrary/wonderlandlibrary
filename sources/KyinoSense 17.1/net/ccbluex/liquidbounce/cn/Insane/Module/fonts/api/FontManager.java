/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api;

import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api.FontFamily;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api.FontRenderer;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api.FontType;

@FunctionalInterface
public interface FontManager {
    public FontFamily fontFamily(FontType var1);

    default public FontRenderer font(FontType fontType, int size) {
        return this.fontFamily(fontType).ofSize(size);
    }
}

