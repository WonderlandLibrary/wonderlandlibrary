package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Cell;
import javafx.scene.control.SkinBase;

public class CellSkinBase extends LabeledSkinBase {
   private DoubleProperty cellSize;
   static final double DEFAULT_CELL_SIZE = 24.0;

   public final double getCellSize() {
      return this.cellSize == null ? 24.0 : this.cellSize.get();
   }

   public final ReadOnlyDoubleProperty cellSizeProperty() {
      return this.cellSizePropertyImpl();
   }

   private DoubleProperty cellSizePropertyImpl() {
      if (this.cellSize == null) {
         this.cellSize = new StyleableDoubleProperty(24.0) {
            public void applyStyle(StyleOrigin var1, Number var2) {
               double var3 = var2 == null ? 24.0 : var2.doubleValue();
               super.applyStyle(var1, (Number)(var3 <= 0.0 ? 24.0 : var3));
            }

            public void set(double var1) {
               super.set(var1);
               ((Cell)CellSkinBase.this.getSkinnable()).requestLayout();
            }

            public Object getBean() {
               return CellSkinBase.this;
            }

            public String getName() {
               return "cellSize";
            }

            public CssMetaData getCssMetaData() {
               return CellSkinBase.StyleableProperties.CELL_SIZE;
            }
         };
      }

      return this.cellSize;
   }

   public CellSkinBase(Cell var1, BehaviorBase var2) {
      super(var1, var2);
      this.consumeMouseEvents(false);
   }

   public static List getClassCssMetaData() {
      return CellSkinBase.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData CELL_SIZE = new CssMetaData("-fx-cell-size", SizeConverter.getInstance(), 24.0) {
         public boolean isSettable(Cell var1) {
            CellSkinBase var2 = (CellSkinBase)var1.getSkin();
            return var2.cellSize == null || !var2.cellSize.isBound();
         }

         public StyleableProperty getStyleableProperty(Cell var1) {
            CellSkinBase var2 = (CellSkinBase)var1.getSkin();
            return (StyleableProperty)var2.cellSizePropertyImpl();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
         var0.add(CELL_SIZE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
