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
    public static final FontRenderer unwrap(IFontRenderer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((FontRendererImpl)$this$unwrap).getWrapped();
    }

    public static final IFontRenderer wrap(FontRenderer $this$wrap) {
        int $i$f$wrap = 0;
        return new FontRendererImpl($this$wrap);
    }
}

