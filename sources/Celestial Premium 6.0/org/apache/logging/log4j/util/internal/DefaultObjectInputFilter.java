/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.util.internal.DefaultObjectInputFilter
 */
package org.apache.logging.log4j.util.internal;

import java.io.ObjectInputFilter;
import java.util.Arrays;
import java.util.List;

/*
 * Exception performing whole class analysis ignored.
 */
public class DefaultObjectInputFilter
implements ObjectInputFilter {
    private static final List<String> REQUIRED_JAVA_CLASSES = Arrays.asList("java.math.BigDecimal", "java.math.BigInteger", "java.rmi.MarshalledObject", "[B");
    private static final List<String> REQUIRED_JAVA_PACKAGES = Arrays.asList("java.lang.", "java.time", "java.util.", "org.apache.logging.log4j.", "[Lorg.apache.logging.log4j.");
    private final ObjectInputFilter delegate;

    public DefaultObjectInputFilter() {
        this.delegate = null;
    }

    public DefaultObjectInputFilter(ObjectInputFilter filter) {
        this.delegate = filter;
    }

    public static DefaultObjectInputFilter newInstance(ObjectInputFilter filter) {
        return new DefaultObjectInputFilter(filter);
    }

    @Override
    public ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo filterInfo) {
        String name;
        ObjectInputFilter.Status status = null;
        if (this.delegate != null && (status = this.delegate.checkInput(filterInfo)) != ObjectInputFilter.Status.UNDECIDED) {
            return status;
        }
        ObjectInputFilter serialFilter = ObjectInputFilter.Config.getSerialFilter();
        if (serialFilter != null && (status = serialFilter.checkInput(filterInfo)) != ObjectInputFilter.Status.UNDECIDED) {
            return status;
        }
        if (filterInfo.serialClass() != null && (DefaultObjectInputFilter.isAllowedByDefault((String)(name = filterInfo.serialClass().getName())) || DefaultObjectInputFilter.isRequiredPackage((String)name))) {
            return ObjectInputFilter.Status.ALLOWED;
        }
        return ObjectInputFilter.Status.REJECTED;
    }

    private static boolean isAllowedByDefault(String name) {
        return DefaultObjectInputFilter.isRequiredPackage((String)name) || REQUIRED_JAVA_CLASSES.contains(name);
    }

    private static boolean isRequiredPackage(String name) {
        for (String packageName : REQUIRED_JAVA_PACKAGES) {
            if (!name.startsWith(packageName)) continue;
            return true;
        }
        return false;
    }
}

