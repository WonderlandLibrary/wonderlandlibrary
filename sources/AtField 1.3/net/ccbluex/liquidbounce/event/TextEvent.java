/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class TextEvent
extends Event {
    private String text;

    public final void setText(@Nullable String string) {
        this.text = string;
    }

    public TextEvent(@Nullable String string) {
        this.text = string;
    }

    public final String getText() {
        return this.text;
    }
}

