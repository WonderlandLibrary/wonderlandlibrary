/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.IntInsnNode
 *  org.objectweb.asm.tree.MultiANewArrayInsnNode
 *  org.objectweb.asm.tree.TypeInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.types.TypesKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b$\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\u0016\u0010&\u001a\u00020\u0001*\u00020\u00032\n\u0010'\u001a\u00060(j\u0002`)\u001a\u001e\u0010*\u001a\u00020\u0001*\u00020\u00032\n\u0010'\u001a\u00060(j\u0002`)2\u0006\u0010+\u001a\u00020,\u001a\u0016\u0010-\u001a\u00020\u0001*\u00020\u00032\n\u0010'\u001a\u00060(j\u0002`)\"\u0019\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u0019\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\"\u0019\u0010\b\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005\"\u0019\u0010\n\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005\"\u0019\u0010\f\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\"\u0019\u0010\u000e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0005\"\u0019\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0011\u0010\u0005\"\u0019\u0010\u0012\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0005\"\u0019\u0010\u0014\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0005\"\u0019\u0010\u0016\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0005\"\u0019\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\"\u0019\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0005\"\u0019\u0010\u001c\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0005\"\u0019\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0005\"\u0019\u0010 \u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0005\"\u0019\u0010\"\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b#\u0010\u0005\"\u0019\u0010$\u001a\u00060\u0001j\u0002`\u0002*\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u0005\u00a8\u0006."}, d2={"aaload", "", "Lcodes/som/anthony/koffee/insns/jvm/U;", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "getAaload", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;)Lkotlin/Unit;", "aastore", "getAastore", "arraylength", "getArraylength", "baload", "getBaload", "bastore", "getBastore", "caload", "getCaload", "castore", "getCastore", "daload", "getDaload", "dastore", "getDastore", "faload", "getFaload", "fastore", "getFastore", "iaload", "getIaload", "iastore", "getIastore", "laload", "getLaload", "lastore", "getLastore", "saload", "getSaload", "sastore", "getSastore", "anewarray", "type", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "multianewarray", "dimensions", "", "newarray", "koffee"})
public final class ArraysKt {
    @NotNull
    public static final Unit getIaload(@NotNull InstructionAssembly $this$iaload) {
        Intrinsics.checkParameterIsNotNull($this$iaload, "$this$iaload");
        $this$iaload.getInstructions().add((AbstractInsnNode)new InsnNode(46));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLaload(@NotNull InstructionAssembly $this$laload) {
        Intrinsics.checkParameterIsNotNull($this$laload, "$this$laload");
        $this$laload.getInstructions().add((AbstractInsnNode)new InsnNode(47));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFaload(@NotNull InstructionAssembly $this$faload) {
        Intrinsics.checkParameterIsNotNull($this$faload, "$this$faload");
        $this$faload.getInstructions().add((AbstractInsnNode)new InsnNode(48));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDaload(@NotNull InstructionAssembly $this$daload) {
        Intrinsics.checkParameterIsNotNull($this$daload, "$this$daload");
        $this$daload.getInstructions().add((AbstractInsnNode)new InsnNode(49));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAaload(@NotNull InstructionAssembly $this$aaload) {
        Intrinsics.checkParameterIsNotNull($this$aaload, "$this$aaload");
        $this$aaload.getInstructions().add((AbstractInsnNode)new InsnNode(50));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getBaload(@NotNull InstructionAssembly $this$baload) {
        Intrinsics.checkParameterIsNotNull($this$baload, "$this$baload");
        $this$baload.getInstructions().add((AbstractInsnNode)new InsnNode(51));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getCaload(@NotNull InstructionAssembly $this$caload) {
        Intrinsics.checkParameterIsNotNull($this$caload, "$this$caload");
        $this$caload.getInstructions().add((AbstractInsnNode)new InsnNode(52));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getSaload(@NotNull InstructionAssembly $this$saload) {
        Intrinsics.checkParameterIsNotNull($this$saload, "$this$saload");
        $this$saload.getInstructions().add((AbstractInsnNode)new InsnNode(53));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getIastore(@NotNull InstructionAssembly $this$iastore) {
        Intrinsics.checkParameterIsNotNull($this$iastore, "$this$iastore");
        $this$iastore.getInstructions().add((AbstractInsnNode)new InsnNode(79));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getLastore(@NotNull InstructionAssembly $this$lastore) {
        Intrinsics.checkParameterIsNotNull($this$lastore, "$this$lastore");
        $this$lastore.getInstructions().add((AbstractInsnNode)new InsnNode(80));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getFastore(@NotNull InstructionAssembly $this$fastore) {
        Intrinsics.checkParameterIsNotNull($this$fastore, "$this$fastore");
        $this$fastore.getInstructions().add((AbstractInsnNode)new InsnNode(81));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getDastore(@NotNull InstructionAssembly $this$dastore) {
        Intrinsics.checkParameterIsNotNull($this$dastore, "$this$dastore");
        $this$dastore.getInstructions().add((AbstractInsnNode)new InsnNode(82));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getAastore(@NotNull InstructionAssembly $this$aastore) {
        Intrinsics.checkParameterIsNotNull($this$aastore, "$this$aastore");
        $this$aastore.getInstructions().add((AbstractInsnNode)new InsnNode(83));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getBastore(@NotNull InstructionAssembly $this$bastore) {
        Intrinsics.checkParameterIsNotNull($this$bastore, "$this$bastore");
        $this$bastore.getInstructions().add((AbstractInsnNode)new InsnNode(84));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getCastore(@NotNull InstructionAssembly $this$castore) {
        Intrinsics.checkParameterIsNotNull($this$castore, "$this$castore");
        $this$castore.getInstructions().add((AbstractInsnNode)new InsnNode(85));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getSastore(@NotNull InstructionAssembly $this$sastore) {
        Intrinsics.checkParameterIsNotNull($this$sastore, "$this$sastore");
        $this$sastore.getInstructions().add((AbstractInsnNode)new InsnNode(86));
        return Unit.INSTANCE;
    }

    @NotNull
    public static final Unit getArraylength(@NotNull InstructionAssembly $this$arraylength) {
        Intrinsics.checkParameterIsNotNull($this$arraylength, "$this$arraylength");
        $this$arraylength.getInstructions().add((AbstractInsnNode)new InsnNode(190));
        return Unit.INSTANCE;
    }

    public static final void newarray(@NotNull InstructionAssembly $this$newarray, @NotNull Object type) {
        int n;
        Intrinsics.checkParameterIsNotNull($this$newarray, "$this$newarray");
        Intrinsics.checkParameterIsNotNull(type, "type");
        InsnList insnList = $this$newarray.getInstructions();
        switch (TypesKt.coerceType(type).getSort()) {
            case 1: {
                n = 4;
                break;
            }
            case 2: {
                n = 5;
                break;
            }
            case 3: {
                n = 8;
                break;
            }
            case 4: {
                n = 9;
                break;
            }
            case 5: {
                n = 10;
                break;
            }
            case 6: {
                n = 6;
                break;
            }
            case 7: {
                n = 11;
                break;
            }
            case 8: {
                n = 7;
                break;
            }
            default: {
                String string = "Invalid type for primitive array creation";
                int n2 = 188;
                InsnList insnList2 = insnList;
                boolean bl = false;
                throw (Throwable)new IllegalStateException(string.toString());
            }
        }
        int n3 = n;
        int n4 = 188;
        insnList.add((AbstractInsnNode)new IntInsnNode(n4, n3));
    }

    public static final void anewarray(@NotNull InstructionAssembly $this$anewarray, @NotNull Object type) {
        Intrinsics.checkParameterIsNotNull($this$anewarray, "$this$anewarray");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$anewarray.getInstructions().add((AbstractInsnNode)new TypeInsnNode(189, TypesKt.coerceType(type).getInternalName()));
    }

    public static final void multianewarray(@NotNull InstructionAssembly $this$multianewarray, @NotNull Object type, int dimensions) {
        Intrinsics.checkParameterIsNotNull($this$multianewarray, "$this$multianewarray");
        Intrinsics.checkParameterIsNotNull(type, "type");
        $this$multianewarray.getInstructions().add((AbstractInsnNode)new MultiANewArrayInsnNode(TypesKt.coerceType(type).getDescriptor(), dimensions));
    }
}

