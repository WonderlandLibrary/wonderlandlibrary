package javafx.beans.property;

public class SimpleLongProperty extends LongPropertyBase {
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

   public SimpleLongProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleLongProperty(long var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleLongProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleLongProperty(Object var1, String var2, long var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
