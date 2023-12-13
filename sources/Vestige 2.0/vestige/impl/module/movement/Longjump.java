package vestige.impl.module.movement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;

@ModuleInfo(name = "Longjump", category = Category.MOVEMENT)
public class Longjump extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Redesky", "Redesky", "Redesky2", "Funcraft", "Hypixel bow", "Librecraft", "Verus");
	
	private boolean started;
	private int counter, ticks;
	
	public Longjump() {
		this.registerSettings(mode);
	}
	
	public void onEnable() {
		started = false;
		counter = ticks = 0;
		
		switch (mode.getMode()) {
			case "Hypixel":
				//mc.timer.timerSpeed = 0.2F;
				break;
		}
	}
	
	public void onDisable() {
		mc.thePlayer.speedInAir = 0.02F;
		mc.timer.timerSpeed = 1F;
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		switch (mode.getMode()) {
			case "Redesky":
				if(MovementUtils.isMoving()) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY += 0.16;
					} else {
						if(mc.thePlayer.motionY > 0.2) {
							mc.thePlayer.motionY += 0.06;
							mc.thePlayer.speedInAir = 0.038F;
						} else {
							mc.thePlayer.motionY += 0.03;
							mc.thePlayer.speedInAir = 0.03F;
						}
					}
				}
				break;
			case "Redesky2":
				if(MovementUtils.isMoving()) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY += 0.9;
						mc.thePlayer.motionX *= 1.1;
						mc.thePlayer.motionZ *= 1.1;
					} else {
						mc.thePlayer.speedInAir = 0.04F;
					}
				}
				break;
			case "Verus":
				if(mc.thePlayer.ticksExisted % 2 == 0) {
					mc.thePlayer.jump();
				}
				
				MovementUtils.strafe(MovementUtils.getBaseMoveSpeed());
				break;
		}
	}
	
	@Listener
	public void onMove(MoveEvent event) {
		switch (mode.getMode()) {
			case "Librecraft":
			case "Hypixel bow":
				if(counter == 14 && mode.is("Hypixel bow")) {
					event.setY(mc.thePlayer.motionY = 0.42);
				}
				
				if(mc.thePlayer.hurtTime == 9) {
					if(mode.is("Librecraft")) {
						event.setY(mc.thePlayer.motionY = 0.5);
					} else if(mode.is("Hypixel bow")) {
						event.setY(mc.thePlayer.motionY = 0.42);
					}
					
					ticks = 0;

					double jumpSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ?
							MovementUtils.getBaseMoveSpeed() + 0.425 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.02 :
								MovementUtils.getBaseMoveSpeed() + 0.425;

					MovementUtils.strafe(event, jumpSpeed);
					started = true;
				} else if(started && mc.thePlayer.onGround) {
					MovementUtils.strafe(event, 0);
					this.setEnabled(false);
					return;
				}
				
				if (++ticks == 1) {
					MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed() + 0.396);
				}

				if (mc.thePlayer.fallDistance > 0 && mc.thePlayer.fallDistance < 3) {
					mc.thePlayer.motionY += 0.02875;
				}

				if(!started) {
					MovementUtils.strafe(event, 0);
				}
				break;
			case "Funcraft":
				if(mc.thePlayer.onGround) {
					if(MovementUtils.isMoving()) {
						MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed() * 2.1);
						event.setY(mc.thePlayer.motionY = 0.4);
					}
				} else if(counter++ < 2) {
					MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed() * 2.2 /*3.5*/);
				} else {
					MovementUtils.strafe(event);
					if(counter > 5) {
						this.setEnabled(false);
						return;
					}
				}
				break;
		}
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		switch (mode.getMode()) {
			case "Hypixel bow":
			case "Librecraft":
				if(!started) {
					mc.timer.timerSpeed = 1F;
					for (int i = 0; i < 9; i++) {
						if (mc.thePlayer.inventory.getStackInSlot(i) == null)
							continue;

						Item item = mc.thePlayer.inventory.getStackInSlot(i).getItem();

						if (item instanceof ItemBow) {
							mc.thePlayer.inventory.currentItem = i;
							if (counter == 0) {
								
							} else if (counter == 1) {
								mc.gameSettings.keyBindUseItem.pressed = true;
							} else if (counter < 5) {
								event.setYaw(mc.thePlayer.rotationYaw);
								event.setPitch(-90);
								mc.gameSettings.keyBindUseItem.pressed = true;
							} else if (counter < 9) {
								event.setYaw(mc.thePlayer.rotationYaw);
								event.setPitch(-90);
								mc.gameSettings.keyBindUseItem.pressed = false;
							}
							counter++;
							break;
						} else if(item instanceof ItemEgg || item instanceof ItemSnowball) {
							mc.thePlayer.inventory.currentItem = i;
							if (counter > 0 && counter < 3) {
								event.setYaw(mc.thePlayer.rotationYaw);
								event.setPitch(-90);
								mc.gameSettings.keyBindUseItem.pressed = true;
							} else if (counter == 3) {
								event.setYaw(mc.thePlayer.rotationYaw);
								event.setPitch(-90);
								mc.gameSettings.keyBindUseItem.pressed = false;
							}
							counter++;
						}
					}
				}
				break;
			case "Hypixel":
				/*
				if(!started) {
					if(++ticks == 2) {
						double offsets [] = {0.41999998688698, 0.7531999805212, 1.00133597911215, 1.166109260938214, 1.24918707874468, 1.170787077218804, 1.015555072702206, 0.78502770378924, 0.4807108763317, 0.10408037809304, 0};
						PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
						for(int i = 0; i < 3; i++) {
							for(double offset : offsets) {
								PacketUtil.sendPacketNoEvent(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, mc.thePlayer.rotationYaw + (float) Math.random(), (float) Math.random(), false));
							}
						}
						mc.timer.timerSpeed = 1F;
					}
					
					if(ticks < 5) {
						event.setOnGround(false);
					} else if(ticks == 5) {
						event.setOnGround(true);
						started = true;
					}
				} else {
					switch (mc.thePlayer.hurtTime) {
						case 9:
							mc.thePlayer.jump();
							MovementUtils.strafe(MovementUtils.getHorizontalMotion() + 0.14);
							break;
						case 8:
						case 7:
							MovementUtils.strafe(MovementUtils.getHorizontalMotion() + 0.09);
							break;
						case 6:
							this.setEnabled(false);
							break;
					}
				}
				*/
				break;
		}
	}
	
}
