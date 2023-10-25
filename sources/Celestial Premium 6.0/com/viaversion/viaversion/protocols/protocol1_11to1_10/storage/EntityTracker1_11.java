/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.protocols.protocol1_11to1_10.storage;

import com.google.common.collect.Sets;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_11Types;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import java.util.Set;

public class EntityTracker1_11
extends EntityTrackerBase {
    private final Set<Integer> holograms = Sets.newConcurrentHashSet();

    public EntityTracker1_11(UserConnection user) {
        super(user, Entity1_11Types.EntityType.PLAYER);
    }

    @Override
    public void removeEntity(int entityId) {
        super.removeEntity(entityId);
        if (this.isHologram(entityId)) {
            this.removeHologram(entityId);
        }
    }

    public void addHologram(int entId) {
        this.holograms.add(entId);
    }

    public boolean isHologram(int entId) {
        return this.holograms.contains(entId);
    }

    public void removeHologram(int entId) {
        this.holograms.remove(entId);
    }
}

