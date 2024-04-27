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
    public static final IEntityOtherPlayerMP wrap(EntityOtherPlayerMP entityOtherPlayerMP) {
        boolean bl = false;
        return new EntityOtherPlayerMPImpl(entityOtherPlayerMP);
    }

    public static final EntityOtherPlayerMP unwrap(IEntityOtherPlayerMP iEntityOtherPlayerMP) {
        boolean bl = false;
        return (EntityOtherPlayerMP)((EntityOtherPlayerMPImpl)iEntityOtherPlayerMP).getWrapped();
    }
}

