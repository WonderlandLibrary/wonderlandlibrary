package org.jboss.errai.reflections.serializers;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.jboss.errai.reflections.ReflectionUtils;
import org.jboss.errai.reflections.Reflections;
import org.jboss.errai.reflections.ReflectionsException;
import org.jboss.errai.reflections.scanners.TypeElementsScanner;
import org.jboss.errai.reflections.scanners.TypesScanner;
import org.jboss.errai.reflections.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaCodeSerializer implements Serializer {
   private static final Logger log = LoggerFactory.getLogger(JavaCodeSerializer.class);
   private static final char pathSeparator = '$';
   private static final String arrayDescriptor = "$$";
   private static final String tokenSeparator = "_";

   public Reflections read(InputStream var1) {
      throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
   }

   public File save(Reflections var1, String var2) {
      if (var2.endsWith("/")) {
         var2 = var2.substring(0, var2.length() - 1);
      }

      String var3 = var2.replace('.', '/').concat(".java");
      File var4 = Utils.prepareFile(var3);
      int var7 = var2.lastIndexOf(46);
      String var5;
      String var6;
      if (var7 == -1) {
         var5 = "";
         var6 = var2.substring(var2.lastIndexOf(47) + 1);
      } else {
         var5 = var2.substring(var2.lastIndexOf(47) + 1, var7);
         var6 = var2.substring(var7 + 1);
      }

      try {
         StringBuilder var8 = new StringBuilder();
         var8.append("//generated using Reflections JavaCodeSerializer").append(" [").append(new Date()).append("]").append("\n");
         if (var5.length() != 0) {
            var8.append("package ").append(var5).append(";\n");
            var8.append("\n");
         }

         var8.append("import static org.jboss.errai.reflections.serializers.JavaCodeSerializer.*;\n");
         var8.append("\n");
         var8.append("public interface ").append(var6).append(" extends IElement").append(" {\n\n");
         var8.append(this.toString(var1));
         var8.append("}\n");
         FileWriter var9 = new FileWriter(var3);
         var9.write(var8.toString());
         var9.close();
         return var4;
      } catch (IOException var10) {
         throw new RuntimeException();
      }
   }

   public String toString(Reflections var1) {
      if (var1.getStore().get(TypesScanner.class).isEmpty() || var1.getStore().get(TypeElementsScanner.class).isEmpty()) {
         log.warn("JavaCodeSerializer needs TypeScanner and TypeElemenetsScanner configured");
      }

      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = Lists.newArrayList();
      int var4 = 1;
      ArrayList var5 = Lists.newArrayList((Iterable)var1.getStore().get(TypesScanner.class).keySet());
      Collections.sort(var5);

      ArrayList var8;
      for(Iterator var6 = var5.iterator(); var6.hasNext(); var3 = var8) {
         String var7 = (String)var6.next();
         var8 = Lists.newArrayList((Object[])var7.split("\\."));

         int var9;
         for(var9 = 0; var9 < Math.min(var8.size(), var3.size()) && ((String)var8.get(var9)).equals(var3.get(var9)); ++var9) {
         }

         int var10;
         for(var10 = var3.size(); var10 > var9; --var10) {
            --var4;
            var2.append(Utils.repeat("\t", var4)).append("}\n");
         }

         for(var10 = var9; var10 < var8.size() - 1; ++var10) {
            var2.append(Utils.repeat("\t", var4++)).append("public interface ").append(this.getNonDuplicateName((String)var8.get(var10), var8, var10)).append(" extends IPackage").append(" {\n");
         }

         String var21 = (String)var8.get(var8.size() - 1);
         ArrayList var11 = Lists.newArrayList();
         SetMultimap var12 = Multimaps.newSetMultimap(new HashMap(), new Supplier(this) {
            final JavaCodeSerializer this$0;

            {
               this.this$0 = var1;
            }

            public Set get() {
               return Sets.newHashSet();
            }

            public Object get() {
               return this.get();
            }
         });
         Iterator var13 = var1.getStore().get(TypeElementsScanner.class, var7).iterator();

         String var14;
         String var16;
         String var17;
         while(var13.hasNext()) {
            var14 = (String)var13.next();
            if (var14.contains("(")) {
               if (!var14.startsWith("<")) {
                  int var15 = var14.indexOf(40);
                  var16 = var14.substring(0, var15);
                  var17 = var14.substring(var15 + 1, var14.indexOf(")"));
                  String var18 = "";
                  if (var17.length() != 0) {
                     var18 = "_" + var17.replace('.', '$').replace(", ", "_").replace("[]", "$$");
                  }

                  String var19 = var16 + var18;
                  var12.put(var16, var19);
               }
            } else {
               var11.add(var14);
            }
         }

         var2.append(Utils.repeat("\t", var4++)).append("public interface ").append(this.getNonDuplicateName(var21, var8, var8.size() - 1)).append(" extends IClass").append(" {\n");
         if (!var11.isEmpty()) {
            var13 = var11.iterator();

            while(var13.hasNext()) {
               var14 = (String)var13.next();
               var2.append(Utils.repeat("\t", var4)).append("public interface ").append(this.getNonDuplicateName(var14, var8)).append(" extends IField").append(" {}\n");
            }
         }

         if (!var12.isEmpty()) {
            var13 = var12.entries().iterator();

            while(var13.hasNext()) {
               Entry var22 = (Entry)var13.next();
               String var23 = (String)var22.getKey();
               var16 = (String)var22.getValue();
               var17 = var12.get(var23).size() == 1 ? var23 : var16;
               var17 = this.getNonDuplicateName(var17, var11);
               var2.append(Utils.repeat("\t", var4)).append("public interface ").append(this.getNonDuplicateName(var17, var8)).append(" extends IMethod").append(" {}\n");
            }
         }
      }

      for(int var20 = var3.size(); var20 >= 1; --var20) {
         var2.append(Utils.repeat("\t", var20)).append("}\n");
      }

      return var2.toString();
   }

   private String getNonDuplicateName(String var1, List var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         if (var1.equals(var2.get(var4))) {
            return this.getNonDuplicateName(var1 + "_", var2, var3);
         }
      }

      return var1;
   }

   private String getNonDuplicateName(String var1, List var2) {
      return this.getNonDuplicateName(var1, var2, var2.size());
   }

   public static Class resolveClassOf(Class var0) throws ClassNotFoundException {
      Class var1 = var0;

      ArrayList var2;
      for(var2 = Lists.newArrayList(); var1 != null && JavaCodeSerializer.IElement.class.isAssignableFrom(var1); var1 = var1.getDeclaringClass()) {
         var2.add(var1);
      }

      Collections.reverse(var2);
      int var3 = 1;

      ArrayList var4;
      for(var4 = Lists.newArrayList(); var3 < var2.size() && (JavaCodeSerializer.IPackage.class.isAssignableFrom((Class)var2.get(var3)) || JavaCodeSerializer.IClass.class.isAssignableFrom((Class)var2.get(var3))); ++var3) {
         var4.add(((Class)var2.get(var3)).getSimpleName());
      }

      String var5 = Joiner.on(".").join((Iterable)var4).replace(".$", "$");
      return Class.forName(var5);
   }

   public static Class resolveClass(Class var0) {
      try {
         return resolveClassOf(var0);
      } catch (Exception var2) {
         throw new ReflectionsException("could not resolve to class " + var0.getName(), var2);
      }
   }

   public static Field resolveField(Class var0) {
      try {
         String var1 = var0.getSimpleName();
         return resolveClassOf(var0).getDeclaredField(var1);
      } catch (Exception var2) {
         throw new ReflectionsException("could not resolve to field " + var0.getName(), var2);
      }
   }

   public static Method resolveMethod(Class var0) {
      String var1 = var0.getSimpleName();

      try {
         String var2;
         Class[] var3;
         if (var1.contains("_")) {
            var2 = var1.substring(0, var1.indexOf("_"));
            String[] var4 = var1.substring(var1.indexOf("_") + 1).split("_");
            var3 = new Class[var4.length];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               String var6 = var4[var5].replace("$$", "[]").replace('$', '.');
               var3[var5] = ReflectionUtils.forName(var6);
            }
         } else {
            var2 = var1;
            var3 = null;
         }

         return resolveClassOf(var0).getDeclaredMethod(var2, var3);
      } catch (Exception var7) {
         throw new ReflectionsException("could not resolve to method " + var0.getName(), var7);
      }
   }

   public interface IMethod extends JavaCodeSerializer.IElement {
   }

   public interface IField extends JavaCodeSerializer.IElement {
   }

   public interface IClass extends JavaCodeSerializer.IElement {
   }

   public interface IPackage extends JavaCodeSerializer.IElement {
   }

   public interface IElement {
   }
}
