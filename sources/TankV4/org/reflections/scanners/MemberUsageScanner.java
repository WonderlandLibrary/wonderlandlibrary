package org.reflections.scanners;

import com.google.common.base.Joiner;
import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.reflections.ReflectionsException;
import org.reflections.util.ClasspathHelper;

public class MemberUsageScanner extends AbstractScanner {
   private ClassPool classPool;

   public void scan(Object var1) {
      try {
         CtClass var2 = this.getClassPool().get(this.getMetadataAdapter().getClassName(var1));
         CtConstructor[] var3 = var2.getDeclaredConstructors();
         int var4 = var3.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            CtConstructor var6 = var3[var5];
            this.scanMember(var6);
         }

         CtMethod[] var8 = var2.getDeclaredMethods();
         var4 = var8.length;

         for(var5 = 0; var5 < var4; ++var5) {
            CtMethod var9 = var8[var5];
            this.scanMember(var9);
         }

         var2.detach();
      } catch (Exception var7) {
         throw new ReflectionsException("Could not scan method usage for " + this.getMetadataAdapter().getClassName(var1), var7);
      }
   }

   void scanMember(CtBehavior var1) throws CannotCompileException {
      String var2 = var1.getDeclaringClass().getName() + "." + var1.getMethodInfo().getName() + "(" + this.parameterNames(var1.getMethodInfo()) + ")";
      var1.instrument(new ExprEditor(this, var2) {
         final String val$key;
         final MemberUsageScanner this$0;

         {
            this.this$0 = var1;
            this.val$key = var2;
         }

         public void edit(NewExpr var1) throws CannotCompileException {
            try {
               MemberUsageScanner.access$000(this.this$0, var1.getConstructor().getDeclaringClass().getName() + "." + "<init>" + "(" + this.this$0.parameterNames(var1.getConstructor().getMethodInfo()) + ")", var1.getLineNumber(), this.val$key);
            } catch (NotFoundException var3) {
               throw new ReflectionsException("Could not find new instance usage in " + this.val$key, var3);
            }
         }

         public void edit(MethodCall var1) throws CannotCompileException {
            try {
               MemberUsageScanner.access$000(this.this$0, var1.getMethod().getDeclaringClass().getName() + "." + var1.getMethodName() + "(" + this.this$0.parameterNames(var1.getMethod().getMethodInfo()) + ")", var1.getLineNumber(), this.val$key);
            } catch (NotFoundException var3) {
               throw new ReflectionsException("Could not find member " + var1.getClassName() + " in " + this.val$key, var3);
            }
         }

         public void edit(ConstructorCall var1) throws CannotCompileException {
            try {
               MemberUsageScanner.access$000(this.this$0, var1.getConstructor().getDeclaringClass().getName() + "." + "<init>" + "(" + this.this$0.parameterNames(var1.getConstructor().getMethodInfo()) + ")", var1.getLineNumber(), this.val$key);
            } catch (NotFoundException var3) {
               throw new ReflectionsException("Could not find member " + var1.getClassName() + " in " + this.val$key, var3);
            }
         }

         public void edit(FieldAccess var1) throws CannotCompileException {
            try {
               MemberUsageScanner.access$000(this.this$0, var1.getField().getDeclaringClass().getName() + "." + var1.getFieldName(), var1.getLineNumber(), this.val$key);
            } catch (NotFoundException var3) {
               throw new ReflectionsException("Could not find member " + var1.getFieldName() + " in " + this.val$key, var3);
            }
         }
      });
   }

   private void put(String var1, int var2, String var3) {
      if (this.acceptResult(var1)) {
         this.getStore().put(var1, var3 + " #" + var2);
      }

   }

   String parameterNames(MethodInfo var1) {
      return Joiner.on(", ").join((Iterable)this.getMetadataAdapter().getParameterNames(var1));
   }

   private ClassPool getClassPool() {
      if (this.classPool == null) {
         synchronized(this){}
         this.classPool = new ClassPool();
         ClassLoader[] var2 = this.getConfiguration().getClassLoaders();
         if (var2 == null) {
            var2 = ClasspathHelper.classLoaders();
         }

         ClassLoader[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ClassLoader var6 = var3[var5];
            this.classPool.appendClassPath((ClassPath)(new LoaderClassPath(var6)));
         }
      }

      return this.classPool;
   }

   static void access$000(MemberUsageScanner var0, String var1, int var2, String var3) {
      var0.put(var1, var2, var3);
   }
}
