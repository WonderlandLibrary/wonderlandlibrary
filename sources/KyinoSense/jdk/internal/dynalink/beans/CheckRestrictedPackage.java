/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

class CheckRestrictedPackage {
    private static final AccessControlContext NO_PERMISSIONS_CONTEXT = CheckRestrictedPackage.createNoPermissionsContext();

    CheckRestrictedPackage() {
    }

    static boolean isRestrictedClass(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return true;
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return false;
        }
        final String name = clazz.getName();
        final int i = name.lastIndexOf(46);
        if (i == -1) {
            return false;
        }
        try {
            AccessController.doPrivileged(new PrivilegedAction<Void>(){

                @Override
                public Void run() {
                    sm.checkPackageAccess(name.substring(0, i));
                    return null;
                }
            }, NO_PERMISSIONS_CONTEXT);
        }
        catch (SecurityException e) {
            return true;
        }
        return false;
    }

    private static AccessControlContext createNoPermissionsContext() {
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, new Permissions())});
    }
}

