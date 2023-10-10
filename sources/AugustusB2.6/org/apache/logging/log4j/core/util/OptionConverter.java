// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;
import java.util.Locale;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import org.apache.logging.log4j.Level;
import java.util.Properties;
import org.apache.logging.log4j.Logger;

public final class OptionConverter
{
    private static final Logger LOGGER;
    private static final String DELIM_START = "${";
    private static final char DELIM_STOP = '}';
    private static final int DELIM_START_LEN = 2;
    private static final int DELIM_STOP_LEN = 1;
    private static final int ONE_K = 1024;
    
    private OptionConverter() {
    }
    
    public static String[] concatenateArrays(final String[] l, final String[] r) {
        final int len = l.length + r.length;
        final String[] a = new String[len];
        System.arraycopy(l, 0, a, 0, l.length);
        System.arraycopy(r, 0, a, l.length, r.length);
        return a;
    }
    
    public static String convertSpecialChars(final String s) {
        final int len = s.length();
        final StringBuilder sbuf = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char c = s.charAt(i++);
            if (c == '\\') {
                c = s.charAt(i++);
                switch (c) {
                    case 'n': {
                        c = '\n';
                        break;
                    }
                    case 'r': {
                        c = '\r';
                        break;
                    }
                    case 't': {
                        c = '\t';
                        break;
                    }
                    case 'f': {
                        c = '\f';
                        break;
                    }
                    case 'b': {
                        c = '\b';
                        break;
                    }
                    case '\"': {
                        c = '\"';
                        break;
                    }
                    case '\'': {
                        c = '\'';
                        break;
                    }
                    case '\\': {
                        c = '\\';
                        break;
                    }
                }
            }
            sbuf.append(c);
        }
        return sbuf.toString();
    }
    
    public static Object instantiateByKey(final Properties props, final String key, final Class<?> superClass, final Object defaultValue) {
        final String className = findAndSubst(key, props);
        if (className == null) {
            OptionConverter.LOGGER.error("Could not find value for key {}", key);
            return defaultValue;
        }
        return instantiateByClassName(className.trim(), superClass, defaultValue);
    }
    
    public static boolean toBoolean(final String value, final boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        final String trimmedVal = value.trim();
        return "true".equalsIgnoreCase(trimmedVal) || (!"false".equalsIgnoreCase(trimmedVal) && defaultValue);
    }
    
    public static int toInt(final String value, final int defaultValue) {
        if (value != null) {
            final String s = value.trim();
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                OptionConverter.LOGGER.error("[{}] is not in proper int form.", s, e);
            }
        }
        return defaultValue;
    }
    
    public static Level toLevel(String value, final Level defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        value = value.trim();
        final int hashIndex = value.indexOf(35);
        if (hashIndex == -1) {
            if ("NULL".equalsIgnoreCase(value)) {
                return null;
            }
            return Level.toLevel(value, defaultValue);
        }
        else {
            Level result = defaultValue;
            final String clazz = value.substring(hashIndex + 1);
            final String levelName = value.substring(0, hashIndex);
            if ("NULL".equalsIgnoreCase(levelName)) {
                return null;
            }
            OptionConverter.LOGGER.debug("toLevel:class=[" + clazz + "]:pri=[" + levelName + "]");
            try {
                final Class<?> customLevel = Loader.loadClass(clazz);
                final Class<?>[] paramTypes = (Class<?>[])new Class[] { String.class, Level.class };
                final Method toLevelMethod = customLevel.getMethod("toLevel", paramTypes);
                final Object[] params = { levelName, defaultValue };
                final Object o = toLevelMethod.invoke(null, params);
                result = (Level)o;
            }
            catch (ClassNotFoundException e6) {
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] not found.");
            }
            catch (NoSuchMethodException e) {
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] does not have a class function toLevel(String, Level)", e);
            }
            catch (InvocationTargetException e2) {
                if (e2.getTargetException() instanceof InterruptedException || e2.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                OptionConverter.LOGGER.warn("custom level class [" + clazz + "] could not be instantiated", e2);
            }
            catch (ClassCastException e3) {
                OptionConverter.LOGGER.warn("class [" + clazz + "] is not a subclass of org.apache.log4j.Level", e3);
            }
            catch (IllegalAccessException e4) {
                OptionConverter.LOGGER.warn("class [" + clazz + "] cannot be instantiated due to access restrictions", e4);
            }
            catch (RuntimeException e5) {
                OptionConverter.LOGGER.warn("class [" + clazz + "], level [" + levelName + "] conversion failed.", e5);
            }
            return result;
        }
    }
    
    public static long toFileSize(final String value, final long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String str = value.trim().toUpperCase(Locale.ENGLISH);
        long multiplier = 1L;
        int index;
        if ((index = str.indexOf("KB")) != -1) {
            multiplier = 1024L;
            str = str.substring(0, index);
        }
        else if ((index = str.indexOf("MB")) != -1) {
            multiplier = 1048576L;
            str = str.substring(0, index);
        }
        else if ((index = str.indexOf("GB")) != -1) {
            multiplier = 1073741824L;
            str = str.substring(0, index);
        }
        try {
            return Long.parseLong(str) * multiplier;
        }
        catch (NumberFormatException e) {
            OptionConverter.LOGGER.error("[{}] is not in proper int form.", str);
            OptionConverter.LOGGER.error("[{}] not in expected format.", value, e);
            return defaultValue;
        }
    }
    
    public static String findAndSubst(final String key, final Properties props) {
        final String value = props.getProperty(key);
        if (value == null) {
            return null;
        }
        try {
            return substVars(value, props);
        }
        catch (IllegalArgumentException e) {
            OptionConverter.LOGGER.error("Bad option value [{}].", value, e);
            return value;
        }
    }
    
    public static Object instantiateByClassName(final String className, final Class<?> superClass, final Object defaultValue) {
        if (className != null) {
            try {
                final Class<?> classObj = Loader.loadClass(className);
                if (!superClass.isAssignableFrom(classObj)) {
                    OptionConverter.LOGGER.error("A \"{}\" object is not assignable to a \"{}\" variable.", className, superClass.getName());
                    OptionConverter.LOGGER.error("The class \"{}\" was loaded by [{}] whereas object of type [{}] was loaded by [{}].", superClass.getName(), superClass.getClassLoader(), classObj.getTypeName(), classObj.getName());
                    return defaultValue;
                }
                return classObj.newInstance();
            }
            catch (Exception e) {
                OptionConverter.LOGGER.error("Could not instantiate class [{}].", className, e);
            }
        }
        return defaultValue;
    }
    
    public static String substVars(final String val, final Properties props) throws IllegalArgumentException {
        final StringBuilder sbuf = new StringBuilder();
        int i = 0;
        while (true) {
            int j = val.indexOf("${", i);
            if (j == -1) {
                if (i == 0) {
                    return val;
                }
                sbuf.append(val.substring(i, val.length()));
                return sbuf.toString();
            }
            else {
                sbuf.append(val.substring(i, j));
                final int k = val.indexOf(125, j);
                if (k == -1) {
                    throw new IllegalArgumentException(Strings.dquote(val) + " has no closing brace. Opening brace at position " + j + '.');
                }
                j += 2;
                final String key = val.substring(j, k);
                String replacement = PropertiesUtil.getProperties().getStringProperty(key, null);
                if (replacement == null && props != null) {
                    replacement = props.getProperty(key);
                }
                if (replacement != null) {
                    final String recursiveReplacement = substVars(replacement, props);
                    sbuf.append(recursiveReplacement);
                }
                i = k + 1;
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
