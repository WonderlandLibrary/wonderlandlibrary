/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import java.util.List;

@ChannelHandler.Sharable
public class WebSocket00FrameEncoder
extends MessageToMessageEncoder<WebSocketFrame>
implements WebSocketFrameEncoder {
    private static final ByteBuf _0XFF_0X00;
    private static final ByteBuf _0X00;
    private static final ByteBuf _0XFF;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        block7: {
            block8: {
                block6: {
                    if (!(webSocketFrame instanceof TextWebSocketFrame)) break block6;
                    ByteBuf byteBuf = webSocketFrame.content();
                    list.add(_0X00.duplicate());
                    list.add(byteBuf.retain());
                    list.add(_0XFF.duplicate());
                    break block7;
                }
                if (!(webSocketFrame instanceof CloseWebSocketFrame)) break block8;
                list.add(_0XFF_0X00.duplicate());
                break block7;
            }
            ByteBuf byteBuf = webSocketFrame.content();
            int n = byteBuf.readableBytes();
            ByteBuf byteBuf2 = channelHandlerContext.alloc().buffer(5);
            boolean bl = true;
            byteBuf2.writeByte(-128);
            int n2 = n >>> 28 & 0x7F;
            int n3 = n >>> 14 & 0x7F;
            int n4 = n >>> 7 & 0x7F;
            int n5 = n & 0x7F;
            if (n2 == 0) {
                if (n3 == 0) {
                    if (n4 == 0) {
                        byteBuf2.writeByte(n5);
                    } else {
                        byteBuf2.writeByte(n4 | 0x80);
                        byteBuf2.writeByte(n5);
                    }
                } else {
                    byteBuf2.writeByte(n3 | 0x80);
                    byteBuf2.writeByte(n4 | 0x80);
                    byteBuf2.writeByte(n5);
                }
            } else {
                byteBuf2.writeByte(n2 | 0x80);
                byteBuf2.writeByte(n3 | 0x80);
                byteBuf2.writeByte(n4 | 0x80);
                byteBuf2.writeByte(n5);
            }
            list.add(byteBuf2);
            list.add(byteBuf.retain());
            bl = false;
            if (!bl) break block7;
            byteBuf2.release();
        }
    }

    static {
        _0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(0));
        _0XFF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(1, 1).writeByte(-1));
        _0XFF_0X00 = Unpooled.unreleasableBuffer(Unpooled.directBuffer(2, 2).writeByte(-1).writeByte(0));
    }
}

