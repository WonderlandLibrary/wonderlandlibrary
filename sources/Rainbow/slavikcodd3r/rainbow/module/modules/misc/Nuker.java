package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "Nuker")
public class Nuker extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		final BlockPos pos1 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY, mc.thePlayer.posZ);
        final BlockPos pos2 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY, mc.thePlayer.posZ);
        final BlockPos pos3 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 1.0);
        final BlockPos pos4 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 1.0);
        final BlockPos pos5 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY, mc.thePlayer.posZ - 1.0);
        final BlockPos pos6 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY, mc.thePlayer.posZ + 1.0);
        final BlockPos pos7 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY, mc.thePlayer.posZ - 1.0);
        final BlockPos pos8 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY, mc.thePlayer.posZ + 1.0);
        final BlockPos pos9 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ);
        final BlockPos pos10 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ);
        final BlockPos pos11 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos12 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos13 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos14 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos15 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos16 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos17 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ);
        final BlockPos pos18 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        final BlockPos pos19 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        final BlockPos pos20 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos21 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos22 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos23 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos24 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos25 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos26 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        final BlockPos pos27 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ);
        final BlockPos pos28 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ);
        final BlockPos pos29 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos30 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos31 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos32 = new BlockPos(mc.thePlayer.posX - 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos33 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ - 1.0);
        final BlockPos pos34 = new BlockPos(mc.thePlayer.posX + 1.0, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ + 1.0);
        final BlockPos pos35 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos1).getBlock() != Blocks.air) {
            this.smashBlock(pos1);
        }
        if (mc.theWorld.getBlockState(pos2).getBlock() != Blocks.air) {
            this.smashBlock(pos2);
        }
        if (mc.theWorld.getBlockState(pos3).getBlock() != Blocks.air) {
            this.smashBlock(pos3);
        }
        if (mc.theWorld.getBlockState(pos4).getBlock() != Blocks.air) {
            this.smashBlock(pos4);
        }
        if (mc.theWorld.getBlockState(pos5).getBlock() != Blocks.air) {
            this.smashBlock(pos5);
        }
        if (mc.theWorld.getBlockState(pos6).getBlock() != Blocks.air) {
            this.smashBlock(pos6);
        }
        if (mc.theWorld.getBlockState(pos7).getBlock() != Blocks.air) {
            this.smashBlock(pos7);
        }
        if (mc.theWorld.getBlockState(pos8).getBlock() != Blocks.air) {
            this.smashBlock(pos8);
        }
        if (mc.theWorld.getBlockState(pos9).getBlock() != Blocks.air) {
            this.smashBlock(pos9);
        }
        if (mc.theWorld.getBlockState(pos10).getBlock() != Blocks.air) {
            this.smashBlock(pos10);
        }
        if (mc.theWorld.getBlockState(pos11).getBlock() != Blocks.air) {
            this.smashBlock(pos11);
        }
        if (mc.theWorld.getBlockState(pos12).getBlock() != Blocks.air) {
            this.smashBlock(pos12);
        }
        if (mc.theWorld.getBlockState(pos13).getBlock() != Blocks.air) {
            this.smashBlock(pos13);
        }
        if (mc.theWorld.getBlockState(pos14).getBlock() != Blocks.air) {
            this.smashBlock(pos14);
        }
        if (mc.theWorld.getBlockState(pos15).getBlock() != Blocks.air) {
            this.smashBlock(pos15);
        }
        if (mc.theWorld.getBlockState(pos16).getBlock() != Blocks.air) {
            this.smashBlock(pos16);
        }
        if (mc.theWorld.getBlockState(pos18).getBlock() != Blocks.air) {
            this.smashBlock(pos18);
        }
        if (mc.theWorld.getBlockState(pos19).getBlock() != Blocks.air) {
            this.smashBlock(pos19);
        }
        if (mc.theWorld.getBlockState(pos20).getBlock() != Blocks.air) {
            this.smashBlock(pos20);
        }
        if (mc.theWorld.getBlockState(pos21).getBlock() != Blocks.air) {
            this.smashBlock(pos21);
        }
        if (mc.theWorld.getBlockState(pos22).getBlock() != Blocks.air) {
            this.smashBlock(pos22);
        }
        if (mc.theWorld.getBlockState(pos23).getBlock() != Blocks.air) {
            this.smashBlock(pos23);
        }
        if (mc.theWorld.getBlockState(pos24).getBlock() != Blocks.air) {
            this.smashBlock(pos24);
        }
        if (mc.theWorld.getBlockState(pos25).getBlock() != Blocks.air) {
            this.smashBlock(pos25);
        }
        if (mc.theWorld.getBlockState(pos17).getBlock() != Blocks.air) {
            this.smashBlock(pos17);
        }
        if (mc.theWorld.getBlockState(pos26).getBlock() != Blocks.air) {
            this.smashBlock(pos26);
        }
        if (mc.theWorld.getBlockState(pos27).getBlock() != Blocks.air) {
            this.smashBlock(pos27);
        }
        if (mc.theWorld.getBlockState(pos28).getBlock() != Blocks.air) {
            this.smashBlock(pos28);
        }
        if (mc.theWorld.getBlockState(pos29).getBlock() != Blocks.air) {
            this.smashBlock(pos29);
        }
        if (mc.theWorld.getBlockState(pos30).getBlock() != Blocks.air) {
            this.smashBlock(pos30);
        }
        if (mc.theWorld.getBlockState(pos31).getBlock() != Blocks.air) {
            this.smashBlock(pos31);
        }
        if (mc.theWorld.getBlockState(pos32).getBlock() != Blocks.air) {
            this.smashBlock(pos32);
        }
        if (mc.theWorld.getBlockState(pos33).getBlock() != Blocks.air) {
            this.smashBlock(pos33);
        }
        if (mc.theWorld.getBlockState(pos34).getBlock() != Blocks.air) {
            this.smashBlock(pos34);
        }
        if (mc.theWorld.getBlockState(pos35).getBlock() != Blocks.air) {
            this.smashBlock(pos35);
        }
}

	private void smashBlock(BlockPos pos) {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());		
	}
}
