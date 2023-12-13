package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ExceptionTable implements Cloneable {
   private ConstPool constPool;
   private ArrayList entries;

   public ExceptionTable(ConstPool var1) {
      this.constPool = var1;
      this.entries = new ArrayList();
   }

   ExceptionTable(ConstPool var1, DataInputStream var2) throws IOException {
      this.constPool = var1;
      int var3 = var2.readUnsignedShort();
      ArrayList var4 = new ArrayList(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = var2.readUnsignedShort();
         int var7 = var2.readUnsignedShort();
         int var8 = var2.readUnsignedShort();
         int var9 = var2.readUnsignedShort();
         var4.add(new ExceptionTableEntry(var6, var7, var8, var9));
      }

      this.entries = var4;
   }

   public Object clone() throws CloneNotSupportedException {
      ExceptionTable var1 = (ExceptionTable)super.clone();
      var1.entries = new ArrayList(this.entries);
      return var1;
   }

   public int size() {
      return this.entries.size();
   }

   public int startPc(int var1) {
      ExceptionTableEntry var2 = (ExceptionTableEntry)this.entries.get(var1);
      return var2.startPc;
   }

   public void setStartPc(int var1, int var2) {
      ExceptionTableEntry var3 = (ExceptionTableEntry)this.entries.get(var1);
      var3.startPc = var2;
   }

   public int endPc(int var1) {
      ExceptionTableEntry var2 = (ExceptionTableEntry)this.entries.get(var1);
      return var2.endPc;
   }

   public void setEndPc(int var1, int var2) {
      ExceptionTableEntry var3 = (ExceptionTableEntry)this.entries.get(var1);
      var3.endPc = var2;
   }

   public int handlerPc(int var1) {
      ExceptionTableEntry var2 = (ExceptionTableEntry)this.entries.get(var1);
      return var2.handlerPc;
   }

   public void setHandlerPc(int var1, int var2) {
      ExceptionTableEntry var3 = (ExceptionTableEntry)this.entries.get(var1);
      var3.handlerPc = var2;
   }

   public int catchType(int var1) {
      ExceptionTableEntry var2 = (ExceptionTableEntry)this.entries.get(var1);
      return var2.catchType;
   }

   public void setCatchType(int var1, int var2) {
      ExceptionTableEntry var3 = (ExceptionTableEntry)this.entries.get(var1);
      var3.catchType = var2;
   }

   public void add(int var1, ExceptionTable var2, int var3) {
      int var4 = var2.size();

      while(true) {
         --var4;
         if (var4 < 0) {
            return;
         }

         ExceptionTableEntry var5 = (ExceptionTableEntry)var2.entries.get(var4);
         this.add(var1, var5.startPc + var3, var5.endPc + var3, var5.handlerPc + var3, var5.catchType);
      }
   }

   public void add(int var1, int var2, int var3, int var4, int var5) {
      if (var2 < var3) {
         this.entries.add(var1, new ExceptionTableEntry(var2, var3, var4, var5));
      }

   }

   public void add(int var1, int var2, int var3, int var4) {
      if (var1 < var2) {
         this.entries.add(new ExceptionTableEntry(var1, var2, var3, var4));
      }

   }

   public void remove(int var1) {
      this.entries.remove(var1);
   }

   public ExceptionTable copy(ConstPool var1, Map var2) {
      ExceptionTable var3 = new ExceptionTable(var1);
      ConstPool var4 = this.constPool;
      int var5 = this.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         ExceptionTableEntry var7 = (ExceptionTableEntry)this.entries.get(var6);
         int var8 = var4.copy(var7.catchType, var1, var2);
         var3.add(var7.startPc, var7.endPc, var7.handlerPc, var8);
      }

      return var3;
   }

   void shiftPc(int var1, int var2, boolean var3) {
      int var4 = this.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         ExceptionTableEntry var6 = (ExceptionTableEntry)this.entries.get(var5);
         var6.startPc = shiftPc(var6.startPc, var1, var2, var3);
         var6.endPc = shiftPc(var6.endPc, var1, var2, var3);
         var6.handlerPc = shiftPc(var6.handlerPc, var1, var2, var3);
      }

   }

   private static int shiftPc(int var0, int var1, int var2, boolean var3) {
      if (var0 > var1 || var3 && var0 == var1) {
         var0 += var2;
      }

      return var0;
   }

   void write(DataOutputStream var1) throws IOException {
      int var2 = this.size();
      var1.writeShort(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         ExceptionTableEntry var4 = (ExceptionTableEntry)this.entries.get(var3);
         var1.writeShort(var4.startPc);
         var1.writeShort(var4.endPc);
         var1.writeShort(var4.handlerPc);
         var1.writeShort(var4.catchType);
      }

   }
}
