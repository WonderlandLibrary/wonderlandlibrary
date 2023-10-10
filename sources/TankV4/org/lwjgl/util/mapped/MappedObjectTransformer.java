package org.lwjgl.util.mapped;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class MappedObjectTransformer {
   static final boolean PRINT_ACTIVITY;
   static final boolean PRINT_TIMING;
   static final boolean PRINT_BYTECODE;
   static final Map className_to_subtype;
   static final String MAPPED_OBJECT_JVM;
   static final String MAPPED_HELPER_JVM;
   static final String MAPPEDSET_PREFIX;
   static final String MAPPED_SET2_JVM;
   static final String MAPPED_SET3_JVM;
   static final String MAPPED_SET4_JVM;
   static final String CACHE_LINE_PAD_JVM;
   static final String VIEWADDRESS_METHOD_NAME = "getViewAddress";
   static final String NEXT_METHOD_NAME = "next";
   static final String ALIGN_METHOD_NAME = "getAlign";
   static final String SIZEOF_METHOD_NAME = "getSizeof";
   static final String CAPACITY_METHOD_NAME = "capacity";
   static final String VIEW_CONSTRUCTOR_NAME = "constructView$LWJGL";
   static final Map OPCODE_TO_NAME;
   static final Map INSNTYPE_TO_NAME;
   static boolean is_currently_computing_frames;
   static final boolean $assertionsDisabled = !MappedObjectTransformer.class.desiredAssertionStatus();

   public static void register(Class var0) {
      if (!MappedObjectClassLoader.FORKED) {
         MappedType var1 = (MappedType)var0.getAnnotation(MappedType.class);
         if (var1 != null && var1.padding() < 0) {
            throw new ClassFormatError("Invalid mapped type padding: " + var1.padding());
         } else if (var0.getEnclosingClass() != null && !Modifier.isStatic(var0.getModifiers())) {
            throw new InternalError("only top-level or static inner classes are allowed");
         } else {
            String var2 = jvmClassName(var0);
            HashMap var3 = new HashMap();
            long var4 = 0L;
            Field[] var6 = var0.getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Field var9 = var6[var8];
               MappedObjectTransformer.FieldInfo var10 = registerField(var1 == null || var1.autoGenerateOffsets(), var2, var4, var9);
               if (var10 != null) {
                  var3.put(var9.getName(), var10);
                  var4 = Math.max(var4, var10.offset + var10.lengthPadded);
               }
            }

            int var11 = 4;
            var7 = 0;
            boolean var12 = false;
            if (var1 != null) {
               var11 = var1.align();
               if (var1.cacheLinePadding()) {
                  if (var1.padding() != 0) {
                     throw new ClassFormatError("Mapped type padding cannot be specified together with cacheLinePadding.");
                  }

                  int var13 = (int)(var4 % (long)CacheUtil.getCacheLineSize());
                  if (var13 != 0) {
                     var7 = CacheUtil.getCacheLineSize() - var13;
                  }

                  var12 = true;
               } else {
                  var7 = var1.padding();
               }
            }

            var4 += (long)var7;
            MappedObjectTransformer.MappedSubtypeInfo var14 = new MappedObjectTransformer.MappedSubtypeInfo(var2, var3, (int)var4, var11, var7, var12);
            if (className_to_subtype.put(var2, var14) != null) {
               throw new InternalError("duplicate mapped type: " + var14.className);
            }
         }
      }
   }

   private static MappedObjectTransformer.FieldInfo registerField(boolean var0, String var1, long var2, Field var4) {
      if (Modifier.isStatic(var4.getModifiers())) {
         return null;
      } else if (!var4.getType().isPrimitive() && var4.getType() != ByteBuffer.class) {
         throw new ClassFormatError("field '" + var1 + "." + var4.getName() + "' not supported: " + var4.getType());
      } else {
         MappedField var5 = (MappedField)var4.getAnnotation(MappedField.class);
         if (var5 == null && !var0) {
            throw new ClassFormatError("field '" + var1 + "." + var4.getName() + "' missing annotation " + MappedField.class.getName() + ": " + var1);
         } else {
            Pointer var6 = (Pointer)var4.getAnnotation(Pointer.class);
            if (var6 != null && var4.getType() != Long.TYPE) {
               throw new ClassFormatError("The @Pointer annotation can only be used on long fields. @Pointer field found: " + var1 + "." + var4.getName() + ": " + var4.getType());
            } else if (Modifier.isVolatile(var4.getModifiers()) && (var6 != null || var4.getType() == ByteBuffer.class)) {
               throw new ClassFormatError("The volatile keyword is not supported for @Pointer or ByteBuffer fields. Volatile field found: " + var1 + "." + var4.getName() + ": " + var4.getType());
            } else {
               long var7;
               if (var4.getType() != Long.TYPE && var4.getType() != Double.TYPE) {
                  if (var4.getType() == Double.TYPE) {
                     var7 = 8L;
                  } else if (var4.getType() != Integer.TYPE && var4.getType() != Float.TYPE) {
                     if (var4.getType() != Character.TYPE && var4.getType() != Short.TYPE) {
                        if (var4.getType() == Byte.TYPE) {
                           var7 = 1L;
                        } else {
                           if (var4.getType() != ByteBuffer.class) {
                              throw new ClassFormatError(var4.getType().getName());
                           }

                           var7 = var5.byteLength();
                           if (var7 < 0L) {
                              throw new IllegalStateException("invalid byte length for mapped ByteBuffer field: " + var1 + "." + var4.getName() + " [length=" + var7 + "]");
                           }
                        }
                     } else {
                        var7 = 2L;
                     }
                  } else {
                     var7 = 4L;
                  }
               } else if (var6 == null) {
                  var7 = 8L;
               } else {
                  var7 = (long)MappedObjectUnsafe.INSTANCE.addressSize();
               }

               if (var4.getType() != ByteBuffer.class && var2 % var7 != 0L) {
                  throw new IllegalStateException("misaligned mapped type: " + var1 + "." + var4.getName());
               } else {
                  CacheLinePad var9 = (CacheLinePad)var4.getAnnotation(CacheLinePad.class);
                  long var10 = var2;
                  if (var5 != null && var5.byteOffset() != -1L) {
                     if (var5.byteOffset() < 0L) {
                        throw new ClassFormatError("Invalid field byte offset: " + var1 + "." + var4.getName() + " [byteOffset=" + var5.byteOffset() + "]");
                     }

                     if (var9 != null) {
                        throw new ClassFormatError("A field byte offset cannot be specified together with cache-line padding: " + var1 + "." + var4.getName());
                     }

                     var10 = var5.byteOffset();
                  }

                  long var12 = var7;
                  if (var9 != null) {
                     if (var9.before() && var10 % (long)CacheUtil.getCacheLineSize() != 0L) {
                        var10 += (long)CacheUtil.getCacheLineSize() - (var10 & (long)(CacheUtil.getCacheLineSize() - 1));
                     }

                     if (var9.after() && (var10 + var7) % (long)CacheUtil.getCacheLineSize() != 0L) {
                        var12 = var7 + ((long)CacheUtil.getCacheLineSize() - (var10 + var7) % (long)CacheUtil.getCacheLineSize());
                     }

                     if (!$assertionsDisabled && var9.before() && var10 % (long)CacheUtil.getCacheLineSize() != 0L) {
                        throw new AssertionError();
                     }

                     if (!$assertionsDisabled && var9.after() && (var10 + var12) % (long)CacheUtil.getCacheLineSize() != 0L) {
                        throw new AssertionError();
                     }
                  }

                  if (PRINT_ACTIVITY) {
                     LWJGLUtil.log(MappedObjectTransformer.class.getSimpleName() + ": " + var1 + "." + var4.getName() + " [type=" + var4.getType().getSimpleName() + ", offset=" + var10 + "]");
                  }

                  return new MappedObjectTransformer.FieldInfo(var10, var7, var12, Type.getType(var4.getType()), Modifier.isVolatile(var4.getModifiers()), var6 != null);
               }
            }
         }
      }
   }

   static byte[] transformMappedObject(byte[] var0) {
      ClassWriter var1 = new ClassWriter(0);
      ClassAdapter var2 = new ClassAdapter(var1) {
         private final String[] DEFINALIZE_LIST = new String[]{"getViewAddress", "next", "getAlign", "getSizeof", "capacity"};

         public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
            String[] var6 = this.DEFINALIZE_LIST;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               if (var2.equals(var9)) {
                  var1 &= -17;
                  break;
               }
            }

            return super.visitMethod(var1, var2, var3, var4, var5);
         }
      };
      (new ClassReader(var0)).accept(var2, 0);
      return var1.toByteArray();
   }

   static byte[] transformMappedAPI(String var0, byte[] var1) {
      ClassWriter var2 = new ClassWriter(2) {
         protected String getCommonSuperClass(String var1, String var2) {
            return (!MappedObjectTransformer.is_currently_computing_frames || var1.startsWith("java/")) && var2.startsWith("java/") ? super.getCommonSuperClass(var1, var2) : "java/lang/Object";
         }
      };
      MappedObjectTransformer.TransformationAdapter var3 = new MappedObjectTransformer.TransformationAdapter(var2, var0);
      Object var4 = var3;
      if (className_to_subtype.containsKey(var0)) {
         var4 = getMethodGenAdapter(var0, var3);
      }

      (new ClassReader(var1)).accept((ClassVisitor)var4, 4);
      if (!var3.transformed) {
         return var1;
      } else {
         var1 = var2.toByteArray();
         if (PRINT_BYTECODE) {
            printBytecode(var1);
         }

         return var1;
      }
   }

   private static ClassAdapter getMethodGenAdapter(String var0, ClassVisitor var1) {
      return new ClassAdapter(var1, var0) {
         final String val$className;

         {
            this.val$className = var2;
         }

         public void visitEnd() {
            MappedObjectTransformer.MappedSubtypeInfo var1 = (MappedObjectTransformer.MappedSubtypeInfo)MappedObjectTransformer.className_to_subtype.get(this.val$className);
            this.generateViewAddressGetter();
            this.generateCapacity();
            this.generateAlignGetter(var1);
            this.generateSizeofGetter();
            this.generateNext();
            Iterator var2 = var1.fields.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               MappedObjectTransformer.FieldInfo var4 = (MappedObjectTransformer.FieldInfo)var1.fields.get(var3);
               if (var4.type.getDescriptor().length() > 1) {
                  this.generateByteBufferGetter(var3, var4);
               } else {
                  this.generateFieldGetter(var3, var4);
                  this.generateFieldSetter(var3, var4);
               }
            }

            super.visitEnd();
         }

         private void generateViewAddressGetter() {
            MethodVisitor var1 = super.visitMethod(1, "getViewAddress", "(I)J", (String)null, (String[])null);
            var1.visitCode();
            var1.visitVarInsn(25, 0);
            var1.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "baseAddress", "J");
            var1.visitVarInsn(21, 1);
            var1.visitFieldInsn(178, this.val$className, "SIZEOF", "I");
            var1.visitInsn(104);
            var1.visitInsn(133);
            var1.visitInsn(97);
            if (MappedObject.CHECKS) {
               var1.visitInsn(92);
               var1.visitVarInsn(25, 0);
               var1.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "checkAddress", "(JL" + MappedObjectTransformer.MAPPED_OBJECT_JVM + ";)V");
            }

            var1.visitInsn(173);
            var1.visitMaxs(3, 2);
            var1.visitEnd();
         }

         private void generateCapacity() {
            MethodVisitor var1 = super.visitMethod(1, "capacity", "()I", (String)null, (String[])null);
            var1.visitCode();
            var1.visitVarInsn(25, 0);
            var1.visitMethodInsn(182, MappedObjectTransformer.MAPPED_OBJECT_JVM, "backingByteBuffer", "()L" + MappedObjectTransformer.jvmClassName(ByteBuffer.class) + ";");
            var1.visitInsn(89);
            var1.visitMethodInsn(182, MappedObjectTransformer.jvmClassName(ByteBuffer.class), "capacity", "()I");
            var1.visitInsn(95);
            var1.visitMethodInsn(184, MappedObjectTransformer.jvmClassName(MemoryUtil.class), "getAddress0", "(L" + MappedObjectTransformer.jvmClassName(Buffer.class) + ";)J");
            var1.visitVarInsn(25, 0);
            var1.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "baseAddress", "J");
            var1.visitInsn(101);
            var1.visitInsn(136);
            var1.visitInsn(96);
            var1.visitFieldInsn(178, this.val$className, "SIZEOF", "I");
            var1.visitInsn(108);
            var1.visitInsn(172);
            var1.visitMaxs(3, 1);
            var1.visitEnd();
         }

         private void generateAlignGetter(MappedObjectTransformer.MappedSubtypeInfo var1) {
            MethodVisitor var2 = super.visitMethod(1, "getAlign", "()I", (String)null, (String[])null);
            var2.visitCode();
            MappedObjectTransformer.visitIntNode(var2, var1.sizeof);
            var2.visitInsn(172);
            var2.visitMaxs(1, 1);
            var2.visitEnd();
         }

         private void generateSizeofGetter() {
            MethodVisitor var1 = super.visitMethod(1, "getSizeof", "()I", (String)null, (String[])null);
            var1.visitCode();
            var1.visitFieldInsn(178, this.val$className, "SIZEOF", "I");
            var1.visitInsn(172);
            var1.visitMaxs(1, 1);
            var1.visitEnd();
         }

         private void generateNext() {
            MethodVisitor var1 = super.visitMethod(1, "next", "()V", (String)null, (String[])null);
            var1.visitCode();
            var1.visitVarInsn(25, 0);
            var1.visitInsn(89);
            var1.visitFieldInsn(180, MappedObjectTransformer.MAPPED_OBJECT_JVM, "viewAddress", "J");
            var1.visitFieldInsn(178, this.val$className, "SIZEOF", "I");
            var1.visitInsn(133);
            var1.visitInsn(97);
            var1.visitMethodInsn(182, this.val$className, "setViewAddress", "(J)V");
            var1.visitInsn(177);
            var1.visitMaxs(3, 1);
            var1.visitEnd();
         }

         private void generateByteBufferGetter(String var1, MappedObjectTransformer.FieldInfo var2) {
            MethodVisitor var3 = super.visitMethod(9, MappedObjectTransformer.getterName(var1), "(L" + this.val$className + ";I)" + var2.type.getDescriptor(), (String)null, (String[])null);
            var3.visitCode();
            var3.visitVarInsn(25, 0);
            var3.visitVarInsn(21, 1);
            var3.visitMethodInsn(182, this.val$className, "getViewAddress", "(I)J");
            MappedObjectTransformer.visitIntNode(var3, (int)var2.offset);
            var3.visitInsn(133);
            var3.visitInsn(97);
            MappedObjectTransformer.visitIntNode(var3, (int)var2.length);
            var3.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + MappedObjectTransformer.jvmClassName(ByteBuffer.class) + ";");
            var3.visitInsn(176);
            var3.visitMaxs(3, 2);
            var3.visitEnd();
         }

         private void generateFieldGetter(String var1, MappedObjectTransformer.FieldInfo var2) {
            MethodVisitor var3 = super.visitMethod(9, MappedObjectTransformer.getterName(var1), "(L" + this.val$className + ";I)" + var2.type.getDescriptor(), (String)null, (String[])null);
            var3.visitCode();
            var3.visitVarInsn(25, 0);
            var3.visitVarInsn(21, 1);
            var3.visitMethodInsn(182, this.val$className, "getViewAddress", "(I)J");
            MappedObjectTransformer.visitIntNode(var3, (int)var2.offset);
            var3.visitInsn(133);
            var3.visitInsn(97);
            var3.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, var2.getAccessType() + "get", "(J)" + var2.type.getDescriptor());
            var3.visitInsn(var2.type.getOpcode(172));
            var3.visitMaxs(3, 2);
            var3.visitEnd();
         }

         private void generateFieldSetter(String var1, MappedObjectTransformer.FieldInfo var2) {
            MethodVisitor var3 = super.visitMethod(9, MappedObjectTransformer.setterName(var1), "(L" + this.val$className + ";I" + var2.type.getDescriptor() + ")V", (String)null, (String[])null);
            var3.visitCode();
            byte var4 = 0;
            switch(var2.type.getSort()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
               var4 = 21;
               break;
            case 6:
               var4 = 23;
               break;
            case 7:
               var4 = 22;
               break;
            case 8:
               var4 = 24;
            }

            var3.visitVarInsn(var4, 2);
            var3.visitVarInsn(25, 0);
            var3.visitVarInsn(21, 1);
            var3.visitMethodInsn(182, this.val$className, "getViewAddress", "(I)J");
            MappedObjectTransformer.visitIntNode(var3, (int)var2.offset);
            var3.visitInsn(133);
            var3.visitInsn(97);
            var3.visitMethodInsn(184, MappedObjectTransformer.MAPPED_HELPER_JVM, var2.getAccessType() + "put", "(" + var2.type.getDescriptor() + "J)V");
            var3.visitInsn(177);
            var3.visitMaxs(4, 4);
            var3.visitEnd();
         }
      };
   }

   static int transformMethodCall(InsnList var0, int var1, Map var2, MethodInsnNode var3, MappedObjectTransformer.MappedSubtypeInfo var4, Map var5) {
      switch(var3.getOpcode()) {
      case 182:
         if ("asArray".equals(var3.name) && var3.desc.equals("()[L" + MAPPED_OBJECT_JVM + ";")) {
            AbstractInsnNode var10;
            checkInsnAfterIsArray(var10 = var3.getNext(), 192);
            checkInsnAfterIsArray(var10 = var10.getNext(), 58);
            Frame var11 = (Frame)var2.get(var10);
            String var12 = ((BasicValue)var11.getStack(var11.getStackSize() - 1)).getType().getElementType().getInternalName();
            if (!var3.owner.equals(var12)) {
               throw new ClassCastException("Source: " + var3.owner + " - Target: " + var12);
            }

            VarInsnNode var9 = (VarInsnNode)var10;
            var5.put(var9.var, var4);
            var0.remove(var3.getNext());
            var0.remove(var3);
         }

         if ("dup".equals(var3.name) && var3.desc.equals("()L" + MAPPED_OBJECT_JVM + ";")) {
            var1 = replace(var0, var1, var3, generateDupInstructions(var3));
         } else if ("slice".equals(var3.name) && var3.desc.equals("()L" + MAPPED_OBJECT_JVM + ";")) {
            var1 = replace(var0, var1, var3, generateSliceInstructions(var3));
         } else if ("runViewConstructor".equals(var3.name) && "()V".equals(var3.desc)) {
            var1 = replace(var0, var1, var3, generateRunViewConstructorInstructions(var3));
         } else if ("copyTo".equals(var3.name) && var3.desc.equals("(L" + MAPPED_OBJECT_JVM + ";)V")) {
            var1 = replace(var0, var1, var3, generateCopyToInstructions(var4));
         } else if ("copyRange".equals(var3.name) && var3.desc.equals("(L" + MAPPED_OBJECT_JVM + ";I)V")) {
            var1 = replace(var0, var1, var3, generateCopyRangeInstructions(var4));
         }
         break;
      case 183:
         if (var3.owner.equals(MAPPED_OBJECT_JVM) && "<init>".equals(var3.name) && "()V".equals(var3.desc)) {
            var0.remove(var3.getPrevious());
            var0.remove(var3);
            var1 -= 2;
         }
         break;
      case 184:
         boolean var6 = "map".equals(var3.name) && var3.desc.equals("(JI)L" + MAPPED_OBJECT_JVM + ";");
         boolean var7 = "map".equals(var3.name) && var3.desc.equals("(Ljava/nio/ByteBuffer;)L" + MAPPED_OBJECT_JVM + ";");
         boolean var8 = "malloc".equals(var3.name) && var3.desc.equals("(I)L" + MAPPED_OBJECT_JVM + ";");
         if (var6 || var7 || var8) {
            var1 = replace(var0, var1, var3, generateMapInstructions(var4, var3.owner, var6, var8));
         }
      }

      return var1;
   }

   private static InsnList generateCopyRangeInstructions(MappedObjectTransformer.MappedSubtypeInfo var0) {
      InsnList var1 = new InsnList();
      var1.add(getIntNode(var0.sizeof));
      var1.add(new InsnNode(104));
      var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "copy", "(L" + MAPPED_OBJECT_JVM + ";L" + MAPPED_OBJECT_JVM + ";I)V"));
      return var1;
   }

   private static InsnList generateCopyToInstructions(MappedObjectTransformer.MappedSubtypeInfo var0) {
      InsnList var1 = new InsnList();
      var1.add(getIntNode(var0.sizeof - var0.padding));
      var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "copy", "(L" + MAPPED_OBJECT_JVM + ";L" + MAPPED_OBJECT_JVM + ";I)V"));
      return var1;
   }

   private static InsnList generateRunViewConstructorInstructions(MethodInsnNode var0) {
      InsnList var1 = new InsnList();
      var1.add(new InsnNode(89));
      var1.add(new MethodInsnNode(182, var0.owner, "constructView$LWJGL", "()V"));
      return var1;
   }

   private static InsnList generateSliceInstructions(MethodInsnNode var0) {
      InsnList var1 = new InsnList();
      var1.add(new TypeInsnNode(187, var0.owner));
      var1.add(new InsnNode(89));
      var1.add(new MethodInsnNode(183, var0.owner, "<init>", "()V"));
      var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "slice", "(L" + MAPPED_OBJECT_JVM + ";L" + MAPPED_OBJECT_JVM + ";)L" + MAPPED_OBJECT_JVM + ";"));
      return var1;
   }

   private static InsnList generateDupInstructions(MethodInsnNode var0) {
      InsnList var1 = new InsnList();
      var1.add(new TypeInsnNode(187, var0.owner));
      var1.add(new InsnNode(89));
      var1.add(new MethodInsnNode(183, var0.owner, "<init>", "()V"));
      var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "dup", "(L" + MAPPED_OBJECT_JVM + ";L" + MAPPED_OBJECT_JVM + ";)L" + MAPPED_OBJECT_JVM + ";"));
      return var1;
   }

   private static InsnList generateMapInstructions(MappedObjectTransformer.MappedSubtypeInfo var0, String var1, boolean var2, boolean var3) {
      InsnList var4 = new InsnList();
      if (var3) {
         var4.add(getIntNode(var0.sizeof));
         var4.add(new InsnNode(104));
         var4.add(new MethodInsnNode(184, var0.cacheLinePadded ? jvmClassName(CacheUtil.class) : jvmClassName(BufferUtils.class), "createByteBuffer", "(I)L" + jvmClassName(ByteBuffer.class) + ";"));
      } else if (var2) {
         var4.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + jvmClassName(ByteBuffer.class) + ";"));
      }

      var4.add(new TypeInsnNode(187, var1));
      var4.add(new InsnNode(89));
      var4.add(new MethodInsnNode(183, var1, "<init>", "()V"));
      var4.add(new InsnNode(90));
      var4.add(new InsnNode(95));
      var4.add(getIntNode(var0.align));
      var4.add(getIntNode(var0.sizeof));
      var4.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "setup", "(L" + MAPPED_OBJECT_JVM + ";Ljava/nio/ByteBuffer;II)V"));
      return var4;
   }

   static InsnList transformFieldAccess(FieldInsnNode var0) {
      MappedObjectTransformer.MappedSubtypeInfo var1 = (MappedObjectTransformer.MappedSubtypeInfo)className_to_subtype.get(var0.owner);
      if (var1 == null) {
         return "view".equals(var0.name) && var0.owner.startsWith(MAPPEDSET_PREFIX) ? generateSetViewInstructions(var0) : null;
      } else if ("SIZEOF".equals(var0.name)) {
         return generateSIZEOFInstructions(var0, var1);
      } else if ("view".equals(var0.name)) {
         return generateViewInstructions(var0, var1);
      } else if (!"baseAddress".equals(var0.name) && !"viewAddress".equals(var0.name)) {
         MappedObjectTransformer.FieldInfo var2 = (MappedObjectTransformer.FieldInfo)var1.fields.get(var0.name);
         if (var2 == null) {
            return null;
         } else {
            return var0.desc.equals("L" + jvmClassName(ByteBuffer.class) + ";") ? generateByteBufferInstructions(var0, var1, var2.offset) : generateFieldInstructions(var0, var2);
         }
      } else {
         return generateAddressInstructions(var0);
      }
   }

   private static InsnList generateSetViewInstructions(FieldInsnNode var0) {
      if (var0.getOpcode() == 180) {
         throwAccessErrorOnReadOnlyField(var0.owner, var0.name);
      }

      if (var0.getOpcode() != 181) {
         throw new InternalError();
      } else {
         InsnList var1 = new InsnList();
         if (MAPPED_SET2_JVM.equals(var0.owner)) {
            var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "put_views", "(L" + MAPPED_SET2_JVM + ";I)V"));
         } else if (MAPPED_SET3_JVM.equals(var0.owner)) {
            var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "put_views", "(L" + MAPPED_SET3_JVM + ";I)V"));
         } else {
            if (!MAPPED_SET4_JVM.equals(var0.owner)) {
               throw new InternalError();
            }

            var1.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "put_views", "(L" + MAPPED_SET4_JVM + ";I)V"));
         }

         return var1;
      }
   }

   private static InsnList generateSIZEOFInstructions(FieldInsnNode var0, MappedObjectTransformer.MappedSubtypeInfo var1) {
      if (!"I".equals(var0.desc)) {
         throw new InternalError();
      } else {
         InsnList var2 = new InsnList();
         if (var0.getOpcode() == 178) {
            var2.add(getIntNode(var1.sizeof));
            return var2;
         } else {
            if (var0.getOpcode() == 179) {
               throwAccessErrorOnReadOnlyField(var0.owner, var0.name);
            }

            throw new InternalError();
         }
      }
   }

   private static InsnList generateViewInstructions(FieldInsnNode var0, MappedObjectTransformer.MappedSubtypeInfo var1) {
      if (!"I".equals(var0.desc)) {
         throw new InternalError();
      } else {
         InsnList var2 = new InsnList();
         if (var0.getOpcode() == 180) {
            if (var1.sizeof_shift != 0) {
               var2.add(getIntNode(var1.sizeof_shift));
               var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "get_view_shift", "(L" + MAPPED_OBJECT_JVM + ";I)I"));
            } else {
               var2.add(getIntNode(var1.sizeof));
               var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "get_view", "(L" + MAPPED_OBJECT_JVM + ";I)I"));
            }

            return var2;
         } else if (var0.getOpcode() == 181) {
            if (var1.sizeof_shift != 0) {
               var2.add(getIntNode(var1.sizeof_shift));
               var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "put_view_shift", "(L" + MAPPED_OBJECT_JVM + ";II)V"));
            } else {
               var2.add(getIntNode(var1.sizeof));
               var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "put_view", "(L" + MAPPED_OBJECT_JVM + ";II)V"));
            }

            return var2;
         } else {
            throw new InternalError();
         }
      }
   }

   private static InsnList generateAddressInstructions(FieldInsnNode var0) {
      if (!"J".equals(var0.desc)) {
         throw new IllegalStateException();
      } else if (var0.getOpcode() == 180) {
         return null;
      } else {
         if (var0.getOpcode() == 181) {
            throwAccessErrorOnReadOnlyField(var0.owner, var0.name);
         }

         throw new InternalError();
      }
   }

   private static InsnList generateByteBufferInstructions(FieldInsnNode var0, MappedObjectTransformer.MappedSubtypeInfo var1, long var2) {
      if (var0.getOpcode() == 181) {
         throwAccessErrorOnReadOnlyField(var0.owner, var0.name);
      }

      if (var0.getOpcode() == 180) {
         InsnList var4 = new InsnList();
         var4.add(new FieldInsnNode(180, var1.className, "viewAddress", "J"));
         var4.add(new LdcInsnNode(var2));
         var4.add(new InsnNode(97));
         var4.add(new LdcInsnNode(((MappedObjectTransformer.FieldInfo)var1.fields.get(var0.name)).length));
         var4.add(new InsnNode(136));
         var4.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, "newBuffer", "(JI)L" + jvmClassName(ByteBuffer.class) + ";"));
         return var4;
      } else {
         throw new InternalError();
      }
   }

   private static InsnList generateFieldInstructions(FieldInsnNode var0, MappedObjectTransformer.FieldInfo var1) {
      InsnList var2 = new InsnList();
      if (var0.getOpcode() == 181) {
         var2.add(getIntNode((int)var1.offset));
         var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, var1.getAccessType() + "put", "(L" + MAPPED_OBJECT_JVM + ";" + var0.desc + "I)V"));
         return var2;
      } else if (var0.getOpcode() == 180) {
         var2.add(getIntNode((int)var1.offset));
         var2.add(new MethodInsnNode(184, MAPPED_HELPER_JVM, var1.getAccessType() + "get", "(L" + MAPPED_OBJECT_JVM + ";I)" + var0.desc));
         return var2;
      } else {
         throw new InternalError();
      }
   }

   static int transformArrayAccess(InsnList var0, int var1, Map var2, VarInsnNode var3, MappedObjectTransformer.MappedSubtypeInfo var4, int var5) {
      int var6 = ((Frame)var2.get(var3)).getStackSize() + 1;
      Object var7 = var3;

      while(true) {
         var7 = ((AbstractInsnNode)var7).getNext();
         if (var7 == null) {
            throw new InternalError();
         }

         Frame var8 = (Frame)var2.get(var7);
         if (var8 != null) {
            int var9 = var8.getStackSize();
            if (var9 == var6 + 1 && ((AbstractInsnNode)var7).getOpcode() == 50) {
               Object var10 = var7;

               while(true) {
                  var7 = ((AbstractInsnNode)var7).getNext();
                  if (var7 == null) {
                     break;
                  }

                  var8 = (Frame)var2.get(var7);
                  if (var8 != null) {
                     var9 = var8.getStackSize();
                     FieldInsnNode var11;
                     if (var9 == var6 + 1 && ((AbstractInsnNode)var7).getOpcode() == 181) {
                        var11 = (FieldInsnNode)var7;
                        var0.insert((AbstractInsnNode)var7, new MethodInsnNode(184, var4.className, setterName(var11.name), "(L" + var4.className + ";I" + var11.desc + ")V"));
                        var0.remove((AbstractInsnNode)var7);
                        break;
                     }

                     if (var9 == var6 && ((AbstractInsnNode)var7).getOpcode() == 180) {
                        var11 = (FieldInsnNode)var7;
                        var0.insert((AbstractInsnNode)var7, new MethodInsnNode(184, var4.className, getterName(var11.name), "(L" + var4.className + ";I)" + var11.desc));
                        var0.remove((AbstractInsnNode)var7);
                        break;
                     }

                     if (var9 == var6 && ((AbstractInsnNode)var7).getOpcode() == 89 && ((AbstractInsnNode)var7).getNext().getOpcode() == 180) {
                        var11 = (FieldInsnNode)((AbstractInsnNode)var7).getNext();
                        MethodInsnNode var12 = new MethodInsnNode(184, var4.className, getterName(var11.name), "(L" + var4.className + ";I)" + var11.desc);
                        var0.insert((AbstractInsnNode)var7, new InsnNode(92));
                        var0.insert(((AbstractInsnNode)var7).getNext(), var12);
                        var0.remove((AbstractInsnNode)var7);
                        var0.remove(var11);
                        var7 = var12;
                     } else if (var9 < var6) {
                        throw new ClassFormatError("Invalid " + var4.className + " view array usage detected: " + getOpcodeName((AbstractInsnNode)var7));
                     }
                  }
               }

               var0.remove((AbstractInsnNode)var10);
               return var1;
            }

            if (var9 == var6 && ((AbstractInsnNode)var7).getOpcode() == 190) {
               if (LWJGLUtil.DEBUG && var3.getNext() != var7) {
                  throw new InternalError();
               }

               var0.remove((AbstractInsnNode)var7);
               var3.var = var5;
               var0.insert(var3, new MethodInsnNode(182, var4.className, "capacity", "()I"));
               return var1 + 1;
            }

            if (var9 < var6) {
               throw new ClassFormatError("Invalid " + var4.className + " view array usage detected: " + getOpcodeName((AbstractInsnNode)var7));
            }
         }
      }
   }

   private static void getClassEnums(Class var0, Map var1, String... var2) {
      try {
         Field[] var3 = var0.getFields();
         int var4 = var3.length;

         label38:
         for(int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            if (Modifier.isStatic(var6.getModifiers()) && var6.getType() == Integer.TYPE) {
               String[] var7 = var2;
               int var8 = var2.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String var10 = var7[var9];
                  if (var6.getName().startsWith(var10)) {
                     continue label38;
                  }
               }

               if (var1.put((Integer)var6.get((Object)null), var6.getName()) != null) {
                  throw new IllegalStateException();
               }
            }
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   static String getOpcodeName(AbstractInsnNode var0) {
      String var1 = (String)OPCODE_TO_NAME.get(var0.getOpcode());
      return (String)INSNTYPE_TO_NAME.get(var0.getType()) + ": " + var0.getOpcode() + (var1 == null ? "" : " [" + (String)OPCODE_TO_NAME.get(var0.getOpcode()) + "]");
   }

   static String jvmClassName(Class var0) {
      return var0.getName().replace('.', '/');
   }

   static String getterName(String var0) {
      return "get$" + Character.toUpperCase(var0.charAt(0)) + var0.substring(1) + "$LWJGL";
   }

   static String setterName(String var0) {
      return "set$" + Character.toUpperCase(var0.charAt(0)) + var0.substring(1) + "$LWJGL";
   }

   private static void checkInsnAfterIsArray(AbstractInsnNode var0, int var1) {
      if (var0 == null) {
         throw new ClassFormatError("Unexpected end of instructions after .asArray() method.");
      } else if (var0.getOpcode() != var1) {
         throw new ClassFormatError("The result of .asArray() must be stored to a local variable. Found: " + getOpcodeName(var0));
      }
   }

   static AbstractInsnNode getIntNode(int var0) {
      if (var0 <= 5 && -1 <= var0) {
         return new InsnNode(2 + var0 + 1);
      } else if (var0 >= -128 && var0 <= 127) {
         return new IntInsnNode(16, var0);
      } else {
         return (AbstractInsnNode)(var0 >= -32768 && var0 <= 32767 ? new IntInsnNode(17, var0) : new LdcInsnNode(var0));
      }
   }

   static void visitIntNode(MethodVisitor var0, int var1) {
      if (var1 <= 5 && -1 <= var1) {
         var0.visitInsn(2 + var1 + 1);
      } else if (var1 >= -128 && var1 <= 127) {
         var0.visitIntInsn(16, var1);
      } else if (var1 >= -32768 && var1 <= 32767) {
         var0.visitIntInsn(17, var1);
      } else {
         var0.visitLdcInsn(var1);
      }

   }

   static int replace(InsnList var0, int var1, AbstractInsnNode var2, InsnList var3) {
      int var4 = var3.size();
      var0.insert(var2, var3);
      var0.remove(var2);
      return var1 + (var4 - 1);
   }

   private static void throwAccessErrorOnReadOnlyField(String var0, String var1) {
      throw new IllegalAccessError("The " + var0 + "." + var1 + " field is final.");
   }

   private static void printBytecode(byte[] var0) {
      StringWriter var1 = new StringWriter();
      TraceClassVisitor var2 = new TraceClassVisitor(new ClassWriter(0), new PrintWriter(var1));
      (new ClassReader(var0)).accept(var2, 0);
      String var3 = var1.toString();
      LWJGLUtil.log(var3);
   }

   static {
      PRINT_ACTIVITY = LWJGLUtil.DEBUG && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintActivity");
      PRINT_TIMING = PRINT_ACTIVITY && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintTiming");
      PRINT_BYTECODE = LWJGLUtil.DEBUG && LWJGLUtil.getPrivilegedBoolean("org.lwjgl.util.mapped.PrintBytecode");
      MAPPED_OBJECT_JVM = jvmClassName(MappedObject.class);
      MAPPED_HELPER_JVM = jvmClassName(MappedHelper.class);
      MAPPEDSET_PREFIX = jvmClassName(MappedSet.class);
      MAPPED_SET2_JVM = jvmClassName(MappedSet2.class);
      MAPPED_SET3_JVM = jvmClassName(MappedSet3.class);
      MAPPED_SET4_JVM = jvmClassName(MappedSet4.class);
      CACHE_LINE_PAD_JVM = "L" + jvmClassName(CacheLinePad.class) + ";";
      OPCODE_TO_NAME = new HashMap();
      INSNTYPE_TO_NAME = new HashMap();
      getClassEnums(Opcodes.class, OPCODE_TO_NAME, "V1_", "ACC_", "T_", "F_", "MH_");
      getClassEnums(AbstractInsnNode.class, INSNTYPE_TO_NAME);
      className_to_subtype = new HashMap();
      className_to_subtype.put(MAPPED_OBJECT_JVM, new MappedObjectTransformer.MappedSubtypeInfo(MAPPED_OBJECT_JVM, (Map)null, -1, -1, -1, false));
      String var0 = System.getProperty("java.vm.name");
      if (var0 != null && !var0.contains("Server")) {
         System.err.println("Warning: " + MappedObject.class.getSimpleName() + "s have inferiour performance on Client VMs, please consider switching to a Server VM.");
      }

   }

   private static class MappedSubtypeInfo {
      final String className;
      final int sizeof;
      final int sizeof_shift;
      final int align;
      final int padding;
      final boolean cacheLinePadded;
      final Map fields;

      MappedSubtypeInfo(String var1, Map var2, int var3, int var4, int var5, boolean var6) {
         this.className = var1;
         this.sizeof = var3;
         if ((var3 - 1 & var3) == 0) {
            this.sizeof_shift = getPoT(var3);
         } else {
            this.sizeof_shift = 0;
         }

         this.align = var4;
         this.padding = var5;
         this.cacheLinePadded = var6;
         this.fields = var2;
      }

      private static int getPoT(int var0) {
         int var1;
         for(var1 = -1; var0 > 0; var0 >>= 1) {
            ++var1;
         }

         return var1;
      }
   }

   private static class FieldInfo {
      final long offset;
      final long length;
      final long lengthPadded;
      final Type type;
      final boolean isVolatile;
      final boolean isPointer;

      FieldInfo(long var1, long var3, long var5, Type var7, boolean var8, boolean var9) {
         this.offset = var1;
         this.length = var3;
         this.lengthPadded = var5;
         this.type = var7;
         this.isVolatile = var8;
         this.isPointer = var9;
      }

      String getAccessType() {
         return this.isPointer ? "a" : this.type.getDescriptor().toLowerCase() + (this.isVolatile ? "v" : "");
      }
   }

   private static class TransformationAdapter extends ClassAdapter {
      final String className;
      boolean transformed;

      TransformationAdapter(ClassVisitor var1, String var2) {
         super(var1);
         this.className = var2;
      }

      public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
         MappedObjectTransformer.MappedSubtypeInfo var6 = (MappedObjectTransformer.MappedSubtypeInfo)MappedObjectTransformer.className_to_subtype.get(this.className);
         if (var6 != null && var6.fields.containsKey(var2)) {
            if (MappedObjectTransformer.PRINT_ACTIVITY) {
               LWJGLUtil.log(MappedObjectTransformer.class.getSimpleName() + ": discarding field: " + this.className + "." + var2 + ":" + var3);
            }

            return null;
         } else {
            return (FieldVisitor)((var1 & 8) == 0 ? new FieldNode(this, var1, var2, var3, var4, var5) {
               final MappedObjectTransformer.TransformationAdapter this$0;

               {
                  this.this$0 = var1;
               }

               public void visitEnd() {
                  if (this.visibleAnnotations == null) {
                     this.accept(MappedObjectTransformer.TransformationAdapter.access$000(this.this$0));
                  } else {
                     boolean var1 = false;
                     boolean var2 = false;
                     byte var3 = 0;
                     Iterator var4 = this.visibleAnnotations.iterator();

                     while(var4.hasNext()) {
                        AnnotationNode var5 = (AnnotationNode)var4.next();
                        if (MappedObjectTransformer.CACHE_LINE_PAD_JVM.equals(var5.desc)) {
                           if (!"J".equals(this.desc) && !"D".equals(this.desc)) {
                              if (!"I".equals(this.desc) && !"F".equals(this.desc)) {
                                 if (!"S".equals(this.desc) && !"C".equals(this.desc)) {
                                    if (!"B".equals(this.desc) && !"Z".equals(this.desc)) {
                                       throw new ClassFormatError("The @CacheLinePad annotation cannot be used on non-primitive fields: " + this.this$0.className + "." + this.name);
                                    }

                                    var3 = 1;
                                 } else {
                                    var3 = 2;
                                 }
                              } else {
                                 var3 = 4;
                              }
                           } else {
                              var3 = 8;
                           }

                           this.this$0.transformed = true;
                           var2 = true;
                           if (var5.values != null) {
                              for(int var6 = 0; var6 < var5.values.size(); var6 += 2) {
                                 boolean var7 = var5.values.get(var6 + 1).equals(Boolean.TRUE);
                                 if ("before".equals(var5.values.get(var6))) {
                                    var1 = var7;
                                 } else {
                                    var2 = var7;
                                 }
                              }
                           }
                           break;
                        }
                     }

                     int var8;
                     int var9;
                     if (var1) {
                        var8 = CacheUtil.getCacheLineSize() / var3 - 1;

                        for(var9 = var8; var9 >= 1; --var9) {
                           MappedObjectTransformer.TransformationAdapter.access$100(this.this$0).visitField(this.access | 1 | 4096, this.name + "$PAD_" + var9, this.desc, this.signature, (Object)null);
                        }
                     }

                     this.accept(MappedObjectTransformer.TransformationAdapter.access$200(this.this$0));
                     if (var2) {
                        var8 = CacheUtil.getCacheLineSize() / var3 - 1;

                        for(var9 = 1; var9 <= var8; ++var9) {
                           MappedObjectTransformer.TransformationAdapter.access$300(this.this$0).visitField(this.access | 1 | 4096, this.name + "$PAD" + var9, this.desc, this.signature, (Object)null);
                        }
                     }

                  }
               }
            } : super.visitField(var1, var2, var3, var4, var5));
         }
      }

      public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
         if ("<init>".equals(var2)) {
            MappedObjectTransformer.MappedSubtypeInfo var6 = (MappedObjectTransformer.MappedSubtypeInfo)MappedObjectTransformer.className_to_subtype.get(this.className);
            if (var6 != null) {
               if (!"()V".equals(var3)) {
                  throw new ClassFormatError(this.className + " can only have a default constructor, found: " + var3);
               }

               MethodVisitor var7 = super.visitMethod(var1, var2, var3, var4, var5);
               var7.visitVarInsn(25, 0);
               var7.visitMethodInsn(183, MappedObjectTransformer.MAPPED_OBJECT_JVM, "<init>", "()V");
               var7.visitInsn(177);
               var7.visitMaxs(0, 0);
               var2 = "constructView$LWJGL";
            }
         }

         MethodVisitor var8 = super.visitMethod(var1, var2, var3, var4, var5);
         return new MethodNode(this, var1, var2, var3, var4, var5, var8) {
            boolean needsTransformation;
            final MethodVisitor val$mv;
            final MappedObjectTransformer.TransformationAdapter this$0;

            {
               this.this$0 = var1;
               this.val$mv = var7;
            }

            public void visitMaxs(int var1, int var2) {
               MappedObjectTransformer.is_currently_computing_frames = true;
               super.visitMaxs(var1, var2);
               MappedObjectTransformer.is_currently_computing_frames = false;
            }

            public void visitFieldInsn(int var1, String var2, String var3, String var4) {
               if (MappedObjectTransformer.className_to_subtype.containsKey(var2) || var2.startsWith(MappedObjectTransformer.MAPPEDSET_PREFIX)) {
                  this.needsTransformation = true;
               }

               super.visitFieldInsn(var1, var2, var3, var4);
            }

            public void visitMethodInsn(int var1, String var2, String var3, String var4) {
               if (MappedObjectTransformer.className_to_subtype.containsKey(var2)) {
                  this.needsTransformation = true;
               }

               super.visitMethodInsn(var1, var2, var3, var4);
            }

            public void visitEnd() {
               if (this.needsTransformation) {
                  this.this$0.transformed = true;

                  try {
                     this.transformMethod(this.analyse());
                  } catch (Exception var2) {
                     throw new RuntimeException(var2);
                  }
               }

               this.accept(this.val$mv);
            }

            private Frame[] analyse() throws AnalyzerException {
               Analyzer var1 = new Analyzer(new SimpleVerifier());
               var1.analyze(this.this$0.className, this);
               return var1.getFrames();
            }

            private void transformMethod(Frame[] var1) {
               InsnList var2 = this.instructions;
               HashMap var3 = new HashMap();
               HashMap var4 = new HashMap();

               int var5;
               for(var5 = 0; var5 < var1.length; ++var5) {
                  var4.put(var2.get(var5), var1[var5]);
               }

               for(var5 = 0; var5 < var2.size(); ++var5) {
                  AbstractInsnNode var6 = var2.get(var5);
                  switch(var6.getType()) {
                  case 2:
                     if (var6.getOpcode() == 25) {
                        VarInsnNode var11 = (VarInsnNode)var6;
                        MappedObjectTransformer.MappedSubtypeInfo var12 = (MappedObjectTransformer.MappedSubtypeInfo)var3.get(var11.var);
                        if (var12 != null) {
                           var5 = MappedObjectTransformer.transformArrayAccess(var2, var5, var4, var11, var12, var11.var);
                        }
                     }
                  case 3:
                  default:
                     break;
                  case 4:
                     FieldInsnNode var7 = (FieldInsnNode)var6;
                     InsnList var8 = MappedObjectTransformer.transformFieldAccess(var7);
                     if (var8 != null) {
                        var5 = MappedObjectTransformer.replace(var2, var5, var6, var8);
                     }
                     break;
                  case 5:
                     MethodInsnNode var9 = (MethodInsnNode)var6;
                     MappedObjectTransformer.MappedSubtypeInfo var10 = (MappedObjectTransformer.MappedSubtypeInfo)MappedObjectTransformer.className_to_subtype.get(var9.owner);
                     if (var10 != null) {
                        var5 = MappedObjectTransformer.transformMethodCall(var2, var5, var4, var9, var10, var3);
                     }
                  }
               }

            }
         };
      }

      static ClassVisitor access$000(MappedObjectTransformer.TransformationAdapter var0) {
         return var0.cv;
      }

      static ClassVisitor access$100(MappedObjectTransformer.TransformationAdapter var0) {
         return var0.cv;
      }

      static ClassVisitor access$200(MappedObjectTransformer.TransformationAdapter var0) {
         return var0.cv;
      }

      static ClassVisitor access$300(MappedObjectTransformer.TransformationAdapter var0) {
         return var0.cv;
      }
   }
}
