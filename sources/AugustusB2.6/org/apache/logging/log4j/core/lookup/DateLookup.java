// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "date", category = "Lookup")
public class DateLookup implements StrLookup
{
    private static final Logger LOGGER;
    private static final Marker LOOKUP;
    
    @Override
    public String lookup(final String key) {
        return this.formatDate(System.currentTimeMillis(), key);
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        return this.formatDate(event.getTimeMillis(), key);
    }
    
    private String formatDate(final long date, final String format) {
        DateFormat dateFormat = null;
        if (format != null) {
            try {
                dateFormat = new SimpleDateFormat(format);
            }
            catch (Exception ex) {
                DateLookup.LOGGER.error(DateLookup.LOOKUP, "Invalid date format: [{}], using default", format, ex);
            }
        }
        if (dateFormat == null) {
            dateFormat = DateFormat.getInstance();
        }
        return dateFormat.format(new Date(date));
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LOOKUP = MarkerManager.getMarker("LOOKUP");
    }
}
