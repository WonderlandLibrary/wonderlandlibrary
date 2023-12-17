// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Sets;
import net.minecraft.util.IntHashMap;
import java.util.Set;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

public class EntityTracker
{
    private static final Logger LOGGER;
    private final WorldServer world;
    private final Set<EntityTrackerEntry> entries;
    private final IntHashMap<EntityTrackerEntry> trackedEntityHashTable;
    private int maxTrackingDistanceThreshold;
    
    public EntityTracker(final WorldServer theWorldIn) {
        this.entries = (Set<EntityTrackerEntry>)Sets.newHashSet();
        this.trackedEntityHashTable = new IntHashMap<EntityTrackerEntry>();
        this.world = theWorldIn;
        this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getPlayerList().getEntityViewDistance();
    }
    
    public static long getPositionLong(final double value) {
        return MathHelper.lfloor(value * 4096.0);
    }
    
    public static void updateServerPosition(final Entity entityIn, final double x, final double y, final double z) {
        entityIn.serverPosX = getPositionLong(x);
        entityIn.serverPosY = getPositionLong(y);
        entityIn.serverPosZ = getPositionLong(z);
    }
    
    public void track(final Entity entityIn) {
        if (entityIn instanceof EntityPlayerMP) {
            this.track(entityIn, 512, 2);
            final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
            for (final EntityTrackerEntry entitytrackerentry : this.entries) {
                if (entitytrackerentry.getTrackedEntity() != entityplayermp) {
                    entitytrackerentry.updatePlayerEntity(entityplayermp);
                }
            }
        }
        else if (entityIn instanceof EntityFishHook) {
            this.track(entityIn, 64, 5, true);
        }
        else if (entityIn instanceof EntityArrow) {
            this.track(entityIn, 64, 20, false);
        }
        else if (entityIn instanceof EntitySmallFireball) {
            this.track(entityIn, 64, 10, false);
        }
        else if (entityIn instanceof EntityFireball) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntitySnowball) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityLlamaSpit) {
            this.track(entityIn, 64, 10, false);
        }
        else if (entityIn instanceof EntityEnderPearl) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityEnderEye) {
            this.track(entityIn, 64, 4, true);
        }
        else if (entityIn instanceof EntityEgg) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityPotion) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityExpBottle) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityFireworkRocket) {
            this.track(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityItem) {
            this.track(entityIn, 64, 20, true);
        }
        else if (entityIn instanceof EntityMinecart) {
            this.track(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityBoat) {
            this.track(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntitySquid) {
            this.track(entityIn, 64, 3, true);
        }
        else if (entityIn instanceof EntityWither) {
            this.track(entityIn, 80, 3, false);
        }
        else if (entityIn instanceof EntityShulkerBullet) {
            this.track(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityBat) {
            this.track(entityIn, 80, 3, false);
        }
        else if (entityIn instanceof EntityDragon) {
            this.track(entityIn, 160, 3, true);
        }
        else if (entityIn instanceof IAnimals) {
            this.track(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityTNTPrimed) {
            this.track(entityIn, 160, 10, true);
        }
        else if (entityIn instanceof EntityFallingBlock) {
            this.track(entityIn, 160, 20, true);
        }
        else if (entityIn instanceof EntityHanging) {
            this.track(entityIn, 160, Integer.MAX_VALUE, false);
        }
        else if (entityIn instanceof EntityArmorStand) {
            this.track(entityIn, 160, 3, true);
        }
        else if (entityIn instanceof EntityXPOrb) {
            this.track(entityIn, 160, 20, true);
        }
        else if (entityIn instanceof EntityAreaEffectCloud) {
            this.track(entityIn, 160, Integer.MAX_VALUE, true);
        }
        else if (entityIn instanceof EntityEnderCrystal) {
            this.track(entityIn, 256, Integer.MAX_VALUE, false);
        }
        else if (entityIn instanceof EntityEvokerFangs) {
            this.track(entityIn, 160, 2, false);
        }
    }
    
    public void track(final Entity entityIn, final int trackingRange, final int updateFrequency) {
        this.track(entityIn, trackingRange, updateFrequency, false);
    }
    
    public void track(final Entity entityIn, final int trackingRange, final int updateFrequency, final boolean sendVelocityUpdates) {
        try {
            if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }
            final EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, this.maxTrackingDistanceThreshold, updateFrequency, sendVelocityUpdates);
            this.entries.add(entitytrackerentry);
            this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
            entitytrackerentry.updatePlayerEntities(this.world.playerEntities);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
            crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
            crashreportcategory.addDetail("Update interval", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    String s = "Once per " + updateFrequency + " ticks";
                    if (updateFrequency == Integer.MAX_VALUE) {
                        s = "Maximum (" + s + ")";
                    }
                    return s;
                }
            });
            entityIn.addEntityCrashInfo(crashreportcategory);
            this.trackedEntityHashTable.lookup(entityIn.getEntityId()).getTrackedEntity().addEntityCrashInfo(crashreport.makeCategory("Entity That Is Already Tracked"));
            try {
                throw new ReportedException(crashreport);
            }
            catch (ReportedException reportedexception) {
                EntityTracker.LOGGER.error("\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
            }
        }
    }
    
    public void untrack(final Entity entityIn) {
        if (entityIn instanceof EntityPlayerMP) {
            final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
            for (final EntityTrackerEntry entitytrackerentry : this.entries) {
                entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
            }
        }
        final EntityTrackerEntry entitytrackerentry2 = this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
        if (entitytrackerentry2 != null) {
            this.entries.remove(entitytrackerentry2);
            entitytrackerentry2.sendDestroyEntityPacketToTrackedPlayers();
        }
    }
    
    public void tick() {
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (final EntityTrackerEntry entitytrackerentry : this.entries) {
            entitytrackerentry.updatePlayerList(this.world.playerEntities);
            if (entitytrackerentry.playerEntitiesUpdated) {
                final Entity entity = entitytrackerentry.getTrackedEntity();
                if (!(entity instanceof EntityPlayerMP)) {
                    continue;
                }
                list.add((EntityPlayerMP)entity);
            }
        }
        for (int i = 0; i < list.size(); ++i) {
            final EntityPlayerMP entityplayermp = list.get(i);
            for (final EntityTrackerEntry entitytrackerentry2 : this.entries) {
                if (entitytrackerentry2.getTrackedEntity() != entityplayermp) {
                    entitytrackerentry2.updatePlayerEntity(entityplayermp);
                }
            }
        }
    }
    
    public void updateVisibility(final EntityPlayerMP player) {
        for (final EntityTrackerEntry entitytrackerentry : this.entries) {
            if (entitytrackerentry.getTrackedEntity() == player) {
                entitytrackerentry.updatePlayerEntities(this.world.playerEntities);
            }
            else {
                entitytrackerentry.updatePlayerEntity(player);
            }
        }
    }
    
    public void sendToTracking(final Entity entityIn, final Packet<?> packetIn) {
        final EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.lookup(entityIn.getEntityId());
        if (entitytrackerentry != null) {
            entitytrackerentry.sendPacketToTrackedPlayers(packetIn);
        }
    }
    
    public void sendToTrackingAndSelf(final Entity entityIn, final Packet<?> packetIn) {
        final EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.lookup(entityIn.getEntityId());
        if (entitytrackerentry != null) {
            entitytrackerentry.sendToTrackingAndSelf(packetIn);
        }
    }
    
    public void removePlayerFromTrackers(final EntityPlayerMP player) {
        for (final EntityTrackerEntry entitytrackerentry : this.entries) {
            entitytrackerentry.removeTrackedPlayerSymmetric(player);
        }
    }
    
    public void sendLeashedEntitiesInChunk(final EntityPlayerMP player, final Chunk chunkIn) {
        final List<Entity> list = (List<Entity>)Lists.newArrayList();
        final List<Entity> list2 = (List<Entity>)Lists.newArrayList();
        for (final EntityTrackerEntry entitytrackerentry : this.entries) {
            final Entity entity = entitytrackerentry.getTrackedEntity();
            if (entity != player && entity.chunkCoordX == chunkIn.x && entity.chunkCoordZ == chunkIn.z) {
                entitytrackerentry.updatePlayerEntity(player);
                if (entity instanceof EntityLiving && ((EntityLiving)entity).getLeashHolder() != null) {
                    list.add(entity);
                }
                if (entity.getPassengers().isEmpty()) {
                    continue;
                }
                list2.add(entity);
            }
        }
        if (!list.isEmpty()) {
            for (final Entity entity2 : list) {
                player.connection.sendPacket(new SPacketEntityAttach(entity2, ((EntityLiving)entity2).getLeashHolder()));
            }
        }
        if (!list2.isEmpty()) {
            for (final Entity entity3 : list2) {
                player.connection.sendPacket(new SPacketSetPassengers(entity3));
            }
        }
    }
    
    public void setViewDistance(final int distance) {
        this.maxTrackingDistanceThreshold = (distance - 1) * 16;
        for (final EntityTrackerEntry entitytrackerentry : this.entries) {
            entitytrackerentry.setMaxRange(this.maxTrackingDistanceThreshold);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
