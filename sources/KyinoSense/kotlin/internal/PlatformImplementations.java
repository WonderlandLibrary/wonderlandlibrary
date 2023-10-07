/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.internal;

import java.lang.reflect.Method;
import java.util.regex.MatchResult;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
import kotlin.text.MatchGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0010B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u00a8\u0006\u0011"}, d2={"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "ReflectAddSuppressedMethod", "kotlin-stdlib"})
public class PlatformImplementations {
    public void addSuppressed(@NotNull Throwable cause, @NotNull Throwable exception) {
        block0: {
            Intrinsics.checkParameterIsNotNull(cause, "cause");
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            Method method = ReflectAddSuppressedMethod.method;
            if (method == null) break block0;
            method.invoke((Object)cause, exception);
        }
    }

    @Nullable
    public MatchGroup getMatchResultNamedGroup(@NotNull MatchResult matchResult, @NotNull String name) {
        Intrinsics.checkParameterIsNotNull(matchResult, "matchResult");
        Intrinsics.checkParameterIsNotNull(name, "name");
        throw (Throwable)new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    @NotNull
    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lkotlin/internal/PlatformImplementations$ReflectAddSuppressedMethod;", "", "()V", "method", "Ljava/lang/reflect/Method;", "kotlin-stdlib"})
    private static final class ReflectAddSuppressedMethod {
        @JvmField
        @Nullable
        public static final Method method;
        public static final ReflectAddSuppressedMethod INSTANCE;

        private ReflectAddSuppressedMethod() {
        }

        /*
         * Unable to fully structure code
         */
        static {
            block3: {
                ReflectAddSuppressedMethod.INSTANCE = var0 = new ReflectAddSuppressedMethod();
                var1_1 = Throwable.class;
                var2_2 = false;
                var3_3 = false;
                throwableClass = var1_1;
                $i$a$-let-PlatformImplementations$ReflectAddSuppressedMethod$method$1 = false;
                v0 = throwableClass.getMethods();
                Intrinsics.checkExpressionValueIsNotNull(v0, "throwableClass.methods");
                var6_6 = v0;
                var7_7 = false;
                var8_8 = var6_6;
                var9_9 = false;
                var10_10 = var8_8;
                var11_11 = var10_10.length;
                for (var12_12 = 0; var12_12 < var11_11; ++var12_12) {
                    it = var13_13 = var10_10[var12_12];
                    $i$a$-find-PlatformImplementations$ReflectAddSuppressedMethod$method$1$1 = false;
                    v1 = it;
                    Intrinsics.checkExpressionValueIsNotNull(v1, "it");
                    if (!Intrinsics.areEqual(v1.getName(), "addSuppressed")) ** GOTO lbl-1000
                    v2 = it.getParameterTypes();
                    Intrinsics.checkExpressionValueIsNotNull(v2, "it.parameterTypes");
                    if (Intrinsics.areEqual(ArraysKt.singleOrNull(v2), throwableClass)) {
                        v3 = true;
                    } else lbl-1000:
                    // 2 sources

                    {
                        v3 = false;
                    }
                    if (!v3) continue;
                    v4 = var13_13;
                    break block3;
                }
                v4 = null;
            }
            ReflectAddSuppressedMethod.method = v4;
        }
    }
}

