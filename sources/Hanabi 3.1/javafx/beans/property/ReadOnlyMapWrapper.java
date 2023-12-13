package javafx.beans.property;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class ReadOnlyMapWrapper extends SimpleMapProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyMapWrapper() {
   }

   public ReadOnlyMapWrapper(ObservableMap var1) {
      super(var1);
   }

   public ReadOnlyMapWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyMapWrapper(Object var1, String var2, ObservableMap var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyMapProperty getReadOnlyProperty() {
      if (this.readOnlyProperty == null) {
         this.readOnlyProperty = new ReadOnlyPropertyImpl();
      }

      return this.readOnlyProperty;
   }

   protected void fireValueChangedEvent() {
      super.fireValueChangedEvent();
      if (this.readOnlyProperty != null) {
         this.readOnlyProperty.fireValueChangedEvent();
      }

   }

   protected void fireValueChangedEvent(MapChangeListener.Change var1) {
      super.fireValueChangedEvent(var1);
      if (this.readOnlyProperty != null) {
         this.readOnlyProperty.fireValueChangedEvent(var1);
      }

   }

   private class ReadOnlyPropertyImpl extends ReadOnlyMapPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public ObservableMap get() {
         return ReadOnlyMapWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyMapWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyMapWrapper.this.getName();
      }

      public ReadOnlyIntegerProperty sizeProperty() {
         return ReadOnlyMapWrapper.this.sizeProperty();
      }

      public ReadOnlyBooleanProperty emptyProperty() {
         return ReadOnlyMapWrapper.this.emptyProperty();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
