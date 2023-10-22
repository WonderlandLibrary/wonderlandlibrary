package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.AppletParameters;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Utils {
   static final Set bannedNames = new HashSet();
   static Object unnamedKey = null;

   public static Map getNamedParameters(Applet2Context var0) {
      HashMap var1 = new HashMap();
      Map var2 = var0.getParameters().rawMap();
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof String && !bannedNames.contains((String)var4)) {
               var1.put((String)var4, (String)var2.get(var4));
            }
         }
      }

      return var1;
   }

   public static String[] getUnnamed(Applet2Context var0) {
      AppletParameters var1 = var0.getParameters();
      return (String[])((String[])var1.get(unnamedKey));
   }

   static {
      try {
         Class var0 = Class.forName("sun.plugin2.util.ParameterNames", true, (ClassLoader)null);
         Field[] var1 = var0.getDeclaredFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field var4 = var1[var3];
            if (var4.getType() == String.class) {
               String var5 = (String)var4.get((Object)null);
               bannedNames.add(var5);
            } else if ("ARGUMENTS".equals(var4.getName())) {
               unnamedKey = var4.get((Object)null);
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
