package javafx.beans.property;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ReadOnlySetWrapper extends SimpleSetProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlySetWrapper() {
   }

   public ReadOnlySetWrapper(ObservableSet var1) {
      super(var1);
   }

   public ReadOnlySetWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlySetWrapper(Object var1, String var2, ObservableSet var3) {
      super(var1, var2, var3);
   }

   public ReadOnlySetProperty getReadOnlyProperty() {
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

   protected void fireValueChangedEvent(SetChangeListener.Change var1) {
      super.fireValueChangedEvent(var1);
      if (this.readOnlyProperty != null) {
         this.readOnlyProperty.fireValueChangedEvent(var1);
      }

   }

   private class ReadOnlyPropertyImpl extends ReadOnlySetPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public ObservableSet get() {
         return ReadOnlySetWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlySetWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlySetWrapper.this.getName();
      }

      public ReadOnlyIntegerProperty sizeProperty() {
         return ReadOnlySetWrapper.this.sizeProperty();
      }

      public ReadOnlyBooleanProperty emptyProperty() {
         return ReadOnlySetWrapper.this.emptyProperty();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
