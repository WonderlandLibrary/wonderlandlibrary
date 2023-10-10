// 
// Decompiled by Procyon v0.5.36
// 

package viamcp.utils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;

public class NettyUtil
{
    public static ChannelPipeline decodeEncodePlacement(final ChannelPipeline instance, String base, final String newHandler, final ChannelHandler handler) {
        final String s = base;
        switch (s) {
            case "decoder": {
                if (instance.get("via-decoder") != null) {
                    base = "via-decoder";
                    break;
                }
                break;
            }
            case "encoder": {
                if (instance.get("via-encoder") != null) {
                    base = "via-encoder";
                    break;
                }
                break;
            }
        }
        return instance.addBefore(base, newHandler, handler);
    }
}
