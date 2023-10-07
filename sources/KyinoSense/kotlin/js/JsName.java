/*
 * Decompiled with CFR 0.152.
 */
package kotlin.js;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.OptionalExpectation;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Target;

@Target(allowedTargets={AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER})
@Retention(value=RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\"\u0018\u00002\u00020\u0001B\b\u0012\u0006\u0010\u0002\u001a\u00020\u0003R\u0013\u0010\u0002\u001a\u00020\u0003X\u0086\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2={"Lkotlin/js/JsName;", "", "name", "", "()Ljava/lang/String;", "kotlin-stdlib"})
@OptionalExpectation
@interface JsName {
    public String name();
}

