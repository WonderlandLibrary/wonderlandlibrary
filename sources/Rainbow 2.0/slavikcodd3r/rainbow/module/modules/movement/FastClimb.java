package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.FastClimbMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "FastClimb")
public class FastClimb extends Module
{
	private FastClimbMode ncp;
	private FastClimbMode mineplex;
	private FastClimbMode dac;
	private FastClimbMode spartan;
	private FastClimbMode cubecraft;
	private FastClimbMode aac;
	private FastClimbMode horizon;
	private FastClimbMode aerox;
	private FastClimbMode teleport;
	private FastClimbMode jump;
	private FastClimbMode matrix;
	private FastClimbMode bac;
    @Op(name = "Bobbing")
    private boolean bobbing;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public FastClimb() {
        this.ncp = new FastClimbMode("NCP", true, this);
        this.mineplex = new FastClimbMode("Mineplex", false, this);
        this.dac = new FastClimbMode("DAC", false, this);
        this.spartan = new FastClimbMode("Spartan", false, this);
        this.cubecraft = new FastClimbMode("CubeCraft", false, this);
        this.aac = new FastClimbMode("AAC", false, this);
        this.horizon = new FastClimbMode("Horizon", false, this);
        this.aerox = new FastClimbMode("Aerox", false, this);
        this.teleport = new FastClimbMode("Teleport", false, this);
        this.jump = new FastClimbMode("Jump", false, this);
        this.matrix = new FastClimbMode("Matrix", false, this);
        this.bac = new FastClimbMode("BAC", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.dac);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.horizon);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.jump);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.bac);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.ncp.getValue()) {
            this.setSuffix("NCP");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.dac.getValue()) {
        	this.setSuffix("DAC");
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.horizon.getValue()) {
        	this.setSuffix("Horizon");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix("Teleport");
        }
        else if (this.jump.getValue()) {
        	this.setSuffix("Jump");
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
        else if (this.bac.getValue()) {
        	this.setSuffix("BAC");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	Timer.timerSpeed = 1.0f;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    	if (mc.thePlayer.isMoving()) {
    	if (this.bobbing && ClientUtils.player().isOnLadder()) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
        if (this.ncp.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY = 0.28);
        }
        else if (this.mineplex.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	mc.thePlayer.onGround = true;
        	event.setY(mc.thePlayer.motionY += 0.09200000017881393);
        }
        else if (this.dac.getValue()) {
        	if (event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	Timer.timerSpeed = 10.0f;
        	} else {
            Timer.timerSpeed = 1.0f;	
        	}
        }
        else if (this.spartan.getValue()) {
        	if (event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	Timer.timerSpeed = 10.0f;	
        	} else {
            Timer.timerSpeed = 1.0f;	
        	}
        }
        else if (this.cubecraft.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY = 0.28);
        }
        else if (this.aac.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY += 0.001);
        }
        else if (this.horizon.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY = 9);
        }
        else if (this.aerox.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY = 0.42);
        }
        else if (this.teleport.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
        }
        else if (this.jump.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder() && mc.thePlayer.ticksExisted % 2 == 0) {
        	mc.thePlayer.jump();
        }
        else if (this.bac.getValue() && event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	event.setY(mc.thePlayer.motionY = 0.42);
        }
        else if (this.matrix.getValue()) {
        	if (event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
        	Timer.timerSpeed = 0.3f;
        	if (mc.thePlayer.ticksExisted % 2 == 0) {
        	Timer.timerSpeed = 10.0f;	
        	}
        	} else {
            Timer.timerSpeed = 1.0f;	
        	}
        }
    	}
    }
}
