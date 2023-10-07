/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.DynamicLinkerFactory;
import jdk.internal.dynalink.MonomorphicCallSite;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;

public class DefaultBootstrapper {
    private static final DynamicLinker dynamicLinker = new DynamicLinkerFactory().createLinker();

    private DefaultBootstrapper() {
    }

    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) {
        return DefaultBootstrapper.bootstrapInternal(caller, name, type);
    }

    public static CallSite publicBootstrap(MethodHandles.Lookup caller, String name, MethodType type) {
        return DefaultBootstrapper.bootstrapInternal(MethodHandles.publicLookup(), name, type);
    }

    private static CallSite bootstrapInternal(MethodHandles.Lookup caller, String name, MethodType type) {
        return dynamicLinker.link(new MonomorphicCallSite(CallSiteDescriptorFactory.create(caller, name, type)));
    }
}

