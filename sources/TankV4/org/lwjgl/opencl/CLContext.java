package org.lwjgl.opencl;

import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

public final class CLContext extends CLObjectChild {
   private static final CLContext.CLContextUtil util = (CLContext.CLContextUtil)CLPlatform.getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");
   private final CLObjectRegistry clCommandQueues;
   private final CLObjectRegistry clMems;
   private final CLObjectRegistry clSamplers;
   private final CLObjectRegistry clPrograms;
   private final CLObjectRegistry clEvents;
   private long contextCallback;
   private long printfCallback;

   CLContext(long var1, CLPlatform var3) {
      super(var1, var3);
      if (this.isValid()) {
         this.clCommandQueues = new CLObjectRegistry();
         this.clMems = new CLObjectRegistry();
         this.clSamplers = new CLObjectRegistry();
         this.clPrograms = new CLObjectRegistry();
         this.clEvents = new CLObjectRegistry();
      } else {
         this.clCommandQueues = null;
         this.clMems = null;
         this.clSamplers = null;
         this.clPrograms = null;
         this.clEvents = null;
      }

   }

   public CLCommandQueue getCLCommandQueue(long var1) {
      return (CLCommandQueue)this.clCommandQueues.getObject(var1);
   }

   public CLMem getCLMem(long var1) {
      return (CLMem)this.clMems.getObject(var1);
   }

   public CLSampler getCLSampler(long var1) {
      return (CLSampler)this.clSamplers.getObject(var1);
   }

   public CLProgram getCLProgram(long var1) {
      return (CLProgram)this.clPrograms.getObject(var1);
   }

   public CLEvent getCLEvent(long var1) {
      return (CLEvent)this.clEvents.getObject(var1);
   }

   public static CLContext create(CLPlatform var0, List var1, IntBuffer var2) throws LWJGLException {
      return create(var0, var1, (CLContextCallback)null, (Drawable)null, var2);
   }

   public static CLContext create(CLPlatform var0, List var1, CLContextCallback var2, IntBuffer var3) throws LWJGLException {
      return create(var0, var1, var2, (Drawable)null, var3);
   }

   public static CLContext create(CLPlatform var0, List var1, CLContextCallback var2, Drawable var3, IntBuffer var4) throws LWJGLException {
      return util.create(var0, var1, var2, var3, var4);
   }

   public static CLContext createFromType(CLPlatform var0, long var1, IntBuffer var3) throws LWJGLException {
      return util.createFromType(var0, var1, (CLContextCallback)null, (Drawable)null, var3);
   }

   public static CLContext createFromType(CLPlatform var0, long var1, CLContextCallback var3, IntBuffer var4) throws LWJGLException {
      return util.createFromType(var0, var1, var3, (Drawable)null, var4);
   }

   public static CLContext createFromType(CLPlatform var0, long var1, CLContextCallback var3, Drawable var4, IntBuffer var5) throws LWJGLException {
      return util.createFromType(var0, var1, var3, var4, var5);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public List getInfoDevices() {
      return util.getInfoDevices(this);
   }

   public List getSupportedImageFormats(long var1, int var3) {
      return this.getSupportedImageFormats(var1, var3, (Filter)null);
   }

   public List getSupportedImageFormats(long var1, int var3, Filter var4) {
      return util.getSupportedImageFormats(this, var1, var3, var4);
   }

   CLObjectRegistry getCLCommandQueueRegistry() {
      return this.clCommandQueues;
   }

   CLObjectRegistry getCLMemRegistry() {
      return this.clMems;
   }

   CLObjectRegistry getCLSamplerRegistry() {
      return this.clSamplers;
   }

   CLObjectRegistry getCLProgramRegistry() {
      return this.clPrograms;
   }

   CLObjectRegistry getCLEventRegistry() {
      return this.clEvents;
   }

   void setContextCallback(long var1) {
      this.contextCallback = var1;
   }

   void setPrintfCallback(long var1, int var3) {
      if (var3 == 0) {
         this.printfCallback = var1;
      }

   }

   void releaseImpl() {
      if (this.release() <= 0) {
         if (this.contextCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.contextCallback);
         }

         if (this.printfCallback != 0L) {
            CallbackUtil.deleteGlobalRef(this.printfCallback);
         }

      }
   }

   interface CLContextUtil extends InfoUtil {
      List getInfoDevices(CLContext var1);

      CLContext create(CLPlatform var1, List var2, CLContextCallback var3, Drawable var4, IntBuffer var5) throws LWJGLException;

      CLContext createFromType(CLPlatform var1, long var2, CLContextCallback var4, Drawable var5, IntBuffer var6) throws LWJGLException;

      List getSupportedImageFormats(CLContext var1, long var2, int var4, Filter var5);
   }
}
