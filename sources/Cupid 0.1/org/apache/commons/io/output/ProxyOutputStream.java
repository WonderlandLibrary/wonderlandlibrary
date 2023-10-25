package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream extends FilterOutputStream {
  public ProxyOutputStream(OutputStream proxy) {
    super(proxy);
  }
  
  public void write(int idx) throws IOException {
    try {
      beforeWrite(1);
      this.out.write(idx);
      afterWrite(1);
    } catch (IOException e) {
      handleIOException(e);
    } 
  }
  
  public void write(byte[] bts) throws IOException {
    try {
      int len = (bts != null) ? bts.length : 0;
      beforeWrite(len);
      this.out.write(bts);
      afterWrite(len);
    } catch (IOException e) {
      handleIOException(e);
    } 
  }
  
  public void write(byte[] bts, int st, int end) throws IOException {
    try {
      beforeWrite(end);
      this.out.write(bts, st, end);
      afterWrite(end);
    } catch (IOException e) {
      handleIOException(e);
    } 
  }
  
  public void flush() throws IOException {
    try {
      this.out.flush();
    } catch (IOException e) {
      handleIOException(e);
    } 
  }
  
  public void close() throws IOException {
    try {
      this.out.close();
    } catch (IOException e) {
      handleIOException(e);
    } 
  }
  
  protected void beforeWrite(int n) throws IOException {}
  
  protected void afterWrite(int n) throws IOException {}
  
  protected void handleIOException(IOException e) throws IOException {
    throw e;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\output\ProxyOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */