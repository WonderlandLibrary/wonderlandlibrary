/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module;

import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import net.dev.important.modules.module.Category;

@Retention(value=AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(value=RetentionPolicy.RUNTIME)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0087\u0002\u0018\u00002\u00020\u0001BJ\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003R\u000f\u0010\f\u001a\u00020\u000b\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u000f\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000eR\u000f\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u000fR\u000f\u0010\r\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0010R\u000f\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0010R\u000f\u0010\b\u001a\u00020\t\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0011R\u000f\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0010R\u000f\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0010\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/module/Info;", "", "name", "", "spacedName", "description", "category", "Lnet/dev/important/modules/module/Category;", "keyBind", "", "canEnable", "", "array", "cnName", "()Z", "()Lnet/dev/important/modules/module/Category;", "()Ljava/lang/String;", "()I", "LiquidBounce"})
public @interface Info {
    public String name();

    public String spacedName() default "";

    public String description();

    public Category category();

    public int keyBind() default 0;

    public boolean canEnable() default true;

    public boolean array() default true;

    public String cnName() default "unknown";
}

