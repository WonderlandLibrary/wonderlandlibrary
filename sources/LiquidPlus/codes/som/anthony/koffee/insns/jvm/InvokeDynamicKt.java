/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Handle
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InvokeDynamicInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.types.TypesKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\u001aK\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\f\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\r\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\u000e\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aW\u0010\u000f\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u00052\u0006\u0010\u0011\u001a\u00020\u00012\u000e\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\n\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2={"h_invokeinterface", "Lorg/objectweb/asm/Handle;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "owner", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "name", "", "returnType", "parameterTypes", "", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Lorg/objectweb/asm/Handle;", "h_invokespecial", "h_invokestatic", "h_invokevirtual", "invokedynamic", "", "handle", "args", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;Lorg/objectweb/asm/Handle;[Ljava/lang/Object;)V", "koffee"})
public final class InvokeDynamicKt {
    /*
     * WARNING - void declaration
     */
    public static final void invokedynamic(@NotNull InstructionAssembly $this$invokedynamic, @NotNull String name, @NotNull Object returnType, @NotNull Object[] parameterTypes, @NotNull Handle handle, @NotNull Object[] args2) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$invokedynamic, "$this$invokedynamic");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Intrinsics.checkParameterIsNotNull(handle, "handle");
        Intrinsics.checkParameterIsNotNull(args2, "args");
        Object[] objectArray = parameterTypes;
        Type type = TypesKt.coerceType(returnType);
        boolean $i$f$map = false;
        void var9_9 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var12_12 = $this$mapTo$iv$iv;
        int n = ((void)var12_12).length;
        for (int i = 0; i < n; ++i) {
            void p1;
            void item$iv$iv;
            void var16_16 = item$iv$iv = var12_12[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = TypesKt.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        $this$invokedynamic.getInstructions().add((AbstractInsnNode)new InvokeDynamicInsnNode(name, descriptor, handle, Arrays.copyOf(args2, args2.length)));
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final Handle h_invokestatic(@NotNull InstructionAssembly $this$h_invokestatic, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$h_invokestatic, "$this$h_invokestatic");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Object[] objectArray = parameterTypes;
        Type type = TypesKt.coerceType(returnType);
        boolean $i$f$map = false;
        void var8_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var11_11 = $this$mapTo$iv$iv;
        int n = ((void)var11_11).length;
        for (int i = 0; i < n; ++i) {
            void p1;
            void item$iv$iv;
            void var15_15 = item$iv$iv = var11_11[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = TypesKt.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        return new Handle(6, TypesKt.coerceType(owner).getInternalName(), name, descriptor, false);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final Handle h_invokevirtual(@NotNull InstructionAssembly $this$h_invokevirtual, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$h_invokevirtual, "$this$h_invokevirtual");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Object[] objectArray = parameterTypes;
        Type type = TypesKt.coerceType(returnType);
        boolean $i$f$map = false;
        void var8_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var11_11 = $this$mapTo$iv$iv;
        int n = ((void)var11_11).length;
        for (int i = 0; i < n; ++i) {
            void p1;
            void item$iv$iv;
            void var15_15 = item$iv$iv = var11_11[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = TypesKt.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        return new Handle(5, TypesKt.coerceType(owner).getInternalName(), name, descriptor, false);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final Handle h_invokespecial(@NotNull InstructionAssembly $this$h_invokespecial, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$h_invokespecial, "$this$h_invokespecial");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Object[] objectArray = parameterTypes;
        Type type = TypesKt.coerceType(returnType);
        boolean $i$f$map = false;
        void var8_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var11_11 = $this$mapTo$iv$iv;
        int n = ((void)var11_11).length;
        for (int i = 0; i < n; ++i) {
            void p1;
            void item$iv$iv;
            void var15_15 = item$iv$iv = var11_11[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = TypesKt.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        return new Handle(7, TypesKt.coerceType(owner).getInternalName(), name, descriptor, false);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final Handle h_invokeinterface(@NotNull InstructionAssembly $this$h_invokeinterface, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$h_invokeinterface, "$this$h_invokeinterface");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(returnType, "returnType");
        Intrinsics.checkParameterIsNotNull(parameterTypes, "parameterTypes");
        Object[] objectArray = parameterTypes;
        Type type = TypesKt.coerceType(returnType);
        boolean $i$f$map = false;
        void var8_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        void var11_11 = $this$mapTo$iv$iv;
        int n = ((void)var11_11).length;
        for (int i = 0; i < n; ++i) {
            void p1;
            void item$iv$iv;
            void var15_15 = item$iv$iv = var11_11[i];
            collection = destination$iv$iv;
            boolean bl = false;
            Type type2 = TypesKt.coerceType(p1);
            collection.add(type2);
        }
        collection = (List)destination$iv$iv;
        $this$map$iv = collection;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        Type[] typeArray = thisCollection$iv.toArray(new Type[0]);
        if (typeArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        collection = typeArray;
        Type[] typeArray2 = (Type[])collection;
        String descriptor = Type.getMethodDescriptor((Type)type, (Type[])Arrays.copyOf(typeArray2, typeArray2.length));
        return new Handle(9, TypesKt.coerceType(owner).getInternalName(), name, descriptor, true);
    }
}

