/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.handler.codec.socks.SocksMessage;
import io.netty.handler.codec.socks.SocksMessageType;
import io.netty.handler.codec.socks.SocksResponseType;

public abstract class SocksResponse
extends SocksMessage {
    private final SocksResponseType responseType;

    protected SocksResponse(SocksResponseType socksResponseType) {
        super(SocksMessageType.RESPONSE);
        if (socksResponseType == null) {
            throw new NullPointerException("responseType");
        }
        this.responseType = socksResponseType;
    }

    public SocksResponseType responseType() {
        return this.responseType;
    }
}

