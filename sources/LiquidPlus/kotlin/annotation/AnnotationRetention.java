/*
 * Decompiled with CFR 0.152.
 */
package kotlin.annotation;

import kotlin.Metadata;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lkotlin/annotation/AnnotationRetention;", "", "(Ljava/lang/String;I)V", "SOURCE", "BINARY", "RUNTIME", "kotlin-stdlib"})
public final class AnnotationRetention
extends Enum<AnnotationRetention> {
    public static final /* enum */ AnnotationRetention SOURCE = new AnnotationRetention();
    public static final /* enum */ AnnotationRetention BINARY = new AnnotationRetention();
    public static final /* enum */ AnnotationRetention RUNTIME = new AnnotationRetention();
    private static final /* synthetic */ AnnotationRetention[] $VALUES;

    public static AnnotationRetention[] values() {
        return (AnnotationRetention[])$VALUES.clone();
    }

    public static AnnotationRetention valueOf(String value) {
        return Enum.valueOf(AnnotationRetention.class, value);
    }

    static {
        $VALUES = annotationRetentionArray = new AnnotationRetention[]{AnnotationRetention.SOURCE, AnnotationRetention.BINARY, AnnotationRetention.RUNTIME};
    }
}

