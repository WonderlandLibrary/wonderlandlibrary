/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

public class AutoDiscovery {
    private AutoDiscovery() {
    }

    public static List<GuardingDynamicLinker> loadLinkers() {
        return AutoDiscovery.getLinkers(ServiceLoader.load(GuardingDynamicLinker.class));
    }

    public static List<GuardingDynamicLinker> loadLinkers(ClassLoader cl) {
        return AutoDiscovery.getLinkers(ServiceLoader.load(GuardingDynamicLinker.class, cl));
    }

    private static <T> List<T> getLinkers(ServiceLoader<T> loader) {
        LinkedList<T> list = new LinkedList<T>();
        for (T linker : loader) {
            list.add(linker);
        }
        return list;
    }
}

