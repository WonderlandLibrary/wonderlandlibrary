package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import org.apache.logging.log4j.core.util.Constants;

public abstract class AbstractOutputStreamAppender<M extends OutputStreamManager> extends AbstractAppender {
  private final boolean immediateFlush;
  
  private final M manager;
  
  public static abstract class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> {
    @PluginBuilderAttribute
    private boolean bufferedIo = true;
    
    @PluginBuilderAttribute
    private int bufferSize = Constants.ENCODER_BYTE_BUFFER_SIZE;
    
    @PluginBuilderAttribute
    private boolean immediateFlush = true;
    
    public int getBufferSize() {
      return this.bufferSize;
    }
    
    public boolean isBufferedIo() {
      return this.bufferedIo;
    }
    
    public boolean isImmediateFlush() {
      return this.immediateFlush;
    }
    
    public B withImmediateFlush(boolean immediateFlush) {
      this.immediateFlush = immediateFlush;
      return (B)asBuilder();
    }
    
    public B withBufferedIo(boolean bufferedIo) {
      this.bufferedIo = bufferedIo;
      return (B)asBuilder();
    }
    
    public B withBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
      return (B)asBuilder();
    }
  }
  
  @Deprecated
  protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
    super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
    this.manager = manager;
    this.immediateFlush = immediateFlush;
  }
  
  protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, Property[] properties, M manager) {
    super(name, filter, layout, ignoreExceptions, properties);
    this.manager = manager;
    this.immediateFlush = immediateFlush;
  }
  
  public boolean getImmediateFlush() {
    return this.immediateFlush;
  }
  
  public M getManager() {
    return this.manager;
  }
  
  public void start() {
    if (getLayout() == null)
      LOGGER.error("No layout set for the appender named [" + getName() + "]."); 
    if (this.manager == null)
      LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "]."); 
    super.start();
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    return stop(timeout, timeUnit, true);
  }
  
  protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
    boolean stopped = super.stop(timeout, timeUnit, changeLifeCycleState);
    stopped &= this.manager.stop(timeout, timeUnit);
    if (changeLifeCycleState)
      setStopped(); 
    LOGGER.debug("Appender {} stopped with status {}", getName(), Boolean.valueOf(stopped));
    return stopped;
  }
  
  public void append(LogEvent event) {
    try {
      tryAppend(event);
    } catch (AppenderLoggingException ex) {
      error("Unable to write to stream " + this.manager.getName() + " for appender " + getName(), event, (Throwable)ex);
      throw ex;
    } 
  }
  
  private void tryAppend(LogEvent event) {
    if (Constants.ENABLE_DIRECT_ENCODERS) {
      directEncodeEvent(event);
    } else {
      writeByteArrayToManager(event);
    } 
  }
  
  protected void directEncodeEvent(LogEvent event) {
    getLayout().encode(event, (ByteBufferDestination)this.manager);
    if (this.immediateFlush || event.isEndOfBatch())
      this.manager.flush(); 
  }
  
  protected void writeByteArrayToManager(LogEvent event) {
    byte[] bytes = getLayout().toByteArray(event);
    if (bytes != null && bytes.length > 0)
      this.manager.write(bytes, (this.immediateFlush || event.isEndOfBatch())); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\AbstractOutputStreamAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */