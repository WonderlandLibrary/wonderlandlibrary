/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.injection.backend.IChatComponentImpl;
import net.minecraft.util.text.ITextComponent;

public final class IChatComponentImplKt {
    public static final ITextComponent unwrap(IIChatComponent iIChatComponent) {
        boolean bl = false;
        return ((IChatComponentImpl)iIChatComponent).getWrapped();
    }

    public static final IIChatComponent wrap(ITextComponent iTextComponent) {
        boolean bl = false;
        return new IChatComponentImpl(iTextComponent);
    }
}

