/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

public final class EventState
extends Enum<EventState> {
    public static final /* enum */ EventState PRE;
    public static final /* enum */ EventState POST;
    private static final /* synthetic */ EventState[] $VALUES;
    private final String stateName;

    static {
        EventState[] eventStateArray = new EventState[2];
        EventState[] eventStateArray2 = eventStateArray;
        eventStateArray[0] = PRE = new EventState("PRE");
        eventStateArray[1] = POST = new EventState("POST");
        $VALUES = eventStateArray;
    }

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

