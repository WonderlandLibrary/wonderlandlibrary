// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.jmx;

import java.util.Iterator;
import javax.management.NotCompliantMBeanException;
import javax.management.InstanceAlreadyExistsException;
import java.util.Enumeration;
import javax.management.MBeanRegistrationException;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import javax.management.JMException;
import javax.management.Attribute;
import javax.management.RuntimeOperationsException;
import javax.management.AttributeList;
import org.apache.log4j.Appender;
import java.util.Vector;
import javax.management.MBeanServer;
import javax.management.MBeanRegistration;
import javax.management.DynamicMBean;

public abstract class AbstractDynamicMBean implements DynamicMBean, MBeanRegistration
{
    String dClassName;
    MBeanServer server;
    private final Vector mbeanList;
    
    public AbstractDynamicMBean() {
        this.mbeanList = new Vector();
    }
    
    protected static String getAppenderName(final Appender appender) {
        String name = appender.getName();
        if (name == null || name.trim().length() == 0) {
            name = appender.toString();
        }
        return name;
    }
    
    @Override
    public AttributeList getAttributes(final String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"), "Cannot invoke a getter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributeNames.length == 0) {
            return resultList;
        }
        for (final String attributeName : attributeNames) {
            try {
                final Object value = this.getAttribute(attributeName);
                resultList.add(new Attribute(attributeName, value));
            }
            catch (final JMException e) {
                e.printStackTrace();
            }
            catch (final RuntimeException e2) {
                e2.printStackTrace();
            }
        }
        return resultList;
    }
    
    protected abstract Logger getLogger();
    
    @Override
    public void postDeregister() {
        this.getLogger().debug("postDeregister is called.");
    }
    
    @Override
    public void postRegister(final Boolean registrationDone) {
    }
    
    @Override
    public void preDeregister() {
        this.getLogger().debug("preDeregister called.");
        final Enumeration iterator = this.mbeanList.elements();
        while (iterator.hasMoreElements()) {
            final ObjectName name = iterator.nextElement();
            try {
                this.server.unregisterMBean(name);
            }
            catch (final InstanceNotFoundException e) {
                this.getLogger().warn("Missing MBean " + name.getCanonicalName());
            }
            catch (final MBeanRegistrationException e2) {
                this.getLogger().warn("Failed unregistering " + name.getCanonicalName());
            }
        }
    }
    
    @Override
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) {
        this.getLogger().debug("preRegister called. Server=" + server + ", name=" + name);
        this.server = server;
        return name;
    }
    
    protected void registerMBean(final Object mbean, final ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        this.server.registerMBean(mbean, objectName);
        this.mbeanList.add(objectName);
    }
    
    @Override
    public AttributeList setAttributes(final AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"), "Cannot invoke a setter of " + this.dClassName);
        }
        final AttributeList resultList = new AttributeList();
        if (attributes.isEmpty()) {
            return resultList;
        }
        for (final Object attribute : attributes) {
            final Attribute attr = (Attribute)attribute;
            try {
                this.setAttribute(attr);
                final String name = attr.getName();
                final Object value = this.getAttribute(name);
                resultList.add(new Attribute(name, value));
            }
            catch (final JMException e) {
                e.printStackTrace();
            }
            catch (final RuntimeException e2) {
                e2.printStackTrace();
            }
        }
        return resultList;
    }
}
