package me.animation;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u00002\b0\u00000B\b¢R0¢\b\n\u0000\bj\bj\b\bj\b\tj\b\nj\bj\b\fj\b\rj\bj\bj\bj\b¨"}, d2={"Lme/animation/EaseUtils$EnumEasingType;", "", "(Ljava/lang/String;I)V", "friendlyName", "", "getFriendlyName", "()Ljava/lang/String;", "NONE", "SINE", "QUAD", "CUBIC", "QUART", "QUINT", "EXPO", "CIRC", "BACK", "ELASTIC", "BOUNCE", "Pride"})
public static final class EaseUtils$EnumEasingType
extends Enum<EaseUtils$EnumEasingType> {
    public static final EaseUtils$EnumEasingType NONE;
    public static final EaseUtils$EnumEasingType SINE;
    public static final EaseUtils$EnumEasingType QUAD;
    public static final EaseUtils$EnumEasingType CUBIC;
    public static final EaseUtils$EnumEasingType QUART;
    public static final EaseUtils$EnumEasingType QUINT;
    public static final EaseUtils$EnumEasingType EXPO;
    public static final EaseUtils$EnumEasingType CIRC;
    public static final EaseUtils$EnumEasingType BACK;
    public static final EaseUtils$EnumEasingType ELASTIC;
    public static final EaseUtils$EnumEasingType BOUNCE;
    private static final EaseUtils$EnumEasingType[] $VALUES;
    @NotNull
    private final String friendlyName;

    static {
        EaseUtils$EnumEasingType[] enumEasingTypeArray = new EaseUtils$EnumEasingType[11];
        EaseUtils$EnumEasingType[] enumEasingTypeArray2 = enumEasingTypeArray;
        enumEasingTypeArray[0] = NONE = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[1] = SINE = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[2] = QUAD = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[3] = CUBIC = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[4] = QUART = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[5] = QUINT = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[6] = EXPO = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[7] = CIRC = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[8] = BACK = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[9] = ELASTIC = new EaseUtils$EnumEasingType();
        enumEasingTypeArray[10] = BOUNCE = new EaseUtils$EnumEasingType();
        $VALUES = enumEasingTypeArray;
    }

    @NotNull
    public final String getFriendlyName() {
        return this.friendlyName;
    }

    private EaseUtils$EnumEasingType() {
        String string;
        String string2 = this.name();
        int n = 0;
        int n2 = 1;
        StringBuilder stringBuilder = new StringBuilder();
        EaseUtils$EnumEasingType enumEasingType = this;
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.substring(n, n2);
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        string2 = string = string4;
        n = 0;
        String string5 = string2;
        if (string5 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string6 = string5.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(string6, "(this as java.lang.String).toUpperCase()");
        string = string6;
        string2 = this.name();
        n = 1;
        n2 = this.name().length();
        stringBuilder = stringBuilder.append(string);
        bl = false;
        String string7 = string2;
        if (string7 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string8 = string7.substring(n, n2);
        Intrinsics.checkExpressionValueIsNotNull(string8, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        string2 = string = string8;
        n = 0;
        String string9 = string2;
        if (string9 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string10 = string9.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string10, "(this as java.lang.String).toLowerCase()");
        string = string10;
        enumEasingType.friendlyName = stringBuilder.append(string).toString();
    }

    public static EaseUtils$EnumEasingType[] values() {
        return (EaseUtils$EnumEasingType[])$VALUES.clone();
    }

    public static EaseUtils$EnumEasingType valueOf(String string) {
        return Enum.valueOf(EaseUtils$EnumEasingType.class, string);
    }
}
