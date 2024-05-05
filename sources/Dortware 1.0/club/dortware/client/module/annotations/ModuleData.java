package club.dortware.client.module.annotations;

import club.dortware.client.module.enums.ModuleCategory;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An {@code Annotation} used for module info.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModuleData {

    String name();

    String description() default "Default description :(";

    ModuleCategory category();

    int defaultKeyBind() default 0;

}
