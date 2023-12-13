package com.ibm.icu.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class URLHandler {
   public static final String PROPNAME = "urlhandler.props";
   private static final Map handlers;
   private static final boolean DEBUG = ICUDebug.enabled("URLHandler");

   public static URLHandler get(URL var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getProtocol();
         if (handlers != null) {
            Method var2 = (Method)handlers.get(var1);
            if (var2 != null) {
               try {
                  URLHandler var3 = (URLHandler)var2.invoke((Object)null, var0);
                  if (var3 != null) {
                     return var3;
                  }
               } catch (IllegalAccessException var4) {
                  if (DEBUG) {
                     System.err.println(var4);
                  }
               } catch (IllegalArgumentException var5) {
                  if (DEBUG) {
                     System.err.println(var5);
                  }
               } catch (InvocationTargetException var6) {
                  if (DEBUG) {
                     System.err.println(var6);
                  }
               }
            }
         }

         return getDefault(var0);
      }
   }

   protected static URLHandler getDefault(URL var0) {
      Object var1 = null;
      String var2 = var0.getProtocol();

      try {
         if (var2.equals("file")) {
            var1 = new URLHandler.FileURLHandler(var0);
         } else if (var2.equals("jar") || var2.equals("wsjar")) {
            var1 = new URLHandler.JarURLHandler(var0);
         }
      } catch (Exception var4) {
      }

      return (URLHandler)var1;
   }

   public void guide(URLHandler.URLVisitor var1, boolean var2) {
      this.guide(var1, var2, true);
   }

   public abstract void guide(URLHandler.URLVisitor var1, boolean var2, boolean var3);

   static boolean access$000() {
      return DEBUG;
   }

   static {
      HashMap var0 = null;

      try {
         InputStream var1 = URLHandler.class.getResourceAsStream("urlhandler.props");
         if (var1 == null) {
            ClassLoader var2 = Utility.getFallbackClassLoader();
            var1 = var2.getResourceAsStream("urlhandler.props");
         }

         if (var1 != null) {
            Class[] var14 = new Class[]{URL.class};
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var1));

            for(String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
               var4 = var4.trim();
               if (var4.length() != 0 && var4.charAt(0) != '#') {
                  int var5 = var4.indexOf(61);
                  if (var5 == -1) {
                     if (DEBUG) {
                        System.err.println("bad urlhandler line: '" + var4 + "'");
                     }
                     break;
                  }

                  String var6 = var4.substring(0, var5).trim();
                  String var7 = var4.substring(var5 + 1).trim();

                  try {
                     Class var8 = Class.forName(var7);
                     Method var9 = var8.getDeclaredMethod("get", var14);
                     if (var0 == null) {
                        var0 = new HashMap();
                     }

                     var0.put(var6, var9);
                  } catch (ClassNotFoundException var10) {
                     if (DEBUG) {
                        System.err.println(var10);
                     }
                  } catch (NoSuchMethodException var11) {
                     if (DEBUG) {
                        System.err.println(var11);
                     }
                  } catch (SecurityException var12) {
                     if (DEBUG) {
                        System.err.println(var12);
                     }
                  }
               }
            }

            var3.close();
         }
      } catch (Throwable var13) {
         if (DEBUG) {
            System.err.println(var13);
         }
      }

      handlers = var0;
   }

   public interface URLVisitor {
      void visit(String var1);
   }

   private static class JarURLHandler extends URLHandler {
      JarFile jarFile;
      String prefix;

      JarURLHandler(URL var1) {
         try {
            this.prefix = var1.getPath();
            int var2 = this.prefix.lastIndexOf("!/");
            if (var2 >= 0) {
               this.prefix = this.prefix.substring(var2 + 2);
            }

            String var3 = var1.getProtocol();
            if (!var3.equals("jar")) {
               String var4 = var1.toString();
               int var5 = var4.indexOf(":");
               if (var5 != -1) {
                  var1 = new URL("jar" + var4.substring(var5));
               }
            }

            JarURLConnection var7 = (JarURLConnection)var1.openConnection();
            this.jarFile = var7.getJarFile();
         } catch (Exception var6) {
            if (URLHandler.access$000()) {
               System.err.println("icurb jar error: " + var6);
            }

            throw new IllegalArgumentException("jar error: " + var6.getMessage());
         }
      }

      public void guide(URLHandler.URLVisitor var1, boolean var2, boolean var3) {
         try {
            Enumeration var4 = this.jarFile.entries();

            while(true) {
               String var6;
               while(true) {
                  do {
                     JarEntry var5;
                     do {
                        if (!var4.hasMoreElements()) {
                           return;
                        }

                        var5 = (JarEntry)var4.nextElement();
                     } while(var5.isDirectory());

                     var6 = var5.getName();
                  } while(!var6.startsWith(this.prefix));

                  var6 = var6.substring(this.prefix.length());
                  int var7 = var6.lastIndexOf(47);
                  if (var7 == -1) {
                     break;
                  }

                  if (var2) {
                     if (var3) {
                        var6 = var6.substring(var7 + 1);
                     }
                     break;
                  }
               }

               var1.visit(var6);
            }
         } catch (Exception var8) {
            if (URLHandler.access$000()) {
               System.err.println("icurb jar error: " + var8);
            }
         }

      }
   }

   private static class FileURLHandler extends URLHandler {
      File file;

      FileURLHandler(URL var1) {
         try {
            this.file = new File(var1.toURI());
         } catch (URISyntaxException var3) {
         }

         if (this.file == null || !this.file.exists()) {
            if (URLHandler.access$000()) {
               System.err.println("file does not exist - " + var1.toString());
            }

            throw new IllegalArgumentException();
         }
      }

      public void guide(URLHandler.URLVisitor var1, boolean var2, boolean var3) {
         if (this.file.isDirectory()) {
            this.process(var1, var2, var3, "/", this.file.listFiles());
         } else {
            var1.visit(this.file.getName());
         }

      }

      private void process(URLHandler.URLVisitor var1, boolean var2, boolean var3, String var4, File[] var5) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            File var7 = var5[var6];
            if (var7.isDirectory()) {
               if (var2) {
                  this.process(var1, var2, var3, var4 + var7.getName() + '/', var7.listFiles());
               }
            } else {
               var1.visit(var3 ? var7.getName() : var4 + var7.getName());
            }
         }

      }
   }
}
