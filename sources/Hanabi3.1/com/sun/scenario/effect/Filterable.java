package com.sun.scenario.effect;

public interface Filterable extends LockableResource {
   Object getData();

   int getContentWidth();

   int getContentHeight();

   void setContentWidth(int var1);

   void setContentHeight(int var1);

   int getMaxContentWidth();

   int getMaxContentHeight();

   int getPhysicalWidth();

   int getPhysicalHeight();

   float getPixelScale();

   void flush();
}
