/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.IntInsnNode
 *  org.objectweb.asm.tree.LdcInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000 \n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a\u0012\u0010\"\u001a\u00020\u0001*\u00020\u00032\u0006\u0010#\u001a\u00020$\u001a\u0012\u0010%\u001a\u00020\u0001*\u00020\u00032\u0006\u0010#\u001a\u00020&\u001a\u0012\u0010'\u001a\u00020\u0001*\u00020\u00032\u0006\u0010#\u001a\u00020$\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0019\u0010\b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\"\u0019\u0010\n\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\"\u0019\u0010\f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\"\u0019\u0010\u000e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\"\u0019\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005\"\u0019\u0010\u0012\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005\"\u0019\u0010\u0014\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005\"\u0019\u0010\u0016\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005\"\u0019\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\"\u0019\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005\"\u0019\u0010\u001c\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005\"\u0019\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0005\"\u0019\u0010 \u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0005\u00a8\u0006("}, d2={"aconst_null", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "getAconst_null", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "dconst_0", "getDconst_0", "dconst_1", "getDconst_1", "fconst_0", "getFconst_0", "fconst_1", "getFconst_1", "fconst_2", "getFconst_2", "iconst_0", "getIconst_0", "iconst_1", "getIconst_1", "iconst_2", "getIconst_2", "iconst_3", "getIconst_3", "iconst_4", "getIconst_4", "iconst_5", "getIconst_5", "iconst_m1", "getIconst_m1", "lconst_0", "getLconst_0", "lconst_1", "getLconst_1", "bipush", "v", "", "ldc", "", "sipush", "koffee"})
public final class ConstantsKt {
    @NotNull
    public static final Unit getAconst_null(@NotNull InstructionAssembly $this$aconst_null) {
        Intrinsics.checkParameterIsNotNull($this$aconst_null, "$this$aconst_null");
        $this$aconst_null.getInstructions().add((AbstractInsnNode)new InsnNode(1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_m1(@NotNull InstructionAssembly $this$iconst_m1) {
        Intrinsics.checkParameterIsNotNull($this$iconst_m1, "$this$iconst_m1");
        $this$iconst_m1.getInstructions().add((AbstractInsnNode)new InsnNode(2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_0(@NotNull InstructionAssembly $this$iconst_0) {
        Intrinsics.checkParameterIsNotNull($this$iconst_0, "$this$iconst_0");
        $this$iconst_0.getInstructions().add((AbstractInsnNode)new InsnNode(3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_1(@NotNull InstructionAssembly $this$iconst_1) {
        Intrinsics.checkParameterIsNotNull($this$iconst_1, "$this$iconst_1");
        $this$iconst_1.getInstructions().add((AbstractInsnNode)new InsnNode(4));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_2(@NotNull InstructionAssembly $this$iconst_2) {
        Intrinsics.checkParameterIsNotNull($this$iconst_2, "$this$iconst_2");
        $this$iconst_2.getInstructions().add((AbstractInsnNode)new InsnNode(5));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_3(@NotNull InstructionAssembly $this$iconst_3) {
        Intrinsics.checkParameterIsNotNull($this$iconst_3, "$this$iconst_3");
        $this$iconst_3.getInstructions().add((AbstractInsnNode)new InsnNode(6));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_4(@NotNull InstructionAssembly $this$iconst_4) {
        Intrinsics.checkParameterIsNotNull($this$iconst_4, "$this$iconst_4");
        $this$iconst_4.getInstructions().add((AbstractInsnNode)new InsnNode(7));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIconst_5(@NotNull InstructionAssembly $this$iconst_5) {
        Intrinsics.checkParameterIsNotNull($this$iconst_5, "$this$iconst_5");
        $this$iconst_5.getInstructions().add((AbstractInsnNode)new InsnNode(8));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLconst_0(@NotNull InstructionAssembly $this$lconst_0) {
        Intrinsics.checkParameterIsNotNull($this$lconst_0, "$this$lconst_0");
        $this$lconst_0.getInstructions().add((AbstractInsnNode)new InsnNode(9));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLconst_1(@NotNull InstructionAssembly $this$lconst_1) {
        Intrinsics.checkParameterIsNotNull($this$lconst_1, "$this$lconst_1");
        $this$lconst_1.getInstructions().add((AbstractInsnNode)new InsnNode(10));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFconst_0(@NotNull InstructionAssembly $this$fconst_0) {
        Intrinsics.checkParameterIsNotNull($this$fconst_0, "$this$fconst_0");
        $this$fconst_0.getInstructions().add((AbstractInsnNode)new InsnNode(11));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFconst_1(@NotNull InstructionAssembly $this$fconst_1) {
        Intrinsics.checkParameterIsNotNull($this$fconst_1, "$this$fconst_1");
        $this$fconst_1.getInstructions().add((AbstractInsnNode)new InsnNode(12));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFconst_2(@NotNull InstructionAssembly $this$fconst_2) {
        Intrinsics.checkParameterIsNotNull($this$fconst_2, "$this$fconst_2");
        $this$fconst_2.getInstructions().add((AbstractInsnNode)new InsnNode(13));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDconst_0(@NotNull InstructionAssembly $this$dconst_0) {
        Intrinsics.checkParameterIsNotNull($this$dconst_0, "$this$dconst_0");
        $this$dconst_0.getInstructions().add((AbstractInsnNode)new InsnNode(14));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDconst_1(@NotNull InstructionAssembly $this$dconst_1) {
        Intrinsics.checkParameterIsNotNull($this$dconst_1, "$this$dconst_1");
        $this$dconst_1.getInstructions().add((AbstractInsnNode)new InsnNode(15));
        return Unit.INSTANCE;
    }

    public static final void bipush(@NotNull InstructionAssembly $this$bipush, int v) {
        Intrinsics.checkParameterIsNotNull($this$bipush, "$this$bipush");
        $this$bipush.getInstructions().add((AbstractInsnNode)new IntInsnNode(16, v));
    }

    public static final void sipush(@NotNull InstructionAssembly $this$sipush, int v) {
        Intrinsics.checkParameterIsNotNull($this$sipush, "$this$sipush");
        $this$sipush.getInstructions().add((AbstractInsnNode)new IntInsnNode(17, v));
    }

    public static final void ldc(@NotNull InstructionAssembly $this$ldc, @NotNull Object v) {
        Intrinsics.checkParameterIsNotNull($this$ldc, "$this$ldc");
        Intrinsics.checkParameterIsNotNull(v, "v");
        $this$ldc.getInstructions().add((AbstractInsnNode)new LdcInsnNode(v));
    }
}

