package org.alphacentauri.modules;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;

public class ModuleTeam extends Module {
   public ModuleTeam() {
      super("Team", "Don\'t attack your team", new String[]{"team", "teams"}, Module.Category.Misc, 11254411);
   }

   public static boolean isinTeam(EntityPlayer player) {
      return AC.getModuleManager().get(ModuleTeam.class).isEnabled()?getTeamColor(player) == getTeamColor(AC.getMC().getPlayer()):false;
   }

   private static int getTeamColor(EntityPlayer player) {
      int color = 16777215;
      ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)player.getTeam();
      if(scoreplayerteam != null) {
         String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
         if(s.length() >= 2) {
            color = AC.getMC().fontRendererObj.getColorCode(s.charAt(1));
         }
      }

      return color;
   }
}
