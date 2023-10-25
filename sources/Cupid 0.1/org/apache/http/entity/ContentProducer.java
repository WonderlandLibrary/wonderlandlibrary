package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

public interface ContentProducer {
  void writeTo(OutputStream paramOutputStream) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\entity\ContentProducer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */