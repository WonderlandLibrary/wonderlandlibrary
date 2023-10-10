package org.lwjgl.opengl;

import java.awt.Canvas;
import org.lwjgl.LWJGLException;

final class MacOSXAWTGLCanvasPeerInfo extends MacOSXCanvasPeerInfo {
   private final Canvas component;

   MacOSXAWTGLCanvasPeerInfo(Canvas var1, PixelFormat var2, ContextAttribs var3, boolean var4) throws LWJGLException {
      super(var2, var3, var4);
      this.component = var1;
   }

   protected void doLockAndInitHandle() throws LWJGLException {
      this.initHandle(this.component);
   }
}
