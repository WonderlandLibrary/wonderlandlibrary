// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.jmx;

import javax.management.InvalidAttributeValueException;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.Priority;
import javax.management.Attribute;
import org.apache.log4j.spi.OptionHandler;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.io.InterruptedIOException;
import javax.management.RuntimeOperationsException;
import java.lang.reflect.Method;
import java.beans.PropertyDescriptor;
import java.beans.BeanInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanAttributeInfo;
import org.apache.log4j.Level;
import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.beans.IntrospectionException;
import org.apache.log4j.Layout;
import javax.management.MBeanOperationInfo;
import java.util.Hashtable;
import java.util.Vector;
import javax.management.MBeanConstructorInfo;
import org.apache.log4j.Logger;

public class LayoutDynamicMBean extends AbstractDynamicMBean
{
    private static Logger cat;
    private final MBeanConstructorInfo[] dConstructors;
    private final Vector dAttributes;
    private final String dClassName;
    private final Hashtable dynamicProps;
    private final MBeanOperationInfo[] dOperations;
    private final String dDescription = "This MBean acts as a management facade for log4j layouts.";
    private final Layout layout;
    
    public LayoutDynamicMBean(final Layout layout) throws IntrospectionException {
        this.dConstructors = new MBeanConstructorInfo[1];
        this.dAttributes = new Vector();
        this.dClassName = this.getClass().getName();
        this.dynamicProps = new Hashtable(5);
        this.dOperations = new MBeanOperationInfo[1];
        this.layout = layout;
        this.buildDynamicMBeanInfo();
    }
    
    private void buildDynamicMBeanInfo() throws IntrospectionException {
        final Constructor[] constructors = this.getClass().getConstructors();
        this.dConstructors[0] = new MBeanConstructorInfo("LayoutDynamicMBean(): Constructs a LayoutDynamicMBean instance", constructors[0]);
        final BeanInfo bi = Introspector.getBeanInfo(this.layout.getClass());
        final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        for (int size = pd.length, i = 0; i < size; ++i) {
            final String name = pd[i].getName();
            final Method readMethod = pd[i].getReadMethod();
            final Method writeMethod = pd[i].getWriteMethod();
            if (readMethod != null) {
                final Class returnClass = readMethod.getReturnType();
                if (this.isSupportedType(returnClass)) {
                    String returnClassName;
                    if (returnClass.isAssignableFrom(Level.class)) {
                        returnClassName = "java.lang.String";
                    }
                    else {
                        returnClassName = returnClass.getName();
                    }
                    this.dAttributes.add(new MBeanAttributeInfo(name, returnClassName, "Dynamic", true, writeMethod != null, false));
                    this.dynamicProps.put(name, new MethodUnion(readMethod, writeMethod));
                }
            }
        }
        final MBeanParameterInfo[] params = new MBeanParameterInfo[0];
        this.dOperations[0] = new MBeanOperationInfo("activateOptions", "activateOptions(): add an layout", params, "void", 1);
    }
    
    @Override
    public Object getAttribute(final String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + this.dClassName + " with null attribute name");
        }
        final MethodUnion mu = this.dynamicProps.get(attributeName);
        LayoutDynamicMBean.cat.debug("----name=" + attributeName + ", mu=" + mu);
        if (mu != null && mu.readMethod != null) {
            try {
                return mu.readMethod.invoke(this.layout, (Object[])null);
            }
            catch (final InvocationTargetException e) {
                if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                return null;
            }
            catch (final IllegalAccessException e2) {
                return null;
            }
            catch (final RuntimeException e3) {
                return null;
            }
        }
        throw new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + this.dClassName);
    }
    
    @Override
    protected Logger getLogger() {
        return LayoutDynamicMBean.cat;
    }
    
    @Override
    public MBeanInfo getMBeanInfo() {
        LayoutDynamicMBean.cat.debug("getMBeanInfo called.");
        final MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.dAttributes.size()];
        this.dAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, "This MBean acts as a management facade for log4j layouts.", attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }
    
    @Override
    public Object invoke(final String operationName, final Object[] params, final String[] signature) throws MBeanException, ReflectionException {
        if (operationName.equals("activateOptions") && this.layout instanceof OptionHandler) {
            final OptionHandler oh = (OptionHandler)this.layout;
            oh.activateOptions();
            return "Options activated.";
        }
        return null;
    }
    
    private boolean isSupportedType(final Class clazz) {
        return clazz.isPrimitive() || clazz == String.class || clazz.isAssignableFrom(Level.class);
    }
    
    @Override
    public void setAttribute(final Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + this.dClassName + " with null attribute");
        }
        final String name = attribute.getName();
        Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke the setter of " + this.dClassName + " with null attribute name");
        }
        final MethodUnion mu = this.dynamicProps.get(name);
        if (mu != null && mu.writeMethod != null) {
            final Object[] o = { null };
            final Class[] params = mu.writeMethod.getParameterTypes();
            if (params[0] == Priority.class) {
                value = OptionConverter.toLevel((String)value, (Level)this.getAttribute(name));
            }
            o[0] = value;
            try {
                mu.writeMethod.invoke(this.layout, o);
            }
            catch (final InvocationTargetException e) {
                if (e.getTargetException() instanceof InterruptedException || e.getTargetException() instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LayoutDynamicMBean.cat.error("FIXME", e);
            }
            catch (final IllegalAccessException e2) {
                LayoutDynamicMBean.cat.error("FIXME", e2);
            }
            catch (final RuntimeException e3) {
                LayoutDynamicMBean.cat.error("FIXME", e3);
            }
            return;
        }
        throw new AttributeNotFoundException("Attribute " + name + " not found in " + this.getClass().getName());
    }
    
    static {
        LayoutDynamicMBean.cat = Logger.getLogger(LayoutDynamicMBean.class);
    }
}
