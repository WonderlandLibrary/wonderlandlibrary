package me.robbanrobbin.jigsaw.client.modules;

import java.util.List;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class LongJump extends Module {

	public LongJump() {
		super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes your jumps longer.");
	}

	public static boolean active;
	boolean speedTick;
	int delay;
	int delay2;
	boolean hypickle2bigboostready = true;
	int hypickle2delayafterbigboost = 0;
	public static int stage;
	private int airTicks;
	private int groundTicks;
	int timeonground;
	boolean off = false;
	
	@Override
	public ModSetting[] getModSettings() {
		SliderSetting timerSpeedSlider = new SliderSetting("Timer Speed", "longJumpTimerSpeed", 0.1, 1.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { timerSpeedSlider };
	}

	public void longjump() {
		if (mc.gameSettings.keyBindSneak.pressed) {
			return;
		}
		Jigsaw.disableSprint = true;
		mc.player.setSprinting(false);
		mc.gameSettings.keyBindLeft.pressed = false;
		mc.gameSettings.keyBindRight.pressed = false;
		mc.gameSettings.keyBindBack.pressed = false;
		if (isMoving()) {
			if (Keyboard.isKeyDown(56)) {
//				updatePosition(0.0D, 2.147483647E9D, 0.0D);
			}
			float direction3 = mc.player.rotationYaw + ((!mc.player.onGround) || (mc.player.moveForward < 0.0F) ? 0 : 0)
					+ (mc.player.moveStrafing > 0.0F ? -90.0F * (mc.player.moveForward > 0.0F ? 0.5F
							: mc.player.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F)
					- (mc.player.moveStrafing < 0.0F ? -90.0F * (mc.player.moveForward > 0.0F ? 0.5F
							: mc.player.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
			
			float xDir = (float) Math.cos((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
			float zDir = (float) Math.sin((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
			if (!mc.player.isCollidedVertically) {
				this.airTicks += 1;
				if (mc.gameSettings.keyBindSneak.isPressed()) {
				}
				this.groundTicks = 0;
				
				if (!mc.player.isCollidedVertically) {
					if (mc.player.motionY == -0.07190068807140403D) {
						mc.player.motionY *= 0.3499999940395355D;
					}
					if (mc.player.motionY == -0.10306193759436909D) {
						mc.player.motionY *= 0.550000011920929D;
					}
					if (mc.player.motionY == -0.13395038817442878D) {
						mc.player.motionY *= 0.6700000166893005D;
					}
					if (mc.player.motionY == -0.16635183030382D) {
						mc.player.motionY *= 0.6899999976158142D;
					}
					if (mc.player.motionY == -0.19088711097794803D) {
						mc.player.motionY *= 0.7099999785423279D;
					}
					if (mc.player.motionY == -0.21121925191528862D) {
//						Jigsaw.chatMessage("§bHELLO");
//						mc.player.motionY *= 0.90000000298023224D;
					}
					if (mc.player.motionY == -0.11979897632390576D) {
						mc.player.motionY *= 0.9300000071525574D;
					}
					if (mc.player.motionY == -0.18758479151225355D) {
						mc.player.motionY *= 0.7200000286102295D;
					}
					if (mc.player.motionY == -0.21075983825251726D) {
						mc.player.motionY *= 0.7599999904632568D;
					}
					if ((getDistance(mc.player, 69.0D) < 0.5D)
							&& (!Utils.getBlockState(new BlockPos(mc.player.posX,
									mc.player.posY - 0.32D, mc.player.posZ)).isFullCube())) {
						if (mc.player.motionY == -0.23537393014173347D) {
							mc.player.motionY *= 0.029999999329447746D;
						}
						if (mc.player.motionY == -0.08531999505205401D) {
							mc.player.motionY *= -0.5D;
						}
						if (mc.player.motionY == -0.03659320313669756D) {
							mc.player.motionY *= -0.10000000149011612D;
						}
						if (mc.player.motionY == -0.07481386749524899D) {
							mc.player.motionY *= -0.07000000029802322D;
						}
						if (mc.player.motionY == -0.0732677700939672D) {
							mc.player.motionY *= -0.05000000074505806D;
						}
						if (mc.player.motionY == -0.07480988066790395D) {
							mc.player.motionY *= -0.03999999910593033D;
						}
						if (mc.player.motionY == -0.0784000015258789D) {
							mc.player.motionY *= 0.10000000149011612D;
						}
						if (mc.player.motionY == -0.08608320193943977D) {
							mc.player.motionY *= 0.10000000149011612D;
						}
						if (mc.player.motionY == -0.08683615560584318D) {
							mc.player.motionY *= 0.05000000074505806D;
						}
						if (mc.player.motionY == -0.08265497329678266D) {
							mc.player.motionY *= 0.05000000074505806D;
						}
						if (mc.player.motionY == -0.08245009535659828D) {
							mc.player.motionY *= 0.05000000074505806D;
						}
						if (mc.player.motionY == -0.08244005633718426D) {
							mc.player.motionY = -0.08243956442521608D;
						}
						if (mc.player.motionY == -0.08243956442521608D) {
							mc.player.motionY = -0.08244005590677261D;
						}
						if ((mc.player.motionY > -0.1D) && (mc.player.motionY < -0.08D)
								&& (!mc.player.onGround) && (mc.gameSettings.keyBindForward.pressed)) {
							mc.player.motionY = -9.999999747378752E-5D;
						}
					} else {
						if ((mc.player.motionY < -0.2D) && mc.player.motionY > -0.3) {
							mc.player.motionY *= 0.9D;
						}
						if ((mc.player.motionY < -0.3D)) {
							mc.player.motionY *= 0.99D;
						}
					}
				}
				float timerSpeed = (float) ClientSettings.longJumpTimerSpeed;
				mc.timer.setTimerSpeed(timerSpeed);
				if (mc.gameSettings.keyBindForward.pressed || (airTicks >= 7 && mc.gameSettings.keyBindBack.pressed)) {
					double baseSpeed = 0.2873D;
					if (mc.player.isPotionActive(MobEffects.SPEED)) {
						int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
						baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
					}
					try {
						double factor = airTicks * 0.8d;
						
						if(airTicks == 1) {
							factor = 1.83;
						}
						if(airTicks == 2) {
							factor = 2.02f;
						}
						if(airTicks == 3) {
							factor = 2.42f;
						}
						if(airTicks == 4) {
							factor = 2.40f;
						}
						if(airTicks == 5) {
							factor = 2.22f;
						}
						if(airTicks == 6) {
							factor = 2.12;
						}
						if(airTicks == 7) {
							factor = 2.02f;
						}
						if(airTicks == 8) {
							factor = 1.92f;
						}
						if(airTicks == 9) {
							factor = 1.82f;
						}
						if(airTicks >= 10) {
							factor = 0.92f;
						}
//						Jigsaw.chatMessage(airTicks + " - " + factor);
						mc.player.motionX = (xDir * factor * baseSpeed);
						mc.player.motionZ = (zDir * factor * baseSpeed);
						this.off = true;
					} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
						
					}
				} else {
					mc.player.motionX = 0.0D;
					mc.player.motionZ = 0.0D;
				}
			} else {
				Utils.resetMcTimerTPS();
				this.airTicks = 0;
				this.groundTicks += 1;
				mc.player.motionX = (xDir * 0.3D);
				mc.player.motionZ = (zDir * 0.3D);
				if(off) {
					mc.player.motionX = 0;
					mc.player.motionZ = 0;
					this.setToggled(false, true);
					mc.gameSettings.keyBindForward.pressed = false;
				}
				else {
					mc.player.motionY = 0.42399999499320984D;
				}
//				mc.player.motionX = 0.0D;
//				mc.player.motionZ = 0.0D;
				if (this.groundTicks > 2) {
					this.groundTicks = 0;
					
					mc.player.motionY = 0.399399995803833D;
					
				}
			}
		}
	}

	private double addSpeedForSpeedEffect() {
		double baseSpeed = 1.0D;
		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
			baseSpeed = 0.4D;
		}
		return baseSpeed;
	}

	public void updatePosition(double x, double y, double z) {
		mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
	}

	private double getDistance(EntityPlayer player, double distance) {
		List boundingBoxes = mc.world.getCollisionBoxes(player,
				player.getEntityBoundingBox().offset(0.0D, -distance, 0.0D));
		if (boundingBoxes.isEmpty()) {
			return 0.0D;
		}
		double y = 0.0D;

		return player.posY - y;
	}

	public boolean isMoving() {
		if ((mc.player.movementInput.moveForward == 0.0F) && (mc.player.moveStrafing == 0.0F)) {
			return false;
		}
		return true;
	}

	public void onUpdate() {
		longjump();
		super.onUpdate();
	}
	
	@Override
	public void onToggle() {
		Jigsaw.disableSprint = false;
		Utils.resetMcTimerTPS();
		off = false;
		super.onToggle();
	}

	public String getValue() {
		return null;
	}

}
