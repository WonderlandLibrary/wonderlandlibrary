/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

public final class NotifyType
extends Enum<NotifyType> {
    public static final /* enum */ NotifyType SUCCESS;
    public static final /* enum */ NotifyType ERROR;
    public static final /* enum */ NotifyType WARNING;
    public static final /* enum */ NotifyType INFO;
    private static final /* synthetic */ NotifyType[] $VALUES;

    static {
        NotifyType[] notifyTypeArray = new NotifyType[4];
        NotifyType[] notifyTypeArray2 = notifyTypeArray;
        notifyTypeArray[0] = SUCCESS = new NotifyType();
        notifyTypeArray[1] = ERROR = new NotifyType();
        notifyTypeArray[2] = WARNING = new NotifyType();
        notifyTypeArray[3] = INFO = new NotifyType();
        $VALUES = notifyTypeArray;
    }

    public static NotifyType[] values() {
        return (NotifyType[])$VALUES.clone();
    }

    public static NotifyType valueOf(String string) {
        return Enum.valueOf(NotifyType.class, string);
    }
}

