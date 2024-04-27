/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.random;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.AbstractPlatformRandom;
import kotlin.random.KotlinRandom;
import kotlin.random.PlatformRandom;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0081\b\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0000\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0007\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0007\u00a8\u0006\n"}, d2={"defaultPlatformRandom", "Lkotlin/random/Random;", "doubleFromParts", "", "hi26", "", "low27", "asJavaRandom", "Ljava/util/Random;", "asKotlinRandom", "kotlin-stdlib"})
public final class PlatformRandomKt {
    @SinceKotlin(version="1.3")
    @NotNull
    public static final java.util.Random asJavaRandom(@NotNull Random $this$asJavaRandom) {
        Intrinsics.checkNotNullParameter($this$asJavaRandom, "<this>");
        AbstractPlatformRandom abstractPlatformRandom = $this$asJavaRandom instanceof AbstractPlatformRandom ? (AbstractPlatformRandom)$this$asJavaRandom : null;
        return abstractPlatformRandom == null ? (java.util.Random)new KotlinRandom($this$asJavaRandom) : abstractPlatformRandom.getImpl();
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final Random asKotlinRandom(@NotNull java.util.Random $this$asKotlinRandom) {
        Intrinsics.checkNotNullParameter($this$asKotlinRandom, "<this>");
        KotlinRandom kotlinRandom = $this$asKotlinRandom instanceof KotlinRandom ? (KotlinRandom)$this$asKotlinRandom : null;
        return kotlinRandom == null ? (Random)new PlatformRandom($this$asKotlinRandom) : kotlinRandom.getImpl();
    }

    @InlineOnly
    private static final Random defaultPlatformRandom() {
        return PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
    }

    public static final double doubleFromParts(int hi26, int low27) {
        return (double)(((long)hi26 << 27) + (long)low27) / 9.007199254740992E15;
    }
}

