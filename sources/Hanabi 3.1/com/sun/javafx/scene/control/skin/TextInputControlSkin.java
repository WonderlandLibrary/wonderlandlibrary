package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAction;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import javafx.stage.Window;
import javafx.util.Duration;

public abstract class TextInputControlSkin extends BehaviorSkinBase {
   static boolean preload = false;
   protected static final boolean SHOW_HANDLES;
   protected final ObservableObjectValue fontMetrics;
   protected final ObjectProperty textFill;
   protected final ObjectProperty promptTextFill;
   protected final ObjectProperty highlightFill;
   protected final ObjectProperty highlightTextFill;
   protected final BooleanProperty displayCaret;
   private BooleanProperty forwardBias;
   private BooleanProperty blink;
   protected ObservableBooleanValue caretVisible;
   private CaretBlinking caretBlinking;
   protected final Path caretPath;
   protected StackPane caretHandle;
   protected StackPane selectionHandle1;
   protected StackPane selectionHandle2;
   private static final boolean IS_FXVK_SUPPORTED;
   private static boolean USE_FXVK;
   static int vkType;
   private int imstart;
   private int imlength;
   private List imattrs;
   final MenuItem undoMI;
   final MenuItem redoMI;
   final MenuItem cutMI;
   final MenuItem copyMI;
   final MenuItem pasteMI;
   final MenuItem deleteMI;
   final MenuItem selectWordMI;
   final MenuItem selectAllMI;
   final MenuItem separatorMI;

   public BooleanProperty forwardBiasProperty() {
      return this.forwardBias;
   }

   public void setForwardBias(boolean var1) {
      this.forwardBias.set(var1);
   }

   public boolean isForwardBias() {
      return this.forwardBias.get();
   }

   public Point2D getMenuPosition() {
      if (SHOW_HANDLES) {
         if (this.caretHandle.isVisible()) {
            return new Point2D(this.caretHandle.getLayoutX() + this.caretHandle.getWidth() / 2.0, this.caretHandle.getLayoutY());
         } else {
            return this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible() ? new Point2D((this.selectionHandle1.getLayoutX() + this.selectionHandle1.getWidth() / 2.0 + this.selectionHandle2.getLayoutX() + this.selectionHandle2.getWidth() / 2.0) / 2.0, this.selectionHandle2.getLayoutY() + this.selectionHandle2.getHeight() / 2.0) : null;
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public void toggleUseVK() {
      ++vkType;
      if (vkType < 4) {
         USE_FXVK = true;
         ((TextInputControl)this.getSkinnable()).getProperties().put("vkType", FXVK.VK_TYPE_NAMES[vkType]);
         FXVK.attach(this.getSkinnable());
      } else {
         FXVK.detach();
         vkType = -1;
         USE_FXVK = false;
      }

   }

   public TextInputControlSkin(final TextInputControl var1, TextInputControlBehavior var2) {
      super(var1, var2);
      this.textFill = new StyleableObjectProperty(Color.BLACK) {
         public Object getBean() {
            return TextInputControlSkin.this;
         }

         public String getName() {
            return "textFill";
         }

         public CssMetaData getCssMetaData() {
            return TextInputControlSkin.StyleableProperties.TEXT_FILL;
         }
      };
      this.promptTextFill = new StyleableObjectProperty(Color.GRAY) {
         public Object getBean() {
            return TextInputControlSkin.this;
         }

         public String getName() {
            return "promptTextFill";
         }

         public CssMetaData getCssMetaData() {
            return TextInputControlSkin.StyleableProperties.PROMPT_TEXT_FILL;
         }
      };
      this.highlightFill = new StyleableObjectProperty(Color.DODGERBLUE) {
         protected void invalidated() {
            TextInputControlSkin.this.updateHighlightFill();
         }

         public Object getBean() {
            return TextInputControlSkin.this;
         }

         public String getName() {
            return "highlightFill";
         }

         public CssMetaData getCssMetaData() {
            return TextInputControlSkin.StyleableProperties.HIGHLIGHT_FILL;
         }
      };
      this.highlightTextFill = new StyleableObjectProperty(Color.WHITE) {
         protected void invalidated() {
            TextInputControlSkin.this.updateHighlightTextFill();
         }

         public Object getBean() {
            return TextInputControlSkin.this;
         }

         public String getName() {
            return "highlightTextFill";
         }

         public CssMetaData getCssMetaData() {
            return TextInputControlSkin.StyleableProperties.HIGHLIGHT_TEXT_FILL;
         }
      };
      this.displayCaret = new StyleableBooleanProperty(true) {
         public Object getBean() {
            return TextInputControlSkin.this;
         }

         public String getName() {
            return "displayCaret";
         }

         public CssMetaData getCssMetaData() {
            return TextInputControlSkin.StyleableProperties.DISPLAY_CARET;
         }
      };
      this.forwardBias = new SimpleBooleanProperty(this, "forwardBias", true);
      this.blink = new SimpleBooleanProperty(this, "blink", true);
      this.caretBlinking = new CaretBlinking(this.blink);
      this.caretPath = new Path();
      this.caretHandle = null;
      this.selectionHandle1 = null;
      this.selectionHandle2 = null;
      this.imattrs = new ArrayList();
      this.undoMI = new ContextMenuItem("Undo");
      this.redoMI = new ContextMenuItem("Redo");
      this.cutMI = new ContextMenuItem("Cut");
      this.copyMI = new ContextMenuItem("Copy");
      this.pasteMI = new ContextMenuItem("Paste");
      this.deleteMI = new ContextMenuItem("DeleteSelection");
      this.selectWordMI = new ContextMenuItem("SelectWord");
      this.selectAllMI = new ContextMenuItem("SelectAll");
      this.separatorMI = new SeparatorMenuItem();
      this.fontMetrics = new ObjectBinding() {
         {
            this.bind(new Observable[]{var1.fontProperty()});
         }

         protected FontMetrics computeValue() {
            TextInputControlSkin.this.invalidateMetrics();
            return Toolkit.getToolkit().getFontLoader().getFontMetrics(var1.getFont());
         }
      };
      this.caretVisible = new BooleanBinding() {
         {
            this.bind(new Observable[]{var1.focusedProperty(), var1.anchorProperty(), var1.caretPositionProperty(), var1.disabledProperty(), var1.editableProperty(), TextInputControlSkin.this.displayCaret, TextInputControlSkin.this.blink});
         }

         protected boolean computeValue() {
            return !TextInputControlSkin.this.blink.get() && TextInputControlSkin.this.displayCaret.get() && var1.isFocused() && (PlatformUtil.isWindows() || var1.getCaretPosition() == var1.getAnchor()) && !var1.isDisabled() && var1.isEditable();
         }
      };
      if (SHOW_HANDLES) {
         this.caretHandle = new StackPane();
         this.selectionHandle1 = new StackPane();
         this.selectionHandle2 = new StackPane();
         this.caretHandle.setManaged(false);
         this.selectionHandle1.setManaged(false);
         this.selectionHandle2.setManaged(false);
         this.caretHandle.visibleProperty().bind(new BooleanBinding() {
            {
               this.bind(new Observable[]{var1.focusedProperty(), var1.anchorProperty(), var1.caretPositionProperty(), var1.disabledProperty(), var1.editableProperty(), var1.lengthProperty(), TextInputControlSkin.this.displayCaret});
            }

            protected boolean computeValue() {
               return TextInputControlSkin.this.displayCaret.get() && var1.isFocused() && var1.getCaretPosition() == var1.getAnchor() && !var1.isDisabled() && var1.isEditable() && var1.getLength() > 0;
            }
         });
         this.selectionHandle1.visibleProperty().bind(new BooleanBinding() {
            {
               this.bind(new Observable[]{var1.focusedProperty(), var1.anchorProperty(), var1.caretPositionProperty(), var1.disabledProperty(), TextInputControlSkin.this.displayCaret});
            }

            protected boolean computeValue() {
               return TextInputControlSkin.this.displayCaret.get() && var1.isFocused() && var1.getCaretPosition() != var1.getAnchor() && !var1.isDisabled();
            }
         });
         this.selectionHandle2.visibleProperty().bind(new BooleanBinding() {
            {
               this.bind(new Observable[]{var1.focusedProperty(), var1.anchorProperty(), var1.caretPositionProperty(), var1.disabledProperty(), TextInputControlSkin.this.displayCaret});
            }

            protected boolean computeValue() {
               return TextInputControlSkin.this.displayCaret.get() && var1.isFocused() && var1.getCaretPosition() != var1.getAnchor() && !var1.isDisabled();
            }
         });
         this.caretHandle.getStyleClass().setAll((Object[])("caret-handle"));
         this.selectionHandle1.getStyleClass().setAll((Object[])("selection-handle"));
         this.selectionHandle2.getStyleClass().setAll((Object[])("selection-handle"));
         this.selectionHandle1.setId("selection-handle-1");
         this.selectionHandle2.setId("selection-handle-2");
      }

      if (IS_FXVK_SUPPORTED) {
         if (preload) {
            Scene var3 = var1.getScene();
            if (var3 != null) {
               Window var4 = var3.getWindow();
               if (var4 != null) {
                  FXVK.init(var1);
               }
            }
         }

         var1.focusedProperty().addListener((var2x) -> {
            if (USE_FXVK) {
               Scene var3 = ((TextInputControl)this.getSkinnable()).getScene();
               if (var1.isEditable() && var1.isFocused()) {
                  FXVK.attach(var1);
               } else if (var3 == null || var3.getWindow() == null || !var3.getWindow().isFocused() || !(var3.getFocusOwner() instanceof TextInputControl) || !((TextInputControl)var3.getFocusOwner()).isEditable()) {
                  FXVK.detach();
               }
            }

         });
      }

      if (var1.getOnInputMethodTextChanged() == null) {
         var1.setOnInputMethodTextChanged((var1x) -> {
            this.handleInputMethodEvent(var1x);
         });
      }

      var1.setInputMethodRequests(new ExtendedInputMethodRequests() {
         public Point2D getTextLocation(int var1x) {
            Scene var2 = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getScene();
            Window var3 = var2.getWindow();
            Rectangle2D var4 = TextInputControlSkin.this.getCharacterBounds(var1.getSelection().getStart() + var1x);
            Point2D var5 = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).localToScene(var4.getMinX(), var4.getMaxY());
            Point2D var6 = new Point2D(var3.getX() + var2.getX() + var5.getX(), var3.getY() + var2.getY() + var5.getY());
            return var6;
         }

         public int getLocationOffset(int var1x, int var2) {
            return TextInputControlSkin.this.getInsertionPoint((double)var1x, (double)var2);
         }

         public void cancelLatestCommittedText() {
         }

         public String getSelectedText() {
            TextInputControl var1x = (TextInputControl)TextInputControlSkin.this.getSkinnable();
            IndexRange var2 = var1x.getSelection();
            return var1x.getText(var2.getStart(), var2.getEnd());
         }

         public int getInsertPositionOffset() {
            int var1x = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getCaretPosition();
            if (var1x < TextInputControlSkin.this.imstart) {
               return var1x;
            } else {
               return var1x < TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength ? TextInputControlSkin.this.imstart : var1x - TextInputControlSkin.this.imlength;
            }
         }

         public String getCommittedText(int var1x, int var2) {
            TextInputControl var3 = (TextInputControl)TextInputControlSkin.this.getSkinnable();
            if (var1x < TextInputControlSkin.this.imstart) {
               return var2 <= TextInputControlSkin.this.imstart ? var3.getText(var1x, var2) : var3.getText(var1x, TextInputControlSkin.this.imstart) + var3.getText(TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength, var2 + TextInputControlSkin.this.imlength);
            } else {
               return var3.getText(var1x + TextInputControlSkin.this.imlength, var2 + TextInputControlSkin.this.imlength);
            }
         }

         public int getCommittedTextLength() {
            return ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getText().length() - TextInputControlSkin.this.imlength;
         }
      });
   }

   protected String maskText(String var1) {
      return var1;
   }

   public char getCharacter(int var1) {
      return '\u0000';
   }

   public int getInsertionPoint(double var1, double var3) {
      return 0;
   }

   public Rectangle2D getCharacterBounds(int var1) {
      return null;
   }

   public void scrollCharacterToVisible(int var1) {
   }

   protected void invalidateMetrics() {
   }

   protected void updateTextFill() {
   }

   protected void updateHighlightFill() {
   }

   protected void updateHighlightTextFill() {
   }

   protected void handleInputMethodEvent(InputMethodEvent var1) {
      TextInputControl var2 = (TextInputControl)this.getSkinnable();
      if (var2.isEditable() && !var2.textProperty().isBound() && !var2.isDisabled()) {
         if (PlatformUtil.isIOS()) {
            var2.setText(var1.getCommitted());
            return;
         }

         if (this.imlength != 0) {
            this.removeHighlight(this.imattrs);
            this.imattrs.clear();
            var2.selectRange(this.imstart, this.imstart + this.imlength);
         }

         if (var1.getCommitted().length() != 0) {
            String var3 = var1.getCommitted();
            var2.replaceText(var2.getSelection(), var3);
         }

         this.imstart = var2.getSelection().getStart();
         StringBuilder var8 = new StringBuilder();
         Iterator var4 = var1.getComposed().iterator();

         while(var4.hasNext()) {
            InputMethodTextRun var5 = (InputMethodTextRun)var4.next();
            var8.append(var5.getText());
         }

         var2.replaceText(var2.getSelection(), var8.toString());
         this.imlength = var8.length();
         if (this.imlength != 0) {
            int var9 = this.imstart;

            int var7;
            for(Iterator var10 = var1.getComposed().iterator(); var10.hasNext(); var9 = var7) {
               InputMethodTextRun var6 = (InputMethodTextRun)var10.next();
               var7 = var9 + var6.getText().length();
               this.createInputMethodAttributes(var6.getHighlight(), var9, var7);
            }

            this.addHighlight(this.imattrs, this.imstart);
            int var11 = var1.getCaretPosition();
            if (var11 >= 0 && var11 < this.imlength) {
               var2.selectRange(this.imstart + var11, this.imstart + var11);
            }
         }
      }

   }

   protected abstract PathElement[] getUnderlineShape(int var1, int var2);

   protected abstract PathElement[] getRangeShape(int var1, int var2);

   protected abstract void addHighlight(List var1, int var2);

   protected abstract void removeHighlight(List var1);

   public abstract void nextCharacterVisually(boolean var1);

   private void createInputMethodAttributes(InputMethodHighlight var1, int var2, int var3) {
      double var4 = 0.0;
      double var6 = 0.0;
      double var8 = 0.0;
      double var10 = 0.0;
      PathElement[] var12 = this.getUnderlineShape(var2, var3);

      for(int var13 = 0; var13 < var12.length; ++var13) {
         PathElement var14 = var12[var13];
         if (var14 instanceof MoveTo) {
            var4 = var6 = ((MoveTo)var14).getX();
            var8 = var10 = ((MoveTo)var14).getY();
         } else if (var14 instanceof LineTo) {
            var4 = var4 < ((LineTo)var14).getX() ? var4 : ((LineTo)var14).getX();
            var6 = var6 > ((LineTo)var14).getX() ? var6 : ((LineTo)var14).getX();
            var8 = var8 < ((LineTo)var14).getY() ? var8 : ((LineTo)var14).getY();
            var10 = var10 > ((LineTo)var14).getY() ? var10 : ((LineTo)var14).getY();
         } else if (var14 instanceof HLineTo) {
            var4 = var4 < ((HLineTo)var14).getX() ? var4 : ((HLineTo)var14).getX();
            var6 = var6 > ((HLineTo)var14).getX() ? var6 : ((HLineTo)var14).getX();
         } else if (var14 instanceof VLineTo) {
            var8 = var8 < ((VLineTo)var14).getY() ? var8 : ((VLineTo)var14).getY();
            var10 = var10 > ((VLineTo)var14).getY() ? var10 : ((VLineTo)var14).getY();
         }

         if (var14 instanceof ClosePath || var13 == var12.length - 1 || var13 < var12.length - 1 && var12[var13 + 1] instanceof MoveTo) {
            Object var15 = null;
            if (var1 == InputMethodHighlight.SELECTED_RAW) {
               var15 = new Path();
               ((Path)var15).getElements().addAll(this.getRangeShape(var2, var3));
               ((Shape)var15).setFill(Color.BLUE);
               ((Shape)var15).setOpacity(0.30000001192092896);
            } else if (var1 == InputMethodHighlight.UNSELECTED_RAW) {
               var15 = new Line(var4 + 2.0, var10 + 1.0, var6 - 2.0, var10 + 1.0);
               ((Shape)var15).setStroke((Paint)this.textFill.get());
               ((Shape)var15).setStrokeWidth(var10 - var8);
               ObservableList var16 = ((Shape)var15).getStrokeDashArray();
               var16.add(2.0);
               var16.add(2.0);
            } else if (var1 == InputMethodHighlight.SELECTED_CONVERTED) {
               var15 = new Line(var4 + 2.0, var10 + 1.0, var6 - 2.0, var10 + 1.0);
               ((Shape)var15).setStroke((Paint)this.textFill.get());
               ((Shape)var15).setStrokeWidth((var10 - var8) * 3.0);
            } else if (var1 == InputMethodHighlight.UNSELECTED_CONVERTED) {
               var15 = new Line(var4 + 2.0, var10 + 1.0, var6 - 2.0, var10 + 1.0);
               ((Shape)var15).setStroke((Paint)this.textFill.get());
               ((Shape)var15).setStrokeWidth(var10 - var8);
            }

            if (var15 != null) {
               ((Shape)var15).setManaged(false);
               this.imattrs.add(var15);
            }
         }
      }

   }

   protected boolean isRTL() {
      return ((TextInputControl)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
   }

   public void setCaretAnimating(boolean var1) {
      if (var1) {
         this.caretBlinking.start();
      } else {
         this.caretBlinking.stop();
         this.blink.set(true);
      }

   }

   public void populateContextMenu(ContextMenu var1) {
      TextInputControl var2 = (TextInputControl)this.getSkinnable();
      boolean var3 = var2.isEditable();
      boolean var4 = var2.getLength() > 0;
      boolean var5 = var2.getSelection().getLength() > 0;
      boolean var6 = this.maskText("A") != "A";
      ObservableList var7 = var1.getItems();
      if (SHOW_HANDLES) {
         var7.clear();
         if (!var6 && var5) {
            if (var3) {
               var7.add(this.cutMI);
            }

            var7.add(this.copyMI);
         }

         if (var3 && Clipboard.getSystemClipboard().hasString()) {
            var7.add(this.pasteMI);
         }

         if (var4) {
            if (!var5) {
               var7.add(this.selectWordMI);
            }

            var7.add(this.selectAllMI);
         }

         this.selectWordMI.getProperties().put("refreshMenu", Boolean.TRUE);
         this.selectAllMI.getProperties().put("refreshMenu", Boolean.TRUE);
      } else {
         if (var3) {
            var7.setAll((Object[])(this.undoMI, this.redoMI, this.cutMI, this.copyMI, this.pasteMI, this.deleteMI, this.separatorMI, this.selectAllMI));
         } else {
            var7.setAll((Object[])(this.copyMI, this.separatorMI, this.selectAllMI));
         }

         this.undoMI.setDisable(!((TextInputControl)this.getSkinnable()).isUndoable());
         this.redoMI.setDisable(!((TextInputControl)this.getSkinnable()).isRedoable());
         this.cutMI.setDisable(var6 || !var5);
         this.copyMI.setDisable(var6 || !var5);
         this.pasteMI.setDisable(!Clipboard.getSystemClipboard().hasString());
         this.deleteMI.setDisable(!var5);
      }

   }

   public static List getClassCssMetaData() {
      return TextInputControlSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SHOW_TEXT_RANGE:
            Integer var3 = (Integer)var2[0];
            Integer var4 = (Integer)var2[1];
            if (var3 != null && var4 != null) {
               this.scrollCharacterToVisible(var4);
               this.scrollCharacterToVisible(var3);
               this.scrollCharacterToVisible(var4);
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("com.sun.javafx.virtualKeyboard.preload");
         if (var0 != null && var0.equalsIgnoreCase("PRERENDER")) {
            preload = true;
         }

         return null;
      });
      SHOW_HANDLES = IS_TOUCH_SUPPORTED && !PlatformUtil.isIOS();
      IS_FXVK_SUPPORTED = Platform.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD);
      USE_FXVK = IS_FXVK_SUPPORTED;
      vkType = -1;
   }

   private static class StyleableProperties {
      private static final CssMetaData TEXT_FILL;
      private static final CssMetaData PROMPT_TEXT_FILL;
      private static final CssMetaData HIGHLIGHT_FILL;
      private static final CssMetaData HIGHLIGHT_TEXT_FILL;
      private static final CssMetaData DISPLAY_CARET;
      private static final List STYLEABLES;

      static {
         TEXT_FILL = new CssMetaData("-fx-text-fill", PaintConverter.getInstance(), Color.BLACK) {
            public boolean isSettable(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return var2.textFill == null || !var2.textFill.isBound();
            }

            public StyleableProperty getStyleableProperty(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return (StyleableProperty)var2.textFill;
            }
         };
         PROMPT_TEXT_FILL = new CssMetaData("-fx-prompt-text-fill", PaintConverter.getInstance(), Color.GRAY) {
            public boolean isSettable(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return var2.promptTextFill == null || !var2.promptTextFill.isBound();
            }

            public StyleableProperty getStyleableProperty(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return (StyleableProperty)var2.promptTextFill;
            }
         };
         HIGHLIGHT_FILL = new CssMetaData("-fx-highlight-fill", PaintConverter.getInstance(), Color.DODGERBLUE) {
            public boolean isSettable(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return var2.highlightFill == null || !var2.highlightFill.isBound();
            }

            public StyleableProperty getStyleableProperty(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return (StyleableProperty)var2.highlightFill;
            }
         };
         HIGHLIGHT_TEXT_FILL = new CssMetaData("-fx-highlight-text-fill", PaintConverter.getInstance(), Color.WHITE) {
            public boolean isSettable(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return var2.highlightTextFill == null || !var2.highlightTextFill.isBound();
            }

            public StyleableProperty getStyleableProperty(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return (StyleableProperty)var2.highlightTextFill;
            }
         };
         DISPLAY_CARET = new CssMetaData("-fx-display-caret", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return var2.displayCaret == null || !var2.displayCaret.isBound();
            }

            public StyleableProperty getStyleableProperty(TextInputControl var1) {
               TextInputControlSkin var2 = (TextInputControlSkin)var1.getSkin();
               return (StyleableProperty)var2.displayCaret;
            }
         };
         ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
         var0.add(TEXT_FILL);
         var0.add(PROMPT_TEXT_FILL);
         var0.add(HIGHLIGHT_FILL);
         var0.add(HIGHLIGHT_TEXT_FILL);
         var0.add(DISPLAY_CARET);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   class ContextMenuItem extends MenuItem {
      ContextMenuItem(String var2) {
         super(ControlResources.getString("TextInputControl.menu." + var2));
         this.setOnAction((var2x) -> {
            ((TextInputControlBehavior)TextInputControlSkin.this.getBehavior()).callAction(var2);
         });
      }
   }

   private static final class CaretBlinking {
      private final Timeline caretTimeline;
      private final WeakReference blinkPropertyRef;

      public CaretBlinking(BooleanProperty var1) {
         this.blinkPropertyRef = new WeakReference(var1);
         this.caretTimeline = new Timeline();
         this.caretTimeline.setCycleCount(-1);
         this.caretTimeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, (var1x) -> {
            this.setBlink(false);
         }, new KeyValue[0]), new KeyFrame(Duration.seconds(0.5), (var1x) -> {
            this.setBlink(true);
         }, new KeyValue[0]), new KeyFrame(Duration.seconds(1.0), new KeyValue[0]));
      }

      public void start() {
         this.caretTimeline.play();
      }

      public void stop() {
         this.caretTimeline.stop();
      }

      private void setBlink(boolean var1) {
         BooleanProperty var2 = (BooleanProperty)this.blinkPropertyRef.get();
         if (var2 == null) {
            this.caretTimeline.stop();
         } else {
            var2.set(var1);
         }
      }
   }
}
