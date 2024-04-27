/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.event.ClickEvent
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.injection.backend.ClickEventImpl;
import net.minecraft.util.text.event.ClickEvent;

public final class ClickEventImplKt {
    public static final ClickEvent unwrap(IClickEvent iClickEvent) {
        boolean bl = false;
        return ((ClickEventImpl)iClickEvent).getWrapped();
    }

    public static final IClickEvent wrap(ClickEvent clickEvent) {
        boolean bl = false;
        return new ClickEventImpl(clickEvent);
    }
}

