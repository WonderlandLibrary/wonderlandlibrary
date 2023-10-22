package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DatePickerBehavior;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.HijrahChronology;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class DatePickerSkin extends ComboBoxPopupControl {
   private DatePicker datePicker;
   private TextField displayNode;
   private DatePickerContent datePickerContent;

   public DatePickerSkin(DatePicker var1) {
      super(var1, new DatePickerBehavior(var1));
      this.datePicker = var1;
      this.arrow.paddingProperty().addListener(new InvalidationListener() {
         private boolean rounding = false;

         public void invalidated(Observable var1) {
            if (!this.rounding) {
               Insets var2 = DatePickerSkin.this.arrow.getPadding();
               Insets var3 = new Insets((double)Math.round(var2.getTop()), (double)Math.round(var2.getRight()), (double)Math.round(var2.getBottom()), (double)Math.round(var2.getLeft()));
               if (!var3.equals(var2)) {
                  this.rounding = true;
                  DatePickerSkin.this.arrow.setPadding(var3);
                  this.rounding = false;
               }
            }

         }
      });
      this.registerChangeListener(var1.chronologyProperty(), "CHRONOLOGY");
      this.registerChangeListener(var1.converterProperty(), "CONVERTER");
      this.registerChangeListener(var1.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
      this.registerChangeListener(var1.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
      this.registerChangeListener(var1.valueProperty(), "VALUE");
   }

   public Node getPopupContent() {
      if (this.datePickerContent == null) {
         if (this.datePicker.getChronology() instanceof HijrahChronology) {
            this.datePickerContent = new DatePickerHijrahContent(this.datePicker);
         } else {
            this.datePickerContent = new DatePickerContent(this.datePicker);
         }
      }

      return this.datePickerContent;
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return 50.0;
   }

   protected void focusLost() {
   }

   public void show() {
      super.show();
      this.datePickerContent.clearFocus();
   }

   protected void handleControlPropertyChanged(String var1) {
      if (!"CHRONOLOGY".equals(var1) && !"DAY_CELL_FACTORY".equals(var1)) {
         if ("CONVERTER".equals(var1)) {
            this.updateDisplayNode();
         } else if ("EDITOR".equals(var1)) {
            this.getEditableInputNode();
         } else {
            LocalDate var2;
            if ("SHOWING".equals(var1)) {
               if (this.datePicker.isShowing()) {
                  if (this.datePickerContent != null) {
                     var2 = (LocalDate)this.datePicker.getValue();
                     this.datePickerContent.displayedYearMonthProperty().set(var2 != null ? YearMonth.from(var2) : YearMonth.now());
                     this.datePickerContent.updateValues();
                  }

                  this.show();
               } else {
                  this.hide();
               }
            } else if ("SHOW_WEEK_NUMBERS".equals(var1)) {
               if (this.datePickerContent != null) {
                  this.datePickerContent.updateGrid();
                  this.datePickerContent.updateWeeknumberDateCells();
               }
            } else if ("VALUE".equals(var1)) {
               this.updateDisplayNode();
               if (this.datePickerContent != null) {
                  var2 = (LocalDate)this.datePicker.getValue();
                  this.datePickerContent.displayedYearMonthProperty().set(var2 != null ? YearMonth.from(var2) : YearMonth.now());
                  this.datePickerContent.updateValues();
               }

               this.datePicker.fireEvent(new ActionEvent());
            } else {
               super.handleControlPropertyChanged(var1);
            }
         }
      } else {
         this.updateDisplayNode();
         this.datePickerContent = null;
         this.popup = null;
      }

   }

   protected TextField getEditor() {
      return ((DatePicker)this.getSkinnable()).getEditor();
   }

   protected StringConverter getConverter() {
      return ((DatePicker)this.getSkinnable()).getConverter();
   }

   public Node getDisplayNode() {
      if (this.displayNode == null) {
         this.displayNode = this.getEditableInputNode();
         this.displayNode.getStyleClass().add("date-picker-display-node");
         this.updateDisplayNode();
      }

      this.displayNode.setEditable(this.datePicker.isEditable());
      return this.displayNode;
   }

   public void syncWithAutoUpdate() {
      if (!this.getPopup().isShowing() && this.datePicker.isShowing()) {
         this.datePicker.hide();
      }

   }
}
