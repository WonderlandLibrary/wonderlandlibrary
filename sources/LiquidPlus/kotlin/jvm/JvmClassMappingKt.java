/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.jvm;

import java.lang.annotation.Annotation;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u0018\u001a\u00020\u0019\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\r*\u0006\u0012\u0002\b\u00030\u001a\u00a2\u0006\u0002\u0010\u001b\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"-\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018G\u00a2\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b\"&\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\u0002H\u00028\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000e\";\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018\u00c7\u0002X\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u000f\u0010\t\u001a\u0004\b\u0010\u0010\u000b\"+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u000b\"-\u0010\u0013\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u000b\"+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00078G\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\u001c"}, d2={"annotationClass", "Lkotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "java", "Ljava/lang/Class;", "getJavaClass$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "getRuntimeClassOfKClassInstance$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "kotlin-stdlib"})
@JvmName(name="JvmClassMappingKt")
public final class JvmClassMappingKt {
    @JvmName(name="getJavaClass")
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull KClass<T> $this$java) {
        Intrinsics.checkNotNullParameter($this$java, "<this>");
        return ((ClassBasedDeclarationContainer)((Object)$this$java)).getJClass();
    }

    public static /* synthetic */ void getJavaClass$annotations(KClass kClass) {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    public static final <T> Class<T> getJavaPrimitiveType(@NotNull KClass<T> $this$javaPrimitiveType) {
        Intrinsics.checkNotNullParameter($this$javaPrimitiveType, "<this>");
        Class<?> thisJClass = ((ClassBasedDeclarationContainer)((Object)$this$javaPrimitiveType)).getJClass();
        if (thisJClass.isPrimitive()) {
            return thisJClass;
        }
        String string = thisJClass.getName();
        if (string == null) return null;
        int n = -1;
        switch (string.hashCode()) {
            case -527879800: {
                if (string.equals("java.lang.Float")) {
                    n = 1;
                }
                break;
            }
            case 399092968: {
                if (string.equals("java.lang.Void")) {
                    n = 2;
                }
                break;
            }
            case 155276373: {
                if (string.equals("java.lang.Character")) {
                    n = 3;
                }
                break;
            }
            case 398795216: {
                if (string.equals("java.lang.Long")) {
                    n = 4;
                }
                break;
            }
            case 761287205: {
                if (string.equals("java.lang.Double")) {
                    n = 5;
                }
                break;
            }
            case -515992664: {
                if (string.equals("java.lang.Short")) {
                    n = 6;
                }
                break;
            }
            case 344809556: {
                if (string.equals("java.lang.Boolean")) {
                    n = 7;
                }
                break;
            }
            case 398507100: {
                if (string.equals("java.lang.Byte")) {
                    n = 8;
                }
                break;
            }
            case -2056817302: {
                if (string.equals("java.lang.Integer")) {
                    n = 9;
                }
                break;
            }
        }
        switch (n) {
            case 7: {
                Class<Object> clazz = Boolean.TYPE;
                return clazz;
            }
            case 3: {
                Class<Object> clazz = Character.TYPE;
                return clazz;
            }
            case 8: {
                Class<Object> clazz = Byte.TYPE;
                return clazz;
            }
            case 6: {
                Class<Object> clazz = Short.TYPE;
                return clazz;
            }
            case 9: {
                Class<Object> clazz = Integer.TYPE;
                return clazz;
            }
            case 1: {
                Class<Object> clazz = Float.TYPE;
                return clazz;
            }
            case 4: {
                Class<Object> clazz = Long.TYPE;
                return clazz;
            }
            case 5: {
                Class<Object> clazz = Double.TYPE;
                return clazz;
            }
            case 2: {
                Class<Object> clazz = Void.TYPE;
                return clazz;
            }
            default: {
                return null;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull KClass<T> $this$javaObjectType) {
        Intrinsics.checkNotNullParameter($this$javaObjectType, "<this>");
        Class<?> thisJClass = ((ClassBasedDeclarationContainer)((Object)$this$javaObjectType)).getJClass();
        if (!thisJClass.isPrimitive()) {
            return thisJClass;
        }
        String string = thisJClass.getName();
        if (string != null) {
            int n = -1;
            switch (string.hashCode()) {
                case 64711720: {
                    if (!string.equals("boolean")) break;
                    n = 1;
                    break;
                }
                case 3625364: {
                    if (!string.equals("void")) break;
                    n = 2;
                    break;
                }
                case 3039496: {
                    if (!string.equals("byte")) break;
                    n = 3;
                    break;
                }
                case -1325958191: {
                    if (!string.equals("double")) break;
                    n = 4;
                    break;
                }
                case 3052374: {
                    if (!string.equals("char")) break;
                    n = 5;
                    break;
                }
                case 109413500: {
                    if (!string.equals("short")) break;
                    n = 6;
                    break;
                }
                case 97526364: {
                    if (!string.equals("float")) break;
                    n = 7;
                    break;
                }
                case 104431: {
                    if (!string.equals("int")) break;
                    n = 8;
                    break;
                }
                case 3327612: {
                    if (!string.equals("long")) break;
                    n = 9;
                    break;
                }
            }
            switch (n) {
                case 1: {
                    return Boolean.class;
                }
                case 5: {
                    return Character.class;
                }
                case 3: {
                    return Byte.class;
                }
                case 6: {
                    return Short.class;
                }
                case 8: {
                    return Integer.class;
                }
                case 7: {
                    return Float.class;
                }
                case 9: {
                    return Long.class;
                }
                case 4: {
                    return Double.class;
                }
                case 2: {
                    return Void.class;
                }
            }
        }
        Class<Object> clazz = thisJClass;
        return clazz;
    }

    @JvmName(name="getKotlinClass")
    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull Class<T> $this$kotlin) {
        Intrinsics.checkNotNullParameter($this$kotlin, "<this>");
        KClass kClass = Reflection.getOrCreateKotlinClass($this$kotlin);
        return kClass;
    }

    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull T $this$javaClass) {
        Intrinsics.checkNotNullParameter($this$javaClass, "<this>");
        boolean $i$f$getJavaClass = false;
        Class<?> clazz = $this$javaClass.getClass();
        if (clazz == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>");
        }
        return clazz;
    }

    @JvmName(name="getRuntimeClassOfKClassInstance")
    @NotNull
    public static final <T> Class<KClass<T>> getRuntimeClassOfKClassInstance(@NotNull KClass<T> $this$javaClass) {
        Intrinsics.checkNotNullParameter($this$javaClass, "<this>");
        boolean $i$f$getRuntimeClassOfKClassInstance = false;
        Class<KClass<T>> clazz = ((Object)$this$javaClass).getClass();
        if (clazz == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.Class<kotlin.reflect.KClass<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>>");
        }
        return clazz;
    }

    @Deprecated(message="Use 'java' property to get Java class corresponding to this Kotlin class or cast this instance to Any if you really want to get the runtime Java class of this implementation of KClass.", replaceWith=@ReplaceWith(expression="(this as Any).javaClass", imports={}), level=DeprecationLevel.ERROR)
    public static /* synthetic */ void getRuntimeClassOfKClassInstance$annotations(KClass kClass) {
    }

    public static final /* synthetic */ boolean isArrayOf(Object[] $this$isArrayOf) {
        Intrinsics.checkNotNullParameter($this$isArrayOf, "<this>");
        Intrinsics.reifiedOperationMarker(4, "T");
        return Object.class.isAssignableFrom($this$isArrayOf.getClass().getComponentType());
    }

    @NotNull
    public static final <T extends Annotation> KClass<? extends T> getAnnotationClass(@NotNull T $this$annotationClass) {
        Intrinsics.checkNotNullParameter($this$annotationClass, "<this>");
        Class<? extends Annotation> clazz = $this$annotationClass.annotationType();
        Intrinsics.checkNotNullExpressionValue(clazz, "this as java.lang.annota\u2026otation).annotationType()");
        return JvmClassMappingKt.getKotlinClass(clazz);
    }
}

