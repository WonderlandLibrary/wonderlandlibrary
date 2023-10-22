package javafx.beans.property;

public class SimpleIntegerProperty extends IntegerPropertyBase {
   private static final Object DEFAULT_BEAN = null;
   private static final String DEFAULT_NAME = "";
   private final Object bean;
   private final String name;

   public Object getBean() {
      return this.bean;
   }

   public String getName() {
      return this.name;
   }

   public SimpleIntegerProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleIntegerProperty(int var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleIntegerProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleIntegerProperty(Object var1, String var2, int var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
