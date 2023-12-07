package Velo.api.Util.Other.other;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class PlayerUtil {
	public static Minecraft mc = Minecraft.getMinecraft();
	
	
	public static double getPlayerBaseSpeed() {
        double value = 0.33;
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int multiplier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            value *= 1.0 + 0.15 * (multiplier + 1);
        }
        return value;
	}
	
	
    public static boolean isValidBlock(Block block, boolean toPlace) {
        if (block instanceof BlockContainer)
            return false;
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        } else {
            final Material material = block.getMaterial();
            return !material.isReplaceable() && !material.isLiquid();
        }
    }
	
	 public static boolean isInLiquid() {
	      boolean inLiquid = false;
	      AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
	      int y = (int)playerBB.minY;

	      for(int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
	         for(int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
	            Block b = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	            if (b != null && !(b instanceof BlockAir)) {
	               if (!(b instanceof BlockLiquid)) {
	                  return false;
	               }

	               inLiquid = true;
	            }
	         }
	      }

	      return inLiquid;
	   }
	 
	 
	  public static boolean holdingSword() {
	      return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
	   }

	   public static boolean isOnLiquid() {
	      boolean onLiquid = false;
	      AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
	      WorldClient world = mc.theWorld;
	      int y = (int)playerBB.offset(0.0D, -0.01D, 0.0D).minY;

	      for(int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
	         for(int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
	            Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();
	            if (b != null && !(b instanceof BlockAir)) {
	               if (!(b instanceof BlockLiquid)) {
	                  return false;
	               }

	               onLiquid = true;
	            }
	         }
	      }

	      return onLiquid;
	   }
	
	   public static boolean isOnSameTeam(EntityPlayer entity) {
		      if (entity.getTeam() != null && mc.thePlayer.getTeam() != null) {
		         char c1 = entity.getDisplayName().getFormattedText().charAt(1);
		         char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
		         return c1 == c2;
		      } else {
		         return false;
		      }
		   }
	
	   public static void damageSpoof() {
		      double offset = 0.060100000351667404D;
		      NetHandlerPlayClient netHandler = mc.getNetHandler();
		      EntityPlayerSP thePlayer = mc.thePlayer;
		      double xPosition = thePlayer.posX;
		      double yPosition = thePlayer.posY;
		      double zPosition = thePlayer.posZ;

		      for(int i = 0; (double)i < (double)getMaxFallDist() / 0.05510000046342611D + 1.0D; ++i) {
		         netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xPosition, yPosition + 0.060100000351667404D, zPosition, false));
		         netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xPosition, yPosition + 5.000000237487257E-4D, zPosition, false));
		         netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(xPosition, yPosition + 0.004999999888241291D + 6.01000003516674E-8D, zPosition, false));
		      }

		      netHandler.addToSendQueue(new C03PacketPlayer(true));
		   }
	   
	   
	   public static float getMaxFallDist() {
		      PotionEffect jumpEffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
		      int fall = jumpEffect != null ? jumpEffect.getAmplifier() + 1 : 0;
		      return (float)(mc.thePlayer.getMaxFallHeight() + fall);
		   }
	   
	   
	   public static boolean isInsideBlock() {
		      
		      WorldClient theWorld = mc.theWorld;
		      EntityPlayerSP thePlayer = mc.thePlayer;
		      AxisAlignedBB bb = thePlayer.getEntityBoundingBox();

		      for(int xPosition = MathHelper.floor_double(bb.minX); xPosition < MathHelper.floor_double(bb.maxX) + 1; ++xPosition) {
		         for(int yPosition = MathHelper.floor_double(bb.minY); yPosition < MathHelper.floor_double(bb.maxY) + 1; ++yPosition) {
		            for(int zPosition = MathHelper.floor_double(bb.minZ); zPosition < MathHelper.floor_double(bb.maxZ) + 1; ++zPosition) {
		               Block b = theWorld.getBlockState(new BlockPos(xPosition, yPosition, zPosition)).getBlock();
		               AxisAlignedBB boundingBox;
		               if (b != null && (boundingBox = b.getCollisionBoundingBox(theWorld, new BlockPos(xPosition, yPosition, zPosition), theWorld.getBlockState(new BlockPos(xPosition, yPosition, zPosition)))) != null && thePlayer.getEntityBoundingBox().intersectsWith(boundingBox) && !(b instanceof BlockAir)) {
		                  return true;
		               }
		            }
		         }
		      }

		      return false;
		   }
	   
}
