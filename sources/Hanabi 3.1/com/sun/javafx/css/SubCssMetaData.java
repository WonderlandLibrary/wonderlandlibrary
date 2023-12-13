package com.sun.javafx.css;

import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.StyleableProperty;
import javafx.scene.Node;

public class SubCssMetaData extends CssMetaData {
   public SubCssMetaData(String var1, StyleConverter var2, Object var3) {
      super(var1, var2, var3);
   }

   public SubCssMetaData(String var1, StyleConverter var2) {
      super(var1, var2);
   }

   public boolean isSettable(Node var1) {
      return false;
   }

   public StyleableProperty getStyleableProperty(Node var1) {
      return null;
   }
}
