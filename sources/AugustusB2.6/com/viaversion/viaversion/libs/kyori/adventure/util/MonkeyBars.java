// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class MonkeyBars
{
    private MonkeyBars() {
    }
    
    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(final Class<E> type, final E... constants) {
        final Set<E> set = EnumSet.noneOf(type);
        Collections.addAll(set, constants);
        return Collections.unmodifiableSet((Set<? extends E>)set);
    }
    
    @NotNull
    public static <T> List<T> addOne(@NotNull final List<T> oldList, final T newElement) {
        if (oldList.isEmpty()) {
            return Collections.singletonList(newElement);
        }
        final List<T> newList = new ArrayList<T>(oldList.size() + 1);
        newList.addAll((Collection<? extends T>)oldList);
        newList.add(newElement);
        return Collections.unmodifiableList((List<? extends T>)newList);
    }
}
