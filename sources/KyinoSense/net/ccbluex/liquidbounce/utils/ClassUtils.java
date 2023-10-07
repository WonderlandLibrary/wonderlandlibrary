/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  org.apache.logging.log4j.core.config.plugins.ResolverUtil
 *  org.apache.logging.log4j.core.config.plugins.ResolverUtil$ClassTest
 *  org.apache.logging.log4j.core.config.plugins.ResolverUtil$Test
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0007\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\t0\b2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00020\u0001H\u0007J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0005H\u0007J\u0006\u0010\u000f\u001a\u00020\u0006J\u0006\u0010\u0010\u001a\u00020\u0006J4\u0010\u0011\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00120\u000b0\b\"\b\b\u0000\u0010\u0012*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00052\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00120\u000bR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/utils/ClassUtils;", "", "()V", "cachedClasses", "", "", "", "getValues", "", "Lnet/ccbluex/liquidbounce/value/Value;", "clazz", "Ljava/lang/Class;", "instance", "hasClass", "className", "hasForge", "isBlockUnder", "resolvePackage", "T", "packagePath", "KyinoClient"})
public final class ClassUtils {
    private static final Map<String, Boolean> cachedClasses;
    public static final ClassUtils INSTANCE;

    @JvmStatic
    public static final boolean hasClass(@NotNull String className) {
        boolean bl;
        Intrinsics.checkParameterIsNotNull(className, "className");
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

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final List<Value<?>> getValues(@NotNull Class<?> clazz, @NotNull Object instance) {
        void $this$filterIsInstanceTo$iv$iv;
        Iterable $this$mapTo$iv$iv;
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        Intrinsics.checkParameterIsNotNull(instance, "instance");
        Field[] fieldArray = clazz.getDeclaredFields();
        Intrinsics.checkExpressionValueIsNotNull(fieldArray, "clazz.declaredFields");
        Field[] $this$map$iv = fieldArray;
        boolean $i$f$map = false;
        Field[] fieldArray2 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        Iterator iterator2 = $this$mapTo$iv$iv;
        int n = ((void)iterator2).length;
        for (int i = 0; i < n; ++i) {
            void valueField;
            void item$iv$iv;
            void var11_12 = item$iv$iv = iterator2[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            void v1 = valueField;
            Intrinsics.checkExpressionValueIsNotNull(v1, "valueField");
            v1.setAccessible(true);
            Object object = valueField.get(instance);
            collection.add(object);
        }
        Iterable $this$filterIsInstance$iv = (List)destination$iv$iv;
        boolean $i$f$filterIsInstance = false;
        $this$mapTo$iv$iv = $this$filterIsInstance$iv;
        destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof Value)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        return (List)destination$iv$iv;
    }

    public final boolean hasForge() {
        return ClassUtils.hasClass("net.minecraftforge.common.MinecraftForge");
    }

    @NotNull
    public final <T> List<Class<? extends T>> resolvePackage(@NotNull String packagePath, @NotNull Class<T> clazz) {
        Intrinsics.checkParameterIsNotNull(packagePath, "packagePath");
        Intrinsics.checkParameterIsNotNull(clazz, "clazz");
        ResolverUtil resolver = new ResolverUtil();
        resolver.setClassLoader(clazz.getClassLoader());
        resolver.findInPackage((ResolverUtil.Test)new ResolverUtil.ClassTest(){

            public boolean matches(@Nullable Class<?> type) {
                return true;
            }
        }, packagePath);
        boolean bl = false;
        List list = new ArrayList();
        for (Class resolved : resolver.getClasses()) {
            if (!clazz.isAssignableFrom(resolved) || clazz.isInterface()) continue;
            Class clazz2 = resolved;
            Intrinsics.checkExpressionValueIsNotNull(clazz2, "resolved");
            if (Modifier.isAbstract(clazz2.getModifiers())) continue;
            list.add(resolved);
        }
        return list;
    }

    public final boolean isBlockUnder() {
        if (Minecraft.func_71410_x().field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)Minecraft.func_71410_x().field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb;
            EntityPlayerSP entityPlayerSP = Minecraft.func_71410_x().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "Minecraft.getMinecraft().thePlayer");
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP.func_174813_aQ().func_72317_d(0.0, -((double)off), 0.0), "Minecraft.getMinecraft()\u2026.0, -off.toDouble(), 0.0)");
            List list = Minecraft.func_71410_x().field_71441_e.func_72945_a((Entity)Minecraft.func_71410_x().field_71439_g, bb);
            Intrinsics.checkExpressionValueIsNotNull(list, "Minecraft.getMinecraft()\u2026     bb\n                )");
            Collection collection = list;
            boolean bl = false;
            if (!(!collection.isEmpty())) continue;
            return true;
        }
        return false;
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

