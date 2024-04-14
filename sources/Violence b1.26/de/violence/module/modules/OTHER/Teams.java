package de.violence.module.modules.OTHER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class Teams extends Module {
   private VSetting scoreboardTeams = new VSetting("Scoreboard-Team", this, false);
   private VSetting colorCheck = new VSetting("Color", this, false);

   public Teams() {
      super("Teams", Category.OTHER);
   }

   public static boolean isInTeam(EntityLivingBase e) {
      boolean a = false;
      if(VSetting.getByName("Scoreboard-Team", Module.getByName("Teams")).isToggled()) {
         a = Minecraft.getMinecraft().thePlayer.isOnSameTeam(e);
      }

      if(VSetting.getByName("Color", Module.getByName("Teams")).isToggled()) {
         a = getPrefix(e).equalsIgnoreCase(getPrefix(Minecraft.getMinecraft().thePlayer));
      }

      return a;
   }

   static String getPrefix(EntityLivingBase e) {
      return e.getDisplayName().getUnformattedText().substring(1, 2);
   }
}
