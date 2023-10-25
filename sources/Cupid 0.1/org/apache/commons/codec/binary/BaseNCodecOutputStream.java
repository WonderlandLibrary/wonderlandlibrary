package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BaseNCodecOutputStream extends FilterOutputStream {
  private final boolean doEncode;
  
  private final BaseNCodec baseNCodec;
  
  private final byte[] singleByte = new byte[1];
  
  private final BaseNCodec.Context context = new BaseNCodec.Context();
  
  public BaseNCodecOutputStream(OutputStream out, BaseNCodec basedCodec, boolean doEncode) {
    super(out);
    this.baseNCodec = basedCodec;
    this.doEncode = doEncode;
  }
  
  public void write(int i) throws IOException {
    this.singleByte[0] = (byte)i;
    write(this.singleByte, 0, 1);
  }
  
  public void write(byte[] b, int offset, int len) throws IOException {
    if (b == null)
      throw new NullPointerException(); 
    if (offset < 0 || len < 0)
      throw new IndexOutOfBoundsException(); 
    if (offset > b.length || offset + len > b.length)
      throw new IndexOutOfBoundsException(); 
    if (len > 0) {
      if (this.doEncode) {
        this.baseNCodec.encode(b, offset, len, this.context);
      } else {
        this.baseNCodec.decode(b, offset, len, this.context);
      } 
      flush(false);
    } 
  }
  
  private void flush(boolean propagate) throws IOException {
    int avail = this.baseNCodec.available(this.context);
    if (avail > 0) {
      byte[] buf = new byte[avail];
      int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
      if (c > 0)
        this.out.write(buf, 0, c); 
    } 
    if (propagate)
      this.out.flush(); 
  }
  
  public void flush() throws IOException {
    flush(true);
  }
  
  public void close() throws IOException {
    if (this.doEncode) {
      this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
    } else {
      this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
    } 
    flush();
    this.out.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\binary\BaseNCodecOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */