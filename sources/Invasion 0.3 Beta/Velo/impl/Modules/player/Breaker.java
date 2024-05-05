package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Breaker extends Module {
	
	public static NumberSetting range = new NumberSetting("Range", 4, 1, 7, 0.1);
	public static BooleanSetting beds = new BooleanSetting("Beds", true);
	public static  BooleanSetting cakes = new BooleanSetting("Cakes", true);
	public static BooleanSetting eggs= new BooleanSetting("Eggs", true);
	
	public Breaker() {
		super("Breaker", "Breaker", Keyboard.KEY_NONE, Category.PLAYER);
		this.loadSettings(range, beds, cakes, eggs);
	}
	
	public void onUpdate(EventUpdate event) {
		for(int radius = (int) range.getValue(), x = -radius; x < radius; ++x) {
       		for(int y = radius; y > -radius; --y) {
       			for(int z = -radius; z < radius; ++z) {
					final int xPos = (int) mc.thePlayer.posX + x;
       				final int yPos = (int) mc.thePlayer.posY + y;
       				final int zPos = (int) mc.thePlayer.posZ + z;
       				
       				final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
       				final Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
       				
       				if(beds.isEnabled() && block instanceof BlockBed) {
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       				}
       				
       				if(cakes.isEnabled() && block instanceof BlockCake) {
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       				}
       				
       				if(eggs.isEnabled() && block instanceof BlockDragonEgg) {
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       					mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
       				}
				}
			}
		}
	}
}
