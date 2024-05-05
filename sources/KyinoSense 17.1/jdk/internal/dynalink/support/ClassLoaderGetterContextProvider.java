/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.security.AccessControlContext;
import java.security.Permissions;
import java.security.ProtectionDomain;

public class ClassLoaderGetterContextProvider {
    public static final AccessControlContext GET_CLASS_LOADER_CONTEXT;

    static {
        Permissions perms = new Permissions();
        perms.add(new RuntimePermission("getClassLoader"));
        GET_CLASS_LOADER_CONTEXT = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }
}

