package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@NotThreadSafe
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo {
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  private OutputStream outstream;
  
  private ByteArrayBuffer buffer;
  
  private Charset charset;
  
  private boolean ascii;
  
  private int minChunkLimit;
  
  private HttpTransportMetricsImpl metrics;
  
  private CodingErrorAction onMalformedCharAction;
  
  private CodingErrorAction onUnmappableCharAction;
  
  private CharsetEncoder encoder;
  
  private ByteBuffer bbuf;
  
  protected AbstractSessionOutputBuffer(OutputStream outstream, int buffersize, Charset charset, int minChunkLimit, CodingErrorAction malformedCharAction, CodingErrorAction unmappableCharAction) {
    Args.notNull(outstream, "Input stream");
    Args.notNegative(buffersize, "Buffer size");
    this.outstream = outstream;
    this.buffer = new ByteArrayBuffer(buffersize);
    this.charset = (charset != null) ? charset : Consts.ASCII;
    this.ascii = this.charset.equals(Consts.ASCII);
    this.encoder = null;
    this.minChunkLimit = (minChunkLimit >= 0) ? minChunkLimit : 512;
    this.metrics = createTransportMetrics();
    this.onMalformedCharAction = (malformedCharAction != null) ? malformedCharAction : CodingErrorAction.REPORT;
    this.onUnmappableCharAction = (unmappableCharAction != null) ? unmappableCharAction : CodingErrorAction.REPORT;
  }
  
  public AbstractSessionOutputBuffer() {}
  
  protected void init(OutputStream outstream, int buffersize, HttpParams params) {
    Args.notNull(outstream, "Input stream");
    Args.notNegative(buffersize, "Buffer size");
    Args.notNull(params, "HTTP parameters");
    this.outstream = outstream;
    this.buffer = new ByteArrayBuffer(buffersize);
    String charset = (String)params.getParameter("http.protocol.element-charset");
    this.charset = (charset != null) ? Charset.forName(charset) : Consts.ASCII;
    this.ascii = this.charset.equals(Consts.ASCII);
    this.encoder = null;
    this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", 512);
    this.metrics = createTransportMetrics();
    CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
    this.onMalformedCharAction = (a1 != null) ? a1 : CodingErrorAction.REPORT;
    CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
    this.onUnmappableCharAction = (a2 != null) ? a2 : CodingErrorAction.REPORT;
  }
  
  protected HttpTransportMetricsImpl createTransportMetrics() {
    return new HttpTransportMetricsImpl();
  }
  
  public int capacity() {
    return this.buffer.capacity();
  }
  
  public int length() {
    return this.buffer.length();
  }
  
  public int available() {
    return capacity() - length();
  }
  
  protected void flushBuffer() throws IOException {
    int len = this.buffer.length();
    if (len > 0) {
      this.outstream.write(this.buffer.buffer(), 0, len);
      this.buffer.clear();
      this.metrics.incrementBytesTransferred(len);
    } 
  }
  
  public void flush() throws IOException {
    flushBuffer();
    this.outstream.flush();
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    if (b == null)
      return; 
    if (len > this.minChunkLimit || len > this.buffer.capacity()) {
      flushBuffer();
      this.outstream.write(b, off, len);
      this.metrics.incrementBytesTransferred(len);
    } else {
      int freecapacity = this.buffer.capacity() - this.buffer.length();
      if (len > freecapacity)
        flushBuffer(); 
      this.buffer.append(b, off, len);
    } 
  }
  
  public void write(byte[] b) throws IOException {
    if (b == null)
      return; 
    write(b, 0, b.length);
  }
  
  public void write(int b) throws IOException {
    if (this.buffer.isFull())
      flushBuffer(); 
    this.buffer.append(b);
  }
  
  public void writeLine(String s) throws IOException {
    if (s == null)
      return; 
    if (s.length() > 0)
      if (this.ascii) {
        for (int i = 0; i < s.length(); i++)
          write(s.charAt(i)); 
      } else {
        CharBuffer cbuf = CharBuffer.wrap(s);
        writeEncoded(cbuf);
      }  
    write(CRLF);
  }
  
  public void writeLine(CharArrayBuffer charbuffer) throws IOException {
    if (charbuffer == null)
      return; 
    if (this.ascii) {
      int off = 0;
      int remaining = charbuffer.length();
      while (remaining > 0) {
        int chunk = this.buffer.capacity() - this.buffer.length();
        chunk = Math.min(chunk, remaining);
        if (chunk > 0)
          this.buffer.append(charbuffer, off, chunk); 
        if (this.buffer.isFull())
          flushBuffer(); 
        off += chunk;
        remaining -= chunk;
      } 
    } else {
      CharBuffer cbuf = CharBuffer.wrap(charbuffer.buffer(), 0, charbuffer.length());
      writeEncoded(cbuf);
    } 
    write(CRLF);
  }
  
  private void writeEncoded(CharBuffer cbuf) throws IOException {
    if (!cbuf.hasRemaining())
      return; 
    if (this.encoder == null) {
      this.encoder = this.charset.newEncoder();
      this.encoder.onMalformedInput(this.onMalformedCharAction);
      this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
    } 
    if (this.bbuf == null)
      this.bbuf = ByteBuffer.allocate(1024); 
    this.encoder.reset();
    while (cbuf.hasRemaining()) {
      CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
      handleEncodingResult(coderResult);
    } 
    CoderResult result = this.encoder.flush(this.bbuf);
    handleEncodingResult(result);
    this.bbuf.clear();
  }
  
  private void handleEncodingResult(CoderResult result) throws IOException {
    if (result.isError())
      result.throwException(); 
    this.bbuf.flip();
    while (this.bbuf.hasRemaining())
      write(this.bbuf.get()); 
    this.bbuf.compact();
  }
  
  public HttpTransportMetrics getMetrics() {
    return this.metrics;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\AbstractSessionOutputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */