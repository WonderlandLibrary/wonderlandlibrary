/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class ScreenEvent
extends Event {
    private final IGuiScreen guiScreen;

    public ScreenEvent(@Nullable IGuiScreen iGuiScreen) {
        this.guiScreen = iGuiScreen;
    }

    public final IGuiScreen getGuiScreen() {
        return this.guiScreen;
    }
}

