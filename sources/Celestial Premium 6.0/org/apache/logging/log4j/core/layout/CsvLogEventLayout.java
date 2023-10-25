/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.csv.CSVFormat
 *  org.apache.commons.csv.QuoteMode
 */
package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractCsvLayout;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="CsvLogEventLayout", category="Core", elementType="layout", printObject=true)
public class CsvLogEventLayout
extends AbstractCsvLayout {
    public static CsvLogEventLayout createDefaultLayout() {
        return new CsvLogEventLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf((String)"Default"), null, null);
    }

    public static CsvLogEventLayout createLayout(CSVFormat format) {
        return new CsvLogEventLayout(null, Charset.forName("UTF-8"), format, null, null);
    }

    @PluginFactory
    public static CsvLogEventLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value="format", defaultString="Default") String format, @PluginAttribute(value="delimiter") Character delimiter, @PluginAttribute(value="escape") Character escape, @PluginAttribute(value="quote") Character quote, @PluginAttribute(value="quoteMode") QuoteMode quoteMode, @PluginAttribute(value="nullString") String nullString, @PluginAttribute(value="recordSeparator") String recordSeparator, @PluginAttribute(value="charset", defaultString="UTF-8") Charset charset, @PluginAttribute(value="header") String header, @PluginAttribute(value="footer") String footer) {
        CSVFormat csvFormat = CsvLogEventLayout.createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
        return new CsvLogEventLayout(config, charset, csvFormat, header, footer);
    }

    protected CsvLogEventLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
        super(config, charset, csvFormat, header, footer);
    }

    @Override
    public String toSerializable(LogEvent event) {
        StringBuilder buffer = CsvLogEventLayout.getStringBuilder();
        CSVFormat format = this.getFormat();
        try {
            format.print((Object)event.getNanoTime(), (Appendable)buffer, true);
            format.print((Object)event.getTimeMillis(), (Appendable)buffer, false);
            format.print((Object)event.getLevel(), (Appendable)buffer, false);
            format.print((Object)event.getThreadId(), (Appendable)buffer, false);
            format.print((Object)event.getThreadName(), (Appendable)buffer, false);
            format.print((Object)event.getThreadPriority(), (Appendable)buffer, false);
            format.print((Object)event.getMessage().getFormattedMessage(), (Appendable)buffer, false);
            format.print((Object)event.getLoggerFqcn(), (Appendable)buffer, false);
            format.print((Object)event.getLoggerName(), (Appendable)buffer, false);
            format.print((Object)event.getMarker(), (Appendable)buffer, false);
            format.print((Object)event.getThrownProxy(), (Appendable)buffer, false);
            format.print((Object)event.getSource(), (Appendable)buffer, false);
            format.print((Object)event.getContextData(), (Appendable)buffer, false);
            format.print((Object)event.getContextStack(), (Appendable)buffer, false);
            format.println((Appendable)buffer);
            return buffer.toString();
        }
        catch (IOException e) {
            StatusLogger.getLogger().error(event.toString(), (Throwable)e);
            return format.getCommentMarker() + " " + e;
        }
    }
}

