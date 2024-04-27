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
    public static final EntityLivingBase unwrap(IEntityLivingBase $this$unwrap) {
        int $i$f$unwrap = 0;
        return (EntityLivingBase)((EntityLivingBaseImpl)$this$unwrap).getWrapped();
    }

    public static final IEntityLivingBase wrap(EntityLivingBase $this$wrap) {
        int $i$f$wrap = 0;
        return new EntityLivingBaseImpl<EntityLivingBase>($this$wrap);
    }
}

