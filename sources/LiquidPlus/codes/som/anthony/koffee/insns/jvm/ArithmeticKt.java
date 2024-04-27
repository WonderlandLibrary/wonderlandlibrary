/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.IincInsnNode
 *  org.objectweb.asm.tree.InsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u001a\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\bh\n\u0002\u0010\b\n\u0002\b\u0002\u001a\u0012\u0010j\u001a\u00020\u0001*\u00020\u00032\u0006\u0010k\u001a\u00020l\u001a\u001a\u0010j\u001a\u00020\u0001*\u00020\u00032\u0006\u0010k\u001a\u00020l2\u0006\u0010m\u001a\u00020l\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0019\u0010\b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\"\u0019\u0010\n\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\"\u0019\u0010\f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\"\u0019\u0010\u000e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\"\u0019\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005\"\u0019\u0010\u0012\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005\"\u0019\u0010\u0014\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005\"\u0019\u0010\u0016\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005\"\u0019\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\"\u0019\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005\"\u0019\u0010\u001c\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005\"\u0019\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0005\"\u0019\u0010 \u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0005\"\u0019\u0010\"\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b#\u0010\u0005\"\u0019\u0010$\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u0005\"\u0019\u0010&\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b'\u0010\u0005\"\u0019\u0010(\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b)\u0010\u0005\"\u0019\u0010*\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b+\u0010\u0005\"\u0019\u0010,\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b-\u0010\u0005\"\u0019\u0010.\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b/\u0010\u0005\"\u0019\u00100\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b1\u0010\u0005\"\u0019\u00102\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b3\u0010\u0005\"\u0019\u00104\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b5\u0010\u0005\"\u0019\u00106\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b7\u0010\u0005\"\u0019\u00108\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b9\u0010\u0005\"\u0019\u0010:\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b;\u0010\u0005\"\u0019\u0010<\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b=\u0010\u0005\"\u0019\u0010>\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b?\u0010\u0005\"\u0019\u0010@\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bA\u0010\u0005\"\u0019\u0010B\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bC\u0010\u0005\"\u0019\u0010D\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bE\u0010\u0005\"\u0019\u0010F\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bG\u0010\u0005\"\u0019\u0010H\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bI\u0010\u0005\"\u0019\u0010J\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bK\u0010\u0005\"\u0019\u0010L\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bM\u0010\u0005\"\u0019\u0010N\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bO\u0010\u0005\"\u0019\u0010P\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bQ\u0010\u0005\"\u0019\u0010R\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bS\u0010\u0005\"\u0019\u0010T\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bU\u0010\u0005\"\u0019\u0010V\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bW\u0010\u0005\"\u0019\u0010X\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bY\u0010\u0005\"\u0019\u0010Z\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b[\u0010\u0005\"\u0019\u0010\\\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b]\u0010\u0005\"\u0019\u0010^\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b_\u0010\u0005\"\u0019\u0010`\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\ba\u0010\u0005\"\u0019\u0010b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bc\u0010\u0005\"\u0019\u0010d\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\be\u0010\u0005\"\u0019\u0010f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bg\u0010\u0005\"\u0019\u0010h\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\bi\u0010\u0005\u00a8\u0006n"}, d2={"d2f", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "getD2f", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "d2i", "getD2i", "d2l", "getD2l", "dadd", "getDadd", "ddiv", "getDdiv", "dmul", "getDmul", "dneg", "getDneg", "drem", "getDrem", "dsub", "getDsub", "f2d", "getF2d", "f2i", "getF2i", "f2l", "getF2l", "fadd", "getFadd", "fdiv", "getFdiv", "fmul", "getFmul", "fneg", "getFneg", "frem", "getFrem", "fsub", "getFsub", "i2b", "getI2b", "i2c", "getI2c", "i2d", "getI2d", "i2f", "getI2f", "i2l", "getI2l", "i2s", "getI2s", "iadd", "getIadd", "iand", "getIand", "idiv", "getIdiv", "imul", "getImul", "ineg", "getIneg", "ior", "getIor", "irem", "getIrem", "ishl", "getIshl", "ishr", "getIshr", "isub", "getIsub", "iushr", "getIushr", "ixor", "getIxor", "l2d", "getL2d", "l2f", "getL2f", "l2i", "getL2i", "ladd", "getLadd", "land", "getLand", "ldiv", "getLdiv", "lmul", "getLmul", "lneg", "getLneg", "lor", "getLor", "lrem", "getLrem", "lshl", "getLshl", "lshr", "getLshr", "lsub", "getLsub", "lushr", "getLushr", "lxor", "getLxor", "iinc", "slot", "", "amount", "koffee"})
public final class ArithmeticKt {
    @NotNull
    public static final Unit getIadd(@NotNull InstructionAssembly $this$iadd) {
        Intrinsics.checkParameterIsNotNull($this$iadd, "$this$iadd");
        $this$iadd.getInstructions().add((AbstractInsnNode)new InsnNode(96));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLadd(@NotNull InstructionAssembly $this$ladd) {
        Intrinsics.checkParameterIsNotNull($this$ladd, "$this$ladd");
        $this$ladd.getInstructions().add((AbstractInsnNode)new InsnNode(97));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFadd(@NotNull InstructionAssembly $this$fadd) {
        Intrinsics.checkParameterIsNotNull($this$fadd, "$this$fadd");
        $this$fadd.getInstructions().add((AbstractInsnNode)new InsnNode(98));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDadd(@NotNull InstructionAssembly $this$dadd) {
        Intrinsics.checkParameterIsNotNull($this$dadd, "$this$dadd");
        $this$dadd.getInstructions().add((AbstractInsnNode)new InsnNode(99));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIsub(@NotNull InstructionAssembly $this$isub) {
        Intrinsics.checkParameterIsNotNull($this$isub, "$this$isub");
        $this$isub.getInstructions().add((AbstractInsnNode)new InsnNode(100));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLsub(@NotNull InstructionAssembly $this$lsub) {
        Intrinsics.checkParameterIsNotNull($this$lsub, "$this$lsub");
        $this$lsub.getInstructions().add((AbstractInsnNode)new InsnNode(101));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFsub(@NotNull InstructionAssembly $this$fsub) {
        Intrinsics.checkParameterIsNotNull($this$fsub, "$this$fsub");
        $this$fsub.getInstructions().add((AbstractInsnNode)new InsnNode(102));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDsub(@NotNull InstructionAssembly $this$dsub) {
        Intrinsics.checkParameterIsNotNull($this$dsub, "$this$dsub");
        $this$dsub.getInstructions().add((AbstractInsnNode)new InsnNode(103));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getImul(@NotNull InstructionAssembly $this$imul) {
        Intrinsics.checkParameterIsNotNull($this$imul, "$this$imul");
        $this$imul.getInstructions().add((AbstractInsnNode)new InsnNode(104));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLmul(@NotNull InstructionAssembly $this$lmul) {
        Intrinsics.checkParameterIsNotNull($this$lmul, "$this$lmul");
        $this$lmul.getInstructions().add((AbstractInsnNode)new InsnNode(105));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFmul(@NotNull InstructionAssembly $this$fmul) {
        Intrinsics.checkParameterIsNotNull($this$fmul, "$this$fmul");
        $this$fmul.getInstructions().add((AbstractInsnNode)new InsnNode(106));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDmul(@NotNull InstructionAssembly $this$dmul) {
        Intrinsics.checkParameterIsNotNull($this$dmul, "$this$dmul");
        $this$dmul.getInstructions().add((AbstractInsnNode)new InsnNode(107));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIdiv(@NotNull InstructionAssembly $this$idiv) {
        Intrinsics.checkParameterIsNotNull($this$idiv, "$this$idiv");
        $this$idiv.getInstructions().add((AbstractInsnNode)new InsnNode(108));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLdiv(@NotNull InstructionAssembly $this$ldiv) {
        Intrinsics.checkParameterIsNotNull($this$ldiv, "$this$ldiv");
        $this$ldiv.getInstructions().add((AbstractInsnNode)new InsnNode(109));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFdiv(@NotNull InstructionAssembly $this$fdiv) {
        Intrinsics.checkParameterIsNotNull($this$fdiv, "$this$fdiv");
        $this$fdiv.getInstructions().add((AbstractInsnNode)new InsnNode(110));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDdiv(@NotNull InstructionAssembly $this$ddiv) {
        Intrinsics.checkParameterIsNotNull($this$ddiv, "$this$ddiv");
        $this$ddiv.getInstructions().add((AbstractInsnNode)new InsnNode(111));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIrem(@NotNull InstructionAssembly $this$irem) {
        Intrinsics.checkParameterIsNotNull($this$irem, "$this$irem");
        $this$irem.getInstructions().add((AbstractInsnNode)new InsnNode(112));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLrem(@NotNull InstructionAssembly $this$lrem) {
        Intrinsics.checkParameterIsNotNull($this$lrem, "$this$lrem");
        $this$lrem.getInstructions().add((AbstractInsnNode)new InsnNode(113));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFrem(@NotNull InstructionAssembly $this$frem) {
        Intrinsics.checkParameterIsNotNull($this$frem, "$this$frem");
        $this$frem.getInstructions().add((AbstractInsnNode)new InsnNode(114));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDrem(@NotNull InstructionAssembly $this$drem) {
        Intrinsics.checkParameterIsNotNull($this$drem, "$this$drem");
        $this$drem.getInstructions().add((AbstractInsnNode)new InsnNode(115));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIneg(@NotNull InstructionAssembly $this$ineg) {
        Intrinsics.checkParameterIsNotNull($this$ineg, "$this$ineg");
        $this$ineg.getInstructions().add((AbstractInsnNode)new InsnNode(116));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLneg(@NotNull InstructionAssembly $this$lneg) {
        Intrinsics.checkParameterIsNotNull($this$lneg, "$this$lneg");
        $this$lneg.getInstructions().add((AbstractInsnNode)new InsnNode(117));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFneg(@NotNull InstructionAssembly $this$fneg) {
        Intrinsics.checkParameterIsNotNull($this$fneg, "$this$fneg");
        $this$fneg.getInstructions().add((AbstractInsnNode)new InsnNode(118));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDneg(@NotNull InstructionAssembly $this$dneg) {
        Intrinsics.checkParameterIsNotNull($this$dneg, "$this$dneg");
        $this$dneg.getInstructions().add((AbstractInsnNode)new InsnNode(119));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIshl(@NotNull InstructionAssembly $this$ishl) {
        Intrinsics.checkParameterIsNotNull($this$ishl, "$this$ishl");
        $this$ishl.getInstructions().add((AbstractInsnNode)new InsnNode(120));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLshl(@NotNull InstructionAssembly $this$lshl) {
        Intrinsics.checkParameterIsNotNull($this$lshl, "$this$lshl");
        $this$lshl.getInstructions().add((AbstractInsnNode)new InsnNode(121));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIshr(@NotNull InstructionAssembly $this$ishr) {
        Intrinsics.checkParameterIsNotNull($this$ishr, "$this$ishr");
        $this$ishr.getInstructions().add((AbstractInsnNode)new InsnNode(122));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLshr(@NotNull InstructionAssembly $this$lshr) {
        Intrinsics.checkParameterIsNotNull($this$lshr, "$this$lshr");
        $this$lshr.getInstructions().add((AbstractInsnNode)new InsnNode(123));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIushr(@NotNull InstructionAssembly $this$iushr) {
        Intrinsics.checkParameterIsNotNull($this$iushr, "$this$iushr");
        $this$iushr.getInstructions().add((AbstractInsnNode)new InsnNode(124));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLushr(@NotNull InstructionAssembly $this$lushr) {
        Intrinsics.checkParameterIsNotNull($this$lushr, "$this$lushr");
        $this$lushr.getInstructions().add((AbstractInsnNode)new InsnNode(125));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIand(@NotNull InstructionAssembly $this$iand) {
        Intrinsics.checkParameterIsNotNull($this$iand, "$this$iand");
        $this$iand.getInstructions().add((AbstractInsnNode)new InsnNode(126));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLand(@NotNull InstructionAssembly $this$land) {
        Intrinsics.checkParameterIsNotNull($this$land, "$this$land");
        $this$land.getInstructions().add((AbstractInsnNode)new InsnNode(127));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIor(@NotNull InstructionAssembly $this$ior) {
        Intrinsics.checkParameterIsNotNull($this$ior, "$this$ior");
        $this$ior.getInstructions().add((AbstractInsnNode)new InsnNode(128));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLor(@NotNull InstructionAssembly $this$lor) {
        Intrinsics.checkParameterIsNotNull($this$lor, "$this$lor");
        $this$lor.getInstructions().add((AbstractInsnNode)new InsnNode(129));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIxor(@NotNull InstructionAssembly $this$ixor) {
        Intrinsics.checkParameterIsNotNull($this$ixor, "$this$ixor");
        $this$ixor.getInstructions().add((AbstractInsnNode)new InsnNode(130));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLxor(@NotNull InstructionAssembly $this$lxor) {
        Intrinsics.checkParameterIsNotNull($this$lxor, "$this$lxor");
        $this$lxor.getInstructions().add((AbstractInsnNode)new InsnNode(131));
        return Unit.INSTANCE;
    }

    public static final void iinc(@NotNull InstructionAssembly $this$iinc, int slot) {
        Intrinsics.checkParameterIsNotNull($this$iinc, "$this$iinc");
        $this$iinc.getInstructions().add((AbstractInsnNode)new IincInsnNode(slot, 1));
    }

    public static final void iinc(@NotNull InstructionAssembly $this$iinc, int slot, int amount) {
        Intrinsics.checkParameterIsNotNull($this$iinc, "$this$iinc");
        $this$iinc.getInstructions().add((AbstractInsnNode)new IincInsnNode(slot, amount));
    }

    @NotNull
    public static final Unit getI2l(@NotNull InstructionAssembly $this$i2l) {
        Intrinsics.checkParameterIsNotNull($this$i2l, "$this$i2l");
        $this$i2l.getInstructions().add((AbstractInsnNode)new InsnNode(133));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getI2f(@NotNull InstructionAssembly $this$i2f) {
        Intrinsics.checkParameterIsNotNull($this$i2f, "$this$i2f");
        $this$i2f.getInstructions().add((AbstractInsnNode)new InsnNode(134));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getI2d(@NotNull InstructionAssembly $this$i2d) {
        Intrinsics.checkParameterIsNotNull($this$i2d, "$this$i2d");
        $this$i2d.getInstructions().add((AbstractInsnNode)new InsnNode(135));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getL2i(@NotNull InstructionAssembly $this$l2i) {
        Intrinsics.checkParameterIsNotNull($this$l2i, "$this$l2i");
        $this$l2i.getInstructions().add((AbstractInsnNode)new InsnNode(136));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getL2f(@NotNull InstructionAssembly $this$l2f) {
        Intrinsics.checkParameterIsNotNull($this$l2f, "$this$l2f");
        $this$l2f.getInstructions().add((AbstractInsnNode)new InsnNode(137));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getL2d(@NotNull InstructionAssembly $this$l2d) {
        Intrinsics.checkParameterIsNotNull($this$l2d, "$this$l2d");
        $this$l2d.getInstructions().add((AbstractInsnNode)new InsnNode(138));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getF2i(@NotNull InstructionAssembly $this$f2i) {
        Intrinsics.checkParameterIsNotNull($this$f2i, "$this$f2i");
        $this$f2i.getInstructions().add((AbstractInsnNode)new InsnNode(139));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getF2l(@NotNull InstructionAssembly $this$f2l) {
        Intrinsics.checkParameterIsNotNull($this$f2l, "$this$f2l");
        $this$f2l.getInstructions().add((AbstractInsnNode)new InsnNode(140));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getF2d(@NotNull InstructionAssembly $this$f2d) {
        Intrinsics.checkParameterIsNotNull($this$f2d, "$this$f2d");
        $this$f2d.getInstructions().add((AbstractInsnNode)new InsnNode(141));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getD2i(@NotNull InstructionAssembly $this$d2i) {
        Intrinsics.checkParameterIsNotNull($this$d2i, "$this$d2i");
        $this$d2i.getInstructions().add((AbstractInsnNode)new InsnNode(142));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getD2l(@NotNull InstructionAssembly $this$d2l) {
        Intrinsics.checkParameterIsNotNull($this$d2l, "$this$d2l");
        $this$d2l.getInstructions().add((AbstractInsnNode)new InsnNode(143));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getD2f(@NotNull InstructionAssembly $this$d2f) {
        Intrinsics.checkParameterIsNotNull($this$d2f, "$this$d2f");
        $this$d2f.getInstructions().add((AbstractInsnNode)new InsnNode(144));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getI2b(@NotNull InstructionAssembly $this$i2b) {
        Intrinsics.checkParameterIsNotNull($this$i2b, "$this$i2b");
        $this$i2b.getInstructions().add((AbstractInsnNode)new InsnNode(145));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getI2c(@NotNull InstructionAssembly $this$i2c) {
        Intrinsics.checkParameterIsNotNull($this$i2c, "$this$i2c");
        $this$i2c.getInstructions().add((AbstractInsnNode)new InsnNode(146));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getI2s(@NotNull InstructionAssembly $this$i2s) {
        Intrinsics.checkParameterIsNotNull($this$i2s, "$this$i2s");
        $this$i2s.getInstructions().add((AbstractInsnNode)new InsnNode(147));
        return Unit.INSTANCE;
    }
}

