package org.apache.commons.compress.archivers.sevenz;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.ARMOptions;
import org.tukaani.xz.ARMThumbOptions;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.IA64Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.PowerPCOptions;
import org.tukaani.xz.SPARCOptions;
import org.tukaani.xz.X86Options;

class Coders {
  private static final Map<SevenZMethod, CoderBase> CODER_MAP = new HashMap<SevenZMethod, CoderBase>() {
      private static final long serialVersionUID = 1664829131806520867L;
    };
  
  static CoderBase findByMethod(SevenZMethod method) {
    return CODER_MAP.get(method);
  }
  
  static InputStream addDecoder(InputStream is, Coder coder, byte[] password) throws IOException {
    CoderBase cb = findByMethod(SevenZMethod.byId(coder.decompressionMethodId));
    if (cb == null)
      throw new IOException("Unsupported compression method " + Arrays.toString(coder.decompressionMethodId)); 
    return cb.decode(is, coder, password);
  }
  
  static OutputStream addEncoder(OutputStream out, SevenZMethod method, Object options) throws IOException {
    CoderBase cb = findByMethod(method);
    if (cb == null)
      throw new IOException("Unsupported compression method " + method); 
    return cb.encode(out, options);
  }
  
  static class CopyDecoder extends CoderBase {
    CopyDecoder() {
      super(new Class[0]);
    }
    
    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
      return in;
    }
    
    OutputStream encode(OutputStream out, Object options) {
      return out;
    }
  }
  
  static class LZMADecoder extends CoderBase {
    LZMADecoder() {
      super(new Class[0]);
    }
    
    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
      byte propsByte = coder.properties[0];
      long dictSize = coder.properties[1];
      for (int i = 1; i < 4; i++)
        dictSize |= (coder.properties[i + 1] & 0xFFL) << 8 * i; 
      if (dictSize > 2147483632L)
        throw new IOException("Dictionary larger than 4GiB maximum size"); 
      return (InputStream)new LZMAInputStream(in, -1L, propsByte, (int)dictSize);
    }
  }
  
  static class BCJDecoder extends CoderBase {
    private final FilterOptions opts;
    
    BCJDecoder(FilterOptions opts) {
      super(new Class[0]);
      this.opts = opts;
    }
    
    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
      try {
        return this.opts.getInputStream(in);
      } catch (AssertionError e) {
        IOException ex = new IOException("BCJ filter needs XZ for Java > 1.4 - see http://commons.apache.org/proper/commons-compress/limitations.html#7Z");
        ex.initCause(e);
        throw ex;
      } 
    }
    
    OutputStream encode(OutputStream out, Object options) {
      FinishableOutputStream fo = this.opts.getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
      return new FilterOutputStream((OutputStream)fo) {
          public void flush() {}
        };
    }
  }
  
  static class DeflateDecoder extends CoderBase {
    DeflateDecoder() {
      super(new Class[] { Number.class });
    }
    
    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
      return new InflaterInputStream(new Coders.DummyByteAddingInputStream(in), new Inflater(true));
    }
    
    OutputStream encode(OutputStream out, Object options) {
      int level = numberOptionOrDefault(options, 9);
      return new DeflaterOutputStream(out, new Deflater(level, true));
    }
  }
  
  static class BZIP2Decoder extends CoderBase {
    BZIP2Decoder() {
      super(new Class[] { Number.class });
    }
    
    InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
      return (InputStream)new BZip2CompressorInputStream(in);
    }
    
    OutputStream encode(OutputStream out, Object options) throws IOException {
      int blockSize = numberOptionOrDefault(options, 9);
      return (OutputStream)new BZip2CompressorOutputStream(out, blockSize);
    }
  }
  
  private static class DummyByteAddingInputStream extends FilterInputStream {
    private boolean addDummyByte = true;
    
    private DummyByteAddingInputStream(InputStream in) {
      super(in);
    }
    
    public int read() throws IOException {
      int result = super.read();
      if (result == -1 && this.addDummyByte) {
        this.addDummyByte = false;
        result = 0;
      } 
      return result;
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
      int result = super.read(b, off, len);
      if (result == -1 && this.addDummyByte) {
        this.addDummyByte = false;
        b[off] = 0;
        return 1;
      } 
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\Coders.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */