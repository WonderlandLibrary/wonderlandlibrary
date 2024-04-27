package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u00002\b0\u00000B\b0¢R0¢\b\n\u0000\bj\bj\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/event/EventState;", "", "stateName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getStateName", "()Ljava/lang/String;", "PRE", "POST", "Pride"})
public final class EventState
extends Enum<EventState> {
    public static final EventState PRE;
    public static final EventState POST;
    private static final EventState[] $VALUES;
    @NotNull
    private final String stateName;

    static {
        EventState[] eventStateArray = new EventState[2];
        EventState[] eventStateArray2 = eventStateArray;
        eventStateArray[0] = PRE = new EventState("PRE");
        eventStateArray[1] = POST = new EventState("POST");
        $VALUES = eventStateArray;
    }

    @NotNull
    public final String getStateName() {
        return this.stateName;
    }

    private EventState(String stateName) {
        this.stateName = stateName;
    }

    public static EventState[] values() {
        return (EventState[])$VALUES.clone();
    }

    public static EventState valueOf(String string) {
        return Enum.valueOf(EventState.class, string);
    }
}
