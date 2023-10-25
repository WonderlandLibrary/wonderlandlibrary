/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.protobuf.CodedInputStream
 */
package io.netty.handler.codec.protobuf;

import com.google.protobuf.CodedInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class ProtobufVarint32FrameDecoder
extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        byte[] byArray = new byte[5];
        for (int i = 0; i < byArray.length; ++i) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }
            byArray[i] = byteBuf.readByte();
            if (byArray[i] < 0) continue;
            int n = CodedInputStream.newInstance((byte[])byArray, (int)0, (int)(i + 1)).readRawVarint32();
            if (n < 0) {
                throw new CorruptedFrameException("negative length: " + n);
            }
            if (byteBuf.readableBytes() < n) {
                byteBuf.resetReaderIndex();
                return;
            }
            list.add(byteBuf.readBytes(n));
            return;
        }
        throw new CorruptedFrameException("length wider than 32-bit");
    }
}

