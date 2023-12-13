// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import java.util.Collection;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;
import java.io.InputStream;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Properties;
import org.apache.log4j.Priority;
import org.apache.logging.log4j.spi.StandardLevel;
import org.apache.log4j.Level;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Logger;

public class OptionConverter
{
    static String DELIM_START;
    static char DELIM_STOP;
    static int DELIM_START_LEN;
    static int DELIM_STOP_LEN;
    private static final Logger LOGGER;
    static final int MAX_CUTOFF_LEVEL;
    static final int MIN_CUTOFF_LEVEL;
    static final ConcurrentMap<String, Level> LEVELS;
    private static final String LOG4J2_LEVEL_CLASS;
    private static final CharMap[] charMap;
    
    public static String[] concatanateArrays(final String[] l, final String[] r) {
        final int len = l.length + r.length;
        final String[] a = new String[len];
        System.arraycopy(l, 0, a, 0, l.length);
        System.arraycopy(r, 0, a, l.length, r.length);
        return a;
    }
    
    static int toLog4j2Level(final int v1Level) {
        if (v1Level >= OptionConverter.MAX_CUTOFF_LEVEL) {
            return StandardLevel.OFF.intLevel();
        }
        if (v1Level > 10000) {
            final int offset = Math.round((v1Level - 10000) / 100.0f);
            return StandardLevel.DEBUG.intLevel() - offset;
        }
        if (v1Level > 5000) {
            final int offset = Math.round((v1Level - 5000) / 50.0f);
            return StandardLevel.TRACE.intLevel() - offset;
        }
        if (v1Level > OptionConverter.MIN_CUTOFF_LEVEL) {
            final int offset = 5000 - v1Level;
            return StandardLevel.TRACE.intLevel() + offset;
        }
        return StandardLevel.ALL.intLevel();
    }
    
    static int toLog4j1Level(final int v2Level) {
        if (v2Level == StandardLevel.ALL.intLevel()) {
            return Integer.MIN_VALUE;
        }
        if (v2Level > StandardLevel.TRACE.intLevel()) {
            return OptionConverter.MIN_CUTOFF_LEVEL + (StandardLevel.ALL.intLevel() - v2Level);
        }
        if (v2Level > StandardLevel.DEBUG.intLevel()) {
            return 5000 + 50 * (StandardLevel.TRACE.intLevel() - v2Level);
        }
        if (v2Level > StandardLevel.OFF.intLevel()) {
            return 10000 + 100 * (StandardLevel.DEBUG.intLevel() - v2Level);
        }
        return Integer.MAX_VALUE;
    }
    
    static int toSyslogLevel(final int v2Level) {
        if (v2Level <= StandardLevel.FATAL.intLevel()) {
            return 0;
        }
        if (v2Level <= StandardLevel.ERROR.intLevel()) {
            return 3 - 3 * (StandardLevel.ERROR.intLevel() - v2Level) / (StandardLevel.ERROR.intLevel() - StandardLevel.FATAL.intLevel());
        }
        if (v2Level <= StandardLevel.WARN.intLevel()) {
            return 4;
        }
        if (v2Level <= StandardLevel.INFO.intLevel()) {
            return 6 - 2 * (StandardLevel.INFO.intLevel() - v2Level) / (StandardLevel.INFO.intLevel() - StandardLevel.WARN.intLevel());
        }
        return 7;
    }
    
    public static org.apache.logging.log4j.Level createLevel(final Priority level) {
        final String name = level.toString().toUpperCase() + "#" + level.getClass().getName();
        return org.apache.logging.log4j.Level.forName(name, toLog4j2Level(level.toInt()));
    }
    
    public static org.apache.logging.log4j.Level convertLevel(final Priority level) {
        return (level != null) ? level.getVersion2Level() : org.apache.logging.log4j.Level.ERROR;
    }
    
    public static Level convertLevel(final org.apache.logging.log4j.Level level) {
        Level actualLevel = toLevel(level.name(), null);
        if (actualLevel == null) {
            actualLevel = toLevel(OptionConverter.LOG4J2_LEVEL_CLASS, level.name(), null);
        }
        return (actualLevel != null) ? actualLevel : Level.ERROR;
    }
    
    public static org.apache.logging.log4j.Level convertLevel(final String level, final org.apache.logging.log4j.Level defaultLevel) {
        final Level actualLevel = toLevel(level, null);
        return (actualLevel != null) ? actualLevel.getVersion2Level() : defaultLevel;
    }
    
    public static String convertSpecialChars(final String s) {
        final int len = s.length();
        final StringBuilder sbuf = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char c = s.charAt(i++);
            if (c == '\\') {
                c = s.charAt(i++);
                for (final CharMap entry : OptionConverter.charMap) {
                    if (entry.key == c) {
                        c = entry.replacement;
                    }
                }
            }
            sbuf.append(c);
        }
        return sbuf.toString();
    }
    
    public static String findAndSubst(final String key, final Properties props) {
        final String value = props.getProperty(key);
        if (value == null) {
            return null;
        }
        try {
            return substVars(value, props);
        }
        catch (final IllegalArgumentException e) {
            OptionConverter.LOGGER.error("Bad option value [{}].", value, e);
            return value;
        }
    }
    
    public static String getSystemProperty(final String key, final String def) {
        try {
            return System.getProperty(key, def);
        }
        catch (final Throwable e) {
            OptionConverter.LOGGER.debug("Was not allowed to read system property \"{}\".", key);
            return def;
        }
    }
    
    public static Object instantiateByClassName(final String className, final Class<?> superClass, final Object defaultValue) {
        if (className != null) {
            try {
                final Object obj = LoaderUtil.newInstanceOf(className);
                if (!superClass.isAssignableFrom(obj.getClass())) {
                    OptionConverter.LOGGER.error("A \"{}\" object is not assignable to a \"{}\" variable", className, superClass.getName());
                    return defaultValue;
                }
                return obj;
            }
            catch (final ReflectiveOperationException e) {
                OptionConverter.LOGGER.error("Could not instantiate class [" + className + "].", e);
            }
        }
        return defaultValue;
    }
    
    public static Object instantiateByKey(final Properties props, final String key, final Class superClass, final Object defaultValue) {
        final String className = findAndSubst(key, props);
        if (className == null) {
            LogLog.error("Could not find value for key " + key);
            return defaultValue;
        }
        return instantiateByClassName(className.trim(), superClass, defaultValue);
    }
    
    public static void selectAndConfigure(final InputStream inputStream, final String clazz, final LoggerRepository hierarchy) {
        Configurator configurator = null;
        if (clazz != null) {
            OptionConverter.LOGGER.debug("Preferred configurator class: " + clazz);
            configurator = (Configurator)instantiateByClassName(clazz, Configurator.class, null);
            if (configurator == null) {
                OptionConverter.LOGGER.error("Could not instantiate configurator [" + clazz + "].");
                return;
            }
        }
        else {
            configurator = new PropertyConfigurator();
        }
        configurator.doConfigure(inputStream, hierarchy);
    }
    
    public static void selectAndConfigure(final URL url, String clazz, final LoggerRepository hierarchy) {
        Configurator configurator = null;
        final String filename = url.getFile();
        if (clazz == null && filename != null && filename.endsWith(".xml")) {
            clazz = "org.apache.log4j.xml.DOMConfigurator";
        }
        if (clazz != null) {
            OptionConverter.LOGGER.debug("Preferred configurator class: " + clazz);
            configurator = (Configurator)instantiateByClassName(clazz, Configurator.class, null);
            if (configurator == null) {
                OptionConverter.LOGGER.error("Could not instantiate configurator [" + clazz + "].");
                return;
            }
        }
        else {
            configurator = new PropertyConfigurator();
        }
        configurator.doConfigure(url, hierarchy);
    }
    
    public static String substVars(final String val, final Properties props) throws IllegalArgumentException {
        return substVars(val, props, new ArrayList<String>());
    }
    
    private static String substVars(final String val, final Properties props, final List<String> keys) throws IllegalArgumentException {
        if (val == null) {
            return null;
        }
        final StringBuilder sbuf = new StringBuilder();
        int i = 0;
        while (true) {
            int j = val.indexOf(OptionConverter.DELIM_START, i);
            if (j == -1) {
                if (i == 0) {
                    return val;
                }
                sbuf.append(val.substring(i));
                return sbuf.toString();
            }
            else {
                sbuf.append(val.substring(i, j));
                final int k = val.indexOf(OptionConverter.DELIM_STOP, j);
                if (k == -1) {
                    throw new IllegalArgumentException(Strings.dquote(val) + " has no closing brace. Opening brace at position " + j + '.');
                }
                j += OptionConverter.DELIM_START_LEN;
                final String key = val.substring(j, k);
                String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
                if (replacement == null && props != null) {
                    replacement = props.getProperty(key);
                }
                if (replacement != null) {
                    if (!keys.contains(key)) {
                        final List<String> usedKeys = new ArrayList<String>(keys);
                        usedKeys.add(key);
                        final String recursiveReplacement = substVars(replacement, props, usedKeys);
                        sbuf.append(recursiveReplacement);
                    }
                    else {
                        sbuf.append(replacement);
                    }
                }
                i = k + OptionConverter.DELIM_STOP_LEN;
            }
        }
    }
    
    public static boolean toBoolean(final String value, final boolean dEfault) {
        if (value == null) {
            return dEfault;
        }
        final String trimmedVal = value.trim();
        return "true".equalsIgnoreCase(trimmedVal) || (!"false".equalsIgnoreCase(trimmedVal) && dEfault);
    }
    
    public static long toFileSize(final String value, final long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String s = value.trim().toUpperCase();
        long multiplier = 1L;
        int index;
        if ((index = s.indexOf("KB")) != -1) {
            multiplier = 1024L;
            s = s.substring(0, index);
        }
        else if ((index = s.indexOf("MB")) != -1) {
            multiplier = 1048576L;
            s = s.substring(0, index);
        }
        else if ((index = s.indexOf("GB")) != -1) {
            multiplier = 1073741824L;
            s = s.substring(0, index);
        }
        if (s != null) {
            try {
                return Long.valueOf(s) * multiplier;
            }
            catch (final NumberFormatException e) {
                LogLog.error("[" + s + "] is not in proper int form.");
                LogLog.error("[" + value + "] not in expected format.", e);
            }
        }
        return defaultValue;
    }
    
    public static int toInt(final String value, final int dEfault) {
        if (value != null) {
            final String s = value.trim();
            try {
                return Integer.valueOf(s);
            }
            catch (final NumberFormatException e) {
                LogLog.error("[" + s + "] is not in proper int form.");
                e.printStackTrace();
            }
        }
        return dEfault;
    }
    
    public static Level toLevel(String value, final Level defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        final Level cached = OptionConverter.LEVELS.get(value);
        if (cached != null) {
            return cached;
        }
        final int hashIndex = value.indexOf(35);
        if (hashIndex != -1) {
            final String clazz = value.substring(hashIndex + 1);
            final String levelName = value.substring(0, hashIndex);
            final Level customLevel = toLevel(clazz, levelName, defaultValue);
            if (customLevel != null && levelName.equals(customLevel.toString()) && clazz.equals(customLevel.getClass().getName())) {
                OptionConverter.LEVELS.putIfAbsent(value, customLevel);
            }
            return customLevel;
        }
        if ("NULL".equalsIgnoreCase(value)) {
            return null;
        }
        final Level standardLevel = Level.toLevel(value, defaultValue);
        if (standardLevel != null && value.equals(standardLevel.toString())) {
            OptionConverter.LEVELS.putIfAbsent(value, standardLevel);
        }
        return standardLevel;
    }
    
    public static Level toLevel(final String clazz, final String levelName, final Level defaultValue) {
        if ("NULL".equalsIgnoreCase(levelName)) {
            return null;
        }
        OptionConverter.LOGGER.debug("toLevel:class=[" + clazz + "]:pri=[" + levelName + "]");
        if (!OptionConverter.LOG4J2_LEVEL_CLASS.equals(clazz)) {
            try {
                final Class<?> customLevel = LoaderUtil.loadClass(clazz);
                final Class<?>[] paramTypes = { String.class, Level.class };
                final Method toLevelMethod = customLevel.getMethod("toLevel", paramTypes);
                final Object[] params = { levelName, defaultValue };
                final Object o = toLevelMethod.invoke(null, params);
                return (Level)o;
            }
            catch (final ClassNotFoundException e) {
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] not found.");
            }
            catch (final NoSuchMethodException e2) {
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] does not have a class function toLevel(String, Level)", e2);
            }
            catch (final InvocationTargetException e3) {
                if (e3.getTargetException() instanceof InterruptedException || e3.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] could not be instantiated", e3);
            }
            catch (final ClassCastException e4) {
                OptionConverter.LOGGER.warn("class [" + clazz + "] is not a subclass of org.apache.log4j.Level", e4);
            }
            catch (final IllegalAccessException e5) {
                OptionConverter.LOGGER.warn("class [" + clazz + "] cannot be instantiated due to access restrictions", e5);
            }
            catch (final RuntimeException e6) {
                OptionConverter.LOGGER.warn("class [" + clazz + "], level [" + levelName + "] conversion failed.", e6);
            }
            return defaultValue;
        }
        final org.apache.logging.log4j.Level v2Level = org.apache.logging.log4j.Level.getLevel(levelName.toUpperCase());
        if (v2Level != null) {
            return new LevelWrapper(v2Level);
        }
        return defaultValue;
    }
    
    private OptionConverter() {
    }
    
    static {
        OptionConverter.DELIM_START = "${";
        OptionConverter.DELIM_STOP = '}';
        OptionConverter.DELIM_START_LEN = 2;
        OptionConverter.DELIM_STOP_LEN = 1;
        LOGGER = StatusLogger.getLogger();
        MAX_CUTOFF_LEVEL = 50000 + 100 * (StandardLevel.FATAL.intLevel() - StandardLevel.OFF.intLevel() - 1) + 1;
        MIN_CUTOFF_LEVEL = -2147478648 - (Integer.MIN_VALUE + StandardLevel.ALL.intLevel()) + StandardLevel.TRACE.intLevel();
        LEVELS = new ConcurrentHashMap<String, Level>();
        LOG4J2_LEVEL_CLASS = org.apache.logging.log4j.Level.class.getName();
        charMap = new CharMap[] { new CharMap('n', '\n'), new CharMap('r', '\r'), new CharMap('t', '\t'), new CharMap('f', '\f'), new CharMap('\b', '\b'), new CharMap('\"', '\"'), new CharMap('\'', '\''), new CharMap('\\', '\\') };
    }
    
    private static class CharMap
    {
        final char key;
        final char replacement;
        
        public CharMap(final char key, final char replacement) {
            this.key = key;
            this.replacement = replacement;
        }
    }
    
    private static class LevelWrapper extends Level
    {
        private static final long serialVersionUID = -7693936267612508528L;
        
        protected LevelWrapper(final org.apache.logging.log4j.Level v2Level) {
            super(OptionConverter.toLog4j1Level(v2Level.intLevel()), v2Level.name(), OptionConverter.toSyslogLevel(v2Level.intLevel()), v2Level);
        }
    }
}
