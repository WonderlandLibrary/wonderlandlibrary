package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

final class QuantumClipboard implements TKClipboard {
   private ClipboardAssistance systemAssistant;
   private AccessControlContext accessContext = null;
   private boolean isCaching;
   private List dataCache;
   private Set transferModesCache;
   private Image dragImage = null;
   private double dragOffsetX = 0.0;
   private double dragOffsetY = 0.0;
   private static ClipboardAssistance currentDragboard;
   private static final Pattern findTagIMG = Pattern.compile("IMG\\s+SRC=\\\"([^\\\"]+)\\\"", 34);

   private QuantumClipboard() {
   }

   public void setSecurityContext(AccessControlContext var1) {
      if (this.accessContext != null) {
         throw new RuntimeException("Clipboard security context has been already set!");
      } else {
         this.accessContext = var1;
      }
   }

   private AccessControlContext getAccessControlContext() {
      if (this.accessContext == null) {
         throw new RuntimeException("Clipboard security context has not been set!");
      } else {
         return this.accessContext;
      }
   }

   public static QuantumClipboard getClipboardInstance(ClipboardAssistance var0) {
      QuantumClipboard var1 = new QuantumClipboard();
      var1.systemAssistant = var0;
      var1.isCaching = false;
      return var1;
   }

   static ClipboardAssistance getCurrentDragboard() {
      return currentDragboard;
   }

   static void releaseCurrentDragboard() {
      currentDragboard = null;
   }

   public static QuantumClipboard getDragboardInstance(ClipboardAssistance var0, boolean var1) {
      QuantumClipboard var2 = new QuantumClipboard();
      var2.systemAssistant = var0;
      var2.isCaching = true;
      if (var1) {
         currentDragboard = var0;
      }

      return var2;
   }

   public static int transferModesToClipboardActions(Set var0) {
      int var1 = 0;
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         TransferMode var3 = (TransferMode)var2.next();
         switch (var3) {
            case COPY:
               var1 |= 1;
               break;
            case MOVE:
               var1 |= 2;
               break;
            case LINK:
               var1 |= 1073741824;
               break;
            default:
               throw new IllegalArgumentException("unsupported TransferMode " + var0);
         }
      }

      return var1;
   }

   public void setSupportedTransferMode(Set var1) {
      if (this.isCaching) {
         this.transferModesCache = var1;
      }

      int var2 = transferModesToClipboardActions(var1);
      this.systemAssistant.setSupportedActions(var2);
   }

   public static Set clipboardActionsToTransferModes(int var0) {
      EnumSet var1 = EnumSet.noneOf(TransferMode.class);
      if ((var0 & 1) != 0) {
         var1.add(TransferMode.COPY);
      }

      if ((var0 & 2) != 0) {
         var1.add(TransferMode.MOVE);
      }

      if ((var0 & 1073741824) != 0) {
         var1.add(TransferMode.LINK);
      }

      return var1;
   }

   public Set getTransferModes() {
      if (this.transferModesCache != null) {
         return EnumSet.copyOf(this.transferModesCache);
      } else {
         ClipboardAssistance var1 = currentDragboard != null ? currentDragboard : this.systemAssistant;
         Set var2 = clipboardActionsToTransferModes(var1.getSupportedSourceActions());
         return var2;
      }
   }

   public void setDragView(Image var1) {
      this.dragImage = var1;
   }

   public void setDragViewOffsetX(double var1) {
      this.dragOffsetX = var1;
   }

   public void setDragViewOffsetY(double var1) {
      this.dragOffsetY = var1;
   }

   public Image getDragView() {
      return this.dragImage;
   }

   public double getDragViewOffsetX() {
      return this.dragOffsetX;
   }

   public double getDragViewOffsetY() {
      return this.dragOffsetY;
   }

   public void close() {
      this.systemAssistant.close();
   }

   public void flush() {
      if (this.isCaching) {
         this.putContentToPeer((Pair[])this.dataCache.toArray(new Pair[0]));
      }

      this.clearCache();
      this.clearDragView();
      this.systemAssistant.flush();
   }

   public Object getContent(DataFormat var1) {
      if (this.dataCache != null) {
         Iterator var11 = this.dataCache.iterator();

         Pair var13;
         do {
            if (!var11.hasNext()) {
               return null;
            }

            var13 = (Pair)var11.next();
         } while(var13.getKey() != var1);

         return var13.getValue();
      } else {
         ClipboardAssistance var2 = currentDragboard != null ? currentDragboard : this.systemAssistant;
         if (var1 == DataFormat.IMAGE) {
            return this.readImage();
         } else if (var1 == DataFormat.URL) {
            return var2.getData("text/uri-list");
         } else if (var1 != DataFormat.FILES) {
            Iterator var12 = var1.getIdentifiers().iterator();

            Object var15;
            do {
               if (!var12.hasNext()) {
                  return null;
               }

               String var14 = (String)var12.next();
               var15 = var2.getData(var14);
               if (var15 instanceof ByteBuffer) {
                  try {
                     ByteBuffer var16 = (ByteBuffer)var15;
                     ByteArrayInputStream var7 = new ByteArrayInputStream(var16.array());
                     ObjectInputStream var8 = new ObjectInputStream(var7) {
                        protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
                           return Class.forName(var1.getName(), false, Thread.currentThread().getContextClassLoader());
                        }
                     };
                     var15 = var8.readObject();
                  } catch (IOException var9) {
                  } catch (ClassNotFoundException var10) {
                  }
               }
            } while(var15 == null);

            return var15;
         } else {
            Object var3 = var2.getData("application/x-java-file-list");
            if (var3 == null) {
               return Collections.emptyList();
            } else {
               String[] var4 = (String[])((String[])var3);
               ArrayList var5 = new ArrayList(var4.length);

               for(int var6 = 0; var6 < var4.length; ++var6) {
                  var5.add(new File(var4[var6]));
               }

               return var5;
            }
         }
      }
   }

   private static Image convertObjectToImage(Object var0) {
      if (var0 instanceof Image) {
         return (Image)var0;
      } else {
         Pixels var1;
         if (var0 instanceof ByteBuffer) {
            ByteBuffer var2 = (ByteBuffer)var0;

            try {
               var2.rewind();
               int var3 = var2.getInt();
               int var4 = var2.getInt();
               var1 = Application.GetApplication().createPixels(var3, var4, var2.slice());
            } catch (Exception var5) {
               return null;
            }
         } else {
            if (!(var0 instanceof Pixels)) {
               return null;
            }

            var1 = (Pixels)var0;
         }

         com.sun.prism.Image var6 = PixelUtils.pixelsToImage(var1);
         ImageLoader var7 = Toolkit.getToolkit().loadPlatformImage(var6);
         return Image.impl_fromPlatformImage(var7);
      }
   }

   private Image readImage() {
      ClipboardAssistance var1 = currentDragboard != null ? currentDragboard : this.systemAssistant;
      Object var2 = var1.getData("application/x-java-rawimage");
      if (var2 != null) {
         return convertObjectToImage(var2);
      } else {
         Object var3 = var1.getData("text/html");
         if (var3 != null) {
            String var4 = this.parseIMG(var3);
            if (var4 != null) {
               try {
                  SecurityManager var5 = System.getSecurityManager();
                  if (var5 != null) {
                     AccessControlContext var6 = this.getAccessControlContext();
                     URL var7 = new URL(var4);
                     String var8 = var7.getProtocol();
                     if (var8.equalsIgnoreCase("jar")) {
                        String var9 = var7.getFile();
                        var7 = new URL(var9);
                        var8 = var7.getProtocol();
                     }

                     if (var8.equalsIgnoreCase("file")) {
                        FilePermission var14 = new FilePermission(var7.getFile(), "read");
                        var5.checkPermission(var14, var6);
                     } else if (!var8.equalsIgnoreCase("ftp") && !var8.equalsIgnoreCase("http") && !var8.equalsIgnoreCase("https")) {
                        Permission var16 = PermissionHelper.getAccessClipboardPermission();
                        var5.checkPermission(var16, var6);
                     } else {
                        int var15 = var7.getPort();
                        String var10 = var15 == -1 ? var7.getHost() : var7.getHost() + ":" + var15;
                        SocketPermission var11 = new SocketPermission(var10, "connect");
                        var5.checkPermission(var11, var6);
                     }
                  }

                  return new Image(var4);
               } catch (MalformedURLException var12) {
                  return null;
               } catch (SecurityException var13) {
                  return null;
               }
            }
         }

         return null;
      }
   }

   private String parseIMG(Object var1) {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof String)) {
         return null;
      } else {
         String var2 = (String)var1;
         Matcher var3 = findTagIMG.matcher(var2);
         return var3.find() ? var3.group(1) : null;
      }
   }

   private boolean placeImage(Image var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.impl_getUrl();
         if (var2 != null && !PixelUtils.supportedFormatType(var2)) {
            this.systemAssistant.setData("text/uri-list", var2);
            return true;
         } else {
            com.sun.prism.Image var3 = (com.sun.prism.Image)var1.impl_getPlatformImage();
            Pixels var4 = PixelUtils.imageToPixels(var3);
            if (var4 != null) {
               this.systemAssistant.setData("application/x-java-rawimage", var4);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public Set getContentTypes() {
      HashSet var1 = new HashSet();
      if (this.dataCache == null) {
         ClipboardAssistance var10 = currentDragboard != null ? currentDragboard : this.systemAssistant;
         String[] var11 = var10.getMimeTypes();
         if (var11 == null) {
            return var1;
         } else {
            String[] var4 = var11;
            int var5 = var11.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (var7.equalsIgnoreCase("application/x-java-rawimage")) {
                  var1.add(DataFormat.IMAGE);
               } else if (var7.equalsIgnoreCase("text/uri-list")) {
                  var1.add(DataFormat.URL);
               } else if (var7.equalsIgnoreCase("application/x-java-file-list")) {
                  var1.add(DataFormat.FILES);
               } else if (var7.equalsIgnoreCase("text/html")) {
                  var1.add(DataFormat.HTML);

                  try {
                     if (this.parseIMG(var10.getData("text/html")) != null) {
                        var1.add(DataFormat.IMAGE);
                     }
                  } catch (Exception var9) {
                  }
               } else {
                  DataFormat var8 = DataFormat.lookupMimeType(var7);
                  if (var8 == null) {
                     var8 = new DataFormat(new String[]{var7});
                  }

                  var1.add(var8);
               }
            }

            return var1;
         }
      } else {
         Iterator var2 = this.dataCache.iterator();

         while(var2.hasNext()) {
            Pair var3 = (Pair)var2.next();
            var1.add(var3.getKey());
         }

         return var1;
      }
   }

   public boolean hasContent(DataFormat var1) {
      if (this.dataCache != null) {
         Iterator var9 = this.dataCache.iterator();

         Pair var10;
         do {
            if (!var9.hasNext()) {
               return false;
            }

            var10 = (Pair)var9.next();
         } while(var10.getKey() != var1);

         return true;
      } else {
         ClipboardAssistance var2 = currentDragboard != null ? currentDragboard : this.systemAssistant;
         String[] var3 = var2.getMimeTypes();
         if (var3 == null) {
            return false;
         } else {
            String[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (var1 == DataFormat.IMAGE && var7.equalsIgnoreCase("application/x-java-rawimage")) {
                  return true;
               }

               if (var1 == DataFormat.URL && var7.equalsIgnoreCase("text/uri-list")) {
                  return true;
               }

               if (var1 == DataFormat.IMAGE && var7.equalsIgnoreCase("text/html") && this.parseIMG(var2.getData("text/html")) != null) {
                  return true;
               }

               if (var1 == DataFormat.FILES && var7.equalsIgnoreCase("application/x-java-file-list")) {
                  return true;
               }

               DataFormat var8 = DataFormat.lookupMimeType(var7);
               if (var8 != null && var8.equals(var1)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static ByteBuffer prepareImage(Image var0) {
      PixelReader var1 = var0.getPixelReader();
      int var2 = (int)var0.getWidth();
      int var3 = (int)var0.getHeight();
      byte[] var4 = new byte[var2 * var3 * 4];
      var1.getPixels(0, 0, var2, var3, WritablePixelFormat.getByteBgraInstance(), (byte[])var4, 0, var2 * 4);
      ByteBuffer var5 = ByteBuffer.allocate(8 + var2 * var3 * 4);
      var5.putInt(var2);
      var5.putInt(var3);
      var5.put(var4);
      return var5;
   }

   private static ByteBuffer prepareOffset(double var0, double var2) {
      ByteBuffer var4 = ByteBuffer.allocate(8);
      var4.rewind();
      var4.putInt((int)var0);
      var4.putInt((int)var2);
      return var4;
   }

   private boolean putContentToPeer(Pair... var1) {
      this.systemAssistant.emptyCache();
      boolean var2 = false;
      Pair[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Pair var6 = var3[var5];
         DataFormat var7 = (DataFormat)var6.getKey();
         Object var8 = var6.getValue();
         if (var7 == DataFormat.IMAGE) {
            var2 = this.placeImage(convertObjectToImage(var8));
         } else if (var7 == DataFormat.URL) {
            this.systemAssistant.setData("text/uri-list", var8);
            var2 = true;
         } else if (var7 == DataFormat.RTF) {
            this.systemAssistant.setData("text/rtf", var8);
            var2 = true;
         } else if (var7 == DataFormat.FILES) {
            List var30 = (List)var8;
            if (var30.size() != 0) {
               String[] var33 = new String[var30.size()];
               int var34 = 0;

               File var13;
               for(Iterator var35 = var30.iterator(); var35.hasNext(); var33[var34++] = var13.getAbsolutePath()) {
                  var13 = (File)var35.next();
               }

               this.systemAssistant.setData("application/x-java-file-list", var33);
               var2 = true;
            }
         } else {
            ByteArrayOutputStream var9;
            if (var8 instanceof Serializable) {
               if (var7 != DataFormat.PLAIN_TEXT && var7 != DataFormat.HTML || !(var8 instanceof String)) {
                  try {
                     var9 = new ByteArrayOutputStream();
                     ObjectOutputStream var10 = new ObjectOutputStream(var9);
                     var10.writeObject(var8);
                     var10.close();
                     var8 = ByteBuffer.wrap(var9.toByteArray());
                  } catch (IOException var23) {
                     throw new IllegalArgumentException("Could not serialize the data", var23);
                  }
               }
            } else if (var8 instanceof InputStream) {
               var9 = new ByteArrayOutputStream();

               try {
                  InputStream var31 = (InputStream)var8;
                  Throwable var11 = null;

                  try {
                     for(int var12 = var31.read(); var12 != -1; var12 = var31.read()) {
                        var9.write(var12);
                     }
                  } catch (Throwable var24) {
                     var11 = var24;
                     throw var24;
                  } finally {
                     if (var31 != null) {
                        if (var11 != null) {
                           try {
                              var31.close();
                           } catch (Throwable var22) {
                              var11.addSuppressed(var22);
                           }
                        } else {
                           var31.close();
                        }
                     }

                  }
               } catch (IOException var26) {
                  throw new IllegalArgumentException("Could not serialize the data", var26);
               }

               var8 = ByteBuffer.wrap(var9.toByteArray());
            } else if (!(var8 instanceof ByteBuffer)) {
               throw new IllegalArgumentException("Only serializable objects or ByteBuffer can be used as data with data format " + var7);
            }

            for(Iterator var29 = var7.getIdentifiers().iterator(); var29.hasNext(); var2 = true) {
               String var32 = (String)var29.next();
               this.systemAssistant.setData(var32, var8);
            }
         }
      }

      if (this.dragImage != null) {
         ByteBuffer var27 = prepareImage(this.dragImage);
         ByteBuffer var28 = prepareOffset(this.dragOffsetX, this.dragOffsetY);
         this.systemAssistant.setData("application/x-java-drag-image", var27);
         this.systemAssistant.setData("application/x-java-drag-image-offset", var28);
      }

      return var2;
   }

   public boolean putContent(Pair... var1) {
      Pair[] var2 = var1;
      int var3 = var1.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Pair var5 = var2[var4];
         DataFormat var6 = (DataFormat)var5.getKey();
         Object var7 = var5.getValue();
         if (var6 == null) {
            throw new NullPointerException("Clipboard.putContent: null data format");
         }

         if (var7 == null) {
            throw new NullPointerException("Clipboard.putContent: null data");
         }
      }

      boolean var8 = false;
      if (this.isCaching) {
         if (this.dataCache == null) {
            this.dataCache = new ArrayList(var1.length);
         }

         Pair[] var9 = var1;
         var4 = var1.length;

         for(int var10 = 0; var10 < var4; ++var10) {
            Pair var11 = var9[var10];
            this.dataCache.add(var11);
            var8 = true;
         }
      } else {
         var8 = this.putContentToPeer(var1);
         this.systemAssistant.flush();
      }

      return var8;
   }

   private void clearCache() {
      this.dataCache = null;
      this.transferModesCache = null;
   }

   private void clearDragView() {
      this.dragImage = null;
      this.dragOffsetX = this.dragOffsetY = 0.0;
   }
}
