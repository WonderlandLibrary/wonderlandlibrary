package today.flux.module.implement.Movement.speed;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import today.flux.event.MoveEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.SubModule;
import today.flux.module.implement.Movement.Speed;
import today.flux.utility.MoveUtils;
import today.flux.utility.TimeHelper;

public class HypixelDisabled extends SubModule {

    static Minecraft mc = Minecraft.getMinecraft();
    private double speed;
    private int stage;

    private double lastDist;

    public HypixelDisabled() {
        super("Hypixel", "Speed");
    }

    @Override
    public void onDisable() {
        mc.getTimer().timerSpeed = 1.0f;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        lastDist = 0;
        speed = MoveUtils.defaultSpeed();
        stage = 2;
        super.onEnable();
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static TimeHelper slowdown = new TimeHelper();
    @EventTarget
    public void onMove(MoveEvent e) {
        if (stage > 0 && !mc.thePlayer.isInWater()) {
            if (stage == 1 && mc.thePlayer.onGround && MoveUtils.isMoving())
                stage += 1;

            if (stage == 2 && mc.thePlayer.onGround && MoveUtils.isMoving()) {
                mc.thePlayer.jump();
                e.setY(mc.thePlayer.motionY *= 0.98);
            } else if (stage >= 3) {
                if (mc.thePlayer.isCollidedVertically) {
                    speed = getBaseSpeed();
                    lastDist = 0.0;
                    stage = 0;
                }
            }
        } else {
            stage = 0;
        }

        if (e.y < 0) e.y *= 0.9;

        speed = getBaseSpeed();

        if (stage < 1) {
            ++stage;
            lastDist = 0.0;
        }

        float diff;

        int amplifier = -1;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }

        if (stage == 2 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)
                && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
            speed *= 1.785 + 0.04D * (amplifier + 1);

        } else if (stage == 3) {
            diff = (float) (0.72 * (lastDist - getBaseSpeed() * (isOnIce() ? 1.1 : 1.0)));
            speed = lastDist - diff;
        } else {
            if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
            }
            speed = MoveUtils.calculateFriction(speed, lastDist, getBaseSpeed());
        }

        speed = Math.max(speed, getBaseSpeed() * 0.85);
        ++stage;

        if (MoveUtils.isMoving()) {
            if ((mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed) && Math.abs(today.flux.module.implement.Misc.disabler.Hypixel.yawDiff) > 15) {
                slowdown.reset();
            } else {
                Speed.setMotion(e, (slowdown.isDelayComplete(500) ? 1 : 0.7) * speed);
            }
        } else {
            Speed.setMotion(e, 0);
            stage = 0;
        }
    }

    private double getBaseSpeed() {
        final EntityPlayerSP player = mc.thePlayer;
        double base = 0.2895;
        final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
        final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
        if (moveSpeed != null)
            base *= 1.0 + 0.19 * (moveSpeed.getAmplifier() + 1);

        if (moveSlowness != null)
            base *= 1.0 - 0.13 * (moveSlowness.getAmplifier() + 1);

        if (player.isInWater()) {
            base *= 0.5203619984250619;
            final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(mc.thePlayer);

            if (depthStriderLevel > 0) {
                double[] DEPTH_STRIDER_VALUES = new double[]{1.0, 1.4304347400741908, 1.7347825295420374, 1.9217391028296074};
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }

        } else if (player.isInLava()) {
            base *= 0.5203619984250619;
        }
        return base;
    }

    public static boolean isOnIce() {
        Block blockUnder = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, StrictMath.floor(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY) - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }
}

