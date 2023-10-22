package javafx.beans.property;

public class ReadOnlyIntegerWrapper extends SimpleIntegerProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyIntegerWrapper() {
   }

   public ReadOnlyIntegerWrapper(int var1) {
      super(var1);
   }

   public ReadOnlyIntegerWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyIntegerWrapper(Object var1, String var2, int var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyIntegerProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyIntegerPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public int get() {
         return ReadOnlyIntegerWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyIntegerWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyIntegerWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
