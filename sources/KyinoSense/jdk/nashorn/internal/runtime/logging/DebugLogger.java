/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.logging;

import java.io.PrintWriter;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;

public final class DebugLogger {
    public static final DebugLogger DISABLED_LOGGER = new DebugLogger("disabled", Level.OFF, false);
    private final Logger logger;
    private final boolean isEnabled;
    private int indent;
    private static final int INDENT_SPACE = 4;
    private final boolean isQuiet;

    public DebugLogger(String loggerName, Level loggerLevel, boolean isQuiet) {
        this.logger = DebugLogger.instantiateLogger(loggerName, loggerLevel);
        this.isQuiet = isQuiet;
        assert (this.logger != null);
        this.isEnabled = this.getLevel() != Level.OFF;
    }

    private static Logger instantiateLogger(String name, final Level level) {
        final Logger logger = Logger.getLogger(name);
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                for (Handler h : logger.getHandlers()) {
                    logger.removeHandler(h);
                }
                logger.setLevel(level);
                logger.setUseParentHandlers(false);
                ConsoleHandler c = new ConsoleHandler();
                c.setFormatter(new Formatter(){

                    @Override
                    public String format(LogRecord record) {
                        StringBuilder sb = new StringBuilder();
                        sb.append('[').append(record.getLoggerName()).append("] ").append(record.getMessage()).append('\n');
                        return sb.toString();
                    }
                });
                logger.addHandler(c);
                c.setLevel(level);
                return null;
            }
        }, DebugLogger.createLoggerControlAccCtxt());
        return logger;
    }

    public Level getLevel() {
        return this.logger.getLevel() == null ? Level.OFF : this.logger.getLevel();
    }

    public PrintWriter getOutputStream() {
        return Context.getCurrentErr();
    }

    public static String quote(String str) {
        if (str.isEmpty()) {
            return "''";
        }
        char startQuote = '\u0000';
        char endQuote = '\u0000';
        char quote = '\u0000';
        if (str.startsWith("\\") || str.startsWith("\"")) {
            startQuote = str.charAt(0);
        }
        if (str.endsWith("\\") || str.endsWith("\"")) {
            endQuote = str.charAt(str.length() - 1);
        }
        if (startQuote == '\u0000' || endQuote == '\u0000') {
            char c = quote = startQuote == '\u0000' ? endQuote : startQuote;
        }
        if (quote == '\u0000') {
            quote = '\'';
        }
        return (startQuote == '\u0000' ? quote : startQuote) + str + (endQuote == '\u0000' ? quote : endQuote);
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public static boolean isEnabled(DebugLogger logger) {
        return logger != null && logger.isEnabled();
    }

    public void indent(int pos) {
        if (this.isEnabled) {
            this.indent += pos * 4;
        }
    }

    public void indent() {
        this.indent += 4;
    }

    public void unindent() {
        this.indent -= 4;
        if (this.indent < 0) {
            this.indent = 0;
        }
    }

    private static void logEvent(RuntimeEvent<?> event) {
        Global global;
        if (event != null && (global = Context.getGlobal()).has("Debug")) {
            ScriptObject debug = (ScriptObject)global.get("Debug");
            ScriptFunction addRuntimeEvent = (ScriptFunction)debug.get("addRuntimeEvent");
            ScriptRuntime.apply(addRuntimeEvent, debug, event);
        }
    }

    public boolean isLoggable(Level level) {
        return this.logger.isLoggable(level);
    }

    public void finest(String str) {
        this.log(Level.FINEST, str);
    }

    public void finest(RuntimeEvent<?> event, String str) {
        this.finest(str);
        DebugLogger.logEvent(event);
    }

    public void finest(Object ... objs) {
        this.log(Level.FINEST, objs);
    }

    public void finest(RuntimeEvent<?> event, Object ... objs) {
        this.finest(objs);
        DebugLogger.logEvent(event);
    }

    public void finer(String str) {
        this.log(Level.FINER, str);
    }

    public void finer(RuntimeEvent<?> event, String str) {
        this.finer(str);
        DebugLogger.logEvent(event);
    }

    public void finer(Object ... objs) {
        this.log(Level.FINER, objs);
    }

    public void finer(RuntimeEvent<?> event, Object ... objs) {
        this.finer(objs);
        DebugLogger.logEvent(event);
    }

    public void fine(String str) {
        this.log(Level.FINE, str);
    }

    public void fine(RuntimeEvent<?> event, String str) {
        this.fine(str);
        DebugLogger.logEvent(event);
    }

    public void fine(Object ... objs) {
        this.log(Level.FINE, objs);
    }

    public void fine(RuntimeEvent<?> event, Object ... objs) {
        this.fine(objs);
        DebugLogger.logEvent(event);
    }

    public void config(String str) {
        this.log(Level.CONFIG, str);
    }

    public void config(RuntimeEvent<?> event, String str) {
        this.config(str);
        DebugLogger.logEvent(event);
    }

    public void config(Object ... objs) {
        this.log(Level.CONFIG, objs);
    }

    public void config(RuntimeEvent<?> event, Object ... objs) {
        this.config(objs);
        DebugLogger.logEvent(event);
    }

    public void info(String str) {
        this.log(Level.INFO, str);
    }

    public void info(RuntimeEvent<?> event, String str) {
        this.info(str);
        DebugLogger.logEvent(event);
    }

    public void info(Object ... objs) {
        this.log(Level.INFO, objs);
    }

    public void info(RuntimeEvent<?> event, Object ... objs) {
        this.info(objs);
        DebugLogger.logEvent(event);
    }

    public void warning(String str) {
        this.log(Level.WARNING, str);
    }

    public void warning(RuntimeEvent<?> event, String str) {
        this.warning(str);
        DebugLogger.logEvent(event);
    }

    public void warning(Object ... objs) {
        this.log(Level.WARNING, objs);
    }

    public void warning(RuntimeEvent<?> event, Object ... objs) {
        this.warning(objs);
        DebugLogger.logEvent(event);
    }

    public void severe(String str) {
        this.log(Level.SEVERE, str);
    }

    public void severe(RuntimeEvent<?> event, String str) {
        this.severe(str);
        DebugLogger.logEvent(event);
    }

    public void severe(Object ... objs) {
        this.log(Level.SEVERE, objs);
    }

    public void severe(RuntimeEvent<?> event, Object ... objs) {
        this.severe(objs);
        DebugLogger.logEvent(event);
    }

    public void log(Level level, String str) {
        if (this.isEnabled && !this.isQuiet && this.logger.isLoggable(level)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.indent; ++i) {
                sb.append(' ');
            }
            sb.append(str);
            this.logger.log(level, sb.toString());
        }
    }

    public void log(Level level, Object ... objs) {
        if (this.isEnabled && !this.isQuiet && this.logger.isLoggable(level)) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : objs) {
                sb.append(obj);
            }
            this.log(level, sb.toString());
        }
    }

    private static AccessControlContext createLoggerControlAccCtxt() {
        Permissions perms = new Permissions();
        perms.add(new LoggingPermission("control", null));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }
}

