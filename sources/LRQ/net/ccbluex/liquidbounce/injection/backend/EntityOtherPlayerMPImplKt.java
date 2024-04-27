/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP;
import net.ccbluex.liquidbounce.injection.backend.EntityOtherPlayerMPImpl;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public final class EntityOtherPlayerMPImplKt {
    public static final EntityOtherPlayerMP unwrap(IEntityOtherPlayerMP $this$unwrap) {
        int $i$f$unwrap = 0;
        return (EntityOtherPlayerMP)((EntityOtherPlayerMPImpl)$this$unwrap).getWrapped();
    }

    public static final IEntityOtherPlayerMP wrap(EntityOtherPlayerMP $this$wrap) {
        int $i$f$wrap = 0;
        return new EntityOtherPlayerMPImpl<EntityOtherPlayerMP>($this$wrap);
    }
}

