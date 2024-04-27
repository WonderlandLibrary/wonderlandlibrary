/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityTNTPrimed
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityTNTPrimed;
import net.ccbluex.liquidbounce.injection.backend.EntityImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;

public final class EntityTNTPrimedImpl
extends EntityImpl
implements IEntityTNTPrimed {
    public EntityTNTPrimedImpl(EntityTNTPrimed entityTNTPrimed) {
        super((Entity)entityTNTPrimed);
    }

    @Override
    public int getFuse() {
        return ((EntityTNTPrimed)this.getWrapped()).func_184536_l();
    }
}

