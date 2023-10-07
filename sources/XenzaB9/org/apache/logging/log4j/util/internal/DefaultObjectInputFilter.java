// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.io.ObjectInputFilter;

public class DefaultObjectInputFilter implements ObjectInputFilter
{
    private static final List<String> REQUIRED_JAVA_CLASSES;
    private static final List<String> REQUIRED_JAVA_PACKAGES;
    private final ObjectInputFilter delegate;
    
    public DefaultObjectInputFilter() {
        this.delegate = null;
    }
    
    public DefaultObjectInputFilter(final ObjectInputFilter filter) {
        this.delegate = filter;
    }
    
    public static DefaultObjectInputFilter newInstance(final ObjectInputFilter filter) {
        return new DefaultObjectInputFilter(filter);
    }
    
    public ObjectInputFilter.Status checkInput(final ObjectInputFilter.FilterInfo filterInfo) {
        ObjectInputFilter.Status status = null;
        if (this.delegate != null) {
            status = this.delegate.checkInput(filterInfo);
            if (status != ObjectInputFilter.Status.UNDECIDED) {
                return status;
            }
        }
        final ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
        if (serialFilter != null) {
            status = serialFilter.checkInput(filterInfo);
            if (status != ObjectInputFilter.Status.UNDECIDED) {
                return status;
            }
        }
        if (filterInfo.serialClass() != null) {
            final String name = filterInfo.serialClass().getName();
            if (isAllowedByDefault(name) || isRequiredPackage(name)) {
                return ObjectInputFilter.Status.ALLOWED;
            }
        }
        return ObjectInputFilter.Status.REJECTED;
    }
    
    private static boolean isAllowedByDefault(final String name) {
        return isRequiredPackage(name) || DefaultObjectInputFilter.REQUIRED_JAVA_CLASSES.contains(name);
    }
    
    private static boolean isRequiredPackage(final String name) {
        for (final String packageName : DefaultObjectInputFilter.REQUIRED_JAVA_PACKAGES) {
            if (name.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        REQUIRED_JAVA_CLASSES = Arrays.asList("java.math.BigDecimal", "java.math.BigInteger", "java.rmi.MarshalledObject", "[B");
        REQUIRED_JAVA_PACKAGES = Arrays.asList("java.lang.", "java.time", "java.util.", "org.apache.logging.log4j.", "[Lorg.apache.logging.log4j.");
    }
}
