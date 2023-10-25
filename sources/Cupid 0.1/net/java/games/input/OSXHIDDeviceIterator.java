package net.java.games.input;

import java.io.IOException;

final class OSXHIDDeviceIterator {
  private final long iterator_address = nCreateIterator();
  
  private static final native long nCreateIterator();
  
  public final void close() {
    nReleaseIterator(this.iterator_address);
  }
  
  private static final native void nReleaseIterator(long paramLong);
  
  public final OSXHIDDevice next() throws IOException {
    return nNext(this.iterator_address);
  }
  
  private static final native OSXHIDDevice nNext(long paramLong) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\OSXHIDDeviceIterator.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */