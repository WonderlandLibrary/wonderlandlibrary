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
    public static final SPacketEncryptionRequest unwrap(ISPacketEncryptionRequest $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketEncryptionRequest)((SPacketEncryptionRequestImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketEncryptionRequest wrap(SPacketEncryptionRequest $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketEncryptionRequestImpl<SPacketEncryptionRequest>($this$wrap);
    }
}

