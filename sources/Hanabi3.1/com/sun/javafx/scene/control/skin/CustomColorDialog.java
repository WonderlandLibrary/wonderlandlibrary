package com.sun.javafx.scene.control.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class CustomColorDialog extends HBox {
   private final Stage dialog = new Stage();
   private ColorRectPane colorRectPane;
   private ControlsPane controlsPane;
   private ObjectProperty currentColorProperty;
   private ObjectProperty customColorProperty;
   private Runnable onSave;
   private Runnable onUse;
   private Runnable onCancel;
   private WebColorField webField;
   private Scene customScene;
   private String saveBtnText;
   private boolean showUseBtn;
   private boolean showOpacitySlider;
   private final EventHandler keyEventListener;
   private InvalidationListener positionAdjuster;

   public CustomColorDialog(Window var1) {
      this.currentColorProperty = new SimpleObjectProperty(Color.WHITE);
      this.customColorProperty = new SimpleObjectProperty(Color.TRANSPARENT);
      this.webField = null;
      this.showUseBtn = true;
      this.showOpacitySlider = true;
      this.keyEventListener = (var1x) -> {
         switch (var1x.getCode()) {
            case ESCAPE:
               this.dialog.setScene((Scene)null);
               this.dialog.close();
            default:
         }
      };
      this.positionAdjuster = new InvalidationListener() {
         public void invalidated(Observable var1) {
            if (!Double.isNaN(CustomColorDialog.this.dialog.getWidth()) && !Double.isNaN(CustomColorDialog.this.dialog.getHeight())) {
               CustomColorDialog.this.dialog.widthProperty().removeListener(CustomColorDialog.this.positionAdjuster);
               CustomColorDialog.this.dialog.heightProperty().removeListener(CustomColorDialog.this.positionAdjuster);
               CustomColorDialog.this.fixPosition();
            }
         }
      };
      this.getStyleClass().add("custom-color-dialog");
      if (var1 != null) {
         this.dialog.initOwner(var1);
      }

      this.dialog.setTitle(ColorPickerSkin.getString("customColorDialogTitle"));
      this.dialog.initModality(Modality.APPLICATION_MODAL);
      this.dialog.initStyle(StageStyle.UTILITY);
      this.dialog.setResizable(false);
      this.dialog.addEventHandler(KeyEvent.ANY, this.keyEventListener);
      this.customScene = new Scene(this);
      Scene var2 = var1.getScene();
      if (var2 != null) {
         if (var2.getUserAgentStylesheet() != null) {
            this.customScene.setUserAgentStylesheet(var2.getUserAgentStylesheet());
         }

         this.customScene.getStylesheets().addAll(var2.getStylesheets());
      }

      this.buildUI();
      this.dialog.setScene(this.customScene);
   }

   private void buildUI() {
      this.colorRectPane = new ColorRectPane();
      this.controlsPane = new ControlsPane();
      setHgrow(this.controlsPane, Priority.ALWAYS);
      this.getChildren().setAll((Object[])(this.colorRectPane, this.controlsPane));
   }

   public void setCurrentColor(Color var1) {
      this.currentColorProperty.set(var1);
   }

   Color getCurrentColor() {
      return (Color)this.currentColorProperty.get();
   }

   ObjectProperty customColorProperty() {
      return this.customColorProperty;
   }

   void setCustomColor(Color var1) {
      this.customColorProperty.set(var1);
   }

   public Color getCustomColor() {
      return (Color)this.customColorProperty.get();
   }

   public Runnable getOnSave() {
      return this.onSave;
   }

   public void setSaveBtnToOk() {
      this.saveBtnText = ColorPickerSkin.getString("OK");
      this.buildUI();
   }

   public void setOnSave(Runnable var1) {
      this.onSave = var1;
   }

   public Runnable getOnUse() {
      return this.onUse;
   }

   public void setOnUse(Runnable var1) {
      this.onUse = var1;
   }

   public void setShowUseBtn(boolean var1) {
      this.showUseBtn = var1;
      this.buildUI();
   }

   public void setShowOpacitySlider(boolean var1) {
      this.showOpacitySlider = var1;
      this.buildUI();
   }

   public Runnable getOnCancel() {
      return this.onCancel;
   }

   public void setOnCancel(Runnable var1) {
      this.onCancel = var1;
   }

   public void setOnHidden(EventHandler var1) {
      this.dialog.setOnHidden(var1);
   }

   public Stage getDialog() {
      return this.dialog;
   }

   public void show() {
      if (this.dialog.getOwner() != null) {
         this.dialog.widthProperty().addListener(this.positionAdjuster);
         this.dialog.heightProperty().addListener(this.positionAdjuster);
         this.positionAdjuster.invalidated((Observable)null);
      }

      if (this.dialog.getScene() == null) {
         this.dialog.setScene(this.customScene);
      }

      this.colorRectPane.updateValues();
      this.dialog.show();
   }

   public void hide() {
      if (this.dialog.getOwner() != null) {
         this.dialog.hide();
      }

   }

   private void fixPosition() {
      Window var1 = this.dialog.getOwner();
      Screen var2 = com.sun.javafx.util.Utils.getScreen(var1);
      Rectangle2D var3 = var2.getBounds();
      double var4 = var1.getX() + var1.getWidth();
      double var6 = var1.getX() - this.dialog.getWidth();
      double var8;
      if (var3.getMaxX() >= var4 + this.dialog.getWidth()) {
         var8 = var4;
      } else if (var3.getMinX() <= var6) {
         var8 = var6;
      } else {
         var8 = Math.max(var3.getMinX(), var3.getMaxX() - this.dialog.getWidth());
      }

      double var10 = Math.max(var3.getMinY(), Math.min(var3.getMaxY() - this.dialog.getHeight(), var1.getY()));
      this.dialog.setX(var8);
      this.dialog.setY(var10);
   }

   public void layoutChildren() {
      super.layoutChildren();
      if (!(this.dialog.getMinWidth() > 0.0) || !(this.dialog.getMinHeight() > 0.0)) {
         double var1 = Math.max(0.0, this.computeMinWidth(this.getHeight()) + (this.dialog.getWidth() - this.customScene.getWidth()));
         double var3 = Math.max(0.0, this.computeMinHeight(this.getWidth()) + (this.dialog.getHeight() - this.customScene.getHeight()));
         this.dialog.setMinWidth(var1);
         this.dialog.setMinHeight(var3);
      }
   }

   static double clamp(double var0) {
      return var0 < 0.0 ? 0.0 : (var0 > 1.0 ? 1.0 : var0);
   }

   private static LinearGradient createHueGradient() {
      Stop[] var2 = new Stop[255];

      for(int var3 = 0; var3 < 255; ++var3) {
         double var0 = 1.0 - 0.00392156862745098 * (double)var3;
         int var4 = (int)((double)var3 / 255.0 * 360.0);
         var2[var3] = new Stop(var0, Color.hsb((double)var4, 1.0, 1.0));
      }

      return new LinearGradient(0.0, 1.0, 0.0, 0.0, true, CycleMethod.NO_CYCLE, var2);
   }

   private static int doubleToInt(double var0) {
      return (int)(var0 * 255.0 + 0.5);
   }

   private class ControlsPane extends VBox {
      private Label currentColorLabel;
      private Label newColorLabel;
      private Region currentColorRect;
      private Region newColorRect;
      private Region currentTransparent;
      private GridPane currentAndNewColor;
      private Region currentNewColorBorder;
      private ToggleButton hsbButton;
      private ToggleButton rgbButton;
      private ToggleButton webButton;
      private HBox hBox;
      private Label[] labels = new Label[4];
      private Slider[] sliders = new Slider[4];
      private IntegerField[] fields = new IntegerField[4];
      private Label[] units = new Label[4];
      private HBox buttonBox;
      private Region whiteBox;
      private GridPane settingsPane = new GridPane();
      private Property[] bindedProperties = new Property[4];

      public ControlsPane() {
         this.getStyleClass().add("controls-pane");
         this.currentNewColorBorder = new Region();
         this.currentNewColorBorder.setId("current-new-color-border");
         this.currentTransparent = new Region();
         this.currentTransparent.getStyleClass().addAll("transparent-pattern");
         this.currentColorRect = new Region();
         this.currentColorRect.getStyleClass().add("color-rect");
         this.currentColorRect.setId("current-color");
         this.currentColorRect.backgroundProperty().bind(new ObjectBinding() {
            {
               this.bind(new Observable[]{CustomColorDialog.this.currentColorProperty});
            }

            protected Background computeValue() {
               return new Background(new BackgroundFill[]{new BackgroundFill((Paint)CustomColorDialog.this.currentColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY)});
            }
         });
         this.newColorRect = new Region();
         this.newColorRect.getStyleClass().add("color-rect");
         this.newColorRect.setId("new-color");
         this.newColorRect.backgroundProperty().bind(new ObjectBinding() {
            {
               this.bind(new Observable[]{CustomColorDialog.this.customColorProperty});
            }

            protected Background computeValue() {
               return new Background(new BackgroundFill[]{new BackgroundFill((Paint)CustomColorDialog.this.customColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY)});
            }
         });
         this.currentColorLabel = new Label(ColorPickerSkin.getString("currentColor"));
         this.newColorLabel = new Label(ColorPickerSkin.getString("newColor"));
         this.whiteBox = new Region();
         this.whiteBox.getStyleClass().add("customcolor-controls-background");
         this.hsbButton = new ToggleButton(ColorPickerSkin.getString("colorType.hsb"));
         this.hsbButton.getStyleClass().add("left-pill");
         this.rgbButton = new ToggleButton(ColorPickerSkin.getString("colorType.rgb"));
         this.rgbButton.getStyleClass().add("center-pill");
         this.webButton = new ToggleButton(ColorPickerSkin.getString("colorType.web"));
         this.webButton.getStyleClass().add("right-pill");
         ToggleGroup var2 = new ToggleGroup();
         this.hBox = new HBox();
         this.hBox.setAlignment(Pos.CENTER);
         this.hBox.getChildren().addAll(this.hsbButton, this.rgbButton, this.webButton);
         Region var3 = new Region();
         var3.setId("spacer1");
         Region var4 = new Region();
         var4.setId("spacer2");
         Region var5 = new Region();
         var5.setId("spacer-side");
         Region var6 = new Region();
         var6.setId("spacer-side");
         Region var7 = new Region();
         var7.setId("spacer-bottom");
         this.currentAndNewColor = new GridPane();
         this.currentAndNewColor.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
         ((ColumnConstraints)this.currentAndNewColor.getColumnConstraints().get(0)).setHgrow(Priority.ALWAYS);
         ((ColumnConstraints)this.currentAndNewColor.getColumnConstraints().get(1)).setHgrow(Priority.ALWAYS);
         this.currentAndNewColor.getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), new RowConstraints());
         ((RowConstraints)this.currentAndNewColor.getRowConstraints().get(2)).setVgrow(Priority.ALWAYS);
         VBox.setVgrow(this.currentAndNewColor, Priority.ALWAYS);
         this.currentAndNewColor.getStyleClass().add("current-new-color-grid");
         this.currentAndNewColor.add(this.currentColorLabel, 0, 0);
         this.currentAndNewColor.add(this.newColorLabel, 1, 0);
         this.currentAndNewColor.add(var3, 0, 1, 2, 1);
         this.currentAndNewColor.add(this.currentTransparent, 0, 2, 2, 1);
         this.currentAndNewColor.add(this.currentColorRect, 0, 2);
         this.currentAndNewColor.add(this.newColorRect, 1, 2);
         this.currentAndNewColor.add(this.currentNewColorBorder, 0, 2, 2, 1);
         this.currentAndNewColor.add(var4, 0, 3, 2, 1);
         this.settingsPane = new GridPane();
         this.settingsPane.setId("settings-pane");
         this.settingsPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints());
         ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(0)).setHgrow(Priority.NEVER);
         ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(2)).setHgrow(Priority.ALWAYS);
         ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(3)).setHgrow(Priority.NEVER);
         ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(4)).setHgrow(Priority.NEVER);
         ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(5)).setHgrow(Priority.NEVER);
         this.settingsPane.add(this.whiteBox, 0, 0, 6, 5);
         this.settingsPane.add(this.hBox, 0, 0, 6, 1);
         this.settingsPane.add(var5, 0, 0);
         this.settingsPane.add(var6, 5, 0);
         this.settingsPane.add(var7, 0, 4);
         CustomColorDialog.this.webField = new WebColorField();
         CustomColorDialog.this.webField.getStyleClass().add("web-field");
         CustomColorDialog.this.webField.setSkin(new WebColorFieldSkin(CustomColorDialog.this.webField));
         CustomColorDialog.this.webField.valueProperty().bindBidirectional(CustomColorDialog.this.customColorProperty);
         CustomColorDialog.this.webField.visibleProperty().bind(var2.selectedToggleProperty().isEqualTo(this.webButton));
         this.settingsPane.add(CustomColorDialog.this.webField, 2, 1);

         for(int var8 = 0; var8 < 4; ++var8) {
            this.labels[var8] = new Label();
            this.labels[var8].getStyleClass().add("settings-label");
            this.sliders[var8] = new Slider();
            this.fields[var8] = new IntegerField();
            this.fields[var8].getStyleClass().add("color-input-field");
            this.fields[var8].setSkin(new IntegerFieldSkin(this.fields[var8]));
            this.units[var8] = new Label(var8 == 0 ? "°" : "%");
            this.units[var8].getStyleClass().add("settings-unit");
            if (var8 > 0 && var8 < 3) {
               this.labels[var8].visibleProperty().bind(var2.selectedToggleProperty().isNotEqualTo(this.webButton));
            }

            if (var8 < 3) {
               this.sliders[var8].visibleProperty().bind(var2.selectedToggleProperty().isNotEqualTo(this.webButton));
               this.fields[var8].visibleProperty().bind(var2.selectedToggleProperty().isNotEqualTo(this.webButton));
               this.units[var8].visibleProperty().bind(var2.selectedToggleProperty().isEqualTo(this.hsbButton));
            }

            int var9 = 1 + var8;
            if (var8 == 3) {
               ++var9;
            }

            if (var8 != 3 || CustomColorDialog.this.showOpacitySlider) {
               this.settingsPane.add(this.labels[var8], 1, var9);
               this.settingsPane.add(this.sliders[var8], 2, var9);
               this.settingsPane.add(this.fields[var8], 3, var9);
               this.settingsPane.add(this.units[var8], 4, var9);
            }
         }

         this.set(3, ColorPickerSkin.getString("opacity_colon"), 100, CustomColorDialog.this.colorRectPane.alpha);
         this.hsbButton.setToggleGroup(var2);
         this.rgbButton.setToggleGroup(var2);
         this.webButton.setToggleGroup(var2);
         var2.selectedToggleProperty().addListener((var2x, var3x, var4x) -> {
            if (var4x == null) {
               var2.selectToggle(var3x);
            } else if (var4x == this.hsbButton) {
               this.showHSBSettings();
            } else if (var4x == this.rgbButton) {
               this.showRGBSettings();
            } else {
               this.showWebSettings();
            }

         });
         var2.selectToggle(this.hsbButton);
         this.buttonBox = new HBox();
         this.buttonBox.setId("buttons-hbox");
         Button var11 = new Button(CustomColorDialog.this.saveBtnText != null && !CustomColorDialog.this.saveBtnText.isEmpty() ? CustomColorDialog.this.saveBtnText : ColorPickerSkin.getString("Save"));
         var11.setDefaultButton(true);
         var11.setOnAction((var1x) -> {
            if (CustomColorDialog.this.onSave != null) {
               CustomColorDialog.this.onSave.run();
            }

            CustomColorDialog.this.dialog.hide();
         });
         Button var12 = new Button(ColorPickerSkin.getString("Use"));
         var12.setOnAction((var1x) -> {
            if (CustomColorDialog.this.onUse != null) {
               CustomColorDialog.this.onUse.run();
            }

            CustomColorDialog.this.dialog.hide();
         });
         Button var10 = new Button(ColorPickerSkin.getString("Cancel"));
         var10.setCancelButton(true);
         var10.setOnAction((var1x) -> {
            CustomColorDialog.this.customColorProperty.set(CustomColorDialog.this.getCurrentColor());
            if (CustomColorDialog.this.onCancel != null) {
               CustomColorDialog.this.onCancel.run();
            }

            CustomColorDialog.this.dialog.hide();
         });
         if (CustomColorDialog.this.showUseBtn) {
            this.buttonBox.getChildren().addAll(var11, var12, var10);
         } else {
            this.buttonBox.getChildren().addAll(var11, var10);
         }

         this.getChildren().addAll(this.currentAndNewColor, this.settingsPane, this.buttonBox);
      }

      private void showHSBSettings() {
         this.set(0, ColorPickerSkin.getString("hue_colon"), 360, CustomColorDialog.this.colorRectPane.hue);
         this.set(1, ColorPickerSkin.getString("saturation_colon"), 100, CustomColorDialog.this.colorRectPane.sat);
         this.set(2, ColorPickerSkin.getString("brightness_colon"), 100, CustomColorDialog.this.colorRectPane.bright);
      }

      private void showRGBSettings() {
         this.set(0, ColorPickerSkin.getString("red_colon"), 255, CustomColorDialog.this.colorRectPane.red);
         this.set(1, ColorPickerSkin.getString("green_colon"), 255, CustomColorDialog.this.colorRectPane.green);
         this.set(2, ColorPickerSkin.getString("blue_colon"), 255, CustomColorDialog.this.colorRectPane.blue);
      }

      private void showWebSettings() {
         this.labels[0].setText(ColorPickerSkin.getString("web_colon"));
      }

      private void set(int var1, String var2, int var3, Property var4) {
         this.labels[var1].setText(var2);
         if (this.bindedProperties[var1] != null) {
            this.sliders[var1].valueProperty().unbindBidirectional(this.bindedProperties[var1]);
            this.fields[var1].valueProperty().unbindBidirectional(this.bindedProperties[var1]);
         }

         this.sliders[var1].setMax((double)var3);
         this.sliders[var1].valueProperty().bindBidirectional(var4);
         this.labels[var1].setLabelFor(this.sliders[var1]);
         this.fields[var1].setMaxValue(var3);
         this.fields[var1].valueProperty().bindBidirectional(var4);
         this.bindedProperties[var1] = var4;
      }
   }

   private class ColorRectPane extends HBox {
      private Pane colorRect;
      private Pane colorBar;
      private Pane colorRectOverlayOne;
      private Pane colorRectOverlayTwo;
      private Region colorRectIndicator;
      private Region colorBarIndicator;
      private boolean changeIsLocal = false;
      private DoubleProperty hue = new SimpleDoubleProperty(-1.0) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateHSBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private DoubleProperty sat = new SimpleDoubleProperty(-1.0) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateHSBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private DoubleProperty bright = new SimpleDoubleProperty(-1.0) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateHSBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private IntegerProperty red = new SimpleIntegerProperty(-1) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateRGBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private IntegerProperty green = new SimpleIntegerProperty(-1) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateRGBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private IntegerProperty blue = new SimpleIntegerProperty(-1) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               ColorRectPane.this.updateRGBColor();
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };
      private DoubleProperty alpha = new SimpleDoubleProperty(100.0) {
         protected void invalidated() {
            if (!ColorRectPane.this.changeIsLocal) {
               ColorRectPane.this.changeIsLocal = true;
               CustomColorDialog.this.setCustomColor(new Color(CustomColorDialog.this.getCustomColor().getRed(), CustomColorDialog.this.getCustomColor().getGreen(), CustomColorDialog.this.getCustomColor().getBlue(), CustomColorDialog.clamp(ColorRectPane.this.alpha.get() / 100.0)));
               ColorRectPane.this.changeIsLocal = false;
            }

         }
      };

      private void updateRGBColor() {
         Color var1 = Color.rgb(this.red.get(), this.green.get(), this.blue.get(), CustomColorDialog.clamp(this.alpha.get() / 100.0));
         this.hue.set(var1.getHue());
         this.sat.set(var1.getSaturation() * 100.0);
         this.bright.set(var1.getBrightness() * 100.0);
         CustomColorDialog.this.setCustomColor(var1);
      }

      private void updateHSBColor() {
         Color var1 = Color.hsb(this.hue.get(), CustomColorDialog.clamp(this.sat.get() / 100.0), CustomColorDialog.clamp(this.bright.get() / 100.0), CustomColorDialog.clamp(this.alpha.get() / 100.0));
         this.red.set(CustomColorDialog.doubleToInt(var1.getRed()));
         this.green.set(CustomColorDialog.doubleToInt(var1.getGreen()));
         this.blue.set(CustomColorDialog.doubleToInt(var1.getBlue()));
         CustomColorDialog.this.setCustomColor(var1);
      }

      private void colorChanged() {
         if (!this.changeIsLocal) {
            this.changeIsLocal = true;
            this.hue.set(CustomColorDialog.this.getCustomColor().getHue());
            this.sat.set(CustomColorDialog.this.getCustomColor().getSaturation() * 100.0);
            this.bright.set(CustomColorDialog.this.getCustomColor().getBrightness() * 100.0);
            this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
            this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
            this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
            this.changeIsLocal = false;
         }

      }

      public ColorRectPane() {
         this.getStyleClass().add("color-rect-pane");
         CustomColorDialog.this.customColorProperty().addListener((var1x, var2x, var3x) -> {
            this.colorChanged();
         });
         this.colorRectIndicator = new Region();
         this.colorRectIndicator.setId("color-rect-indicator");
         this.colorRectIndicator.setManaged(false);
         this.colorRectIndicator.setMouseTransparent(true);
         this.colorRectIndicator.setCache(true);
         StackPane var2 = new StackPane();
         this.colorRect = new StackPane() {
            public Orientation getContentBias() {
               return Orientation.VERTICAL;
            }

            protected double computePrefWidth(double var1) {
               return var1;
            }

            protected double computeMaxWidth(double var1) {
               return var1;
            }
         };
         this.colorRect.getStyleClass().addAll("color-rect", "transparent-pattern");
         Pane var3 = new Pane();
         var3.backgroundProperty().bind(new ObjectBinding() {
            {
               this.bind(new Observable[]{ColorRectPane.this.hue});
            }

            protected Background computeValue() {
               return new Background(new BackgroundFill[]{new BackgroundFill(Color.hsb(ColorRectPane.this.hue.getValue(), 1.0, 1.0), CornerRadii.EMPTY, Insets.EMPTY)});
            }
         });
         this.colorRectOverlayOne = new Pane();
         this.colorRectOverlayOne.getStyleClass().add("color-rect");
         this.colorRectOverlayOne.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(new LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb(255, 255, 255, 1.0)), new Stop(1.0, Color.rgb(255, 255, 255, 0.0))}), CornerRadii.EMPTY, Insets.EMPTY)}));
         EventHandler var4 = (var1x) -> {
            double var2 = var1x.getX();
            double var4 = var1x.getY();
            this.sat.set(CustomColorDialog.clamp(var2 / this.colorRect.getWidth()) * 100.0);
            this.bright.set(100.0 - CustomColorDialog.clamp(var4 / this.colorRect.getHeight()) * 100.0);
         };
         this.colorRectOverlayTwo = new Pane();
         this.colorRectOverlayTwo.getStyleClass().addAll("color-rect");
         this.colorRectOverlayTwo.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(new LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb(0, 0, 0, 0.0)), new Stop(1.0, Color.rgb(0, 0, 0, 1.0))}), CornerRadii.EMPTY, Insets.EMPTY)}));
         this.colorRectOverlayTwo.setOnMouseDragged(var4);
         this.colorRectOverlayTwo.setOnMousePressed(var4);
         Pane var5 = new Pane();
         var5.setMouseTransparent(true);
         var5.getStyleClass().addAll("color-rect", "color-rect-border");
         this.colorBar = new Pane();
         this.colorBar.getStyleClass().add("color-bar");
         this.colorBar.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(CustomColorDialog.createHueGradient(), CornerRadii.EMPTY, Insets.EMPTY)}));
         this.colorBarIndicator = new Region();
         this.colorBarIndicator.setId("color-bar-indicator");
         this.colorBarIndicator.setMouseTransparent(true);
         this.colorBarIndicator.setCache(true);
         this.colorRectIndicator.layoutXProperty().bind(this.sat.divide(100).multiply(this.colorRect.widthProperty()));
         this.colorRectIndicator.layoutYProperty().bind(Bindings.subtract(1, this.bright.divide(100)).multiply(this.colorRect.heightProperty()));
         this.colorBarIndicator.layoutYProperty().bind(this.hue.divide(360).multiply(this.colorBar.heightProperty()));
         var2.opacityProperty().bind(this.alpha.divide(100));
         EventHandler var6 = (var1x) -> {
            double var2 = var1x.getY();
            this.hue.set(CustomColorDialog.clamp(var2 / this.colorRect.getHeight()) * 360.0);
         };
         this.colorBar.setOnMouseDragged(var6);
         this.colorBar.setOnMousePressed(var6);
         this.colorBar.getChildren().setAll((Object[])(this.colorBarIndicator));
         var2.getChildren().setAll((Object[])(var3, this.colorRectOverlayOne, this.colorRectOverlayTwo));
         this.colorRect.getChildren().setAll((Object[])(var2, var5, this.colorRectIndicator));
         HBox.setHgrow(this.colorRect, Priority.SOMETIMES);
         this.getChildren().addAll(this.colorRect, this.colorBar);
      }

      private void updateValues() {
         if (CustomColorDialog.this.getCurrentColor() == null) {
            CustomColorDialog.this.setCurrentColor(Color.TRANSPARENT);
         }

         this.changeIsLocal = true;
         this.hue.set(CustomColorDialog.this.getCurrentColor().getHue());
         this.sat.set(CustomColorDialog.this.getCurrentColor().getSaturation() * 100.0);
         this.bright.set(CustomColorDialog.this.getCurrentColor().getBrightness() * 100.0);
         this.alpha.set(CustomColorDialog.this.getCurrentColor().getOpacity() * 100.0);
         CustomColorDialog.this.setCustomColor(Color.hsb(this.hue.get(), CustomColorDialog.clamp(this.sat.get() / 100.0), CustomColorDialog.clamp(this.bright.get() / 100.0), CustomColorDialog.clamp(this.alpha.get() / 100.0)));
         this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
         this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
         this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
         this.changeIsLocal = false;
      }

      protected void layoutChildren() {
         super.layoutChildren();
         this.colorRectIndicator.autosize();
         double var1 = Math.min(this.colorRect.getWidth(), this.colorRect.getHeight());
         this.colorRect.resize(var1, var1);
         this.colorBar.resize(this.colorBar.getWidth(), var1);
      }
   }
}
