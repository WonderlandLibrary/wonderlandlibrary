package my.NewSnake.Tank.module.modules.RENDER;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.BlockCullEvent;
import my.NewSnake.event.events.BoundingBoxEvent;
import my.NewSnake.event.events.InsideBlockRenderEvent;
import my.NewSnake.event.events.MoveEvent;
import my.NewSnake.event.events.PushOutOfBlocksEvent;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.AxisAlignedBB;

@Module.Mod
public class Freecam extends Module {
   @Option.Op(
      min = 0.1D,
      max = 2.0D,
      increment = 0.1D
   )
   private double speed = 1.0D;
   private EntityOtherPlayerMP freecamEntity;

   @EventTarget
   private void onMove(MoveEvent var1) {
      if (ClientUtils.movementInput().jump) {
         var1.setY(ClientUtils.player().motionY = this.speed);
      } else if (ClientUtils.movementInput().sneak) {
         var1.setY(ClientUtils.player().motionY = -this.speed);
      } else {
         var1.setY(ClientUtils.player().motionY = 0.0D);
      }

      ClientUtils.setMoveSpeed(var1, this.speed);
   }

   @EventTarget
   private void onInsideBlockRender(InsideBlockRenderEvent var1) {
      var1.setCancelled(true);
   }

   public void enable() {
      if (ClientUtils.player() != null) {
         this.freecamEntity = new EntityOtherPlayerMP(ClientUtils.world(), new GameProfile(new UUID(69L, 96L), "Freecam"));
         this.freecamEntity.inventory = ClientUtils.player().inventory;
         this.freecamEntity.inventoryContainer = ClientUtils.player().inventoryContainer;
         this.freecamEntity.setPositionAndRotation(ClientUtils.x(), ClientUtils.y(), ClientUtils.z(), ClientUtils.yaw(), ClientUtils.pitch());
         this.freecamEntity.rotationYawHead = ClientUtils.player().rotationYawHead;
         ClientUtils.world().addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
         super.enable();
      }
   }

   @EventTarget
   private void onBlockCull(BlockCullEvent var1) {
      var1.setCancelled(true);
   }

   public void disable() {
      ClientUtils.player().setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
      ClientUtils.world().removeEntityFromWorld(this.freecamEntity.getEntityId());
      ClientUtils.mc().renderGlobal.loadRenderers();
      super.disable();
   }

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      if (var1.getState() == Event.State.PRE) {
         var1.setCancelled(true);
      }

   }

   @EventTarget
   private void onBoundingBox(BoundingBoxEvent var1) {
      var1.setBoundingBox((AxisAlignedBB)null);
   }

   @EventTarget
   private void onPushOutOfBlocks(PushOutOfBlocksEvent var1) {
      var1.setCancelled(true);
   }
}
