package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class GzipDecompressingEntity extends DecompressingEntity {
  public GzipDecompressingEntity(HttpEntity entity) {
    super(entity);
  }
  
  InputStream decorate(InputStream wrapped) throws IOException {
    return new GZIPInputStream(wrapped);
  }
  
  public Header getContentEncoding() {
    return null;
  }
  
  public long getContentLength() {
    return -1L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\entity\GzipDecompressingEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */