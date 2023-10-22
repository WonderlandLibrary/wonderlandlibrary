package javafx.beans.property;

public class ReadOnlyLongWrapper extends SimpleLongProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyLongWrapper() {
   }

   public ReadOnlyLongWrapper(long var1) {
      super(var1);
   }

   public ReadOnlyLongWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyLongWrapper(Object var1, String var2, long var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyLongProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyLongPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public long get() {
         return ReadOnlyLongWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyLongWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyLongWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
