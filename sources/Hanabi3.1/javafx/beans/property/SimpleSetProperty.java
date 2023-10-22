package javafx.beans.property;

import javafx.collections.ObservableSet;

public class SimpleSetProperty extends SetPropertyBase {
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

   public SimpleSetProperty() {
      this(DEFAULT_BEAN, "");
   }

   public SimpleSetProperty(ObservableSet var1) {
      this(DEFAULT_BEAN, "", var1);
   }

   public SimpleSetProperty(Object var1, String var2) {
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }

   public SimpleSetProperty(Object var1, String var2, ObservableSet var3) {
      super(var3);
      this.bean = var1;
      this.name = var2 == null ? "" : var2;
   }
}
