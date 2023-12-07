package Velo.impl.Modules.movement;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class Step extends Module {
	
	public int ticks = 0;
	public float tickselapsed = 0;
	public boolean hasStepped = false;
	
	public static ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Ncp", "Aac", "Dev");
	public static NumberSetting stepheight = new NumberSetting("StepHeight", 1, 0, 20, 0.5);
	
	public Step() {
		super("Step", "Step", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(mode, stepheight);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		ticks = 0;
		tickselapsed = 0;
		mc.timer.timerSpeed = 1F;
		hasStepped = false;
		mc.thePlayer.stepHeight = 0.5F;
	}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		this.setDisplayName("Step " + mode.modes.get(mode.index));
		if(mode.equalsIgnorecase("Vanilla")) {
			mc.thePlayer.stepHeight = (float) stepheight.getValue();
		}
		double x = -Math.sin(mc.thePlayer.getDirection()) * 0.9;
		double z = Math.cos(mc.thePlayer.getDirection()) * 0.9;
		if(mode.equalsIgnorecase("Ncp")) {
			if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z)).getBlock() instanceof BlockAir) && !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z)).getBlock() instanceof BlockFlower) && !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z)).getBlock() instanceof BlockBush) && mc.thePlayer.isMoving()) {
				ticks++;
				switch(ticks) {
				case 1:
					if(mc.thePlayer.onGround) {
						mc.timer.timerSpeed = 1.05F;
						//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
						mc.thePlayer.motionY = 0.42F;
					}
					break;
				case 3:
					//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					mc.thePlayer.motionY = -1F;
				case 4:
					mc.thePlayer.setSpeed(0.15);
					mc.timer.timerSpeed = 1.0F;
				}
			} else {
				ticks = 0;
			}
		}
	}
}
