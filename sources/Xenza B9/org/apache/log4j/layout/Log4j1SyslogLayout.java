// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.layout;

import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.util.NetUtils;
import org.apache.logging.log4j.Logger;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

@Plugin(name = "Log4j1SyslogLayout", category = "Core", elementType = "layout", printObject = true)
public final class Log4j1SyslogLayout extends AbstractStringLayout
{
    private static final String localHostname;
    private final Facility facility;
    private final boolean facilityPrinting;
    private final boolean header;
    private final StringLayout messageLayout;
    private static final String[] dateFormatOptions;
    private final LogEventPatternConverter dateConverter;
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private Log4j1SyslogLayout(final Facility facility, final boolean facilityPrinting, final boolean header, final StringLayout messageLayout, final Charset charset) {
        super(charset);
        this.dateConverter = DatePatternConverter.newInstance(Log4j1SyslogLayout.dateFormatOptions);
        this.facility = facility;
        this.facilityPrinting = facilityPrinting;
        this.header = header;
        this.messageLayout = messageLayout;
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final String message = (this.messageLayout != null) ? this.messageLayout.toSerializable(event) : event.getMessage().getFormattedMessage();
        final StringBuilder buf = AbstractStringLayout.getStringBuilder();
        buf.append('<');
        buf.append(Priority.getPriority(this.facility, event.getLevel()));
        buf.append('>');
        if (this.header) {
            final int index = buf.length() + 4;
            this.dateConverter.format(event, buf);
            if (buf.charAt(index) == '0') {
                buf.setCharAt(index, ' ');
            }
            buf.append(' ');
            buf.append(Log4j1SyslogLayout.localHostname);
            buf.append(' ');
        }
        if (this.facilityPrinting) {
            buf.append((this.facility != null) ? this.facility.name().toLowerCase() : "user").append(':');
        }
        buf.append(message);
        return buf.toString();
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "logfilepatternreceiver");
        result.put("dateFormat", Log4j1SyslogLayout.dateFormatOptions[0]);
        if (this.header) {
            result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
        }
        else {
            result.put("format", "<LEVEL>MESSAGE");
        }
        return result;
    }
    
    static {
        localHostname = NetUtils.getLocalHostname();
        dateFormatOptions = new String[] { "MMM dd HH:mm:ss", null, "en" };
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<Log4j1SyslogLayout>
    {
        @PluginBuilderAttribute
        private Facility facility;
        @PluginBuilderAttribute
        private boolean facilityPrinting;
        @PluginBuilderAttribute
        private boolean header;
        @PluginElement("Layout")
        private Layout<? extends Serializable> messageLayout;
        
        public Builder() {
            this.facility = Facility.USER;
            this.setCharset(StandardCharsets.UTF_8);
        }
        
        @Override
        public Log4j1SyslogLayout build() {
            if (!this.isValid()) {
                return null;
            }
            if (this.messageLayout != null && !(this.messageLayout instanceof StringLayout)) {
                Log4j1SyslogLayout.LOGGER.error("Log4j1SyslogLayout: the message layout must be a StringLayout.");
                return null;
            }
            return new Log4j1SyslogLayout(this.facility, this.facilityPrinting, this.header, (StringLayout)this.messageLayout, this.getCharset(), null);
        }
        
        public Facility getFacility() {
            return this.facility;
        }
        
        public boolean isFacilityPrinting() {
            return this.facilityPrinting;
        }
        
        public boolean isHeader() {
            return this.header;
        }
        
        public Layout<? extends Serializable> getMessageLayout() {
            return this.messageLayout;
        }
        
        public B setFacility(final Facility facility) {
            this.facility = facility;
            return this.asBuilder();
        }
        
        public B setFacilityPrinting(final boolean facilityPrinting) {
            this.facilityPrinting = facilityPrinting;
            return this.asBuilder();
        }
        
        public B setHeader(final boolean header) {
            this.header = header;
            return this.asBuilder();
        }
        
        public B setMessageLayout(final Layout<? extends Serializable> messageLayout) {
            this.messageLayout = messageLayout;
            return this.asBuilder();
        }
    }
}
