/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

public final class EventState
extends Enum {
    private final String stateName;
    private static final EventState[] $VALUES;
    public static final /* enum */ EventState POST;
    public static final /* enum */ EventState PRE;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private EventState() {
        void var3_1;
        void var2_-1;
        void var1_-1;
        this.stateName = var3_1;
    }

    public static EventState[] values() {
        return (EventState[])$VALUES.clone();
    }

    public final String getStateName() {
        return this.stateName;
    }

    public static EventState valueOf(String string) {
        return Enum.valueOf(EventState.class, string);
    }

    static {
        EventState[] eventStateArray = new EventState[2];
        EventState[] eventStateArray2 = eventStateArray;
        eventStateArray[0] = PRE = new EventState("PRE", 0, "PRE");
        eventStateArray[1] = POST = new EventState("POST", 1, "POST");
        $VALUES = eventStateArray;
    }
}

