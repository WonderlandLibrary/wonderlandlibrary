package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javassist.CtClass;

public class SignatureAttribute extends AttributeInfo {
   public static final String tag = "Signature";

   SignatureAttribute(ConstPool var1, int var2, DataInputStream var3) throws IOException {
      super(var1, var2, var3);
   }

   public SignatureAttribute(ConstPool var1, String var2) {
      super(var1, "Signature");
      int var3 = var1.addUtf8Info(var2);
      byte[] var4 = new byte[]{(byte)(var3 >>> 8), (byte)var3};
      this.set(var4);
   }

   public String getSignature() {
      return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
   }

   public void setSignature(String var1) {
      int var2 = this.getConstPool().addUtf8Info(var1);
      ByteArray.write16bit(var2, this.info, 0);
   }

   public AttributeInfo copy(ConstPool var1, Map var2) {
      return new SignatureAttribute(var1, this.getSignature());
   }

   void renameClass(String var1, String var2) {
      String var3 = renameClass(this.getSignature(), var1, var2);
      this.setSignature(var3);
   }

   void renameClass(Map var1) {
      String var2 = renameClass(this.getSignature(), var1);
      this.setSignature(var2);
   }

   static String renameClass(String var0, String var1, String var2) {
      HashMap var3 = new HashMap();
      var3.put(var1, var2);
      return renameClass(var0, (Map)var3);
   }

   static String renameClass(String var0, Map var1) {
      if (var1 == null) {
         return var0;
      } else {
         StringBuilder var2 = new StringBuilder();
         int var3 = 0;
         int var4 = 0;

         int var5;
         while(true) {
            var5 = var0.indexOf(76, var4);
            if (var5 < 0) {
               break;
            }

            StringBuilder var6 = new StringBuilder();
            int var7 = var5;

            char var8;
            try {
               while(true) {
                  ++var7;
                  if ((var8 = var0.charAt(var7)) == ';') {
                     break;
                  }

                  var6.append(var8);
                  if (var8 == '<') {
                     while(true) {
                        ++var7;
                        if ((var8 = var0.charAt(var7)) == '>') {
                           var6.append(var8);
                           break;
                        }

                        var6.append(var8);
                     }
                  }
               }
            } catch (IndexOutOfBoundsException var11) {
               break;
            }

            var4 = var7 + 1;
            String var9 = var6.toString();
            String var10 = (String)var1.get(var9);
            if (var10 != null) {
               var2.append(var0.substring(var3, var5));
               var2.append('L');
               var2.append(var10);
               var2.append(var8);
               var3 = var4;
            }
         }

         if (var3 == 0) {
            return var0;
         } else {
            var5 = var0.length();
            if (var3 < var5) {
               var2.append(var0.substring(var3, var5));
            }

            return var2.toString();
         }
      }
   }

   private static boolean isNamePart(int var0) {
      return var0 != 59 && var0 != 60;
   }

   public static SignatureAttribute.ClassSignature toClassSignature(String var0) throws BadBytecode {
      try {
         return parseSig(var0);
      } catch (IndexOutOfBoundsException var2) {
         throw error(var0);
      }
   }

   public static SignatureAttribute.MethodSignature toMethodSignature(String var0) throws BadBytecode {
      try {
         return parseMethodSig(var0);
      } catch (IndexOutOfBoundsException var2) {
         throw error(var0);
      }
   }

   public static SignatureAttribute.ObjectType toFieldSignature(String var0) throws BadBytecode {
      try {
         return parseObjectType(var0, new SignatureAttribute.Cursor(), false);
      } catch (IndexOutOfBoundsException var2) {
         throw error(var0);
      }
   }

   public static SignatureAttribute.Type toTypeSignature(String var0) throws BadBytecode {
      try {
         return parseType(var0, new SignatureAttribute.Cursor());
      } catch (IndexOutOfBoundsException var2) {
         throw error(var0);
      }
   }

   private static SignatureAttribute.ClassSignature parseSig(String var0) throws BadBytecode, IndexOutOfBoundsException {
      SignatureAttribute.Cursor var1 = new SignatureAttribute.Cursor();
      SignatureAttribute.TypeParameter[] var2 = parseTypeParams(var0, var1);
      SignatureAttribute.ClassType var3 = parseClassType(var0, var1);
      int var4 = var0.length();
      ArrayList var5 = new ArrayList();

      while(var1.position < var4 && var0.charAt(var1.position) == 'L') {
         var5.add(parseClassType(var0, var1));
      }

      SignatureAttribute.ClassType[] var6 = (SignatureAttribute.ClassType[])((SignatureAttribute.ClassType[])var5.toArray(new SignatureAttribute.ClassType[var5.size()]));
      return new SignatureAttribute.ClassSignature(var2, var3, var6);
   }

   private static SignatureAttribute.MethodSignature parseMethodSig(String var0) throws BadBytecode {
      SignatureAttribute.Cursor var1 = new SignatureAttribute.Cursor();
      SignatureAttribute.TypeParameter[] var2 = parseTypeParams(var0, var1);
      if (var0.charAt(var1.position++) != '(') {
         throw error(var0);
      } else {
         ArrayList var3 = new ArrayList();

         SignatureAttribute.Type var4;
         while(var0.charAt(var1.position) != ')') {
            var4 = parseType(var0, var1);
            var3.add(var4);
         }

         ++var1.position;
         var4 = parseType(var0, var1);
         int var5 = var0.length();
         ArrayList var6 = new ArrayList();

         while(var1.position < var5 && var0.charAt(var1.position) == '^') {
            ++var1.position;
            SignatureAttribute.ObjectType var7 = parseObjectType(var0, var1, false);
            if (var7 instanceof SignatureAttribute.ArrayType) {
               throw error(var0);
            }

            var6.add(var7);
         }

         SignatureAttribute.Type[] var9 = (SignatureAttribute.Type[])((SignatureAttribute.Type[])var3.toArray(new SignatureAttribute.Type[var3.size()]));
         SignatureAttribute.ObjectType[] var8 = (SignatureAttribute.ObjectType[])((SignatureAttribute.ObjectType[])var6.toArray(new SignatureAttribute.ObjectType[var6.size()]));
         return new SignatureAttribute.MethodSignature(var2, var9, var4, var8);
      }
   }

   private static SignatureAttribute.TypeParameter[] parseTypeParams(String var0, SignatureAttribute.Cursor var1) throws BadBytecode {
      ArrayList var2 = new ArrayList();
      if (var0.charAt(var1.position) == '<') {
         ++var1.position;

         while(var0.charAt(var1.position) != '>') {
            int var3 = var1.position;
            int var4 = var1.indexOf(var0, 58);
            SignatureAttribute.ObjectType var5 = parseObjectType(var0, var1, true);
            ArrayList var6 = new ArrayList();

            while(var0.charAt(var1.position) == ':') {
               ++var1.position;
               SignatureAttribute.ObjectType var7 = parseObjectType(var0, var1, false);
               var6.add(var7);
            }

            SignatureAttribute.TypeParameter var8 = new SignatureAttribute.TypeParameter(var0, var3, var4, var5, (SignatureAttribute.ObjectType[])((SignatureAttribute.ObjectType[])var6.toArray(new SignatureAttribute.ObjectType[var6.size()])));
            var2.add(var8);
         }

         ++var1.position;
      }

      return (SignatureAttribute.TypeParameter[])((SignatureAttribute.TypeParameter[])var2.toArray(new SignatureAttribute.TypeParameter[var2.size()]));
   }

   private static SignatureAttribute.ObjectType parseObjectType(String var0, SignatureAttribute.Cursor var1, boolean var2) throws BadBytecode {
      int var4 = var1.position;
      switch(var0.charAt(var4)) {
      case 'L':
         return parseClassType2(var0, var1, (SignatureAttribute.ClassType)null);
      case 'T':
         int var3 = var1.indexOf(var0, 59);
         return new SignatureAttribute.TypeVariable(var0, var4 + 1, var3);
      case '[':
         return parseArray(var0, var1);
      default:
         if (var2) {
            return null;
         } else {
            throw error(var0);
         }
      }
   }

   private static SignatureAttribute.ClassType parseClassType(String var0, SignatureAttribute.Cursor var1) throws BadBytecode {
      if (var0.charAt(var1.position) == 'L') {
         return parseClassType2(var0, var1, (SignatureAttribute.ClassType)null);
      } else {
         throw error(var0);
      }
   }

   private static SignatureAttribute.ClassType parseClassType2(String var0, SignatureAttribute.Cursor var1, SignatureAttribute.ClassType var2) throws BadBytecode {
      int var3 = ++var1.position;

      char var4;
      do {
         var4 = var0.charAt(var1.position++);
      } while(var4 != '$' && var4 != '<' && var4 != ';');

      int var5 = var1.position - 1;
      SignatureAttribute.TypeArgument[] var6;
      if (var4 == '<') {
         var6 = parseTypeArgs(var0, var1);
         var4 = var0.charAt(var1.position++);
      } else {
         var6 = null;
      }

      SignatureAttribute.ClassType var7 = SignatureAttribute.ClassType.make(var0, var3, var5, var6, var2);
      if (var4 != '$' && var4 != '.') {
         return var7;
      } else {
         --var1.position;
         return parseClassType2(var0, var1, var7);
      }
   }

   private static SignatureAttribute.TypeArgument[] parseTypeArgs(String var0, SignatureAttribute.Cursor var1) throws BadBytecode {
      ArrayList var2;
      char var3;
      SignatureAttribute.TypeArgument var4;
      for(var2 = new ArrayList(); (var3 = var0.charAt(var1.position++)) != '>'; var2.add(var4)) {
         if (var3 == '*') {
            var4 = new SignatureAttribute.TypeArgument((SignatureAttribute.ObjectType)null, '*');
         } else {
            if (var3 != '+' && var3 != '-') {
               var3 = ' ';
               --var1.position;
            }

            var4 = new SignatureAttribute.TypeArgument(parseObjectType(var0, var1, false), var3);
         }
      }

      return (SignatureAttribute.TypeArgument[])((SignatureAttribute.TypeArgument[])var2.toArray(new SignatureAttribute.TypeArgument[var2.size()]));
   }

   private static SignatureAttribute.ObjectType parseArray(String var0, SignatureAttribute.Cursor var1) throws BadBytecode {
      int var2;
      for(var2 = 1; var0.charAt(++var1.position) == '['; ++var2) {
      }

      return new SignatureAttribute.ArrayType(var2, parseType(var0, var1));
   }

   private static SignatureAttribute.Type parseType(String var0, SignatureAttribute.Cursor var1) throws BadBytecode {
      Object var2 = parseObjectType(var0, var1, true);
      if (var2 == null) {
         var2 = new SignatureAttribute.BaseType(var0.charAt(var1.position++));
      }

      return (SignatureAttribute.Type)var2;
   }

   private static BadBytecode error(String var0) {
      return new BadBytecode("bad signature: " + var0);
   }

   static BadBytecode access$000(String var0) {
      return error(var0);
   }

   public static class TypeVariable extends SignatureAttribute.ObjectType {
      String name;

      TypeVariable(String var1, int var2, int var3) {
         this.name = var1.substring(var2, var3);
      }

      public TypeVariable(String var1) {
         this.name = var1;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      void encode(StringBuffer var1) {
         var1.append('T').append(this.name).append(';');
      }
   }

   public static class ArrayType extends SignatureAttribute.ObjectType {
      int dim;
      SignatureAttribute.Type componentType;

      public ArrayType(int var1, SignatureAttribute.Type var2) {
         this.dim = var1;
         this.componentType = var2;
      }

      public int getDimension() {
         return this.dim;
      }

      public SignatureAttribute.Type getComponentType() {
         return this.componentType;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(this.componentType.toString());

         for(int var2 = 0; var2 < this.dim; ++var2) {
            var1.append("[]");
         }

         return var1.toString();
      }

      void encode(StringBuffer var1) {
         for(int var2 = 0; var2 < this.dim; ++var2) {
            var1.append('[');
         }

         this.componentType.encode(var1);
      }
   }

   public static class NestedClassType extends SignatureAttribute.ClassType {
      SignatureAttribute.ClassType parent;

      NestedClassType(String var1, int var2, int var3, SignatureAttribute.TypeArgument[] var4, SignatureAttribute.ClassType var5) {
         super(var1, var2, var3, var4);
         this.parent = var5;
      }

      public NestedClassType(SignatureAttribute.ClassType var1, String var2, SignatureAttribute.TypeArgument[] var3) {
         super(var2, var3);
         this.parent = var1;
      }

      public SignatureAttribute.ClassType getDeclaringClass() {
         return this.parent;
      }
   }

   public static class ClassType extends SignatureAttribute.ObjectType {
      String name;
      SignatureAttribute.TypeArgument[] arguments;
      public static SignatureAttribute.ClassType OBJECT = new SignatureAttribute.ClassType("java.lang.Object", (SignatureAttribute.TypeArgument[])null);

      static SignatureAttribute.ClassType make(String var0, int var1, int var2, SignatureAttribute.TypeArgument[] var3, SignatureAttribute.ClassType var4) {
         return (SignatureAttribute.ClassType)(var4 == null ? new SignatureAttribute.ClassType(var0, var1, var2, var3) : new SignatureAttribute.NestedClassType(var0, var1, var2, var3, var4));
      }

      ClassType(String var1, int var2, int var3, SignatureAttribute.TypeArgument[] var4) {
         this.name = var1.substring(var2, var3).replace('/', '.');
         this.arguments = var4;
      }

      public ClassType(String var1, SignatureAttribute.TypeArgument[] var2) {
         this.name = var1;
         this.arguments = var2;
      }

      public ClassType(String var1) {
         this(var1, (SignatureAttribute.TypeArgument[])null);
      }

      public String getName() {
         return this.name;
      }

      public SignatureAttribute.TypeArgument[] getTypeArguments() {
         return this.arguments;
      }

      public SignatureAttribute.ClassType getDeclaringClass() {
         return null;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         SignatureAttribute.ClassType var2 = this.getDeclaringClass();
         if (var2 != null) {
            var1.append(var2.toString()).append('.');
         }

         return this.toString2(var1);
      }

      private String toString2(StringBuffer var1) {
         var1.append(this.name);
         if (this.arguments != null) {
            var1.append('<');
            int var2 = this.arguments.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               if (var3 > 0) {
                  var1.append(", ");
               }

               var1.append(this.arguments[var3].toString());
            }

            var1.append('>');
         }

         return var1.toString();
      }

      public String jvmTypeName() {
         StringBuffer var1 = new StringBuffer();
         SignatureAttribute.ClassType var2 = this.getDeclaringClass();
         if (var2 != null) {
            var1.append(var2.jvmTypeName()).append('$');
         }

         return this.toString2(var1);
      }

      void encode(StringBuffer var1) {
         var1.append('L');
         this.encode2(var1);
         var1.append(';');
      }

      void encode2(StringBuffer var1) {
         SignatureAttribute.ClassType var2 = this.getDeclaringClass();
         if (var2 != null) {
            var2.encode2(var1);
            var1.append('$');
         }

         var1.append(this.name.replace('.', '/'));
         if (this.arguments != null) {
            SignatureAttribute.TypeArgument.encode(var1, this.arguments);
         }

      }
   }

   public abstract static class ObjectType extends SignatureAttribute.Type {
      public String encode() {
         StringBuffer var1 = new StringBuffer();
         this.encode(var1);
         return var1.toString();
      }
   }

   public static class BaseType extends SignatureAttribute.Type {
      char descriptor;

      BaseType(char var1) {
         this.descriptor = var1;
      }

      public BaseType(String var1) {
         this(Descriptor.of(var1).charAt(0));
      }

      public char getDescriptor() {
         return this.descriptor;
      }

      public CtClass getCtlass() {
         return Descriptor.toPrimitiveClass(this.descriptor);
      }

      public String toString() {
         return Descriptor.toClassName(Character.toString(this.descriptor));
      }

      void encode(StringBuffer var1) {
         var1.append(this.descriptor);
      }
   }

   public abstract static class Type {
      abstract void encode(StringBuffer var1);

      static void toString(StringBuffer var0, SignatureAttribute.Type[] var1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var2 > 0) {
               var0.append(", ");
            }

            var0.append(var1[var2]);
         }

      }

      public String jvmTypeName() {
         return this.toString();
      }
   }

   public static class TypeArgument {
      SignatureAttribute.ObjectType arg;
      char wildcard;

      TypeArgument(SignatureAttribute.ObjectType var1, char var2) {
         this.arg = var1;
         this.wildcard = var2;
      }

      public TypeArgument(SignatureAttribute.ObjectType var1) {
         this(var1, ' ');
      }

      public TypeArgument() {
         this((SignatureAttribute.ObjectType)null, '*');
      }

      public static SignatureAttribute.TypeArgument subclassOf(SignatureAttribute.ObjectType var0) {
         return new SignatureAttribute.TypeArgument(var0, '+');
      }

      public static SignatureAttribute.TypeArgument superOf(SignatureAttribute.ObjectType var0) {
         return new SignatureAttribute.TypeArgument(var0, '-');
      }

      public char getKind() {
         return this.wildcard;
      }

      public SignatureAttribute.ObjectType getType() {
         return this.arg;
      }

      public String toString() {
         if (this.wildcard == '*') {
            return "?";
         } else {
            String var1 = this.arg.toString();
            if (this.wildcard == ' ') {
               return var1;
            } else {
               return this.wildcard == '+' ? "? extends " + var1 : "? super " + var1;
            }
         }
      }

      static void encode(StringBuffer param0, SignatureAttribute.TypeArgument[] param1) {
         // $FF: Couldn't be decompiled
      }
   }

   public static class TypeParameter {
      String name;
      SignatureAttribute.ObjectType superClass;
      SignatureAttribute.ObjectType[] superInterfaces;

      TypeParameter(String var1, int var2, int var3, SignatureAttribute.ObjectType var4, SignatureAttribute.ObjectType[] var5) {
         this.name = var1.substring(var2, var3);
         this.superClass = var4;
         this.superInterfaces = var5;
      }

      public TypeParameter(String var1, SignatureAttribute.ObjectType var2, SignatureAttribute.ObjectType[] var3) {
         this.name = var1;
         this.superClass = var2;
         if (var3 == null) {
            this.superInterfaces = new SignatureAttribute.ObjectType[0];
         } else {
            this.superInterfaces = var3;
         }

      }

      public TypeParameter(String var1) {
         this(var1, (SignatureAttribute.ObjectType)null, (SignatureAttribute.ObjectType[])null);
      }

      public String getName() {
         return this.name;
      }

      public SignatureAttribute.ObjectType getClassBound() {
         return this.superClass;
      }

      public SignatureAttribute.ObjectType[] getInterfaceBound() {
         return this.superInterfaces;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(this.getName());
         if (this.superClass != null) {
            var1.append(" extends ").append(this.superClass.toString());
         }

         int var2 = this.superInterfaces.length;
         if (var2 > 0) {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (var3 <= 0 && this.superClass == null) {
                  var1.append(" extends ");
               } else {
                  var1.append(" & ");
               }

               var1.append(this.superInterfaces[var3].toString());
            }
         }

         return var1.toString();
      }

      static void toString(StringBuffer var0, SignatureAttribute.TypeParameter[] var1) {
         var0.append('<');

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var2 > 0) {
               var0.append(", ");
            }

            var0.append(var1[var2]);
         }

         var0.append('>');
      }

      void encode(StringBuffer var1) {
         var1.append(this.name);
         if (this.superClass == null) {
            var1.append(":Ljava/lang/Object;");
         } else {
            var1.append(':');
            this.superClass.encode(var1);
         }

         for(int var2 = 0; var2 < this.superInterfaces.length; ++var2) {
            var1.append(':');
            this.superInterfaces[var2].encode(var1);
         }

      }
   }

   public static class MethodSignature {
      SignatureAttribute.TypeParameter[] typeParams;
      SignatureAttribute.Type[] params;
      SignatureAttribute.Type retType;
      SignatureAttribute.ObjectType[] exceptions;

      public MethodSignature(SignatureAttribute.TypeParameter[] var1, SignatureAttribute.Type[] var2, SignatureAttribute.Type var3, SignatureAttribute.ObjectType[] var4) {
         this.typeParams = var1 == null ? new SignatureAttribute.TypeParameter[0] : var1;
         this.params = var2 == null ? new SignatureAttribute.Type[0] : var2;
         this.retType = (SignatureAttribute.Type)(var3 == null ? new SignatureAttribute.BaseType("void") : var3);
         this.exceptions = var4 == null ? new SignatureAttribute.ObjectType[0] : var4;
      }

      public SignatureAttribute.TypeParameter[] getTypeParameters() {
         return this.typeParams;
      }

      public SignatureAttribute.Type[] getParameterTypes() {
         return this.params;
      }

      public SignatureAttribute.Type getReturnType() {
         return this.retType;
      }

      public SignatureAttribute.ObjectType[] getExceptionTypes() {
         return this.exceptions;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         SignatureAttribute.TypeParameter.toString(var1, this.typeParams);
         var1.append(" (");
         SignatureAttribute.Type.toString(var1, this.params);
         var1.append(") ");
         var1.append(this.retType);
         if (this.exceptions.length > 0) {
            var1.append(" throws ");
            SignatureAttribute.Type.toString(var1, this.exceptions);
         }

         return var1.toString();
      }

      public String encode() {
         StringBuffer var1 = new StringBuffer();
         int var2;
         if (this.typeParams.length > 0) {
            var1.append('<');

            for(var2 = 0; var2 < this.typeParams.length; ++var2) {
               this.typeParams[var2].encode(var1);
            }

            var1.append('>');
         }

         var1.append('(');

         for(var2 = 0; var2 < this.params.length; ++var2) {
            this.params[var2].encode(var1);
         }

         var1.append(')');
         this.retType.encode(var1);
         if (this.exceptions.length > 0) {
            for(var2 = 0; var2 < this.exceptions.length; ++var2) {
               var1.append('^');
               this.exceptions[var2].encode(var1);
            }
         }

         return var1.toString();
      }
   }

   public static class ClassSignature {
      SignatureAttribute.TypeParameter[] params;
      SignatureAttribute.ClassType superClass;
      SignatureAttribute.ClassType[] interfaces;

      public ClassSignature(SignatureAttribute.TypeParameter[] var1, SignatureAttribute.ClassType var2, SignatureAttribute.ClassType[] var3) {
         this.params = var1 == null ? new SignatureAttribute.TypeParameter[0] : var1;
         this.superClass = var2 == null ? SignatureAttribute.ClassType.OBJECT : var2;
         this.interfaces = var3 == null ? new SignatureAttribute.ClassType[0] : var3;
      }

      public ClassSignature(SignatureAttribute.TypeParameter[] var1) {
         this(var1, (SignatureAttribute.ClassType)null, (SignatureAttribute.ClassType[])null);
      }

      public SignatureAttribute.TypeParameter[] getParameters() {
         return this.params;
      }

      public SignatureAttribute.ClassType getSuperClass() {
         return this.superClass;
      }

      public SignatureAttribute.ClassType[] getInterfaces() {
         return this.interfaces;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         SignatureAttribute.TypeParameter.toString(var1, this.params);
         var1.append(" extends ").append(this.superClass);
         if (this.interfaces.length > 0) {
            var1.append(" implements ");
            SignatureAttribute.Type.toString(var1, this.interfaces);
         }

         return var1.toString();
      }

      public String encode() {
         StringBuffer var1 = new StringBuffer();
         int var2;
         if (this.params.length > 0) {
            var1.append('<');

            for(var2 = 0; var2 < this.params.length; ++var2) {
               this.params[var2].encode(var1);
            }

            var1.append('>');
         }

         this.superClass.encode(var1);

         for(var2 = 0; var2 < this.interfaces.length; ++var2) {
            this.interfaces[var2].encode(var1);
         }

         return var1.toString();
      }
   }

   private static class Cursor {
      int position;

      private Cursor() {
         this.position = 0;
      }

      int indexOf(String var1, int var2) throws BadBytecode {
         int var3 = var1.indexOf(var2, this.position);
         if (var3 < 0) {
            throw SignatureAttribute.access$000(var1);
         } else {
            this.position = var3 + 1;
            return var3;
         }
      }

      Cursor(Object var1) {
         this();
      }
   }
}
