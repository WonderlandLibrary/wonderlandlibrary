/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  org.apache.logging.log4j.Level
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.render;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002+,B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010 \u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010!\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\"\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u0010\u0010#\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004H\u0007J\u000e\u0010$\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010%\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)J\u000e\u0010*\u001a\u00020'2\u0006\u0010(\u001a\u00020)\u00a8\u0006-"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/EaseUtils;", "", "()V", "apply", "", "type", "Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingType;", "order", "Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingOrder;", "value", "easeInBack", "x", "easeInCirc", "easeInCubic", "easeInElastic", "easeInExpo", "easeInOutBack", "easeInOutCirc", "easeInOutCubic", "easeInOutElastic", "easeInOutExpo", "easeInOutQuad", "easeInOutQuart", "easeInOutQuint", "easeInOutSine", "easeInQuad", "easeInQuart", "easeInQuint", "easeInSine", "easeOutBack", "easeOutCirc", "easeOutCubic", "easeOutElastic", "easeOutExpo", "easeOutQuad", "easeOutQuart", "easeOutQuint", "easeOutSine", "getEnumEasingList", "Lnet/ccbluex/liquidbounce/value/ListValue;", "name", "", "getEnumEasingOrderList", "EnumEasingOrder", "EnumEasingType", "LiKingSense"})
public final class EaseUtils {
    public static final EaseUtils INSTANCE;

    public final double easeInSine(double x) {
        double d = x * Math.PI / (double)2;
        double d2 = 1.0;
        boolean bl = false;
        double d3 = Math.cos(d);
        return d2 - d3;
    }

    public final double easeOutSine(double x) {
        double d = x * Math.PI / (double)2;
        boolean bl = false;
        return Math.sin(d);
    }

    public final double easeInOutSine(double x) {
        double d = Math.PI * x;
        boolean bl = false;
        return -(Math.cos(d) - 1.0) / (double)2;
    }

    public final double easeInQuad(double x) {
        return x * x;
    }

    public final double easeOutQuad(double x) {
        return 1.0 - (1.0 - x) * (1.0 - x);
    }

    public final double easeInOutQuad(double x) {
        double d;
        if (x < 0.5) {
            d = (double)2 * x * x;
        } else {
            double d2 = (double)-2 * x + (double)2;
            int n = 2;
            double d3 = 1.0;
            boolean bl = false;
            double d4 = Math.pow(d2, n);
            d = d3 - d4 / (double)2;
        }
        return d;
    }

    public final double easeInCubic(double x) {
        return x * x * x;
    }

    public final double easeOutCubic(double x) {
        double d = 1.0 - x;
        int n = 3;
        double d2 = 1.0;
        boolean bl = false;
        double d3 = Math.pow(d, n);
        return d2 - d3;
    }

    public final double easeInOutCubic(double x) {
        double d;
        if (x < 0.5) {
            d = (double)4 * x * x * x;
        } else {
            double d2 = (double)-2 * x + (double)2;
            int n = 3;
            double d3 = 1.0;
            boolean bl = false;
            double d4 = Math.pow(d2, n);
            d = d3 - d4 / (double)2;
        }
        return d;
    }

    public final double easeInQuart(double x) {
        return x * x * x * x;
    }

    @JvmStatic
    public static final double easeOutQuart(double x) {
        double d = 1.0 - x;
        int n = 4;
        double d2 = 1.0;
        boolean bl = false;
        double d3 = Math.pow(d, n);
        return d2 - d3;
    }

    public final double easeInOutQuart(double x) {
        double d;
        if (x < 0.5) {
            d = (double)8 * x * x * x * x;
        } else {
            double d2 = (double)-2 * x + (double)2;
            int n = 4;
            double d3 = 1.0;
            boolean bl = false;
            double d4 = Math.pow(d2, n);
            d = d3 - d4 / (double)2;
        }
        return d;
    }

    public final double easeInQuint(double x) {
        return x * x * x * x * x;
    }

    public final double easeOutQuint(double x) {
        double d = 1.0 - x;
        int n = 5;
        double d2 = 1.0;
        boolean bl = false;
        double d3 = Math.pow(d, n);
        return d2 - d3;
    }

    public final double easeInOutQuint(double x) {
        double d;
        if (x < 0.5) {
            d = (double)16 * x * x * x * x * x;
        } else {
            double d2 = (double)-2 * x + (double)2;
            int n = 5;
            double d3 = 1.0;
            boolean bl = false;
            double d4 = Math.pow(d2, n);
            d = d3 - d4 / (double)2;
        }
        return d;
    }

    public final double easeInExpo(double x) {
        double d;
        if (x == 0.0) {
            d = 0.0;
        } else {
            double d2 = 2.0;
            double d3 = (double)10 * x - (double)10;
            boolean bl = false;
            d = Math.pow(d2, d3);
        }
        return d;
    }

    public final double easeOutExpo(double x) {
        double d;
        if (x == 1.0) {
            d = 1.0;
        } else {
            double d2 = 2.0;
            double d3 = (double)-10 * x;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d2, d3);
            d = d4 - d5;
        }
        return d;
    }

    public final double easeInOutExpo(double x) {
        double d;
        if (x == 0.0) {
            d = 0.0;
        } else if (x == 1.0) {
            d = 1.0;
        } else if (x < 0.5) {
            double d2 = 2.0;
            double d3 = (double)20 * x - (double)10;
            boolean bl = false;
            d = Math.pow(d2, d3) / (double)2;
        } else {
            double d4 = 2.0;
            double d5 = (double)-20 * x + (double)10;
            double d6 = 2;
            boolean bl = false;
            double d7 = Math.pow(d4, d5);
            d = (d6 - d7) / (double)2;
        }
        return d;
    }

    public final double easeInCirc(double x) {
        double d = x;
        int n = 2;
        double d2 = 1.0;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d, n);
        d = d2 - d4;
        n = 0;
        d2 = Math.sqrt(d);
        return d3 - d2;
    }

    public final double easeOutCirc(double x) {
        double d = x - 1.0;
        int n = 2;
        double d2 = 1.0;
        boolean bl = false;
        double d3 = Math.pow(d, n);
        d = d2 - d3;
        n = 0;
        return Math.sqrt(d);
    }

    public final double easeInOutCirc(double x) {
        double d;
        if (x < 0.5) {
            double d2 = (double)2 * x;
            int n = 2;
            double d3 = 1.0;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d2, n);
            d2 = d3 - d5;
            n = 0;
            d3 = Math.sqrt(d2);
            d = (d4 - d3) / (double)2;
        } else {
            double d6 = (double)-2 * x + (double)2;
            int n = 2;
            double d7 = 1.0;
            boolean bl = false;
            double d8 = Math.pow(d6, n);
            d6 = d7 - d8;
            n = 0;
            d = (Math.sqrt(d6) + 1.0) / (double)2;
        }
        return d;
    }

    public final double easeInBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        return c3 * x * x * x - c1 * x * x;
    }

    public final double easeOutBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1.0;
        double d = x - 1.0;
        int n = 3;
        double d2 = c3;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d, n);
        double d5 = d3 + d2 * d4;
        d = x - 1.0;
        n = 2;
        d2 = c1;
        d3 = d5;
        bl = false;
        d4 = Math.pow(d, n);
        return d3 + d2 * d4;
    }

    public final double easeInOutBack(double x) {
        double d;
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        if (x < 0.5) {
            double d2 = (double)2 * x;
            int n = 2;
            boolean bl = false;
            d = Math.pow(d2, n) * ((c2 + 1.0) * (double)2 * x - c2) / (double)2;
        } else {
            double d3 = (double)2 * x - (double)2;
            int n = 2;
            boolean bl = false;
            d = (Math.pow(d3, n) * ((c2 + 1.0) * (x * (double)2 - (double)2) + c2) + (double)2) / (double)2;
        }
        return d;
    }

    public final double easeInElastic(double x) {
        double d;
        double c4 = 2.0943951023931953;
        if (x == 0.0) {
            d = 0.0;
        } else if (x == 1.0) {
            d = 1.0;
        } else {
            double d2 = -2.0;
            double d3 = (double)10 * x - (double)10;
            boolean bl = false;
            double d4 = Math.pow(d2, d3);
            d2 = (x * (double)10 - 10.75) * c4;
            double d5 = d4;
            boolean bl2 = false;
            double d6 = Math.sin(d2);
            d = d5 * d6;
        }
        return d;
    }

    public final double easeOutElastic(double x) {
        double d;
        double c4 = 2.0943951023931953;
        if (x == 0.0) {
            d = 0.0;
        } else if (x == 1.0) {
            d = 1.0;
        } else {
            double d2 = 2.0;
            double d3 = (double)-10 * x;
            boolean bl = false;
            double d4 = Math.pow(d2, d3);
            d2 = (x * (double)10 - 0.75) * c4;
            double d5 = d4;
            boolean bl2 = false;
            double d6 = Math.sin(d2);
            d = d5 * d6 + 1.0;
        }
        return d;
    }

    public final double easeInOutElastic(double x) {
        double d;
        double c5 = 1.3962634015954636;
        if (x == 0.0) {
            d = 0.0;
        } else if (x == 1.0) {
            d = 1.0;
        } else if (x < 0.5) {
            double d2 = 2.0;
            double d3 = (double)20 * x - (double)10;
            boolean bl = false;
            double d4 = Math.pow(d2, d3);
            d2 = ((double)20 * x - 11.125) * c5;
            double d5 = d4;
            boolean bl2 = false;
            double d6 = Math.sin(d2);
            d = -(d5 * d6) / (double)2;
        } else {
            double d7 = 2.0;
            double d8 = (double)-20 * x + (double)10;
            boolean bl = false;
            double d9 = Math.pow(d7, d8);
            d7 = ((double)20 * x - 11.125) * c5;
            double d10 = d9;
            boolean bl3 = false;
            double d11 = Math.sin(d7);
            d = d10 * d11 / (double)2 + 1.0;
        }
        return d;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final ListValue getEnumEasingList(@NotNull String name) {
        void $this$toTypedArray$iv;
        Collection<String> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        EnumEasingType[] enumEasingTypeArray = EnumEasingType.values();
        String string = name;
        boolean $i$f$map = false;
        void var4_5 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var7_8 = $this$mapTo$iv$iv;
        int n = ((void)var7_8).length;
        for (int i = 0; i < n; ++i) {
            void it;
            void item$iv$iv;
            void var11_12 = item$iv$iv = var7_8[i];
            collection = destination$iv$iv;
            boolean bl = false;
            String string2 = it.toString();
            collection.add(string2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = stringArray;
        String string3 = EnumEasingType.SINE.toString();
        String[] stringArray2 = (String[])collection;
        String string4 = string;
        return new ListValue(string4, stringArray2, string3);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final ListValue getEnumEasingOrderList(@NotNull String name) {
        void $this$toTypedArray$iv;
        Collection<String> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        EnumEasingOrder[] enumEasingOrderArray = EnumEasingOrder.values();
        String string = name;
        boolean $i$f$map = false;
        void var4_5 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var7_8 = $this$mapTo$iv$iv;
        int n = ((void)var7_8).length;
        for (int i = 0; i < n; ++i) {
            void it;
            void item$iv$iv;
            void var11_12 = item$iv$iv = var7_8[i];
            collection = destination$iv$iv;
            boolean bl = false;
            String string2 = it.toString();
            collection.add(string2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = stringArray;
        String string3 = EnumEasingOrder.FAST_AT_START.toString();
        String[] stringArray2 = (String[])collection;
        String string4 = string;
        return new ListValue(string4, stringArray2, string3);
    }

    public final double apply(@NotNull EnumEasingType type, @NotNull EnumEasingOrder order, double value) {
        double d;
        Method method;
        boolean bl;
        Method[] methodArray;
        String methodName;
        block5: {
            Intrinsics.checkParameterIsNotNull((Object)((Object)type), (String)"type");
            Intrinsics.checkParameterIsNotNull((Object)((Object)order), (String)"order");
            if (type == EnumEasingType.NONE) {
                return value;
            }
            methodName = "ease" + order.getMethodName() + type.getFriendlyName();
            Method[] methodArray2 = this.getClass().getDeclaredMethods();
            Intrinsics.checkExpressionValueIsNotNull((Object)methodArray2, (String)"this.javaClass.declaredMethods");
            methodArray = methodArray2;
            bl = false;
            Method[] methodArray3 = methodArray;
            boolean bl2 = false;
            Method[] methodArray4 = methodArray3;
            int n = methodArray4.length;
            for (int i = 0; i < n; ++i) {
                Method method2;
                Method it = method2 = methodArray4[i];
                boolean bl3 = false;
                Method method3 = it;
                Intrinsics.checkExpressionValueIsNotNull((Object)method3, (String)"it");
                if (!method3.getName().equals(methodName)) continue;
                method = method2;
                break block5;
            }
            method = null;
        }
        methodArray = method;
        bl = false;
        boolean bl4 = false;
        Method[] it = methodArray;
        boolean bl5 = false;
        if (it != null) {
            Object object = it.invoke(INSTANCE, value);
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Double");
            }
            d = (Double)object;
        } else {
            ClientUtils.getLogger().log(Level.ERROR, "Cannot found easing method: " + methodName);
            d = value;
        }
        return d;
    }

    private EaseUtils() {
    }

    static {
        EaseUtils easeUtils;
        INSTANCE = easeUtils = new EaseUtils();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingType;", "", "(Ljava/lang/String;I)V", "friendlyName", "", "getFriendlyName", "()Ljava/lang/String;", "NONE", "SINE", "QUAD", "CUBIC", "QUART", "QUINT", "EXPO", "CIRC", "BACK", "ELASTIC", "LiKingSense"})
    public static final class EnumEasingType
    extends Enum<EnumEasingType> {
        public static final /* enum */ EnumEasingType NONE;
        public static final /* enum */ EnumEasingType SINE;
        public static final /* enum */ EnumEasingType QUAD;
        public static final /* enum */ EnumEasingType CUBIC;
        public static final /* enum */ EnumEasingType QUART;
        public static final /* enum */ EnumEasingType QUINT;
        public static final /* enum */ EnumEasingType EXPO;
        public static final /* enum */ EnumEasingType CIRC;
        public static final /* enum */ EnumEasingType BACK;
        public static final /* enum */ EnumEasingType ELASTIC;
        private static final /* synthetic */ EnumEasingType[] $VALUES;
        @NotNull
        private final String friendlyName;

        static {
            EnumEasingType[] enumEasingTypeArray = new EnumEasingType[10];
            EnumEasingType[] enumEasingTypeArray2 = enumEasingTypeArray;
            enumEasingTypeArray[0] = NONE = new EnumEasingType();
            enumEasingTypeArray[1] = SINE = new EnumEasingType();
            enumEasingTypeArray[2] = QUAD = new EnumEasingType();
            enumEasingTypeArray[3] = CUBIC = new EnumEasingType();
            enumEasingTypeArray[4] = QUART = new EnumEasingType();
            enumEasingTypeArray[5] = QUINT = new EnumEasingType();
            enumEasingTypeArray[6] = EXPO = new EnumEasingType();
            enumEasingTypeArray[7] = CIRC = new EnumEasingType();
            enumEasingTypeArray[8] = BACK = new EnumEasingType();
            enumEasingTypeArray[9] = ELASTIC = new EnumEasingType();
            $VALUES = enumEasingTypeArray;
        }

        @NotNull
        public final String getFriendlyName() {
            return this.friendlyName;
        }

        private EnumEasingType() {
            String string;
            String string2 = this.name();
            int n = 0;
            int n2 = 1;
            StringBuilder stringBuilder = new StringBuilder();
            EnumEasingType enumEasingType = this;
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string3.substring(n, n2);
            Intrinsics.checkExpressionValueIsNotNull((Object)string4, (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            string2 = string = string4;
            n = 0;
            String string5 = string2;
            if (string5 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string6 = string5.toUpperCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string6, (String)"(this as java.lang.String).toUpperCase()");
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
            Intrinsics.checkExpressionValueIsNotNull((Object)string8, (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            string2 = string = string8;
            n = 0;
            String string9 = string2;
            if (string9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string10 = string9.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)string10, (String)"(this as java.lang.String).toLowerCase()");
            string = string10;
            enumEasingType.friendlyName = stringBuilder.append(string).toString();
        }

        public static EnumEasingType[] values() {
            return (EnumEasingType[])$VALUES.clone();
        }

        public static EnumEasingType valueOf(String string) {
            return Enum.valueOf(EnumEasingType.class, string);
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/EaseUtils$EnumEasingOrder;", "", "methodName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getMethodName", "()Ljava/lang/String;", "FAST_AT_START", "FAST_AT_END", "FAST_AT_START_AND_END", "LiKingSense"})
    public static final class EnumEasingOrder
    extends Enum<EnumEasingOrder> {
        public static final /* enum */ EnumEasingOrder FAST_AT_START;
        public static final /* enum */ EnumEasingOrder FAST_AT_END;
        public static final /* enum */ EnumEasingOrder FAST_AT_START_AND_END;
        private static final /* synthetic */ EnumEasingOrder[] $VALUES;
        @NotNull
        private final String methodName;

        static {
            EnumEasingOrder[] enumEasingOrderArray = new EnumEasingOrder[3];
            EnumEasingOrder[] enumEasingOrderArray2 = enumEasingOrderArray;
            enumEasingOrderArray[0] = FAST_AT_START = new EnumEasingOrder("Out");
            enumEasingOrderArray[1] = FAST_AT_END = new EnumEasingOrder("In");
            enumEasingOrderArray[2] = FAST_AT_START_AND_END = new EnumEasingOrder("InOut");
            $VALUES = enumEasingOrderArray;
        }

        @NotNull
        public final String getMethodName() {
            return this.methodName;
        }

        private EnumEasingOrder(String methodName) {
            this.methodName = methodName;
        }

        public static EnumEasingOrder[] values() {
            return (EnumEasingOrder[])$VALUES.clone();
        }

        public static EnumEasingOrder valueOf(String string) {
            return Enum.valueOf(EnumEasingOrder.class, string);
        }
    }
}

