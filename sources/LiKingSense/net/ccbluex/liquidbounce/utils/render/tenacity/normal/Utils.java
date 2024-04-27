/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.tenacity.normal;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;

public interface Utils {
    public static final IMinecraft mc = LiquidBounce.INSTANCE.getWrapper().getMinecraft();
    public static final IFontRenderer fr = mc.getFontRendererObj();
}

