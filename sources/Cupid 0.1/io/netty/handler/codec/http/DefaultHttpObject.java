package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;

public class DefaultHttpObject implements HttpObject {
  private DecoderResult decoderResult = DecoderResult.SUCCESS;
  
  public DecoderResult getDecoderResult() {
    return this.decoderResult;
  }
  
  public void setDecoderResult(DecoderResult decoderResult) {
    if (decoderResult == null)
      throw new NullPointerException("decoderResult"); 
    this.decoderResult = decoderResult;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\DefaultHttpObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */