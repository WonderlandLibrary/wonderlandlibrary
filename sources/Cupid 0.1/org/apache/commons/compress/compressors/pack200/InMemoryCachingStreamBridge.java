package org.apache.commons.compress.compressors.pack200;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class InMemoryCachingStreamBridge extends StreamBridge {
  InMemoryCachingStreamBridge() {
    super(new ByteArrayOutputStream());
  }
  
  InputStream getInputView() throws IOException {
    return new ByteArrayInputStream(((ByteArrayOutputStream)this.out).toByteArray());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\pack200\InMemoryCachingStreamBridge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */