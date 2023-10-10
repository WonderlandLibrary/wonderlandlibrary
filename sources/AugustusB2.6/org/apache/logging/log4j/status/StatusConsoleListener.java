// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.status;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;

public class StatusConsoleListener implements StatusListener
{
    private Level level;
    private String[] filters;
    private final PrintStream stream;
    
    public StatusConsoleListener(final Level level) {
        this(level, System.out);
    }
    
    public StatusConsoleListener(final Level level, final PrintStream stream) {
        this.level = Level.FATAL;
        if (stream == null) {
            throw new IllegalArgumentException("You must provide a stream to use for this listener.");
        }
        this.level = level;
        this.stream = stream;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    @Override
    public Level getStatusLevel() {
        return this.level;
    }
    
    @Override
    public void log(final StatusData data) {
        if (!this.filtered(data)) {
            this.stream.println(data.getFormattedStatus());
        }
    }
    
    public void setFilters(final String... filters) {
        this.filters = filters;
    }
    
    private boolean filtered(final StatusData data) {
        if (this.filters == null) {
            return false;
        }
        final String caller = data.getStackTraceElement().getClassName();
        for (final String filter : this.filters) {
            if (caller.startsWith(filter)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void close() throws IOException {
        if (this.stream != System.out && this.stream != System.err) {
            this.stream.close();
        }
    }
}
