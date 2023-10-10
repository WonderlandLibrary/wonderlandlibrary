// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.function.ObjIntConsumer;
import java.util.Iterator;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public final class LinkedHashMultiset<E> extends AbstractMapBasedMultiset<E>
{
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <E> LinkedHashMultiset<E> create() {
        return new LinkedHashMultiset<E>();
    }
    
    public static <E> LinkedHashMultiset<E> create(final int distinctElements) {
        return new LinkedHashMultiset<E>(distinctElements);
    }
    
    public static <E> LinkedHashMultiset<E> create(final Iterable<? extends E> elements) {
        final LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
        Iterables.addAll(multiset, elements);
        return multiset;
    }
    
    private LinkedHashMultiset() {
        super(new LinkedHashMap());
    }
    
    private LinkedHashMultiset(final int distinctElements) {
        super((Map<Object, Count>)Maps.newLinkedHashMapWithExpectedSize(distinctElements));
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultiset((Multiset<Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int distinctElements = Serialization.readCount(stream);
        this.setBackingMap(new LinkedHashMap<E, Count>());
        Serialization.populateMultiset((Multiset<Object>)this, stream, distinctElements);
    }
}
