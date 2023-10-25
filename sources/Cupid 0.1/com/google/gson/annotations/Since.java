package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Since {
  double value();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\annotations\Since.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */