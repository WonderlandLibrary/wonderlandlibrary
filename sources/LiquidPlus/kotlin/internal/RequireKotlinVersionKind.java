/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.Metadata;
import kotlin.SinceKotlin;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0081\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lkotlin/internal/RequireKotlinVersionKind;", "", "(Ljava/lang/String;I)V", "LANGUAGE_VERSION", "COMPILER_VERSION", "API_VERSION", "kotlin-stdlib"})
@SinceKotlin(version="1.2")
public final class RequireKotlinVersionKind
extends Enum<RequireKotlinVersionKind> {
    public static final /* enum */ RequireKotlinVersionKind LANGUAGE_VERSION = new RequireKotlinVersionKind();
    public static final /* enum */ RequireKotlinVersionKind COMPILER_VERSION = new RequireKotlinVersionKind();
    public static final /* enum */ RequireKotlinVersionKind API_VERSION = new RequireKotlinVersionKind();
    private static final /* synthetic */ RequireKotlinVersionKind[] $VALUES;

    public static RequireKotlinVersionKind[] values() {
        return (RequireKotlinVersionKind[])$VALUES.clone();
    }

    public static RequireKotlinVersionKind valueOf(String value) {
        return Enum.valueOf(RequireKotlinVersionKind.class, value);
    }

    static {
        $VALUES = requireKotlinVersionKindArray = new RequireKotlinVersionKind[]{RequireKotlinVersionKind.LANGUAGE_VERSION, RequireKotlinVersionKind.COMPILER_VERSION, RequireKotlinVersionKind.API_VERSION};
    }
}

