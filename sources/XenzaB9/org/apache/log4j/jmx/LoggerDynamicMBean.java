// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.jmx;

import javax.management.InvalidAttributeValueException;
import javax.management.Attribute;
import java.beans.IntrospectionException;
import javax.management.JMException;
import javax.management.Notification;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.MBeanException;
import org.apache.log4j.Level;
import javax.management.AttributeNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanAttributeInfo;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.Appender;
import java.util.Vector;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import org.apache.log4j.Logger;
import javax.management.NotificationListener;

public class LoggerDynamicMBean extends AbstractDynamicMBean implements NotificationListener
{
    private static Logger cat;
    private final MBeanConstructorInfo[] dConstructors;
    private final MBeanOperationInfo[] dOperations;
    private final Vector dAttributes;
    private final String dClassName;
    private final String dDescription = "This MBean acts as a management facade for a org.apache.log4j.Logger instance.";
    private final Logger logger;
    
    public LoggerDynamicMBean(final Logger logger) {
        this.dConstructors = new MBeanConstructorInfo[1];
        this.dOperations = new MBeanOperationInfo[1];
        this.dAttributes = new Vector();
        this.dClassName = this.getClass().getName();
        this.logger = logger;
        this.buildDynamicMBeanInfo();
    }
    
    void addAppender(final String appenderClass, final String appenderName) {
        LoggerDynamicMBean.cat.debug("addAppender called with " + appenderClass + ", " + appenderName);
        final Appender appender = (Appender)OptionConverter.instantiateByClassName(appenderClass, Appender.class, null);
        appender.setName(appenderName);
        this.logger.addAppender(appender);
    }
    
    void appenderMBeanRegistration() {
        final Enumeration enumeration = this.logger.getAllAppenders();
        while (enumeration.hasMoreElements()) {
            final Appender appender = enumeration.nextElement();
            this.registerAppenderMBean(appender);
        }
    }
    
    private void buildDynamicMBeanInfo() {
        final Constructor[] constructors = this.getClass().getConstructors();
        this.dConstructors[0] = new MBeanConstructorInfo("HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance", constructors[0]);
        this.dAttributes.add(new MBeanAttributeInfo("name", "java.lang.String", "The name of this Logger.", true, false, false));
        this.dAttributes.add(new MBeanAttributeInfo("priority", "java.lang.String", "The priority of this logger.", true, true, false));
        final MBeanParameterInfo[] params = { new MBeanParameterInfo("class name", "java.lang.String", "add an appender to this logger"), new MBeanParameterInfo("appender name", "java.lang.String", "name of the appender") };
        this.dOperations[0] = new MBeanOperationInfo("addAppender", "addAppender(): add an appender", params, "void", 1);
    }
    
    @Override
    public Object getAttribute(final String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + this.dClassName + " with null attribute name");
        }
        if (attributeName.equals("name")) {
            return this.logger.getName();
        }
        if (!attributeName.equals("priority")) {
            if (attributeName.startsWith("appender=")) {
                try {
                    return new ObjectName("log4j:" + attributeName);
                }
                catch (final MalformedObjectNameException e) {
                    LoggerDynamicMBean.cat.error("Could not create ObjectName" + attributeName);
                }
                catch (final RuntimeException e2) {
                    LoggerDynamicMBean.cat.error("Could not create ObjectName" + attributeName);
                }
            }
            throw new AttributeNotFoundException("Cannot find " + attributeName + " attribute in " + this.dClassName);
        }
        final Level l = this.logger.getLevel();
        if (l == null) {
            return null;
        }
        return l.toString();
    }
    
    @Override
    protected Logger getLogger() {
        return this.logger;
    }
    
    @Override
    public MBeanInfo getMBeanInfo() {
        final MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.dAttributes.size()];
        this.dAttributes.toArray(attribs);
        final MBeanInfo mb = new MBeanInfo(this.dClassName, "This MBean acts as a management facade for a org.apache.log4j.Logger instance.", attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
        return mb;
    }
    
    @Override
    public void handleNotification(final Notification notification, final Object handback) {
        LoggerDynamicMBean.cat.debug("Received notification: " + notification.getType());
        this.registerAppenderMBean((Appender)notification.getUserData());
    }
    
    @Override
    public Object invoke(final String operationName, final Object[] params, final String[] signature) throws MBeanException, ReflectionException {
        if (operationName.equals("addAppender")) {
            this.addAppender((String)params[0], (String)params[1]);
            return "Hello world.";
        }
        return null;
    }
    
    @Override
    public void postRegister(final Boolean registrationDone) {
        this.appenderMBeanRegistration();
    }
    
    void registerAppenderMBean(final Appender appender) {
        final String name = AbstractDynamicMBean.getAppenderName(appender);
        LoggerDynamicMBean.cat.debug("Adding AppenderMBean for appender named " + name);
        ObjectName objectName = null;
        try {
            final AppenderDynamicMBean appenderMBean = new AppenderDynamicMBean(appender);
            objectName = new ObjectName("log4j", "appender", name);
            if (!this.server.isRegistered(objectName)) {
                this.registerMBean(appenderMBean, objectName);
                this.dAttributes.add(new MBeanAttributeInfo("appender=" + name, "javax.management.ObjectName", "The " + name + " appender.", true, true, false));
            }
        }
        catch (final JMException e) {
            LoggerDynamicMBean.cat.error("Could not add appenderMBean for [" + name + "].", e);
        }
        catch (final IntrospectionException e2) {
            LoggerDynamicMBean.cat.error("Could not add appenderMBean for [" + name + "].", e2);
        }
        catch (final RuntimeException e3) {
            LoggerDynamicMBean.cat.error("Could not add appenderMBean for [" + name + "].", e3);
        }
    }
    
    @Override
    public void setAttribute(final Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Cannot invoke a setter of " + this.dClassName + " with null attribute");
        }
        final String name = attribute.getName();
        final Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke the setter of " + this.dClassName + " with null attribute name");
        }
        if (name.equals("priority")) {
            if (value instanceof String) {
                final String s = (String)value;
                Level p = this.logger.getLevel();
                if (s.equalsIgnoreCase("NULL")) {
                    p = null;
                }
                else {
                    p = OptionConverter.toLevel(s, p);
                }
                this.logger.setLevel(p);
            }
            return;
        }
        throw new AttributeNotFoundException("Attribute " + name + " not found in " + this.getClass().getName());
    }
    
    static {
        LoggerDynamicMBean.cat = Logger.getLogger(LoggerDynamicMBean.class);
    }
}
