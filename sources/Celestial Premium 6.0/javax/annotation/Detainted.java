/*
 * Decompiled with CFR 0.150.
 */
package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Untainted;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Untainted(when=When.ALWAYS)
@Retention(value=RetentionPolicy.RUNTIME)
@TypeQualifierNickname
public @interface Detainted {
}

