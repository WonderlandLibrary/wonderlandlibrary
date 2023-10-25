package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class InputStreamEntity extends AbstractHttpEntity {
  private final InputStream content;
  
  private final long length;
  
  public InputStreamEntity(InputStream instream) {
    this(instream, -1L);
  }
  
  public InputStreamEntity(InputStream instream, long length) {
    this(instream, length, null);
  }
  
  public InputStreamEntity(InputStream instream, ContentType contentType) {
    this(instream, -1L, contentType);
  }
  
  public InputStreamEntity(InputStream instream, long length, ContentType contentType) {
    this.content = (InputStream)Args.notNull(instream, "Source input stream");
    this.length = length;
    if (contentType != null)
      setContentType(contentType.toString()); 
  }
  
  public boolean isRepeatable() {
    return false;
  }
  
  public long getContentLength() {
    return this.length;
  }
  
  public InputStream getContent() throws IOException {
    return this.content;
  }
  
  public void writeTo(OutputStream outstream) throws IOException {
    Args.notNull(outstream, "Output stream");
    InputStream instream = this.content;
    try {
      byte[] buffer = new byte[4096];
      if (this.length < 0L) {
        int l;
        while ((l = instream.read(buffer)) != -1)
          outstream.write(buffer, 0, l); 
      } else {
        long remaining = this.length;
        while (remaining > 0L) {
          int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
          if (l == -1)
            break; 
          outstream.write(buffer, 0, l);
          remaining -= l;
        } 
      } 
    } finally {
      instream.close();
    } 
  }
  
  public boolean isStreaming() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\entity\InputStreamEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */