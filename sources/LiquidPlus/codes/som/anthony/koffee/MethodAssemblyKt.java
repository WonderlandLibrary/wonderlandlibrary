/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.tree.MethodNode
 */
package codes.som.anthony.koffee;

import codes.som.anthony.koffee.MethodAssembly;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u001a#\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0017\u0010\u0002\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\u0002\b\u0006\u00a8\u0006\u0000"}, d2={"koffee", "Lorg/objectweb/asm/tree/MethodNode;", "routine", "Lkotlin/Function1;", "Lcodes/som/anthony/koffee/MethodAssembly;", "", "Lkotlin/ExtensionFunctionType;"})
public final class MethodAssemblyKt {
    @NotNull
    public static final MethodNode koffee(@NotNull MethodNode $this$koffee, @NotNull Function1<? super MethodAssembly, Unit> routine) {
        Intrinsics.checkParameterIsNotNull($this$koffee, "$this$koffee");
        Intrinsics.checkParameterIsNotNull(routine, "routine");
        MethodAssembly assembly = new MethodAssembly($this$koffee);
        routine.invoke(assembly);
        return assembly.getNode();
    }
}

