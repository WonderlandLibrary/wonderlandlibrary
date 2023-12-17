// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Collection;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import java.util.Iterator;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Sets;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Set;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry
{
    private static final Logger LOGGER;
    private final Entity trackedEntity;
    private final int range;
    private int maxRange;
    private final int updateFrequency;
    private long encodedPosX;
    private long encodedPosY;
    private long encodedPosZ;
    private int encodedRotationYaw;
    private int encodedRotationPitch;
    private int lastHeadMotion;
    private double lastTrackedEntityMotionX;
    private double lastTrackedEntityMotionY;
    private double motionZ;
    public int updateCounter;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean updatedPlayerVisibility;
    private final boolean sendVelocityUpdates;
    private int ticksSinceLastForcedTeleport;
    private List<Entity> passengers;
    private boolean ridingEntity;
    private boolean onGround;
    public boolean playerEntitiesUpdated;
    private final Set<EntityPlayerMP> trackingPlayers;
    
    public EntityTrackerEntry(final Entity entityIn, final int rangeIn, final int maxRangeIn, final int updateFrequencyIn, final boolean sendVelocityUpdatesIn) {
        this.passengers = Collections.emptyList();
        this.trackingPlayers = (Set<EntityPlayerMP>)Sets.newHashSet();
        this.trackedEntity = entityIn;
        this.range = rangeIn;
        this.maxRange = maxRangeIn;
        this.updateFrequency = updateFrequencyIn;
        this.sendVelocityUpdates = sendVelocityUpdatesIn;
        this.encodedPosX = EntityTracker.getPositionLong(entityIn.posX);
        this.encodedPosY = EntityTracker.getPositionLong(entityIn.posY);
        this.encodedPosZ = EntityTracker.getPositionLong(entityIn.posZ);
        this.encodedRotationYaw = MathHelper.floor(entityIn.rotationYaw * 256.0f / 360.0f);
        this.encodedRotationPitch = MathHelper.floor(entityIn.rotationPitch * 256.0f / 360.0f);
        this.lastHeadMotion = MathHelper.floor(entityIn.getRotationYawHead() * 256.0f / 360.0f);
        this.onGround = entityIn.onGround;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof EntityTrackerEntry && ((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId();
    }
    
    @Override
    public int hashCode() {
        return this.trackedEntity.getEntityId();
    }
    
    public void updatePlayerList(final List<EntityPlayer> players) {
        this.playerEntitiesUpdated = false;
        if (!this.updatedPlayerVisibility || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0) {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.updatedPlayerVisibility = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(players);
        }
        final List<Entity> list = this.trackedEntity.getPassengers();
        if (!list.equals(this.passengers)) {
            this.passengers = list;
            this.sendPacketToTrackedPlayers(new SPacketSetPassengers(this.trackedEntity));
        }
        if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
            final EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
            final ItemStack itemstack = entityitemframe.getDisplayedItem();
            if (itemstack.getItem() instanceof ItemMap) {
                final MapData mapdata = Items.FILLED_MAP.getMapData(itemstack, this.trackedEntity.world);
                for (final EntityPlayer entityplayer : players) {
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);
                    final Packet<?> packet = Items.FILLED_MAP.createMapDataPacket(itemstack, this.trackedEntity.world, entityplayermp);
                    if (packet != null) {
                        entityplayermp.connection.sendPacket(packet);
                    }
                }
            }
            this.sendMetadata();
        }
        if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataManager().isDirty()) {
            if (this.trackedEntity.isRiding()) {
                final int j1 = MathHelper.floor(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int l1 = MathHelper.floor(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final boolean flag3 = Math.abs(j1 - this.encodedRotationYaw) >= 1 || Math.abs(l1 - this.encodedRotationPitch) >= 1;
                if (flag3) {
                    this.sendPacketToTrackedPlayers(new SPacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)j1, (byte)l1, this.trackedEntity.onGround));
                    this.encodedRotationYaw = j1;
                    this.encodedRotationPitch = l1;
                }
                this.encodedPosX = EntityTracker.getPositionLong(this.trackedEntity.posX);
                this.encodedPosY = EntityTracker.getPositionLong(this.trackedEntity.posY);
                this.encodedPosZ = EntityTracker.getPositionLong(this.trackedEntity.posZ);
                this.sendMetadata();
                this.ridingEntity = true;
            }
            else {
                ++this.ticksSinceLastForcedTeleport;
                final long i1 = EntityTracker.getPositionLong(this.trackedEntity.posX);
                final long i2 = EntityTracker.getPositionLong(this.trackedEntity.posY);
                final long j2 = EntityTracker.getPositionLong(this.trackedEntity.posZ);
                final int k2 = MathHelper.floor(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int m = MathHelper.floor(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final long j3 = i1 - this.encodedPosX;
                final long k3 = i2 - this.encodedPosY;
                final long l2 = j2 - this.encodedPosZ;
                Packet<?> packet2 = null;
                final boolean flag4 = j3 * j3 + k3 * k3 + l2 * l2 >= 128L || this.updateCounter % 60 == 0;
                final boolean flag5 = Math.abs(k2 - this.encodedRotationYaw) >= 1 || Math.abs(m - this.encodedRotationPitch) >= 1;
                if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
                    if (j3 >= -32768L && j3 < 32768L && k3 >= -32768L && k3 < 32768L && l2 >= -32768L && l2 < 32768L && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.onGround == this.trackedEntity.onGround) {
                        if ((!flag4 || !flag5) && !(this.trackedEntity instanceof EntityArrow)) {
                            if (flag4) {
                                packet2 = new SPacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), j3, k3, l2, this.trackedEntity.onGround);
                            }
                            else if (flag5) {
                                packet2 = new SPacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)k2, (byte)m, this.trackedEntity.onGround);
                            }
                        }
                        else {
                            packet2 = new SPacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), j3, k3, l2, (byte)k2, (byte)m, this.trackedEntity.onGround);
                        }
                    }
                    else {
                        this.onGround = this.trackedEntity.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        this.resetPlayerVisibility();
                        packet2 = new SPacketEntityTeleport(this.trackedEntity);
                    }
                }
                boolean flag6 = this.sendVelocityUpdates;
                if (this.trackedEntity instanceof EntityLivingBase && ((EntityLivingBase)this.trackedEntity).isElytraFlying()) {
                    flag6 = true;
                }
                if (flag6 && this.updateCounter > 0) {
                    final double d0 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
                    final double d2 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
                    final double d3 = this.trackedEntity.motionZ - this.motionZ;
                    final double d4 = 0.02;
                    final double d5 = d0 * d0 + d2 * d2 + d3 * d3;
                    if (d5 > 4.0E-4 || (d5 > 0.0 && this.trackedEntity.motionX == 0.0 && this.trackedEntity.motionY == 0.0 && this.trackedEntity.motionZ == 0.0)) {
                        this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                        this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                        this.motionZ = this.trackedEntity.motionZ;
                        this.sendPacketToTrackedPlayers(new SPacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                    }
                }
                if (packet2 != null) {
                    this.sendPacketToTrackedPlayers(packet2);
                }
                this.sendMetadata();
                if (flag4) {
                    this.encodedPosX = i1;
                    this.encodedPosY = i2;
                    this.encodedPosZ = j2;
                }
                if (flag5) {
                    this.encodedRotationYaw = k2;
                    this.encodedRotationPitch = m;
                }
                this.ridingEntity = false;
            }
            final int k4 = MathHelper.floor(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            if (Math.abs(k4 - this.lastHeadMotion) >= 1) {
                this.sendPacketToTrackedPlayers(new SPacketEntityHeadLook(this.trackedEntity, (byte)k4));
                this.lastHeadMotion = k4;
            }
            this.trackedEntity.isAirBorne = false;
        }
        ++this.updateCounter;
        if (this.trackedEntity.velocityChanged) {
            this.sendToTrackingAndSelf(new SPacketEntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }
    
    private void sendMetadata() {
        final EntityDataManager entitydatamanager = this.trackedEntity.getDataManager();
        if (entitydatamanager.isDirty()) {
            this.sendToTrackingAndSelf(new SPacketEntityMetadata(this.trackedEntity.getEntityId(), entitydatamanager, false));
        }
        if (this.trackedEntity instanceof EntityLivingBase) {
            final AttributeMap attributemap = (AttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
            final Set<IAttributeInstance> set = attributemap.getDirtyInstances();
            if (!set.isEmpty()) {
                this.sendToTrackingAndSelf(new SPacketEntityProperties(this.trackedEntity.getEntityId(), set));
            }
            set.clear();
        }
    }
    
    public void sendPacketToTrackedPlayers(final Packet<?> packetIn) {
        for (final EntityPlayerMP entityplayermp : this.trackingPlayers) {
            entityplayermp.connection.sendPacket(packetIn);
        }
    }
    
    public void sendToTrackingAndSelf(final Packet<?> packetIn) {
        this.sendPacketToTrackedPlayers(packetIn);
        if (this.trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.trackedEntity).connection.sendPacket(packetIn);
        }
    }
    
    public void sendDestroyEntityPacketToTrackedPlayers() {
        for (final EntityPlayerMP entityplayermp : this.trackingPlayers) {
            this.trackedEntity.removeTrackingPlayer(entityplayermp);
            entityplayermp.removeEntity(this.trackedEntity);
        }
    }
    
    public void removeFromTrackedPlayers(final EntityPlayerMP playerMP) {
        if (this.trackingPlayers.contains(playerMP)) {
            this.trackedEntity.removeTrackingPlayer(playerMP);
            playerMP.removeEntity(this.trackedEntity);
            this.trackingPlayers.remove(playerMP);
        }
    }
    
    public void updatePlayerEntity(final EntityPlayerMP playerMP) {
        if (playerMP != this.trackedEntity) {
            if (this.isVisibleTo(playerMP)) {
                if (!this.trackingPlayers.contains(playerMP) && (this.isPlayerWatchingThisChunk(playerMP) || this.trackedEntity.forceSpawn)) {
                    this.trackingPlayers.add(playerMP);
                    final Packet<?> packet = this.createSpawnPacket();
                    playerMP.connection.sendPacket(packet);
                    if (!this.trackedEntity.getDataManager().isEmpty()) {
                        playerMP.connection.sendPacket(new SPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataManager(), true));
                    }
                    boolean flag = this.sendVelocityUpdates;
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final AttributeMap attributemap = (AttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
                        final Collection<IAttributeInstance> collection = attributemap.getWatchedAttributes();
                        if (!collection.isEmpty()) {
                            playerMP.connection.sendPacket(new SPacketEntityProperties(this.trackedEntity.getEntityId(), collection));
                        }
                        if (((EntityLivingBase)this.trackedEntity).isElytraFlying()) {
                            flag = true;
                        }
                    }
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;
                    if (flag && !(packet instanceof SPacketSpawnMob)) {
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                            final ItemStack itemstack = ((EntityLivingBase)this.trackedEntity).getItemStackFromSlot(entityequipmentslot);
                            if (!itemstack.isEmpty()) {
                                playerMP.connection.sendPacket(new SPacketEntityEquipment(this.trackedEntity.getEntityId(), entityequipmentslot, itemstack));
                            }
                        }
                    }
                    if (this.trackedEntity instanceof EntityPlayer) {
                        final EntityPlayer entityplayer = (EntityPlayer)this.trackedEntity;
                        if (entityplayer.isPlayerSleeping()) {
                            playerMP.connection.sendPacket(new SPacketUseBed(entityplayer, new BlockPos(this.trackedEntity)));
                        }
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final EntityLivingBase entitylivingbase = (EntityLivingBase)this.trackedEntity;
                        for (final PotionEffect potioneffect : entitylivingbase.getActivePotionEffects()) {
                            playerMP.connection.sendPacket(new SPacketEntityEffect(this.trackedEntity.getEntityId(), potioneffect));
                        }
                    }
                    if (!this.trackedEntity.getPassengers().isEmpty()) {
                        playerMP.connection.sendPacket(new SPacketSetPassengers(this.trackedEntity));
                    }
                    if (this.trackedEntity.isRiding()) {
                        playerMP.connection.sendPacket(new SPacketSetPassengers(this.trackedEntity.getRidingEntity()));
                    }
                    this.trackedEntity.addTrackingPlayer(playerMP);
                    playerMP.addEntity(this.trackedEntity);
                }
            }
            else if (this.trackingPlayers.contains(playerMP)) {
                this.trackingPlayers.remove(playerMP);
                this.trackedEntity.removeTrackingPlayer(playerMP);
                playerMP.removeEntity(this.trackedEntity);
            }
        }
    }
    
    public boolean isVisibleTo(final EntityPlayerMP playerMP) {
        final double d0 = playerMP.posX - this.encodedPosX / 4096.0;
        final double d2 = playerMP.posZ - this.encodedPosZ / 4096.0;
        final int i = Math.min(this.range, this.maxRange);
        return d0 >= -i && d0 <= i && d2 >= -i && d2 <= i && this.trackedEntity.isSpectatedByPlayer(playerMP);
    }
    
    private boolean isPlayerWatchingThisChunk(final EntityPlayerMP playerMP) {
        return playerMP.getServerWorld().getPlayerChunkMap().isPlayerWatchingChunk(playerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }
    
    public void updatePlayerEntities(final List<EntityPlayer> players) {
        for (int i = 0; i < players.size(); ++i) {
            this.updatePlayerEntity(players.get(i));
        }
    }
    
    private Packet<?> createSpawnPacket() {
        if (this.trackedEntity.isDead) {
            EntityTrackerEntry.LOGGER.warn("Fetching addPacket for removed entity");
        }
        if (this.trackedEntity instanceof EntityPlayerMP) {
            return new SPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
        }
        if (this.trackedEntity instanceof IAnimals) {
            this.lastHeadMotion = MathHelper.floor(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            return new SPacketSpawnMob((EntityLivingBase)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityPainting) {
            return new SPacketSpawnPainting((EntityPainting)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityItem) {
            return new SPacketSpawnObject(this.trackedEntity, 2, 1);
        }
        if (this.trackedEntity instanceof EntityMinecart) {
            final EntityMinecart entityminecart = (EntityMinecart)this.trackedEntity;
            return new SPacketSpawnObject(this.trackedEntity, 10, entityminecart.getType().getId());
        }
        if (this.trackedEntity instanceof EntityBoat) {
            return new SPacketSpawnObject(this.trackedEntity, 1);
        }
        if (this.trackedEntity instanceof EntityXPOrb) {
            return new SPacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityFishHook) {
            final Entity entity2 = ((EntityFishHook)this.trackedEntity).getAngler();
            return new SPacketSpawnObject(this.trackedEntity, 90, (entity2 == null) ? this.trackedEntity.getEntityId() : entity2.getEntityId());
        }
        if (this.trackedEntity instanceof EntitySpectralArrow) {
            final Entity entity3 = ((EntitySpectralArrow)this.trackedEntity).shootingEntity;
            return new SPacketSpawnObject(this.trackedEntity, 91, 1 + ((entity3 == null) ? this.trackedEntity.getEntityId() : entity3.getEntityId()));
        }
        if (this.trackedEntity instanceof EntityTippedArrow) {
            final Entity entity4 = ((EntityArrow)this.trackedEntity).shootingEntity;
            return new SPacketSpawnObject(this.trackedEntity, 60, 1 + ((entity4 == null) ? this.trackedEntity.getEntityId() : entity4.getEntityId()));
        }
        if (this.trackedEntity instanceof EntitySnowball) {
            return new SPacketSpawnObject(this.trackedEntity, 61);
        }
        if (this.trackedEntity instanceof EntityLlamaSpit) {
            return new SPacketSpawnObject(this.trackedEntity, 68);
        }
        if (this.trackedEntity instanceof EntityPotion) {
            return new SPacketSpawnObject(this.trackedEntity, 73);
        }
        if (this.trackedEntity instanceof EntityExpBottle) {
            return new SPacketSpawnObject(this.trackedEntity, 75);
        }
        if (this.trackedEntity instanceof EntityEnderPearl) {
            return new SPacketSpawnObject(this.trackedEntity, 65);
        }
        if (this.trackedEntity instanceof EntityEnderEye) {
            return new SPacketSpawnObject(this.trackedEntity, 72);
        }
        if (this.trackedEntity instanceof EntityFireworkRocket) {
            return new SPacketSpawnObject(this.trackedEntity, 76);
        }
        if (this.trackedEntity instanceof EntityFireball) {
            final EntityFireball entityfireball = (EntityFireball)this.trackedEntity;
            SPacketSpawnObject spacketspawnobject = null;
            int i = 63;
            if (this.trackedEntity instanceof EntitySmallFireball) {
                i = 64;
            }
            else if (this.trackedEntity instanceof EntityDragonFireball) {
                i = 93;
            }
            else if (this.trackedEntity instanceof EntityWitherSkull) {
                i = 66;
            }
            if (entityfireball.shootingEntity != null) {
                spacketspawnobject = new SPacketSpawnObject(this.trackedEntity, i, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
            }
            else {
                spacketspawnobject = new SPacketSpawnObject(this.trackedEntity, i, 0);
            }
            spacketspawnobject.setSpeedX((int)(entityfireball.accelerationX * 8000.0));
            spacketspawnobject.setSpeedY((int)(entityfireball.accelerationY * 8000.0));
            spacketspawnobject.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0));
            return spacketspawnobject;
        }
        if (this.trackedEntity instanceof EntityShulkerBullet) {
            final SPacketSpawnObject spacketspawnobject2 = new SPacketSpawnObject(this.trackedEntity, 67, 0);
            spacketspawnobject2.setSpeedX((int)(this.trackedEntity.motionX * 8000.0));
            spacketspawnobject2.setSpeedY((int)(this.trackedEntity.motionY * 8000.0));
            spacketspawnobject2.setSpeedZ((int)(this.trackedEntity.motionZ * 8000.0));
            return spacketspawnobject2;
        }
        if (this.trackedEntity instanceof EntityEgg) {
            return new SPacketSpawnObject(this.trackedEntity, 62);
        }
        if (this.trackedEntity instanceof EntityEvokerFangs) {
            return new SPacketSpawnObject(this.trackedEntity, 79);
        }
        if (this.trackedEntity instanceof EntityTNTPrimed) {
            return new SPacketSpawnObject(this.trackedEntity, 50);
        }
        if (this.trackedEntity instanceof EntityEnderCrystal) {
            return new SPacketSpawnObject(this.trackedEntity, 51);
        }
        if (this.trackedEntity instanceof EntityFallingBlock) {
            final EntityFallingBlock entityfallingblock = (EntityFallingBlock)this.trackedEntity;
            return new SPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(entityfallingblock.getBlock()));
        }
        if (this.trackedEntity instanceof EntityArmorStand) {
            return new SPacketSpawnObject(this.trackedEntity, 78);
        }
        if (this.trackedEntity instanceof EntityItemFrame) {
            final EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
            return new SPacketSpawnObject(this.trackedEntity, 71, entityitemframe.facingDirection.getHorizontalIndex(), entityitemframe.getHangingPosition());
        }
        if (this.trackedEntity instanceof EntityLeashKnot) {
            final EntityLeashKnot entityleashknot = (EntityLeashKnot)this.trackedEntity;
            return new SPacketSpawnObject(this.trackedEntity, 77, 0, entityleashknot.getHangingPosition());
        }
        if (this.trackedEntity instanceof EntityAreaEffectCloud) {
            return new SPacketSpawnObject(this.trackedEntity, 3);
        }
        throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
    }
    
    public void removeTrackedPlayerSymmetric(final EntityPlayerMP playerMP) {
        if (this.trackingPlayers.contains(playerMP)) {
            this.trackingPlayers.remove(playerMP);
            this.trackedEntity.removeTrackingPlayer(playerMP);
            playerMP.removeEntity(this.trackedEntity);
        }
    }
    
    public Entity getTrackedEntity() {
        return this.trackedEntity;
    }
    
    public void setMaxRange(final int maxRangeIn) {
        this.maxRange = maxRangeIn;
    }
    
    public void resetPlayerVisibility() {
        this.updatedPlayerVisibility = false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
