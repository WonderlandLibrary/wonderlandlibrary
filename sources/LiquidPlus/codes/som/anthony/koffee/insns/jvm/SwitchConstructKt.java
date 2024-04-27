/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.LookupSwitchInsnNode
 *  org.objectweb.asm.tree.TableSwitchInsnNode
 */
package codes.som.anthony.koffee.insns.jvm;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.labels.LabelsKt;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\u001aO\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u000522\u0010\u0006\u001a\u001a\u0012\u0016\b\u0001\u0012\u0012\u0012\u0004\u0012\u00020\t\u0012\b\u0012\u00060\u0004j\u0002`\u00050\b0\u0007\"\u0012\u0012\u0004\u0012\u00020\t\u0012\b\u0012\u00060\u0004j\u0002`\u00050\b\u00a2\u0006\u0002\u0010\n\u001aG\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u00052\u001a\u0010\u000e\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0004j\u0002`\u00050\u0007\"\u00060\u0004j\u0002`\u0005\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2={"lookupswitch", "", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "defaultLabel", "", "Lcodes/som/anthony/koffee/labels/LabelLike;", "branches", "", "Lkotlin/Pair;", "", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Ljava/lang/Object;[Lkotlin/Pair;)V", "tableswitch", "min", "max", "labels", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;IILjava/lang/Object;[Ljava/lang/Object;)V", "koffee"})
public final class SwitchConstructKt {
    /*
     * WARNING - void declaration
     */
    public static final void tableswitch(@NotNull InstructionAssembly $this$tableswitch, int min, int max, @NotNull Object defaultLabel, Object ... labels) {
        Intrinsics.checkParameterIsNotNull($this$tableswitch, "$this$tableswitch");
        Intrinsics.checkParameterIsNotNull(defaultLabel, "defaultLabel");
        Intrinsics.checkParameterIsNotNull(labels, "labels");
        int size$iv = labels.length;
        boolean $i$f$Array = false;
        LabelNode[] result$iv = new LabelNode[size$iv];
        int n = 0;
        int n2 = result$iv.length;
        while (n < n2) {
            void it;
            LabelNode labelNode;
            void i$iv;
            void var11_10 = i$iv;
            void var14_13 = i$iv++;
            LabelNode[] labelNodeArray = result$iv;
            boolean bl = false;
            labelNodeArray[var14_13] = labelNode = LabelsKt.coerceLabel(labels[it]);
        }
        LabelNode[] labelNodes = result$iv;
        $this$tableswitch.getInstructions().add((AbstractInsnNode)new TableSwitchInsnNode(min, max, LabelsKt.coerceLabel(defaultLabel), Arrays.copyOf(labelNodes, labelNodes.length)));
    }

    /*
     * WARNING - void declaration
     */
    public static final void lookupswitch(@NotNull InstructionAssembly $this$lookupswitch, @NotNull Object defaultLabel, Pair<Integer, ? extends Object> ... branches) {
        boolean bl;
        Object[] objectArray;
        void var13_14;
        int n;
        Intrinsics.checkParameterIsNotNull($this$lookupswitch, "$this$lookupswitch");
        Intrinsics.checkParameterIsNotNull(defaultLabel, "defaultLabel");
        Intrinsics.checkParameterIsNotNull(branches, "branches");
        int size$iv = branches.length;
        boolean $i$f$IntArray = false;
        int[] result$iv = new int[size$iv];
        int n2 = 0;
        int n3 = result$iv.length;
        while (n2 < n3) {
            void it;
            int n4;
            void i$iv;
            n = i$iv;
            var13_14 = i$iv++;
            objectArray = result$iv;
            bl = false;
            objectArray[var13_14] = n4 = ((Number)branches[it].getFirst()).intValue();
        }
        int[] keys2 = result$iv;
        int size$iv2 = branches.length;
        boolean $i$f$Array = false;
        LabelNode[] result$iv2 = new LabelNode[size$iv2];
        n3 = 0;
        n = result$iv2.length;
        while (n3 < n) {
            void it;
            void i$iv;
            bl = i$iv;
            var13_14 = i$iv++;
            objectArray = result$iv2;
            boolean bl2 = false;
            LabelNode labelNode = LabelsKt.coerceLabel(branches[it].getSecond());
            objectArray[var13_14] = (int)labelNode;
        }
        LabelNode[] labels = result$iv2;
        $this$lookupswitch.getInstructions().add((AbstractInsnNode)new LookupSwitchInsnNode(LabelsKt.coerceLabel(defaultLabel), keys2, labels));
    }
}

