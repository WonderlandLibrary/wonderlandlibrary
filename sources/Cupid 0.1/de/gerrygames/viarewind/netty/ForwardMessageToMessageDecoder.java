package de.gerrygames.viarewind.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class ForwardMessageToMessageDecoder extends MessageToMessageDecoder {
  protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
    out.add(msg);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\netty\ForwardMessageToMessageDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */