package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.StepMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils2;

@Module.Mod(displayName = "Step")
public class Step extends Module
{
	private StepMode vanilla;
	private StepMode packet;
	private StepMode spartan;
	private StepMode matrix;
	private StepMode cubecraft;
	private StepMode mineplex;
	private StepMode ncp;
	private StepMode aac;
	private StepMode aerox;
	private StepMode flag;
	private StepMode position;
	private StepMode teleport;
	private StepMode timer;
    @Op(name = "Height", min = 1, max = 1337, increment = 1)
    public float height;
    @Op(name = "Bobbing")
    private boolean bobbing;
    private boolean didSend;
    private int sendTicks;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Step() {
        this.vanilla = new StepMode("Vanilla", true, this);
        this.packet = new StepMode("Packet", false, this);
        this.spartan = new StepMode("Spartan", false, this);
        this.matrix = new StepMode("Matrix", false, this);
        this.cubecraft = new StepMode("CubeCraft", false, this);
        this.mineplex = new StepMode("Mineplex", false, this);
        this.ncp = new StepMode("NCP", false, this);
        this.aac = new StepMode("AAC", false, this);
        this.aerox = new StepMode("Aerox", false, this);
        this.flag = new StepMode("Flag", false, this);
        this.position = new StepMode("Position", false, this);
        this.teleport = new StepMode("Teleport", false, this);
        this.timer = new StepMode("Timer", false, this);
        this.height = 3.5f;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.flag);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.teleport);
        OptionManager.getOptionList().add(this.timer);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Vanilla")).toString());
        }
        else if (this.packet.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Packet")).toString());
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.ncp.getValue()) {
        	this.setSuffix("NCP");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.aerox.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Aerox")).toString());
        }
        else if (this.flag.getValue()) {
            this.setSuffix(new StringBuilder(String.valueOf("Flag")).toString());
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
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
		mc.thePlayer.stepHeight = 0.6f;
		Timer.timerSpeed = 1.0f;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
        if (this.ncp.getValue()) {
        final MovementInput movementInput = ClientUtils.mc().thePlayer.movementInput;
        final double n = MovementInput.moveForward * 0.3 * Math.cos(Math.toRadians(ClientUtils.mc().thePlayer.rotationYaw + 90.0f));
        final MovementInput movementInput2 = ClientUtils.mc().thePlayer.movementInput;
        final double xOffset = n + MovementInput.moveStrafe * 0.3 * Math.sin(Math.toRadians(ClientUtils.mc().thePlayer.rotationYaw + 90.0f));
        final MovementInput movementInput3 = ClientUtils.mc().thePlayer.movementInput;
        final double n2 = MovementInput.moveForward * 0.3 * Math.sin(Math.toRadians(ClientUtils.mc().thePlayer.rotationYaw + 90.0f));
        final MovementInput movementInput4 = ClientUtils.mc().thePlayer.movementInput;
        final double zOffset = n2 - MovementInput.moveStrafe * 0.3 * Math.cos(Math.toRadians(ClientUtils.mc().thePlayer.rotationYaw + 90.0f));
        final boolean clearAbove = ClientUtils.mc().theWorld.getCollidingBoundingBoxes(ClientUtils.mc().thePlayer, ClientUtils.mc().thePlayer.boundingBox.offset(xOffset, 2.1, zOffset)).isEmpty();
        final boolean carpetCheck = ClientUtils.mc().theWorld.getCollidingBoundingBoxes(ClientUtils.mc().thePlayer, ClientUtils.mc().thePlayer.boundingBox.offset(xOffset, 2.01, zOffset)).isEmpty();
        final boolean two = !ClientUtils.mc().theWorld.getCollidingBoundingBoxes(ClientUtils.mc().thePlayer, ClientUtils.mc().thePlayer.boundingBox.offset(xOffset, 1.6, zOffset)).isEmpty();
        final boolean onedotfive = !ClientUtils.mc().theWorld.getCollidingBoundingBoxes(ClientUtils.mc().thePlayer, ClientUtils.mc().thePlayer.boundingBox.offset(xOffset, 1.4, zOffset)).isEmpty() && !two;
        if (clearAbove && ClientUtils.mc().thePlayer.onGround && !ClientUtils.mc().thePlayer.isOnLadder()) {
            if (ClientUtils.mc().thePlayer.isCollidedHorizontally) {
                ++this.sendTicks;
            }
            double[] array2;
            if (two) {
                final double[] array3;
                array2 = (array3 = new double[10]);
                array3[0] = 0.4;
                array3[1] = 0.75;
                array3[2] = 0.5;
                array3[3] = 0.41;
                array3[4] = 0.83;
                array3[5] = 1.16;
                array3[6] = 1.41;
                array3[7] = 1.57;
                array3[8] = 1.58;
                array3[9] = 1.42;
            }
            else if (onedotfive) {
                final double[] array4;
                array2 = (array4 = new double[7]);
                array4[0] = 0.4;
                array4[1] = 0.75;
                array4[2] = 0.5;
                array4[3] = 0.41;
                array4[4] = 0.83;
                array4[5] = 1.16;
                array4[6] = 1.41;
            }
            else {
                final double[] array5;
                array2 = (array5 = new double[2]);
                array5[0] = 0.42;
                array5[1] = 0.75;
            }
            final double[] pos = array2;
            if (this.sendTicks > (two ? 7 : (onedotfive ? 5 : 1))) {
                double[] array6;
                for (int length = (array6 = pos).length, i = 0; i < length; ++i) {
                    final double position = array6[i];
                    ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.mc().thePlayer.posX, ClientUtils.mc().thePlayer.posY + position, ClientUtils.mc().thePlayer.posZ, true));
                }
                ClientUtils.mc().thePlayer.setPosition(ClientUtils.mc().thePlayer.posX, ClientUtils.mc().thePlayer.posY + pos[pos.length - 1], ClientUtils.mc().thePlayer.posZ);
                if (!carpetCheck) {
                    ClientUtils.mc().thePlayer.motionY = 0.3;
                }
                this.sendTicks = 0;
            }
        }
        }
    }
    
    public void s(final double off) {
    	if (this.ncp.getValue()) {
        ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.mc().thePlayer.posX, ClientUtils.mc().thePlayer.posY + off, ClientUtils.mc().thePlayer.posZ, true));
    	}
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.bobbing && mc.thePlayer.isCollidedHorizontally) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
        if (this.vanilla.getValue()) {
    		mc.thePlayer.stepHeight = height;
        }
        else if (this.packet.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.42, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 0.75, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.0, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.16, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.25, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.2, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.3, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.2, ClientUtils.z(), ClientUtils.player().onGround));
            ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.x(), ClientUtils.y() + 1.4, ClientUtils.z(), ClientUtils.player().onGround));
    		mc.thePlayer.stepHeight = height;
            } else {
        		mc.thePlayer.stepHeight = 0.6f;
            }
        }
        else if (this.spartan.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
            	mc.thePlayer.stepHeight = 1.0f;
            } else {
            	mc.thePlayer.stepHeight = 0.6f;
            }
        }
        else if (this.matrix.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally) {
		        if (mc.thePlayer.ticksExisted % 3 == 0) {
		        	mc.thePlayer.jump();
		        }
            }
        }
        else if (this.cubecraft.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
    		mc.thePlayer.stepHeight = 3.0f;
            } else {
        		mc.thePlayer.stepHeight = 0.6f;
            }
        }
        else if (this.mineplex.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, true));
            	mc.thePlayer.stepHeight = 2.0f;
            } else {
            	mc.thePlayer.stepHeight = 0.6f;
            }
        }
        else if (this.aac.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	mc.thePlayer.jump();
            }
        }
        else if (this.aerox.getValue()) {
    		mc.thePlayer.stepHeight = height;
        }
        else if (this.flag.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 1337, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            	mc.thePlayer.stepHeight = this.height;
            } else {
            	mc.thePlayer.stepHeight = 0.6f;
            }
        }
        else if (this.position.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
            }
        }
        else if (this.teleport.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ);
            }
        }
        else if (this.timer.getValue()) {
            if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            	Timer.timerSpeed = 10f;
            	mc.thePlayer.jump();
            } else {
            	Timer.timerSpeed = 1.0f;
            }
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
