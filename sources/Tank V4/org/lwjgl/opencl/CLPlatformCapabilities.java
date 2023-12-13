package org.lwjgl.opencl;

import java.util.Set;
import java.util.StringTokenizer;

public class CLPlatformCapabilities {
   public final int majorVersion;
   public final int minorVersion;
   public final boolean OpenCL11;
   public final boolean OpenCL12;
   final boolean CL_APPLE_ContextLoggingFunctions;
   public final boolean CL_APPLE_SetMemObjectDestructor;
   public final boolean CL_APPLE_gl_sharing;
   public final boolean CL_KHR_d3d10_sharing;
   public final boolean CL_KHR_gl_event;
   public final boolean CL_KHR_gl_sharing;
   public final boolean CL_KHR_icd;

   public CLPlatformCapabilities(CLPlatform var1) {
      String var2 = var1.getInfoString(2308);
      String var3 = var1.getInfoString(2305);
      if (!var3.startsWith("OpenCL ")) {
         throw new RuntimeException("Invalid OpenCL version string: " + var3);
      } else {
         try {
            StringTokenizer var4 = new StringTokenizer(var3.substring(7), ". ");
            this.majorVersion = Integer.parseInt(var4.nextToken());
            this.minorVersion = Integer.parseInt(var4.nextToken());
            this.OpenCL11 = 1 < this.majorVersion || 1 == this.majorVersion && 1 <= this.minorVersion;
            this.OpenCL12 = 1 < this.majorVersion || 1 == this.majorVersion && 2 <= this.minorVersion;
         } catch (RuntimeException var5) {
            throw new RuntimeException("The major and/or minor OpenCL version \"" + var3 + "\" is malformed: " + var5.getMessage());
         }

         Set var6 = APIUtil.getExtensions(var2);
         this.CL_APPLE_ContextLoggingFunctions = var6.contains("cl_apple_contextloggingfunctions") && CLCapabilities.CL_APPLE_ContextLoggingFunctions;
         this.CL_APPLE_SetMemObjectDestructor = var6.contains("cl_apple_setmemobjectdestructor") && CLCapabilities.CL_APPLE_SetMemObjectDestructor;
         this.CL_APPLE_gl_sharing = var6.contains("cl_apple_gl_sharing") && CLCapabilities.CL_APPLE_gl_sharing;
         this.CL_KHR_d3d10_sharing = var6.contains("cl_khr_d3d10_sharing");
         this.CL_KHR_gl_event = var6.contains("cl_khr_gl_event") && CLCapabilities.CL_KHR_gl_event;
         this.CL_KHR_gl_sharing = var6.contains("cl_khr_gl_sharing") && CLCapabilities.CL_KHR_gl_sharing;
         this.CL_KHR_icd = var6.contains("cl_khr_icd") && CLCapabilities.CL_KHR_icd;
      }
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("OpenCL ").append(this.majorVersion).append('.').append(this.minorVersion);
      var1.append(" - Extensions: ");
      if (this.CL_APPLE_ContextLoggingFunctions) {
         var1.append("cl_apple_contextloggingfunctions ");
      }

      if (this.CL_APPLE_SetMemObjectDestructor) {
         var1.append("cl_apple_setmemobjectdestructor ");
      }

      if (this.CL_APPLE_gl_sharing) {
         var1.append("cl_apple_gl_sharing ");
      }

      if (this.CL_KHR_d3d10_sharing) {
         var1.append("cl_khr_d3d10_sharing ");
      }

      if (this.CL_KHR_gl_event) {
         var1.append("cl_khr_gl_event ");
      }

      if (this.CL_KHR_gl_sharing) {
         var1.append("cl_khr_gl_sharing ");
      }

      if (this.CL_KHR_icd) {
         var1.append("cl_khr_icd ");
      }

      return var1.toString();
   }
}
