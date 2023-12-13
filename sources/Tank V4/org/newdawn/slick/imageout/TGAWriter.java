package org.newdawn.slick.imageout;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class TGAWriter implements ImageWriter {
   private static short flipEndian(short var0) {
      int var1 = var0 & '\uffff';
      return (short)(var1 << 8 | (var1 & '\uff00') >>> 8);
   }

   public void saveImage(Image var1, String var2, OutputStream var3, boolean var4) throws IOException {
      DataOutputStream var5 = new DataOutputStream(new BufferedOutputStream(var3));
      var5.writeByte(0);
      var5.writeByte(0);
      var5.writeByte(2);
      var5.writeShort(flipEndian((short)0));
      var5.writeShort(flipEndian((short)0));
      var5.writeByte(0);
      var5.writeShort(flipEndian((short)0));
      var5.writeShort(flipEndian((short)0));
      var5.writeShort(flipEndian((short)var1.getWidth()));
      var5.writeShort(flipEndian((short)var1.getHeight()));
      if (var4) {
         var5.writeByte(32);
         var5.writeByte(1);
      } else {
         var5.writeByte(24);
         var5.writeByte(0);
      }

      for(int var7 = 0; var7 < var1.getHeight(); ++var7) {
         for(int var8 = 0; var8 < var1.getWidth(); ++var8) {
            Color var6 = var1.getColor(var8, var7);
            var5.writeByte((byte)((int)(var6.b * 255.0F)));
            var5.writeByte((byte)((int)(var6.g * 255.0F)));
            var5.writeByte((byte)((int)(var6.r * 255.0F)));
            if (var4) {
               var5.writeByte((byte)((int)(var6.a * 255.0F)));
            }
         }
      }

      var5.close();
   }
}
