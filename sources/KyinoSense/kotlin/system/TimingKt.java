/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.system;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0014\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0017\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b\u001a\u0017\u0010\u0005\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b\u00a8\u0006\u0006"}, d2={"measureNanoTime", "", "block", "Lkotlin/Function0;", "", "measureTimeMillis", "kotlin-stdlib"})
@JvmName(name="TimingKt")
public final class TimingKt {
    public static final long measureTimeMillis(@NotNull Function0<Unit> block) {
        int $i$f$measureTimeMillis = 0;
        Intrinsics.checkParameterIsNotNull(block, "block");
        long start = System.currentTimeMillis();
        block.invoke();
        return System.currentTimeMillis() - start;
    }

    public static final long measureNanoTime(@NotNull Function0<Unit> block) {
        int $i$f$measureNanoTime = 0;
        Intrinsics.checkParameterIsNotNull(block, "block");
        long start = System.nanoTime();
        block.invoke();
        return System.nanoTime() - start;
    }
}

