package com.sun.prism.paint;

public abstract class Paint {
   private final Type type;
   private final boolean proportional;
   private final boolean isMutable;

   Paint(Type var1, boolean var2, boolean var3) {
      this.type = var1;
      this.proportional = var2;
      this.isMutable = var3;
   }

   public final Type getType() {
      return this.type;
   }

   public boolean isProportional() {
      return this.proportional;
   }

   public abstract boolean isOpaque();

   public boolean isMutable() {
      return this.isMutable;
   }

   public static enum Type {
      COLOR("Color", false, false),
      LINEAR_GRADIENT("LinearGradient", true, false),
      RADIAL_GRADIENT("RadialGradient", true, false),
      IMAGE_PATTERN("ImagePattern", false, true);

      private String name;
      private boolean isGradient;
      private boolean isImagePattern;

      private Type(String var3, boolean var4, boolean var5) {
         this.name = var3;
         this.isGradient = var4;
         this.isImagePattern = var5;
      }

      public String getName() {
         return this.name;
      }

      public boolean isGradient() {
         return this.isGradient;
      }

      public boolean isImagePattern() {
         return this.isImagePattern;
      }
   }
}
