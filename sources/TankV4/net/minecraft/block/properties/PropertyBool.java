package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class PropertyBool extends PropertyHelper {
   private final ImmutableSet allowedValues = ImmutableSet.of(true, false);

   public static PropertyBool create(String var0) {
      return new PropertyBool(var0);
   }

   public Collection getAllowedValues() {
      return this.allowedValues;
   }

   protected PropertyBool(String var1) {
      super(var1, Boolean.class);
   }

   public String getName(Boolean var1) {
      return var1.toString();
   }

   public String getName(Comparable var1) {
      return this.getName((Boolean)var1);
   }
}
