package javassist.bytecode;

public class AccessFlag {
   public static final int PUBLIC = 1;
   public static final int PRIVATE = 2;
   public static final int PROTECTED = 4;
   public static final int STATIC = 8;
   public static final int FINAL = 16;
   public static final int SYNCHRONIZED = 32;
   public static final int VOLATILE = 64;
   public static final int BRIDGE = 64;
   public static final int TRANSIENT = 128;
   public static final int VARARGS = 128;
   public static final int NATIVE = 256;
   public static final int INTERFACE = 512;
   public static final int ABSTRACT = 1024;
   public static final int STRICT = 2048;
   public static final int SYNTHETIC = 4096;
   public static final int ANNOTATION = 8192;
   public static final int ENUM = 16384;
   public static final int MANDATED = 32768;
   public static final int SUPER = 32;

   public static int setPublic(int var0) {
      return var0 & -7 | 1;
   }

   public static int setProtected(int var0) {
      return var0 & -4 | 4;
   }

   public static int setPrivate(int var0) {
      return var0 & -6 | 2;
   }

   public static int setPackage(int var0) {
      return var0 & -8;
   }

   public static boolean isPublic(int var0) {
      return (var0 & 1) != 0;
   }

   public static boolean isProtected(int var0) {
      return (var0 & 4) != 0;
   }

   public static boolean isPrivate(int var0) {
      return (var0 & 2) != 0;
   }

   public static boolean isPackage(int var0) {
      return (var0 & 7) == 0;
   }

   public static int clear(int var0, int var1) {
      return var0 & ~var1;
   }

   public static int of(int var0) {
      return var0;
   }

   public static int toModifier(int var0) {
      return var0;
   }
}
