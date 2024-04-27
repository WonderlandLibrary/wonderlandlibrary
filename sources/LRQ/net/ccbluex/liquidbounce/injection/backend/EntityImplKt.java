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
    public static final Entity unwrap(IEntity $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((EntityImpl)$this$unwrap).getWrapped();
    }

    public static final IEntity wrap(Entity $this$wrap) {
        int $i$f$wrap = 0;
        return new EntityImpl<Entity>($this$wrap);
    }
}

