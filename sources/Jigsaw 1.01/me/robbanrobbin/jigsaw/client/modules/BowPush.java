package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumHand;

public class BowPush extends Module {
	
	private WaitTimer releaseTimer = new WaitTimer();
	private WaitTimer jumpTimer = new WaitTimer();
	private boolean released = false;
	private int knockbackCount = 0;

	public BowPush() {
		super("BowPush", Keyboard.KEY_NONE, Category.EXPLOITS, "Pushes you using bows and arrows on anticheats!!");
	}
	
	@Override
	public void onToggle() {
		super.onToggle();
		releaseTimer.reset();
		Utils.resetMcTimerTPS();
		released = true;
		knockbackCount = 0;
	}
	
	@Override
	public void onPreMotion(PreMotionEvent event) {
		super.onPreMotion(event);
		
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);
		mc.timer.tickLength = 120f;
		if(mc.player.onGround) {
			mc.player.motionX = 0;
			mc.player.motionZ = 0;
			if(jumpTimer.hasTimeElapsed(2400, true)) {
				mc.player.jump();
			}
			releaseTimer.reset();
		}
		else {
			if(mc.player.movementInput.moveForward == 0) {
				moveForwards(0.0f);
			}
		}
		
		double difX = mc.player.prevPosX - mc.player.posX;
		double difZ = mc.player.prevPosZ - mc.player.posZ;
		
		double dist = Math.sqrt(difX * difX + difZ * difZ);
		
		double addToPitch = mc.player.movementInput.moveForward == 0 || mc.player.isCollidedHorizontally ? 0 : 138;
		
		event.pitch = (float) (-90 + addToPitch);
		
		if(Utils.returnItemStackIfPlayerHoldingItem(Items.BOW) != null && released) {
			released = false;
			sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		}
		
	}
	
	@Override
	public void onLateUpdate() {
		super.onLateUpdate();
		
		double difX = mc.player.prevPosX - mc.player.posX;
		double difZ = mc.player.prevPosZ - mc.player.posZ;
		
		double dist = Math.max(1, Math.sqrt(difX * difX + difZ * difZ) * 1.2);
		
		if(releaseTimer.hasTimeElapsed((int)(200 / dist), true) && !released) {
			released = true;
			mc.playerController.onStoppedUsingItem(mc.player);
		}
		
	}

	@Override
	public void onPacketRecieved(PacketEvent event) {
		Packet packet = event.getPacket();
		if(packet instanceof SPacketEntityVelocity) {
			Entity entity = mc.world.getEntityByID(((SPacketEntityVelocity) packet).getEntityID());
			if (entity instanceof EntityPlayerSP) {
				event.cancel();
				
				knockbackCount++;
				
				SPacketEntityVelocity vel = ((SPacketEntityVelocity) packet);
				
				
				Jigsaw.chatMessage("motionY = " + mc.player.motionY);
				
				double difX = ((double) vel.getMotionX() / 8000.0D);
				double difZ = ((double) vel.getMotionZ() / 8000.0D);
				
				float dist = (float) Math.max(1.2, Math.sqrt(difX * difX + difZ * difZ));
				
				float moveAmount = dist / 1.25f;
				if(knockbackCount > 5) {
					moveAmount /= knockbackCount - 5;
				}
				
				final float finalMoveAmount = moveAmount;
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mc.addScheduledTask(new Runnable() {
							
							@Override
							public void run() {
								moveForwards(finalMoveAmount);
								double finalMotionY = ((double) vel.getMotionY() / 8000.0D) - 0.1;
								
								mc.player.motionY = finalMotionY;
								
								System.out.println(finalMoveAmount);
							}
						});
					}
				}).start();
			}
		}
		super.onPacketRecieved(event);
	}
	
	public void moveForwards(float speed) {
		mc.player.motionX = (-(Math.sin(getLookYawThing()) * speed));
		mc.player.motionZ = (Math.cos(getLookYawThing()) * speed);
	}

	public float getLookYawThing() {
		float var1 = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.player.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.player.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.player.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.player.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;

		return var1;
	}

}
