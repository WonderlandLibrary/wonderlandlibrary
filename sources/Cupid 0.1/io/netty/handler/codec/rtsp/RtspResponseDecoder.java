package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponseStatus;

public class RtspResponseDecoder extends RtspObjectDecoder {
  private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");
  
  public RtspResponseDecoder() {}
  
  public RtspResponseDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength) {
    super(maxInitialLineLength, maxHeaderSize, maxContentLength);
  }
  
  public RtspResponseDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength, boolean validateHeaders) {
    super(maxInitialLineLength, maxHeaderSize, maxContentLength, validateHeaders);
  }
  
  protected HttpMessage createMessage(String[] initialLine) throws Exception {
    return (HttpMessage)new DefaultHttpResponse(RtspVersions.valueOf(initialLine[0]), new HttpResponseStatus(Integer.parseInt(initialLine[1]), initialLine[2]), this.validateHeaders);
  }
  
  protected HttpMessage createInvalidMessage() {
    return (HttpMessage)new DefaultHttpResponse(RtspVersions.RTSP_1_0, UNKNOWN_STATUS, this.validateHeaders);
  }
  
  protected boolean isDecodingRequest() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\rtsp\RtspResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */