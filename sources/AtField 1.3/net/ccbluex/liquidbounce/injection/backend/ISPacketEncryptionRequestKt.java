/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.login.server.SPacketEncryptionRequest
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.login.server.ISPacketEncryptionRequest;
import net.ccbluex.liquidbounce.injection.backend.SPacketEncryptionRequestImpl;
import net.minecraft.network.login.server.SPacketEncryptionRequest;

public final class ISPacketEncryptionRequestKt {
    public static final SPacketEncryptionRequest unwrap(ISPacketEncryptionRequest iSPacketEncryptionRequest) {
        boolean bl = false;
        return (SPacketEncryptionRequest)((SPacketEncryptionRequestImpl)iSPacketEncryptionRequest).getWrapped();
    }

    public static final ISPacketEncryptionRequest wrap(SPacketEncryptionRequest sPacketEncryptionRequest) {
        boolean bl = false;
        return new SPacketEncryptionRequestImpl(sPacketEncryptionRequest);
    }
}

