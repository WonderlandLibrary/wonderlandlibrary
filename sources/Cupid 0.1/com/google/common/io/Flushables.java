package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public final class Flushables {
  private static final Logger logger = Logger.getLogger(Flushables.class.getName());
  
  public static void flush(Flushable flushable, boolean swallowIOException) throws IOException {
    try {
      flushable.flush();
    } catch (IOException e) {
      if (swallowIOException) {
        logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", e);
      } else {
        throw e;
      } 
    } 
  }
  
  public static void flushQuietly(Flushable flushable) {
    try {
      flush(flushable, true);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\Flushables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */