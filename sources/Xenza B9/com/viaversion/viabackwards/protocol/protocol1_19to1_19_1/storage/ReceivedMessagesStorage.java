// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage;

import java.util.Arrays;
import com.viaversion.viaversion.api.minecraft.PlayerMessageSignature;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class ReceivedMessagesStorage implements StorableObject
{
    private final PlayerMessageSignature[] signatures;
    private int size;
    private int unacknowledged;
    
    public ReceivedMessagesStorage() {
        this.signatures = new PlayerMessageSignature[5];
    }
    
    public void add(final PlayerMessageSignature signature) {
        PlayerMessageSignature toPush = signature;
        for (int i = 0; i < this.size; ++i) {
            final PlayerMessageSignature entry = this.signatures[i];
            this.signatures[i] = toPush;
            toPush = entry;
            if (entry.uuid().equals(signature.uuid())) {
                return;
            }
        }
        if (this.size < this.signatures.length) {
            this.signatures[this.size++] = toPush;
        }
    }
    
    public PlayerMessageSignature[] lastSignatures() {
        return Arrays.copyOf(this.signatures, this.size);
    }
    
    public int tickUnacknowledged() {
        return this.unacknowledged++;
    }
    
    public void resetUnacknowledgedCount() {
        this.unacknowledged = 0;
    }
}
