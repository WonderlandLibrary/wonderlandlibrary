/*
 * Decompiled with CFR 0.152.
 */
package kotlin.annotation;

import kotlin.Metadata;
import kotlin.SinceKotlin;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0011\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011\u00a8\u0006\u0012"}, d2={"Lkotlin/annotation/AnnotationTarget;", "", "(Ljava/lang/String;I)V", "CLASS", "ANNOTATION_CLASS", "TYPE_PARAMETER", "PROPERTY", "FIELD", "LOCAL_VARIABLE", "VALUE_PARAMETER", "CONSTRUCTOR", "FUNCTION", "PROPERTY_GETTER", "PROPERTY_SETTER", "TYPE", "EXPRESSION", "FILE", "TYPEALIAS", "kotlin-stdlib"})
public final class AnnotationTarget
extends Enum<AnnotationTarget> {
    public static final /* enum */ AnnotationTarget CLASS = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget ANNOTATION_CLASS = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget TYPE_PARAMETER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FIELD = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget LOCAL_VARIABLE = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget VALUE_PARAMETER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget CONSTRUCTOR = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FUNCTION = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY_GETTER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY_SETTER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget TYPE = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget EXPRESSION = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FILE = new AnnotationTarget();
    @SinceKotlin(version="1.1")
    public static final /* enum */ AnnotationTarget TYPEALIAS = new AnnotationTarget();
    private static final /* synthetic */ AnnotationTarget[] $VALUES;

    public static AnnotationTarget[] values() {
        return (AnnotationTarget[])$VALUES.clone();
    }

    public static AnnotationTarget valueOf(String value) {
        return Enum.valueOf(AnnotationTarget.class, value);
    }

    static {
        $VALUES = annotationTargetArray = new AnnotationTarget[]{AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.TYPE, AnnotationTarget.EXPRESSION, AnnotationTarget.FILE, AnnotationTarget.TYPEALIAS};
    }
}

