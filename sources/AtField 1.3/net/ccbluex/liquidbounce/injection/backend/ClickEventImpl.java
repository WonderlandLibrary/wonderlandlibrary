/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.event.ClickEvent
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.minecraft.util.text.event.ClickEvent;
import org.jetbrains.annotations.Nullable;

public final class ClickEventImpl
implements IClickEvent {
    private final ClickEvent wrapped;

    public final ClickEvent getWrapped() {
        return this.wrapped;
    }

    public ClickEventImpl(ClickEvent clickEvent) {
        this.wrapped = clickEvent;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ClickEventImpl && ((ClickEventImpl)object).wrapped.equals(this.wrapped);
    }
}

