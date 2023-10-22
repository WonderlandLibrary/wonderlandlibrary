package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ColorPickerBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class ColorPickerSkin extends ComboBoxPopupControl {
   private Label displayNode;
   private StackPane pickerColorBox;
   private Rectangle colorRect;
   private ColorPalette popupContent;
   BooleanProperty colorLabelVisible = new StyleableBooleanProperty(true) {
      public void invalidated() {
         if (ColorPickerSkin.this.displayNode != null) {
            if (ColorPickerSkin.this.colorLabelVisible.get()) {
               ColorPickerSkin.this.displayNode.setText(ColorPickerSkin.colorDisplayName((Color)((ColorPicker)ColorPickerSkin.this.getSkinnable()).getValue()));
            } else {
               ColorPickerSkin.this.displayNode.setText("");
            }
         }

      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "colorLabelVisible";
      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.COLOR_LABEL_VISIBLE;
      }
   };
   private final StyleableStringProperty imageUrl = new StyleableStringProperty() {
      public void applyStyle(StyleOrigin var1, String var2) {
         super.applyStyle(var1, var2);
         if (var2 == null) {
            if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
               ColorPickerSkin.this.pickerColorBox.getChildren().remove(1);
            }
         } else if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
            ImageView var3 = (ImageView)ColorPickerSkin.this.pickerColorBox.getChildren().get(1);
            var3.setImage(StyleManager.getInstance().getCachedImage(var2));
         } else {
            ColorPickerSkin.this.pickerColorBox.getChildren().add(new ImageView(StyleManager.getInstance().getCachedImage(var2)));
         }

      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "imageUrl";
      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.GRAPHIC;
      }
   };
   private final StyleableDoubleProperty colorRectWidth = new StyleableDoubleProperty(12.0) {
      protected void invalidated() {
         if (ColorPickerSkin.this.pickerColorBox != null) {
            ColorPickerSkin.this.pickerColorBox.requestLayout();
         }

      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.COLOR_RECT_WIDTH;
      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "colorRectWidth";
      }
   };
   private final StyleableDoubleProperty colorRectHeight = new StyleableDoubleProperty(12.0) {
      protected void invalidated() {
         if (ColorPickerSkin.this.pickerColorBox != null) {
            ColorPickerSkin.this.pickerColorBox.requestLayout();
         }

      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.COLOR_RECT_HEIGHT;
      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "colorRectHeight";
      }
   };
   private final StyleableDoubleProperty colorRectX = new StyleableDoubleProperty(0.0) {
      protected void invalidated() {
         if (ColorPickerSkin.this.pickerColorBox != null) {
            ColorPickerSkin.this.pickerColorBox.requestLayout();
         }

      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.COLOR_RECT_X;
      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "colorRectX";
      }
   };
   private final StyleableDoubleProperty colorRectY = new StyleableDoubleProperty(0.0) {
      protected void invalidated() {
         if (ColorPickerSkin.this.pickerColorBox != null) {
            ColorPickerSkin.this.pickerColorBox.requestLayout();
         }

      }

      public CssMetaData getCssMetaData() {
         return ColorPickerSkin.StyleableProperties.COLOR_RECT_Y;
      }

      public Object getBean() {
         return ColorPickerSkin.this;
      }

      public String getName() {
         return "colorRectY";
      }
   };
   private static final Map colorNameMap = new HashMap(24);
   private static final Map cssNameMap = new HashMap(139);

   public StringProperty imageUrlProperty() {
      return this.imageUrl;
   }

   public ColorPickerSkin(ColorPicker var1) {
      super(var1, new ColorPickerBehavior(var1));
      this.updateComboBoxMode();
      this.registerChangeListener(var1.valueProperty(), "VALUE");
      this.displayNode = new Label();
      this.displayNode.getStyleClass().add("color-picker-label");
      this.displayNode.setManaged(false);
      this.pickerColorBox = new PickerColorBox();
      this.pickerColorBox.getStyleClass().add("picker-color");
      this.colorRect = new Rectangle(12.0, 12.0);
      this.colorRect.getStyleClass().add("picker-color-rect");
      this.updateColor();
      this.pickerColorBox.getChildren().add(this.colorRect);
      this.displayNode.setGraphic(this.pickerColorBox);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      if (!this.colorLabelVisible.get()) {
         return super.computePrefWidth(var1, var3, var5, var7, var9);
      } else {
         String var11 = this.displayNode.getText();
         double var12 = 0.0;

         for(Iterator var14 = colorNameMap.values().iterator(); var14.hasNext(); var12 = Math.max(var12, super.computePrefWidth(var1, var3, var5, var7, var9))) {
            String var15 = (String)var14.next();
            this.displayNode.setText(var15);
         }

         this.displayNode.setText(formatHexString(Color.BLACK));
         var12 = Math.max(var12, super.computePrefWidth(var1, var3, var5, var7, var9));
         this.displayNode.setText(var11);
         return var12;
      }
   }

   private void updateComboBoxMode() {
      ObservableList var1 = ((ComboBoxBase)this.getSkinnable()).getStyleClass();
      if (var1.contains("button")) {
         this.setMode(ComboBoxMode.BUTTON);
      } else if (var1.contains("split-button")) {
         this.setMode(ComboBoxMode.SPLITBUTTON);
      }

   }

   static String colorDisplayName(Color var0) {
      if (var0 != null) {
         String var1 = (String)colorNameMap.get(var0);
         if (var1 == null) {
            var1 = formatHexString(var0);
         }

         return var1;
      } else {
         return null;
      }
   }

   static String tooltipString(Color var0) {
      if (var0 != null) {
         String var1 = "";
         String var2 = (String)colorNameMap.get(var0);
         if (var2 != null) {
            var1 = var1 + var2 + " ";
         }

         var1 = var1 + formatHexString(var0);
         String var3 = (String)cssNameMap.get(var0);
         if (var3 != null) {
            var1 = var1 + " (css: " + var3 + ")";
         }

         return var1;
      } else {
         return null;
      }
   }

   static String formatHexString(Color var0) {
      return var0 != null ? String.format((Locale)null, "#%02x%02x%02x", Math.round(var0.getRed() * 255.0), Math.round(var0.getGreen() * 255.0), Math.round(var0.getBlue() * 255.0)) : null;
   }

   protected Node getPopupContent() {
      if (this.popupContent == null) {
         this.popupContent = new ColorPalette((ColorPicker)this.getSkinnable());
         this.popupContent.setPopupControl(this.getPopup());
      }

      return this.popupContent;
   }

   protected void focusLost() {
   }

   public void show() {
      super.show();
      ColorPicker var1 = (ColorPicker)this.getSkinnable();
      this.popupContent.updateSelection((Color)var1.getValue());
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("SHOWING".equals(var1)) {
         if (((ComboBoxBase)this.getSkinnable()).isShowing()) {
            this.show();
         } else if (!this.popupContent.isCustomColorDialogShowing()) {
            this.hide();
         }
      } else if ("VALUE".equals(var1)) {
         this.updateColor();
         if (this.popupContent != null) {
         }
      }

   }

   public Node getDisplayNode() {
      return this.displayNode;
   }

   private void updateColor() {
      ColorPicker var1 = (ColorPicker)this.getSkinnable();
      this.colorRect.setFill((Paint)var1.getValue());
      if (this.colorLabelVisible.get()) {
         this.displayNode.setText(colorDisplayName((Color)var1.getValue()));
      } else {
         this.displayNode.setText("");
      }

   }

   public void syncWithAutoUpdate() {
      if (!this.getPopup().isShowing() && ((ComboBoxBase)this.getSkinnable()).isShowing()) {
         ((ComboBoxBase)this.getSkinnable()).hide();
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.updateComboBoxMode();
      super.layoutChildren(var1, var3, var5, var7);
   }

   static String getString(String var0) {
      return ControlResources.getString("ColorPicker." + var0);
   }

   public static List getClassCssMetaData() {
      return ColorPickerSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected StringConverter getConverter() {
      return null;
   }

   protected TextField getEditor() {
      return null;
   }

   static {
      colorNameMap.put(Color.TRANSPARENT, getString("colorName.transparent"));
      colorNameMap.put(Color.BLACK, getString("colorName.black"));
      colorNameMap.put(Color.BLUE, getString("colorName.blue"));
      colorNameMap.put(Color.CYAN, getString("colorName.cyan"));
      colorNameMap.put(Color.DARKBLUE, getString("colorName.darkblue"));
      colorNameMap.put(Color.DARKCYAN, getString("colorName.darkcyan"));
      colorNameMap.put(Color.DARKGRAY, getString("colorName.darkgray"));
      colorNameMap.put(Color.DARKGREEN, getString("colorName.darkgreen"));
      colorNameMap.put(Color.DARKMAGENTA, getString("colorName.darkmagenta"));
      colorNameMap.put(Color.DARKRED, getString("colorName.darkred"));
      colorNameMap.put(Color.GRAY, getString("colorName.gray"));
      colorNameMap.put(Color.GREEN, getString("colorName.green"));
      colorNameMap.put(Color.LIGHTBLUE, getString("colorName.lightblue"));
      colorNameMap.put(Color.LIGHTCYAN, getString("colorName.lightcyan"));
      colorNameMap.put(Color.LIGHTGRAY, getString("colorName.lightgray"));
      colorNameMap.put(Color.LIGHTGREEN, getString("colorName.lightgreen"));
      colorNameMap.put(Color.LIGHTYELLOW, getString("colorName.lightyellow"));
      colorNameMap.put(Color.MAGENTA, getString("colorName.magenta"));
      colorNameMap.put(Color.MEDIUMBLUE, getString("colorName.mediumblue"));
      colorNameMap.put(Color.ORANGE, getString("colorName.orange"));
      colorNameMap.put(Color.PINK, getString("colorName.pink"));
      colorNameMap.put(Color.RED, getString("colorName.red"));
      colorNameMap.put(Color.WHITE, getString("colorName.white"));
      colorNameMap.put(Color.YELLOW, getString("colorName.yellow"));
      cssNameMap.put(Color.ALICEBLUE, "aliceblue");
      cssNameMap.put(Color.ANTIQUEWHITE, "antiquewhite");
      cssNameMap.put(Color.AQUAMARINE, "aquamarine");
      cssNameMap.put(Color.AZURE, "azure");
      cssNameMap.put(Color.BEIGE, "beige");
      cssNameMap.put(Color.BISQUE, "bisque");
      cssNameMap.put(Color.BLACK, "black");
      cssNameMap.put(Color.BLANCHEDALMOND, "blanchedalmond");
      cssNameMap.put(Color.BLUE, "blue");
      cssNameMap.put(Color.BLUEVIOLET, "blueviolet");
      cssNameMap.put(Color.BROWN, "brown");
      cssNameMap.put(Color.BURLYWOOD, "burlywood");
      cssNameMap.put(Color.CADETBLUE, "cadetblue");
      cssNameMap.put(Color.CHARTREUSE, "chartreuse");
      cssNameMap.put(Color.CHOCOLATE, "chocolate");
      cssNameMap.put(Color.CORAL, "coral");
      cssNameMap.put(Color.CORNFLOWERBLUE, "cornflowerblue");
      cssNameMap.put(Color.CORNSILK, "cornsilk");
      cssNameMap.put(Color.CRIMSON, "crimson");
      cssNameMap.put(Color.CYAN, "cyan");
      cssNameMap.put(Color.DARKBLUE, "darkblue");
      cssNameMap.put(Color.DARKCYAN, "darkcyan");
      cssNameMap.put(Color.DARKGOLDENROD, "darkgoldenrod");
      cssNameMap.put(Color.DARKGRAY, "darkgray");
      cssNameMap.put(Color.DARKGREEN, "darkgreen");
      cssNameMap.put(Color.DARKKHAKI, "darkkhaki");
      cssNameMap.put(Color.DARKMAGENTA, "darkmagenta");
      cssNameMap.put(Color.DARKOLIVEGREEN, "darkolivegreen");
      cssNameMap.put(Color.DARKORANGE, "darkorange");
      cssNameMap.put(Color.DARKORCHID, "darkorchid");
      cssNameMap.put(Color.DARKRED, "darkred");
      cssNameMap.put(Color.DARKSALMON, "darksalmon");
      cssNameMap.put(Color.DARKSEAGREEN, "darkseagreen");
      cssNameMap.put(Color.DARKSLATEBLUE, "darkslateblue");
      cssNameMap.put(Color.DARKSLATEGRAY, "darkslategray");
      cssNameMap.put(Color.DARKTURQUOISE, "darkturquoise");
      cssNameMap.put(Color.DARKVIOLET, "darkviolet");
      cssNameMap.put(Color.DEEPPINK, "deeppink");
      cssNameMap.put(Color.DEEPSKYBLUE, "deepskyblue");
      cssNameMap.put(Color.DIMGRAY, "dimgray");
      cssNameMap.put(Color.DODGERBLUE, "dodgerblue");
      cssNameMap.put(Color.FIREBRICK, "firebrick");
      cssNameMap.put(Color.FLORALWHITE, "floralwhite");
      cssNameMap.put(Color.FORESTGREEN, "forestgreen");
      cssNameMap.put(Color.GAINSBORO, "gainsboro");
      cssNameMap.put(Color.GHOSTWHITE, "ghostwhite");
      cssNameMap.put(Color.GOLD, "gold");
      cssNameMap.put(Color.GOLDENROD, "goldenrod");
      cssNameMap.put(Color.GRAY, "gray");
      cssNameMap.put(Color.GREEN, "green");
      cssNameMap.put(Color.GREENYELLOW, "greenyellow");
      cssNameMap.put(Color.HONEYDEW, "honeydew");
      cssNameMap.put(Color.HOTPINK, "hotpink");
      cssNameMap.put(Color.INDIANRED, "indianred");
      cssNameMap.put(Color.INDIGO, "indigo");
      cssNameMap.put(Color.IVORY, "ivory");
      cssNameMap.put(Color.KHAKI, "khaki");
      cssNameMap.put(Color.LAVENDER, "lavender");
      cssNameMap.put(Color.LAVENDERBLUSH, "lavenderblush");
      cssNameMap.put(Color.LAWNGREEN, "lawngreen");
      cssNameMap.put(Color.LEMONCHIFFON, "lemonchiffon");
      cssNameMap.put(Color.LIGHTBLUE, "lightblue");
      cssNameMap.put(Color.LIGHTCORAL, "lightcoral");
      cssNameMap.put(Color.LIGHTCYAN, "lightcyan");
      cssNameMap.put(Color.LIGHTGOLDENRODYELLOW, "lightgoldenrodyellow");
      cssNameMap.put(Color.LIGHTGRAY, "lightgray");
      cssNameMap.put(Color.LIGHTGREEN, "lightgreen");
      cssNameMap.put(Color.LIGHTPINK, "lightpink");
      cssNameMap.put(Color.LIGHTSALMON, "lightsalmon");
      cssNameMap.put(Color.LIGHTSEAGREEN, "lightseagreen");
      cssNameMap.put(Color.LIGHTSKYBLUE, "lightskyblue");
      cssNameMap.put(Color.LIGHTSLATEGRAY, "lightslategray");
      cssNameMap.put(Color.LIGHTSTEELBLUE, "lightsteelblue");
      cssNameMap.put(Color.LIGHTYELLOW, "lightyellow");
      cssNameMap.put(Color.LIME, "lime");
      cssNameMap.put(Color.LIMEGREEN, "limegreen");
      cssNameMap.put(Color.LINEN, "linen");
      cssNameMap.put(Color.MAGENTA, "magenta");
      cssNameMap.put(Color.MAROON, "maroon");
      cssNameMap.put(Color.MEDIUMAQUAMARINE, "mediumaquamarine");
      cssNameMap.put(Color.MEDIUMBLUE, "mediumblue");
      cssNameMap.put(Color.MEDIUMORCHID, "mediumorchid");
      cssNameMap.put(Color.MEDIUMPURPLE, "mediumpurple");
      cssNameMap.put(Color.MEDIUMSEAGREEN, "mediumseagreen");
      cssNameMap.put(Color.MEDIUMSLATEBLUE, "mediumslateblue");
      cssNameMap.put(Color.MEDIUMSPRINGGREEN, "mediumspringgreen");
      cssNameMap.put(Color.MEDIUMTURQUOISE, "mediumturquoise");
      cssNameMap.put(Color.MEDIUMVIOLETRED, "mediumvioletred");
      cssNameMap.put(Color.MIDNIGHTBLUE, "midnightblue");
      cssNameMap.put(Color.MINTCREAM, "mintcream");
      cssNameMap.put(Color.MISTYROSE, "mistyrose");
      cssNameMap.put(Color.MOCCASIN, "moccasin");
      cssNameMap.put(Color.NAVAJOWHITE, "navajowhite");
      cssNameMap.put(Color.NAVY, "navy");
      cssNameMap.put(Color.OLDLACE, "oldlace");
      cssNameMap.put(Color.OLIVE, "olive");
      cssNameMap.put(Color.OLIVEDRAB, "olivedrab");
      cssNameMap.put(Color.ORANGE, "orange");
      cssNameMap.put(Color.ORANGERED, "orangered");
      cssNameMap.put(Color.ORCHID, "orchid");
      cssNameMap.put(Color.PALEGOLDENROD, "palegoldenrod");
      cssNameMap.put(Color.PALEGREEN, "palegreen");
      cssNameMap.put(Color.PALETURQUOISE, "paleturquoise");
      cssNameMap.put(Color.PALEVIOLETRED, "palevioletred");
      cssNameMap.put(Color.PAPAYAWHIP, "papayawhip");
      cssNameMap.put(Color.PEACHPUFF, "peachpuff");
      cssNameMap.put(Color.PERU, "peru");
      cssNameMap.put(Color.PINK, "pink");
      cssNameMap.put(Color.PLUM, "plum");
      cssNameMap.put(Color.POWDERBLUE, "powderblue");
      cssNameMap.put(Color.PURPLE, "purple");
      cssNameMap.put(Color.RED, "red");
      cssNameMap.put(Color.ROSYBROWN, "rosybrown");
      cssNameMap.put(Color.ROYALBLUE, "royalblue");
      cssNameMap.put(Color.SADDLEBROWN, "saddlebrown");
      cssNameMap.put(Color.SALMON, "salmon");
      cssNameMap.put(Color.SANDYBROWN, "sandybrown");
      cssNameMap.put(Color.SEAGREEN, "seagreen");
      cssNameMap.put(Color.SEASHELL, "seashell");
      cssNameMap.put(Color.SIENNA, "sienna");
      cssNameMap.put(Color.SILVER, "silver");
      cssNameMap.put(Color.SKYBLUE, "skyblue");
      cssNameMap.put(Color.SLATEBLUE, "slateblue");
      cssNameMap.put(Color.SLATEGRAY, "slategray");
      cssNameMap.put(Color.SNOW, "snow");
      cssNameMap.put(Color.SPRINGGREEN, "springgreen");
      cssNameMap.put(Color.STEELBLUE, "steelblue");
      cssNameMap.put(Color.TAN, "tan");
      cssNameMap.put(Color.TEAL, "teal");
      cssNameMap.put(Color.THISTLE, "thistle");
      cssNameMap.put(Color.TOMATO, "tomato");
      cssNameMap.put(Color.TRANSPARENT, "transparent");
      cssNameMap.put(Color.TURQUOISE, "turquoise");
      cssNameMap.put(Color.VIOLET, "violet");
      cssNameMap.put(Color.WHEAT, "wheat");
      cssNameMap.put(Color.WHITE, "white");
      cssNameMap.put(Color.WHITESMOKE, "whitesmoke");
      cssNameMap.put(Color.YELLOW, "yellow");
      cssNameMap.put(Color.YELLOWGREEN, "yellowgreen");
   }

   private static class StyleableProperties {
      private static final CssMetaData COLOR_LABEL_VISIBLE;
      private static final CssMetaData COLOR_RECT_WIDTH;
      private static final CssMetaData COLOR_RECT_HEIGHT;
      private static final CssMetaData COLOR_RECT_X;
      private static final CssMetaData COLOR_RECT_Y;
      private static final CssMetaData GRAPHIC;
      private static final List STYLEABLES;

      static {
         COLOR_LABEL_VISIBLE = new CssMetaData("-fx-color-label-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.colorLabelVisible == null || !var2.colorLabelVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return (StyleableProperty)var2.colorLabelVisible;
            }
         };
         COLOR_RECT_WIDTH = new CssMetaData("-fx-color-rect-width", SizeConverter.getInstance(), 12.0) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return !var2.colorRectWidth.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.colorRectWidth;
            }
         };
         COLOR_RECT_HEIGHT = new CssMetaData("-fx-color-rect-height", SizeConverter.getInstance(), 12.0) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return !var2.colorRectHeight.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.colorRectHeight;
            }
         };
         COLOR_RECT_X = new CssMetaData("-fx-color-rect-x", SizeConverter.getInstance(), 0) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return !var2.colorRectX.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.colorRectX;
            }
         };
         COLOR_RECT_Y = new CssMetaData("-fx-color-rect-y", SizeConverter.getInstance(), 0) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return !var2.colorRectY.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.colorRectY;
            }
         };
         GRAPHIC = new CssMetaData("-fx-graphic", com.sun.javafx.css.converters.StringConverter.getInstance()) {
            public boolean isSettable(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return !var2.imageUrl.isBound();
            }

            public StyleableProperty getStyleableProperty(ColorPicker var1) {
               ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
               return var2.imageUrl;
            }
         };
         ArrayList var0 = new ArrayList(ComboBoxBaseSkin.getClassCssMetaData());
         var0.add(COLOR_LABEL_VISIBLE);
         var0.add(COLOR_RECT_WIDTH);
         var0.add(COLOR_RECT_HEIGHT);
         var0.add(COLOR_RECT_X);
         var0.add(COLOR_RECT_Y);
         var0.add(GRAPHIC);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private class PickerColorBox extends StackPane {
      private PickerColorBox() {
      }

      protected void layoutChildren() {
         double var1 = this.snappedTopInset();
         double var3 = this.snappedLeftInset();
         double var5 = this.getWidth();
         double var7 = this.getHeight();
         double var9 = this.snappedRightInset();
         double var11 = this.snappedBottomInset();
         ColorPickerSkin.this.colorRect.setX(this.snapPosition(ColorPickerSkin.this.colorRectX.get()));
         ColorPickerSkin.this.colorRect.setY(this.snapPosition(ColorPickerSkin.this.colorRectY.get()));
         ColorPickerSkin.this.colorRect.setWidth(this.snapSize(ColorPickerSkin.this.colorRectWidth.get()));
         ColorPickerSkin.this.colorRect.setHeight(this.snapSize(ColorPickerSkin.this.colorRectHeight.get()));
         if (this.getChildren().size() == 2) {
            ImageView var13 = (ImageView)this.getChildren().get(1);
            Pos var14 = StackPane.getAlignment(var13);
            this.layoutInArea(var13, var3, var1, var5 - var3 - var9, var7 - var1 - var11, 0.0, getMargin(var13), var14 != null ? var14.getHpos() : this.getAlignment().getHpos(), var14 != null ? var14.getVpos() : this.getAlignment().getVpos());
            ColorPickerSkin.this.colorRect.setLayoutX(var13.getLayoutX());
            ColorPickerSkin.this.colorRect.setLayoutY(var13.getLayoutY());
         } else {
            Pos var15 = StackPane.getAlignment(ColorPickerSkin.this.colorRect);
            this.layoutInArea(ColorPickerSkin.this.colorRect, var3, var1, var5 - var3 - var9, var7 - var1 - var11, 0.0, getMargin(ColorPickerSkin.this.colorRect), var15 != null ? var15.getHpos() : this.getAlignment().getHpos(), var15 != null ? var15.getVpos() : this.getAlignment().getVpos());
         }

      }

      // $FF: synthetic method
      PickerColorBox(Object var2) {
         this();
      }
   }
}
