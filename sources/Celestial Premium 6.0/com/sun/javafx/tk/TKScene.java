/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import java.security.AccessControlContext;

public interface TKScene {
    public void dispose();

    public void waitForRenderingToComplete();

    public void waitForSynchronization();

    public void releaseSynchronization(boolean var1);

    public void setTKSceneListener(TKSceneListener var1);

    public void setTKScenePaintListener(TKScenePaintListener var1);

    public void setRoot(NGNode var1);

    public void markDirty();

    public void setCamera(NGCamera var1);

    public NGLightBase[] getLights();

    public void setLights(NGLightBase[] var1);

    public void setFillPaint(Object var1);

    public void setCursor(Object var1);

    public void enableInputMethodEvents(boolean var1);

    public void finishInputMethodComposition();

    public void entireSceneNeedsRepaint();

    public TKClipboard createDragboard(boolean var1);

    public AccessControlContext getAccessControlContext();
}

