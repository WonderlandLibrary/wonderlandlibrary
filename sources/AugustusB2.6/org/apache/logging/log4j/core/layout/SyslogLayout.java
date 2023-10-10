// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.core.LogEvent;
import java.util.regex.Matcher;
import org.apache.logging.log4j.core.util.NetUtils;
import java.util.Locale;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.core.net.Facility;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SyslogLayout", category = "Core", elementType = "layout", printObject = true)
public final class SyslogLayout extends AbstractStringLayout
{
    public static final Pattern NEWLINE_PATTERN;
    private final Facility facility;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final SimpleDateFormat dateFormat;
    private final String localHostname;
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    protected SyslogLayout(final Facility facility, final boolean includeNL, final String escapeNL, final Charset charset) {
        super(charset);
        this.dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);
        this.localHostname = NetUtils.getLocalHostname();
        this.facility = facility;
        this.includeNewLine = includeNL;
        this.escapeNewLine = ((escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL));
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buf = AbstractStringLayout.getStringBuilder();
        buf.append('<');
        buf.append(Priority.getPriority(this.facility, event.getLevel()));
        buf.append('>');
        this.addDate(event.getTimeMillis(), buf);
        buf.append(' ');
        buf.append(this.localHostname);
        buf.append(' ');
        String message = event.getMessage().getFormattedMessage();
        if (null != this.escapeNewLine) {
            message = SyslogLayout.NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
        }
        buf.append(message);
        if (this.includeNewLine) {
            buf.append('\n');
        }
        return buf.toString();
    }
    
    private synchronized void addDate(final long timestamp, final StringBuilder buf) {
        final int index = buf.length() + 4;
        buf.append(this.dateFormat.format(new Date(timestamp)));
        if (buf.charAt(index) == '0') {
            buf.setCharAt(index, ' ');
        }
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "logfilepatternreceiver");
        result.put("dateFormat", this.dateFormat.toPattern());
        result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
        return result;
    }
    
    @Deprecated
    public static SyslogLayout createLayout(final Facility facility, final boolean includeNewLine, final String escapeNL, final Charset charset) {
        return new SyslogLayout(facility, includeNewLine, escapeNL, charset);
    }
    
    public Facility getFacility() {
        return this.facility;
    }
    
    static {
        NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<SyslogLayout>
    {
        @PluginBuilderAttribute
        private Facility facility;
        @PluginBuilderAttribute("newLine")
        private boolean includeNewLine;
        @PluginBuilderAttribute("newLineEscape")
        private String escapeNL;
        
        public Builder() {
            this.facility = Facility.LOCAL0;
            this.setCharset(StandardCharsets.UTF_8);
        }
        
        @Override
        public SyslogLayout build() {
            return new SyslogLayout(this.facility, this.includeNewLine, this.escapeNL, this.getCharset());
        }
        
        public Facility getFacility() {
            return this.facility;
        }
        
        public boolean isIncludeNewLine() {
            return this.includeNewLine;
        }
        
        public String getEscapeNL() {
            return this.escapeNL;
        }
        
        public B setFacility(final Facility facility) {
            this.facility = facility;
            return this.asBuilder();
        }
        
        public B setIncludeNewLine(final boolean includeNewLine) {
            this.includeNewLine = includeNewLine;
            return this.asBuilder();
        }
        
        public B setEscapeNL(final String escapeNL) {
            this.escapeNL = escapeNL;
            return this.asBuilder();
        }
    }
}
