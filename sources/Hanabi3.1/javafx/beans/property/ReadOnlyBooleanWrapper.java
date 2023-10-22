package javafx.beans.property;

public class ReadOnlyBooleanWrapper extends SimpleBooleanProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyBooleanWrapper() {
   }

   public ReadOnlyBooleanWrapper(boolean var1) {
      super(var1);
   }

   public ReadOnlyBooleanWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyBooleanWrapper(Object var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyBooleanProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyBooleanPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public boolean get() {
         return ReadOnlyBooleanWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyBooleanWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyBooleanWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
