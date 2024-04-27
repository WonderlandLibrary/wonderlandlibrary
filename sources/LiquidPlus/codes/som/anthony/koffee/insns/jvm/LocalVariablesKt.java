/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u001a\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\bR\n\u0002\u0010\b\n\u0002\b\n\u001a\u0012\u0010T\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010W\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010X\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010Y\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010Z\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010[\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010\\\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010]\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010^\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\u001a\u0012\u0010_\u001a\u00020\u0001*\u00020\u00032\u0006\u0010U\u001a\u00020V\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0019\u0010\b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\"\u0019\u0010\n\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\"\u0019\u0010\f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\"\u0019\u0010\u000e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\"\u0019\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005\"\u0019\u0010\u0012\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005\"\u0019\u0010\u0014\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005\"\u0019\u0010\u0016\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005\"\u0019\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\"\u0019\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005\"\u0019\u0010\u001c\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005\"\u0019\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0005\"\u0019\u0010 \u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0005\"\u0019\u0010\"\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b#\u0010\u0005\"\u0019\u0010$\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u0005\"\u0019\u0010&\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b'\u0010\u0005\"\u0019\u0010(\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b)\u0010\u0005\"\u0019\u0010*\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b+\u0010\u0005\"\u0019\u0010,\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b-\u0010\u0005\"\u0019\u0010.\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b/\u0010\u0005\"\u0019\u00100\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b1\u0010\u0005\"\u0019\u00102\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b3\u0010\u0005\"\u0019\u00104\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b5\u0010\u0005\"\u0019\u00106\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b7\u0010\u0005\"\u0019\u00108\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b9\u0010\u0005\"\u0019\u0010:\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b;\u0010\u0005\"\u0019\u0010<\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b=\u0010\u0005\"\u0019\u0010>\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b?\u0010\u0005\"\u0019\u0010@\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bA\u0010\u0005\"\u0019\u0010B\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bC\u0010\u0005\"\u0019\u0010D\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bE\u0010\u0005\"\u0019\u0010F\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bG\u0010\u0005\"\u0019\u0010H\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bI\u0010\u0005\"\u0019\u0010J\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bK\u0010\u0005\"\u0019\u0010L\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bM\u0010\u0005\"\u0019\u0010N\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bO\u0010\u0005\"\u0019\u0010P\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bQ\u0010\u0005\"\u0019\u0010R\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bS\u0010\u0005\u00a8\u0006`"}, d2={"aload_0", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "getAload_0", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "aload_1", "getAload_1", "aload_2", "getAload_2", "aload_3", "getAload_3", "astore_0", "getAstore_0", "astore_1", "getAstore_1", "astore_2", "getAstore_2", "astore_3", "getAstore_3", "dload_0", "getDload_0", "dload_1", "getDload_1", "dload_2", "getDload_2", "dload_3", "getDload_3", "dstore_0", "getDstore_0", "dstore_1", "getDstore_1", "dstore_2", "getDstore_2", "dstore_3", "getDstore_3", "fload_0", "getFload_0", "fload_1", "getFload_1", "fload_2", "getFload_2", "fload_3", "getFload_3", "fstore_0", "getFstore_0", "fstore_1", "getFstore_1", "fstore_2", "getFstore_2", "fstore_3", "getFstore_3", "iload_0", "getIload_0", "iload_1", "getIload_1", "iload_2", "getIload_2", "iload_3", "getIload_3", "istore_0", "getIstore_0", "istore_1", "getIstore_1", "istore_2", "getIstore_2", "istore_3", "getIstore_3", "lload_0", "getLload_0", "lload_1", "getLload_1", "lload_2", "getLload_2", "lload_3", "getLload_3", "lstore_0", "getLstore_0", "lstore_1", "getLstore_1", "lstore_2", "getLstore_2", "lstore_3", "getLstore_3", "aload", "slot", "", "astore", "dload", "dstore", "fload", "fstore", "iload", "istore", "lload", "lstore", "koffee"})
public final class LocalVariablesKt {
    public static final void iload(@NotNull InstructionAssembly $this$iload, int slot) {
        Intrinsics.checkParameterIsNotNull($this$iload, "$this$iload");
        $this$iload.getInstructions().add((AbstractInsnNode)new VarInsnNode(21, slot));
    }

    public static final void lload(@NotNull InstructionAssembly $this$lload, int slot) {
        Intrinsics.checkParameterIsNotNull($this$lload, "$this$lload");
        $this$lload.getInstructions().add((AbstractInsnNode)new VarInsnNode(22, slot));
    }

    public static final void fload(@NotNull InstructionAssembly $this$fload, int slot) {
        Intrinsics.checkParameterIsNotNull($this$fload, "$this$fload");
        $this$fload.getInstructions().add((AbstractInsnNode)new VarInsnNode(23, slot));
    }

    public static final void dload(@NotNull InstructionAssembly $this$dload, int slot) {
        Intrinsics.checkParameterIsNotNull($this$dload, "$this$dload");
        $this$dload.getInstructions().add((AbstractInsnNode)new VarInsnNode(24, slot));
    }

    public static final void aload(@NotNull InstructionAssembly $this$aload, int slot) {
        Intrinsics.checkParameterIsNotNull($this$aload, "$this$aload");
        $this$aload.getInstructions().add((AbstractInsnNode)new VarInsnNode(25, slot));
    }

    public static final void istore(@NotNull InstructionAssembly $this$istore, int slot) {
        Intrinsics.checkParameterIsNotNull($this$istore, "$this$istore");
        $this$istore.getInstructions().add((AbstractInsnNode)new VarInsnNode(54, slot));
    }

    public static final void lstore(@NotNull InstructionAssembly $this$lstore, int slot) {
        Intrinsics.checkParameterIsNotNull($this$lstore, "$this$lstore");
        $this$lstore.getInstructions().add((AbstractInsnNode)new VarInsnNode(55, slot));
    }

    public static final void fstore(@NotNull InstructionAssembly $this$fstore, int slot) {
        Intrinsics.checkParameterIsNotNull($this$fstore, "$this$fstore");
        $this$fstore.getInstructions().add((AbstractInsnNode)new VarInsnNode(56, slot));
    }

    public static final void dstore(@NotNull InstructionAssembly $this$dstore, int slot) {
        Intrinsics.checkParameterIsNotNull($this$dstore, "$this$dstore");
        $this$dstore.getInstructions().add((AbstractInsnNode)new VarInsnNode(57, slot));
    }

    public static final void astore(@NotNull InstructionAssembly $this$astore, int slot) {
        Intrinsics.checkParameterIsNotNull($this$astore, "$this$astore");
        $this$astore.getInstructions().add((AbstractInsnNode)new VarInsnNode(58, slot));
    }

    @NotNull
    public static final Unit getIload_0(@NotNull InstructionAssembly $this$iload_0) {
        Intrinsics.checkParameterIsNotNull($this$iload_0, "$this$iload_0");
        $this$iload_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(21, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLload_0(@NotNull InstructionAssembly $this$lload_0) {
        Intrinsics.checkParameterIsNotNull($this$lload_0, "$this$lload_0");
        $this$lload_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(22, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFload_0(@NotNull InstructionAssembly $this$fload_0) {
        Intrinsics.checkParameterIsNotNull($this$fload_0, "$this$fload_0");
        $this$fload_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(23, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDload_0(@NotNull InstructionAssembly $this$dload_0) {
        Intrinsics.checkParameterIsNotNull($this$dload_0, "$this$dload_0");
        $this$dload_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(24, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAload_0(@NotNull InstructionAssembly $this$aload_0) {
        Intrinsics.checkParameterIsNotNull($this$aload_0, "$this$aload_0");
        $this$aload_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(25, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIstore_0(@NotNull InstructionAssembly $this$istore_0) {
        Intrinsics.checkParameterIsNotNull($this$istore_0, "$this$istore_0");
        $this$istore_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(54, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLstore_0(@NotNull InstructionAssembly $this$lstore_0) {
        Intrinsics.checkParameterIsNotNull($this$lstore_0, "$this$lstore_0");
        $this$lstore_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(55, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFstore_0(@NotNull InstructionAssembly $this$fstore_0) {
        Intrinsics.checkParameterIsNotNull($this$fstore_0, "$this$fstore_0");
        $this$fstore_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(56, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDstore_0(@NotNull InstructionAssembly $this$dstore_0) {
        Intrinsics.checkParameterIsNotNull($this$dstore_0, "$this$dstore_0");
        $this$dstore_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(57, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAstore_0(@NotNull InstructionAssembly $this$astore_0) {
        Intrinsics.checkParameterIsNotNull($this$astore_0, "$this$astore_0");
        $this$astore_0.getInstructions().add((AbstractInsnNode)new VarInsnNode(58, 0));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIload_1(@NotNull InstructionAssembly $this$iload_1) {
        Intrinsics.checkParameterIsNotNull($this$iload_1, "$this$iload_1");
        $this$iload_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(21, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLload_1(@NotNull InstructionAssembly $this$lload_1) {
        Intrinsics.checkParameterIsNotNull($this$lload_1, "$this$lload_1");
        $this$lload_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(22, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFload_1(@NotNull InstructionAssembly $this$fload_1) {
        Intrinsics.checkParameterIsNotNull($this$fload_1, "$this$fload_1");
        $this$fload_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(23, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDload_1(@NotNull InstructionAssembly $this$dload_1) {
        Intrinsics.checkParameterIsNotNull($this$dload_1, "$this$dload_1");
        $this$dload_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(24, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAload_1(@NotNull InstructionAssembly $this$aload_1) {
        Intrinsics.checkParameterIsNotNull($this$aload_1, "$this$aload_1");
        $this$aload_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(25, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIstore_1(@NotNull InstructionAssembly $this$istore_1) {
        Intrinsics.checkParameterIsNotNull($this$istore_1, "$this$istore_1");
        $this$istore_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(54, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLstore_1(@NotNull InstructionAssembly $this$lstore_1) {
        Intrinsics.checkParameterIsNotNull($this$lstore_1, "$this$lstore_1");
        $this$lstore_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(55, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFstore_1(@NotNull InstructionAssembly $this$fstore_1) {
        Intrinsics.checkParameterIsNotNull($this$fstore_1, "$this$fstore_1");
        $this$fstore_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(56, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDstore_1(@NotNull InstructionAssembly $this$dstore_1) {
        Intrinsics.checkParameterIsNotNull($this$dstore_1, "$this$dstore_1");
        $this$dstore_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(57, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAstore_1(@NotNull InstructionAssembly $this$astore_1) {
        Intrinsics.checkParameterIsNotNull($this$astore_1, "$this$astore_1");
        $this$astore_1.getInstructions().add((AbstractInsnNode)new VarInsnNode(58, 1));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIload_2(@NotNull InstructionAssembly $this$iload_2) {
        Intrinsics.checkParameterIsNotNull($this$iload_2, "$this$iload_2");
        $this$iload_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(21, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLload_2(@NotNull InstructionAssembly $this$lload_2) {
        Intrinsics.checkParameterIsNotNull($this$lload_2, "$this$lload_2");
        $this$lload_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(22, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFload_2(@NotNull InstructionAssembly $this$fload_2) {
        Intrinsics.checkParameterIsNotNull($this$fload_2, "$this$fload_2");
        $this$fload_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(23, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDload_2(@NotNull InstructionAssembly $this$dload_2) {
        Intrinsics.checkParameterIsNotNull($this$dload_2, "$this$dload_2");
        $this$dload_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(24, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAload_2(@NotNull InstructionAssembly $this$aload_2) {
        Intrinsics.checkParameterIsNotNull($this$aload_2, "$this$aload_2");
        $this$aload_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(25, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIstore_2(@NotNull InstructionAssembly $this$istore_2) {
        Intrinsics.checkParameterIsNotNull($this$istore_2, "$this$istore_2");
        $this$istore_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(54, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLstore_2(@NotNull InstructionAssembly $this$lstore_2) {
        Intrinsics.checkParameterIsNotNull($this$lstore_2, "$this$lstore_2");
        $this$lstore_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(55, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFstore_2(@NotNull InstructionAssembly $this$fstore_2) {
        Intrinsics.checkParameterIsNotNull($this$fstore_2, "$this$fstore_2");
        $this$fstore_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(56, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDstore_2(@NotNull InstructionAssembly $this$dstore_2) {
        Intrinsics.checkParameterIsNotNull($this$dstore_2, "$this$dstore_2");
        $this$dstore_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(57, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAstore_2(@NotNull InstructionAssembly $this$astore_2) {
        Intrinsics.checkParameterIsNotNull($this$astore_2, "$this$astore_2");
        $this$astore_2.getInstructions().add((AbstractInsnNode)new VarInsnNode(58, 2));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIload_3(@NotNull InstructionAssembly $this$iload_3) {
        Intrinsics.checkParameterIsNotNull($this$iload_3, "$this$iload_3");
        $this$iload_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(21, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLload_3(@NotNull InstructionAssembly $this$lload_3) {
        Intrinsics.checkParameterIsNotNull($this$lload_3, "$this$lload_3");
        $this$lload_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(22, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFload_3(@NotNull InstructionAssembly $this$fload_3) {
        Intrinsics.checkParameterIsNotNull($this$fload_3, "$this$fload_3");
        $this$fload_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(23, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDload_3(@NotNull InstructionAssembly $this$dload_3) {
        Intrinsics.checkParameterIsNotNull($this$dload_3, "$this$dload_3");
        $this$dload_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(24, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAload_3(@NotNull InstructionAssembly $this$aload_3) {
        Intrinsics.checkParameterIsNotNull($this$aload_3, "$this$aload_3");
        $this$aload_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(25, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIstore_3(@NotNull InstructionAssembly $this$istore_3) {
        Intrinsics.checkParameterIsNotNull($this$istore_3, "$this$istore_3");
        $this$istore_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(54, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLstore_3(@NotNull InstructionAssembly $this$lstore_3) {
        Intrinsics.checkParameterIsNotNull($this$lstore_3, "$this$lstore_3");
        $this$lstore_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(55, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFstore_3(@NotNull InstructionAssembly $this$fstore_3) {
        Intrinsics.checkParameterIsNotNull($this$fstore_3, "$this$fstore_3");
        $this$fstore_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(56, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDstore_3(@NotNull InstructionAssembly $this$dstore_3) {
        Intrinsics.checkParameterIsNotNull($this$dstore_3, "$this$dstore_3");
        $this$dstore_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(57, 3));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAstore_3(@NotNull InstructionAssembly $this$astore_3) {
        Intrinsics.checkParameterIsNotNull($this$astore_3, "$this$astore_3");
        $this$astore_3.getInstructions().add((AbstractInsnNode)new VarInsnNode(58, 3));
        return Unit.INSTANCE;
    }
}

