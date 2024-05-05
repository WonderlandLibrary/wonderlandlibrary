package space.lunaclient.luna.api.setting;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModeSetting
{
  String name();
  
  String currentOption();
  
  String[] options();
  
  boolean locked();
}
