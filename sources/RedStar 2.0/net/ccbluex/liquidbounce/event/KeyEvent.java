package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\b\n\b\u000020B\r0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/KeyEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "key", "", "(I)V", "getKey", "()I", "Pride"})
public final class KeyEvent
extends Event {
    private final int key;

    public final int getKey() {
        return this.key;
    }

    public KeyEvent(int key) {
        this.key = key;
    }
}
