package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMessage;

public class RtspRequestDecoder extends RtspObjectDecoder {
  public RtspRequestDecoder() {}
  
  public RtspRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength) {
    super(maxInitialLineLength, maxHeaderSize, maxContentLength);
  }
  
  public RtspRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength, boolean validateHeaders) {
    super(maxInitialLineLength, maxHeaderSize, maxContentLength, validateHeaders);
  }
  
  protected HttpMessage createMessage(String[] initialLine) throws Exception {
    return (HttpMessage)new DefaultHttpRequest(RtspVersions.valueOf(initialLine[2]), RtspMethods.valueOf(initialLine[0]), initialLine[1], this.validateHeaders);
  }
  
  protected HttpMessage createInvalidMessage() {
    return (HttpMessage)new DefaultHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
  }
  
  protected boolean isDecodingRequest() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\rtsp\RtspRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */