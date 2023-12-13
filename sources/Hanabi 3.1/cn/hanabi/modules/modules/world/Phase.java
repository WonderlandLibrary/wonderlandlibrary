package cn.hanabi.modules.modules.world;

import cn.hanabi.events.BBSetEvent;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.PlayerUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Phase extends Mod {
   public Phase() {
      super("Phase", Category.WORLD);
   }

   public static boolean isInsideBlock() {
      EntityPlayerSP player = mc.thePlayer;
      WorldClient world = mc.theWorld;
      AxisAlignedBB bb = player.getEntityBoundingBox();

      for(int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
               Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
               AxisAlignedBB boundingBox;
               if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @EventTarget
   public void onCollide(BBSetEvent collide) {
      if (isInsideBlock()) {
         collide.setBoundingBox((AxisAlignedBB)null);
      }

   }

   @EventTarget
   public void onMove(EventMove event) {
      if (isInsideBlock()) {
         EntityPlayerSP var10001;
         if (mc.gameSettings.keyBindJump.isKeyDown()) {
            var10001 = mc.thePlayer;
            event.setY(var10001.motionY += 0.09000000357627869);
         } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            var10001 = mc.thePlayer;
            event.setY(var10001.motionY -= 0.09000000357627869);
         } else {
            event.setY(mc.thePlayer.motionY = 0.0);
         }

         event.setSpeed(PlayerUtil.getBaseMoveSpeed());
      }

   }

   @EventTarget
   public void onPost(EventPostMotion event) {
      if (mc.thePlayer.stepHeight > 0.0F) {
         mc.thePlayer.stepHeight = 0.0F;
      }

      float moveStrafe = mc.thePlayer.movementInput.moveStrafe;
      float moveForward = mc.thePlayer.movementInput.moveForward;
      float rotationYaw = mc.thePlayer.rotationYaw;
      double multiplier = 0.3;
      double mx = (double)(-MathHelper.sin((float)StrictMath.toRadians((double)rotationYaw)));
      double mz = (double)MathHelper.cos((float)StrictMath.toRadians((double)rotationYaw));
      double x = (double)moveForward * multiplier * mx + (double)moveStrafe * multiplier * mz;
      double z = (double)moveForward * multiplier * mz - (double)moveStrafe * multiplier * mx;
      if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && mc.thePlayer.onGround) {
         double posX = mc.thePlayer.posX;
         double posY = mc.thePlayer.posY;
         double posZ = mc.thePlayer.posZ;
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX + x, posY, posZ + z, true));
         mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 3.0, posZ, true));
         mc.thePlayer.setPosition(posX + x, posY, posZ + z);
      }

   }

   public void onDisable() {
      super.onDisable();
      mc.thePlayer.stepHeight = 0.6F;
   }
}
