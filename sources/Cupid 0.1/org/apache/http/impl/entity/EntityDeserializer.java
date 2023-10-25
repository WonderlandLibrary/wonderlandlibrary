package org.apache.http.impl.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class EntityDeserializer {
  private final ContentLengthStrategy lenStrategy;
  
  public EntityDeserializer(ContentLengthStrategy lenStrategy) {
    this.lenStrategy = (ContentLengthStrategy)Args.notNull(lenStrategy, "Content length strategy");
  }
  
  protected BasicHttpEntity doDeserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
    BasicHttpEntity entity = new BasicHttpEntity();
    long len = this.lenStrategy.determineLength(message);
    if (len == -2L) {
      entity.setChunked(true);
      entity.setContentLength(-1L);
      entity.setContent((InputStream)new ChunkedInputStream(inbuffer));
    } else if (len == -1L) {
      entity.setChunked(false);
      entity.setContentLength(-1L);
      entity.setContent((InputStream)new IdentityInputStream(inbuffer));
    } else {
      entity.setChunked(false);
      entity.setContentLength(len);
      entity.setContent((InputStream)new ContentLengthInputStream(inbuffer, len));
    } 
    Header contentTypeHeader = message.getFirstHeader("Content-Type");
    if (contentTypeHeader != null)
      entity.setContentType(contentTypeHeader); 
    Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
    if (contentEncodingHeader != null)
      entity.setContentEncoding(contentEncodingHeader); 
    return entity;
  }
  
  public HttpEntity deserialize(SessionInputBuffer inbuffer, HttpMessage message) throws HttpException, IOException {
    Args.notNull(inbuffer, "Session input buffer");
    Args.notNull(message, "HTTP message");
    return (HttpEntity)doDeserialize(inbuffer, message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\entity\EntityDeserializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */