package org.apache.logging.log4j.core.appender.mom.jeromq;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.util.Cancellable;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.zeromq.ZMQ;

public class JeroMqManager extends AbstractManager {
  public static final String SYS_PROPERTY_ENABLE_SHUTDOWN_HOOK = "log4j.jeromq.enableShutdownHook";
  
  public static final String SYS_PROPERTY_IO_THREADS = "log4j.jeromq.ioThreads";
  
  private static final JeroMqManagerFactory FACTORY = new JeroMqManagerFactory();
  
  private static final ZMQ.Context CONTEXT;
  
  private static final Cancellable SHUTDOWN_HOOK;
  
  private final ZMQ.Socket publisher;
  
  static {
    LOGGER.trace("JeroMqManager using ZMQ version {}", ZMQ.getVersionString());
    int ioThreads = PropertiesUtil.getProperties().getIntegerProperty("log4j.jeromq.ioThreads", 1);
    LOGGER.trace("JeroMqManager creating ZMQ context with ioThreads = {}", Integer.valueOf(ioThreads));
    CONTEXT = ZMQ.context(ioThreads);
    boolean enableShutdownHook = PropertiesUtil.getProperties().getBooleanProperty("log4j.jeromq.enableShutdownHook", true);
    if (enableShutdownHook) {
      SHUTDOWN_HOOK = ((ShutdownCallbackRegistry)LogManager.getFactory()).addShutdownCallback(CONTEXT::close);
    } else {
      SHUTDOWN_HOOK = null;
    } 
  }
  
  private JeroMqManager(String name, JeroMqConfiguration config) {
    super(null, name);
    this.publisher = CONTEXT.socket(1);
    this.publisher.setAffinity(config.affinity);
    this.publisher.setBacklog(config.backlog);
    this.publisher.setDelayAttachOnConnect(config.delayAttachOnConnect);
    if (config.identity != null)
      this.publisher.setIdentity(config.identity); 
    this.publisher.setIPv4Only(config.ipv4Only);
    this.publisher.setLinger(config.linger);
    this.publisher.setMaxMsgSize(config.maxMsgSize);
    this.publisher.setRcvHWM(config.rcvHwm);
    this.publisher.setReceiveBufferSize(config.receiveBufferSize);
    this.publisher.setReceiveTimeOut(config.receiveTimeOut);
    this.publisher.setReconnectIVL(config.reconnectIVL);
    this.publisher.setReconnectIVLMax(config.reconnectIVLMax);
    this.publisher.setSendBufferSize(config.sendBufferSize);
    this.publisher.setSendTimeOut(config.sendTimeOut);
    this.publisher.setSndHWM(config.sndHwm);
    this.publisher.setTCPKeepAlive(config.tcpKeepAlive);
    this.publisher.setTCPKeepAliveCount(config.tcpKeepAliveCount);
    this.publisher.setTCPKeepAliveIdle(config.tcpKeepAliveIdle);
    this.publisher.setTCPKeepAliveInterval(config.tcpKeepAliveInterval);
    this.publisher.setXpubVerbose(config.xpubVerbose);
    for (String endpoint : config.endpoints)
      this.publisher.bind(endpoint); 
    LOGGER.debug("Created JeroMqManager with {}", config);
  }
  
  public boolean send(byte[] data) {
    return this.publisher.send(data);
  }
  
  protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
    this.publisher.close();
    return true;
  }
  
  public static JeroMqManager getJeroMqManager(String name, long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
    return (JeroMqManager)getManager(name, FACTORY, new JeroMqConfiguration(affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, endpoints));
  }
  
  public static ZMQ.Context getContext() {
    return CONTEXT;
  }
  
  private static class JeroMqConfiguration {
    private final long affinity;
    
    private final long backlog;
    
    private final boolean delayAttachOnConnect;
    
    private final byte[] identity;
    
    private final boolean ipv4Only;
    
    private final long linger;
    
    private final long maxMsgSize;
    
    private final long rcvHwm;
    
    private final long receiveBufferSize;
    
    private final int receiveTimeOut;
    
    private final long reconnectIVL;
    
    private final long reconnectIVLMax;
    
    private final long sendBufferSize;
    
    private final int sendTimeOut;
    
    private final long sndHwm;
    
    private final int tcpKeepAlive;
    
    private final long tcpKeepAliveCount;
    
    private final long tcpKeepAliveIdle;
    
    private final long tcpKeepAliveInterval;
    
    private final boolean xpubVerbose;
    
    private final List<String> endpoints;
    
    private JeroMqConfiguration(long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
      this.affinity = affinity;
      this.backlog = backlog;
      this.delayAttachOnConnect = delayAttachOnConnect;
      this.identity = identity;
      this.ipv4Only = ipv4Only;
      this.linger = linger;
      this.maxMsgSize = maxMsgSize;
      this.rcvHwm = rcvHwm;
      this.receiveBufferSize = receiveBufferSize;
      this.receiveTimeOut = receiveTimeOut;
      this.reconnectIVL = reconnectIVL;
      this.reconnectIVLMax = reconnectIVLMax;
      this.sendBufferSize = sendBufferSize;
      this.sendTimeOut = sendTimeOut;
      this.sndHwm = sndHwm;
      this.tcpKeepAlive = tcpKeepAlive;
      this.tcpKeepAliveCount = tcpKeepAliveCount;
      this.tcpKeepAliveIdle = tcpKeepAliveIdle;
      this.tcpKeepAliveInterval = tcpKeepAliveInterval;
      this.xpubVerbose = xpubVerbose;
      this.endpoints = endpoints;
    }
    
    public String toString() {
      return "JeroMqConfiguration{affinity=" + this.affinity + ", backlog=" + this.backlog + ", delayAttachOnConnect=" + this.delayAttachOnConnect + ", identity=" + 
        
        Arrays.toString(this.identity) + ", ipv4Only=" + this.ipv4Only + ", linger=" + this.linger + ", maxMsgSize=" + this.maxMsgSize + ", rcvHwm=" + this.rcvHwm + ", receiveBufferSize=" + this.receiveBufferSize + ", receiveTimeOut=" + this.receiveTimeOut + ", reconnectIVL=" + this.reconnectIVL + ", reconnectIVLMax=" + this.reconnectIVLMax + ", sendBufferSize=" + this.sendBufferSize + ", sendTimeOut=" + this.sendTimeOut + ", sndHwm=" + this.sndHwm + ", tcpKeepAlive=" + this.tcpKeepAlive + ", tcpKeepAliveCount=" + this.tcpKeepAliveCount + ", tcpKeepAliveIdle=" + this.tcpKeepAliveIdle + ", tcpKeepAliveInterval=" + this.tcpKeepAliveInterval + ", xpubVerbose=" + this.xpubVerbose + ", endpoints=" + this.endpoints + '}';
    }
  }
  
  private static class JeroMqManagerFactory implements ManagerFactory<JeroMqManager, JeroMqConfiguration> {
    private JeroMqManagerFactory() {}
    
    public JeroMqManager createManager(String name, JeroMqManager.JeroMqConfiguration data) {
      return new JeroMqManager(name, data);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\mom\jeromq\JeroMqManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */