package dev.monsoon.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class WorldUtil {
	
	public static BlockPos getForwardBlock(double length) {
		
		Minecraft mc = Minecraft.getMinecraft();
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        BlockPos fPos = new BlockPos(mc.thePlayer.posX + (-Math.sin(yaw) * length), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(yaw) * length));
        return fPos;
	}

    public static void placeBlock(BlockPos pos)
    {
        for (EnumFacing enumFacing : EnumFacing.values())
        {
            if (!Minecraft.getMinecraft().theWorld.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.air))
            {
                Vec3 vec = new Vec3(pos.getX() + 0.5D + (double) enumFacing.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getFrontOffsetZ() * 0.5D);

                float[] old = new float[]{Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch};

                //Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook((float) Math.toDegrees(Math.atan2((vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ), (vec.xCoord - Minecraft.getMinecraft().thePlayer.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.yCoord - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight())), (Math.sqrt((vec.xCoord - Minecraft.getMinecraft().thePlayer.posX) * (vec.xCoord - Minecraft.getMinecraft().thePlayer.posX) + (vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ) * (vec.zCoord - Minecraft.getMinecraft().thePlayer.posZ)))))), Minecraft.getMinecraft().thePlayer.onGround));
                //Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                Minecraft.getMinecraft().playerController.func_178890_a(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld,Minecraft.getMinecraft().thePlayer.getHeldItem(), pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3(pos.getX(), pos.getY(),pos.getZ()));
                Minecraft.getMinecraft().thePlayer.swingItem();
                //Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                 //Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(old[0], old[1], Minecraft.getMinecraft().thePlayer.onGround));

                return;
            }
        }
    }

}
