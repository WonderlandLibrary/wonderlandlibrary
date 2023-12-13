package team.massacre.api.property.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import team.massacre.api.property.Property;

public class MultiSelectEnumProperty<T extends Enum<T>> extends Property<List<T>> {
   private final T[] values;

   @SafeVarargs
   public MultiSelectEnumProperty(String label, Supplier<Boolean> dependency, T... values) {
      super(label, Arrays.asList(values), dependency);
      if (values.length == 0) {
         throw new RuntimeException("Must have at least one default value.");
      } else {
         this.values = this.getEnumConstants();
      }
   }

   @SafeVarargs
   public MultiSelectEnumProperty(String label, T... values) {
      this(label, () -> {
         return true;
      }, values);
   }

   private T[] getEnumConstants() {
      return (Enum[])((Enum[])((Enum)((List)this.value).get(0)).getClass().getEnumConstants());
   }

   public T[] getValues() {
      return this.values;
   }

   public boolean isSelected(T variant) {
      return ((List)this.getValue()).contains(variant);
   }

   public void setValue(int index) {
      List<T> values = new ArrayList((Collection)this.value);
      T referencedVariant = this.values[index];
      if (values.contains(referencedVariant)) {
         values.remove(referencedVariant);
      } else {
         values.add(referencedVariant);
      }

      this.setValue(values);
   }
}
