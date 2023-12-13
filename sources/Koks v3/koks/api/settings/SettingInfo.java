package koks.api.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kroko
 * @created on 07.10.2020 : 16:05
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingInfo {

    String name();

    float min() default 0;
    float max() default 0;
    boolean onlyInt() default false;

    String[] modes() default "";

}
