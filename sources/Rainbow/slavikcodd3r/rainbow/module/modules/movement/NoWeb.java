package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.NoWebMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "NoWeb")
public class NoWeb extends Module
{
    @Op(name = "Speed", min = 0.1, max = 1.3, increment = 1)
    public double speed;
	private NoWebMode vanilla;
	private NoWebMode teleport;
	private NoWebMode oldaac;
	private NoWebMode aac;
	private NoWebMode rewinside;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public NoWeb() {
        this.vanilla = new NoWebMode("Vanilla", true, this);
        this.teleport = new NoWebMode("Teleport", false, this);
        this.oldaac = new NoWebMode("OldAAC", false, this);
        this.aac = new NoWebMode("AAC", false, this);
        this.rewinside = new NoWebMode("Rewinside", false, this);
        this.speed = 1.0;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.rewinside);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Teleport")).toString());
        }
        else if (this.oldaac.getValue()) {
        	this.setSuffix("OldAAC");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.rewinside.getValue()) {
        	this.setSuffix("Rewinside");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	final BlockPos pos1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockWeb) {
        	if (this.oldaac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.59f;
        		if (!ClientUtils.movementInput().jump || !ClientUtils.movementInput().sneak) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.aac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 1.0f;
        		if (!ClientUtils.movementInput().jump || !ClientUtils.movementInput().sneak) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.rewinside.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.42f;
        	}
        }
        final BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockWeb) {
        	if (this.oldaac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.59f;
        		if (!ClientUtils.movementInput().jump || !ClientUtils.movementInput().sneak) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.aac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 1.0f;
        		if (!ClientUtils.movementInput().jump || !ClientUtils.movementInput().sneak) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.rewinside.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.42f;
        	}
        }
        final BlockPos pos3 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockWeb) {
        	if (this.oldaac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.59f;
        		if (!ClientUtils.movementInput().jump || !ClientUtils.movementInput().sneak) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.aac.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 1.0f;
        		if (!ClientUtils.movementInput().jump) {
        			mc.thePlayer.motionY = 0.0;
        		}
        	}
        	if (this.rewinside.getValue()) {
        		mc.thePlayer.jumpMovementFactor = 0.42f;
        	}
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    	final BlockPos pos1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockWeb) {
        	if (this.vanilla.getValue()) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}
        	if (this.teleport.getValue() && mc.thePlayer.ticksExisted % 2 == 0) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}        	
        }
        final BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockWeb) {
        	if (this.vanilla.getValue()) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}
        	if (this.teleport.getValue() && mc.thePlayer.ticksExisted % 2 == 0) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}        	
        }
        final BlockPos pos3 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockWeb) {
        	if (this.vanilla.getValue()) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}
        	if (this.teleport.getValue() && mc.thePlayer.ticksExisted % 2 == 0) {
        		ClientUtils.setMoveSpeed(event, speed);
        		if (ClientUtils.movementInput().jump) {
        			event.setY(mc.thePlayer.motionY = speed);
        		}
        		if (ClientUtils.movementInput().sneak) {
        			event.setY(mc.thePlayer.motionY = -speed);
        		}
        	}        	
        }
    }
}
