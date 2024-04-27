/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;

public final class NotifyType
extends Enum {
    public static final /* enum */ NotifyType WARNING;
    private Color renderColor;
    private static final NotifyType[] $VALUES;
    public static final /* enum */ NotifyType ERROR;
    public static final /* enum */ NotifyType INFO;
    public static final /* enum */ NotifyType SUCCESS;

    public static NotifyType[] values() {
        return (NotifyType[])$VALUES.clone();
    }

    static {
        NotifyType[] notifyTypeArray = new NotifyType[4];
        NotifyType[] notifyTypeArray2 = notifyTypeArray;
        notifyTypeArray[0] = SUCCESS = new NotifyType("SUCCESS", 0, new Color(0, 157, 255, 240));
        notifyTypeArray[1] = ERROR = new NotifyType("ERROR", 1, new Color(255, 0, 0, 200));
        notifyTypeArray[2] = WARNING = new NotifyType("WARNING", 2, new Color(16121088));
        notifyTypeArray[3] = INFO = new NotifyType("INFO", 3, new Color(16121088));
        $VALUES = notifyTypeArray;
    }

    public final Color getRenderColor() {
        return this.renderColor;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private NotifyType() {
        void var3_1;
        void var2_-1;
        void var1_-1;
        this.renderColor = var3_1;
    }

    public static NotifyType valueOf(String string) {
        return Enum.valueOf(NotifyType.class, string);
    }

    public final void setRenderColor(Color color) {
        this.renderColor = color;
    }
}

