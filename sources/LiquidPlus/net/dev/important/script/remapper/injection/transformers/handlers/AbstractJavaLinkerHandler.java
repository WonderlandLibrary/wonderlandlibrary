/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 */
package net.dev.important.script.remapper.injection.transformers.handlers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.script.remapper.Remapper;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J$\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007J\u001c\u0010\n\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\u0006\u0010\u0007\u001a\u00020\u0004H\u0007\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/script/remapper/injection/transformers/handlers/AbstractJavaLinkerHandler;", "", "()V", "addMember", "", "clazz", "Ljava/lang/Class;", "name", "accessibleObject", "Ljava/lang/reflect/AccessibleObject;", "setPropertyGetter", "LiquidBounce"})
public final class AbstractJavaLinkerHandler {
    @NotNull
    public static final AbstractJavaLinkerHandler INSTANCE = new AbstractJavaLinkerHandler();

    private AbstractJavaLinkerHandler() {
    }

    @JvmStatic
    @NotNull
    public static final String addMember(@NotNull Class<?> clazz, @NotNull String name, @NotNull AccessibleObject accessibleObject) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(accessibleObject, "accessibleObject");
        if (!(accessibleObject instanceof Method)) {
            return name;
        }
        Object currentClass = clazz;
        while (!Intrinsics.areEqual(((Class)currentClass).getName(), "java.lang.Object")) {
            Object object = Type.getMethodDescriptor((Method)((Method)accessibleObject));
            Intrinsics.checkNotNullExpressionValue(object, "getMethodDescriptor(accessibleObject)");
            String remapped = Remapper.INSTANCE.remapMethod((Class<?>)currentClass, name, (String)object);
            if (!Intrinsics.areEqual(remapped, name)) {
                return remapped;
            }
            if (((Class)currentClass).getSuperclass() == null) break;
            object = ((Class)currentClass).getSuperclass();
            Intrinsics.checkNotNullExpressionValue(object, "currentClass.superclass");
            currentClass = object;
        }
        return name;
    }

    @JvmStatic
    @NotNull
    public static final String addMember(@NotNull Class<?> clazz, @NotNull String name) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        Intrinsics.checkNotNullParameter(name, "name");
        Class<?> currentClass = clazz;
        while (!Intrinsics.areEqual(currentClass.getName(), "java.lang.Object")) {
            String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (!Intrinsics.areEqual(remapped, name)) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) break;
            Class<?> clazz2 = currentClass.getSuperclass();
            Intrinsics.checkNotNullExpressionValue(clazz2, "currentClass.superclass");
            currentClass = clazz2;
        }
        return name;
    }

    @JvmStatic
    @NotNull
    public static final String setPropertyGetter(@NotNull Class<?> clazz, @NotNull String name) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        Intrinsics.checkNotNullParameter(name, "name");
        Class<?> currentClass = clazz;
        while (!Intrinsics.areEqual(currentClass.getName(), "java.lang.Object")) {
            String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (!Intrinsics.areEqual(remapped, name)) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) break;
            Class<?> clazz2 = currentClass.getSuperclass();
            Intrinsics.checkNotNullExpressionValue(clazz2, "currentClass.superclass");
            currentClass = clazz2;
        }
        return name;
    }
}

