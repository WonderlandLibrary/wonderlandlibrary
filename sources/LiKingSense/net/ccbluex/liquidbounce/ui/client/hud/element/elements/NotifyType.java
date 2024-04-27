/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/NotifyType;", "", "icon", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getIcon", "()Ljava/lang/String;", "setIcon", "(Ljava/lang/String;)V", "SUCCESS", "ERROR", "WARNING", "INFO", "LiKingSense"})
public final class NotifyType
extends Enum<NotifyType> {
    public static final /* enum */ NotifyType SUCCESS;
    public static final /* enum */ NotifyType ERROR;
    public static final /* enum */ NotifyType WARNING;
    public static final /* enum */ NotifyType INFO;
    private static final /* synthetic */ NotifyType[] $VALUES;
    @NotNull
    private String icon;

    static {
        NotifyType[] notifyTypeArray = new NotifyType[4];
        NotifyType[] notifyTypeArray2 = notifyTypeArray;
        notifyTypeArray[0] = SUCCESS = new NotifyType("check-circle");
        notifyTypeArray[1] = ERROR = new NotifyType("close-circle");
        notifyTypeArray[2] = WARNING = new NotifyType("warning");
        notifyTypeArray[3] = INFO = new NotifyType("information");
        $VALUES = notifyTypeArray;
    }

    @NotNull
    public final String getIcon() {
        return this.icon;
    }

    public final void setIcon(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull((Object)string, (String)"<set-?>");
        this.icon = string;
    }

    private NotifyType(String icon) {
        this.icon = icon;
    }

    public static NotifyType[] values() {
        return (NotifyType[])$VALUES.clone();
    }

    public static NotifyType valueOf(String string) {
        return Enum.valueOf(NotifyType.class, string);
    }
}

