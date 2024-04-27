/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.gui.client.hud.element;

import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Retention(value=AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(value=RetentionPolicy.RUNTIME)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\b\u0087\u0002\u0018\u00002\u00020\u0001B:\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005R\u000f\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u000bR\u000f\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u000bR\u000f\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\fR\u000f\u0010\b\u001a\u00020\t\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\rR\u000f\u0010\n\u001a\u00020\u0005\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000f\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u000b\u00a8\u0006\u000e"}, d2={"Lnet/dev/important/gui/client/hud/element/ElementInfo;", "", "name", "", "single", "", "force", "disableScale", "priority", "", "retrieveDamage", "()Z", "()Ljava/lang/String;", "()I", "LiquidBounce"})
public @interface ElementInfo {
    public String name();

    public boolean single() default false;

    public boolean force() default false;

    public boolean disableScale() default false;

    public int priority() default 0;

    public boolean retrieveDamage() default false;
}

