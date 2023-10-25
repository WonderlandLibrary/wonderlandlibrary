package org.apache.commons.compress.compressors.pack200;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class StreamBridge extends FilterOutputStream {
  private InputStream input;
  
  private final Object INPUT_LOCK = new Object();
  
  protected StreamBridge(OutputStream out) {
    super(out);
  }
  
  protected StreamBridge() {
    this(null);
  }
  
  InputStream getInput() throws IOException {
    synchronized (this.INPUT_LOCK) {
      if (this.input == null)
        this.input = getInputView(); 
    } 
    return this.input;
  }
  
  abstract InputStream getInputView() throws IOException;
  
  void stop() throws IOException {
    close();
    synchronized (this.INPUT_LOCK) {
      if (this.input != null) {
        this.input.close();
        this.input = null;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\pack200\StreamBridge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */