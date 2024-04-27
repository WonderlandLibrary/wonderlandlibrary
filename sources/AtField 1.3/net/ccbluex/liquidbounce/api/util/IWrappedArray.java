/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function2
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u0000 \u000b*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001\u000bJ\u0016\u0010\u0003\u001a\u00028\u00002\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6\u0002\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00028\u0000H\u00a6\u0002\u00a2\u0006\u0002\u0010\n\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "T", "", "get", "index", "", "(I)Ljava/lang/Object;", "set", "", "value", "(ILjava/lang/Object;)V", "Companion", "AtField"})
public interface IWrappedArray
extends Iterable,
KMappedMarker {
    public static final Companion Companion = net.ccbluex.liquidbounce.api.util.IWrappedArray$Companion.$$INSTANCE;

    public void set(int var1, Object var2);

    public Object get(int var1);

    public static final class Companion {
        static final Companion $$INSTANCE;

        private Companion() {
        }

        static {
            Companion companion;
            $$INSTANCE = companion = new Companion();
        }

        public final void forEachIndexed(IWrappedArray iWrappedArray, Function2 function2) {
            boolean bl = false;
            int n = 0;
            for (Object t : iWrappedArray) {
                Integer n2 = n;
                ++n;
                function2.invoke((Object)n2, t);
            }
        }
    }
}

