package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.layout.Rfc5424Layout;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.SocketOptions;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
public class SyslogAppender extends SocketAppender {
  protected static final String RFC5424 = "RFC5424";
  
  public static class Builder<B extends Builder<B>> extends SocketAppender.AbstractBuilder<B> implements org.apache.logging.log4j.core.util.Builder<SocketAppender> {
    @PluginBuilderAttribute("facility")
    private Facility facility = Facility.LOCAL0;
    
    @PluginBuilderAttribute("id")
    private String id;
    
    @PluginBuilderAttribute("enterpriseNumber")
    private int enterpriseNumber = 18060;
    
    @PluginBuilderAttribute("includeMdc")
    private boolean includeMdc = true;
    
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
    private Charset charsetName = StandardCharsets.UTF_8;
    
    @PluginBuilderAttribute("exceptionPattern")
    private String exceptionPattern;
    
    @PluginElement("LoggerFields")
    private LoggerFields[] loggerFields;
    
    public SyslogAppender build() {
      Protocol protocol = getProtocol();
      SslConfiguration sslConfiguration = getSslConfiguration();
      boolean useTlsMessageFormat = (sslConfiguration != null || protocol == Protocol.SSL);
      Configuration configuration = getConfiguration();
      Layout<? extends Serializable> layout = getLayout();
      if (layout == null)
        layout = "RFC5424".equalsIgnoreCase(this.format) ? (Layout<? extends Serializable>)Rfc5424Layout.createLayout(this.facility, this.id, this.enterpriseNumber, this.includeMdc, this.mdcId, this.mdcPrefix, this.eventPrefix, this.newLine, this.escapeNL, this.appName, this.msgId, this.excludes, this.includes, this.required, this.exceptionPattern, useTlsMessageFormat, this.loggerFields, configuration) : (Layout<? extends Serializable>)((SyslogLayout.Builder)SyslogLayout.newBuilder().setFacility(this.facility).setIncludeNewLine(this.newLine).setEscapeNL(this.escapeNL).setCharset(this.charsetName)).build(); 
      String name = getName();
      if (name == null) {
        SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
        return null;
      } 
      AbstractSocketManager manager = SocketAppender.createSocketManager(name, protocol, getHost(), getPort(), getConnectTimeoutMillis(), sslConfiguration, 
          getReconnectDelayMillis(), getImmediateFail(), layout, Constants.ENCODER_BYTE_BUFFER_SIZE, (SocketOptions)null);
      return new SyslogAppender(name, layout, getFilter(), isIgnoreExceptions(), isImmediateFlush(), manager, 
          getAdvertise() ? configuration.getAdvertiser() : null, null);
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
    
    public B setFacility(Facility facility) {
      this.facility = facility;
      return (B)asBuilder();
    }
    
    public B setId(String id) {
      this.id = id;
      return (B)asBuilder();
    }
    
    public B setEnterpriseNumber(int enterpriseNumber) {
      this.enterpriseNumber = enterpriseNumber;
      return (B)asBuilder();
    }
    
    public B setIncludeMdc(boolean includeMdc) {
      this.includeMdc = includeMdc;
      return (B)asBuilder();
    }
    
    public B setMdcId(String mdcId) {
      this.mdcId = mdcId;
      return (B)asBuilder();
    }
    
    public B setMdcPrefix(String mdcPrefix) {
      this.mdcPrefix = mdcPrefix;
      return (B)asBuilder();
    }
    
    public B setEventPrefix(String eventPrefix) {
      this.eventPrefix = eventPrefix;
      return (B)asBuilder();
    }
    
    public B setNewLine(boolean newLine) {
      this.newLine = newLine;
      return (B)asBuilder();
    }
    
    public B setEscapeNL(String escapeNL) {
      this.escapeNL = escapeNL;
      return (B)asBuilder();
    }
    
    public B setAppName(String appName) {
      this.appName = appName;
      return (B)asBuilder();
    }
    
    public B setMsgId(String msgId) {
      this.msgId = msgId;
      return (B)asBuilder();
    }
    
    public B setExcludes(String excludes) {
      this.excludes = excludes;
      return (B)asBuilder();
    }
    
    public B setIncludes(String includes) {
      this.includes = includes;
      return (B)asBuilder();
    }
    
    public B setRequired(String required) {
      this.required = required;
      return (B)asBuilder();
    }
    
    public B setFormat(String format) {
      this.format = format;
      return (B)asBuilder();
    }
    
    public B setCharsetName(Charset charset) {
      this.charsetName = charset;
      return (B)asBuilder();
    }
    
    public B setExceptionPattern(String exceptionPattern) {
      this.exceptionPattern = exceptionPattern;
      return (B)asBuilder();
    }
    
    public B setLoggerFields(LoggerFields[] loggerFields) {
      this.loggerFields = loggerFields;
      return (B)asBuilder();
    }
  }
  
  protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, properties);
  }
  
  @Deprecated
  protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser) {
    super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
  }
  
  @Deprecated
  public static <B extends Builder<B>> SyslogAppender createAppender(String host, int port, String protocolStr, SslConfiguration sslConfiguration, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Facility facility, String id, int enterpriseNumber, boolean includeMdc, String mdcId, String mdcPrefix, String eventPrefix, boolean newLine, String escapeNL, String appName, String msgId, String excludes, String includes, String required, String format, Filter filter, Configuration configuration, Charset charset, String exceptionPattern, LoggerFields[] loggerFields, boolean advertise) {
    return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<Builder<Builder<Builder<Builder<Builder>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>>)newSyslogAppenderBuilder()
      .withHost(host))
      .withPort(port))
      .withProtocol((Protocol)EnglishEnums.valueOf(Protocol.class, protocolStr)))
      .withSslConfiguration(sslConfiguration))
      .withConnectTimeoutMillis(connectTimeoutMillis))
      .withReconnectDelayMillis(reconnectDelayMillis))
      .withImmediateFail(immediateFail)).setName(appName))
      .withImmediateFlush(immediateFlush)).setIgnoreExceptions(ignoreExceptions)).setFilter(filter))
      .setConfiguration(configuration))
      .withAdvertise(advertise))
      .setFacility(facility)
      .setId(id)
      .setEnterpriseNumber(enterpriseNumber)
      .setIncludeMdc(includeMdc)
      .setMdcId(mdcId)
      .setMdcPrefix(mdcPrefix)
      .setEventPrefix(eventPrefix)
      .setNewLine(newLine)
      .setAppName(appName)
      .setMsgId(msgId)
      .setExcludes(excludes)
      .setIncludeMdc(includeMdc)
      .setRequired(required)
      .setFormat(format)
      .setCharsetName(charset)
      .setExceptionPattern(exceptionPattern)
      .setLoggerFields(loggerFields)
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newSyslogAppenderBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\SyslogAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */