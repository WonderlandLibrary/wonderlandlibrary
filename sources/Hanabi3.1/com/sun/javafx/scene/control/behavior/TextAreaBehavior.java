package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextAreaBehavior extends TextInputControlBehavior {
   protected static final List TEXT_AREA_BINDINGS = new ArrayList();
   private TextAreaSkin skin;
   private ContextMenu contextMenu = new ContextMenu();
   private TwoLevelFocusBehavior tlFocus;
   private boolean focusGainedByMouseClick = false;
   private boolean shiftDown = false;
   private boolean deferClick = false;

   public TextAreaBehavior(TextArea var1) {
      super(var1, TEXT_AREA_BINDINGS);
      if (IS_TOUCH_SUPPORTED) {
         this.contextMenu.getStyleClass().add("text-input-context-menu");
      }

      var1.focusedProperty().addListener(new ChangeListener() {
         public void changed(ObservableValue var1, Boolean var2, Boolean var3) {
            TextArea var4 = (TextArea)TextAreaBehavior.this.getControl();
            if (var4.isFocused()) {
               if (PlatformUtil.isIOS()) {
                  Bounds var5 = var4.getBoundsInParent();
                  double var6 = var5.getWidth();
                  double var8 = var5.getHeight();
                  Affine3D var10 = TextFieldBehavior.calculateNodeToSceneTransform(var4);
                  String var11 = var4.textProperty().getValueSafe();
                  var4.getScene().getWindow().impl_getPeer().requestInput(var11, TextFieldBehavior.TextInputTypes.TEXT_AREA.ordinal(), var6, var8, var10.getMxx(), var10.getMxy(), var10.getMxz(), var10.getMxt(), var10.getMyx(), var10.getMyy(), var10.getMyz(), var10.getMyt(), var10.getMzx(), var10.getMzy(), var10.getMzz(), var10.getMzt());
               }

               if (!TextAreaBehavior.this.focusGainedByMouseClick) {
                  TextAreaBehavior.this.setCaretAnimating(true);
               }
            } else {
               if (PlatformUtil.isIOS() && var4.getScene() != null) {
                  var4.getScene().getWindow().impl_getPeer().releaseInput();
               }

               TextAreaBehavior.this.focusGainedByMouseClick = false;
               TextAreaBehavior.this.setCaretAnimating(false);
            }

         }
      });
      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusBehavior(var1);
      }

   }

   public void dispose() {
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   public void setTextAreaSkin(TextAreaSkin var1) {
      this.skin = var1;
   }

   public void callAction(String var1) {
      TextArea var2 = (TextArea)this.getControl();
      boolean var3 = false;
      if (var2.isEditable()) {
         this.setEditing(true);
         var3 = true;
         if ("InsertNewLine".equals(var1)) {
            this.insertNewLine();
         } else if ("TraverseOrInsertTab".equals(var1)) {
            this.insertTab();
         } else {
            var3 = false;
         }

         this.setEditing(false);
      }

      if (!var3) {
         var3 = true;
         if ("LineStart".equals(var1)) {
            this.lineStart(false, false);
         } else if ("LineEnd".equals(var1)) {
            this.lineEnd(false, false);
         } else if ("SelectLineStart".equals(var1)) {
            this.lineStart(true, false);
         } else if ("SelectLineStartExtend".equals(var1)) {
            this.lineStart(true, true);
         } else if ("SelectLineEnd".equals(var1)) {
            this.lineEnd(true, false);
         } else if ("SelectLineEndExtend".equals(var1)) {
            this.lineEnd(true, true);
         } else if ("PreviousLine".equals(var1)) {
            this.skin.previousLine(false);
         } else if ("NextLine".equals(var1)) {
            this.skin.nextLine(false);
         } else if ("SelectPreviousLine".equals(var1)) {
            this.skin.previousLine(true);
         } else if ("SelectNextLine".equals(var1)) {
            this.skin.nextLine(true);
         } else if ("ParagraphStart".equals(var1)) {
            this.skin.paragraphStart(true, false);
         } else if ("ParagraphEnd".equals(var1)) {
            this.skin.paragraphEnd(true, PlatformUtil.isWindows(), false);
         } else if ("SelectParagraphStart".equals(var1)) {
            this.skin.paragraphStart(true, true);
         } else if ("SelectParagraphEnd".equals(var1)) {
            this.skin.paragraphEnd(true, PlatformUtil.isWindows(), true);
         } else if ("PreviousPage".equals(var1)) {
            this.skin.previousPage(false);
         } else if ("NextPage".equals(var1)) {
            this.skin.nextPage(false);
         } else if ("SelectPreviousPage".equals(var1)) {
            this.skin.previousPage(true);
         } else if ("SelectNextPage".equals(var1)) {
            this.skin.nextPage(true);
         } else if ("TraverseOrInsertTab".equals(var1)) {
            var1 = "TraverseNext";
            var3 = false;
         } else {
            var3 = false;
         }
      }

      if (!var3) {
         super.callAction(var1);
      }

   }

   private void insertNewLine() {
      TextArea var1 = (TextArea)this.getControl();
      var1.replaceSelection("\n");
   }

   private void insertTab() {
      TextArea var1 = (TextArea)this.getControl();
      var1.replaceSelection("\t");
   }

   protected void deleteChar(boolean var1) {
      this.skin.deleteChar(var1);
   }

   protected void deleteFromLineStart() {
      TextArea var1 = (TextArea)this.getControl();
      int var2 = var1.getCaretPosition();
      if (var2 > 0) {
         this.lineStart(false, false);
         int var3 = var1.getCaretPosition();
         if (var2 > var3) {
            this.replaceText(var3, var2, "");
         }
      }

   }

   private void lineStart(boolean var1, boolean var2) {
      this.skin.lineStart(var1, var2);
   }

   private void lineEnd(boolean var1, boolean var2) {
      this.skin.lineEnd(var1, var2);
   }

   protected void scrollCharacterToVisible(int var1) {
      this.skin.scrollCharacterToVisible(var1);
   }

   protected void replaceText(int var1, int var2, String var3) {
      ((TextArea)this.getControl()).replaceText(var1, var2, var3);
   }

   public void mousePressed(MouseEvent var1) {
      TextArea var2 = (TextArea)this.getControl();
      super.mousePressed(var1);
      if (!var2.isDisabled()) {
         if (!var2.isFocused()) {
            this.focusGainedByMouseClick = true;
            var2.requestFocus();
         }

         this.setCaretAnimating(false);
         if (var1.getButton() == MouseButton.PRIMARY && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown()) {
            HitInfo var3 = this.skin.getIndex(var1.getX(), var1.getY());
            int var4 = Utils.getHitInsertionIndex(var3, var2.textProperty().getValueSafe());
            int var5 = var2.getAnchor();
            int var6 = var2.getCaretPosition();
            if (var1.getClickCount() < 2 && (var1.isSynthesized() || var5 != var6 && (var4 > var5 && var4 < var6 || var4 < var5 && var4 > var6))) {
               this.deferClick = true;
            } else if (!var1.isControlDown() && !var1.isAltDown() && !var1.isShiftDown() && !var1.isMetaDown() && !var1.isShortcutDown()) {
               switch (var1.getClickCount()) {
                  case 1:
                     this.skin.positionCaret(var3, false, false);
                     break;
                  case 2:
                     this.mouseDoubleClick(var3);
                     break;
                  case 3:
                     this.mouseTripleClick(var3);
               }
            } else if (var1.isShiftDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isMetaDown() && !var1.isShortcutDown() && var1.getClickCount() == 1) {
               this.shiftDown = true;
               if (PlatformUtil.isMac()) {
                  var2.extendSelection(var4);
               } else {
                  this.skin.positionCaret(var3, true, false);
               }
            }
         }

         if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
         }
      }

   }

   public void mouseDragged(MouseEvent var1) {
      TextArea var2 = (TextArea)this.getControl();
      if (!var2.isDisabled() && !var1.isSynthesized() && var1.getButton() == MouseButton.PRIMARY && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isShiftDown() && !var1.isMetaDown()) {
         this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), true, false);
      }

      this.deferClick = false;
   }

   public void mouseReleased(MouseEvent var1) {
      TextArea var2 = (TextArea)this.getControl();
      super.mouseReleased(var1);
      if (!var2.isDisabled()) {
         this.setCaretAnimating(false);
         if (this.deferClick) {
            this.deferClick = false;
            this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), this.shiftDown, false);
            this.shiftDown = false;
         }

         this.setCaretAnimating(true);
      }

   }

   public void contextMenuRequested(ContextMenuEvent var1) {
      TextArea var2 = (TextArea)this.getControl();
      if (this.contextMenu.isShowing()) {
         this.contextMenu.hide();
      } else if (var2.getContextMenu() == null) {
         double var3 = var1.getScreenX();
         double var5 = var1.getScreenY();
         double var7 = var1.getSceneX();
         if (IS_TOUCH_SUPPORTED) {
            Point2D var9;
            if (var2.getSelection().getLength() == 0) {
               this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), false, false);
               var9 = this.skin.getMenuPosition();
            } else {
               var9 = this.skin.getMenuPosition();
               if (var9 != null && (var9.getX() <= 0.0 || var9.getY() <= 0.0)) {
                  this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), false, false);
                  var9 = this.skin.getMenuPosition();
               }
            }

            if (var9 != null) {
               Point2D var10 = ((TextArea)this.getControl()).localToScene(var9);
               Scene var11 = ((TextArea)this.getControl()).getScene();
               Window var12 = var11.getWindow();
               Point2D var13 = new Point2D(var12.getX() + var11.getX() + var10.getX(), var12.getY() + var11.getY() + var10.getY());
               var3 = var13.getX();
               var7 = var10.getX();
               var5 = var13.getY();
            }
         }

         this.skin.populateContextMenu(this.contextMenu);
         double var17 = this.contextMenu.prefWidth(-1.0);
         double var18 = var3 - (IS_TOUCH_SUPPORTED ? var17 / 2.0 : 0.0);
         Screen var19 = com.sun.javafx.util.Utils.getScreenForPoint(var3, 0.0);
         Rectangle2D var14 = var19.getBounds();
         if (var18 < var14.getMinX()) {
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", var3);
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", var7);
            this.contextMenu.show(this.getControl(), var14.getMinX(), var5);
         } else if (var3 + var17 > var14.getMaxX()) {
            double var15 = var17 - (var14.getMaxX() - var3);
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", var3);
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", var7);
            this.contextMenu.show(this.getControl(), var3 - var15, var5);
         } else {
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
            ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
            this.contextMenu.show(this.getControl(), var18, var5);
         }
      }

      var1.consume();
   }

   protected void setCaretAnimating(boolean var1) {
      this.skin.setCaretAnimating(var1);
   }

   protected void mouseDoubleClick(HitInfo var1) {
      TextArea var2 = (TextArea)this.getControl();
      var2.previousWord();
      if (PlatformUtil.isWindows()) {
         var2.selectNextWord();
      } else {
         var2.selectEndOfNextWord();
      }

   }

   protected void mouseTripleClick(HitInfo var1) {
      this.skin.paragraphStart(false, false);
      this.skin.paragraphEnd(false, PlatformUtil.isWindows(), true);
   }

   static {
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "LineStart"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "LineEnd"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "PreviousPage"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "NextPage"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "InsertNewLine"));
      TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.TAB, KeyEvent.KEY_PRESSED, "TraverseOrInsertTab"));
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "SelectLineStart")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "SelectLineEnd")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "SelectPreviousPage")).shift());
      TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "SelectNextPage")).shift());
      if (PlatformUtil.isMac()) {
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "LineStart")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "LineStart")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "LineEnd")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "LineEnd")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "Home")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "Home")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "End")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "End")).shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend")).shift().shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend")).shift().shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend")).shift().shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend")).shift().shortcut());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend")).shortcut().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend")).shortcut().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend")).shortcut().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend")).shortcut().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart")).alt());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart")).alt());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd")).alt());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd")).alt());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart")).alt().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart")).alt().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd")).alt().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd")).alt().shift());
      } else {
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart")).ctrl());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart")).ctrl());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd")).ctrl());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd")).ctrl());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart")).ctrl().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart")).ctrl().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd")).ctrl().shift());
         TEXT_AREA_BINDINGS.add((new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd")).ctrl().shift());
      }

      TEXT_AREA_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
      TEXT_AREA_BINDINGS.add(new KeyBinding((KeyCode)null, KeyEvent.KEY_PRESSED, "Consume"));
   }
}
