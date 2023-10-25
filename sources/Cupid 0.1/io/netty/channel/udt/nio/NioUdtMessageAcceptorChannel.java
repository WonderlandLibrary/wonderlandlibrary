package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import java.util.List;

public class NioUdtMessageAcceptorChannel extends NioUdtAcceptorChannel {
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  public NioUdtMessageAcceptorChannel() {
    super(TypeUDT.DATAGRAM);
  }
  
  protected int doReadMessages(List<Object> buf) throws Exception {
    SocketChannelUDT channelUDT = javaChannel().accept();
    if (channelUDT == null)
      return 0; 
    buf.add(new NioUdtMessageConnectorChannel((Channel)this, channelUDT));
    return 1;
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\nio\NioUdtMessageAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */