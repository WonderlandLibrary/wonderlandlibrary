// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.layout;

import java.io.Serializable;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.List;
import java.util.Objects;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

@Plugin(name = "Log4j1XmlLayout", category = "Core", elementType = "layout", printObject = true)
public final class Log4j1XmlLayout extends AbstractStringLayout
{
    private static final String EOL = "\r\n";
    private final boolean locationInfo;
    private final boolean properties;
    
    @PluginFactory
    public static Log4j1XmlLayout createLayout(@PluginAttribute("locationInfo") final boolean locationInfo, @PluginAttribute("properties") final boolean properties) {
        return new Log4j1XmlLayout(locationInfo, properties);
    }
    
    private Log4j1XmlLayout(final boolean locationInfo, final boolean properties) {
        super(StandardCharsets.UTF_8);
        this.locationInfo = locationInfo;
        this.properties = properties;
    }
    
    public boolean isLocationInfo() {
        return this.locationInfo;
    }
    
    public boolean isProperties() {
        return this.properties;
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        final StringBuilder text = AbstractStringLayout.getStringBuilder();
        this.formatTo(event, text);
        this.getStringBuilderEncoder().encode(text, destination);
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder text = AbstractStringLayout.getStringBuilder();
        this.formatTo(event, text);
        return text.toString();
    }
    
    private void formatTo(final LogEvent event, final StringBuilder buf) {
        buf.append("<log4j:event logger=\"");
        buf.append(Transform.escapeHtmlTags(event.getLoggerName()));
        buf.append("\" timestamp=\"");
        buf.append(event.getTimeMillis());
        buf.append("\" level=\"");
        buf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
        buf.append("\" thread=\"");
        buf.append(Transform.escapeHtmlTags(event.getThreadName()));
        buf.append("\">");
        buf.append("\r\n");
        buf.append("<log4j:message><![CDATA[");
        Transform.appendEscapingCData(buf, event.getMessage().getFormattedMessage());
        buf.append("]]></log4j:message>");
        buf.append("\r\n");
        final List<String> ndc = event.getContextStack().asList();
        if (!ndc.isEmpty()) {
            buf.append("<log4j:NDC><![CDATA[");
            Transform.appendEscapingCData(buf, Strings.join(ndc, ' '));
            buf.append("]]></log4j:NDC>");
            buf.append("\r\n");
        }
        final Throwable thrown = event.getThrown();
        if (thrown != null) {
            buf.append("<log4j:throwable><![CDATA[");
            final StringWriter w = new StringWriter();
            thrown.printStackTrace(new PrintWriter(w));
            Transform.appendEscapingCData(buf, w.toString());
            buf.append("]]></log4j:throwable>");
            buf.append("\r\n");
        }
        if (this.locationInfo) {
            final StackTraceElement source = event.getSource();
            if (source != null) {
                buf.append("<log4j:locationInfo class=\"");
                buf.append(Transform.escapeHtmlTags(source.getClassName()));
                buf.append("\" method=\"");
                buf.append(Transform.escapeHtmlTags(source.getMethodName()));
                buf.append("\" file=\"");
                buf.append(Transform.escapeHtmlTags(source.getFileName()));
                buf.append("\" line=\"");
                buf.append(source.getLineNumber());
                buf.append("\"/>");
                buf.append("\r\n");
            }
        }
        if (this.properties) {
            final ReadOnlyStringMap contextMap = event.getContextData();
            if (!contextMap.isEmpty()) {
                buf.append("<log4j:properties>\r\n");
                contextMap.forEach((key, val) -> {
                    if (val != null) {
                        buf.append("<log4j:data name=\"");
                        buf.append(Transform.escapeHtmlTags(key));
                        buf.append("\" value=\"");
                        buf.append(Transform.escapeHtmlTags(Objects.toString(val, null)));
                        buf.append("\"/>");
                        buf.append("\r\n");
                    }
                    return;
                });
                buf.append("</log4j:properties>");
                buf.append("\r\n");
            }
        }
        buf.append("</log4j:event>");
        buf.append("\r\n");
        buf.append("\r\n");
    }
}
