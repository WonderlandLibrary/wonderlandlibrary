// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.LinkedHashMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable
{
    private final Map<Class<? extends B>, B> delegate;
    
    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
    }
    
    public static <B> MutableClassToInstanceMap<B> create(final Map<Class<? extends B>, B> backingMap) {
        return new MutableClassToInstanceMap<B>(backingMap);
    }
    
    private MutableClassToInstanceMap(final Map<Class<? extends B>, B> delegate) {
        this.delegate = Preconditions.checkNotNull(delegate);
    }
    
    @Override
    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }
    
    private static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> entry) {
        return new ForwardingMapEntry<Class<? extends B>, B>() {
            @Override
            protected Map.Entry<Class<? extends B>, B> delegate() {
                return entry;
            }
            
            @Override
            public B setValue(final B value) {
                return super.setValue((B)cast((Class<Object>)((ForwardingMapEntry<Class, V>)this).getKey(), value));
            }
        };
    }
    
    @Override
    public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
        return new ForwardingSet<Map.Entry<Class<? extends B>, B>>() {
            @Override
            protected Set<Map.Entry<Class<? extends B>, B>> delegate() {
                return MutableClassToInstanceMap.this.delegate().entrySet();
            }
            
            @Override
            public Spliterator<Map.Entry<Class<? extends B>, B>> spliterator() {
                return CollectSpliterators.map(this.delegate().spliterator(), x$0 -> checkedEntry((Map.Entry<Class<?>, Object>)x$0));
            }
            
            @Override
            public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
                return new TransformedIterator<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>>(this, this.delegate().iterator()) {
                    @Override
                    Map.Entry<Class<? extends B>, B> transform(final Map.Entry<Class<? extends B>, B> from) {
                        return (Map.Entry<Class<? extends B>, B>)checkedEntry((Map.Entry<Class<?>, Object>)from);
                    }
                };
            }
            
            @Override
            public Object[] toArray() {
                final Object[] result = this.standardToArray();
                return result;
            }
            
            @Override
            public <T> T[] toArray(final T[] array) {
                return this.standardToArray(array);
            }
        };
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public B put(final Class<? extends B> key, final B value) {
        return super.put(key, cast((Class<B>)key, value));
    }
    
    @Override
    public void putAll(final Map<? extends Class<? extends B>, ? extends B> map) {
        final Map<Class<? extends B>, B> copy = new LinkedHashMap<Class<? extends B>, B>(map);
        for (final Map.Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
            cast((Class<Object>)entry.getKey(), entry.getValue());
        }
        super.putAll((Map<? extends Class<? extends B>, ? extends B>)copy);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        return cast(type, (Object)this.put((Class<? extends B>)type, (B)value));
    }
    
    @CheckForNull
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return cast(type, this.get(type));
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    private static <B, T extends B> T cast(final Class<T> type, @CheckForNull final B value) {
        return Primitives.wrap(type).cast(value);
    }
    
    private Object writeReplace() {
        return new SerializedForm(this.delegate());
    }
    
    private static final class SerializedForm<B> implements Serializable
    {
        private final Map<Class<? extends B>, B> backingMap;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Map<Class<? extends B>, B> backingMap) {
            this.backingMap = backingMap;
        }
        
        Object readResolve() {
            return MutableClassToInstanceMap.create(this.backingMap);
        }
    }
}
