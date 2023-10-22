package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.tk.FontMetrics;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TextFieldSkin extends TextInputControlSkin {
   private Pane textGroup;
   private Group handleGroup;
   private Rectangle clip;
   private Text textNode;
   private Text promptNode;
   private Path selectionHighlightPath;
   private Path characterBoundingPath;
   private ObservableBooleanValue usePromptText;
   private DoubleProperty textTranslateX;
   private double caretWidth;
   protected ObservableDoubleValue textRight;
   private double pressX;
   private double pressY;
   public static final char BULLET = '●';

   protected int translateCaretPosition(int var1) {
      return var1;
   }

   protected Point2D translateCaretPosition(Point2D var1) {
      return var1;
   }

   public TextFieldSkin(TextField var1) {
      this(var1, (TextFieldBehavior)(var1 instanceof PasswordField ? new PasswordFieldBehavior((PasswordField)var1) : new TextFieldBehavior(var1)));
   }

   public TextFieldSkin(final TextField var1, TextFieldBehavior var2) {
      super(var1, var2);
      this.textGroup = new Pane();
      this.clip = new Rectangle();
      this.textNode = new Text();
      this.selectionHighlightPath = new Path();
      this.characterBoundingPath = new Path();
      this.textTranslateX = new SimpleDoubleProperty(this, "textTranslateX");
      var2.setTextFieldSkin(this);
      var1.caretPositionProperty().addListener((var2x, var3x, var4) -> {
         if (var1.getWidth() > 0.0) {
            this.updateTextNodeCaretPos(var1.getCaretPosition());
            if (!this.isForwardBias()) {
               this.setForwardBias(true);
            }

            this.updateCaretOff();
         }

      });
      this.forwardBiasProperty().addListener((var2x) -> {
         if (var1.getWidth() > 0.0) {
            this.updateTextNodeCaretPos(var1.getCaretPosition());
            this.updateCaretOff();
         }

      });
      this.textRight = new DoubleBinding() {
         {
            this.bind(new Observable[]{TextFieldSkin.this.textGroup.widthProperty()});
         }

         protected double computeValue() {
            return TextFieldSkin.this.textGroup.getWidth();
         }
      };
      this.clip.setSmooth(false);
      this.clip.setX(0.0);
      this.clip.widthProperty().bind(this.textGroup.widthProperty());
      this.clip.heightProperty().bind(this.textGroup.heightProperty());
      this.textGroup.setClip(this.clip);
      this.textGroup.getChildren().addAll(this.selectionHighlightPath, this.textNode, new Group(new Node[]{this.caretPath}));
      this.getChildren().add(this.textGroup);
      if (SHOW_HANDLES) {
         this.handleGroup = new Group();
         this.handleGroup.setManaged(false);
         this.handleGroup.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
         this.getChildren().add(this.handleGroup);
      }

      this.textNode.setManaged(false);
      this.textNode.getStyleClass().add("text");
      this.textNode.fontProperty().bind(var1.fontProperty());
      this.textNode.layoutXProperty().bind(this.textTranslateX);
      this.textNode.textProperty().bind(new StringBinding() {
         {
            this.bind(new Observable[]{var1.textProperty()});
         }

         protected String computeValue() {
            return TextFieldSkin.this.maskText(var1.textProperty().getValueSafe());
         }
      });
      this.textNode.fillProperty().bind(this.textFill);
      this.textNode.impl_selectionFillProperty().bind(new ObjectBinding() {
         {
            this.bind(new Observable[]{TextFieldSkin.this.highlightTextFill, TextFieldSkin.this.textFill, var1.focusedProperty()});
         }

         protected Paint computeValue() {
            return var1.isFocused() ? (Paint)TextFieldSkin.this.highlightTextFill.get() : (Paint)TextFieldSkin.this.textFill.get();
         }
      });
      this.updateTextNodeCaretPos(var1.getCaretPosition());
      var1.selectionProperty().addListener((var1x) -> {
         this.updateSelection();
      });
      this.selectionHighlightPath.setManaged(false);
      this.selectionHighlightPath.setStroke((Paint)null);
      this.selectionHighlightPath.layoutXProperty().bind(this.textTranslateX);
      this.selectionHighlightPath.visibleProperty().bind(var1.anchorProperty().isNotEqualTo(var1.caretPositionProperty()).and(var1.focusedProperty()));
      this.selectionHighlightPath.fillProperty().bind(this.highlightFill);
      this.textNode.impl_selectionShapeProperty().addListener((var1x) -> {
         this.updateSelection();
      });
      this.caretPath.setManaged(false);
      this.caretPath.setStrokeWidth(1.0);
      this.caretPath.fillProperty().bind(this.textFill);
      this.caretPath.strokeProperty().bind(this.textFill);
      this.caretPath.opacityProperty().bind(new DoubleBinding() {
         {
            this.bind(new Observable[]{TextFieldSkin.this.caretVisible});
         }

         protected double computeValue() {
            return TextFieldSkin.this.caretVisible.get() ? 1.0 : 0.0;
         }
      });
      this.caretPath.layoutXProperty().bind(this.textTranslateX);
      this.textNode.impl_caretShapeProperty().addListener((var2x) -> {
         this.caretPath.getElements().setAll((Object[])this.textNode.impl_caretShapeProperty().get());
         if (this.caretPath.getElements().size() == 0) {
            this.updateTextNodeCaretPos(var1.getCaretPosition());
         } else if (this.caretPath.getElements().size() != 4) {
            this.caretWidth = (double)Math.round(this.caretPath.getLayoutBounds().getWidth());
         }

      });
      var1.fontProperty().addListener((var2x) -> {
         var1.requestLayout();
         ((TextField)this.getSkinnable()).requestLayout();
      });
      this.registerChangeListener(var1.prefColumnCountProperty(), "prefColumnCount");
      if (var1.isFocused()) {
         this.setCaretAnimating(true);
      }

      var1.alignmentProperty().addListener((var2x) -> {
         if (var1.getWidth() > 0.0) {
            this.updateTextPos();
            this.updateCaretOff();
            var1.requestLayout();
         }

      });
      this.usePromptText = new BooleanBinding() {
         {
            this.bind(new Observable[]{var1.textProperty(), var1.promptTextProperty(), TextFieldSkin.this.promptTextFill});
         }

         protected boolean computeValue() {
            String var1x = var1.getText();
            String var2 = var1.getPromptText();
            return (var1x == null || var1x.isEmpty()) && var2 != null && !var2.isEmpty() && !((Paint)TextFieldSkin.this.promptTextFill.get()).equals(Color.TRANSPARENT);
         }
      };
      this.promptTextFill.addListener((var1x) -> {
         this.updateTextPos();
      });
      var1.textProperty().addListener((var1x) -> {
         if (!((TextFieldBehavior)this.getBehavior()).isEditing()) {
            this.updateTextPos();
         }

      });
      if (this.usePromptText.get()) {
         this.createPromptNode();
      }

      this.usePromptText.addListener((var2x) -> {
         this.createPromptNode();
         var1.requestLayout();
      });
      if (SHOW_HANDLES) {
         this.selectionHandle1.setRotate(180.0);
         EventHandler var3 = (var1x) -> {
            this.pressX = var1x.getX();
            this.pressY = var1x.getY();
            var1x.consume();
         };
         this.caretHandle.setOnMousePressed(var3);
         this.selectionHandle1.setOnMousePressed(var3);
         this.selectionHandle2.setOnMousePressed(var3);
         this.caretHandle.setOnMouseDragged((var1x) -> {
            Point2D var2 = new Point2D(this.caretHandle.getLayoutX() + var1x.getX() + this.pressX - this.textNode.getLayoutX(), this.caretHandle.getLayoutY() + var1x.getY() - this.pressY - 6.0);
            HitInfo var3 = this.textNode.impl_hitTestChar(this.translateCaretPosition(var2));
            this.positionCaret(var3, false);
            var1x.consume();
         });
         this.selectionHandle1.setOnMouseDragged(new EventHandler() {
            public void handle(MouseEvent var1) {
               TextField var2 = (TextField)TextFieldSkin.this.getSkinnable();
               Point2D var3 = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
               Point2D var4 = new Point2D(var1.getSceneX() - var3.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle1.getWidth() / 2.0, var1.getSceneY() - var3.getY() - TextFieldSkin.this.pressY - 6.0);
               HitInfo var5 = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(var4));
               int var6 = var5.getCharIndex();
               if (var2.getAnchor() < var2.getCaretPosition()) {
                  var2.selectRange(var2.getCaretPosition(), var2.getAnchor());
               }

               if (var6 >= 0) {
                  if (var6 >= var2.getAnchor() - 1) {
                     var5.setCharIndex(Math.max(0, var2.getAnchor() - 1));
                  }

                  TextFieldSkin.this.positionCaret(var5, true);
               }

               var1.consume();
            }
         });
         this.selectionHandle2.setOnMouseDragged(new EventHandler() {
            public void handle(MouseEvent var1) {
               TextField var2 = (TextField)TextFieldSkin.this.getSkinnable();
               Point2D var3 = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
               Point2D var4 = new Point2D(var1.getSceneX() - var3.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle2.getWidth() / 2.0, var1.getSceneY() - var3.getY() - TextFieldSkin.this.pressY - 6.0);
               HitInfo var5 = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(var4));
               int var6 = var5.getCharIndex();
               if (var2.getAnchor() > var2.getCaretPosition()) {
                  var2.selectRange(var2.getCaretPosition(), var2.getAnchor());
               }

               if (var6 > 0) {
                  if (var6 <= var2.getAnchor()) {
                     var5.setCharIndex(Math.min(var2.getAnchor() + 1, var2.getLength()));
                  }

                  TextFieldSkin.this.positionCaret(var5, true);
               }

               var1.consume();
            }
         });
      }

   }

   private void updateTextNodeCaretPos(int var1) {
      if (var1 != 0 && !this.isForwardBias()) {
         this.textNode.setImpl_caretPosition(var1 - 1);
      } else {
         this.textNode.setImpl_caretPosition(var1);
      }

      this.textNode.impl_caretBiasProperty().set(this.isForwardBias());
   }

   private void createPromptNode() {
      if (this.promptNode == null && this.usePromptText.get()) {
         this.promptNode = new Text();
         this.textGroup.getChildren().add(0, this.promptNode);
         this.promptNode.setManaged(false);
         this.promptNode.getStyleClass().add("text");
         this.promptNode.visibleProperty().bind(this.usePromptText);
         this.promptNode.fontProperty().bind(((TextField)this.getSkinnable()).fontProperty());
         this.promptNode.textProperty().bind(((TextField)this.getSkinnable()).promptTextProperty());
         this.promptNode.fillProperty().bind(this.promptTextFill);
         this.updateSelection();
      }
   }

   private void updateSelection() {
      TextField var1 = (TextField)this.getSkinnable();
      IndexRange var2 = var1.getSelection();
      if (var2 != null && var2.getLength() != 0) {
         this.textNode.impl_selectionStartProperty().set(var2.getStart());
         this.textNode.impl_selectionEndProperty().set(var2.getStart());
         this.textNode.impl_selectionEndProperty().set(var2.getEnd());
      } else {
         this.textNode.impl_selectionStartProperty().set(-1);
         this.textNode.impl_selectionEndProperty().set(-1);
      }

      PathElement[] var3 = (PathElement[])this.textNode.impl_selectionShapeProperty().get();
      if (var3 == null) {
         this.selectionHighlightPath.getElements().clear();
      } else {
         this.selectionHighlightPath.getElements().setAll((Object[])var3);
      }

      if (SHOW_HANDLES && var2 != null && var2.getLength() > 0) {
         int var4 = var1.getCaretPosition();
         int var5 = var1.getAnchor();
         this.updateTextNodeCaretPos(var5);
         Bounds var6 = this.caretPath.getBoundsInParent();
         if (var4 < var5) {
            this.selectionHandle2.setLayoutX(var6.getMinX() - this.selectionHandle2.getWidth() / 2.0);
         } else {
            this.selectionHandle1.setLayoutX(var6.getMinX() - this.selectionHandle1.getWidth() / 2.0);
         }

         this.updateTextNodeCaretPos(var4);
         var6 = this.caretPath.getBoundsInParent();
         if (var4 < var5) {
            this.selectionHandle1.setLayoutX(var6.getMinX() - this.selectionHandle1.getWidth() / 2.0);
         } else {
            this.selectionHandle2.setLayoutX(var6.getMinX() - this.selectionHandle2.getWidth() / 2.0);
         }
      }

   }

   protected void handleControlPropertyChanged(String var1) {
      if ("prefColumnCount".equals(var1)) {
         ((TextField)this.getSkinnable()).requestLayout();
      } else {
         super.handleControlPropertyChanged(var1);
      }

   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      TextField var11 = (TextField)this.getSkinnable();
      double var12 = (double)((FontMetrics)this.fontMetrics.get()).computeStringWidth("W");
      int var14 = var11.getPrefColumnCount();
      return (double)var14 * var12 + var9 + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.computePrefHeight(var1, var3, var5, var7, var9);
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return var3 + this.textNode.getLayoutBounds().getHeight() + var7;
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((TextField)this.getSkinnable()).prefHeight(var1);
   }

   public double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      return var1 + this.textNode.getBaselineOffset();
   }

   private void updateTextPos() {
      double var1 = this.textTranslateX.get();
      double var5 = this.textNode.getLayoutBounds().getWidth();
      double var3;
      switch (this.getHAlignment()) {
         case CENTER:
            double var7 = this.textRight.get() / 2.0;
            if (this.usePromptText.get()) {
               var3 = var7 - this.promptNode.getLayoutBounds().getWidth() / 2.0;
               this.promptNode.setLayoutX(var3);
            } else {
               var3 = var7 - var5 / 2.0;
            }

            if (var3 + var5 <= this.textRight.get()) {
               this.textTranslateX.set(var3);
            }
            break;
         case RIGHT:
            var3 = this.textRight.get() - var5 - this.caretWidth / 2.0;
            if (var3 > var1 || var3 > 0.0) {
               this.textTranslateX.set(var3);
            }

            if (this.usePromptText.get()) {
               this.promptNode.setLayoutX(this.textRight.get() - this.promptNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0);
            }
            break;
         case LEFT:
         default:
            var3 = this.caretWidth / 2.0;
            if (var3 < var1 || var3 + var5 <= this.textRight.get()) {
               this.textTranslateX.set(var3);
            }

            if (this.usePromptText.get()) {
               this.promptNode.layoutXProperty().set(var3);
            }
      }

   }

   protected void updateCaretOff() {
      double var1 = 0.0;
      double var3 = this.caretPath.getLayoutBounds().getMinX() + this.textTranslateX.get();
      if (var3 < 0.0) {
         var1 = var3;
      } else if (var3 > this.textRight.get() - this.caretWidth) {
         var1 = var3 - (this.textRight.get() - this.caretWidth);
      }

      switch (this.getHAlignment()) {
         case CENTER:
            this.textTranslateX.set(this.textTranslateX.get() - var1);
            break;
         case RIGHT:
            this.textTranslateX.set(Math.max(this.textTranslateX.get() - var1, this.textRight.get() - this.textNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0));
            break;
         case LEFT:
         default:
            this.textTranslateX.set(Math.min(this.textTranslateX.get() - var1, this.caretWidth / 2.0));
      }

      if (SHOW_HANDLES) {
         this.caretHandle.setLayoutX(var3 - this.caretHandle.getWidth() / 2.0 + 1.0);
      }

   }

   public void replaceText(int var1, int var2, String var3) {
      double var4 = this.textNode.getBoundsInParent().getMaxX();
      double var6 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
      ((TextField)this.getSkinnable()).replaceText(var1, var2, var3);
      this.scrollAfterDelete(var4, var6);
   }

   public void deleteChar(boolean var1) {
      double var2 = this.textNode.getBoundsInParent().getMaxX();
      double var4 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
      boolean var6 = var1 ? !((TextField)this.getSkinnable()).deletePreviousChar() : !((TextField)this.getSkinnable()).deleteNextChar();
      if (!var6) {
         this.scrollAfterDelete(var2, var4);
      }

   }

   public void scrollAfterDelete(double var1, double var3) {
      Bounds var5 = this.textNode.getLayoutBounds();
      Bounds var6 = this.textNode.localToParent(var5);
      Bounds var7 = this.clip.getBoundsInParent();
      Bounds var8 = this.caretPath.getLayoutBounds();
      double var9;
      switch (this.getHAlignment()) {
         case CENTER:
         case LEFT:
         default:
            if (var6.getMinX() < var7.getMinX() + this.caretWidth / 2.0 && var6.getMaxX() <= var7.getMaxX()) {
               var9 = var3 - var8.getMaxX() - this.textTranslateX.get();
               if (var6.getMaxX() + var9 < var7.getMaxX()) {
                  if (var1 <= var7.getMaxX()) {
                     var9 = var1 - var6.getMaxX();
                  } else {
                     var9 = var7.getMaxX() - var6.getMaxX();
                  }
               }

               this.textTranslateX.set(this.textTranslateX.get() + var9);
            }
            break;
         case RIGHT:
            if (var6.getMaxX() > var7.getMaxX()) {
               var9 = var3 - var8.getMaxX() - this.textTranslateX.get();
               if (var6.getMaxX() + var9 < var7.getMaxX()) {
                  if (var1 <= var7.getMaxX()) {
                     var9 = var1 - var6.getMaxX();
                  } else {
                     var9 = var7.getMaxX() - var6.getMaxX();
                  }
               }

               this.textTranslateX.set(this.textTranslateX.get() + var9);
            } else {
               this.updateTextPos();
            }
      }

      this.updateCaretOff();
   }

   public HitInfo getIndex(double var1, double var3) {
      Point2D var5 = new Point2D(var1 - this.textTranslateX.get() - this.snappedLeftInset(), var3 - this.snappedTopInset());
      return this.textNode.impl_hitTestChar(this.translateCaretPosition(var5));
   }

   public void positionCaret(HitInfo var1, boolean var2) {
      TextField var3 = (TextField)this.getSkinnable();
      int var4 = Utils.getHitInsertionIndex(var1, var3.textProperty().getValueSafe());
      if (var2) {
         var3.selectPositionCaret(var4);
      } else {
         var3.positionCaret(var4);
      }

      this.setForwardBias(var1.isLeading());
   }

   public Rectangle2D getCharacterBounds(int var1) {
      double var2;
      double var4;
      double var6;
      double var8;
      Bounds var10;
      if (var1 == this.textNode.getText().length()) {
         var10 = this.textNode.getBoundsInLocal();
         var2 = var10.getMaxX();
         var4 = 0.0;
         var6 = 0.0;
         var8 = var10.getMaxY();
      } else {
         this.characterBoundingPath.getElements().clear();
         this.characterBoundingPath.getElements().addAll(this.textNode.impl_getRangeShape(var1, var1 + 1));
         this.characterBoundingPath.setLayoutX(this.textNode.getLayoutX());
         this.characterBoundingPath.setLayoutY(this.textNode.getLayoutY());
         var10 = this.characterBoundingPath.getBoundsInLocal();
         var2 = var10.getMinX();
         var4 = var10.getMinY();
         var6 = var10.isEmpty() ? 0.0 : var10.getWidth();
         var8 = var10.isEmpty() ? 0.0 : var10.getHeight();
      }

      var10 = this.textGroup.getBoundsInParent();
      return new Rectangle2D(var2 + var10.getMinX() + this.textTranslateX.get(), var4 + var10.getMinY(), var6, var8);
   }

   protected PathElement[] getUnderlineShape(int var1, int var2) {
      return this.textNode.impl_getUnderlineShape(var1, var2);
   }

   protected PathElement[] getRangeShape(int var1, int var2) {
      return this.textNode.impl_getRangeShape(var1, var2);
   }

   protected void addHighlight(List var1, int var2) {
      this.textGroup.getChildren().addAll(var1);
   }

   protected void removeHighlight(List var1) {
      this.textGroup.getChildren().removeAll(var1);
   }

   public void nextCharacterVisually(boolean var1) {
      if (this.isRTL()) {
         var1 = !var1;
      }

      Bounds var2 = this.caretPath.getLayoutBounds();
      if (this.caretPath.getElements().size() == 4) {
         var2 = (new Path(new PathElement[]{(PathElement)this.caretPath.getElements().get(0), (PathElement)this.caretPath.getElements().get(1)})).getLayoutBounds();
      }

      double var3 = var1 ? var2.getMaxX() : var2.getMinX();
      double var5 = (var2.getMinY() + var2.getMaxY()) / 2.0;
      HitInfo var7 = this.textNode.impl_hitTestChar(this.translateCaretPosition(new Point2D(var3, var5)));
      Path var8 = new Path(this.textNode.impl_getRangeShape(var7.getCharIndex(), var7.getCharIndex() + 1));
      if (var1 && var8.getLayoutBounds().getMaxX() > var2.getMaxX() || !var1 && var8.getLayoutBounds().getMinX() < var2.getMinX()) {
         var7.setLeading(!var7.isLeading());
      }

      this.positionCaret(var7, false);
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      super.layoutChildren(var1, var3, var5, var7);
      if (this.textNode != null) {
         Bounds var11 = this.textNode.getLayoutBounds();
         double var12 = this.textNode.getBaselineOffset();
         double var14 = var11.getHeight() - var12;
         double var9;
         switch (((TextField)this.getSkinnable()).getAlignment().getVpos()) {
            case TOP:
               var9 = var12;
               break;
            case CENTER:
               var9 = (var12 + this.textGroup.getHeight() - var14) / 2.0;
               break;
            case BOTTOM:
            default:
               var9 = this.textGroup.getHeight() - var14;
         }

         this.textNode.setY(var9);
         if (this.promptNode != null) {
            this.promptNode.setY(var9);
         }

         if (((TextField)this.getSkinnable()).getWidth() > 0.0) {
            this.updateTextPos();
            this.updateCaretOff();
         }
      }

      if (SHOW_HANDLES) {
         this.handleGroup.setLayoutX(var1 + this.textTranslateX.get());
         this.handleGroup.setLayoutY(var3);
         this.selectionHandle1.resize(this.selectionHandle1.prefWidth(-1.0), this.selectionHandle1.prefHeight(-1.0));
         this.selectionHandle2.resize(this.selectionHandle2.prefWidth(-1.0), this.selectionHandle2.prefHeight(-1.0));
         this.caretHandle.resize(this.caretHandle.prefWidth(-1.0), this.caretHandle.prefHeight(-1.0));
         Bounds var16 = this.caretPath.getBoundsInParent();
         this.caretHandle.setLayoutY(var16.getMaxY() - 1.0);
         this.selectionHandle1.setLayoutY(var16.getMinY() - this.selectionHandle1.getHeight() + 1.0);
         this.selectionHandle2.setLayoutY(var16.getMaxY() - 1.0);
      }

   }

   protected HPos getHAlignment() {
      HPos var1 = ((TextField)this.getSkinnable()).getAlignment().getHpos();
      return var1;
   }

   public Point2D getMenuPosition() {
      Point2D var1 = super.getMenuPosition();
      if (var1 != null) {
         var1 = new Point2D(Math.max(0.0, var1.getX() - this.textNode.getLayoutX() - this.snappedLeftInset() + this.textTranslateX.get()), Math.max(0.0, var1.getY() - this.textNode.getLayoutY() - this.snappedTopInset()));
      }

      return var1;
   }

   protected String maskText(String var1) {
      if (!(this.getSkinnable() instanceof PasswordField)) {
         return var1;
      } else {
         int var2 = var1.length();
         StringBuilder var3 = new StringBuilder(var2);

         for(int var4 = 0; var4 < var2; ++var4) {
            var3.append('●');
         }

         return var3.toString();
      }
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case BOUNDS_FOR_RANGE:
         case OFFSET_AT_POINT:
            return this.textNode.queryAccessibleAttribute(var1, var2);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
