package io.netty.channel;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);
  
  protected abstract void initChannel(C paramC) throws Exception;
  
  public final void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    ChannelPipeline pipeline = ctx.pipeline();
    boolean success = false;
    try {
      initChannel((C)ctx.channel());
      pipeline.remove(this);
      ctx.fireChannelRegistered();
      success = true;
    } catch (Throwable t) {
      logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
    } finally {
      if (pipeline.context(this) != null)
        pipeline.remove(this); 
      if (!success)
        ctx.close(); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */