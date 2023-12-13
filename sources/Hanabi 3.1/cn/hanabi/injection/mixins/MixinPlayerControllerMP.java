package cn.hanabi.injection.mixins;

import cn.hanabi.events.DamageBlockEvent;
import cn.hanabi.events.EventAttack;
import cn.hanabi.injection.interfaces.INetworkManager;
import cn.hanabi.injection.interfaces.IPlayerControllerMP;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerControllerMP.class})
public class MixinPlayerControllerMP implements IPlayerControllerMP {
   @Shadow
   private WorldSettings.GameType currentGameType;
   @Shadow
   private float curBlockDamageMP;
   @Shadow
   private int blockHitDelay;

   @Inject(
      method = {"attackEntity"},
      at = {@At("HEAD")}
   )
   public void attack(EntityPlayer playerIn, Entity targetEntity, CallbackInfo info) {
      EventManager.call(new EventAttack(targetEntity));
   }

   @Inject(
      method = {"onPlayerDamageBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable cir) {
      DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
      EventManager.call(event);
   }

   @Overwrite
   public ItemStack windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
      synchronized(playerIn.openContainer) {
         ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
         ((INetworkManager)Minecraft.getMinecraft().getNetHandler().getNetworkManager()).sendPacketNoEvent(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, playerIn.openContainer.getNextTransactionID(playerIn.inventory)));
         return itemstack;
      }
   }

   public float getCurBlockDamageMP() {
      return this.curBlockDamageMP;
   }

   public void setCurBlockDamageMP(float f) {
      this.curBlockDamageMP = f;
   }

   public int getBlockDELAY() {
      return this.blockHitDelay;
   }

   public void setBlockHitDelay(int i) {
      this.blockHitDelay = i;
   }
}
