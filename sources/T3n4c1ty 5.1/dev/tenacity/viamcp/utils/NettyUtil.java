package dev.tenacity.viamcp.utils;

import dev.tenacity.viamcp.handler.CommonTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;

public class NettyUtil {
    public static ChannelPipeline decodeEncodePlacement(ChannelPipeline instance, String base, String newHandler, ChannelHandler handler) {
        switch (base) {
            case "decoder": {
                if (instance.get(CommonTransformer.HANDLER_DECODER_NAME) != null) {
                    base = CommonTransformer.HANDLER_DECODER_NAME;
                }

                break;
            }
            case "encoder": {
                if (instance.get(CommonTransformer.HANDLER_ENCODER_NAME) != null) {
                    base = CommonTransformer.HANDLER_ENCODER_NAME;
                }

                break;
            }
        }

        return instance.addBefore(base, newHandler, handler);
    }
}
