package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\b0¢R0X¢\n\u0000\b\"\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/event/TextEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "text", "", "(Ljava/lang/String;)V", "getText", "()Ljava/lang/String;", "setText", "Pride"})
public final class TextEvent
extends Event {
    @Nullable
    private String text;

    @Nullable
    public final String getText() {
        return this.text;
    }

    public final void setText(@Nullable String string) {
        this.text = string;
    }

    public TextEvent(@Nullable String text) {
        this.text = text;
    }
}
