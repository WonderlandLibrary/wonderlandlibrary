package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

@Mod(displayName = "AntiCactus")
public class AntiCactus extends Module
{
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		super.enable();
	}
	
	public void disable() {
		super.disable();
	}
	
	
    @EventTarget
    private void onPacket(final PacketSendEvent event) {
    	if (mc.theWorld != null) {
        final BlockPos pos1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.9, mc.thePlayer.posZ);
    	if (mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
        final BlockPos pos2 = new BlockPos(mc.thePlayer.posX + 0.9, mc.thePlayer.posY, mc.thePlayer.posZ);
    	if (mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
        final BlockPos pos3 = new BlockPos(mc.thePlayer.posX - 0.9, mc.thePlayer.posY, mc.thePlayer.posZ);
    	if (mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
        final BlockPos pos4 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 0.9);
    	if (mc.theWorld.getBlockState(pos4).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
        final BlockPos pos5 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 0.9);
    	if (mc.theWorld.getBlockState(pos5).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
        final BlockPos pos6 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    	if (mc.theWorld.getBlockState(pos6).getBlock() instanceof BlockCactus && event.getPacket() instanceof C03PacketPlayer) {
    		event.setCancelled(true);
    	}
    	}
    }
}