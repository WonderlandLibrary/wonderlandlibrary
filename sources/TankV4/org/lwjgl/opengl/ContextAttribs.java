package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

public final class ContextAttribs {
   private static final int CONTEXT_ES2_PROFILE_BIT_EXT = 4;
   private static final int CONTEXT_ROBUST_ACCESS_BIT_ARB = 4;
   private static final int CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
   private static final int NO_RESET_NOTIFICATION_ARB = 33377;
   private static final int LOSE_CONTEXT_ON_RESET_ARB = 33362;
   private static final int CONTEXT_RESET_ISOLATION_BIT_ARB = 8;
   private int majorVersion;
   private int minorVersion;
   private int layerPlane;
   private boolean debug;
   private boolean forwardCompatible;
   private boolean robustAccess;
   private boolean profileCore;
   private boolean profileCompatibility;
   private boolean profileES;
   private boolean loseContextOnReset;
   private boolean contextResetIsolation;

   public ContextAttribs() {
      this(1, 0);
   }

   public ContextAttribs(int var1, int var2) {
      if (var1 >= 0 && 4 >= var1 && var2 >= 0 && (var1 != 4 || 4 >= var2) && (var1 != 3 || 3 >= var2) && (var1 != 2 || 1 >= var2) && (var1 != 1 || 5 >= var2)) {
         this.majorVersion = var1;
         this.minorVersion = var2;
      } else {
         throw new IllegalArgumentException("Invalid OpenGL version specified: " + var1 + '.' + var2);
      }
   }

   private ContextAttribs(ContextAttribs var1) {
      this.majorVersion = var1.majorVersion;
      this.minorVersion = var1.minorVersion;
      this.layerPlane = var1.layerPlane;
      this.debug = var1.debug;
      this.forwardCompatible = var1.forwardCompatible;
      this.robustAccess = var1.robustAccess;
      this.profileCore = var1.profileCore;
      this.profileCompatibility = var1.profileCompatibility;
      this.profileES = var1.profileES;
      this.loseContextOnReset = var1.loseContextOnReset;
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public int getLayerPlane() {
      return this.layerPlane;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public boolean isForwardCompatible() {
      return this.forwardCompatible;
   }

   public boolean isProfileCore() {
      return this.profileCore;
   }

   public boolean isProfileCompatibility() {
      return this.profileCompatibility;
   }

   public boolean isProfileES() {
      return this.profileES;
   }

   public ContextAttribs withLayer(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid layer plane specified: " + var1);
      } else if (var1 == this.layerPlane) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.layerPlane = var1;
         return var2;
      }
   }

   public ContextAttribs withDebug(boolean var1) {
      if (var1 == this.debug) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.debug = var1;
         return var2;
      }
   }

   public ContextAttribs withForwardCompatible(boolean var1) {
      if (var1 == this.forwardCompatible) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.forwardCompatible = var1;
         return var2;
      }
   }

   public ContextAttribs withProfileCore(boolean var1) {
      if (this.majorVersion < 3 || this.majorVersion == 3 && this.minorVersion < 2) {
         throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
      } else if (var1 == this.profileCore) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.profileCore = var1;
         if (var1) {
            var2.profileCompatibility = false;
         }

         return var2;
      }
   }

   public ContextAttribs withProfileCompatibility(boolean var1) {
      if (this.majorVersion < 3 || this.majorVersion == 3 && this.minorVersion < 2) {
         throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");
      } else if (var1 == this.profileCompatibility) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.profileCompatibility = var1;
         if (var1) {
            var2.profileCore = false;
         }

         return var2;
      }
   }

   public ContextAttribs withProfileES(boolean var1) {
      if (this.majorVersion == 2 && this.minorVersion == 0) {
         if (var1 == this.profileES) {
            return this;
         } else {
            ContextAttribs var2 = new ContextAttribs(this);
            var2.profileES = var1;
            return var2;
         }
      } else {
         throw new IllegalArgumentException("The OpenGL ES profiles is only supported for OpenGL version 2.0.");
      }
   }

   public ContextAttribs withLoseContextOnReset(boolean var1) {
      if (var1 == this.loseContextOnReset) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.loseContextOnReset = var1;
         return var2;
      }
   }

   public ContextAttribs withContextResetIsolation(boolean var1) {
      if (var1 == this.contextResetIsolation) {
         return this;
      } else {
         ContextAttribs var2 = new ContextAttribs(this);
         var2.contextResetIsolation = var1;
         return var2;
      }
   }

   private static ContextAttribsImplementation getImplementation() {
      switch(LWJGLUtil.getPlatform()) {
      case 1:
         return new LinuxContextAttribs();
      case 3:
         return new WindowsContextAttribs();
      default:
         throw new IllegalStateException("Unsupported platform");
      }
   }

   IntBuffer getAttribList() {
      if (LWJGLUtil.getPlatform() == 2) {
         return null;
      } else {
         ContextAttribsImplementation var1 = getImplementation();
         int var2 = 0;
         if (this.majorVersion != 1 || this.minorVersion != 0) {
            var2 += 2;
         }

         if (0 < this.layerPlane) {
            ++var2;
         }

         int var3 = 0;
         if (this.debug) {
            var3 |= var1.getDebugBit();
         }

         if (this.forwardCompatible) {
            var3 |= var1.getForwardCompatibleBit();
         }

         if (this.robustAccess) {
            var3 |= 4;
         }

         if (this.contextResetIsolation) {
            var3 |= 8;
         }

         if (0 < var3) {
            ++var2;
         }

         int var4 = 0;
         if (this.profileCore) {
            var4 |= var1.getProfileCoreBit();
         } else if (this.profileCompatibility) {
            var4 |= var1.getProfileCompatibilityBit();
         } else if (this.profileES) {
            var4 |= 4;
         }

         if (0 < var4) {
            ++var2;
         }

         if (this.loseContextOnReset) {
            ++var2;
         }

         if (var2 == 0) {
            return null;
         } else {
            IntBuffer var5 = BufferUtils.createIntBuffer(var2 * 2 + 1);
            if (this.majorVersion != 1 || this.minorVersion != 0) {
               var5.put(var1.getMajorVersionAttrib()).put(this.majorVersion);
               var5.put(var1.getMinorVersionAttrib()).put(this.minorVersion);
            }

            if (0 < this.layerPlane) {
               var5.put(var1.getLayerPlaneAttrib()).put(this.layerPlane);
            }

            if (0 < var3) {
               var5.put(var1.getFlagsAttrib()).put(var3);
            }

            if (0 < var4) {
               var5.put(var1.getProfileMaskAttrib()).put(var4);
            }

            if (this.loseContextOnReset) {
               var5.put(33366).put(33362);
            }

            var5.put(0);
            var5.rewind();
            return var5;
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(32);
      var1.append("ContextAttribs:");
      var1.append(" Version=").append(this.majorVersion).append('.').append(this.minorVersion);
      var1.append(" - Layer=").append(this.layerPlane);
      var1.append(" - Debug=").append(this.debug);
      var1.append(" - ForwardCompatible=").append(this.forwardCompatible);
      var1.append(" - RobustAccess=").append(this.robustAccess);
      if (this.robustAccess) {
         var1.append(" (").append(this.loseContextOnReset ? "LOSE_CONTEXT_ON_RESET" : "NO_RESET_NOTIFICATION");
      }

      var1.append(" - Profile=");
      if (this.profileCore) {
         var1.append("Core");
      } else if (this.profileCompatibility) {
         var1.append("Compatibility");
      } else {
         var1.append("None");
      }

      return var1.toString();
   }
}
