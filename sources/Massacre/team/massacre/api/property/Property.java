package team.massacre.api.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class Property<T> {
   protected final String label;
   protected String cachedNormName;
   protected final Supplier<Boolean> dependency;
   private final List<ValueChangeListener<T>> valueChangeListeners;
   protected T value;

   public Property(String label, T value, Supplier<Boolean> dependency) {
      this.valueChangeListeners = new ArrayList();
      this.label = label;
      this.value = value;
      this.dependency = dependency;
   }

   public Property(String label, T value) {
      this(label, value, () -> {
         return true;
      });
   }

   public void addValueChangeListener(ValueChangeListener<T> valueChangeListener) {
      this.valueChangeListeners.add(valueChangeListener);
   }

   public String getLabelNoSpaces() {
      if (this.cachedNormName == null) {
         this.cachedNormName = this.label.replace(" ", "");
      }

      return this.cachedNormName;
   }

   public boolean isAvailable() {
      return (Boolean)this.dependency.get();
   }

   public String getLabel() {
      return this.label;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T value) {
      T oldValue = this.value;
      this.value = value;
      if (oldValue != value) {
         Iterator var3 = this.valueChangeListeners.iterator();

         while(var3.hasNext()) {
            ValueChangeListener<T> valueChangeListener = (ValueChangeListener)var3.next();
            valueChangeListener.onValueChange(oldValue, value);
         }
      }

   }

   public void callFirstTime() {
      Iterator var1 = this.valueChangeListeners.iterator();

      while(var1.hasNext()) {
         ValueChangeListener<T> valueChangeListener = (ValueChangeListener)var1.next();
         valueChangeListener.onValueChange(this.value, this.value);
      }

   }

   public Class<?> getType() {
      return this.value.getClass();
   }
}
