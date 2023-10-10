// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.csv.CSVFormat;

public abstract class AbstractCsvLayout extends AbstractStringLayout
{
    protected static final String DEFAULT_CHARSET = "UTF-8";
    protected static final String DEFAULT_FORMAT = "Default";
    private static final String CONTENT_TYPE = "text/csv";
    private final CSVFormat format;
    
    protected static CSVFormat createFormat(final String format, final Character delimiter, final Character escape, final Character quote, final QuoteMode quoteMode, final String nullString, final String recordSeparator) {
        CSVFormat csvFormat = CSVFormat.valueOf(format);
        if (isNotNul(delimiter)) {
            csvFormat = csvFormat.withDelimiter((char)delimiter);
        }
        if (isNotNul(escape)) {
            csvFormat = csvFormat.withEscape(escape);
        }
        if (isNotNul(quote)) {
            csvFormat = csvFormat.withQuote(quote);
        }
        if (quoteMode != null) {
            csvFormat = csvFormat.withQuoteMode(quoteMode);
        }
        if (nullString != null) {
            csvFormat = csvFormat.withNullString(nullString);
        }
        if (recordSeparator != null) {
            csvFormat = csvFormat.withRecordSeparator(recordSeparator);
        }
        return csvFormat;
    }
    
    private static boolean isNotNul(final Character character) {
        return character != null && character != '\0';
    }
    
    protected AbstractCsvLayout(final Configuration config, final Charset charset, final CSVFormat csvFormat, final String header, final String footer) {
        super(config, charset, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(header).build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footer).build());
        this.format = csvFormat;
    }
    
    @Override
    public String getContentType() {
        return "text/csv; charset=" + this.getCharset();
    }
    
    public CSVFormat getFormat() {
        return this.format;
    }
}
