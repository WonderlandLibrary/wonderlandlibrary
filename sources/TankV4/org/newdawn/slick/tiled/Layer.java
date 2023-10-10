package org.newdawn.slick.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Layer {
   private static byte[] baseCodes = new byte[256];
   private final TiledMap map;
   public int index;
   public String name;
   public int[][][] data;
   public int width;
   public int height;
   public Properties props;

   public Layer(TiledMap var1, Element var2) throws SlickException {
      this.map = var1;
      this.name = var2.getAttribute("name");
      this.width = Integer.parseInt(var2.getAttribute("width"));
      this.height = Integer.parseInt(var2.getAttribute("height"));
      this.data = new int[this.width][this.height][3];
      Element var3 = (Element)var2.getElementsByTagName("properties").item(0);
      if (var3 != null) {
         NodeList var4 = var3.getElementsByTagName("property");
         if (var4 != null) {
            this.props = new Properties();

            for(int var5 = 0; var5 < var4.getLength(); ++var5) {
               Element var6 = (Element)var4.item(var5);
               String var7 = var6.getAttribute("name");
               String var8 = var6.getAttribute("value");
               this.props.setProperty(var7, var8);
            }
         }
      }

      Element var16 = (Element)var2.getElementsByTagName("data").item(0);
      String var17 = var16.getAttribute("encoding");
      String var18 = var16.getAttribute("compression");
      if (var17.equals("base64") && var18.equals("gzip")) {
         try {
            Node var19 = var16.getFirstChild();
            char[] var20 = var19.getNodeValue().trim().toCharArray();
            byte[] var9 = this.decodeBase64(var20);
            GZIPInputStream var10 = new GZIPInputStream(new ByteArrayInputStream(var9));

            for(int var11 = 0; var11 < this.height; ++var11) {
               for(int var12 = 0; var12 < this.width; ++var12) {
                  byte var13 = 0;
                  int var21 = var13 | var10.read();
                  var21 |= var10.read() << 8;
                  var21 |= var10.read() << 16;
                  var21 |= var10.read() << 24;
                  if (var21 == 0) {
                     this.data[var12][var11][0] = -1;
                     this.data[var12][var11][1] = 0;
                     this.data[var12][var11][2] = 0;
                  } else {
                     TileSet var14 = var1.findTileSet(var21);
                     if (var14 != null) {
                        this.data[var12][var11][0] = var14.index;
                        this.data[var12][var11][1] = var21 - var14.firstGID;
                     }

                     this.data[var12][var11][2] = var21;
                  }
               }
            }

         } catch (IOException var15) {
            Log.error((Throwable)var15);
            throw new SlickException("Unable to decode base 64 block");
         }
      } else {
         throw new SlickException("Unsupport tiled map type: " + var17 + "," + var18 + " (only gzip base64 supported)");
      }
   }

   public int getTileID(int var1, int var2) {
      return this.data[var1][var2][2];
   }

   public void setTileID(int var1, int var2, int var3) {
      if (var3 == 0) {
         this.data[var1][var2][0] = -1;
         this.data[var1][var2][1] = 0;
         this.data[var1][var2][2] = 0;
      } else {
         TileSet var4 = this.map.findTileSet(var3);
         this.data[var1][var2][0] = var4.index;
         this.data[var1][var2][1] = var3 - var4.firstGID;
         this.data[var1][var2][2] = var3;
      }

   }

   public void render(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, int var8, int var9) {
      for(int var10 = 0; var10 < this.map.getTileSetCount(); ++var10) {
         TileSet var11 = null;

         for(int var12 = 0; var12 < var5; ++var12) {
            if (var3 + var12 >= 0 && var4 + var6 >= 0 && var3 + var12 < this.width && var4 + var6 < this.height && this.data[var3 + var12][var4 + var6][0] == var10) {
               if (var11 == null) {
                  var11 = this.map.getTileSet(var10);
                  var11.tiles.startUse();
               }

               int var13 = var11.getTileX(this.data[var3 + var12][var4 + var6][1]);
               int var14 = var11.getTileY(this.data[var3 + var12][var4 + var6][1]);
               int var15 = var11.tileHeight - var9;
               var11.tiles.renderInUse(var1 + var12 * var8, var2 + var6 * var9 - var15, var13, var14);
            }
         }

         if (var7) {
            if (var11 != null) {
               var11.tiles.endUse();
               var11 = null;
            }

            this.map.renderedLine(var6, var6 + var4, this.index);
         }

         if (var11 != null) {
            var11.tiles.endUse();
         }
      }

   }

   private byte[] decodeBase64(char[] var1) {
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3] > 255 || baseCodes[var1[var3]] < 0) {
            --var2;
         }
      }

      var3 = var2 / 4 * 3;
      if (var2 % 4 == 3) {
         var3 += 2;
      }

      if (var2 % 4 == 2) {
         ++var3;
      }

      byte[] var4 = new byte[var3];
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;

      for(int var8 = 0; var8 < var1.length; ++var8) {
         byte var9 = var1[var8] > 255 ? -1 : baseCodes[var1[var8]];
         if (var9 >= 0) {
            var6 <<= 6;
            var5 += 6;
            var6 |= var9;
            if (var5 >= 8) {
               var5 -= 8;
               var4[var7++] = (byte)(var6 >> var5 & 255);
            }
         }
      }

      if (var7 != var4.length) {
         throw new RuntimeException("Data length appears to be wrong (wrote " + var7 + " should be " + var4.length + ")");
      } else {
         return var4;
      }
   }

   static {
      int var0;
      for(var0 = 0; var0 < 256; ++var0) {
         baseCodes[var0] = -1;
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         baseCodes[var0] = (byte)(var0 - 65);
      }

      for(var0 = 97; var0 <= 122; ++var0) {
         baseCodes[var0] = (byte)(26 + var0 - 97);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         baseCodes[var0] = (byte)(52 + var0 - 48);
      }

      baseCodes[43] = 62;
      baseCodes[47] = 63;
   }
}
