package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class DatePicker extends ComboBoxBase {
   private LocalDate lastValidDate;
   private Chronology lastValidChronology;
   private ObjectProperty dayCellFactory;
   private ObjectProperty chronology;
   private BooleanProperty showWeekNumbers;
   private ObjectProperty converter;
   private StringConverter defaultConverter;
   private ReadOnlyObjectWrapper editor;
   private static final String DEFAULT_STYLE_CLASS = "date-picker";

   public DatePicker() {
      this((LocalDate)null);
      this.valueProperty().addListener((var1) -> {
         LocalDate var2 = (LocalDate)this.getValue();
         Chronology var3 = this.getChronology();
         if (this.validateDate(var3, var2)) {
            this.lastValidDate = var2;
         } else {
            System.err.println("Restoring value to " + (this.lastValidDate == null ? "null" : this.getConverter().toString(this.lastValidDate)));
            this.setValue(this.lastValidDate);
         }

      });
      this.chronologyProperty().addListener((var1) -> {
         LocalDate var2 = (LocalDate)this.getValue();
         Chronology var3 = this.getChronology();
         if (this.validateDate(var3, var2)) {
            this.lastValidChronology = var3;
            this.defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, (Locale)null, var3);
         } else {
            System.err.println("Restoring value to " + this.lastValidChronology);
            this.setChronology(this.lastValidChronology);
         }

      });
   }

   private boolean validateDate(Chronology var1, LocalDate var2) {
      try {
         if (var2 != null) {
            var1.date(var2);
         }

         return true;
      } catch (DateTimeException var4) {
         System.err.println(var4);
         return false;
      }
   }

   public DatePicker(LocalDate var1) {
      this.lastValidDate = null;
      this.lastValidChronology = IsoChronology.INSTANCE;
      this.chronology = new SimpleObjectProperty(this, "chronology", (Object)null);
      this.converter = new SimpleObjectProperty(this, "converter", (Object)null);
      this.defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, (Locale)null, this.getChronology());
      this.setValue(var1);
      this.getStyleClass().add("date-picker");
      this.setAccessibleRole(AccessibleRole.DATE_PICKER);
      this.setEditable(true);
   }

   public final void setDayCellFactory(Callback var1) {
      this.dayCellFactoryProperty().set(var1);
   }

   public final Callback getDayCellFactory() {
      return this.dayCellFactory != null ? (Callback)this.dayCellFactory.get() : null;
   }

   public final ObjectProperty dayCellFactoryProperty() {
      if (this.dayCellFactory == null) {
         this.dayCellFactory = new SimpleObjectProperty(this, "dayCellFactory");
      }

      return this.dayCellFactory;
   }

   public final ObjectProperty chronologyProperty() {
      return this.chronology;
   }

   public final Chronology getChronology() {
      Object var1 = (Chronology)this.chronology.get();
      if (var1 == null) {
         try {
            var1 = Chronology.ofLocale(Locale.getDefault(Category.FORMAT));
         } catch (Exception var3) {
            System.err.println(var3);
         }

         if (var1 == null) {
            var1 = IsoChronology.INSTANCE;
         }
      }

      return (Chronology)var1;
   }

   public final void setChronology(Chronology var1) {
      this.chronology.setValue(var1);
   }

   public final BooleanProperty showWeekNumbersProperty() {
      if (this.showWeekNumbers == null) {
         String var1 = Locale.getDefault(Category.FORMAT).getCountry();
         boolean var2 = !var1.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(var1);
         this.showWeekNumbers = new StyleableBooleanProperty(var2) {
            public CssMetaData getCssMetaData() {
               return DatePicker.StyleableProperties.SHOW_WEEK_NUMBERS;
            }

            public Object getBean() {
               return DatePicker.this;
            }

            public String getName() {
               return "showWeekNumbers";
            }
         };
      }

      return this.showWeekNumbers;
   }

   public final void setShowWeekNumbers(boolean var1) {
      this.showWeekNumbersProperty().setValue(var1);
   }

   public final boolean isShowWeekNumbers() {
      return this.showWeekNumbersProperty().getValue();
   }

   public final ObjectProperty converterProperty() {
      return this.converter;
   }

   public final void setConverter(StringConverter var1) {
      this.converterProperty().set(var1);
   }

   public final StringConverter getConverter() {
      StringConverter var1 = (StringConverter)this.converterProperty().get();
      return var1 != null ? var1 : this.defaultConverter;
   }

   public final TextField getEditor() {
      return (TextField)this.editorProperty().get();
   }

   public final ReadOnlyObjectProperty editorProperty() {
      if (this.editor == null) {
         this.editor = new ReadOnlyObjectWrapper(this, "editor");
         this.editor.set(new ComboBoxPopupControl.FakeFocusTextField());
      }

      return this.editor.getReadOnlyProperty();
   }

   protected Skin createDefaultSkin() {
      return new DatePickerSkin(this);
   }

   public static List getClassCssMetaData() {
      return DatePicker.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case DATE:
            return this.getValue();
         case TEXT:
            String var3 = this.getAccessibleText();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            } else {
               LocalDate var4 = (LocalDate)this.getValue();
               StringConverter var5 = this.getConverter();
               if (var4 != null && var5 != null) {
                  return var5.toString(var4);
               }

               return "";
            }
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   private static class StyleableProperties {
      private static final String country;
      private static final CssMetaData SHOW_WEEK_NUMBERS;
      private static final List STYLEABLES;

      static {
         country = Locale.getDefault(Category.FORMAT).getCountry();
         SHOW_WEEK_NUMBERS = new CssMetaData("-fx-show-week-numbers", BooleanConverter.getInstance(), !country.isEmpty() && ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country)) {
            public boolean isSettable(DatePicker var1) {
               return var1.showWeekNumbers == null || !var1.showWeekNumbers.isBound();
            }

            public StyleableProperty getStyleableProperty(DatePicker var1) {
               return (StyleableProperty)var1.showWeekNumbersProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         Collections.addAll(var0, new CssMetaData[]{SHOW_WEEK_NUMBERS});
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
