package team.massacre.impl.module.movement;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.impl.module.combat.KillAura2;
import team.massacre.utils.PacketUtil;

public class NoSlowDown extends Module {
   public EnumProperty<NoSlowDown.Mode> mode;

   public NoSlowDown() {
      super("No Slow Down", 0, Category.MOVEMENT);
      this.mode = new EnumProperty("Mode", NoSlowDown.Mode.NCP);
      this.addValues(new Property[]{this.mode});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((NoSlowDown.Mode)this.mode.getValue()).name());
      KillAura2 aura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
      switch((NoSlowDown.Mode)this.mode.getValue()) {
      case Redesky:
         if (this.mc.thePlayer.isUsingItem() && aura.target == null) {
            EntityPlayerSP var10000 = this.mc.thePlayer;
            var10000.motionX *= 0.7D;
            var10000 = this.mc.thePlayer;
            var10000.motionZ *= 0.7D;
            if (eventUpdate.isPre()) {
               PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            } else {
               this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());
            }
         }
         break;
      case NCP:
         if (this.mc.thePlayer.isUsingItem() && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && aura.target == null) {
            switch(eventUpdate.getType()) {
            case PRE:
               PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D)), EnumFacing.DOWN));
               break;
            case POST:
               PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
            }
         }
      }

   }

   public static enum Mode {
      Redesky,
      NCP;
   }
}
