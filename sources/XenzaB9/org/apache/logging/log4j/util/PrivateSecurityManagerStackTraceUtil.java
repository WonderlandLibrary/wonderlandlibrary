// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.security.Permission;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Deque;

final class PrivateSecurityManagerStackTraceUtil
{
    private static final PrivateSecurityManager SECURITY_MANAGER;
    
    private PrivateSecurityManagerStackTraceUtil() {
    }
    
    static boolean isEnabled() {
        return PrivateSecurityManagerStackTraceUtil.SECURITY_MANAGER != null;
    }
    
    static Deque<Class<?>> getCurrentStackTrace() {
        final Class<?>[] array = PrivateSecurityManagerStackTraceUtil.SECURITY_MANAGER.getClassContext();
        final Deque<Class<?>> classes = new ArrayDeque<Class<?>>(array.length);
        Collections.addAll(classes, array);
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
        catch (final SecurityException ignored) {
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
