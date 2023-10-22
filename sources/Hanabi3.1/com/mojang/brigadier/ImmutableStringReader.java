package com.mojang.brigadier;

public interface ImmutableStringReader {
   String getString();

   int getRemainingLength();

   int getTotalLength();

   int getCursor();

   String getRead();

   String getRemaining();

   boolean canRead(int var1);

   boolean canRead();

   char peek();

   char peek(int var1);
}
