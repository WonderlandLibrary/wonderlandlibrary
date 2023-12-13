package com.sun.javafx.sg.prism;

import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.Arrays;

public class GrowableDataBuffer {
   static final int VAL_GROW_QUANTUM = 1024;
   static final int MAX_VAL_GROW = 1048576;
   static final int MIN_OBJ_GROW = 32;
   static WeakLink buflist = new WeakLink();
   byte[] vals;
   int writevalpos;
   int readvalpos;
   int savevalpos;
   Object[] objs;
   int writeobjpos;
   int readobjpos;
   int saveobjpos;

   public static GrowableDataBuffer getBuffer(int var0) {
      return getBuffer(var0, 32);
   }

   public static synchronized GrowableDataBuffer getBuffer(int var0, int var1) {
      WeakLink var2 = buflist;
      WeakLink var3 = buflist.next;

      while(var3 != null) {
         GrowableDataBuffer var4 = (GrowableDataBuffer)var3.bufref.get();
         WeakLink var5 = var3.next;
         if (var4 == null) {
            var3 = var5;
            var2.next = var5;
         } else {
            if (var4.valueCapacity() >= var0 && var4.objectCapacity() >= var1) {
               var2.next = var5;
               return var4;
            }

            var2 = var3;
            var3 = var5;
         }
      }

      return new GrowableDataBuffer(var0, var1);
   }

   public static synchronized void returnBuffer(GrowableDataBuffer var0) {
      int var1 = var0.valueCapacity();
      int var2 = var0.objectCapacity();
      var0.reset();
      WeakLink var3 = buflist;
      WeakLink var4 = buflist.next;

      while(var4 != null) {
         GrowableDataBuffer var5 = (GrowableDataBuffer)var4.bufref.get();
         WeakLink var6 = var4.next;
         if (var5 == null) {
            var4 = var6;
            var3.next = var6;
         } else {
            int var7 = var5.valueCapacity();
            int var8 = var5.objectCapacity();
            if (var7 > var1 || var7 == var1 && var8 >= var2) {
               break;
            }

            var3 = var4;
            var4 = var6;
         }
      }

      WeakLink var9 = new WeakLink();
      var9.bufref = new WeakReference(var0);
      var3.next = var9;
      var9.next = var4;
   }

   private GrowableDataBuffer(int var1, int var2) {
      this.vals = new byte[var1];
      this.objs = new Object[var2];
   }

   public int readValuePosition() {
      return this.readvalpos;
   }

   public int writeValuePosition() {
      return this.writevalpos;
   }

   public int readObjectPosition() {
      return this.readobjpos;
   }

   public int writeObjectPosition() {
      return this.writeobjpos;
   }

   public int valueCapacity() {
      return this.vals.length;
   }

   public int objectCapacity() {
      return this.objs.length;
   }

   public void save() {
      this.savevalpos = this.readvalpos;
      this.saveobjpos = this.readobjpos;
   }

   public void restore() {
      this.readvalpos = this.savevalpos;
      this.readobjpos = this.saveobjpos;
   }

   public boolean hasValues() {
      return this.readvalpos < this.writevalpos;
   }

   public boolean hasObjects() {
      return this.readobjpos < this.writeobjpos;
   }

   public boolean isEmpty() {
      return this.writevalpos == 0;
   }

   public void reset() {
      this.readvalpos = this.savevalpos = this.writevalpos = 0;
      this.readobjpos = this.saveobjpos = 0;
      if (this.writeobjpos > 0) {
         Arrays.fill(this.objs, 0, this.writeobjpos, (Object)null);
         this.writeobjpos = 0;
      }

   }

   public void append(GrowableDataBuffer var1) {
      this.ensureWriteCapacity(var1.writevalpos);
      System.arraycopy(var1.vals, 0, this.vals, this.writevalpos, var1.writevalpos);
      this.writevalpos += var1.writevalpos;
      if (this.writeobjpos + var1.writeobjpos > this.objs.length) {
         this.objs = Arrays.copyOf(this.objs, this.writeobjpos + var1.writeobjpos);
      }

      System.arraycopy(var1.objs, 0, this.objs, this.writeobjpos, var1.writeobjpos);
      this.writeobjpos += var1.writeobjpos;
   }

   private void ensureWriteCapacity(int var1) {
      if (var1 > this.vals.length - this.writevalpos) {
         var1 = this.writevalpos + var1 - this.vals.length;
         int var2 = Math.min(this.vals.length, 1048576);
         if (var2 < var1) {
            var2 = var1;
         }

         int var3 = this.vals.length + var2;
         var3 = var3 + 1023 & -1024;
         this.vals = Arrays.copyOf(this.vals, var3);
      }

   }

   private void ensureReadCapacity(int var1) {
      if (this.readvalpos + var1 > this.writevalpos) {
         throw new BufferOverflowException();
      }
   }

   public void putBoolean(boolean var1) {
      this.putByte((byte)(var1 ? 1 : 0));
   }

   public void putByte(byte var1) {
      this.ensureWriteCapacity(1);
      this.vals[this.writevalpos++] = var1;
   }

   public void putChar(char var1) {
      this.ensureWriteCapacity(2);
      this.vals[this.writevalpos++] = (byte)(var1 >> 8);
      this.vals[this.writevalpos++] = (byte)var1;
   }

   public void putShort(short var1) {
      this.ensureWriteCapacity(2);
      this.vals[this.writevalpos++] = (byte)(var1 >> 8);
      this.vals[this.writevalpos++] = (byte)var1;
   }

   public void putInt(int var1) {
      this.ensureWriteCapacity(4);
      this.vals[this.writevalpos++] = (byte)(var1 >> 24);
      this.vals[this.writevalpos++] = (byte)(var1 >> 16);
      this.vals[this.writevalpos++] = (byte)(var1 >> 8);
      this.vals[this.writevalpos++] = (byte)var1;
   }

   public void putLong(long var1) {
      this.ensureWriteCapacity(8);
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 56));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 48));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 40));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 32));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 24));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 16));
      this.vals[this.writevalpos++] = (byte)((int)(var1 >> 8));
      this.vals[this.writevalpos++] = (byte)((int)var1);
   }

   public void putFloat(float var1) {
      this.putInt(Float.floatToIntBits(var1));
   }

   public void putDouble(double var1) {
      this.putLong(Double.doubleToLongBits(var1));
   }

   public void putObject(Object var1) {
      if (this.writeobjpos >= this.objs.length) {
         this.objs = Arrays.copyOf(this.objs, this.writeobjpos + 32);
      }

      this.objs[this.writeobjpos++] = var1;
   }

   public byte peekByte(int var1) {
      if (var1 >= this.writevalpos) {
         throw new BufferOverflowException();
      } else {
         return this.vals[var1];
      }
   }

   public Object peekObject(int var1) {
      if (var1 >= this.writeobjpos) {
         throw new BufferOverflowException();
      } else {
         return this.objs[var1];
      }
   }

   public boolean getBoolean() {
      this.ensureReadCapacity(1);
      return this.vals[this.readvalpos++] != 0;
   }

   public byte getByte() {
      this.ensureReadCapacity(1);
      return this.vals[this.readvalpos++];
   }

   public int getUByte() {
      this.ensureReadCapacity(1);
      return this.vals[this.readvalpos++] & 255;
   }

   public char getChar() {
      this.ensureReadCapacity(2);
      int var1 = this.vals[this.readvalpos++];
      var1 = var1 << 8 | this.vals[this.readvalpos++] & 255;
      return (char)var1;
   }

   public short getShort() {
      this.ensureReadCapacity(2);
      int var1 = this.vals[this.readvalpos++];
      var1 = var1 << 8 | this.vals[this.readvalpos++] & 255;
      return (short)var1;
   }

   public int getInt() {
      this.ensureReadCapacity(4);
      int var1 = this.vals[this.readvalpos++];
      var1 = var1 << 8 | this.vals[this.readvalpos++] & 255;
      var1 = var1 << 8 | this.vals[this.readvalpos++] & 255;
      var1 = var1 << 8 | this.vals[this.readvalpos++] & 255;
      return var1;
   }

   public long getLong() {
      this.ensureReadCapacity(8);
      long var1 = (long)this.vals[this.readvalpos++];
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      var1 = var1 << 8 | (long)(this.vals[this.readvalpos++] & 255);
      return var1;
   }

   public float getFloat() {
      return Float.intBitsToFloat(this.getInt());
   }

   public double getDouble() {
      return Double.longBitsToDouble(this.getLong());
   }

   public Object getObject() {
      if (this.readobjpos >= this.objs.length) {
         throw new BufferOverflowException();
      } else {
         return this.objs[this.readobjpos++];
      }
   }

   static class WeakLink {
      WeakReference bufref;
      WeakLink next;
   }
}
