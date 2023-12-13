package javafx.beans.property;

import javafx.collections.ObservableMap;

public class SimpleMapProperty extends MapPropertyBase {
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

   public SimpleMapProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleMapProperty(ObservableMap var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleMapProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleMapProperty(Object var1, String var2, ObservableMap var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
