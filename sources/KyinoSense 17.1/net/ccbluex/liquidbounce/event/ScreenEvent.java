/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/event/ScreenEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "guiScreen", "Lnet/minecraft/client/gui/GuiScreen;", "(Lnet/minecraft/client/gui/GuiScreen;)V", "getGuiScreen", "()Lnet/minecraft/client/gui/GuiScreen;", "KyinoClient"})
public final class ScreenEvent
extends Event {
    @Nullable
    private final GuiScreen guiScreen;

    @Nullable
    public final GuiScreen getGuiScreen() {
        return this.guiScreen;
    }

    public ScreenEvent(@Nullable GuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }
}

