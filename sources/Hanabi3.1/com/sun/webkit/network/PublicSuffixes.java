package com.sun.webkit.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.IDN;
import java.net.URI;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

final class PublicSuffixes {
   private static final Logger logger = Logger.getLogger(PublicSuffixes.class.getName());
   private static final Map rulesCache = new ConcurrentHashMap();
   private static final File pslFile = (File)AccessController.doPrivileged(() -> {
      return new File(getLibDir(), "../security/public_suffix_list.dat");
   });
   private static final boolean pslFileExists = (Boolean)AccessController.doPrivileged(() -> {
      if (!pslFile.exists()) {
         logger.log(Level.WARNING, "Resource not found: lib/security/public_suffix_list.dat");
         return false;
      } else {
         return true;
      }
   });

   private PublicSuffixes() {
      throw new AssertionError();
   }

   static boolean pslFileExists() {
      return pslFileExists;
   }

   static boolean isPublicSuffix(String var0) {
      if (var0.length() == 0) {
         return false;
      } else if (!pslFileExists()) {
         return false;
      } else {
         Rules var1 = PublicSuffixes.Rules.getRules(var0);
         return var1 == null ? false : var1.match(var0);
      }
   }

   private static File getLibDir() {
      try {
         String var0 = "PublicSuffixes.class";
         Class var1 = PublicSuffixes.class;
         String var2 = var1.getResource(var0).toString();
         if (var2.startsWith("jar:file:") && var2.indexOf(33) != -1) {
            String var3 = var2.substring(4, var2.lastIndexOf(33));
            int var4 = Math.max(var3.lastIndexOf(47), var3.lastIndexOf(92));
            String var5 = var3.substring(0, var4);
            return new File((new URI(var5)).getPath());
         } else {
            return null;
         }
      } catch (Exception var6) {
         return null;
      }
   }

   private static String toLogString(Map var0) {
      if (var0.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder();
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.append(String.format("%n    "));
            var1.append((String)var3.getKey());
            var1.append(": ");
            var1.append(var3.getValue());
         }

         return var1.toString();
      }
   }

   private static class Rules {
      private final Map rules = new HashMap();

      private Rules(InputStream var1) throws IOException {
         InputStreamReader var2 = new InputStreamReader(var1, "UTF-8");
         BufferedReader var3 = new BufferedReader(var2);
         int var5 = var3.read();

         String var4;
         while(var5 != -1 && (var4 = var3.readLine()) != null) {
            Rule var6;
            if (var4.startsWith("!")) {
               var4 = var4.substring(1);
               var6 = PublicSuffixes.Rule.EXCEPTION_RULE;
            } else if (var4.startsWith("*.")) {
               var4 = var4.substring(2);
               var6 = PublicSuffixes.Rule.WILDCARD_RULE;
            } else {
               var6 = PublicSuffixes.Rule.SIMPLE_RULE;
            }

            try {
               var4 = IDN.toASCII(var4, 1);
            } catch (Exception var8) {
               PublicSuffixes.logger.log(Level.WARNING, String.format("Error parsing rule: [%s]", var4), var8);
               continue;
            }

            this.rules.put(var4, var6);
            var5 = var3.read();
         }

         if (PublicSuffixes.logger.isLoggable(Level.FINEST)) {
            PublicSuffixes.logger.log(Level.FINEST, "rules: {0}", PublicSuffixes.toLogString(this.rules));
         }

      }

      static Rules getRules(String var0) {
         String var1 = getTopLevelDomain(var0);
         return var1.isEmpty() ? null : (Rules)PublicSuffixes.rulesCache.computeIfAbsent(var1, (var1x) -> {
            return createRules(var1);
         });
      }

      private static String getTopLevelDomain(String var0) {
         var0 = IDN.toUnicode(var0, 1);
         int var1 = var0.lastIndexOf(46);
         return var1 == -1 ? var0 : var0.substring(var1 + 1);
      }

      private static Rules createRules(String var0) {
         try {
            InputStream var1 = getPubSuffixStream();
            Throwable var2 = null;

            ZipInputStream var3;
            try {
               if (var1 != null) {
                  var3 = new ZipInputStream(var1);

                  for(ZipEntry var4 = var3.getNextEntry(); var4 != null; var4 = var3.getNextEntry()) {
                     if (var4.getName().equals(var0)) {
                        Rules var5 = new Rules(var3);
                        return var5;
                     }
                  }

                  return null;
               }

               var3 = null;
            } catch (Throwable var17) {
               var2 = var17;
               throw var17;
            } finally {
               if (var1 != null) {
                  if (var2 != null) {
                     try {
                        var1.close();
                     } catch (Throwable var16) {
                        var2.addSuppressed(var16);
                     }
                  } else {
                     var1.close();
                  }
               }

            }

            return var3;
         } catch (IOException var19) {
            PublicSuffixes.logger.log(Level.WARNING, "Unexpected error", var19);
            return null;
         }
      }

      private static InputStream getPubSuffixStream() {
         InputStream var0 = (InputStream)AccessController.doPrivileged(() -> {
            try {
               return new FileInputStream(PublicSuffixes.pslFile);
            } catch (FileNotFoundException var1) {
               PublicSuffixes.logger.log(Level.WARNING, "Resource not found: lib/security/public_suffix_list.dat");
               return null;
            }
         });
         return var0;
      }

      boolean match(String var1) {
         Rule var2 = (Rule)this.rules.get(var1);
         if (var2 == PublicSuffixes.Rule.EXCEPTION_RULE) {
            return false;
         } else if (var2 != PublicSuffixes.Rule.SIMPLE_RULE && var2 != PublicSuffixes.Rule.WILDCARD_RULE) {
            int var3 = var1.indexOf(46) + 1;
            if (var3 == 0) {
               var3 = var1.length();
            }

            String var4 = var1.substring(var3);
            return this.rules.get(var4) == PublicSuffixes.Rule.WILDCARD_RULE;
         } else {
            return true;
         }
      }
   }

   private static enum Rule {
      SIMPLE_RULE,
      WILDCARD_RULE,
      EXCEPTION_RULE;
   }
}
