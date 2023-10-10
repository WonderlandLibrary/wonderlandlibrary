// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import java.util.zip.Deflater;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.DecoderException;
import com.viaversion.viaversion.api.type.Type;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Inflater;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public class CompressionProvider implements Provider
{
    public void handlePlayCompression(final UserConnection user, final int threshold) {
        if (!user.isClientSide()) {
            throw new IllegalStateException("PLAY state Compression packet is unsupported");
        }
        final ChannelPipeline pipe = user.getChannel().pipeline();
        if (threshold < 0) {
            if (pipe.get("compress") != null) {
                pipe.remove("compress");
                pipe.remove("decompress");
            }
        }
        else if (pipe.get("compress") == null) {
            pipe.addBefore(Via.getManager().getInjector().getEncoderName(), "compress", this.getEncoder(threshold));
            pipe.addBefore(Via.getManager().getInjector().getDecoderName(), "decompress", this.getDecoder(threshold));
        }
        else {
            ((CompressionHandler)pipe.get("compress")).setCompressionThreshold(threshold);
            ((CompressionHandler)pipe.get("decompress")).setCompressionThreshold(threshold);
        }
    }
    
    protected CompressionHandler getEncoder(final int threshold) {
        return new Compressor(threshold);
    }
    
    protected CompressionHandler getDecoder(final int threshold) {
        return new Decompressor(threshold);
    }
    
    private static class Decompressor extends MessageToMessageDecoder<ByteBuf> implements CompressionHandler
    {
        private final Inflater inflater;
        private int threshold;
        
        public Decompressor(final int var1) {
            this.threshold = var1;
            this.inflater = new Inflater();
        }
        
        @Override
        protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
            if (!in.isReadable()) {
                return;
            }
            final int outLength = Type.VAR_INT.readPrimitive(in);
            if (outLength == 0) {
                out.add(in.readBytes(in.readableBytes()));
                return;
            }
            if (outLength < this.threshold) {
                throw new DecoderException("Badly compressed packet - size of " + outLength + " is below server threshold of " + this.threshold);
            }
            if (outLength > 2097152) {
                throw new DecoderException("Badly compressed packet - size of " + outLength + " is larger than protocol maximum of " + 2097152);
            }
            ByteBuf temp = in;
            if (!in.hasArray()) {
                temp = ctx.alloc().heapBuffer().writeBytes(in);
            }
            else {
                in.retain();
            }
            final ByteBuf output = ctx.alloc().heapBuffer(outLength, outLength);
            try {
                this.inflater.setInput(temp.array(), temp.arrayOffset() + temp.readerIndex(), temp.readableBytes());
                output.writerIndex(output.writerIndex() + this.inflater.inflate(output.array(), output.arrayOffset(), outLength));
                out.add(output.retain());
            }
            finally {
                output.release();
                temp.release();
                this.inflater.reset();
            }
        }
        
        @Override
        public void setCompressionThreshold(final int threshold) {
            this.threshold = threshold;
        }
    }
    
    private static class Compressor extends MessageToByteEncoder<ByteBuf> implements CompressionHandler
    {
        private final Deflater deflater;
        private int threshold;
        
        public Compressor(final int var1) {
            this.threshold = var1;
            this.deflater = new Deflater();
        }
        
        @Override
        protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
            final int frameLength = in.readableBytes();
            if (frameLength < this.threshold) {
                out.writeByte(0);
                out.writeBytes(in);
                return;
            }
            Type.VAR_INT.writePrimitive(out, frameLength);
            ByteBuf temp = in;
            if (!in.hasArray()) {
                temp = ctx.alloc().heapBuffer().writeBytes(in);
            }
            else {
                in.retain();
            }
            final ByteBuf output = ctx.alloc().heapBuffer();
            try {
                this.deflater.setInput(temp.array(), temp.arrayOffset() + temp.readerIndex(), temp.readableBytes());
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    output.ensureWritable(4096);
                    output.writerIndex(output.writerIndex() + this.deflater.deflate(output.array(), output.arrayOffset() + output.writerIndex(), output.writableBytes()));
                }
                out.writeBytes(output);
            }
            finally {
                output.release();
                temp.release();
                this.deflater.reset();
            }
        }
        
        @Override
        public void setCompressionThreshold(final int threshold) {
            this.threshold = threshold;
        }
    }
    
    public interface CompressionHandler extends ChannelHandler
    {
        void setCompressionThreshold(final int p0);
    }
}
