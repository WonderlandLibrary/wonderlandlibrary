package javafx.embed.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.input.DataFormat;

final class DataFlavorUtils {
   static String getFxMimeType(DataFlavor var0) {
      return var0.getPrimaryType() + "/" + var0.getSubType();
   }

   static DataFlavor[] getDataFlavors(String[] var0) {
      ArrayList var1 = new ArrayList(var0.length);
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         DataFlavor var6 = null;

         try {
            var6 = new DataFlavor(var5);
         } catch (ClassNotFoundException var8) {
            continue;
         }

         var1.add(var6);
      }

      return (DataFlavor[])var1.toArray(new DataFlavor[0]);
   }

   static DataFlavor getDataFlavor(DataFormat var0) {
      DataFlavor[] var1 = getDataFlavors((String[])var0.getIdentifiers().toArray(new String[1]));
      return var1.length == 0 ? null : var1[0];
   }

   static String getMimeType(DataFormat var0) {
      Iterator var1 = var0.getIdentifiers().iterator();
      if (var1.hasNext()) {
         String var2 = (String)var1.next();
         return var2;
      } else {
         return null;
      }
   }

   static DataFormat getDataFormat(DataFlavor var0) {
      String var1 = getFxMimeType(var0);
      DataFormat var2 = DataFormat.lookupMimeType(var1);
      if (var2 == null) {
         var2 = new DataFormat(new String[]{var1});
      }

      return var2;
   }

   static Object adjustFxData(DataFlavor var0, Object var1) throws UnsupportedEncodingException {
      if (var1 instanceof String) {
         if (var0.isRepresentationClassInputStream()) {
            String var2 = var0.getParameter("charset");
            return new ByteArrayInputStream(var2 != null ? ((String)var1).getBytes(var2) : ((String)var1).getBytes());
         }

         if (var0.isRepresentationClassByteBuffer()) {
         }
      }

      return var1 instanceof ByteBuffer && var0.isRepresentationClassInputStream() ? new ByteBufferInputStream((ByteBuffer)var1) : var1;
   }

   static Object adjustSwingData(DataFlavor var0, String var1, Object var2) {
      if (var2 == null) {
         return var2;
      } else if (var0.isFlavorJavaFileListType()) {
         List var9 = (List)var2;
         String[] var10 = new String[var9.size()];
         int var11 = 0;

         File var13;
         for(Iterator var12 = var9.iterator(); var12.hasNext(); var10[var11++] = var13.getPath()) {
            var13 = (File)var12.next();
         }

         return var10;
      } else {
         DataFormat var3 = DataFormat.lookupMimeType(var1);
         if (DataFormat.PLAIN_TEXT.equals(var3)) {
            if (var0.isFlavorTextType()) {
               if (var2 instanceof InputStream) {
                  InputStream var4 = (InputStream)var2;
                  ByteArrayOutputStream var5 = new ByteArrayOutputStream();
                  byte[] var6 = new byte[64];

                  try {
                     for(int var7 = var4.read(var6); var7 != -1; var7 = var4.read(var6)) {
                        var5.write(var6, 0, var7);
                     }

                     var5.close();
                     return new String(var5.toByteArray());
                  } catch (Exception var8) {
                  }
               }
            } else if (var2 != null) {
               return var2.toString();
            }
         }

         return var2;
      }
   }

   static Map adjustSwingDataFlavors(DataFlavor[] var0) {
      HashMap var1 = new HashMap(var0.length);
      DataFlavor[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         DataFlavor var5 = var2[var4];
         String var6 = getFxMimeType(var5);
         if (var1.containsKey(var6)) {
            Set var7 = (Set)var1.get(var6);

            try {
               var7.add(var5);
            } catch (UnsupportedOperationException var9) {
            }
         } else {
            Object var14 = new HashSet();
            if (var5.isFlavorTextType()) {
               ((Set)var14).add(DataFlavor.stringFlavor);
               var14 = Collections.unmodifiableSet((Set)var14);
            } else {
               ((Set)var14).add(var5);
            }

            var1.put(var6, var14);
         }
      }

      HashMap var10 = new HashMap();
      Iterator var11 = var1.keySet().iterator();

      while(var11.hasNext()) {
         String var12 = (String)var11.next();
         DataFlavor[] var13 = (DataFlavor[])((Set)var1.get(var12)).toArray(new DataFlavor[0]);
         if (var13.length == 1) {
            var10.put(var12, var13[0]);
         } else {
            var10.put(var12, var13[0]);
         }
      }

      return var10;
   }

   private static Object readData(Transferable var0, DataFlavor var1) {
      Object var2 = null;

      try {
         var2 = var0.getTransferData(var1);
      } catch (UnsupportedFlavorException var4) {
         var4.printStackTrace(System.err);
      } catch (IOException var5) {
         var5.printStackTrace(System.err);
      }

      return var2;
   }

   static Map readAllData(Transferable var0, Map var1, boolean var2) {
      HashMap var3 = new HashMap();
      DataFlavor[] var4 = var0.getTransferDataFlavors();
      int var5 = var4.length;

      DataFlavor var7;
      Object var8;
      for(int var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         var8 = var2 ? readData(var0, var7) : null;
         if (var8 != null || !var2) {
            String var9 = getFxMimeType(var7);
            var8 = adjustSwingData(var7, var9, var8);
            var3.put(var9, var8);
         }
      }

      Iterator var10 = var1.entrySet().iterator();

      while(true) {
         Map.Entry var11;
         String var12;
         do {
            if (!var10.hasNext()) {
               return var3;
            }

            var11 = (Map.Entry)var10.next();
            var12 = (String)var11.getKey();
            var7 = (DataFlavor)var11.getValue();
            var8 = var2 ? readData(var0, var7) : null;
         } while(var8 == null && var2);

         var8 = adjustSwingData(var7, var12, var8);
         var3.put(var11.getKey(), var8);
      }
   }

   private static class ByteBufferInputStream extends InputStream {
      private final ByteBuffer bb;

      private ByteBufferInputStream(ByteBuffer var1) {
         this.bb = var1;
      }

      public int available() {
         return this.bb.remaining();
      }

      public int read() throws IOException {
         return !this.bb.hasRemaining() ? -1 : this.bb.get() & 255;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (!this.bb.hasRemaining()) {
            return -1;
         } else {
            var3 = Math.min(var3, this.bb.remaining());
            this.bb.get(var1, var2, var3);
            return var3;
         }
      }

      // $FF: synthetic method
      ByteBufferInputStream(ByteBuffer var1, Object var2) {
         this(var1);
      }
   }
}
