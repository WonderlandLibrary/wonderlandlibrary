package me.rhys.base.module.setting.manifest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Clamp {
  double min();
  
  double max();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\setting\manifest\Clamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */