package org.apache.logging.log4j.core.layout;

public interface Encoder<T> {
  void encode(T paramT, ByteBufferDestination paramByteBufferDestination);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */