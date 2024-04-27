package net.ccbluex.liquidbounce.api.minecraft.event;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\u0000\n\b\bf\u000020:¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "", "WAction", "Pride"})
public interface IClickEvent {

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "", "(Ljava/lang/String;I)V", "OPEN_URL", "Pride"})
    public static final class WAction
    extends Enum<WAction> {
        public static final WAction OPEN_URL;
        private static final WAction[] $VALUES;

        static {
            WAction[] wActionArray = new WAction[1];
            WAction[] wActionArray2 = wActionArray;
            wActionArray[0] = OPEN_URL = new WAction();
            $VALUES = wActionArray;
        }

        public static WAction[] values() {
            return (WAction[])$VALUES.clone();
        }

        public static WAction valueOf(String string) {
            return Enum.valueOf(WAction.class, string);
        }
    }
}
