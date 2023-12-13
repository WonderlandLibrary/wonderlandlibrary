package javafx.css;

import javafx.beans.NamedArg;

public class SimpleStyleableFloatProperty extends StyleableFloatProperty {
   private static final Object DEFAULT_BEAN = null;
   private static final String DEFAULT_NAME = "";
   private final Object bean;
   private final String name;
   private final CssMetaData cssMetaData;

   public SimpleStyleableFloatProperty(@NamedArg("cssMetaData") CssMetaData var1) {
      this(var1, DEFAULT_BEAN, "");
   }

   public SimpleStyleableFloatProperty(@NamedArg("cssMetaData") CssMetaData var1, @NamedArg("initialValue") Float var2) {
      this(var1, DEFAULT_BEAN, "", var2);
   }

   public SimpleStyleableFloatProperty(@NamedArg("cssMetaData") CssMetaData var1, @NamedArg("bean") Object var2, @NamedArg("name") String var3) {
      this.bean = var2;
      this.name = var3 == null ? "" : var3;
      this.cssMetaData = var1;
   }

   public SimpleStyleableFloatProperty(@NamedArg("cssMetaData") CssMetaData var1, @NamedArg("bean") Object var2, @NamedArg("name") String var3, @NamedArg("initialValue") Float var4) {
      super(var4);
      this.bean = var2;
      this.name = var3 == null ? "" : var3;
      this.cssMetaData = var1;
   }

   public Object getBean() {
      return this.bean;
   }

   public String getName() {
      return this.name;
   }

   public final CssMetaData getCssMetaData() {
      return this.cssMetaData;
   }
}
