package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Locale.Category;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DatePickerContent extends VBox {
   protected DatePicker datePicker;
   private Button backMonthButton;
   private Button forwardMonthButton;
   private Button backYearButton;
   private Button forwardYearButton;
   private Label monthLabel;
   private Label yearLabel;
   protected GridPane gridPane;
   private int daysPerWeek;
   private List dayNameCells = new ArrayList();
   private List weekNumberCells = new ArrayList();
   protected List dayCells = new ArrayList();
   private LocalDate[] dayCellDates;
   private DateCell lastFocusedDayCell = null;
   final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
   final DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL");
   final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("y");
   final DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy");
   final DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern("w");
   final DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc");
   final DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern("d");
   private ObjectProperty displayedYearMonth = new SimpleObjectProperty(this, "displayedYearMonth");

   static String getString(String var0) {
      return ControlResources.getString("DatePicker." + var0);
   }

   DatePickerContent(final DatePicker var1) {
      this.datePicker = var1;
      this.getStyleClass().add("date-picker-popup");
      this.daysPerWeek = this.getDaysPerWeek();
      LocalDate var2 = (LocalDate)var1.getValue();
      this.displayedYearMonth.set(var2 != null ? YearMonth.from(var2) : YearMonth.now());
      this.displayedYearMonth.addListener((var1x, var2x, var3x) -> {
         this.updateValues();
      });
      this.getChildren().add(this.createMonthYearPane());
      this.gridPane = new GridPane() {
         protected double computePrefWidth(double var1x) {
            double var3 = super.computePrefWidth(var1x);
            int var5 = DatePickerContent.this.daysPerWeek + (var1.isShowWeekNumbers() ? 1 : 0);
            double var6 = this.snapSpace(this.getHgap());
            double var8 = this.snapSpace(this.getInsets().getLeft());
            double var10 = this.snapSpace(this.getInsets().getRight());
            double var12 = var6 * (double)(var5 - 1);
            double var14 = var3 - var8 - var10 - var12;
            return this.snapSize(var14 / (double)var5) * (double)var5 + var8 + var10 + var12;
         }

         protected void layoutChildren() {
            if (this.getWidth() > 0.0 && this.getHeight() > 0.0) {
               super.layoutChildren();
            }

         }
      };
      this.gridPane.setFocusTraversable(true);
      this.gridPane.getStyleClass().add("calendar-grid");
      this.gridPane.setVgap(-1.0);
      this.gridPane.setHgap(-1.0);
      WeakChangeListener var5 = new WeakChangeListener((var1x, var2x, var3x) -> {
         if (var3x == this.gridPane) {
            if (var2x instanceof DateCell) {
               this.gridPane.impl_traverse(Direction.PREVIOUS);
            } else if (this.lastFocusedDayCell != null) {
               Platform.runLater(() -> {
                  this.lastFocusedDayCell.requestFocus();
               });
            } else {
               this.clearFocus();
            }
         }

      });
      this.gridPane.sceneProperty().addListener(new WeakChangeListener((var1x, var2x, var3x) -> {
         if (var2x != null) {
            var2x.focusOwnerProperty().removeListener(var5);
         }

         if (var3x != null) {
            var3x.focusOwnerProperty().addListener(var5);
         }

      }));
      if (this.gridPane.getScene() != null) {
         this.gridPane.getScene().focusOwnerProperty().addListener(var5);
      }

      int var3;
      DateCell var4;
      for(var3 = 0; var3 < this.daysPerWeek; ++var3) {
         var4 = new DateCell();
         var4.getStyleClass().add("day-name-cell");
         this.dayNameCells.add(var4);
      }

      for(var3 = 0; var3 < 6; ++var3) {
         var4 = new DateCell();
         var4.getStyleClass().add("week-number-cell");
         this.weekNumberCells.add(var4);
      }

      this.createDayCells();
      this.updateGrid();
      this.getChildren().add(this.gridPane);
      this.refresh();
      this.addEventHandler(KeyEvent.ANY, (var2x) -> {
         Node var3 = this.getScene().getFocusOwner();
         if (var3 instanceof DateCell) {
            this.lastFocusedDayCell = (DateCell)var3;
         }

         if (var2x.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (var2x.getCode()) {
               case HOME:
                  this.goToDate(LocalDate.now(), true);
                  var2x.consume();
                  break;
               case PAGE_UP:
                  if (PlatformUtil.isMac() && var2x.isMetaDown() || !PlatformUtil.isMac() && var2x.isControlDown()) {
                     if (!this.backYearButton.isDisabled()) {
                        this.forward(-1, ChronoUnit.YEARS, true);
                     }
                  } else if (!this.backMonthButton.isDisabled()) {
                     this.forward(-1, ChronoUnit.MONTHS, true);
                  }

                  var2x.consume();
                  break;
               case PAGE_DOWN:
                  if (PlatformUtil.isMac() && var2x.isMetaDown() || !PlatformUtil.isMac() && var2x.isControlDown()) {
                     if (!this.forwardYearButton.isDisabled()) {
                        this.forward(1, ChronoUnit.YEARS, true);
                     }
                  } else if (!this.forwardMonthButton.isDisabled()) {
                     this.forward(1, ChronoUnit.MONTHS, true);
                  }

                  var2x.consume();
            }

            var3 = this.getScene().getFocusOwner();
            if (var3 instanceof DateCell) {
               this.lastFocusedDayCell = (DateCell)var3;
            }
         }

         switch (var2x.getCode()) {
            case F4:
            case F10:
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
            case TAB:
               break;
            case ESCAPE:
               var1.hide();
               var2x.consume();
               break;
            default:
               var2x.consume();
         }

      });
   }

   ObjectProperty displayedYearMonthProperty() {
      return this.displayedYearMonth;
   }

   protected BorderPane createMonthYearPane() {
      BorderPane var1 = new BorderPane();
      var1.getStyleClass().add("month-year-pane");
      HBox var2 = new HBox();
      var2.getStyleClass().add("spinner");
      this.backMonthButton = new Button();
      this.backMonthButton.getStyleClass().add("left-button");
      this.forwardMonthButton = new Button();
      this.forwardMonthButton.getStyleClass().add("right-button");
      StackPane var3 = new StackPane();
      var3.getStyleClass().add("left-arrow");
      var3.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      this.backMonthButton.setGraphic(var3);
      StackPane var4 = new StackPane();
      var4.getStyleClass().add("right-arrow");
      var4.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      this.forwardMonthButton.setGraphic(var4);
      this.backMonthButton.setOnAction((var1x) -> {
         this.forward(-1, ChronoUnit.MONTHS, false);
      });
      this.monthLabel = new Label();
      this.monthLabel.getStyleClass().add("spinner-label");
      this.forwardMonthButton.setOnAction((var1x) -> {
         this.forward(1, ChronoUnit.MONTHS, false);
      });
      var2.getChildren().addAll(this.backMonthButton, this.monthLabel, this.forwardMonthButton);
      var1.setLeft(var2);
      HBox var5 = new HBox();
      var5.getStyleClass().add("spinner");
      this.backYearButton = new Button();
      this.backYearButton.getStyleClass().add("left-button");
      this.forwardYearButton = new Button();
      this.forwardYearButton.getStyleClass().add("right-button");
      StackPane var6 = new StackPane();
      var6.getStyleClass().add("left-arrow");
      var6.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      this.backYearButton.setGraphic(var6);
      StackPane var7 = new StackPane();
      var7.getStyleClass().add("right-arrow");
      var7.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
      this.forwardYearButton.setGraphic(var7);
      this.backYearButton.setOnAction((var1x) -> {
         this.forward(-1, ChronoUnit.YEARS, false);
      });
      this.yearLabel = new Label();
      this.yearLabel.getStyleClass().add("spinner-label");
      this.forwardYearButton.setOnAction((var1x) -> {
         this.forward(1, ChronoUnit.YEARS, false);
      });
      var5.getChildren().addAll(this.backYearButton, this.yearLabel, this.forwardYearButton);
      var5.setFillHeight(false);
      var1.setRight(var5);
      return var1;
   }

   private void refresh() {
      this.updateMonthLabelWidth();
      this.updateDayNameCells();
      this.updateValues();
   }

   void updateValues() {
      this.updateWeeknumberDateCells();
      this.updateDayCells();
      this.updateMonthYearPane();
   }

   void updateGrid() {
      this.gridPane.getColumnConstraints().clear();
      this.gridPane.getChildren().clear();
      int var1 = this.daysPerWeek + (this.datePicker.isShowWeekNumbers() ? 1 : 0);
      ColumnConstraints var2 = new ColumnConstraints();
      var2.setPercentWidth(100.0);

      int var3;
      for(var3 = 0; var3 < var1; ++var3) {
         this.gridPane.getColumnConstraints().add(var2);
      }

      for(var3 = 0; var3 < this.daysPerWeek; ++var3) {
         this.gridPane.add((Node)this.dayNameCells.get(var3), var3 + var1 - this.daysPerWeek, 1);
      }

      if (this.datePicker.isShowWeekNumbers()) {
         for(var3 = 0; var3 < 6; ++var3) {
            this.gridPane.add((Node)this.weekNumberCells.get(var3), 0, var3 + 2);
         }
      }

      for(var3 = 0; var3 < 6; ++var3) {
         for(int var4 = 0; var4 < this.daysPerWeek; ++var4) {
            this.gridPane.add((Node)this.dayCells.get(var3 * this.daysPerWeek + var4), var4 + var1 - this.daysPerWeek, var3 + 2);
         }
      }

   }

   void updateDayNameCells() {
      int var1 = WeekFields.of(this.getLocale()).getFirstDayOfWeek().getValue();
      LocalDate var2 = LocalDate.of(2009, 7, 12 + var1);

      for(int var3 = 0; var3 < this.daysPerWeek; ++var3) {
         String var4 = this.weekDayNameFormatter.withLocale(this.getLocale()).format(var2.plus((long)var3, ChronoUnit.DAYS));
         ((DateCell)this.dayNameCells.get(var3)).setText(this.titleCaseWord(var4));
      }

   }

   void updateWeeknumberDateCells() {
      if (this.datePicker.isShowWeekNumbers()) {
         Locale var1 = this.getLocale();
         LocalDate var3 = ((YearMonth)this.displayedYearMonth.get()).atDay(1);

         for(int var4 = 0; var4 < 6; ++var4) {
            LocalDate var5 = var3.plus((long)var4, ChronoUnit.WEEKS);
            String var6 = this.weekNumberFormatter.withLocale(var1).withDecimalStyle(DecimalStyle.of(var1)).format(var5);
            ((DateCell)this.weekNumberCells.get(var4)).setText(var6);
         }
      }

   }

   void updateDayCells() {
      Locale var1 = this.getLocale();
      Chronology var2 = this.getPrimaryChronology();
      int var3 = this.determineFirstOfMonthDayOfWeek();
      YearMonth var4 = (YearMonth)this.displayedYearMonth.get();
      YearMonth var5 = null;
      YearMonth var6 = null;
      int var7 = -1;
      int var8 = -1;
      boolean var9 = true;

      for(int var10 = 0; var10 < 6 * this.daysPerWeek; ++var10) {
         DateCell var11 = (DateCell)this.dayCells.get(var10);
         var11.getStyleClass().setAll((Object[])("cell", "date-cell", "day-cell"));
         var11.setDisable(false);
         var11.setStyle((String)null);
         var11.setGraphic((Node)null);
         var11.setTooltip((Tooltip)null);

         try {
            if (var7 == -1) {
               var7 = var4.lengthOfMonth();
            }

            YearMonth var12 = var4;
            int var13 = var10 - var3 + 1;
            if (var10 < var3) {
               if (var5 == null) {
                  var5 = var4.minusMonths(1L);
                  var8 = var5.lengthOfMonth();
               }

               var12 = var5;
               var13 = var10 + var8 - var3 + 1;
               var11.getStyleClass().add("previous-month");
            } else if (var10 >= var3 + var7) {
               if (var6 == null) {
                  var6 = var4.plusMonths(1L);
                  int var18 = var6.lengthOfMonth();
               }

               var12 = var6;
               var13 = var10 - var7 - var3 + 1;
               var11.getStyleClass().add("next-month");
            }

            LocalDate var14 = var12.atDay(var13);
            this.dayCellDates[var10] = var14;
            ChronoLocalDate var15 = var2.date(var14);
            var11.setDisable(false);
            if (this.isToday(var14)) {
               var11.getStyleClass().add("today");
            }

            if (var14.equals(this.datePicker.getValue())) {
               var11.getStyleClass().add("selected");
            }

            String var16 = this.dayCellFormatter.withLocale(var1).withChronology(var2).withDecimalStyle(DecimalStyle.of(var1)).format(var15);
            var11.setText(var16);
            var11.updateItem(var14, false);
         } catch (DateTimeException var17) {
            var11.setText(" ");
            var11.setDisable(true);
         }
      }

   }

   private int getDaysPerWeek() {
      ValueRange var1 = this.getPrimaryChronology().range(ChronoField.DAY_OF_WEEK);
      return (int)(var1.getMaximum() - var1.getMinimum() + 1L);
   }

   private int getMonthsPerYear() {
      ValueRange var1 = this.getPrimaryChronology().range(ChronoField.MONTH_OF_YEAR);
      return (int)(var1.getMaximum() - var1.getMinimum() + 1L);
   }

   private void updateMonthLabelWidth() {
      if (this.monthLabel != null) {
         int var1 = this.getMonthsPerYear();
         double var2 = 0.0;

         for(int var4 = 0; var4 < var1; ++var4) {
            YearMonth var5 = ((YearMonth)this.displayedYearMonth.get()).withMonth(var4 + 1);
            String var6 = this.monthFormatterSO.withLocale(this.getLocale()).format(var5);
            if (Character.isDigit(var6.charAt(0))) {
               var6 = this.monthFormatter.withLocale(this.getLocale()).format(var5);
            }

            var2 = Math.max(var2, Utils.computeTextWidth(this.monthLabel.getFont(), var6, 0.0));
         }

         this.monthLabel.setMinWidth(var2);
      }

   }

   protected void updateMonthYearPane() {
      YearMonth var1 = (YearMonth)this.displayedYearMonth.get();
      String var2 = this.formatMonth(var1);
      this.monthLabel.setText(var2);
      var2 = this.formatYear(var1);
      this.yearLabel.setText(var2);
      double var3 = Utils.computeTextWidth(this.yearLabel.getFont(), var2, 0.0);
      if (var3 > this.yearLabel.getMinWidth()) {
         this.yearLabel.setMinWidth(var3);
      }

      Chronology var5 = this.datePicker.getChronology();
      LocalDate var6 = var1.atDay(1);
      this.backMonthButton.setDisable(!this.isValidDate(var5, var6, -1, ChronoUnit.DAYS));
      this.forwardMonthButton.setDisable(!this.isValidDate(var5, var6, 1, ChronoUnit.MONTHS));
      this.backYearButton.setDisable(!this.isValidDate(var5, var6, -1, ChronoUnit.YEARS));
      this.forwardYearButton.setDisable(!this.isValidDate(var5, var6, 1, ChronoUnit.YEARS));
   }

   private String formatMonth(YearMonth var1) {
      Locale var2 = this.getLocale();
      Chronology var3 = this.getPrimaryChronology();

      try {
         ChronoLocalDate var4 = var3.date(var1.atDay(1));
         String var5 = this.monthFormatterSO.withLocale(this.getLocale()).withChronology(var3).format(var4);
         if (Character.isDigit(var5.charAt(0))) {
            var5 = this.monthFormatter.withLocale(this.getLocale()).withChronology(var3).format(var4);
         }

         return this.titleCaseWord(var5);
      } catch (DateTimeException var6) {
         return "";
      }
   }

   private String formatYear(YearMonth var1) {
      Locale var2 = this.getLocale();
      Chronology var3 = this.getPrimaryChronology();

      try {
         DateTimeFormatter var4 = this.yearFormatter;
         ChronoLocalDate var5 = var3.date(var1.atDay(1));
         int var6 = var5.getEra().getValue();
         int var7 = var3.eras().size();
         if (var7 == 2 && var6 == 0 || var7 > 2) {
            var4 = this.yearWithEraFormatter;
         }

         String var8 = var4.withLocale(this.getLocale()).withChronology(var3).withDecimalStyle(DecimalStyle.of(this.getLocale())).format(var5);
         return var8;
      } catch (DateTimeException var9) {
         return "";
      }
   }

   private String titleCaseWord(String var1) {
      if (var1.length() > 0) {
         int var2 = var1.codePointAt(0);
         if (!Character.isTitleCase(var2)) {
            var1 = new String(new int[]{Character.toTitleCase(var2)}, 0, 1) + var1.substring(Character.offsetByCodePoints(var1, 0, 1));
         }
      }

      return var1;
   }

   private int determineFirstOfMonthDayOfWeek() {
      int var1 = WeekFields.of(this.getLocale()).getFirstDayOfWeek().getValue();
      int var2 = ((YearMonth)this.displayedYearMonth.get()).atDay(1).getDayOfWeek().getValue() - var1;
      if (var2 < 0) {
         var2 += this.daysPerWeek;
      }

      return var2;
   }

   private boolean isToday(LocalDate var1) {
      return var1.equals(LocalDate.now());
   }

   protected LocalDate dayCellDate(DateCell var1) {
      assert this.dayCellDates != null;

      return this.dayCellDates[this.dayCells.indexOf(var1)];
   }

   public void goToDayCell(DateCell var1, int var2, ChronoUnit var3, boolean var4) {
      this.goToDate(this.dayCellDate(var1).plus((long)var2, var3), var4);
   }

   protected void forward(int var1, ChronoUnit var2, boolean var3) {
      YearMonth var4 = (YearMonth)this.displayedYearMonth.get();
      DateCell var5 = this.lastFocusedDayCell;
      if (var5 == null || !this.dayCellDate(var5).getMonth().equals(var4.getMonth())) {
         var5 = this.findDayCellForDate(var4.atDay(1));
      }

      this.goToDayCell(var5, var1, var2, var3);
   }

   public void goToDate(LocalDate var1, boolean var2) {
      if (this.isValidDate(this.datePicker.getChronology(), var1)) {
         this.displayedYearMonth.set(YearMonth.from(var1));
         if (var2) {
            this.findDayCellForDate(var1).requestFocus();
         }
      }

   }

   public void selectDayCell(DateCell var1) {
      this.datePicker.setValue(this.dayCellDate(var1));
      this.datePicker.hide();
   }

   private DateCell findDayCellForDate(LocalDate var1) {
      for(int var2 = 0; var2 < this.dayCellDates.length; ++var2) {
         if (var1.equals(this.dayCellDates[var2])) {
            return (DateCell)this.dayCells.get(var2);
         }
      }

      return (DateCell)this.dayCells.get(this.dayCells.size() / 2 + 1);
   }

   void clearFocus() {
      LocalDate var1 = (LocalDate)this.datePicker.getValue();
      if (var1 == null) {
         var1 = LocalDate.now();
      }

      if (YearMonth.from(var1).equals(this.displayedYearMonth.get())) {
         this.goToDate(var1, true);
      } else {
         this.backMonthButton.requestFocus();
      }

      if (this.backMonthButton.getWidth() == 0.0) {
         this.backMonthButton.requestLayout();
         this.forwardMonthButton.requestLayout();
         this.backYearButton.requestLayout();
         this.forwardYearButton.requestLayout();
      }

   }

   protected void createDayCells() {
      EventHandler var1 = (var1x) -> {
         if (var1x.getButton() == MouseButton.PRIMARY) {
            DateCell var2 = (DateCell)var1x.getSource();
            this.selectDayCell(var2);
            this.lastFocusedDayCell = var2;
         }
      };

      for(int var2 = 0; var2 < 6; ++var2) {
         for(int var3 = 0; var3 < this.daysPerWeek; ++var3) {
            DateCell var4 = this.createDayCell();
            var4.addEventHandler(MouseEvent.MOUSE_CLICKED, var1);
            this.dayCells.add(var4);
         }
      }

      this.dayCellDates = new LocalDate[6 * this.daysPerWeek];
   }

   private DateCell createDayCell() {
      DateCell var1 = null;
      if (this.datePicker.getDayCellFactory() != null) {
         var1 = (DateCell)this.datePicker.getDayCellFactory().call(this.datePicker);
      }

      if (var1 == null) {
         var1 = new DateCell();
      }

      return var1;
   }

   protected Locale getLocale() {
      return Locale.getDefault(Category.FORMAT);
   }

   protected Chronology getPrimaryChronology() {
      return this.datePicker.getChronology();
   }

   protected boolean isValidDate(Chronology var1, LocalDate var2, int var3, ChronoUnit var4) {
      if (var2 != null) {
         try {
            return this.isValidDate(var1, var2.plus((long)var3, var4));
         } catch (DateTimeException var6) {
         }
      }

      return false;
   }

   protected boolean isValidDate(Chronology var1, LocalDate var2) {
      try {
         if (var2 != null) {
            var1.date(var2);
         }

         return true;
      } catch (DateTimeException var4) {
         return false;
      }
   }
}
