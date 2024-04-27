/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.TryCatchBlockNode
 */
package codes.som.anthony.koffee;

import codes.som.anthony.koffee.BlockAssembly;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\u001a1\u0010\u0000\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u00012\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\b\t\u001aE\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0001*\u00020\u00022\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\f2\u0017\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\b\t\u00a8\u0006\n"}, d2={"assembleBlock", "Lkotlin/Pair;", "Lorg/objectweb/asm/tree/InsnList;", "", "Lorg/objectweb/asm/tree/TryCatchBlockNode;", "routine", "Lkotlin/Function1;", "Lcodes/som/anthony/koffee/BlockAssembly;", "", "Lkotlin/ExtensionFunctionType;", "koffee", "tryCatchBlocks", ""})
public final class BlockAssemblyKt {
    @NotNull
    public static final Pair<InsnList, List<TryCatchBlockNode>> assembleBlock(@NotNull Function1<? super BlockAssembly, Unit> routine) {
        List list;
        Intrinsics.checkParameterIsNotNull(routine, "routine");
        InsnList insnList = new InsnList();
        boolean bl = false;
        List list2 = list = (List)new ArrayList();
        InsnList insnList2 = insnList;
        BlockAssembly blockAssembly = new BlockAssembly(insnList2, list2);
        routine.invoke(blockAssembly);
        return new Pair<InsnList, List<TryCatchBlockNode>>(blockAssembly.getInstructions(), blockAssembly.getTryCatchBlocks());
    }

    @NotNull
    public static final Pair<InsnList, List<TryCatchBlockNode>> koffee(@NotNull InsnList $this$koffee, @NotNull List<TryCatchBlockNode> tryCatchBlocks, @NotNull Function1<? super BlockAssembly, Unit> routine) {
        Intrinsics.checkParameterIsNotNull($this$koffee, "$this$koffee");
        Intrinsics.checkParameterIsNotNull(tryCatchBlocks, "tryCatchBlocks");
        Intrinsics.checkParameterIsNotNull(routine, "routine");
        BlockAssembly blockAssembly = new BlockAssembly($this$koffee, tryCatchBlocks);
        routine.invoke(blockAssembly);
        return new Pair<InsnList, List<TryCatchBlockNode>>(blockAssembly.getInstructions(), blockAssembly.getTryCatchBlocks());
    }

    @NotNull
    public static /* synthetic */ Pair koffee$default(InsnList insnList, List list, Function1 function1, int n, Object object) {
        if ((n & 1) != 0) {
            boolean bl = false;
            list = new ArrayList();
        }
        return BlockAssemblyKt.koffee(insnList, list, function1);
    }
}

