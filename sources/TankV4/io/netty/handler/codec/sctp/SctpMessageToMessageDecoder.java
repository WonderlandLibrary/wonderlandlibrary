package io.netty.handler.codec.sctp;

import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class SctpMessageToMessageDecoder extends MessageToMessageDecoder {
   public boolean acceptInboundMessage(Object var1) throws Exception {
      if (var1 instanceof SctpMessage) {
         SctpMessage var2 = (SctpMessage)var1;
         if (var2.isComplete()) {
            return true;
         } else {
            throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", SctpMessageCompletionHandler.class.getSimpleName()));
         }
      } else {
         return false;
      }
   }
}
