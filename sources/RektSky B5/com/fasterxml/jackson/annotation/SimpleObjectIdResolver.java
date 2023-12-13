/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import java.util.HashMap;
import java.util.Map;

public class SimpleObjectIdResolver
implements ObjectIdResolver {
    protected Map<ObjectIdGenerator.IdKey, Object> _items;

    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object ob) {
        if (this._items == null) {
            this._items = new HashMap<ObjectIdGenerator.IdKey, Object>();
        } else {
            Object old = this._items.get(id);
            if (old != null) {
                if (old == ob) {
                    return;
                }
                throw new IllegalStateException("Already had POJO for id (" + id.key.getClass().getName() + ") [" + id + "]");
            }
        }
        this._items.put(id, ob);
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        return this._items == null ? null : this._items.get(id);
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == this.getClass();
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new SimpleObjectIdResolver();
    }
}

