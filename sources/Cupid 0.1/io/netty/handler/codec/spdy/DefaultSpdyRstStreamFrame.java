package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyRstStreamFrame extends DefaultSpdyStreamFrame implements SpdyRstStreamFrame {
  private SpdyStreamStatus status;
  
  public DefaultSpdyRstStreamFrame(int streamId, int statusCode) {
    this(streamId, SpdyStreamStatus.valueOf(statusCode));
  }
  
  public DefaultSpdyRstStreamFrame(int streamId, SpdyStreamStatus status) {
    super(streamId);
    setStatus(status);
  }
  
  public SpdyRstStreamFrame setStreamId(int streamId) {
    super.setStreamId(streamId);
    return this;
  }
  
  public SpdyRstStreamFrame setLast(boolean last) {
    super.setLast(last);
    return this;
  }
  
  public SpdyStreamStatus status() {
    return this.status;
  }
  
  public SpdyRstStreamFrame setStatus(SpdyStreamStatus status) {
    this.status = status;
    return this;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append(StringUtil.NEWLINE);
    buf.append("--> Stream-ID = ");
    buf.append(streamId());
    buf.append(StringUtil.NEWLINE);
    buf.append("--> Status: ");
    buf.append(status());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\DefaultSpdyRstStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */