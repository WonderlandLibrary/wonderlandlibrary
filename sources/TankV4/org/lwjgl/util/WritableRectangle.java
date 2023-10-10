package org.lwjgl.util;

public interface WritableRectangle extends WritablePoint, WritableDimension {
   void setBounds(int var1, int var2, int var3, int var4);

   void setBounds(ReadablePoint var1, ReadableDimension var2);

   void setBounds(ReadableRectangle var1);
}
