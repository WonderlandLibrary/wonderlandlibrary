/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
    private final WorldServer theWorld;
    private IntHashMap<EntityTrackerEntry> trackedEntityHashTable;
    private Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
    private int maxTrackingDistanceThreshold;
    private static final Logger logger = LogManager.getLogger();

    public void func_151248_b(Entity entity, Packet packet) {
        EntityTrackerEntry entityTrackerEntry = this.trackedEntityHashTable.lookup(entity.getEntityId());
        if (entityTrackerEntry != null) {
            entityTrackerEntry.func_151261_b(packet);
        }
    }

    public void trackEntity(Entity entity, int n, int n2) {
        this.addEntityToTracker(entity, n, n2, false);
    }

    public void trackEntity(Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            this.trackEntity(entity, 512, 2);
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entity;
            for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
                if (entityTrackerEntry.trackedEntity == entityPlayerMP) continue;
                entityTrackerEntry.updatePlayerEntity(entityPlayerMP);
            }
        } else if (entity instanceof EntityFishHook) {
            this.addEntityToTracker(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            this.addEntityToTracker(entity, 64, 20, false);
        } else if (entity instanceof EntitySmallFireball) {
            this.addEntityToTracker(entity, 64, 10, false);
        } else if (entity instanceof EntityFireball) {
            this.addEntityToTracker(entity, 64, 10, false);
        } else if (entity instanceof EntitySnowball) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderPearl) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderEye) {
            this.addEntityToTracker(entity, 64, 4, true);
        } else if (entity instanceof EntityEgg) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityPotion) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityExpBottle) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityFireworkRocket) {
            this.addEntityToTracker(entity, 64, 10, true);
        } else if (entity instanceof EntityItem) {
            this.addEntityToTracker(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecart) {
            this.addEntityToTracker(entity, 80, 3, true);
        } else if (entity instanceof EntityBoat) {
            this.addEntityToTracker(entity, 80, 3, true);
        } else if (entity instanceof EntitySquid) {
            this.addEntityToTracker(entity, 64, 3, true);
        } else if (entity instanceof EntityWither) {
            this.addEntityToTracker(entity, 80, 3, false);
        } else if (entity instanceof EntityBat) {
            this.addEntityToTracker(entity, 80, 3, false);
        } else if (entity instanceof EntityDragon) {
            this.addEntityToTracker(entity, 160, 3, true);
        } else if (entity instanceof IAnimals) {
            this.addEntityToTracker(entity, 80, 3, true);
        } else if (entity instanceof EntityTNTPrimed) {
            this.addEntityToTracker(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingBlock) {
            this.addEntityToTracker(entity, 160, 20, true);
        } else if (entity instanceof EntityHanging) {
            this.addEntityToTracker(entity, 160, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityArmorStand) {
            this.addEntityToTracker(entity, 160, 3, true);
        } else if (entity instanceof EntityXPOrb) {
            this.addEntityToTracker(entity, 160, 20, true);
        } else if (entity instanceof EntityEnderCrystal) {
            this.addEntityToTracker(entity, 256, Integer.MAX_VALUE, false);
        }
    }

    public void addEntityToTracker(Entity entity, int n, final int n2, boolean bl) {
        if (n > this.maxTrackingDistanceThreshold) {
            n = this.maxTrackingDistanceThreshold;
        }
        try {
            if (this.trackedEntityHashTable.containsItem(entity.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }
            EntityTrackerEntry entityTrackerEntry = new EntityTrackerEntry(entity, n, n2, bl);
            this.trackedEntities.add(entityTrackerEntry);
            this.trackedEntityHashTable.addKey(entity.getEntityId(), entityTrackerEntry);
            entityTrackerEntry.updatePlayerEntities(this.theWorld.playerEntities);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Entity To Track");
            crashReportCategory.addCrashSection("Tracking range", String.valueOf(n) + " blocks");
            crashReportCategory.addCrashSectionCallable("Update interval", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    String string = "Once per " + n2 + " ticks";
                    if (n2 == Integer.MAX_VALUE) {
                        string = "Maximum (" + string + ")";
                    }
                    return string;
                }
            });
            entity.addEntityCrashInfo(crashReportCategory);
            CrashReportCategory crashReportCategory2 = crashReport.makeCategory("Entity That Is Already Tracked");
            this.trackedEntityHashTable.lookup((int)entity.getEntityId()).trackedEntity.addEntityCrashInfo(crashReportCategory2);
            throw new ReportedException(crashReport);
        }
    }

    public void func_85172_a(EntityPlayerMP entityPlayerMP, Chunk chunk) {
        for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
            if (entityTrackerEntry.trackedEntity == entityPlayerMP || entityTrackerEntry.trackedEntity.chunkCoordX != chunk.xPosition || entityTrackerEntry.trackedEntity.chunkCoordZ != chunk.zPosition) continue;
            entityTrackerEntry.updatePlayerEntity(entityPlayerMP);
        }
    }

    public void removePlayerFromTrackers(EntityPlayerMP entityPlayerMP) {
        for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
            entityTrackerEntry.removeTrackedPlayerSymmetric(entityPlayerMP);
        }
    }

    public EntityTracker(WorldServer worldServer) {
        this.trackedEntityHashTable = new IntHashMap();
        this.theWorld = worldServer;
        this.maxTrackingDistanceThreshold = worldServer.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
    }

    public void updateTrackedEntities() {
        ArrayList arrayList = Lists.newArrayList();
        for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
            entityTrackerEntry.updatePlayerList(this.theWorld.playerEntities);
            if (!entityTrackerEntry.playerEntitiesUpdated || !(entityTrackerEntry.trackedEntity instanceof EntityPlayerMP)) continue;
            arrayList.add((EntityPlayerMP)entityTrackerEntry.trackedEntity);
        }
        int n = 0;
        while (n < arrayList.size()) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)arrayList.get(n);
            for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
                if (entityTrackerEntry.trackedEntity == entityPlayerMP) continue;
                entityTrackerEntry.updatePlayerEntity(entityPlayerMP);
            }
            ++n;
        }
    }

    public void sendToAllTrackingEntity(Entity entity, Packet packet) {
        EntityTrackerEntry entityTrackerEntry = this.trackedEntityHashTable.lookup(entity.getEntityId());
        if (entityTrackerEntry != null) {
            entityTrackerEntry.sendPacketToTrackedPlayers(packet);
        }
    }

    public void func_180245_a(EntityPlayerMP entityPlayerMP) {
        for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
            if (entityTrackerEntry.trackedEntity == entityPlayerMP) {
                entityTrackerEntry.updatePlayerEntities(this.theWorld.playerEntities);
                continue;
            }
            entityTrackerEntry.updatePlayerEntity(entityPlayerMP);
        }
    }

    public void untrackEntity(Entity entity) {
        Object object;
        if (entity instanceof EntityPlayerMP) {
            object = (EntityPlayerMP)entity;
            for (EntityTrackerEntry entityTrackerEntry : this.trackedEntities) {
                entityTrackerEntry.removeFromTrackedPlayers((EntityPlayerMP)object);
            }
        }
        if ((object = this.trackedEntityHashTable.removeObject(entity.getEntityId())) != null) {
            this.trackedEntities.remove(object);
            ((EntityTrackerEntry)object).sendDestroyEntityPacketToTrackedPlayers();
        }
    }
}

