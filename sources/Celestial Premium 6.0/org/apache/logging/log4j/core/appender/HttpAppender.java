/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.HttpManager;
import org.apache.logging.log4j.core.appender.HttpURLConnectionManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

@Plugin(name="Http", category="Core", elementType="appender", printObject=true)
public final class HttpAppender
extends AbstractAppender {
    private final HttpManager manager;

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private HttpAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, HttpManager manager, Property[] properties) {
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
    public void append(LogEvent event) {
        try {
            this.manager.send(this.getLayout(), event);
        }
        catch (Exception e) {
            this.error("Unable to send HTTP in appender [" + this.getName() + "]", event, e);
        }
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        this.setStopped();
        return stopped &= this.manager.stop(timeout, timeUnit);
    }

    @Override
    public String toString() {
        return "HttpAppender{name=" + this.getName() + ", state=" + (Object)((Object)this.getState()) + '}';
    }

    public static class Builder<B extends Builder<B>>
    extends AbstractAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<HttpAppender> {
        @PluginBuilderAttribute
        @Required(message="No URL provided for HttpAppender")
        private URL url;
        @PluginBuilderAttribute
        private String method = "POST";
        @PluginBuilderAttribute
        private int connectTimeoutMillis = 0;
        @PluginBuilderAttribute
        private int readTimeoutMillis = 0;
        @PluginElement(value="Headers")
        private Property[] headers;
        @PluginElement(value="SslConfiguration")
        private SslConfiguration sslConfiguration;
        @PluginBuilderAttribute
        private boolean verifyHostname = true;

        @Override
        public HttpAppender build() {
            HttpURLConnectionManager httpManager = new HttpURLConnectionManager(this.getConfiguration(), this.getConfiguration().getLoggerContext(), this.getName(), this.url, this.method, this.connectTimeoutMillis, this.readTimeoutMillis, this.headers, this.sslConfiguration, this.verifyHostname);
            return new HttpAppender(this.getName(), this.getLayout(), this.getFilter(), this.isIgnoreExceptions(), httpManager, this.getPropertyArray());
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

        public B setUrl(URL url) {
            this.url = url;
            return (B)((Builder)this.asBuilder());
        }

        public B setMethod(String method) {
            this.method = method;
            return (B)((Builder)this.asBuilder());
        }

        public B setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return (B)((Builder)this.asBuilder());
        }

        public B setReadTimeoutMillis(int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return (B)((Builder)this.asBuilder());
        }

        public B setHeaders(Property[] headers) {
            this.headers = headers;
            return (B)((Builder)this.asBuilder());
        }

        public B setSslConfiguration(SslConfiguration sslConfiguration) {
            this.sslConfiguration = sslConfiguration;
            return (B)((Builder)this.asBuilder());
        }

        public B setVerifyHostname(boolean verifyHostname) {
            this.verifyHostname = verifyHostname;
            return (B)((Builder)this.asBuilder());
        }
    }
}

