/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

public final class ClassUtils {
    private static final Map<String, Boolean> cachedClasses;
    public static final ClassUtils INSTANCE;

    @JvmStatic
    public static final boolean hasClass(String className) {
        boolean bl;
        if (cachedClasses.containsKey(className)) {
            Boolean bl2 = cachedClasses.get(className);
            if (bl2 == null) {
                Intrinsics.throwNpe();
            }
            bl = bl2;
        } else {
            boolean bl3;
            try {
                Class.forName(className);
                cachedClasses.put(className, true);
                bl3 = true;
            }
            catch (ClassNotFoundException e) {
                cachedClasses.put(className, false);
                bl3 = false;
            }
            bl = bl3;
        }
        return bl;
    }

    public final boolean hasForge() {
        return ClassUtils.hasClass("net.minecraftforge.common.MinecraftForge");
    }

    private ClassUtils() {
    }

    static {
        ClassUtils classUtils;
        INSTANCE = classUtils = new ClassUtils();
        boolean bl = false;
        cachedClasses = new LinkedHashMap();
    }
}

