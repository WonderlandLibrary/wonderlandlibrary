package org.apache.commons.lang3;

public enum JavaVersion {
   JAVA_0_9(1.5F, "0.9"),
   JAVA_1_1(1.1F, "1.1"),
   JAVA_1_2(1.2F, "1.2"),
   JAVA_1_3(1.3F, "1.3"),
   JAVA_1_4(1.4F, "1.4"),
   JAVA_1_5(1.5F, "1.5"),
   JAVA_1_6(1.6F, "1.6"),
   JAVA_1_7(1.7F, "1.7"),
   JAVA_1_8(1.8F, "1.8");

   private final float value;
   private final String name;
   private static final JavaVersion[] $VALUES = new JavaVersion[]{JAVA_0_9, JAVA_1_1, JAVA_1_2, JAVA_1_3, JAVA_1_4, JAVA_1_5, JAVA_1_6, JAVA_1_7, JAVA_1_8};

   private JavaVersion(float var3, String var4) {
      this.value = var3;
      this.name = var4;
   }

   public boolean atLeast(JavaVersion var1) {
      return this.value >= var1.value;
   }

   static JavaVersion getJavaVersion(String var0) {
      return get(var0);
   }

   static JavaVersion get(String var0) {
      if ("0.9".equals(var0)) {
         return JAVA_0_9;
      } else if ("1.1".equals(var0)) {
         return JAVA_1_1;
      } else if ("1.2".equals(var0)) {
         return JAVA_1_2;
      } else if ("1.3".equals(var0)) {
         return JAVA_1_3;
      } else if ("1.4".equals(var0)) {
         return JAVA_1_4;
      } else if ("1.5".equals(var0)) {
         return JAVA_1_5;
      } else if ("1.6".equals(var0)) {
         return JAVA_1_6;
      } else if ("1.7".equals(var0)) {
         return JAVA_1_7;
      } else {
         return "1.8".equals(var0) ? JAVA_1_8 : null;
      }
   }

   public String toString() {
      return this.name;
   }
}
