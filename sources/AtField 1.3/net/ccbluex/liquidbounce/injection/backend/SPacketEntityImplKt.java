/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.injection.backend.SPacketEntityImpl;
import net.minecraft.network.play.server.SPacketEntity;

public final class SPacketEntityImplKt {
    public static final ISPacketEntity wrap(SPacketEntity sPacketEntity) {
        boolean bl = false;
        return new SPacketEntityImpl(sPacketEntity);
    }

    public static final SPacketEntity unwrap(ISPacketEntity iSPacketEntity) {
        boolean bl = false;
        return (SPacketEntity)((SPacketEntityImpl)iSPacketEntity).getWrapped();
    }
}

