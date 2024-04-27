/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.FieldInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.types.TypesKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u001a*\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u0005\u001a*\u0010\t\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u0005\u001a*\u0010\n\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u0005\u001a*\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\u0004j\u0002`\u0005\u00a8\u0006\f"}, d2={"getfield", "", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "owner", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "name", "", "type", "getstatic", "putfield", "putstatic", "koffee"})
public final class FieldsKt {
    public static final void getstatic(@NotNull InstructionAssembly $this$getstatic, @NotNull Object owner, @NotNull String name, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$getstatic, "$this$getstatic");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$getstatic.getInstructions().add((AbstractInsnNode)new FieldInsnNode(178, TypesKt.coerceType(owner).getInternalName(), name, TypesKt.coerceType(type).getDescriptor()));
    }

    public static final void getfield(@NotNull InstructionAssembly $this$getfield, @NotNull Object owner, @NotNull String name, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$getfield, "$this$getfield");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$getfield.getInstructions().add((AbstractInsnNode)new FieldInsnNode(180, TypesKt.coerceType(owner).getInternalName(), name, TypesKt.coerceType(type).getDescriptor()));
    }

    public static final void putstatic(@NotNull InstructionAssembly $this$putstatic, @NotNull Object owner, @NotNull String name, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$putstatic, "$this$putstatic");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$putstatic.getInstructions().add((AbstractInsnNode)new FieldInsnNode(179, TypesKt.coerceType(owner).getInternalName(), name, TypesKt.coerceType(type).getDescriptor()));
    }

    public static final void putfield(@NotNull InstructionAssembly $this$putfield, @NotNull Object owner, @NotNull String name, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$putfield, "$this$putfield");
        Intrinsics.checkParameterIsNotNull(owner, "owner");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$putfield.getInstructions().add((AbstractInsnNode)new FieldInsnNode(181, TypesKt.coerceType(owner).getInternalName(), name, TypesKt.coerceType(type).getDescriptor()));
    }
}

