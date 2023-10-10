/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.api.protocol.ProtocolPathKey;

public class ProtocolPathKeyImpl
implements ProtocolPathKey {
    private final int clientProtocolVersion;
    private final int serverProtocolVersion;

    public ProtocolPathKeyImpl(int clientProtocolVersion, int serverProtocolVersion) {
        this.clientProtocolVersion = clientProtocolVersion;
        this.serverProtocolVersion = serverProtocolVersion;
    }

    @Override
    public int clientProtocolVersion() {
        return this.clientProtocolVersion;
    }

    @Override
    public int serverProtocolVersion() {
        return this.serverProtocolVersion;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ProtocolPathKeyImpl that = (ProtocolPathKeyImpl)o2;
        if (this.clientProtocolVersion != that.clientProtocolVersion) {
            return false;
        }
        return this.serverProtocolVersion == that.serverProtocolVersion;
    }

    public int hashCode() {
        int result = this.clientProtocolVersion;
        result = 31 * result + this.serverProtocolVersion;
        return result;
    }
}

