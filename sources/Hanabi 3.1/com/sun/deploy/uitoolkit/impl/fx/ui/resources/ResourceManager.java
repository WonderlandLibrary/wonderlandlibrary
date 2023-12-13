package com.sun.deploy.uitoolkit.impl.fx.ui.resources;

import com.sun.deploy.trace.Trace;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResourceManager {
   private static ResourceBundle rbFX;
   private static ResourceBundle rbJRE;
   private static final String UNDERSCORE = "_";
   private static final String ESC_UNDERSCORE = "__";
   private static final String AMPERSAND = "&";
   private static final String ESC_AMPERSAND = "&&";
   private static Pattern p_start = Pattern.compile("^&[^&]");
   private static Pattern p_other = Pattern.compile("[^&]&[^&]");
   private static Pattern p_underscore = Pattern.compile("_");
   private static Pattern p_escampersand = Pattern.compile("&&");

   static void reset() {
      rbFX = ResourceBundle.getBundle("com.sun.deploy.uitoolkit.impl.fx.ui.resources.Deployment");

      try {
         rbJRE = ResourceBundle.getBundle("com.sun.deploy.resources.Deployment");
      } catch (MissingResourceException var1) {
         Trace.ignoredException(var1);
         rbJRE = rbFX;
      }

   }

   public static String getMessage(String var0) {
      return convertMnemonics(getString(var0));
   }

   private static String escapeUnderscore(String var0) {
      return p_underscore.matcher(var0).replaceAll("__");
   }

   private static String unescapeAmpersand(String var0) {
      return p_escampersand.matcher(var0).replaceAll("&");
   }

   private static String convertMnemonics(String var0) {
      String var2;
      if (p_start.matcher(var0).find()) {
         var2 = "_" + var0.substring(1);
      } else {
         Matcher var1;
         if ((var1 = p_other.matcher(var0)).find()) {
            var2 = escapeUnderscore(var0.substring(0, var1.start() + 1)) + "_" + var0.substring(var1.end() - 1);
         } else {
            var2 = escapeUnderscore(var0);
         }
      }

      return unescapeAmpersand(var2);
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      try {
         return (new MessageFormat(getMessage(var0))).format(var1);
      } catch (MissingResourceException var3) {
         Trace.ignoredException(var3);
         return var0;
      }
   }

   public static String getString(String var0) {
      try {
         return rbFX.containsKey(var0) ? rbFX.getString(var0) : rbJRE.getString(var0);
      } catch (MissingResourceException var2) {
         return var0;
      }
   }

   public static String getString(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static ImageView getIcon(final String var0) {
      try {
         return (ImageView)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public ImageView run() {
               return ResourceManager.getIcon_(var0);
            }
         });
      } catch (Exception var2) {
         Trace.ignoredException(var2);
         return null;
      }
   }

   public static ImageView getIcon_(String var0) {
      String var1 = getString(var0);
      URL var2 = rbFX.getClass().getResource(var1);
      String var3 = rbFX.getClass().getName();
      if (var2 == null || var0.equals("about.java.image")) {
         var2 = rbJRE.getClass().getResource(var1);
         var3 = rbJRE.getClass().getName();
      }

      return getIcon(var2);
   }

   public static ImageView getIcon(URL var0) {
      Image var1 = new Image(var0.toString());
      return new ImageView(var1);
   }

   static {
      reset();
   }
}
