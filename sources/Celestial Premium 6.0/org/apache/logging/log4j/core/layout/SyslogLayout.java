/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Priority;

@Plugin(name="SyslogLayout", category="Core", elementType="layout", printObject=true)
public class SyslogLayout
extends AbstractStringLayout {
    public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    private final Facility facility;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
    private final String localHostname = this.getLocalHostname();

    protected SyslogLayout(Facility facility, boolean includeNL, String escapeNL, Charset charset) {
        super(charset);
        this.facility = facility;
        this.includeNewLine = includeNL;
        this.escapeNewLine = escapeNL == null ? null : Matcher.quoteReplacement(escapeNL);
    }

    @Override
    public String toSerializable(LogEvent event) {
        StringBuilder buf = new StringBuilder();
        buf.append("<");
        buf.append(Priority.getPriority(this.facility, event.getLevel()));
        buf.append(">");
        this.addDate(event.getMillis(), buf);
        buf.append(" ");
        buf.append(this.localHostname);
        buf.append(" ");
        String message = event.getMessage().getFormattedMessage();
        if (null != this.escapeNewLine) {
            message = NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
        }
        buf.append(message);
        if (this.includeNewLine) {
            buf.append("\n");
        }
        return buf.toString();
    }

    private String getLocalHostname() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException uhe) {
            LOGGER.error("Could not determine local host name", (Throwable)uhe);
            return "UNKNOWN_LOCALHOST";
        }
    }

    private synchronized void addDate(long timestamp, StringBuilder buf) {
        int index = buf.length() + 4;
        buf.append(this.dateFormat.format(new Date(timestamp)));
        if (buf.charAt(index) == '0') {
            buf.setCharAt(index, ' ');
        }
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("structured", "false");
        result.put("formatType", "logfilepatternreceiver");
        result.put("dateFormat", this.dateFormat.toPattern());
        result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
        return result;
    }

    @PluginFactory
    public static SyslogLayout createLayout(@PluginAttribute(value="facility") String facility, @PluginAttribute(value="newLine") String includeNL, @PluginAttribute(value="newLineEscape") String escapeNL, @PluginAttribute(value="charset") String charsetName) {
        Charset charset = Charsets.getSupportedCharset(charsetName);
        boolean includeNewLine = Boolean.parseBoolean(includeNL);
        Facility f = Facility.toFacility(facility, Facility.LOCAL0);
        return new SyslogLayout(f, includeNewLine, escapeNL, charset);
    }
}

