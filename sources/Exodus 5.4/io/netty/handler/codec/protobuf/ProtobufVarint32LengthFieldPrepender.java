/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.protobuf.CodedOutputStream
 */
package io.netty.handler.codec.protobuf;

import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.OutputStream;

@ChannelHandler.Sharable
public class ProtobufVarint32LengthFieldPrepender
extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        int n = byteBuf.readableBytes();
        int n2 = CodedOutputStream.computeRawVarint32Size((int)n);
        byteBuf2.ensureWritable(n2 + n);
        CodedOutputStream codedOutputStream = CodedOutputStream.newInstance((OutputStream)new ByteBufOutputStream(byteBuf2), (int)n2);
        codedOutputStream.writeRawVarint32(n);
        codedOutputStream.flush();
        byteBuf2.writeBytes(byteBuf, byteBuf.readerIndex(), n);
    }
}

