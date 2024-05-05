/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;

public final class WrappedSet<O, T>
extends WrappedCollection<O, T, Collection<? extends O>>
implements Set<T>,
KMappedMarker {
    public WrappedSet(Set<? extends O> wrapped, Function1<? super T, ? extends O> unwrapper, Function1<? super O, ? extends T> wrapper) {
        super((Collection)wrapped, unwrapper, wrapper);
    }
}

