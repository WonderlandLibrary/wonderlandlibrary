package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.module.modes.VelocityMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Velocity")
public class Velocity extends Module
{
    @Op(name = "Percent", min = 0.0, max = 10000.0, increment = 1.0)
    private double percent;
    @Op(name = "SetX", min = 0.0, max = 10.0, increment = 0.01)
    private double setx;
    @Op(name = "SetY", min = 0.0, max = 10.0, increment = 0.01)
    private double sety;
    @Op(name = "SetZ", min = 0.0, max = 10.0, increment = 0.01)
    private double setz;
	private VelocityMode vanilla;
	private VelocityMode cancel;
	private VelocityMode oldaac;
	private VelocityMode oldaac2;
	private VelocityMode aac;
	private VelocityMode reverse;
	private VelocityMode set;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Velocity() {
        this.vanilla = new VelocityMode("Vanilla", true, this);
        this.cancel = new VelocityMode("Cancel", false, this);
        this.oldaac = new VelocityMode("OldAAC", false, this);
        this.oldaac2 = new VelocityMode("OldAAC2", false, this);
        this.aac = new VelocityMode("AAC", false, this);
        this.reverse = new VelocityMode("Reverse", false, this);
        this.set = new VelocityMode("Set", false, this);
        this.percent = 0.0;
        this.setx = 0.0;
        this.sety = 0.0;
        this.setz = 0.0;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.cancel);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.oldaac2);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.reverse);
        OptionManager.getOptionList().add(this.set);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.cancel.getValue()) {
        	this.setSuffix("Cancel");
        }
        else if (this.oldaac.getValue()) {
        	this.setSuffix("OldAAC");
        }
        else if (this.oldaac2.getValue()) {
        	this.setSuffix("OldAAC2");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.reverse.getValue()) {
        	this.setSuffix("Reverse");
        }
        else if (this.set.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Set")).toString());
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
    	if (this.aac.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    			if (mc.thePlayer.onGround) {
    				mc.thePlayer.jump();
    			}
    		}
    	}
    }
    
    @EventTarget
    private void onPacket(final PacketReceiveEvent event) {
    	if (this.cancel.getValue()) {
    		if (event.getPacket() instanceof S12PacketEntityVelocity) {
    			event.setCancelled(true);
    		}
    		if (event.getPacket() instanceof S27PacketExplosion) {
    			event.setCancelled(true);
    		}
    	}
    	if (this.vanilla.getValue()) {
    		if (event.getPacket() instanceof S12PacketEntityVelocity) {
                final S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
                if (ClientUtils.world().getEntityByID(packet.func_149412_c()) == ClientUtils.player()) {
                    if (this.percent > 0.0) {
                        final S12PacketEntityVelocity s12PacketEntityVelocity5;
                        final S12PacketEntityVelocity s12PacketEntityVelocity10;
                        final S12PacketEntityVelocity s12PacketEntityVelocity4 = s12PacketEntityVelocity10 = (s12PacketEntityVelocity5 = packet);
                        s12PacketEntityVelocity10.field_149415_b *= (int)(this.percent / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity7;
                        final S12PacketEntityVelocity s12PacketEntityVelocity11;
                        final S12PacketEntityVelocity s12PacketEntityVelocity6 = s12PacketEntityVelocity11 = (s12PacketEntityVelocity7 = packet);
                        s12PacketEntityVelocity11.field_149416_c *= (int)(this.percent / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity9;
                        final S12PacketEntityVelocity s12PacketEntityVelocity12;
                        final S12PacketEntityVelocity s12PacketEntityVelocity8 = s12PacketEntityVelocity12 = (s12PacketEntityVelocity9 = packet);
                        s12PacketEntityVelocity12.field_149414_d *= (int)(this.percent / 100.0);
                    }
                    else {
                        event.setCancelled(true);
                    }
                }
            }
            else if (event.getPacket() instanceof S27PacketExplosion) {
                final S27PacketExplosion packet2;
                final S27PacketExplosion s27PacketExplosion5;
                final S27PacketExplosion s27PacketExplosion10;
                final S27PacketExplosion s27PacketExplosion4 = s27PacketExplosion10 = (s27PacketExplosion5 = (packet2 = (S27PacketExplosion)event.getPacket()));
                s27PacketExplosion10.field_149152_f *= (float)(this.percent / 100.0);
                final S27PacketExplosion s27PacketExplosion7;
                final S27PacketExplosion s27PacketExplosion11;
                final S27PacketExplosion s27PacketExplosion6 = s27PacketExplosion11 = (s27PacketExplosion7 = packet2);
                s27PacketExplosion11.field_149153_g *= (float)(this.percent / 100.0);
                final S27PacketExplosion s27PacketExplosion9;
                final S27PacketExplosion s27PacketExplosion12;
                final S27PacketExplosion s27PacketExplosion8 = s27PacketExplosion12 = (s27PacketExplosion9 = packet2);
                s27PacketExplosion12.field_149159_h *= (float)(this.percent / 100.0);
            }
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.set.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    			mc.thePlayer.setVelocity(setx, sety, setz);
    		}
    	}
    	if (this.reverse.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    			mc.thePlayer.onGround = true;
    		}
    	}
    	if (this.oldaac2.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    		 double yaw = ClientUtils.mc().thePlayer.rotationYawHead;
             yaw = Math.toRadians(yaw);
             final double motionX = -Math.sin(yaw) * 0.08;
             final double motionZ = Math.cos(yaw) * 0.08;
             ClientUtils.mc().thePlayer.motionX = motionX;
             ClientUtils.mc().thePlayer.motionZ = motionZ;
    		}
    	}
    	if (this.oldaac.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.26, mc.thePlayer.posZ);
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.3, mc.thePlayer.posZ);
            }
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    	if (this.set.getValue()) {
    		if (mc.thePlayer.hurtTime > 0) {
    			ClientUtils.setMoveSpeed(event, 0);
    		}
    	}
    }
}
