package org.apache.logging.log4j.core.jmx;

import com.lmax.disruptor.RingBuffer;
import javax.management.ObjectName;

public class RingBufferAdmin implements RingBufferAdminMBean {
  private final RingBuffer<?> ringBuffer;
  
  private final ObjectName objectName;
  
  public static RingBufferAdmin forAsyncLogger(RingBuffer<?> ringBuffer, String contextName) {
    String ctxName = Server.escape(contextName);
    String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { ctxName });
    return new RingBufferAdmin(ringBuffer, name);
  }
  
  public static RingBufferAdmin forAsyncLoggerConfig(RingBuffer<?> ringBuffer, String contextName, String configName) {
    String ctxName = Server.escape(contextName);
    String cfgName = Server.escape(configName);
    String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { ctxName, cfgName });
    return new RingBufferAdmin(ringBuffer, name);
  }
  
  protected RingBufferAdmin(RingBuffer<?> ringBuffer, String mbeanName) {
    this.ringBuffer = ringBuffer;
    try {
      this.objectName = new ObjectName(mbeanName);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public long getBufferSize() {
    return (this.ringBuffer == null) ? 0L : this.ringBuffer.getBufferSize();
  }
  
  public long getRemainingCapacity() {
    return (this.ringBuffer == null) ? 0L : this.ringBuffer.remainingCapacity();
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\RingBufferAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */