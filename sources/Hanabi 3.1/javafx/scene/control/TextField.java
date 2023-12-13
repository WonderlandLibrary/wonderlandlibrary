package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;

public class TextField extends TextInputControl {
   public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
   private IntegerProperty prefColumnCount;
   private ObjectProperty onAction;
   private ObjectProperty alignment;

   public TextField() {
      this("");
   }

   public TextField(String var1) {
      super(new TextFieldContent());
      this.prefColumnCount = new StyleableIntegerProperty(12) {
         private int oldValue = this.get();

         protected void invalidated() {
            int var1 = this.get();
            if (var1 < 0) {
               if (this.isBound()) {
                  this.unbind();
               }

               this.set(this.oldValue);
               throw new IllegalArgumentException("value cannot be negative.");
            } else {
               this.oldValue = var1;
            }
         }

         public CssMetaData getCssMetaData() {
            return TextField.StyleableProperties.PREF_COLUMN_COUNT;
         }

         public Object getBean() {
            return TextField.this;
         }

         public String getName() {
            return "prefColumnCount";
         }
      };
      this.onAction = new ObjectPropertyBase() {
         protected void invalidated() {
            TextField.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
         }

         public Object getBean() {
            return TextField.this;
         }

         public String getName() {
            return "onAction";
         }
      };
      this.getStyleClass().add("text-field");
      this.setAccessibleRole(AccessibleRole.TEXT_FIELD);
      this.setText(var1);
   }

   public CharSequence getCharacters() {
      return ((TextFieldContent)this.getContent()).characters;
   }

   public final IntegerProperty prefColumnCountProperty() {
      return this.prefColumnCount;
   }

   public final int getPrefColumnCount() {
      return this.prefColumnCount.getValue();
   }

   public final void setPrefColumnCount(int var1) {
      this.prefColumnCount.setValue((Number)var1);
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onActionProperty().get();
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public final ObjectProperty alignmentProperty() {
      if (this.alignment == null) {
         this.alignment = new StyleableObjectProperty(Pos.CENTER_LEFT) {
            public CssMetaData getCssMetaData() {
               return TextField.StyleableProperties.ALIGNMENT;
            }

            public Object getBean() {
               return TextField.this;
            }

            public String getName() {
               return "alignment";
            }
         };
      }

      return this.alignment;
   }

   public final void setAlignment(Pos var1) {
      this.alignmentProperty().set(var1);
   }

   public final Pos getAlignment() {
      return this.alignment == null ? Pos.CENTER_LEFT : (Pos)this.alignment.get();
   }

   protected Skin createDefaultSkin() {
      return new TextFieldSkin(this);
   }

   public static List getClassCssMetaData() {
      return TextField.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData ALIGNMENT;
      private static final CssMetaData PREF_COLUMN_COUNT;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER_LEFT) {
            public boolean isSettable(TextField var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(TextField var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }
         };
         PREF_COLUMN_COUNT = new CssMetaData("-fx-pref-column-count", SizeConverter.getInstance(), 12) {
            public boolean isSettable(TextField var1) {
               return var1.prefColumnCount == null || !var1.prefColumnCount.isBound();
            }

            public StyleableProperty getStyleableProperty(TextField var1) {
               return (StyleableProperty)var1.prefColumnCountProperty();
            }
         };
         ArrayList var0 = new ArrayList(TextInputControl.getClassCssMetaData());
         var0.add(ALIGNMENT);
         var0.add(PREF_COLUMN_COUNT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private static final class TextFieldContent implements TextInputControl.Content {
      private ExpressionHelper helper;
      private StringBuilder characters;

      private TextFieldContent() {
         this.helper = null;
         this.characters = new StringBuilder();
      }

      public String get(int var1, int var2) {
         return this.characters.substring(var1, var2);
      }

      public void insert(int var1, String var2, boolean var3) {
         var2 = TextInputControl.filterInput(var2, true, true);
         if (!var2.isEmpty()) {
            this.characters.insert(var1, var2);
            if (var3) {
               ExpressionHelper.fireValueChangedEvent(this.helper);
            }
         }

      }

      public void delete(int var1, int var2, boolean var3) {
         if (var2 > var1) {
            this.characters.delete(var1, var2);
            if (var3) {
               ExpressionHelper.fireValueChangedEvent(this.helper);
            }
         }

      }

      public int length() {
         return this.characters.length();
      }

      public String get() {
         return this.characters.toString();
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public String getValue() {
         return this.get();
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      // $FF: synthetic method
      TextFieldContent(Object var1) {
         this();
      }
   }
}
