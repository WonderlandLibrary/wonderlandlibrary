/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

public interface ObjectIdResolver {
    public void bindItem(ObjectIdGenerator.IdKey var1, Object var2);

    public Object resolveId(ObjectIdGenerator.IdKey var1);

    public ObjectIdResolver newForDeserialization(Object var1);

    public boolean canUseFor(ObjectIdResolver var1);
}

