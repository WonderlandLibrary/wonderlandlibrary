package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame implements SpdyPingFrame {
  private int id;
  
  public DefaultSpdyPingFrame(int id) {
    setId(id);
  }
  
  public int id() {
    return this.id;
  }
  
  public SpdyPingFrame setId(int id) {
    this.id = id;
    return this;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append(StringUtil.NEWLINE);
    buf.append("--> ID = ");
    buf.append(id());
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\DefaultSpdyPingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */