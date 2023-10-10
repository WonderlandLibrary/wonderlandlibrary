// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom.jeromq;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.Cancellable;
import org.zeromq.ZMQ;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class JeroMqManager extends AbstractManager
{
    public static final String SYS_PROPERTY_ENABLE_SHUTDOWN_HOOK = "log4j.jeromq.enableShutdownHook";
    public static final String SYS_PROPERTY_IO_THREADS = "log4j.jeromq.ioThreads";
    private static final JeroMqManagerFactory FACTORY;
    private static final ZMQ.Context CONTEXT;
    private static final Cancellable SHUTDOWN_HOOK;
    private final ZMQ.Socket publisher;
    
    private JeroMqManager(final String name, final JeroMqConfiguration config) {
        super(null, name);
        (this.publisher = JeroMqManager.CONTEXT.socket(1)).setAffinity(config.affinity);
        this.publisher.setBacklog(config.backlog);
        this.publisher.setDelayAttachOnConnect(config.delayAttachOnConnect);
        if (config.identity != null) {
            this.publisher.setIdentity(config.identity);
        }
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
        for (final String endpoint : config.endpoints) {
            this.publisher.bind(endpoint);
        }
        JeroMqManager.LOGGER.debug("Created JeroMqManager with {}", config);
    }
    
    public boolean send(final byte[] data) {
        return this.publisher.send(data);
    }
    
    @Override
    protected boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        this.publisher.close();
        return true;
    }
    
    public static JeroMqManager getJeroMqManager(final String name, final long affinity, final long backlog, final boolean delayAttachOnConnect, final byte[] identity, final boolean ipv4Only, final long linger, final long maxMsgSize, final long rcvHwm, final long receiveBufferSize, final int receiveTimeOut, final long reconnectIVL, final long reconnectIVLMax, final long sendBufferSize, final int sendTimeOut, final long sndHwm, final int tcpKeepAlive, final long tcpKeepAliveCount, final long tcpKeepAliveIdle, final long tcpKeepAliveInterval, final boolean xpubVerbose, final List<String> endpoints) {
        return AbstractManager.getManager(name, (ManagerFactory<JeroMqManager, JeroMqConfiguration>)JeroMqManager.FACTORY, new JeroMqConfiguration(affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, (List)endpoints));
    }
    
    public static ZMQ.Context getContext() {
        return JeroMqManager.CONTEXT;
    }
    
    static {
        FACTORY = new JeroMqManagerFactory();
        JeroMqManager.LOGGER.trace("JeroMqManager using ZMQ version {}", ZMQ.getVersionString());
        final int ioThreads = PropertiesUtil.getProperties().getIntegerProperty("log4j.jeromq.ioThreads", 1);
        JeroMqManager.LOGGER.trace("JeroMqManager creating ZMQ context with ioThreads = {}", (Object)ioThreads);
        CONTEXT = ZMQ.context(ioThreads);
        final boolean enableShutdownHook = PropertiesUtil.getProperties().getBooleanProperty("log4j.jeromq.enableShutdownHook", true);
        if (enableShutdownHook) {
            SHUTDOWN_HOOK = ((ShutdownCallbackRegistry)LogManager.getFactory()).addShutdownCallback(JeroMqManager.CONTEXT::close);
        }
        else {
            SHUTDOWN_HOOK = null;
        }
    }
    
    private static class JeroMqConfiguration
    {
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
        
        private JeroMqConfiguration(final long affinity, final long backlog, final boolean delayAttachOnConnect, final byte[] identity, final boolean ipv4Only, final long linger, final long maxMsgSize, final long rcvHwm, final long receiveBufferSize, final int receiveTimeOut, final long reconnectIVL, final long reconnectIVLMax, final long sendBufferSize, final int sendTimeOut, final long sndHwm, final int tcpKeepAlive, final long tcpKeepAliveCount, final long tcpKeepAliveIdle, final long tcpKeepAliveInterval, final boolean xpubVerbose, final List<String> endpoints) {
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
        
        @Override
        public String toString() {
            return "JeroMqConfiguration{affinity=" + this.affinity + ", backlog=" + this.backlog + ", delayAttachOnConnect=" + this.delayAttachOnConnect + ", identity=" + Arrays.toString(this.identity) + ", ipv4Only=" + this.ipv4Only + ", linger=" + this.linger + ", maxMsgSize=" + this.maxMsgSize + ", rcvHwm=" + this.rcvHwm + ", receiveBufferSize=" + this.receiveBufferSize + ", receiveTimeOut=" + this.receiveTimeOut + ", reconnectIVL=" + this.reconnectIVL + ", reconnectIVLMax=" + this.reconnectIVLMax + ", sendBufferSize=" + this.sendBufferSize + ", sendTimeOut=" + this.sendTimeOut + ", sndHwm=" + this.sndHwm + ", tcpKeepAlive=" + this.tcpKeepAlive + ", tcpKeepAliveCount=" + this.tcpKeepAliveCount + ", tcpKeepAliveIdle=" + this.tcpKeepAliveIdle + ", tcpKeepAliveInterval=" + this.tcpKeepAliveInterval + ", xpubVerbose=" + this.xpubVerbose + ", endpoints=" + this.endpoints + '}';
        }
    }
    
    private static class JeroMqManagerFactory implements ManagerFactory<JeroMqManager, JeroMqConfiguration>
    {
        @Override
        public JeroMqManager createManager(final String name, final JeroMqConfiguration data) {
            return new JeroMqManager(name, data, null);
        }
    }
}
