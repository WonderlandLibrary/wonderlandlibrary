package io.netty.handler.codec.spdy;

public abstract class DefaultSpdyStreamFrame implements SpdyStreamFrame {
  private int streamId;
  
  private boolean last;
  
  protected DefaultSpdyStreamFrame(int streamId) {
    setStreamId(streamId);
  }
  
  public int streamId() {
    return this.streamId;
  }
  
  public SpdyStreamFrame setStreamId(int streamId) {
    if (streamId <= 0)
      throw new IllegalArgumentException("Stream-ID must be positive: " + streamId); 
    this.streamId = streamId;
    return this;
  }
  
  public boolean isLast() {
    return this.last;
  }
  
  public SpdyStreamFrame setLast(boolean last) {
    this.last = last;
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\DefaultSpdyStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */