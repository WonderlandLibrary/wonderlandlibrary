package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.SpiderMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils2;

@Module.Mod(displayName = "Spider")
public class Spider extends Module
{
    @Op(name = "Motion", min = 0, max = 10.0, increment = 0.01)
    private double motion;
    @Op(name = "Bobbing")
    private boolean bobbing;
	private SpiderMode vanilla;
	private SpiderMode aac;
	private SpiderMode cubecraft;
	private SpiderMode mineplex;
	private SpiderMode packet;
	private SpiderMode flag;
	private SpiderMode position;
	private SpiderMode teleport;
	private SpiderMode aerox;
    private int state;  
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Spider() {
        this.vanilla = new SpiderMode("Vanilla", true, this);
        this.aac = new SpiderMode("AAC", false, this);
        this.cubecraft = new SpiderMode("CubeCraft", false, this);
        this.mineplex = new SpiderMode("Mineplex", false, this);
        this.packet = new SpiderMode("Packet", false, this);
        this.flag = new SpiderMode("Flag", false, this);
        this.position = new SpiderMode("Position", false, this);
        this.teleport = new SpiderMode("Teleport", false, this);
        this.aerox = new SpiderMode("Aerox", false, this);
        this.motion = 0.42;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.flag);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.aerox);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.packet.getValue()) {
        	this.setSuffix("Packet");
        }
        else if (this.flag.getValue()) {
        	this.setSuffix("Flag");
        }
        else if (this.position.getValue()) {
        	this.setSuffix("Position");
        }
        else if (this.teleport.getValue()) {
        	this.setSuffix("Teleport");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
    }
    
    public void enable() {
    	this.state = 0;
    	super.enable();
    }
    
    public void disable() {
    	Timer.timerSpeed = 1.0f;
    	this.state = 0;
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
    private void onPacket(final PacketSendEvent event) {
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
        if (this.aac.getValue() && event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            if (ClientUtils.mc().thePlayer.isCollidedHorizontally && !ClientUtils.mc().thePlayer.isOnLadder()) {
                final double speed = 1.0E-9;
                final float f = MoveUtils2.getDirection();
                final double x = -Math.sin(f) * speed;
                final double z = Math.cos(f) * speed;
                final C03PacketPlayer c03PacketPlayer = packet;
                c03PacketPlayer.x += x;
                final C03PacketPlayer c03PacketPlayer2 = packet;
                c03PacketPlayer2.z += z;
                this.state = 2;
            }
        }
    }
    
    @EventTarget
    private void onSetBB(final BoundingBoxEvent event) {
        if (this.aac.getValue() && ClientUtils.mc().thePlayer.isCollidedHorizontally && !ClientUtils.mc().thePlayer.isOnLadder() && event.getBlockPos().getY() < ClientUtils.mc().thePlayer.posY) {
            event.setBoundingBox(new AxisAlignedBB(event.getBlockPos(), event.getBlockPos().add(1, 1, 1)));
        }
    }
    
    int getCollidedDir() {
        if (!mc.thePlayer.isCollidedHorizontally) {
            return 0;
        }
        final double width = 0.2;
        final AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX - 0.29, mc.thePlayer.posY + 1.9, mc.thePlayer.posZ + 0.29, mc.thePlayer.posX + 0.29, mc.thePlayer.posY, mc.thePlayer.posZ - 0.29);
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3000001, 0.0, 0.0)).isEmpty()) {
            return 1;
        }
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3000001, 0.0, 0.0)).isEmpty()) {
            return 2;
        }
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, 0.3000001)).isEmpty()) {
            return 3;
        }
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0, 0.0, -0.3000001)).isEmpty()) {
            return 4;
        }
        return 0;
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
    	if (this.aerox.getValue() && mc.thePlayer.isCollidedHorizontally && mc.thePlayer.ticksExisted % 3 == 0) {
    		mc.thePlayer.jump();
    	}
    	if (this.teleport.getValue() && mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
    	}
    	if (this.position.getValue() && mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
    	}
    	if (this.flag.getValue() && mc.thePlayer.isCollidedHorizontally) {
    		if (mc.thePlayer.ticksExisted % 2 == 0) {
    			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1337, mc.thePlayer.posZ, true));
    		} else {
    			mc.thePlayer.jump();
    		}
    	}
    	if (this.packet.getValue() && mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    	}
    	if (this.mineplex.getValue()) {
    		if (mc.thePlayer.isCollidedHorizontally) {
		        final int collideDir = this.getCollidedDir();
				switch (collideDir) {
		        case 1: {
		        	mc.thePlayer.jump();
		            if (mc.thePlayer.posX < 0.0) {
		            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((int)mc.thePlayer.posX - 0.3, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		                break;
		            }
		            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((int)(mc.thePlayer.posX + 1.0) - 0.3, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		            break;
		        }
		        case 3: {
		        	mc.thePlayer.jump();
		            if (mc.thePlayer.posZ < 0.0) {
		                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, (int)mc.thePlayer.posZ - 0.3, true));
		                break;
		            }
		            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, (int)(mc.thePlayer.posZ + 1.0) - 0.3, true));
		            break;
		        }
				}
    		}
    	}
    	if (this.cubecraft.getValue()) {
    		if (mc.thePlayer.isCollidedHorizontally) {
    			Timer.timerSpeed = 0.2f;
    			mc.thePlayer.motionY = 0.42;
    		} else {
    			Timer.timerSpeed = 1.0f;
    		}
    	}
    	if (this.vanilla.getValue() && mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.motionY = motion;
    	}
    	else if (this.aac.getValue()) {
    		if (ClientUtils.mc().thePlayer.isCollidedHorizontally && !ClientUtils.mc().thePlayer.isOnLadder()) {
                if (ClientUtils.mc().thePlayer.motionY < 0.0) {
                    this.state = 1;
                }
                if (ClientUtils.mc().thePlayer.onGround) {
                    ClientUtils.mc().thePlayer.motionY = 0.42;
                }
                else if (ClientUtils.mc().thePlayer.motionY < 0.0 && this.state == 2) {
                    ClientUtils.mc().thePlayer.motionY = -0.1;
                    ClientUtils.mc().thePlayer.motionY = 0.24;
                    this.state = 0;
                }
    		}
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
