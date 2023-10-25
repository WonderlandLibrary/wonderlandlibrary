/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.handler.codec.socks.SocksMessage;
import io.netty.handler.codec.socks.SocksMessageType;
import io.netty.handler.codec.socks.SocksRequestType;

public abstract class SocksRequest
extends SocksMessage {
    private final SocksRequestType requestType;

    protected SocksRequest(SocksRequestType socksRequestType) {
        super(SocksMessageType.REQUEST);
        if (socksRequestType == null) {
            throw new NullPointerException("requestType");
        }
        this.requestType = socksRequestType;
    }

    public SocksRequestType requestType() {
        return this.requestType;
    }
}

