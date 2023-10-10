// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import java.util.Set;
import java.util.AbstractSequentialList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.List;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Objects;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public class LinkedListMultimap<K, V> extends AbstractMultimap<K, V> implements ListMultimap<K, V>, Serializable
{
    @CheckForNull
    private transient Node<K, V> head;
    @CheckForNull
    private transient Node<K, V> tail;
    private transient Map<K, KeyList<K, V>> keyToKeyList;
    private transient int size;
    private transient int modCount;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap<K, V>();
    }
    
    public static <K, V> LinkedListMultimap<K, V> create(final int expectedKeys) {
        return new LinkedListMultimap<K, V>(expectedKeys);
    }
    
    public static <K, V> LinkedListMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap<K, V>(multimap);
    }
    
    LinkedListMultimap() {
        this(12);
    }
    
    private LinkedListMultimap(final int expectedKeys) {
        this.keyToKeyList = Platform.newHashMapWithExpectedSize(expectedKeys);
    }
    
    private LinkedListMultimap(final Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        this.putAll(multimap);
    }
    
    @CanIgnoreReturnValue
    private Node<K, V> addNode(@ParametricNullness final K key, @ParametricNullness final V value, @CheckForNull final Node<K, V> nextSibling) {
        final Node<K, V> node = new Node<K, V>(key, value);
        if (this.head == null) {
            final Node<K, V> node2 = node;
            this.tail = node2;
            this.head = node2;
            this.keyToKeyList.put(key, new KeyList<K, V>(node));
            ++this.modCount;
        }
        else if (nextSibling == null) {
            Objects.requireNonNull(this.tail).next = node;
            node.previous = this.tail;
            this.tail = node;
            KeyList<K, V> keyList = this.keyToKeyList.get(key);
            if (keyList == null) {
                this.keyToKeyList.put(key, keyList = new KeyList<K, V>(node));
                ++this.modCount;
            }
            else {
                final KeyList<K, V> list = keyList;
                ++list.count;
                final Node<K, V> keyTail = keyList.tail;
                keyTail.nextSibling = node;
                node.previousSibling = keyTail;
                keyList.tail = node;
            }
        }
        else {
            final KeyList<K, V> list2;
            final KeyList<K, V> keyList = list2 = Objects.requireNonNull(this.keyToKeyList.get(key));
            ++list2.count;
            node.previous = nextSibling.previous;
            node.previousSibling = nextSibling.previousSibling;
            node.next = nextSibling;
            node.nextSibling = nextSibling;
            if (nextSibling.previousSibling == null) {
                keyList.head = node;
            }
            else {
                nextSibling.previousSibling.nextSibling = node;
            }
            if (nextSibling.previous == null) {
                this.head = node;
            }
            else {
                nextSibling.previous.next = node;
            }
            nextSibling.previous = node;
            nextSibling.previousSibling = node;
        }
        ++this.size;
        return node;
    }
    
    private void removeNode(final Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        }
        else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        }
        else {
            this.tail = node.previous;
        }
        if (node.previousSibling == null && node.nextSibling == null) {
            final KeyList<K, V> keyList = Objects.requireNonNull(this.keyToKeyList.remove(node.key));
            keyList.count = 0;
            ++this.modCount;
        }
        else {
            final KeyList<K, V> list;
            final KeyList<K, V> keyList = list = Objects.requireNonNull(this.keyToKeyList.get(node.key));
            --list.count;
            if (node.previousSibling == null) {
                keyList.head = (Node<K, V>)Objects.requireNonNull((Node<K, V>)node.nextSibling);
            }
            else {
                node.previousSibling.nextSibling = node.nextSibling;
            }
            if (node.nextSibling == null) {
                keyList.tail = (Node<K, V>)Objects.requireNonNull((Node<K, V>)node.previousSibling);
            }
            else {
                node.nextSibling.previousSibling = node.previousSibling;
            }
        }
        --this.size;
    }
    
    private void removeAllNodes(@ParametricNullness final K key) {
        Iterators.clear(new ValueForKeyIterator(key));
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.head == null;
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.keyToKeyList.containsKey(key);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.values().contains(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
        this.addNode(key, value, null);
        return true;
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        final List<V> oldValues = this.getCopy(key);
        final ListIterator<V> keyValues = new ValueForKeyIterator(key);
        final Iterator<? extends V> newValues = values.iterator();
        while (keyValues.hasNext() && newValues.hasNext()) {
            keyValues.next();
            keyValues.set((V)newValues.next());
        }
        while (keyValues.hasNext()) {
            keyValues.next();
            keyValues.remove();
        }
        while (newValues.hasNext()) {
            keyValues.add((V)newValues.next());
        }
        return oldValues;
    }
    
    private List<V> getCopy(@ParametricNullness final K key) {
        return Collections.unmodifiableList((List<? extends V>)Lists.newArrayList((Iterator<?>)new ValueForKeyIterator(key)));
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> removeAll(final Object key) {
        final K castKey = (K)key;
        final List<V> oldValues = this.getCopy(castKey);
        this.removeAllNodes(castKey);
        return oldValues;
    }
    
    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyToKeyList.clear();
        this.size = 0;
        ++this.modCount;
    }
    
    @Override
    public List<V> get(@ParametricNullness final K key) {
        return new AbstractSequentialList<V>() {
            @Override
            public int size() {
                final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
                return (keyList == null) ? 0 : keyList.count;
            }
            
            @Override
            public ListIterator<V> listIterator(final int index) {
                return new ValueForKeyIterator(key, index);
            }
        };
    }
    
    @Override
    Set<K> createKeySet() {
        class KeySetImpl extends Sets.ImprovedAbstractSet<K>
        {
            @Override
            public int size() {
                return LinkedListMultimap.this.keyToKeyList.size();
            }
            
            @Override
            public Iterator<K> iterator() {
                return new DistinctKeyIterator();
            }
            
            @Override
            public boolean contains(@CheckForNull final Object key) {
                return LinkedListMultimap.this.containsKey(key);
            }
            
            @Override
            public boolean remove(@CheckForNull final Object o) {
                return !LinkedListMultimap.this.removeAll(o).isEmpty();
            }
        }
        return new KeySetImpl();
    }
    
    @Override
    Multiset<K> createKeys() {
        return (Multiset<K>)new Multimaps.Keys((Multimap<Object, Object>)this);
    }
    
    @Override
    public List<V> values() {
        return (List<V>)(List)super.values();
    }
    
    @Override
    List<V> createValues() {
        class ValuesImpl extends AbstractSequentialList<V>
        {
            @Override
            public int size() {
                return LinkedListMultimap.this.size;
            }
            
            @Override
            public ListIterator<V> listIterator(final int index) {
                final NodeIterator nodeItr = new NodeIterator(index);
                return new TransformedListIterator<Map.Entry<K, V>, V>(this, nodeItr) {
                    @ParametricNullness
                    @Override
                    V transform(final Map.Entry<K, V> entry) {
                        return entry.getValue();
                    }
                    
                    @Override
                    public void set(@ParametricNullness final V value) {
                        nodeItr.setValue(value);
                    }
                };
            }
        }
        return new ValuesImpl();
    }
    
    @Override
    public List<Map.Entry<K, V>> entries() {
        return (List<Map.Entry<K, V>>)(List)super.entries();
    }
    
    @Override
    List<Map.Entry<K, V>> createEntries() {
        class EntriesImpl extends AbstractSequentialList<Map.Entry<K, V>>
        {
            @Override
            public int size() {
                return LinkedListMultimap.this.size;
            }
            
            @Override
            public ListIterator<Map.Entry<K, V>> listIterator(final int index) {
                return new NodeIterator(index);
            }
            
            @Override
            public void forEach(final Consumer<? super Map.Entry<K, V>> action) {
                Preconditions.checkNotNull(action);
                for (Node<K, V> node = LinkedListMultimap.this.head; node != null; node = node.next) {
                    action.accept(node);
                }
            }
        }
        return new EntriesImpl();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return (Map<K, Collection<V>>)new Multimaps.AsMap((Multimap<Object, Object>)this);
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.size());
        for (final Map.Entry<K, V> entry : this.entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyToKeyList = (Map<K, KeyList<K, V>>)Maps.newLinkedHashMap();
        for (int size = stream.readInt(), i = 0; i < size; ++i) {
            final K key = (K)stream.readObject();
            final V value = (V)stream.readObject();
            this.put(key, value);
        }
    }
    
    private static final class Node<K, V> extends AbstractMapEntry<K, V>
    {
        @ParametricNullness
        final K key;
        @ParametricNullness
        V value;
        @CheckForNull
        Node<K, V> next;
        @CheckForNull
        Node<K, V> previous;
        @CheckForNull
        Node<K, V> nextSibling;
        @CheckForNull
        Node<K, V> previousSibling;
        
        Node(@ParametricNullness final K key, @ParametricNullness final V value) {
            this.key = key;
            this.value = value;
        }
        
        @ParametricNullness
        @Override
        public K getKey() {
            return this.key;
        }
        
        @ParametricNullness
        @Override
        public V getValue() {
            return this.value;
        }
        
        @ParametricNullness
        @Override
        public V setValue(@ParametricNullness final V newValue) {
            final V result = this.value;
            this.value = newValue;
            return result;
        }
    }
    
    private static class KeyList<K, V>
    {
        Node<K, V> head;
        Node<K, V> tail;
        int count;
        
        KeyList(final Node<K, V> firstNode) {
            this.head = firstNode;
            this.tail = firstNode;
            firstNode.previousSibling = null;
            firstNode.nextSibling = null;
            this.count = 1;
        }
    }
    
    private class NodeIterator implements ListIterator<Map.Entry<K, V>>
    {
        int nextIndex;
        @CheckForNull
        Node<K, V> next;
        @CheckForNull
        Node<K, V> current;
        @CheckForNull
        Node<K, V> previous;
        int expectedModCount;
        
        NodeIterator(int index) {
            this.expectedModCount = LinkedListMultimap.this.modCount;
            final int size = LinkedListMultimap.this.size();
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = LinkedListMultimap.this.tail;
                this.nextIndex = size;
                while (index++ < size) {
                    this.previous();
                }
            }
            else {
                this.next = LinkedListMultimap.this.head;
                while (index-- > 0) {
                    this.next();
                }
            }
            this.current = null;
        }
        
        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public boolean hasNext() {
            this.checkForConcurrentModification();
            return this.next != null;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Node<K, V> next() {
            this.checkForConcurrentModification();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            final Node<K, V> next = this.next;
            this.current = next;
            this.previous = next;
            this.next = this.next.next;
            ++this.nextIndex;
            return this.current;
        }
        
        @Override
        public void remove() {
            this.checkForConcurrentModification();
            Preconditions.checkState(this.current != null, (Object)"no calls to next() since the last call to remove()");
            if (this.current != this.next) {
                this.previous = this.current.previous;
                --this.nextIndex;
            }
            else {
                this.next = this.current.next;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
        
        @Override
        public boolean hasPrevious() {
            this.checkForConcurrentModification();
            return this.previous != null;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Node<K, V> previous() {
            this.checkForConcurrentModification();
            if (this.previous == null) {
                throw new NoSuchElementException();
            }
            final Node<K, V> previous = this.previous;
            this.current = previous;
            this.next = previous;
            this.previous = this.previous.previous;
            --this.nextIndex;
            return this.current;
        }
        
        @Override
        public int nextIndex() {
            return this.nextIndex;
        }
        
        @Override
        public int previousIndex() {
            return this.nextIndex - 1;
        }
        
        @Override
        public void set(final Map.Entry<K, V> e) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Map.Entry<K, V> e) {
            throw new UnsupportedOperationException();
        }
        
        void setValue(@ParametricNullness final V value) {
            Preconditions.checkState(this.current != null);
            this.current.value = value;
        }
    }
    
    private class DistinctKeyIterator implements Iterator<K>
    {
        final Set<K> seenKeys;
        @CheckForNull
        Node<K, V> next;
        @CheckForNull
        Node<K, V> current;
        int expectedModCount;
        
        private DistinctKeyIterator() {
            this.seenKeys = (Set<K>)Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
            this.next = LinkedListMultimap.this.head;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
        
        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public boolean hasNext() {
            this.checkForConcurrentModification();
            return this.next != null;
        }
        
        @ParametricNullness
        @Override
        public K next() {
            this.checkForConcurrentModification();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
            } while (this.next != null && !this.seenKeys.add(this.next.key));
            return this.current.key;
        }
        
        @Override
        public void remove() {
            this.checkForConcurrentModification();
            Preconditions.checkState(this.current != null, (Object)"no calls to next() since the last call to remove()");
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
    }
    
    private class ValueForKeyIterator implements ListIterator<V>
    {
        @ParametricNullness
        final K key;
        int nextIndex;
        @CheckForNull
        Node<K, V> next;
        @CheckForNull
        Node<K, V> current;
        @CheckForNull
        Node<K, V> previous;
        
        ValueForKeyIterator(final K key) {
            this.key = key;
            final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
            this.next = ((keyList == null) ? null : keyList.head);
        }
        
        public ValueForKeyIterator(final K key, int index) {
            final KeyList<K, V> keyList = LinkedListMultimap.this.keyToKeyList.get(key);
            final int size = (keyList == null) ? 0 : keyList.count;
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = ((keyList == null) ? null : keyList.tail);
                this.nextIndex = size;
                while (index++ < size) {
                    this.previous();
                }
            }
            else {
                this.next = ((keyList == null) ? null : keyList.head);
                while (index-- > 0) {
                    this.next();
                }
            }
            this.key = key;
            this.current = null;
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public V next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            final Node<K, V> next = this.next;
            this.current = next;
            this.previous = next;
            this.next = this.next.nextSibling;
            ++this.nextIndex;
            return this.current.value;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.previous != null;
        }
        
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public V previous() {
            if (this.previous == null) {
                throw new NoSuchElementException();
            }
            final Node<K, V> previous = this.previous;
            this.current = previous;
            this.next = previous;
            this.previous = this.previous.previousSibling;
            --this.nextIndex;
            return this.current.value;
        }
        
        @Override
        public int nextIndex() {
            return this.nextIndex;
        }
        
        @Override
        public int previousIndex() {
            return this.nextIndex - 1;
        }
        
        @Override
        public void remove() {
            Preconditions.checkState(this.current != null, (Object)"no calls to next() since the last call to remove()");
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                --this.nextIndex;
            }
            else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }
        
        @Override
        public void set(@ParametricNullness final V value) {
            Preconditions.checkState(this.current != null);
            this.current.value = value;
        }
        
        @Override
        public void add(@ParametricNullness final V value) {
            this.previous = (Node<K, V>)LinkedListMultimap.this.addNode(this.key, value, this.next);
            ++this.nextIndex;
            this.current = null;
        }
    }
}
