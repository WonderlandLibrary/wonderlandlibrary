package io.github.nevalackin.client.module.combat.miniGames;

import com.google.common.collect.Lists;
import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.packet.SendPacketEvent;
import io.github.nevalackin.client.event.player.PlayerPositionUpdateEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.game.GetFOVEvent;
import io.github.nevalackin.client.event.render.model.ModelRenderEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.event.world.LoadWorldEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.property.MultiSelectionEnumProperty;
import io.github.nevalackin.client.util.player.RotationUtil;
import io.github.nevalackin.client.util.player.TeamsUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.*;

public final class CripsVsBloods extends Module {

    // Automatically shot the gun
    private final BooleanProperty autoShootProperty = new BooleanProperty("Auto Shoot", true);
    // Amount of ping to compensate by
    private final DoubleProperty lagCompensation = new DoubleProperty("Lag Compensation", 0, 0, 500, 1);
    // Recoil control
    private final BooleanProperty recoilControlProperty = new BooleanProperty("Recoil Control", true);
    // Lockview / silent aim
    private final BooleanProperty silentAimProperty = new BooleanProperty("Silent Aim", false);
    // Draw a model where the player is predicted to be after <lagCompensation#getValue> time
    private final BooleanProperty visualizePredictionProperty = new BooleanProperty("Visualize Prediction", true, this::isLagCompensating);
    // Allow shooting while off ground
    private final BooleanProperty jumpShotProperty = new BooleanProperty("Jump Shot", true);
    // Draw bullet tracers
    private final BooleanProperty bulletTracersProperty = new BooleanProperty("Bullet Tracers", false);

    // The weapon type you are currently configuring
    private final EnumProperty<WeaponType> configuringProperty = new EnumProperty<>("Configuring", WeaponType.RIFLE);
    // Holds the min damage for different weapon types
    private final DoubleProperty[] minDamageProperties = new DoubleProperty[]{
        new DoubleProperty("Pistol Min. Damage", 30,
                           () -> this.configuringProperty.getValue() == WeaponType.PISTOL,
                           1, 101, 1),
        new DoubleProperty("Shotgun Min. Damage", 50,
                           () -> this.configuringProperty.getValue() == WeaponType.SHOTGUN,
                           1, 101, 1),
        new DoubleProperty("Rifle Min. Damage", 30,
                           () -> this.configuringProperty.getValue() == WeaponType.RIFLE,
                           1, 101, 1),
        new DoubleProperty("SMG Min. Damage", 5,
                           () -> this.configuringProperty.getValue() == WeaponType.SMG,
                           1, 101, 1),
        new DoubleProperty("Sniper Min. Damage", 101,
                           () -> this.configuringProperty.getValue() == WeaponType.SNIPER,
                           1, 101, 1)
    };
    // Holds the selected hit boxes for each weapon type
    @SuppressWarnings("rawtypes")
    private final MultiSelectionEnumProperty[] hitBoxSelection = new MultiSelectionEnumProperty[]{
        new MultiSelectionEnumProperty<>("Pistol Hit Boxes",
                                         Lists.newArrayList(HitBoxType.HEAD),
                                         HitBoxType.values(),
                                         () -> this.configuringProperty.getValue() == WeaponType.PISTOL),
        new MultiSelectionEnumProperty<>("Shotgun Hit Boxes",
                                         Lists.newArrayList(HitBoxType.HEAD, HitBoxType.BODY),
                                         HitBoxType.values(),
                                         () -> this.configuringProperty.getValue() == WeaponType.SHOTGUN),
        new MultiSelectionEnumProperty<>("Rifle Hit Boxes",
                                         Lists.newArrayList(HitBoxType.HEAD, HitBoxType.BODY),
                                         HitBoxType.values(),
                                         () -> this.configuringProperty.getValue() == WeaponType.RIFLE),
        new MultiSelectionEnumProperty<>("SMG Hit Boxes",
                                         Lists.newArrayList(HitBoxType.HEAD, HitBoxType.BODY),
                                         HitBoxType.values(),
                                         () -> this.configuringProperty.getValue() == WeaponType.SMG),
        new MultiSelectionEnumProperty<>("Sniper Hit Boxes",
                                         Lists.newArrayList(HitBoxType.HEAD, HitBoxType.BODY),
                                         HitBoxType.values(),
                                         () -> this.configuringProperty.getValue() == WeaponType.SNIPER)
    };

    // The max range it will target entities inside of
    private final DoubleProperty maxTargetRange = new DoubleProperty("Max Range", 64, 0, 128, 1);

    // Keep track of current recoil
    private float recoilCompensation;
    // Store the best shot found
    private Shot bestShot;
    // Last time a c08 was sent
    private final long[] clientSideShotTimes = new long[10];
    // Record of shot delays
    private final long[] serverSideShotTimes = new long[10];
    // Store bullet tracers
    private final List<BulletTracer> tracers = new ArrayList<>();

    public CripsVsBloods() {
        super("Crips Vs Bloods", Category.COMBAT, Category.SubCategory.COMBAT_MINI_GAMES);

        this.lagCompensation.addValueAlias(0, "Auto");

        this.register(this.configuringProperty);

        for (final DoubleProperty minDmgProp : this.minDamageProperties) {
            minDmgProp.addValueAlias(101, "HP");
            this.register(minDmgProp);
        }

        for (final MultiSelectionEnumProperty hitBoxProp : this.hitBoxSelection) {
            this.register(hitBoxProp);
        }

        this.register(this.autoShootProperty,
                      this.bulletTracersProperty, this.jumpShotProperty, this.visualizePredictionProperty,
                      this.recoilControlProperty, this.silentAimProperty,
                      this.maxTargetRange, this.lagCompensation);
    }

    private boolean isLagCompensating() {
        return this.lagCompensation.getValue() > 0;
    }

    @EventLink
    private final Listener<SendPacketEvent> onSendPacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement) {

            final C08PacketPlayerBlockPlacement blockPlacementPacket = (C08PacketPlayerBlockPlacement) packet;
            final Weapon weapon;

            if (blockPlacementPacket.getStack() != null && (weapon = this.getWeapon(blockPlacementPacket.getStack())) != null) {
                // Add new time
                this.clientSideShotTimes[0] = System.currentTimeMillis();
                // Short so that the oldest shot time is first (ascending order)
                Arrays.sort(this.clientSideShotTimes);
                // Accumulate recoil
                this.recoilCompensation += weapon.recoil * 0.15F;
            }
        }
    };

    @EventLink(-5)
    private final Listener<ModelRenderEvent> onRenderModel = event -> {
        if (!event.isPre() && this.visualizePredictionProperty.getValue() && this.isLagCompensating() && this.isFeasibleTarget(event.getEntity())) {
            final Vec3 prediction = this.calculatePredictionOffset(event.getEntity());

            if (prediction.lengthVector() > 0.0) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                glDisable(GL_TEXTURE_2D);
                final boolean restore = DrawUtil.glEnableBlend();

                glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                glPushMatrix();

                glRotatef(180.0F - event.getBodyYaw(), 0.0F, -1.0F, 0.0F);
                glTranslated(prediction.xCoord, prediction.yCoord, prediction.zCoord);
                glRotatef(180.0F - event.getBodyYaw(), 0.0F, 1.0F, 0.0F);

                event.drawModel();

                glPopMatrix();

                DrawUtil.glRestoreBlend(restore);
                glEnable(GL_TEXTURE_2D);
            }
        }
    };

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S2FPacketSetSlot) {
            final S2FPacketSetSlot setSlotPacket = (S2FPacketSetSlot) packet;

            final int windowID = setSlotPacket.func_149175_c();
            final int slot = setSlotPacket.func_149173_d();
            final ItemStack stack = setSlotPacket.func_149174_e();
            // Check if stack is not null and is on your hot bar
            if (windowID != 0 || slot < 36 || stack == null) return;
            // Check if is weapon and is not reloading
            final Weapon weaponInSlot = this.getWeapon(stack);
            if (weaponInSlot == null || this.isReloading(stack)) return;
            // Get stack that was previously in slot before S2FPacketSetSlot
            final ItemStack stackInSlot = this.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            // Stack is a weapon and is not reloading
            if (this.getWeapon(stackInSlot) == null || this.isReloading(stackInSlot)) return;
            // And ammo count (items in stack count) has decreased
            if (stack.stackSize < stackInSlot.stackSize) {
                this.onServerSideShot(weaponInSlot);
            }
        } else if (packet instanceof S2APacketParticles) {
            final S2APacketParticles particles = (S2APacketParticles) packet;
            // Remove bullet tracers
            if (particles.getParticleType() == EnumParticleTypes.FLAME) {
//                event.setCancelled();
            }
        }
    };

    @EventLink
    private final Listener<GetFOVEvent> onGetFOV = event -> {
        if (this.getHeldWeapon() == Weapon.AWP && this.isScoped())
            event.setFov(event.getFov() / 2.f);
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (!this.tracers.isEmpty()) {
            glDisable(GL_TEXTURE_2D);
            final boolean restore = DrawUtil.glEnableBlend();
            glDisable(GL_DEPTH_TEST);
            glLineWidth(2.f);
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

            final long timeOfFrame = System.currentTimeMillis();

            glBegin(GL_LINES);
            {
                glColor4f(1.0F, 0.0F, 0.0F, 0.5F);

                final List<BulletTracer> bulletTracers = this.tracers;

                for (int i = 0, bulletTracersSize = bulletTracers.size(); i < bulletTracersSize; i++) {
                    final BulletTracer tracer = bulletTracers.get(i);

                    if (timeOfFrame - tracer.timeOfCreation > 1000L) {
                        this.tracers.remove(i);
                        --bulletTracersSize;
                        --i;
                        continue;
                    }

                    glVertex3d(tracer.start.xCoord, tracer.start.yCoord, tracer.start.zCoord);
                    glVertex3d(tracer.end.xCoord, tracer.end.yCoord, tracer.end.zCoord);
                }
            }
            glEnd();

            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

            glEnable(GL_DEPTH_TEST);
            DrawUtil.glRestoreBlend(restore);
            glEnable(GL_TEXTURE_2D);
        }
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            this.bestShot = null;

            // Reset recoil if not shot in 500ms
            if (this.timeSinceLastShot(this.serverSideShotTimes) > 500L) {
                this.recoilCompensation = 0;
            }

            if (!this.jumpShotProperty.getValue() && !event.isOnGround()) return;
            if (this.isInvulnerable()) return;
            if (this.isReloading()) return;
            final Weapon weapon = this.getHeldWeapon();
            if (weapon == null) return;

            this.onFindOptimalShot(shot -> {
                // Save the optimal shot
                this.bestShot = shot;
                // Calculate calculates to shot
                final float[] rotations = RotationUtil.getRotations(shot.src, shot.predictionShotDst);
                // Set rotations server side
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
                // If non-silent aim set client side rotations
                if (!this.silentAimProperty.getValue()) {
                    this.mc.thePlayer.rotationYaw = rotations[0];
                    this.mc.thePlayer.rotationPitch = rotations[1];
                }
            });

            // Silently do recoil even when no target
            if (this.recoilControlProperty.getValue())
                event.setPitch(event.getPitch() + this.recoilCompensation);
        } else if (this.bestShot != null && this.autoShootProperty.getValue()) {
            final Weapon weapon = this.getHeldWeapon();
            if (weapon == null) return;
            if (weapon == Weapon.AWP && !this.isScoped()) return;

            // 1 tick delay to not packet spam so hard for no gain
            if (this.timeSinceLastShot(this.clientSideShotTimes) >= weapon.delay) {
                if (this.bulletTracersProperty.getValue())
                    this.tracers.add(new BulletTracer(this.bestShot.src, this.bestShot.predictionShotDst, TracerType.CLIENT));
                // Fire shot
                this.mc.thePlayer.sendQueue.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getHeldItem()));
            }
        }
    };

    private long timeBetweenClientServerShot() {
        return this.serverSideShotTimes[this.serverSideShotTimes.length - 1] - this.clientSideShotTimes[this.clientSideShotTimes.length - 1];
    }

    private long timeSinceLastShot(final long[] shots) {
        return System.currentTimeMillis() - shots[shots.length - 1];
    }

    private void onServerSideShot(final Weapon weapon) {
        // Replace the oldest shot time with the newest
        this.serverSideShotTimes[0] = System.currentTimeMillis();
        // Resort with changes
        Arrays.sort(this.serverSideShotTimes);
    }

    private boolean isScoped() {
        return this.mc.thePlayer.isSneaking();
    }

    private boolean validateDistToEntity(final EntityPlayer entity) {
        final double dist = entity.getDistanceToEntity(this.mc.thePlayer);
        return dist < this.maxTargetRange.getValue();
    }

    private boolean isFeasibleTarget(final EntityPlayer entity) {
        return entity.isEntityAlive() && // health > 0 && !entity.isDead
            !entity.isInvisible() && // is not invisible
            entity instanceof EntityOtherPlayerMP && // Entity is not local player
            !TeamsUtil.TeamsMode.NAME.getComparator().isOnSameTeam(this.mc.thePlayer, entity) && // Is not on same team
            this.validateDistToEntity(entity); // More optimized to only count nearby entities as valid
    }

    private void onFindOptimalShot(final Consumer<Shot> shotConsumer) {
        this.mc.theWorld.playerEntities.stream()
            // Filter out entities that are useless
            .filter(this::isFeasibleTarget)
            // Find the most desirable entity
            .sorted(this.getTargetComparator().reversed())
            // Map players to Shots
            .map(this::calculateOptimalShot)
            // Check shot exists
            .filter(Objects::nonNull)
            // Get the first shot
            .findFirst()
            // Call shotConsumer if Shot exists
            .ifPresent(shotConsumer);
    }

    private Comparator<EntityPlayer> getTargetComparator() {
        return Comparator.comparingDouble(player -> {
            final double distanceWeight = 1.0 - this.mc.thePlayer.getDistanceToEntity(player) / this.maxTargetRange.getValue();
            final double heightWeight = 1.0 - player.getHealth() / player.getMaxHealth();
            return distanceWeight + heightWeight;
        });
    }

    private Shot calculateOptimalShot(final EntityPlayer player) {
        // Shooting exactly at eye height for best level ground results
        final Vec3 baseShot = RotationUtil.getHitOrigin(player);
        final Vec3 dst = baseShot.add(this.calculatePredictionOffset(player));
        // Where the shot will come from
        final Vec3 src = RotationUtil.getHitOrigin(this.mc.thePlayer);
        // See if the shot is possible
        if (RotationUtil.validateHitVec(this.mc, src, dst, false))
            return new Shot(src, baseShot, dst, true, false);

        return null;
    }

    private final Map<String, TimestampedPosition[]> positionHistoryCache = new HashMap<>(8);

    @EventLink
    private final Listener<PlayerPositionUpdateEvent> onPlayerUpdatePosition = event -> {
        final EntityPlayer player = event.getPlayer();
        if (this.isFeasibleTarget(player)) {
            final String key = player.getName();
            final TimestampedPosition[] history = this.positionHistoryCache.get(key);
            if (history != null) {
                Arrays.sort(history);
            } else {
                final TimestampedPosition[] buffer = new TimestampedPosition[10];
                buffer[0] = new TimestampedPosition(event.getMove());
                this.positionHistoryCache.put(key, buffer);
            }
        }
    };

    private Prediction runPrediction(final EntityPlayer player) {
        final Vec3 src = RotationUtil.getHitOrigin(player);
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        return null;
    }

    private Vec3 calculatePredictionOffset(final EntityPlayer player) {
        // How many ticks to lag compensate by
        final double lagCompTicks = this.lagCompensation.getValue() / 50.0;
        // Calculate position change of player
        final double dx = player.posX - player.prevPosX;
        final double dy = player.posY - player.prevPosY;
        final double dz = player.posZ - player.prevPosZ;
        return new Vec3(dx * lagCompTicks, dy, dz * lagCompTicks);
    }

    private boolean isInvulnerable() {
        return this.mc.ingameGUI.field_175200_y.startsWith("Invulnerable");
    }

    private boolean isReloading() {
        return this.isReloading(this.mc.thePlayer.getHeldItem());
    }

    private boolean isReloading(final ItemStack stack) {
        if (stack == null) return false;
        return stack.isItemDamaged();
    }

    private Weapon getHeldWeapon() {
        return this.getWeapon(this.mc.thePlayer.getHeldItem());
    }

    private Weapon getWeapon(final ItemStack stack) {
        if (stack == null) return null;
        final String stackDisplayName = StringUtils.stripControlCodes(stack.getDisplayName());
        if (stackDisplayName == null) return null;
        return this.getWeapons()
            .filter(weapon -> stackDisplayName.startsWith(weapon.name))
            .findAny()
            .orElse(null);
    }

    private double getMinDamage(final Weapon weapon) {
        return this.minDamageProperties[weapon.type.ordinal()].getValue();
    }

    private Stream<Weapon> getWeapons() {
        return Arrays.stream(Weapon.values());
    }

    @Override
    public void onEnable() {
        this.bestShot = null;
        this.recoilCompensation = 0;
        this.clearCaches();
    }

    private void clearCaches() {
        Arrays.fill(this.clientSideShotTimes, 0);
        Arrays.fill(this.serverSideShotTimes, 0);
        this.tracers.clear();
        this.positionHistoryCache.clear();
    }

    @Override
    public void onDisable() {

    }

    @EventLink
    private final Listener<LoadWorldEvent> onLoadWorld = event -> {
        this.clearCaches();
    };

    private static class Shot {
        private final Vec3 src;
        private final Vec3 baseShotDst, predictionShotDst;
        private final boolean headshot;
        private final boolean wallbang;

        public Shot(Vec3 src, Vec3 baseShotDst, Vec3 predictionShotDst, boolean headshot, boolean wallbang) {
            this.src = src;
            this.baseShotDst = baseShotDst;
            this.predictionShotDst = predictionShotDst;
            this.headshot = headshot;
            this.wallbang = wallbang;
        }
    }

    private enum WeaponType {
        PISTOL("Pistol"),
        SHOTGUN("Shotgun"),
        RIFLE("Rifle"),
        SMG("SMG"),
        SNIPER("Sniper");

        private final String name;

        WeaponType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private final static class BulletTracer {
        private final Vec3 start, end;
        private final TracerType type;
        private final long timeOfCreation;

        BulletTracer(Vec3 start, Vec3 end, TracerType type) {
            this.start = start;
            this.end = end;
            this.type = type;
            this.timeOfCreation = System.currentTimeMillis();
        }
    }

    private enum TracerType {
        CLIENT, SERVER,
    }

    private static class Prediction {
        private final Vec3 src, dst;
        private final List<Vec3> samples;
        private final double confidence;
        private final double srcDistance;

        public Prediction(Vec3 src, Vec3 dst, List<Vec3> samples, double confidence, double srcDistance) {
            this.src = src;
            this.dst = dst;
            this.samples = samples;
            this.confidence = confidence;
            this.srcDistance = srcDistance;
        }
    }

    private static class TimestampedPosition implements Comparable<TimestampedPosition> {
        private final Vec3 position;
        private final long timestamp;

        public TimestampedPosition(Vec3 position) {
            this.position = position;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public int compareTo(TimestampedPosition other) {
            return Long.compare(this.timestamp, other.timestamp);
        }
    }

    private enum HitBoxType {
        HEAD("Head"), BODY("Body");

        private final String name;

        HitBoxType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Weapon {
        USP("USP", WeaponType.PISTOL, 1, 13, 41, 80, 400L),
        HK45("HK45", WeaponType.PISTOL, 1, 10, 44, 88, 250L),
        MP5("MP5", WeaponType.SMG, 3, 40, 1, 1, 50L),
        M4("M4", WeaponType.RIFLE, 2, 30, 33, 104, 100L),
        P90("P90", WeaponType.SMG, 3, 35, 24, 50, 100L),
        PUMP_ACTION("Pump Action", WeaponType.SHOTGUN, 2, 8, 1, 1, 1400L),
        SPAS("SPAS-12", WeaponType.SHOTGUN, 2, 10, 1, 1, 550L),
        DEAGLE("Desert Eagle", WeaponType.PISTOL, 3, 7, 50, 122, 400L),
        AUG("Steyr AUG", WeaponType.RIFLE, 4, 30, 1, 1, 500L),
        AWP("50 Cal", WeaponType.SNIPER, 1, 10, 80, 711, 950L),
        AK_47("AK-47", WeaponType.RIFLE, 4, 30, 44, 121, 150L);

        private final String name;
        private final WeaponType type;
        private final float recoil;
        private final int maxAmmo;
        private final double baseDamage;
        private final double headshotDamage;
        private final long delay;

        Weapon(String name, WeaponType type, int recoil, int maxAmmo, double baseDamage, double headshotDamage, long delay) {
            this.name = name;
            this.type = type;
            this.recoil = recoil;
            this.baseDamage = baseDamage;
            this.headshotDamage = headshotDamage;
            this.maxAmmo = maxAmmo;
            this.delay = delay;
        }
    }
}
