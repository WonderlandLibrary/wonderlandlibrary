package org.apache.logging.log4j.status;

import java.io.Closeable;
import java.util.EventListener;
import org.apache.logging.log4j.Level;

public interface StatusListener extends Closeable, EventListener {
  void log(StatusData paramStatusData);
  
  Level getStatusLevel();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\status\StatusListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */