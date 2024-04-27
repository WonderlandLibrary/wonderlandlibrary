/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0005H\u0007J\u0006\u0010\t\u001a\u00020\u0006R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/utils/ClassUtils;", "", "()V", "cachedClasses", "", "", "", "hasClass", "className", "hasForge", "LiKingSense"})
public final class ClassUtils {
    private static final Map<String, Boolean> cachedClasses;
    public static final ClassUtils INSTANCE;

    @JvmStatic
    public static final boolean hasClass(@NotNull String className) {
        boolean bl;
        Intrinsics.checkParameterIsNotNull((Object)className, (String)"className");
        if (cachedClasses.containsKey(className)) {
            bl = cachedClasses.get(className);
        } else {
            boolean bl2;
            try {
                Class.forName(className);
                cachedClasses.put(className, true);
                bl2 = true;
            }
            catch (ClassNotFoundException e) {
                cachedClasses.put(className, false);
                bl2 = false;
            }
            bl = bl2;
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

