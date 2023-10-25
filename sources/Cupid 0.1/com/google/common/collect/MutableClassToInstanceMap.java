package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MutableClassToInstanceMap<B> extends MapConstraints.ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {
  public static <B> MutableClassToInstanceMap<B> create() {
    return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
  }
  
  public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
    return new MutableClassToInstanceMap<B>(backingMap);
  }
  
  private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
    super(delegate, (MapConstraint)VALUE_CAN_BE_CAST_TO_KEY);
  }
  
  private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>() {
      public void checkKeyValue(Class<?> key, Object value) {
        MutableClassToInstanceMap.cast((Class)key, (B)value);
      }
    };
  
  private static final long serialVersionUID = 0L;
  
  public <T extends B> T putInstance(Class<T> type, T value) {
    return cast(type, put(type, (B)value));
  }
  
  public <T extends B> T getInstance(Class<T> type) {
    return cast(type, get(type));
  }
  
  private static <B, T extends B> T cast(Class<T> type, B value) {
    return Primitives.wrap(type).cast(value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\MutableClassToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */