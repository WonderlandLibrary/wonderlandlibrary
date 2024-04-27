/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketUseEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.injection.backend.CPacketUseEntityImpl;
import net.minecraft.network.play.client.CPacketUseEntity;

public final class CPacketUseEntityImplKt {
    public static final ICPacketUseEntity wrap(CPacketUseEntity cPacketUseEntity) {
        boolean bl = false;
        return new CPacketUseEntityImpl(cPacketUseEntity);
    }

    public static final CPacketUseEntity unwrap(ICPacketUseEntity iCPacketUseEntity) {
        boolean bl = false;
        return (CPacketUseEntity)((CPacketUseEntityImpl)iCPacketUseEntity).getWrapped();
    }
}

