package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SplitPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;

@DefaultProperty("items")
public class SplitPane extends Control {
   private static final String RESIZABLE_WITH_PARENT = "resizable-with-parent";
   private ObjectProperty orientation;
   private final ObservableList items;
   private final ObservableList dividers;
   private final ObservableList unmodifiableDividers;
   private final WeakHashMap dividerCache;
   private static final String DEFAULT_STYLE_CLASS = "split-pane";
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public static void setResizableWithParent(Node var0, Boolean var1) {
      if (var1 == null) {
         var0.getProperties().remove("resizable-with-parent");
      } else {
         var0.getProperties().put("resizable-with-parent", var1);
      }

   }

   public static Boolean isResizableWithParent(Node var0) {
      if (var0.hasProperties()) {
         Object var1 = var0.getProperties().get("resizable-with-parent");
         if (var1 != null) {
            return (Boolean)var1;
         }
      }

      return true;
   }

   public SplitPane() {
      this((Node[])null);
   }

   public SplitPane(Node... var1) {
      this.items = FXCollections.observableArrayList();
      this.dividers = FXCollections.observableArrayList();
      this.unmodifiableDividers = FXCollections.unmodifiableObservableList(this.dividers);
      this.dividerCache = new WeakHashMap();
      this.getStyleClass().setAll((Object[])("split-pane"));
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.getItems().addListener(new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            label55:
            while(true) {
               int var2;
               if (var1.next()) {
                  var2 = var1.getFrom();
                  int var5 = var2;

                  int var4;
                  for(var4 = 0; var4 < var1.getRemovedSize(); ++var4) {
                     if (var5 < SplitPane.this.dividers.size()) {
                        SplitPane.this.dividerCache.put(var5, Double.MAX_VALUE);
                     } else if (var5 == SplitPane.this.dividers.size() && !SplitPane.this.dividers.isEmpty()) {
                        if (var1.wasReplaced()) {
                           SplitPane.this.dividerCache.put(var5 - 1, ((Divider)SplitPane.this.dividers.get(var5 - 1)).getPosition());
                        } else {
                           SplitPane.this.dividerCache.put(var5 - 1, Double.MAX_VALUE);
                        }
                     }

                     ++var5;
                  }

                  var4 = 0;

                  while(true) {
                     if (var4 >= SplitPane.this.dividers.size()) {
                        continue label55;
                     }

                     if (SplitPane.this.dividerCache.get(var4) == null) {
                        SplitPane.this.dividerCache.put(var4, ((Divider)SplitPane.this.dividers.get(var4)).getPosition());
                     }

                     ++var4;
                  }
               }

               SplitPane.this.dividers.clear();

               for(var2 = 0; var2 < SplitPane.this.getItems().size() - 1; ++var2) {
                  if (SplitPane.this.dividerCache.containsKey(var2) && (Double)SplitPane.this.dividerCache.get(var2) != Double.MAX_VALUE) {
                     Divider var3 = new Divider();
                     var3.setPosition((Double)SplitPane.this.dividerCache.get(var2));
                     SplitPane.this.dividers.add(var3);
                  } else {
                     SplitPane.this.dividers.add(new Divider());
                  }

                  SplitPane.this.dividerCache.remove(var2);
               }

               return;
            }
         }
      });
      if (var1 != null) {
         this.getItems().addAll(var1);
      }

      this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
   }

   public final void setOrientation(Orientation var1) {
      this.orientationProperty().set(var1);
   }

   public final Orientation getOrientation() {
      return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
   }

   public final ObjectProperty orientationProperty() {
      if (this.orientation == null) {
         this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) {
            public void invalidated() {
               boolean var1 = this.get() == Orientation.VERTICAL;
               SplitPane.this.pseudoClassStateChanged(SplitPane.VERTICAL_PSEUDOCLASS_STATE, var1);
               SplitPane.this.pseudoClassStateChanged(SplitPane.HORIZONTAL_PSEUDOCLASS_STATE, !var1);
            }

            public CssMetaData getCssMetaData() {
               return SplitPane.StyleableProperties.ORIENTATION;
            }

            public Object getBean() {
               return SplitPane.this;
            }

            public String getName() {
               return "orientation";
            }
         };
      }

      return this.orientation;
   }

   public ObservableList getItems() {
      return this.items;
   }

   public ObservableList getDividers() {
      return this.unmodifiableDividers;
   }

   public void setDividerPosition(int var1, double var2) {
      if (this.getDividers().size() <= var1) {
         this.dividerCache.put(var1, var2);
      } else {
         if (var1 >= 0) {
            ((Divider)this.getDividers().get(var1)).setPosition(var2);
         }

      }
   }

   public void setDividerPositions(double... var1) {
      int var2;
      if (this.dividers.isEmpty()) {
         for(var2 = 0; var2 < var1.length; ++var2) {
            this.dividerCache.put(var2, var1[var2]);
         }

      } else {
         for(var2 = 0; var2 < var1.length && var2 < this.dividers.size(); ++var2) {
            ((Divider)this.dividers.get(var2)).setPosition(var1[var2]);
         }

      }
   }

   public double[] getDividerPositions() {
      double[] var1 = new double[this.dividers.size()];

      for(int var2 = 0; var2 < this.dividers.size(); ++var2) {
         var1[var2] = ((Divider)this.dividers.get(var2)).getPosition();
      }

      return var1;
   }

   protected Skin createDefaultSkin() {
      return new SplitPaneSkin(this);
   }

   public static List getClassCssMetaData() {
      return SplitPane.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   public static class Divider {
      private DoubleProperty position;

      public final void setPosition(double var1) {
         this.positionProperty().set(var1);
      }

      public final double getPosition() {
         return this.position == null ? 0.5 : this.position.get();
      }

      public final DoubleProperty positionProperty() {
         if (this.position == null) {
            this.position = new SimpleDoubleProperty(this, "position", 0.5);
         }

         return this.position;
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData ORIENTATION;
      private static final List STYLEABLES;

      static {
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(SplitPane var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(SplitPane var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(SplitPane var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(ORIENTATION);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
