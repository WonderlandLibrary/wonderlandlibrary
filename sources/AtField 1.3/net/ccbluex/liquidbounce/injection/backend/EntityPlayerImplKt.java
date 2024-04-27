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
    public static final IEntityPlayer wrap(EntityPlayer entityPlayer) {
        boolean bl = false;
        return new EntityPlayerImpl(entityPlayer);
    }

    public static final EntityPlayer unwrap(IEntityPlayer iEntityPlayer) {
        boolean bl = false;
        return (EntityPlayer)((EntityPlayerImpl)iEntityPlayer).getWrapped();
    }
}

