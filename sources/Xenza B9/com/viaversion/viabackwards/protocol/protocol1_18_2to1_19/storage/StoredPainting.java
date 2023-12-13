// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage;

import com.viaversion.viaversion.api.minecraft.Position;
import java.util.UUID;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class StoredPainting implements StorableObject
{
    private final int entityId;
    private final UUID uuid;
    private final Position position;
    private final byte direction;
    
    public StoredPainting(final int entityId, final UUID uuid, final Position position, final int direction3d) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.position = position;
        this.direction = this.to2dDirection(direction3d);
    }
    
    public int entityId() {
        return this.entityId;
    }
    
    public UUID uuid() {
        return this.uuid;
    }
    
    public Position position() {
        return this.position;
    }
    
    public byte direction() {
        return this.direction;
    }
    
    private byte to2dDirection(final int direction) {
        switch (direction) {
            case 0:
            case 1: {
                return -1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 0;
            }
            case 4: {
                return 1;
            }
            case 5: {
                return 3;
            }
            default: {
                throw new IllegalArgumentException("Invalid direction: " + direction);
            }
        }
    }
}
