package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum<EnumFacing> {
  protected PropertyDirection(String name, Collection<EnumFacing> values) {
    super(name, EnumFacing.class, values);
  }
  
  public static PropertyDirection create(String name) {
    return create(name, Predicates.alwaysTrue());
  }
  
  public static PropertyDirection create(String name, Predicate<EnumFacing> filter) {
    return create(name, Collections2.filter(Lists.newArrayList((Object[])EnumFacing.values()), filter));
  }
  
  public static PropertyDirection create(String name, Collection<EnumFacing> values) {
    return new PropertyDirection(name, values);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\properties\PropertyDirection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */