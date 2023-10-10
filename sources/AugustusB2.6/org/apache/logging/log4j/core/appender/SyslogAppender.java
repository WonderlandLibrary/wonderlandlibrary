// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.net.SocketOptions;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.layout.Rfc5424Layout;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.layout.LoggerFields;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
public class SyslogAppender extends SocketAppender
{
    protected static final String RFC5424 = "RFC5424";
    
    protected SyslogAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final AbstractSocketManager manager, final Advertiser advertiser, final Property[] properties) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, properties);
    }
    
    @Deprecated
    protected SyslogAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final AbstractSocketManager manager, final Advertiser advertiser) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
    }
    
    @Deprecated
    public static <B extends Builder<B>> SyslogAppender createAppender(final String host, final int port, final String protocolStr, final SslConfiguration sslConfiguration, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final String name, final boolean immediateFlush, final boolean ignoreExceptions, final Facility facility, final String id, final int enterpriseNumber, final boolean includeMdc, final String mdcId, final String mdcPrefix, final String eventPrefix, final boolean newLine, final String escapeNL, final String appName, final String msgId, final String excludes, final String includes, final String required, final String format, final Filter filter, final Configuration configuration, final Charset charset, final String exceptionPattern, final LoggerFields[] loggerFields, final boolean advertise) {
        return newSyslogAppenderBuilder().withHost(host).withPort(port).withProtocol(EnglishEnums.valueOf(Protocol.class, protocolStr)).withSslConfiguration(sslConfiguration).withConnectTimeoutMillis(connectTimeoutMillis).withReconnectDelayMillis(reconnectDelayMillis).withImmediateFail(immediateFail).setName(appName).withImmediateFlush(immediateFlush).setIgnoreExceptions(ignoreExceptions).setFilter(filter).setConfiguration(configuration).withAdvertise(advertise).setFacility(facility).setId(id).setEnterpriseNumber(enterpriseNumber).setIncludeMdc(includeMdc).setMdcId(mdcId).setMdcPrefix(mdcPrefix).setEventPrefix(eventPrefix).setNewLine(newLine).setAppName(appName).setMsgId(msgId).setExcludes(excludes).setIncludeMdc(includeMdc).setRequired(required).setFormat(format).setCharsetName(charset).setExceptionPattern(exceptionPattern).setLoggerFields(loggerFields).build();
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newSyslogAppenderBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractBuilder<B> implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
    {
        @PluginBuilderAttribute("facility")
        private Facility facility;
        @PluginBuilderAttribute("id")
        private String id;
        @PluginBuilderAttribute("enterpriseNumber")
        private int enterpriseNumber;
        @PluginBuilderAttribute("includeMdc")
        private boolean includeMdc;
        @PluginBuilderAttribute("mdcId")
        private String mdcId;
        @PluginBuilderAttribute("mdcPrefix")
        private String mdcPrefix;
        @PluginBuilderAttribute("eventPrefix")
        private String eventPrefix;
        @PluginBuilderAttribute("newLine")
        private boolean newLine;
        @PluginBuilderAttribute("newLineEscape")
        private String escapeNL;
        @PluginBuilderAttribute("appName")
        private String appName;
        @PluginBuilderAttribute("messageId")
        private String msgId;
        @PluginBuilderAttribute("mdcExcludes")
        private String excludes;
        @PluginBuilderAttribute("mdcIncludes")
        private String includes;
        @PluginBuilderAttribute("mdcRequired")
        private String required;
        @PluginBuilderAttribute("format")
        private String format;
        @PluginBuilderAttribute("charset")
        private Charset charsetName;
        @PluginBuilderAttribute("exceptionPattern")
        private String exceptionPattern;
        @PluginElement("LoggerFields")
        private LoggerFields[] loggerFields;
        
        public Builder() {
            this.facility = Facility.LOCAL0;
            this.enterpriseNumber = 18060;
            this.includeMdc = true;
            this.charsetName = StandardCharsets.UTF_8;
        }
        
        @Override
        public SyslogAppender build() {
            final Protocol protocol = this.getProtocol();
            final SslConfiguration sslConfiguration = this.getSslConfiguration();
            final boolean useTlsMessageFormat = sslConfiguration != null || protocol == Protocol.SSL;
            final Configuration configuration = this.getConfiguration();
            Layout<? extends Serializable> layout = this.getLayout();
            if (layout == null) {
                layout = ("RFC5424".equalsIgnoreCase(this.format) ? Rfc5424Layout.createLayout(this.facility, this.id, this.enterpriseNumber, this.includeMdc, this.mdcId, this.mdcPrefix, this.eventPrefix, this.newLine, this.escapeNL, this.appName, this.msgId, this.excludes, this.includes, this.required, this.exceptionPattern, useTlsMessageFormat, this.loggerFields, configuration) : SyslogLayout.newBuilder().setFacility(this.facility).setIncludeNewLine(this.newLine).setEscapeNL(this.escapeNL).setCharset(this.charsetName).build());
            }
            final String name = this.getName();
            if (name == null) {
                SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
                return null;
            }
            final AbstractSocketManager manager = SocketAppender.createSocketManager(name, protocol, this.getHost(), this.getPort(), this.getConnectTimeoutMillis(), sslConfiguration, this.getReconnectDelayMillis(), this.getImmediateFail(), layout, Constants.ENCODER_BYTE_BUFFER_SIZE, null);
            return new SyslogAppender(name, layout, this.getFilter(), this.isIgnoreExceptions(), this.isImmediateFlush(), manager, this.getAdvertise() ? configuration.getAdvertiser() : null, null);
        }
        
        public Facility getFacility() {
            return this.facility;
        }
        
        public String getId() {
            return this.id;
        }
        
        public int getEnterpriseNumber() {
            return this.enterpriseNumber;
        }
        
        public boolean isIncludeMdc() {
            return this.includeMdc;
        }
        
        public String getMdcId() {
            return this.mdcId;
        }
        
        public String getMdcPrefix() {
            return this.mdcPrefix;
        }
        
        public String getEventPrefix() {
            return this.eventPrefix;
        }
        
        public boolean isNewLine() {
            return this.newLine;
        }
        
        public String getEscapeNL() {
            return this.escapeNL;
        }
        
        public String getAppName() {
            return this.appName;
        }
        
        public String getMsgId() {
            return this.msgId;
        }
        
        public String getExcludes() {
            return this.excludes;
        }
        
        public String getIncludes() {
            return this.includes;
        }
        
        public String getRequired() {
            return this.required;
        }
        
        public String getFormat() {
            return this.format;
        }
        
        public Charset getCharsetName() {
            return this.charsetName;
        }
        
        public String getExceptionPattern() {
            return this.exceptionPattern;
        }
        
        public LoggerFields[] getLoggerFields() {
            return this.loggerFields;
        }
        
        public B setFacility(final Facility facility) {
            this.facility = facility;
            return this.asBuilder();
        }
        
        public B setId(final String id) {
            this.id = id;
            return this.asBuilder();
        }
        
        public B setEnterpriseNumber(final int enterpriseNumber) {
            this.enterpriseNumber = enterpriseNumber;
            return this.asBuilder();
        }
        
        public B setIncludeMdc(final boolean includeMdc) {
            this.includeMdc = includeMdc;
            return this.asBuilder();
        }
        
        public B setMdcId(final String mdcId) {
            this.mdcId = mdcId;
            return this.asBuilder();
        }
        
        public B setMdcPrefix(final String mdcPrefix) {
            this.mdcPrefix = mdcPrefix;
            return this.asBuilder();
        }
        
        public B setEventPrefix(final String eventPrefix) {
            this.eventPrefix = eventPrefix;
            return this.asBuilder();
        }
        
        public B setNewLine(final boolean newLine) {
            this.newLine = newLine;
            return this.asBuilder();
        }
        
        public B setEscapeNL(final String escapeNL) {
            this.escapeNL = escapeNL;
            return this.asBuilder();
        }
        
        public B setAppName(final String appName) {
            this.appName = appName;
            return this.asBuilder();
        }
        
        public B setMsgId(final String msgId) {
            this.msgId = msgId;
            return this.asBuilder();
        }
        
        public B setExcludes(final String excludes) {
            this.excludes = excludes;
            return this.asBuilder();
        }
        
        public B setIncludes(final String includes) {
            this.includes = includes;
            return this.asBuilder();
        }
        
        public B setRequired(final String required) {
            this.required = required;
            return this.asBuilder();
        }
        
        public B setFormat(final String format) {
            this.format = format;
            return this.asBuilder();
        }
        
        public B setCharsetName(final Charset charset) {
            this.charsetName = charset;
            return this.asBuilder();
        }
        
        public B setExceptionPattern(final String exceptionPattern) {
            this.exceptionPattern = exceptionPattern;
            return this.asBuilder();
        }
        
        public B setLoggerFields(final LoggerFields[] loggerFields) {
            this.loggerFields = loggerFields;
            return this.asBuilder();
        }
    }
}
