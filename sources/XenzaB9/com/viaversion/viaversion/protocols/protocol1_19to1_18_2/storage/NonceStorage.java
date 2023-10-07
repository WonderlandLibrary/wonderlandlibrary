// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public final class NonceStorage implements StorableObject
{
    private final byte[] nonce;
    
    public NonceStorage(final byte[] nonce) {
        this.nonce = nonce;
    }
    
    public byte[] nonce() {
        return this.nonce;
    }
}
