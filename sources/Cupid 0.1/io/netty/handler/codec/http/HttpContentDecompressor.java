package io.netty.handler.codec.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;

public class HttpContentDecompressor extends HttpContentDecoder {
  private final boolean strict;
  
  public HttpContentDecompressor() {
    this(false);
  }
  
  public HttpContentDecompressor(boolean strict) {
    this.strict = strict;
  }
  
  protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception {
    if ("gzip".equalsIgnoreCase(contentEncoding) || "x-gzip".equalsIgnoreCase(contentEncoding))
      return new EmbeddedChannel(new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) }); 
    if ("deflate".equalsIgnoreCase(contentEncoding) || "x-deflate".equalsIgnoreCase(contentEncoding)) {
      ZlibWrapper wrapper;
      if (this.strict) {
        wrapper = ZlibWrapper.ZLIB;
      } else {
        wrapper = ZlibWrapper.ZLIB_OR_NONE;
      } 
      return new EmbeddedChannel(new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibDecoder(wrapper) });
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpContentDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */