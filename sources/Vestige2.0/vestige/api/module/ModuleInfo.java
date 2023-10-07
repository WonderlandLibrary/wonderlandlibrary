package vestige.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {

    String name();
    String description() default "";
    Category category();
    int key() default 0;
    EventListenType listenType() default EventListenType.AUTOMATIC;

}
