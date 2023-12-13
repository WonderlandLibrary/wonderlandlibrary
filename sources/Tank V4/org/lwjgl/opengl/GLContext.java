package org.lwjgl.opengl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.Sys;

public final class GLContext {
   private static final ThreadLocal current_capabilities = new ThreadLocal();
   private static GLContext.CapabilitiesCacheEntry fast_path_cache = new GLContext.CapabilitiesCacheEntry();
   private static final ThreadLocal thread_cache_entries = new ThreadLocal();
   private static final Map capability_cache = new WeakHashMap();
   private static int gl_ref_count;
   private static boolean did_auto_load;

   public static ContextCapabilities getCapabilities() {
      ContextCapabilities var0 = getCapabilitiesImpl();
      if (var0 == null) {
         throw new RuntimeException("No OpenGL context found in the current thread.");
      } else {
         return var0;
      }
   }

   private static ContextCapabilities getCapabilitiesImpl() {
      GLContext.CapabilitiesCacheEntry var0 = fast_path_cache;
      return var0.owner == Thread.currentThread() ? var0.capabilities : getThreadLocalCapabilities();
   }

   static ContextCapabilities getCapabilities(Object var0) {
      return (ContextCapabilities)capability_cache.get(var0);
   }

   private static ContextCapabilities getThreadLocalCapabilities() {
      return (ContextCapabilities)current_capabilities.get();
   }

   static void setCapabilities(ContextCapabilities var0) {
      current_capabilities.set(var0);
      GLContext.CapabilitiesCacheEntry var1 = (GLContext.CapabilitiesCacheEntry)thread_cache_entries.get();
      if (var1 == null) {
         var1 = new GLContext.CapabilitiesCacheEntry();
         thread_cache_entries.set(var1);
      }

      var1.owner = Thread.currentThread();
      var1.capabilities = var0;
      fast_path_cache = var1;
   }

   static long getPlatformSpecificFunctionAddress(String var0, String[] var1, String[] var2, String var3) {
      String var4 = (String)AccessController.doPrivileged(new PrivilegedAction() {
         public String run() {
            return System.getProperty("os.name");
         }

         public Object run() {
            return this.run();
         }
      });

      for(int var5 = 0; var5 < var1.length; ++var5) {
         if (var4.startsWith(var1[var5])) {
            String var6 = var3.replaceFirst(var0, var2[var5]);
            long var7 = getFunctionAddress(var6);
            return var7;
         }
      }

      return 0L;
   }

   static long getFunctionAddress(String[] var0) {
      String[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         long var5 = getFunctionAddress(var4);
         if (var5 != 0L) {
            return var5;
         }
      }

      return 0L;
   }

   static long getFunctionAddress(String var0) {
      ByteBuffer var1 = MemoryUtil.encodeASCII(var0);
      return ngetFunctionAddress(MemoryUtil.getAddress(var1));
   }

   private static native long ngetFunctionAddress(long var0);

   static int getSupportedExtensions(Set var0) {
      String var1 = GL11.glGetString(7938);
      if (var1 == null) {
         throw new IllegalStateException("glGetString(GL_VERSION) returned null - possibly caused by missing current context.");
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, ". ");
         String var3 = var2.nextToken();
         String var4 = var2.nextToken();
         int var5 = 0;
         int var6 = 0;

         try {
            var5 = Integer.parseInt(var3);
            var6 = Integer.parseInt(var4);
         } catch (NumberFormatException var15) {
            LWJGLUtil.log("The major and/or minor OpenGL version is malformed: " + var15.getMessage());
         }

         int[][] var7 = new int[][]{{1, 2, 3, 4, 5}, {0, 1}, {0, 1, 2, 3}, {0, 1, 2, 3, 4}};

         int var8;
         for(var8 = 1; var8 <= var7.length; ++var8) {
            int[] var9 = var7[var8 - 1];
            int[] var10 = var9;
            int var11 = var9.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               int var13 = var10[var12];
               if (var8 < var5 || var8 == var5 && var13 <= var6) {
                  var0.add("OpenGL" + Integer.toString(var8) + Integer.toString(var13));
               }
            }
         }

         var8 = 0;
         if (var5 < 3) {
            String var16 = GL11.glGetString(7939);
            if (var16 == null) {
               throw new IllegalStateException("glGetString(GL_EXTENSIONS) returned null - is there a context current?");
            }

            StringTokenizer var18 = new StringTokenizer(var16);

            while(var18.hasMoreTokens()) {
               var0.add(var18.nextToken());
            }
         } else {
            int var17 = GL11.glGetInteger(33309);

            for(int var19 = 0; var19 < var17; ++var19) {
               var0.add(GL30.glGetStringi(7939, var19));
            }

            if (3 < var5 || 2 <= var6) {
               Util.checkGLError();

               try {
                  var8 = GL11.glGetInteger(37158);
                  Util.checkGLError();
               } catch (OpenGLException var14) {
                  LWJGLUtil.log("Failed to retrieve CONTEXT_PROFILE_MASK");
               }
            }
         }

         return var8;
      }
   }

   static void initNativeStubs(Class var0, Set var1, String var2) {
      resetNativeStubs(var0);
      if (var1.contains(var2)) {
         try {
            AccessController.doPrivileged(new PrivilegedExceptionAction(var0) {
               final Class val$extension_class;

               {
                  this.val$extension_class = var1;
               }

               public Object run() throws Exception {
                  Method var1 = this.val$extension_class.getDeclaredMethod("initNativeStubs");
                  var1.invoke((Object)null);
                  return null;
               }
            });
         } catch (Exception var4) {
            LWJGLUtil.log("Failed to initialize extension " + var0 + " - exception: " + var4);
            var1.remove(var2);
         }
      }

   }

   public static synchronized void useContext(Object var0) throws LWJGLException {
      useContext(var0, false);
   }

   public static synchronized void useContext(Object var0, boolean var1) throws LWJGLException {
      if (var0 == null) {
         ContextCapabilities.unloadAllStubs();
         setCapabilities((ContextCapabilities)null);
         if (did_auto_load) {
            unloadOpenGLLibrary();
         }

      } else {
         if (gl_ref_count == 0) {
            loadOpenGLLibrary();
            did_auto_load = true;
         }

         try {
            ContextCapabilities var2 = (ContextCapabilities)capability_cache.get(var0);
            if (var2 == null) {
               new ContextCapabilities(var1);
               capability_cache.put(var0, getCapabilities());
            } else {
               setCapabilities(var2);
            }

         } catch (LWJGLException var3) {
            if (did_auto_load) {
               unloadOpenGLLibrary();
            }

            throw var3;
         }
      }
   }

   public static synchronized void loadOpenGLLibrary() throws LWJGLException {
      if (gl_ref_count == 0) {
         nLoadOpenGLLibrary();
      }

      ++gl_ref_count;
   }

   private static native void nLoadOpenGLLibrary() throws LWJGLException;

   public static synchronized void unloadOpenGLLibrary() {
      --gl_ref_count;
      if (gl_ref_count == 0 && LWJGLUtil.getPlatform() != 1) {
         nUnloadOpenGLLibrary();
      }

   }

   private static native void nUnloadOpenGLLibrary();

   static native void resetNativeStubs(Class var0);

   static {
      Sys.initialize();
   }

   private static final class CapabilitiesCacheEntry {
      Thread owner;
      ContextCapabilities capabilities;

      private CapabilitiesCacheEntry() {
      }

      CapabilitiesCacheEntry(Object var1) {
         this();
      }
   }
}
