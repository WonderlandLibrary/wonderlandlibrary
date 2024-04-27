/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketCustomPayload
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.injection.backend.CPacketCustomPayloadImpl;
import net.minecraft.network.play.client.CPacketCustomPayload;

public final class CPacketCustomPayloadImplKt {
    public static final CPacketCustomPayload unwrap(ICPacketCustomPayload iCPacketCustomPayload) {
        boolean bl = false;
        return (CPacketCustomPayload)((CPacketCustomPayloadImpl)iCPacketCustomPayload).getWrapped();
    }

    public static final ICPacketCustomPayload wrap(CPacketCustomPayload cPacketCustomPayload) {
        boolean bl = false;
        return new CPacketCustomPayloadImpl(cPacketCustomPayload);
    }
}

