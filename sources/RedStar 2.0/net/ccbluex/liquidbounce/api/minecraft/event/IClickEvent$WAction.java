package net.ccbluex.liquidbounce.api.minecraft.event;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "", "(Ljava/lang/String;I)V", "OPEN_URL", "Pride"})
public static final class IClickEvent$WAction
extends Enum<IClickEvent$WAction> {
    public static final IClickEvent$WAction OPEN_URL;
    private static final IClickEvent$WAction[] $VALUES;

    static {
        IClickEvent$WAction[] wActionArray = new IClickEvent$WAction[1];
        IClickEvent$WAction[] wActionArray2 = wActionArray;
        wActionArray[0] = OPEN_URL = new IClickEvent$WAction();
        $VALUES = wActionArray;
    }

    public static IClickEvent$WAction[] values() {
        return (IClickEvent$WAction[])$VALUES.clone();
    }

    public static IClickEvent$WAction valueOf(String string) {
        return Enum.valueOf(IClickEvent$WAction.class, string);
    }
}
