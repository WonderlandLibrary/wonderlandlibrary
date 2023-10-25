package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengles.ContextAttribs;

public final class Display {
  private static final Thread shutdown_hook = new Thread() {
      public void run() {
        Display.reset();
      }
    };
  
  private static final DisplayImplementation display_impl;
  
  private static final DisplayMode initial_mode;
  
  private static Canvas parent;
  
  private static DisplayMode current_mode;
  
  private static int x = -1;
  
  private static ByteBuffer[] cached_icons;
  
  private static int y = -1;
  
  private static int width = 0;
  
  private static int height = 0;
  
  private static String title = "Game";
  
  private static boolean fullscreen;
  
  private static int swap_interval;
  
  private static DrawableLWJGL drawable;
  
  private static boolean window_created;
  
  private static boolean parent_resized;
  
  private static boolean window_resized;
  
  private static boolean window_resizable;
  
  private static float r;
  
  private static float g;
  
  private static float b;
  
  private static final ComponentListener component_listener = new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        synchronized (GlobalLock.lock) {
          Display.parent_resized = true;
        } 
      }
    };
  
  static {
    Sys.initialize();
    display_impl = createDisplayImplementation();
    try {
      current_mode = initial_mode = display_impl.init();
      LWJGLUtil.log("Initial mode: " + initial_mode);
    } catch (LWJGLException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public static Drawable getDrawable() {
    return drawable;
  }
  
  private static DisplayImplementation createDisplayImplementation() {
    switch (LWJGLUtil.getPlatform()) {
      case 1:
        return new LinuxDisplay();
      case 3:
        return new WindowsDisplay();
      case 2:
        return new MacOSXDisplay();
    } 
    throw new IllegalStateException("Unsupported platform");
  }
  
  public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      DisplayMode[] unfilteredModes = display_impl.getAvailableDisplayModes();
      if (unfilteredModes == null)
        return new DisplayMode[0]; 
      HashSet<DisplayMode> modes = new HashSet<DisplayMode>(unfilteredModes.length);
      modes.addAll(Arrays.asList(unfilteredModes));
      DisplayMode[] filteredModes = new DisplayMode[modes.size()];
      modes.toArray(filteredModes);
      LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");
      return filteredModes;
    } 
  }
  
  public static DisplayMode getDesktopDisplayMode() {
    return initial_mode;
  }
  
  public static DisplayMode getDisplayMode() {
    return current_mode;
  }
  
  public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (mode == null)
        throw new NullPointerException("mode must be non-null"); 
      boolean was_fullscreen = isFullscreen();
      current_mode = mode;
      if (isCreated()) {
        destroyWindow();
        try {
          if (was_fullscreen && !isFullscreen()) {
            display_impl.resetDisplayMode();
          } else if (isFullscreen()) {
            switchDisplayMode();
          } 
          createWindow();
          makeCurrentAndSetSwapInterval();
        } catch (LWJGLException e) {
          drawable.destroy();
          display_impl.resetDisplayMode();
          throw e;
        } 
      } 
    } 
  }
  
  private static DisplayMode getEffectiveMode() {
    return (!isFullscreen() && parent != null) ? new DisplayMode(parent.getWidth(), parent.getHeight()) : current_mode;
  }
  
  private static int getWindowX() {
    if (!isFullscreen() && parent == null) {
      if (x == -1)
        return Math.max(0, (initial_mode.getWidth() - current_mode.getWidth()) / 2); 
      return x;
    } 
    return 0;
  }
  
  private static int getWindowY() {
    if (!isFullscreen() && parent == null) {
      if (y == -1)
        return Math.max(0, (initial_mode.getHeight() - current_mode.getHeight()) / 2); 
      return y;
    } 
    return 0;
  }
  
  private static void createWindow() throws LWJGLException {
    if (window_created)
      return; 
    Canvas tmp_parent = isFullscreen() ? null : parent;
    if (tmp_parent != null && !tmp_parent.isDisplayable())
      throw new LWJGLException("Parent.isDisplayable() must be true"); 
    if (tmp_parent != null)
      tmp_parent.addComponentListener(component_listener); 
    DisplayMode mode = getEffectiveMode();
    display_impl.createWindow(drawable, mode, tmp_parent, getWindowX(), getWindowY());
    window_created = true;
    width = getDisplayMode().getWidth();
    height = getDisplayMode().getHeight();
    setTitle(title);
    initControls();
    if (cached_icons != null) {
      setIcon(cached_icons);
    } else {
      setIcon(new ByteBuffer[] { LWJGLUtil.LWJGLIcon32x32, LWJGLUtil.LWJGLIcon16x16 });
    } 
  }
  
  private static void releaseDrawable() {
    try {
      Context context = drawable.getContext();
      if (context != null && context.isCurrent()) {
        context.releaseCurrent();
        context.releaseDrawable();
      } 
    } catch (LWJGLException e) {
      LWJGLUtil.log("Exception occurred while trying to release context: " + e);
    } 
  }
  
  private static void destroyWindow() {
    if (!window_created)
      return; 
    if (parent != null)
      parent.removeComponentListener(component_listener); 
    releaseDrawable();
    if (Mouse.isCreated())
      Mouse.destroy(); 
    if (Keyboard.isCreated())
      Keyboard.destroy(); 
    display_impl.destroyWindow();
    window_created = false;
  }
  
  private static void switchDisplayMode() throws LWJGLException {
    if (!current_mode.isFullscreenCapable())
      throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display"); 
    display_impl.switchDisplayMode(current_mode);
  }
  
  public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new LWJGLException("Display not yet created."); 
      if (brightness < -1.0F || brightness > 1.0F)
        throw new IllegalArgumentException("Invalid brightness value"); 
      if (contrast < 0.0F)
        throw new IllegalArgumentException("Invalid contrast value"); 
      int rampSize = display_impl.getGammaRampLength();
      if (rampSize == 0)
        throw new LWJGLException("Display configuration not supported"); 
      FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
      for (int i = 0; i < rampSize; i++) {
        float intensity = i / (rampSize - 1);
        float rampEntry = (float)Math.pow(intensity, gamma);
        rampEntry += brightness;
        rampEntry = (rampEntry - 0.5F) * contrast + 0.5F;
        if (rampEntry > 1.0F) {
          rampEntry = 1.0F;
        } else if (rampEntry < 0.0F) {
          rampEntry = 0.0F;
        } 
        gammaRamp.put(i, rampEntry);
      } 
      display_impl.setGammaRamp(gammaRamp);
      LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
    } 
  }
  
  public static void sync(int fps) {
    Sync.sync(fps);
  }
  
  public static String getTitle() {
    synchronized (GlobalLock.lock) {
      return title;
    } 
  }
  
  public static Canvas getParent() {
    synchronized (GlobalLock.lock) {
      return parent;
    } 
  }
  
  public static void setParent(Canvas parent) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (Display.parent != parent) {
        Display.parent = parent;
        if (!isCreated())
          return; 
        destroyWindow();
        try {
          if (isFullscreen()) {
            switchDisplayMode();
          } else {
            display_impl.resetDisplayMode();
          } 
          createWindow();
          makeCurrentAndSetSwapInterval();
        } catch (LWJGLException e) {
          drawable.destroy();
          display_impl.resetDisplayMode();
          throw e;
        } 
      } 
    } 
  }
  
  public static void setFullscreen(boolean fullscreen) throws LWJGLException {
    setDisplayModeAndFullscreenInternal(fullscreen, current_mode);
  }
  
  public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
    setDisplayModeAndFullscreenInternal(mode.isFullscreenCapable(), mode);
  }
  
  private static void setDisplayModeAndFullscreenInternal(boolean fullscreen, DisplayMode mode) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (mode == null)
        throw new NullPointerException("mode must be non-null"); 
      DisplayMode old_mode = current_mode;
      current_mode = mode;
      boolean was_fullscreen = isFullscreen();
      Display.fullscreen = fullscreen;
      if (was_fullscreen != isFullscreen() || !mode.equals(old_mode)) {
        if (!isCreated())
          return; 
        destroyWindow();
        try {
          if (isFullscreen()) {
            switchDisplayMode();
          } else {
            display_impl.resetDisplayMode();
          } 
          createWindow();
          makeCurrentAndSetSwapInterval();
        } catch (LWJGLException e) {
          drawable.destroy();
          display_impl.resetDisplayMode();
          throw e;
        } 
      } 
    } 
  }
  
  public static boolean isFullscreen() {
    synchronized (GlobalLock.lock) {
      return (fullscreen && current_mode.isFullscreenCapable());
    } 
  }
  
  public static void setTitle(String newTitle) {
    synchronized (GlobalLock.lock) {
      if (newTitle == null)
        newTitle = ""; 
      title = newTitle;
      if (isCreated())
        display_impl.setTitle(title); 
    } 
  }
  
  public static boolean isCloseRequested() {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Cannot determine close requested state of uncreated window"); 
      return display_impl.isCloseRequested();
    } 
  }
  
  public static boolean isVisible() {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Cannot determine minimized state of uncreated window"); 
      return display_impl.isVisible();
    } 
  }
  
  public static boolean isActive() {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Cannot determine focused state of uncreated window"); 
      return display_impl.isActive();
    } 
  }
  
  public static boolean isDirty() {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Cannot determine dirty state of uncreated window"); 
      return display_impl.isDirty();
    } 
  }
  
  public static void processMessages() {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Display not created"); 
      display_impl.update();
    } 
    pollDevices();
  }
  
  public static void swapBuffers() throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Display not created"); 
      if (LWJGLUtil.DEBUG)
        drawable.checkGLError(); 
      drawable.swapBuffers();
    } 
  }
  
  public static void update() {
    update(true);
  }
  
  public static void update(boolean processMessages) {
    synchronized (GlobalLock.lock) {
      if (!isCreated())
        throw new IllegalStateException("Display not created"); 
      if (display_impl.isVisible() || display_impl.isDirty())
        try {
          swapBuffers();
        } catch (LWJGLException e) {
          throw new RuntimeException(e);
        }  
      window_resized = (!isFullscreen() && parent == null && display_impl.wasResized());
      if (window_resized) {
        width = display_impl.getWidth();
        height = display_impl.getHeight();
      } 
      if (parent_resized) {
        reshape();
        parent_resized = false;
        window_resized = true;
      } 
      if (processMessages)
        processMessages(); 
    } 
  }
  
  static void pollDevices() {
    if (Mouse.isCreated()) {
      Mouse.poll();
      Mouse.updateCursor();
    } 
    if (Keyboard.isCreated())
      Keyboard.poll(); 
    if (Controllers.isCreated())
      Controllers.poll(); 
  }
  
  public static void releaseContext() throws LWJGLException {
    drawable.releaseContext();
  }
  
  public static boolean isCurrent() throws LWJGLException {
    return drawable.isCurrent();
  }
  
  public static void makeCurrent() throws LWJGLException {
    drawable.makeCurrent();
  }
  
  private static void removeShutdownHook() {
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            Runtime.getRuntime().removeShutdownHook(Display.shutdown_hook);
            return null;
          }
        });
  }
  
  private static void registerShutdownHook() {
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            Runtime.getRuntime().addShutdownHook(Display.shutdown_hook);
            return null;
          }
        });
  }
  
  public static void create() throws LWJGLException {
    create(new PixelFormat());
  }
  
  public static void create(PixelFormat pixel_format) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, (Drawable)null, (ContextAttribs)null);
    } 
  }
  
  public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, shared_drawable, (ContextAttribs)null);
    } 
  }
  
  public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, (Drawable)null, attribs);
    } 
  }
  
  public static void create(PixelFormat pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (isCreated())
        throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time."); 
      if (pixel_format == null)
        throw new NullPointerException("pixel_format cannot be null"); 
      removeShutdownHook();
      registerShutdownHook();
      if (isFullscreen())
        switchDisplayMode(); 
      DrawableGL drawable = new DrawableGL() {
          public void destroy() {
            synchronized (GlobalLock.lock) {
              if (!Display.isCreated())
                return; 
              Display.releaseDrawable();
              super.destroy();
              Display.destroyWindow();
              Display.x = Display.y = -1;
              Display.cached_icons = null;
              Display.reset();
              Display.removeShutdownHook();
            } 
          }
        };
      Display.drawable = drawable;
      try {
        drawable.setPixelFormat(pixel_format, attribs);
        try {
          createWindow();
          try {
            drawable.context = new ContextGL(drawable.peer_info, attribs, (shared_drawable != null) ? ((DrawableGL)shared_drawable).getContext() : null);
            try {
              makeCurrentAndSetSwapInterval();
              initContext();
            } catch (LWJGLException e) {
              drawable.destroy();
              throw e;
            } 
          } catch (LWJGLException e) {
            destroyWindow();
            throw e;
          } 
        } catch (LWJGLException e) {
          drawable.destroy();
          throw e;
        } 
      } catch (LWJGLException e) {
        display_impl.resetDisplayMode();
        throw e;
      } 
    } 
  }
  
  public static void create(PixelFormatLWJGL pixel_format) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, (Drawable)null, (ContextAttribs)null);
    } 
  }
  
  public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, shared_drawable, (ContextAttribs)null);
    } 
  }
  
  public static void create(PixelFormatLWJGL pixel_format, ContextAttribs attribs) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      create(pixel_format, (Drawable)null, attribs);
    } 
  }
  
  public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
    synchronized (GlobalLock.lock) {
      if (isCreated())
        throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time."); 
      if (pixel_format == null)
        throw new NullPointerException("pixel_format cannot be null"); 
      removeShutdownHook();
      registerShutdownHook();
      if (isFullscreen())
        switchDisplayMode(); 
      DrawableGLES drawable = new DrawableGLES() {
          public void setPixelFormat(PixelFormatLWJGL pf, ContextAttribs attribs) throws LWJGLException {
            throw new UnsupportedOperationException();
          }
          
          public void destroy() {
            synchronized (GlobalLock.lock) {
              if (!Display.isCreated())
                return; 
              Display.releaseDrawable();
              super.destroy();
              Display.destroyWindow();
              Display.x = Display.y = -1;
              Display.cached_icons = null;
              Display.reset();
              Display.removeShutdownHook();
            } 
          }
        };
      Display.drawable = drawable;
      try {
        drawable.setPixelFormat(pixel_format);
        try {
          createWindow();
          try {
            drawable.createContext(attribs, shared_drawable);
            try {
              makeCurrentAndSetSwapInterval();
              initContext();
            } catch (LWJGLException e) {
              drawable.destroy();
              throw e;
            } 
          } catch (LWJGLException e) {
            destroyWindow();
            throw e;
          } 
        } catch (LWJGLException e) {
          drawable.destroy();
          throw e;
        } 
      } catch (LWJGLException e) {
        display_impl.resetDisplayMode();
        throw e;
      } 
    } 
  }
  
  public static void setInitialBackground(float red, float green, float blue) {
    r = red;
    g = green;
    b = blue;
  }
  
  private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
    makeCurrent();
    try {
      drawable.checkGLError();
    } catch (OpenGLException e) {
      LWJGLUtil.log("OpenGL error during context creation: " + e.getMessage());
    } 
    setSwapInterval(swap_interval);
  }
  
  private static void initContext() {
    drawable.initContext(r, g, b);
    update();
  }
  
  static DisplayImplementation getImplementation() {
    return display_impl;
  }
  
  static boolean getPrivilegedBoolean(final String property_name) {
    return ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>() {
          public Boolean run() {
            return Boolean.valueOf(Boolean.getBoolean(property_name));
          }
        })).booleanValue();
  }
  
  static String getPrivilegedString(final String property_name) {
    return AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
          public String run() {
            return System.getProperty(property_name);
          }
        });
  }
  
  private static void initControls() {
    if (!getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput")) {
      if (!Mouse.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse"))
        try {
          Mouse.create();
        } catch (LWJGLException e) {
          if (LWJGLUtil.DEBUG) {
            e.printStackTrace(System.err);
          } else {
            LWJGLUtil.log("Failed to create Mouse: " + e);
          } 
        }  
      if (!Keyboard.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard"))
        try {
          Keyboard.create();
        } catch (LWJGLException e) {
          if (LWJGLUtil.DEBUG) {
            e.printStackTrace(System.err);
          } else {
            LWJGLUtil.log("Failed to create Keyboard: " + e);
          } 
        }  
    } 
  }
  
  public static void destroy() {
    if (isCreated())
      drawable.destroy(); 
  }
  
  private static void reset() {
    display_impl.resetDisplayMode();
    current_mode = initial_mode;
  }
  
  public static boolean isCreated() {
    synchronized (GlobalLock.lock) {
      return window_created;
    } 
  }
  
  public static void setSwapInterval(int value) {
    synchronized (GlobalLock.lock) {
      swap_interval = value;
      if (isCreated())
        drawable.setSwapInterval(swap_interval); 
    } 
  }
  
  public static void setVSyncEnabled(boolean sync) {
    synchronized (GlobalLock.lock) {
      setSwapInterval(sync ? 1 : 0);
    } 
  }
  
  public static void setLocation(int new_x, int new_y) {
    synchronized (GlobalLock.lock) {
      x = new_x;
      y = new_y;
      if (isCreated() && !isFullscreen())
        reshape(); 
    } 
  }
  
  private static void reshape() {
    DisplayMode mode = getEffectiveMode();
    display_impl.reshape(getWindowX(), getWindowY(), mode.getWidth(), mode.getHeight());
  }
  
  public static String getAdapter() {
    synchronized (GlobalLock.lock) {
      return display_impl.getAdapter();
    } 
  }
  
  public static String getVersion() {
    synchronized (GlobalLock.lock) {
      return display_impl.getVersion();
    } 
  }
  
  public static int setIcon(ByteBuffer[] icons) {
    synchronized (GlobalLock.lock) {
      if (cached_icons != icons) {
        cached_icons = new ByteBuffer[icons.length];
        for (int i = 0; i < icons.length; i++) {
          cached_icons[i] = BufferUtils.createByteBuffer(icons[i].capacity());
          int old_position = icons[i].position();
          cached_icons[i].put(icons[i]);
          icons[i].position(old_position);
          cached_icons[i].flip();
        } 
      } 
      if (isCreated() && parent == null)
        return display_impl.setIcon(cached_icons); 
      return 0;
    } 
  }
  
  public static void setResizable(boolean resizable) {
    window_resizable = resizable;
    if (isCreated())
      display_impl.setResizable(resizable); 
  }
  
  public static boolean isResizable() {
    return window_resizable;
  }
  
  public static boolean wasResized() {
    return window_resized;
  }
  
  public static int getX() {
    if (isFullscreen())
      return 0; 
    if (parent != null)
      return parent.getX(); 
    return display_impl.getX();
  }
  
  public static int getY() {
    if (isFullscreen())
      return 0; 
    if (parent != null)
      return parent.getY(); 
    return display_impl.getY();
  }
  
  public static int getWidth() {
    if (isFullscreen())
      return getDisplayMode().getWidth(); 
    if (parent != null)
      return parent.getWidth(); 
    return width;
  }
  
  public static int getHeight() {
    if (isFullscreen())
      return getDisplayMode().getHeight(); 
    if (parent != null)
      return parent.getHeight(); 
    return height;
  }
  
  public static float getPixelScaleFactor() {
    return display_impl.getPixelScaleFactor();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\Display.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */