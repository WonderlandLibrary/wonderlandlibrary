package javafx.scene.web;

import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.print.PrinterJob;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class HTMLEditor extends Control {
   public HTMLEditor() {
      ((StyleableProperty)super.skinClassNameProperty()).applyStyle((StyleOrigin)null, "com.sun.javafx.scene.web.skin.HTMLEditorSkin");
      this.getStyleClass().add("html-editor");
   }

   protected Skin createDefaultSkin() {
      return new HTMLEditorSkin(this);
   }

   public String getHtmlText() {
      return ((HTMLEditorSkin)this.getSkin()).getHTMLText();
   }

   public void setHtmlText(String var1) {
      ((HTMLEditorSkin)this.getSkin()).setHTMLText(var1);
   }

   public void print(PrinterJob var1) {
      ((HTMLEditorSkin)this.getSkin()).print(var1);
   }
}
