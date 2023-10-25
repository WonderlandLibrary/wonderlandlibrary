package org.apache.logging.log4j.core.layout;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.status.StatusLogger;

public class LockingStringBuilderEncoder implements Encoder<StringBuilder> {
  private final Charset charset;
  
  private final CharsetEncoder charsetEncoder;
  
  private final CharBuffer cachedCharBuffer;
  
  public LockingStringBuilderEncoder(Charset charset) {
    this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE);
  }
  
  public LockingStringBuilderEncoder(Charset charset, int charBufferSize) {
    this.charset = Objects.<Charset>requireNonNull(charset, "charset");
    this
      .charsetEncoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    this.cachedCharBuffer = CharBuffer.wrap(new char[charBufferSize]);
  }
  
  private CharBuffer getCharBuffer() {
    return this.cachedCharBuffer;
  }
  
  public void encode(StringBuilder source, ByteBufferDestination destination) {
    try {
      synchronized (destination) {
        TextEncoderHelper.encodeText(this.charsetEncoder, this.cachedCharBuffer, destination.getByteBuffer(), source, destination);
      } 
    } catch (Exception ex) {
      logEncodeTextException(ex, source, destination);
      TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
    } 
  }
  
  private void logEncodeTextException(Exception ex, StringBuilder text, ByteBufferDestination destination) {
    StatusLogger.getLogger().error("Recovering from LockingStringBuilderEncoder.encode('{}') error", text, ex);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\LockingStringBuilderEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */