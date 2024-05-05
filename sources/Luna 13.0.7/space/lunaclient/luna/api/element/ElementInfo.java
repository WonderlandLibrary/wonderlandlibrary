package space.lunaclient.luna.api.element;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementInfo
{
  String name();
  
  int keyCode() default 0;
  
  boolean toggled() default false;
  
  Category category();
  
  String description() default "";
}
