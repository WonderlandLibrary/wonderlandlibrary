/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package codes.som.anthony.koffee.modifiers;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0000H\u0086\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2={"Lcodes/som/anthony/koffee/modifiers/Modifiers;", "", "access", "", "(I)V", "getAccess", "()I", "plus", "modifiers", "koffee"})
public class Modifiers {
    private final int access;

    @NotNull
    public final Modifiers plus(@NotNull Modifiers modifiers) {
        Intrinsics.checkParameterIsNotNull(modifiers, "modifiers");
        return new Modifiers(this.access | modifiers.access);
    }

    public final int getAccess() {
        return this.access;
    }

    public Modifiers(int access) {
        this.access = access;
    }
}

