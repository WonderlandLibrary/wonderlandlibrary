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
    private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler<RecyclableArrayList>(){

        @Override
        protected RecyclableArrayList newObject(Recycler.Handle handle) {
            return new RecyclableArrayList(handle);
        }
    };
    private static final long serialVersionUID = -8605125654176467947L;
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private final Recycler.Handle handle;

    private static void checkNullElements(Collection<?> collection) {
        if (collection instanceof RandomAccess && collection instanceof List) {
            List list = (List)collection;
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                if (list.get(i) != null) continue;
                throw new IllegalArgumentException("c contains null values");
            }
        } else {
            for (Object obj : collection) {
                if (obj != null) continue;
                throw new IllegalArgumentException("c contains null values");
            }
        }
    }

    @Override
    public boolean add(Object object) {
        if (object == null) {
            throw new NullPointerException("element");
        }
        return super.add(object);
    }

    @Override
    public Object set(int n, Object object) {
        if (object == null) {
            throw new NullPointerException("element");
        }
        return super.set(n, object);
    }

    private RecyclableArrayList(Recycler.Handle handle) {
        this(handle, 8);
    }

    @Override
    public boolean addAll(Collection<?> collection) {
        RecyclableArrayList.checkNullElements(collection);
        return super.addAll(collection);
    }

    @Override
    public void add(int n, Object object) {
        if (object == null) {
            throw new NullPointerException("element");
        }
        super.add(n, object);
    }

    private RecyclableArrayList(Recycler.Handle handle, int n) {
        super(n);
        this.handle = handle;
    }

    @Override
    public boolean addAll(int n, Collection<?> collection) {
        RecyclableArrayList.checkNullElements(collection);
        return super.addAll(n, collection);
    }

    public static RecyclableArrayList newInstance(int n) {
        RecyclableArrayList recyclableArrayList = RECYCLER.get();
        recyclableArrayList.ensureCapacity(n);
        return recyclableArrayList;
    }

    public boolean recycle() {
        this.clear();
        return RECYCLER.recycle(this, this.handle);
    }

    public static RecyclableArrayList newInstance() {
        return RecyclableArrayList.newInstance(8);
    }
}

