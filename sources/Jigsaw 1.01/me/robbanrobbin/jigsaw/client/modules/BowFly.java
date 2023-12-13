package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
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

public class BowFly extends Module {
	
	private WaitTimer releaseTimer = new WaitTimer();
	private WaitTimer jumpTimer = new WaitTimer();
	private WaitTimer arrowTimer = new WaitTimer();
	
	private boolean released = false;
	private boolean prepareArrow = false;
	private int knockbackCount = 0;

	public BowFly() {
		super("BowFly", Keyboard.KEY_NONE, Category.EXPLOITS, "Pushes you using bows and arrows on anticheats!!");
	}
	
	@Override
	public void onToggle() {
		super.onToggle();
		releaseTimer.reset();
		jumpTimer.reset();
		arrowTimer.reset();
		Utils.resetMcTimerTPS();
		released = true;
		prepareArrow = false;
		knockbackCount = 0;
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);
		mc.timer.tickLength = 200f;
		if(mc.player.onGround) {
			if(knockbackCount > 0) {
				this.setToggled(false, true);
				return;
			}
			if(jumpTimer.hasTimeElapsed(1100, true)) {
				mc.player.jump();
			}
			if(jumpTimer.hasTimeElapsed(301, false)) {
				prepareArrow = true;
			}
			else {
				knockbackCount = 0;
			}
		}
		else {
			jumpTimer.reset();
		}
		
		double difX = mc.player.prevPosX - mc.player.posX;
		double difZ = mc.player.prevPosZ - mc.player.posZ;
		
		double dist = Math.sqrt(difX * difX + difZ * difZ);
		
		double addToPitch = mc.player.movementInput.moveForward == 0 || mc.player.isCollidedHorizontally ? 0 : 138;
		
		event.pitch = (float) (-90);
		
		if(Utils.returnItemStackIfPlayerHoldingItem(Items.BOW) != null && released && prepareArrow) {
			released = false;
			prepareArrow = false;
			releaseTimer.reset(); //start dragging arrow
			sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		}
		
	}
	
	@Override
	public void onLateUpdate() {
		super.onLateUpdate();
		
		double difX = mc.player.prevPosX - mc.player.posX;
		double difZ = mc.player.prevPosZ - mc.player.posZ;
		
		double dist = Math.max(1, Math.sqrt(difX * difX + difZ * difZ) * 1.2);
		
		if(releaseTimer.hasTimeElapsed(500, true) && !released) {
			released = true;
			mc.playerController.onStoppedUsingItem(mc.player); //release dragged arrow
			prepareArrow = true;
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
				
				if(knockbackCount == 1) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(700);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							moveForwards(1.35f);
						}
					}).start();
				}
				
				SPacketEntityVelocity vel = ((SPacketEntityVelocity) packet);
				
				double motionYFromPacket = ((double) vel.getMotionY()) / 8000D;
				
				if(motionYFromPacket > 0.02 && motionYFromPacket < 0.42) {
					motionYFromPacket = 0.42;
				}
				
				if(motionYFromPacket < 0) {
					motionYFromPacket = -motionYFromPacket;
				}
				
				if(mc.player.motionY > motionYFromPacket || motionYFromPacket < 0.06) {
					return;
				}
				
				double motionY = Math.min(0.440, motionYFromPacket);
				
				System.out.println(motionY + " = motionY, " + mc.player.motionY + " = player.motionY");
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int sleep = 0;
						if(knockbackCount == 1) {
							sleep = 600;
						}
						
						Jigsaw.chatMessage("sleep : " + sleep);
						try {
							Thread.sleep(sleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mc.addScheduledTask(new Runnable() {
							
							@Override
							public void run() {
								mc.player.motionY = motionY;
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
