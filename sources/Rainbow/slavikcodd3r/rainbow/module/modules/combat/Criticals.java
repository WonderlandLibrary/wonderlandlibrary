package slavikcodd3r.rainbow.module.modules.combat;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.AttackEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.CriticalsMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.module.modules.movement.LongJump;
import slavikcodd3r.rainbow.module.modules.movement.Speed;
import slavikcodd3r.rainbow.module.modules.player.SafeWalk;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Module.Mod(displayName = "Criticals")
public class Criticals extends Module
{
	private CriticalsMode packet;
	private CriticalsMode motion;
	private CriticalsMode jump;
	private CriticalsMode fakejump;
	private CriticalsMode fakejumppacket;
	private CriticalsMode noground;
	private CriticalsMode nogroundpacket;
	private CriticalsMode minijump;
	private CriticalsMode minijumppacket;
	private CriticalsMode mineplex;
	private CriticalsMode hypixel;
	private CriticalsMode flag;
	private CriticalsMode oldaac;
	private CriticalsMode aac;
	private CriticalsMode agc;
	private CriticalsMode ncp;
	private CriticalsMode latestncp;
	private CriticalsMode down;
	private CriticalsMode oldspartan;
	private CriticalsMode spartan;
	private CriticalsMode position;
	private CriticalsMode teleport;
	private CriticalsMode zero;
	private CriticalsMode aerox;
	private CriticalsMode security;
	private CriticalsMode hover;
	private CriticalsMode spam;
	private CriticalsMode other;
	public static boolean docrits;
    int groundTicks;
    int stage;
    int count;
    double y;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Criticals() {
        this.packet = new CriticalsMode("Packet", true, this);
        this.motion = new CriticalsMode("Motion", false, this);
        this.jump = new CriticalsMode("Jump", false, this);
        this.fakejump = new CriticalsMode("FakeJump", false, this);
        this.fakejumppacket = new CriticalsMode("FakeJumpPacket", false, this);
        this.noground = new CriticalsMode("NoGround", false, this);
        this.nogroundpacket = new CriticalsMode("NoGroundPacket", false, this);
        this.minijump = new CriticalsMode("MiniJump", false, this);
        this.minijumppacket = new CriticalsMode("MiniJumpPacket", false, this);
        this.mineplex = new CriticalsMode("Mineplex", false, this);
        this.hypixel = new CriticalsMode("Hypixel", false, this);
        this.flag = new CriticalsMode("Flag", false, this);
        this.oldaac = new CriticalsMode("OldAAC", false, this);
        this.aac = new CriticalsMode("AAC", false, this);
        this.agc = new CriticalsMode("AGC", false, this);
        this.ncp = new CriticalsMode("NCP", false, this);
        this.latestncp = new CriticalsMode("LatestNCP", false, this);
        this.down = new CriticalsMode("Down", false, this);
        this.oldspartan = new CriticalsMode("OldSpartan", false, this);
        this.spartan = new CriticalsMode("Spartan", false, this);
        this.position = new CriticalsMode("Position", false, this);
        this.teleport = new CriticalsMode("Teleport", false, this);
        this.zero = new CriticalsMode("Zero", false, this);
        this.aerox = new CriticalsMode("Aerox", false, this);
        this.security = new CriticalsMode("Security", false, this);
        this.hover = new CriticalsMode("Hover", false, this);
        this.spam = new CriticalsMode("Spam", false, this);
        this.other = new CriticalsMode("Other", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.motion);
        OptionManager.getOptionList().add(this.jump);
        OptionManager.getOptionList().add(this.fakejump);
        OptionManager.getOptionList().add(this.fakejumppacket);
        OptionManager.getOptionList().add(this.noground);
        OptionManager.getOptionList().add(this.nogroundpacket);
        OptionManager.getOptionList().add(this.minijump);
        OptionManager.getOptionList().add(this.minijumppacket);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.hypixel);
        OptionManager.getOptionList().add(this.flag);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.agc);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.latestncp);
        OptionManager.getOptionList().add(this.down);
        OptionManager.getOptionList().add(this.oldspartan);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.zero);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.security);
        OptionManager.getOptionList().add(this.hover);
        OptionManager.getOptionList().add(this.spam);
        OptionManager.getOptionList().add(this.other);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.packet.getValue()) {
            this.setSuffix("Packet");
        }
        else if (this.motion.getValue()) {
        	this.setSuffix("Motion");
        }
        else if (this.jump.getValue()) {
        	this.setSuffix("Jump");
        }
        else if (this.fakejump.getValue()) {
        	this.setSuffix("FakeJump");
        }
        else if (this.fakejumppacket.getValue()) {
        	this.setSuffix("FakeJumpPacket");
        }
        else if (this.noground.getValue()) {
        	this.setSuffix("NoGround");
        }
        else if (this.nogroundpacket.getValue()) {
        	this.setSuffix("NoGroundPacket");
        }
        else if (this.minijump.getValue()) {
        	this.setSuffix("MiniJump");
        }
        else if (this.minijumppacket.getValue()) {
        	this.setSuffix("MiniJumpPacket");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.hypixel.getValue()) {
        	this.setSuffix("Hypixel");
        }
        else if (this.flag.getValue()) {
        	this.setSuffix("Flag");
        }
        else if (this.oldaac.getValue()) {
        	this.setSuffix("OldAAC");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.agc.getValue()) {
        	this.setSuffix("AGC");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.latestncp.getValue()) {
        	this.setSuffix("LatestNCP");
        }
        else if (this.down.getValue()) {
        	this.setSuffix("Down");
        }
        else if (this.oldspartan.getValue()) {
        	this.setSuffix("OldSpartan");
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.position.getValue()) {
        	this.setSuffix("Position");
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix("Teleport");
        }
        else if (this.zero.getValue()) {
        	this.setSuffix("Zero");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
        else if (this.security.getValue()) {
        	this.setSuffix("Security");
        }
        else if (this.hover.getValue()) {
        	this.setSuffix("Hover");
        }
        else if (this.spam.getValue()) {
        	this.setSuffix("Spam");
        }
        else if (this.other.getValue()) {
        	this.setSuffix("Other");
        }
    }
    
    public void enable() {
    	this.docrits = false;
    	if (this.noground.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.hypixel.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.oldaac.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.oldspartan.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.agc.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.aerox.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	else if (this.security.getValue()) {
    		if (mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	}
    	super.enable();
    }
    
    public void disable() {
    	this.docrits = false;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onPacket(final PacketReceiveEvent event) {
    	if (this.aerox.getValue()) {
            if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof S00PacketKeepAlive) {
                event.setCancelled(true);
            }
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.noground.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.hypixel.getValue()) {
    		event.setGround(false);
    		if (mc.thePlayer.ticksExisted % 100 == 0) {	   
 			   mc.thePlayer.fallDistance = (float) (Math.random() * 1.0E-12);		   
 		   }
    	}
    	else if (this.oldaac.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.aac.getValue()) {
    		mc.thePlayer.lastReportedPosY = 0.0;
            final double ypos = mc.thePlayer.posY;
            if (MoveUtils.isOnGround(0.001)) {
                event.setGround(false);
                if (this.stage == 0) {
                    this.y = ypos + 1.0E-8;
                    event.setGround(true);
                }
                else if (this.stage == 1) {
                    this.y -= 5.0E-15;
                }
                else {
                    this.y -= 4.0E-15;
                }
                if (this.y <= mc.thePlayer.posY) {
                    this.stage = 0;
                    this.y = mc.thePlayer.posY;
                    event.setGround(true);
                }
                event.setY(this.y);
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
    	else if (this.agc.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.oldspartan.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.aerox.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.security.getValue()) {
    		event.setGround(false);
    	}
    	else if (this.fakejump.getValue() && mc.thePlayer.isSwingInProgress && mc.thePlayer.onGround) {
    		event.setY(event.getY() + 0.2);
    		event.setGround(false);
    	}
    	else if (this.mineplex.getValue()) {
    		mc.thePlayer.fallDistance = (float) 1.0E-12;
    	}
    	else if (this.hover.getValue()) {
    		Criticals.mc.thePlayer.lastReportedPosY = 0.0;
            final double ypos = Criticals.mc.thePlayer.posY;
            if (MoveUtils.isOnGround(0.001)) {
                event.setGround(false);
                if (this.stage == 0) {
                    this.y = ypos + 1.0E-8;
                    event.setGround(true);
                }
                else if (this.stage == 1) {
                    this.y -= 5.0E-15;
                }
                else {
                    this.y -= 4.0E-15;
                }
                if (this.y <= Criticals.mc.thePlayer.posY) {
                    this.stage = 0;
                    this.y = Criticals.mc.thePlayer.posY;
                    event.setGround(true);
                }
                event.setY(this.y);
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
    }
    
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
    	if (this.aerox.getValue()) {
       		if (event.getPacket() instanceof C13PacketPlayerAbilities) {
                final C13PacketPlayerAbilities packet = (C13PacketPlayerAbilities)event.getPacket();
                packet.allowFlying = true;
                packet.creativeMode = true;
                packet.flying = true;
                packet.setAllowFlying(true);
                packet.setCreativeMode(true);
                packet.setFlying(true);
    		}
    	}
        if (event.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                if (this.packet.getValue()) {
                	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-5, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                }
                else if (this.motion.getValue()) {
                	if (mc.thePlayer.onGround) {
                	final double y = 0.0625;
                    mc.thePlayer.fallDistance = (float) y;
                	mc.thePlayer.motionY = y;
                	}
                }
                else if (this.jump.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.motionY = 0.42;
                	}
                }
                else if (this.fakejumppacket.getValue()) {
                  	if (mc.thePlayer.onGround) {
                  		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
                  		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                  	}
                }
                else if (this.nogroundpacket.getValue()) {
                	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                }
                else if (this.minijump.getValue()) {
                	if (mc.thePlayer.onGround) {
                		final double y = 1.0E-12;
                        mc.thePlayer.fallDistance = (float) y;
                        mc.thePlayer.motionY = y;
                	}
                }              
                else if (this.minijumppacket.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 6.05E-9, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.mineplex.getValue()) {
                	if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0;
                	}
                }
                else if (this.flag.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 999999999, mc.thePlayer.posZ, false));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.down.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.jump();
                        } else {
                    	mc.thePlayer.motionY -= 1;
                	}
                }
                else if (this.ncp.getValue()) {
                	if (mc.thePlayer.onGround && !(new LongJump().getInstance().isEnabled()) && !(new Speed().getInstance().isEnabled())) {
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY + 0.05000000074505806, ClientUtils.player().posZ, false));
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, false));
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY + 0.012511000037193298, ClientUtils.player().posZ, false));
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, false));
                	}
                }
                else if (this.latestncp.getValue()) {
                	if (mc.thePlayer.onGround && !(new LongJump().getInstance().isEnabled()) && !(new Speed().getInstance().isEnabled())) {
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.11, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1100013579, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.3579E-6, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.spartan.getValue()) {
                	if (mc.thePlayer.onGround) {
                  		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
                  		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.position.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
                	}
                }
                else if (this.teleport.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
                	}
                }
                else if (this.zero.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0, 0 + 0.0625, 0, true));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0, 0 + 1.1E-5, 0, false));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.spam.getValue()) {
                	for (int i = 0; i < 10; ++i) {
                	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-5, mc.thePlayer.posZ, false));
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
                else if (this.other.getValue()) {
                	if (mc.thePlayer.onGround) {
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-5, mc.thePlayer.posZ, false));
                		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                	}
                }
            }
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
