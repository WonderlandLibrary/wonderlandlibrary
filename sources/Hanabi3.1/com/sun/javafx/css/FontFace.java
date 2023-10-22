package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FontFace {
   private final Map descriptors;
   private final List sources;

   public FontFace(Map var1, List var2) {
      this.descriptors = var1;
      this.sources = var2;
   }

   public Map getDescriptors() {
      return this.descriptors;
   }

   public List getSources() {
      return this.sources;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("@font-face { ");
      Iterator var2 = this.descriptors.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.append((String)var3.getKey());
         var1.append(" : ");
         var1.append((String)var3.getValue());
         var1.append("; ");
      }

      var1.append("src : ");
      var2 = this.sources.iterator();

      while(var2.hasNext()) {
         FontFaceSrc var4 = (FontFaceSrc)var2.next();
         var1.append(var4.getType());
         var1.append(" \"");
         var1.append(var4.getSrc());
         var1.append("\", ");
      }

      var1.append("; ");
      var1.append(" }");
      return var1.toString();
   }

   final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      Set var3 = this.getDescriptors() != null ? this.getDescriptors().entrySet() : null;
      int var4 = var3 != null ? var3.size() : 0;
      var1.writeShort(var4);
      if (var3 != null) {
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            int var7 = var2.addString((String)var6.getKey());
            var1.writeInt(var7);
            var7 = var2.addString((String)var6.getValue());
            var1.writeInt(var7);
         }
      }

      List var8 = this.getSources();
      var4 = var8 != null ? var8.size() : 0;
      var1.writeShort(var4);

      for(int var9 = 0; var9 < var4; ++var9) {
         FontFaceSrc var10 = (FontFaceSrc)var8.get(var9);
         var10.writeBinary(var1, var2);
      }

   }

   static final FontFace readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
      short var3 = var1.readShort();
      HashMap var4 = new HashMap(var3);

      int var6;
      for(int var5 = 0; var5 < var3; ++var5) {
         var6 = var1.readInt();
         String var7 = var2[var6];
         var6 = var1.readInt();
         String var8 = var2[var6];
         var4.put(var7, var8);
      }

      var3 = var1.readShort();
      ArrayList var9 = new ArrayList(var3);

      for(var6 = 0; var6 < var3; ++var6) {
         FontFaceSrc var10 = FontFace.FontFaceSrc.readBinary(var0, var1, var2);
         var9.add(var10);
      }

      return new FontFace(var4, var9);
   }

   public static class FontFaceSrc {
      private final FontFaceSrcType type;
      private final String src;
      private final String format;

      public FontFaceSrc(FontFaceSrcType var1, String var2, String var3) {
         this.type = var1;
         this.src = var2;
         this.format = var3;
      }

      public FontFaceSrc(FontFaceSrcType var1, String var2) {
         this.type = var1;
         this.src = var2;
         this.format = null;
      }

      public FontFaceSrcType getType() {
         return this.type;
      }

      public String getSrc() {
         return this.src;
      }

      public String getFormat() {
         return this.format;
      }

      final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
         var1.writeInt(var2.addString(this.type.name()));
         var1.writeInt(var2.addString(this.src));
         var1.writeInt(var2.addString(this.format));
      }

      static final FontFaceSrc readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
         int var3 = var1.readInt();
         FontFaceSrcType var4 = var2[var3] != null ? FontFace.FontFaceSrcType.valueOf(var2[var3]) : null;
         var3 = var1.readInt();
         String var5 = var2[var3];
         var3 = var1.readInt();
         String var6 = var2[var3];
         return new FontFaceSrc(var4, var5, var6);
      }
   }

   public static enum FontFaceSrcType {
      URL,
      LOCAL,
      REFERENCE;
   }
}
