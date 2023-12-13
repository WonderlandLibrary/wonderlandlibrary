package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.SystemClipboard;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class WinSystemClipboard extends SystemClipboard {
   private long ptr = 0L;
   static final byte[] terminator;
   static final String defaultCharset = "UTF-16LE";
   static final String RTFCharset = "US-ASCII";

   private static native void initIDs();

   protected WinSystemClipboard(String var1) {
      super(var1);
      this.create();
   }

   protected final long getPtr() {
      return this.ptr;
   }

   protected native boolean isOwner();

   protected native void create();

   protected native void dispose();

   protected native void push(Object[] var1, int var2);

   protected native boolean pop();

   private byte[] fosSerialize(String var1, long var2) {
      Object var4 = this.getLocalData(var1);
      if (var4 instanceof ByteBuffer) {
         byte[] var18 = ((ByteBuffer)var4).array();
         if ("text/html".equals(var1)) {
            var18 = WinHTMLCodec.encode(var18);
         }

         return var18;
      } else {
         ByteBuffer var19;
         ByteBuffer var20;
         if (var4 instanceof String) {
            String var17 = ((String)var4).replaceAll("(\r\n|\r|\n)", "\r\n");
            byte[] var21;
            if ("text/html".equals(var1)) {
               try {
                  var21 = var17.getBytes("UTF-8");
                  var20 = ByteBuffer.allocate(var21.length + 1);
                  var20.put(var21);
                  var20.put((byte)0);
                  return WinHTMLCodec.encode(var20.array());
               } catch (UnsupportedEncodingException var12) {
                  return null;
               }
            } else if ("text/rtf".equals(var1)) {
               try {
                  var21 = var17.getBytes("US-ASCII");
                  var20 = ByteBuffer.allocate(var21.length + 1);
                  var20.put(var21);
                  var20.put((byte)0);
                  return var20.array();
               } catch (UnsupportedEncodingException var13) {
                  return null;
               }
            } else {
               var19 = ByteBuffer.allocate((var17.length() + 1) * 2);

               try {
                  var19.put(var17.getBytes("UTF-16LE"));
               } catch (UnsupportedEncodingException var14) {
               }

               var19.put(terminator);
               return var19.array();
            }
         } else {
            if ("application/x-java-file-list".equals(var1)) {
               String[] var5 = (String[])((String[])var4);
               if (var5 != null && var5.length > 0) {
                  int var6 = 0;
                  String[] var7 = var5;
                  int var8 = var5.length;

                  int var9;
                  for(var9 = 0; var9 < var8; ++var9) {
                     String var10 = var7[var9];
                     var6 += (var10.length() + 1) * 2;
                  }

                  var6 += 2;

                  try {
                     var20 = ByteBuffer.allocate(var6);
                     String[] var22 = var5;
                     var9 = var5.length;

                     for(int var23 = 0; var23 < var9; ++var23) {
                        String var11 = var22[var23];
                        var20.put(var11.getBytes("UTF-16LE"));
                        var20.put(terminator);
                     }

                     var20.put(terminator);
                     return var20.array();
                  } catch (UnsupportedEncodingException var15) {
                  }
               }
            } else if ("application/x-java-rawimage".equals(var1)) {
               Pixels var16 = (Pixels)var4;
               if (var16 != null) {
                  var19 = ByteBuffer.allocate(var16.getWidth() * var16.getHeight() * 4 + 8);
                  var19.putInt(var16.getWidth());
                  var19.putInt(var16.getHeight());
                  var19.put(var16.asByteBuffer());
                  return var19.array();
               }
            }

            return null;
         }
      }
   }

   protected final void pushToSystem(HashMap var1, int var2) {
      Set var3 = var1.keySet();
      HashSet var4 = new HashSet();
      MimeTypeParser var5 = new MimeTypeParser();
      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         var5.parse(var7);
         if (!var5.isInMemoryFile()) {
            var4.add(var7);
         }
      }

      this.push(var4.toArray(), var2);
   }

   private native byte[] popBytes(String var1, long var2);

   protected final Object popFromSystem(String var1) {
      if (!this.pop()) {
         return null;
      } else {
         MimeTypeParser var2 = new MimeTypeParser(var1);
         String var3 = var2.getMime();
         byte[] var4 = this.popBytes(var3, (long)var2.getIndex());
         if (var4 != null) {
            if (!"text/plain".equals(var3) && !"text/uri-list".equals(var3)) {
               if ("text/html".equals(var3)) {
                  try {
                     var4 = WinHTMLCodec.decode(var4);
                     return new String(var4, 0, var4.length, "UTF-8");
                  } catch (UnsupportedEncodingException var12) {
                  }
               } else if ("text/rtf".equals(var3)) {
                  try {
                     return new String(var4, 0, var4.length, "US-ASCII");
                  } catch (UnsupportedEncodingException var11) {
                  }
               } else {
                  if (!"application/x-java-file-list".equals(var3)) {
                     if ("application/x-java-rawimage".equals(var3)) {
                        ByteBuffer var14 = ByteBuffer.wrap(var4, 0, 8);
                        return Application.GetApplication().createPixels(var14.getInt(), var14.getInt(), ByteBuffer.wrap(var4, 8, var4.length - 8));
                     }

                     return ByteBuffer.wrap(var4);
                  }

                  try {
                     String var5 = new String(var4, 0, var4.length, "UTF-16LE");
                     return var5.split("\u0000");
                  } catch (UnsupportedEncodingException var10) {
                  }
               }
            } else {
               try {
                  return new String(var4, 0, var4.length - 2, "UTF-16LE");
               } catch (UnsupportedEncodingException var13) {
               }
            }
         } else {
            if ("text/uri-list".equals(var3) || "text/plain".equals(var3)) {
               var4 = this.popBytes(var3 + ";locale", (long)var2.getIndex());
               if (var4 != null) {
                  try {
                     return new String(var4, 0, var4.length - 1, "UTF-8");
                  } catch (UnsupportedEncodingException var9) {
                  }
               }
            }

            if ("text/uri-list".equals(var3)) {
               String[] var15 = (String[])((String[])this.popFromSystem("application/x-java-file-list"));
               if (var15 != null) {
                  StringBuilder var6 = new StringBuilder();

                  for(int var7 = 0; var7 < var15.length; ++var7) {
                     String var8 = var15[var7];
                     var8 = var8.replace("\\", "/");
                     if (var6.length() > 0) {
                        var6.append("\r\n");
                     }

                     var6.append("file:/").append(var8);
                  }

                  return var6.toString();
               }
            }
         }

         return null;
      }
   }

   private native String[] popMimesFromSystem();

   protected final String[] mimesFromSystem() {
      return !this.pop() ? null : this.popMimesFromSystem();
   }

   public String toString() {
      return "Windows System Clipboard";
   }

   protected final void close() {
      this.dispose();
      this.ptr = 0L;
   }

   protected native void pushTargetActionToSystem(int var1);

   private native int popSupportedSourceActions();

   protected int supportedSourceActionsFromSystem() {
      return !this.pop() ? 0 : this.popSupportedSourceActions();
   }

   static {
      initIDs();
      terminator = new byte[]{0, 0};
   }

   private static final class MimeTypeParser {
      protected static final String externalBodyMime = "message/external-body";
      protected String mime;
      protected boolean bInMemoryFile;
      protected int index;

      public MimeTypeParser() {
         this.parse("");
      }

      public MimeTypeParser(String var1) {
         this.parse(var1);
      }

      public void parse(String var1) {
         this.mime = var1;
         this.bInMemoryFile = false;
         this.index = -1;
         if (var1.startsWith("message/external-body")) {
            String[] var2 = var1.split(";");
            String var3 = "";
            int var4 = -1;

            for(int var5 = 1; var5 < var2.length; ++var5) {
               String[] var6 = var2[var5].split("=");
               if (var6.length == 2) {
                  if (var6[0].trim().equalsIgnoreCase("index")) {
                     var4 = Integer.parseInt(var6[1].trim());
                  } else if (var6[0].trim().equalsIgnoreCase("access-type")) {
                     var3 = var6[1].trim();
                  }
               }

               if (var4 != -1 && !var3.isEmpty()) {
                  break;
               }
            }

            if (var3.equalsIgnoreCase("clipboard")) {
               this.bInMemoryFile = true;
               this.mime = var2[0];
               this.index = var4;
            }
         }

      }

      public String getMime() {
         return this.mime;
      }

      public int getIndex() {
         return this.index;
      }

      public boolean isInMemoryFile() {
         return this.bInMemoryFile;
      }
   }
}
