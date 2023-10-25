package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;

@GwtCompatible(emulated = true)
final class Platform {
  static long systemNanoTime() {
    return System.nanoTime();
  }
  
  static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
    return matcher.precomputedInternal();
  }
  
  static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
    WeakReference<? extends Enum<?>> ref = Enums.<T>getEnumConstants(enumClass).get(value);
    return (ref == null) ? Optional.<T>absent() : Optional.<T>of(enumClass.cast(ref.get()));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */