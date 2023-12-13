package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.HighJumpMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Module.Mod(displayName = "HighJump")
public class HighJump extends Module
{   
    @Op(name = "Motion", min = 0.42, max = 10.0, increment = 0.01)
    private double motion;
    @Op(name = "Bobbing")
    private boolean bobbing;
	private HighJumpMode vanilla;
	private HighJumpMode oldncp;
	private HighJumpMode spartan;
	private HighJumpMode mineplex;
	private HighJumpMode down;
	private HighJumpMode minesecure;
	private HighJumpMode aac;
	private HighJumpMode aac2;
	private HighJumpMode cubecraft;
	private HighJumpMode position;
	private HighJumpMode teleport;
	private HighJumpMode timer;
	private HighJumpMode voidmod;
	private HighJumpMode matrix;
	Minecraft mc = Minecraft.getMinecraft();	
	private boolean sentineldisabled;
    private double moveSpeed;
    private int stage;
    
    public HighJump() {
        this.vanilla = new HighJumpMode("Vanilla", true, this);
        this.oldncp = new HighJumpMode("OldNCP", false, this);
        this.spartan = new HighJumpMode("Spartan", false, this);
        this.mineplex = new HighJumpMode("Mineplex", false, this);
        this.down = new HighJumpMode("Down", false, this);
        this.minesecure = new HighJumpMode("MineSecure", false, this);
        this.aac = new HighJumpMode("AAC", false, this);
        this.aac2 = new HighJumpMode("AAC2", false, this);
        this.cubecraft = new HighJumpMode("CubeCraft", false, this);
        this.position = new HighJumpMode("Position", false, this);
        this.teleport = new HighJumpMode("Teleport", false, this);
        this.timer = new HighJumpMode("Timer", false, this);
        this.voidmod = new HighJumpMode("Void", false, this);
        this.matrix = new HighJumpMode("Matrix", false, this);
        this.motion = 1;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.oldncp);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.down);
        OptionManager.getOptionList().add(this.minesecure);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.aac2);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.timer);
        OptionManager.getOptionList().add(this.voidmod);
        OptionManager.getOptionList().add(this.matrix);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.oldncp.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("OldNCP")).toString());
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.down.getValue()) {
        	this.setSuffix("Down");
        }
        else if (this.minesecure.getValue()) {
        	this.setSuffix("MineSecure");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.aac2.getValue()) {
        	this.setSuffix("AAC2");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.position.getValue()) {
        	this.setSuffix("Position");
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix("Teleport");
        }
        else if (this.timer.getValue()) {
        	this.setSuffix("Timer");
        }
        else if (this.voidmod.getValue()) {
        	this.setSuffix("Void");
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
    }
    
    public void enable() {
    	this.sentineldisabled = false;
    	if (this.cubecraft.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ, false));
        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    	}
    	super.enable();
    }
    
    public void disable() {
    	Timer.timerSpeed = 1.0f;
    	this.sentineldisabled = false;
    	if (this.oldncp.getValue()) {
			ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    	}
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    	if (this.minesecure.getValue()) {
            mc.thePlayer.motionY += 0.049999;
    	}
    	else if (this.aac.getValue()) {
        	mc.thePlayer.motionY += 0.059D;
    	}
    	else if (this.aac2.getValue()) {
    		mc.thePlayer.motionY += mc.thePlayer.fallDistance == 0 ? 0.0499D : 0.05D;
    	}
    }
    
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
    	if (this.vanilla.getValue()) {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.motionY = motion;
		}
	}
    	else if (this.oldncp.getValue()) {
			ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    		if (mc.thePlayer.ticksExisted % 3 == 0) {
    	        mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                }
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.motionY = motion;
    		}
    	}
    	else if (this.spartan.getValue()) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.motionY = 1;
    		}
    	}
    	else if (this.down.getValue()) {
    		if (mc.thePlayer.ticksExisted % 3 == 0) {
			mc.thePlayer.motionY = -motion;
    		}
    	}
    	else if (this.position.getValue()) {
    		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
    	}
    	else if (this.teleport.getValue()) {
    		mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
    	}
    	else if (this.timer.getValue()) {
    		Timer.timerSpeed = 0.2f;
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.motionY = motion;
    		}
    	}
    	else if (this.voidmod.getValue()) {
    		if (mc.thePlayer.fallDistance > 3.5f) {
    			event.setGround(true);
    			mc.thePlayer.motionY = 1;
    			mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.matrix.getValue()) {
    		if (mc.thePlayer.onGround) {
    			event.setGround(false);
    			mc.thePlayer.motionY = 0.7;
    			mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.cubecraft.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    			this.sentineldisabled = true;
    		}
    		if (this.sentineldisabled) {
    			event.setGround(false);
            	event.setY(event.getY() + 1.0E-12);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
            	event.setY(event.getY() - 1.0E-12);
    			if (mc.thePlayer.onGround) {
    				mc.thePlayer.motionY = motion;
    			}
    		}
    	}
	}
	}
	
	@EventTarget
    private void onMove(final MoveEvent event) {
		if (this.spartan.getValue()) {
			ClientUtils.setMoveSpeed(event, 2.0);
		}
		else if (this.mineplex.getValue()) {
			Timer.timerSpeed = 1.0f;
	        if (!mc.thePlayer.isMoving()) {
	            this.stage = 0;
	        }
	        if (mc.thePlayer.onGround) {
	            event.setX(0.0);
	            event.setZ(0.0);
	            Timer.timerSpeed *= 4.0;
	            this.moveSpeed = 0.8;
	            event.setY(mc.thePlayer.motionY = 0.42);
	            ++this.stage;
	            return;
	        }
	        this.moveSpeed -= 0.01;
	        if (this.stage > -17273) {
	            ClientUtils.setMoveSpeed(event, this.moveSpeed);
	        }
			if (mc.thePlayer.onGround) {
				ClientUtils.setMoveSpeed(event, 0);
				event.setY(mc.thePlayer.motionY = 0.42);
			}
	        if (mc.thePlayer.fallDistance == 0.0f) {
                event.setY(mc.thePlayer.motionY += 0.038);
            }
            else if (mc.thePlayer.fallDistance <= 1.4) {
                event.setY(mc.thePlayer.motionY += 0.032);
            }
		}
		else if (this.cubecraft.getValue()) {
			if (!this.sentineldisabled) {
				ClientUtils.setMoveSpeed(event, 0);
			}
		}
	}
}
