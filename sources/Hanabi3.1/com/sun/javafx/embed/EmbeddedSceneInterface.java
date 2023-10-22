package com.sun.javafx.embed;

import com.sun.javafx.scene.traversal.Direction;
import java.nio.IntBuffer;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodRequests;

public interface EmbeddedSceneInterface {
   void setSize(int var1, int var2);

   void setPixelScaleFactors(float var1, float var2);

   boolean getPixels(IntBuffer var1, int var2, int var3);

   void mouseEvent(int var1, int var2, boolean var3, boolean var4, boolean var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, int var14, boolean var15);

   void keyEvent(int var1, int var2, char[] var3, int var4);

   void menuEvent(int var1, int var2, int var3, int var4, boolean var5);

   boolean traverseOut(Direction var1);

   void setDragStartListener(HostDragStartListener var1);

   EmbeddedSceneDTInterface createDropTarget();

   void inputMethodEvent(EventType var1, ObservableList var2, String var3, int var4);

   InputMethodRequests getInputMethodRequests();
}
