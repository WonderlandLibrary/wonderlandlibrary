package com.sun.webkit.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

final class BufferData {
   private final AtomicInteger idCount = new AtomicInteger(0);
   private final HashMap strMap = new HashMap();
   private final HashMap intArrMap = new HashMap();
   private final HashMap floatArrMap = new HashMap();
   private ByteBuffer buffer;

   private int createID() {
      return this.idCount.incrementAndGet();
   }

   int addIntArray(int[] var1) {
      int var2 = this.createID();
      this.intArrMap.put(var2, var1);
      return var2;
   }

   int[] getIntArray(int var1) {
      return (int[])this.intArrMap.get(var1);
   }

   int addFloatArray(float[] var1) {
      int var2 = this.createID();
      this.floatArrMap.put(var2, var1);
      return var2;
   }

   float[] getFloatArray(int var1) {
      return (float[])this.floatArrMap.get(var1);
   }

   int addString(String var1) {
      int var2 = this.createID();
      this.strMap.put(var2, var1);
      return var2;
   }

   String getString(int var1) {
      return (String)this.strMap.get(var1);
   }

   ByteBuffer getBuffer() {
      return this.buffer;
   }

   void setBuffer(ByteBuffer var1) {
      this.buffer = var1;
   }
}
