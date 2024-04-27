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
    public static final IChatStyle wrap(Style style) {
        boolean bl = false;
        return new ChatStyleImpl(style);
    }

    public static final Style unwrap(IChatStyle iChatStyle) {
        boolean bl = false;
        return ((ChatStyleImpl)iChatStyle).getWrapped();
    }
}

