/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.JvmStatic
 *  org.apache.logging.log4j.Level
 */
package net.ccbluex.liquidbounce.utils.render;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import org.apache.logging.log4j.Level;

public final class EaseUtils {
    public static final EaseUtils INSTANCE;

    public final double easeInQuad(double d) {
        return d * d;
    }

    public final double easeOutCirc(double d) {
        double d2 = d - 1.0;
        int n = 2;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d2, n);
        d2 = d3 - d4;
        n = 0;
        return Math.sqrt(d2);
    }

    public final double easeInQuint(double d) {
        return d * d * d * d * d;
    }

    public final double easeInOutCubic(double d) {
        double d2;
        if (d < 0.5) {
            d2 = (double)4 * d * d * d;
        } else {
            double d3 = (double)-2 * d + (double)2;
            int n = 3;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d3, n);
            d2 = d4 - d5 / (double)2;
        }
        return d2;
    }

    public final double easeInCubic(double d) {
        return d * d * d;
    }

    public final double easeInOutExpo(double d) {
        double d2;
        if (d == 0.0) {
            d2 = 0.0;
        } else if (d == 1.0) {
            d2 = 1.0;
        } else if (d < 0.5) {
            double d3 = 2.0;
            double d4 = (double)20 * d - (double)10;
            boolean bl = false;
            d2 = Math.pow(d3, d4) / (double)2;
        } else {
            double d5 = 2.0;
            double d6 = (double)-20 * d + (double)10;
            double d7 = 2;
            boolean bl = false;
            double d8 = Math.pow(d5, d6);
            d2 = (d7 - d8) / (double)2;
        }
        return d2;
    }

    public final double easeInSine(double d) {
        double d2 = d * Math.PI / (double)2;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.cos(d2);
        return d3 - d4;
    }

    public final double apply(EnumEasingType enumEasingType, EnumEasingOrder enumEasingOrder, double d) {
        double d2;
        Method method;
        boolean bl;
        Method[] methodArray;
        String string;
        block5: {
            if (enumEasingType == EnumEasingType.NONE) {
                return d;
            }
            string = "ease" + enumEasingOrder.getMethodName() + enumEasingType.getFriendlyName();
            methodArray = this.getClass().getDeclaredMethods();
            bl = false;
            Method[] methodArray2 = methodArray;
            boolean bl2 = false;
            Method[] methodArray3 = methodArray2;
            int n = methodArray3.length;
            for (int i = 0; i < n; ++i) {
                Method method2;
                Method method3 = method2 = methodArray3[i];
                boolean bl3 = false;
                if (!method3.getName().equals(string)) continue;
                method = method2;
                break block5;
            }
            method = null;
        }
        methodArray = method;
        bl = false;
        boolean bl4 = false;
        Method[] methodArray4 = methodArray;
        boolean bl5 = false;
        if (methodArray4 != null) {
            Object object = methodArray4.invoke(INSTANCE, d);
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Double");
            }
            d2 = (Double)object;
        } else {
            ClientUtils.getLogger().log(Level.ERROR, "Cannot found easing method: " + string);
            d2 = d;
        }
        return d2;
    }

    public final double easeOutExpo(double d) {
        double d2;
        if (d == 1.0) {
            d2 = 1.0;
        } else {
            double d3 = 2.0;
            double d4 = (double)-10 * d;
            double d5 = 1.0;
            boolean bl = false;
            double d6 = Math.pow(d3, d4);
            d2 = d5 - d6;
        }
        return d2;
    }

    private EaseUtils() {
    }

    public final double easeInOutCirc(double d) {
        double d2;
        if (d < 0.5) {
            double d3 = (double)2 * d;
            int n = 2;
            double d4 = 1.0;
            double d5 = 1.0;
            boolean bl = false;
            double d6 = Math.pow(d3, n);
            d3 = d4 - d6;
            n = 0;
            d4 = Math.sqrt(d3);
            d2 = (d5 - d4) / (double)2;
        } else {
            double d7 = (double)-2 * d + (double)2;
            int n = 2;
            double d8 = 1.0;
            boolean bl = false;
            double d9 = Math.pow(d7, n);
            d7 = d8 - d9;
            n = 0;
            d2 = (Math.sqrt(d7) + 1.0) / (double)2;
        }
        return d2;
    }

    @JvmStatic
    public static final double easeOutQuart(double d) {
        double d2 = 1.0 - d;
        int n = 4;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d2, n);
        return d3 - d4;
    }

    public final double easeInOutElastic(double d) {
        double d2;
        double d3 = 1.3962634015954636;
        if (d == 0.0) {
            d2 = 0.0;
        } else if (d == 1.0) {
            d2 = 1.0;
        } else if (d < 0.5) {
            double d4 = 2.0;
            double d5 = (double)20 * d - (double)10;
            boolean bl = false;
            double d6 = Math.pow(d4, d5);
            d4 = ((double)20 * d - 11.125) * d3;
            double d7 = d6;
            boolean bl2 = false;
            double d8 = Math.sin(d4);
            d2 = -(d7 * d8) / (double)2;
        } else {
            double d9 = 2.0;
            double d10 = (double)-20 * d + (double)10;
            boolean bl = false;
            double d11 = Math.pow(d9, d10);
            d9 = ((double)20 * d - 11.125) * d3;
            double d12 = d11;
            boolean bl3 = false;
            double d13 = Math.sin(d9);
            d2 = d12 * d13 / (double)2 + 1.0;
        }
        return d2;
    }

    public final double easeInElastic(double d) {
        double d2;
        double d3 = 2.0943951023931953;
        if (d == 0.0) {
            d2 = 0.0;
        } else if (d == 1.0) {
            d2 = 1.0;
        } else {
            double d4 = -2.0;
            double d5 = (double)10 * d - (double)10;
            boolean bl = false;
            double d6 = Math.pow(d4, d5);
            d4 = (d * (double)10 - 10.75) * d3;
            double d7 = d6;
            boolean bl2 = false;
            double d8 = Math.sin(d4);
            d2 = d7 * d8;
        }
        return d2;
    }

    public final double easeOutSine(double d) {
        double d2 = d * Math.PI / (double)2;
        boolean bl = false;
        return Math.sin(d2);
    }

    public final double easeOutQuad(double d) {
        return 1.0 - (1.0 - d) * (1.0 - d);
    }

    public final double easeOutQuint(double d) {
        double d2 = 1.0 - d;
        int n = 5;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d2, n);
        return d3 - d4;
    }

    public final double easeInOutQuad(double d) {
        double d2;
        if (d < 0.5) {
            d2 = (double)2 * d * d;
        } else {
            double d3 = (double)-2 * d + (double)2;
            int n = 2;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d3, n);
            d2 = d4 - d5 / (double)2;
        }
        return d2;
    }

    public final double easeOutCubic(double d) {
        double d2 = 1.0 - d;
        int n = 3;
        double d3 = 1.0;
        boolean bl = false;
        double d4 = Math.pow(d2, n);
        return d3 - d4;
    }

    public final double easeInExpo(double d) {
        double d2;
        if (d == 0.0) {
            d2 = 0.0;
        } else {
            double d3 = 2.0;
            double d4 = (double)10 * d - (double)10;
            boolean bl = false;
            d2 = Math.pow(d3, d4);
        }
        return d2;
    }

    static {
        EaseUtils easeUtils;
        INSTANCE = easeUtils = new EaseUtils();
    }

    public final double easeInCirc(double d) {
        double d2 = d;
        int n = 2;
        double d3 = 1.0;
        double d4 = 1.0;
        boolean bl = false;
        double d5 = Math.pow(d2, n);
        d2 = d3 - d5;
        n = 0;
        d3 = Math.sqrt(d2);
        return d4 - d3;
    }

    public final double easeInOutSine(double d) {
        double d2 = Math.PI * d;
        boolean bl = false;
        return -(Math.cos(d2) - 1.0) / (double)2;
    }

    public final double easeInOutQuart(double d) {
        double d2;
        if (d < 0.5) {
            d2 = (double)8 * d * d * d * d;
        } else {
            double d3 = (double)-2 * d + (double)2;
            int n = 4;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d3, n);
            d2 = d4 - d5 / (double)2;
        }
        return d2;
    }

    public final double easeInOutBack(double d) {
        double d2;
        double d3 = 1.70158;
        double d4 = d3 * 1.525;
        if (d < 0.5) {
            double d5 = (double)2 * d;
            int n = 2;
            boolean bl = false;
            d2 = Math.pow(d5, n) * ((d4 + 1.0) * (double)2 * d - d4) / (double)2;
        } else {
            double d6 = (double)2 * d - (double)2;
            int n = 2;
            boolean bl = false;
            d2 = (Math.pow(d6, n) * ((d4 + 1.0) * (d * (double)2 - (double)2) + d4) + (double)2) / (double)2;
        }
        return d2;
    }

    public final double easeOutBack(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        double d4 = d - 1.0;
        int n = 3;
        double d5 = d3;
        double d6 = 1.0;
        boolean bl = false;
        double d7 = Math.pow(d4, n);
        double d8 = d6 + d5 * d7;
        d4 = d - 1.0;
        n = 2;
        d5 = d2;
        d6 = d8;
        bl = false;
        d7 = Math.pow(d4, n);
        return d6 + d5 * d7;
    }

    public final double easeOutElastic(double d) {
        double d2;
        double d3 = 2.0943951023931953;
        if (d == 0.0) {
            d2 = 0.0;
        } else if (d == 1.0) {
            d2 = 1.0;
        } else {
            double d4 = 2.0;
            double d5 = (double)-10 * d;
            boolean bl = false;
            double d6 = Math.pow(d4, d5);
            d4 = (d * (double)10 - 0.75) * d3;
            double d7 = d6;
            boolean bl2 = false;
            double d8 = Math.sin(d4);
            d2 = d7 * d8 + 1.0;
        }
        return d2;
    }

    public final double easeInBack(double d) {
        double d2 = 1.70158;
        double d3 = d2 + 1.0;
        return d3 * d * d * d - d2 * d * d;
    }

    public final ListValue getEnumEasingOrderList(String string) {
        Collection<String> collection;
        Object object = EnumEasingOrder.values();
        String string2 = string;
        boolean bl = false;
        EnumEasingOrder[] enumEasingOrderArray = object;
        Collection collection2 = new ArrayList(((EnumEasingOrder[])object).length);
        boolean bl2 = false;
        EnumEasingOrder[] enumEasingOrderArray2 = enumEasingOrderArray;
        int n = enumEasingOrderArray2.length;
        for (int i = 0; i < n; ++i) {
            EnumEasingOrder enumEasingOrder;
            EnumEasingOrder enumEasingOrder2 = enumEasingOrder = enumEasingOrderArray2[i];
            collection = collection2;
            boolean bl3 = false;
            String string3 = enumEasingOrder2.toString();
            collection.add(string3);
        }
        collection = (List)collection2;
        object = collection;
        bl = false;
        enumEasingOrderArray = object;
        String[] stringArray = enumEasingOrderArray.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = stringArray;
        String string4 = EnumEasingOrder.FAST_AT_START.toString();
        String[] stringArray2 = (String[])collection;
        String string5 = string2;
        return new ListValue(string5, stringArray2, string4);
    }

    public final double easeInOutQuint(double d) {
        double d2;
        if (d < 0.5) {
            d2 = (double)16 * d * d * d * d * d;
        } else {
            double d3 = (double)-2 * d + (double)2;
            int n = 5;
            double d4 = 1.0;
            boolean bl = false;
            double d5 = Math.pow(d3, n);
            d2 = d4 - d5 / (double)2;
        }
        return d2;
    }

    public final double easeInQuart(double d) {
        return d * d * d * d;
    }

    public final ListValue getEnumEasingList(String string) {
        Collection<String> collection;
        Object object = EnumEasingType.values();
        String string2 = string;
        boolean bl = false;
        EnumEasingType[] enumEasingTypeArray = object;
        Collection collection2 = new ArrayList(((EnumEasingType[])object).length);
        boolean bl2 = false;
        EnumEasingType[] enumEasingTypeArray2 = enumEasingTypeArray;
        int n = enumEasingTypeArray2.length;
        for (int i = 0; i < n; ++i) {
            EnumEasingType enumEasingType;
            EnumEasingType enumEasingType2 = enumEasingType = enumEasingTypeArray2[i];
            collection = collection2;
            boolean bl3 = false;
            String string3 = enumEasingType2.toString();
            collection.add(string3);
        }
        collection = (List)collection2;
        object = collection;
        bl = false;
        enumEasingTypeArray = object;
        String[] stringArray = enumEasingTypeArray.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = stringArray;
        String string4 = EnumEasingType.SINE.toString();
        String[] stringArray2 = (String[])collection;
        String string5 = string2;
        return new ListValue(string5, stringArray2, string4);
    }

    public static final class EnumEasingOrder
    extends Enum {
        private final String methodName;
        public static final /* enum */ EnumEasingOrder FAST_AT_START_AND_END;
        private static final EnumEasingOrder[] $VALUES;
        public static final /* enum */ EnumEasingOrder FAST_AT_END;
        public static final /* enum */ EnumEasingOrder FAST_AT_START;

        public static EnumEasingOrder[] values() {
            return (EnumEasingOrder[])$VALUES.clone();
        }

        public final String getMethodName() {
            return this.methodName;
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private EnumEasingOrder() {
            void var3_1;
            void var2_-1;
            void var1_-1;
            this.methodName = var3_1;
        }

        static {
            EnumEasingOrder[] enumEasingOrderArray = new EnumEasingOrder[3];
            EnumEasingOrder[] enumEasingOrderArray2 = enumEasingOrderArray;
            enumEasingOrderArray[0] = FAST_AT_START = new EnumEasingOrder("FAST_AT_START", 0, "Out");
            enumEasingOrderArray[1] = FAST_AT_END = new EnumEasingOrder("FAST_AT_END", 1, "In");
            enumEasingOrderArray[2] = FAST_AT_START_AND_END = new EnumEasingOrder("FAST_AT_START_AND_END", 2, "InOut");
            $VALUES = enumEasingOrderArray;
        }

        public static EnumEasingOrder valueOf(String string) {
            return Enum.valueOf(EnumEasingOrder.class, string);
        }
    }

    public static final class EnumEasingType
    extends Enum {
        public static final /* enum */ EnumEasingType EXPO;
        public static final /* enum */ EnumEasingType ELASTIC;
        public static final /* enum */ EnumEasingType QUART;
        public static final /* enum */ EnumEasingType CIRC;
        public static final /* enum */ EnumEasingType BACK;
        public static final /* enum */ EnumEasingType QUINT;
        public static final /* enum */ EnumEasingType SINE;
        private final String friendlyName;
        private static final EnumEasingType[] $VALUES;
        public static final /* enum */ EnumEasingType CUBIC;
        public static final /* enum */ EnumEasingType NONE;
        public static final /* enum */ EnumEasingType QUAD;

        public static EnumEasingType[] values() {
            return (EnumEasingType[])$VALUES.clone();
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private EnumEasingType() {
            String string;
            void var2_-1;
            void var1_-1;
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
            string2 = string = string3.substring(n, n2);
            n = 0;
            String string4 = string2;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string = string4.toUpperCase();
            string2 = this.name();
            n = 1;
            n2 = this.name().length();
            stringBuilder = stringBuilder.append(string);
            bl = false;
            String string5 = string2;
            if (string5 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string2 = string = string5.substring(n, n2);
            n = 0;
            String string6 = string2;
            if (string6 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string = string6.toLowerCase();
            enumEasingType.friendlyName = stringBuilder.append(string).toString();
        }

        static {
            EnumEasingType[] enumEasingTypeArray = new EnumEasingType[10];
            EnumEasingType[] enumEasingTypeArray2 = enumEasingTypeArray;
            enumEasingTypeArray[0] = NONE = new EnumEasingType("NONE", 0);
            enumEasingTypeArray[1] = SINE = new EnumEasingType("SINE", 1);
            enumEasingTypeArray[2] = QUAD = new EnumEasingType("QUAD", 2);
            enumEasingTypeArray[3] = CUBIC = new EnumEasingType("CUBIC", 3);
            enumEasingTypeArray[4] = QUART = new EnumEasingType("QUART", 4);
            enumEasingTypeArray[5] = QUINT = new EnumEasingType("QUINT", 5);
            enumEasingTypeArray[6] = EXPO = new EnumEasingType("EXPO", 6);
            enumEasingTypeArray[7] = CIRC = new EnumEasingType("CIRC", 7);
            enumEasingTypeArray[8] = BACK = new EnumEasingType("BACK", 8);
            enumEasingTypeArray[9] = ELASTIC = new EnumEasingType("ELASTIC", 9);
            $VALUES = enumEasingTypeArray;
        }

        public final String getFriendlyName() {
            return this.friendlyName;
        }

        public static EnumEasingType valueOf(String string) {
            return Enum.valueOf(EnumEasingType.class, string);
        }
    }
}

