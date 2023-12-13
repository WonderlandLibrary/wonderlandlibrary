// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import java.util.Collection;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import java.util.Iterator;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import java.util.List;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry
{
    private static final Logger logger;
    public Entity trackedEntity;
    public int trackingDistanceThreshold;
    public int updateFrequency;
    public int encodedPosX;
    public int encodedPosY;
    public int encodedPosZ;
    public int encodedRotationYaw;
    public int encodedRotationPitch;
    public int lastHeadMotion;
    public double lastTrackedEntityMotionX;
    public double lastTrackedEntityMotionY;
    public double motionZ;
    public int updateCounter;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean firstUpdateDone;
    private boolean sendVelocityUpdates;
    private int ticksSinceLastForcedTeleport;
    private Entity field_85178_v;
    private boolean ridingEntity;
    private boolean field_180234_y;
    public boolean playerEntitiesUpdated;
    public Set trackingPlayers;
    private static final String __OBFID = "CL_00001443";
    
    public EntityTrackerEntry(final Entity p_i1525_1_, final int p_i1525_2_, final int p_i1525_3_, final boolean p_i1525_4_) {
        this.trackingPlayers = Sets.newHashSet();
        this.trackedEntity = p_i1525_1_;
        this.trackingDistanceThreshold = p_i1525_2_;
        this.updateFrequency = p_i1525_3_;
        this.sendVelocityUpdates = p_i1525_4_;
        this.encodedPosX = MathHelper.floor_double(p_i1525_1_.posX * 32.0);
        this.encodedPosY = MathHelper.floor_double(p_i1525_1_.posY * 32.0);
        this.encodedPosZ = MathHelper.floor_double(p_i1525_1_.posZ * 32.0);
        this.encodedRotationYaw = MathHelper.floor_float(p_i1525_1_.rotationYaw * 256.0f / 360.0f);
        this.encodedRotationPitch = MathHelper.floor_float(p_i1525_1_.rotationPitch * 256.0f / 360.0f);
        this.lastHeadMotion = MathHelper.floor_float(p_i1525_1_.getRotationYawHead() * 256.0f / 360.0f);
        this.field_180234_y = p_i1525_1_.onGround;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof EntityTrackerEntry && ((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId();
    }
    
    @Override
    public int hashCode() {
        return this.trackedEntity.getEntityId();
    }
    
    public void updatePlayerList(final List p_73122_1_) {
        this.playerEntitiesUpdated = false;
        if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0) {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(p_73122_1_);
        }
        if (this.field_85178_v != this.trackedEntity.ridingEntity || (this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0)) {
            this.field_85178_v = this.trackedEntity.ridingEntity;
            this.func_151259_a(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
        }
        if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
            final EntityItemFrame var2 = (EntityItemFrame)this.trackedEntity;
            final ItemStack var3 = var2.getDisplayedItem();
            if (var3 != null && var3.getItem() instanceof ItemMap) {
                final MapData var4 = Items.filled_map.getMapData(var3, this.trackedEntity.worldObj);
                for (final EntityPlayer var6 : p_73122_1_) {
                    final EntityPlayerMP var7 = (EntityPlayerMP)var6;
                    var4.updateVisiblePlayers(var7, var3);
                    final Packet var8 = Items.filled_map.createMapDataPacket(var3, this.trackedEntity.worldObj, var7);
                    if (var8 != null) {
                        var7.playerNetServerHandler.sendPacket(var8);
                    }
                }
            }
            this.sendMetadataToAllAssociatedPlayers();
        }
        if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataWatcher().hasObjectChanged()) {
            if (this.trackedEntity.ridingEntity == null) {
                ++this.ticksSinceLastForcedTeleport;
                final int var9 = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                final int var10 = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                final int var11 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                final int var12 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int var13 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final int var14 = var9 - this.encodedPosX;
                final int var15 = var10 - this.encodedPosY;
                final int var16 = var11 - this.encodedPosZ;
                Object var17 = null;
                final boolean var18 = Math.abs(var14) >= 4 || Math.abs(var15) >= 4 || Math.abs(var16) >= 4 || this.updateCounter % 60 == 0;
                final boolean var19 = Math.abs(var12 - this.encodedRotationYaw) >= 4 || Math.abs(var13 - this.encodedRotationPitch) >= 4;
                if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
                    if (var14 >= -128 && var14 < 128 && var15 >= -128 && var15 < 128 && var16 >= -128 && var16 < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.field_180234_y == this.trackedEntity.onGround) {
                        if (var18 && var19) {
                            var17 = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte)var14, (byte)var15, (byte)var16, (byte)var12, (byte)var13, this.trackedEntity.onGround);
                        }
                        else if (var18) {
                            var17 = new S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte)var14, (byte)var15, (byte)var16, this.trackedEntity.onGround);
                        }
                        else if (var19) {
                            var17 = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)var12, (byte)var13, this.trackedEntity.onGround);
                        }
                    }
                    else {
                        this.field_180234_y = this.trackedEntity.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        var17 = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), var9, var10, var11, (byte)var12, (byte)var13, this.trackedEntity.onGround);
                    }
                }
                if (this.sendVelocityUpdates) {
                    final double var20 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
                    final double var21 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
                    final double var22 = this.trackedEntity.motionZ - this.motionZ;
                    final double var23 = 0.02;
                    final double var24 = var20 * var20 + var21 * var21 + var22 * var22;
                    if (var24 > var23 * var23 || (var24 > 0.0 && this.trackedEntity.motionX == 0.0 && this.trackedEntity.motionY == 0.0 && this.trackedEntity.motionZ == 0.0)) {
                        this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                        this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                        this.motionZ = this.trackedEntity.motionZ;
                        this.func_151259_a(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                    }
                }
                if (var17 != null) {
                    this.func_151259_a((Packet)var17);
                }
                this.sendMetadataToAllAssociatedPlayers();
                if (var18) {
                    this.encodedPosX = var9;
                    this.encodedPosY = var10;
                    this.encodedPosZ = var11;
                }
                if (var19) {
                    this.encodedRotationYaw = var12;
                    this.encodedRotationPitch = var13;
                }
                this.ridingEntity = false;
            }
            else {
                final int var9 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int var10 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final boolean var25 = Math.abs(var9 - this.encodedRotationYaw) >= 4 || Math.abs(var10 - this.encodedRotationPitch) >= 4;
                if (var25) {
                    this.func_151259_a(new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)var9, (byte)var10, this.trackedEntity.onGround));
                    this.encodedRotationYaw = var9;
                    this.encodedRotationPitch = var10;
                }
                this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }
            final int var9 = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            if (Math.abs(var9 - this.lastHeadMotion) >= 4) {
                this.func_151259_a(new S19PacketEntityHeadLook(this.trackedEntity, (byte)var9));
                this.lastHeadMotion = var9;
            }
            this.trackedEntity.isAirBorne = false;
        }
        ++this.updateCounter;
        if (this.trackedEntity.velocityChanged) {
            this.func_151261_b(new S12PacketEntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }
    
    private void sendMetadataToAllAssociatedPlayers() {
        final DataWatcher var1 = this.trackedEntity.getDataWatcher();
        if (var1.hasObjectChanged()) {
            this.func_151261_b(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), var1, false));
        }
        if (this.trackedEntity instanceof EntityLivingBase) {
            final ServersideAttributeMap var2 = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
            final Set var3 = var2.getAttributeInstanceSet();
            if (!var3.isEmpty()) {
                this.func_151261_b(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), var3));
            }
            var3.clear();
        }
    }
    
    public void func_151259_a(final Packet p_151259_1_) {
        for (final EntityPlayerMP var3 : this.trackingPlayers) {
            var3.playerNetServerHandler.sendPacket(p_151259_1_);
        }
    }
    
    public void func_151261_b(final Packet p_151261_1_) {
        this.func_151259_a(p_151261_1_);
        if (this.trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(p_151261_1_);
        }
    }
    
    public void sendDestroyEntityPacketToTrackedPlayers() {
        for (final EntityPlayerMP var2 : this.trackingPlayers) {
            var2.func_152339_d(this.trackedEntity);
        }
    }
    
    public void removeFromTrackedPlayers(final EntityPlayerMP p_73118_1_) {
        if (this.trackingPlayers.contains(p_73118_1_)) {
            p_73118_1_.func_152339_d(this.trackedEntity);
            this.trackingPlayers.remove(p_73118_1_);
        }
    }
    
    public void updatePlayerEntity(final EntityPlayerMP p_73117_1_) {
        if (p_73117_1_ != this.trackedEntity) {
            if (this.func_180233_c(p_73117_1_)) {
                if (!this.trackingPlayers.contains(p_73117_1_) && (this.isPlayerWatchingThisChunk(p_73117_1_) || this.trackedEntity.forceSpawn)) {
                    this.trackingPlayers.add(p_73117_1_);
                    final Packet var2 = this.func_151260_c();
                    p_73117_1_.playerNetServerHandler.sendPacket(var2);
                    if (!this.trackedEntity.getDataWatcher().getIsBlank()) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
                    }
                    final NBTTagCompound var3 = this.trackedEntity.func_174819_aU();
                    if (var3 != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), var3));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final ServersideAttributeMap var4 = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
                        final Collection var5 = var4.getWatchedAttributes();
                        if (!var5.isEmpty()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), var5));
                        }
                    }
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;
                    if (this.sendVelocityUpdates && !(var2 instanceof S0FPacketSpawnMob)) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }
                    if (this.trackedEntity.ridingEntity != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
                    }
                    if (this.trackedEntity instanceof EntityLiving && ((EntityLiving)this.trackedEntity).getLeashedToEntity() != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving)this.trackedEntity).getLeashedToEntity()));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        for (int var6 = 0; var6 < 5; ++var6) {
                            final ItemStack var7 = ((EntityLivingBase)this.trackedEntity).getEquipmentInSlot(var6);
                            if (var7 != null) {
                                p_73117_1_.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), var6, var7));
                            }
                        }
                    }
                    if (this.trackedEntity instanceof EntityPlayer) {
                        final EntityPlayer var8 = (EntityPlayer)this.trackedEntity;
                        if (var8.isPlayerSleeping()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S0APacketUseBed(var8, new BlockPos(this.trackedEntity)));
                        }
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final EntityLivingBase var9 = (EntityLivingBase)this.trackedEntity;
                        for (final PotionEffect var11 : var9.getActivePotionEffects()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), var11));
                        }
                    }
                }
            }
            else if (this.trackingPlayers.contains(p_73117_1_)) {
                this.trackingPlayers.remove(p_73117_1_);
                p_73117_1_.func_152339_d(this.trackedEntity);
            }
        }
    }
    
    public boolean func_180233_c(final EntityPlayerMP p_180233_1_) {
        final double var2 = p_180233_1_.posX - this.encodedPosX / 32;
        final double var3 = p_180233_1_.posZ - this.encodedPosZ / 32;
        return var2 >= -this.trackingDistanceThreshold && var2 <= this.trackingDistanceThreshold && var3 >= -this.trackingDistanceThreshold && var3 <= this.trackingDistanceThreshold && this.trackedEntity.func_174827_a(p_180233_1_);
    }
    
    private boolean isPlayerWatchingThisChunk(final EntityPlayerMP p_73121_1_) {
        return p_73121_1_.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(p_73121_1_, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }
    
    public void updatePlayerEntities(final List p_73125_1_) {
        for (int var2 = 0; var2 < p_73125_1_.size(); ++var2) {
            this.updatePlayerEntity(p_73125_1_.get(var2));
        }
    }
    
    private Packet func_151260_c() {
        if (this.trackedEntity.isDead) {
            EntityTrackerEntry.logger.warn("Fetching addPacket for removed entity");
        }
        if (this.trackedEntity instanceof EntityItem) {
            return new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
        }
        if (this.trackedEntity instanceof EntityPlayerMP) {
            return new S0CPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityMinecart) {
            final EntityMinecart var9 = (EntityMinecart)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 10, var9.func_180456_s().func_180039_a());
        }
        if (this.trackedEntity instanceof EntityBoat) {
            return new S0EPacketSpawnObject(this.trackedEntity, 1);
        }
        if (this.trackedEntity instanceof IAnimals) {
            this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            return new S0FPacketSpawnMob((EntityLivingBase)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityFishHook) {
            final EntityPlayer var10 = ((EntityFishHook)this.trackedEntity).angler;
            return new S0EPacketSpawnObject(this.trackedEntity, 90, (var10 != null) ? var10.getEntityId() : this.trackedEntity.getEntityId());
        }
        if (this.trackedEntity instanceof EntityArrow) {
            final Entity var11 = ((EntityArrow)this.trackedEntity).shootingEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 60, (var11 != null) ? var11.getEntityId() : this.trackedEntity.getEntityId());
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
            final EntityFireball var12 = (EntityFireball)this.trackedEntity;
            S0EPacketSpawnObject var13 = null;
            byte var14 = 63;
            if (this.trackedEntity instanceof EntitySmallFireball) {
                var14 = 64;
            }
            else if (this.trackedEntity instanceof EntityWitherSkull) {
                var14 = 66;
            }
            if (var12.shootingEntity != null) {
                var13 = new S0EPacketSpawnObject(this.trackedEntity, var14, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
            }
            else {
                var13 = new S0EPacketSpawnObject(this.trackedEntity, var14, 0);
            }
            var13.func_149003_d((int)(var12.accelerationX * 8000.0));
            var13.func_149000_e((int)(var12.accelerationY * 8000.0));
            var13.func_149007_f((int)(var12.accelerationZ * 8000.0));
            return var13;
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
            final EntityFallingBlock var15 = (EntityFallingBlock)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(var15.getBlock()));
        }
        if (this.trackedEntity instanceof EntityArmorStand) {
            return new S0EPacketSpawnObject(this.trackedEntity, 78);
        }
        if (this.trackedEntity instanceof EntityPainting) {
            return new S10PacketSpawnPainting((EntityPainting)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityItemFrame) {
            final EntityItemFrame var16 = (EntityItemFrame)this.trackedEntity;
            final S0EPacketSpawnObject var13 = new S0EPacketSpawnObject(this.trackedEntity, 71, var16.field_174860_b.getHorizontalIndex());
            final BlockPos var17 = var16.func_174857_n();
            var13.func_148996_a(MathHelper.floor_float(var17.getX() * 32));
            var13.func_148995_b(MathHelper.floor_float(var17.getY() * 32));
            var13.func_149005_c(MathHelper.floor_float(var17.getZ() * 32));
            return var13;
        }
        if (this.trackedEntity instanceof EntityLeashKnot) {
            final EntityLeashKnot var18 = (EntityLeashKnot)this.trackedEntity;
            final S0EPacketSpawnObject var13 = new S0EPacketSpawnObject(this.trackedEntity, 77);
            final BlockPos var17 = var18.func_174857_n();
            var13.func_148996_a(MathHelper.floor_float(var17.getX() * 32));
            var13.func_148995_b(MathHelper.floor_float(var17.getY() * 32));
            var13.func_149005_c(MathHelper.floor_float(var17.getZ() * 32));
            return var13;
        }
        if (this.trackedEntity instanceof EntityXPOrb) {
            return new S11PacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
        }
        throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
    }
    
    public void removeTrackedPlayerSymmetric(final EntityPlayerMP p_73123_1_) {
        if (this.trackingPlayers.contains(p_73123_1_)) {
            this.trackingPlayers.remove(p_73123_1_);
            p_73123_1_.func_152339_d(this.trackedEntity);
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
