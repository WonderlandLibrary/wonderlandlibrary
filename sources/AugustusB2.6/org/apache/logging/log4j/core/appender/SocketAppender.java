// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.net.SslSocketManager;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.TcpSocketManager;
import org.apache.logging.log4j.core.net.SocketOptions;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.Protocol;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.net.AbstractSocketManager;

@Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
public class SocketAppender extends AbstractOutputStreamAppender<AbstractSocketManager>
{
    private final Object advertisement;
    private final Advertiser advertiser;
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    protected SocketAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final AbstractSocketManager manager, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        else {
            this.advertisement = null;
        }
        this.advertiser = advertiser;
    }
    
    @Deprecated
    protected SocketAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final AbstractSocketManager manager, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser) {
        this(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        super.stop(timeout, timeUnit, false);
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
        this.setStopped();
        return true;
    }
    
    @Deprecated
    @PluginFactory
    public static SocketAppender createAppender(final String host, final int port, final Protocol protocol, final SslConfiguration sslConfig, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final String name, final boolean immediateFlush, final boolean ignoreExceptions, final Layout<? extends Serializable> layout, final Filter filter, final boolean advertise, final Configuration configuration) {
        return newBuilder().withAdvertise(advertise).setConfiguration(configuration).withConnectTimeoutMillis(connectTimeoutMillis).setFilter(filter).withHost(host).setIgnoreExceptions(ignoreExceptions).withImmediateFail(immediateFail).setLayout(layout).setName(name).withPort(port).withProtocol(protocol).withReconnectDelayMillis(reconnectDelayMillis).withSslConfiguration(sslConfig).build();
    }
    
    @Deprecated
    public static SocketAppender createAppender(final String host, final String portNum, final String protocolIn, final SslConfiguration sslConfig, final int connectTimeoutMillis, final String delayMillis, final String immediateFail, final String name, final String immediateFlush, final String ignore, final Layout<? extends Serializable> layout, final Filter filter, final String advertise, final Configuration config) {
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean fail = Booleans.parseBoolean(immediateFail, true);
        final int reconnectDelayMillis = AbstractAppender.parseInt(delayMillis, 0);
        final int port = AbstractAppender.parseInt(portNum, 0);
        final Protocol p = (protocolIn == null) ? Protocol.UDP : Protocol.valueOf(protocolIn);
        return createAppender(host, port, p, sslConfig, connectTimeoutMillis, reconnectDelayMillis, fail, name, isFlush, ignoreExceptions, layout, filter, isAdvertise, config);
    }
    
    @Deprecated
    protected static AbstractSocketManager createSocketManager(final String name, final Protocol protocol, final String host, final int port, final int connectTimeoutMillis, final SslConfiguration sslConfig, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        return createSocketManager(name, protocol, host, port, connectTimeoutMillis, sslConfig, reconnectDelayMillis, immediateFail, layout, bufferSize, null);
    }
    
    protected static AbstractSocketManager createSocketManager(final String name, Protocol protocol, final String host, final int port, final int connectTimeoutMillis, final SslConfiguration sslConfig, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        if (protocol == Protocol.TCP && sslConfig != null) {
            protocol = Protocol.SSL;
        }
        if (protocol != Protocol.SSL && sslConfig != null) {
            SocketAppender.LOGGER.info("Appender {} ignoring SSL configuration for {} protocol", name, protocol);
        }
        switch (protocol) {
            case TCP: {
                return TcpSocketManager.getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
            }
            case UDP: {
                return DatagramSocketManager.getSocketManager(host, port, layout, bufferSize);
            }
            case SSL: {
                return SslSocketManager.getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
            }
            default: {
                throw new IllegalArgumentException(protocol.toString());
            }
        }
    }
    
    @Override
    protected void directEncodeEvent(final LogEvent event) {
        this.writeByteArrayToManager(event);
    }
    
    public abstract static class AbstractBuilder<B extends AbstractBuilder<B>> extends AbstractOutputStreamAppender.Builder<B>
    {
        @PluginBuilderAttribute
        private boolean advertise;
        @PluginBuilderAttribute
        private int connectTimeoutMillis;
        @PluginBuilderAttribute
        @ValidHost
        private String host;
        @PluginBuilderAttribute
        private boolean immediateFail;
        @PluginBuilderAttribute
        @ValidPort
        private int port;
        @PluginBuilderAttribute
        private Protocol protocol;
        @PluginBuilderAttribute
        @PluginAliases({ "reconnectDelay", "reconnectionDelay", "delayMillis", "reconnectionDelayMillis" })
        private int reconnectDelayMillis;
        @PluginElement("SocketOptions")
        private SocketOptions socketOptions;
        @PluginElement("SslConfiguration")
        @PluginAliases({ "SslConfig" })
        private SslConfiguration sslConfiguration;
        
        public AbstractBuilder() {
            this.host = "localhost";
            this.immediateFail = true;
            this.protocol = Protocol.TCP;
        }
        
        public boolean getAdvertise() {
            return this.advertise;
        }
        
        public int getConnectTimeoutMillis() {
            return this.connectTimeoutMillis;
        }
        
        public String getHost() {
            return this.host;
        }
        
        public int getPort() {
            return this.port;
        }
        
        public Protocol getProtocol() {
            return this.protocol;
        }
        
        public SslConfiguration getSslConfiguration() {
            return this.sslConfiguration;
        }
        
        public boolean getImmediateFail() {
            return this.immediateFail;
        }
        
        public B withAdvertise(final boolean advertise) {
            this.advertise = advertise;
            return this.asBuilder();
        }
        
        public B withConnectTimeoutMillis(final int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this.asBuilder();
        }
        
        public B withHost(final String host) {
            this.host = host;
            return this.asBuilder();
        }
        
        public B withImmediateFail(final boolean immediateFail) {
            this.immediateFail = immediateFail;
            return this.asBuilder();
        }
        
        public B withPort(final int port) {
            this.port = port;
            return this.asBuilder();
        }
        
        public B withProtocol(final Protocol protocol) {
            this.protocol = protocol;
            return this.asBuilder();
        }
        
        public B withReconnectDelayMillis(final int reconnectDelayMillis) {
            this.reconnectDelayMillis = reconnectDelayMillis;
            return this.asBuilder();
        }
        
        public B withSocketOptions(final SocketOptions socketOptions) {
            this.socketOptions = socketOptions;
            return this.asBuilder();
        }
        
        public B withSslConfiguration(final SslConfiguration sslConfiguration) {
            this.sslConfiguration = sslConfiguration;
            return this.asBuilder();
        }
        
        public int getReconnectDelayMillis() {
            return this.reconnectDelayMillis;
        }
        
        public SocketOptions getSocketOptions() {
            return this.socketOptions;
        }
    }
    
    public static class Builder extends AbstractBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
    {
        @Override
        public SocketAppender build() {
            boolean immediateFlush = this.isImmediateFlush();
            final boolean bufferedIo = this.isBufferedIo();
            final Layout<? extends Serializable> layout = this.getLayout();
            if (layout == null) {
                SocketAppender.LOGGER.error("No layout provided for SocketAppender");
                return null;
            }
            final String name = this.getName();
            if (name == null) {
                SocketAppender.LOGGER.error("No name provided for SocketAppender");
                return null;
            }
            final Protocol protocol = this.getProtocol();
            final Protocol actualProtocol = (protocol != null) ? protocol : Protocol.TCP;
            if (actualProtocol == Protocol.UDP) {
                immediateFlush = true;
            }
            final AbstractSocketManager manager = SocketAppender.createSocketManager(name, actualProtocol, this.getHost(), this.getPort(), this.getConnectTimeoutMillis(), this.getSslConfiguration(), this.getReconnectDelayMillis(), this.getImmediateFail(), layout, this.getBufferSize(), this.getSocketOptions());
            return new SocketAppender(name, layout, this.getFilter(), manager, this.isIgnoreExceptions(), !bufferedIo || immediateFlush, this.getAdvertise() ? this.getConfiguration().getAdvertiser() : null, this.getPropertyArray());
        }
    }
}
