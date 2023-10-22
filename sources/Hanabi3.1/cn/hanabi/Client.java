package cn.hanabi;

import aLph4anTi1eaK_cN.Annotation.Setup;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventWorldChange;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

public class Client {
   public static String username;
   public static String rank;
   public static boolean active = true;
   public static boolean onDebug = false;
   public static boolean map = true;
   public static WorldClient worldChange;
   public static boolean isGameInit = false;
   public static float pitch;
   public static boolean sleep = false;

   public static void onGameLoop() {
      isGameInit = true;
      WorldClient world = Minecraft.getMinecraft().theWorld;
      if (worldChange == null) {
         worldChange = world;
      } else if (world == null) {
         worldChange = null;
      } else {
         if (worldChange != world) {
            worldChange = world;
            EventManager.call(new EventWorldChange());
         }

         EventManager.call(new EventLoop());
      }
   }

   @Setup
   public static void Load() {
      Hanabi.INSTANCE.startClient();
   }

   public static void doLogin() {
      username = "Faithfully";
      rank = "admin";
      onDebug = true;
      new Hanabi();
   }
}
