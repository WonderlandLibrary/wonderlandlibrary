package org.lwjgl.util;

public interface WritableDimension {
   void setSize(int var1, int var2);

   void setSize(ReadableDimension var1);

   void setHeight(int var1);

   void setWidth(int var1);
}
