/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.LabelNode
 */
package codes.som.anthony.koffee.labels;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\t\u001a\u00020\nH\u0086\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u000b"}, d2={"Lcodes/som/anthony/koffee/labels/KoffeeLabel;", "", "insns", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "label", "Lorg/objectweb/asm/tree/LabelNode;", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Lorg/objectweb/asm/tree/LabelNode;)V", "getLabel$koffee", "()Lorg/objectweb/asm/tree/LabelNode;", "unaryPlus", "", "koffee"})
public final class KoffeeLabel {
    private final InstructionAssembly insns;
    @NotNull
    private final LabelNode label;

    public final void unaryPlus() {
        this.insns.getInstructions().add((AbstractInsnNode)this.label);
    }

    @NotNull
    public final LabelNode getLabel$koffee() {
        return this.label;
    }

    public KoffeeLabel(@NotNull InstructionAssembly insns, @NotNull LabelNode label) {
        Intrinsics.checkParameterIsNotNull(insns, "insns");
        Intrinsics.checkParameterIsNotNull(label, "label");
        this.insns = insns;
        this.label = label;
    }
}

