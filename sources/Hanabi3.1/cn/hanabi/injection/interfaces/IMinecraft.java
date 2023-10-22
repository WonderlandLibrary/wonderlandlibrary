package cn.hanabi.injection.interfaces;

import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
   Session getSession();

   void setSession(Session var1);

   LanguageManager getLanguageManager();

   Timer getTimer();

   void setRightClickDelayTimer(int var1);

   void setClickCounter(int var1);

   void runCrinkMouse();

   void runRightMouse();
}
