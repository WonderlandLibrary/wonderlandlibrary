/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import me.AquaVit.liquidSense.API.LongJumpAPI;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "LongJump", description = "Allows you to jump further.", category = ModuleCategory.MOVEMENT)
public class LongJump extends Module {

    private final ListValue modeValue = new ListValue("Mode", new String[] {"NCP", "AACv1", "AACv2", "AACv3", "Mineplex", "Mineplex2", "Mineplex3","WatchDog"}, "NCP");
    private final FloatValue ncpBoostValue = new FloatValue("NCPBoost", 4.25F, 1F, 10F);
    private final IntegerValue delay = new IntegerValue("Delay", 200, 10, 1000);
    private final IntegerValue dns = new IntegerValue("Distance", 10, 1, 10);
    private final BoolValue autoJumpValue = new BoolValue("AutoJump", false);
    private final BoolValue autoclose = new BoolValue("AutoClose", false);

    private boolean jumped;
    private boolean canBoost;
    private boolean teleported;
    private boolean canMineplexBoost;
    private double lastDif;
    private double moveSpeed;
    private int stage;
    private int groundTicks;
    private double speed;
    private int downTicks;
    private double lastDist;
    private final MSTimer delayTimer = new MSTimer();

    public double getDownMotion(int stage) {
        double[] motion = {-0.012255154820464102
                , -0.029804124002464114
                , -0.04384329955726414
                , -0.05507464016846417
                , -0.0640597127913122
                , -0.07124777099670104
                , -0.07699821764670045
                , -0.08159857503525064
                , -0.08527886100093134
                , -0.08822308981734832
                , -0.09057847290557984
                , -0.09246277940424341
                , -0.09397022462563696
                , -0.09517618082072195
                , -0.09614094579116605
                , -0.09691275777902224
                , -0.0975302073785079
                , -0.098024167065457};

        if (stage <= motion.length) {
            return motion[stage];
        } else {
            return mc.thePlayer.motionY;
        }
    }

    @Override
    public void onEnable() {
        this.lastDif = 0.0;
        this.moveSpeed = 0.0;
        this.stage = 0;
        this.groundTicks = 1;
        if (modeValue.get().equalsIgnoreCase("watchdog")){
            mc.timer.timerSpeed = 0.6F;
            delayTimer.reset();
            speed = MovementUtils.LongJumpMoveSpeed();
            downTicks = 0;
        }

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event) {

        if(jumped) {
            final String mode = modeValue.get();

            if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                jumped = false;
                canMineplexBoost = false;

                if (mode.equalsIgnoreCase("NCP")) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                return;
            }

            switch (mode.toLowerCase()) {
                case "ncp":
                    MovementUtils.strafe(MovementUtils.getSpeed() * (canBoost ? ncpBoostValue.get() : 1F));
                    canBoost = false;
                    break;
                case "aacv1":
                    mc.thePlayer.motionY += 0.05999D;
                    MovementUtils.strafe(MovementUtils.getSpeed() * 1.08F);
                    break;
                case "aacv2":
                case "mineplex3":
                    mc.thePlayer.jumpMovementFactor = 0.09F;
                    mc.thePlayer.motionY += 0.0132099999999999999999999999999;
                    mc.thePlayer.jumpMovementFactor = 0.08F;
                    MovementUtils.strafe();
                    break;
                case "aacv3":
                    final EntityPlayerSP player = mc.thePlayer;

                    if(player.fallDistance > 0.5F && !teleported) {
                        double value = 3;
                        EnumFacing horizontalFacing = player.getHorizontalFacing();
                        double x = 0;
                        double z = 0;
                        switch(horizontalFacing) {
                            case NORTH:
                                z = -value;
                                break;
                            case EAST:
                                x = +value;
                                break;
                            case SOUTH:
                                z = +value;
                                break;
                            case WEST:
                                x = -value;
                                break;
                        }

                        player.setPosition(player.posX + x, player.posY, player.posZ + z);
                        teleported = true;
                    }
                    break;
                case "mineplex":
                    mc.thePlayer.motionY += 0.0132099999999999999999999999999;
                    mc.thePlayer.jumpMovementFactor = 0.08F;
                    MovementUtils.strafe();
                    break;
                case "mineplex2":
                    if(!canMineplexBoost)
                        break;

                    mc.thePlayer.jumpMovementFactor = 0.1F;

                    if(mc.thePlayer.fallDistance > 1.5F) {
                        mc.thePlayer.jumpMovementFactor = 0F;
                        mc.thePlayer.motionY = -10F;
                    }
                    MovementUtils.strafe();
                    break;
            }
        }

        if(autoJumpValue.get() && mc.thePlayer.onGround && MovementUtils.isMoving()) {
            jumped = true;
            mc.thePlayer.jump();
        }
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        final String mode = modeValue.get();

        if (mode.equalsIgnoreCase("mineplex3")) {
            if(mc.thePlayer.fallDistance != 0)
                mc.thePlayer.motionY += 0.037;
        } else if (mode.equalsIgnoreCase("ncp") && !MovementUtils.isMoving() && jumped) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
            event.zeroXZ();
        } else if (mode.equalsIgnoreCase("watchdog")){
            if (!delayTimer.isDelayComplete(delay.get())) {
                event.setX(0);
                event.setZ(0);
            }

            if (delayTimer.isDelayComplete(delay.get())) {

                if (stage == 1 && MovementUtils.isMoving()) {
                    stage = 2;
                    speed = (1.38 * MovementUtils.LongJumpMoveSpeed() - 0.01) / 1.6;
                } else if (stage == 2 && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                    stage = 3;
                    event.setX(0);
                    event.setZ(0);
                    event.setY(mc.thePlayer.motionY = (0.423 + MovementUtils.LongJumpEffect() * 0.09));
                    speed *= 2.149;
                } else if (stage == 3) {
                    stage = 4;
                    final double difference = 0.64 * (lastDist - MovementUtils.LongJumpMoveSpeed());
                    speed = (lastDist - difference) * 1.92;
                } else {
                    if (mc.thePlayer.motionY < 0 && mc.thePlayer.fallDistance <= 1.2) {
                        mc.thePlayer.motionY = getDownMotion(downTicks);
                        downTicks++;
                    }
                    speed = lastDist - lastDist / 159.0;
                }
                speed = Math.max(speed, MovementUtils.LongJumpMoveSpeed());
                if (speed > 0.7) {
                    speed = 0.7 - Math.random() / 50;
                }
                MovementUtils.setMoveSpeed(event, speed * 0.1 * dns.get());
            }
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event){
        if(modeValue.get().equalsIgnoreCase("watchdog")) {
            switch (event.getEventState()) {
                case PRE:
                    if ((mc.thePlayer.isCollidedVertically || mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getCollisionBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0)) {
                        if (stage == 0) {
                            mc.thePlayer.posY += 1.0E-10D;
                            stage = 1;
                            downTicks = 0;
                        } else if (stage == 4) {
                            if (autoclose.get())
                                this.setState(false);
                        }
                    }
                    break;
                case POST:
                    final double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    final double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
            }
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        jumped = true;
        canBoost = true;
        teleported = false;

        if(getState()) {
            switch(modeValue.get().toLowerCase()) {
                case "mineplex":
                    event.setMotion(event.getMotion() * 4.08f);
                    break;
                case "mineplex2":
                    if(mc.thePlayer.isCollidedHorizontally) {
                        event.setMotion(2.31f);
                        canMineplexBoost = true;
                        mc.thePlayer.onGround = false;
                    }
                    break;
            }
        }

    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
