package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMA2Options;

class LZMA2Decoder extends CoderBase {
  LZMA2Decoder() {
    super(new Class[] { LZMA2Options.class, Number.class });
  }
  
  InputStream decode(InputStream in, Coder coder, byte[] password) throws IOException {
    try {
      int dictionarySize = getDictionarySize(coder);
      return (InputStream)new LZMA2InputStream(in, dictionarySize);
    } catch (IllegalArgumentException ex) {
      throw new IOException(ex.getMessage());
    } 
  }
  
  OutputStream encode(OutputStream out, Object opts) throws IOException {
    LZMA2Options options = getOptions(opts);
    FinishableWrapperOutputStream finishableWrapperOutputStream = new FinishableWrapperOutputStream(out);
    return (OutputStream)options.getOutputStream((FinishableOutputStream)finishableWrapperOutputStream);
  }
  
  byte[] getOptionsAsProperties(Object opts) {
    int dictSize = getDictSize(opts);
    int lead = Integer.numberOfLeadingZeros(dictSize);
    int secondBit = (dictSize >>> 30 - lead) - 2;
    return new byte[] { (byte)((19 - lead) * 2 + secondBit) };
  }
  
  Object getOptionsFromCoder(Coder coder, InputStream in) {
    return Integer.valueOf(getDictionarySize(coder));
  }
  
  private int getDictSize(Object opts) {
    if (opts instanceof LZMA2Options)
      return ((LZMA2Options)opts).getDictSize(); 
    return numberOptionOrDefault(opts);
  }
  
  private int getDictionarySize(Coder coder) throws IllegalArgumentException {
    int dictionarySizeBits = 0xFF & coder.properties[0];
    if ((dictionarySizeBits & 0xFFFFFFC0) != 0)
      throw new IllegalArgumentException("Unsupported LZMA2 property bits"); 
    if (dictionarySizeBits > 40)
      throw new IllegalArgumentException("Dictionary larger than 4GiB maximum size"); 
    if (dictionarySizeBits == 40)
      return -1; 
    return (0x2 | dictionarySizeBits & 0x1) << dictionarySizeBits / 2 + 11;
  }
  
  private LZMA2Options getOptions(Object opts) throws IOException {
    if (opts instanceof LZMA2Options)
      return (LZMA2Options)opts; 
    LZMA2Options options = new LZMA2Options();
    options.setDictSize(numberOptionOrDefault(opts));
    return options;
  }
  
  private int numberOptionOrDefault(Object opts) {
    return numberOptionOrDefault(opts, 8388608);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\LZMA2Decoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */