package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

@Module.Mod
public class MultiUso extends Module {
   @Option.Op(
      min = 0.1D,
      max = 20.0D,
      increment = 1.0D
   )
   private double ValorSword;
   @Option.Op
   private boolean AntiFogo;
   @Option.Op
   private boolean Sword;

   @EventTarget
   public void AntiFogo(UpdateEvent var1) {
      int var2;
      if (this.AntiFogo) {
         label58: {
            ClientUtils.mc();
            if (Minecraft.thePlayer.capabilities.isCreativeMode) {
               ClientUtils.mc();
               if (!Minecraft.thePlayer.isImmuneToFire()) {
                  break label58;
               }
            }

            ClientUtils.mc();
            if (Minecraft.thePlayer.isBurning()) {
               ClientUtils.mc();
               if (Minecraft.thePlayer.onGround) {
                  for(var2 = 0; var2 < 10; ++var2) {
                     ClientUtils.packet(new C03PacketPlayer());
                  }
               }
            }
         }
      }

      if (this.Sword) {
         if (this != false) {
            Minecraft var10000 = mc;
            if (!Minecraft.thePlayer.isUsingItem()) {
               return;
            }

            var10000 = mc;
            if (!Minecraft.thePlayer.isCollidedVertically) {
               return;
            }

            var10000 = mc;
            if (!Minecraft.thePlayer.isMoving()) {
               return;
            }

            var10000 = mc;
            if (!Minecraft.thePlayer.onGround) {
               return;
            }

            var10000 = mc;
            ItemStack var3 = Minecraft.thePlayer.getCurrentEquippedItem();
            Minecraft var10001 = mc;
            Minecraft var10002 = mc;
            if (!var3.interactWithEntity(Minecraft.thePlayer, Minecraft.thePlayer)) {
               return;
            }

            var10000 = mc;
            if (!Minecraft.thePlayer.getHeldItem().isItemEnchantable()) {
               return;
            }

            var10000 = mc;
            if (!(Minecraft.thePlayer.getHealth() <= 20.0F)) {
               return;
            }
         }

         for(var2 = 0; (double)var2 < this.ValorSword; ++var2) {
            ClientUtils.player().getActivePotionEffect(Potion.regeneration).deincrementDuration();
            ClientUtils.packet(new C03PacketPlayer());
         }
      }

   }
}
