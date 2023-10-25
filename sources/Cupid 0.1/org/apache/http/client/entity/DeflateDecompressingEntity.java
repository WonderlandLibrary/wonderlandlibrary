package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity {
  public DeflateDecompressingEntity(HttpEntity entity) {
    super(entity);
  }
  
  InputStream decorate(InputStream wrapped) throws IOException {
    return new DeflateInputStream(wrapped);
  }
  
  public Header getContentEncoding() {
    return null;
  }
  
  public long getContentLength() {
    return -1L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\entity\DeflateDecompressingEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */