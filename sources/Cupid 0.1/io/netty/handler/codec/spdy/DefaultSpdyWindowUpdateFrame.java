package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyWindowUpdateFrame implements SpdyWindowUpdateFrame {
  private int streamId;
  
  private int deltaWindowSize;
  
  public DefaultSpdyWindowUpdateFrame(int streamId, int deltaWindowSize) {
    setStreamId(streamId);
    setDeltaWindowSize(deltaWindowSize);
  }
  
  public int streamId() {
    return this.streamId;
  }
  
  public SpdyWindowUpdateFrame setStreamId(int streamId) {
    if (streamId < 0)
      throw new IllegalArgumentException("Stream-ID cannot be negative: " + streamId); 
    this.streamId = streamId;
    return this;
  }
  
  public int deltaWindowSize() {
    return this.deltaWindowSize;
  }
  
  public SpdyWindowUpdateFrame setDeltaWindowSize(int deltaWindowSize) {
    if (deltaWindowSize <= 0)
      throw new IllegalArgumentException("Delta-Window-Size must be positive: " + deltaWindowSize); 
    this.deltaWindowSize = deltaWindowSize;
    return this;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append(StringUtil.NEWLINE);
    buf.append("--> Stream-ID = ");
    buf.append(streamId());
    buf.append(StringUtil.NEWLINE);
    buf.append("--> Delta-Window-Size = ");
    buf.append(deltaWindowSize());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\DefaultSpdyWindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */