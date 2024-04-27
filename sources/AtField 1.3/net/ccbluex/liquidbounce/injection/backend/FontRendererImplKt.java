/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.minecraft.client.gui.FontRenderer;

public final class FontRendererImplKt {
    public static final IFontRenderer wrap(FontRenderer fontRenderer) {
        boolean bl = false;
        return new FontRendererImpl(fontRenderer);
    }

    public static final FontRenderer unwrap(IFontRenderer iFontRenderer) {
        boolean bl = false;
        return ((FontRendererImpl)iFontRenderer).getWrapped();
    }
}

