package org.lwjgl.opencl;

import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

public final class CLContext extends CLObjectChild<CLPlatform> {
  private static final CLContextUtil util = (CLContextUtil)CLPlatform.<CLContext>getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");
  
  private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
  
  private final CLObjectRegistry<CLMem> clMems;
  
  private final CLObjectRegistry<CLSampler> clSamplers;
  
  private final CLObjectRegistry<CLProgram> clPrograms;
  
  private final CLObjectRegistry<CLEvent> clEvents;
  
  private long contextCallback;
  
  private long printfCallback;
  
  CLContext(long pointer, CLPlatform platform) {
    super(pointer, platform);
    if (isValid()) {
      this.clCommandQueues = new CLObjectRegistry<CLCommandQueue>();
      this.clMems = new CLObjectRegistry<CLMem>();
      this.clSamplers = new CLObjectRegistry<CLSampler>();
      this.clPrograms = new CLObjectRegistry<CLProgram>();
      this.clEvents = new CLObjectRegistry<CLEvent>();
    } else {
      this.clCommandQueues = null;
      this.clMems = null;
      this.clSamplers = null;
      this.clPrograms = null;
      this.clEvents = null;
    } 
  }
  
  public CLCommandQueue getCLCommandQueue(long id) {
    return this.clCommandQueues.getObject(id);
  }
  
  public CLMem getCLMem(long id) {
    return this.clMems.getObject(id);
  }
  
  public CLSampler getCLSampler(long id) {
    return this.clSamplers.getObject(id);
  }
  
  public CLProgram getCLProgram(long id) {
    return this.clPrograms.getObject(id);
  }
  
  public CLEvent getCLEvent(long id) {
    return this.clEvents.getObject(id);
  }
  
  public static CLContext create(CLPlatform platform, List<CLDevice> devices, IntBuffer errcode_ret) throws LWJGLException {
    return create(platform, devices, (CLContextCallback)null, (Drawable)null, errcode_ret);
  }
  
  public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
    return create(platform, devices, pfn_notify, (Drawable)null, errcode_ret);
  }
  
  public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
    return util.create(platform, devices, pfn_notify, share_drawable, errcode_ret);
  }
  
  public static CLContext createFromType(CLPlatform platform, long device_type, IntBuffer errcode_ret) throws LWJGLException {
    return util.createFromType(platform, device_type, null, null, errcode_ret);
  }
  
  public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
    return util.createFromType(platform, device_type, pfn_notify, null, errcode_ret);
  }
  
  public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
    return util.createFromType(platform, device_type, pfn_notify, share_drawable, errcode_ret);
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public List<CLDevice> getInfoDevices() {
    return util.getInfoDevices(this);
  }
  
  public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type) {
    return getSupportedImageFormats(flags, image_type, (Filter<CLImageFormat>)null);
  }
  
  public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type, Filter<CLImageFormat> filter) {
    return util.getSupportedImageFormats(this, flags, image_type, filter);
  }
  
  CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() {
    return this.clCommandQueues;
  }
  
  CLObjectRegistry<CLMem> getCLMemRegistry() {
    return this.clMems;
  }
  
  CLObjectRegistry<CLSampler> getCLSamplerRegistry() {
    return this.clSamplers;
  }
  
  CLObjectRegistry<CLProgram> getCLProgramRegistry() {
    return this.clPrograms;
  }
  
  CLObjectRegistry<CLEvent> getCLEventRegistry() {
    return this.clEvents;
  }
  
  private boolean checkCallback(long callback, int result) {
    if (result == 0 && (callback == 0L || isValid()))
      return true; 
    if (callback != 0L)
      CallbackUtil.deleteGlobalRef(callback); 
    return false;
  }
  
  void setContextCallback(long callback) {
    if (checkCallback(callback, 0))
      this.contextCallback = callback; 
  }
  
  void setPrintfCallback(long callback, int result) {
    if (checkCallback(callback, result))
      this.printfCallback = callback; 
  }
  
  void releaseImpl() {
    if (release() > 0)
      return; 
    if (this.contextCallback != 0L)
      CallbackUtil.deleteGlobalRef(this.contextCallback); 
    if (this.printfCallback != 0L)
      CallbackUtil.deleteGlobalRef(this.printfCallback); 
  }
  
  static interface CLContextUtil extends InfoUtil<CLContext> {
    List<CLDevice> getInfoDevices(CLContext param1CLContext);
    
    CLContext create(CLPlatform param1CLPlatform, List<CLDevice> param1List, CLContextCallback param1CLContextCallback, Drawable param1Drawable, IntBuffer param1IntBuffer) throws LWJGLException;
    
    CLContext createFromType(CLPlatform param1CLPlatform, long param1Long, CLContextCallback param1CLContextCallback, Drawable param1Drawable, IntBuffer param1IntBuffer) throws LWJGLException;
    
    List<CLImageFormat> getSupportedImageFormats(CLContext param1CLContext, long param1Long, int param1Int, Filter<CLImageFormat> param1Filter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */