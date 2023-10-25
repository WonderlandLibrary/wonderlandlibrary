/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(name="Socket", category="Core", elementType="appender", printObject=true)
public class SocketAppender
extends AbstractOutputStreamAppender {
    private Object advertisement;
    private final Advertiser advertiser;

    protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            HashMap<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.advertiser = advertiser;
    }

    @Override
    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }

    @PluginFactory
    public static SocketAppender createAppender(@PluginAttribute(value="host") String host, @PluginAttribute(value="port") String portNum, @PluginAttribute(value="protocol") String protocol, @PluginAttribute(value="reconnectionDelay") String delay, @PluginAttribute(value="immediateFail") String immediateFail, @PluginAttribute(value="name") String name, @PluginAttribute(value="immediateFlush") String immediateFlush, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="advertise") String advertise, @PluginConfiguration Configuration config) {
        AbstractSocketManager manager;
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean fail = Booleans.parseBoolean(immediateFail, true);
        int reconnectDelay = AbstractAppender.parseInt(delay, 0);
        int port = AbstractAppender.parseInt(portNum, 0);
        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }
        if (name == null) {
            LOGGER.error("No name provided for SocketAppender");
            return null;
        }
        Protocol p = EnglishEnums.valueOf(Protocol.class, protocol != null ? protocol : Protocol.TCP.name());
        if (p.equals((Object)Protocol.UDP)) {
            isFlush = true;
        }
        if ((manager = SocketAppender.createSocketManager(p, host, port, reconnectDelay, fail, layout)) == null) {
            return null;
        }
        return new SocketAppender(name, layout, filter, manager, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }

    protected static AbstractSocketManager createSocketManager(Protocol p, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout) {
        switch (p) {
            case TCP: {
                return TCPSocketManager.getSocketManager(host, port, delay, immediateFail, layout);
            }
            case UDP: {
                return DatagramSocketManager.getSocketManager(host, port, layout);
            }
        }
        return null;
    }
}

