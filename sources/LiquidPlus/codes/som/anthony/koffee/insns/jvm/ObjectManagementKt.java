/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.TypeInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.types.TypesKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005\u001a\u0016\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005\u001a\u0016\u0010\u0007\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005\u00a8\u0006\b"}, d2={"checkcast", "", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "type", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "instanceof", "new", "koffee"})
public final class ObjectManagementKt {
    public static final void new(@NotNull InstructionAssembly $this$new, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$new, "$this$new");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$new.getInstructions().add((AbstractInsnNode)new TypeInsnNode(187, TypesKt.coerceType(type).getInternalName()));
    }

    public static final void checkcast(@NotNull InstructionAssembly $this$checkcast, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$checkcast, "$this$checkcast");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$checkcast.getInstructions().add((AbstractInsnNode)new TypeInsnNode(192, TypesKt.coerceType(type).getInternalName()));
    }

    public static final void instanceof(@NotNull InstructionAssembly $this$instanceof, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$instanceof, "$this$instanceof");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$instanceof.getInstructions().add((AbstractInsnNode)new TypeInsnNode(193, TypesKt.coerceType(type).getInternalName()));
    }
}

