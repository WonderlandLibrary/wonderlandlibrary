package org.apache.logging.log4j.core.appender.nosql;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractNoSqlConnection<W, T extends NoSqlObject<W>> implements NoSqlConnection<W, T> {
  private final AtomicBoolean closed = new AtomicBoolean(false);
  
  public void close() {
    if (this.closed.compareAndSet(false, true))
      closeImpl(); 
  }
  
  protected abstract void closeImpl();
  
  public boolean isClosed() {
    return this.closed.get();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\nosql\AbstractNoSqlConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */