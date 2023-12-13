package com.sun.javafx.tk;

import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import java.security.AccessControlContext;

public interface TKScene {
   void dispose();

   void waitForRenderingToComplete();

   void waitForSynchronization();

   void releaseSynchronization(boolean var1);

   void setTKSceneListener(TKSceneListener var1);

   void setTKScenePaintListener(TKScenePaintListener var1);

   void setRoot(NGNode var1);

   void markDirty();

   void setCamera(NGCamera var1);

   NGLightBase[] getLights();

   void setLights(NGLightBase[] var1);

   void setFillPaint(Object var1);

   void setCursor(Object var1);

   void enableInputMethodEvents(boolean var1);

   void finishInputMethodComposition();

   void entireSceneNeedsRepaint();

   TKClipboard createDragboard(boolean var1);

   AccessControlContext getAccessControlContext();
}
