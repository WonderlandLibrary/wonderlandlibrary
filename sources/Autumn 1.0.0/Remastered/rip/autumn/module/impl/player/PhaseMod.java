package rip.autumn.module.impl.player;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.player.BlockRenderEvent;
import rip.autumn.events.player.BoundingBoxEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.events.player.PushEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.MoveUtil;
import rip.autumn.utils.PlayerUtils;

@Label("Phase")
@Category(ModuleCategory.PLAYER)
public final class PhaseMod extends Module {
   private final EnumOption mode;
   private int moveUnder;

   public PhaseMod() {
      this.mode = new EnumOption("Mode", PhaseMod.Mode.NORMAL);
      this.setMode(this.mode);
      this.addOptions(this.mode);
   }

   @Listener(TickEvent.class)
   public void onTick(TickEvent event) {
      if (getPlayer() != null && this.moveUnder == 1) {
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2.0D, getZ(), false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
         this.moveUnder = 0;
      }

      if (getPlayer() != null && this.moveUnder == 1488) {
         double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
         double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
         double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
         double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX() + x, getY(), getZ() + z, false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, getY(), Double.NEGATIVE_INFINITY, true));
         this.moveUnder = 0;
      }

   }

   @Listener(BoundingBoxEvent.class)
   public void onBoundingBox(BoundingBoxEvent event) {
      switch(mode.getValue().toString()) {
      case "NORMAL":
         if (PlayerUtils.isInsideBlock()) {
            event.setBoundingBox(null);
         }
         break;
      case "VANILLA":
         if (mc.thePlayer.isCollidedHorizontally && !PlayerUtils.isInsideBlock()) {
            double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
            double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
            double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
            event.setBoundingBox(null);
            mc.thePlayer.setPosition(getX() + x, getY(), getZ() + z);
            this.moveUnder = 69;
         }

         if (PlayerUtils.isInsideBlock()) {
            event.setBoundingBox(null);
         }
         break;
      }
   }

   @Listener(PushEvent.class)
   public void onPush(PushEvent event) {
      event.setCancelled();
   }

   @Listener(BlockRenderEvent.class)
   public void onBlockRender(BlockRenderEvent event) {
      event.setCancelled();
   }

   @Listener(MotionUpdateEvent.class)
   public void onMotionUpdate(MotionUpdateEvent event) {
      switch(mode.getValue().toString()) {
      case "NORMAL":
         if (!event.isPre()) {
            double multiplier = 0.3D;
            double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
            double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
            double x = mc.thePlayer.movementInput.moveForward * multiplier * mx + mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
            double z = mc.thePlayer.movementInput.moveForward * multiplier * mz - mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
            if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
               mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(getX() + x, getY() + 0.001D, getZ() + z, false));

               for(int i = 1; i < 10; ++i) {
                  mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 0.22D, getZ(), false));
               }

               mc.thePlayer.setPosition(getX() + x, getY(), getZ() + z);
            }
         }
         break;
      case "VANILLA":
         if (mc.gameSettings.keyBindSneak.isPressed() && !PlayerUtils.isInsideBlock()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2.0D, getZ(), true));
            this.moveUnder = 2;
         }
         break;
      }
   }

   @Listener(ReceivePacketEvent.class)
   public void onReceive(ReceivePacketEvent event) {
      if (event.getPacket() instanceof S02PacketChat) {
         S02PacketChat packet = (S02PacketChat)event.getPacket();
         if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
            event.setCancelled();
         }
      }

      switch (mode.getValue().toString()) {
         case "VANILLA":
            if(event.getPacket() instanceof S08PacketPlayerPosLook) {
               switch (moveUnder) {
                  case 2:
                     this.moveUnder = 1;
                     break;
                  case 69:
                     this.moveUnder = 1488;
                     break;
               }
            }
            break;
      }
   }

   @Listener(MoveEvent.class)
   public void onMove(MoveEvent event) {
      switch(mode.getValue().toString()) {
      case "NORMAL":
         if (PlayerUtils.isInsideBlock()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               event.y = mc.thePlayer.motionY += 0.09000000357627869D;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
               event.y = mc.thePlayer.motionY -= 0.0D;
            } else {
               event.y = mc.thePlayer.motionY = 0.0D;
            }

            MoveUtil.setSpeed(0.3D);
            if (mc.thePlayer.ticksExisted % 2 == 0) {
               event.y = mc.thePlayer.motionY += 0.09000000357627869D;
            }
         }
         break;
      case "VANILLA":
         if (PlayerUtils.isInsideBlock()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               event.y = mc.thePlayer.motionY = 0.5D;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
               event.y = mc.thePlayer.motionY = -0.5D;
            } else {
               event.y = mc.thePlayer.motionY = 0.0D;
            }

            MoveUtil.setSpeed(0.28D);
         }
         break;
      }
   }

   private enum Mode {
      NORMAL,
      VANILLA;
   }
}
