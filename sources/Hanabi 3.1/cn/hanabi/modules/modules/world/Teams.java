package cn.hanabi.modules.modules.world;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Teams extends Mod {
   static boolean clientfriendOld;
   TimeHelper timer = new TimeHelper();

   public Teams() {
      super("Teams", Category.PLAYER);
   }

   public static boolean isOnSameTeam(Entity entity) {
      if (ModManager.getModule("Teams").isEnabled() && Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("§")) {
         return Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() > 2 && entity.getDisplayName().getUnformattedText().length() > 2 ? Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2)) : false;
      } else {
         return false;
      }
   }

   public void onDisable() {
   }
}
