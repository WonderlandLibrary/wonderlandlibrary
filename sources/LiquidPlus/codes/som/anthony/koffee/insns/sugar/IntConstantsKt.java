/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package codes.som.anthony.koffee.insns.sugar;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.insns.jvm.ConstantsKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"push_int", "", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "i", "", "koffee"})
public final class IntConstantsKt {
    public static final void push_int(@NotNull InstructionAssembly $this$push_int, int i) {
        Intrinsics.checkParameterIsNotNull($this$push_int, "$this$push_int");
        int n = i;
        if (n == -1) {
            ConstantsKt.getIconst_m1($this$push_int);
        } else if (n == 0) {
            ConstantsKt.getIconst_0($this$push_int);
        } else if (n == 1) {
            ConstantsKt.getIconst_1($this$push_int);
        } else if (n == 2) {
            ConstantsKt.getIconst_2($this$push_int);
        } else if (n == 3) {
            ConstantsKt.getIconst_3($this$push_int);
        } else if (n == 4) {
            ConstantsKt.getIconst_4($this$push_int);
        } else if (n == 5) {
            ConstantsKt.getIconst_5($this$push_int);
        } else {
            int n2 = n;
            if (-128 <= n2 && 127 >= n2) {
                ConstantsKt.bipush($this$push_int, i);
            } else {
                n2 = n;
                if (Short.MIN_VALUE <= n2 && Short.MAX_VALUE >= n2) {
                    ConstantsKt.sipush($this$push_int, i);
                } else {
                    ConstantsKt.ldc($this$push_int, i);
                }
            }
        }
    }
}

