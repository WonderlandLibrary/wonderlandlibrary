package gnu.trove.impl;

public class Constants {
   private static final boolean VERBOSE = System.getProperty("gnu.trove.verbose", (String)null) != null;
   public static final int DEFAULT_CAPACITY = 10;
   public static final float DEFAULT_LOAD_FACTOR = 0.5F;
   public static final byte DEFAULT_BYTE_NO_ENTRY_VALUE;
   public static final short DEFAULT_SHORT_NO_ENTRY_VALUE;
   public static final char DEFAULT_CHAR_NO_ENTRY_VALUE;
   public static final int DEFAULT_INT_NO_ENTRY_VALUE;
   public static final long DEFAULT_LONG_NO_ENTRY_VALUE;
   public static final float DEFAULT_FLOAT_NO_ENTRY_VALUE;
   public static final double DEFAULT_DOUBLE_NO_ENTRY_VALUE;

   static {
      String var1 = System.getProperty("gnu.trove.no_entry.byte", "0");
      byte var0;
      if ("MAX_VALUE".equalsIgnoreCase(var1)) {
         var0 = 127;
      } else if ("MIN_VALUE".equalsIgnoreCase(var1)) {
         var0 = -128;
      } else {
         var0 = Byte.valueOf(var1);
      }

      if (var0 > 127) {
         var0 = 127;
      } else if (var0 < -128) {
         var0 = -128;
      }

      DEFAULT_BYTE_NO_ENTRY_VALUE = var0;
      if (VERBOSE) {
         System.out.println("DEFAULT_BYTE_NO_ENTRY_VALUE: " + DEFAULT_BYTE_NO_ENTRY_VALUE);
      }

      var1 = System.getProperty("gnu.trove.no_entry.short", "0");
      short var3;
      if ("MAX_VALUE".equalsIgnoreCase(var1)) {
         var3 = 32767;
      } else if ("MIN_VALUE".equalsIgnoreCase(var1)) {
         var3 = -32768;
      } else {
         var3 = Short.valueOf(var1);
      }

      if (var3 > 32767) {
         var3 = 32767;
      } else if (var3 < -32768) {
         var3 = -32768;
      }

      DEFAULT_SHORT_NO_ENTRY_VALUE = var3;
      if (VERBOSE) {
         System.out.println("DEFAULT_SHORT_NO_ENTRY_VALUE: " + DEFAULT_SHORT_NO_ENTRY_VALUE);
      }

      var1 = System.getProperty("gnu.trove.no_entry.char", "\u0000");
      char var4;
      if ("MAX_VALUE".equalsIgnoreCase(var1)) {
         var4 = '\uffff';
      } else if ("MIN_VALUE".equalsIgnoreCase(var1)) {
         var4 = 0;
      } else {
         var4 = var1.toCharArray()[0];
      }

      if (var4 > '\uffff') {
         var4 = '\uffff';
      } else if (var4 < 0) {
         var4 = 0;
      }

      DEFAULT_CHAR_NO_ENTRY_VALUE = var4;
      if (VERBOSE) {
         System.out.println("DEFAULT_CHAR_NO_ENTRY_VALUE: " + Integer.valueOf(var4));
      }

      var1 = System.getProperty("gnu.trove.no_entry.int", "0");
      int var5;
      if ("MAX_VALUE".equalsIgnoreCase(var1)) {
         var5 = Integer.MAX_VALUE;
      } else if ("MIN_VALUE".equalsIgnoreCase(var1)) {
         var5 = Integer.MIN_VALUE;
      } else {
         var5 = Integer.valueOf(var1);
      }

      DEFAULT_INT_NO_ENTRY_VALUE = var5;
      if (VERBOSE) {
         System.out.println("DEFAULT_INT_NO_ENTRY_VALUE: " + DEFAULT_INT_NO_ENTRY_VALUE);
      }

      String var2 = System.getProperty("gnu.trove.no_entry.long", "0");
      long var6;
      if ("MAX_VALUE".equalsIgnoreCase(var2)) {
         var6 = Long.MAX_VALUE;
      } else if ("MIN_VALUE".equalsIgnoreCase(var2)) {
         var6 = Long.MIN_VALUE;
      } else {
         var6 = Long.valueOf(var2);
      }

      DEFAULT_LONG_NO_ENTRY_VALUE = var6;
      if (VERBOSE) {
         System.out.println("DEFAULT_LONG_NO_ENTRY_VALUE: " + DEFAULT_LONG_NO_ENTRY_VALUE);
      }

      var1 = System.getProperty("gnu.trove.no_entry.float", "0");
      float var7;
      if ("MAX_VALUE".equalsIgnoreCase(var1)) {
         var7 = Float.MAX_VALUE;
      } else if ("MIN_VALUE".equalsIgnoreCase(var1)) {
         var7 = Float.MIN_VALUE;
      } else if ("MIN_NORMAL".equalsIgnoreCase(var1)) {
         var7 = 1.17549435E-38F;
      } else if ("NEGATIVE_INFINITY".equalsIgnoreCase(var1)) {
         var7 = Float.NEGATIVE_INFINITY;
      } else if ("POSITIVE_INFINITY".equalsIgnoreCase(var1)) {
         var7 = Float.POSITIVE_INFINITY;
      } else {
         var7 = Float.valueOf(var1);
      }

      DEFAULT_FLOAT_NO_ENTRY_VALUE = var7;
      if (VERBOSE) {
         System.out.println("DEFAULT_FLOAT_NO_ENTRY_VALUE: " + DEFAULT_FLOAT_NO_ENTRY_VALUE);
      }

      var2 = System.getProperty("gnu.trove.no_entry.double", "0");
      double var8;
      if ("MAX_VALUE".equalsIgnoreCase(var2)) {
         var8 = Double.MAX_VALUE;
      } else if ("MIN_VALUE".equalsIgnoreCase(var2)) {
         var8 = Double.MIN_VALUE;
      } else if ("MIN_NORMAL".equalsIgnoreCase(var2)) {
         var8 = 2.2250738585072014E-308D;
      } else if ("NEGATIVE_INFINITY".equalsIgnoreCase(var2)) {
         var8 = Double.NEGATIVE_INFINITY;
      } else if ("POSITIVE_INFINITY".equalsIgnoreCase(var2)) {
         var8 = Double.POSITIVE_INFINITY;
      } else {
         var8 = Double.valueOf(var2);
      }

      DEFAULT_DOUBLE_NO_ENTRY_VALUE = var8;
      if (VERBOSE) {
         System.out.println("DEFAULT_DOUBLE_NO_ENTRY_VALUE: " + DEFAULT_DOUBLE_NO_ENTRY_VALUE);
      }

   }
}
