package io.netty.channel.sctp;

import com.sun.nio.sctp.MessageInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;

public final class SctpMessage extends DefaultByteBufHolder {
  private final int streamIdentifier;
  
  private final int protocolIdentifier;
  
  private final MessageInfo msgInfo;
  
  public SctpMessage(int protocolIdentifier, int streamIdentifier, ByteBuf payloadBuffer) {
    super(payloadBuffer);
    this.protocolIdentifier = protocolIdentifier;
    this.streamIdentifier = streamIdentifier;
    this.msgInfo = null;
  }
  
  public SctpMessage(MessageInfo msgInfo, ByteBuf payloadBuffer) {
    super(payloadBuffer);
    if (msgInfo == null)
      throw new NullPointerException("msgInfo"); 
    this.msgInfo = msgInfo;
    this.streamIdentifier = msgInfo.streamNumber();
    this.protocolIdentifier = msgInfo.payloadProtocolID();
  }
  
  public int streamIdentifier() {
    return this.streamIdentifier;
  }
  
  public int protocolIdentifier() {
    return this.protocolIdentifier;
  }
  
  public MessageInfo messageInfo() {
    return this.msgInfo;
  }
  
  public boolean isComplete() {
    if (this.msgInfo != null)
      return this.msgInfo.isComplete(); 
    return true;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    SctpMessage sctpFrame = (SctpMessage)o;
    if (this.protocolIdentifier != sctpFrame.protocolIdentifier)
      return false; 
    if (this.streamIdentifier != sctpFrame.streamIdentifier)
      return false; 
    if (!content().equals(sctpFrame.content()))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = this.streamIdentifier;
    result = 31 * result + this.protocolIdentifier;
    result = 31 * result + content().hashCode();
    return result;
  }
  
  public SctpMessage copy() {
    if (this.msgInfo == null)
      return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, content().copy()); 
    return new SctpMessage(this.msgInfo, content().copy());
  }
  
  public SctpMessage duplicate() {
    if (this.msgInfo == null)
      return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, content().duplicate()); 
    return new SctpMessage(this.msgInfo, content().copy());
  }
  
  public SctpMessage retain() {
    super.retain();
    return this;
  }
  
  public SctpMessage retain(int increment) {
    super.retain(increment);
    return this;
  }
  
  public String toString() {
    if (refCnt() == 0)
      return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=(FREED)}"; 
    return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=" + ByteBufUtil.hexDump(content()) + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\sctp\SctpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */