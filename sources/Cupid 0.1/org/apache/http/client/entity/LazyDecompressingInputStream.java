package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LazyDecompressingInputStream extends InputStream {
  private final InputStream wrappedStream;
  
  private final DecompressingEntity decompressingEntity;
  
  private InputStream wrapperStream;
  
  public LazyDecompressingInputStream(InputStream wrappedStream, DecompressingEntity decompressingEntity) {
    this.wrappedStream = wrappedStream;
    this.decompressingEntity = decompressingEntity;
  }
  
  private void initWrapper() throws IOException {
    if (this.wrapperStream == null)
      this.wrapperStream = this.decompressingEntity.decorate(this.wrappedStream); 
  }
  
  public int read() throws IOException {
    initWrapper();
    return this.wrapperStream.read();
  }
  
  public int read(byte[] b) throws IOException {
    initWrapper();
    return this.wrapperStream.read(b);
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    initWrapper();
    return this.wrapperStream.read(b, off, len);
  }
  
  public long skip(long n) throws IOException {
    initWrapper();
    return this.wrapperStream.skip(n);
  }
  
  public boolean markSupported() {
    return false;
  }
  
  public int available() throws IOException {
    initWrapper();
    return this.wrapperStream.available();
  }
  
  public void close() throws IOException {
    try {
      if (this.wrapperStream != null)
        this.wrapperStream.close(); 
    } finally {
      this.wrappedStream.close();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\entity\LazyDecompressingInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */