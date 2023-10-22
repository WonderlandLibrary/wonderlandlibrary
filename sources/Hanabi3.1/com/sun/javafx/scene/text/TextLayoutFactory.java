package com.sun.javafx.scene.text;

public interface TextLayoutFactory {
   TextLayout createLayout();

   TextLayout getLayout();

   void disposeLayout(TextLayout var1);
}
