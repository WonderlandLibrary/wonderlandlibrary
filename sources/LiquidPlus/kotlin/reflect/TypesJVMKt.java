/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.KTypeBase;
import kotlin.reflect.GenericArrayTypeImpl;
import kotlin.reflect.KClass;
import kotlin.reflect.KClassifier;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KTypeProjection;
import kotlin.reflect.KVariance;
import kotlin.reflect.ParameterizedTypeImpl;
import kotlin.reflect.TypeVariableImpl;
import kotlin.reflect.TypesJVMKt;
import kotlin.reflect.WildcardTypeImpl;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0083\u0004\u00a2\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t\u00a8\u0006\u0015"}, d2={"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"})
public final class TypesJVMKt {
    @NotNull
    public static final Type getJavaType(@NotNull KType $this$javaType) {
        Type type;
        Intrinsics.checkNotNullParameter($this$javaType, "<this>");
        if ($this$javaType instanceof KTypeBase && (type = ((KTypeBase)$this$javaType).getJavaType()) != null) {
            Type type2;
            Type it = type2 = type;
            boolean bl = false;
            return it;
        }
        return TypesJVMKt.computeJavaType$default($this$javaType, false, 1, null);
    }

    @SinceKotlin(version="1.4")
    @ExperimentalStdlibApi
    @LowPriorityInOverloadResolution
    public static /* synthetic */ void getJavaType$annotations(KType kType) {
    }

    @ExperimentalStdlibApi
    private static final Type computeJavaType(KType $this$computeJavaType, boolean forceWrapper) {
        KClassifier classifier = $this$computeJavaType.getClassifier();
        if (classifier instanceof KTypeParameter) {
            return new TypeVariableImpl((KTypeParameter)classifier);
        }
        if (classifier instanceof KClass) {
            Class<Object> jClass = forceWrapper ? JvmClassMappingKt.getJavaObjectType((KClass)classifier) : JvmClassMappingKt.getJavaClass((KClass)classifier);
            List<KTypeProjection> arguments = $this$computeJavaType.getArguments();
            if (arguments.isEmpty()) {
                return jClass;
            }
            if (jClass.isArray()) {
                Type type;
                if (jClass.getComponentType().isPrimitive()) {
                    return jClass;
                }
                KTypeProjection kTypeProjection = CollectionsKt.singleOrNull(arguments);
                if (kTypeProjection == null) {
                    throw new IllegalArgumentException(Intrinsics.stringPlus("kotlin.Array must have exactly one type argument: ", $this$computeJavaType));
                }
                KTypeProjection kTypeProjection2 = kTypeProjection;
                KVariance variance = kTypeProjection2.component1();
                KType elementType = kTypeProjection2.component2();
                KVariance kVariance = variance;
                int n = kVariance == null ? -1 : WhenMappings.$EnumSwitchMapping$0[kVariance.ordinal()];
                switch (n) {
                    case -1: 
                    case 1: {
                        type = jClass;
                        break;
                    }
                    case 2: 
                    case 3: {
                        KType kType = elementType;
                        Intrinsics.checkNotNull(kType);
                        Type javaElementType = TypesJVMKt.computeJavaType$default(kType, false, 1, null);
                        if (javaElementType instanceof Class) {
                            type = jClass;
                            break;
                        }
                        type = new GenericArrayTypeImpl(javaElementType);
                        break;
                    }
                    default: {
                        throw new NoWhenBranchMatchedException();
                    }
                }
                return type;
            }
            return TypesJVMKt.createPossiblyInnerType(jClass, arguments);
        }
        throw new UnsupportedOperationException(Intrinsics.stringPlus("Unsupported type classifier: ", $this$computeJavaType));
    }

    static /* synthetic */ Type computeJavaType$default(KType kType, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            bl = false;
        }
        return TypesJVMKt.computeJavaType(kType, bl);
    }

    /*
     * WARNING - void declaration
     */
    @ExperimentalStdlibApi
    private static final Type createPossiblyInnerType(Class<?> jClass, List<KTypeProjection> arguments) {
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Class<?> clazz = jClass.getDeclaringClass();
        if (clazz == null) {
            Collection<Type> collection2;
            void $this$mapTo$iv$iv2;
            void $this$map$iv2;
            Iterable iterable = arguments;
            Type type = null;
            Class<?> clazz2 = jClass;
            boolean $i$f$map2 = false;
            void var6_16 = $this$map$iv2;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv2 : $this$mapTo$iv$iv2) {
                void p0;
                KTypeProjection kTypeProjection = (KTypeProjection)item$iv$iv2;
                collection2 = destination$iv$iv;
                boolean bl = false;
                Type type2 = TypesJVMKt.getJavaType((KTypeProjection)p0);
                collection2.add(type2);
            }
            collection2 = (List)destination$iv$iv;
            List list = collection2;
            Type type3 = type;
            Class<?> clazz3 = clazz2;
            return new ParameterizedTypeImpl(clazz3, type3, list);
        }
        Class<?> ownerClass = clazz;
        if (Modifier.isStatic(jClass.getModifiers())) {
            Collection<Type> collection3;
            void $this$mapTo$iv$iv3;
            void $this$map$iv3;
            clazz = arguments;
            Type type = ownerClass;
            Class<?> clazz4 = jClass;
            boolean $i$f$map = false;
            void $i$f$map2 = $this$map$iv3;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv3, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv3) {
                void p0;
                KTypeProjection item$iv$iv2 = (KTypeProjection)item$iv$iv;
                collection3 = destination$iv$iv;
                boolean bl = false;
                Type type4 = TypesJVMKt.getJavaType((KTypeProjection)p0);
                collection3.add(type4);
            }
            collection3 = (List)destination$iv$iv;
            List list = collection3;
            Type type5 = type;
            Class<?> clazz5 = clazz4;
            return new ParameterizedTypeImpl(clazz5, type5, list);
        }
        int n = jClass.getTypeParameters().length;
        Iterable $i$f$map = arguments.subList(0, n);
        Type type = TypesJVMKt.createPossiblyInnerType(ownerClass, arguments.subList(n, arguments.size()));
        Class<?> clazz6 = jClass;
        boolean $i$f$map3 = false;
        void destination$iv$iv = $this$map$iv;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void p0;
            KTypeProjection bl = (KTypeProjection)item$iv$iv;
            collection = destination$iv$iv2;
            boolean bl2 = false;
            Type type6 = TypesJVMKt.getJavaType((KTypeProjection)p0);
            collection.add(type6);
        }
        collection = (List)destination$iv$iv2;
        List list = collection;
        Type type7 = type;
        Class<?> clazz7 = clazz6;
        return new ParameterizedTypeImpl(clazz7, type7, list);
    }

    private static final Type getJavaType(KTypeProjection $this$javaType) {
        Type type;
        KVariance kVariance = $this$javaType.getVariance();
        if (kVariance == null) {
            return WildcardTypeImpl.Companion.getSTAR();
        }
        KVariance variance = kVariance;
        KType kType = $this$javaType.getType();
        Intrinsics.checkNotNull(kType);
        KType type2 = kType;
        KVariance kVariance2 = variance;
        int n = WhenMappings.$EnumSwitchMapping$0[kVariance2.ordinal()];
        switch (n) {
            case 2: {
                type = TypesJVMKt.computeJavaType(type2, true);
                break;
            }
            case 1: {
                type = new WildcardTypeImpl(null, TypesJVMKt.computeJavaType(type2, true));
                break;
            }
            case 3: {
                type = new WildcardTypeImpl(TypesJVMKt.computeJavaType(type2, true), null);
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return type;
    }

    @ExperimentalStdlibApi
    private static /* synthetic */ void getJavaType$annotations(KTypeProjection kTypeProjection) {
    }

    private static final String typeToString(Type type) {
        String string;
        if (type instanceof Class) {
            String string2;
            if (((Class)type).isArray()) {
                Sequence<Type> unwrap2 = SequencesKt.generateSequence(type, (Function1)typeToString.unwrap.1.INSTANCE);
                string2 = Intrinsics.stringPlus(((Class)SequencesKt.last(unwrap2)).getName(), StringsKt.repeat("[]", SequencesKt.count(unwrap2)));
            } else {
                string2 = ((Class)type).getName();
            }
            String string3 = string2;
            Intrinsics.checkNotNullExpressionValue(string3, "{\n        if (type.isArr\u2026   } else type.name\n    }");
            string = string3;
        } else {
            string = type.toString();
        }
        return string;
    }

    public static final /* synthetic */ Type access$computeJavaType(KType $receiver, boolean forceWrapper) {
        return TypesJVMKt.computeJavaType($receiver, forceWrapper);
    }

    public static final /* synthetic */ String access$typeToString(Type type) {
        return TypesJVMKt.typeToString(type);
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[KVariance.values().length];
            nArray[KVariance.IN.ordinal()] = 1;
            nArray[KVariance.INVARIANT.ordinal()] = 2;
            nArray[KVariance.OUT.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

