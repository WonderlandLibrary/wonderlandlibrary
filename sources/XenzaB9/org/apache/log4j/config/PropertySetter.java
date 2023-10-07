// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.Priority;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.Appender;
import org.apache.logging.log4j.core.util.OptionConverter;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import java.beans.PropertyDescriptor;

public class PropertySetter
{
    private static final PropertyDescriptor[] EMPTY_PROPERTY_DESCRIPTOR_ARRAY;
    private static Logger LOGGER;
    protected Object obj;
    protected PropertyDescriptor[] props;
    
    public PropertySetter(final Object obj) {
        this.obj = obj;
    }
    
    public static void setProperties(final Object obj, final Properties properties, final String prefix) {
        new PropertySetter(obj).setProperties(properties, prefix);
    }
    
    protected void introspect() {
        try {
            final BeanInfo bi = Introspector.getBeanInfo(this.obj.getClass());
            this.props = bi.getPropertyDescriptors();
        }
        catch (final IntrospectionException ex) {
            PropertySetter.LOGGER.error("Failed to introspect {}: {}", this.obj, ex.getMessage());
            this.props = PropertySetter.EMPTY_PROPERTY_DESCRIPTOR_ARRAY;
        }
    }
    
    public void setProperties(final Properties properties, final String prefix) {
        final int len = prefix.length();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefix)) {
                if (key.indexOf(46, len + 1) > 0) {
                    continue;
                }
                final String value = OptionConverter.findAndSubst(key, properties);
                key = key.substring(len);
                if (("layout".equals(key) || "errorhandler".equals(key)) && this.obj instanceof Appender) {
                    continue;
                }
                final PropertyDescriptor prop = this.getPropertyDescriptor(Introspector.decapitalize(key));
                if (prop != null && OptionHandler.class.isAssignableFrom(prop.getPropertyType()) && prop.getWriteMethod() != null) {
                    final OptionHandler opt = (OptionHandler)OptionConverter.instantiateByKey(properties, prefix + key, prop.getPropertyType(), null);
                    final PropertySetter setter = new PropertySetter(opt);
                    setter.setProperties(properties, prefix + key + ".");
                    try {
                        prop.getWriteMethod().invoke(this.obj, opt);
                    }
                    catch (final InvocationTargetException ex) {
                        if (ex.getTargetException() instanceof InterruptedException || ex.getTargetException() instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        PropertySetter.LOGGER.warn("Failed to set property [{}] to value \"{}\".", key, value, ex);
                    }
                    catch (final IllegalAccessException | RuntimeException ex2) {
                        PropertySetter.LOGGER.warn("Failed to set property [{}] to value \"{}\".", key, value, ex2);
                    }
                }
                else {
                    this.setProperty(key, value);
                }
            }
        }
        this.activate();
    }
    
    public void setProperty(String name, final String value) {
        if (value == null) {
            return;
        }
        name = Introspector.decapitalize(name);
        final PropertyDescriptor prop = this.getPropertyDescriptor(name);
        if (prop == null) {
            PropertySetter.LOGGER.warn("No such property [" + name + "] in " + this.obj.getClass().getName() + ".");
        }
        else {
            try {
                this.setProperty(prop, name, value);
            }
            catch (final PropertySetterException ex) {
                PropertySetter.LOGGER.warn("Failed to set property [{}] to value \"{}\".", name, value, ex.rootCause);
            }
        }
    }
    
    public void setProperty(final PropertyDescriptor prop, final String name, final String value) throws PropertySetterException {
        final Method setter = prop.getWriteMethod();
        if (setter == null) {
            throw new PropertySetterException("No setter for property [" + name + "].");
        }
        final Class<?>[] paramTypes = setter.getParameterTypes();
        if (paramTypes.length != 1) {
            throw new PropertySetterException("#params for setter != 1");
        }
        Object arg;
        try {
            arg = this.convertArg(value, paramTypes[0]);
        }
        catch (final Throwable t) {
            throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed. Reason: " + t);
        }
        if (arg == null) {
            throw new PropertySetterException("Conversion to type [" + paramTypes[0] + "] failed.");
        }
        PropertySetter.LOGGER.debug("Setting property [" + name + "] to [" + arg + "].");
        try {
            setter.invoke(this.obj, arg);
        }
        catch (final InvocationTargetException ex) {
            if (ex.getTargetException() instanceof InterruptedException || ex.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            throw new PropertySetterException(ex);
        }
        catch (final IllegalAccessException | RuntimeException ex2) {
            throw new PropertySetterException(ex2);
        }
    }
    
    protected Object convertArg(final String val, final Class<?> type) {
        if (val == null) {
            return null;
        }
        final String v = val.trim();
        if (String.class.isAssignableFrom(type)) {
            return val;
        }
        if (Integer.TYPE.isAssignableFrom(type)) {
            return Integer.parseInt(v);
        }
        if (Long.TYPE.isAssignableFrom(type)) {
            return Long.parseLong(v);
        }
        if (Boolean.TYPE.isAssignableFrom(type)) {
            if ("true".equalsIgnoreCase(v)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(v)) {
                return Boolean.FALSE;
            }
        }
        else {
            if (Priority.class.isAssignableFrom(type)) {
                return org.apache.log4j.helpers.OptionConverter.toLevel(v, Log4j1Configuration.DEFAULT_LEVEL);
            }
            if (ErrorHandler.class.isAssignableFrom(type)) {
                return OptionConverter.instantiateByClassName(v, ErrorHandler.class, null);
            }
        }
        return null;
    }
    
    protected PropertyDescriptor getPropertyDescriptor(final String name) {
        if (this.props == null) {
            this.introspect();
        }
        for (final PropertyDescriptor prop : this.props) {
            if (name.equals(prop.getName())) {
                return prop;
            }
        }
        return null;
    }
    
    public void activate() {
        if (this.obj instanceof OptionHandler) {
            ((OptionHandler)this.obj).activateOptions();
        }
    }
    
    static {
        EMPTY_PROPERTY_DESCRIPTOR_ARRAY = new PropertyDescriptor[0];
        PropertySetter.LOGGER = StatusLogger.getLogger();
    }
}
