// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.movement;

import net.augustus.events.EventJump;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventMove;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventUpdate;
import net.augustus.modules.combat.KillAura;
import net.minecraft.util.MathHelper;
import java.util.Arrays;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.entity.Entity;
import net.augustus.utils.SigmaBlockUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.augustus.Augustus;
import net.augustus.events.EventPreMotion;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventPostMotion;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.augustus.utils.SigmaMoveUtils;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Speed extends Module
{
    public StringValue mode;
    public DoubleValue vanillaSpeed;
    public DoubleValue vanillaHeight;
    public DoubleValue dmgSpeed;
    public BooleanValue damageBoost;
    public BooleanValue strafe;
    private int tickCounter;
    private int tickCounter2;
    private int ticks;
    private float speedYaw;
    private boolean wasOnGround;
    public int stage;
    public boolean collided;
    public double stair;
    private double speed;
    private boolean lessSlow;
    public double less;
    private TimeHelper timer;
    private TimeHelper lastCheck;
    private boolean shouldslow;
    private boolean polared;
    private boolean first;
    private double lastDist;
    private int offGround;
    private int motionDelay;
    
    public Speed() {
        super("Speed", new Color(74, 133, 93), Categorys.MOVEMENT);
        this.mode = new StringValue(1, "Mode", this, "GroundStrafe", new String[] { "VOnGround", "VBhop", "LegitHop", "LegitAbuse", "Matrix", "TeleportAbuse", "NCP", "Test", "Verus", "NoCheatMinus", "GamerCheatOG", "GamerCheatBHop", "CheatmineSafe", "CheatmineRage", "NCPRace", "NCPWtf", "NCPLow", "GroundStrafe", "Strafe", "FixedStrafe" });
        this.vanillaSpeed = new DoubleValue(2, "Speed", this, 1.0, 0.0, 10.0, 2);
        this.vanillaHeight = new DoubleValue(3, "Height", this, 1.0, 0.0, 5.0, 2);
        this.dmgSpeed = new DoubleValue(4, "DMGSpeed", this, 1.0, 0.0, 2.0, 2);
        this.damageBoost = new BooleanValue(5, "DamageBoost", this, false);
        this.strafe = new BooleanValue(6, "Strafe", this, false);
        this.ticks = 0;
        this.stage = 0;
        this.collided = false;
        this.stair = 0.0;
        this.speed = 0.0;
        this.lessSlow = false;
        this.less = 0.0;
        this.timer = new TimeHelper();
        this.lastCheck = new TimeHelper();
    }
    
    @Override
    public void onEnable() {
        this.lastDist = 0.0;
        this.first = true;
        final boolean player = Speed.mc.thePlayer == null;
        this.collided = (!player && Speed.mc.thePlayer.isCollidedHorizontally);
        this.lessSlow = false;
        if (Speed.mc.thePlayer != null) {
            this.speed = SigmaMoveUtils.defaultSpeed();
        }
        this.less = 0.0;
        this.stage = 2;
        Speed.mc.getTimer().timerSpeed = 1.0f;
        if (Speed.mc.thePlayer != null) {
            Speed.mc.thePlayer.jumpMovementFactor = 0.02f;
            Speed.mc.thePlayer.setSpeedInAir(0.02f);
            Speed.mc.thePlayer.setSpeedOnGround(0.1f);
        }
        this.tickCounter2 = -2;
        this.tickCounter = 0;
        this.ticks = 0;
    }
    
    @Override
    public void onDisable() {
        Speed.mc.getTimer().timerSpeed = 1.0f;
        if (Speed.mc.thePlayer != null) {
            Speed.mc.thePlayer.jumpMovementFactor = 0.02f;
            Speed.mc.thePlayer.setSpeedInAir(0.02f);
            Speed.mc.thePlayer.setSpeedOnGround(0.1f);
        }
        this.tickCounter2 = -2;
        this.tickCounter = 0;
    }
    
    @EventTarget
    public void onRecv(final EventReadPacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook)e.getPacket();
            if (this.lastCheck.reached(300L)) {
                pac.yaw = Speed.mc.thePlayer.rotationYaw;
                pac.pitch = Speed.mc.thePlayer.rotationPitch;
            }
            this.stage = -4;
            this.lastCheck.reset();
        }
    }
    
    @EventTarget
    public void onEventPostMotion(final EventPostMotion eventPostMotion) {
        if (!Speed.mc.thePlayer.onGround) {
            ++this.offGround;
        }
        else {
            this.offGround = 0;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "CheatmineSafe": {
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.motionY = 0.38;
                    MoveUtil.strafe();
                    break;
                }
                break;
            }
            case "CheatmineRage": {
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.jump();
                    Speed.mc.thePlayer.motionY = 0.35;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        this.speedYaw = ((Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) ? Speed.mm.targetStrafe.moveYaw : Augustus.getInstance().getYawPitchHelper().realYaw);
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "GroundStrafe": {
                this.groundStrafe();
                break;
            }
            case "Strafe": {
                this.strafe();
                break;
            }
            case "FixedStrafe": {
                this.fixedstrafe();
                break;
            }
            case "NCPLow": {
                this.ncplow();
                break;
            }
            case "NCPWtf": {
                this.ncpwtf();
                break;
            }
            case "NCPRace": {
                this.ncprace();
                break;
            }
            case "GamerCheatBHop": {
                this.gamercheatbhop();
                break;
            }
            case "GamerCheatOG": {
                this.gamercheatog();
                break;
            }
            case "NoCheatMinus": {
                this.nocheatminus();
                break;
            }
            case "Verus": {
                this.verus();
                break;
            }
            case "VOnGround": {
                this.vonground();
                break;
            }
            case "VBhop": {
                this.vbhop();
                break;
            }
            case "LegitAbuse": {
                this.legitAbuse();
                break;
            }
            case "TeleportAbuse": {
                this.teleportAbuse();
                break;
            }
            case "Matrix": {
                this.matrix();
                break;
            }
            case "Test": {
                this.test();
                break;
            }
        }
    }
    
    private void groundStrafe() {
        if (Speed.mc.thePlayer.onGround) {
            Speed.mc.thePlayer.jump();
            MoveUtil.strafe();
        }
    }
    
    private void strafe() {
        if (Speed.mc.thePlayer.onGround) {
            Speed.mc.thePlayer.jump();
        }
        MoveUtil.strafe();
    }
    
    private void fixedstrafe() {
        if (Speed.mc.thePlayer.onGround) {
            Speed.mc.thePlayer.jump();
        }
        MoveUtil.setSpeed((float)SigmaMoveUtils.defaultSpeed());
    }
    
    private void ncpwtf() {
        final boolean hasSpeed = Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed);
        double speedAmplifier = 0.0;
        if (hasSpeed) {
            speedAmplifier = Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }
        MoveUtil.strafe();
        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
        thePlayer.motionX *= 1.0045 + speedAmplifier * 0.019999999552965164;
        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
        thePlayer2.motionZ *= 1.0045 + speedAmplifier * 0.019999999552965164;
        final boolean pushDown = true;
        if (Speed.mc.thePlayer.onGround && this.isMoving() && !Speed.mc.gameSettings.keyBindJump.pressed) {
            final float boost = (float)(speedAmplifier * 0.06499999761581421);
            Speed.mc.thePlayer.jump();
            if (!pushDown) {
                return;
            }
            Speed.mc.timer.timerSpeed = 1.405f;
            MoveUtil.strafe(((Speed.mc.thePlayer.ticksExisted % 10 > 7) ? 0.4f : 0.325f) + boost);
            final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
            thePlayer3.motionX *= 1.01 + speedAmplifier * 0.17499999701976776;
            final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
            thePlayer4.motionZ *= 1.01 + speedAmplifier * 0.17499999701976776;
            Speed.mc.thePlayer.cameraPitch = 0.0f;
            Speed.mc.thePlayer.cameraYaw = 0.0f;
        }
        else if (!Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.motionY > 0.3 && !Speed.mc.gameSettings.keyBindJump.pressed) {
            Speed.mc.timer.timerSpeed = 0.85f;
            Speed.mc.thePlayer.motionY = -0.42;
            final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
            thePlayer5.posY -= 0.45;
            Speed.mc.thePlayer.cameraPitch = 0.0f;
        }
        Speed.mc.thePlayer.stepHeight = 0.5f;
        MoveUtil.strafe();
    }
    
    private void ncprace() {
        if (Speed.mc.thePlayer.onGround) {
            Speed.mc.thePlayer.jump();
            Speed.mc.timer.timerSpeed = 1.2f;
            MoveUtil.multiplyXZ(1.0708);
            final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
            thePlayer.moveStrafing *= 2.0f;
            return;
        }
        Speed.mc.timer.timerSpeed = 0.98f;
        Speed.mc.thePlayer.jumpMovementFactor = 0.0265f;
    }
    
    private void gamercheatbhop() {
        MoveUtil.setSpeed(0.6f);
        if (Speed.mc.thePlayer.isCollidedVertically) {
            MoveUtil.setSpeed(0.2f);
            Speed.mc.thePlayer.motionY = 0.35;
        }
    }
    
    private void gamercheatog() {
        MoveUtil.setSpeed(0.56f);
        Speed.mc.thePlayer.motionY = 0.0;
        if (Speed.mc.thePlayer.ticksExisted % 3 == 0) {
            final double d = Speed.mc.thePlayer.posY - 1.0E-10;
            Speed.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Speed.mc.thePlayer.posX, d, Speed.mc.thePlayer.posZ, true));
        }
        final double y1 = Speed.mc.thePlayer.posY + 1.0E-10;
        Speed.mc.thePlayer.setPosition(Speed.mc.thePlayer.posX, y1, Speed.mc.thePlayer.posZ);
    }
    
    private void nocheatminus() {
        if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f) {
            this.speed = SigmaMoveUtils.defaultSpeed();
        }
        if (this.stage == 1 && Speed.mc.thePlayer.isCollidedVertically && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
            this.speed = 0.25 + SigmaMoveUtils.defaultSpeed() - 0.01;
        }
        else if (!SigmaBlockUtils.isInLiquid() && this.stage == 2 && Speed.mc.thePlayer.isCollidedVertically && SigmaMoveUtils.isOnGround(0.001) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
            Speed.mc.thePlayer.motionY = 0.4;
            Speed.mc.thePlayer.jump();
            this.speed *= 2.149;
        }
        else if (this.stage == 3) {
            final double difference = 0.66 * (this.lastDist - SigmaMoveUtils.defaultSpeed());
            this.speed = this.lastDist - difference;
        }
        else {
            final List<AxisAlignedBB> collidingList = Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0));
            if ((collidingList.size() > 0 || Speed.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                if (1.35 * SigmaMoveUtils.defaultSpeed() - 0.01 > this.speed) {
                    this.stage = 0;
                }
                else {
                    this.stage = ((Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                }
            }
            this.speed = this.lastDist - this.lastDist / 159.0;
        }
        this.speed = Math.max(this.speed, SigmaMoveUtils.defaultSpeed());
        if (this.stage > 0) {
            if (SigmaBlockUtils.isInLiquid()) {
                this.speed = 0.1;
            }
            MoveUtil.setSpeed((float)this.speed);
        }
        if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
            ++this.stage;
        }
    }
    
    private void verus() {
        MoveUtil.setSpeed2(0.2919999957084656);
        if (this.damageBoost.getBoolean() && Speed.mc.thePlayer.hurtTime != 0 && Speed.mc.thePlayer.fallDistance < 3.0f) {
            MoveUtil.setSpeed2((float)this.dmgSpeed.getValue());
        }
        else {
            MoveUtil.setSpeed2(0.2919999957084656);
        }
        if (this.canJump()) {
            Speed.mc.thePlayer.jump();
        }
        else {
            Speed.mc.thePlayer.jumpMovementFactor = 0.1f;
        }
    }
    
    private void vonground() {
        if (this.isMoving()) {
            MoveUtil.setSpeed((float)(0.1 * this.vanillaSpeed.getValue()), this.strafe.getBoolean());
        }
    }
    
    private void vbhop() {
        if (this.canJump()) {
            Speed.mc.thePlayer.motionY = Speed.mc.thePlayer.jumpMovementFactor * this.vanillaHeight.getValue();
            if (this.strafe.getBoolean()) {
                MoveUtil.strafe();
            }
        }
        else if (this.isMoving()) {
            MoveUtil.setSpeed((float)(0.1 * this.vanillaSpeed.getValue()), this.strafe.getBoolean());
        }
    }
    
    private void legitAbuse() {
        if (this.isMoving()) {
            if (Speed.mc.thePlayer.onGround) {
                Speed.mc.thePlayer.jump();
            }
            final KeyBinding[] gameSettings = { Speed.mc.gameSettings.keyBindForward, Speed.mc.gameSettings.keyBindRight, Speed.mc.gameSettings.keyBindBack, Speed.mc.gameSettings.keyBindLeft };
            final int[] down = { 0 };
            Arrays.stream(gameSettings).forEach(keyBinding -> down[0] += (keyBinding.isKeyDown() ? 1 : 0));
            final boolean active = down[0] == 1;
            if (!active) {
                return;
            }
            final double increase = Speed.mc.thePlayer.onGround ? 0.0026000750109401644 : 5.199896488849598E-4;
            final double yaw = MoveUtil.direction();
            final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
            thePlayer.motionX += -MathHelper.sin((float)yaw) * increase;
            final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
            thePlayer2.motionZ += MathHelper.cos((float)yaw) * increase;
        }
    }
    
    private void teleportAbuse() {
        if (Speed.mc.thePlayer.onGround) {
            Speed.mc.thePlayer.jump();
            if (!this.first) {
                Speed.mc.timer.timerSpeed = 30.0f;
            }
            else {
                Speed.mc.timer.timerSpeed = 5.0f;
            }
            this.first = false;
        }
        else {
            Speed.mc.timer.timerSpeed = 0.3f;
        }
    }
    
    private void matrix() {
        ++this.tickCounter;
        if (Speed.mc.thePlayer.motionX == 0.0 && Speed.mc.thePlayer.motionZ == 0.0) {
            return;
        }
        if (MoveUtil.isMoving()) {
            return;
        }
        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
        thePlayer.motionY -= 0.009998999536037445;
        if (Speed.mc.thePlayer.onGround) {
            this.tickCounter = 0;
            MoveUtil.strafeMatrix();
        }
        else if (Speed.mc.thePlayer.movementInput.moveForward > 0.0f && Speed.mc.thePlayer.movementInput.moveStrafe != 0.0f) {
            Speed.mc.thePlayer.setSpeedInAir(0.02f);
        }
        else {
            Speed.mc.thePlayer.setSpeedInAir(0.0208f);
        }
    }
    
    private void test() {
        if (Speed.mc.thePlayer.onGround) {
            this.tickCounter = 0;
        }
        Label_0153: {
            if (MoveUtil.isMoving()) {
                final KillAura killAura = Speed.mm.killAura;
                if (KillAura.target == null && !Speed.mm.longJump.isToggled()) {
                    if (!Speed.mc.thePlayer.isUsingItem()) {
                        Speed.mc.getTimer().timerSpeed = 1.23f;
                        break Label_0153;
                    }
                    if (Speed.mc.thePlayer.isBlocking()) {
                        Speed.mc.getTimer().timerSpeed = 1.23f;
                        break Label_0153;
                    }
                    if (Speed.mc.getTimer().timerSpeed > 1.0f) {
                        Speed.mc.getTimer().timerSpeed = 1.0f;
                    }
                    break Label_0153;
                }
            }
            if (Speed.mc.getTimer().timerSpeed > 1.0f) {
                Speed.mc.getTimer().timerSpeed = 1.0f;
            }
        }
        if (Speed.mc.thePlayer.hurtTime > 8 && !Speed.mm.longJump.isToggled() && !Speed.mc.thePlayer.isBurning() && Speed.mc.thePlayer.fallDistance < 2.0f && !Speed.mc.thePlayer.isPotionActive(Potion.wither) && !Speed.mc.thePlayer.isPotionActive(Potion.poison)) {
            MoveUtil.addSpeed(0.4000000059604645, false);
        }
        ++this.tickCounter;
    }
    
    private void ncplow() {
        if (this.isMoving()) {
            if (Speed.mc.gameSettings.keyBindJump.pressed) {
                return;
            }
            if (Speed.mc.thePlayer.onGround) {
                ++this.motionDelay;
                this.motionDelay %= 3;
                if (this.motionDelay == 0) {
                    final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                    thePlayer.motionY += 0.18000000715255737;
                    final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                    thePlayer2.motionX *= 1.2000000476837158;
                    final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                    thePlayer3.motionZ *= 1.2000000476837158;
                }
            }
            if (!Speed.mc.thePlayer.onGround) {
                final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                thePlayer4.motionX *= 1.0499999523162842;
                final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
                thePlayer5.motionZ *= 1.0499999523162842;
            }
            Speed.mc.thePlayer.speedInAir = 0.022f;
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        final String var2 = this.mode.getSelected();
        byte var3 = -1;
        switch (var2.hashCode()) {
            case 77115: {
                if (var2.equals("NCP")) {
                    var3 = 0;
                    break;
                }
                break;
            }
        }
        switch (var3) {
            case 0: {
                if (MoveUtil.isMoving()) {
                    if (Speed.mc.thePlayer.onGround) {
                        Speed.mc.thePlayer.jump();
                        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                        thePlayer.motionX *= 1.01;
                        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                        thePlayer2.motionZ *= 1.01;
                        Speed.mc.thePlayer.setSpeedInAir(0.022f);
                    }
                    final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                    thePlayer3.motionY -= 9.9999E-4;
                    MoveUtil.strafe();
                }
                else {
                    Speed.mc.thePlayer.motionX = 0.0;
                    Speed.mc.thePlayer.motionZ = 0.0;
                }
                Speed.mc.getTimer().timerSpeed = 1.0865f;
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "Matrix2":
            case "LegitHop":
            case "Matrix": {
                if (this.isMoving()) {
                    Speed.mc.thePlayer.movementInput.jump = true;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (Speed.mc.thePlayer.fallDistance > 4.0f && !this.mode.getSelected().equals("LegitHop")) {
            eventMove.setCanceled(true);
        }
        if (!BlockUtil.isScaffoldToggled()) {
            float yaw = 0.0f;
            Label_0140: {
                if (Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) {
                    yaw = Speed.mm.targetStrafe.moveYaw;
                }
                else {
                    if (Speed.mm.killAura.isToggled()) {
                        final KillAura killAura = Speed.mm.killAura;
                        if (KillAura.target != null && Speed.mm.killAura.moveFix.getBoolean()) {
                            yaw = Speed.mc.thePlayer.rotationYaw;
                            break Label_0140;
                        }
                    }
                    yaw = Augustus.getInstance().getYawPitchHelper().realYaw;
                }
            }
            eventMove.setYaw(yaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!BlockUtil.isScaffoldToggled()) {
            float yaw = 0.0f;
            Label_0104: {
                if (Speed.mm.targetStrafe.target != null && Speed.mm.targetStrafe.isToggled()) {
                    yaw = Speed.mm.targetStrafe.moveYaw;
                }
                else {
                    if (Speed.mm.killAura.isToggled()) {
                        final KillAura killAura = Speed.mm.killAura;
                        if (KillAura.target != null && Speed.mm.killAura.moveFix.getBoolean()) {
                            yaw = Speed.mc.thePlayer.rotationYaw;
                            break Label_0104;
                        }
                    }
                    yaw = Augustus.getInstance().getYawPitchHelper().realYaw;
                }
            }
            eventJump.setYaw(yaw);
        }
    }
    
    private boolean isMoving() {
        return Speed.mc.thePlayer.moveForward != 0.0f || (Speed.mc.thePlayer.moveStrafing != 0.0f && !Speed.mc.thePlayer.isCollidedHorizontally);
    }
    
    private boolean canJump() {
        return this.isMoving() && Speed.mc.thePlayer.onGround;
    }
}
