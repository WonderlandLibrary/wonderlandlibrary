/*
 * Decompiled with CFR 0.152.
 */
package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface ToString {
    public boolean includeFieldNames() default true;

    public String[] exclude() default {};

    public String[] of() default {};

    public boolean callSuper() default false;

    public boolean doNotUseGetters() default false;

    public boolean onlyExplicitlyIncluded() default false;

    @Target(value={ElementType.FIELD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Exclude {
    }

    @Target(value={ElementType.FIELD, ElementType.METHOD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Include {
        public int rank() default 0;

        public String name() default "";
    }
}

