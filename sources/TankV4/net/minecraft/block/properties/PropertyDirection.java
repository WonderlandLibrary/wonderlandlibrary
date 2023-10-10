package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum {
   public static PropertyDirection create(String var0) {
      return create(var0, Predicates.alwaysTrue());
   }

   protected PropertyDirection(String var1, Collection var2) {
      super(var1, EnumFacing.class, var2);
   }

   public static PropertyDirection create(String var0, Predicate var1) {
      return create(var0, Collections2.filter(Lists.newArrayList((Object[])EnumFacing.values()), var1));
   }

   public static PropertyDirection create(String var0, Collection var1) {
      return new PropertyDirection(var0, var1);
   }
}
