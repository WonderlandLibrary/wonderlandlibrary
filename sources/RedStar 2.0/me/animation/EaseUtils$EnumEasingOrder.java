package me.animation;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u00002\b0\u00000B\b0¢R0¢\b\n\u0000\bj\bj\b\bj\b\t¨\n"}, d2={"Lme/animation/EaseUtils$EnumEasingOrder;", "", "methodName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getMethodName", "()Ljava/lang/String;", "FAST_AT_START", "FAST_AT_END", "FAST_AT_START_AND_END", "Pride"})
public static final class EaseUtils$EnumEasingOrder
extends Enum<EaseUtils$EnumEasingOrder> {
    public static final EaseUtils$EnumEasingOrder FAST_AT_START;
    public static final EaseUtils$EnumEasingOrder FAST_AT_END;
    public static final EaseUtils$EnumEasingOrder FAST_AT_START_AND_END;
    private static final EaseUtils$EnumEasingOrder[] $VALUES;
    @NotNull
    private final String methodName;

    static {
        EaseUtils$EnumEasingOrder[] enumEasingOrderArray = new EaseUtils$EnumEasingOrder[3];
        EaseUtils$EnumEasingOrder[] enumEasingOrderArray2 = enumEasingOrderArray;
        enumEasingOrderArray[0] = FAST_AT_START = new EaseUtils$EnumEasingOrder("Out");
        enumEasingOrderArray[1] = FAST_AT_END = new EaseUtils$EnumEasingOrder("In");
        enumEasingOrderArray[2] = FAST_AT_START_AND_END = new EaseUtils$EnumEasingOrder("InOut");
        $VALUES = enumEasingOrderArray;
    }

    @NotNull
    public final String getMethodName() {
        return this.methodName;
    }

    private EaseUtils$EnumEasingOrder(String methodName) {
        this.methodName = methodName;
    }

    public static EaseUtils$EnumEasingOrder[] values() {
        return (EaseUtils$EnumEasingOrder[])$VALUES.clone();
    }

    public static EaseUtils$EnumEasingOrder valueOf(String string) {
        return Enum.valueOf(EaseUtils$EnumEasingOrder.class, string);
    }
}
