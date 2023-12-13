package com.sun.prism;

import com.sun.glass.ui.Screen;

public interface RenderTarget extends Surface {
   Screen getAssociatedScreen();

   Graphics createGraphics();

   boolean isOpaque();

   void setOpaque(boolean var1);

   boolean isMSAA();
}
