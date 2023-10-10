// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Objects;
import org.apache.logging.log4j.core.selector.ContextSelector;
import javax.management.ObjectName;

public class ContextSelectorAdmin implements ContextSelectorAdminMBean
{
    private final ObjectName objectName;
    private final ContextSelector selector;
    
    public ContextSelectorAdmin(final String contextName, final ContextSelector selector) {
        this.selector = Objects.requireNonNull(selector, "ContextSelector");
        try {
            final String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", Server.escape(contextName));
            this.objectName = new ObjectName(mbeanName);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    @Override
    public String getImplementationClassName() {
        return this.selector.getClass().getName();
    }
}
