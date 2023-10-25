/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.FXPermissions
 */
package com.sun.javafx.tk;

import com.sun.javafx.FXPermissions;
import java.lang.reflect.Constructor;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.Permission;

public class PermissionHelper {
    private static boolean awtInitialized = false;
    private static Permission awtClipboardPermission;

    private static synchronized Permission getAWTClipboardPermission() {
        if (!awtInitialized) {
            try {
                Class<?> class_ = Class.forName("java.awt.AWTPermission", false, PermissionHelper.class.getClassLoader());
                Constructor<?> constructor = class_.getConstructor(String.class);
                awtClipboardPermission = (Permission)constructor.newInstance("accessClipboard");
            }
            catch (Exception exception) {
                awtClipboardPermission = null;
            }
            awtInitialized = true;
        }
        return awtClipboardPermission;
    }

    public static void checkClipboardPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        try {
            securityManager.checkPermission((Permission)FXPermissions.ACCESS_CLIPBOARD_PERMISSION);
        }
        catch (SecurityException securityException) {
            Permission permission = PermissionHelper.getAWTClipboardPermission();
            if (permission == null) {
                throw securityException;
            }
            try {
                securityManager.checkPermission(permission);
            }
            catch (SecurityException securityException2) {
                throw securityException;
            }
        }
    }

    public static void checkClipboardPermission(AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        if (accessControlContext == null) {
            throw new AccessControlException("AccessControlContext is null");
        }
        try {
            securityManager.checkPermission((Permission)FXPermissions.ACCESS_CLIPBOARD_PERMISSION, accessControlContext);
        }
        catch (SecurityException securityException) {
            Permission permission = PermissionHelper.getAWTClipboardPermission();
            if (permission == null) {
                throw securityException;
            }
            try {
                securityManager.checkPermission(permission, accessControlContext);
            }
            catch (SecurityException securityException2) {
                throw securityException;
            }
        }
    }

    private PermissionHelper() {
    }
}

