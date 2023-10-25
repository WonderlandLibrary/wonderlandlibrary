package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.StringLayout;

public class WriterManager extends AbstractManager {
  protected final StringLayout layout;
  
  private volatile Writer writer;
  
  public static <T> WriterManager getManager(String name, T data, ManagerFactory<? extends WriterManager, T> factory) {
    return AbstractManager.<WriterManager, T>getManager(name, (ManagerFactory)factory, data);
  }
  
  public WriterManager(Writer writer, String streamName, StringLayout layout, boolean writeHeader) {
    super(null, streamName);
    this.writer = writer;
    this.layout = layout;
    if (writeHeader && layout != null) {
      byte[] header = layout.getHeader();
      if (header != null)
        try {
          this.writer.write(new String(header, layout.getCharset()));
        } catch (IOException e) {
          logError("Unable to write header", e);
        }  
    } 
  }
  
  protected synchronized void closeWriter() {
    Writer w = this.writer;
    try {
      w.close();
    } catch (IOException ex) {
      logError("Unable to close stream", ex);
    } 
  }
  
  public synchronized void flush() {
    try {
      this.writer.flush();
    } catch (IOException ex) {
      String msg = "Error flushing stream " + getName();
      throw new AppenderLoggingException(msg, ex);
    } 
  }
  
  protected Writer getWriter() {
    return this.writer;
  }
  
  public boolean isOpen() {
    return (getCount() > 0);
  }
  
  public boolean releaseSub(long timeout, TimeUnit timeUnit) {
    writeFooter();
    closeWriter();
    return true;
  }
  
  protected void setWriter(Writer writer) {
    byte[] header = this.layout.getHeader();
    if (header != null) {
      try {
        writer.write(new String(header, this.layout.getCharset()));
        this.writer = writer;
      } catch (IOException ioe) {
        logError("Unable to write header", ioe);
      } 
    } else {
      this.writer = writer;
    } 
  }
  
  protected synchronized void write(String str) {
    try {
      this.writer.write(str);
    } catch (IOException ex) {
      String msg = "Error writing to stream " + getName();
      throw new AppenderLoggingException(msg, ex);
    } 
  }
  
  protected void writeFooter() {
    if (this.layout == null)
      return; 
    byte[] footer = this.layout.getFooter();
    if (footer != null && footer.length > 0)
      write(new String(footer, this.layout.getCharset())); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\WriterManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */