package org.alphacentauri.modules;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleKick extends Module {
   public Property mode = new Property(this, "Mode", ModuleKick.Mode.SelfHurt);

   public ModuleKick() {
      super("Kick", "Kick yourself", new String[]{"kick"}, Module.Category.Exploits, 10932865);
   }

   public void setEnabledSilent(boolean enabled) {
      if(this.mode.value == ModuleKick.Mode.InvalidPacket) {
         AC.getMC().getPlayer().sendQueue.addToSendQueue(new C04PacketPlayerPosition(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN, true));
      } else if(this.mode.value == ModuleKick.Mode.SelfHurt) {
         AC.getMC().getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(AC.getMC().getPlayer(), Action.ATTACK));
      } else if(this.mode.value == ModuleKick.Mode.Quit) {
         AC.getMC().theWorld.sendQuittingDisconnectingPacket();
         AC.getMC().loadWorld((WorldClient)null);
      } else if(this.mode.value == ModuleKick.Mode.IllegalCharacters) {
         AC.getMC().getPlayer().sendChatMessage("§l§e§e§t§ §k§i§c§k§ §e§x§p§l§o§i§t");
      }

      super.setEnabledSilent(false);
   }

   public static enum Mode {
      InvalidPacket,
      SelfHurt,
      Quit,
      IllegalCharacters;
   }
}
