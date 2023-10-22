package javafx.beans.property;

public class ReadOnlyStringWrapper extends SimpleStringProperty {
   private ReadOnlyPropertyImpl readOnlyProperty;

   public ReadOnlyStringWrapper() {
   }

   public ReadOnlyStringWrapper(String var1) {
      super(var1);
   }

   public ReadOnlyStringWrapper(Object var1, String var2) {
      super(var1, var2);
   }

   public ReadOnlyStringWrapper(Object var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   public ReadOnlyStringProperty getReadOnlyProperty() {
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

   private class ReadOnlyPropertyImpl extends ReadOnlyStringPropertyBase {
      private ReadOnlyPropertyImpl() {
      }

      public String get() {
         return ReadOnlyStringWrapper.this.get();
      }

      public Object getBean() {
         return ReadOnlyStringWrapper.this.getBean();
      }

      public String getName() {
         return ReadOnlyStringWrapper.this.getName();
      }

      // $FF: synthetic method
      ReadOnlyPropertyImpl(Object var2) {
         this();
      }
   }
}
