/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.injection.backend.EntityLivingBaseImpl;
import net.minecraft.entity.EntityLivingBase;

public final class EntityLivingBaseImplKt {
    public static final EntityLivingBase unwrap(IEntityLivingBase iEntityLivingBase) {
        boolean bl = false;
        return (EntityLivingBase)((EntityLivingBaseImpl)iEntityLivingBase).getWrapped();
    }

    public static final IEntityLivingBase wrap(EntityLivingBase entityLivingBase) {
        boolean bl = false;
        return new EntityLivingBaseImpl(entityLivingBase);
    }
}

