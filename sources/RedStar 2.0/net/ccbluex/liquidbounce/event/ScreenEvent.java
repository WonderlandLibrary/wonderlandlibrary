package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\b0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/ScreenEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "guiScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;)V", "getGuiScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "Pride"})
public final class ScreenEvent
extends Event {
    @Nullable
    private final IGuiScreen guiScreen;

    @Nullable
    public final IGuiScreen getGuiScreen() {
        return this.guiScreen;
    }

    public ScreenEvent(@Nullable IGuiScreen guiScreen) {
        this.guiScreen = guiScreen;
    }
}
