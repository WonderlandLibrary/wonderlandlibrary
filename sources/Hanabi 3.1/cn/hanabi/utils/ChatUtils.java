package cn.hanabi.utils;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent.Serializer;

public class ChatUtils {
   public static final String PRIMARY_COLOR = "§7";
   public static final String SECONDARY_COLOR = "§1";
   private static final String PREFIX = "§7[§1Hanabi§7] ";

   public static void send(String s) {
      JsonObject object = new JsonObject();
      object.addProperty("text", s);
      Minecraft.getMinecraft().thePlayer.addChatMessage(Serializer.jsonToComponent(object.toString()));
   }

   public static void success(String s) {
      info(s);
   }

   public static void info(String s) {
      send("§7[§1Hanabi§7] " + s);
   }
}
