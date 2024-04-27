/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.TryCatchContainer;
import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.insns.jvm.GuardAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a@\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0010\b\u0000\u0010\u0002*\u00020\u0003*\u00020\u0004*\u00020\u0005*\u0002H\u00022\u0017\u0010\u0006\u001a\u0013\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\b\t\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2={"guard", "Lcodes/som/anthony/koffee/insns/jvm/GuardAssembly;", "T", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "Lcodes/som/anthony/koffee/TryCatchContainer;", "Lcodes/som/anthony/koffee/labels/LabelScope;", "routine", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Lkotlin/jvm/functions/Function1;)Lcodes/som/anthony/koffee/insns/jvm/GuardAssembly;", "koffee"})
public final class TryCatchBlocksKt {
    @NotNull
    public static final <T extends InstructionAssembly & TryCatchContainer> GuardAssembly<T> guard(@NotNull T $this$guard, @NotNull Function1<? super T, Unit> routine) {
        Intrinsics.checkParameterIsNotNull($this$guard, "$this$guard");
        Intrinsics.checkParameterIsNotNull(routine, "routine");
        LabelNode startNode = new LabelNode();
        LabelNode endNode = new LabelNode();
        LabelNode exitNode = new LabelNode();
        $this$guard.getInstructions().add((AbstractInsnNode)startNode);
        routine.invoke($this$guard);
        $this$guard.getInstructions().add((AbstractInsnNode)new JumpInsnNode(167, exitNode));
        $this$guard.getInstructions().add((AbstractInsnNode)endNode);
        $this$guard.getInstructions().add((AbstractInsnNode)exitNode);
        return new GuardAssembly<T>($this$guard, startNode, endNode, exitNode);
    }
}

