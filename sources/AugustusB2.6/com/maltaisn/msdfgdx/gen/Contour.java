// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import java.util.List;

public final class Contour
{
    private final List<Object> points;
    
    @Override
    public final String toString() {
        return "{ " + CollectionsKt___CollectionsJvmKt.joinToString$default$1494b5c(this.points, "; ", null, null, 0, null, null, 62) + " }";
    }
    
    public Contour(final List<?> points) {
        Intrinsics.checkParameterIsNotNull(points, "points");
        this.points = (List<Object>)points;
    }
}
