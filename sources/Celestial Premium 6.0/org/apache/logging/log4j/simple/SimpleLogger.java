/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.simple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SimpleLogger
extends AbstractLogger {
    private static final char SPACE = ' ';
    private DateFormat dateFormatter;
    private Level level;
    private final boolean showDateTime;
    private final boolean showContextMap;
    private PrintStream stream;
    private final String logName;

    public SimpleLogger(String name, Level defaultLevel, boolean showLogName, boolean showShortLogName, boolean showDateTime, boolean showContextMap, String dateTimeFormat, MessageFactory messageFactory, PropertiesUtil props, PrintStream stream) {
        super(name, messageFactory);
        int index;
        String lvl = props.getStringProperty("org.apache.logging.log4j.simplelog." + name + ".level");
        this.level = Level.toLevel(lvl, defaultLevel);
        this.logName = showShortLogName ? ((index = name.lastIndexOf(".")) > 0 && index < name.length() ? name.substring(index + 1) : name) : (showLogName ? name : null);
        this.showDateTime = showDateTime;
        this.showContextMap = showContextMap;
        this.stream = stream;
        if (showDateTime) {
            try {
                this.dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
            catch (IllegalArgumentException e) {
                this.dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
            }
        }
    }

    public void setStream(PrintStream stream) {
        this.stream = stream;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        if (level != null) {
            this.level = level;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void log(Marker marker, String fqcn, Level level, Message msg, Throwable throwable) {
        Map<String, String> mdc;
        StringBuilder sb = new StringBuilder();
        if (this.showDateTime) {
            String dateText;
            Date now = new Date();
            DateFormat dateFormat = this.dateFormatter;
            synchronized (dateFormat) {
                dateText = this.dateFormatter.format(now);
            }
            sb.append(dateText);
            sb.append(' ');
        }
        sb.append(level.toString());
        sb.append(' ');
        if (this.logName != null && this.logName.length() > 0) {
            sb.append(this.logName);
            sb.append(' ');
        }
        sb.append(msg.getFormattedMessage());
        if (this.showContextMap && (mdc = ThreadContext.getContext()).size() > 0) {
            sb.append(' ');
            sb.append(mdc.toString());
            sb.append(' ');
        }
        Object[] params = msg.getParameters();
        Throwable t = throwable == null && params != null && params[params.length - 1] instanceof Throwable ? (Throwable)params[params.length - 1] : throwable;
        if (t != null) {
            sb.append(' ');
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            sb.append(baos.toString());
        }
        this.stream.println(sb.toString());
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String msg) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String msg, Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String msg, Object ... p1) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Object msg, Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Message msg, Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }
}

