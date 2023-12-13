package me.aidanmees.trivia.client.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoBlock extends Module {
static WaitTimer timer = new WaitTimer();
	public AutoBlock() {
		super("AutoBlock", Keyboard.KEY_NONE, Category.HIDDEN, "Blocks everytime an aura is attacking, to minimize damage.");
	}

	
	@Override
	public void onUpdate() {
		  if(mc.thePlayer.isBlocking() && mc.thePlayer.isMoving()) {
	            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0,0,0), EnumFacing.UP));
	        }
	}

	public static void startBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
					mc.thePlayer.getHeldItem().getMaxItemUseDuration());
			stopBlock();
		
		}
		
	}

	public static void stopBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
	}
	
	public static boolean doBlock() {
		return !(! !InfiniteReach.doBlock());
	}

}
