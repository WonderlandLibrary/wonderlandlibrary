/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.csv.CSVFormat
 *  org.apache.commons.csv.QuoteMode
 */
package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;

public abstract class AbstractCsvLayout
extends AbstractStringLayout {
    protected static final String DEFAULT_CHARSET = "UTF-8";
    protected static final String DEFAULT_FORMAT = "Default";
    private static final String CONTENT_TYPE = "text/csv";
    private final CSVFormat format;

    protected static CSVFormat createFormat(String format, Character delimiter, Character escape, Character quote, QuoteMode quoteMode, String nullString, String recordSeparator) {
        CSVFormat csvFormat = CSVFormat.valueOf((String)format);
        if (AbstractCsvLayout.isNotNul(delimiter)) {
            csvFormat = csvFormat.withDelimiter(delimiter.charValue());
        }
        if (AbstractCsvLayout.isNotNul(escape)) {
            csvFormat = csvFormat.withEscape(escape);
        }
        if (AbstractCsvLayout.isNotNul(quote)) {
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

    private static boolean isNotNul(Character character) {
        return character != null && character.charValue() != '\u0000';
    }

    protected AbstractCsvLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
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

