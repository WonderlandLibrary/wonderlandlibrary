// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom.jeromq;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.util.Strings;
import java.util.ArrayList;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.Property;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "JeroMQ", category = "Core", elementType = "appender", printObject = true)
public final class JeroMqAppender extends AbstractAppender
{
    private static final int DEFAULT_BACKLOG = 100;
    private static final int DEFAULT_IVL = 100;
    private static final int DEFAULT_RCV_HWM = 1000;
    private static final int DEFAULT_SND_HWM = 1000;
    private final JeroMqManager manager;
    private final List<String> endpoints;
    private int sendRcFalse;
    private int sendRcTrue;
    
    private JeroMqAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final List<String> endpoints, final long affinity, final long backlog, final boolean delayAttachOnConnect, final byte[] identity, final boolean ipv4Only, final long linger, final long maxMsgSize, final long rcvHwm, final long receiveBufferSize, final int receiveTimeOut, final long reconnectIVL, final long reconnectIVLMax, final long sendBufferSize, final int sendTimeOut, final long sndHWM, final int tcpKeepAlive, final long tcpKeepAliveCount, final long tcpKeepAliveIdle, final long tcpKeepAliveInterval, final boolean xpubVerbose, final Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = JeroMqManager.getJeroMqManager(name, affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHWM, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, endpoints);
        this.endpoints = endpoints;
    }
    
    @PluginFactory
    public static JeroMqAppender createAppender(@Required(message = "No name provided for JeroMqAppender") @PluginAttribute("name") final String name, @PluginElement("Layout") Layout<?> layout, @PluginElement("Filter") final Filter filter, @PluginElement("Properties") final Property[] properties, @PluginAttribute("ignoreExceptions") final boolean ignoreExceptions, @PluginAttribute(value = "affinity", defaultLong = 0L) final long affinity, @PluginAttribute(value = "backlog", defaultLong = 100L) final long backlog, @PluginAttribute("delayAttachOnConnect") final boolean delayAttachOnConnect, @PluginAttribute("identity") final byte[] identity, @PluginAttribute(value = "ipv4Only", defaultBoolean = true) final boolean ipv4Only, @PluginAttribute(value = "linger", defaultLong = -1L) final long linger, @PluginAttribute(value = "maxMsgSize", defaultLong = -1L) final long maxMsgSize, @PluginAttribute(value = "rcvHwm", defaultLong = 1000L) final long rcvHwm, @PluginAttribute(value = "receiveBufferSize", defaultLong = 0L) final long receiveBufferSize, @PluginAttribute(value = "receiveTimeOut", defaultLong = -1L) final int receiveTimeOut, @PluginAttribute(value = "reconnectIVL", defaultLong = 100L) final long reconnectIVL, @PluginAttribute(value = "reconnectIVLMax", defaultLong = 0L) final long reconnectIVLMax, @PluginAttribute(value = "sendBufferSize", defaultLong = 0L) final long sendBufferSize, @PluginAttribute(value = "sendTimeOut", defaultLong = -1L) final int sendTimeOut, @PluginAttribute(value = "sndHwm", defaultLong = 1000L) final long sndHwm, @PluginAttribute(value = "tcpKeepAlive", defaultInt = -1) final int tcpKeepAlive, @PluginAttribute(value = "tcpKeepAliveCount", defaultLong = -1L) final long tcpKeepAliveCount, @PluginAttribute(value = "tcpKeepAliveIdle", defaultLong = -1L) final long tcpKeepAliveIdle, @PluginAttribute(value = "tcpKeepAliveInterval", defaultLong = -1L) final long tcpKeepAliveInterval, @PluginAttribute("xpubVerbose") final boolean xpubVerbose) {
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        List<String> endpoints;
        if (properties == null) {
            endpoints = new ArrayList<String>(0);
        }
        else {
            endpoints = new ArrayList<String>(properties.length);
            for (final Property property : properties) {
                if ("endpoint".equalsIgnoreCase(property.getName())) {
                    final String value = property.getValue();
                    if (Strings.isNotEmpty(value)) {
                        endpoints.add(value);
                    }
                }
            }
        }
        JeroMqAppender.LOGGER.debug("Creating JeroMqAppender with name={}, filter={}, layout={}, ignoreExceptions={}, endpoints={}", name, filter, layout, ignoreExceptions, endpoints);
        return new JeroMqAppender(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions, endpoints, affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, null);
    }
    
    @Override
    public synchronized void append(final LogEvent event) {
        final Layout<? extends Serializable> layout = this.getLayout();
        final byte[] formattedMessage = layout.toByteArray(event);
        if (this.manager.send(this.getLayout().toByteArray(event))) {
            ++this.sendRcTrue;
        }
        else {
            ++this.sendRcFalse;
            JeroMqAppender.LOGGER.error("Appender {} could not send message {} to JeroMQ {}", this.getName(), this.sendRcFalse, formattedMessage);
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= this.manager.stop(timeout, timeUnit);
        this.setStopped();
        return stopped;
    }
    
    int getSendRcFalse() {
        return this.sendRcFalse;
    }
    
    int getSendRcTrue() {
        return this.sendRcTrue;
    }
    
    void resetSendRcs() {
        final int n = 0;
        this.sendRcFalse = n;
        this.sendRcTrue = n;
    }
    
    @Override
    public String toString() {
        return "JeroMqAppender{name=" + this.getName() + ", state=" + this.getState() + ", manager=" + this.manager + ", endpoints=" + this.endpoints + '}';
    }
}
