/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {
    public Set<EntityPlayerMP> trackingPlayers = Sets.newHashSet();
    public int encodedRotationYaw;
    private boolean onGround;
    public int encodedPosY;
    private int ticksSinceLastForcedTeleport;
    public int updateFrequency;
    private double lastTrackedEntityPosX;
    public int lastHeadMotion;
    public Entity trackedEntity;
    private double lastTrackedEntityPosZ;
    private boolean firstUpdateDone;
    public int updateCounter;
    private static final Logger logger = LogManager.getLogger();
    private double lastTrackedEntityPosY;
    public double lastTrackedEntityMotionY;
    public int encodedRotationPitch;
    public int encodedPosZ;
    public boolean playerEntitiesUpdated;
    private boolean ridingEntity;
    private boolean sendVelocityUpdates;
    public double lastTrackedEntityMotionX;
    private Entity field_85178_v;
    public int trackingDistanceThreshold;
    public int encodedPosX;
    public double motionZ;

    public void removeFromTrackedPlayers(EntityPlayerMP entityPlayerMP) {
        if (this.trackingPlayers.contains(entityPlayerMP)) {
            entityPlayerMP.removeEntity(this.trackedEntity);
            this.trackingPlayers.remove(entityPlayerMP);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers() {
        for (EntityPlayerMP entityPlayerMP : this.trackingPlayers) {
            entityPlayerMP.removeEntity(this.trackedEntity);
        }
    }

    public void updatePlayerEntity(EntityPlayerMP entityPlayerMP) {
        if (entityPlayerMP != this.trackedEntity) {
            if (this.func_180233_c(entityPlayerMP)) {
                if (!this.trackingPlayers.contains(entityPlayerMP) && (this.isPlayerWatchingThisChunk(entityPlayerMP) || this.trackedEntity.forceSpawn)) {
                    Object object;
                    Collection<IAttributeInstance> object22;
                    NBTTagCompound nBTTagCompound;
                    this.trackingPlayers.add(entityPlayerMP);
                    Packet packet = this.func_151260_c();
                    entityPlayerMP.playerNetServerHandler.sendPacket(packet);
                    if (!this.trackedEntity.getDataWatcher().getIsBlank()) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
                    }
                    if ((nBTTagCompound = this.trackedEntity.getNBTTagCompound()) != null) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), nBTTagCompound));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase && !(object22 = ((ServersideAttributeMap)(object = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap())).getWatchedAttributes()).isEmpty()) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), object22));
                    }
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;
                    if (this.sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob)) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }
                    if (this.trackedEntity.ridingEntity != null) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
                    }
                    if (this.trackedEntity instanceof EntityLiving && ((EntityLiving)this.trackedEntity).getLeashedToEntity() != null) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving)this.trackedEntity).getLeashedToEntity()));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        int n = 0;
                        while (n < 5) {
                            ItemStack itemStack = ((EntityLivingBase)this.trackedEntity).getEquipmentInSlot(n);
                            if (itemStack != null) {
                                entityPlayerMP.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), n, itemStack));
                            }
                            ++n;
                        }
                    }
                    if (this.trackedEntity instanceof EntityPlayer && ((EntityPlayer)(object = (EntityPlayer)this.trackedEntity)).isPlayerSleeping()) {
                        entityPlayerMP.playerNetServerHandler.sendPacket(new S0APacketUseBed((EntityPlayer)object, new BlockPos(this.trackedEntity)));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        object = (EntityLivingBase)this.trackedEntity;
                        for (PotionEffect potionEffect : ((EntityLivingBase)object).getActivePotionEffects()) {
                            entityPlayerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), potionEffect));
                        }
                    }
                }
            } else if (this.trackingPlayers.contains(entityPlayerMP)) {
                this.trackingPlayers.remove(entityPlayerMP);
                entityPlayerMP.removeEntity(this.trackedEntity);
            }
        }
    }

    public void updatePlayerList(List<EntityPlayer> list) {
        this.playerEntitiesUpdated = false;
        if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0) {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(list);
        }
        if (this.field_85178_v != this.trackedEntity.ridingEntity || this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0) {
            this.field_85178_v = this.trackedEntity.ridingEntity;
            this.sendPacketToTrackedPlayers(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
        }
        if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
            EntityItemFrame entityItemFrame = (EntityItemFrame)this.trackedEntity;
            ItemStack itemStack = entityItemFrame.getDisplayedItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemMap) {
                MapData mapData = Items.filled_map.getMapData(itemStack, this.trackedEntity.worldObj);
                for (EntityPlayer entityPlayer : list) {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityPlayer;
                    mapData.updateVisiblePlayers(entityPlayerMP, itemStack);
                    Packet packet = Items.filled_map.createMapDataPacket(itemStack, this.trackedEntity.worldObj, entityPlayerMP);
                    if (packet == null) continue;
                    entityPlayerMP.playerNetServerHandler.sendPacket(packet);
                }
            }
            this.sendMetadataToAllAssociatedPlayers();
        }
        if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataWatcher().hasObjectChanged()) {
            int n;
            if (this.trackedEntity.ridingEntity == null) {
                double d;
                double d2;
                double d3;
                double d4;
                double d5;
                boolean bl;
                ++this.ticksSinceLastForcedTeleport;
                n = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                int n2 = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                int n3 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                int n4 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                int n5 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                int n6 = n - this.encodedPosX;
                int n7 = n2 - this.encodedPosY;
                int n8 = n3 - this.encodedPosZ;
                Packet<INetHandlerPlayClient> packet = null;
                boolean bl2 = Math.abs(n6) >= 4 || Math.abs(n7) >= 4 || Math.abs(n8) >= 4 || this.updateCounter % 60 == 0;
                boolean bl3 = bl = Math.abs(n4 - this.encodedRotationYaw) >= 4 || Math.abs(n5 - this.encodedRotationPitch) >= 4;
                if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
                    if (n6 >= -128 && n6 < 128 && n7 >= -128 && n7 < 128 && n8 >= -128 && n8 < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.onGround == this.trackedEntity.onGround) {
                        if (!(bl2 && bl || this.trackedEntity instanceof EntityArrow)) {
                            if (bl2) {
                                packet = new S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte)n6, (byte)n7, (byte)n8, this.trackedEntity.onGround);
                            } else if (bl) {
                                packet = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)n4, (byte)n5, this.trackedEntity.onGround);
                            }
                        } else {
                            packet = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte)n6, (byte)n7, (byte)n8, (byte)n4, (byte)n5, this.trackedEntity.onGround);
                        }
                    } else {
                        this.onGround = this.trackedEntity.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        packet = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), n, n2, n3, (byte)n4, (byte)n5, this.trackedEntity.onGround);
                    }
                }
                if (this.sendVelocityUpdates && ((d5 = (d4 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX) * d4 + (d3 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY) * d3 + (d2 = this.trackedEntity.motionZ - this.motionZ) * d2) > (d = 0.02) * d || d5 > 0.0 && this.trackedEntity.motionX == 0.0 && this.trackedEntity.motionY == 0.0 && this.trackedEntity.motionZ == 0.0)) {
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;
                    this.sendPacketToTrackedPlayers(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                }
                if (packet != null) {
                    this.sendPacketToTrackedPlayers(packet);
                }
                this.sendMetadataToAllAssociatedPlayers();
                if (bl2) {
                    this.encodedPosX = n;
                    this.encodedPosY = n2;
                    this.encodedPosZ = n3;
                }
                if (bl) {
                    this.encodedRotationYaw = n4;
                    this.encodedRotationPitch = n5;
                }
                this.ridingEntity = false;
            } else {
                boolean bl;
                n = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                int n9 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                boolean bl4 = bl = Math.abs(n - this.encodedRotationYaw) >= 4 || Math.abs(n9 - this.encodedRotationPitch) >= 4;
                if (bl) {
                    this.sendPacketToTrackedPlayers(new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)n, (byte)n9, this.trackedEntity.onGround));
                    this.encodedRotationYaw = n;
                    this.encodedRotationPitch = n9;
                }
                this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }
            n = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            if (Math.abs(n - this.lastHeadMotion) >= 4) {
                this.sendPacketToTrackedPlayers(new S19PacketEntityHeadLook(this.trackedEntity, (byte)n));
                this.lastHeadMotion = n;
            }
            this.trackedEntity.isAirBorne = false;
        }
        ++this.updateCounter;
        if (this.trackedEntity.velocityChanged) {
            this.func_151261_b(new S12PacketEntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }

    public void sendPacketToTrackedPlayers(Packet packet) {
        for (EntityPlayerMP entityPlayerMP : this.trackingPlayers) {
            entityPlayerMP.playerNetServerHandler.sendPacket(packet);
        }
    }

    private void sendMetadataToAllAssociatedPlayers() {
        DataWatcher dataWatcher = this.trackedEntity.getDataWatcher();
        if (dataWatcher.hasObjectChanged()) {
            this.func_151261_b(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), dataWatcher, false));
        }
        if (this.trackedEntity instanceof EntityLivingBase) {
            ServersideAttributeMap serversideAttributeMap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
            Set<IAttributeInstance> set = serversideAttributeMap.getAttributeInstanceSet();
            if (!set.isEmpty()) {
                this.func_151261_b(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), set));
            }
            set.clear();
        }
    }

    public EntityTrackerEntry(Entity entity, int n, int n2, boolean bl) {
        this.trackedEntity = entity;
        this.trackingDistanceThreshold = n;
        this.updateFrequency = n2;
        this.sendVelocityUpdates = bl;
        this.encodedPosX = MathHelper.floor_double(entity.posX * 32.0);
        this.encodedPosY = MathHelper.floor_double(entity.posY * 32.0);
        this.encodedPosZ = MathHelper.floor_double(entity.posZ * 32.0);
        this.encodedRotationYaw = MathHelper.floor_float(entity.rotationYaw * 256.0f / 360.0f);
        this.encodedRotationPitch = MathHelper.floor_float(entity.rotationPitch * 256.0f / 360.0f);
        this.lastHeadMotion = MathHelper.floor_float(entity.getRotationYawHead() * 256.0f / 360.0f);
        this.onGround = entity.onGround;
    }

    private Packet func_151260_c() {
        if (this.trackedEntity.isDead) {
            logger.warn("Fetching addPacket for removed entity");
        }
        if (this.trackedEntity instanceof EntityItem) {
            return new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
        }
        if (this.trackedEntity instanceof EntityPlayerMP) {
            return new S0CPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityMinecart) {
            EntityMinecart entityMinecart = (EntityMinecart)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 10, entityMinecart.getMinecartType().getNetworkID());
        }
        if (this.trackedEntity instanceof EntityBoat) {
            return new S0EPacketSpawnObject(this.trackedEntity, 1);
        }
        if (this.trackedEntity instanceof IAnimals) {
            this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            return new S0FPacketSpawnMob((EntityLivingBase)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityFishHook) {
            EntityPlayer entityPlayer = ((EntityFishHook)this.trackedEntity).angler;
            return new S0EPacketSpawnObject(this.trackedEntity, 90, entityPlayer != null ? entityPlayer.getEntityId() : this.trackedEntity.getEntityId());
        }
        if (this.trackedEntity instanceof EntityArrow) {
            Entity entity = ((EntityArrow)this.trackedEntity).shootingEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 60, entity != null ? entity.getEntityId() : this.trackedEntity.getEntityId());
        }
        if (this.trackedEntity instanceof EntitySnowball) {
            return new S0EPacketSpawnObject(this.trackedEntity, 61);
        }
        if (this.trackedEntity instanceof EntityPotion) {
            return new S0EPacketSpawnObject(this.trackedEntity, 73, ((EntityPotion)this.trackedEntity).getPotionDamage());
        }
        if (this.trackedEntity instanceof EntityExpBottle) {
            return new S0EPacketSpawnObject(this.trackedEntity, 75);
        }
        if (this.trackedEntity instanceof EntityEnderPearl) {
            return new S0EPacketSpawnObject(this.trackedEntity, 65);
        }
        if (this.trackedEntity instanceof EntityEnderEye) {
            return new S0EPacketSpawnObject(this.trackedEntity, 72);
        }
        if (this.trackedEntity instanceof EntityFireworkRocket) {
            return new S0EPacketSpawnObject(this.trackedEntity, 76);
        }
        if (this.trackedEntity instanceof EntityFireball) {
            EntityFireball entityFireball = (EntityFireball)this.trackedEntity;
            S0EPacketSpawnObject s0EPacketSpawnObject = null;
            int n = 63;
            if (this.trackedEntity instanceof EntitySmallFireball) {
                n = 64;
            } else if (this.trackedEntity instanceof EntityWitherSkull) {
                n = 66;
            }
            s0EPacketSpawnObject = entityFireball.shootingEntity != null ? new S0EPacketSpawnObject(this.trackedEntity, n, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId()) : new S0EPacketSpawnObject(this.trackedEntity, n, 0);
            s0EPacketSpawnObject.setSpeedX((int)(entityFireball.accelerationX * 8000.0));
            s0EPacketSpawnObject.setSpeedY((int)(entityFireball.accelerationY * 8000.0));
            s0EPacketSpawnObject.setSpeedZ((int)(entityFireball.accelerationZ * 8000.0));
            return s0EPacketSpawnObject;
        }
        if (this.trackedEntity instanceof EntityEgg) {
            return new S0EPacketSpawnObject(this.trackedEntity, 62);
        }
        if (this.trackedEntity instanceof EntityTNTPrimed) {
            return new S0EPacketSpawnObject(this.trackedEntity, 50);
        }
        if (this.trackedEntity instanceof EntityEnderCrystal) {
            return new S0EPacketSpawnObject(this.trackedEntity, 51);
        }
        if (this.trackedEntity instanceof EntityFallingBlock) {
            EntityFallingBlock entityFallingBlock = (EntityFallingBlock)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(entityFallingBlock.getBlock()));
        }
        if (this.trackedEntity instanceof EntityArmorStand) {
            return new S0EPacketSpawnObject(this.trackedEntity, 78);
        }
        if (this.trackedEntity instanceof EntityPainting) {
            return new S10PacketSpawnPainting((EntityPainting)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityItemFrame) {
            EntityItemFrame entityItemFrame = (EntityItemFrame)this.trackedEntity;
            S0EPacketSpawnObject s0EPacketSpawnObject = new S0EPacketSpawnObject(this.trackedEntity, 71, entityItemFrame.facingDirection.getHorizontalIndex());
            BlockPos blockPos = entityItemFrame.getHangingPosition();
            s0EPacketSpawnObject.setX(MathHelper.floor_float(blockPos.getX() * 32));
            s0EPacketSpawnObject.setY(MathHelper.floor_float(blockPos.getY() * 32));
            s0EPacketSpawnObject.setZ(MathHelper.floor_float(blockPos.getZ() * 32));
            return s0EPacketSpawnObject;
        }
        if (this.trackedEntity instanceof EntityLeashKnot) {
            EntityLeashKnot entityLeashKnot = (EntityLeashKnot)this.trackedEntity;
            S0EPacketSpawnObject s0EPacketSpawnObject = new S0EPacketSpawnObject(this.trackedEntity, 77);
            BlockPos blockPos = entityLeashKnot.getHangingPosition();
            s0EPacketSpawnObject.setX(MathHelper.floor_float(blockPos.getX() * 32));
            s0EPacketSpawnObject.setY(MathHelper.floor_float(blockPos.getY() * 32));
            s0EPacketSpawnObject.setZ(MathHelper.floor_float(blockPos.getZ() * 32));
            return s0EPacketSpawnObject;
        }
        if (this.trackedEntity instanceof EntityXPOrb) {
            return new S11PacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
        }
        throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
    }

    public void removeTrackedPlayerSymmetric(EntityPlayerMP entityPlayerMP) {
        if (this.trackingPlayers.contains(entityPlayerMP)) {
            this.trackingPlayers.remove(entityPlayerMP);
            entityPlayerMP.removeEntity(this.trackedEntity);
        }
    }

    public boolean func_180233_c(EntityPlayerMP entityPlayerMP) {
        double d = entityPlayerMP.posX - (double)(this.encodedPosX / 32);
        double d2 = entityPlayerMP.posZ - (double)(this.encodedPosZ / 32);
        return d >= (double)(-this.trackingDistanceThreshold) && d <= (double)this.trackingDistanceThreshold && d2 >= (double)(-this.trackingDistanceThreshold) && d2 <= (double)this.trackingDistanceThreshold && this.trackedEntity.isSpectatedByPlayer(entityPlayerMP);
    }

    public void func_151261_b(Packet packet) {
        this.sendPacketToTrackedPlayers(packet);
        if (this.trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(packet);
        }
    }

    public void updatePlayerEntities(List<EntityPlayer> list) {
        int n = 0;
        while (n < list.size()) {
            this.updatePlayerEntity((EntityPlayerMP)list.get(n));
            ++n;
        }
    }

    private boolean isPlayerWatchingThisChunk(EntityPlayerMP entityPlayerMP) {
        return entityPlayerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(entityPlayerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry)object).trackedEntity.getEntityId() == this.trackedEntity.getEntityId() : false;
    }

    public int hashCode() {
        return this.trackedEntity.getEntityId();
    }
}

