package com.sun.javafx.font;

public interface CompositeFontResource extends FontResource {
   FontResource getSlotResource(int var1);

   int getNumSlots();

   int getSlotForFont(String var1);
}
