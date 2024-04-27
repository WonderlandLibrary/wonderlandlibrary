/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.util.Random;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0012J\u0016\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0004J\u0018\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fH\u0007J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u001cJ\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0019J\u000e\u0010\u001d\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000fJ\u0010\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u000fH\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\b\u00a8\u0006\u001f"}, d2={"Lnet/ccbluex/liquidbounce/utils/misc/RandomUtils;", "", "()V", "tiph", "", "getTiph", "()F", "setTiph", "(F)V", "tiphrun", "getTiphrun", "setTiphrun", "animtips", "", "easing", "", "animtipsrun", "nextDouble", "", "startInclusive", "endInclusive", "nextFloat", "nextInt", "endExclusive", "random", "", "length", "chars", "", "randomNumber", "randomString", "LiKingSense"})
public final class RandomUtils {
    private static float tiph;
    private static float tiphrun;
    public static final RandomUtils INSTANCE;

    @JvmStatic
    public static final int nextInt(int startInclusive, int endExclusive) {
        return endExclusive - startInclusive <= 0 ? startInclusive : startInclusive + new Random().nextInt(endExclusive - startInclusive);
    }

    public final double nextDouble(double startInclusive, double endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0 ? startInclusive : startInclusive + (endInclusive - startInclusive) * Math.random();
    }

    public final float nextFloat(float startInclusive, float endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f ? startInclusive : (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random());
    }

    @NotNull
    public final String randomNumber(int length) {
        return this.random(length, "123456789");
    }

    @JvmStatic
    @NotNull
    public static final String randomString(int length) {
        return INSTANCE.random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    @NotNull
    public final String random(int length, @NotNull String chars) {
        Intrinsics.checkParameterIsNotNull((Object)chars, (String)"chars");
        String string = chars;
        int n = length;
        RandomUtils randomUtils = this;
        boolean bl = false;
        char[] cArray = string.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull((Object)cArray, (String)"(this as java.lang.String).toCharArray()");
        char[] cArray2 = cArray;
        return randomUtils.random(n, cArray2);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String random(int length, @NotNull char[] chars) {
        Intrinsics.checkParameterIsNotNull((Object)chars, (String)"chars");
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        int n2 = length;
        while (n < n2) {
            void i;
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
            ++i;
        }
        String string = stringBuilder.toString();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"stringBuilder.toString()");
        return string;
    }

    public final float getTiph() {
        return tiph;
    }

    public final void setTiph(float f) {
        tiph = f;
    }

    public final float getTiphrun() {
        return tiphrun;
    }

    public final void setTiphrun(float f) {
        tiphrun = f;
    }

    public final void animtips(int easing) {
        float f = 2.0f;
        float f2 = 7.5f;
        float f3 = (float)easing - tiph;
        float f4 = tiph;
        boolean bl = false;
        float f5 = (float)Math.pow(f, f2);
        tiph = f4 + f3 / f5 * (float)RenderUtils.deltaTime;
    }

    public final void animtipsrun(int easing) {
        float f = 2.0f;
        float f2 = 7.5f;
        float f3 = (float)easing - tiphrun;
        float f4 = tiphrun;
        boolean bl = false;
        float f5 = (float)Math.pow(f, f2);
        tiphrun = f4 + f3 / f5 * (float)RenderUtils.deltaTime;
    }

    private RandomUtils() {
    }

    static {
        RandomUtils randomUtils;
        INSTANCE = randomUtils = new RandomUtils();
    }
}

