package javafx.beans.property;

public class ReadOnlyDoubleWrapper extends SimpleDoubleProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyDoubleWrapper() {
   }

   public ReadOnlyDoubleWrapper(double var1) {
      super(var1);
   }

   public ReadOnlyDoubleWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyDoubleWrapper(Object var1, String var2, double var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyDoubleProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyDoublePropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public double get() {
         return ReadOnlyDoubleWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyDoubleWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyDoubleWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
