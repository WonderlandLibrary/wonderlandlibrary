package artemis.mods.Movement;


import artemis.Category;
import artemis.Artemis;
import artemis.module;
import artemis.settings.Setting;
import artemis.util.TimeUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.ChatComponentText;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
/*
 * Credit to GA • Let's codes(GleichAngemeldet) for MC-Central fly
 * 
 * 
 */
public class Fly extends module {
    public String FlyMode = Artemis.instance.settingsManager.getSettingByName("Fly Mode").getValString();
	private int trys;;
TimeUtils time;

static int toggle = 0;
    public Fly() {
        super("Fly", "Allows you to fly", Keyboard.KEY_F, 876, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Hypixel");
        options.add("Vanilla");
        options.add("MC-Central");
        options.add("Cubecraft");
        options.add("Cubecraft2");
        options.add("Rewinside");
        options.add("AAC");
        Artemis.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Hypixel", options));
    }
    
    
    public void onUpdate() {
        if (isToggled()) {
        this.setName("Fly §7" + FlyMode);
        
        if(FlyMode.equalsIgnoreCase("Cubecarft2")) {
        mc.gameSettings.keyBindJump.pressed = true;
        if (time.hasTimerReached(300L)) {
            mc.thePlayer.setSprinting(false);
 
            mc.thePlayer.rotationYaw = this.mc.thePlayer.rotationYaw;
            mc.thePlayer.rotationPitch = this.mc.thePlayer.rotationPitch;
 
            mc.thePlayer.motionY -= 0.25f;
            mc.thePlayer.motionX *= 1.8f;
            mc.thePlayer.motionZ *= 1.8f;
 
            double yaw = Math.toRadians(this.mc.thePlayer.rotationYaw);
            double x = -Math.sin(yaw) * 3;
            double z = Math.cos(yaw) * 3;
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY - 0.25F, mc.thePlayer.posZ + z);
            mc.timer.timerSpeed = 0.12F;
            if (mc.thePlayer.onGround) {
                toggle += 1;
                if (toggle == 2) {
                    toggle = 0;
                    toggle();
                }
            }
            time.reset();
        }
        }
        if(FlyMode.equalsIgnoreCase("Hypixel")) {
            double y;
            double y1;
            mc.thePlayer.motionY = 0;
            if(mc.thePlayer.ticksExisted % 3 == 0) {
                y = mc.thePlayer.posY - 1.0E-10D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
            }
            y1 = mc.thePlayer.posY + 1.0E-10D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
        }

        if(FlyMode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.capabilities.allowFlying = true;
     }
        if(FlyMode.equalsIgnoreCase("AAC")) {
        	if (mc.theWorld != null && mc.thePlayer != null) {
                // OLD AAC
                if (mc.thePlayer.fallDistance > 0.0F)
                    mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 2 == 0 ? 0.1 : 0.0;
            }
        }
        
        if(FlyMode.equalsIgnoreCase("MC-Central")) {
        	if (mc.thePlayer.onGround) {
        	    mc.timer.timerSpeed = 1F;
        	 } else {
        	    mc.thePlayer.motionY = -0.001;
        	    mc.timer.timerSpeed = 0.8F;
        	   }
        	
     }
        if(FlyMode.equalsIgnoreCase("Cubecraft-BugUP")) {
        	this.mc.thePlayer.addChatMessage(new ChatComponentText("[Artemis] Press 'sneak' to activate!"));
        if (this.mc.thePlayer != null && this.mc.theWorld != null) {
			if (this.mc.thePlayer.fallDistance > 0 && this.mc.gameSettings.keyBindSneak.pressed) {
				this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1,
						this.mc.thePlayer.posZ);
				this.trys++;
				this.mc.thePlayer.fallDistance = 0;
				if (this.trys > 3) {
					this.trys = 0;
					this.mc.gameSettings.keyBindSneak.pressed = false;
				}
			}
			if (this.mc.thePlayer.onGround && this.trys > 0 && !this.mc.gameSettings.keyBindSneak.pressed) {
				this.trys = 0;
			}
		}
        }
        if(FlyMode.equalsIgnoreCase("Rewinside")) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            mc.gameSettings.keyBindLeft.pressed = false;
            mc.gameSettings.keyBindRight.pressed = false;
            mc.gameSettings.keyBindBack.pressed = false;
            mc.gameSettings.keyBindJump.pressed = false;
            mc.gameSettings.keyBindSprint.pressed = false;
            mc.thePlayer.setSprinting(false);
            mc.thePlayer.motionY = 0.0D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
            mc.thePlayer.onGround = true;
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY - 1.0E-10D, mc.thePlayer.posZ, true));
            }
        }
        }
        if(FlyMode.equalsIgnoreCase("Cubecraft")) {
        	double motionValue = 1.12, teleportValue = 2.2;


				double yaw = Math.toRadians(this.mc.thePlayer.rotationYaw);
				double motionX = -Math.sin(yaw) * motionValue;
				double motionZ = Math.cos(yaw) * motionValue;

				this.mc.thePlayer.motionX = motionX;
				this.mc.thePlayer.motionZ = motionZ;

				double teleportX = -Math.sin(yaw) * teleportValue;
				double teleportZ = Math.cos(yaw) * teleportValue;

				this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + teleportX, this.mc.thePlayer.posY - 0.25F,
						this.mc.thePlayer.posZ + teleportZ);

				this.mc.timer.timerSpeed = 0.12F;
				this.mc.thePlayer.motionY -= 0.25F;

				//this.time.clear();
			}
    }
    }

    @Override
    public void onDisable() {
    	this.trys = 0;
        super.onDisable();
        mc.timer.timerSpeed = 1F;
        if(FlyMode.equalsIgnoreCase("Vanilla"))
            mc.thePlayer.capabilities.isFlying = false;
    }
}