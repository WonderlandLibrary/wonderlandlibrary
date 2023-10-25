package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import net.minecraft.util.IStringSerializable;

public class PropertyEnum<T extends Enum<T> & IStringSerializable> extends PropertyHelper<T> {
  private final ImmutableSet<T> allowedValues;
  
  private final Map<String, T> nameToValue = Maps.newHashMap();
  
  protected PropertyEnum(String name, Class<T> valueClass, Collection<T> allowedValues) {
    super(name, valueClass);
    this.allowedValues = ImmutableSet.copyOf(allowedValues);
    for (Enum enum_ : allowedValues) {
      String s = ((IStringSerializable)enum_).getName();
      if (this.nameToValue.containsKey(s))
        throw new IllegalArgumentException("Multiple values have the same name '" + s + "'"); 
      this.nameToValue.put(s, (T)enum_);
    } 
  }
  
  public Collection<T> getAllowedValues() {
    return (Collection<T>)this.allowedValues;
  }
  
  public String getName(T value) {
    return ((IStringSerializable)value).getName();
  }
  
  public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz) {
    return create(name, clazz, Predicates.alwaysTrue());
  }
  
  public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, Predicate<T> filter) {
    return create(name, clazz, Collections2.filter(Lists.newArrayList((Object[])clazz.getEnumConstants()), filter));
  }
  
  public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, T... values) {
    return create(name, clazz, Lists.newArrayList((Object[])values));
  }
  
  public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String name, Class<T> clazz, Collection<T> values) {
    return new PropertyEnum<>(name, clazz, values);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\properties\PropertyEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */