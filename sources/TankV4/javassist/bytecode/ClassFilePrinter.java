package javassist.bytecode;

import java.io.PrintWriter;
import java.util.List;
import javassist.Modifier;

public class ClassFilePrinter {
   public static void print(ClassFile var0) {
      print(var0, new PrintWriter(System.out, true));
   }

   public static void print(ClassFile var0, PrintWriter var1) {
      int var4 = AccessFlag.toModifier(var0.getAccessFlags() & -33);
      var1.println("major: " + var0.major + ", minor: " + var0.minor + " modifiers: " + Integer.toHexString(var0.getAccessFlags()));
      var1.println(Modifier.toString(var4) + " class " + var0.getName() + " extends " + var0.getSuperclass());
      String[] var5 = var0.getInterfaces();
      int var6;
      if (var5 != null && var5.length > 0) {
         var1.print("    implements ");
         var1.print(var5[0]);

         for(var6 = 1; var6 < var5.length; ++var6) {
            var1.print(", " + var5[var6]);
         }

         var1.println();
      }

      var1.println();
      List var2 = var0.getFields();
      int var3 = var2.size();

      int var8;
      for(var6 = 0; var6 < var3; ++var6) {
         FieldInfo var7 = (FieldInfo)var2.get(var6);
         var8 = var7.getAccessFlags();
         var1.println(Modifier.toString(AccessFlag.toModifier(var8)) + " " + var7.getName() + "\t" + var7.getDescriptor());
         printAttributes(var7.getAttributes(), var1, 'f');
      }

      var1.println();
      var2 = var0.getMethods();
      var3 = var2.size();

      for(var6 = 0; var6 < var3; ++var6) {
         MethodInfo var9 = (MethodInfo)var2.get(var6);
         var8 = var9.getAccessFlags();
         var1.println(Modifier.toString(AccessFlag.toModifier(var8)) + " " + var9.getName() + "\t" + var9.getDescriptor());
         printAttributes(var9.getAttributes(), var1, 'm');
         var1.println();
      }

      var1.println();
      printAttributes(var0.getAttributes(), var1, 'c');
   }

   static void printAttributes(List var0, PrintWriter var1, char var2) {
      if (var0 != null) {
         int var3 = var0.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            AttributeInfo var5 = (AttributeInfo)var0.get(var4);
            if (var5 instanceof CodeAttribute) {
               CodeAttribute var6 = (CodeAttribute)var5;
               var1.println("attribute: " + var5.getName() + ": " + var5.getClass().getName());
               var1.println("max stack " + var6.getMaxStack() + ", max locals " + var6.getMaxLocals() + ", " + var6.getExceptionTable().size() + " catch blocks");
               var1.println("<code attribute begin>");
               printAttributes(var6.getAttributes(), var1, var2);
               var1.println("<code attribute end>");
            } else if (var5 instanceof AnnotationsAttribute) {
               var1.println("annnotation: " + var5.toString());
            } else if (var5 instanceof ParameterAnnotationsAttribute) {
               var1.println("parameter annnotations: " + var5.toString());
            } else if (var5 instanceof StackMapTable) {
               var1.println("<stack map table begin>");
               StackMapTable.Printer.print((StackMapTable)var5, var1);
               var1.println("<stack map table end>");
            } else if (var5 instanceof StackMap) {
               var1.println("<stack map begin>");
               ((StackMap)var5).print(var1);
               var1.println("<stack map end>");
            } else if (var5 instanceof SignatureAttribute) {
               SignatureAttribute var10 = (SignatureAttribute)var5;
               String var7 = var10.getSignature();
               var1.println("signature: " + var7);

               try {
                  String var8;
                  if (var2 == 'c') {
                     var8 = SignatureAttribute.toClassSignature(var7).toString();
                  } else if (var2 == 'm') {
                     var8 = SignatureAttribute.toMethodSignature(var7).toString();
                  } else {
                     var8 = SignatureAttribute.toFieldSignature(var7).toString();
                  }

                  var1.println("           " + var8);
               } catch (BadBytecode var9) {
                  var1.println("           syntax error");
               }
            } else {
               var1.println("attribute: " + var5.getName() + " (" + var5.get().length + " byte): " + var5.getClass().getName());
            }
         }

      }
   }
}
