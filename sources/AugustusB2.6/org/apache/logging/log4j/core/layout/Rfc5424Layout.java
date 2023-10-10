// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.message.MapMessage;
import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.SortedMap;
import org.apache.logging.log4j.util.StringBuilders;
import java.util.TreeMap;
import org.apache.logging.log4j.LoggingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.logging.log4j.message.StructuredDataCollectionMessage;
import org.apache.logging.log4j.message.MessageCollectionMessage;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.TlsSyslogFrame;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import java.util.Iterator;
import java.util.HashMap;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.ProcessIdUtil;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.layout.internal.IncludeChecker;
import org.apache.logging.log4j.core.layout.internal.ExcludeChecker;
import java.util.ArrayList;
import org.apache.logging.log4j.core.util.Patterns;
import org.apache.logging.log4j.core.util.NetUtils;
import java.util.regex.Matcher;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import java.util.Map;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.layout.internal.ListChecker;
import java.util.List;
import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.core.net.Facility;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Rfc5424Layout", category = "Core", elementType = "layout", printObject = true)
public final class Rfc5424Layout extends AbstractStringLayout
{
    public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
    public static final String DEFAULT_ID = "Audit";
    public static final Pattern NEWLINE_PATTERN;
    public static final Pattern PARAM_VALUE_ESCAPE_PATTERN;
    public static final String DEFAULT_MDCID = "mdc";
    private static final String LF = "\n";
    private static final int TWO_DIGITS = 10;
    private static final int THREE_DIGITS = 100;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MINUTES_PER_HOUR = 60;
    private static final String COMPONENT_KEY = "RFC5424-Converter";
    private final Facility facility;
    private final String defaultId;
    private final int enterpriseNumber;
    private final boolean includeMdc;
    private final String mdcId;
    private final StructuredDataId mdcSdId;
    private final String localHostName;
    private final String appName;
    private final String messageId;
    private final String configName;
    private final String mdcPrefix;
    private final String eventPrefix;
    private final List<String> mdcExcludes;
    private final List<String> mdcIncludes;
    private final List<String> mdcRequired;
    private final ListChecker listChecker;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final boolean useTlsMessageFormat;
    private long lastTimestamp;
    private String timestamppStr;
    private final List<PatternFormatter> exceptionFormatters;
    private final Map<String, FieldFormatter> fieldFormatters;
    private final String procId;
    
    private Rfc5424Layout(final Configuration config, final Facility facility, final String id, final int ein, final boolean includeMDC, final boolean includeNL, final String escapeNL, final String mdcId, final String mdcPrefix, final String eventPrefix, final String appName, final String messageId, final String excludes, final String includes, final String required, final Charset charset, final String exceptionPattern, final boolean useTLSMessageFormat, final LoggerFields[] loggerFields) {
        super(charset);
        this.lastTimestamp = -1L;
        final PatternParser exceptionParser = createPatternParser(config, ThrowablePatternConverter.class);
        this.exceptionFormatters = ((exceptionPattern == null) ? null : exceptionParser.parse(exceptionPattern));
        this.facility = facility;
        this.defaultId = ((id == null) ? "Audit" : id);
        this.enterpriseNumber = ein;
        this.includeMdc = includeMDC;
        this.includeNewLine = includeNL;
        this.escapeNewLine = ((escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL));
        this.mdcId = ((mdcId != null) ? mdcId : ((id == null) ? "mdc" : id));
        this.mdcSdId = new StructuredDataId(this.mdcId, this.enterpriseNumber, null, null);
        this.mdcPrefix = mdcPrefix;
        this.eventPrefix = eventPrefix;
        this.appName = appName;
        this.messageId = messageId;
        this.useTlsMessageFormat = useTLSMessageFormat;
        this.localHostName = NetUtils.getLocalHostname();
        ListChecker checker = null;
        if (excludes != null) {
            final String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
            if (array.length > 0) {
                this.mdcExcludes = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcExcludes.add(str.trim());
                }
                checker = new ExcludeChecker(this.mdcExcludes);
            }
            else {
                this.mdcExcludes = null;
            }
        }
        else {
            this.mdcExcludes = null;
        }
        if (includes != null) {
            final String[] array = includes.split(Patterns.COMMA_SEPARATOR);
            if (array.length > 0) {
                this.mdcIncludes = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcIncludes.add(str.trim());
                }
                checker = new IncludeChecker(this.mdcIncludes);
            }
            else {
                this.mdcIncludes = null;
            }
        }
        else {
            this.mdcIncludes = null;
        }
        if (required != null) {
            final String[] array = required.split(Patterns.COMMA_SEPARATOR);
            if (array.length > 0) {
                this.mdcRequired = new ArrayList<String>(array.length);
                for (final String str : array) {
                    this.mdcRequired.add(str.trim());
                }
            }
            else {
                this.mdcRequired = null;
            }
        }
        else {
            this.mdcRequired = null;
        }
        this.listChecker = ((checker != null) ? checker : ListChecker.NOOP_CHECKER);
        final String name = (config == null) ? null : config.getName();
        this.configName = (Strings.isNotEmpty(name) ? name : null);
        this.fieldFormatters = this.createFieldFormatters(loggerFields, config);
        this.procId = ProcessIdUtil.getProcessId();
    }
    
    private Map<String, FieldFormatter> createFieldFormatters(final LoggerFields[] loggerFields, final Configuration config) {
        final Map<String, FieldFormatter> sdIdMap = new HashMap<String, FieldFormatter>((loggerFields == null) ? 0 : loggerFields.length);
        if (loggerFields != null) {
            for (final LoggerFields loggerField : loggerFields) {
                final StructuredDataId key = (loggerField.getSdId() == null) ? this.mdcSdId : loggerField.getSdId();
                final Map<String, List<PatternFormatter>> sdParams = new HashMap<String, List<PatternFormatter>>();
                final Map<String, String> fields = loggerField.getMap();
                if (!fields.isEmpty()) {
                    final PatternParser fieldParser = createPatternParser(config, null);
                    for (final Map.Entry<String, String> entry : fields.entrySet()) {
                        final List<PatternFormatter> formatters = fieldParser.parse(entry.getValue());
                        sdParams.put(entry.getKey(), formatters);
                    }
                    final FieldFormatter fieldFormatter = new FieldFormatter(sdParams, loggerField.getDiscardIfAllFieldsAreEmpty());
                    sdIdMap.put(key.toString(), fieldFormatter);
                }
            }
        }
        return (sdIdMap.size() > 0) ? sdIdMap : null;
    }
    
    private static PatternParser createPatternParser(final Configuration config, final Class<? extends PatternConverter> filterClass) {
        if (config == null) {
            return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
        }
        PatternParser parser = config.getComponent("RFC5424-Converter");
        if (parser == null) {
            parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
            config.addComponent("RFC5424-Converter", parser);
            parser = config.getComponent("RFC5424-Converter");
        }
        return parser;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "true");
        result.put("formatType", "RFC5424");
        return result;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = AbstractStringLayout.getStringBuilder();
        this.appendPriority(buf, event.getLevel());
        this.appendTimestamp(buf, event.getTimeMillis());
        this.appendSpace(buf);
        this.appendHostName(buf);
        this.appendSpace(buf);
        this.appendAppName(buf);
        this.appendSpace(buf);
        this.appendProcessId(buf);
        this.appendSpace(buf);
        this.appendMessageId(buf, event.getMessage());
        this.appendSpace(buf);
        this.appendStructuredElements(buf, event);
        this.appendMessage(buf, event);
        if (this.useTlsMessageFormat) {
            return new TlsSyslogFrame(buf.toString()).toString();
        }
        return buf.toString();
    }
    
    private void appendPriority(final StringBuilder buffer, final Level logLevel) {
        buffer.append('<');
        buffer.append(Priority.getPriority(this.facility, logLevel));
        buffer.append(">1 ");
    }
    
    private void appendTimestamp(final StringBuilder buffer, final long milliseconds) {
        buffer.append(this.computeTimeStampString(milliseconds));
    }
    
    private void appendSpace(final StringBuilder buffer) {
        buffer.append(' ');
    }
    
    private void appendHostName(final StringBuilder buffer) {
        buffer.append(this.localHostName);
    }
    
    private void appendAppName(final StringBuilder buffer) {
        if (this.appName != null) {
            buffer.append(this.appName);
        }
        else if (this.configName != null) {
            buffer.append(this.configName);
        }
        else {
            buffer.append('-');
        }
    }
    
    private void appendProcessId(final StringBuilder buffer) {
        buffer.append(this.getProcId());
    }
    
    private void appendMessageId(final StringBuilder buffer, final Message message) {
        final boolean isStructured = message instanceof StructuredDataMessage;
        final String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
        if (type != null) {
            buffer.append(type);
        }
        else if (this.messageId != null) {
            buffer.append(this.messageId);
        }
        else {
            buffer.append('-');
        }
    }
    
    private void appendMessage(final StringBuilder buffer, final LogEvent event) {
        final Message message = event.getMessage();
        final String text = (message instanceof StructuredDataMessage || message instanceof MessageCollectionMessage) ? message.getFormat() : message.getFormattedMessage();
        if (text != null && text.length() > 0) {
            buffer.append(' ').append(this.escapeNewlines(text, this.escapeNewLine));
        }
        if (this.exceptionFormatters != null && event.getThrown() != null) {
            final StringBuilder exception = new StringBuilder("\n");
            for (final PatternFormatter formatter : this.exceptionFormatters) {
                formatter.format(event, exception);
            }
            buffer.append(this.escapeNewlines(exception.toString(), this.escapeNewLine));
        }
        if (this.includeNewLine) {
            buffer.append("\n");
        }
    }
    
    private void appendStructuredElements(final StringBuilder buffer, final LogEvent event) {
        final Message message = event.getMessage();
        final boolean isStructured = message instanceof StructuredDataMessage || message instanceof StructuredDataCollectionMessage;
        if (!isStructured && this.fieldFormatters != null && this.fieldFormatters.isEmpty() && !this.includeMdc) {
            buffer.append('-');
            return;
        }
        final Map<String, StructuredDataElement> sdElements = new HashMap<String, StructuredDataElement>();
        final Map<String, String> contextMap = event.getContextData().toMap();
        if (this.mdcRequired != null) {
            this.checkRequired(contextMap);
        }
        if (this.fieldFormatters != null) {
            for (final Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet()) {
                final String sdId = sdElement.getKey();
                final StructuredDataElement elem = sdElement.getValue().format(event);
                sdElements.put(sdId, elem);
            }
        }
        if (this.includeMdc && contextMap.size() > 0) {
            final String mdcSdIdStr = this.mdcSdId.toString();
            final StructuredDataElement union = sdElements.get(mdcSdIdStr);
            if (union != null) {
                union.union(contextMap);
                sdElements.put(mdcSdIdStr, union);
            }
            else {
                final StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, this.mdcPrefix, false);
                sdElements.put(mdcSdIdStr, formattedContextMap);
            }
        }
        if (isStructured) {
            if (message instanceof MessageCollectionMessage) {
                for (final StructuredDataMessage data : (StructuredDataCollectionMessage)message) {
                    this.addStructuredData(sdElements, data);
                }
            }
            else {
                this.addStructuredData(sdElements, (StructuredDataMessage)message);
            }
        }
        if (sdElements.isEmpty()) {
            buffer.append('-');
            return;
        }
        for (final Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
            this.formatStructuredElement(entry.getKey(), entry.getValue(), buffer, this.listChecker);
        }
    }
    
    private void addStructuredData(final Map<String, StructuredDataElement> sdElements, final StructuredDataMessage data) {
        final Map<String, String> map = ((MapMessage<M, String>)data).getData();
        final StructuredDataId id = data.getId();
        final String sdId = this.getId(id);
        if (sdElements.containsKey(sdId)) {
            final StructuredDataElement union = sdElements.get(id.toString());
            union.union(map);
            sdElements.put(sdId, union);
        }
        else {
            final StructuredDataElement formattedData = new StructuredDataElement(map, this.eventPrefix, false);
            sdElements.put(sdId, formattedData);
        }
    }
    
    private String escapeNewlines(final String text, final String replacement) {
        if (null == replacement) {
            return text;
        }
        return Rfc5424Layout.NEWLINE_PATTERN.matcher(text).replaceAll(replacement);
    }
    
    protected String getProcId() {
        return this.procId;
    }
    
    protected List<String> getMdcExcludes() {
        return this.mdcExcludes;
    }
    
    protected List<String> getMdcIncludes() {
        return this.mdcIncludes;
    }
    
    private String computeTimeStampString(final long now) {
        final long last;
        synchronized (this) {
            last = this.lastTimestamp;
            if (now == this.lastTimestamp) {
                return this.timestamppStr;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(now);
        buffer.append(Integer.toString(cal.get(1)));
        buffer.append('-');
        this.pad(cal.get(2) + 1, 10, buffer);
        buffer.append('-');
        this.pad(cal.get(5), 10, buffer);
        buffer.append('T');
        this.pad(cal.get(11), 10, buffer);
        buffer.append(':');
        this.pad(cal.get(12), 10, buffer);
        buffer.append(':');
        this.pad(cal.get(13), 10, buffer);
        buffer.append('.');
        this.pad(cal.get(14), 100, buffer);
        int tzmin = (cal.get(15) + cal.get(16)) / 60000;
        if (tzmin == 0) {
            buffer.append('Z');
        }
        else {
            if (tzmin < 0) {
                tzmin = -tzmin;
                buffer.append('-');
            }
            else {
                buffer.append('+');
            }
            final int tzhour = tzmin / 60;
            tzmin -= tzhour * 60;
            this.pad(tzhour, 10, buffer);
            buffer.append(':');
            this.pad(tzmin, 10, buffer);
        }
        synchronized (this) {
            if (last == this.lastTimestamp) {
                this.lastTimestamp = now;
                this.timestamppStr = buffer.toString();
            }
        }
        return buffer.toString();
    }
    
    private void pad(final int val, int max, final StringBuilder buf) {
        while (max > 1) {
            if (val < max) {
                buf.append('0');
            }
            max /= 10;
        }
        buf.append(Integer.toString(val));
    }
    
    private void formatStructuredElement(final String id, final StructuredDataElement data, final StringBuilder sb, final ListChecker checker) {
        if ((id == null && this.defaultId == null) || data.discard()) {
            return;
        }
        sb.append('[');
        sb.append(id);
        if (!this.mdcSdId.toString().equals(id)) {
            this.appendMap(data.getPrefix(), data.getFields(), sb, ListChecker.NOOP_CHECKER);
        }
        else {
            this.appendMap(data.getPrefix(), data.getFields(), sb, checker);
        }
        sb.append(']');
    }
    
    private String getId(final StructuredDataId id) {
        final StringBuilder sb = new StringBuilder();
        if (id == null || id.getName() == null) {
            sb.append(this.defaultId);
        }
        else {
            sb.append(id.getName());
        }
        int ein = (id != null) ? id.getEnterpriseNumber() : this.enterpriseNumber;
        if (ein < 0) {
            ein = this.enterpriseNumber;
        }
        if (ein >= 0) {
            sb.append('@').append(ein);
        }
        return sb.toString();
    }
    
    private void checkRequired(final Map<String, String> map) {
        for (final String key : this.mdcRequired) {
            final String value = map.get(key);
            if (value == null) {
                throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
            }
        }
    }
    
    private void appendMap(final String prefix, final Map<String, String> map, final StringBuilder sb, final ListChecker checker) {
        final SortedMap<String, String> sorted = new TreeMap<String, String>(map);
        for (final Map.Entry<String, String> entry : sorted.entrySet()) {
            if (checker.check(entry.getKey()) && entry.getValue() != null) {
                sb.append(' ');
                if (prefix != null) {
                    sb.append(prefix);
                }
                final String safeKey = this.escapeNewlines(this.escapeSDParams(entry.getKey()), this.escapeNewLine);
                final String safeValue = this.escapeNewlines(this.escapeSDParams(entry.getValue()), this.escapeNewLine);
                StringBuilders.appendKeyDqValue(sb, safeKey, safeValue);
            }
        }
    }
    
    private String escapeSDParams(final String value) {
        return Rfc5424Layout.PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("facility=").append(this.facility.name());
        sb.append(" appName=").append(this.appName);
        sb.append(" defaultId=").append(this.defaultId);
        sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
        sb.append(" newLine=").append(this.includeNewLine);
        sb.append(" includeMDC=").append(this.includeMdc);
        sb.append(" messageId=").append(this.messageId);
        return sb.toString();
    }
    
    @PluginFactory
    public static Rfc5424Layout createLayout(@PluginAttribute(value = "facility", defaultString = "LOCAL0") final Facility facility, @PluginAttribute("id") final String id, @PluginAttribute(value = "enterpriseNumber", defaultInt = 18060) final int enterpriseNumber, @PluginAttribute(value = "includeMDC", defaultBoolean = true) final boolean includeMDC, @PluginAttribute(value = "mdcId", defaultString = "mdc") final String mdcId, @PluginAttribute("mdcPrefix") final String mdcPrefix, @PluginAttribute("eventPrefix") final String eventPrefix, @PluginAttribute("newLine") final boolean newLine, @PluginAttribute("newLineEscape") final String escapeNL, @PluginAttribute("appName") final String appName, @PluginAttribute("messageId") final String msgId, @PluginAttribute("mdcExcludes") final String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") final String required, @PluginAttribute("exceptionPattern") final String exceptionPattern, @PluginAttribute("useTlsMessageFormat") final boolean useTlsMessageFormat, @PluginElement("LoggerFields") final LoggerFields[] loggerFields, @PluginConfiguration final Configuration config) {
        if (includes != null && excludes != null) {
            Rfc5424Layout.LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
            includes = null;
        }
        return new Rfc5424Layout(config, facility, id, enterpriseNumber, includeMDC, newLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, StandardCharsets.UTF_8, exceptionPattern, useTlsMessageFormat, loggerFields);
    }
    
    public Facility getFacility() {
        return this.facility;
    }
    
    static {
        NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
        PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
    }
    
    private class FieldFormatter
    {
        private final Map<String, List<PatternFormatter>> delegateMap;
        private final boolean discardIfEmpty;
        
        public FieldFormatter(final Map<String, List<PatternFormatter>> fieldMap, final boolean discardIfEmpty) {
            this.discardIfEmpty = discardIfEmpty;
            this.delegateMap = fieldMap;
        }
        
        public StructuredDataElement format(final LogEvent event) {
            final Map<String, String> map = new HashMap<String, String>(this.delegateMap.size());
            for (final Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet()) {
                final StringBuilder buffer = new StringBuilder();
                for (final PatternFormatter formatter : entry.getValue()) {
                    formatter.format(event, buffer);
                }
                map.put(entry.getKey(), buffer.toString());
            }
            return new StructuredDataElement(map, Rfc5424Layout.this.eventPrefix, this.discardIfEmpty);
        }
    }
    
    private class StructuredDataElement
    {
        private final Map<String, String> fields;
        private final boolean discardIfEmpty;
        private final String prefix;
        
        public StructuredDataElement(final Map<String, String> fields, final String prefix, final boolean discardIfEmpty) {
            this.discardIfEmpty = discardIfEmpty;
            this.fields = fields;
            this.prefix = prefix;
        }
        
        boolean discard() {
            if (!this.discardIfEmpty) {
                return false;
            }
            boolean foundNotEmptyValue = false;
            for (final Map.Entry<String, String> entry : this.fields.entrySet()) {
                if (Strings.isNotEmpty(entry.getValue())) {
                    foundNotEmptyValue = true;
                    break;
                }
            }
            return !foundNotEmptyValue;
        }
        
        void union(final Map<String, String> addFields) {
            this.fields.putAll(addFields);
        }
        
        Map<String, String> getFields() {
            return this.fields;
        }
        
        String getPrefix() {
            return this.prefix;
        }
    }
}
