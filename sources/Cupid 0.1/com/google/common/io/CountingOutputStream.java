package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nullable;

@Beta
public final class CountingOutputStream extends FilterOutputStream {
  private long count;
  
  public CountingOutputStream(@Nullable OutputStream out) {
    super(out);
  }
  
  public long getCount() {
    return this.count;
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    this.out.write(b, off, len);
    this.count += len;
  }
  
  public void write(int b) throws IOException {
    this.out.write(b);
    this.count++;
  }
  
  public void close() throws IOException {
    this.out.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\CountingOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */