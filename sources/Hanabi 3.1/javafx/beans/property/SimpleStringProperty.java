package javafx.beans.property;

public class SimpleStringProperty extends StringPropertyBase {
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

   public SimpleStringProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleStringProperty(String var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleStringProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleStringProperty(Object var1, String var2, String var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
