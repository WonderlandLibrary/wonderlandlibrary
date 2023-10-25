/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksInitRequest;
import io.netty.handler.codec.socks.SocksProtocolVersion;
import io.netty.handler.codec.socks.SocksRequest;
import java.util.ArrayList;
import java.util.List;

public class SocksInitRequestDecoder
extends ReplayingDecoder<State> {
    private SocksRequest msg;
    private byte authSchemeNum;
    private SocksProtocolVersion version;
    private final List<SocksAuthScheme> authSchemes = new ArrayList<SocksAuthScheme>();
    private static final String name = "SOCKS_INIT_REQUEST_DECODER";

    public SocksInitRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch ((State)((Object)this.state())) {
            case CHECK_PROTOCOL_VERSION: {
                this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
                if (this.version != SocksProtocolVersion.SOCKS5) break;
                this.checkpoint(State.READ_AUTH_SCHEMES);
            }
            case READ_AUTH_SCHEMES: {
                this.authSchemes.clear();
                this.authSchemeNum = byteBuf.readByte();
                for (int i = 0; i < this.authSchemeNum; ++i) {
                    this.authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte()));
                }
                this.msg = new SocksInitRequest(this.authSchemes);
            }
        }
        channelHandlerContext.pipeline().remove(this);
        list.add(this.msg);
    }

    @Deprecated
    public static String getName() {
        return name;
    }

    static enum State {
        CHECK_PROTOCOL_VERSION,
        READ_AUTH_SCHEMES;

    }
}

