// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.net.URL;
import org.apache.logging.log4j.core.util.Builder;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Http", category = "Core", elementType = "appender", printObject = true)
public final class HttpAppender extends AbstractAppender
{
    private final HttpManager manager;
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private HttpAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final HttpManager manager, final Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
        Objects.requireNonNull(layout, "layout");
        this.manager = Objects.requireNonNull(manager, "manager");
    }
    
    @Override
    public void start() {
        super.start();
        this.manager.startup();
    }
    
    @Override
    public void append(final LogEvent event) {
        try {
            this.manager.send(this.getLayout(), event);
        }
        catch (Exception e) {
            this.error("Unable to send HTTP in appender [" + this.getName() + "]", event, e);
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
    
    @Override
    public String toString() {
        return "HttpAppender{name=" + this.getName() + ", state=" + this.getState() + '}';
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<HttpAppender>
    {
        @PluginBuilderAttribute
        @Required(message = "No URL provided for HttpAppender")
        private URL url;
        @PluginBuilderAttribute
        private String method;
        @PluginBuilderAttribute
        private int connectTimeoutMillis;
        @PluginBuilderAttribute
        private int readTimeoutMillis;
        @PluginElement("Headers")
        private Property[] headers;
        @PluginElement("SslConfiguration")
        private SslConfiguration sslConfiguration;
        @PluginBuilderAttribute
        private boolean verifyHostname;
        
        public Builder() {
            this.method = "POST";
            this.connectTimeoutMillis = 0;
            this.readTimeoutMillis = 0;
            this.verifyHostname = true;
        }
        
        @Override
        public HttpAppender build() {
            final HttpManager httpManager = new HttpURLConnectionManager(this.getConfiguration(), this.getConfiguration().getLoggerContext(), this.getName(), this.url, this.method, this.connectTimeoutMillis, this.readTimeoutMillis, this.headers, this.sslConfiguration, this.verifyHostname);
            return new HttpAppender(this.getName(), this.getLayout(), this.getFilter(), this.isIgnoreExceptions(), httpManager, this.getPropertyArray(), null);
        }
        
        public URL getUrl() {
            return this.url;
        }
        
        public String getMethod() {
            return this.method;
        }
        
        public int getConnectTimeoutMillis() {
            return this.connectTimeoutMillis;
        }
        
        public int getReadTimeoutMillis() {
            return this.readTimeoutMillis;
        }
        
        public Property[] getHeaders() {
            return this.headers;
        }
        
        public SslConfiguration getSslConfiguration() {
            return this.sslConfiguration;
        }
        
        public boolean isVerifyHostname() {
            return this.verifyHostname;
        }
        
        public B setUrl(final URL url) {
            this.url = url;
            return this.asBuilder();
        }
        
        public B setMethod(final String method) {
            this.method = method;
            return this.asBuilder();
        }
        
        public B setConnectTimeoutMillis(final int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this.asBuilder();
        }
        
        public B setReadTimeoutMillis(final int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this.asBuilder();
        }
        
        public B setHeaders(final Property[] headers) {
            this.headers = headers;
            return this.asBuilder();
        }
        
        public B setSslConfiguration(final SslConfiguration sslConfiguration) {
            this.sslConfiguration = sslConfiguration;
            return this.asBuilder();
        }
        
        public B setVerifyHostname(final boolean verifyHostname) {
            this.verifyHostname = verifyHostname;
            return this.asBuilder();
        }
    }
}
