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
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="CsvParameterLayout", category="Core", elementType="layout", printObject=true)
public class CsvParameterLayout
extends AbstractCsvLayout {
    public static AbstractCsvLayout createDefaultLayout() {
        return new CsvParameterLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf((String)"Default"), null, null);
    }

    public static AbstractCsvLayout createLayout(CSVFormat format) {
        return new CsvParameterLayout(null, Charset.forName("UTF-8"), format, null, null);
    }

    @PluginFactory
    public static AbstractCsvLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value="format", defaultString="Default") String format, @PluginAttribute(value="delimiter") Character delimiter, @PluginAttribute(value="escape") Character escape, @PluginAttribute(value="quote") Character quote, @PluginAttribute(value="quoteMode") QuoteMode quoteMode, @PluginAttribute(value="nullString") String nullString, @PluginAttribute(value="recordSeparator") String recordSeparator, @PluginAttribute(value="charset", defaultString="UTF-8") Charset charset, @PluginAttribute(value="header") String header, @PluginAttribute(value="footer") String footer) {
        CSVFormat csvFormat = CsvParameterLayout.createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
        return new CsvParameterLayout(config, charset, csvFormat, header, footer);
    }

    public CsvParameterLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
        super(config, charset, csvFormat, header, footer);
    }

    @Override
    public String toSerializable(LogEvent event) {
        Message message = event.getMessage();
        Object[] parameters = message.getParameters();
        StringBuilder buffer = CsvParameterLayout.getStringBuilder();
        try {
            this.getFormat().printRecord((Appendable)buffer, parameters);
            return buffer.toString();
        }
        catch (IOException e) {
            StatusLogger.getLogger().error(message, (Throwable)e);
            return this.getFormat().getCommentMarker() + " " + e;
        }
    }
}

