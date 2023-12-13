package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.SpinnerSkin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.StringConverter;

public class Spinner extends Control {
   private static final String DEFAULT_STYLE_CLASS = "spinner";
   public static final String STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL = "arrows-on-right-horizontal";
   public static final String STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL = "arrows-on-left-vertical";
   public static final String STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL = "arrows-on-left-horizontal";
   public static final String STYLE_CLASS_SPLIT_ARROWS_VERTICAL = "split-arrows-vertical";
   public static final String STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL = "split-arrows-horizontal";
   private ReadOnlyObjectWrapper value;
   private ObjectProperty valueFactory;
   private BooleanProperty editable;
   private TextField textField;
   private ReadOnlyObjectWrapper editor;

   public Spinner() {
      this.value = new ReadOnlyObjectWrapper(this, "value");
      this.valueFactory = new SimpleObjectProperty(this, "valueFactory") {
         protected void invalidated() {
            Spinner.this.value.unbind();
            SpinnerValueFactory var1 = (SpinnerValueFactory)this.get();
            if (var1 != null) {
               Spinner.this.value.bind(var1.valueProperty());
            }

         }
      };
      this.getStyleClass().add("spinner");
      this.setAccessibleRole(AccessibleRole.SPINNER);
      this.getEditor().setOnAction((var1) -> {
         String var2 = this.getEditor().getText();
         SpinnerValueFactory var3 = this.getValueFactory();
         if (var3 != null) {
            StringConverter var4 = var3.getConverter();
            if (var4 != null) {
               Object var5 = var4.fromString(var2);
               var3.setValue(var5);
            }
         }

      });
      this.getEditor().editableProperty().bind(this.editableProperty());
      this.value.addListener((var1, var2, var3) -> {
         this.setText(var3);
      });
      this.getProperties().addListener((var1) -> {
         if (var1.wasAdded() && var1.getKey() == "FOCUSED") {
            this.setFocused((Boolean)var1.getValueAdded());
            this.getProperties().remove("FOCUSED");
         }

      });
   }

   public Spinner(@NamedArg("min") int var1, @NamedArg("max") int var2, @NamedArg("initialValue") int var3) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.IntegerSpinnerValueFactory(var1, var2, var3)));
   }

   public Spinner(@NamedArg("min") int var1, @NamedArg("max") int var2, @NamedArg("initialValue") int var3, @NamedArg("amountToStepBy") int var4) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.IntegerSpinnerValueFactory(var1, var2, var3, var4)));
   }

   public Spinner(@NamedArg("min") double var1, @NamedArg("max") double var3, @NamedArg("initialValue") double var5) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.DoubleSpinnerValueFactory(var1, var3, var5)));
   }

   public Spinner(@NamedArg("min") double var1, @NamedArg("max") double var3, @NamedArg("initialValue") double var5, @NamedArg("amountToStepBy") double var7) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.DoubleSpinnerValueFactory(var1, var3, var5, var7)));
   }

   Spinner(@NamedArg("min") LocalDate var1, @NamedArg("max") LocalDate var2, @NamedArg("initialValue") LocalDate var3) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.LocalDateSpinnerValueFactory(var1, var2, var3)));
   }

   Spinner(@NamedArg("min") LocalDate var1, @NamedArg("max") LocalDate var2, @NamedArg("initialValue") LocalDate var3, @NamedArg("amountToStepBy") long var4, @NamedArg("temporalUnit") TemporalUnit var6) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.LocalDateSpinnerValueFactory(var1, var2, var3, var4, var6)));
   }

   Spinner(@NamedArg("min") LocalTime var1, @NamedArg("max") LocalTime var2, @NamedArg("initialValue") LocalTime var3) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(var1, var2, var3)));
   }

   Spinner(@NamedArg("min") LocalTime var1, @NamedArg("max") LocalTime var2, @NamedArg("initialValue") LocalTime var3, @NamedArg("amountToStepBy") long var4, @NamedArg("temporalUnit") TemporalUnit var6) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(var1, var2, var3, var4, var6)));
   }

   public Spinner(@NamedArg("items") ObservableList var1) {
      this((SpinnerValueFactory)(new SpinnerValueFactory.ListSpinnerValueFactory(var1)));
   }

   public Spinner(@NamedArg("valueFactory") SpinnerValueFactory var1) {
      this();
      this.setValueFactory(var1);
   }

   public void increment() {
      this.increment(1);
   }

   public void increment(int var1) {
      SpinnerValueFactory var2 = this.getValueFactory();
      if (var2 == null) {
         throw new IllegalStateException("Can't increment Spinner with a null SpinnerValueFactory");
      } else {
         this.commitEditorText();
         var2.increment(var1);
      }
   }

   public void decrement() {
      this.decrement(1);
   }

   public void decrement(int var1) {
      SpinnerValueFactory var2 = this.getValueFactory();
      if (var2 == null) {
         throw new IllegalStateException("Can't decrement Spinner with a null SpinnerValueFactory");
      } else {
         this.commitEditorText();
         var2.decrement(var1);
      }
   }

   protected Skin createDefaultSkin() {
      return new SpinnerSkin(this);
   }

   public final Object getValue() {
      return this.value.get();
   }

   public final ReadOnlyObjectProperty valueProperty() {
      return this.value;
   }

   public final void setValueFactory(SpinnerValueFactory var1) {
      this.valueFactory.setValue(var1);
   }

   public final SpinnerValueFactory getValueFactory() {
      return (SpinnerValueFactory)this.valueFactory.get();
   }

   public final ObjectProperty valueFactoryProperty() {
      return this.valueFactory;
   }

   public final void setEditable(boolean var1) {
      this.editableProperty().set(var1);
   }

   public final boolean isEditable() {
      return this.editable == null ? true : this.editable.get();
   }

   public final BooleanProperty editableProperty() {
      if (this.editable == null) {
         this.editable = new SimpleBooleanProperty(this, "editable", false);
      }

      return this.editable;
   }

   public final ReadOnlyObjectProperty editorProperty() {
      if (this.editor == null) {
         this.editor = new ReadOnlyObjectWrapper(this, "editor");
         this.textField = new ComboBoxPopupControl.FakeFocusTextField();
         this.editor.set(this.textField);
      }

      return this.editor.getReadOnlyProperty();
   }

   public final TextField getEditor() {
      return (TextField)this.editorProperty().get();
   }

   private void setText(Object var1) {
      String var2 = null;
      SpinnerValueFactory var3 = this.getValueFactory();
      if (var3 != null) {
         StringConverter var4 = var3.getConverter();
         if (var4 != null) {
            var2 = var4.toString(var1);
         }
      }

      this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
      if (var2 == null) {
         if (var1 == null) {
            this.getEditor().clear();
            return;
         }

         var2 = var1.toString();
      }

      this.getEditor().setText(var2);
   }

   static int wrapValue(int var0, int var1, int var2) {
      if (var2 == 0) {
         throw new RuntimeException();
      } else {
         int var3 = var0 % var2;
         if (var3 > var1 && var2 < var1) {
            var3 = var3 + var2 - var1;
         } else if (var3 < var1 && var2 > var1) {
            var3 = var3 + var2 - var1;
         }

         return var3;
      }
   }

   static BigDecimal wrapValue(BigDecimal var0, BigDecimal var1, BigDecimal var2) {
      if (var2.doubleValue() == 0.0) {
         throw new RuntimeException();
      } else if (var0.compareTo(var1) < 0) {
         return var2;
      } else {
         return var0.compareTo(var2) > 0 ? var1 : var0;
      }
   }

   private void commitEditorText() {
      if (this.isEditable()) {
         String var1 = this.getEditor().getText();
         SpinnerValueFactory var2 = this.getValueFactory();
         if (var2 != null) {
            StringConverter var3 = var2.getConverter();
            if (var3 != null) {
               Object var4 = var3.fromString(var1);
               var2.setValue(var4);
            }
         }

      }
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            Object var3 = this.getValue();
            SpinnerValueFactory var4 = this.getValueFactory();
            if (var4 != null) {
               StringConverter var5 = var4.getConverter();
               if (var5 != null) {
                  return var5.toString(var3);
               }
            }

            return var3 != null ? var3.toString() : "";
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case INCREMENT:
            this.increment();
            break;
         case DECREMENT:
            this.decrement();
            break;
         default:
            super.executeAccessibleAction(var1);
      }

   }
}
