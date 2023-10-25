package io.netty.handler.codec.sctp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class SctpInboundByteStreamHandler extends MessageToMessageDecoder<SctpMessage> {
  private final int protocolIdentifier;
  
  private final int streamIdentifier;
  
  public SctpInboundByteStreamHandler(int protocolIdentifier, int streamIdentifier) {
    this.protocolIdentifier = protocolIdentifier;
    this.streamIdentifier = streamIdentifier;
  }
  
  public final boolean acceptInboundMessage(Object msg) throws Exception {
    if (super.acceptInboundMessage(msg))
      return acceptInboundMessage((SctpMessage)msg); 
    return false;
  }
  
  protected boolean acceptInboundMessage(SctpMessage msg) {
    return (msg.protocolIdentifier() == this.protocolIdentifier && msg.streamIdentifier() == this.streamIdentifier);
  }
  
  protected void decode(ChannelHandlerContext ctx, SctpMessage msg, List<Object> out) throws Exception {
    if (!msg.isComplete())
      throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", new Object[] { SctpMessageCompletionHandler.class.getSimpleName() })); 
    out.add(msg.content().retain());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\sctp\SctpInboundByteStreamHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */