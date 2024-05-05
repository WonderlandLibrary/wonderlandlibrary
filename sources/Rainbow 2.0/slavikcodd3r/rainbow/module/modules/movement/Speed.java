package slavikcodd3r.rainbow.module.modules.movement;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.module.modes.SpeedMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MathUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.MoveUtils2;
import slavikcodd3r.rainbow.utils.MovementUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Module.Mod(displayName = "Speed")
public class Speed extends Module
{
	private SpeedMode vanilla;
    private SpeedMode guardian;
    private SpeedMode mineplex;
    private SpeedMode mineplex2;
    private SpeedMode mineplex3;
    private SpeedMode mineplexground;
    private SpeedMode mineplexground2;
    private SpeedMode spartan;
    private SpeedMode oldaac;
    private SpeedMode aac;
    private SpeedMode aacground;
    private SpeedMode aerox;
    private SpeedMode aeroxground;
    private SpeedMode acr;
    private SpeedMode acr2;
    private SpeedMode bhop;
    private SpeedMode matrix;
    private SpeedMode potion;
    private SpeedMode kohi;
    private SpeedMode oldncp;
    private SpeedMode oldncpground;
    private SpeedMode cubecraft;
    private SpeedMode cubecraftground;
    private SpeedMode yport;
    private SpeedMode timerbypass;
    private SpeedMode other;
    @Op(name = "VanillaSpeed", min = 0.0, max = 10.0, increment = 0.01)   
    private double vanillaspeed;
    @Op(name = "VanillaY", min = 0.0, max = 10.0, increment = 0.01)
    private double vanillay;
    @Op(name = "MineplexSpeed", min = 0.2873, max = 2.0, increment = 0.01)
    private double mineplexspeed;
    public static Minecraft mc = Minecraft.getMinecraft();
    private double moveSpeed;
    private double matrixSpeed;
    private int stage;
    private double lastDist;
    @Op(name = "Bobbing")
    private boolean bobbing;
    private double speed;
    private double dist;
    public boolean doSlow;
    private boolean extraSpeed;
    private boolean setZero;
    private int speedTicks;
    private int yPortTicks;
    
    public Speed() {
        this.vanilla = new SpeedMode("Vanilla", true, this);
        this.guardian = new SpeedMode("Guardian", false, this);
        this.mineplex = new SpeedMode("Mineplex", false, this);
        this.mineplex2 = new SpeedMode("Mineplex2", false, this);
        this.mineplex3 = new SpeedMode("Mineplex3", false, this);
        this.mineplexground = new SpeedMode("MineplexGround", false, this);
        this.mineplexground2 = new SpeedMode("MineplexGround2", false, this);
        this.spartan = new SpeedMode("Spartan", false, this);
        this.oldaac = new SpeedMode("OldAAC", false, this);
        this.aac = new SpeedMode("AAC", false, this);
        this.aacground = new SpeedMode("AACGround", false, this);
        this.aerox = new SpeedMode("Aerox", false, this);
        this.aeroxground = new SpeedMode("AeroxGround", false, this);
        this.acr = new SpeedMode("ACR", false, this);
        this.acr2 = new SpeedMode("ACR2", false, this);
        this.bhop = new SpeedMode("BHop", false, this);
        this.matrix = new SpeedMode("Matrix", false, this);
        this.potion = new SpeedMode("Potion", false, this);
        this.kohi = new SpeedMode("Kohi", false, this);
        this.oldncp = new SpeedMode("OldNCP", false, this);
        this.oldncpground = new SpeedMode("OldNCPGround", false, this);
        this.cubecraft = new SpeedMode("CubeCraft", false, this);
        this.cubecraftground = new SpeedMode("CubeCraftGround", false, this);
        this.yport = new SpeedMode("YPort", false, this);
        this.timerbypass = new SpeedMode("TimerBypass", false, this);
        this.other = new SpeedMode("Other", false, this);
        this.vanillaspeed = 1.0;
        this.vanillay = 0.5;
        this.mineplexspeed = 2.0;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.guardian);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.mineplex2);
        OptionManager.getOptionList().add(this.mineplex3);
        OptionManager.getOptionList().add(this.mineplexground);
        OptionManager.getOptionList().add(this.mineplexground2);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.aacground);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.aeroxground);
        OptionManager.getOptionList().add(this.acr);
        OptionManager.getOptionList().add(this.acr2);
        OptionManager.getOptionList().add(this.bhop);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.potion);
        OptionManager.getOptionList().add(this.kohi);
        OptionManager.getOptionList().add(this.oldncp);
        OptionManager.getOptionList().add(this.oldncpground);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.cubecraftground);
        OptionManager.getOptionList().add(this.yport);
        OptionManager.getOptionList().add(this.timerbypass);
        OptionManager.getOptionList().add(this.other);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.guardian.getValue()) {
            this.setSuffix("Guardian");
        }
        else if (this.mineplex.getValue()) {
            this.setSuffix("Mineplex");
        }
        else if (this.mineplex2.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Mineplex2")).toString());
        }
        else if (this.mineplex3.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Mineplex3")).toString());
        }
        else if (this.mineplexground.getValue()) {
            this.setSuffix("MineplexGround");
        }
        else if (this.mineplexground2.getValue()) {
            this.setSuffix("MineplexGround2");
        }
        else if (this.spartan.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Spartan")).toString());
        }
        else if (this.oldaac.getValue()) {
            this.setSuffix("OldAAC");
        }
        else if (this.aac.getValue()) {
            this.setSuffix("AAC");
        }
        else if (this.aacground.getValue()) {
            this.setSuffix("AACGround");
        }
        else if (this.aerox.getValue()) {
            this.setSuffix("Aerox");
        }
        else if (this.aeroxground.getValue()) {
            this.setSuffix("AeroxGround");
        }
        else if (this.acr.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("ACR")).toString());
        }
        else if (this.acr2.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("ACR2")).toString());
        }
        else if (this.bhop.getValue()) {
            this.setSuffix("BHop");
        }
        else if (this.matrix.getValue()) {
            this.setSuffix("Matrix");
        }
        else if (this.potion.getValue()) {
            this.setSuffix("Potion");
        }
        else if (this.kohi.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Kohi")).toString());
        }
        else if (this.oldncp.getValue()) {
            this.setSuffix("OldNCP");
        }
        else if (this.oldncpground.getValue()) {
            this.setSuffix("OldNCPGround");
        }
        else if (this.cubecraft.getValue()) {
            this.setSuffix("CubeCraft");
        }
        else if (this.cubecraftground.getValue()) {
            this.setSuffix("CubeCraftGround");
        }
        else if (this.yport.getValue()) {
            this.setSuffix("YPort");
        }
        else if (this.timerbypass.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("TimerBypass")).toString());
        }
        else if (this.other.getValue()) {
            this.setSuffix("Other");
        }
    }
    
    public void enable() {
    	if (ClientUtils.player() != null) {
    		this.moveSpeed = Speed.getBaseMoveSpeed();
    	}
        this.lastDist = 0.0;
        this.stage = 2;
    	this.matrixSpeed = 4.0;
    	this.speed = -0.4;
        if (this.mineplex.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed Mineplex");
        	}
        }
        else if (this.mineplexground.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed MineplexGround");
        	}
        }
        else if (this.aac.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed AAC");
        	}
        }
        else if (this.oldncp.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed OldNCP");
        	}
        }
        else if (this.oldncpground.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed OldNCPGround");
        	}
        }
        else if (this.cubecraftground.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed CubeCraftGround");
        	}
        }
        else if (this.mineplexground2.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Speed MineplexGround2");
        	} else {
        	mc.thePlayer.jump();
        	}
        }
    	super.enable();
    }
    
    public void disable() {
		Timer.timerSpeed = 1.0f;
		mc.gameSettings.keyBindJump.pressed = false;
		mc.thePlayer.speedInAir = 0.02f;
        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        if (this.mineplexground2.getValue()) {
        	MoveUtils.setMotion(Speed.getBaseMoveSpeed());
        }
        else if (this.aerox.getValue()) {
        	MoveUtils.setMotion(0);
        }
        else if (this.aeroxground.getValue()) {
        	MoveUtils.setMotion(0);
        }
        else if (this.potion.getValue()) {
            ClientUtils.player().removePotionEffect(Potion.moveSpeed.id);
        }
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
        if (mc.thePlayer.isMoving()) {
        if (this.yport.getValue()) {
            if (ClientUtils.mc().thePlayer.onGround) {
                ClientUtils.mc().thePlayer.jump();
            }
        }
        if (this.bhop.getValue()) {
            if (ClientUtils.mc().thePlayer.onGround) {
                ClientUtils.mc().thePlayer.jump();
            }
            else {
                final double direction = MoveUtils2.getDirection();
                final double Motion = Math.sqrt(ClientUtils.mc().thePlayer.motionX * ClientUtils.mc().thePlayer.motionX + ClientUtils.mc().thePlayer.motionZ * ClientUtils.mc().thePlayer.motionZ);
                ClientUtils.mc().thePlayer.motionX = -Math.sin(direction) * 1.0 * Motion;
                ClientUtils.mc().thePlayer.motionZ = Math.cos(direction) * 1.0 * Motion;
            }
        }
        if (this.guardian.getValue()) {
            if (ClientUtils.mc().thePlayer.onGround) {
                ClientUtils.mc().thePlayer.jump();
            }
            else {
                final double direction = MoveUtils2.getDirection();
                final double Motion = Math.sqrt(ClientUtils.mc().thePlayer.motionX * ClientUtils.mc().thePlayer.motionX + ClientUtils.mc().thePlayer.motionZ * ClientUtils.mc().thePlayer.motionZ);
                ClientUtils.mc().thePlayer.motionX = -Math.sin(direction) * 1.0 * Motion;
                ClientUtils.mc().thePlayer.motionZ = Math.cos(direction) * 1.0 * Motion;
            }
        }
        }
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
    	else if (this.aeroxground.getValue()) {
            if (event.getPacket() instanceof S32PacketConfirmTransaction) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof S00PacketKeepAlive) {
                event.setCancelled(true);
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
    	else if (this.aeroxground.getValue()) {
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
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
		if (mc.thePlayer.isMoving()) {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
		if (this.vanilla.getValue()) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = vanillay;
			}
		}
		else if (this.guardian.getValue()) {
			if (mc.thePlayer.onGround) {
				Timer.timerSpeed = 10f;
			} else {
				Timer.timerSpeed = 1.0f;
			}
		}
		else if (this.mineplex.getValue()) {
			MovementUtils.strafe();
		}
		else if (this.cubecraft.getValue()) {
			MovementUtils.strafe();
		}
 		else if (this.mineplexground.getValue()) {
            NetUtil.sendPacket(new C09PacketHeldItemChange(Speed.airSlot()));
            final C08PacketPlayerBlockPlacement place = new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).add(0, -1, 0), EnumFacing.UP.getIndex(), null, 0.5f, 1.0f, 0.5f);
            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
	        mc.thePlayer.sendQueue.addToSendQueue(place);
	        mc.thePlayer.onGround = false;
	        if (mc.thePlayer.ticksExisted % 2.0 == 0.0) {
	            mc.thePlayer.jumpMovementFactor = 0.1f;			
	        }
		}
		else if (this.spartan.getValue()) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			}
		}
		else if (this.oldaac.getValue()) {
			MovementUtils.strafe();
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				mc.thePlayer.jump();
			}
		}
		else if (this.aac.getValue()) {
            mc.timer.timerSpeed = 0.6F;
			if (mc.thePlayer.onGround) {
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                event.setGround(false);
                  mc.thePlayer.jump();
                  mc.thePlayer.motionY = 0.5;
                  mc.thePlayer.speedInAir = 0.02F;
                  mc.thePlayer.jumpMovementFactor = 0.02F;          
                  } else {
                	  if (mc.thePlayer.isMoving()) {
                      mc.thePlayer.jumpMovementFactor = 0.48F;
                   this.move(mc.thePlayer.rotationYaw, (float) 0.2);
                	  }
		}
		}
		else if (this.aerox.getValue()) {
			event.setGround(false);
		}
		else if (this.aeroxground.getValue()) {
			event.setGround(false);
		}
		else if (this.acr.getValue()) {
			event.setGround(true);
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			}
		}
		else if (this.acr2.getValue()) {
			event.setGround(true);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true));
		}
		else if (this.mineplexground2.getValue()) {
			mc.thePlayer.onGround = false;
			mc.thePlayer.motionY = -10;
		}
		else if (this.potion.getValue()) {
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 0, 10));
		}
		else if (this.kohi.getValue()) {
			if (mc.thePlayer.onGround) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
            double yaw = Math.toRadians((double)mc.thePlayer.rotationYaw);
            double x = -Math.sin(yaw) * vanillaspeed;
            double z = Math.cos(yaw) * vanillaspeed;
            if (mc.gameSettings.keyBindForward.pressed) {
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
            }
            if (mc.gameSettings.keyBindBack.pressed) {
            mc.thePlayer.setPosition(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ - z);
            }
            }
		}
		else if (this.oldncp.getValue()) {
	        if (event.getState() == Event.State.PRE) {
	            final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
	            final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
	            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	        }
		}
		else if (this.oldncpground.getValue()) {
			if (event.getState() == Event.State.PRE) {
	            if (stage == 3.0) {
	                event.setY(event.getY() + 0.4);
	            }
	            final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
	            final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
	            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	            ClientUtils.player().motionY = -0.41;
		 }
		}
		else if (this.timerbypass.getValue()) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
			Timer.timerSpeed = (float) vanillaspeed;
		}
		else if (this.yport.getValue()) {
			mc.thePlayer.motionY = -20;
		}
		else if (this.other.getValue()) {
			mc.gameSettings.keyBindJump.pressed = true;
		}
		}
    }
    
	@EventTarget
    public void onMove(final MoveEvent event) {
		if (mc.thePlayer.isMoving()) {
		if (this.vanilla.getValue()) {
			ClientUtils.setMoveSpeed(event, vanillaspeed);
		}
		else if (this.mineplex.getValue()) {
	        if (mc.thePlayer.onGround) {
	        	ClientUtils.setMoveSpeed(event, 0.2873);
                event.setY(mc.thePlayer.motionY = 0.42);
	        } else {
	        	ClientUtils.setMoveSpeed(event, 0.4999999999);
	        }
		}
		else if (this.mineplex2.getValue()) {
            mc.timer.timerSpeed = 1.0f;
            if (mc.thePlayer.onGround && mc.gameSettings.keyBindForward.pressed) {
                event.setY(mc.thePlayer.motionY = 0.42);
                this.speed = (this.doSlow ? 0.8 : (this.speed + 0.549999));
               mc.timer.timerSpeed = 2.0f;
                ClientUtils.setMoveSpeed(event, 0.0);
                this.doSlow = false;
                return;
            }
            this.speed = Math.min(this.speed, mineplexspeed);
            this.speed = Math.max(this.speed, Speed.getBaseMoveSpeed());
            ClientUtils.setMoveSpeed(event, this.speed -= this.speed / 64.0);
		}
		else if (this.mineplex3.getValue()) {
            mc.timer.timerSpeed = 1.0f;
            if (mc.thePlayer.onGround && mc.gameSettings.keyBindForward.pressed) {
                event.setY(mc.thePlayer.motionY = 0.42);
                this.speed = (this.doSlow ? 0.8 : (this.speed + 0.549999));
               mc.timer.timerSpeed = 2.0f;
                ClientUtils.setMoveSpeed(event, 0.0);
                this.doSlow = false;
                return;
            }
            this.speed = Math.min(this.speed, mineplexspeed);
            this.speed = Math.max(this.speed, Speed.getBaseMoveSpeed());
            ClientUtils.setMoveSpeed(event, this.speed -= this.speed / 64.0);
	        if (mc.thePlayer.fallDistance == 0.0f) {
                event.setY(mc.thePlayer.motionY += 0.038);
            }
            else if (mc.thePlayer.fallDistance <= 1.4) {
                event.setY(mc.thePlayer.motionY += 0.032);
            }
		}
		else if (this.mineplexground2.getValue()) {
			Timer.timerSpeed = 1.0f;
	        if (mc.thePlayer.onGround) {
	        	event.setX(0);
	        	event.setZ(0);
	            Timer.timerSpeed *= 4.0;
	            this.moveSpeed = 0.8;
	            ++this.stage;
	            return;
	        }
	        this.moveSpeed -= 0.01;
	        if (this.stage > -17273) {
	            ClientUtils.setMoveSpeed(event, this.moveSpeed);
	        }
	        if (this.moveSpeed <= 0.6) {
	        	mc.thePlayer.onGround = true;
	        	event.setX(0);
	        	event.setZ(0);
	        	event.setY(mc.thePlayer.motionY = 0.42);
	            Timer.timerSpeed *= 4.0;
	        	this.moveSpeed = 0.8;
	        }
		}
		else if (this.spartan.getValue()) {
			ClientUtils.setMoveSpeed(event, 1.0);
		}
		else if (this.aerox.getValue()) {
			if (mc.thePlayer.onGround) {
				ClientUtils.setMoveSpeed(event, 0);
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				event.setY(0.42);
			}
		}
		else if (this.aeroxground.getValue()) {
			if (mc.thePlayer.onGround) {
            if (mc.thePlayer.ticksExisted % 3 == 0) {
				ClientUtils.setMoveSpeed(event, 0);
				event.setY(-10);
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
				mc.thePlayer.jump();
            }
		}
		}
		else if (this.aacground.getValue()) {
			event.setY(0);
			mc.thePlayer.jump();
		}
		else if (this.acr.getValue()) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
    	        ClientUtils.setMoveSpeed(event, vanillaspeed);
            } else {
    	        ClientUtils.setMoveSpeed(event, 0.06);
            }
		}
		else if (this.acr2.getValue()) {
	        ClientUtils.setMoveSpeed(event, vanillaspeed);
		}
		else if (this.matrix.getValue()) {
			Timer.timerSpeed = 0.3f;
	        if (mc.thePlayer.ticksExisted % 2.0 == 0.0) {
	        	Timer.timerSpeed = 10.0f;
	        }
		}
		else if (this.oldncpground.getValue()) {
			if (mc.thePlayer.onGround || stage == 3.0) {
                if (MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.138, 3)) {
                    event.setY(-0.0931);
                }
                if ((!ClientUtils.player().isCollidedHorizontally && ClientUtils.player().moveForward != 0.0f) || ClientUtils.player().moveStrafing != 0.0f) {
                    if (stage == 2.0) {
                        this.moveSpeed *= 2.149;
                        stage = 3;
                    }
                    else if (stage == 3.0) {
                        stage = 2;
                        final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    }
                    else if (ClientUtils.world().getCollidingBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0)).size() > 0 || ClientUtils.player().isCollidedVertically) {
                        stage = 1;
                    }
                }
                else {
                    ClientUtils.mc().timer.timerSpeed = 1.0f;
                }
                ClientUtils.setMoveSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed()));
    		}
		}
		else if (this.oldncp.getValue()) {
	        if (MathUtils.roundToPlace(ClientUtils.player().posY - (int)ClientUtils.player().posY, 3) == MathUtils.roundToPlace(0.138, 3)) {
                final EntityPlayerSP player3;
                final EntityPlayerSP player = player3 = ClientUtils.player();
                player3.motionY -= 0.08;
                event.setY(event.getY() - 0.0931);
                final EntityPlayerSP player4;
                final EntityPlayerSP player2 = player4 = ClientUtils.player();
                player4.posY -= 0.0931;
            }
            if (stage == 2.0 && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                event.setY(ClientUtils.player().motionY = 0.4);
                this.moveSpeed *= 2.149;
            }
            else if (stage == 3.0) {
                final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            }
            else {
                final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                if (collidingList.size() > 0 || ClientUtils.player().isCollidedVertically) {
                    stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            ClientUtils.setMoveSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed()));
            ++stage;
		}
		else if (this.cubecraftground.getValue()) {
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.ticksExisted % 4 == 0) {
					ClientUtils.setMoveSpeed(event, 2.4);
				}
			}
		}
		else if (this.cubecraft.getValue()) {
			if (mc.thePlayer.onGround) {
				event.setY(mc.thePlayer.motionY = 0.42);
	        	ClientUtils.setMoveSpeed(event, 2.4);
	        } else {
				ClientUtils.setMoveSpeed(event, 0.2873);
	        }
		}
		}
	}
	
    public void move(float yaw, float multiplyer) {
        double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
        this.mc.thePlayer.motionX = moveX;
        this.mc.thePlayer.motionZ = moveZ;
     }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (ClientUtils.player().isPotionActive(Potion.moveSpeed)) {
            final int amplifier = ClientUtils.player().getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (Speed.mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        ClientUtils.sendMessage("Select a empty slot");
        return -10;
    }
}