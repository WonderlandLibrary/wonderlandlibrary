/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public final class WrappedSet
extends WrappedCollection
implements Set,
KMappedMarker {
    public WrappedSet(Set set, Function1 function1, Function1 function12) {
        super(set, function1, function12);
    }
}

