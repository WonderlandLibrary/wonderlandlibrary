package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.RadiumClient;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.player.Scaffold;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.*;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ModuleInfo(label = "Kill Aura", category = ModuleCategory.COMBAT)
public final class KillAura extends Module {

    private static final C08PacketPlayerBlockPlacement BLOCK_PACKET = new C08PacketPlayerBlockPlacement(
            new BlockPos(-0.0F, -0.0F, -0.0F), 255, null, 0.0f, 0.0f, 0.0f);
    public static int waitTicks;
    private final EnumProperty<AuraMode> auraModeProperty = new EnumProperty<>("Mode", AuraMode.PRIORITY);
    private final EnumProperty<SortingMethod> sortingMethodProperty = new EnumProperty<>("Sorting Method", SortingMethod.HEALTH);
    private final EnumProperty<AttackMethod> attackMethodProperty = new EnumProperty<>("Attack Method", AttackMethod.POST);
    private final Property<Boolean> duraProperty = new Property<>("Dura", false);
    private final DoubleProperty minApsProperty = new DoubleProperty("Min APS", 8.0,
            () -> !this.duraProperty.getValue(), 1.0, 20.0, 1.0);
    private final DoubleProperty maxApsProperty = new DoubleProperty("Max APS", 10.0,
            () -> !this.duraProperty.getValue(), 1.0, 20.0, 1.0);
    private final DoubleProperty rangeProperty = new DoubleProperty("Range", 4.3, 3.0,
            6.0, 0.1, Representation.DISTANCE);
    private final DoubleProperty rangeThruWalls = new DoubleProperty("Wall Range", 2.0, 1.0,
            8.0, 0.1, Representation.DISTANCE);
    private final Property<Boolean> autoblockProperty = new Property<>("Autoblock", true);
    private final DoubleProperty blockRangeProperty = new DoubleProperty("Block Range", 8.0,
            autoblockProperty::getValue,
            3.0, 8.0, 0.1, Representation.DISTANCE);
    private final MultiSelectEnumProperty<Checks> checksProperty = new MultiSelectEnumProperty<>("Checks", Checks.ALIVE, Checks.ROTATION);
    private final DoubleProperty maxAngleChangeProperty = new DoubleProperty("Max Angle Step", 45.0, 1.0, 201.25, 0.25);
    private final DoubleProperty fovProperty = new DoubleProperty("Scan Degrees", 201.25, () -> checksProperty.isSelected(Checks.FOV), 0, 201.25, 0.25);
    private final Property<Boolean> fovCircleProperty = new Property<>("Scan Circle", false, fovProperty::isAvailable);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
        if (this.fovCircleProperty.isAvailable() && this.fovCircleProperty.getValue()) {
            final LockedResolution lr = event.getResolution();
            // Fix radius param to accurately reflect actual fov
            RenderingUtils.drawLoop(lr.getWidth() / 2.0F, lr.getHeight() / 2.0F, this.fovProperty.getValue(), 90, 0.5F, 0xFFFFFFFF, false);
        }
    };
    private final Property<Boolean> lockViewProperty = new Property<>("Lock View", false);
    private final Property<Boolean> keepSprintProperty = new Property<>("Keep Sprint", true);
    private final Property<Boolean> forceUpdateProperty = new Property<>("Force Update", false);
    private final MultiSelectEnumProperty<Targets> targetsProperty = new MultiSelectEnumProperty<>(
            "Targets", Targets.PLAYERS);
    private final TimerUtil attackTimer = new TimerUtil();

    private final Map<Entity, EntityData> entityDataCache = new HashMap<>();
    private final DataSupplier dataSupplier = entity -> {
        final EntityData data = new EntityData(RotationUtils.getRotationsToEntity(entity), getDistToEntity(entity.posX, entity.posY, entity.posZ));
        entityDataCache.put(entity, data);
        return data;
    };

    private EntityLivingBase target;
    private boolean entityInBlockRange;
    private Scaffold scaffold;

    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent = event -> {
        if (event.getPacket() instanceof C0APacketAnimation)
            this.attackTimer.reset();
    };

    @EventLink(EventBusPriorities.LOWEST)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            this.entityDataCache.clear();

            this.entityInBlockRange = false;
            EntityLivingBase optimalTarget = null;

            final List<EntityLivingBase> entities = Wrapper.getLivingEntities(this::isValid);

            if (entities.size() > 1)
                entities.sort(this.sortingMethodProperty.getValue().getSorter());

            for (EntityLivingBase entity : entities) {
                final double dist = this.computeData(entity).dist;

                if (!this.entityInBlockRange && dist < this.blockRangeProperty.getValue())
                    this.entityInBlockRange = true;

                if (dist < this.rangeProperty.getValue()) {
                    optimalTarget = entity;
                    break;
                }
            }

            this.target = optimalTarget;

            if (isOccupied() || checkWaitTicks())
                return;

            if (optimalTarget != null) {
                if (Wrapper.getTimer().timerSpeed > 1.0F)
                    Wrapper.getTimer().timerSpeed = 1.0F;


                RotationUtils.rotate(event, this.computeData(optimalTarget).rotations,
                        this.maxAngleChangeProperty.getValue().floatValue(), this.lockViewProperty.getValue());

                if (this.attackMethodProperty.getValue() == AttackMethod.PRE)
                    tryAttack(event);
            }
        } else {
            if (isOccupied())
                return;

            if (this.target != null && this.attackMethodProperty.getValue() == AttackMethod.POST)
                tryAttack(event);

            if (this.entityInBlockRange && this.autoblockProperty.getValue() && isHoldingSword()) {
                Wrapper.getPlayerController().sendUseItem(Wrapper.getPlayer(), Wrapper.getWorld(),
                        Wrapper.getPlayer().getCurrentEquippedItem(), BLOCK_PACKET);
            }
        }
    };

    public KillAura() {
        setSuffixListener(auraModeProperty);
    }

    private static double getDistToEntity(final double x, final double y, final double z) {
        final EntityPlayer localPlayer = Wrapper.getPlayer();
        final double xDif = x - localPlayer.posX;
        final double yDif = y - localPlayer.posY;
        final double zDif = z - localPlayer.posZ;
        return Math.sqrt(xDif * xDif + zDif * zDif + yDif * yDif);
    }

    public static boolean isAutoBlocking() {
        final KillAura aura = getInstance();
        return aura.isEnabled() && aura.autoblockProperty.getValue() && aura.entityInBlockRange;
    }

    public static KillAura getInstance() {
        return ModuleManager.getInstance(KillAura.class);
    }

    public static double getEffectiveHealth(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            return player.getHealth() * (PlayerUtils.getTotalArmorProtection(player) / 20.0);
        } else return 0;
    }

    private void tryAttack(UpdatePositionEvent event) {
        if (isUsingItem())
            return;

        final EntityPlayer localPlayer = Wrapper.getPlayer();

        int min = this.minApsProperty.getValue().intValue();
        int max = this.maxApsProperty.getValue().intValue();

        if (min > max) {
            min = max;
            max = min;
        }

        final int cps;
        if (min == max) cps = min;
        else cps = RandomUtils.nextInt(min, max);

        if (attackTimer.hasElapsed(1000L / cps) && isLookingAtEntity(event.getYaw(), event.getPitch())) {
            localPlayer.swingItem();
            Wrapper.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));

            if (this.duraProperty.getValue()) {
                InventoryUtils.windowClick(36, 8, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                Wrapper.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                InventoryUtils.windowClick(44, 0, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
            }

            if (!this.keepSprintProperty.getValue() && localPlayer.isSprinting()) {
                localPlayer.motionX *= 0.6D;
                localPlayer.motionZ *= 0.6D;
                localPlayer.setSprinting(false);
            }
        } else if (this.forceUpdateProperty.getValue() && event.isOnGround()) {
            Wrapper.sendPacketDirect(new C03PacketPlayer(true));
        }
    }

    private boolean isLookingAtEntity(final float yaw, final float pitch) {
        final double range = this.rangeProperty.getValue();
        final Vec3 src = Wrapper.getPlayer().getPositionEyes(1.0F);
        final Vec3 rotationVec = Entity.getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        final MovingObjectPosition obj = Wrapper.getWorld().rayTraceBlocks(src, dest,
                false, false, true);
        if (obj == null) return false;
        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (this.checksProperty.isSelected(Checks.RAYTRACE)) return false;
            else if (this.computeData(this.target).dist > this.rangeThruWalls.getValue()) return false;
        }
        if (!this.checksProperty.isSelected(Checks.ROTATION)) return true;
        return this.target.getEntityBoundingBox().expand(0.1F, 0.1F, 0.1F).calculateIntercept(src, dest) != null;
    }

    private EntityData computeData(final Entity entity) {
        final EntityData requiredData;
        final EntityData data = this.entityDataCache.getOrDefault(entity, null);
        if (data == null) requiredData = this.dataSupplier.calculate(entity);
        else requiredData = data;
        return requiredData;
    }

    private boolean fovCheck(EntityLivingBase entity, int fov) {
        final float[] rotations = this.computeData(entity).rotations;
        final EntityPlayer player = Wrapper.getPlayer();
        final float yawChange = MathHelper.wrapAngleTo180_float(player.rotationYaw - rotations[0]);
        final float pitchChange = MathHelper.wrapAngleTo180_float(player.rotationPitch - rotations[1]);
        return Math.sqrt(yawChange * yawChange + pitchChange * pitchChange) < fov;
    }

    @Override
    public void onEnable() {
        this.entityDataCache.clear();

        if (this.scaffold == null)
            this.scaffold = ModuleManager.getInstance(Scaffold.class);
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.entityInBlockRange = false;
    }

    private boolean isInMenu() {
        return Wrapper.getCurrentScreen() != null;
    }

    private boolean isOccupied() {
        return isInMenu() || this.scaffold.isEnabled();
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }

    private boolean checkWaitTicks() {
        if (waitTicks > 0) {
            waitTicks--;
            return true;
        }
        return false;
    }

    private boolean isUsingItem() {
        return Wrapper.getPlayer().isUsingItem() && !isHoldingSword();
    }

    private boolean isHoldingSword() {
        final ItemStack stack;
        return (stack = Wrapper.getPlayer().getCurrentEquippedItem()) != null && stack.getItem() instanceof ItemSword;
    }

    private boolean isValid(EntityLivingBase entity) {
        if (this.checksProperty.isSelected(Checks.ALIVE) && !entity.isEntityAlive())
            return false;
        if (entity.isInvisible() && !this.targetsProperty.isSelected(Targets.INVISIBLES))
            return false;
        if (entity == Wrapper.getPlayer().ridingEntity)
            return false;
        if (entity instanceof EntityOtherPlayerMP) {
            final EntityPlayer player = (EntityPlayer) entity;
            if (!this.targetsProperty.isSelected(Targets.PLAYERS))
                return false;
            if (ModuleManager.getInstance(AntiBot.class).isBot(player))
                return false;
            if (!this.targetsProperty.isSelected(Targets.TEAMMATES) && PlayerUtils.isTeamMate(player))
                return false;
            if (!targetsProperty.isSelected(Targets.FRIENDS) &&
                    RadiumClient.getInstance().getPlayerManager().isFriend(player))
                return false;
        } else if (entity instanceof EntityMob) {
            if (!this.targetsProperty.isSelected(Targets.MOBS))
                return false;
        } else if (entity instanceof EntityAnimal) {
            if (!this.targetsProperty.isSelected(Targets.ANIMALS))
                return false;
        } else {
            // Ignore any other types of entities
            return false;
        }

        return this.computeData(entity).dist <
                Math.max(this.blockRangeProperty.getValue(), this.rangeProperty.getValue()) &&
                (!this.checksProperty.isSelected(Checks.FOV) || this.fovCheck(entity, this.fovProperty.getValue().intValue()));
    }

    private enum AuraMode {
        PRIORITY
    }

    private enum AttackMethod {
        PRE,
        POST
    }

    private enum Targets {
        PLAYERS,
        TEAMMATES,
        FRIENDS,
        INVISIBLES,
        MOBS,
        ANIMALS
    }

    private enum Checks {
        ALIVE,
        FOV,
        ROTATION,
        RAYTRACE
    }

    private enum SortingMethod {
        DISTANCE(new DistanceSorting()),
        HEALTH(new HealthSorting()),
        HURT_TIME(new HurtTimeSorting()),
        ANGLE(new AngleSorting()),
        CROSSHAIR(new CrosshairSorting()),
        COMBINED(new CombinedSorting());

        private final Comparator<EntityLivingBase> sorter;

        SortingMethod(Comparator<EntityLivingBase> sorter) {
            this.sorter = sorter;
        }

        public Comparator<EntityLivingBase> getSorter() {
            return sorter;
        }
    }

    @FunctionalInterface
    private interface DataSupplier {
        EntityData calculate(Entity entity);
    }

    private static class EntityData {
        private final float[] rotations;
        private final double dist;

        public EntityData(float[] rotations, double dist) {
            this.rotations = rotations;
            this.dist = dist;
        }
    }

    private static abstract class AngleBasedSorting implements Comparator<EntityLivingBase> {
        protected abstract float getCurrentAngle();

        @Override
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            final float yaw = this.getCurrentAngle();

            return Double.compare(
                    Math.abs(RotationUtils.getYawToEntity(o1) - yaw),
                    Math.abs(RotationUtils.getYawToEntity(o2) - yaw));
        }
    }

    private static class AngleSorting extends AngleBasedSorting {
        @Override
        protected float getCurrentAngle() {
            return Wrapper.getPlayer().currentEvent.getYaw();
        }
    }

    private static class CrosshairSorting extends AngleBasedSorting {
        @Override
        protected float getCurrentAngle() {
            return Wrapper.getPlayer().rotationYaw;
        }
    }

    private static class CombinedSorting implements Comparator<EntityLivingBase> {
        @Override
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            int t1 = 0;
            for (SortingMethod sortingMethod : SortingMethod.values()) {
                Comparator<EntityLivingBase> sorter = sortingMethod.getSorter();
                if (sorter == this) continue;
                t1 += sorter.compare(o1, o2);
            }
            return t1;
        }
    }

    private static class DistanceSorting implements Comparator<EntityLivingBase> {
        @Override
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            final KillAura aura = KillAura.getInstance();
            return Double.compare(aura.computeData(o1).dist, aura.computeData(o2).dist);
        }
    }

    private static class HealthSorting implements Comparator<EntityLivingBase> {
        @Override
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Double.compare(getEffectiveHealth(o1), getEffectiveHealth(o2));
        }
    }

    private static class HurtTimeSorting implements Comparator<EntityLivingBase> {
        @Override
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Integer.compare(
                    EntityLivingBase.MAX_HURT_RESISTANT_TIME - o2.hurtResistantTime,
                    EntityLivingBase.MAX_HURT_RESISTANT_TIME - o1.hurtResistantTime);
        }
    }
}
