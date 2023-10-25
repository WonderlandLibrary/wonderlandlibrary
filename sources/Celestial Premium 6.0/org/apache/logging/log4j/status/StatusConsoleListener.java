/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.status;

import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.util.PropertiesUtil;

public class StatusConsoleListener
implements StatusListener {
    private static final String STATUS_LEVEL = "org.apache.logging.log4j.StatusLevel";
    private Level level = Level.FATAL;
    private String[] filters = null;
    private final PrintStream stream;

    public StatusConsoleListener() {
        String str = PropertiesUtil.getProperties().getStringProperty(STATUS_LEVEL);
        if (str != null) {
            this.level = Level.toLevel(str, Level.FATAL);
        }
        this.stream = System.out;
    }

    public StatusConsoleListener(Level level) {
        this.level = level;
        this.stream = System.out;
    }

    public StatusConsoleListener(Level level, PrintStream stream) {
        this.level = level;
        this.stream = stream;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public Level getStatusLevel() {
        return this.level;
    }

    @Override
    public void log(StatusData data) {
        if (!this.filtered(data)) {
            this.stream.println(data.getFormattedStatus());
        }
    }

    public void setFilters(String ... filters) {
        this.filters = filters;
    }

    private boolean filtered(StatusData data) {
        if (this.filters == null) {
            return false;
        }
        String caller = data.getStackTraceElement().getClassName();
        for (String filter : this.filters) {
            if (!caller.startsWith(filter)) continue;
            return true;
        }
        return false;
    }
}

