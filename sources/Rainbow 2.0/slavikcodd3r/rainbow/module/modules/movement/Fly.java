package slavikcodd3r.rainbow.module.modules.movement;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.BoundingBoxEntityEvent;
import slavikcodd3r.rainbow.event.events.BoundingBoxEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.module.modes.FlyMode;
import slavikcodd3r.rainbow.module.modules.combat.AutoPotion;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.BlockUtils;
import slavikcodd3r.rainbow.utils.ChatUtils;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.FlyUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.NetUtil;
import slavikcodd3r.rainbow.utils.SpeedUtils;
import slavikcodd3r.rainbow.utils.TimeHelper;
import slavikcodd3r.rainbow.utils.TimerDelay;
import slavikcodd3r.rainbow.utils.Vec3d;

@Module.Mod(displayName = "Fly")
public class Fly extends Module
{
	private FlyMode vanilla;
	private FlyMode creative;
	private FlyMode jetpack;
	private FlyMode oldncp;
	private FlyMode oldncp2;
	private FlyMode ncp;
	private FlyMode ncppacket;
	private FlyMode ncpdown;
	private FlyMode faithfulmc;
	private FlyMode spartan;
	private FlyMode mineplex;
	private FlyMode guardian;
	private FlyMode hypixel;
	private FlyMode cubecraft;
	private FlyMode aerox;
	private FlyMode acr;
	private FlyMode packet;
	private FlyMode matrix;
	private FlyMode voidmod;
	private FlyMode mccentral;
	private FlyMode verus;
	private FlyMode oldaac;
	private FlyMode fakejump;
	private FlyMode fakefall;
    public TimerDelay time;
    @Op(name = "VanillaSpeed", min = 0.0, max = 10.0, increment = 0.01)
    private double vanillaspeed;
    @Op(name = "VanillaGlide", min = 0.0, max = 1.0, increment = 0.01)
    private double vanillaglide;
    @Op(name = "Bobbing")
    private boolean bobbing;
    @Op(name = "AntiKick")
    private boolean antikick;
    public static Minecraft mc = Minecraft.getMinecraft();
	private float air;
    private float groundTicks;
    double motionY;
    int count;
    boolean collided;
    boolean half;
    private double centralSpeed;
    public TimeHelper timer;
    int counter = 0;
    boolean shouldSpeed;
    double speed;
    private boolean back;
    private double mineplexSpeed;
    private boolean mineplexRape;
    private boolean watchdoge = true;
    public Consumer<MoveEvent> onMove;
    private int groundTicks2;
    private int airTicks;
    private int headStart;
    private boolean done;
    private double x;
    private double y;
    private double z;
    private double speed2;
    private double dist;
    public boolean doSlow;
    private boolean extraSpeed;
    private boolean setZero;
    private int speedTicks;
    private int yPortTicks;
    private boolean sentineldisabled;
    private static final double SPEED_BASE = 0.2873;
    private double moveSpeed;
    private double lastDist;
    public static int stage;
    public static int settingUpTicks;
    
    public Fly() {
        this.vanilla = new FlyMode("Vanilla", true, this);
        this.creative = new FlyMode("Creative", false, this);
        this.jetpack = new FlyMode("Jetpack", false, this);
        this.oldncp = new FlyMode("OldNCP", false, this);
        this.oldncp2 = new FlyMode("OldNCP2", false, this);
        this.ncp = new FlyMode("NCP", false, this);
        this.ncppacket = new FlyMode("NCPPacket", false, this);
        this.ncpdown = new FlyMode("NCPDown", false, this);
        this.faithfulmc = new FlyMode("FaithfulMC", false, this);
        this.spartan = new FlyMode("Spartan", false, this);
        this.mineplex = new FlyMode("Mineplex", false, this);
        this.guardian = new FlyMode("Guardian", false, this);
        this.hypixel = new FlyMode("Hypixel", false, this);
        this.cubecraft = new FlyMode("CubeCraft", false, this);
        this.aerox = new FlyMode("Aerox", false, this);
        this.acr = new FlyMode("ACR", false, this);
        this.packet = new FlyMode("Packet", false, this);
        this.matrix = new FlyMode("Matrix", false, this);
        this.voidmod = new FlyMode("Void", false, this);
        this.mccentral = new FlyMode("MC-Central", false, this);
        this.verus = new FlyMode("Verus", false, this);
        this.oldaac = new FlyMode("OldAAC", false, this);
        this.fakejump = new FlyMode("FakeJump", false, this);
        this.fakefall = new FlyMode("FakeFall", false, this);
        this.vanillaspeed = 1.0;
        this.vanillaglide = 0;
        this.timer = new TimeHelper();
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.creative);
        OptionManager.getOptionList().add(this.jetpack);
        OptionManager.getOptionList().add(this.oldncp);
        OptionManager.getOptionList().add(this.oldncp2);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.ncppacket);
        OptionManager.getOptionList().add(this.ncpdown);
        OptionManager.getOptionList().add(this.faithfulmc);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.guardian);
        OptionManager.getOptionList().add(this.hypixel);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.acr);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.voidmod);
        OptionManager.getOptionList().add(this.mccentral);
        OptionManager.getOptionList().add(this.verus);
        OptionManager.getOptionList().add(this.oldaac);
        OptionManager.getOptionList().add(this.fakejump);
        OptionManager.getOptionList().add(this.fakefall);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.creative.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Creative")).toString());
        }
        else if (this.jetpack.getValue()) {
            this.setSuffix("Jetpack");
        }
        else if (this.oldncp.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("OldNCP")).toString());
        }
        else if (this.oldncp2.getValue()) {
        	this.setSuffix("OldNCP2");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.ncppacket.getValue()) {
        	this.setSuffix("NCPPacket");
        }
        else if (this.ncpdown.getValue()) {
        	this.setSuffix("NCPDown");
        }
        else if (this.faithfulmc.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("FaithfulMC")).toString());
        }
        else if (this.spartan.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Spartan")).toString());
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.guardian.getValue()) {
        	this.setSuffix("Guardian");
        }
        else if (this.hypixel.getValue()) {
        	this.setSuffix("Hypixel");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
        else if (this.acr.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("ACR")).toString());
        }
        else if (this.packet.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Packet")).toString());
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
        else if (this.voidmod.getValue()) {
        	this.setSuffix("Void");
        }
        else if (this.mccentral.getValue()) {
        	this.setSuffix("MC-Central");
        }
        else if (this.verus.getValue()) {
        	this.setSuffix("Verus");
        }
        else if (this.oldaac.getValue()) {
        	this.setSuffix("OldAAC");
        }
        else if (this.fakejump.getValue()) {
        	this.setSuffix("FakeJump");
        }
        else if (this.fakefall.getValue()) {
        	this.setSuffix("FakeFall");
        }
    }
    
    public void enable() {
    	this.settingUpTicks = 5;
    	this.counter = 0;
    	this.count = 0;
    	this.stage = 0;
    	this.sentineldisabled = false;
        this.watchdoge = true;
    	this.mineplexRape = false;
    	this.done = false;
    	this.speed = 0.2873;
        this.centralSpeed = 1.2;
        this.mineplexSpeed = 0.2873;
        this.moveSpeed = 0;
        if (this.guardian.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Fly Guardian");
        	} else {
        		mc.thePlayer.jump();
        	}
        }
        else if (this.spartan.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Fly Spartan");
        	}
        }
        else if (this.ncpdown.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Fly NCPDown");
        	}
        }
        else if (this.mineplex.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Fly Mineplex");
        	}
        }
        else if (this.aerox.getValue()) {
        	if (!mc.thePlayer.onGround) {
        		ClientUtils.sendMessage("You need to be on ground to use Fly Aerox");
        	}
        }
        else if (this.oldncp2.getValue()) {
        	if (mc.thePlayer.onGround) {
        		for (int i = 0; i < 80.0 + 40.0 * (0.5 - 0.5); ++i) {
                    ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY + 0.049, ClientUtils.player().posZ, false));
                    ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, false));
                }
                ClientUtils.player().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ, true));
        	}
        }
        else if (this.matrix.getValue()) {
        	if (mc.thePlayer.onGround) {
        		mc.thePlayer.jump();
        	}
        }
        else if (this.cubecraft.getValue()) {
        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ, false));
        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }
        this.motionY = mc.thePlayer.motionY;
        this.air = 0.0f;
        this.count = 0;
        this.half = (mc.thePlayer.posY != (int)mc.thePlayer.posY);
        this.collided = mc.thePlayer.isCollidedHorizontally;
        this.groundTicks = 0.0f;
    	super.enable();
    }
    
    public void disable() {
    	this.settingUpTicks = 5;
    	this.counter = 0;
    	this.count = 0;
    	this.stage = 0;
    	this.sentineldisabled = false;
        this.watchdoge = true;
    	this.mineplexRape = false;
    	this.done = false;     
        this.moveSpeed = 0;
    	mc.thePlayer.stepHeight = 0.5f;
        NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    	Timer.timerSpeed = 1.0f;
		mc.thePlayer.capabilities.isFlying = false;
        KeyBinding.setKeyBindState(ClientUtils.mc().gameSettings.keyBindJump.getKeyCode(), false);
    	mc.thePlayer.speedInAir = 0.02f;
		if (this.ncp.getValue()) {
			MoveUtils.setMotion(0);
		}
		else if (this.oldncp.getValue()) {
			ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
        else if (this.aerox.getValue()) {
        	MoveUtils.setMotion(0);
        }
        else if (this.mccentral.getValue()) {
        	MoveUtils.setMotion(0);
        }
    	super.disable();
    }
    
    protected EntityPlayerSP player() {
        return ClientUtils.mc().thePlayer;
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
		if (this.ncp.getValue()) {
	            if (Keyboard.isKeyDown(56)) {
	                ClientUtils.updatePosition(0.0, 2.147483647E9, 0.0);
	            }
	            final float direction = this.player().rotationYaw + ((this.player().moveForward < 0.0f) ? 180 : 0) + ((this.player().moveStrafing > 0.0f) ? (-90.0f * ((this.player().moveForward < 0.0f) ? -0.5f : ((this.player().moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f) - ((this.player().moveStrafing < 0.0f) ? (-90.0f * ((this.player().moveForward < 0.0f) ? -0.5f : ((this.player().moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
	            final float xDir = (float)Math.cos((direction + 90.0f) * 3.141592653589793 / 180.0);
	            final float zDir = (float)Math.sin((direction + 90.0f) * 3.141592653589793 / 180.0);
	            if (!this.player().isCollidedVertically) {
	                ++this.airTicks;
	                if (ClientUtils.mc().gameSettings.keyBindSneak.isKeyDown()) {
	                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(0.0, 2.147483647E9, 0.0, false));
	                }
	                this.groundTicks = 0;
	                if (!this.player().isCollidedVertically) {
	                    if (this.player().motionY == -0.07190068807140403) {
	                        final EntityPlayerSP player27;
	                        final EntityPlayerSP player25 = player27 = this.player();
	                        player27.motionY *= 0.3499999940395355;
	                    }
	                    if (this.player().motionY == -0.10306193759436909) {
	                        final EntityPlayerSP player28;
	                        final EntityPlayerSP player25 = player28 = this.player();
	                        player28.motionY *= 0.550000011920929;
	                    }
	                    if (this.player().motionY == -0.13395038817442878) {
	                        final EntityPlayerSP player29;
	                        final EntityPlayerSP player25 = player29 = this.player();
	                        player29.motionY *= 0.6700000166893005;
	                    }
	                    if (this.player().motionY == -0.16635183030382) {
	                        final EntityPlayerSP player30;
	                        final EntityPlayerSP player25 = player30 = this.player();
	                        player30.motionY *= 0.6899999976158142;
	                    }
	                    if (this.player().motionY == -0.19088711097794803) {
	                        final EntityPlayerSP player31;
	                        final EntityPlayerSP player25 = player31 = this.player();
	                        player31.motionY *= 0.7099999785423279;
	                    }
	                    if (this.player().motionY == -0.21121925191528862) {
	                        final EntityPlayerSP player32;
	                        final EntityPlayerSP player25 = player32 = this.player();
	                        player32.motionY *= 0.20000000298023224;
	                    }
	                    if (this.player().motionY == -0.11979897632390576) {
	                        final EntityPlayerSP player33;
	                        final EntityPlayerSP player25 = player33 = this.player();
	                        player33.motionY *= 0.9300000071525574;
	                    }
	                    if (this.player().motionY == -0.18758479151225355) {
	                        final EntityPlayerSP player34;
	                        final EntityPlayerSP player25 = player34 = this.player();
	                        player34.motionY *= 0.7200000286102295;
	                    }
	                    if (this.player().motionY == -0.21075983825251726) {
	                        final EntityPlayerSP player35;
	                        final EntityPlayerSP player25 = player35 = this.player();
	                        player35.motionY *= 0.7599999904632568;
	                    }
	                    if (this.player().motionY < -0.2 && this.player().motionY > -0.24) {
	                        final EntityPlayerSP player36;
	                        final EntityPlayerSP player25 = player36 = this.player();
	                        player36.motionY *= 0.7;
	                    }
	                    if (this.player().motionY < -0.25 && this.player().motionY > -0.32) {
	                        final EntityPlayerSP player37;
	                        final EntityPlayerSP player25 = player37 = this.player();
	                        player37.motionY *= 0.8;
	                    }
	                    if (this.player().motionY < -0.35 && this.player().motionY > -0.8) {
	                        final EntityPlayerSP player38;
	                        final EntityPlayerSP player25 = player38 = this.player();
	                        player38.motionY *= 0.98;
	                    }
	                    if (this.player().motionY < -0.8 && this.player().motionY > -1.6) {
	                        final EntityPlayerSP player39;
	                        final EntityPlayerSP player25 = player39 = this.player();
	                        player39.motionY *= 0.99;
	                    }
	                }
	                final double[] var8 = { 0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237 };
	                if (ClientUtils.mc().gameSettings.keyBindForward.isKeyDown()) {
	                    try {
	                        this.player().motionX = xDir * var8[this.airTicks - 1] * 3.0;
	                        this.player().motionZ = zDir * var8[this.airTicks - 1] * 3.0;
	                    }
	                    catch (ArrayIndexOutOfBoundsException e) {
	                    	e.printStackTrace();
	                    }
	                }
	                else {
	                    this.player().motionX = 0.0;
	                    this.player().motionZ = 0.0;
	                }
	            }
	            else {
	                final Timer var9 = ClientUtils.mc().timer;
	                Timer.timerSpeed = 1.0f;
	                this.airTicks = 0;
	                ++this.groundTicks;
	                --this.headStart;
	                final EntityPlayerSP player40;
	                final EntityPlayerSP player25 = player40 = this.player();
	                player40.motionX /= 13.0;
	                final EntityPlayerSP player41;
	                final EntityPlayerSP player26 = player41 = this.player();
	                player41.motionZ /= 13.0;
	                if (this.groundTicks == 1) {
	                    ClientUtils.updatePosition(this.player().posX, this.player().posY, this.player().posZ);
	                    ClientUtils.updatePosition(this.player().posX + 0.0624, this.player().posY, this.player().posZ);
	                    ClientUtils.updatePosition(this.player().posX, this.player().posY + 0.419, this.player().posZ);
	                    ClientUtils.updatePosition(this.player().posX + 0.0624, this.player().posY, this.player().posZ);
	                    ClientUtils.updatePosition(this.player().posX, this.player().posY + 0.419, this.player().posZ);
	                }
	                if (this.groundTicks > 2) {
	                    this.groundTicks = 0;
	                    this.player().motionX = xDir * 0.3;
	                    this.player().motionZ = zDir * 0.3;
	                    this.player().motionY = 0.42399999499320984;
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
    }
    
    @EventTarget
    public void onBB(final BoundingBoxEntityEvent event) {
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
    	if (this.antikick) {
	        if (mc.thePlayer.ticksExisted % 7 == 0) {
	        	FlyUtils.fall();
	        	FlyUtils.ascend();
	        }
    	}
		if (this.vanilla.getValue()) {
            if (ClientUtils.movementInput().jump) {
                ClientUtils.player().motionY = this.vanillaspeed;
            }
            else if (ClientUtils.movementInput().sneak) {
                ClientUtils.player().motionY = -this.vanillaspeed;
            }
            else {
                ClientUtils.player().motionY = -this.vanillaglide;
		}
		}
		else if (this.creative.getValue()) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed((float) (vanillaspeed));
		}
		else if (this.jetpack.getValue()) {
	        if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
	            mc.thePlayer.motionY += 0.1;
		}
		}
		else if (this.oldncp.getValue()) {
			ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(ClientUtils.mc().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
	        if (mc.thePlayer.ticksExisted % 3 == 0) {
	        mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.05, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            }
            if (ClientUtils.movementInput().jump) {
                ClientUtils.player().motionY = this.vanillaspeed;
            }
            else if (ClientUtils.movementInput().sneak) {
                ClientUtils.player().motionY = -this.vanillaspeed;
            }
            else {
                ClientUtils.player().motionY = -0.05;
		        }
		}
		else if (this.oldncp2.getValue()) {
			if (event.getState() == Event.State.PRE) {
	            final double xDist = ClientUtils.x() - ClientUtils.player().prevPosX;
	            final double zDist = ClientUtils.z() - ClientUtils.player().prevPosZ;
	            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	        }
		}
		else if (this.ncp.getValue()) {
			Timer.timerSpeed = 0.05f;
		}
		else if (this.ncppacket.getValue()) {
			mc.thePlayer.motionY = 0;
			mc.thePlayer.onGround = true;
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				event.setY(event.getY() + 1.0E-12);
			}
		}
		else if (this.ncpdown.getValue()) {
			mc.thePlayer.onGround = true;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.jumpMovementFactor = 0.0f;
			mc.gameSettings.keyBindJump.pressed = false;
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				mc.thePlayer.motionY = 0.42;
			}
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				mc.thePlayer.motionY -= 150.0;
			}
		}
		else if (this.cubecraft.getValue()) {
			if (mc.thePlayer.hurtTime > 0) {
				this.sentineldisabled = true;
			}
			if (this.sentineldisabled) {
				mc.thePlayer.cameraYaw = 0.11f;
				event.setGround(false);
				if (!ClientUtils.movementInput().jump && !ClientUtils.movementInput().sneak) {
            	event.setY(event.getY() + 1.0E-12);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
            	event.setY(event.getY() - 1.0E-12);
            }
            }
			}
		}
		else if (this.faithfulmc.getValue()) {
			Timer.timerSpeed = 0.3f;
			if (ClientUtils.movementInput().jump) {
                ClientUtils.player().motionY = this.vanillaspeed;
            }
            else if (ClientUtils.movementInput().sneak) {
                ClientUtils.player().motionY = -this.vanillaspeed;
            }
            else {
                ClientUtils.player().motionY = -this.vanillaglide;
		}
		}
		else if (this.spartan.getValue()) {
            if (ClientUtils.movementInput().jump) {
            	ClientUtils.player().setPosition(ClientUtils.player().posX, ClientUtils.player().posY + 1, ClientUtils.player().posZ);
            }
            else if (ClientUtils.movementInput().sneak) {
            	ClientUtils.player().setPosition(ClientUtils.player().posX, ClientUtils.player().posY - 1, ClientUtils.player().posZ);
            }
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			mc.thePlayer.motionY = -1;
	        if (mc.thePlayer.ticksExisted % 2 == 0) {
	        	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
	        }
		}
		else if (this.guardian.getValue()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            ClientUtils.player().motionY = -0.0d;
            }
			else if (this.aerox.getValue()) {
				event.setGround(false);
		        if (mc.thePlayer.ticksExisted % 3 == 0) {	
		        	mc.thePlayer.jump();
		        }
			}
			else if (this.acr.getValue()) {
				if (!mc.thePlayer.isInWater()) {
				if (!ClientUtils.movementInput().sneak) {
				event.setGround(true);
				mc.thePlayer.motionY = -0.0d;
				}
	            if (ClientUtils.movementInput().jump && mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.isMoving() && !ClientUtils.movementInput().sneak) {
	            	mc.thePlayer.motionY = 0.5124;
	            }
				}
			}
			else if (this.packet.getValue()) {
				if (ClientUtils.movementInput().jump) {
					 mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + vanillaspeed, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
		             mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - vanillaspeed, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
				}
				else if (ClientUtils.movementInput().sneak) {
					mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - vanillaspeed, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
		            mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + vanillaspeed, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
				} else {
					double yaw = Math.toRadians((double)mc.thePlayer.rotationYaw);
					double x = -Math.sin(yaw) * vanillaspeed;
					double z = Math.cos(yaw) * vanillaspeed;
					mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
					mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX - x, mc.thePlayer.posY, mc.thePlayer.posZ - z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround ? true : false));
				}
			}
			else if (this.matrix.getValue()) {
				if (mc.thePlayer.fallDistance >= 1.0f) {
					mc.timer.timerSpeed = 0.3f;
				}
				if (mc.thePlayer.fallDistance >= 1.5f) {
					mc.timer.timerSpeed = 10f;
				}
				if (mc.thePlayer.fallDistance >= 2.0f) {
					mc.thePlayer.motionY = -0.02f;
					mc.thePlayer.fallDistance = 0;
					mc.timer.timerSpeed = 1.0f;
					}
				}
			else if (this.verus.getValue()) {
				if (this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock() == Blocks.air) {
	                this.mc.theWorld.setBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ), Blocks.barrier.getDefaultState(), 2);
	    		}
			}
			else if (this.voidmod.getValue()) {
				if (mc.thePlayer.fallDistance > 3.5f && mc.thePlayer.hurtTime > 0) {
					mc.thePlayer.motionY = 9;
					mc.thePlayer.fallDistance = 0;
				}
				if (ClientUtils.movementInput().sneak) {
					Timer.timerSpeed = 0.1f;
				} else {
					Timer.timerSpeed = 1.0f;
				}
			}
			else if (this.mccentral.getValue()) {
				if (mc.thePlayer.ticksExisted % 11 == 0) {
					MoveUtils.setMotion(1.5);
					mc.thePlayer.motionY = 0.42;
				}
			}
			else if (this.hypixel.getValue()) {
				if (ClientUtils.movementInput().jump) {
					mc.thePlayer.motionY = 0.42;
				}
				else if (ClientUtils.movementInput().sneak) {
					mc.thePlayer.motionY = -0.42;
				}
				counter++;
                mc.thePlayer.motionY = 0;
                if (counter >= 3) {
                    this.counter = 0;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.210401E-13, mc.thePlayer.posZ);
                    final double y = RandomUtils.nextDouble(1.4310893E-13, 1.1238901E-4);
                    event.setY(event.getY() + (float) (Math.random() * y));
                }
			}
			else if (this.oldaac.getValue()) {
                if (this.mc.thePlayer.movementInput.jump) {
                    this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ);
                }
                else if (this.mc.thePlayer.movementInput.sneak) {
                    this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.42, this.mc.thePlayer.posZ);
                }
                else {
                    this.mc.thePlayer.motionY = 0.0;
                    if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                        this.mc.thePlayer.motionY = -0.047;
                    }
                    else {
                        this.mc.thePlayer.motionY = 0.04;
                    }
                }
			}
			else if (this.fakejump.getValue()) {
				mc.thePlayer.motionY = 0;
				if (this.counter == 0) {
					event.setY(event.getY() + 0.41999998688697815);
				}
				if (this.counter == 1) {
					event.setY(event.getY() + 0.33319999363422365);
				}
				if (this.counter == 2) {
					event.setY(event.getY() + 0.24813599859094576);
				}
				if (this.counter == 3) {
					event.setY(event.getY() + 0.16477328182606651);
				}
				if (this.counter == 4) {
					event.setY(event.getY() + 0.08307781780646721);
				}
				if (this.counter == 5) {
					event.setY(event.getY() + 0);
				}
				if (this.counter == 6) {
					event.setY(event.getY() - 0.0784000015258789);
				}
				if (this.counter == 7) {
					event.setY(event.getY() - 0.1552320045166016);
				}
				if (this.counter == 8) {
					event.setY(event.getY() - 0.230527368912964);
				}
				if (this.counter == 9) {
					event.setY(event.getY() - 0.30431682745754424);
				}
				if (this.counter == 10) {
					event.setY(event.getY() - 0.37663049823865513);
				}
				if (this.counter == 11) {
					event.setY(event.getY() - 0.44749789698341763);
				}
				if (this.counter == 12) {
					event.setY(event.getY() - 0.0784000015258789);
					this.counter = 0;
				}
				counter++;
			}
			}
    @EventTarget
    public void onMove(final MoveEvent event) {
		if (this.vanilla.getValue()) {
	        ClientUtils.setMoveSpeed(event, this.vanillaspeed);
		}
		else if (this.faithfulmc.getValue()) {
	        ClientUtils.setMoveSpeed(event, this.vanillaspeed);
		}
		else if (this.spartan.getValue()) {
	        ClientUtils.setMoveSpeed(event, this.vanillaspeed);
		}
		else if (this.oldncp.getValue()) {
	        ClientUtils.setMoveSpeed(event, this.vanillaspeed);
		}
		else if (this.mineplex.getValue()) {
			if (!this.done) {
                NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(Speed.airSlot()));
                final C08PacketPlayerBlockPlacement place = new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).add(0, -1, 0), EnumFacing.UP.getIndex(), null, 0.5f, 1.0f, 0.5f);
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
    	        mc.thePlayer.sendQueue.addToSendQueue(place);
                SpeedUtils.setMoveSpeed(event, this.back ? (-this.mineplexSpeed) : this.mineplexSpeed);
                this.back = !this.back;
                if (mc.thePlayer.isMovingOnGround() && mc.thePlayer.ticksExisted % 2 == 0) {
                    this.mineplexSpeed += 0.105;
                }
                if (this.mineplexSpeed >= 2.5 * 1.3 && mc.thePlayer.isMovingOnGround()) {
                    SpeedUtils.setMoveSpeed(event, 0.0);
                    event.setY(mc.thePlayer.motionY = 0.42);
                    this.done = true;
                }
            }
            else {
                NetUtil.sendPacketNoEvents(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                if (mc.thePlayer.fallDistance == 0.0f) {
                    event.setY(mc.thePlayer.motionY += 0.038);
                }
                else if (mc.thePlayer.fallDistance <= 1.4) {
                    event.setY(mc.thePlayer.motionY += 0.032);
                }
                mineplexSpeed = this.mineplexSpeed * ((this.mineplexSpeed < 1.0) ? 0.99 : ((this.mineplexSpeed < 2.0) ? 0.985 : ((this.mineplexSpeed < 2.5) ? 0.972 : 0.97)));
                ClientUtils.setMoveSpeed(event, this.mineplexSpeed = mineplexSpeed);
                if (mc.thePlayer.isMovingOnGround()) {
                    this.done = false;
                }
            }
		}
		else if (this.oldncp2.getValue()) {
			if (ClientUtils.player().isCollidedHorizontally || (ClientUtils.player().moveForward == 0.0f && ClientUtils.player().moveStrafing != 0.0f)) {
                stage = 0;
                settingUpTicks = 6;
                ClientUtils.setMoveSpeed(event, 0);
            }
            else {
                if (settingUpTicks > 0 && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    this.moveSpeed = 0.09;
                    --settingUpTicks;
                    ClientUtils.setMoveSpeed(event, 0);
                }
                else if (stage == 1 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                	this.moveSpeed = 4.5 * Speed.getBaseMoveSpeed() - 0.01;
                    ClientUtils.setMoveSpeed(event, this.moveSpeed);
                }
                else if (stage == 2 && ClientUtils.player().isCollidedVertically && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    event.setY(ClientUtils.player().motionY = 0.4);
                    this.moveSpeed *= 1.987;
                    ClientUtils.setMoveSpeed(event, this.moveSpeed);
                }
                else if (stage == 3) {
                	event.setY(Minecraft.getMinecraft().thePlayer.motionY = 0);
                    final double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                    ClientUtils.setMoveSpeed(event, this.moveSpeed);
                }
                else {
                	event.setY(Minecraft.getMinecraft().thePlayer.motionY = 0);
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                    ClientUtils.setMoveSpeed(event, this.moveSpeed);
                    if (this.moveSpeed <= 0.2873) {
                    	ClientUtils.setMoveSpeed(event, 0.2873);
                    }
                }
                final List collidingList = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, ClientUtils.player().motionY, 0.0));
                final List collidingList2 = ClientUtils.world().getCollidingBlockBoundingBoxes(ClientUtils.player(), ClientUtils.player().boundingBox.offset(0.0, -0.4, 0.0));
                if (!ClientUtils.player().isCollidedVertically && (collidingList.size() > 0 || collidingList2.size() > 0) && stage > 10) {
                    if (stage >= 38) {
                        stage = 0;
                        settingUpTicks = 5;
                    }
                    else {
                    }
                }
                if (settingUpTicks <= 0 && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
                    ++stage;
                }
            }
		}
		else if (this.guardian.getValue()) {
	        ClientUtils.setMoveSpeed(event, 0.2873);
		}
		else if (this.aerox.getValue()) {
	        if (mc.thePlayer.ticksExisted % 3 == 0) {	
	        	ClientUtils.setMoveSpeed(event, 0);
	        }
		}
		 else if (this.cubecraft.getValue()) {
				if (!this.sentineldisabled) {
					ClientUtils.setMoveSpeed(event, 0);
				}
				if (this.sentineldisabled) {
			 Timer.timerSpeed = 0.2f;
			 event.setY(mc.thePlayer.motionY = 0); 
   			 if (ClientUtils.movementInput().jump) {
				 event.setY(mc.thePlayer.motionY = 0.42);
			 }
			 else if (ClientUtils.movementInput().sneak) {
				 event.setY(mc.thePlayer.motionY = -0.42);
			 }
   			 if (!ClientUtils.movementInput().jump && !ClientUtils.movementInput().sneak) {
	            if (mc.thePlayer.ticksExisted % 2 == 0) {
	 	        ClientUtils.setMoveSpeed(event, 2.4);
	            } else {
	            	ClientUtils.setMoveSpeed(event, 0.2873);
	            }
		 }
				}
		}
		else if (this.acr.getValue()) {
			if (!mc.thePlayer.isInWater()) {
            if (mc.thePlayer.ticksExisted % 2 == 0 && !ClientUtils.movementInput().sneak) {
    	        ClientUtils.setMoveSpeed(event, vanillaspeed);
            }
			}
		}
		else if (this.fakefall.getValue()) {
			event.setY(0);
		}
    }
}