package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@Immutable
public class LoggingSessionOutputBuffer implements SessionOutputBuffer {
  private final SessionOutputBuffer out;
  
  private final Wire wire;
  
  private final String charset;
  
  public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
    this.out = out;
    this.wire = wire;
    this.charset = (charset != null) ? charset : Consts.ASCII.name();
  }
  
  public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
    this(out, wire, null);
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    this.out.write(b, off, len);
    if (this.wire.enabled())
      this.wire.output(b, off, len); 
  }
  
  public void write(int b) throws IOException {
    this.out.write(b);
    if (this.wire.enabled())
      this.wire.output(b); 
  }
  
  public void write(byte[] b) throws IOException {
    this.out.write(b);
    if (this.wire.enabled())
      this.wire.output(b); 
  }
  
  public void flush() throws IOException {
    this.out.flush();
  }
  
  public void writeLine(CharArrayBuffer buffer) throws IOException {
    this.out.writeLine(buffer);
    if (this.wire.enabled()) {
      String s = new String(buffer.buffer(), 0, buffer.length());
      String tmp = s + "\r\n";
      this.wire.output(tmp.getBytes(this.charset));
    } 
  }
  
  public void writeLine(String s) throws IOException {
    this.out.writeLine(s);
    if (this.wire.enabled()) {
      String tmp = s + "\r\n";
      this.wire.output(tmp.getBytes(this.charset));
    } 
  }
  
  public HttpTransportMetrics getMetrics() {
    return this.out.getMetrics();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\LoggingSessionOutputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */