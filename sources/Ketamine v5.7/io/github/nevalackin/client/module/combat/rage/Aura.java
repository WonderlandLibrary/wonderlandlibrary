package io.github.nevalackin.client.module.combat.rage;

import io.github.nevalackin.client.binding.Bind;
import io.github.nevalackin.client.binding.BindType;
import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.game.UpdateLookEvent;
import io.github.nevalackin.client.event.player.JumpEvent;
import io.github.nevalackin.client.event.player.MoveFlyingEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.player.TeamsUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.*;

public final class Aura extends Module {

    // The mode, single or multi
    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.MULTI);
    // The max amount of targets for multi aura
    private final DoubleProperty maxTargetsProperty = new DoubleProperty("Targets", 2,
                                                                         () -> this.modeProperty.getValue() == Mode.MULTI,
                                                                         2, 8, 1);
    // The method used for sorting valid entities
    private final EnumProperty<SortingMode> sortingModeProperty = new EnumProperty<>("Sorting", SortingMode.HEALTH, this::doesSingleAura);
    // The type of auto block
    private final EnumProperty<AutoBlockMode> autoBlockModeProperty = new EnumProperty<>("Autoblock", AutoBlockMode.INTERACT);
    private final BooleanProperty checkTeamsProperty = new BooleanProperty("Teams", false);
    // The method for checking if another player is on the same team as you
    private final EnumProperty<TeamsUtil.TeamsMode> teamsCheckProperty = new EnumProperty<>("Teams Check", TeamsUtil.TeamsMode.NAME, this.checkTeamsProperty::getValue);
    // The reach before an entity is considered 'valid'
    private final DoubleProperty reachProperty = new DoubleProperty("Range", 4.2, 3.0, 6.0, 0.1);
    // If you attack
    private final BooleanProperty autoAttackProperty = new BooleanProperty("Auto Attack", true);
    // Swing method
    private final EnumProperty<Swing> swingMethodProperty = new EnumProperty<>("Swing", Swing.CLIENT, this.autoAttackProperty::getValue);
    // Whether you attack on PRE or POST, requires auto attack
    private final EnumProperty<AttackMode> attackModeProperty = new EnumProperty<>("Attack Mode", AttackMode.PRE, this.autoAttackProperty::getValue);
    // Applies legit sprinting and motionX/Z on hit (requires auto attack)
    private final BooleanProperty keepSprintProperty = new BooleanProperty("Keep Sprint", true, this.autoAttackProperty::getValue);
    // The delay (in ticks) between attacks
    private final DoubleProperty ticksProperty = new DoubleProperty("Delay", 0,
                                                                    this.autoAttackProperty::getValue, 0, 10, 1);
    // The delay before your allowed to switch to a new target
    private final DoubleProperty retargetDelayProperty = new DoubleProperty("Switch Delay", 7,
                                                                            this::doesSingleAura, 1, 20, 1);
    // The minimum distance a valid entity can be before you block (requires autoblock)
    private final DoubleProperty blockRangeProperty = new DoubleProperty("Block Range", 6.0,
                                                                         () -> this.autoBlockModeProperty.getValue() != AutoBlockMode.OFF,
                                                                         3.0, 8.0, 0.1);
    // The rotation mode
    private final EnumProperty<RotationsMode> rotationsModeProperty = new EnumProperty<>("Rotations", RotationsMode.SMOOTH);
    // The point to look at when rotating
    private final EnumProperty<RotationUtil.RotationsPoint> rotationsPointProperty = new EnumProperty<>("Rotations Point", RotationUtil.RotationsPoint.CLOSEST);
    // The max angle you can step in a tick (only used with rotations LINEAR/SMOOTH)
    private final DoubleProperty angleChangeProperty = new DoubleProperty("Smoothing", 10.0,
                                                                          () -> this.rotationsModeProperty.getValue() == RotationsMode.SMOOTH,
                                                                          1.0, 80.0, 0.5);
    // Silent aim (use events to set yaw/pitch only server side)
    private final BooleanProperty silentAimProperty = new BooleanProperty("Silent Aim", true, this::doesRotations);
    // The bounding box scale (only used with auto attack)
    private final DoubleProperty boundingBoxScaleProperty = new DoubleProperty("Bounding Box Scale", 0.1,
                                                                               this.autoAttackProperty::getValue,
                                                                               -1.0, 1.0, 0.1);
    private final BooleanProperty drawBoundingBoxProperty = new BooleanProperty("Draw Bounding Box", true, this.autoAttackProperty::getValue);
    // Can hit through walls (requires auto attack & rotations)
    private final BooleanProperty passThroughWalls = new BooleanProperty("Walls", true,
                                                                         () -> this.doesRotations() && this.autoAttackProperty.getValue());
    // Critical options (only used for auto attack)
    private final EnumProperty<CriticalMode> criticalProperty = new EnumProperty<>("Crits", CriticalMode.WATCHDOG,
                                                                                   this.autoAttackProperty::getValue);
    private final DoubleProperty criticalDelayProperty = new DoubleProperty("Crits Delay", 0,
                                                                            () -> this.criticalProperty.getValue() != CriticalMode.OFF && this.criticalProperty.check(),
                                                                            0, 20, 1);
    // Extra durability option (requires auto attack)
    private final EnumProperty<ExtraDurabilityMode> extraDurabilityProperty = new EnumProperty<>("Dura", ExtraDurabilityMode.OFF,
                                                                                                 this.autoAttackProperty::getValue);
    // Ping adjust (only used for attacking)
    private final DoubleProperty pingProperty = new DoubleProperty("Ping", -1, this.autoAttackProperty::getValue, -1, 10, 1);
    // The entities that will be attacked (used for interact autoblock and post attack)
    private final List<EntityLivingBase> attackList = new ArrayList<>();
    // Whether an entity is inside the block range (used for autoblock)
    private boolean entityInBlockRange;
    // Timers in ticks (instead of millisecond timers because it's better)
    private int ticksSinceLastAttack;
    private int ticksSinceSendCritical;
    private int ticksSinceLastRetarget;
    private int ticksSinceLastFastAttack;
    // Cache the distance to entities (note: static so that SortingMode can access it)
    private static final Map<EntityLivingBase, Double> entityDistCache = new HashMap<>();
    // Store the last updated rotate yaw/pitch (for bypass rotations)
    private final float[] rotationStore = new float[2];
    // State of if aura has a target (for bypass rotations)
    private boolean rotating;

    public Aura() {
        super("Kill Aura", Category.COMBAT, Category.SubCategory.COMBAT_RAGE);

        this.register(this.modeProperty,
                      this.sortingModeProperty,
                      // Multi aura only
                      this.maxTargetsProperty,
                      // Reach
                      this.reachProperty,
                      // Autoblock
                      this.autoBlockModeProperty, this.blockRangeProperty,
                      // Teams
                      this.checkTeamsProperty, this.teamsCheckProperty,
                      // Rotations
                      this.rotationsModeProperty, this.angleChangeProperty, this.silentAimProperty, this.rotationsPointProperty,
                      // Attack
                      this.autoAttackProperty, this.swingMethodProperty, this.attackModeProperty, this.keepSprintProperty,
                      this.ticksProperty, this.boundingBoxScaleProperty, this.drawBoundingBoxProperty, this.passThroughWalls,
                      // Misc
                      this.pingProperty, this.retargetDelayProperty,
                      // Criticals / Dura
                      this.criticalProperty, this.criticalDelayProperty, this.extraDurabilityProperty);

        // Add the value aliases (to make sliders look nice)
        this.boundingBoxScaleProperty.addValueAlias(0.1, "Vanilla");

        this.reachProperty.addValueAlias(4.2, "NCP");
        this.reachProperty.addValueAlias(3.0, "Legit");

        this.ticksProperty.addValueAlias(0, "Auto");
        this.ticksProperty.addValueAlias(1, "20 APS");
        this.ticksProperty.addValueAlias(2, "10 APS");

        this.pingProperty.addValueAlias(-1, "Auto");

        this.retargetDelayProperty.addValueAlias(1, "None");

        this.criticalDelayProperty.addValueAlias(0, "Auto");
        this.criticalDelayProperty.addValueAlias(1, "None");

        this.setSuffix(() -> this.modeProperty.getValue().toString());
    }

    private boolean doesRotations() {
        return this.rotationsModeProperty.getValue() != RotationsMode.OFF;
    }

    private boolean doesSingleAura() {
        return this.modeProperty.getValue() == Mode.SINGLE;
    }

    private boolean isBypassRotations() {
        return this.rotationsModeProperty.getValue() == RotationsMode.UNDETECTABLE;
    }

    @EventLink
    private final Listener<UpdateLookEvent> onUpdateLook = event -> {
        // Undetectable rotations mode
        if (this.isBypassRotations()) {
            // Get the eye-pos of the player
            final Vec3 hitOrigin = RotationUtil.getHitOrigin(this.mc.thePlayer);
            // If was not rotating previously
            if (!this.rotating) {
                // Set rotationStore to the current player angles
                this.rotationStore[0] = this.mc.thePlayer.rotationYaw;
                this.rotationStore[1] = this.mc.thePlayer.rotationPitch;
            }
            // Reset rotating state
            this.rotating = false;
            // Get the current targeted entity
            final EntityLivingBase target = this.getTarget();
            if (target == null) return;
            // Find the optimal attack hit vec to aim at
            final Vec3 attackHitVec = RotationUtil.getCenterPointOnBB(target.getEntityBoundingBox(),
                                                                      0.5 + Math.random() * 0.1);
            // Calculate the yaw/pitch deltas to target
            float[] rotations = RotationUtil.getRotations(hitOrigin, attackHitVec);
            // Apply GCD fix
            RotationUtil.applyGCD(rotations, this.rotationStore);
            // Update rotations store
            this.rotationStore[0] = rotations[0];
            this.rotationStore[1] = rotations[1];
            // Is now rotating
            this.rotating = true;
        }
    };

    @EventLink
    private final Listener<MoveFlyingEvent> onMoveUpdate = event -> {
        // Move fix
        if (this.isBypassRotations() && this.rotating) {
            event.setYaw(this.rotationStore[0]);
        }
    };

    @EventLink
    private final Listener<JumpEvent> onJump = event -> {
        // Jump fix
        if (this.isBypassRotations() && this.rotating) {
            event.setYaw(this.rotationStore[0]);
        }
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (this.drawBoundingBoxProperty.getValue() && this.getTarget() != null) {
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            final boolean restore = DrawUtil.glEnableBlend();

            int lastColour = 0;

            for (final EntityLivingBase target : this.attackList) {
                final AxisAlignedBB boundingBox = DrawUtil.interpolate(target,
                                                                       RotationUtil.getHittableBoundingBox(target,
                                                                                                                          this.boundingBoxScaleProperty.getValue()),
                                                                       event.getPartialTicks());
                final int colour = target.hurtTime > 0 ? 0x4000FF59 : 0x40FF0000;

                if (colour != lastColour) {
                    DrawUtil.glColour(colour);
                    lastColour = colour;
                }

                DrawUtil.glDrawBoundingBox(boundingBox, 0, true);
            }

            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            DrawUtil.glRestoreBlend(restore);
        }
    };

    @EventLink(0)
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            // Increase ticks count every tick (duh)
            this.ticksSinceLastAttack++;
            this.ticksSinceSendCritical++;
            this.ticksSinceLastRetarget++;
            this.ticksSinceLastFastAttack++;
            // Clear the dist cache every tick
            entityDistCache.clear();

            // Reset target
            this.attackList.clear();

            if (event.isRotating()) return;

            // Save previous target
            final EntityLivingBase lastTarget = this.getTarget();

            final List<EntityLivingBase> validEntities = this.mc.theWorld.getLoadedEntityList().stream() // For all the loaded entities in the world
                .filter(EntityLivingBase.class::isInstance) // Filter out non EntityLivingBase entities (e.g. armor stands)
                .map(entity -> (EntityLivingBase) entity) // cast the Entities to EntityLivingBases
                .filter(this::validateEntity) // Filter out all invalid entities
                .collect(Collectors.toList());

            // Find if there is entity inside block range
            this.entityInBlockRange = validEntities.stream()
                .anyMatch(entity -> entityDistCache.get(entity) < this.blockRangeProperty.getValue());

            // Find entities inside reach
            final Stream<EntityLivingBase> hittableEntities = validEntities.stream()
                .filter(entity -> entityDistCache.get(entity) < this.reachProperty.getValue()); // Check reach

            // Get the eye-pos of the player
            final Vec3 hitOrigin = RotationUtil.getHitOrigin(this.mc.thePlayer);

            // is non-silent aiming
            final boolean isLockView = !this.silentAimProperty.getValue();

            switch (this.modeProperty.getValue()) {
                case MULTI:
                    final boolean calculateRotations = this.doesRotations();
                    final List<float[]> rotationsToEntities = new ArrayList<>();

                    // Add all hittable entities to the attack list
                    hittableEntities.sorted(SortingMode.ANGLE.getSorter()) // Always use angle sorting
                        .limit(this.maxTargetsProperty.getValue().longValue())
                        .forEach(entity -> {
                            final Vec3 attackHitVec = this.getAttackHitVec(hitOrigin, entity);

                            if (attackHitVec != null) {
                                if (calculateRotations) {
                                    // Calculate rotations (with max yaw/pitch change & GCD) to best attack hit vec
                                    final float[] rotations = RotationUtil.getRotations(
                                        new float[]{isLockView ? this.mc.thePlayer.rotationYaw : event.getLastTickYaw(),
                                            isLockView ? this.mc.thePlayer.rotationPitch : event.getLastTickPitch()},
                                        this.rotationsModeProperty.getValue() == RotationsMode.SNAP ? 0.0F : this.angleChangeProperty.getValue().floatValue(), hitOrigin, attackHitVec);

                                    rotationsToEntities.add(rotations);
                                }

                                this.attackList.add(entity);
                            }
                        });

                    if (calculateRotations && !this.attackList.isEmpty()) {
                        final float[] avgRotations = RotationUtil.calculateAverageRotations(rotationsToEntities);

                        this.setServerSideRotations(avgRotations, event, isLockView);
                    }
                    break;
                case SINGLE:
                    hittableEntities.min(this.sortingModeProperty.getValue().getSorter()) // Sort and get the most optimal
                        .ifPresent(entity -> { // If there is an entity
                            if (lastTarget != null && this.validateEntity(lastTarget) &&
                                entityDistCache.get(entity) < this.reachProperty.getValue() && // Validate the last target
                                lastTarget != entity) {
                                if (this.ticksSinceLastRetarget >= this.retargetDelayProperty.getValue()) {
                                    // Set target to most optimal entity
                                    this.ticksSinceLastRetarget = 0;
                                } else {
                                    entity = lastTarget;
                                }
                            }

                            final Vec3 attackHitVec = this.getAttackHitVec(hitOrigin, entity);

                            if (attackHitVec != null) {
                                // Set target to most optimal entity
                                this.attackList.add(entity);

                                if (this.doesRotations()) {
                                    // Calculate rotations (with max yaw/pitch change & GCD) to best attack hit vec
                                    final float[] rotations = RotationUtil.getRotations(
                                        new float[]{isLockView ? this.mc.thePlayer.rotationYaw : event.getLastTickYaw(),
                                            isLockView ? this.mc.thePlayer.rotationPitch : event.getLastTickPitch()},
                                        this.rotationsModeProperty.getValue() == RotationsMode.SNAP ? 0.0F : this.angleChangeProperty.getValue().floatValue(),
                                        hitOrigin,
                                        attackHitVec);

                                    this.setServerSideRotations(rotations, event, isLockView);
                                }
                            }
                        });
                    break;
            }

            if (this.attackModeProperty.getValue() == AttackMode.PRE)
                this.attack(event);
        } else {
            if (this.attackModeProperty.getValue() == AttackMode.POST)
                this.attack(event);

            if (this.canAutoBlock()) {
                switch (this.autoBlockModeProperty.getValue()) {
                    case INTERACT:
                        for (final EntityLivingBase target : this.attackList) {
                            this.mc.playerController.interactWithEntitySendPacket(this.mc.thePlayer, target);
                        }
                    case NCP:
                        // Using send use item will automatically unblock before next attack so long as right click is not held
                        this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                        break;
                }
            }
        }
    };

    private void setServerSideRotations(float[] rotations,
                                        final UpdatePositionEvent event,
                                        final boolean lockView) {
        // When using undetectable rotations set to stored
        if (this.isBypassRotations())
            rotations = this.rotationStore;

        // Set event yaw (rotations yaw)
        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);

        // Do lock view aim
        if (lockView) {
            this.mc.thePlayer.rotationYaw = rotations[0];
            this.mc.thePlayer.rotationPitch = rotations[1];
        }
    }

    private Vec3 getAttackHitVec(final Vec3 hitOrigin, final EntityLivingBase entity) {
        final AxisAlignedBB boundingBox = RotationUtil.getHittableBoundingBox(entity, this.boundingBoxScaleProperty.getValue().floatValue());
        // Get optimal attack hit vec
        return RotationUtil.getAttackHitVec(this.mc, hitOrigin, boundingBox,
                                            this.rotationsPointProperty.getValue().getHitVec(hitOrigin, boundingBox),
                                            this.passThroughWalls.getValue(), -1);
    }

    private void attack(final UpdatePositionEvent event) {
        if (!this.attackList.isEmpty() && // If there is a target (that has been rotated to)
            this.autoAttackProperty.getValue()) {

            boolean attacked = false;

            for (final EntityLivingBase target : this.attackList) {
                if (!this.shouldAttack(target)) continue; // Check if should attack

                // Check if actually look at the target server side
                final boolean checkRotations = this.doesRotations() && this.attackList.size() == 1;
                // Where the hit originates from (your eye-pos)
                final Vec3 origin = RotationUtil.getHitOrigin(this.mc.thePlayer);
                // Calculate where your hit vector will be on the entity
                final MovingObjectPosition intercept = RotationUtil.calculateIntercept(
                    RotationUtil.getHittableBoundingBox(target, this.boundingBoxScaleProperty.getValue()),
                    origin,
                    event.isPre() ? event.getLastTickYaw() : event.getYaw(),
                    event.isPre() ? event.getLastTickPitch() : event.getPitch(),
                    this.reachProperty.getValue());

                if (!checkRotations || (intercept != null && RotationUtil.validateHitVec(this.mc, origin, intercept.hitVec, this.passThroughWalls.getValue()))) {
                    final double criticalDelay = this.criticalDelayProperty.getValue();
                    final int hurtTicksRemaining = target.hurtTime;
                    final boolean willCritical = this.criticalProperty.getValue() != CriticalMode.OFF &&
                        (criticalDelay == 0 ? hurtTicksRemaining <= this.getPing() : this.ticksSinceSendCritical >= criticalDelay) &&
                        this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically;

                    switch (this.extraDurabilityProperty.getValue()) {
                        case ITEM_SWAP:
                            // Swap sword to last hot bar slot
                            this.doSwordSwap();
                            // Hit with fist
                            this.doAttack(target, Swing.SILENT);

                            if (willCritical) {
                                this.doCritical(event, target, true);

                                // Critical hit with fist
                                this.doAttack(target, Swing.SILENT);
                            }

                            // Swap sword back to first slot
                            this.doSwordSwap();

                            if (willCritical) {
                                // Sending c04 with onGround=true will "uncritical" the next hit
                                this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.getPosX(), event.getPosY(), event.getPosZ(), true));
                            }
                        case ON_CRITICAL:
                            if (willCritical) {
                                // Attack before critical with sword
                                this.doAttack(target, Swing.SILENT);
                            }
                            break;
                    }

                    if (willCritical) {
                        // Send packets to set server-side fallDist
                        this.doCritical(event, target, false);
                    }

                    this.doAttack(target, this.swingMethodProperty.getValue());

                    attacked = true;

                    // Do vanilla mc movement slowdown on hit
                    if (!this.keepSprintProperty.getValue()) {
                        this.mc.thePlayer.setSprinting(false);
                        this.mc.thePlayer.motionX *= 0.6;
                        this.mc.thePlayer.motionZ *= 0.6;
                    }
                }
            }

            if (attacked) {
                // Reset attack timer
                this.ticksSinceLastAttack = 0;
            }
        }
    }

    private void doAttack(final EntityLivingBase entity, final Swing swingMethod) {
        this.mc.playerController.syncCurrentPlayItem();

        switch (swingMethod) {
            case CLIENT:
                this.mc.thePlayer.swingItem();
                break;
            case SILENT:
                this.mc.thePlayer.sendQueue.sendPacket(new C0APacketAnimation());
                break;
        }

        this.mc.thePlayer.sendQueue.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
    }

    // Note :: I have this method to avoid resetting the timer in Inventory Manager
    private void doSwordSwap() {
        final ItemStack itemstack = this.mc.thePlayer.openContainer.slotClick(InventoryUtil.EXCLUDE_ARMOR_BEGIN, 0, 2, this.mc.thePlayer);

        this.mc.thePlayer.sendQueue.sendPacket(new C0EPacketClickWindow(this.mc.thePlayer.inventoryContainer.windowId,
                                                                        InventoryUtil.EXCLUDE_ARMOR_BEGIN, 0, 2,
                                                                        itemstack,
                                                                        this.mc.thePlayer.openContainer.getNextTransactionID(this.mc.thePlayer.inventory)));
    }

    private void doCritical(final UpdatePositionEvent event, final EntityLivingBase target, final boolean forceCritical) {
        final double criticalDelay = this.criticalDelayProperty.getValue();
        final boolean willCritical = forceCritical ||
            criticalDelay == 0 ? // Is auto delay
            this.ticksSinceSendCritical >= 3 && target.hurtTime >= this.getPing() : // Check last critical was over 3 ticks ago and hurtTime >= ping
            this.ticksSinceSendCritical >= criticalDelay; // Check critical delay only

        if (willCritical) {
            for (final double offset : this.criticalProperty.getValue().offsets) {
                this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(event.getPosX(),
                                                                                                   event.getPosY() + offset + Math.random() * 0.001f,
                                                                                                   event.getPosZ(), false));
            }

            this.ticksSinceSendCritical = 0;
        }
    }

    private boolean shouldAttack(final EntityLivingBase entity) {
        if (this.ticksProperty.getValue() != 0) {
            return this.ticksSinceLastAttack >= this.ticksProperty.getValue();
        }

        final int hurtTicksRemaining = entity.hurtTime;

        if (hurtTicksRemaining == this.getPing() || (hurtTicksRemaining == 0 && this.ticksSinceLastFastAttack >= 2 * this.extraDurabilityProperty.getValue().numberOfAttacks * this.attackList.size())) {
            this.ticksSinceLastFastAttack = 0;
            return true;
        }

        return false;
    }

    public int getPing() {
        // Manual ping override (ping slider)
        if (this.pingProperty.getValue() != -1) {
            return this.pingProperty.getValue().intValue();
        }

        // Auto ping (doesn't work on all servers)
        final NetworkPlayerInfo info = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());
        return info == null ? 0 : (int) Math.ceil(info.getResponseTime() / 50.0);
    }

    public boolean canAutoBlock() {
        final ItemStack heldItem = this.mc.thePlayer.getHeldItem();
        return this.autoBlockModeProperty.getValue() != AutoBlockMode.OFF && this.entityInBlockRange &&
            heldItem != null && heldItem.getItem() instanceof ItemSword;
    }

    private boolean validateEntity(final EntityLivingBase entity) {
        return entity.isEntityAlive() && // health > 0 && !entity.isDead
            entity instanceof EntityOtherPlayerMP && // Entity is not local player
//            !(entity instanceof EntityPlayerSP) && // Entity is not local player
            this.validateDistToEntity(entity) && // More optimized to only count nearby entities as valid
            this.runTeamsCheck(entity) &&
            !entity.isInvisible(); // is not invisible
    }

    private boolean runTeamsCheck(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;

            if (this.checkTeamsProperty.getValue()) {
                return !this.teamsCheckProperty.getValue().getComparator().isOnSameTeam(this.mc.thePlayer, player);
            }
        }

        return true;
    }

    private boolean validateDistToEntity(final EntityLivingBase entity) {
        final double dist = entity.getDistanceToEntity(this.mc.thePlayer);

        if (dist < Math.max(this.reachProperty.getValue(), this.blockRangeProperty.getValue())) {
            entityDistCache.put(entity, dist);
            return true;
        }

        return false;
    }

    public double getCachedDistance(final EntityLivingBase entity) {
        if (!this.validateDistToEntity(entity)) return 0;
        return entityDistCache.get(entity);
    }

    public double getMaxDistance() {
        return this.reachProperty.getValue();
    }

    @Override
    public void onEnable() {
        this.ticksSinceLastAttack = 0;
        this.rotating = false;
    }

    @Override
    public void onDisable() {
        this.attackList.clear();
        entityDistCache.clear();
        this.entityInBlockRange = false;
    }

    public EntityLivingBase getTarget() {
        return this.attackList.isEmpty() ? null : this.attackList.get(0);
    }

    public List<EntityLivingBase> getAttackList() {
        return this.attackList;
    }

    private enum Mode {
        SINGLE("Single"),
        MULTI("Multi");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum ExtraDurabilityMode {
        OFF("Off", 1),
        ON_CRITICAL("Do Critical", 2),
        ITEM_SWAP("Item Swap", 4);

        private final String name;
        private final int numberOfAttacks;

        ExtraDurabilityMode(String name, int numberOfAttacks) {
            this.name = name;
            this.numberOfAttacks = numberOfAttacks;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum CriticalMode {
        OFF("Off", null),
        NCP("NCP", new double[]{0.0626, 0}),
        WATCHDOG("Watchdog", new double[]{0.06f, 0.01f});

        private final String name;
        private final double[] offsets;

        CriticalMode(String name, double[] offsets) {
            this.name = name;
            this.offsets = offsets;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum AttackMode {
        PRE("Pre"),
        POST("Post");

        private final String name;

        AttackMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum RotationsMode {
        OFF("Off"),
        SNAP("Snap"),
        UNDETECTABLE("Undetectable"),
        SMOOTH("Smooth");

        private final String name;

        RotationsMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Swing {
        CLIENT("Client"),
        SILENT("Silent"),
        NO_SWING("No Swing");

        private final String name;

        Swing(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum AutoBlockMode {
        OFF("Off"),
        NCP("NCP"),
        INTERACT("Interact");

        private final String name;

        AutoBlockMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum SortingMode {
        HURT_TIME("HurtTime", Comparator.comparingInt(entity -> entity.hurtTime)),
        HEALTH("Health", Comparator.comparingDouble(EntityLivingBase::getHealth)),
        DISTANCE("Distance", Comparator.comparingDouble(entityDistCache::get)),
        ANGLE("Angle", Comparator.comparingDouble(entity -> {
            final Entity player = Minecraft.getMinecraft().thePlayer;
            return Math.abs(RotationUtil.calculateYawFromSrcToDst(player.rotationYaw, player.posX, player.posZ, entity.posX, entity.posZ) - player.rotationYaw);
        })),
        ARMOR("Armor", Comparator.comparingDouble(EntityLivingBase::getTotalArmorValue));

        private final String name;
        private final Comparator<EntityLivingBase> sorter;

        public Comparator<EntityLivingBase> getSorter() {
            return sorter;
        }

        SortingMode(String name, Comparator<EntityLivingBase> sorter) {
            this.name = name;
            this.sorter = sorter;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
