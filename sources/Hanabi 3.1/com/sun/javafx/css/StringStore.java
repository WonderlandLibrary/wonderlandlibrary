package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringStore {
   private final Map stringMap = new HashMap();
   public final List strings = new ArrayList();

   public int addString(String var1) {
      Integer var2 = (Integer)this.stringMap.get(var1);
      if (var2 == null) {
         var2 = this.strings.size();
         this.strings.add(var1);
         this.stringMap.put(var1, var2);
      }

      return var2;
   }

   public void writeBinary(DataOutputStream var1) throws IOException {
      var1.writeShort(this.strings.size());
      if (this.stringMap.containsKey((Object)null)) {
         Integer var2 = (Integer)this.stringMap.get((Object)null);
         var1.writeShort(var2);
      } else {
         var1.writeShort(-1);
      }

      for(int var4 = 0; var4 < this.strings.size(); ++var4) {
         String var3 = (String)this.strings.get(var4);
         if (var3 != null) {
            var1.writeUTF(var3);
         }
      }

   }

   static String[] readBinary(DataInputStream var0) throws IOException {
      short var1 = var0.readShort();
      short var2 = var0.readShort();
      String[] var3 = new String[var1];
      Arrays.fill(var3, (Object)null);

      for(int var4 = 0; var4 < var1; ++var4) {
         if (var4 != var2) {
            var3[var4] = var0.readUTF();
         }
      }

      return var3;
   }
}
