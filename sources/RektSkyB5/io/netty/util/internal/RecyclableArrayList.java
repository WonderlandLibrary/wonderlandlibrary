/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.Recycler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public final class RecyclableArrayList
extends ArrayList<Object> {
    private static final long serialVersionUID = -8605125654176467947L;
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler<RecyclableArrayList>(){

        @Override
        protected RecyclableArrayList newObject(Recycler.Handle handle) {
            return new RecyclableArrayList(handle);
        }
    };
    private final Recycler.Handle handle;

    public static RecyclableArrayList newInstance() {
        return RecyclableArrayList.newInstance(8);
    }

    public static RecyclableArrayList newInstance(int minCapacity) {
        RecyclableArrayList ret = RECYCLER.get();
        ret.ensureCapacity(minCapacity);
        return ret;
    }

    private RecyclableArrayList(Recycler.Handle handle) {
        this(handle, 8);
    }

    private RecyclableArrayList(Recycler.Handle handle, int initialCapacity) {
        super(initialCapacity);
        this.handle = handle;
    }

    @Override
    public boolean addAll(Collection<?> c2) {
        RecyclableArrayList.checkNullElements(c2);
        return super.addAll(c2);
    }

    @Override
    public boolean addAll(int index, Collection<?> c2) {
        RecyclableArrayList.checkNullElements(c2);
        return super.addAll(index, c2);
    }

    private static void checkNullElements(Collection<?> c2) {
        if (c2 instanceof RandomAccess && c2 instanceof List) {
            List list = (List)c2;
            int size = list.size();
            for (int i2 = 0; i2 < size; ++i2) {
                if (list.get(i2) != null) continue;
                throw new IllegalArgumentException("c contains null values");
            }
        } else {
            for (Object element : c2) {
                if (element != null) continue;
                throw new IllegalArgumentException("c contains null values");
            }
        }
    }

    @Override
    public boolean add(Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        return super.add(element);
    }

    @Override
    public void add(int index, Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        super.add(index, element);
    }

    @Override
    public Object set(int index, Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        return super.set(index, element);
    }

    public boolean recycle() {
        this.clear();
        return RECYCLER.recycle(this, this.handle);
    }
}

