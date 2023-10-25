package org.apache.http.impl.entity;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedOutputStream;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class EntitySerializer {
  private final ContentLengthStrategy lenStrategy;
  
  public EntitySerializer(ContentLengthStrategy lenStrategy) {
    this.lenStrategy = (ContentLengthStrategy)Args.notNull(lenStrategy, "Content length strategy");
  }
  
  protected OutputStream doSerialize(SessionOutputBuffer outbuffer, HttpMessage message) throws HttpException, IOException {
    long len = this.lenStrategy.determineLength(message);
    if (len == -2L)
      return (OutputStream)new ChunkedOutputStream(outbuffer); 
    if (len == -1L)
      return (OutputStream)new IdentityOutputStream(outbuffer); 
    return (OutputStream)new ContentLengthOutputStream(outbuffer, len);
  }
  
  public void serialize(SessionOutputBuffer outbuffer, HttpMessage message, HttpEntity entity) throws HttpException, IOException {
    Args.notNull(outbuffer, "Session output buffer");
    Args.notNull(message, "HTTP message");
    Args.notNull(entity, "HTTP entity");
    OutputStream outstream = doSerialize(outbuffer, message);
    entity.writeTo(outstream);
    outstream.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\entity\EntitySerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */