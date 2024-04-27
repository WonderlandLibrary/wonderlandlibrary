/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0005H\u0007J\u0006\u0010\t\u001a\u00020\u0006R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lnet/dev/important/utils/ClassUtils;", "", "()V", "cachedClasses", "", "", "", "hasClass", "className", "hasForge", "LiquidBounce"})
public final class ClassUtils {
    @NotNull
    public static final ClassUtils INSTANCE = new ClassUtils();
    @NotNull
    private static final Map<String, Boolean> cachedClasses = new LinkedHashMap();

    private ClassUtils() {
    }

    @JvmStatic
    public static final boolean hasClass(@NotNull String className) {
        boolean bl;
        Intrinsics.checkNotNullParameter(className, "className");
        if (cachedClasses.containsKey(className)) {
            Boolean bl2 = cachedClasses.get(className);
            Intrinsics.checkNotNull(bl2);
            bl = bl2;
        } else {
            boolean bl3;
            try {
                Class.forName(className);
                Map<String, Boolean> map = cachedClasses;
                Boolean bl4 = true;
                map.put(className, bl4);
                bl3 = true;
            }
            catch (ClassNotFoundException e) {
                Map<String, Boolean> map = cachedClasses;
                Boolean bl5 = false;
                map.put(className, bl5);
                bl3 = false;
            }
            bl = bl3;
        }
        return bl;
    }

    public final boolean hasForge() {
        return ClassUtils.hasClass("net.minecraftforge.common.MinecraftForge");
    }
}

