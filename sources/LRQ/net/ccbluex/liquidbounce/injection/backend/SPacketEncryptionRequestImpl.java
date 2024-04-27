/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.login.server.SPacketEncryptionRequest
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.login.server.ISPacketEncryptionRequest;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.SPacketEncryptionRequest;

public final class SPacketEncryptionRequestImpl<T extends SPacketEncryptionRequest>
extends PacketImpl<T>
implements ISPacketEncryptionRequest {
    @Override
    public byte[] getVerifyToken() {
        return ((SPacketEncryptionRequest)this.getWrapped()).func_149607_e();
    }

    public SPacketEncryptionRequestImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

