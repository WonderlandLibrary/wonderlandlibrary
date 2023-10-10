// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Spliterator;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.List;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.RandomAccess;
import java.util.Objects;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.Stack;

public abstract class AbstractObjectList<K> extends AbstractObjectCollection<K> implements ObjectList<K>, Stack<K>
{
    protected AbstractObjectList() {
    }
    
    protected void ensureIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
    }
    
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size() + ")");
        }
    }
    
    @Override
    public void add(final int index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final K k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public K remove(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public K set(final int index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends K> c) {
        this.ensureIndex(index);
        final Iterator<? extends K> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.next());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public ObjectListIterator<K> iterator() {
        return this.listIterator();
    }
    
    @Override
    public ObjectListIterator<K> listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new ObjectIterators.AbstractIndexBasedListIterator<K>(0, index) {
            @Override
            protected final K get(final int i) {
                return AbstractObjectList.this.get(i);
            }
            
            @Override
            protected final void add(final int i, final K k) {
                AbstractObjectList.this.add(i, k);
            }
            
            @Override
            protected final void set(final int i, final K k) {
                AbstractObjectList.this.set(i, k);
            }
            
            @Override
            protected final void remove(final int i) {
                AbstractObjectList.this.remove(i);
            }
            
            @Override
            protected final int getMaxPos() {
                return AbstractObjectList.this.size();
            }
        };
    }
    
    @Override
    public boolean contains(final Object k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final Object k) {
        final ObjectListIterator<K> i = this.listIterator();
        while (i.hasNext()) {
            final K e = i.next();
            if (Objects.equals(k, e)) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object k) {
        final ObjectListIterator<K> i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final K e = i.previous();
            if (Objects.equals(k, e)) {
                return i.nextIndex();
            }
        }
        return -1;
    }
    
    @Override
    public void size(final int size) {
        int i = this.size();
        if (size > i) {
            while (i++ < size) {
                this.add(null);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public ObjectList<K> subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return (this instanceof RandomAccess) ? new ObjectRandomAccessSubList<K>(this, from, to) : new ObjectSubList<K>(this, from, to);
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        if (this instanceof RandomAccess) {
            for (int i = 0, max = this.size(); i < max; ++i) {
                action.accept(this.get(i));
            }
        }
        else {
            super.forEach(action);
        }
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final ObjectListIterator<K> i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.next();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final K[] a, int offset, int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (this instanceof RandomAccess) {
            while (length-- != 0) {
                this.add(index++, a[offset++]);
            }
        }
        else {
            final ObjectListIterator<K> iter = this.listIterator(index);
            while (length-- != 0) {
                iter.add(a[offset++]);
            }
        }
    }
    
    @Override
    public void addElements(final int index, final K[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final Object[] a, int offset, int length) {
        this.ensureIndex(from);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            int current = from;
            while (length-- != 0) {
                a[offset++] = this.get(current++);
            }
        }
        else {
            final ObjectListIterator<K> i = this.listIterator(from);
            while (length-- != 0) {
                a[offset++] = i.next();
            }
        }
    }
    
    @Override
    public void setElements(final int index, final K[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            for (int i = 0; i < length; ++i) {
                this.set(i + index, a[i + offset]);
            }
        }
        else {
            final ObjectListIterator<K> iter = this.listIterator(index);
            int j = 0;
            while (j < length) {
                iter.next();
                iter.set(a[offset + j++]);
            }
        }
    }
    
    @Override
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    @Override
    public Object[] toArray() {
        final int size = this.size();
        final Object[] ret = new Object[size];
        this.getElements(0, ret, 0, size);
        return ret;
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        this.getElements(0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
    
    @Override
    public int hashCode() {
        final ObjectIterator<K> i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final K k = i.next();
            h = 31 * h + ((k == null) ? 0 : k.hashCode());
        }
        return h;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        final List<?> l = (List<?>)o;
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final ListIterator<?> i1 = this.listIterator();
        final ListIterator<?> i2 = l.listIterator();
        while (s-- != 0) {
            if (!Objects.equals(i1.next(), i2.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final List<? extends K> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ObjectList) {
            final ObjectListIterator<K> i1 = this.listIterator();
            final ObjectListIterator<K> i2 = ((ObjectList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final K e1 = i1.next();
                final K e2 = i2.next();
                final int r;
                if ((r = ((Comparable)e1).compareTo(e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends K> i3 = (ListIterator<? extends K>)this.listIterator();
        final ListIterator<? extends K> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final K o) {
        this.add(o);
    }
    
    @Override
    public K pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.remove(this.size() - 1);
    }
    
    @Override
    public K top() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.get(this.size() - 1);
    }
    
    @Override
    public K peek(final int i) {
        return this.get(this.size() - 1 - i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("[");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final K k = i.next();
            if (this == k) {
                s.append("(this list)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("]");
        return s.toString();
    }
    
    static final class IndexBasedSpliterator<K> extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K>
    {
        final ObjectList<K> l;
        
        IndexBasedSpliterator(final ObjectList<K> l, final int pos) {
            super(pos);
            this.l = l;
        }
        
        IndexBasedSpliterator(final ObjectList<K> l, final int pos, final int maxPos) {
            super(pos, maxPos);
            this.l = l;
        }
        
        @Override
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }
        
        @Override
        protected final K get(final int i) {
            return this.l.get(i);
        }
        
        @Override
        protected final IndexBasedSpliterator<K> makeForSplit(final int pos, final int maxPos) {
            return new IndexBasedSpliterator<K>(this.l, pos, maxPos);
        }
    }
    
    public static class ObjectSubList<K> extends AbstractObjectList<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectList<K> l;
        protected final int from;
        protected int to;
        
        public ObjectSubList(final ObjectList<K> l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private boolean assertRange() {
            assert this.from <= this.l.size();
            assert this.to <= this.l.size();
            assert this.to >= this.from;
            return true;
        }
        
        @Override
        public boolean add(final K k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final K k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends K> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection<?>)c);
        }
        
        @Override
        public K get(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.get(this.from + index);
        }
        
        @Override
        public K remove(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.remove(this.from + index);
        }
        
        @Override
        public K set(final int index, final K k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        @Override
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert this.assertRange();
        }
        
        @Override
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public void setElements(final int index, final K[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.setElements(this.from + index, a, offset, length);
            assert this.assertRange();
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            this.ensureIndex(index);
            return (this.l instanceof RandomAccess) ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator(index + this.from));
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return (this.l instanceof RandomAccess) ? new IndexBasedSpliterator<K>(this.l, this.from, this.to) : super.spliterator();
        }
        
        @Override
        public ObjectList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectSubList((ObjectList<Object>)this, from, to);
        }
        
        private final class RandomAccessIter extends ObjectIterators.AbstractIndexBasedListIterator<K>
        {
            RandomAccessIter(final int pos) {
                super(0, pos);
            }
            
            @Override
            protected final K get(final int i) {
                return ObjectSubList.this.l.get(ObjectSubList.this.from + i);
            }
            
            @Override
            protected final void add(final int i, final K k) {
                ObjectSubList.this.add(i, k);
            }
            
            @Override
            protected final void set(final int i, final K k) {
                ObjectSubList.this.set(i, k);
            }
            
            @Override
            protected final void remove(final int i) {
                ObjectSubList.this.remove(i);
            }
            
            @Override
            protected final int getMaxPos() {
                return ObjectSubList.this.to - ObjectSubList.this.from;
            }
            
            @Override
            public void add(final K k) {
                super.add(k);
                assert ObjectSubList.this.assertRange();
            }
            
            @Override
            public void remove() {
                super.remove();
                assert ObjectSubList.this.assertRange();
            }
        }
        
        private class ParentWrappingIter implements ObjectListIterator<K>
        {
            private ObjectListIterator<K> parent;
            
            ParentWrappingIter(final ObjectListIterator<K> parent) {
                this.parent = parent;
            }
            
            @Override
            public int nextIndex() {
                return this.parent.nextIndex() - ObjectSubList.this.from;
            }
            
            @Override
            public int previousIndex() {
                return this.parent.previousIndex() - ObjectSubList.this.from;
            }
            
            @Override
            public boolean hasNext() {
                return this.parent.nextIndex() < ObjectSubList.this.to;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= ObjectSubList.this.from;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.parent.next();
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                return this.parent.previous();
            }
            
            @Override
            public void add(final K k) {
                this.parent.add(k);
            }
            
            @Override
            public void set(final K k) {
                this.parent.set(k);
            }
            
            @Override
            public void remove() {
                this.parent.remove();
            }
            
            @Override
            public int back(final int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int currentPos = this.parent.previousIndex();
                int parentNewPos = currentPos - n;
                if (parentNewPos < ObjectSubList.this.from - 1) {
                    parentNewPos = ObjectSubList.this.from - 1;
                }
                final int toSkip = parentNewPos - currentPos;
                return this.parent.back(toSkip);
            }
            
            @Override
            public int skip(final int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int currentPos = this.parent.nextIndex();
                int parentNewPos = currentPos + n;
                if (parentNewPos > ObjectSubList.this.to) {
                    parentNewPos = ObjectSubList.this.to;
                }
                final int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }
    }
    
    public static class ObjectRandomAccessSubList<K> extends ObjectSubList<K> implements RandomAccess
    {
        private static final long serialVersionUID = -107070782945191929L;
        
        public ObjectRandomAccessSubList(final ObjectList<K> l, final int from, final int to) {
            super(l, from, to);
        }
        
        @Override
        public ObjectList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectRandomAccessSubList((ObjectList<Object>)this, from, to);
        }
    }
}
