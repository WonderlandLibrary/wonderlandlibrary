/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.injection.backend.EntityPlayerImpl;
import net.minecraft.entity.player.EntityPlayer;

public final class EntityPlayerImplKt {
    public static final EntityPlayer unwrap(IEntityPlayer $this$unwrap) {
        int $i$f$unwrap = 0;
        return (EntityPlayer)((EntityPlayerImpl)$this$unwrap).getWrapped();
    }

    public static final IEntityPlayer wrap(EntityPlayer $this$wrap) {
        int $i$f$wrap = 0;
        return new EntityPlayerImpl<EntityPlayer>($this$wrap);
    }
}

