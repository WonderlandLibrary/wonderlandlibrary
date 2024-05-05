package team.massacre.api.property.impl;

import java.util.function.Supplier;
import team.massacre.api.property.Property;

public class EnumProperty<T extends Enum<T>> extends Property<T> {
   private final T[] values;

   public EnumProperty(String label, T value, Supplier<Boolean> dependency) {
      super(label, value, dependency);
      this.values = this.getEnumConstants();
   }

   public EnumProperty(String label, T value) {
      this(label, value, () -> {
         return true;
      });
   }

   private T[] getEnumConstants() {
      return (Enum[])((Enum[])((Enum)this.value).getClass().getEnumConstants());
   }

   public T[] getValues() {
      return this.values;
   }

   public void setValue(int index) {
      this.setValue(this.values[index]);
   }
}
