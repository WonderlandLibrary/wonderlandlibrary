package javafx.beans.property;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ReadOnlyListWrapper extends SimpleListProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyListWrapper() {
   }

   public ReadOnlyListWrapper(ObservableList var1) {
      super(var1);
   }

   public ReadOnlyListWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyListWrapper(Object var1, String var2, ObservableList var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyListProperty getReadOnlyProperty() {
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

   protected void fireValueChangedEvent(ListChangeListener.Change var1) {
      super.fireValueChangedEvent(var1);
      if (this.readOnlyProperty != null) {
         var1.reset();
         this.readOnlyProperty.fireValueChangedEvent(var1);
      }

   }

   private class ReadOnlyPropertyImpl extends ReadOnlyListPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public ObservableList get() {
         return ReadOnlyListWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyListWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyListWrapper.this.getName();
      }

      public ReadOnlyIntegerProperty sizeProperty() {
         return ReadOnlyListWrapper.this.sizeProperty();
      }

      public ReadOnlyBooleanProperty emptyProperty() {
         return ReadOnlyListWrapper.this.emptyProperty();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
