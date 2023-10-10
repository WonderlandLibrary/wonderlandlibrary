package javassist.convert;

import javassist.CannotCompileException;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.Analyzer;
import javassist.bytecode.analysis.Frame;

public final class TransformAccessArrayField extends Transformer {
   private final String methodClassname;
   private final CodeConverter.ArrayAccessReplacementMethodNames names;
   private Frame[] frames;
   private int offset;

   public TransformAccessArrayField(Transformer var1, String var2, CodeConverter.ArrayAccessReplacementMethodNames var3) throws NotFoundException {
      super(var1);
      this.methodClassname = var2;
      this.names = var3;
   }

   public void initialize(ConstPool var1, CtClass var2, MethodInfo var3) throws CannotCompileException {
      CodeIterator var4 = var3.getCodeAttribute().iterator();

      while(var4.hasNext()) {
         try {
            int var5 = var4.next();
            int var6 = var4.byteAt(var5);
            if (var6 == 50) {
               this.initFrames(var2, var3);
            }

            if (var6 != 50 && var6 != 51 && var6 != 52 && var6 != 49 && var6 != 48 && var6 != 46 && var6 != 47 && var6 != 53) {
               if (var6 == 83 || var6 == 84 || var6 == 85 || var6 == 82 || var6 == 81 || var6 == 79 || var6 == 80 || var6 == 86) {
                  this.replace(var1, var4, var5, var6, this.getStoreReplacementSignature(var6));
               }
            } else {
               this.replace(var1, var4, var5, var6, this.getLoadReplacementSignature(var6));
            }
         } catch (Exception var7) {
            throw new CannotCompileException(var7);
         }
      }

   }

   public void clean() {
      this.frames = null;
      this.offset = -1;
   }

   public int transform(CtClass var1, int var2, CodeIterator var3, ConstPool var4) throws BadBytecode {
      return var2;
   }

   private Frame getFrame(int var1) throws BadBytecode {
      return this.frames[var1 - this.offset];
   }

   private void initFrames(CtClass var1, MethodInfo var2) throws BadBytecode {
      if (this.frames == null) {
         this.frames = (new Analyzer()).analyze(var1, var2);
         this.offset = 0;
      }

   }

   private int updatePos(int var1, int var2) {
      if (this.offset > -1) {
         this.offset += var2;
      }

      return var1 + var2;
   }

   private String getTopType(int var1) throws BadBytecode {
      Frame var2 = this.getFrame(var1);
      if (var2 == null) {
         return null;
      } else {
         CtClass var3 = var2.peek().getCtClass();
         return var3 != null ? Descriptor.toJvmName(var3) : null;
      }
   }

   private int replace(ConstPool var1, CodeIterator var2, int var3, int var4, String var5) throws BadBytecode {
      String var6 = null;
      String var7 = this.getMethodName(var4);
      if (var7 != null) {
         if (var4 == 50) {
            var6 = this.getTopType(var2.lookAhead());
            if (var6 == null) {
               return var3;
            }

            if ("java/lang/Object".equals(var6)) {
               var6 = null;
            }
         }

         var2.writeByte(0, var3);
         CodeIterator.Gap var8 = var2.insertGapAt(var3, var6 != null ? 5 : 2, false);
         var3 = var8.position;
         int var9 = var1.addClassInfo(this.methodClassname);
         int var10 = var1.addMethodrefInfo(var9, var7, var5);
         var2.writeByte(184, var3);
         var2.write16bit(var10, var3 + 1);
         if (var6 != null) {
            int var11 = var1.addClassInfo(var6);
            var2.writeByte(192, var3 + 3);
            var2.write16bit(var11, var3 + 4);
         }

         var3 = this.updatePos(var3, var8.length);
      }

      return var3;
   }

   private String getMethodName(int var1) {
      String var2 = null;
      switch(var1) {
      case 46:
         var2 = this.names.intRead();
         break;
      case 47:
         var2 = this.names.longRead();
         break;
      case 48:
         var2 = this.names.floatRead();
         break;
      case 49:
         var2 = this.names.doubleRead();
         break;
      case 50:
         var2 = this.names.objectRead();
         break;
      case 51:
         var2 = this.names.byteOrBooleanRead();
         break;
      case 52:
         var2 = this.names.charRead();
         break;
      case 53:
         var2 = this.names.shortRead();
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      default:
         break;
      case 79:
         var2 = this.names.intWrite();
         break;
      case 80:
         var2 = this.names.longWrite();
         break;
      case 81:
         var2 = this.names.floatWrite();
         break;
      case 82:
         var2 = this.names.doubleWrite();
         break;
      case 83:
         var2 = this.names.objectWrite();
         break;
      case 84:
         var2 = this.names.byteOrBooleanWrite();
         break;
      case 85:
         var2 = this.names.charWrite();
         break;
      case 86:
         var2 = this.names.shortWrite();
      }

      if (var2.equals("")) {
         var2 = null;
      }

      return var2;
   }

   private String getLoadReplacementSignature(int var1) throws BadBytecode {
      switch(var1) {
      case 46:
         return "(Ljava/lang/Object;I)I";
      case 47:
         return "(Ljava/lang/Object;I)J";
      case 48:
         return "(Ljava/lang/Object;I)F";
      case 49:
         return "(Ljava/lang/Object;I)D";
      case 50:
         return "(Ljava/lang/Object;I)Ljava/lang/Object;";
      case 51:
         return "(Ljava/lang/Object;I)B";
      case 52:
         return "(Ljava/lang/Object;I)C";
      case 53:
         return "(Ljava/lang/Object;I)S";
      default:
         throw new BadBytecode(var1);
      }
   }

   private String getStoreReplacementSignature(int var1) throws BadBytecode {
      switch(var1) {
      case 79:
         return "(Ljava/lang/Object;II)V";
      case 80:
         return "(Ljava/lang/Object;IJ)V";
      case 81:
         return "(Ljava/lang/Object;IF)V";
      case 82:
         return "(Ljava/lang/Object;ID)V";
      case 83:
         return "(Ljava/lang/Object;ILjava/lang/Object;)V";
      case 84:
         return "(Ljava/lang/Object;IB)V";
      case 85:
         return "(Ljava/lang/Object;IC)V";
      case 86:
         return "(Ljava/lang/Object;IS)V";
      default:
         throw new BadBytecode(var1);
      }
   }
}
