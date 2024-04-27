/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\u00a8\u0006\b"}, d2={"monitorenter", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "getMonitorenter", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "monitorexit", "getMonitorexit", "koffee"})
public final class SynchronizationKt {
    @NotNull
    public static final Unit getMonitorenter(@NotNull InstructionAssembly $this$monitorenter) {
        Intrinsics.checkParameterIsNotNull($this$monitorenter, "$this$monitorenter");
        $this$monitorenter.getInstructions().add((AbstractInsnNode)new InsnNode(194));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getMonitorexit(@NotNull InstructionAssembly $this$monitorexit) {
        Intrinsics.checkParameterIsNotNull($this$monitorexit, "$this$monitorexit");
        $this$monitorexit.getInstructions().add((AbstractInsnNode)new InsnNode(195));
        return Unit.INSTANCE;
    }
}

