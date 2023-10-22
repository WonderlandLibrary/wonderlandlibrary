package javafx.beans.property;

public class ReadOnlyFloatWrapper extends SimpleFloatProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyFloatWrapper() {
   }

   public ReadOnlyFloatWrapper(float var1) {
      super(var1);
   }

   public ReadOnlyFloatWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyFloatWrapper(Object var1, String var2, float var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyFloatProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyFloatPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public float get() {
         return ReadOnlyFloatWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyFloatWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyFloatWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
