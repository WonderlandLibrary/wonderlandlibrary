/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viabackwards.api.entities.storage.EntityData;

public class EntityObjectData
extends EntityData {
    private final boolean isObject;
    private final int objectData;

    public EntityObjectData(int id, boolean isObject, int replacementId, int objectData) {
        super(id, replacementId);
        this.isObject = isObject;
        this.objectData = objectData;
    }

    @Override
    public boolean isObjectType() {
        return this.isObject;
    }

    @Override
    public int objectData() {
        return this.objectData;
    }
}

