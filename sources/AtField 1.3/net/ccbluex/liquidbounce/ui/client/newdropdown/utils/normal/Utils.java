/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;

public interface Utils {
    public static final IFontRenderer fr;
    public static final IMinecraft mc;

    static {
        mc = LiquidBounce.INSTANCE.getWrapper().getMinecraft();
        fr = mc.getFontRendererObj();
    }
}

