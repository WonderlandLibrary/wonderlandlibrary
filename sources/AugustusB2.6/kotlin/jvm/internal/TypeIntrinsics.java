// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import kotlin.Function;

public class TypeIntrinsics
{
    public static Object beforeCheckcastToFunctionOfArity(Object obj, final int arity) {
        if (obj != null) {
            final Object o = obj;
            final int n = 2;
            final FunctionBase functionBase = (FunctionBase)o;
            final FunctionBase functionBase2;
            if (!(o instanceof Function) || (((functionBase2 = functionBase) instanceof FunctionBase) ? functionBase2.getArity() : ((functionBase2 instanceof Function0) ? 0 : ((functionBase2 instanceof Function1) ? 1 : ((functionBase2 instanceof Function2) ? 2 : ((functionBase2 instanceof Function3) ? 3 : ((functionBase2 instanceof Function4) ? 4 : ((functionBase2 instanceof Function5) ? 5 : ((functionBase2 instanceof Function6) ? 6 : ((functionBase2 instanceof Function7) ? 7 : ((functionBase2 instanceof Function8) ? 8 : ((functionBase2 instanceof Function9) ? 9 : ((functionBase2 instanceof Function10) ? 10 : ((functionBase2 instanceof Function11) ? 11 : ((functionBase2 instanceof Function12) ? 12 : ((functionBase2 instanceof Function13) ? 13 : ((functionBase2 instanceof Function14) ? 14 : ((functionBase2 instanceof Function15) ? 15 : ((functionBase2 instanceof Function16) ? 16 : ((functionBase2 instanceof Function17) ? 17 : ((functionBase2 instanceof Function18) ? 18 : ((functionBase2 instanceof Function19) ? 19 : ((functionBase2 instanceof Function20) ? 20 : ((functionBase2 instanceof Function21) ? 21 : ((functionBase2 instanceof Function22) ? 22 : -1)))))))))))))))))))))))) != n) {
                final Object o2 = obj;
                final String string = "kotlin.jvm.functions.Function" + 2;
                final Object o3 = o2;
                obj = ((o2 == null) ? "null" : o3.getClass().getName()) + " cannot be cast to " + string;
                throw Intrinsics.sanitizeStackTrace(obj = (obj = new ClassCastException((String)obj)), TypeIntrinsics.class.getName());
            }
        }
        return obj;
    }
}
