/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.Style
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import net.ccbluex.liquidbounce.injection.backend.ChatStyleImpl;
import net.minecraft.util.text.Style;

public final class ChatStyleImplKt {
    public static final Style unwrap(IChatStyle $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ChatStyleImpl)$this$unwrap).getWrapped();
    }

    public static final IChatStyle wrap(Style $this$wrap) {
        int $i$f$wrap = 0;
        return new ChatStyleImpl($this$wrap);
    }
}

