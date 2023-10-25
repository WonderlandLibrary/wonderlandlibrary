/*
 * Decompiled with CFR 0.150.
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
    public int getClientProtocolVersion() {
        return this.clientProtocolVersion;
    }

    @Override
    public int getServerProtocolVersion() {
        return this.serverProtocolVersion;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ProtocolPathKeyImpl that = (ProtocolPathKeyImpl)o;
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

