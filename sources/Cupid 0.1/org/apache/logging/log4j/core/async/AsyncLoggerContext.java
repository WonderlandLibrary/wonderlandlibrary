package org.apache.logging.log4j.core.async;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class AsyncLoggerContext extends LoggerContext {
  private final AsyncLoggerDisruptor loggerDisruptor;
  
  public AsyncLoggerContext(String name) {
    super(name);
    this.loggerDisruptor = new AsyncLoggerDisruptor(name);
  }
  
  public AsyncLoggerContext(String name, Object externalContext) {
    super(name, externalContext);
    this.loggerDisruptor = new AsyncLoggerDisruptor(name);
  }
  
  public AsyncLoggerContext(String name, Object externalContext, URI configLocn) {
    super(name, externalContext, configLocn);
    this.loggerDisruptor = new AsyncLoggerDisruptor(name);
  }
  
  public AsyncLoggerContext(String name, Object externalContext, String configLocn) {
    super(name, externalContext, configLocn);
    this.loggerDisruptor = new AsyncLoggerDisruptor(name);
  }
  
  protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
    return new AsyncLogger(ctx, name, messageFactory, this.loggerDisruptor);
  }
  
  public void setName(String name) {
    super.setName("AsyncContext[" + name + "]");
    this.loggerDisruptor.setContextName(name);
  }
  
  public void start() {
    this.loggerDisruptor.start();
    super.start();
  }
  
  public void start(Configuration config) {
    maybeStartHelper(config);
    super.start(config);
  }
  
  private void maybeStartHelper(Configuration config) {
    if (config instanceof org.apache.logging.log4j.core.config.DefaultConfiguration) {
      StatusLogger.getLogger().debug("[{}] Not starting Disruptor for DefaultConfiguration.", getName());
    } else {
      this.loggerDisruptor.start();
    } 
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    this.loggerDisruptor.stop(timeout, timeUnit);
    super.stop(timeout, timeUnit);
    return true;
  }
  
  public RingBufferAdmin createRingBufferAdmin() {
    return this.loggerDisruptor.createRingBufferAdmin(getName());
  }
  
  public void setUseThreadLocals(boolean useThreadLocals) {
    this.loggerDisruptor.setUseThreadLocals(useThreadLocals);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncLoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */