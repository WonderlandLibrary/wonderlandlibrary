package javafx.beans.property;

public class SimpleBooleanProperty extends BooleanPropertyBase {
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

   public SimpleBooleanProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleBooleanProperty(boolean var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleBooleanProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleBooleanProperty(Object var1, String var2, boolean var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
