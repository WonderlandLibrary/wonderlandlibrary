/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.event;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/event/EventState;", "", "stateName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getStateName", "()Ljava/lang/String;", "PRE", "POST", "LiquidBounce"})
public final class EventState
extends Enum<EventState> {
    @NotNull
    private final String stateName;
    public static final /* enum */ EventState PRE = new EventState("PRE");
    public static final /* enum */ EventState POST = new EventState("POST");
    private static final /* synthetic */ EventState[] $VALUES;

    private EventState(String stateName) {
        this.stateName = stateName;
    }

    @NotNull
    public final String getStateName() {
        return this.stateName;
    }

    public static EventState[] values() {
        return (EventState[])$VALUES.clone();
    }

    public static EventState valueOf(String value) {
        return Enum.valueOf(EventState.class, value);
    }

    static {
        $VALUES = eventStateArray = new EventState[]{EventState.PRE, EventState.POST};
    }
}

