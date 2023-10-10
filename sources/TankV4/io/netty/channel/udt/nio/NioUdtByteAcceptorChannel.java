package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.ChannelMetadata;
import java.util.List;

public class NioUdtByteAcceptorChannel extends NioUdtAcceptorChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);

   public NioUdtByteAcceptorChannel() {
      super(TypeUDT.STREAM);
   }

   protected int doReadMessages(List var1) throws Exception {
      SocketChannelUDT var2 = this.javaChannel().accept();
      if (var2 == null) {
         return 0;
      } else {
         var1.add(new NioUdtByteConnectorChannel(this, var2));
         return 1;
      }
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }
}
