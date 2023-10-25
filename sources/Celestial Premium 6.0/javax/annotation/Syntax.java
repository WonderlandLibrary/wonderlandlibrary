/*
 * Decompiled with CFR 0.150.
 */
package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

@Documented
@TypeQualifier(applicableTo=CharSequence.class)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Syntax {
    public String value();

    public When when() default When.ALWAYS;
}

