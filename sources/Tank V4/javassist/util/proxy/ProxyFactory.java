package javassist.util.proxy;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import javassist.CannotCompileException;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.DuplicateMemberException;
import javassist.bytecode.ExceptionsAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.StackMapTable;

public class ProxyFactory {
   private Class superClass = null;
   private Class[] interfaces = null;
   private MethodFilter methodFilter = null;
   private MethodHandler handler = null;
   private List signatureMethods = null;
   private boolean hasGetHandler = false;
   private byte[] signature = null;
   private String classname;
   private String basename;
   private String superName;
   private Class thisClass = null;
   private boolean factoryUseCache;
   private boolean factoryWriteReplace;
   public String writeDirectory = null;
   private static final Class OBJECT_TYPE = Object.class;
   private static final String HOLDER = "_methods_";
   private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
   private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
   private static final String FILTER_SIGNATURE_TYPE = "[B";
   private static final String HANDLER = "handler";
   private static final String NULL_INTERCEPTOR_HOLDER = "javassist.util.proxy.RuntimeSupport";
   private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
   private static final String HANDLER_TYPE = 'L' + MethodHandler.class.getName().replace('.', '/') + ';';
   private static final String HANDLER_SETTER = "setHandler";
   private static final String HANDLER_SETTER_TYPE;
   private static final String HANDLER_GETTER = "getHandler";
   private static final String HANDLER_GETTER_TYPE;
   private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
   private static final String SERIAL_VERSION_UID_TYPE = "J";
   private static final long SERIAL_VERSION_UID_VALUE = -1L;
   public static volatile boolean useCache;
   public static volatile boolean useWriteReplace;
   private static WeakHashMap proxyCache;
   private static char[] hexDigits;
   public static ProxyFactory.ClassLoaderProvider classLoaderProvider;
   public static ProxyFactory.UniqueName nameGenerator;
   private static Comparator sorter;
   private static final String HANDLER_GETTER_KEY = "getHandler:()";

   public boolean isUseCache() {
      return this.factoryUseCache;
   }

   public void setUseCache(boolean var1) {
      if (this.handler != null && var1) {
         throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
      } else {
         this.factoryUseCache = var1;
      }
   }

   public boolean isUseWriteReplace() {
      return this.factoryWriteReplace;
   }

   public void setUseWriteReplace(boolean var1) {
      this.factoryWriteReplace = var1;
   }

   public static boolean isProxyClass(Class var0) {
      return Proxy.class.isAssignableFrom(var0);
   }

   public ProxyFactory() {
      this.factoryUseCache = useCache;
      this.factoryWriteReplace = useWriteReplace;
   }

   public void setSuperclass(Class var1) {
      this.superClass = var1;
      this.signature = null;
   }

   public Class getSuperclass() {
      return this.superClass;
   }

   public void setInterfaces(Class[] var1) {
      this.interfaces = var1;
      this.signature = null;
   }

   public Class[] getInterfaces() {
      return this.interfaces;
   }

   public void setFilter(MethodFilter var1) {
      this.methodFilter = var1;
      this.signature = null;
   }

   public Class createClass() {
      if (this.signature == null) {
         this.computeSignature(this.methodFilter);
      }

      return this.createClass1();
   }

   public Class createClass(MethodFilter var1) {
      this.computeSignature(var1);
      return this.createClass1();
   }

   Class createClass(byte[] var1) {
      this.installSignature(var1);
      return this.createClass1();
   }

   private Class createClass1() {
      Class var1 = this.thisClass;
      if (var1 == null) {
         ClassLoader var2 = this.getClassLoader();
         WeakHashMap var3;
         synchronized(var3 = proxyCache){}
         if (this.factoryUseCache) {
            this.createClass2(var2);
         } else {
            this.createClass3(var2);
         }

         var1 = this.thisClass;
         this.thisClass = null;
      }

      return var1;
   }

   public String getKey(Class var1, Class[] var2, byte[] var3, boolean var4) {
      StringBuffer var5 = new StringBuffer();
      if (var1 != null) {
         var5.append(var1.getName());
      }

      var5.append(":");

      int var6;
      for(var6 = 0; var6 < var2.length; ++var6) {
         var5.append(var2[var6].getName());
         var5.append(":");
      }

      for(var6 = 0; var6 < var3.length; ++var6) {
         byte var7 = var3[var6];
         int var8 = var7 & 15;
         int var9 = var7 >> 4 & 15;
         var5.append(hexDigits[var8]);
         var5.append(hexDigits[var9]);
      }

      if (var4) {
         var5.append(":w");
      }

      return var5.toString();
   }

   private void createClass2(ClassLoader var1) {
      String var2 = this.getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
      HashMap var3 = (HashMap)proxyCache.get(var1);
      if (var3 == null) {
         var3 = new HashMap();
         proxyCache.put(var1, var3);
      }

      ProxyFactory.ProxyDetails var4 = (ProxyFactory.ProxyDetails)var3.get(var2);
      if (var4 != null) {
         WeakReference var5 = var4.proxyClass;
         this.thisClass = (Class)var5.get();
         if (this.thisClass != null) {
            return;
         }
      }

      this.createClass3(var1);
      var4 = new ProxyFactory.ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace);
      var3.put(var2, var4);
   }

   private void createClass3(ClassLoader var1) {
      this.allocateClassName();

      try {
         ClassFile var2 = this.make();
         if (this.writeDirectory != null) {
            FactoryHelper.writeFile(var2, this.writeDirectory);
         }

         this.thisClass = FactoryHelper.toClass(var2, var1, this.getDomain());
         this.setField("_filter_signature", this.signature);
         if (!this.factoryUseCache) {
            this.setField("default_interceptor", this.handler);
         }

      } catch (CannotCompileException var3) {
         throw new RuntimeException(var3.getMessage(), var3);
      }
   }

   private void setField(String var1, Object var2) {
      if (this.thisClass != null && var2 != null) {
         try {
            Field var3 = this.thisClass.getField(var1);
            SecurityActions.setAccessible(var3, true);
            var3.set((Object)null, var2);
            SecurityActions.setAccessible(var3, false);
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }

   }

   static byte[] getFilterSignature(Class var0) {
      return (byte[])((byte[])getField(var0, "_filter_signature"));
   }

   private static Object getField(Class var0, String var1) {
      try {
         Field var2 = var0.getField(var1);
         var2.setAccessible(true);
         Object var3 = var2.get((Object)null);
         var2.setAccessible(false);
         return var3;
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public static MethodHandler getHandler(Proxy var0) {
      try {
         Field var1 = var0.getClass().getDeclaredField("handler");
         var1.setAccessible(true);
         Object var2 = var1.get(var0);
         var1.setAccessible(false);
         return (MethodHandler)var2;
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   protected ClassLoader getClassLoader() {
      return classLoaderProvider.get(this);
   }

   protected ClassLoader getClassLoader0() {
      ClassLoader var1 = null;
      if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
         var1 = this.superClass.getClassLoader();
      } else if (this.interfaces != null && this.interfaces.length > 0) {
         var1 = this.interfaces[0].getClassLoader();
      }

      if (var1 == null) {
         var1 = this.getClass().getClassLoader();
         if (var1 == null) {
            var1 = Thread.currentThread().getContextClassLoader();
            if (var1 == null) {
               var1 = ClassLoader.getSystemClassLoader();
            }
         }
      }

      return var1;
   }

   protected ProtectionDomain getDomain() {
      Class var1;
      if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
         var1 = this.superClass;
      } else if (this.interfaces != null && this.interfaces.length > 0) {
         var1 = this.interfaces[0];
      } else {
         var1 = this.getClass();
      }

      return var1.getProtectionDomain();
   }

   public Object create(Class[] var1, Object[] var2, MethodHandler var3) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Object var4 = this.create(var1, var2);
      ((Proxy)var4).setHandler(var3);
      return var4;
   }

   public Object create(Class[] var1, Object[] var2) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Class var3 = this.createClass();
      Constructor var4 = var3.getConstructor(var1);
      return var4.newInstance(var2);
   }

   /** @deprecated */
   public void setHandler(MethodHandler var1) {
      if (this.factoryUseCache && var1 != null) {
         this.factoryUseCache = false;
         this.thisClass = null;
      }

      this.handler = var1;
      this.setField("default_interceptor", this.handler);
   }

   private static String makeProxyName(String var0) {
      ProxyFactory.UniqueName var1;
      synchronized(var1 = nameGenerator){}
      return nameGenerator.get(var0);
   }

   private ClassFile make() throws CannotCompileException {
      ClassFile var1 = new ClassFile(false, this.classname, this.superName);
      var1.setAccessFlags(1);
      setInterfaces(var1, this.interfaces, this.hasGetHandler ? Proxy.class : ProxyObject.class);
      ConstPool var2 = var1.getConstPool();
      FieldInfo var3;
      if (!this.factoryUseCache) {
         var3 = new FieldInfo(var2, "default_interceptor", HANDLER_TYPE);
         var3.setAccessFlags(9);
         var1.addField(var3);
      }

      var3 = new FieldInfo(var2, "handler", HANDLER_TYPE);
      var3.setAccessFlags(2);
      var1.addField(var3);
      FieldInfo var4 = new FieldInfo(var2, "_filter_signature", "[B");
      var4.setAccessFlags(9);
      var1.addField(var4);
      FieldInfo var5 = new FieldInfo(var2, "serialVersionUID", "J");
      var5.setAccessFlags(25);
      var1.addField(var5);
      this.makeConstructors(this.classname, var1, var2, this.classname);
      ArrayList var6 = new ArrayList();
      int var7 = this.overrideMethods(var1, var2, this.classname, var6);
      addClassInitializer(var1, var2, this.classname, var7, var6);
      addSetter(this.classname, var1, var2);
      if (!this.hasGetHandler) {
         addGetter(this.classname, var1, var2);
      }

      if (this.factoryWriteReplace) {
         try {
            var1.addMethod(makeWriteReplace(var2));
         } catch (DuplicateMemberException var9) {
         }
      }

      this.thisClass = null;
      return var1;
   }

   private void checkClassAndSuperName() {
      if (this.interfaces == null) {
         this.interfaces = new Class[0];
      }

      if (this.superClass == null) {
         this.superClass = OBJECT_TYPE;
         this.superName = this.superClass.getName();
         this.basename = this.interfaces.length == 0 ? this.superName : this.interfaces[0].getName();
      } else {
         this.superName = this.superClass.getName();
         this.basename = this.superName;
      }

      if (Modifier.isFinal(this.superClass.getModifiers())) {
         throw new RuntimeException(this.superName + " is final");
      } else {
         if (this.basename.startsWith("java.")) {
            this.basename = "org.javassist.tmp." + this.basename;
         }

      }
   }

   private void allocateClassName() {
      this.classname = makeProxyName(this.basename);
   }

   private void makeSortedMethodList() {
      this.checkClassAndSuperName();
      this.hasGetHandler = false;
      HashMap var1 = this.getMethods(this.superClass, this.interfaces);
      this.signatureMethods = new ArrayList(var1.entrySet());
      Collections.sort(this.signatureMethods, sorter);
   }

   private void computeSignature(MethodFilter var1) {
      this.makeSortedMethodList();
      int var2 = this.signatureMethods.size();
      int var3 = var2 + 7 >> 3;
      this.signature = new byte[var3];

      for(int var4 = 0; var4 < var2; ++var4) {
         Entry var5 = (Entry)this.signatureMethods.get(var4);
         Method var6 = (Method)var5.getValue();
         int var7 = var6.getModifiers();
         if (!Modifier.isFinal(var7) && !Modifier.isStatic(var7)) {
            String var10001 = this.basename;
            if (var6 != false && (var1 == null || var1.isHandled(var6))) {
               this.setBit(this.signature, var4);
            }
         }
      }

   }

   private void installSignature(byte[] var1) {
      this.makeSortedMethodList();
      int var2 = this.signatureMethods.size();
      int var3 = var2 + 7 >> 3;
      if (var1.length != var3) {
         throw new RuntimeException("invalid filter signature length for deserialized proxy class");
      } else {
         this.signature = var1;
      }
   }

   private void setBit(byte[] var1, int var2) {
      int var3 = var2 >> 3;
      if (var3 < var1.length) {
         int var4 = var2 & 7;
         int var5 = 1 << var4;
         byte var6 = var1[var3];
         var1[var3] = (byte)(var6 | var5);
      }

   }

   private static void setInterfaces(ClassFile var0, Class[] var1, Class var2) {
      String var3 = var2.getName();
      String[] var4;
      if (var1 != null && var1.length != 0) {
         var4 = new String[var1.length + 1];

         for(int var5 = 0; var5 < var1.length; ++var5) {
            var4[var5] = var1[var5].getName();
         }

         var4[var1.length] = var3;
      } else {
         var4 = new String[]{var3};
      }

      var0.setInterfaces(var4);
   }

   private static void addClassInitializer(ClassFile var0, ConstPool var1, String var2, int var3, ArrayList var4) throws CannotCompileException {
      FieldInfo var5 = new FieldInfo(var1, "_methods_", "[Ljava/lang/reflect/Method;");
      var5.setAccessFlags(10);
      var0.addField(var5);
      MethodInfo var6 = new MethodInfo(var1, "<clinit>", "()V");
      var6.setAccessFlags(8);
      setThrows(var6, var1, new Class[]{ClassNotFoundException.class});
      Bytecode var7 = new Bytecode(var1, 0, 2);
      var7.addIconst(var3 * 2);
      var7.addAnewarray("java.lang.reflect.Method");
      boolean var8 = false;
      var7.addAstore(0);
      var7.addLdc(var2);
      var7.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
      boolean var9 = true;
      var7.addAstore(1);
      Iterator var10 = var4.iterator();

      while(var10.hasNext()) {
         ProxyFactory.Find2MethodsArgs var11 = (ProxyFactory.Find2MethodsArgs)var10.next();
         callFind2Methods(var7, var11.methodName, var11.delegatorName, var11.origIndex, var11.descriptor, 1, 0);
      }

      var7.addAload(0);
      var7.addPutstatic(var2, "_methods_", "[Ljava/lang/reflect/Method;");
      var7.addLconst(-1L);
      var7.addPutstatic(var2, "serialVersionUID", "J");
      var7.addOpcode(177);
      var6.setCodeAttribute(var7.toCodeAttribute());
      var0.addMethod(var6);
   }

   private static void callFind2Methods(Bytecode var0, String var1, String var2, int var3, String var4, int var5, int var6) {
      String var7 = RuntimeSupport.class.getName();
      String var8 = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V";
      var0.addAload(var5);
      var0.addLdc(var1);
      if (var2 == null) {
         var0.addOpcode(1);
      } else {
         var0.addLdc(var2);
      }

      var0.addIconst(var3);
      var0.addLdc(var4);
      var0.addAload(var6);
      var0.addInvokestatic(var7, "find2Methods", var8);
   }

   private static void addSetter(String var0, ClassFile var1, ConstPool var2) throws CannotCompileException {
      MethodInfo var3 = new MethodInfo(var2, "setHandler", HANDLER_SETTER_TYPE);
      var3.setAccessFlags(1);
      Bytecode var4 = new Bytecode(var2, 2, 2);
      var4.addAload(0);
      var4.addAload(1);
      var4.addPutfield(var0, "handler", HANDLER_TYPE);
      var4.addOpcode(177);
      var3.setCodeAttribute(var4.toCodeAttribute());
      var1.addMethod(var3);
   }

   private static void addGetter(String var0, ClassFile var1, ConstPool var2) throws CannotCompileException {
      MethodInfo var3 = new MethodInfo(var2, "getHandler", HANDLER_GETTER_TYPE);
      var3.setAccessFlags(1);
      Bytecode var4 = new Bytecode(var2, 1, 1);
      var4.addAload(0);
      var4.addGetfield(var0, "handler", HANDLER_TYPE);
      var4.addOpcode(176);
      var3.setCodeAttribute(var4.toCodeAttribute());
      var1.addMethod(var3);
   }

   private int overrideMethods(ClassFile var1, ConstPool var2, String var3, ArrayList var4) throws CannotCompileException {
      String var5 = makeUniqueName("_d", this.signatureMethods);
      Iterator var6 = this.signatureMethods.iterator();

      int var7;
      for(var7 = 0; var6.hasNext(); ++var7) {
         Entry var8 = (Entry)var6.next();
         String var9 = (String)var8.getKey();
         Method var10 = (Method)var8.getValue();
         if ((ClassFile.MAJOR_VERSION < 49 || !isBridge(var10)) && this.signature > var7) {
            this.override(var3, var10, var5, var7, keyToDesc(var9, var10), var1, var2, var4);
         }
      }

      return var7;
   }

   private static boolean isBridge(Method var0) {
      return var0.isBridge();
   }

   private void override(String var1, Method var2, String var3, int var4, String var5, ClassFile var6, ConstPool var7, ArrayList var8) throws CannotCompileException {
      Class var9 = var2.getDeclaringClass();
      String var10 = var3 + var4 + var2.getName();
      MethodInfo var11;
      if (Modifier.isAbstract(var2.getModifiers())) {
         var10 = null;
      } else {
         var11 = makeDelegator(var2, var5, var7, var9, var10);
         var11.setAccessFlags(var11.getAccessFlags() & -65);
         var6.addMethod(var11);
      }

      var11 = makeForwarder(var1, var2, var5, var7, var9, var10, var4, var8);
      var6.addMethod(var11);
   }

   private void makeConstructors(String var1, ClassFile var2, ConstPool var3, String var4) throws CannotCompileException {
      Constructor[] var5 = SecurityActions.getDeclaredConstructors(this.superClass);
      boolean var6 = !this.factoryUseCache;

      for(int var7 = 0; var7 < var5.length; ++var7) {
         Constructor var8 = var5[var7];
         int var9 = var8.getModifiers();
         if (!Modifier.isFinal(var9) && !Modifier.isPrivate(var9)) {
            String var10001 = this.basename;
            if (var8 != false) {
               MethodInfo var10 = makeConstructor(var1, var8, var3, this.superClass, var6);
               var2.addMethod(var10);
            }
         }
      }

   }

   private static String makeUniqueName(String var0, List var1) {
      if (var1.iterator() != false) {
         return var0;
      } else {
         for(int var2 = 100; var2 < 999; ++var2) {
            String var3 = var0 + var2;
            if (var1.iterator() != false) {
               return var3;
            }
         }

         throw new RuntimeException("cannot make a unique method name");
      }
   }

   private static String getPackageName(String var0) {
      int var1 = var0.lastIndexOf(46);
      return var1 < 0 ? null : var0.substring(0, var1);
   }

   private HashMap getMethods(Class var1, Class[] var2) {
      HashMap var3 = new HashMap();
      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         this.getMethods(var3, var2[var5], var4);
      }

      this.getMethods(var3, var1, var4);
      return var3;
   }

   private void getMethods(HashMap var1, Class var2, Set var3) {
      if (var3.add(var2)) {
         Class[] var4 = var2.getInterfaces();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            this.getMethods(var1, var4[var5], var3);
         }

         Class var11 = var2.getSuperclass();
         if (var11 != null) {
            this.getMethods(var1, var11, var3);
         }

         Method[] var6 = SecurityActions.getDeclaredMethods(var2);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (!Modifier.isPrivate(var6[var7].getModifiers())) {
               Method var8 = var6[var7];
               String var9 = var8.getName() + ':' + RuntimeSupport.makeDescriptor(var8);
               if (var9.startsWith("getHandler:()")) {
                  this.hasGetHandler = true;
               }

               Method var10 = (Method)var1.put(var9, var8);
               if (null != var10 && isBridge(var8) && !Modifier.isPublic(var10.getDeclaringClass().getModifiers()) && !Modifier.isAbstract(var10.getModifiers()) && var7 < var6) {
                  var1.put(var9, var10);
               }

               if (null != var10 && Modifier.isPublic(var10.getModifiers()) && !Modifier.isPublic(var8.getModifiers())) {
                  var1.put(var9, var10);
               }
            }
         }

      }
   }

   private static String keyToDesc(String var0, Method var1) {
      return var0.substring(var0.indexOf(58) + 1);
   }

   private static MethodInfo makeConstructor(String var0, Constructor var1, ConstPool var2, Class var3, boolean var4) {
      String var5 = RuntimeSupport.makeDescriptor(var1.getParameterTypes(), Void.TYPE);
      MethodInfo var6 = new MethodInfo(var2, "<init>", var5);
      var6.setAccessFlags(1);
      setThrows(var6, var2, var1.getExceptionTypes());
      Bytecode var7 = new Bytecode(var2, 0, 0);
      if (var4) {
         var7.addAload(0);
         var7.addGetstatic(var0, "default_interceptor", HANDLER_TYPE);
         var7.addPutfield(var0, "handler", HANDLER_TYPE);
         var7.addGetstatic(var0, "default_interceptor", HANDLER_TYPE);
         var7.addOpcode(199);
         var7.addIndex(10);
      }

      var7.addAload(0);
      var7.addGetstatic("javassist.util.proxy.RuntimeSupport", "default_interceptor", HANDLER_TYPE);
      var7.addPutfield(var0, "handler", HANDLER_TYPE);
      int var8 = var7.currentPc();
      var7.addAload(0);
      int var9 = addLoadParameters(var7, var1.getParameterTypes(), 1);
      var7.addInvokespecial(var3.getName(), "<init>", var5);
      var7.addOpcode(177);
      var7.setMaxLocals(var9 + 1);
      CodeAttribute var10 = var7.toCodeAttribute();
      var6.setCodeAttribute(var10);
      StackMapTable.Writer var11 = new StackMapTable.Writer(32);
      var11.sameFrame(var8);
      var10.setAttribute(var11.toStackMapTable(var2));
      return var6;
   }

   private static MethodInfo makeDelegator(Method var0, String var1, ConstPool var2, Class var3, String var4) {
      MethodInfo var5 = new MethodInfo(var2, var4, var1);
      var5.setAccessFlags(17 | var0.getModifiers() & -1319);
      setThrows(var5, var2, var0);
      Bytecode var6 = new Bytecode(var2, 0, 0);
      var6.addAload(0);
      int var7 = addLoadParameters(var6, var0.getParameterTypes(), 1);
      var6.addInvokespecial(var3.isInterface(), var2.addClassInfo(var3.getName()), var0.getName(), var1);
      addReturn(var6, var0.getReturnType());
      ++var7;
      var6.setMaxLocals(var7);
      var5.setCodeAttribute(var6.toCodeAttribute());
      return var5;
   }

   private static MethodInfo makeForwarder(String var0, Method var1, String var2, ConstPool var3, Class var4, String var5, int var6, ArrayList var7) {
      MethodInfo var8 = new MethodInfo(var3, var1.getName(), var2);
      var8.setAccessFlags(16 | var1.getModifiers() & -1313);
      setThrows(var8, var3, var1);
      int var9 = Descriptor.paramSize(var2);
      Bytecode var10 = new Bytecode(var3, 0, var9 + 2);
      int var11 = var6 * 2;
      int var12 = var6 * 2 + 1;
      int var13 = var9 + 1;
      var10.addGetstatic(var0, "_methods_", "[Ljava/lang/reflect/Method;");
      var10.addAstore(var13);
      var7.add(new ProxyFactory.Find2MethodsArgs(var1.getName(), var5, var2, var11));
      var10.addAload(0);
      var10.addGetfield(var0, "handler", HANDLER_TYPE);
      var10.addAload(0);
      var10.addAload(var13);
      var10.addIconst(var11);
      var10.addOpcode(50);
      var10.addAload(var13);
      var10.addIconst(var12);
      var10.addOpcode(50);
      makeParameterList(var10, var1.getParameterTypes());
      var10.addInvokeinterface((String)MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
      Class var14 = var1.getReturnType();
      addUnwrapper(var10, var14);
      addReturn(var10, var14);
      CodeAttribute var15 = var10.toCodeAttribute();
      var8.setCodeAttribute(var15);
      return var8;
   }

   private static void setThrows(MethodInfo var0, ConstPool var1, Method var2) {
      Class[] var3 = var2.getExceptionTypes();
      setThrows(var0, var1, var3);
   }

   private static void setThrows(MethodInfo var0, ConstPool var1, Class[] var2) {
      if (var2.length != 0) {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = var2[var4].getName();
         }

         ExceptionsAttribute var5 = new ExceptionsAttribute(var1);
         var5.setExceptions(var3);
         var0.setExceptionsAttribute(var5);
      }
   }

   private static int addLoadParameters(Bytecode var0, Class[] var1, int var2) {
      int var3 = 0;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3 += addLoad(var0, var3 + var2, var1[var5]);
      }

      return var3;
   }

   private static int addLoad(Bytecode var0, int var1, Class var2) {
      if (var2.isPrimitive()) {
         if (var2 == Long.TYPE) {
            var0.addLload(var1);
            return 2;
         }

         if (var2 == Float.TYPE) {
            var0.addFload(var1);
         } else {
            if (var2 == Double.TYPE) {
               var0.addDload(var1);
               return 2;
            }

            var0.addIload(var1);
         }
      } else {
         var0.addAload(var1);
      }

      return 1;
   }

   private static int addReturn(Bytecode var0, Class var1) {
      if (var1.isPrimitive()) {
         if (var1 == Long.TYPE) {
            var0.addOpcode(173);
            return 2;
         }

         if (var1 == Float.TYPE) {
            var0.addOpcode(174);
         } else {
            if (var1 == Double.TYPE) {
               var0.addOpcode(175);
               return 2;
            }

            if (var1 == Void.TYPE) {
               var0.addOpcode(177);
               return 0;
            }

            var0.addOpcode(172);
         }
      } else {
         var0.addOpcode(176);
      }

      return 1;
   }

   private static void makeParameterList(Bytecode var0, Class[] var1) {
      int var2 = 1;
      int var3 = var1.length;
      var0.addIconst(var3);
      var0.addAnewarray("java/lang/Object");

      for(int var4 = 0; var4 < var3; ++var4) {
         var0.addOpcode(89);
         var0.addIconst(var4);
         Class var5 = var1[var4];
         if (var5.isPrimitive()) {
            var2 = makeWrapper(var0, var5, var2);
         } else {
            var0.addAload(var2);
            ++var2;
         }

         var0.addOpcode(83);
      }

   }

   private static int makeWrapper(Bytecode var0, Class var1, int var2) {
      int var3 = FactoryHelper.typeIndex(var1);
      String var4 = FactoryHelper.wrapperTypes[var3];
      var0.addNew(var4);
      var0.addOpcode(89);
      addLoad(var0, var2, var1);
      var0.addInvokespecial(var4, "<init>", FactoryHelper.wrapperDesc[var3]);
      return var2 + FactoryHelper.dataSize[var3];
   }

   private static void addUnwrapper(Bytecode var0, Class var1) {
      if (var1.isPrimitive()) {
         if (var1 == Void.TYPE) {
            var0.addOpcode(87);
         } else {
            int var2 = FactoryHelper.typeIndex(var1);
            String var3 = FactoryHelper.wrapperTypes[var2];
            var0.addCheckcast(var3);
            var0.addInvokevirtual(var3, FactoryHelper.unwarpMethods[var2], FactoryHelper.unwrapDesc[var2]);
         }
      } else {
         var0.addCheckcast(var1.getName());
      }

   }

   private static MethodInfo makeWriteReplace(ConstPool var0) {
      MethodInfo var1 = new MethodInfo(var0, "writeReplace", "()Ljava/lang/Object;");
      String[] var2 = new String[]{"java.io.ObjectStreamException"};
      ExceptionsAttribute var3 = new ExceptionsAttribute(var0);
      var3.setExceptions(var2);
      var1.setExceptionsAttribute(var3);
      Bytecode var4 = new Bytecode(var0, 0, 1);
      var4.addAload(0);
      var4.addInvokestatic("javassist.util.proxy.RuntimeSupport", "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
      var4.addOpcode(176);
      var1.setCodeAttribute(var4.toCodeAttribute());
      return var1;
   }

   static {
      HANDLER_SETTER_TYPE = "(" + HANDLER_TYPE + ")V";
      HANDLER_GETTER_TYPE = "()" + HANDLER_TYPE;
      useCache = true;
      useWriteReplace = true;
      proxyCache = new WeakHashMap();
      hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      classLoaderProvider = new ProxyFactory.ClassLoaderProvider() {
         public ClassLoader get(ProxyFactory var1) {
            return var1.getClassLoader0();
         }
      };
      nameGenerator = new ProxyFactory.UniqueName() {
         private final String sep = "_$$_jvst" + Integer.toHexString(this.hashCode() & 4095) + "_";
         private int counter = 0;

         public String get(String var1) {
            return var1 + this.sep + Integer.toHexString(this.counter++);
         }
      };
      sorter = new Comparator() {
         public int compare(Object var1, Object var2) {
            Entry var3 = (Entry)var1;
            Entry var4 = (Entry)var2;
            String var5 = (String)var3.getKey();
            String var6 = (String)var4.getKey();
            return var5.compareTo(var6);
         }
      };
   }

   static class Find2MethodsArgs {
      String methodName;
      String delegatorName;
      String descriptor;
      int origIndex;

      Find2MethodsArgs(String var1, String var2, String var3, int var4) {
         this.methodName = var1;
         this.delegatorName = var2;
         this.descriptor = var3;
         this.origIndex = var4;
      }
   }

   public interface UniqueName {
      String get(String var1);
   }

   public interface ClassLoaderProvider {
      ClassLoader get(ProxyFactory var1);
   }

   static class ProxyDetails {
      byte[] signature;
      WeakReference proxyClass;
      boolean isUseWriteReplace;

      ProxyDetails(byte[] var1, Class var2, boolean var3) {
         this.signature = var1;
         this.proxyClass = new WeakReference(var2);
         this.isUseWriteReplace = var3;
      }
   }
}
