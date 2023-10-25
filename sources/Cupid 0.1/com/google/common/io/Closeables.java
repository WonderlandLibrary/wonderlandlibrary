package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Closeables {
  @VisibleForTesting
  static final Logger logger = Logger.getLogger(Closeables.class.getName());
  
  public static void close(@Nullable Closeable closeable, boolean swallowIOException) throws IOException {
    if (closeable == null)
      return; 
    try {
      closeable.close();
    } catch (IOException e) {
      if (swallowIOException) {
        logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
      } else {
        throw e;
      } 
    } 
  }
  
  public static void closeQuietly(@Nullable InputStream inputStream) {
    try {
      close(inputStream, true);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    } 
  }
  
  public static void closeQuietly(@Nullable Reader reader) {
    try {
      close(reader, true);
    } catch (IOException impossible) {
      throw new AssertionError(impossible);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\Closeables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */