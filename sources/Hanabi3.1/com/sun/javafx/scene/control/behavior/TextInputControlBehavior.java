package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.beans.InvalidationListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class TextInputControlBehavior extends BehaviorBase {
   protected static final List TEXT_INPUT_BINDINGS = new ArrayList();
   TextInputControl textInputControl;
   private KeyEvent lastEvent;
   private InvalidationListener textListener = (var1x) -> {
      this.invalidateBidi();
   };
   private Bidi bidi = null;
   private Boolean mixed = null;
   private Boolean rtlText = null;
   private boolean editing = false;

   public TextInputControlBehavior(TextInputControl var1, List var2) {
      super(var1, var2);
      this.textInputControl = var1;
      var1.textProperty().addListener(this.textListener);
   }

   public void dispose() {
      this.textInputControl.textProperty().removeListener(this.textListener);
      super.dispose();
   }

   protected abstract void deleteChar(boolean var1);

   protected abstract void replaceText(int var1, int var2, String var3);

   protected abstract void setCaretAnimating(boolean var1);

   protected abstract void deleteFromLineStart();

   protected void scrollCharacterToVisible(int var1) {
   }

   protected void callActionForEvent(KeyEvent var1) {
      this.lastEvent = var1;
      super.callActionForEvent(var1);
   }

   public void callAction(String var1) {
      TextInputControl var2 = (TextInputControl)this.getControl();
      boolean var3 = false;
      this.setCaretAnimating(false);
      if (var2.isEditable()) {
         this.setEditing(true);
         var3 = true;
         if ("InputCharacter".equals(var1)) {
            this.defaultKeyTyped(this.lastEvent);
         } else if ("Cut".equals(var1)) {
            this.cut();
         } else if ("Paste".equals(var1)) {
            this.paste();
         } else if ("DeleteFromLineStart".equals(var1)) {
            this.deleteFromLineStart();
         } else if ("DeletePreviousChar".equals(var1)) {
            this.deletePreviousChar();
         } else if ("DeleteNextChar".equals(var1)) {
            this.deleteNextChar();
         } else if ("DeletePreviousWord".equals(var1)) {
            this.deletePreviousWord();
         } else if ("DeleteNextWord".equals(var1)) {
            this.deleteNextWord();
         } else if ("DeleteSelection".equals(var1)) {
            this.deleteSelection();
         } else if ("Undo".equals(var1)) {
            var2.undo();
         } else if ("Redo".equals(var1)) {
            var2.redo();
         } else {
            var3 = false;
         }

         this.setEditing(false);
      }

      if (!var3) {
         var3 = true;
         if ("Copy".equals(var1)) {
            var2.copy();
         } else if ("SelectBackward".equals(var1)) {
            var2.selectBackward();
         } else if ("SelectForward".equals(var1)) {
            var2.selectForward();
         } else if ("SelectLeft".equals(var1)) {
            this.selectLeft();
         } else if ("SelectRight".equals(var1)) {
            this.selectRight();
         } else if ("PreviousWord".equals(var1)) {
            this.previousWord();
         } else if ("NextWord".equals(var1)) {
            this.nextWord();
         } else if ("LeftWord".equals(var1)) {
            this.leftWord();
         } else if ("RightWord".equals(var1)) {
            this.rightWord();
         } else if ("SelectPreviousWord".equals(var1)) {
            this.selectPreviousWord();
         } else if ("SelectNextWord".equals(var1)) {
            this.selectNextWord();
         } else if ("SelectLeftWord".equals(var1)) {
            this.selectLeftWord();
         } else if ("SelectRightWord".equals(var1)) {
            this.selectRightWord();
         } else if ("SelectWord".equals(var1)) {
            this.selectWord();
         } else if ("SelectAll".equals(var1)) {
            var2.selectAll();
         } else if ("Home".equals(var1)) {
            var2.home();
         } else if ("End".equals(var1)) {
            var2.end();
         } else if ("Forward".equals(var1)) {
            var2.forward();
         } else if ("Backward".equals(var1)) {
            var2.backward();
         } else if ("Right".equals(var1)) {
            this.nextCharacterVisually(true);
         } else if ("Left".equals(var1)) {
            this.nextCharacterVisually(false);
         } else if ("Fire".equals(var1)) {
            this.fire(this.lastEvent);
         } else if ("Cancel".equals(var1)) {
            this.cancelEdit(this.lastEvent);
         } else if ("Unselect".equals(var1)) {
            var2.deselect();
         } else if ("SelectHome".equals(var1)) {
            this.selectHome();
         } else if ("SelectEnd".equals(var1)) {
            this.selectEnd();
         } else if ("SelectHomeExtend".equals(var1)) {
            this.selectHomeExtend();
         } else if ("SelectEndExtend".equals(var1)) {
            this.selectEndExtend();
         } else if ("ToParent".equals(var1)) {
            this.forwardToParent(this.lastEvent);
         } else if ("UseVK".equals(var1) && PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
            ((TextInputControlSkin)var2.getSkin()).toggleUseVK();
         } else {
            var3 = false;
         }
      }

      this.setCaretAnimating(true);
      if (!var3) {
         if ("TraverseNext".equals(var1)) {
            this.traverseNext();
         } else if ("TraversePrevious".equals(var1)) {
            this.traversePrevious();
         } else {
            super.callAction(var1);
         }
      }

   }

   private void defaultKeyTyped(KeyEvent var1) {
      TextInputControl var2 = (TextInputControl)this.getControl();
      if (var2.isEditable() && !var2.isDisabled()) {
         String var3 = var1.getCharacter();
         if (var3.length() != 0) {
            if (!var1.isControlDown() && !var1.isAltDown() && (!PlatformUtil.isMac() || !var1.isMetaDown()) || (var1.isControlDown() || PlatformUtil.isMac()) && var1.isAltDown()) {
               if (var3.charAt(0) > 31 && var3.charAt(0) != 127 && !var1.isMetaDown()) {
                  IndexRange var4 = var2.getSelection();
                  int var5 = var4.getStart();
                  int var6 = var4.getEnd();
                  this.replaceText(var5, var6, var3);
                  this.scrollCharacterToVisible(var5);
               }

            }
         }
      }
   }

   private void invalidateBidi() {
      this.bidi = null;
      this.mixed = null;
      this.rtlText = null;
   }

   private Bidi getBidi() {
      if (this.bidi == null) {
         this.bidi = new Bidi(this.textInputControl.textProperty().getValueSafe(), this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? 1 : 0);
      }

      return this.bidi;
   }

   protected boolean isMixed() {
      if (this.mixed == null) {
         this.mixed = this.getBidi().isMixed();
      }

      return this.mixed;
   }

   protected boolean isRTLText() {
      if (this.rtlText == null) {
         Bidi var1 = this.getBidi();
         this.rtlText = var1.isRightToLeft() || this.isMixed() && this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
      }

      return this.rtlText;
   }

   private void nextCharacterVisually(boolean var1) {
      if (this.isMixed()) {
         TextInputControlSkin var2 = (TextInputControlSkin)this.textInputControl.getSkin();
         var2.nextCharacterVisually(var1);
      } else if (var1 != this.isRTLText()) {
         this.textInputControl.forward();
      } else {
         this.textInputControl.backward();
      }

   }

   private void selectLeft() {
      if (this.isRTLText()) {
         this.textInputControl.selectForward();
      } else {
         this.textInputControl.selectBackward();
      }

   }

   private void selectRight() {
      if (this.isRTLText()) {
         this.textInputControl.selectBackward();
      } else {
         this.textInputControl.selectForward();
      }

   }

   private void deletePreviousChar() {
      this.deleteChar(true);
   }

   private void deleteNextChar() {
      this.deleteChar(false);
   }

   protected void deletePreviousWord() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      int var2 = var1.getCaretPosition();
      if (var2 > 0) {
         var1.previousWord();
         int var3 = var1.getCaretPosition();
         this.replaceText(var3, var2, "");
      }

   }

   protected void deleteNextWord() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      int var2 = var1.getCaretPosition();
      if (var2 < var1.getLength()) {
         this.nextWord();
         int var3 = var1.getCaretPosition();
         this.replaceText(var2, var3, "");
      }

   }

   private void deleteSelection() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      IndexRange var2 = var1.getSelection();
      if (var2.getLength() > 0) {
         this.deleteChar(false);
      }

   }

   private void cut() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      var1.cut();
   }

   private void paste() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      var1.paste();
   }

   protected void selectPreviousWord() {
      ((TextInputControl)this.getControl()).selectPreviousWord();
   }

   protected void selectNextWord() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      if (!PlatformUtil.isMac() && !PlatformUtil.isLinux()) {
         var1.selectNextWord();
      } else {
         var1.selectEndOfNextWord();
      }

   }

   private void selectLeftWord() {
      if (this.isRTLText()) {
         this.selectNextWord();
      } else {
         this.selectPreviousWord();
      }

   }

   private void selectRightWord() {
      if (this.isRTLText()) {
         this.selectPreviousWord();
      } else {
         this.selectNextWord();
      }

   }

   protected void selectWord() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      var1.previousWord();
      if (PlatformUtil.isWindows()) {
         var1.selectNextWord();
      } else {
         var1.selectEndOfNextWord();
      }

   }

   protected void previousWord() {
      ((TextInputControl)this.getControl()).previousWord();
   }

   protected void nextWord() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      if (!PlatformUtil.isMac() && !PlatformUtil.isLinux()) {
         var1.nextWord();
      } else {
         var1.endOfNextWord();
      }

   }

   private void leftWord() {
      if (this.isRTLText()) {
         this.nextWord();
      } else {
         this.previousWord();
      }

   }

   private void rightWord() {
      if (this.isRTLText()) {
         this.previousWord();
      } else {
         this.nextWord();
      }

   }

   protected void fire(KeyEvent var1) {
   }

   protected void cancelEdit(KeyEvent var1) {
      this.forwardToParent(var1);
   }

   protected void forwardToParent(KeyEvent var1) {
      if (((TextInputControl)this.getControl()).getParent() != null) {
         ((TextInputControl)this.getControl()).getParent().fireEvent(var1);
      }

   }

   private void selectHome() {
      ((TextInputControl)this.getControl()).selectHome();
   }

   private void selectEnd() {
      ((TextInputControl)this.getControl()).selectEnd();
   }

   private void selectHomeExtend() {
      ((TextInputControl)this.getControl()).extendSelection(0);
   }

   private void selectEndExtend() {
      TextInputControl var1 = (TextInputControl)this.getControl();
      var1.extendSelection(var1.getLength());
   }

   protected void setEditing(boolean var1) {
      this.editing = var1;
   }

   public boolean isEditing() {
      return this.editing;
   }

   static {
      TEXT_INPUT_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
      TEXT_INPUT_BINDINGS.add(new KeyBinding((KeyCode)null, KeyEvent.KEY_PRESSED, "Consume"));
   }
}
