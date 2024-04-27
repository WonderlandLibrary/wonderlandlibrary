/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
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
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0005\u001aK\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\f\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\r\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u001aK\u0010\u000e\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\t\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\n\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\u000f"}, d2={"invokeinterface", "", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "owner", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "name", "", "returnType", "parameterTypes", "", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)V", "invokespecial", "invokestatic", "invokevirtual", "koffee"})
public final class MethodsKt {
    /*
     * WARNING - void declaration
     */
    public static final void invokevirtual(@NotNull InstructionAssembly $this$invokevirtual, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$invokevirtual, "$this$invokevirtual");
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
        $this$invokevirtual.getInstructions().add((AbstractInsnNode)new MethodInsnNode(182, TypesKt.coerceType(owner).getInternalName(), name, descriptor));
    }

    /*
     * WARNING - void declaration
     */
    public static final void invokespecial(@NotNull InstructionAssembly $this$invokespecial, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$invokespecial, "$this$invokespecial");
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
        $this$invokespecial.getInstructions().add((AbstractInsnNode)new MethodInsnNode(183, TypesKt.coerceType(owner).getInternalName(), name, descriptor));
    }

    /*
     * WARNING - void declaration
     */
    public static final void invokestatic(@NotNull InstructionAssembly $this$invokestatic, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$invokestatic, "$this$invokestatic");
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
        $this$invokestatic.getInstructions().add((AbstractInsnNode)new MethodInsnNode(184, TypesKt.coerceType(owner).getInternalName(), name, descriptor));
    }

    /*
     * WARNING - void declaration
     */
    public static final void invokeinterface(@NotNull InstructionAssembly $this$invokeinterface, @NotNull Object owner, @NotNull String name, @NotNull Object returnType, Object ... parameterTypes) {
        void $this$toTypedArray$iv;
        Collection<Type> collection;
        void $this$mapTo$iv$iv;
        Collection $this$map$iv;
        Intrinsics.checkParameterIsNotNull($this$invokeinterface, "$this$invokeinterface");
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
        $this$invokeinterface.getInstructions().add((AbstractInsnNode)new MethodInsnNode(185, TypesKt.coerceType(owner).getInternalName(), name, descriptor));
    }
}

