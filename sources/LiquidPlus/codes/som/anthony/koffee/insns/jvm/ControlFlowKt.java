/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.labels.LabelsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000$\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\b\n\u0000\u001a\u0016\u0010\u001e\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010\"\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010#\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010$\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010%\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010&\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010'\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010(\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010)\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010*\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010+\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010,\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010-\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010.\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u0010/\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u00100\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u00101\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0016\u00102\u001a\u00020\u0001*\u00020\u00032\n\u0010\u001f\u001a\u00060 j\u0002`!\u001a\u0012\u00103\u001a\u00020\u0001*\u00020\u00032\u0006\u00104\u001a\u000205\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0019\u0010\b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\"\u0019\u0010\n\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\"\u0019\u0010\f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\"\u0019\u0010\u000e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\"\u0019\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005\"\u0019\u0010\u0012\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005\"\u0019\u0010\u0014\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005\"\u0019\u0010\u0016\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005\"\u0019\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\"\u0019\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005\"\u0019\u0010\u001c\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005\u00a8\u00066"}, d2={"_return", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "get_return", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "areturn", "getAreturn", "athrow", "getAthrow", "dcmpg", "getDcmpg", "dcmpl", "getDcmpl", "dreturn", "getDreturn", "fcmpg", "getFcmpg", "fcmpl", "getFcmpl", "freturn", "getFreturn", "ireturn", "getIreturn", "lcmp", "getLcmp", "lreturn", "getLreturn", "return", "getReturn", "goto", "label", "", "Lcodes/som/anthony/koffee/labels/LabelLike;", "if_acmpeq", "if_acmpne", "if_icmpeq", "if_icmpge", "if_icmpgt", "if_icmple", "if_icmplt", "if_icmpne", "ifeq", "ifge", "ifgt", "ifle", "iflt", "ifne", "ifnonnull", "ifnull", "jsr", "ret", "slot", "", "koffee"})
public final class ControlFlowKt {
    @NotNull
    public static final Unit getIreturn(@NotNull InstructionAssembly $this$ireturn) {
        Intrinsics.checkParameterIsNotNull($this$ireturn, "$this$ireturn");
        $this$ireturn.getInstructions().add((AbstractInsnNode)new InsnNode(172));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLreturn(@NotNull InstructionAssembly $this$lreturn) {
        Intrinsics.checkParameterIsNotNull($this$lreturn, "$this$lreturn");
        $this$lreturn.getInstructions().add((AbstractInsnNode)new InsnNode(173));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFreturn(@NotNull InstructionAssembly $this$freturn) {
        Intrinsics.checkParameterIsNotNull($this$freturn, "$this$freturn");
        $this$freturn.getInstructions().add((AbstractInsnNode)new InsnNode(174));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDreturn(@NotNull InstructionAssembly $this$dreturn) {
        Intrinsics.checkParameterIsNotNull($this$dreturn, "$this$dreturn");
        $this$dreturn.getInstructions().add((AbstractInsnNode)new InsnNode(175));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAreturn(@NotNull InstructionAssembly $this$areturn) {
        Intrinsics.checkParameterIsNotNull($this$areturn, "$this$areturn");
        $this$areturn.getInstructions().add((AbstractInsnNode)new InsnNode(176));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getReturn(@NotNull InstructionAssembly $this$return) {
        Intrinsics.checkParameterIsNotNull($this$return, "$this$return");
        $this$return.getInstructions().add((AbstractInsnNode)new InsnNode(177));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit get_return(@NotNull InstructionAssembly $this$_return) {
        Intrinsics.checkParameterIsNotNull($this$_return, "$this$_return");
        return ControlFlowKt.getReturn($this$_return);
    }

    @NotNull
    public static final Unit getLcmp(@NotNull InstructionAssembly $this$lcmp) {
        Intrinsics.checkParameterIsNotNull($this$lcmp, "$this$lcmp");
        $this$lcmp.getInstructions().add((AbstractInsnNode)new InsnNode(148));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFcmpl(@NotNull InstructionAssembly $this$fcmpl) {
        Intrinsics.checkParameterIsNotNull($this$fcmpl, "$this$fcmpl");
        $this$fcmpl.getInstructions().add((AbstractInsnNode)new InsnNode(149));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFcmpg(@NotNull InstructionAssembly $this$fcmpg) {
        Intrinsics.checkParameterIsNotNull($this$fcmpg, "$this$fcmpg");
        $this$fcmpg.getInstructions().add((AbstractInsnNode)new InsnNode(150));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDcmpl(@NotNull InstructionAssembly $this$dcmpl) {
        Intrinsics.checkParameterIsNotNull($this$dcmpl, "$this$dcmpl");
        $this$dcmpl.getInstructions().add((AbstractInsnNode)new InsnNode(151));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDcmpg(@NotNull InstructionAssembly $this$dcmpg) {
        Intrinsics.checkParameterIsNotNull($this$dcmpg, "$this$dcmpg");
        $this$dcmpg.getInstructions().add((AbstractInsnNode)new InsnNode(152));
        return Unit.INSTANCE;
    }

    public static final void ifeq(@NotNull InstructionAssembly $this$ifeq, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifeq, "$this$ifeq");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifeq.getInstructions().add((AbstractInsnNode)new JumpInsnNode(153, LabelsKt.coerceLabel(label)));
    }

    public static final void ifne(@NotNull InstructionAssembly $this$ifne, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifne, "$this$ifne");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifne.getInstructions().add((AbstractInsnNode)new JumpInsnNode(154, LabelsKt.coerceLabel(label)));
    }

    public static final void iflt(@NotNull InstructionAssembly $this$iflt, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$iflt, "$this$iflt");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$iflt.getInstructions().add((AbstractInsnNode)new JumpInsnNode(155, LabelsKt.coerceLabel(label)));
    }

    public static final void ifge(@NotNull InstructionAssembly $this$ifge, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifge, "$this$ifge");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifge.getInstructions().add((AbstractInsnNode)new JumpInsnNode(156, LabelsKt.coerceLabel(label)));
    }

    public static final void ifgt(@NotNull InstructionAssembly $this$ifgt, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifgt, "$this$ifgt");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifgt.getInstructions().add((AbstractInsnNode)new JumpInsnNode(157, LabelsKt.coerceLabel(label)));
    }

    public static final void ifle(@NotNull InstructionAssembly $this$ifle, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifle, "$this$ifle");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifle.getInstructions().add((AbstractInsnNode)new JumpInsnNode(158, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmpeq(@NotNull InstructionAssembly $this$if_icmpeq, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmpeq, "$this$if_icmpeq");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmpeq.getInstructions().add((AbstractInsnNode)new JumpInsnNode(159, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmpne(@NotNull InstructionAssembly $this$if_icmpne, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmpne, "$this$if_icmpne");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmpne.getInstructions().add((AbstractInsnNode)new JumpInsnNode(160, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmplt(@NotNull InstructionAssembly $this$if_icmplt, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmplt, "$this$if_icmplt");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmplt.getInstructions().add((AbstractInsnNode)new JumpInsnNode(161, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmpge(@NotNull InstructionAssembly $this$if_icmpge, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmpge, "$this$if_icmpge");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmpge.getInstructions().add((AbstractInsnNode)new JumpInsnNode(162, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmpgt(@NotNull InstructionAssembly $this$if_icmpgt, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmpgt, "$this$if_icmpgt");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmpgt.getInstructions().add((AbstractInsnNode)new JumpInsnNode(163, LabelsKt.coerceLabel(label)));
    }

    public static final void if_icmple(@NotNull InstructionAssembly $this$if_icmple, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_icmple, "$this$if_icmple");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_icmple.getInstructions().add((AbstractInsnNode)new JumpInsnNode(164, LabelsKt.coerceLabel(label)));
    }

    public static final void if_acmpeq(@NotNull InstructionAssembly $this$if_acmpeq, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_acmpeq, "$this$if_acmpeq");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_acmpeq.getInstructions().add((AbstractInsnNode)new JumpInsnNode(165, LabelsKt.coerceLabel(label)));
    }

    public static final void if_acmpne(@NotNull InstructionAssembly $this$if_acmpne, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$if_acmpne, "$this$if_acmpne");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$if_acmpne.getInstructions().add((AbstractInsnNode)new JumpInsnNode(166, LabelsKt.coerceLabel(label)));
    }

    public static final void goto(@NotNull InstructionAssembly $this$goto, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$goto, "$this$goto");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$goto.getInstructions().add((AbstractInsnNode)new JumpInsnNode(167, LabelsKt.coerceLabel(label)));
    }

    public static final void ifnull(@NotNull InstructionAssembly $this$ifnull, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifnull, "$this$ifnull");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifnull.getInstructions().add((AbstractInsnNode)new JumpInsnNode(198, LabelsKt.coerceLabel(label)));
    }

    public static final void ifnonnull(@NotNull InstructionAssembly $this$ifnonnull, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$ifnonnull, "$this$ifnonnull");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$ifnonnull.getInstructions().add((AbstractInsnNode)new JumpInsnNode(199, LabelsKt.coerceLabel(label)));
    }

    public static final void jsr(@NotNull InstructionAssembly $this$jsr, @NotNull Object label) {
        Intrinsics.checkParameterIsNotNull($this$jsr, "$this$jsr");
        Intrinsics.checkParameterIsNotNull(label, "label");
        $this$jsr.getInstructions().add((AbstractInsnNode)new JumpInsnNode(168, LabelsKt.coerceLabel(label)));
    }

    public static final void ret(@NotNull InstructionAssembly $this$ret, int slot) {
        Intrinsics.checkParameterIsNotNull($this$ret, "$this$ret");
        $this$ret.getInstructions().add((AbstractInsnNode)new VarInsnNode(169, slot));
    }

    @NotNull
    public static final Unit getAthrow(@NotNull InstructionAssembly $this$athrow) {
        Intrinsics.checkParameterIsNotNull($this$athrow, "$this$athrow");
        $this$athrow.getInstructions().add((AbstractInsnNode)new InsnNode(191));
        return Unit.INSTANCE;
    }
}

