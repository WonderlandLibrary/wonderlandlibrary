/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.util.Stack;

final class PrivateSecurityManagerStackTraceUtil {
    private static final PrivateSecurityManager SECURITY_MANAGER;

    private PrivateSecurityManagerStackTraceUtil() {
    }

    static boolean isEnabled() {
        return SECURITY_MANAGER != null;
    }

    static Stack<Class<?>> getCurrentStackTrace() {
        Class<?>[] array = SECURITY_MANAGER.getClassContext();
        Stack classes = new Stack();
        classes.ensureCapacity(array.length);
        for (Class<?> clazz : array) {
            classes.push(clazz);
        }
        return classes;
    }

    static {
        PrivateSecurityManager psm;
        try {
            SecurityManager sm = System.getSecurityManager();
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

    private static final class PrivateSecurityManager
    extends SecurityManager {
        private PrivateSecurityManager() {
        }

        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }
}

