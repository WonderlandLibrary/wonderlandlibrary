package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;

final class MacOSXCanvasImplementation implements AWTCanvasImplementation {
   public PeerInfo createPeerInfo(Canvas var1, PixelFormat var2, ContextAttribs var3) throws LWJGLException {
      try {
         return new MacOSXAWTGLCanvasPeerInfo(var1, var2, var3, true);
      } catch (LWJGLException var5) {
         return new MacOSXAWTGLCanvasPeerInfo(var1, var2, var3, false);
      }
   }

   public GraphicsConfiguration findConfiguration(GraphicsDevice var1, PixelFormat var2) throws LWJGLException {
      return null;
   }
}
