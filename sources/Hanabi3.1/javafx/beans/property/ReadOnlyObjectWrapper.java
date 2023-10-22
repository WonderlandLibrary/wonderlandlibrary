package javafx.beans.property;

public class ReadOnlyObjectWrapper extends SimpleObjectProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyObjectWrapper() {
   }

   public ReadOnlyObjectWrapper(Object var1) {
      super(var1);
   }

   public ReadOnlyObjectWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyObjectWrapper(Object var1, String var2, Object var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyObjectProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public Object get() {
         return ReadOnlyObjectWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyObjectWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyObjectWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
