// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.events.impl.EventMotion;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3i;
import vip.Resolute.util.world.Vec3d;
import net.minecraft.util.BlockPos;
import vip.Resolute.events.impl.EventMove;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C0CPacketInput;
import vip.Resolute.events.impl.EventUpdate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import vip.Resolute.events.impl.EventCollide;
import vip.Resolute.events.Event;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import vip.Resolute.util.movement.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C18PacketSpectate;
import vip.Resolute.Resolute;
import vip.Resolute.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.util.misc.TimerUtil;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Fly extends Module
{
    public ModeSetting mode;
    public BooleanSetting bobbing;
    public BooleanSetting xzcancel;
    public NumberSetting motionSpeedProp;
    public NumberSetting verusTicks;
    public NumberSetting verusSpeed;
    public NumberSetting verusTimer;
    public BooleanSetting timerProp;
    public NumberSetting timerSpeedProp;
    public NumberSetting timerDurationProp;
    public NumberSetting initialSpeed;
    public NumberSetting funSpeed;
    public NumberSetting reductionSpeed;
    public BooleanSetting funDamage;
    public BooleanSetting damage;
    public NumberSetting reduce;
    public NumberSetting startSpeed;
    public NumberSetting hylexSpeed;
    public NumberSetting disablerHeld;
    public BooleanSetting disabler5C04;
    private ArrayList<Packet> packetList;
    public static boolean enabled;
    private final TimerUtil funtimer;
    TimerUtils timer;
    TimerUtil barTimer;
    TimerUtils flyTimer;
    int stage;
    private double moveSpeed;
    double lastDist;
    private double mineplexSpeed;
    private boolean hasFallen;
    private int i;
    int ticks;
    public static double startX;
    public static double startY;
    public static double startZ;
    public static boolean back;
    public static boolean done;
    int airTicks;
    boolean hasJumped;
    int slot;
    public boolean hypixelDamaged;
    public double speed;
    private double launchY;
    public boolean doSlow;
    public boolean damaged;
    public double movementSpeed;
    public boolean spoofGround;
    private int verusStage;
    private final ArrayList<C03PacketPlayer> disablerC03List;
    
    public boolean isModeSelected() {
        return this.mode.is("Verus Damage");
    }
    
    public boolean isMode2Selected() {
        return this.mode.is("Disabler5");
    }
    
    public boolean isMode3Selected() {
        return this.mode.is("Funcraft");
    }
    
    public boolean isMode4Selected() {
        return this.mode.is("Verus");
    }
    
    public Fly() {
        super("Fly", 21, "Allows flight", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Motion", "NCP Dev", "Funcraft", "Survivaldub Old", "Verus", "Verus Float", "Verus Damage", "Disabler5", "Packet1" });
        this.bobbing = new BooleanSetting("View Bobbing", false);
        this.xzcancel = new BooleanSetting("X Z Cancel", true);
        this.motionSpeedProp = new NumberSetting("Motion Speed", 5.0, () -> this.mode.is("Motion"), 0.1, 5.0, 0.1);
        this.verusTicks = new NumberSetting("Verus Ticks", 20.0, this::isModeSelected, 5.0, 300.0, 5.0);
        this.verusSpeed = new NumberSetting("Verus Speed", 2.5, () -> this.isModeSelected() || this.isMode4Selected(), 0.1, 5.0, 0.1);
        this.verusTimer = new NumberSetting("Verus Timer", 0.5, this::isModeSelected, 0.05, 2.0, 0.05);
        this.timerProp = new BooleanSetting("Timer", true, this::isMode3Selected);
        this.timerSpeedProp = new NumberSetting("Timer Speed", 1.7, () -> this.isMode3Selected() && this.timerProp.isEnabled(), 1.0, 5.0, 0.1);
        this.timerDurationProp = new NumberSetting("Timer Duration", 600.0, () -> this.isMode3Selected() && this.timerProp.isEnabled(), 0.0, 2000.0, 10.0);
        this.initialSpeed = new NumberSetting("Initial Speed", 1.0, this::isMode3Selected, 0.0, 1.0, 0.1);
        this.funSpeed = new NumberSetting("Funcraft Speed", 1.0, this::isMode3Selected, 0.1, 5.0, 0.1);
        this.reductionSpeed = new NumberSetting("Reduction Speed", 0.8, this::isMode3Selected, 0.0, 1.0, 0.1);
        this.funDamage = new BooleanSetting("Funcraft Damage", true, this::isMode3Selected);
        this.damage = new BooleanSetting("Damage", true, () -> this.mode.is("Survivaldub Old"));
        this.reduce = new NumberSetting("Reduce", 1.2, () -> this.mode.is("Survivaldub Old"), 1.0, 2.0, 0.1);
        this.startSpeed = new NumberSetting("Start Speed", 1.2, () -> this.mode.is("Survivaldub Old"), 1.0, 2.0, 0.1);
        this.hylexSpeed = new NumberSetting("Hylex Speed", 2.0, () -> this.mode.is("Hylex"), 0.1, 4.0, 0.1);
        this.disablerHeld = new NumberSetting("Disabler5 Packets", 7.0, this::isMode2Selected, 3.0, 20.0, 1.0);
        this.disabler5C04 = new BooleanSetting("Disabler5 Alt Packets", false, this::isMode2Selected);
        this.packetList = new ArrayList<Packet>();
        this.funtimer = new TimerUtil();
        this.barTimer = new TimerUtil();
        this.flyTimer = new TimerUtils();
        this.lastDist = 0.0;
        this.i = (int)this.verusTicks.getValue();
        this.hasJumped = false;
        this.slot = 0;
        this.hypixelDamaged = false;
        this.disablerC03List = new ArrayList<C03PacketPlayer>();
        this.addSettings(this.mode, this.bobbing, this.xzcancel, this.motionSpeedProp, this.verusTicks, this.verusSpeed, this.verusTimer, this.timerProp, this.timerSpeedProp, this.timerDurationProp, this.initialSpeed, this.funSpeed, this.reductionSpeed, this.funDamage, this.damage, this.reduce, this.startSpeed, this.hylexSpeed, this.disablerHeld, this.disabler5C04);
        this.timer = new TimerUtils();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.timer.reset();
        this.barTimer.reset();
        Fly.done = false;
        Fly.back = false;
        this.verusStage = 0;
        this.launchY = Fly.mc.thePlayer.posY;
        Fly.startX = Fly.mc.thePlayer.posX;
        Fly.startY = Fly.mc.thePlayer.posY;
        Fly.startZ = Fly.mc.thePlayer.posZ;
        Fly.enabled = true;
        this.hasFallen = false;
        this.movementSpeed = 0.0;
        this.ticks = 0;
        this.airTicks = 0;
        this.mineplexSpeed = 0.0;
        final double x = Fly.mc.thePlayer.posX;
        final double y = Fly.mc.thePlayer.posY;
        final double z = Fly.mc.thePlayer.posZ;
        this.i = (int)this.verusTicks.getValue();
        this.moveSpeed = 0.0;
        this.funtimer.reset();
        this.stage = 0;
        this.damaged = false;
        if (this.mode.is("AAC5")) {
            this.flyTimer.reset();
            Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.42, Fly.mc.thePlayer.posZ);
        }
        if (this.mode.is("Disabler3")) {
            Fly.mc.thePlayer.motionX = 0.0;
            Fly.mc.thePlayer.motionZ = 0.0;
            Fly.mc.thePlayer.motionY = 0.0;
            if (Fly.mc.thePlayer.onGround) {
                Resolute.addChatMessage("Jump into the air first and toggle");
                this.toggled = false;
            }
            else {
                Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, Double.MAX_VALUE, z, true));
            }
        }
        if (this.mode.is("Disabler4")) {
            Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, Double.MAX_VALUE, z, true));
        }
        if (this.mode.is("Disabler2")) {
            Fly.mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(Fly.mc.thePlayer.getGameProfile().getId()));
        }
        if (this.mode.is("NCP Dev") && Fly.mc.thePlayer.onGround) {
            Fly.mc.thePlayer.jump();
        }
        if (this.mode.is("Verus Damage")) {
            if (Fly.mc.theWorld.getCollidingBoundingBoxes(Fly.mc.thePlayer, Fly.mc.thePlayer.getEntityBoundingBox().offset(0.0, 3.0001, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.0001, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
            }
            Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.42, Fly.mc.thePlayer.posZ);
        }
        if (this.mode.is("Verus") && Fly.mc.thePlayer.onGround && !this.damaged) {
            if (Fly.mc.theWorld.getCollidingBoundingBoxes(Fly.mc.thePlayer, Fly.mc.thePlayer.getEntityBoundingBox().offset(0.0, 3.0001, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.0001, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
            }
            Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.42, Fly.mc.thePlayer.posZ);
        }
        if (this.mode.is("Survivaldub Old") && Fly.mc.thePlayer.onGround && Fly.mc.thePlayer.isCollidedVertically && this.damage.isEnabled()) {
            MovementUtils.fallDistDamage();
        }
        if (this.mode.is("Hylex")) {
            if (Fly.mc.theWorld.getCollidingBoundingBoxes(Fly.mc.thePlayer, Fly.mc.thePlayer.getEntityBoundingBox().offset(0.0, 3.0001, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 3.0001, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, false));
                Fly.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY, Fly.mc.thePlayer.posZ, true));
            }
            Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.42, Fly.mc.thePlayer.posZ);
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.speed = 0.0;
        this.timer.reset();
        this.hasFallen = false;
        this.slot = 0;
        Fly.mc.timer.timerSpeed = 1.0f;
        Fly.mc.thePlayer.speedInAir = 0.02f;
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.thePlayer.capabilities.allowFlying = false;
        this.hasJumped = false;
        Fly.mc.thePlayer.noClip = false;
        Fly.enabled = false;
        this.stage = 0;
        this.hypixelDamaged = false;
        try {
            for (final Packet packets : this.packetList) {
                Fly.mc.getNetHandler().sendPacketNoEvent(packets);
            }
            this.packetList.clear();
        }
        catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        if (this.xzcancel.isEnabled()) {
            Fly.mc.thePlayer.motionX = 0.0;
            Fly.mc.thePlayer.motionZ = 0.0;
        }
        if (this.mode.is("AAC5")) {
            this.sendAAC5Packets();
            Fly.mc.thePlayer.noClip = false;
        }
        if (this.mode.is("Disabler5")) {
            this.sendAAC5Packets();
        }
        if (this.mode.is("Motion")) {
            Fly.mc.thePlayer.motionX = 0.0;
            Fly.mc.thePlayer.motionZ = 0.0;
        }
        if (this.mode.is("Redesky S")) {
            Fly.mc.thePlayer.capabilities.isFlying = false;
        }
        if (this.mode.is("Vanilla")) {
            Fly.mc.thePlayer.capabilities.allowFlying = false;
            Fly.mc.thePlayer.capabilities.isFlying = false;
        }
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventCollide) {
            final EventCollide event = (EventCollide)e;
            if ((this.mode.is("Verus Float") || this.mode.is("Collide")) && event.getBlock() instanceof BlockAir && event.getY() <= this.launchY) {
                event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1, this.launchY, event.getZ() + 1));
            }
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event2 = (EventRender2D)e;
            final ScaledResolution resolution = new ScaledResolution(Fly.mc);
            final float x = resolution.getScaledWidth() / 2.0f;
            final float y = resolution.getScaledHeight() / 2.0f + 15.0f;
            final float percentage = Math.min(1.0f, this.barTimer.elapsed() / 20L / 128.0f);
            final float width = 80.0f;
            final float half = width / 2.0f;
            Gui.drawRect(x - half - 0.5f, y - 2.0f, x + half + 0.5f, y + 2.0f, 2013265920);
            GL11.glEnable(3089);
            RenderUtils.startScissorBox(resolution, (int)(x - half), (int)y - 2, (int)(width * percentage), 4);
            RenderUtils.drawGradientRect(x - half, y - 1.5f, x - half + width, y + 1.5f, true, -1571930, -16711936);
            GL11.glDisable(3089);
        }
        if (e instanceof EventPacket) {
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook && this.mode.is("Disabler5")) {
                final S08PacketPlayerPosLook s08PacketPlayerPosLook = ((EventPacket)e).getPacket();
            }
            if (((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer packetPlayer = ((EventPacket)e).getPacket();
                if (this.mode.is("Disabler5")) {
                    this.disablerC03List.add(packetPlayer);
                    e.setCancelled(true);
                    if (this.disablerC03List.size() > this.disablerHeld.getValue()) {
                        this.sendAAC5Packets();
                    }
                }
            }
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook && this.mode.is("NCP Dev") && Fly.mc.thePlayer.ticksExisted % 2 == 0) {
                e.setCancelled(true);
            }
        }
        if (e instanceof EventUpdate) {
            final double xDist = Fly.mc.thePlayer.posX - Fly.mc.thePlayer.prevPosX;
            final double zDist = Fly.mc.thePlayer.posZ - Fly.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            if (e.isPre()) {
                if (this.bobbing.isEnabled()) {
                    Fly.mc.thePlayer.cameraYaw = 0.105f;
                }
                if (this.mode.is("Packet1")) {
                    Fly.mc.getNetHandler().addToSendQueueSilent(new C0CPacketInput());
                    Fly.mc.getNetHandler().addToSendQueueSilent(new C0FPacketConfirmTransaction());
                    Fly.mc.thePlayer.motionY = 0.0;
                    if (MovementUtils.isMoving()) {
                        MovementUtils.setSpeed(1.000002384185791);
                    }
                    if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
                        --thePlayer.motionY;
                    }
                    if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                        final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                        ++thePlayer2.motionY;
                    }
                }
                if (this.mode.is("Verus Damage")) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    Fly.mc.thePlayer.onGround = true;
                    Fly.mc.thePlayer.fallDistance = 0.0f;
                    Fly.mc.thePlayer.isCollidedVertically = true;
                }
                if (this.mode.is("Verus")) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    Fly.mc.thePlayer.onGround = true;
                    Fly.mc.thePlayer.fallDistance = 0.0f;
                    Fly.mc.thePlayer.isCollidedVertically = true;
                }
            }
        }
        if (e instanceof EventCollide) {
            final EventCollide event = (EventCollide)e;
            if (this.mode.is("Spartan") && event.getBlock() instanceof BlockAir && event.getY() < Fly.mc.thePlayer.posY) {
                event.setBoundingBox(new AxisAlignedBB(event.getX(), event.getY(), event.getZ(), event.getX() + 1.0, Fly.mc.thePlayer.posY, event.getZ() + 1.0));
            }
        }
        if (e instanceof EventMove) {
            final EventMove event3 = (EventMove)e;
            if (this.mode.is("Verus Float")) {
                if (Fly.mc.thePlayer.onGround) {
                    this.movementSpeed = 0.592;
                    event3.setY(0.4499999868869782);
                    this.spoofGround = true;
                    this.verusStage = 0;
                }
                else if (this.verusStage <= 4) {
                    this.movementSpeed += 0.05;
                    event3.setY(0.0);
                }
                else {
                    this.movementSpeed = 0.34;
                    this.spoofGround = false;
                }
                ++this.verusStage;
                Fly.mc.thePlayer.motionY = event3.getY();
                MovementUtils.setStrafeSpeed(event3, this.movementSpeed - 1.0E-4);
            }
            if (this.mode.is("Hylex")) {
                if (Fly.mc.thePlayer.ticksExisted % 2 == 0) {
                    MovementUtils.setStrafeSpeed(event3, this.hylexSpeed.getValue());
                }
                else {
                    MovementUtils.setStrafeSpeed(event3, 0.0);
                }
            }
            if (this.mode.is("Spartan")) {
                if (!MovementUtils.isMovingOnGround()) {
                    final BlockPos blockPos = new BlockPos(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.getEntityBoundingBox().minY - 1.0, Fly.mc.thePlayer.posZ);
                    final Vec3d vec = new Vec3d(blockPos).addVector(0.4, 0.4, 0.4).mul(0.4f);
                    Fly.mc.playerController.onPlayerRightClick3d(Fly.mc.thePlayer, Fly.mc.theWorld, new ItemStack(Blocks.barrier), blockPos, EnumFacing.UP, vec);
                }
                MovementUtils.setStrafeSpeed(event3, 0.3);
            }
            if (this.mode.is("Verus")) {
                if (Fly.mc.thePlayer.hurtTime > 0 && !this.damaged) {
                    this.damaged = true;
                    Fly.mc.timer.timerSpeed = 1.0f;
                }
                if (!this.damaged) {
                    event3.setX(0.0);
                    event3.setZ(0.0);
                }
            }
            if (this.mode.is("Funcraft") && MovementUtils.isMoving()) {
                final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                switch (this.stage) {
                    case 0: {
                        this.moveSpeed = baseMoveSpeed;
                        if (!MovementUtils.isOnGround()) {
                            break;
                        }
                        event3.setY(Fly.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                        if ((this.funDamage.isEnabled() && MovementUtils.fallDistDamage()) || Fly.mc.thePlayer.fallDistance >= 3.0f) {
                            this.moveSpeed = baseMoveSpeed * (2.14 * this.initialSpeed.getValue());
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.moveSpeed *= this.funSpeed.getValue();
                        final double difference = this.reductionSpeed.getValue() * (this.moveSpeed - baseMoveSpeed);
                        this.moveSpeed -= difference;
                        break;
                    }
                    case 2: {
                        final double lastDif = this.reductionSpeed.getValue() * (this.lastDist - baseMoveSpeed);
                        this.moveSpeed = this.lastDist - lastDif;
                        break;
                    }
                    default: {
                        this.moveSpeed = this.lastDist - this.lastDist / 159.99;
                        break;
                    }
                }
                MovementUtils.setStrafeSpeed(event3, Math.max(baseMoveSpeed, this.moveSpeed));
                ++this.stage;
            }
            if (this.mode.is("Motion")) {
                MovementUtils.setStrafeSpeed(event3, this.motionSpeedProp.getValue());
            }
        }
        if (e instanceof EventMotion) {
            final EventMotion event4 = (EventMotion)e;
            if (e.isPre()) {
                if (this.mode.is("Verus Float")) {
                    Fly.mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput());
                    event4.setOnGround(Fly.mc.thePlayer.onGround || this.spoofGround);
                }
                if (this.mode.is("Survivaldub Old")) {
                    if (Fly.mc.thePlayer.onGround && this.ticks > 2) {
                        Fly.mc.thePlayer.setSpeed(0.0f);
                        this.toggle();
                    }
                    Fly.mc.thePlayer.cameraYaw = 0.07f;
                    Fly.mc.thePlayer.motionY = 0.0;
                    if (MovementUtils.isMovingOnGround()) {
                        Fly.mc.thePlayer.jump();
                    }
                    if (MovementUtils.isMoving()) {
                        ++this.ticks;
                        if (!Fly.mc.thePlayer.onGround && this.ticks > 1) {
                            if (this.moveSpeed < 1.05) {
                                this.moveSpeed += this.reduce.getValue() / 100.0;
                            }
                            Fly.mc.timer.timerSpeed = 1.0f;
                            Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - 1.0E-10, Fly.mc.thePlayer.posZ);
                            Fly.mc.thePlayer.setSpeed((float)(this.startSpeed.getValue() - this.moveSpeed));
                        }
                    }
                    else {
                        MovementUtils.setSpeed(0.0);
                    }
                }
                if (this.mode.is("Hylex")) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    ((EventMotion)e).setOnGround(true);
                    ((EventMotion)e).onGround = true;
                }
                if (this.mode.is("Funcraft")) {
                    if (this.timerProp.isEnabled()) {
                        if (this.funtimer.hasElapsed((long)this.timerDurationProp.getValue())) {
                            Fly.mc.timer.timerSpeed = 1.0f;
                        }
                        else {
                            Fly.mc.timer.timerSpeed = (float)this.timerSpeedProp.getValue();
                        }
                    }
                    event4.setOnGround(true);
                    Fly.mc.thePlayer.motionY = 0.0;
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + ((Fly.mc.thePlayer.ticksExisted % 2 == 0) ? -3.0E-4f : 3.0E-4f), Fly.mc.thePlayer.posZ);
                    final double xDist2 = Fly.mc.thePlayer.posX - Fly.mc.thePlayer.lastTickPosX;
                    final double zDist2 = Fly.mc.thePlayer.posZ - Fly.mc.thePlayer.lastTickPosZ;
                    this.lastDist = Math.sqrt(xDist2 * xDist2 + zDist2 * zDist2);
                }
                if (this.mode.is("NCP Dev")) {
                    MovementUtils.setMotion(MovementUtils.getBaseMoveSpeed() * 1.0);
                    if (Fly.mc.thePlayer.ticksExisted % 2 == 0) {
                        Fly.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY + 0.28, Fly.mc.thePlayer.posZ, true));
                    }
                    else {
                        Fly.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - 0.28, Fly.mc.thePlayer.posZ, false));
                    }
                    Fly.mc.thePlayer.sendQueue.addToSendQueueSilent(new C0CPacketInput(0.0f, 0.0f, true, true));
                    Fly.mc.thePlayer.motionY = 0.0;
                }
                if (this.mode.is("Vanilla")) {
                    Fly.mc.thePlayer.capabilities.isFlying = true;
                    Fly.mc.thePlayer.capabilities.allowFlying = true;
                }
                if (this.mode.is("Motion") || this.mode.is("Disabler5")) {
                    Fly.mc.thePlayer.fallDistance = 0.0f;
                    Fly.mc.thePlayer.motionX = 0.0;
                    Fly.mc.thePlayer.motionY = 0.0;
                    Fly.mc.thePlayer.motionZ = 0.0;
                    final EntityPlayerSP thePlayer3;
                    final EntityPlayerSP entityPlayerSP = thePlayer3 = Fly.mc.thePlayer;
                    thePlayer3.posY += 0.1;
                    final EntityPlayerSP entityPlayerSP2 = entityPlayerSP;
                    entityPlayerSP2.posY -= 0.1;
                    if (this.mode.is("Disabler5")) {
                        MovementUtils.strafe(5.0f);
                    }
                    if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                        final EntityPlayerSP entityPlayerSP3 = entityPlayerSP;
                        entityPlayerSP3.motionY += 1.5;
                    }
                    if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        final EntityPlayerSP entityPlayerSP4 = entityPlayerSP;
                        entityPlayerSP4.motionY -= 1.5;
                    }
                }
                if (this.mode.is("Collision")) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    MovementUtils.strafe();
                }
                if (this.mode.is("Verus Damage")) {
                    ((EventMotion)e).setOnGround(true);
                    ((EventMotion)e).onGround = true;
                    if (this.i > 0) {
                        MovementUtils.setMotion(this.verusSpeed.getValue());
                    }
                    --this.i;
                }
                if (this.mode.is("Verus")) {
                    event4.setOnGround(true);
                    event4.onGround = true;
                    MovementUtils.setMotion(this.verusSpeed.getValue());
                }
            }
        }
    }
    
    private void sendAAC5Packets() {
        float yaw = Fly.mc.thePlayer.rotationYaw;
        float pitch = Fly.mc.thePlayer.rotationPitch;
        for (final C03PacketPlayer packet : this.disablerC03List) {
            Fly.mc.getNetHandler().sendPacketNoEvent(packet);
            if (packet.isMoving()) {
                if (packet.getRotating()) {
                    yaw = packet.yaw;
                    pitch = packet.pitch;
                }
                if (this.disabler5C04.isEnabled()) {
                    Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.x, 1.0E159, packet.z, true));
                    Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.x, packet.y, packet.z, true));
                    System.out.println(true);
                }
                else {
                    Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.x, 1.0E159, packet.z, yaw, pitch, true));
                    Fly.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.x, packet.y, packet.z, yaw, pitch, true));
                }
            }
        }
        this.disablerC03List.clear();
    }
    
    static {
        Fly.enabled = false;
    }
}
