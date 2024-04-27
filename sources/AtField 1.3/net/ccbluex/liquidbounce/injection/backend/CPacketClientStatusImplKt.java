/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketClientStatus
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.injection.backend.CPacketClientStatusImpl;
import net.minecraft.network.play.client.CPacketClientStatus;

public final class CPacketClientStatusImplKt {
    public static final CPacketClientStatus unwrap(ICPacketClientStatus iCPacketClientStatus) {
        boolean bl = false;
        return (CPacketClientStatus)((CPacketClientStatusImpl)iCPacketClientStatus).getWrapped();
    }

    public static final ICPacketClientStatus wrap(CPacketClientStatus cPacketClientStatus) {
        boolean bl = false;
        return new CPacketClientStatusImpl(cPacketClientStatus);
    }
}

