/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/event/EventState;", "", "stateName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getStateName", "()Ljava/lang/String;", "PRE", "POST", "KyinoClient"})
public final class EventState
extends Enum<EventState> {
    public static final /* enum */ EventState PRE;
    public static final /* enum */ EventState POST;
    private static final /* synthetic */ EventState[] $VALUES;
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

