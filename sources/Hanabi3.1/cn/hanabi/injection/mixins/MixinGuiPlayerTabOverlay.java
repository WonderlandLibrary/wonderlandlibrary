package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IGuiPlayerTabOverlay;
import cn.hanabi.modules.ModManager;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay implements IGuiPlayerTabOverlay {
   @Shadow
   @Final
   private static Ordering field_175252_a;

   public Ordering getField() {
      return field_175252_a;
   }

   @Overwrite
   public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
      String prefix = "";
      String result = "";
      if (networkPlayerInfoIn.getDisplayName() != null) {
         result = prefix + networkPlayerInfoIn.getDisplayName().getFormattedText();
      } else {
         result = prefix + ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
      }

      if (ModManager.getModule("NameProtect").getState()) {
         result = result.replace(Minecraft.getMinecraft().thePlayer.getName(), "PROTECTION");
      }

      return result;
   }
}
