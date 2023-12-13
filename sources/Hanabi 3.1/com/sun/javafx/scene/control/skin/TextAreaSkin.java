package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.tk.FontMetrics;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.geometry.VerticalDirection;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TextAreaSkin extends TextInputControlSkin {
   private final TextArea textArea;
   private final boolean USE_MULTIPLE_NODES = false;
   private double computedMinWidth = Double.NEGATIVE_INFINITY;
   private double computedMinHeight = Double.NEGATIVE_INFINITY;
   private double computedPrefWidth = Double.NEGATIVE_INFINITY;
   private double computedPrefHeight = Double.NEGATIVE_INFINITY;
   private double widthForComputedPrefHeight = Double.NEGATIVE_INFINITY;
   private double characterWidth;
   private double lineHeight;
   private ContentView contentView = new ContentView();
   private Group paragraphNodes = new Group();
   private Text promptNode;
   private ObservableBooleanValue usePromptText;
   private ObservableIntegerValue caretPosition;
   private Group selectionHighlightGroup = new Group();
   private ScrollPane scrollPane;
   private Bounds oldViewportBounds;
   private VerticalDirection scrollDirection = null;
   private Path characterBoundingPath = new Path();
   private Timeline scrollSelectionTimeline = new Timeline();
   private EventHandler scrollSelectionHandler = (var1x) -> {
      switch (this.scrollDirection) {
         case UP:
         case DOWN:
         default:
      }
   };
   public static final int SCROLL_RATE = 30;
   private double pressX;
   private double pressY;
   private boolean handlePressed;
   double targetCaretX = -1.0;
   private static final Path tmpCaretPath = new Path();

   protected void invalidateMetrics() {
      this.computedMinWidth = Double.NEGATIVE_INFINITY;
      this.computedMinHeight = Double.NEGATIVE_INFINITY;
      this.computedPrefWidth = Double.NEGATIVE_INFINITY;
      this.computedPrefHeight = Double.NEGATIVE_INFINITY;
   }

   public TextAreaSkin(final TextArea var1) {
      super(var1, new TextAreaBehavior(var1));
      ((TextAreaBehavior)this.getBehavior()).setTextAreaSkin(this);
      this.textArea = var1;
      this.caretPosition = new IntegerBinding() {
         {
            this.bind(new Observable[]{var1.caretPositionProperty()});
         }

         protected int computeValue() {
            return var1.getCaretPosition();
         }
      };
      this.caretPosition.addListener((var1x, var2x, var3x) -> {
         this.targetCaretX = -1.0;
         if (var3x.intValue() > var2x.intValue()) {
            this.setForwardBias(true);
         }

      });
      this.forwardBiasProperty().addListener((var2x) -> {
         if (var1.getWidth() > 0.0) {
            this.updateTextNodeCaretPos(var1.getCaretPosition());
         }

      });
      this.scrollPane = new ScrollPane();
      this.scrollPane.setFitToWidth(var1.isWrapText());
      this.scrollPane.setContent(this.contentView);
      this.getChildren().add(this.scrollPane);
      ((TextArea)this.getSkinnable()).addEventFilter(ScrollEvent.ANY, (var1x) -> {
         if (var1x.isDirect() && this.handlePressed) {
            var1x.consume();
         }

      });
      this.selectionHighlightGroup.setManaged(false);
      this.selectionHighlightGroup.setVisible(false);
      this.contentView.getChildren().add(this.selectionHighlightGroup);
      this.paragraphNodes.setManaged(false);
      this.contentView.getChildren().add(this.paragraphNodes);
      this.caretPath.setManaged(false);
      this.caretPath.setStrokeWidth(1.0);
      this.caretPath.fillProperty().bind(this.textFill);
      this.caretPath.strokeProperty().bind(this.textFill);
      this.caretPath.opacityProperty().bind(new DoubleBinding() {
         {
            this.bind(new Observable[]{TextAreaSkin.this.caretVisible});
         }

         protected double computeValue() {
            return TextAreaSkin.this.caretVisible.get() ? 1.0 : 0.0;
         }
      });
      this.contentView.getChildren().add(this.caretPath);
      if (SHOW_HANDLES) {
         this.contentView.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
      }

      this.scrollPane.hvalueProperty().addListener((var1x, var2x, var3x) -> {
         ((TextArea)this.getSkinnable()).setScrollLeft(var3x.doubleValue() * this.getScrollLeftMax());
      });
      this.scrollPane.vvalueProperty().addListener((var1x, var2x, var3x) -> {
         ((TextArea)this.getSkinnable()).setScrollTop(var3x.doubleValue() * this.getScrollTopMax());
      });
      this.scrollSelectionTimeline.setCycleCount(-1);
      ObservableList var2 = this.scrollSelectionTimeline.getKeyFrames();
      var2.clear();
      var2.add(new KeyFrame(Duration.millis(350.0), this.scrollSelectionHandler, new KeyValue[0]));
      int var3 = 0;

      for(byte var4 = 1; var3 < var4; ++var3) {
         Object var5 = var4 == 1 ? var1.textProperty().getValueSafe() : (CharSequence)var1.getParagraphs().get(var3);
         this.addParagraphNode(var3, ((CharSequence)var5).toString());
      }

      var1.selectionProperty().addListener((var2x, var3x, var4x) -> {
         var1.requestLayout();
         this.contentView.requestLayout();
      });
      var1.wrapTextProperty().addListener((var1x, var2x, var3x) -> {
         this.invalidateMetrics();
         this.scrollPane.setFitToWidth(var3x);
      });
      var1.prefColumnCountProperty().addListener((var1x, var2x, var3x) -> {
         this.invalidateMetrics();
         this.updatePrefViewportWidth();
      });
      var1.prefRowCountProperty().addListener((var1x, var2x, var3x) -> {
         this.invalidateMetrics();
         this.updatePrefViewportHeight();
      });
      this.updateFontMetrics();
      this.fontMetrics.addListener((var1x) -> {
         this.updateFontMetrics();
      });
      this.contentView.paddingProperty().addListener((var1x) -> {
         this.updatePrefViewportWidth();
         this.updatePrefViewportHeight();
      });
      this.scrollPane.viewportBoundsProperty().addListener((var1x) -> {
         if (this.scrollPane.getViewportBounds() != null) {
            Bounds var2 = this.scrollPane.getViewportBounds();
            if (this.oldViewportBounds == null || this.oldViewportBounds.getWidth() != var2.getWidth() || this.oldViewportBounds.getHeight() != var2.getHeight()) {
               this.invalidateMetrics();
               this.oldViewportBounds = var2;
               this.contentView.requestLayout();
            }
         }

      });
      var1.scrollTopProperty().addListener((var1x, var2x, var3x) -> {
         double var4 = var3x.doubleValue() < this.getScrollTopMax() ? var3x.doubleValue() / this.getScrollTopMax() : 1.0;
         this.scrollPane.setVvalue(var4);
      });
      var1.scrollLeftProperty().addListener((var1x, var2x, var3x) -> {
         double var4 = var3x.doubleValue() < this.getScrollLeftMax() ? var3x.doubleValue() / this.getScrollLeftMax() : 1.0;
         this.scrollPane.setHvalue(var4);
      });
      var1.textProperty().addListener((var2x) -> {
         this.invalidateMetrics();
         ((Text)this.paragraphNodes.getChildren().get(0)).setText(var1.textProperty().getValueSafe());
         this.contentView.requestLayout();
      });
      this.usePromptText = new BooleanBinding() {
         {
            this.bind(new Observable[]{var1.textProperty(), var1.promptTextProperty()});
         }

         protected boolean computeValue() {
            String var1x = var1.getText();
            String var2 = var1.getPromptText();
            return (var1x == null || var1x.isEmpty()) && var2 != null && !var2.isEmpty();
         }
      };
      if (this.usePromptText.get()) {
         this.createPromptNode();
      }

      this.usePromptText.addListener((var2x) -> {
         this.createPromptNode();
         var1.requestLayout();
      });
      this.updateHighlightFill();
      this.updatePrefViewportWidth();
      this.updatePrefViewportHeight();
      if (var1.isFocused()) {
         this.setCaretAnimating(true);
      }

      if (SHOW_HANDLES) {
         this.selectionHandle1.setRotate(180.0);
         EventHandler var6 = (var1x) -> {
            this.pressX = var1x.getX();
            this.pressY = var1x.getY();
            this.handlePressed = true;
            var1x.consume();
         };
         EventHandler var7 = (var1x) -> {
            this.handlePressed = false;
         };
         this.caretHandle.setOnMousePressed(var6);
         this.selectionHandle1.setOnMousePressed(var6);
         this.selectionHandle2.setOnMousePressed(var6);
         this.caretHandle.setOnMouseReleased(var7);
         this.selectionHandle1.setOnMouseReleased(var7);
         this.selectionHandle2.setOnMouseReleased(var7);
         this.caretHandle.setOnMouseDragged((var1x) -> {
            Text var2 = this.getTextNode();
            Point2D var3 = var2.localToScene(0.0, 0.0);
            Point2D var4 = new Point2D(var1x.getSceneX() - var3.getX() + 10.0 - this.pressX + this.caretHandle.getWidth() / 2.0, var1x.getSceneY() - var3.getY() - this.pressY - 6.0);
            HitInfo var5 = var2.impl_hitTestChar(this.translateCaretPosition(var4));
            int var6 = var5.getCharIndex();
            if (var6 > 0) {
               int var7 = var2.getImpl_caretPosition();
               var2.setImpl_caretPosition(var6);
               PathElement var8 = var2.getImpl_caretShape()[0];
               if (var8 instanceof MoveTo && ((MoveTo)var8).getY() > var1x.getY() - this.getTextTranslateY()) {
                  var5.setCharIndex(var6 - 1);
               }

               var2.setImpl_caretPosition(var7);
            }

            this.positionCaret(var5, false, false);
            var1x.consume();
         });
         this.selectionHandle1.setOnMouseDragged(new EventHandler() {
            public void handle(MouseEvent var1) {
               TextArea var2 = (TextArea)TextAreaSkin.this.getSkinnable();
               Text var3 = TextAreaSkin.this.getTextNode();
               Point2D var4 = var3.localToScene(0.0, 0.0);
               Point2D var5 = new Point2D(var1.getSceneX() - var4.getX() + 10.0 - TextAreaSkin.this.pressX + TextAreaSkin.this.selectionHandle1.getWidth() / 2.0, var1.getSceneY() - var4.getY() - TextAreaSkin.this.pressY + TextAreaSkin.this.selectionHandle1.getHeight() + 5.0);
               HitInfo var6 = var3.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(var5));
               int var7 = var6.getCharIndex();
               if (var2.getAnchor() < var2.getCaretPosition()) {
                  var2.selectRange(var2.getCaretPosition(), var2.getAnchor());
               }

               if (var7 > 0) {
                  if (var7 >= var2.getAnchor()) {
                     var7 = var2.getAnchor();
                  }

                  int var8 = var3.getImpl_caretPosition();
                  var3.setImpl_caretPosition(var7);
                  PathElement var9 = var3.getImpl_caretShape()[0];
                  if (var9 instanceof MoveTo && ((MoveTo)var9).getY() > var1.getY() - TextAreaSkin.this.getTextTranslateY()) {
                     var6.setCharIndex(var7 - 1);
                  }

                  var3.setImpl_caretPosition(var8);
               }

               TextAreaSkin.this.positionCaret(var6, true, false);
               var1.consume();
            }
         });
         this.selectionHandle2.setOnMouseDragged(new EventHandler() {
            public void handle(MouseEvent var1) {
               TextArea var2 = (TextArea)TextAreaSkin.this.getSkinnable();
               Text var3 = TextAreaSkin.this.getTextNode();
               Point2D var4 = var3.localToScene(0.0, 0.0);
               Point2D var5 = new Point2D(var1.getSceneX() - var4.getX() + 10.0 - TextAreaSkin.this.pressX + TextAreaSkin.this.selectionHandle2.getWidth() / 2.0, var1.getSceneY() - var4.getY() - TextAreaSkin.this.pressY - 6.0);
               HitInfo var6 = var3.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(var5));
               int var7 = var6.getCharIndex();
               if (var2.getAnchor() > var2.getCaretPosition()) {
                  var2.selectRange(var2.getCaretPosition(), var2.getAnchor());
               }

               if (var7 > 0) {
                  if (var7 <= var2.getAnchor() + 1) {
                     var7 = Math.min(var2.getAnchor() + 2, var2.getLength());
                  }

                  int var8 = var3.getImpl_caretPosition();
                  var3.setImpl_caretPosition(var7);
                  PathElement var9 = var3.getImpl_caretShape()[0];
                  if (var9 instanceof MoveTo && ((MoveTo)var9).getY() > var1.getY() - TextAreaSkin.this.getTextTranslateY()) {
                     var6.setCharIndex(var7 - 1);
                  }

                  var3.setImpl_caretPosition(var8);
                  TextAreaSkin.this.positionCaret(var6, true, false);
               }

               var1.consume();
            }
         });
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.scrollPane.resizeRelocate(var1, var3, var5, var7);
   }

   private void createPromptNode() {
      if (this.promptNode == null && this.usePromptText.get()) {
         this.promptNode = new Text();
         this.contentView.getChildren().add(0, this.promptNode);
         this.promptNode.setManaged(false);
         this.promptNode.getStyleClass().add("text");
         this.promptNode.visibleProperty().bind(this.usePromptText);
         this.promptNode.fontProperty().bind(((TextArea)this.getSkinnable()).fontProperty());
         this.promptNode.textProperty().bind(((TextArea)this.getSkinnable()).promptTextProperty());
         this.promptNode.fillProperty().bind(this.promptTextFill);
      }

   }

   private void addParagraphNode(int var1, String var2) {
      TextArea var3 = (TextArea)this.getSkinnable();
      Text var4 = new Text(var2);
      var4.setTextOrigin(VPos.TOP);
      var4.setManaged(false);
      var4.getStyleClass().add("text");
      var4.boundsTypeProperty().addListener((var1x, var2x, var3x) -> {
         this.invalidateMetrics();
         this.updateFontMetrics();
      });
      this.paragraphNodes.getChildren().add(var1, var4);
      var4.fontProperty().bind(var3.fontProperty());
      var4.fillProperty().bind(this.textFill);
      var4.impl_selectionFillProperty().bind(this.highlightTextFill);
   }

   public void dispose() {
      throw new UnsupportedOperationException();
   }

   public double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      Text var9 = (Text)this.paragraphNodes.getChildren().get(0);
      return Utils.getAscent(((TextArea)this.getSkinnable()).getFont(), var9.getBoundsType()) + this.contentView.snappedTopInset() + this.textArea.snappedTopInset();
   }

   public char getCharacter(int var1) {
      int var2 = this.paragraphNodes.getChildren().size();
      int var3 = 0;
      int var4 = var1;

      String var5;
      for(var5 = null; var3 < var2; ++var3) {
         Text var6 = (Text)this.paragraphNodes.getChildren().get(var3);
         var5 = var6.getText();
         int var7 = var5.length() + 1;
         if (var4 < var7) {
            break;
         }

         var4 -= var7;
      }

      return var4 == var5.length() ? '\n' : var5.charAt(var4);
   }

   public int getInsertionPoint(double var1, double var3) {
      TextArea var5 = (TextArea)this.getSkinnable();
      int var6 = this.paragraphNodes.getChildren().size();
      int var7 = -1;
      if (var6 > 0) {
         if (var3 < this.contentView.snappedTopInset()) {
            Text var8 = (Text)this.paragraphNodes.getChildren().get(0);
            var7 = this.getNextInsertionPoint(var8, var1, -1, VerticalDirection.DOWN);
         } else {
            int var14;
            if (var3 > this.contentView.snappedTopInset() + this.contentView.getHeight()) {
               var14 = var6 - 1;
               Text var9 = (Text)this.paragraphNodes.getChildren().get(var14);
               var7 = this.getNextInsertionPoint(var9, var1, -1, VerticalDirection.UP) + (var5.getLength() - var9.getText().length());
            } else {
               var14 = 0;

               for(int var15 = 0; var15 < var6; ++var15) {
                  Text var10 = (Text)this.paragraphNodes.getChildren().get(var15);
                  Bounds var11 = var10.getBoundsInLocal();
                  double var12 = var10.getLayoutY() + var11.getMinY();
                  if (var3 >= var12 && var3 < var12 + var10.getBoundsInLocal().getHeight()) {
                     var7 = this.getInsertionPoint(var10, var1 - var10.getLayoutX(), var3 - var10.getLayoutY()) + var14;
                     break;
                  }

                  var14 += var10.getText().length() + 1;
               }
            }
         }
      }

      return var7;
   }

   public void positionCaret(HitInfo var1, boolean var2, boolean var3) {
      int var4 = Utils.getHitInsertionIndex(var1, ((TextArea)this.getSkinnable()).getText());
      boolean var5 = var4 > 0 && var4 <= ((TextArea)this.getSkinnable()).getLength() && ((TextArea)this.getSkinnable()).getText().codePointAt(var4 - 1) == 10;
      if (!var1.isLeading() && var5) {
         var1.setLeading(true);
         --var4;
      }

      if (var2) {
         if (var3) {
            ((TextArea)this.getSkinnable()).extendSelection(var4);
         } else {
            ((TextArea)this.getSkinnable()).selectPositionCaret(var4);
         }
      } else {
         ((TextArea)this.getSkinnable()).positionCaret(var4);
      }

      this.setForwardBias(var1.isLeading());
   }

   private double getScrollTopMax() {
      return Math.max(0.0, this.contentView.getHeight() - this.scrollPane.getViewportBounds().getHeight());
   }

   private double getScrollLeftMax() {
      return Math.max(0.0, this.contentView.getWidth() - this.scrollPane.getViewportBounds().getWidth());
   }

   private int getInsertionPoint(Text var1, double var2, double var4) {
      HitInfo var6 = var1.impl_hitTestChar(new Point2D(var2, var4));
      return Utils.getHitInsertionIndex(var6, var1.getText());
   }

   public int getNextInsertionPoint(double var1, int var3, VerticalDirection var4) {
      return 0;
   }

   private int getNextInsertionPoint(Text var1, double var2, int var4, VerticalDirection var5) {
      return 0;
   }

   public Rectangle2D getCharacterBounds(int var1) {
      TextArea var2 = (TextArea)this.getSkinnable();
      int var3 = this.paragraphNodes.getChildren().size();
      int var4 = var2.getLength() + 1;
      Text var5 = null;

      do {
         ObservableList var10000 = this.paragraphNodes.getChildren();
         --var3;
         var5 = (Text)var10000.get(var3);
         var4 -= var5.getText().length() + 1;
      } while(var1 < var4);

      int var6 = var1 - var4;
      boolean var7 = false;
      if (var6 == var5.getText().length()) {
         --var6;
         var7 = true;
      }

      this.characterBoundingPath.getElements().clear();
      this.characterBoundingPath.getElements().addAll(var5.impl_getRangeShape(var6, var6 + 1));
      this.characterBoundingPath.setLayoutX(var5.getLayoutX());
      this.characterBoundingPath.setLayoutY(var5.getLayoutY());
      Bounds var8 = this.characterBoundingPath.getBoundsInLocal();
      double var9 = var8.getMinX() + var5.getLayoutX() - var2.getScrollLeft();
      double var11 = var8.getMinY() + var5.getLayoutY() - var2.getScrollTop();
      double var13 = var8.isEmpty() ? 0.0 : var8.getWidth();
      double var15 = var8.isEmpty() ? 0.0 : var8.getHeight();
      if (var7) {
         var9 += var13;
         var13 = 0.0;
      }

      return new Rectangle2D(var9, var11, var13, var15);
   }

   public void scrollCharacterToVisible(int var1) {
      Platform.runLater(() -> {
         if (((TextArea)this.getSkinnable()).getLength() != 0) {
            Rectangle2D var2 = this.getCharacterBounds(var1);
            this.scrollBoundsToVisible(var2);
         }
      });
   }

   private void scrollCaretToVisible() {
      TextArea var1 = (TextArea)this.getSkinnable();
      Bounds var2 = this.caretPath.getLayoutBounds();
      double var3 = var2.getMinX() - var1.getScrollLeft();
      double var5 = var2.getMinY() - var1.getScrollTop();
      double var7 = var2.getWidth();
      double var9 = var2.getHeight();
      if (SHOW_HANDLES) {
         if (this.caretHandle.isVisible()) {
            var9 += this.caretHandle.getHeight();
         } else if (this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible()) {
            var3 -= this.selectionHandle1.getWidth() / 2.0;
            var5 -= this.selectionHandle1.getHeight();
            var7 += this.selectionHandle1.getWidth() / 2.0 + this.selectionHandle2.getWidth() / 2.0;
            var9 += this.selectionHandle1.getHeight() + this.selectionHandle2.getHeight();
         }
      }

      if (var7 > 0.0 && var9 > 0.0) {
         this.scrollBoundsToVisible(new Rectangle2D(var3, var5, var7, var9));
      }

   }

   private void scrollBoundsToVisible(Rectangle2D var1) {
      TextArea var2 = (TextArea)this.getSkinnable();
      Bounds var3 = this.scrollPane.getViewportBounds();
      double var4 = var3.getWidth();
      double var6 = var3.getHeight();
      double var8 = var2.getScrollTop();
      double var10 = var2.getScrollLeft();
      double var12 = 6.0;
      double var14;
      if (var1.getMinY() < 0.0) {
         var14 = var8 + var1.getMinY();
         if (var14 <= this.contentView.snappedTopInset()) {
            var14 = 0.0;
         }

         var2.setScrollTop(var14);
      } else if (this.contentView.snappedTopInset() + var1.getMaxY() > var6) {
         var14 = var8 + this.contentView.snappedTopInset() + var1.getMaxY() - var6;
         if (var14 >= this.getScrollTopMax() - this.contentView.snappedBottomInset()) {
            var14 = this.getScrollTopMax();
         }

         var2.setScrollTop(var14);
      }

      if (var1.getMinX() < 0.0) {
         var14 = var10 + var1.getMinX() - var12;
         if (var14 <= this.contentView.snappedLeftInset() + var12) {
            var14 = 0.0;
         }

         var2.setScrollLeft(var14);
      } else if (this.contentView.snappedLeftInset() + var1.getMaxX() > var4) {
         var14 = var10 + this.contentView.snappedLeftInset() + var1.getMaxX() - var4 + var12;
         if (var14 >= this.getScrollLeftMax() - this.contentView.snappedRightInset() - var12) {
            var14 = this.getScrollLeftMax();
         }

         var2.setScrollLeft(var14);
      }

   }

   private void updatePrefViewportWidth() {
      int var1 = ((TextArea)this.getSkinnable()).getPrefColumnCount();
      this.scrollPane.setPrefViewportWidth((double)var1 * this.characterWidth + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
      this.scrollPane.setMinViewportWidth(this.characterWidth + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
   }

   private void updatePrefViewportHeight() {
      int var1 = ((TextArea)this.getSkinnable()).getPrefRowCount();
      this.scrollPane.setPrefViewportHeight((double)var1 * this.lineHeight + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
      this.scrollPane.setMinViewportHeight(this.lineHeight + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
   }

   private void updateFontMetrics() {
      Text var1 = (Text)this.paragraphNodes.getChildren().get(0);
      this.lineHeight = Utils.getLineHeight(((TextArea)this.getSkinnable()).getFont(), var1.getBoundsType());
      this.characterWidth = (double)((FontMetrics)this.fontMetrics.get()).computeStringWidth("W");
   }

   protected void updateHighlightFill() {
      Iterator var1 = this.selectionHighlightGroup.getChildren().iterator();

      while(var1.hasNext()) {
         Node var2 = (Node)var1.next();
         Path var3 = (Path)var2;
         var3.setFill((Paint)this.highlightFill.get());
      }

   }

   private double getTextTranslateX() {
      return this.contentView.snappedLeftInset();
   }

   private double getTextTranslateY() {
      return this.contentView.snappedTopInset();
   }

   private double getTextLeft() {
      return 0.0;
   }

   private Point2D translateCaretPosition(Point2D var1) {
      return var1;
   }

   private Text getTextNode() {
      return (Text)this.paragraphNodes.getChildren().get(0);
   }

   public HitInfo getIndex(double var1, double var3) {
      Text var5 = this.getTextNode();
      Point2D var6 = new Point2D(var1 - var5.getLayoutX(), var3 - this.getTextTranslateY());
      HitInfo var7 = var5.impl_hitTestChar(this.translateCaretPosition(var6));
      int var8 = var7.getCharIndex();
      if (var8 > 0) {
         int var9 = var5.getImpl_caretPosition();
         var5.setImpl_caretPosition(var8);
         PathElement var10 = var5.getImpl_caretShape()[0];
         if (var10 instanceof MoveTo && ((MoveTo)var10).getY() > var3 - this.getTextTranslateY()) {
            var7.setCharIndex(var8 - 1);
         }

         var5.setImpl_caretPosition(var9);
      }

      return var7;
   }

   public void nextCharacterVisually(boolean var1) {
      if (this.isRTL()) {
         var1 = !var1;
      }

      Text var2 = this.getTextNode();
      Bounds var3 = this.caretPath.getLayoutBounds();
      if (this.caretPath.getElements().size() == 4) {
         var3 = (new Path(new PathElement[]{(PathElement)this.caretPath.getElements().get(0), (PathElement)this.caretPath.getElements().get(1)})).getLayoutBounds();
      }

      double var4 = var1 ? var3.getMaxX() : var3.getMinX();
      double var6 = (var3.getMinY() + var3.getMaxY()) / 2.0;
      HitInfo var8 = var2.impl_hitTestChar(new Point2D(var4, var6));
      Path var9 = new Path(var2.impl_getRangeShape(var8.getCharIndex(), var8.getCharIndex() + 1));
      if (var1 && var9.getLayoutBounds().getMaxX() > var3.getMaxX() || !var1 && var9.getLayoutBounds().getMinX() < var3.getMinX()) {
         var8.setLeading(!var8.isLeading());
         this.positionCaret(var8, false, false);
      } else {
         int var10 = this.textArea.getCaretPosition();
         this.targetCaretX = var1 ? 0.0 : Double.MAX_VALUE;
         this.downLines(var1 ? 1 : -1, false, false);
         this.targetCaretX = -1.0;
         if (var10 == this.textArea.getCaretPosition()) {
            if (var1) {
               this.textArea.forward();
            } else {
               this.textArea.backward();
            }
         }
      }

   }

   protected void downLines(int var1, boolean var2, boolean var3) {
      Text var4 = this.getTextNode();
      Bounds var5 = this.caretPath.getLayoutBounds();
      double var6 = (var5.getMinY() + var5.getMaxY()) / 2.0 + (double)var1 * this.lineHeight;
      if (var6 < 0.0) {
         var6 = 0.0;
      }

      double var8 = this.targetCaretX >= 0.0 ? this.targetCaretX : var5.getMaxX();
      HitInfo var10 = var4.impl_hitTestChar(this.translateCaretPosition(new Point2D(var8, var6)));
      int var11 = var10.getCharIndex();
      int var12 = var4.getImpl_caretPosition();
      boolean var13 = var4.isImpl_caretBias();
      var4.setImpl_caretBias(var10.isLeading());
      var4.setImpl_caretPosition(var11);
      tmpCaretPath.getElements().clear();
      tmpCaretPath.getElements().addAll(var4.getImpl_caretShape());
      tmpCaretPath.setLayoutX(var4.getLayoutX());
      tmpCaretPath.setLayoutY(var4.getLayoutY());
      Bounds var14 = tmpCaretPath.getLayoutBounds();
      double var15 = (var14.getMinY() + var14.getMaxY()) / 2.0;
      var4.setImpl_caretBias(var13);
      var4.setImpl_caretPosition(var12);
      if (var11 > 0) {
         if (var1 > 0 && var15 > var6) {
            var10.setCharIndex(var11 - 1);
         }

         if (var11 >= this.textArea.getLength() && this.getCharacter(var11 - 1) == '\n') {
            var10.setLeading(true);
         }
      }

      if (var1 == 0 || var1 > 0 && var15 > var5.getMaxY() || var1 < 0 && var15 < var5.getMinY()) {
         this.positionCaret(var10, var2, var3);
         this.targetCaretX = var8;
      }

   }

   public void previousLine(boolean var1) {
      this.downLines(-1, var1, false);
   }

   public void nextLine(boolean var1) {
      this.downLines(1, var1, false);
   }

   public void previousPage(boolean var1) {
      this.downLines(-((int)(this.scrollPane.getViewportBounds().getHeight() / this.lineHeight)), var1, false);
   }

   public void nextPage(boolean var1) {
      this.downLines((int)(this.scrollPane.getViewportBounds().getHeight() / this.lineHeight), var1, false);
   }

   public void lineStart(boolean var1, boolean var2) {
      this.targetCaretX = 0.0;
      this.downLines(0, var1, var2);
      this.targetCaretX = -1.0;
   }

   public void lineEnd(boolean var1, boolean var2) {
      this.targetCaretX = Double.MAX_VALUE;
      this.downLines(0, var1, var2);
      this.targetCaretX = -1.0;
   }

   public void paragraphStart(boolean var1, boolean var2) {
      TextArea var3 = (TextArea)this.getSkinnable();
      String var4 = var3.textProperty().getValueSafe();
      int var5 = var3.getCaretPosition();
      if (var5 > 0) {
         if (var1 && var4.codePointAt(var5 - 1) == 10) {
            --var5;
         }

         while(var5 > 0 && var4.codePointAt(var5 - 1) != 10) {
            --var5;
         }

         if (var2) {
            var3.selectPositionCaret(var5);
         } else {
            var3.positionCaret(var5);
         }
      }

   }

   public void paragraphEnd(boolean var1, boolean var2, boolean var3) {
      TextArea var4 = (TextArea)this.getSkinnable();
      String var5 = var4.textProperty().getValueSafe();
      int var6 = var4.getCaretPosition();
      int var7 = var5.length();
      boolean var8 = false;
      if (var6 < var7) {
         if (var1 && var5.codePointAt(var6) == 10) {
            ++var6;
            var8 = true;
         }

         if (!var2 || !var8) {
            while(var6 < var7 && var5.codePointAt(var6) != 10) {
               ++var6;
            }

            if (var2 && var6 < var7) {
               ++var6;
            }
         }

         if (var3) {
            var4.selectPositionCaret(var6);
         } else {
            var4.positionCaret(var6);
         }
      }

   }

   private void updateTextNodeCaretPos(int var1) {
      Text var2 = this.getTextNode();
      if (this.isForwardBias()) {
         var2.setImpl_caretPosition(var1);
      } else {
         var2.setImpl_caretPosition(var1 - 1);
      }

      var2.impl_caretBiasProperty().set(this.isForwardBias());
   }

   protected PathElement[] getUnderlineShape(int var1, int var2) {
      int var3 = 0;

      int var7;
      for(Iterator var4 = this.paragraphNodes.getChildren().iterator(); var4.hasNext(); var3 = var7 + 1) {
         Node var5 = (Node)var4.next();
         Text var6 = (Text)var5;
         var7 = var3 + var6.textProperty().getValueSafe().length();
         if (var7 >= var1) {
            return var6.impl_getUnderlineShape(var1 - var3, var2 - var3);
         }
      }

      return null;
   }

   protected PathElement[] getRangeShape(int var1, int var2) {
      int var3 = 0;

      int var7;
      for(Iterator var4 = this.paragraphNodes.getChildren().iterator(); var4.hasNext(); var3 = var7 + 1) {
         Node var5 = (Node)var4.next();
         Text var6 = (Text)var5;
         var7 = var3 + var6.textProperty().getValueSafe().length();
         if (var7 >= var1) {
            return var6.impl_getRangeShape(var1 - var3, var2 - var3);
         }
      }

      return null;
   }

   protected void addHighlight(List var1, int var2) {
      int var3 = 0;
      Text var4 = null;

      Iterator var5;
      Node var6;
      int var8;
      for(var5 = this.paragraphNodes.getChildren().iterator(); var5.hasNext(); var3 = var8 + 1) {
         var6 = (Node)var5.next();
         Text var7 = (Text)var6;
         var8 = var3 + var7.textProperty().getValueSafe().length();
         if (var8 >= var2) {
            var4 = var7;
            break;
         }
      }

      if (var4 != null) {
         var5 = var1.iterator();

         while(var5.hasNext()) {
            var6 = (Node)var5.next();
            var6.setLayoutX(var4.getLayoutX());
            var6.setLayoutY(var4.getLayoutY());
         }
      }

      this.contentView.getChildren().addAll(var1);
   }

   protected void removeHighlight(List var1) {
      this.contentView.getChildren().removeAll(var1);
   }

   public void deleteChar(boolean var1) {
      boolean var2 = var1 ? !((TextArea)this.getSkinnable()).deletePreviousChar() : !((TextArea)this.getSkinnable()).deleteNextChar();
      if (var2) {
      }

   }

   public Point2D getMenuPosition() {
      this.contentView.layoutChildren();
      Point2D var1 = super.getMenuPosition();
      if (var1 != null) {
         var1 = new Point2D(Math.max(0.0, var1.getX() - this.contentView.snappedLeftInset() - ((TextArea)this.getSkinnable()).getScrollLeft()), Math.max(0.0, var1.getY() - this.contentView.snappedTopInset() - ((TextArea)this.getSkinnable()).getScrollTop()));
      }

      return var1;
   }

   public Bounds getCaretBounds() {
      return ((TextArea)this.getSkinnable()).sceneToLocal(this.caretPath.localToScene(this.caretPath.getBoundsInLocal()));
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case LINE_FOR_OFFSET:
         case LINE_START:
         case LINE_END:
         case BOUNDS_FOR_RANGE:
         case OFFSET_AT_POINT:
            Text var3 = this.getTextNode();
            return var3.queryAccessibleAttribute(var1, var2);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   // $FF: synthetic method
   private void lambda$new$15(ListChangeListener.Change var1) {
      while(var1.next()) {
         int var2 = var1.getFrom();
         int var3 = var1.getTo();
         List var4 = var1.getRemoved();
         if (var2 < var3) {
            int var5;
            int var6;
            if (var4.isEmpty()) {
               var5 = var2;

               for(var6 = var3; var5 < var6; ++var5) {
                  this.addParagraphNode(var5, ((CharSequence)var1.getList().get(var5)).toString());
               }
            } else {
               var5 = var2;

               for(var6 = var3; var5 < var6; ++var5) {
                  Node var7 = (Node)this.paragraphNodes.getChildren().get(var5);
                  Text var8 = (Text)var7;
                  var8.setText(((CharSequence)var1.getList().get(var5)).toString());
               }
            }
         } else {
            this.paragraphNodes.getChildren().subList(var2, var2 + var4.size()).clear();
         }
      }

   }

   private class ContentView extends Region {
      private ContentView() {
         this.getStyleClass().add("content");
         this.addEventHandler(MouseEvent.MOUSE_PRESSED, (var1x) -> {
            ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mousePressed(var1x);
            var1x.consume();
         });
         this.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
            ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mouseReleased(var1x);
            var1x.consume();
         });
         this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (var1x) -> {
            ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mouseDragged(var1x);
            var1x.consume();
         });
      }

      protected ObservableList getChildren() {
         return super.getChildren();
      }

      public Orientation getContentBias() {
         return Orientation.HORIZONTAL;
      }

      protected double computePrefWidth(double var1) {
         if (TextAreaSkin.this.computedPrefWidth < 0.0) {
            double var3 = 0.0;

            Text var7;
            for(Iterator var5 = TextAreaSkin.this.paragraphNodes.getChildren().iterator(); var5.hasNext(); var3 = Math.max(var3, Utils.computeTextWidth(var7.getFont(), var7.getText(), 0.0))) {
               Node var6 = (Node)var5.next();
               var7 = (Text)var6;
            }

            var3 += this.snappedLeftInset() + this.snappedRightInset();
            Bounds var8 = TextAreaSkin.this.scrollPane.getViewportBounds();
            TextAreaSkin.this.computedPrefWidth = Math.max(var3, var8 != null ? var8.getWidth() : 0.0);
         }

         return TextAreaSkin.this.computedPrefWidth;
      }

      protected double computePrefHeight(double var1) {
         if (var1 != TextAreaSkin.this.widthForComputedPrefHeight) {
            TextAreaSkin.this.invalidateMetrics();
            TextAreaSkin.this.widthForComputedPrefHeight = var1;
         }

         if (TextAreaSkin.this.computedPrefHeight < 0.0) {
            double var3;
            if (var1 == -1.0) {
               var3 = 0.0;
            } else {
               var3 = Math.max(var1 - (this.snappedLeftInset() + this.snappedRightInset()), 0.0);
            }

            double var5 = 0.0;

            Text var9;
            for(Iterator var7 = TextAreaSkin.this.paragraphNodes.getChildren().iterator(); var7.hasNext(); var5 += Utils.computeTextHeight(var9.getFont(), var9.getText(), var3, var9.getBoundsType())) {
               Node var8 = (Node)var7.next();
               var9 = (Text)var8;
            }

            var5 += this.snappedTopInset() + this.snappedBottomInset();
            Bounds var10 = TextAreaSkin.this.scrollPane.getViewportBounds();
            TextAreaSkin.this.computedPrefHeight = Math.max(var5, var10 != null ? var10.getHeight() : 0.0);
         }

         return TextAreaSkin.this.computedPrefHeight;
      }

      protected double computeMinWidth(double var1) {
         if (TextAreaSkin.this.computedMinWidth < 0.0) {
            double var3 = this.snappedLeftInset() + this.snappedRightInset();
            TextAreaSkin.this.computedMinWidth = Math.min(TextAreaSkin.this.characterWidth + var3, this.computePrefWidth(var1));
         }

         return TextAreaSkin.this.computedMinWidth;
      }

      protected double computeMinHeight(double var1) {
         if (TextAreaSkin.this.computedMinHeight < 0.0) {
            double var3 = this.snappedTopInset() + this.snappedBottomInset();
            TextAreaSkin.this.computedMinHeight = Math.min(TextAreaSkin.this.lineHeight + var3, this.computePrefHeight(var1));
         }

         return TextAreaSkin.this.computedMinHeight;
      }

      public void layoutChildren() {
         TextArea var1 = (TextArea)TextAreaSkin.this.getSkinnable();
         double var2 = this.getWidth();
         double var4 = this.snappedTopInset();
         double var6 = this.snappedLeftInset();
         double var8 = Math.max(var2 - (var6 + this.snappedRightInset()), 0.0);
         double var10 = var4;
         ObservableList var12 = TextAreaSkin.this.paragraphNodes.getChildren();

         for(int var13 = 0; var13 < var12.size(); ++var13) {
            Node var14 = (Node)var12.get(var13);
            Text var15 = (Text)var14;
            var15.setWrappingWidth(var8);
            Bounds var16 = var15.getBoundsInLocal();
            var15.setLayoutX(var6);
            var15.setLayoutY(var10);
            var10 += var16.getHeight();
         }

         if (TextAreaSkin.this.promptNode != null) {
            TextAreaSkin.this.promptNode.setLayoutX(var6);
            TextAreaSkin.this.promptNode.setLayoutY(var4 + TextAreaSkin.this.promptNode.getBaselineOffset());
            TextAreaSkin.this.promptNode.setWrappingWidth(var8);
         }

         IndexRange var26 = var1.getSelection();
         Bounds var27 = TextAreaSkin.this.caretPath.getBoundsInParent();
         TextAreaSkin.this.selectionHighlightGroup.getChildren().clear();
         int var28 = var1.getCaretPosition();
         int var29 = var1.getAnchor();
         int var17;
         int var18;
         Text var19;
         if (TextInputControlSkin.SHOW_HANDLES) {
            if (var26.getLength() > 0) {
               TextAreaSkin.this.selectionHandle1.resize(TextAreaSkin.this.selectionHandle1.prefWidth(-1.0), TextAreaSkin.this.selectionHandle1.prefHeight(-1.0));
               TextAreaSkin.this.selectionHandle2.resize(TextAreaSkin.this.selectionHandle2.prefWidth(-1.0), TextAreaSkin.this.selectionHandle2.prefHeight(-1.0));
            } else {
               TextAreaSkin.this.caretHandle.resize(TextAreaSkin.this.caretHandle.prefWidth(-1.0), TextAreaSkin.this.caretHandle.prefHeight(-1.0));
            }

            if (var26.getLength() > 0) {
               var17 = var12.size();
               var18 = var1.getLength() + 1;
               var19 = null;

               do {
                  --var17;
                  var19 = (Text)var12.get(var17);
                  var18 -= var19.getText().length() + 1;
               } while(var29 < var18);

               TextAreaSkin.this.updateTextNodeCaretPos(var29 - var18);
               TextAreaSkin.this.caretPath.getElements().clear();
               TextAreaSkin.this.caretPath.getElements().addAll(var19.getImpl_caretShape());
               TextAreaSkin.this.caretPath.setLayoutX(var19.getLayoutX());
               TextAreaSkin.this.caretPath.setLayoutY(var19.getLayoutY());
               Bounds var20 = TextAreaSkin.this.caretPath.getBoundsInParent();
               if (var28 < var29) {
                  TextAreaSkin.this.selectionHandle2.setLayoutX(var20.getMinX() - TextAreaSkin.this.selectionHandle2.getWidth() / 2.0);
                  TextAreaSkin.this.selectionHandle2.setLayoutY(var20.getMaxY() - 1.0);
               } else {
                  TextAreaSkin.this.selectionHandle1.setLayoutX(var20.getMinX() - TextAreaSkin.this.selectionHandle1.getWidth() / 2.0);
                  TextAreaSkin.this.selectionHandle1.setLayoutY(var20.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight() + 1.0);
               }
            }
         }

         var17 = var12.size();
         var18 = var1.getLength() + 1;
         var19 = null;

         do {
            --var17;
            var19 = (Text)var12.get(var17);
            var18 -= var19.getText().length() + 1;
         } while(var28 < var18);

         TextAreaSkin.this.updateTextNodeCaretPos(var28 - var18);
         TextAreaSkin.this.caretPath.getElements().clear();
         TextAreaSkin.this.caretPath.getElements().addAll(var19.getImpl_caretShape());
         TextAreaSkin.this.caretPath.setLayoutX(var19.getLayoutX());
         var19.setLayoutX(2.0 * var19.getLayoutX() - var19.getBoundsInParent().getMinX());
         TextAreaSkin.this.caretPath.setLayoutY(var19.getLayoutY());
         if (var27 == null || !var27.equals(TextAreaSkin.this.caretPath.getBoundsInParent())) {
            TextAreaSkin.this.scrollCaretToVisible();
         }

         var17 = var26.getStart();
         var18 = var26.getEnd();
         int var30 = 0;

         for(int var31 = var12.size(); var30 < var31; ++var30) {
            Node var21 = (Node)var12.get(var30);
            Text var22 = (Text)var21;
            int var23 = var22.getText().length() + 1;
            if (var18 > var17 && var17 < var23) {
               var22.setImpl_selectionStart(var17);
               var22.setImpl_selectionEnd(Math.min(var18, var23));
               Path var24 = new Path();
               var24.setManaged(false);
               var24.setStroke((Paint)null);
               PathElement[] var25 = var22.getImpl_selectionShape();
               if (var25 != null) {
                  var24.getElements().addAll(var25);
               }

               TextAreaSkin.this.selectionHighlightGroup.getChildren().add(var24);
               TextAreaSkin.this.selectionHighlightGroup.setVisible(true);
               var24.setLayoutX(var22.getLayoutX());
               var24.setLayoutY(var22.getLayoutY());
               TextAreaSkin.this.updateHighlightFill();
            } else {
               var22.setImpl_selectionStart(-1);
               var22.setImpl_selectionEnd(-1);
               TextAreaSkin.this.selectionHighlightGroup.setVisible(false);
            }

            var17 = Math.max(0, var17 - var23);
            var18 = Math.max(0, var18 - var23);
         }

         Bounds var33;
         if (TextInputControlSkin.SHOW_HANDLES) {
            var33 = TextAreaSkin.this.caretPath.getBoundsInParent();
            if (var26.getLength() > 0) {
               if (var28 < var29) {
                  TextAreaSkin.this.selectionHandle1.setLayoutX(var33.getMinX() - TextAreaSkin.this.selectionHandle1.getWidth() / 2.0);
                  TextAreaSkin.this.selectionHandle1.setLayoutY(var33.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight() + 1.0);
               } else {
                  TextAreaSkin.this.selectionHandle2.setLayoutX(var33.getMinX() - TextAreaSkin.this.selectionHandle2.getWidth() / 2.0);
                  TextAreaSkin.this.selectionHandle2.setLayoutY(var33.getMaxY() - 1.0);
               }
            } else {
               TextAreaSkin.this.caretHandle.setLayoutX(var33.getMinX() - TextAreaSkin.this.caretHandle.getWidth() / 2.0 + 1.0);
               TextAreaSkin.this.caretHandle.setLayoutY(var33.getMaxY());
            }
         }

         if (TextAreaSkin.this.scrollPane.getPrefViewportWidth() == 0.0 || TextAreaSkin.this.scrollPane.getPrefViewportHeight() == 0.0) {
            TextAreaSkin.this.updatePrefViewportWidth();
            TextAreaSkin.this.updatePrefViewportHeight();
            if (this.getParent() != null && TextAreaSkin.this.scrollPane.getPrefViewportWidth() > 0.0 || TextAreaSkin.this.scrollPane.getPrefViewportHeight() > 0.0) {
               this.getParent().requestLayout();
            }
         }

         var33 = TextAreaSkin.this.scrollPane.getViewportBounds();
         boolean var32 = TextAreaSkin.this.scrollPane.isFitToWidth();
         boolean var34 = TextAreaSkin.this.scrollPane.isFitToHeight();
         boolean var35 = var1.isWrapText() || this.computePrefWidth(-1.0) <= var33.getWidth();
         boolean var36 = this.computePrefHeight(var2) <= var33.getHeight();
         if (var32 != var35 || var34 != var36) {
            Platform.runLater(() -> {
               TextAreaSkin.this.scrollPane.setFitToWidth(var35);
               TextAreaSkin.this.scrollPane.setFitToHeight(var36);
            });
            this.getParent().requestLayout();
         }

      }

      // $FF: synthetic method
      ContentView(Object var2) {
         this();
      }
   }
}
