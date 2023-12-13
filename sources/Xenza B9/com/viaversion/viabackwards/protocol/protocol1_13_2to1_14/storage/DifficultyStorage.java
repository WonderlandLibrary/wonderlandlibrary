// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StoredObject;

public class DifficultyStorage extends StoredObject
{
    private byte difficulty;
    
    public DifficultyStorage(final UserConnection user) {
        super(user);
    }
    
    public byte getDifficulty() {
        return this.difficulty;
    }
    
    public void setDifficulty(final byte difficulty) {
        this.difficulty = difficulty;
    }
}
