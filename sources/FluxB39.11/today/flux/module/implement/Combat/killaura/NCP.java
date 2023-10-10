package today.flux.module.implement.Combat.killaura;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.Vec3;
import net.minecraft.util.*;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.event.StepEvent;
import today.flux.irc.IRCUser;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.Criticals;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Movement.Strafe;
import today.flux.module.implement.Render.NoRender;
import today.flux.utility.*;
import today.flux.utility.tojatta.api.utilities.angle.Angle;
import today.flux.utility.tojatta.api.utilities.angle.AngleUtility;
import today.flux.utility.tojatta.api.utilities.vector.impl.Vector3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NCP extends SubModule {
    public static List<EntityLivingBase> targets = new ArrayList<>();
    public static List<EntityLivingBase> blockTargets = new ArrayList<>();
    public boolean canAttack;
    boolean isSwitch;
    AngleUtility angleUtility = new AngleUtility(110, 120, 30, 40);
    private int nextAttackDelay;
    private DelayTimer switchTimer = new DelayTimer();
    private DelayTimer delayTimer = new DelayTimer();
    private int index;
    private PreUpdateEvent looked;
    private boolean isReadyToCritical;
    private float[] lastRotations;

    public NCP(boolean isSwitch) {
        super(isSwitch ? "Switch" : "Single", "KillAura");
        this.isSwitch = isSwitch;
    }

    public static float[] getCustomRotation(Vec3 vec) {
        Vec3 playerVector = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        double y = vec.yCoord - playerVector.yCoord;
        double x = vec.xCoord - playerVector.xCoord;
        double z = vec.zCoord - playerVector.zCoord;
        double dff = Math.sqrt(x * x + z * z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(y, dff)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static Vec3 getLocation(AxisAlignedBB bb) {
        double yaw = 0.5 + (KillAura.yawdiff.getValue() / 2);
        double pitch = 0.5 + (KillAura.pitchdiff.getValue() / 2);
        RotationUtils.VecRotation rotation = RotationUtils.searchCenter(bb, true);
        return rotation != null ? rotation.getVec() : new Vec3(bb.minX + (bb.maxX - bb.minX) * yaw,
                bb.minY + (bb.maxY - bb.minY) * pitch, bb.minZ + (bb.maxZ - bb.minZ) * yaw);
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs((angle1 - angle2)) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    @Override
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onEnable() {
        index = 0;
        lastRotations = null;
        super.onEnable();
    }

    @Override
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onDisable() {
        super.onDisable();

        KillAura.unblock();
        KillAura.blockedStatusForRender = false;
    }

    @EventTarget
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onRespawn(RespawnEvent respawnEvent) {
        if (KillAura.autodisable.getValue()) {
            this.setEnabled(false);
        }
    }


    //better than novoline skid
    public void updateAttackTargets() {
        if (targets.isEmpty() || targets.stream().anyMatch(e -> !KillAura.isValid(e, KillAura.range.getValue())) || targets.size() != this.getTargets(true).size()) {
            targets = this.getTargets(true);
        }
    }

    public void updateBlockTargets() {
        if (blockTargets.isEmpty() || blockTargets.stream().anyMatch(e -> !KillAura.isValid(e, KillAura.blockRange.getValue())) || blockTargets.size() != this.getTargets(false).size()) {
            blockTargets = this.getTargets(false);
        }
    }

    public float[] getLoserRotation(Entity target) {
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY - 0.4;
        double zDiff = target.posZ - mc.thePlayer.posZ;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) ((-Math.atan2(yDiff, dist)) * 180.0 / 3.141592653589793);
        float[] array = new float[2];

        float rotationYaw = mc.thePlayer.rotationYaw;

        array[0] = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);

        float rotationPitch = mc.thePlayer.rotationPitch;
        array[1] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
        return array;
    }

    @EventTarget
    public void onPreUpdate(PreUpdateEvent event) {
        if (!ModuleManager.killAuraMod.disableHelper.isDelayComplete(50)) return;

        this.looked = null;
        this.isReadyToCritical = false;

        KillAura.blockedStatusForRender = (!ModuleManager.noRenderMod.isEnabled() || !NoRender.blockAnimation.getValueState()) && KillAura.blocked;

        if (KillAura.blocked) {
            KillAura.blocked = false;
            if (!ServerUtils.INSTANCE.isOnHypixel() || !MoveUtils.isMoving())
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
        }

        try {
            String.class.getMethods()[76].getName();
        } catch (Throwable throwable) {
            updateAttackTargets();
            updateBlockTargets();
        }

        if (targets.isEmpty()) {
            KillAura.target = null;
            lastRotations = null;
            return;
        }

        if (this.index > targets.size() - 1) {
            this.index = 0;
        }

        if (targets.size() > 1 && this.switchTimer.hasPassed(KillAura.switchDelay.getValue())) {
            this.index++;
            this.switchTimer.reset();
        }

        if (this.index > targets.size() - 1 || !isSwitch) {
            this.index = 0;
        }

        KillAura.target = targets.get(index);

        if (lastRotations == null) {
            lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        }

        if (KillAura.instantRotate.getValueState()) {
            float[] customRotation = getCustomRotation(getLocation(targets.get(index).getEntityBoundingBox()));
            if (!ServerUtils.INSTANCE.isOnHypixel()) {
                event.yaw = customRotation[0];
                event.pitch = customRotation[1];
            }
        } else {
            float rotationSpeed = 180 - KillAura.rotationSpeed.getValue() / 2;
            Entity target = KillAura.target;
            double comparison = Math.abs(target.posY - mc.thePlayer.posY) > 1.8
                    ? Math.abs(target.posY - mc.thePlayer.posY) / Math.abs(target.posY - mc.thePlayer.posY) / 2
                    : Math.abs(target.posY - mc.thePlayer.posY);

            Vector3<Double> enemyCoords = new Vector3<>(
                    target.getEntityBoundingBox().minX
                            + (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2,
                    (target instanceof EntityPig || target instanceof EntitySpider
                            ? target.getEntityBoundingBox().minY - target.getEyeHeight() * 1.2
                            : target.posY) - comparison,
                    target.getEntityBoundingBox().minZ
                            + (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2);

            Vector3<Double> myCoords = new Vector3<>(mc.thePlayer.getEntityBoundingBox().minX
                    + (mc.thePlayer.getEntityBoundingBox().maxX - mc.thePlayer.getEntityBoundingBox().minX) / 2,
                    mc.thePlayer.posY,
                    mc.thePlayer.getEntityBoundingBox().minZ
                            + (mc.thePlayer.getEntityBoundingBox().maxZ - mc.thePlayer.getEntityBoundingBox().minZ)
                            / 2);

            Angle srcAngle = new Angle(lastRotations[0], lastRotations[1]);

            Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);

            Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle, rotationSpeed, rotationSpeed);

            if (!ServerUtils.INSTANCE.isOnHypixel()) {
                event.setYaw((smoothedAngle.getYaw()));
                event.setPitch(smoothedAngle.getPitch());
            }
        }

        lastRotations = new float[]{event.yaw, event.pitch};

        // Hypixel critical
        if (!this.isReadyToCritical && ModuleManager.criticalsMod.isEnabled() && BlockUtils.isReallyOnGround() && this.delayTimer.hasPassed(this.nextAttackDelay) && !ModuleManager.speedMod.isEnabled() && !ModuleManager.flyMod.isEnabled() && !ModuleManager.scaffoldMod.isEnabled()) {
            if (targets.get(this.index).hurtResistantTime <= Criticals.hurtTime.getValue() && Criticals.timer.isDelayComplete(200) && (PlayerUtils.isMoving2() || Criticals.timer.isDelayComplete(800))) {
                this.isReadyToCritical = true;
            }
        }

        this.looked = event;

        canAttack = this.delayTimer.hasPassed(this.nextAttackDelay) || (KillAura.aps.getValue() == 15f && this.mc.thePlayer.ticksExisted % 3 == 0);
    }

    @EventTarget
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onStep(StepEvent event) {
        this.isReadyToCritical = false;
    }

    @EventTarget
    public void onLoop(PostUpdateEvent event) {
        if (!ModuleManager.killAuraMod.disableHelper.isDelayComplete(50)) return;

        if (this.looked != null && !Criticals.isEating()) {
            if (canAttack) {
                this.nextAttackDelay = (int) (1000.0F / (KillAura.aps.getValue() + KillAura.getRandomDoubleInRange(-1, 1)));

                this.delayTimer.reset();

                EntityLivingBase target = targets.get(index);

                if (this.isReadyToCritical && ModuleManager.stepMod.isEnabled() && ModuleManager.stepMod.stepping) {
                    this.isReadyToCritical = false;
                }

                if (mc.thePlayer.getDistanceToEntity(targets.get(index)) <= KillAura.range.getValue()) {
                    if (this.isReadyToCritical) {
                        this.isReadyToCritical = false;
                        ModuleManager.criticalsMod.doHoverCrit();
                    }
                    ModuleManager.killAuraMod.attackEntity(target);
                }
            }
        }

        if (KillAura.autoBlock.getValue() && blockTargets.size() > 0 && InvUtils.isHeldingSword() && !KillAura.blocked && (targets.size() == 0 || targets.get(index).canEntityBeSeen(mc.thePlayer) && targets.size() <= 1 || KillAura.blockThoughWall.getValueState())) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
            KillAura.blocked = true;
        }
    }

    private List<EntityLivingBase> getTargets(boolean isAttack) {
        Stream<EntityLivingBase> stream = this.mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .filter(entity -> (IRCUser.getIRCUserByIGN(entity.getName()) == null || IRCUser.getIRCUserByIGN(entity.getName()).perm == 6))
                .map(entity -> (EntityLivingBase) entity)
                .filter(isAttack ? KillAura::isValidAttack : KillAura::isValidBlock);

        if (KillAura.priority.isCurrentMode("Armor")) {
            stream = stream.sorted(Comparator.comparingInt(o -> o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue() : (int) o.getHealth()));
        } else if (KillAura.priority.isCurrentMode("Range")) {
            stream = stream.sorted((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
        } else if (KillAura.priority.isCurrentMode("Fov")) {
            stream = stream.sorted(Comparator.comparingDouble(o -> getDistanceBetweenAngles(mc.thePlayer.rotationPitch, getLoserRotation(o)[0])));
        } else if (KillAura.priority.isCurrentMode("Angle")) {
            stream = stream.sorted((o1, o2) -> {
                float[] rot1 = RotationUtils.getRotation(o1);
                float[] rot2 = RotationUtils.getRotation(o2);
                return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        } else if (KillAura.priority.isCurrentMode("Health")) {
            stream = stream.sorted((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
        }

        List<EntityLivingBase> list = stream.collect(Collectors.toList());
        return list.subList(0, Math.min(list.size(), KillAura.switchSize.getValue().intValue()));
    }
}
