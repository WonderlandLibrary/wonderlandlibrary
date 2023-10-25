package com.ibm.icu.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class ICUBinaryStream extends DataInputStream {
  public ICUBinaryStream(InputStream stream, int size) {
    super(stream);
    mark(size);
  }
  
  public ICUBinaryStream(byte[] raw) {
    this(new ByteArrayInputStream(raw), raw.length);
  }
  
  public void seek(int offset) throws IOException {
    reset();
    int actual = skipBytes(offset);
    if (actual != offset)
      throw new IllegalStateException("Skip(" + offset + ") only skipped " + actual + " bytes"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUBinaryStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */