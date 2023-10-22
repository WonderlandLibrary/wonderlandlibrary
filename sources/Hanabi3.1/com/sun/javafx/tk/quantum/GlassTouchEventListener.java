package com.sun.javafx.tk.quantum;

interface GlassTouchEventListener {
   void notifyBeginTouchEvent(long var1, int var3, boolean var4, int var5);

   void notifyNextTouchEvent(long var1, int var3, long var4, int var6, int var7, int var8, int var9);

   void notifyEndTouchEvent(long var1);
}
