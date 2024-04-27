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
    public static final ClassUtils INSTANCE;
    private static final Map cachedClasses;

    static {
        ClassUtils classUtils;
        INSTANCE = classUtils = new ClassUtils();
        boolean bl = false;
        cachedClasses = new LinkedHashMap();
    }

    private ClassUtils() {
    }

    public final boolean hasForge() {
        return ClassUtils.hasClass("net.minecraftforge.common.MinecraftForge");
    }

    @JvmStatic
    public static final boolean hasClass(String string) {
        boolean bl;
        if (cachedClasses.containsKey(string)) {
            Object v = cachedClasses.get(string);
            if (v == null) {
                Intrinsics.throwNpe();
            }
            bl = (Boolean)v;
        } else {
            boolean bl2;
            try {
                Class.forName(string);
                cachedClasses.put(string, true);
                bl2 = true;
            }
            catch (ClassNotFoundException classNotFoundException) {
                cachedClasses.put(string, false);
                bl2 = false;
            }
            bl = bl2;
        }
        return bl;
    }
}

