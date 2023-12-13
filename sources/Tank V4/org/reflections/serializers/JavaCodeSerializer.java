package org.reflections.serializers;

import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.Utils;

public class JavaCodeSerializer implements Serializer {
   private static final String pathSeparator = "_";
   private static final String doubleSeparator = "__";
   private static final String dotSeparator = ".";
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

         var8.append("public interface ").append(var6).append(" {\n\n");
         var8.append(this.toString(var1));
         var8.append("}\n");
         Files.write(var8.toString(), new File(var3), Charset.defaultCharset());
         return var4;
      } catch (IOException var9) {
         throw new RuntimeException();
      }
   }

   public String toString(Reflections var1) {
      if (var1.getStore().get(TypeElementsScanner.class.getSimpleName()).isEmpty() && Reflections.log != null) {
         Reflections.log.warn("JavaCodeSerializer needs TypeElementsScanner configured");
      }

      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = Lists.newArrayList();
      int var4 = 1;
      ArrayList var5 = Lists.newArrayList((Iterable)var1.getStore().get(TypeElementsScanner.class.getSimpleName()).keySet());
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
            var2.append(Utils.repeat("\t", var4++)).append("public interface ").append(this.getNonDuplicateName((String)var8.get(var10), var8, var10)).append(" {\n");
         }

         String var22 = (String)var8.get(var8.size() - 1);
         ArrayList var11 = Lists.newArrayList();
         ArrayList var12 = Lists.newArrayList();
         SetMultimap var13 = Multimaps.newSetMultimap(new HashMap(), new Supplier(this) {
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
         Iterator var14 = var1.getStore().get(TypeElementsScanner.class.getSimpleName(), var7).iterator();

         String var15;
         String var17;
         String var18;
         while(var14.hasNext()) {
            var15 = (String)var14.next();
            if (var15.startsWith("@")) {
               var11.add(var15.substring(1));
            } else if (var15.contains("(")) {
               if (!var15.startsWith("<")) {
                  int var16 = var15.indexOf(40);
                  var17 = var15.substring(0, var16);
                  var18 = var15.substring(var16 + 1, var15.indexOf(")"));
                  String var19 = "";
                  if (var18.length() != 0) {
                     var19 = "_" + var18.replace(".", "_").replace(", ", "__").replace("[]", "$$");
                  }

                  String var20 = var17 + var19;
                  var13.put(var17, var20);
               }
            } else if (!Utils.isEmpty(var15)) {
               var12.add(var15);
            }
         }

         var2.append(Utils.repeat("\t", var4++)).append("public interface ").append(this.getNonDuplicateName(var22, var8, var8.size() - 1)).append(" {\n");
         if (!var12.isEmpty()) {
            var2.append(Utils.repeat("\t", var4++)).append("public interface fields {\n");
            var14 = var12.iterator();

            while(var14.hasNext()) {
               var15 = (String)var14.next();
               var2.append(Utils.repeat("\t", var4)).append("public interface ").append(this.getNonDuplicateName(var15, var8)).append(" {}\n");
            }

            --var4;
            var2.append(Utils.repeat("\t", var4)).append("}\n");
         }

         String var24;
         if (!var13.isEmpty()) {
            var2.append(Utils.repeat("\t", var4++)).append("public interface methods {\n");
            var14 = var13.entries().iterator();

            while(var14.hasNext()) {
               Entry var23 = (Entry)var14.next();
               var24 = (String)var23.getKey();
               var17 = (String)var23.getValue();
               var18 = var13.get(var24).size() == 1 ? var24 : var17;
               var18 = this.getNonDuplicateName(var18, var12);
               var2.append(Utils.repeat("\t", var4)).append("public interface ").append(this.getNonDuplicateName(var18, var8)).append(" {}\n");
            }

            --var4;
            var2.append(Utils.repeat("\t", var4)).append("}\n");
         }

         if (!var11.isEmpty()) {
            var2.append(Utils.repeat("\t", var4++)).append("public interface annotations {\n");
            var14 = var11.iterator();

            while(var14.hasNext()) {
               var15 = (String)var14.next();
               var24 = this.getNonDuplicateName(var15, var8);
               var2.append(Utils.repeat("\t", var4)).append("public interface ").append(var24).append(" {}\n");
            }

            --var4;
            var2.append(Utils.repeat("\t", var4)).append("}\n");
         }
      }

      for(int var21 = var3.size(); var21 >= 1; --var21) {
         var2.append(Utils.repeat("\t", var21)).append("}\n");
      }

      return var2.toString();
   }

   private String getNonDuplicateName(String var1, List var2, int var3) {
      String var4 = this.normalize(var1);

      for(int var5 = 0; var5 < var3; ++var5) {
         if (var4.equals(var2.get(var5))) {
            return this.getNonDuplicateName(var4 + "_", var2, var3);
         }
      }

      return var4;
   }

   private String normalize(String var1) {
      return var1.replace(".", "_");
   }

   private String getNonDuplicateName(String var1, List var2) {
      return this.getNonDuplicateName(var1, var2, var2.size());
   }

   public static Class resolveClassOf(Class var0) throws ClassNotFoundException {
      Class var1 = var0;

      LinkedList var2;
      for(var2 = Lists.newLinkedList(); var1 != null; var1 = var1.getDeclaringClass()) {
         var2.addFirst(var1.getSimpleName());
      }

      String var3 = Joiner.on(".").join((Iterable)var2.subList(1, var2.size())).replace(".$", "$");
      return Class.forName(var3);
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
         Class var2 = var0.getDeclaringClass().getDeclaringClass();
         return resolveClassOf(var2).getDeclaredField(var1);
      } catch (Exception var3) {
         throw new ReflectionsException("could not resolve to field " + var0.getName(), var3);
      }
   }

   public static Annotation resolveAnnotation(Class var0) {
      try {
         String var1 = var0.getSimpleName().replace("_", ".");
         Class var2 = var0.getDeclaringClass().getDeclaringClass();
         Class var3 = resolveClassOf(var2);
         Class var4 = ReflectionUtils.forName(var1);
         Annotation var5 = var3.getAnnotation(var4);
         return var5;
      } catch (Exception var6) {
         throw new ReflectionsException("could not resolve to annotation " + var0.getName(), var6);
      }
   }

   public static Method resolveMethod(Class var0) {
      String var1 = var0.getSimpleName();

      try {
         String var2;
         Class[] var3;
         if (var1.contains("_")) {
            var2 = var1.substring(0, var1.indexOf("_"));
            String[] var4 = var1.substring(var1.indexOf("_") + 1).split("__");
            var3 = new Class[var4.length];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               String var6 = var4[var5].replace("$$", "[]").replace("_", ".");
               var3[var5] = ReflectionUtils.forName(var6);
            }
         } else {
            var2 = var1;
            var3 = null;
         }

         Class var8 = var0.getDeclaringClass().getDeclaringClass();
         return resolveClassOf(var8).getDeclaredMethod(var2, var3);
      } catch (Exception var7) {
         throw new ReflectionsException("could not resolve to method " + var0.getName(), var7);
      }
   }
}
