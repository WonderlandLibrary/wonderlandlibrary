// 
// Decompiled by Procyon v0.5.36
// 

package viamcp.handler;

import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.InvocationTargetException;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.viaversion.viaversion.util.PipelineUtil;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class CommonTransformer
{
    public static final String HANDLER_DECODER_NAME = "via-decoder";
    public static final String HANDLER_ENCODER_NAME = "via-encoder";
    
    public static void decompress(final ChannelHandlerContext ctx, final ByteBuf buf) throws InvocationTargetException {
        final ChannelHandler handler = ctx.pipeline().get("decompress");
        final ByteBuf decompressed = (handler instanceof MessageToMessageDecoder) ? PipelineUtil.callDecode((MessageToMessageDecoder)handler, ctx, buf).get(0) : PipelineUtil.callDecode((ByteToMessageDecoder)handler, ctx, buf).get(0);
        try {
            buf.clear().writeBytes(decompressed);
        }
        finally {
            decompressed.release();
        }
    }
    
    public static void compress(final ChannelHandlerContext ctx, final ByteBuf buf) throws Exception {
        final ByteBuf compressed = ctx.alloc().buffer();
        try {
            PipelineUtil.callEncode((MessageToByteEncoder)ctx.pipeline().get("compress"), ctx, buf, compressed);
            buf.clear().writeBytes(compressed);
        }
        finally {
            compressed.release();
        }
    }
}
