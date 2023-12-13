package javassist.compiler;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Symbol;

public class MemberResolver implements TokenId {
   private ClassPool classPool;
   private static final int YES = 0;
   private static final int NO = -1;
   private static final String INVALID = "<invalid>";
   private static WeakHashMap invalidNamesMap = new WeakHashMap();
   private Hashtable invalidNames = null;

   public MemberResolver(ClassPool var1) {
      this.classPool = var1;
   }

   public ClassPool getClassPool() {
      return this.classPool;
   }

   private static void fatal() throws CompileError {
      throw new CompileError("fatal");
   }

   public MemberResolver.Method lookupMethod(CtClass var1, CtClass var2, MethodInfo var3, String var4, int[] var5, int[] var6, String[] var7) throws CompileError {
      MemberResolver.Method var8 = null;
      if (var3 != null && var1 == var2 && var3.getName().equals(var4)) {
         int var9 = this.compareSignature(var3.getDescriptor(), var5, var6, var7);
         if (var9 != -1) {
            MemberResolver.Method var10 = new MemberResolver.Method(var1, var3, var9);
            if (var9 == 0) {
               return var10;
            }

            var8 = var10;
         }
      }

      MemberResolver.Method var11 = this.lookupMethod(var1, var4, var5, var6, var7, var8 != null);
      return var11 != null ? var11 : var8;
   }

   private MemberResolver.Method lookupMethod(CtClass var1, String var2, int[] var3, int[] var4, String[] var5, boolean var6) throws CompileError {
      MemberResolver.Method var7 = null;
      ClassFile var8 = var1.getClassFile2();
      int var13;
      MemberResolver.Method var14;
      if (var8 != null) {
         List var9 = var8.getMethods();
         int var10 = var9.size();

         for(int var11 = 0; var11 < var10; ++var11) {
            MethodInfo var12 = (MethodInfo)var9.get(var11);
            if (var12.getName().equals(var2)) {
               var13 = this.compareSignature(var12.getDescriptor(), var3, var4, var5);
               if (var13 != -1) {
                  var14 = new MemberResolver.Method(var1, var12, var13);
                  if (var13 == 0) {
                     return var14;
                  }

                  if (var7 == null || var7.notmatch > var13) {
                     var7 = var14;
                  }
               }
            }
         }
      }

      if (var6) {
         var7 = null;
      } else {
         var6 = var7 != null;
      }

      int var17 = var1.getModifiers();
      boolean var18 = Modifier.isInterface(var17);

      try {
         if (!var18) {
            CtClass var19 = var1.getSuperclass();
            if (var19 != null) {
               MemberResolver.Method var21 = this.lookupMethod(var19, var2, var3, var4, var5, var6);
               if (var21 != null) {
                  return var21;
               }
            }
         }
      } catch (NotFoundException var15) {
      }

      try {
         CtClass[] var20 = var1.getInterfaces();
         int var22 = var20.length;

         for(var13 = 0; var13 < var22; ++var13) {
            var14 = this.lookupMethod(var20[var13], var2, var3, var4, var5, var6);
            if (var14 != null) {
               return var14;
            }
         }

         if (var18) {
            CtClass var23 = var1.getSuperclass();
            if (var23 != null) {
               var14 = this.lookupMethod(var23, var2, var3, var4, var5, var6);
               if (var14 != null) {
                  return var14;
               }
            }
         }
      } catch (NotFoundException var16) {
      }

      return var7;
   }

   private int compareSignature(String var1, int[] var2, int[] var3, String[] var4) throws CompileError {
      int var5 = 0;
      int var6 = 1;
      int var7 = var2.length;
      if (var7 != Descriptor.numOfParameters(var1)) {
         return -1;
      } else {
         int var8 = var1.length();

         for(int var9 = 0; var6 < var8; ++var9) {
            char var10 = var1.charAt(var6++);
            if (var10 == ')') {
               return var9 == var7 ? var5 : -1;
            }

            if (var9 >= var7) {
               return -1;
            }

            int var11;
            for(var11 = 0; var10 == '['; var10 = var1.charAt(var6++)) {
               ++var11;
            }

            if (var2[var9] == 412) {
               if (var11 == 0 && var10 != 'L') {
                  return -1;
               }

               if (var10 == 'L') {
                  var6 = var1.indexOf(59, var6) + 1;
               }
            } else if (var3[var9] != var11) {
               if (var11 != 0 || var10 != 'L' || !var1.startsWith("java/lang/Object;", var6)) {
                  return -1;
               }

               var6 = var1.indexOf(59, var6) + 1;
               ++var5;
               if (var6 <= 0) {
                  return -1;
               }
            } else {
               int var12;
               if (var10 == 'L') {
                  var12 = var1.indexOf(59, var6);
                  if (var12 < 0 || var2[var9] != 307) {
                     return -1;
                  }

                  String var17 = var1.substring(var6, var12);
                  if (!var17.equals(var4[var9])) {
                     CtClass var14 = this.lookupClassByJvmName(var4[var9]);

                     try {
                        if (!var14.subtypeOf(this.lookupClassByJvmName(var17))) {
                           return -1;
                        }

                        ++var5;
                     } catch (NotFoundException var16) {
                        ++var5;
                     }
                  }

                  var6 = var12 + 1;
               } else {
                  var12 = descToType(var10);
                  int var13 = var2[var9];
                  if (var12 != var13) {
                     if (var12 != 324 || var13 != 334 && var13 != 303 && var13 != 306) {
                        return -1;
                     }

                     ++var5;
                  }
               }
            }
         }

         return -1;
      }
   }

   public CtField lookupFieldByJvmName2(String var1, Symbol var2, ASTree var3) throws NoFieldException {
      String var4 = var2.get();
      CtClass var5 = null;

      try {
         var5 = this.lookupClass(jvmToJavaName(var1), true);
      } catch (CompileError var8) {
         throw new NoFieldException(var1 + "/" + var4, var3);
      }

      try {
         return var5.getField(var4);
      } catch (NotFoundException var7) {
         var1 = javaToJvmName(var5.getName());
         throw new NoFieldException(var1 + "$" + var4, var3);
      }
   }

   public CtField lookupFieldByJvmName(String var1, Symbol var2) throws CompileError {
      return this.lookupField(jvmToJavaName(var1), var2);
   }

   public CtField lookupField(String var1, Symbol var2) throws CompileError {
      CtClass var3 = this.lookupClass(var1, false);

      try {
         return var3.getField(var2.get());
      } catch (NotFoundException var5) {
         throw new CompileError("no such field: " + var2.get());
      }
   }

   public CtClass lookupClassByName(ASTList var1) throws CompileError {
      return this.lookupClass(Declarator.astToClassName(var1, '.'), false);
   }

   public CtClass lookupClassByJvmName(String var1) throws CompileError {
      return this.lookupClass(jvmToJavaName(var1), false);
   }

   public CtClass lookupClass(Declarator var1) throws CompileError {
      return this.lookupClass(var1.getType(), var1.getArrayDim(), var1.getClassName());
   }

   public CtClass lookupClass(int var1, int var2, String var3) throws CompileError {
      String var4 = "";
      if (var1 == 307) {
         CtClass var5 = this.lookupClassByJvmName(var3);
         if (var2 <= 0) {
            return var5;
         }

         var4 = var5.getName();
      } else {
         var4 = getTypeName(var1);
      }

      while(var2-- > 0) {
         var4 = var4 + "[]";
      }

      return this.lookupClass(var4, false);
   }

   static String getTypeName(int var0) throws CompileError {
      String var1 = "";
      switch(var0) {
      case 301:
         var1 = "boolean";
         break;
      case 303:
         var1 = "byte";
         break;
      case 306:
         var1 = "char";
         break;
      case 312:
         var1 = "double";
         break;
      case 317:
         var1 = "float";
         break;
      case 324:
         var1 = "int";
         break;
      case 326:
         var1 = "long";
         break;
      case 334:
         var1 = "short";
         break;
      case 344:
         var1 = "void";
         break;
      default:
         fatal();
      }

      return var1;
   }

   public CtClass lookupClass(String var1, boolean var2) throws CompileError {
      Hashtable var3 = this.getInvalidNames();
      Object var4 = var3.get(var1);
      if (var4 == "<invalid>") {
         throw new CompileError("no such class: " + var1);
      } else {
         if (var4 != null) {
            try {
               return this.classPool.get((String)var4);
            } catch (NotFoundException var8) {
            }
         }

         CtClass var5 = null;

         try {
            var5 = this.lookupClass0(var1, var2);
         } catch (NotFoundException var7) {
            var5 = this.searchImports(var1);
         }

         var3.put(var1, var5.getName());
         return var5;
      }
   }

   public static int getInvalidMapSize() {
      return invalidNamesMap.size();
   }

   private Hashtable getInvalidNames() {
      Hashtable var1 = this.invalidNames;
      if (var1 == null) {
         Class var2 = MemberResolver.class;
         synchronized(MemberResolver.class){}
         WeakReference var3 = (WeakReference)invalidNamesMap.get(this.classPool);
         if (var3 != null) {
            var1 = (Hashtable)var3.get();
         }

         if (var1 == null) {
            var1 = new Hashtable();
            invalidNamesMap.put(this.classPool, new WeakReference(var1));
         }

         this.invalidNames = var1;
      }

      return var1;
   }

   private CtClass searchImports(String var1) throws CompileError {
      if (var1.indexOf(46) < 0) {
         Iterator var2 = this.classPool.getImportedPackages();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            String var4 = var3 + '.' + var1;

            try {
               return this.classPool.get(var4);
            } catch (NotFoundException var8) {
               try {
                  if (var3.endsWith("." + var1)) {
                     return this.classPool.get(var3);
                  }
               } catch (NotFoundException var7) {
               }
            }
         }
      }

      this.getInvalidNames().put(var1, "<invalid>");
      throw new CompileError("no such class: " + var1);
   }

   private CtClass lookupClass0(String var1, boolean var2) throws NotFoundException {
      CtClass var3 = null;

      do {
         try {
            var3 = this.classPool.get(var1);
         } catch (NotFoundException var7) {
            int var5 = var1.lastIndexOf(46);
            if (var2 || var5 < 0) {
               throw var7;
            }

            StringBuffer var6 = new StringBuffer(var1);
            var6.setCharAt(var5, '$');
            var1 = var6.toString();
         }
      } while(var3 == null);

      return var3;
   }

   public String resolveClassName(ASTList var1) throws CompileError {
      return var1 == null ? null : javaToJvmName(this.lookupClassByName(var1).getName());
   }

   public String resolveJvmClassName(String var1) throws CompileError {
      return var1 == null ? null : javaToJvmName(this.lookupClassByJvmName(var1).getName());
   }

   public static CtClass getSuperclass(CtClass var0) throws CompileError {
      try {
         CtClass var1 = var0.getSuperclass();
         if (var1 != null) {
            return var1;
         }
      } catch (NotFoundException var2) {
      }

      throw new CompileError("cannot find the super class of " + var0.getName());
   }

   public static CtClass getSuperInterface(CtClass var0, String var1) throws CompileError {
      try {
         CtClass[] var2 = var0.getInterfaces();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].getName().equals(var1)) {
               return var2[var3];
            }
         }
      } catch (NotFoundException var4) {
      }

      throw new CompileError("cannot find the super inetrface " + var1 + " of " + var0.getName());
   }

   public static String javaToJvmName(String var0) {
      return var0.replace('.', '/');
   }

   public static String jvmToJavaName(String var0) {
      return var0.replace('/', '.');
   }

   public static int descToType(char var0) throws CompileError {
      switch(var0) {
      case 'B':
         return 303;
      case 'C':
         return 306;
      case 'D':
         return 312;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'W':
      case 'X':
      case 'Y':
      default:
         fatal();
         return 344;
      case 'F':
         return 317;
      case 'I':
         return 324;
      case 'J':
         return 326;
      case 'L':
      case '[':
         return 307;
      case 'S':
         return 334;
      case 'V':
         return 344;
      case 'Z':
         return 301;
      }
   }

   public static int getModifiers(ASTList var0) {
      int var1 = 0;

      while(var0 != null) {
         Keyword var2 = (Keyword)var0.head();
         var0 = var0.tail();
         switch(var2.get()) {
         case 300:
            var1 |= 1024;
            break;
         case 315:
            var1 |= 16;
            break;
         case 330:
            var1 |= 2;
            break;
         case 331:
            var1 |= 4;
            break;
         case 332:
            var1 |= 1;
            break;
         case 335:
            var1 |= 8;
            break;
         case 338:
            var1 |= 32;
            break;
         case 342:
            var1 |= 128;
            break;
         case 345:
            var1 |= 64;
            break;
         case 347:
            var1 |= 2048;
         }
      }

      return var1;
   }

   public static class Method {
      public CtClass declaring;
      public MethodInfo info;
      public int notmatch;

      public Method(CtClass var1, MethodInfo var2, int var3) {
         this.declaring = var1;
         this.info = var2;
         this.notmatch = var3;
      }

      public boolean isStatic() {
         int var1 = this.info.getAccessFlags();
         return (var1 & 8) != 0;
      }
   }
}
