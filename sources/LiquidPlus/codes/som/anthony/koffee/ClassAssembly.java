/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldNode
 *  org.objectweb.asm.tree.MethodNode
 */
package codes.som.anthony.koffee;

import codes.som.anthony.koffee.MethodAssembly;
import codes.som.anthony.koffee.modifiers.Modifiers;
import codes.som.anthony.koffee.modifiers.abstract;
import codes.som.anthony.koffee.modifiers.annotation;
import codes.som.anthony.koffee.modifiers.bridge;
import codes.som.anthony.koffee.modifiers.deprecated;
import codes.som.anthony.koffee.modifiers.enum;
import codes.som.anthony.koffee.modifiers.final;
import codes.som.anthony.koffee.modifiers.interface;
import codes.som.anthony.koffee.modifiers.mandated;
import codes.som.anthony.koffee.modifiers.module;
import codes.som.anthony.koffee.modifiers.native;
import codes.som.anthony.koffee.modifiers.open;
import codes.som.anthony.koffee.modifiers.package_private;
import codes.som.anthony.koffee.modifiers.private;
import codes.som.anthony.koffee.modifiers.protected;
import codes.som.anthony.koffee.modifiers.public;
import codes.som.anthony.koffee.modifiers.static;
import codes.som.anthony.koffee.modifiers.static_phase;
import codes.som.anthony.koffee.modifiers.strict;
import codes.som.anthony.koffee.modifiers.super;
import codes.som.anthony.koffee.modifiers.synchronized;
import codes.som.anthony.koffee.modifiers.synthetic;
import codes.som.anthony.koffee.modifiers.transient;
import codes.som.anthony.koffee.modifiers.transitive;
import codes.som.anthony.koffee.modifiers.varargs;
import codes.som.anthony.koffee.modifiers.volatile;
import codes.som.anthony.koffee.sugar.ModifiersAccess;
import codes.som.anthony.koffee.sugar.TypesAccess;
import codes.som.anthony.koffee.types.TypesKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B9\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0010\u0010\n\u001a\f\u0012\b\u0012\u00060\fj\u0002`\r0\u000b\u00a2\u0006\u0002\u0010\u000eB\u000f\b\u0000\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\u0002\u0010\u0011J:\u0010-\u001a\u00020.2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\n\u0010/\u001a\u00060\fj\u0002`\r2\n\b\u0002\u00100\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\fJz\u00101\u001a\u0002022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\n\u00103\u001a\u00060\fj\u0002`\r2\u001a\u00104\u001a\u000e\u0012\n\b\u0001\u0012\u00060\fj\u0002`\r05\"\u00060\fj\u0002`\r2\n\b\u0002\u00100\u001a\u0004\u0018\u00010\u00062\u0010\b\u0002\u00106\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u0001052\u0017\u00107\u001a\u0013\u0012\u0004\u0012\u000209\u0012\u0004\u0012\u00020:08\u00a2\u0006\u0002\b;\u00a2\u0006\u0002\u0010<R$\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00048F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R4\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u000b2\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u000b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR$\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020\u00178F\u00a2\u0006\u0006\u001a\u0004\b#\u0010$R(\u0010%\u001a\u0004\u0018\u00010\u00172\b\u0010\u0012\u001a\u0004\u0018\u00010\u00178F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b&\u0010$\"\u0004\b'\u0010(R$\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\b8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,\u00a8\u0006="}, d2={"Lcodes/som/anthony/koffee/ClassAssembly;", "Lcodes/som/anthony/koffee/sugar/ModifiersAccess;", "Lcodes/som/anthony/koffee/sugar/TypesAccess;", "access", "Lcodes/som/anthony/koffee/modifiers/Modifiers;", "name", "", "version", "", "superName", "interfaces", "", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "(Lcodes/som/anthony/koffee/modifiers/Modifiers;Ljava/lang/String;ILjava/lang/String;Ljava/util/List;)V", "node", "Lorg/objectweb/asm/tree/ClassNode;", "(Lorg/objectweb/asm/tree/ClassNode;)V", "value", "getAccess", "()Lcodes/som/anthony/koffee/modifiers/Modifiers;", "setAccess", "(Lcodes/som/anthony/koffee/modifiers/Modifiers;)V", "Lorg/objectweb/asm/Type;", "getInterfaces", "()Ljava/util/List;", "setInterfaces", "(Ljava/util/List;)V", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "getNode", "()Lorg/objectweb/asm/tree/ClassNode;", "self", "getSelf", "()Lorg/objectweb/asm/Type;", "superClass", "getSuperClass", "setSuperClass", "(Lorg/objectweb/asm/Type;)V", "getVersion", "()I", "setVersion", "(I)V", "field", "Lorg/objectweb/asm/tree/FieldNode;", "type", "signature", "method", "Lorg/objectweb/asm/tree/MethodNode;", "returnType", "parameterTypes", "", "exceptions", "routine", "Lkotlin/Function1;", "Lcodes/som/anthony/koffee/MethodAssembly;", "", "Lkotlin/ExtensionFunctionType;", "(Lcodes/som/anthony/koffee/modifiers/Modifiers;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/String;[Lorg/objectweb/asm/Type;Lkotlin/jvm/functions/Function1;)Lorg/objectweb/asm/tree/MethodNode;", "koffee"})
public final class ClassAssembly
implements ModifiersAccess,
TypesAccess {
    @NotNull
    private final ClassNode node;

    @NotNull
    public final Modifiers getAccess() {
        return new Modifiers(this.node.access);
    }

    public final void setAccess(@NotNull Modifiers value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        this.node.access = value.getAccess();
    }

    @NotNull
    public final String getName() {
        String string = this.node.name;
        Intrinsics.checkExpressionValueIsNotNull(string, "node.name");
        return string;
    }

    public final void setName(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        this.node.name = value;
    }

    public final int getVersion() {
        return this.node.version;
    }

    public final void setVersion(int value) {
        this.node.version = value;
    }

    @Nullable
    public final Type getSuperClass() {
        Type type;
        String string = this.node.superName;
        if (string != null) {
            String string2 = string;
            ClassAssembly classAssembly = this;
            boolean bl = false;
            boolean bl2 = false;
            String p1 = string2;
            boolean bl3 = false;
            type = classAssembly.coerceType(p1);
        } else {
            type = null;
        }
        return type;
    }

    public final void setSuperClass(@Nullable Type value) {
        Type type = value;
        this.node.superName = type != null ? type.getInternalName() : null;
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final List<Type> getInterfaces() {
        List list;
        List list2 = this.node.interfaces;
        if (list2 != null) {
            void $this$mapTo$iv$iv;
            void $this$map$iv;
            Iterable iterable = list2;
            ClassAssembly classAssembly = this;
            boolean $i$f$map = false;
            void var4_4 = $this$map$iv;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            Iterator iterator2 = $this$mapTo$iv$iv.iterator();
            while (iterator2.hasNext()) {
                void p1;
                Object item$iv$iv;
                Object t = item$iv$iv = iterator2.next();
                Collection collection = destination$iv$iv;
                boolean bl = false;
                Type type = classAssembly.coerceType(p1);
                collection.add(type);
            }
            list = (List)destination$iv$iv;
        } else {
            list = null;
        }
        return list;
    }

    /*
     * WARNING - void declaration
     */
    public final void setInterfaces(@Nullable List<Type> value) {
        List list;
        ClassNode classNode = this.node;
        List<Type> list2 = value;
        if (list2 != null) {
            Collection<String> collection;
            void $this$mapTo$iv$iv;
            void $this$map$iv;
            Iterable iterable = list2;
            ClassNode classNode2 = classNode;
            boolean $i$f$map = false;
            void var4_5 = $this$map$iv;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            for (Object item$iv$iv : $this$mapTo$iv$iv) {
                void it;
                Type type = (Type)item$iv$iv;
                collection = destination$iv$iv;
                boolean bl = false;
                String string = it.getInternalName();
                collection.add(string);
            }
            collection = (List)destination$iv$iv;
            classNode = classNode2;
            list = collection;
        } else {
            list = null;
        }
        classNode.interfaces = list;
    }

    @NotNull
    public final Type getSelf() {
        String string = this.node.name;
        Intrinsics.checkExpressionValueIsNotNull(string, "node.name");
        return this.coerceType(string);
    }

    @NotNull
    public final FieldNode field(@NotNull Modifiers access, @NotNull String name, @NotNull Object type, @Nullable String signature, @Nullable Object value) {
        Intrinsics.checkParameterIsNotNull(access, "access");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(type, "type");
        FieldNode fieldNode = new FieldNode(458752, access.getAccess(), name, this.coerceType(type).getDescriptor(), signature, value);
        this.node.fields.add(fieldNode);
        return fieldNode;
    }

    @NotNull
    public static /* synthetic */ FieldNode field$default(ClassAssembly classAssembly, Modifiers modifiers, String string, Object object, String string2, Object object2, int n, Object object3) {
        if ((n & 8) != 0) {
            string2 = null;
        }
        if ((n & 0x10) != 0) {
            object2 = null;
        }
        return classAssembly.field(modifiers, string, object, string2, object2);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final MethodNode method(@NotNull Modifiers access, @NotNull String name, @NotNull Object returnType, @NotNull Object[] parameterTypes, @Nullable String signature, @Nullable Type[] exceptions, @NotNull Function1<? super MethodAssembly, Unit> routine) {
        String[] stringArray;
        void $this$toTypedArray$iv;
        void p1;
        Collection<Type> collection;
        void item$iv$iv;
        int n;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull(access, "access");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Intrinsics.checkParameterIsNotNull(routine, "routine");
        Object[] objectArray = parameterTypes;
        ClassAssembly classAssembly = this;
        Type type = this.coerceType(returnType);
        boolean $i$f$map = false;
        void var12_17 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var15_20 = $this$mapTo$iv$iv;
        int n2 = ((void)var15_20).length;
        for (n = 0; n < n2; ++n) {
            void var19_24 = item$iv$iv = var15_20[n];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = classAssembly.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray2 = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        int n3 = 458752;
        int n4 = access.getAccess();
        String string = name;
        String string2 = descriptor;
        String string3 = signature;
        if (exceptions != null) {
            void $this$toTypedArray$iv2;
            Collection<String> collection2;
            Collection $this$map$iv2;
            Type[] $i$f$toTypedArray2 = exceptions;
            String string4 = string3;
            String string5 = string2;
            String string6 = string;
            int n5 = n4;
            int n6 = n3;
            $i$f$map = false;
            $this$mapTo$iv$iv = $this$map$iv2;
            destination$iv$iv = new ArrayList(((void)$this$map$iv2).length);
            $i$f$mapTo = false;
            var15_20 = $this$mapTo$iv$iv;
            n2 = ((void)var15_20).length;
            for (n = 0; n < n2; ++n) {
                void it;
                p1 = item$iv$iv = var15_20[n];
                collection2 = destination$iv$iv;
                boolean bl = false;
                String string7 = it.getInternalName();
                collection2.add(string7);
            }
            collection2 = (List)destination$iv$iv;
            $this$map$iv2 = collection2;
            boolean $i$f$toTypedArray3 = false;
            void thisCollection$iv2 = $this$toTypedArray$iv2;
            String[] stringArray2 = thisCollection$iv2.toArray(new String[0]);
            if (stringArray2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            collection2 = stringArray2;
            n3 = n6;
            n4 = n5;
            string = string6;
            string2 = string5;
            string3 = string4;
            stringArray = (String[])collection2;
        } else {
            stringArray = null;
        }
        String[] stringArray3 = stringArray;
        String string8 = string3;
        String string9 = string2;
        String string10 = string;
        int n7 = n4;
        int n8 = n3;
        MethodNode methodNode = new MethodNode(n8, n7, string10, string9, string8, stringArray3);
        MethodAssembly methodAssembly = new MethodAssembly(methodNode);
        routine.invoke(methodAssembly);
        this.node.methods.add(methodNode);
        return methodNode;
    }

    @NotNull
    public static /* synthetic */ MethodNode method$default(ClassAssembly classAssembly, Modifiers modifiers, String string, Object object, Object[] objectArray, String string2, Type[] typeArray, Function1 function1, int n, Object object2) {
        if ((n & 0x10) != 0) {
            string2 = null;
        }
        if ((n & 0x20) != 0) {
            typeArray = null;
        }
        return classAssembly.method(modifiers, string, object, objectArray, string2, typeArray, function1);
    }

    @NotNull
    public final ClassNode getNode() {
        return this.node;
    }

    public ClassAssembly(@NotNull ClassNode node) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        this.node = node;
    }

    /*
     * WARNING - void declaration
     */
    public ClassAssembly(@NotNull Modifiers access, @NotNull String name, int version, @NotNull String superName, @NotNull List<? extends Object> interfaces) {
        Collection<String> collection;
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(access, "access");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(superName, "superName");
        Intrinsics.checkParameterIsNotNull(interfaces, "interfaces");
        ClassNode classNode = new ClassNode(458752);
        ClassAssembly classAssembly = this;
        boolean bl = false;
        boolean bl2 = false;
        ClassNode it = classNode;
        boolean bl3 = false;
        it.access = access.getAccess();
        it.name = name;
        it.version = version;
        it.superName = superName;
        Iterable iterable = interfaces;
        ClassNode classNode2 = it;
        boolean $i$f$map = false;
        void var14_15 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        Iterator iterator2 = $this$mapTo$iv$iv.iterator();
        while (iterator2.hasNext()) {
            void type;
            Object item$iv$iv;
            Object t = item$iv$iv = iterator2.next();
            collection = destination$iv$iv;
            boolean bl4 = false;
            String string = TypesKt.coerceType(type).getInternalName();
            collection.add(string);
        }
        collection = (List)destination$iv$iv;
        classNode2.interfaces = collection;
        ClassNode classNode3 = classNode;
        classAssembly(classNode3);
    }

    @Override
    @NotNull
    public package_private getPackage_private() {
        return ModifiersAccess.DefaultImpls.getPackage_private(this);
    }

    @Override
    @NotNull
    public public getPublic() {
        return ModifiersAccess.DefaultImpls.getPublic(this);
    }

    @Override
    @NotNull
    public private getPrivate() {
        return ModifiersAccess.DefaultImpls.getPrivate(this);
    }

    @Override
    @NotNull
    public protected getProtected() {
        return ModifiersAccess.DefaultImpls.getProtected(this);
    }

    @Override
    @NotNull
    public static getStatic() {
        return ModifiersAccess.DefaultImpls.getStatic(this);
    }

    @Override
    @NotNull
    public final getFinal() {
        return ModifiersAccess.DefaultImpls.getFinal(this);
    }

    @Override
    @NotNull
    public super getSuper() {
        return ModifiersAccess.DefaultImpls.getSuper(this);
    }

    @Override
    @NotNull
    public super get_super() {
        return ModifiersAccess.DefaultImpls.get_super(this);
    }

    @Override
    @NotNull
    public synchronized getSynchronized() {
        return ModifiersAccess.DefaultImpls.getSynchronized(this);
    }

    @Override
    @NotNull
    public open getOpen() {
        return ModifiersAccess.DefaultImpls.getOpen(this);
    }

    @Override
    @NotNull
    public transitive getTransitive() {
        return ModifiersAccess.DefaultImpls.getTransitive(this);
    }

    @Override
    @NotNull
    public volatile getVolatile() {
        return ModifiersAccess.DefaultImpls.getVolatile(this);
    }

    @Override
    @NotNull
    public bridge getBridge() {
        return ModifiersAccess.DefaultImpls.getBridge(this);
    }

    @Override
    @NotNull
    public static_phase getStatic_phase() {
        return ModifiersAccess.DefaultImpls.getStatic_phase(this);
    }

    @Override
    @NotNull
    public varargs getVarargs() {
        return ModifiersAccess.DefaultImpls.getVarargs(this);
    }

    @Override
    @NotNull
    public transient getTransient() {
        return ModifiersAccess.DefaultImpls.getTransient(this);
    }

    @Override
    @NotNull
    public native getNative() {
        return ModifiersAccess.DefaultImpls.getNative(this);
    }

    @Override
    @NotNull
    public interface getInterface() {
        return ModifiersAccess.DefaultImpls.getInterface(this);
    }

    @Override
    @NotNull
    public interface get_interface() {
        return ModifiersAccess.DefaultImpls.get_interface(this);
    }

    @Override
    @NotNull
    public abstract getAbstract() {
        return ModifiersAccess.DefaultImpls.getAbstract(this);
    }

    @Override
    @NotNull
    public strict getStrict() {
        return ModifiersAccess.DefaultImpls.getStrict(this);
    }

    @Override
    @NotNull
    public synthetic getSynthetic() {
        return ModifiersAccess.DefaultImpls.getSynthetic(this);
    }

    @Override
    @NotNull
    public annotation getAnnotation() {
        return ModifiersAccess.DefaultImpls.getAnnotation(this);
    }

    @Override
    @NotNull
    public enum getEnum() {
        return ModifiersAccess.DefaultImpls.getEnum(this);
    }

    @Override
    @NotNull
    public mandated getMandated() {
        return ModifiersAccess.DefaultImpls.getMandated(this);
    }

    @Override
    @NotNull
    public module getModule() {
        return ModifiersAccess.DefaultImpls.getModule(this);
    }

    @Override
    @NotNull
    public deprecated getDeprecated() {
        return ModifiersAccess.DefaultImpls.getDeprecated(this);
    }

    @Override
    public Type getVoid() {
        return TypesAccess.DefaultImpls.getVoid(this);
    }

    @Override
    public Type getChar() {
        return TypesAccess.DefaultImpls.getChar(this);
    }

    @Override
    public Type getByte() {
        return TypesAccess.DefaultImpls.getByte(this);
    }

    @Override
    public Type getShort() {
        return TypesAccess.DefaultImpls.getShort(this);
    }

    @Override
    public Type getInt() {
        return TypesAccess.DefaultImpls.getInt(this);
    }

    @Override
    public Type getFloat() {
        return TypesAccess.DefaultImpls.getFloat(this);
    }

    @Override
    public Type getLong() {
        return TypesAccess.DefaultImpls.getLong(this);
    }

    @Override
    public Type getDouble() {
        return TypesAccess.DefaultImpls.getDouble(this);
    }

    @Override
    public Type getBoolean() {
        return TypesAccess.DefaultImpls.getBoolean(this);
    }

    @Override
    @NotNull
    public Type coerceType(@NotNull Object value) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        return TypesAccess.DefaultImpls.coerceType(this, value);
    }
}

