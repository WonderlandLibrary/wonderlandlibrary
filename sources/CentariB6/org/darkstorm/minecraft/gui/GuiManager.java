package org.darkstorm.minecraft.gui;

import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.theme.Theme;

public interface GuiManager {
   void update();

   void setup();

   Frame[] getFrames();

   Theme getTheme();

   void render();

   void setTheme(Theme var1);

   void renderPinned();

   void bringForward(Frame var1);

   void removeFrame(Frame var1);

   void addFrame(Frame var1);
}
