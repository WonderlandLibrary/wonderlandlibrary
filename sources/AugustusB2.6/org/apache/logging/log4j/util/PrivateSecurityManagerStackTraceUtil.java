// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.security.Permission;
import java.util.Stack;

final class PrivateSecurityManagerStackTraceUtil
{
    private static final PrivateSecurityManager SECURITY_MANAGER;
    
    private PrivateSecurityManagerStackTraceUtil() {
    }
    
    static boolean isEnabled() {
        return PrivateSecurityManagerStackTraceUtil.SECURITY_MANAGER != null;
    }
    
    static Stack<Class<?>> getCurrentStackTrace() {
        final Class<?>[] array = PrivateSecurityManagerStackTraceUtil.SECURITY_MANAGER.getClassContext();
        final Stack<Class<?>> classes = new Stack<Class<?>>();
        classes.ensureCapacity(array.length);
        for (final Class<?> clazz : array) {
            classes.push(clazz);
        }
        return classes;
    }
    
    static {
        PrivateSecurityManager psm;
        try {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            psm = new PrivateSecurityManager();
        }
        catch (SecurityException ignored) {
            psm = null;
        }
        SECURITY_MANAGER = psm;
    }
    
    private static final class PrivateSecurityManager extends SecurityManager
    {
        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }
}
