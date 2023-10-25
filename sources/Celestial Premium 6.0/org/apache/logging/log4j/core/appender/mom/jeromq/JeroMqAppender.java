/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.mom.jeromq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.mom.jeromq.JeroMqManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.Strings;

@Plugin(name="JeroMQ", category="Core", elementType="appender", printObject=true)
public final class JeroMqAppender
extends AbstractAppender {
    private static final int DEFAULT_BACKLOG = 100;
    private static final int DEFAULT_IVL = 100;
    private static final int DEFAULT_RCV_HWM = 1000;
    private static final int DEFAULT_SND_HWM = 1000;
    private final JeroMqManager manager;
    private final List<String> endpoints;
    private int sendRcFalse;
    private int sendRcTrue;

    private JeroMqAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, List<String> endpoints, long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHWM, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = JeroMqManager.getJeroMqManager(name, affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHWM, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, endpoints);
        this.endpoints = endpoints;
    }

    @PluginFactory
    public static JeroMqAppender createAppender(@Required(message="No name provided for JeroMqAppender") @PluginAttribute(value="name") String name, @PluginElement(value="Layout") Layout<?> layout, @PluginElement(value="Filter") Filter filter, @PluginElement(value="Properties") Property[] properties, @PluginAttribute(value="ignoreExceptions") boolean ignoreExceptions, @PluginAttribute(value="affinity", defaultLong=0L) long affinity, @PluginAttribute(value="backlog", defaultLong=100L) long backlog, @PluginAttribute(value="delayAttachOnConnect") boolean delayAttachOnConnect, @PluginAttribute(value="identity") byte[] identity, @PluginAttribute(value="ipv4Only", defaultBoolean=true) boolean ipv4Only, @PluginAttribute(value="linger", defaultLong=-1L) long linger, @PluginAttribute(value="maxMsgSize", defaultLong=-1L) long maxMsgSize, @PluginAttribute(value="rcvHwm", defaultLong=1000L) long rcvHwm, @PluginAttribute(value="receiveBufferSize", defaultLong=0L) long receiveBufferSize, @PluginAttribute(value="receiveTimeOut", defaultLong=-1L) int receiveTimeOut, @PluginAttribute(value="reconnectIVL", defaultLong=100L) long reconnectIVL, @PluginAttribute(value="reconnectIVLMax", defaultLong=0L) long reconnectIVLMax, @PluginAttribute(value="sendBufferSize", defaultLong=0L) long sendBufferSize, @PluginAttribute(value="sendTimeOut", defaultLong=-1L) int sendTimeOut, @PluginAttribute(value="sndHwm", defaultLong=1000L) long sndHwm, @PluginAttribute(value="tcpKeepAlive", defaultInt=-1) int tcpKeepAlive, @PluginAttribute(value="tcpKeepAliveCount", defaultLong=-1L) long tcpKeepAliveCount, @PluginAttribute(value="tcpKeepAliveIdle", defaultLong=-1L) long tcpKeepAliveIdle, @PluginAttribute(value="tcpKeepAliveInterval", defaultLong=-1L) long tcpKeepAliveInterval, @PluginAttribute(value="xpubVerbose") boolean xpubVerbose) {
        ArrayList<String> endpoints;
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        if (properties == null) {
            endpoints = new ArrayList<String>(0);
        } else {
            endpoints = new ArrayList(properties.length);
            for (Property property : properties) {
                String value;
                if (!"endpoint".equalsIgnoreCase(property.getName()) || !Strings.isNotEmpty(value = property.getValue())) continue;
                endpoints.add(value);
            }
        }
        LOGGER.debug("Creating JeroMqAppender with name={}, filter={}, layout={}, ignoreExceptions={}, endpoints={}", name, filter, layout, ignoreExceptions, endpoints);
        return new JeroMqAppender(name, filter, layout, ignoreExceptions, endpoints, affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, null);
    }

    @Override
    public synchronized void append(LogEvent event) {
        Layout<? extends Serializable> layout = this.getLayout();
        byte[] formattedMessage = layout.toByteArray(event);
        if (this.manager.send(this.getLayout().toByteArray(event))) {
            ++this.sendRcTrue;
        } else {
            ++this.sendRcFalse;
            LOGGER.error("Appender {} could not send message {} to JeroMQ {}", this.getName(), this.sendRcFalse, formattedMessage);
        }
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        this.setStopped();
        return stopped &= this.manager.stop(timeout, timeUnit);
    }

    int getSendRcFalse() {
        return this.sendRcFalse;
    }

    int getSendRcTrue() {
        return this.sendRcTrue;
    }

    void resetSendRcs() {
        this.sendRcFalse = 0;
        this.sendRcTrue = 0;
    }

    @Override
    public String toString() {
        return "JeroMqAppender{name=" + this.getName() + ", state=" + (Object)((Object)this.getState()) + ", manager=" + this.manager + ", endpoints=" + this.endpoints + '}';
    }
}

