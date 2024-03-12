package xyz.cucumber.base.module.feat.combat;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventKnockBack;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to take lower knockback", name = "Velocity", priority = ArrayPriority.HIGH)
public class VelocityModule extends Mod {

	public ModeSettings mode = new ModeSettings("Mode",
			new String[] { "Vanilla", "Legit", "Vulcan", "Intave", "AAC", "Matrix", "Hypixel", "MMC Stack", "MMC Delay", "Polar" });

	public NumberSettings horizontal = new NumberSettings("Horizontal", 0, 0, 100, 1);
	public NumberSettings vertical = new NumberSettings("Vertical", 0, 0, 100, 1);

	public BooleanSettings jump = new BooleanSettings("Jump", false);
	
	public BooleanSettings verticalReduce = new BooleanSettings("Vertical Reduce", false);
	public BooleanSettings intaveReverse = new BooleanSettings("Intave Reverse", false);
	public BooleanSettings intaveStrong = new BooleanSettings("Intave Strong", false);
	
	public BooleanSettings cancelInAir = new BooleanSettings("Cancel In Air", false);

	public boolean blockVelocity, isWorking;

	public EntityLivingBase entity;

	private KillAuraModule killAura;

	public ArrayList<Packet> packets = new ArrayList<Packet>();

	public Timer timer = new Timer();

	public double lastX, lastZ;

	public int ticks, updates;

	public VelocityModule() {
		this.addSettings(mode, horizontal, vertical, jump, verticalReduce, intaveReverse,
				intaveStrong, cancelInAir);
	}

	public void onEnable() {
		this.killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class));
	}

	@EventListener
	public void onUpdate(EventUpdate e) {
		info = mode.getMode();
		switch (mode.getMode().toLowerCase()) {

		case "intave":
			blockVelocity = true;
			
			if(mc.objectMouseOver == null) {
				return;
			}
			
			if(mc.objectMouseOver.entityHit != null && mc.thePlayer.hurtTime == 9 && !mc.thePlayer.isBurning() && jump.isEnabled()) {
                mc.thePlayer.movementInput.jump = true;
                ticks++;
            }
			break;
		case "legit":
			if (mc.objectMouseOver.entityHit != null && mc.thePlayer.hurtTime == 9 && !mc.thePlayer.isBurning()) {
				mc.thePlayer.movementInput.jump = true;
				ticks++;
			}
			break;

		case "aac":
			if (mc.thePlayer.hurtTime == 7) {
				mc.thePlayer.motionX *= 0.25;
				mc.thePlayer.motionZ *= 0.25;
			}

			break;

		case "matrix":
			if (mc.thePlayer.hurtTime > 0) {
				mc.thePlayer.motionX *= 0.6;
				mc.thePlayer.motionZ *= 0.6;
			}
			break;
		}
	}

	@EventListener
	public void onTick(EventTick e) {
		switch (mode.getMode().toLowerCase()) {
		case "mmc delay":
			ticks++;
			break;
		}
	}
	
	@EventListener
	public void onEventKnockBack(EventKnockBack e) {
		e.setMotion(0.6);
	}

	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		Packet p = e.getPacket();

		switch (mode.getMode().toLowerCase()) {

		case "intave":
			if (p instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)p;
                
                if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                	isWorking = true;
                	
                	if(mc.thePlayer.isBurning()) {
                		
                	}
                	
        			if(mc.objectMouseOver.entityHit != null && mc.thePlayer.hurtTime == 9 && !mc.thePlayer.isBurning() && jump.isEnabled()) {
                        mc.thePlayer.movementInput.jump = true;
                        ticks++;
                    }
                }
            }
			break;
		case "vanilla":
		case "vulcan":
			if (p instanceof S12PacketEntityVelocity
					&& ((S12PacketEntityVelocity) p).getEntityID() == mc.thePlayer.getEntityId()) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
				packet.motionX = ((int) ((packet.getMotionX() / 100) * horizontal.getValue()));
				packet.motionY = ((int) ((packet.getMotionY() / 100) * vertical.getValue()));
				packet.motionZ = ((int) ((packet.getMotionZ() / 100) * horizontal.getValue()));

				if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
					e.setCancelled(true);
				}
			}
			break;

		case "hypixel":
			if (p instanceof S12PacketEntityVelocity
					&& ((S12PacketEntityVelocity) p).getEntityID() == mc.thePlayer.getEntityId()) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
				
				if(!mc.thePlayer.onGround) {
					if(cancelInAir.isEnabled()) {
						e.setCancelled(true);	
					}else {
						packet.motionX *= -0.15;
						packet.motionZ *= -0.15;
					}
				}else {
					packet.motionX *= -0.15;
					packet.motionZ *= -0.15;
				}
				
				blockVelocity = true;
			}
			break;
		case "mmc stack":
			if (p instanceof S12PacketEntityVelocity) {
				blockVelocity = !blockVelocity;
				
				if(blockVelocity) {
					e.setCancelled(true);
				}
			}
			break;
		case "mmc delay":
			if (p instanceof S12PacketEntityVelocity) {
				if(ticks >= 25) {
					e.setCancelled(true);
					ticks = 0;
				}
			}
			break;
		}
	}
	@EventListener
	public void onSendPacket(EventSendPacket e) {
		Packet p = e.getPacket();
		
		switch (mode.getMode().toLowerCase()) {
		case "vulcan":
			if(p instanceof C0FPacketConfirmTransaction) {
				if(mc.thePlayer.hurtTime > 0) {
					e.setCancelled(true);
				}
			}
			break;
		}
	}
	
	@EventListener
	public void onMoveButton(EventMoveButton e) {
		switch(mode.getMode().toLowerCase()) {
		case "intave":
			if(mc.thePlayer.hurtTime > 0 && mc.objectMouseOver.entityHit != null) {
				e.forward = true;
			}
			break;
		}
	}
	
	@EventListener
	public void onKnockBack(EventKnockBack e) {
		switch(mode.getMode().toLowerCase()) {
		case "intave":
			e.setFull(!intaveStrong.isEnabled());
			e.setReduceY(verticalReduce.isEnabled());
			break;

		}
	}
	
	@EventListener
	public void onAttack(EventAttack e) {
		switch(mode.getMode().toLowerCase()) {
		case "intave":
			if(mc.objectMouseOver.entityHit != null) {
				if(mc.thePlayer.hurtTime > 0 && blockVelocity) {
					mc.thePlayer.setSprinting(false);
					
					if((!intaveReverse.isEnabled() || isWorking) && intaveStrong.isEnabled()) {
						mc.thePlayer.motionX *= 0.6;
						mc.thePlayer.motionZ *= 0.6;
					}
					
					if(mc.thePlayer.hurtTime <= 6 && intaveReverse.isEnabled()) {
						if(isWorking) {
							mc.thePlayer.motionX = -Math.sin(Math.toRadians(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw)) * 0.02f;
							mc.thePlayer.motionZ = Math.cos(Math.toRadians(RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw)) * 0.02f;
						
							isWorking = false;
						}
					}
					
					blockVelocity = false;
	        	}
			}
			break;
		}
	}
}