// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.entities.storage;

import com.viaversion.viabackwards.api.BackwardsProtocol;

public class EntityObjectData extends EntityData
{
    private final int objectData;
    
    public EntityObjectData(final BackwardsProtocol<?, ?, ?, ?> protocol, final String key, final int id, final int replacementId, final int objectData) {
        super(protocol, key, id, replacementId);
        this.objectData = objectData;
    }
    
    @Override
    public boolean isObjectType() {
        return true;
    }
    
    @Override
    public int objectData() {
        return this.objectData;
    }
}
