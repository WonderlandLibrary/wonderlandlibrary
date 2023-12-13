package me.aidanmees.trivia.client.modules.Movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class Step extends Module {
	private boolean didSend;
	  private int sendTicks;
	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider heightSlider = new BasicSlider("Step Height", ClientSettings.stepHeight, 1, 9, 0,
//				ValueDisplay.DECIMAL);
//		SliderListener listener = new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.stepHeight = (float) slider.getValue();
//			}
//		};
//		heightSlider.addSliderListener(listener);
		SliderSetting<Number> heightSlider = new SliderSetting<Number>("Vanilla Mode - Height", ClientSettings.stepHeight, 1, 9, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { heightSlider };
	}

	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to step up full blocks like a half slab");
	}

	@Override
	public void onDisable() {

		if (currentMode.equals("Vanilla")) {
			mc.thePlayer.stepHeight = 0.6f;
		}

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (currentMode.equals("Vanilla")) {
			mc.thePlayer.stepHeight = (float) ClientSettings.stepHeight;
		}
		if (currentMode.equals("Test")) {
			boolean onedotfive;
			double xOffset = (double) MovementInput.moveForward * 0.3
					* Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f))
					+ (double) MovementInput.moveStrafe * 0.3
							* Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			double zOffset = (double) MovementInput.moveForward * 0.3
					* Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f))
					- (double) MovementInput.moveStrafe * 0.3
							* Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			boolean clearAbove = mc.theWorld
					.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(xOffset, 2.1, zOffset))
					.isEmpty();
			boolean carpetCheck = mc.theWorld
					.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(xOffset, 2.01, zOffset))
					.isEmpty();
			boolean two = !mc.theWorld
					.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(xOffset, 1.6, zOffset))
					.isEmpty();
			boolean bl2 = onedotfive = !mc.theWorld
					.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(xOffset, 1.4, zOffset))
					.isEmpty() && !two;
			if (clearAbove && mc.thePlayer.onGround && !mc.thePlayer.isOnLadder()) {
				double[] arrd;
				if (mc.thePlayer.isCollidedHorizontally) {
					++sendTicks;
				}
				if (two) {
					double[] arrd2 = new double[10];
					arrd2[0] = 0.4;
					arrd2[1] = 0.75;
					arrd2[2] = 0.5;
					arrd2[3] = 0.41;
					arrd2[4] = 0.83;
					arrd2[5] = 1.16;
					arrd2[6] = 1.41;
	                arrd2[7] = 1.57;
	                arrd2[8] = 1.58;
	                arrd = arrd2;
	                arrd2[9] = 1.42;
	            } else if (onedotfive) {
	                double[] arrd3 = new double[7];
	                arrd3[0] = 0.4;
	                arrd3[1] = 0.75;
	                arrd3[2] = 0.5;
	                arrd3[3] = 0.41;
	                arrd3[4] = 0.83;
	                arrd3[5] = 1.16;
	                arrd = arrd3;
	                arrd3[6] = 1.41;
	            } else {
	                double[] arrd4 = new double[2];
	                arrd4[0] = 0.42;
	                arrd = arrd4;
	                arrd4[1] = 0.75;
	            }
	            double[] pos = arrd;
	            if (sendTicks > (two ? 7 : (onedotfive ? 5 : 1))) {
	            	
	                double[] arrd5 = pos;
	                int n = arrd5.length;
	                int n2 = 0;
	                while (n2 < n) {
	                	
	                    double position = arrd5[n2];
	                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + position, mc.thePlayer.posZ, true));
	                    ++n2;
	                }
	                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + pos[pos.length - 1], mc.thePlayer.posZ);
	                if (!carpetCheck) {
	                 
	                }
	                sendTicks = 0;
	            }
	            else{
	            	
	            }
	        }
			
	    }

			  
		if (currentMode.equals("Packet")) {
			if(mc.thePlayer.onGround
					&& !mc.thePlayer.isOnLadder()
					&& (mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F)
					&& canStep() && !mc.thePlayer.movementInput.jump
					&& mc.thePlayer.isCollidedHorizontally)
				{
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 0.41999998688698D, event.z, mc.thePlayer.onGround));
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 0.7531999805212D, event.z, mc.thePlayer.onGround));
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 1.00133597911214D, event.z, mc.thePlayer.onGround));
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 1.16610926093821D, event.z, mc.thePlayer.onGround));
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 1.24918707874468D, event.z, mc.thePlayer.onGround));
					sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y + 1.1707870772188D, event.z, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(event.x, event.y + 1, event.z);
				}
		}
		super.onUpdate();
	}

	@Override
	public void onRender() {

		super.onRender();
	}
	public void s(double off)
	  {
	    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + off, mc.thePlayer.posZ, true));
	  
	

}
	
	@Override
	protected void onModeChanged(String modeBefore, String newMode) {
		mc.thePlayer.stepHeight = 0.6f;
		super.onModeChanged(modeBefore, newMode);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Vanilla", "Packet" , "Test"};
	}
	
	private boolean canStep()
	{
		ArrayList<BlockPos> collisionBlocks = new ArrayList<BlockPos>();
		
		Entity player = mc.thePlayer;
		BlockPos pos1 =
			new BlockPos(player.getEntityBoundingBox().minX - 0.001D,
				player.getEntityBoundingBox().minY - 0.001D,
				player.getEntityBoundingBox().minZ - 0.001D);
		BlockPos pos2 =
			new BlockPos(player.getEntityBoundingBox().maxX + 0.001D,
				player.getEntityBoundingBox().maxY + 0.001D,
				player.getEntityBoundingBox().maxZ + 0.001D);
		
		if(player.worldObj.isAreaLoaded(pos1, pos2))
			for(int x = pos1.getX(); x <= pos2.getX(); x++)
				for(int y = pos1.getY(); y <= pos2.getY(); y++)
					for(int z = pos1.getZ(); z <= pos2.getZ(); z++)
						if(y > player.posY - 1.0D && y <= player.posY)
							collisionBlocks.add(new BlockPos(x, y, z));
		
		BlockPos belowPlayerPos =
			new BlockPos(player.posX, player.posY - 1.0D, player.posZ);
		for(BlockPos collisionBlock : collisionBlocks)
			if(!(player.worldObj.getBlockState(collisionBlock.add(0, 1, 0))
				.getBlock() instanceof BlockFenceGate))
				if(player.worldObj
					.getBlockState(collisionBlock.add(0, 1, 0))
					.getBlock()
					.getCollisionBoundingBox(mc.theWorld, belowPlayerPos,
						mc.theWorld.getBlockState(collisionBlock)) != null)
					return true;
		
		return true;
	}

}
