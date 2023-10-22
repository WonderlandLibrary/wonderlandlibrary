package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.text.HitInfo;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordFieldBehavior extends TextFieldBehavior {
   public PasswordFieldBehavior(PasswordField var1) {
      super(var1);
   }

   protected void deletePreviousWord() {
   }

   protected void deleteNextWord() {
   }

   protected void selectPreviousWord() {
   }

   protected void selectNextWord() {
   }

   protected void previousWord() {
   }

   protected void nextWord() {
   }

   protected void selectWord() {
      ((TextField)this.getControl()).selectAll();
   }

   protected void mouseDoubleClick(HitInfo var1) {
      ((TextField)this.getControl()).selectAll();
   }
}
