/*
 * Decompiled with CFR 0.152.
 */
package kotlin.native.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.OptionalExpectation;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@Target(allowedTargets={AnnotationTarget.PROPERTY, AnnotationTarget.CLASS})
@Retention(value=AnnotationRetention.BINARY)
@java.lang.annotation.Retention(value=RetentionPolicy.CLASS)
@java.lang.annotation.Target(value={ElementType.TYPE})
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\"\u0018\u00002\u00020\u0001B\u0000\u00a8\u0006\u0002"}, d2={"Lkotlin/native/concurrent/ThreadLocal;", "", "kotlin-stdlib"})
@OptionalExpectation
@interface ThreadLocal {
}

