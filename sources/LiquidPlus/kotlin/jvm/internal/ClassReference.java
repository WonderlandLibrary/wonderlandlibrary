/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.jvm.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.reflect.KCallable;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 O2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001OB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0013\u0010F\u001a\u00020\u00122\b\u0010G\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010H\u001a\u00020IH\u0002J\b\u0010J\u001a\u00020KH\u0016J\u0012\u0010L\u001a\u00020\u00122\b\u0010M\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010N\u001a\u000201H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u001a\u0010\"\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b#\u0010\u0014\u001a\u0004\b\"\u0010\u0015R\u001a\u0010$\u001a\u00020\u00128VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b%\u0010\u0014\u001a\u0004\b$\u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001e\u0010(\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030)0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u001e\u0010+\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0016\u0010-\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010/R\u0016\u00100\u001a\u0004\u0018\u0001018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u00103R(\u00104\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b5\u0010\u0014\u001a\u0004\b6\u0010\u000bR\u0016\u00107\u001a\u0004\u0018\u0001018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b8\u00103R \u00109\u001a\b\u0012\u0004\u0012\u00020:0\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b;\u0010\u0014\u001a\u0004\b<\u0010\u000bR \u0010=\u001a\b\u0012\u0004\u0012\u00020>0\b8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\b?\u0010\u0014\u001a\u0004\b@\u0010\u000bR\u001c\u0010A\u001a\u0004\u0018\u00010B8VX\u0097\u0004\u00a2\u0006\f\u0012\u0004\bC\u0010\u0014\u001a\u0004\bD\u0010E\u00a8\u0006P"}, d2={"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isFun", "isFun$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "isValue", "isValue$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "getSealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "getSupertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"})
public final class ClassReference
implements KClass<Object>,
ClassBasedDeclarationContainer {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final Class<?> jClass;
    @NotNull
    private static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES;
    @NotNull
    private static final HashMap<String, String> primitiveFqNames;
    @NotNull
    private static final HashMap<String, String> primitiveWrapperFqNames;
    @NotNull
    private static final HashMap<String, String> classFqNames;
    @NotNull
    private static final Map<String, String> simpleNames;

    public ClassReference(@NotNull Class<?> jClass) {
        Intrinsics.checkNotNullParameter(jClass, "jClass");
        this.jClass = jClass;
    }

    @Override
    @NotNull
    public Class<?> getJClass() {
        return this.jClass;
    }

    @Override
    @Nullable
    public String getSimpleName() {
        return Companion.getClassSimpleName(this.getJClass());
    }

    @Override
    @Nullable
    public String getQualifiedName() {
        return Companion.getClassQualifiedName(this.getJClass());
    }

    @Override
    @NotNull
    public Collection<KCallable<?>> getMembers() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public Collection<KFunction<Object>> getConstructors() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public Collection<KClass<?>> getNestedClasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @NotNull
    public List<Annotation> getAnnotations() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @Nullable
    public Object getObjectInstance() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isInstance(@Nullable Object value) {
        return Companion.isInstance(value, this.getJClass());
    }

    @Override
    @NotNull
    public List<KTypeParameter> getTypeParameters() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getTypeParameters$annotations() {
    }

    @Override
    @NotNull
    public List<KType> getSupertypes() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getSupertypes$annotations() {
    }

    @Override
    @NotNull
    public List<KClass<? extends Object>> getSealedSubclasses() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.3")
    public static /* synthetic */ void getSealedSubclasses$annotations() {
    }

    @Override
    @Nullable
    public KVisibility getVisibility() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void getVisibility$annotations() {
    }

    @Override
    public boolean isFinal() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isFinal$annotations() {
    }

    @Override
    public boolean isOpen() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isOpen$annotations() {
    }

    @Override
    public boolean isAbstract() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isAbstract$annotations() {
    }

    @Override
    public boolean isSealed() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isSealed$annotations() {
    }

    @Override
    public boolean isData() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isData$annotations() {
    }

    @Override
    public boolean isInner() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isInner$annotations() {
    }

    @Override
    public boolean isCompanion() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.1")
    public static /* synthetic */ void isCompanion$annotations() {
    }

    @Override
    public boolean isFun() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.4")
    public static /* synthetic */ void isFun$annotations() {
    }

    @Override
    public boolean isValue() {
        this.error();
        throw new KotlinNothingValueException();
    }

    @SinceKotlin(version="1.5")
    public static /* synthetic */ void isValue$annotations() {
    }

    private final Void error() {
        throw new KotlinReflectionNotSupportedError();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof ClassReference && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass)other));
    }

    @Override
    public int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    @NotNull
    public String toString() {
        return Intrinsics.stringPlus(this.getJClass().toString(), " (Kotlin reflection is not available)");
    }

    /*
     * WARNING - void declaration
     */
    static {
        void $this$mapValuesTo$iv$iv;
        Pair<Object, Integer> pair;
        Object object;
        void $this$mapIndexedTo$iv$iv;
        Companion = new Companion(null);
        Class[] classArray = new Class[]{Function0.class, Function1.class, Function2.class, Function3.class, Function4.class, Function5.class, Function6.class, Function7.class, Function8.class, Function9.class, Function10.class, Function11.class, Function12.class, Function13.class, Function14.class, Function15.class, Function16.class, Function17.class, Function18.class, Function19.class, Function20.class, Function21.class, Function22.class};
        HashMap<String, String> $this$mapIndexed$iv = CollectionsKt.listOf(classArray);
        boolean $i$f$mapIndexed = false;
        Iterable iterable = $this$mapIndexed$iv;
        Object destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$mapIndexed$iv, 10));
        boolean bl = false;
        int index$iv$iv232 = 0;
        for (Object item$iv$iv : $this$mapIndexedTo$iv$iv) {
            void i;
            void clazz;
            int n = index$iv$iv232;
            index$iv$iv232 = n + 1;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Class clazz2 = (Class)item$iv$iv;
            int n2 = n;
            object = destination$iv$iv;
            boolean bl2 = false;
            pair = TuplesKt.to(clazz, (int)i);
            object.add(pair);
        }
        FUNCTION_CLASSES = MapsKt.toMap((List)destination$iv$iv);
        HashMap<String, String> $this$primitiveFqNames_u24lambda_u2d1 = $this$mapIndexed$iv = new HashMap();
        boolean bl3 = false;
        $this$primitiveFqNames_u24lambda_u2d1.put("boolean", "kotlin.Boolean");
        $this$primitiveFqNames_u24lambda_u2d1.put("char", "kotlin.Char");
        $this$primitiveFqNames_u24lambda_u2d1.put("byte", "kotlin.Byte");
        $this$primitiveFqNames_u24lambda_u2d1.put("short", "kotlin.Short");
        $this$primitiveFqNames_u24lambda_u2d1.put("int", "kotlin.Int");
        $this$primitiveFqNames_u24lambda_u2d1.put("float", "kotlin.Float");
        $this$primitiveFqNames_u24lambda_u2d1.put("long", "kotlin.Long");
        $this$primitiveFqNames_u24lambda_u2d1.put("double", "kotlin.Double");
        primitiveFqNames = $this$mapIndexed$iv;
        HashMap<String, String> $this$primitiveWrapperFqNames_u24lambda_u2d2 = $this$mapIndexed$iv = new HashMap<String, String>();
        boolean bl2 = false;
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Boolean", "kotlin.Boolean");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Character", "kotlin.Char");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Byte", "kotlin.Byte");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Short", "kotlin.Short");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Integer", "kotlin.Int");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Float", "kotlin.Float");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Long", "kotlin.Long");
        $this$primitiveWrapperFqNames_u24lambda_u2d2.put("java.lang.Double", "kotlin.Double");
        primitiveWrapperFqNames = $this$mapIndexed$iv;
        HashMap<String, String> $this$classFqNames_u24lambda_u2d4 = $this$mapIndexed$iv = new HashMap();
        boolean $i$a$-apply-ClassReference$Companion$classFqNames$232 = false;
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Object", "kotlin.Any");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.String", "kotlin.String");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.CharSequence", "kotlin.CharSequence");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Throwable", "kotlin.Throwable");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Cloneable", "kotlin.Cloneable");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Number", "kotlin.Number");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Comparable", "kotlin.Comparable");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Enum", "kotlin.Enum");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        $this$classFqNames_u24lambda_u2d4.put("java.lang.Iterable", "kotlin.collections.Iterable");
        $this$classFqNames_u24lambda_u2d4.put("java.util.Iterator", "kotlin.collections.Iterator");
        $this$classFqNames_u24lambda_u2d4.put("java.util.Collection", "kotlin.collections.Collection");
        $this$classFqNames_u24lambda_u2d4.put("java.util.List", "kotlin.collections.List");
        $this$classFqNames_u24lambda_u2d4.put("java.util.Set", "kotlin.collections.Set");
        $this$classFqNames_u24lambda_u2d4.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        $this$classFqNames_u24lambda_u2d4.put("java.util.Map", "kotlin.collections.Map");
        $this$classFqNames_u24lambda_u2d4.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        $this$classFqNames_u24lambda_u2d4.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        $this$classFqNames_u24lambda_u2d4.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        $this$classFqNames_u24lambda_u2d4.putAll((Map)primitiveFqNames);
        $this$classFqNames_u24lambda_u2d4.putAll((Map)primitiveWrapperFqNames);
        destination$iv$iv = primitiveFqNames.values();
        Intrinsics.checkNotNullExpressionValue(destination$iv$iv, "primitiveFqNames.values");
        Iterable $this$associateTo$iv = (Iterable)destination$iv$iv;
        boolean bl4 = false;
        Iterator index$iv$iv232 = $this$associateTo$iv.iterator();
        while (index$iv$iv232.hasNext()) {
            Object element$iv = index$iv$iv232.next();
            Map map = $this$classFqNames_u24lambda_u2d4;
            Object kotlinName = (String)element$iv;
            boolean bl32 = false;
            StringBuilder stringBuilder = new StringBuilder().append("kotlin.jvm.internal.");
            Intrinsics.checkNotNullExpressionValue(kotlinName, "kotlinName");
            kotlinName = TuplesKt.to(stringBuilder.append(StringsKt.substringAfterLast$default((String)kotlinName, '.', null, 2, null)).append("CompanionObject").toString(), Intrinsics.stringPlus((String)kotlinName, ".Companion"));
            map.put(((Pair)kotlinName).getFirst(), ((Pair)kotlinName).getSecond());
        }
        for (Map.Entry entry : FUNCTION_CLASSES.entrySet()) {
            Class klass = (Class)entry.getKey();
            int arity = ((Number)entry.getValue()).intValue();
            $this$classFqNames_u24lambda_u2d4.put(klass.getName(), Intrinsics.stringPlus("kotlin.Function", arity));
        }
        classFqNames = $this$mapIndexed$iv;
        Map $this$mapValues$iv = classFqNames;
        boolean $i$f$mapValues = false;
        Map $i$a$-apply-ClassReference$Companion$classFqNames$232 = $this$mapValues$iv;
        destination$iv$iv = new LinkedHashMap(MapsKt.mapCapacity($this$mapValues$iv.size()));
        boolean bl5 = false;
        Iterable $this$associateByTo$iv$iv$iv = $this$mapValuesTo$iv$iv.entrySet();
        boolean $i$f$associateByTo = false;
        for (Object element$iv$iv$iv : $this$associateByTo$iv$iv$iv) {
            void $dstr$_u24__u24$fqName;
            void it$iv$iv;
            Map.Entry bl32 = (Map.Entry)element$iv$iv$iv;
            Object object2 = destination$iv$iv;
            boolean bl42 = false;
            Object k = it$iv$iv.getKey();
            Map.Entry entry = (Map.Entry)element$iv$iv$iv;
            pair = k;
            object = object2;
            boolean bl52 = false;
            String fqName = (String)$dstr$_u24__u24$fqName.getValue();
            String string = StringsKt.substringAfterLast$default(fqName, '.', null, 2, null);
            object.put(pair, string);
        }
        simpleNames = destination$iv$iv;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public final String getClassSimpleName(@NotNull Class<?> jClass) {
            String string;
            Intrinsics.checkNotNullParameter(jClass, "jClass");
            if (jClass.isAnonymousClass()) {
                string = null;
            } else if (jClass.isLocalClass()) {
                String name = jClass.getSimpleName();
                Method method = jClass.getEnclosingMethod();
                if (method == null) {
                    Constructor<?> constructor = jClass.getEnclosingConstructor();
                    if (constructor == null) {
                        Intrinsics.checkNotNullExpressionValue(name, "name");
                        string = StringsKt.substringAfter$default(name, '$', null, 2, null);
                    } else {
                        Constructor<?> constructor2;
                        Constructor<?> constructor3 = constructor2 = constructor;
                        boolean bl = false;
                        Intrinsics.checkNotNullExpressionValue(name, "name");
                        string = StringsKt.substringAfter$default(name, Intrinsics.stringPlus(constructor3.getName(), "$"), null, 2, null);
                    }
                } else {
                    Method method2;
                    Method method3 = method2 = method;
                    boolean bl = false;
                    Intrinsics.checkNotNullExpressionValue(name, "name");
                    string = StringsKt.substringAfter$default(name, Intrinsics.stringPlus(method3.getName(), "$"), null, 2, null);
                }
            } else if (jClass.isArray()) {
                String string2;
                String string3;
                Class<?> componentType = jClass.getComponentType();
                String string4 = componentType.isPrimitive() ? ((string3 = (String)simpleNames.get(componentType.getName())) == null ? null : Intrinsics.stringPlus(string3, "Array")) : (string2 = null);
                string = string2 == null ? "Array" : string2;
            } else {
                String string5 = (String)simpleNames.get(jClass.getName());
                string = string5 == null ? jClass.getSimpleName() : string5;
            }
            return string;
        }

        @Nullable
        public final String getClassQualifiedName(@NotNull Class<?> jClass) {
            String string;
            Intrinsics.checkNotNullParameter(jClass, "jClass");
            if (jClass.isAnonymousClass()) {
                string = null;
            } else if (jClass.isLocalClass()) {
                string = null;
            } else if (jClass.isArray()) {
                String string2;
                String string3;
                Class<?> componentType = jClass.getComponentType();
                String string4 = componentType.isPrimitive() ? ((string3 = (String)classFqNames.get(componentType.getName())) == null ? null : Intrinsics.stringPlus(string3, "Array")) : (string2 = null);
                string = string2 == null ? "kotlin.Array" : string2;
            } else {
                String string5 = (String)classFqNames.get(jClass.getName());
                string = string5 == null ? jClass.getCanonicalName() : string5;
            }
            return string;
        }

        public final boolean isInstance(@Nullable Object value, @NotNull Class<?> jClass) {
            Intrinsics.checkNotNullParameter(jClass, "jClass");
            Object object = FUNCTION_CLASSES;
            Integer n = (Integer)object.get(jClass);
            if (n != null) {
                object = n;
                int arity = ((Number)object).intValue();
                boolean bl = false;
                return TypeIntrinsics.isFunctionOfArity(value, arity);
            }
            Class<?> objectType = jClass.isPrimitive() ? JvmClassMappingKt.getJavaObjectType(JvmClassMappingKt.getKotlinClass(jClass)) : jClass;
            return objectType.isInstance(value);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

