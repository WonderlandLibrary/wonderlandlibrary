package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import javafx.application.Application;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public final class URLConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return URLConverter.Holder.INSTANCE;
   }

   private URLConverter() {
   }

   public String convert(ParsedValue var1, Font var2) {
      String var3 = null;
      ParsedValue[] var4 = (ParsedValue[])var1.getValue();
      String var5 = var4.length > 0 ? (String)StringConverter.getInstance().convert(var4[0], var2) : null;
      if (var5 != null && !var5.trim().isEmpty()) {
         if (var5.startsWith("url(")) {
            var5 = Utils.stripQuotes(var5.substring(4, var5.length() - 1));
         } else {
            var5 = Utils.stripQuotes(var5);
         }

         String var6 = var4.length > 1 && var4[1] != null ? (String)var4[1].getValue() : null;
         URL var7 = this.resolve(var6, var5);
         if (var7 != null) {
            var3 = var7.toExternalForm();
         }
      }

      return var3;
   }

   URL resolve(String var1, String var2) {
      String var3 = var2 != null ? var2.trim() : null;
      if (var3 != null && !var3.isEmpty()) {
         try {
            URI var4 = new URI(var3);
            if (var4.isAbsolute()) {
               return var4.toURL();
            } else {
               URL var11 = this.resolveRuntimeImport(var4);
               if (var11 != null) {
                  return var11;
               } else {
                  String var6 = var4.getPath();
                  if (var6.startsWith("/")) {
                     ClassLoader var12 = Thread.currentThread().getContextClassLoader();
                     return var12.getResource(var6.substring(1));
                  } else {
                     String var7 = var1 != null ? var1.trim() : null;
                     if (var7 != null && !var7.isEmpty()) {
                        URI var13 = new URI(var7);
                        if (!var13.isOpaque()) {
                           URI var14 = var13.resolve(var4);
                           return var14.toURL();
                        } else {
                           URL var9 = var13.toURL();
                           return new URL(var9, var4.getPath());
                        }
                     } else {
                        ClassLoader var8 = Thread.currentThread().getContextClassLoader();
                        return var8.getResource(var6);
                     }
                  }
               }
            }
         } catch (URISyntaxException | MalformedURLException var10) {
            PlatformLogger var5 = Logging.getCSSLogger();
            if (var5.isLoggable(Level.WARNING)) {
               var5.warning(var10.getLocalizedMessage());
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private URL resolveRuntimeImport(URI var1) {
      String var2 = var1.getPath();
      String var3 = var2.startsWith("/") ? var2.substring(1) : var2;
      if ((var3.startsWith("com/sun/javafx/scene/control/skin/modena/") || var3.startsWith("com/sun/javafx/scene/control/skin/caspian/")) && (var3.endsWith(".css") || var3.endsWith(".bss"))) {
         SecurityManager var4 = System.getSecurityManager();
         if (var4 == null) {
            ClassLoader var14 = Thread.currentThread().getContextClassLoader();
            URL var15 = var14.getResource(var3);
            return var15;
         }

         try {
            URL var5 = (URL)AccessController.doPrivileged(() -> {
               ProtectionDomain var0 = Application.class.getProtectionDomain();
               CodeSource var1 = var0.getCodeSource();
               return var1.getLocation();
            });
            URI var6 = var5.toURI();
            String var7 = var6.getScheme();
            String var8 = var6.getPath();
            if ("file".equals(var7) && var8.endsWith(".jar") && "file".equals(var7)) {
               var7 = "jar:file";
               var8 = var8.concat("!/");
            }

            var8 = var8.concat(var3);
            String var9 = var6.getUserInfo();
            String var10 = var6.getHost();
            int var11 = var6.getPort();
            URI var12 = new URI(var7, var9, var10, var11, var8, (String)null, (String)null);
            return var12.toURL();
         } catch (MalformedURLException | PrivilegedActionException | URISyntaxException var13) {
         }
      }

      return null;
   }

   public String toString() {
      return "URLType";
   }

   // $FF: synthetic method
   URLConverter(Object var1) {
      this();
   }

   public static final class SequenceConverter extends StyleConverterImpl {
      public static SequenceConverter getInstance() {
         return URLConverter.Holder.SEQUENCE_INSTANCE;
      }

      private SequenceConverter() {
      }

      public String[] convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         String[] var4 = new String[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = (String)URLConverter.getInstance().convert(var3[var5], var2);
         }

         return var4;
      }

      public String toString() {
         return "URLSeqType";
      }

      // $FF: synthetic method
      SequenceConverter(Object var1) {
         this();
      }
   }

   private static class Holder {
      static final URLConverter INSTANCE = new URLConverter();
      static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
   }
}
