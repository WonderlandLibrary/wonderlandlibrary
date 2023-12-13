// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import vip.Resolute.util.misc.TimerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.impl.EventUpdate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import vip.Resolute.events.impl.EventCollide;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import vip.Resolute.util.player.InventoryUtils;
import net.minecraft.item.ItemBow;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMove;
import vip.Resolute.events.impl.EventCameraPosition;
import vip.Resolute.events.Event;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import vip.Resolute.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.Resolute.util.misc.TimerUtil;
import net.minecraft.util.Vec3;
import java.util.List;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class LongJump extends Module
{
    public ModeSetting mode;
    public NumberSetting jartexTicks;
    public NumberSetting agcSpeed;
    public NumberSetting agcTimer;
    public NumberSetting bowSpeed;
    public NumberSetting bowY;
    public NumberSetting bowTimer;
    public BooleanSetting yMotionReduce;
    public NumberSetting speedMult;
    public NumberSetting heightMult;
    public BooleanSetting candyStop;
    public BooleanSetting reduce;
    public BooleanSetting visualY;
    public NumberSetting funBoost;
    public BooleanSetting funReduce;
    public BooleanSetting boost;
    public BooleanSetting blink;
    public BooleanSetting timerBoost;
    public BooleanSetting indicate;
    public NumberSetting flightSpeedNum;
    public NumberSetting bowFlightSpeed;
    public NumberSetting timerSpeed;
    public NumberSetting timerEndSpeed;
    public NumberSetting blinkDelay;
    public NumberSetting slowdown;
    public BooleanSetting staffWarn;
    public static BooleanSetting disable;
    public boolean hypixelDamaged;
    private int ticks;
    private int hypixelstage;
    int prevSlot2;
    int i;
    boolean isRiding;
    boolean hasJumped;
    public double moveSpeed;
    public double air;
    public double motionY;
    private int delay;
    private double lastDist;
    public boolean collided;
    public boolean half;
    public int stage;
    public int groundTicks;
    public double lastDistance;
    public double movementSpeed;
    double x;
    double y;
    double z;
    boolean shouldBlink;
    int survivalstage;
    int candystage;
    float timer;
    int landingTicks;
    float dmgTimer;
    float flightSpeed;
    boolean damage;
    boolean hasBow;
    boolean shoot;
    protected boolean boosted;
    protected boolean doneBow;
    public double speed;
    private ArrayList<Packet> packetList;
    private List<Vec3> crumbs;
    private double launchY;
    private TimerUtil timercrumb;
    TimerUtil ljTimer;
    TimerUtil blinkTimer;
    TimerUtil bowTimerSD;
    private boolean onGroundLastTick;
    private double distance;
    double baseMoveSpeed;
    float prevPitch;
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    private boolean damaged;
    private boolean isCharging;
    private int chargedTicks;
    private int bowSlot;
    private double startY;
    private double oldPosY;
    private double yPos;
    
    public boolean isModeSelected() {
        return this.mode.is("Jartex");
    }
    
    public boolean isMode2Selected() {
        return this.mode.is("AGC");
    }
    
    public boolean isMode3Selected() {
        return this.mode.is("Watchdog Bow");
    }
    
    public boolean isMode4Selected() {
        return this.mode.is("SurvivalDub");
    }
    
    public LongJump() {
        super("LongJump", 35, "Makes the player jump farther", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Watchdog", "Watchdog Damage", "Watchdog Bow", "Verus", "AAC", "Experimental", "Funcraft", "SurvivalDub", "Jartex", "AGC" });
        this.jartexTicks = new NumberSetting("Jartex Ticks", 5.0, this::isModeSelected, 1.0, 25.0, 1.0);
        this.agcSpeed = new NumberSetting("AGC Speed", 5.0, this::isMode2Selected, 1.0, 10.0, 1.0);
        this.agcTimer = new NumberSetting("AGC Timer", 0.2, this::isMode2Selected, 0.1, 1.0, 0.1);
        this.bowSpeed = new NumberSetting("Bow Speed", 1.0, this::isMode3Selected, 0.1, 1.0, 0.1);
        this.bowY = new NumberSetting("Bow Y", 0.2, this::isMode3Selected, 0.1, 10.0, 0.1);
        this.bowTimer = new NumberSetting("Bow Timer", 1.0, this::isMode3Selected, 0.1, 2.0, 0.1);
        this.yMotionReduce = new BooleanSetting("Y Motion Reduce", false, this::isMode3Selected);
        this.speedMult = new NumberSetting("Speed Multiplier", 1.82, () -> this.mode.is("Experimental"), 1.0, 2.5, 0.01);
        this.heightMult = new NumberSetting("Height Multiplier", 1.53, () -> this.mode.is("Experimental"), 1.0, 2.0, 0.01);
        this.candyStop = new BooleanSetting("Full Stop", false, () -> this.mode.is("Candy Bar"));
        this.reduce = new BooleanSetting("Reduce", false, () -> this.mode.is("Experimental"));
        this.visualY = new BooleanSetting("Visual Y", false, () -> this.mode.is("Experimental"));
        this.funBoost = new NumberSetting("Funcraft Boost", 3.0, () -> this.mode.is("Funcraft"), 0.1, 10.0, 0.1);
        this.funReduce = new BooleanSetting("Funcraft Reduce", false, () -> this.mode.is("Funcraft"));
        this.boost = new BooleanSetting("Boost", true, this::isMode4Selected);
        this.blink = new BooleanSetting("Blink", true, this::isMode4Selected);
        this.timerBoost = new BooleanSetting("Timer", true, this::isMode4Selected);
        this.indicate = new BooleanSetting("Display Ticks", true, this::isMode4Selected);
        this.flightSpeedNum = new NumberSetting("Flight Speed", 1.5, this::isMode4Selected, 0.1, 1.7, 0.1);
        this.bowFlightSpeed = new NumberSetting("Bow Flight Speed", 1.5, this::isMode4Selected, 0.1, 1.7, 0.1);
        this.timerSpeed = new NumberSetting("Timer Speed", 1.5, this::isMode4Selected, 1.0, 2.0, 0.1);
        this.timerEndSpeed = new NumberSetting("Timer End Speed", 0.1, this::isMode4Selected, 0.1, 2.0, 0.1);
        this.blinkDelay = new NumberSetting("Blink Delay", 150.0, this::isMode4Selected, 10.0, 500.0, 5.0);
        this.slowdown = new NumberSetting("Slowdown", 145.0, this::isMode4Selected, 10.0, 160.0, 5.0);
        this.staffWarn = new BooleanSetting("Staff Warn", true, this::isMode4Selected);
        this.hypixelDamaged = false;
        this.hypixelstage = 0;
        this.i = 0;
        this.delay = 0;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.survivalstage = 0;
        this.candystage = 0;
        this.timer = 0.0f;
        this.dmgTimer = 0.0f;
        this.flightSpeed = 0.0f;
        this.hasBow = false;
        this.shoot = false;
        this.boosted = false;
        this.doneBow = false;
        this.packetList = new ArrayList<Packet>();
        this.crumbs = new CopyOnWriteArrayList<Vec3>();
        this.timercrumb = new TimerUtil();
        this.ljTimer = new TimerUtil();
        this.blinkTimer = new TimerUtil();
        this.bowTimerSD = new TimerUtil();
        this.onGroundLastTick = false;
        this.distance = 0.0;
        this.addSettings(this.mode, LongJump.disable, this.jartexTicks, this.agcSpeed, this.agcTimer, this.bowSpeed, this.bowY, this.bowTimer, this.yMotionReduce, this.speedMult, this.heightMult, this.candyStop, this.reduce, this.visualY, this.funBoost, this.funReduce, this.boost, this.blink, this.timerBoost, this.indicate, this.flightSpeedNum, this.bowFlightSpeed, this.timerSpeed, this.timerEndSpeed, this.slowdown, this.staffWarn);
    }
    
    @Override
    public void onEnable() {
        this.i = 0;
        LongJump.mc.timer.timerSpeed = 1.0f;
        this.isRiding = false;
        this.hasJumped = false;
        final double n = 0.0;
        this.movementSpeed = n;
        this.lastDistance = n;
        final int n2 = 0;
        this.groundTicks = n2;
        this.stage = n2;
        this.groundTicks = 0;
        this.shouldBlink = false;
        this.startY = LongJump.mc.thePlayer.getEyeHeight();
        this.crumbs.clear();
        this.delay = 0;
        this.prevPitch = LongJump.mc.thePlayer.rotationPitch;
        this.hypixelstage = 0;
        this.ticks = 0;
        this.prevSlot2 = LongJump.mc.thePlayer.inventory.currentItem;
        this.collided = false;
        this.damaged = false;
        this.isCharging = false;
        this.chargedTicks = 0;
        this.launchY = LongJump.mc.thePlayer.posY;
        this.doneBow = false;
        this.shoot = false;
        this.hasBow = false;
        this.timer = 0.0f;
        this.landingTicks = 0;
        this.dmgTimer = 0.0f;
        this.flightSpeed = 0.0f;
        this.candystage = 0;
        this.survivalstage = 0;
        this.onGroundLastTick = false;
        this.distance = 0.0;
        this.damage = false;
        this.x = LongJump.mc.thePlayer.posX;
        this.y = LongJump.mc.thePlayer.posY;
        this.z = LongJump.mc.thePlayer.posZ;
        this.oldPosY = LongJump.mc.thePlayer.posY;
        this.yPos = LongJump.mc.thePlayer.getEyeHeight();
        this.bowTimerSD.reset();
        this.blinkTimer.reset();
    }
    
    @Override
    public void onDisable() {
        this.yPos = 0.0;
        this.oldPosY = 0.0;
        this.i = 0;
        LongJump.mc.timer.timerSpeed = 1.0f;
        this.crumbs.clear();
        this.motionY = 0.0;
        this.isRiding = false;
        this.hasJumped = false;
        this.boosted = false;
        this.hypixelDamaged = false;
        this.ticks = 0;
        LongJump.mc.thePlayer.speedInAir = 0.02f;
        this.speed = 0.0;
        this.shouldBlink = false;
        this.x = LongJump.mc.thePlayer.posX;
        this.y = LongJump.mc.thePlayer.posY;
        this.z = LongJump.mc.thePlayer.posZ;
        if (this.mode.is("SurvivalDub") || this.mode.is("Candy Bar")) {
            LongJump.mc.thePlayer.setPosition(this.x, this.y + 1.0E-12, this.z);
        }
        try {
            for (final Packet packets : this.packetList) {
                LongJump.mc.getNetHandler().sendPacketNoEvent(packets);
            }
            this.packetList.clear();
        }
        catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventCameraPosition) {
            final EventCameraPosition event = (EventCameraPosition)e;
            if (this.mode.is("Experimental") && this.visualY.isEnabled()) {
                e.setCancelled(true);
                event.setY(LongJump.mc.thePlayer.prevPosY - this.oldPosY);
            }
        }
        if (e instanceof EventMove) {
            final EventMove event2 = (EventMove)e;
            if (this.mode.is("Mineplex Low Hop")) {
                event2.setY(LongJump.mc.thePlayer.motionY = (LongJump.mc.thePlayer.movementInput.jump ? 0.41999998688697815 : 0.0));
            }
            if (this.mode.is("Experimental")) {
                if (LongJump.mc.thePlayer.hurtTime > 0 && !this.damaged) {
                    this.damaged = true;
                }
                if (!this.damaged) {
                    event2.setX(0.0);
                    event2.setZ(0.0);
                    return;
                }
                if (MovementUtils.isMoving() && this.damaged && LongJump.mc.thePlayer.ticksExisted > 8) {
                    final double baseMoveSpeed = MovementUtils.getBaseSpeedHypixel();
                    switch (this.stage) {
                        case 0: {
                            if (MovementUtils.isMovingOnGround()) {
                                this.moveSpeed = baseMoveSpeed * this.speedMult.getValue();
                                event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815) * this.heightMult.getValue());
                                break;
                            }
                            break;
                        }
                        case 1: {
                            this.moveSpeed -= 0.18 * (this.moveSpeed - MovementUtils.getSpeed());
                            break;
                        }
                        default: {
                            this.moveSpeed = MovementUtils.calculateFriction(this.moveSpeed, this.lastDist, baseMoveSpeed);
                            break;
                        }
                    }
                    MovementUtils.setSpeed(event2, Math.max(this.moveSpeed, baseMoveSpeed));
                    ++this.stage;
                }
            }
            if (this.mode.is("Watchdog") && MovementUtils.isMoving()) {
                final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                switch (this.stage) {
                    case 0: {
                        if (MovementUtils.isOnGround()) {
                            this.moveSpeed = baseMoveSpeed * 2.14;
                            event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.moveSpeed *= 1.0;
                    }
                    case 2: {
                        final double difference = (0.6336 + MovementUtils.getJumpBoostModifier() * 0.2) * (this.moveSpeed - baseMoveSpeed);
                        this.moveSpeed -= difference;
                        break;
                    }
                    default: {
                        this.moveSpeed = MovementUtils.calculateFriction(this.moveSpeed, this.lastDist, baseMoveSpeed);
                        break;
                    }
                }
                MovementUtils.setSpeed(event2, Math.max(this.moveSpeed, baseMoveSpeed));
                ++this.stage;
            }
            if (this.mode.is("13367") && MovementUtils.isMoving()) {
                final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                switch (this.stage) {
                    case 0: {
                        if (MovementUtils.isOnGround()) {
                            this.moveSpeed = baseMoveSpeed * 2.14;
                            event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.moveSpeed *= 2.0;
                    }
                    case 2: {
                        final double difference = (0.6336 + MovementUtils.getJumpBoostModifier() * 0.2) * (this.moveSpeed - baseMoveSpeed);
                        this.moveSpeed -= difference;
                        break;
                    }
                    default: {
                        this.moveSpeed = MovementUtils.calculateFriction(this.moveSpeed, this.lastDist, baseMoveSpeed);
                        break;
                    }
                }
                MovementUtils.setSpeed(event2, Math.max(this.moveSpeed, baseMoveSpeed));
                ++this.stage;
            }
            if (this.mode.is("Watchdog Damage") && MovementUtils.isMoving()) {
                final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                switch (this.stage) {
                    case 0: {
                        if (MovementUtils.isOnGround() && MovementUtils.fallDistDamage()) {
                            this.moveSpeed = baseMoveSpeed * 2.14;
                            event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                            this.damage = true;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (this.damage) {
                            event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                        }
                    }
                    case 2: {
                        final double difference = (0.6336 + MovementUtils.getJumpBoostModifier() * 0.2) * (this.moveSpeed - baseMoveSpeed);
                        this.moveSpeed -= difference;
                        if (this.damage) {
                            event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                            break;
                        }
                        break;
                    }
                    default: {
                        this.moveSpeed = MovementUtils.calculateFriction(this.moveSpeed, this.lastDist, baseMoveSpeed);
                        break;
                    }
                }
                MovementUtils.setSpeed(event2, Math.max(this.moveSpeed, baseMoveSpeed));
                ++this.stage;
            }
            if (this.mode.is("Candy Bar")) {
                if (MovementUtils.isMoving() && this.candystage == 1) {
                    final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                    switch (this.stage) {
                        case 0: {
                            if (MovementUtils.isOnGround()) {
                                this.moveSpeed = baseMoveSpeed * 2.14;
                                event2.setY(LongJump.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.41999998688697815));
                                break;
                            }
                            break;
                        }
                        case 1: {
                            this.moveSpeed *= 2.0;
                        }
                        case 2: {
                            final double difference = (0.6336 + MovementUtils.getJumpBoostModifier() * 0.2) * (this.moveSpeed - baseMoveSpeed);
                            this.moveSpeed -= difference;
                            break;
                        }
                        default: {
                            this.moveSpeed = MovementUtils.calculateFriction(this.moveSpeed, this.lastDist, baseMoveSpeed);
                            break;
                        }
                    }
                    MovementUtils.setSpeed(event2, Math.max(this.moveSpeed, baseMoveSpeed));
                    ++this.stage;
                }
                if (this.candystage == 0 && this.candyStop.isEnabled()) {
                    event2.setX(0.0);
                    event2.setZ(0.0);
                }
            }
            if (this.mode.is("Funcraft")) {
                if ((LongJump.mc.thePlayer.moveForward == 0.0f && LongJump.mc.thePlayer.moveStrafing == 0.0f) || LongJump.mc.theWorld == null) {
                    this.speed = 0.27999999999999997;
                    return;
                }
                if (LongJump.mc.thePlayer.onGround) {
                    if (!this.onGroundLastTick && LongJump.mc.thePlayer.motionY >= -0.3) {
                        this.speed = this.funBoost.getValue() * 0.27999999999999997;
                    }
                    else {
                        this.speed *= 2.15 - 1.0 / Math.pow(10.0, 5.0);
                        event2.setY(LongJump.mc.thePlayer.motionY = 0.41999998688697815);
                        LongJump.mc.thePlayer.onGround = true;
                    }
                }
                else if (this.onGroundLastTick) {
                    if (this.distance < 2.147) {
                        this.distance = 2.147;
                    }
                    this.speed = this.distance - 0.66 * (this.distance - 0.27999999999999997);
                }
                else {
                    this.speed = this.distance - this.distance / 159.0;
                }
                if (this.funReduce.isEnabled()) {
                    LongJump.mc.thePlayer.motionY = -0.02;
                }
                this.onGroundLastTick = LongJump.mc.thePlayer.onGround;
                this.speed = Math.max(this.speed, 0.27999999999999997);
                event2.setX(-(Math.sin(LongJump.mc.thePlayer.getDirection()) * this.speed));
                event2.setZ(Math.cos(LongJump.mc.thePlayer.getDirection()) * this.speed);
            }
        }
        if (e instanceof EventMotion) {
            final EventMotion event3 = (EventMotion)e;
            if (e.isPre()) {
                if (this.mode.is("Funcraft")) {
                    this.distance = Math.hypot(LongJump.mc.thePlayer.posX - LongJump.mc.thePlayer.prevPosX, LongJump.mc.thePlayer.posZ - LongJump.mc.thePlayer.prevPosZ);
                    if (LongJump.disable.isEnabled() && !this.onGroundLastTick && LongJump.mc.thePlayer.isCollidedVertically && this.isEnabled()) {
                        this.toggle();
                    }
                }
                if (this.mode.is("Watchdog Damage")) {
                    if (MovementUtils.isMoving() && MovementUtils.isOnGround() && ++this.groundTicks > 1) {
                        this.toggle();
                    }
                    if (LongJump.mc.thePlayer.fallDistance < 1.0f) {
                        final EntityPlayerSP thePlayer = LongJump.mc.thePlayer;
                        thePlayer.motionY += 0.005;
                    }
                    final EntityPlayerSP player = LongJump.mc.thePlayer;
                    final double xDist = player.posX - player.lastTickPosX;
                    final double zDist = player.posZ - player.lastTickPosZ;
                    this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                }
                if (this.mode.is("Experimental")) {
                    if (!this.damaged) {
                        final int bowSlot = InventoryUtils.findInHotBar(stack -> stack != null && stack.getItem() instanceof ItemBow);
                        if (bowSlot == -1) {
                            return;
                        }
                        if (!this.isCharging) {
                            if (MovementUtils.isOnGround()) {
                                final boolean needSwitch = LongJump.mc.thePlayer.inventory.currentItem != bowSlot;
                                if (needSwitch) {
                                    LongJump.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(bowSlot));
                                }
                                this.bowSlot = bowSlot;
                                LongJump.mc.getNetHandler().sendPacketNoEvent(LongJump.BLOCK_PLACEMENT);
                                this.isCharging = true;
                                this.chargedTicks = 0;
                            }
                        }
                        else {
                            ++this.chargedTicks;
                            if (bowSlot != this.bowSlot) {
                                this.toggle();
                                return;
                            }
                            if (this.chargedTicks == 3) {
                                final int physicalHeldItem = LongJump.mc.thePlayer.inventory.currentItem;
                                LongJump.mc.getNetHandler().sendPacketNoEvent(LongJump.PLAYER_DIGGING);
                                if (this.bowSlot != physicalHeldItem) {
                                    LongJump.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(physicalHeldItem));
                                }
                            }
                            else if (this.chargedTicks == 2) {
                                event3.setPitch(-90.0f);
                            }
                        }
                        return;
                    }
                    else {
                        final EntityPlayer player2 = LongJump.mc.thePlayer;
                        if (this.reduce.isEnabled() && LongJump.mc.thePlayer.motionY < 0.15 && LongJump.mc.thePlayer.motionY > -0.3 && LongJump.mc.thePlayer.fallDistance < 0.05 && !LongJump.mc.thePlayer.isInWater()) {
                            final EntityPlayerSP thePlayer2 = LongJump.mc.thePlayer;
                            thePlayer2.motionY += 0.0417;
                        }
                        if (MovementUtils.isMoving() && MovementUtils.isOnGround() && ++this.groundTicks >= 1) {
                            this.toggle();
                        }
                    }
                }
                if (this.mode.is("Experimental")) {
                    final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                    final double xDif = player.posX - player.lastTickPosX;
                    final double zDif = player.posZ - player.lastTickPosZ;
                    this.lastDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);
                }
                if (this.mode.is("Verus")) {
                    LongJump.mc.gameSettings.keyBindJump.pressed = false;
                    if (LongJump.mc.thePlayer.onGround && MovementUtils.isMoving()) {
                        LongJump.mc.thePlayer.jump();
                        MovementUtils.strafe(0.48f);
                    }
                    else {
                        MovementUtils.strafe();
                    }
                }
                if (this.mode.is("AGC") && LongJump.mc.thePlayer.hurtTime > 0) {
                    LongJump.mc.gameSettings.keyBindForward.pressed = true;
                    LongJump.mc.timer.timerSpeed = (float)this.agcTimer.getValue();
                    if (!LongJump.mc.thePlayer.onGround && LongJump.mc.gameSettings.keyBindForward.isKeyDown()) {
                        LongJump.mc.thePlayer.setSpeed((float)this.agcSpeed.getValue());
                    }
                }
                if (this.mode.is("Candy Bar")) {
                    this.x = LongJump.mc.thePlayer.posX;
                    this.y = LongJump.mc.thePlayer.posY;
                    this.z = LongJump.mc.thePlayer.posZ;
                    if (MovementUtils.isMoving() && MovementUtils.isOnGround() && this.candystage == 1) {
                        this.toggle();
                    }
                    if (LongJump.mc.thePlayer.fallDistance < 1.0f) {
                        final EntityPlayerSP thePlayer3 = LongJump.mc.thePlayer;
                        thePlayer3.motionY += 0.005;
                    }
                    final EntityPlayerSP player = LongJump.mc.thePlayer;
                    final double xDist = player.posX - player.lastTickPosX;
                    final double zDist = player.posZ - player.lastTickPosZ;
                    this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    if (this.candystage == 0 && LongJump.mc.thePlayer.hurtTime == 0) {
                        if (this.dmgTimer <= 7.0f && LongJump.mc.thePlayer.onGround) {
                            LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y + 0.42, this.z, false));
                            LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, false));
                            event3.onGround = false;
                        }
                        else if (this.dmgTimer > 120.0f) {
                            LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true));
                        }
                        ++this.dmgTimer;
                    }
                }
                if (this.mode.is("Candy Bar")) {
                    if (this.candystage == 0 && LongJump.mc.thePlayer.hurtTime > 0) {
                        LongJump.mc.thePlayer.setPosition(this.x, this.y + 0.5 - 0.0061, this.z);
                        this.candystage = 1;
                    }
                    if (this.candystage == 0 && !this.candyStop.isEnabled()) {
                        MovementUtils.setSpeed(0.0);
                    }
                }
                if (this.mode.is("SurvivalDub")) {
                    this.x = LongJump.mc.thePlayer.posX;
                    this.y = LongJump.mc.thePlayer.posY;
                    this.z = LongJump.mc.thePlayer.posZ;
                    if (LongJump.mc.thePlayer.getCurrentEquippedItem() != null) {
                        for (int i = 0; i < 9; ++i) {
                            if (LongJump.mc.thePlayer.inventory.getStackInSlot(i) != null) {
                                if (LongJump.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
                                    this.hasBow = true;
                                }
                            }
                        }
                    }
                    if (!this.hasBow) {
                        if (this.survivalstage > 0) {
                            LongJump.mc.thePlayer.motionY = 0.0;
                            LongJump.mc.thePlayer.setPosition(LongJump.mc.thePlayer.posX, LongJump.mc.thePlayer.posY - 1.0E-10, LongJump.mc.thePlayer.posZ);
                        }
                        else if (this.survivalstage == 0 && LongJump.mc.thePlayer.hurtTime == 0) {
                            if (this.dmgTimer <= 7.0f && LongJump.mc.thePlayer.onGround) {
                                LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y + 0.42, this.z, false));
                                LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, false));
                                event3.onGround = false;
                            }
                            else if (this.dmgTimer > 120.0f) {
                                LongJump.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.x, this.y, this.z, true));
                            }
                            ++this.dmgTimer;
                        }
                    }
                    else {
                        if (this.survivalstage > 0) {
                            LongJump.mc.thePlayer.motionY = 0.0;
                            LongJump.mc.thePlayer.setPosition(LongJump.mc.thePlayer.posX, LongJump.mc.thePlayer.posY - 1.0E-10, LongJump.mc.thePlayer.posZ);
                        }
                        final int slot = this.getSlotWithBow();
                        if (this.getSlotWithBow() == -1) {
                            return;
                        }
                        LongJump.mc.thePlayer.inventory.currentItem = slot;
                        if (LongJump.mc.thePlayer.getCurrentEquippedItem() != null) {
                            if (LongJump.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && this.survivalstage == 0 && LongJump.mc.thePlayer.hurtTime == 0 && !this.shoot) {
                                event3.setPitch(-90.0f);
                                MovementUtils.strafe();
                                LongJump.mc.thePlayer.motionX = 0.0;
                                LongJump.mc.thePlayer.motionY = 0.0;
                                LongJump.mc.thePlayer.motionZ = 0.0;
                                LongJump.mc.thePlayer.jumpMovementFactor = 0.0f;
                                LongJump.mc.thePlayer.onGround = false;
                                LongJump.mc.gameSettings.keyBindUseItem.pressed = true;
                                if (this.bowTimerSD.hasElapsed(165L)) {
                                    this.shoot = true;
                                    this.bowTimerSD.reset();
                                }
                            }
                            else if (this.shoot) {
                                MovementUtils.strafe();
                                LongJump.mc.thePlayer.motionX = 0.0;
                                LongJump.mc.thePlayer.motionY = 0.0;
                                LongJump.mc.thePlayer.motionZ = 0.0;
                                LongJump.mc.thePlayer.jumpMovementFactor = 0.0f;
                                LongJump.mc.thePlayer.onGround = false;
                                LongJump.mc.gameSettings.keyBindUseItem.pressed = false;
                                LongJump.mc.thePlayer.stopUsingItem();
                                LongJump.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                LongJump.mc.thePlayer.inventory.currentItem = this.prevSlot2;
                            }
                        }
                    }
                }
            }
            if (this.mode.is("SurvivalDub")) {
                LongJump.mc.getNetHandler().sendPacketNoEvent(new C0CPacketInput(this.flightSpeed, this.flightSpeed, false, false));
                if (this.staffWarn.isEnabled()) {
                    for (int i = 0; i < LongJump.mc.theWorld.playerEntities.size(); ++i) {
                        final String name = LongJump.mc.theWorld.playerEntities.get(i).getName();
                        if (name.contains("XMati") || name.contains("M1au_") || name.contains("JennyPixu") || name.contains("iChessman7w7") || name.contains("DashoDM") || name.contains("Thyonne") || name.contains("zLuisaz") || name.contains("conlAlfon") || name.contains("Jenn") || name.contains("David") || name.contains("ReachBoy")) {
                            final String string = "There is a staff member in your game";
                            Resolute.getNotificationManager().add(new Notification("Staff Alert", string, 4000L, NotificationType.WARNING));
                        }
                    }
                }
                if (this.survivalstage == 0 && LongJump.mc.thePlayer.hurtTime > 0) {
                    if (this.blink.isEnabled()) {
                        this.shouldBlink = true;
                    }
                    else {
                        this.shouldBlink = false;
                    }
                    LongJump.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(LongJump.mc.thePlayer.inventory.currentItem));
                    this.damage = true;
                    if (!this.hasBow) {
                        LongJump.mc.thePlayer.setPosition(this.x, this.y + 0.5 - 0.0061, this.z);
                    }
                    this.survivalstage = 1;
                    this.timer = 0.0f;
                }
                if (this.survivalstage == 1) {
                    if (this.timer == 0.0f) {
                        this.flightSpeed = 0.46f;
                    }
                    if (this.timer == 1.0f) {
                        this.flightSpeed = 0.75f;
                    }
                    if (this.timer == 2.0f) {
                        if (this.hasBow) {
                            this.flightSpeed = (float)this.bowFlightSpeed.getValue();
                        }
                        else {
                            this.flightSpeed = (float)this.flightSpeedNum.getValue();
                        }
                        this.survivalstage = 2;
                    }
                }
                if (this.survivalstage == 2) {
                    if (this.timerBoost.isEnabled()) {
                        if (this.flightSpeed > (this.hasBow ? this.bowFlightSpeed.getValue() : this.flightSpeedNum.getValue()) - this.timerEndSpeed.getValue()) {
                            LongJump.mc.timer.timerSpeed = (float)this.timerSpeed.getValue();
                        }
                        else {
                            LongJump.mc.timer.timerSpeed = 1.0f;
                        }
                    }
                    if (this.flightSpeed > 0.25) {
                        this.flightSpeed -= (float)(this.flightSpeed / this.slowdown.getValue());
                    }
                }
                if (LongJump.mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving()) {
                    this.flightSpeed = 0.25f;
                }
                if (this.survivalstage != 0) {
                    MovementUtils.setSpeed(this.flightSpeed);
                }
                else {
                    MovementUtils.setSpeed(0.0);
                }
                ++this.timer;
                if (this.survivalstage > 0) {
                    ++this.landingTicks;
                }
            }
            if (this.mode.is("Watchdog Bow")) {
                if (!this.hypixelDamaged && LongJump.mc.thePlayer.getCurrentEquippedItem() != null) {
                    for (int i = 0; i < 9; ++i) {
                        if (LongJump.mc.thePlayer.inventory.getStackInSlot(i) != null) {
                            if (LongJump.mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
                                LongJump.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(LongJump.mc.thePlayer.inventory.currentItem = i));
                            }
                        }
                    }
                }
                if (LongJump.mc.thePlayer.getCurrentEquippedItem() != null && LongJump.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && !this.hypixelDamaged) {
                    if (this.ticks < 7) {
                        event3.setPitch(-90.0f);
                    }
                    MovementUtils.strafe();
                    LongJump.mc.thePlayer.motionX = 0.0;
                    LongJump.mc.thePlayer.motionY = 0.0;
                    LongJump.mc.thePlayer.motionZ = 0.0;
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.0f;
                    LongJump.mc.thePlayer.onGround = false;
                    if (this.ticks != 50) {
                        ++this.ticks;
                    }
                    if (this.ticks >= 7 && this.ticks != 50) {
                        LongJump.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        this.ticks = 50;
                    }
                    else if (this.ticks == 1) {
                        LongJump.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(LongJump.mc.thePlayer.inventory.getCurrentItem()));
                    }
                }
                if (this.ticks == 50 && LongJump.mc.thePlayer.hurtTime > 0) {
                    this.hypixelDamaged = true;
                    if (!this.collided) {
                        LongJump.mc.thePlayer.jump();
                        LongJump.mc.thePlayer.motionY = this.bowY.getValue() / 10.0;
                        ++this.hypixelstage;
                    }
                    if (this.hypixelstage == 1) {
                        this.collided = true;
                    }
                }
                if (this.hypixelDamaged) {
                    LongJump.mc.timer.timerSpeed = (float)this.bowTimer.getValue();
                    if (!LongJump.mc.thePlayer.onGround) {
                        if (LongJump.mc.gameSettings.keyBindForward.isKeyDown()) {
                            LongJump.mc.thePlayer.setSpeed((float)this.bowSpeed.getValue());
                        }
                        if (this.yMotionReduce.isEnabled() && LongJump.mc.thePlayer.fallDistance < 1.0f) {
                            if (LongJump.mc.thePlayer.motionY < 0.0) {
                                final EntityPlayerSP thePlayer4 = LongJump.mc.thePlayer;
                                thePlayer4.motionY *= 0.85;
                            }
                            final EntityPlayerSP thePlayer5 = LongJump.mc.thePlayer;
                            thePlayer5.motionY += 0.001;
                        }
                    }
                    else if (LongJump.mc.thePlayer.hurtTime == 0) {
                        Resolute.getNotificationManager().add(new Notification("Flag alert", "Wait until this notification disappears before toggling again", 10000L, NotificationType.WARNING));
                        this.i = 0;
                        LongJump.mc.timer.timerSpeed = 1.0f;
                        this.motionY = 0.0;
                        this.isRiding = false;
                        this.hasJumped = false;
                        this.boosted = false;
                        this.hypixelDamaged = false;
                        this.ticks = 0;
                        LongJump.mc.thePlayer.speedInAir = 0.02f;
                        this.toggled = false;
                    }
                }
            }
            if (this.mode.is("13367") && e.isPre()) {
                final EntityPlayerSP player = LongJump.mc.thePlayer;
                final double xDist = player.posX - player.lastTickPosX;
                final double zDist = player.posZ - player.lastTickPosZ;
                this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (MovementUtils.isOnGround() && !LongJump.mc.thePlayer.isOnLadder() && ++this.groundTicks >= 1) {
                    this.toggle();
                }
                if (player.fallDistance < 1.0f) {
                    if (player.motionY < 0.0) {
                        final EntityPlayerSP entityPlayerSP = player;
                        entityPlayerSP.motionY *= 0.75;
                    }
                    final EntityPlayerSP entityPlayerSP2 = player;
                    entityPlayerSP2.motionY += 0.001;
                }
            }
            if (this.mode.is("AAC")) {
                if (!LongJump.mc.thePlayer.onGround && !LongJump.mc.thePlayer.isCollided) {
                    LongJump.mc.timer.timerSpeed = 0.6f;
                    if (LongJump.mc.thePlayer.motionY < 0.0 && this.delay > 0) {
                        --this.delay;
                        LongJump.mc.timer.timerSpeed = 0.95f;
                    }
                    else {
                        this.delay = 0;
                        LongJump.mc.thePlayer.motionY /= 0.9800000190734863;
                        final EntityPlayerSP thePlayer6 = LongJump.mc.thePlayer;
                        thePlayer6.motionY += 0.03;
                        final EntityPlayerSP thePlayer7 = LongJump.mc.thePlayer;
                        thePlayer7.motionY *= 0.9800000190734863;
                        LongJump.mc.thePlayer.jumpMovementFactor = 0.03625f;
                    }
                }
                else {
                    LongJump.mc.timer.timerSpeed = 1.0f;
                    this.delay = 2;
                }
            }
            if (this.mode.is("Watchdog") && e.isPre()) {
                final EntityPlayerSP player = LongJump.mc.thePlayer;
                final double xDist = player.posX - player.lastTickPosX;
                final double zDist = player.posZ - player.lastTickPosZ;
                this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (MovementUtils.isOnGround() && !LongJump.mc.thePlayer.isOnLadder() && ++this.groundTicks >= 1) {
                    this.toggle();
                }
                if (player.fallDistance < 1.0f) {
                    if (player.motionY < 0.0) {
                        final EntityPlayerSP entityPlayerSP3 = player;
                        entityPlayerSP3.motionY *= 0.75;
                    }
                    final EntityPlayerSP entityPlayerSP4 = player;
                    entityPlayerSP4.motionY += 0.001;
                }
            }
        }
        if (e instanceof EventCollide) {
            final EventCollide event4 = (EventCollide)e;
            if (this.mode.is("Verus") && event4.getBlock() instanceof BlockAir && event4.getY() <= this.launchY) {
                event4.setBoundingBox(AxisAlignedBB.fromBounds(event4.getX(), event4.getY(), event4.getZ(), event4.getX() + 1, this.launchY, event4.getZ() + 1));
            }
        }
        if (e instanceof EventUpdate && e.isPre() && this.mode.is("Jartex")) {
            if (LongJump.mc.thePlayer.isRiding()) {
                this.isRiding = true;
                LongJump.mc.gameSettings.keyBindSneak.pressed = true;
            }
            else if (this.isRiding) {
                LongJump.mc.thePlayer.jump();
                this.isRiding = false;
                this.hasJumped = true;
                LongJump.mc.gameSettings.keyBindSneak.pressed = false;
            }
            if (this.hasJumped) {
                if (this.i < this.jartexTicks.getValue()) {
                    final EntityPlayerSP thePlayer8 = LongJump.mc.thePlayer;
                    thePlayer8.motionY += 1.5;
                    LongJump.mc.thePlayer.jumpMovementFactor = 0.1f;
                    ++this.i;
                }
                else if (LongJump.mc.thePlayer.onGround) {
                    if (LongJump.disable.isEnabled()) {
                        this.toggled = false;
                        LongJump.mc.timer.timerSpeed = 1.0f;
                    }
                    else {
                        this.i = 0;
                        this.isRiding = false;
                        this.hasJumped = false;
                    }
                }
            }
        }
        if (e instanceof EventPacket && (((EventPacket)e).getPacket() instanceof C0APacketAnimation || ((EventPacket)e).getPacket() instanceof C03PacketPlayer || ((EventPacket)e).getPacket() instanceof C07PacketPlayerDigging || ((EventPacket)e).getPacket() instanceof C08PacketPlayerBlockPlacement) && this.shouldBlink && this.blink.isEnabled()) {
            if (this.blinkTimer.hasElapsed((long)this.blinkDelay.getValue())) {
                try {
                    for (final Packet packets : this.packetList) {
                        LongJump.mc.getNetHandler().sendPacketNoEvent(packets);
                    }
                    this.packetList.clear();
                    this.crumbs.clear();
                }
                catch (ConcurrentModificationException exception) {
                    exception.printStackTrace();
                }
                this.blinkTimer.reset();
            }
            else {
                e.setCancelled(true);
                this.packetList.add(((EventPacket)e).getPacket());
            }
        }
    }
    
    private int getSlotWithBow() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = LongJump.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBow) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    
    public void selfBow() {
        final TimerUtils fuck = new TimerUtils();
        fuck.reset();
        final int oldSlot = LongJump.mc.thePlayer.inventory.currentItem;
        LongJump.mc.gameSettings.keyBindBack.pressed = false;
        LongJump.mc.gameSettings.keyBindForward.pressed = false;
        LongJump.mc.gameSettings.keyBindRight.pressed = false;
        LongJump.mc.gameSettings.keyBindLeft.pressed = false;
        final Thread thread = new Thread() {
            @Override
            public void run() {
                final int oldSlot = Module.mc.thePlayer.inventory.currentItem;
                ItemStack block = Module.mc.thePlayer.getCurrentEquippedItem();
                if (block != null) {
                    block = null;
                }
                int slot = Module.mc.thePlayer.inventory.currentItem;
                for (short g = 0; g < 9; ++g) {
                    if (Module.mc.thePlayer.inventoryContainer.getSlot(g + 36).getHasStack() && Module.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().getItem() instanceof ItemBow && Module.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack().stackSize != 0 && (block == null || block.getItem() instanceof ItemBow)) {
                        slot = g;
                        block = Module.mc.thePlayer.inventoryContainer.getSlot(g + 36).getStack();
                    }
                }
                Module.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(slot));
                Module.mc.thePlayer.inventory.currentItem = slot;
                final float oldPitch = Module.mc.thePlayer.rotationPitch;
                Module.mc.thePlayer.rotationPitch = -90.0f;
                Module.mc.gameSettings.keyBindUseItem.pressed = true;
                try {
                    Thread.sleep(160L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Module.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(Module.mc.thePlayer.rotationYaw, -90.0f, true));
                Module.mc.gameSettings.keyBindUseItem.pressed = false;
                Module.mc.thePlayer.rotationPitch = oldPitch;
                try {
                    Thread.sleep(180L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LongJump.this.doneBow = true;
                Module.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(oldSlot));
                LongJump.this.ljTimer.reset();
                Module.mc.thePlayer.inventory.currentItem = oldSlot;
            }
        };
        thread.start();
        LongJump.mc.gameSettings.keyBindBack.pressed = false;
        LongJump.mc.gameSettings.keyBindForward.pressed = false;
        LongJump.mc.gameSettings.keyBindRight.pressed = false;
        LongJump.mc.gameSettings.keyBindLeft.pressed = false;
        LongJump.mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(oldSlot));
        LongJump.mc.thePlayer.inventory.currentItem = oldSlot;
    }
    
    static {
        LongJump.disable = new BooleanSetting("AutoDisable", true);
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
}
