package viamcp.utils;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JLoggerToLog4j extends Logger {

    public JLoggerToLog4j(org.apache.logging.log4j.Logger logger) {
        super("logger", null);
    }

    public void log(LogRecord record) {
    }

    public void log(Level level, String msg) {
    }

    public void log(Level level, String msg, Object param1) {
    }

    public void log(Level level, String msg, Object[] params) {
    }

    public void log(Level level, String msg, Throwable params) {
    }
}
