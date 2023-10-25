package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {
  private final InheritableThreadLocal<InputStream> m_streams = new InheritableThreadLocal<InputStream>();
  
  public InputStream bindStream(InputStream input) {
    InputStream oldValue = this.m_streams.get();
    this.m_streams.set(input);
    return oldValue;
  }
  
  public void close() throws IOException {
    InputStream input = this.m_streams.get();
    if (null != input)
      input.close(); 
  }
  
  public int read() throws IOException {
    InputStream input = this.m_streams.get();
    if (null != input)
      return input.read(); 
    return -1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\DemuxInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */