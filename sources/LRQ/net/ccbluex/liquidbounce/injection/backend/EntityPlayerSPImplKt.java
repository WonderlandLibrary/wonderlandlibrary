/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerSPImpl;
import net.minecraft.client.entity.EntityPlayerSP;

public final class EntityPlayerSPImplKt {
    public static final EntityPlayerSP unwrap(IEntityPlayerSP $this$unwrap) {
        int $i$f$unwrap = 0;
        return (EntityPlayerSP)((EntityPlayerSPImpl)$this$unwrap).getWrapped();
    }

    public static final IEntityPlayerSP wrap(EntityPlayerSP $this$wrap) {
        int $i$f$wrap = 0;
        return new EntityPlayerSPImpl<EntityPlayerSP>($this$wrap);
    }
}

