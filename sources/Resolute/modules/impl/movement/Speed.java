// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventUpdate;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.block.BlockSlab;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.util.misc.MathUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFood;
import vip.Resolute.events.impl.EventMove;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Speed extends Module
{
    public ModeSetting mode;
    public NumberSetting customHeight;
    public ModeSetting motionMode;
    public NumberSetting watchdogSpeed;
    public BooleanSetting newStrafe;
    public ModeSetting verusMode;
    public NumberSetting vanillaSpeed;
    public NumberSetting mineplexRegSpeed;
    public BooleanSetting watchdogStrafeFix;
    public BooleanSetting watchdogTimerBoost;
    public BooleanSetting dogeSpoof;
    public BooleanSetting disable;
    private double moveSpeed;
    public int stage;
    int ncpStage;
    public boolean reset;
    public boolean doSlow;
    double lastDistance;
    double dist;
    private double nextMotionSpeed;
    double moveSpeed3;
    float speed;
    public double movementSpeed;
    private int verusStage;
    double lastDist;
    public boolean spoofGround;
    int watchdogStage;
    double mineplexMoveSpeed;
    private boolean wasOnGround;
    public static boolean enabled;
    private int stopTicks;
    private float fallDist;
    int jumps;
    
    public boolean isModeSelected() {
        return this.mode.is("Verus");
    }
    
    public boolean isMode2Selected() {
        return this.mode.is("Custom");
    }
    
    public boolean isMode3Selected() {
        return this.mode.is("Mineplex");
    }
    
    public boolean isMode4Selected() {
        return this.mode.is("Hypixel");
    }
    
    public Speed() {
        super("Speed", 48, "Allows you to move faster", Category.MOVEMENT);
        this.mode = new ModeSetting("Mode", "NCP", new String[] { "NCP", "Strafe", "Mineplex Safe", "Mineplex", "Mineplex Fast", "Hypixel", "Verus", "Minemen", "Jartex", "Custom" });
        this.customHeight = new NumberSetting("Custom Height", 0.42, () -> this.mode.is("Custom"), 0.0, 0.42, 0.02);
        this.motionMode = new ModeSetting("Motion", "Low", this::isMode4Selected, new String[] { "Low" });
        this.watchdogSpeed = new NumberSetting("Watchdog Speed", 15.5, this::isMode4Selected, 10.0, 20.0, 0.1);
        this.newStrafe = new BooleanSetting("Watchdog Strafe", false, () -> this.mode.is("Watchdog New") || this.mode.is("Hypixel"));
        this.verusMode = new ModeSetting("Verus Mode", "Float", this::isModeSelected, new String[] { "Float", "Hop", "Port" });
        this.vanillaSpeed = new NumberSetting("Custom Speed", 2.0, this::isMode2Selected, 0.1, 5.0, 0.1);
        this.mineplexRegSpeed = new NumberSetting("Mineplex Regular Speed", 4.0, this::isMode3Selected, 0.1, 10.0, 0.1);
        this.watchdogStrafeFix = new BooleanSetting("Watchdog Strafe Fix", false);
        this.watchdogTimerBoost = new BooleanSetting("Watchdog Timer", false);
        this.dogeSpoof = new BooleanSetting("Doge Spoof", true, () -> this.mode.is("Doge"));
        this.disable = new BooleanSetting("Lagback Check", true);
        this.lastDistance = 0.0;
        this.moveSpeed3 = 0.0;
        this.speed = 0.0f;
        this.lastDist = 0.0;
        this.watchdogStage = 1;
        this.jumps = 0;
        this.addSettings(this.mode, this.customHeight, this.motionMode, this.watchdogSpeed, this.newStrafe, this.verusMode, this.vanillaSpeed, this.mineplexRegSpeed, this.dogeSpoof, this.watchdogStrafeFix, this.watchdogTimerBoost, this.disable);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (Speed.mc.thePlayer != null) {
            this.moveSpeed = MovementUtils.getSpeed();
        }
        Speed.enabled = true;
        this.movementSpeed = 0.0;
        this.ncpStage = 0;
        this.lastDistance = 0.0;
        this.watchdogStage = 2;
        this.moveSpeed3 = 0.0;
        this.doSlow = false;
        this.reset = false;
        this.fallDist = Speed.mc.thePlayer.fallDistance;
        this.nextMotionSpeed = 0.0;
        this.verusStage = 0;
        this.stopTicks = 0;
        Speed.mc.thePlayer.speedInAir = 0.02f;
        Speed.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.speed = 0.0f;
        this.watchdogStage = 2;
        Speed.enabled = false;
        this.doSlow = false;
        this.reset = false;
        this.lastDist = 0.0;
        this.mineplexMoveSpeed = 0.0;
        Speed.mc.thePlayer.speedInAir = 0.02f;
        this.stopTicks = 0;
        Speed.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventPacket) {
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook && this.disable.isEnabled()) {
                Resolute.getNotificationManager().add(new Notification("Flag alert", "Speed was automatically disabled to prevent flags", 4000L, NotificationType.WARNING));
                Speed.enabled = false;
                this.doSlow = false;
                this.reset = false;
                this.lastDist = 0.0;
                Speed.mc.thePlayer.speedInAir = 0.02f;
                Speed.mc.timer.timerSpeed = 1.0f;
                this.toggled = false;
            }
            if (((EventPacket)e).getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition && this.watchdogStrafeFix.isEnabled()) {
                float yaw = Speed.mc.thePlayer.rotationYaw;
                final float pitch = Speed.mc.thePlayer.rotationPitch;
                double offsetX = 0.0;
                double offsetZ = 0.0;
                if (Speed.mc.thePlayer.motionX > 0.0) {
                    offsetX = Speed.mc.thePlayer.motionX / 2.0;
                    yaw = -90.0f;
                }
                if (Speed.mc.thePlayer.motionX < 0.0) {
                    offsetX = Speed.mc.thePlayer.motionX / 2.0;
                    yaw = 90.0f;
                }
                if (Speed.mc.thePlayer.motionZ > 0.0) {
                    offsetZ = Speed.mc.thePlayer.motionZ / 2.0;
                    yaw = 0.0f;
                }
                if (Speed.mc.thePlayer.motionZ < 0.0) {
                    offsetZ = Speed.mc.thePlayer.motionZ / 2.0;
                    yaw = -170.0f;
                }
                ((EventPacket)e).setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Speed.mc.thePlayer.posX + offsetX, Speed.mc.thePlayer.posY, Speed.mc.thePlayer.posZ + offsetZ, yaw, pitch, Speed.mc.thePlayer.onGround));
            }
        }
        if (e instanceof EventMove) {
            final EventMove event = (EventMove)e;
            if (this.mode.is("Strafe")) {
                MovementUtils.setStrafeSpeed(event, Math.max(MovementUtils.getSpeed(), MovementUtils.getBaseMoveSpeed() * 0.98));
            }
            if (this.mode.is("Mineplex")) {
                Speed.mc.timer.timerSpeed = 0.9f;
                if (MovementUtils.isMovingOnGround()) {
                    if (this.mineplexMoveSpeed < 0.5) {
                        this.mineplexMoveSpeed = 0.8;
                    }
                    else {
                        this.mineplexMoveSpeed += 0.5;
                    }
                    event.setY(Speed.mc.thePlayer.motionY = 0.41999998688697815);
                    Speed.mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY + 0.41999998688697815, Speed.mc.thePlayer.posZ, true));
                }
                else if (!MovementUtils.isMoving()) {
                    this.mineplexMoveSpeed = 0.45;
                }
                this.mineplexMoveSpeed = Math.max(this.mineplexMoveSpeed, 0.4);
                this.mineplexMoveSpeed -= this.mineplexMoveSpeed / 44.0;
                this.mineplexMoveSpeed = Math.min(this.mineplexRegSpeed.getValue(), this.mineplexMoveSpeed);
                if (Speed.mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving()) {
                    this.mineplexMoveSpeed = 0.32;
                }
                MovementUtils.setSpeed(event, this.mineplexMoveSpeed);
            }
            if (this.mode.is("Spartan")) {
                if (MovementUtils.isMovingOnGround()) {
                    event.setY(Speed.mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815));
                }
                else if (MovementUtils.isMoving()) {
                    this.movementSpeed = 0.3;
                }
                MovementUtils.setStrafeSpeed(event, this.movementSpeed);
            }
            if (this.mode.is("Watchdog New")) {
                final double baseMoveSpeed = MovementUtils.getBaseSpeedHypixelApplied();
                if (MovementUtils.isMovingOnGround()) {
                    this.wasOnGround = true;
                    double motionY;
                    if (Scaffold.enabled || Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                        motionY = MovementUtils.getJumpHeight();
                    }
                    else {
                        motionY = MovementUtils.getJumpBoostModifier(0.4195);
                    }
                    event.setY(Speed.mc.thePlayer.motionY = motionY);
                    MovementUtils.setStrafeSpeed(event, this.moveSpeed = Math.max(baseMoveSpeed * 1.72, this.lastDist * 1.72));
                    Speed.mc.timer.timerSpeed = 1.0f;
                    this.doSlow = true;
                }
                else if (MovementUtils.isMoving()) {
                    if (this.newStrafe.isEnabled()) {
                        MovementUtils.setStrafeSpeed(event, MovementUtils.getSpeed());
                    }
                    if (this.watchdogTimerBoost.isEnabled()) {
                        Speed.mc.timer.timerSpeed = 1.125f;
                    }
                }
            }
            if (this.mode.is("Hypixel")) {
                final double baseMoveSpeed = MovementUtils.getBaseSpeedHypixelAppliedLow();
                if (MovementUtils.isMovingOnGround()) {
                    this.wasOnGround = true;
                    final double motionY = MovementUtils.getJumpBoostModifier(0.2);
                    event.setY(Speed.mc.thePlayer.motionY = motionY);
                    MovementUtils.setStrafeSpeed(event, this.moveSpeed = Math.max(baseMoveSpeed * (this.watchdogSpeed.getValue() / 10.0), this.lastDist * (this.watchdogSpeed.getValue() / 10.0)));
                    Speed.mc.timer.timerSpeed = 1.0f;
                }
                else if (this.wasOnGround) {
                    final double difference = (0.6556 + 0.02 * MovementUtils.getJumpBoostModifier()) * (this.lastDist - baseMoveSpeed);
                    this.moveSpeed = this.lastDist - difference;
                    this.wasOnGround = false;
                }
                else if (MovementUtils.isMoving()) {
                    this.moveSpeed = MovementUtils.getFriction(this.moveSpeed);
                    if (this.newStrafe.isEnabled() && Speed.mc.thePlayer.fallDistance < 1.0f && MovementUtils.isDistFromGround(1.0)) {
                        MovementUtils.setStrafeSpeed(event, Math.max(MovementUtils.getSpeed(), this.moveSpeed));
                    }
                    if (this.watchdogTimerBoost.isEnabled()) {
                        if (Speed.mc.thePlayer.getHeldItem() == null || (!(Speed.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) && !(Speed.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) || !Speed.mc.thePlayer.isEating()) {
                            Speed.mc.timer.timerSpeed = 1.115f;
                        }
                        else {
                            Speed.mc.timer.timerSpeed = 1.0f;
                        }
                    }
                }
            }
            if (this.mode.is("Verus")) {
                if (this.verusMode.is("Float")) {
                    if (MovementUtils.isMovingOnGround()) {
                        this.movementSpeed = 0.612;
                        event.setY(0.41999998688697815);
                        this.spoofGround = true;
                        this.verusStage = 0;
                    }
                    else if (this.verusStage <= 5) {
                        this.movementSpeed += 0.1;
                        event.setY(0.0);
                        ++this.verusStage;
                    }
                    else {
                        this.movementSpeed = 0.24;
                        this.spoofGround = false;
                    }
                    Speed.mc.thePlayer.motionY = event.getY();
                    MovementUtils.setStrafeSpeed(event, this.movementSpeed - 1.0E-4);
                }
                if (this.verusMode.is("Hop")) {
                    if (MovementUtils.isMovingOnGround()) {
                        this.movementSpeed = 0.612;
                        event.setY(Speed.mc.thePlayer.motionY = 0.41999998688697815);
                    }
                    else {
                        this.movementSpeed = 0.36;
                    }
                    MovementUtils.setStrafeSpeed(event, this.movementSpeed - 1.0E-4);
                }
                if (this.verusMode.is("Port")) {
                    if (MovementUtils.isMovingOnGround()) {
                        this.movementSpeed = 0.512;
                        event.setY(Speed.mc.thePlayer.motionY = 0.41999998688697815);
                        if (!Speed.mc.thePlayer.movementInput.jump) {
                            this.spoofGround = true;
                        }
                    }
                    else if (this.spoofGround) {
                        this.movementSpeed = 0.38;
                        event.setY(Speed.mc.thePlayer.motionY = 0.0);
                        this.spoofGround = false;
                    }
                    MovementUtils.setStrafeSpeed(event, this.movementSpeed - 1.0E-4);
                }
            }
            if (this.mode.is("Mineplex Safe")) {
                final Entity player = Speed.mc.thePlayer;
                final BlockPos pos = new BlockPos(player.posX, player.posY - 1.0, player.posZ);
                final Block block = Speed.mc.theWorld.getBlockState(pos).getBlock();
                Speed.mc.timer.timerSpeed = 1.0f;
                if (MovementUtils.isMovingOnGround()) {
                    event.setY(Speed.mc.thePlayer.motionY = 0.359);
                    this.doSlow = true;
                    this.dist = this.moveSpeed;
                    this.moveSpeed = 0.0;
                }
                else {
                    Speed.mc.timer.timerSpeed = 1.0f;
                    if (this.doSlow) {
                        this.moveSpeed = this.dist + 0.5600000023841858;
                        this.doSlow = false;
                    }
                    else {
                        this.moveSpeed = this.lastDistance * ((this.moveSpeed > 2.2) ? 0.975 : ((this.moveSpeed >= 1.5) ? 0.98 : 0.985));
                    }
                    event.setY(event.getY() - 1.0E-4);
                }
                final double max = 5.0;
                MovementUtils.setStrafeSpeed(event, Math.max(Math.min(this.moveSpeed, max), this.doSlow ? 0.0 : 0.455));
            }
            if (this.mode.is("Custom")) {
                if (MovementUtils.isMovingOnGround()) {
                    ((EventMove)e).setY(Speed.mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(this.customHeight.getValue()));
                }
                MovementUtils.setStrafeSpeed(event, this.vanillaSpeed.getValue());
            }
            if (this.mode.is("NCP") && MovementUtils.isMoving()) {
                MovementUtils.setStrafeSpeed(event, Math.max(MovementUtils.getSpeed(), MovementUtils.getBaseMoveSpeed()));
            }
            if (this.mode.is("NCP Low")) {
                if (MovementUtils.isMoving()) {
                    MovementUtils.setStrafeSpeed(event, Math.max(MovementUtils.getSpeed(), MovementUtils.getBaseMoveSpeed()));
                }
                if (MovementUtils.isMovingOnGround()) {
                    event.setY(Speed.mc.thePlayer.motionY = 0.41999998688697815);
                }
            }
            if (this.mode.is("Velo") && MovementUtils.isMoving() && !MovementUtils.isInLiquid()) {
                MovementUtils.setStrafeSpeed(event, MovementUtils.getBaseSpeedHypixelApplied());
            }
            if (this.mode.is("Watchdoge") && MovementUtils.isMoving()) {
                final double baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
                if (MathUtils.roundToPlace(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                    event.setY(Speed.mc.thePlayer.motionY = 0.31);
                }
                else if (MathUtils.roundToPlace(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.71, 3)) {
                    event.setY(Speed.mc.thePlayer.motionY = 0.04);
                }
                else if (MathUtils.roundToPlace(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.75, 3)) {
                    event.setY(Speed.mc.thePlayer.motionY = -0.2);
                }
                else if (MathUtils.roundToPlace(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.55, 3)) {
                    event.setY(Speed.mc.thePlayer.motionY = -0.19);
                }
                else if (MathUtils.roundToPlace(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                    event.setY(Speed.mc.thePlayer.motionY = -0.2);
                }
                if (MovementUtils.isOnGround() && !this.wasOnGround) {
                    Speed.mc.thePlayer.setPosition(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY + MathUtils.randomNumber(0.0067, 0.0042), Speed.mc.thePlayer.posZ);
                    event.setY(Speed.mc.thePlayer.motionY = MovementUtils.getJumpHeight(0.4000000059604645));
                    this.moveSpeed = baseMoveSpeed * 1.17;
                    this.wasOnGround = true;
                    if (this.watchdogTimerBoost.isEnabled()) {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                }
                else if (this.wasOnGround) {
                    this.wasOnGround = false;
                    final double difference = (0.6556 + 0.02 * MovementUtils.getJumpBoostModifier()) * (this.lastDist - baseMoveSpeed);
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    this.moveSpeed = MovementUtils.getFriction(this.moveSpeed);
                    if (this.watchdogTimerBoost.isEnabled()) {
                        if (Speed.mc.thePlayer.getHeldItem() == null || (!(Speed.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) && !(Speed.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) || !Speed.mc.thePlayer.isEating()) {
                            Speed.mc.timer.timerSpeed = 1.115f;
                        }
                        else {
                            Speed.mc.timer.timerSpeed = 1.0f;
                        }
                    }
                }
                MovementUtils.setStrafeSpeed(event, Math.max(MovementUtils.getSpeed(), this.moveSpeed));
            }
        }
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                final EventMotion event2 = (EventMotion)e;
                if (this.mode.is("Velo") && MovementUtils.isMoving() && !MovementUtils.isInLiquid() && MovementUtils.isOnGround()) {
                    Speed.mc.thePlayer.addVelocity(0.0, 0.39, 0.0);
                }
                if (this.mode.is("Jartex")) {
                    if (Speed.mc.thePlayer.isInWater()) {
                        return;
                    }
                    if (MovementUtils.isMoving()) {
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.jump();
                            Speed.mc.thePlayer.speedInAir = 0.02028f;
                            Speed.mc.timer.timerSpeed = 1.01f;
                        }
                    }
                    else {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                }
                if (this.mode.is("Strafe") && MovementUtils.isMovingOnGround()) {
                    Speed.mc.gameSettings.keyBindJump.pressed = false;
                }
                if (this.mode.is("Watchdog New")) {
                    if (Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isInLava()) {
                        return;
                    }
                    if (this.newStrafe.isEnabled()) {
                        MovementUtils.bypassOffSet(event2);
                    }
                    if (MovementUtils.isMovingOnGround()) {
                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                    }
                    if (event2.isOnGround()) {
                        event2.setY(event2.getY() + 0.015625);
                    }
                    if (Speed.mc.thePlayer.fallDistance < 1.0f) {
                        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                        thePlayer.motionY += 0.005;
                    }
                    if (MovementUtils.isMovingOnGround()) {
                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                    }
                    if (!(Speed.mc.theWorld.getBlockState(new BlockPos(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY - 1.0, Speed.mc.thePlayer.posZ)).getBlock() instanceof BlockSlab) && Speed.mc.thePlayer.motionY < 0.1 && Speed.mc.thePlayer.motionY > -0.25 && Speed.mc.thePlayer.fallDistance < 0.1) {
                        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                        thePlayer2.motionY -= 0.15;
                    }
                    final double x = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                    final double z = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                    this.lastDist = Math.sqrt(x * x + z * z);
                }
                if (this.mode.is("Hypixel")) {
                    if (Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isInLava()) {
                        return;
                    }
                    if (MovementUtils.isMovingOnGround()) {
                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                        if (this.newStrafe.isEnabled()) {
                            event2.setOnGround(false);
                        }
                    }
                    else if (this.newStrafe.isEnabled() && Speed.mc.thePlayer.fallDistance < 1.0f && MovementUtils.isDistFromGround(1.0)) {
                        event2.setOnGround(true);
                    }
                    if (event2.isOnGround()) {
                        event2.setY(event2.getY() + MathUtils.randomNumber(0.0067, 0.0042));
                    }
                    if (Speed.mc.thePlayer.fallDistance < 1.0f && MovementUtils.isMoving()) {
                        final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                        thePlayer3.motionY += 0.015;
                    }
                    final double x = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                    final double z = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                    this.lastDist = Math.sqrt(x * x + z * z);
                }
                if (this.mode.is("Watchdoge")) {
                    if (Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isInLava()) {
                        return;
                    }
                    if (MovementUtils.isMovingOnGround()) {
                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                    }
                    final double x = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                    final double z = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                    this.lastDist = Math.sqrt(x * x + z * z);
                }
            }
            if (this.mode.is("Verus")) {
                final EventMotion event2 = (EventMotion)e;
                Speed.mc.getNetHandler().sendPacketNoEvent(new C0CPacketInput());
                if (this.verusMode.is("Float") || this.verusMode.is("Port")) {
                    event2.setOnGround(Speed.mc.thePlayer.onGround || this.spoofGround);
                }
                if (MovementUtils.isMovingOnGround()) {
                    Speed.mc.gameSettings.keyBindJump.pressed = false;
                }
            }
            if (this.mode.is("NCP") && e.isPre()) {
                if (MovementUtils.isMoving() && !Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (Speed.mc.thePlayer.onGround) {
                        Speed.mc.thePlayer.jump();
                        Speed.mc.timer.timerSpeed = 1.05f;
                        final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                        thePlayer4.motionX *= 1.0529999732971191;
                        final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
                        thePlayer5.motionZ *= 1.0529999732971191;
                        final EntityPlayerSP thePlayer6 = Speed.mc.thePlayer;
                        thePlayer6.motionY *= 0.9750000238418579;
                    }
                    else {
                        Speed.mc.thePlayer.jumpMovementFactor = 0.0265f;
                    }
                }
                else {
                    Speed.mc.timer.timerSpeed = 1.0f;
                }
            }
            if (this.mode.is("NCP Low")) {
                if (Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isInLava()) {
                    return;
                }
                if (MovementUtils.isMovingOnGround()) {
                    Speed.mc.gameSettings.keyBindJump.pressed = false;
                }
                if (!MovementUtils.isOnGround()) {
                    Speed.mc.thePlayer.motionY = -0.4;
                }
                final double x2 = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
                final double z2 = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
                this.lastDist = Math.sqrt(x2 * x2 + z2 * z2);
            }
        }
        if (e instanceof EventUpdate) {
            if (this.mode.is("Strafe") && MovementUtils.isMovingOnGround()) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                Speed.mc.thePlayer.jump();
            }
            if (this.mode.is("Verus") && this.verusMode.is("Port") && MovementUtils.isMovingOnGround()) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
            }
            if (this.mode.is("Minemen") && MovementUtils.isMovingOnGround()) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                Speed.mc.thePlayer.jump();
            }
        }
    }
    
    static {
        Speed.enabled = false;
    }
}
