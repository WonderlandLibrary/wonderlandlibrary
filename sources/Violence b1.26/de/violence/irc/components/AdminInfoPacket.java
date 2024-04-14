package de.violence.irc.components;

import de.violence.Violence;
import de.violence.gui.VSetting;
import de.violence.irc.IrcPacket;
import de.violence.module.Module;
import net.minecraft.client.Minecraft;

public class AdminInfoPacket extends IrcPacket {
   public String getName() {
      return "ai";
   }

   public void onReceivePacket(String s) {
      if(Minecraft.getMinecraft().theWorld != null) {
         if(!VSetting.getByName("Enabled", Module.getByName("IRC")).isToggled()) {
            return;
         }

         String l = s.replace("&", "§");
         Violence.getViolence().sendChat("§c§l! §f" + l);
      }

   }
}
