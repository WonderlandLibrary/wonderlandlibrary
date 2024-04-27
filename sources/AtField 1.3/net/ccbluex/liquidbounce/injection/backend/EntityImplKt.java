/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.minecraft.entity.Entity;

public final class EntityImplKt {
    public static final Entity unwrap(IEntity iEntity) {
        boolean bl = false;
        return ((EntityImpl)iEntity).getWrapped();
    }

    public static final IEntity wrap(Entity entity) {
        boolean bl = false;
        return new EntityImpl(entity);
    }
}

