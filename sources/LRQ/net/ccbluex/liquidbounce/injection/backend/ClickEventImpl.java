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

    public boolean equals(@Nullable Object other) {
        return other instanceof ClickEventImpl && ((ClickEventImpl)other).wrapped.equals(this.wrapped);
    }

    public final ClickEvent getWrapped() {
        return this.wrapped;
    }

    public ClickEventImpl(ClickEvent wrapped) {
        this.wrapped = wrapped;
    }
}

