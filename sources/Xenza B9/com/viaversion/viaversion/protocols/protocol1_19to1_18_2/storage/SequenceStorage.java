// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage;

import com.viaversion.viaversion.api.connection.StorableObject;

public final class SequenceStorage implements StorableObject
{
    private final Object lock;
    private int sequenceId;
    
    public SequenceStorage() {
        this.lock = new Object();
        this.sequenceId = -1;
    }
    
    public int sequenceId() {
        synchronized (this.lock) {
            return this.sequenceId;
        }
    }
    
    public int setSequenceId(final int sequenceId) {
        synchronized (this.lock) {
            final int previousSequence = this.sequenceId;
            this.sequenceId = sequenceId;
            return previousSequence;
        }
    }
}
