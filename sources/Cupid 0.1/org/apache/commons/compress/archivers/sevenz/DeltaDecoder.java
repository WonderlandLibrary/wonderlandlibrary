package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.tukaani.xz.DeltaOptions;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.UnsupportedOptionsException;

class DeltaDecoder extends CoderBase {
  DeltaDecoder() {
    super(new Class[] { Number.class });
  }
  
  InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
    return (new DeltaOptions(getOptionsFromCoder(coder))).getInputStream(in);
  }
  
  OutputStream encode(OutputStream out, Object options) throws IOException {
    int distance = numberOptionOrDefault(options, 1);
    try {
      return (OutputStream)(new DeltaOptions(distance)).getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
    } catch (UnsupportedOptionsException ex) {
      throw new IOException(ex.getMessage());
    } 
  }
  
  byte[] getOptionsAsProperties(Object options) {
    return new byte[] { (byte)(numberOptionOrDefault(options, 1) - 1) };
  }
  
  Object getOptionsFromCoder(Coder coder, InputStream in) {
    return Integer.valueOf(getOptionsFromCoder(coder));
  }
  
  private int getOptionsFromCoder(Coder coder) {
    if (coder.properties == null || coder.properties.length == 0)
      return 1; 
    return (0xFF & coder.properties[0]) + 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\DeltaDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */