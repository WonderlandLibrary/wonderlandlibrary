package space.clowdy.modules.impl;

import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CEntityActionPacket.Action;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class AutoShiftTap extends Module {
     @SubscribeEvent
     public void に(AttackEntityEvent attackEntityEvent) {
          this.mc.player.movementInput.sneaking = true;
          this.mc.getConnection().sendPacket(new CEntityActionPacket(this.mc.player, Action.RELEASE_SHIFT_KEY));
          this.mc.getConnection().sendPacket(new CEntityActionPacket(this.mc.player, Action.PRESS_SHIFT_KEY));
     }

     public AutoShiftTap() {
          super("AutoShiftTap", "\u0012K 2AB05B5 =0 H8DB ?@8 C40@5", 0, Category.GHOST);
     }
}
