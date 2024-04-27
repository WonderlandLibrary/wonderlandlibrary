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
    public static final EntityPlayerSP unwrap(IEntityPlayerSP iEntityPlayerSP) {
        boolean bl = false;
        return (EntityPlayerSP)((EntityPlayerSPImpl)iEntityPlayerSP).getWrapped();
    }

    public static final IEntityPlayerSP wrap(EntityPlayerSP entityPlayerSP) {
        boolean bl = false;
        return new EntityPlayerSPImpl(entityPlayerSP);
    }
}

