package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.TeleportResult;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class TpBedNuker extends Module {
	
	private boolean dead = false;
	private double posY = 0;

	public TpBedNuker() {
		super("TpEggBreaker", Keyboard.KEY_NONE, Category.EXPLOITS, "This is really laggy because it scans a lot of blocks!");
	}
	
	@Override
	public void onEnable() {

		//Jigsaw.sendChatMessage(".damage 20");
		ArrayList<BlockPos> targets = new ArrayList<BlockPos>();
		int range = 100;
		for(int y = range; y >= -range; y--)
		{
			for(int x = range; x >= -range; x--)
			{
				for(int z = range; z >= -range; z--)
				{
					int posX = (int)(mc.thePlayer.posX + x);
					int posY = (int)(mc.thePlayer.posY + y);
					int posZ = (int)(mc.thePlayer.posZ + z);
					BlockPos pos = new BlockPos(posX, posY, posZ);
					if(Utils.getBlock(pos).equals(Blocks.bed)) {
						targets.add(pos);
					}
				}
			}
		}
		for(BlockPos pos : targets) {
//			Utils.infiniteReach(100, 9.5, 9, new ArrayList<Vec3>(), new ArrayList<Vec3>(), pos);
			TeleportResult result = Utils.pathFinderTeleportTo(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5));
			if(!result.foundPath) {
				continue;
			}
			sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
			sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
			TeleportResult resultBack = Utils.pathFinderTeleportBack(result.positions);
			
		}
		this.setToggled(false, true);
		
		
		super.onEnable();
	}
	
}
