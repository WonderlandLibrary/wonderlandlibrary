// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.Tenacity;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.potion.Potion;
import dev.tenacity.event.impl.player.PlayerMoveUpdateEvent;
import dev.tenacity.module.impl.combat.TargetStrafe;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import dev.tenacity.event.impl.player.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import java.util.concurrent.ThreadLocalRandom;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Speed extends Module
{
    private final ModeSetting mode;
    private final ModeSetting watchdogMode;
    private final ModeSetting verusMode;
    private final ModeSetting viperMode;
    private final BooleanSetting autoDisable;
    private final NumberSetting groundSpeed;
    private final NumberSetting timer;
    private final NumberSetting vanillaSpeed;
    private final TimerUtil timerUtil;
    private final float r;
    private double speed;
    private double lastDist;
    private float speedChangingDirection;
    private int stage;
    private boolean strafe;
    private boolean wasOnGround;
    private boolean setTimer;
    private double moveSpeed;
    private int inAirTicks;
    
    public Speed() {
        super("Speed", "Speed", Category.MOVEMENT, "Makes you go faster");
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Watchdog", "Strafe", "Matrix", "HurtTime", "Vanilla", "BHop", "Verus", "Viper", "Vulcan", "Zonecraft", "Heatseeker", "Mineland" });
        this.watchdogMode = new ModeSetting("Watchdog Mode", "Hop", new String[] { "Hop", "New Hop", "Dev", "Low Hop", "Ground" });
        this.verusMode = new ModeSetting("Verus Mode", "Normal", new String[] { "Low", "Normal" });
        this.viperMode = new ModeSetting("Viper Mode", "Normal", new String[] { "High", "Normal" });
        this.autoDisable = new BooleanSetting("Auto Disable", false);
        this.groundSpeed = new NumberSetting("Ground Speed", 2.0, 5.0, 1.0, 0.1);
        this.timer = new NumberSetting("Timer", 1.0, 5.0, 1.0, 0.1);
        this.vanillaSpeed = new NumberSetting("Speed", 1.0, 10.0, 1.0, 0.1);
        this.timerUtil = new TimerUtil();
        this.r = ThreadLocalRandom.current().nextFloat();
        this.setTimer = true;
        this.watchdogMode.addParent(this.mode, modeSetting -> modeSetting.is("Watchdog"));
        this.verusMode.addParent(this.mode, modeSetting -> modeSetting.is("Verus"));
        this.viperMode.addParent(this.mode, modeSetting -> modeSetting.is("Viper"));
        this.groundSpeed.addParent(this.watchdogMode, modeSetting -> modeSetting.is("Ground") && this.mode.is("Watchdog"));
        this.vanillaSpeed.addParent(this.mode, modeSetting -> modeSetting.is("Vanilla") || modeSetting.is("BHop"));
        this.addSettings(this.mode, this.vanillaSpeed, this.watchdogMode, this.verusMode, this.viperMode, this.autoDisable, this.groundSpeed, this.timer);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        this.setSuffix(this.mode.getMode());
        if (this.setTimer) {
            Speed.mc.timer.timerSpeed = this.timer.getValue().floatValue();
        }
        final double distX = e.getX() - Speed.mc.thePlayer.prevPosX;
        final double distZ = e.getZ() - Speed.mc.thePlayer.prevPosZ;
        this.lastDist = Math.hypot(distX, distZ);
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Watchdog": {
                final String mode2 = this.watchdogMode.getMode();
                switch (mode2) {
                    case "New Hop": {
                        if (!e.isPre()) {
                            break;
                        }
                        if (Speed.mc.thePlayer.onGround) {
                            if (MovementUtils.isMoving()) {
                                Speed.mc.thePlayer.jump();
                                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.6);
                                this.inAirTicks = 0;
                                break;
                            }
                            break;
                        }
                        else {
                            ++this.inAirTicks;
                            if (this.inAirTicks <= 2) {
                                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.16);
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    case "Hop":
                    case "Low Hop":
                    case "Dev": {
                        if (e.isPre() && MovementUtils.isMoving() && Speed.mc.thePlayer.fallDistance < 1.0f && Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.jump();
                            break;
                        }
                        break;
                    }
                }
                break;
            }
            case "Heatseeker": {
                if (!e.isPre() || !Speed.mc.thePlayer.onGround) {
                    break;
                }
                if (this.timerUtil.hasTimeElapsed(300L, true)) {
                    this.strafe = !this.strafe;
                }
                if (this.strafe) {
                    MovementUtils.setSpeed(1.5);
                    break;
                }
                break;
            }
            case "Mineland": {
                if (!e.isPre()) {
                    break;
                }
                ++this.stage;
                if (this.stage == 1) {
                    Speed.mc.thePlayer.motionY = 0.2;
                }
                if (Speed.mc.thePlayer.onGround && this.stage > 1) {
                    MovementUtils.setSpeed(0.5);
                }
                if (this.stage % 14 == 0) {
                    this.stage = 0;
                    break;
                }
                break;
            }
            case "Vulcan": {
                if (!e.isPre()) {
                    break;
                }
                if (Speed.mc.thePlayer.onGround) {
                    if (MovementUtils.isMoving()) {
                        Speed.mc.thePlayer.jump();
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.6);
                        this.inAirTicks = 0;
                        break;
                    }
                    break;
                }
                else {
                    ++this.inAirTicks;
                    if (this.inAirTicks == 1) {
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.16);
                        break;
                    }
                    break;
                }
                break;
            }
            case "Zonecraft": {
                if (!e.isPre()) {
                    break;
                }
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.jump();
                    MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.8);
                    this.stage = 0;
                    break;
                }
                if (this.stage == 0 && !Speed.mc.thePlayer.isCollidedHorizontally) {
                    Speed.mc.thePlayer.motionY = -0.4;
                }
                ++this.stage;
                break;
            }
            case "Matrix": {
                if (MovementUtils.isMoving()) {
                    if (Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.motionY < 0.003) {
                        Speed.mc.thePlayer.jump();
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                    if (Speed.mc.thePlayer.motionY > 0.003) {
                        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                        thePlayer.motionX *= this.speed;
                        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                        thePlayer2.motionZ *= this.speed;
                        Speed.mc.timer.timerSpeed = 1.05f;
                    }
                    this.speed = 1.0011999607086182;
                    break;
                }
                break;
            }
            case "HurtTime": {
                if (!MovementUtils.isMoving()) {
                    break;
                }
                if (Speed.mc.thePlayer.hurtTime <= 0) {
                    final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                    thePlayer3.motionX *= 1.0010000467300415;
                    final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                    thePlayer4.motionZ *= 1.0010000467300415;
                }
                else {
                    final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
                    thePlayer5.motionX *= 1.0293999910354614;
                    final EntityPlayerSP thePlayer6 = Speed.mc.thePlayer;
                    thePlayer6.motionZ *= 1.0293999910354614;
                }
                if (Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.motionY < 0.003) {
                    Speed.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "Vanilla": {
                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(this.vanillaSpeed.getValue() / 4.0);
                    break;
                }
                break;
            }
            case "BHop": {
                if (!MovementUtils.isMoving()) {
                    break;
                }
                MovementUtils.setSpeed(this.vanillaSpeed.getValue() / 4.0);
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "Verus": {
                final String mode3 = this.verusMode.getMode();
                switch (mode3) {
                    case "Low": {
                        if (!e.isPre()) {
                            break;
                        }
                        if (MovementUtils.isMoving()) {
                            if (Speed.mc.thePlayer.onGround) {
                                Speed.mc.thePlayer.jump();
                                this.wasOnGround = true;
                            }
                            else if (this.wasOnGround) {
                                if (!Speed.mc.thePlayer.isCollidedHorizontally) {
                                    Speed.mc.thePlayer.motionY = -0.0784000015258789;
                                }
                                this.wasOnGround = false;
                            }
                            MovementUtils.setSpeed(0.33);
                            break;
                        }
                        final EntityPlayerSP thePlayer7 = Speed.mc.thePlayer;
                        final EntityPlayerSP thePlayer8 = Speed.mc.thePlayer;
                        final double n4 = 0.0;
                        thePlayer8.motionZ = n4;
                        thePlayer7.motionX = n4;
                        break;
                    }
                    case "Normal": {
                        if (!e.isPre()) {
                            break;
                        }
                        if (!MovementUtils.isMoving()) {
                            MovementUtils.setSpeed(0.0);
                            break;
                        }
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.jump();
                            MovementUtils.setSpeed(0.48);
                            break;
                        }
                        MovementUtils.setSpeed(MovementUtils.getSpeed());
                        break;
                    }
                }
                break;
            }
            case "Viper": {
                final String mode4 = this.viperMode.getMode();
                switch (mode4) {
                    case "High": {
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.motionY = 0.7;
                            break;
                        }
                        break;
                    }
                    case "Normal": {
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.motionY = 0.42;
                            break;
                        }
                        break;
                    }
                }
                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.2);
                break;
            }
            case "Strafe": {
                if (!e.isPre() || !MovementUtils.isMoving()) {
                    break;
                }
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.jump();
                    break;
                }
                MovementUtils.setSpeed(MovementUtils.getSpeed());
                break;
            }
        }
    }
    
    @Override
    public void onMoveEvent(final MoveEvent e) {
        if (this.mode.is("Watchdog")) {
            final String mode = this.watchdogMode.getMode();
            switch (mode) {
                case "Ground": {
                    this.strafe = !this.strafe;
                    if (Speed.mc.thePlayer.onGround && MovementUtils.isMoving() && Speed.mc.theWorld.getBlockState(new BlockPos(Speed.mc.thePlayer.posX + e.getX(), Speed.mc.thePlayer.posY, Speed.mc.thePlayer.posZ + e.getZ())).getBlock() == Blocks.air && !Speed.mc.thePlayer.isCollidedHorizontally && !Step.isStepping) {
                        if (this.strafe || this.groundSpeed.getValue() >= 1.6) {
                            PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Speed.mc.thePlayer.posX + e.getX(), Speed.mc.thePlayer.posY, Speed.mc.thePlayer.posZ + e.getZ(), true));
                        }
                        e.setSpeed(MovementUtils.getBaseMoveSpeed() * this.groundSpeed.getValue());
                        break;
                    }
                    break;
                }
                case "Low Hop": {
                    if (!MovementUtils.isMoving()) {
                        break;
                    }
                    if (Speed.mc.thePlayer.onGround) {
                        this.inAirTicks = 0;
                    }
                    else {
                        ++this.inAirTicks;
                    }
                    if (this.inAirTicks == 5) {
                        e.setY(Speed.mc.thePlayer.motionY = -0.19);
                        break;
                    }
                    break;
                }
            }
        }
        TargetStrafe.strafe(e);
    }
    
    @Override
    public void onPlayerMoveUpdateEvent(final PlayerMoveUpdateEvent e) {
        if (this.mode.is("Watchdog") && (this.watchdogMode.is("Hop") || this.watchdogMode.is("Dev") || this.watchdogMode.is("Low Hop")) && Speed.mc.thePlayer.fallDistance < 1.0f && !Speed.mc.thePlayer.isPotionActive(Potion.jump)) {
            if (MovementUtils.isMoving()) {
                final String mode = this.watchdogMode.getMode();
                switch (mode) {
                    case "Low Hop":
                    case "Hop": {
                        if (Speed.mc.thePlayer.onGround) {
                            this.speed = 1.5;
                        }
                        this.speed -= 0.025;
                        e.applyMotion(MovementUtils.getBaseMoveSpeed() * this.speed, 0.55f);
                        break;
                    }
                    case "Dev": {
                        if (Speed.mc.thePlayer.onGround) {
                            this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 2.1475 * 0.76;
                            this.wasOnGround = true;
                        }
                        else if (this.wasOnGround) {
                            this.moveSpeed = this.lastDist - 0.81999 * (this.lastDist - MovementUtils.getBaseMoveSpeed());
                            this.moveSpeed *= 1.0989010989010988;
                            this.wasOnGround = false;
                        }
                        else {
                            this.moveSpeed -= (TargetStrafe.canStrafe() ? (this.lastDist / 100.0) : (this.lastDist / 150.0));
                        }
                        if (Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isInLava()) {
                            this.speed = MovementUtils.getBaseMoveSpeed() * 0.25;
                        }
                        else {
                            this.speed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
                        }
                        e.applyMotion(this.speed, 0.6f);
                        break;
                    }
                }
            }
            else {
                e.applyMotion(0.0, 0.0f);
            }
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && this.autoDisable.isEnabled()) {
            NotificationManager.post(NotificationType.WARNING, "Flag Detector", "Speed disabled due to " + ((Speed.mc.thePlayer == null || Speed.mc.thePlayer.ticksExisted < 5) ? "world change" : "lagback"), 1.5f);
            this.toggleSilent();
        }
    }
    
    public boolean shouldPreventJumping() {
        return Tenacity.INSTANCE.isEnabled(Speed.class) && MovementUtils.isMoving() && (!this.mode.is("Watchdog") || !this.watchdogMode.is("Ground"));
    }
    
    @Override
    public void onEnable() {
        this.speed = 1.5;
        this.timerUtil.reset();
        if (Speed.mc.thePlayer != null) {
            this.wasOnGround = Speed.mc.thePlayer.onGround;
        }
        this.inAirTicks = 0;
        this.moveSpeed = 0.0;
        this.stage = 0;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        Speed.mc.thePlayer.motionX = 0.0;
        Speed.mc.thePlayer.motionZ = 0.0;
        super.onDisable();
    }
}
