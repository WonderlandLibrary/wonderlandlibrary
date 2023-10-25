package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
  private static final Logger logger = LogManager.getLogger();
  
  private final WorldServer theWorld;
  
  private Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
  
  private IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
  
  private int maxTrackingDistanceThreshold;
  
  public EntityTracker(WorldServer theWorldIn) {
    this.theWorld = theWorldIn;
    this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
  }
  
  public void trackEntity(Entity p_72786_1_) {
    if (p_72786_1_ instanceof EntityPlayerMP) {
      trackEntity(p_72786_1_, 512, 2);
      EntityPlayerMP entityplayermp = (EntityPlayerMP)p_72786_1_;
      for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
        if (entitytrackerentry.trackedEntity != entityplayermp)
          entitytrackerentry.updatePlayerEntity(entityplayermp); 
      } 
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntityFishHook) {
      addEntityToTracker(p_72786_1_, 64, 5, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntityArrow) {
      addEntityToTracker(p_72786_1_, 64, 20, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntitySmallFireball) {
      addEntityToTracker(p_72786_1_, 64, 10, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntityFireball) {
      addEntityToTracker(p_72786_1_, 64, 10, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntitySnowball) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityEnderPearl) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityEnderEye) {
      addEntityToTracker(p_72786_1_, 64, 4, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntityEgg) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.projectile.EntityPotion) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityExpBottle) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityFireworkRocket) {
      addEntityToTracker(p_72786_1_, 64, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityItem) {
      addEntityToTracker(p_72786_1_, 64, 20, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityMinecart) {
      addEntityToTracker(p_72786_1_, 80, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityBoat) {
      addEntityToTracker(p_72786_1_, 80, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.passive.EntitySquid) {
      addEntityToTracker(p_72786_1_, 64, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.boss.EntityWither) {
      addEntityToTracker(p_72786_1_, 80, 3, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.passive.EntityBat) {
      addEntityToTracker(p_72786_1_, 80, 3, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.boss.EntityDragon) {
      addEntityToTracker(p_72786_1_, 160, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.passive.IAnimals) {
      addEntityToTracker(p_72786_1_, 80, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityTNTPrimed) {
      addEntityToTracker(p_72786_1_, 160, 10, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityFallingBlock) {
      addEntityToTracker(p_72786_1_, 160, 20, true);
    } else if (p_72786_1_ instanceof EntityHanging) {
      addEntityToTracker(p_72786_1_, 160, 2147483647, false);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityArmorStand) {
      addEntityToTracker(p_72786_1_, 160, 3, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityXPOrb) {
      addEntityToTracker(p_72786_1_, 160, 20, true);
    } else if (p_72786_1_ instanceof net.minecraft.entity.item.EntityEnderCrystal) {
      addEntityToTracker(p_72786_1_, 256, 2147483647, false);
    } 
  }
  
  public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency) {
    addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
  }
  
  public void addEntityToTracker(Entity entityIn, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates) {
    if (trackingRange > this.maxTrackingDistanceThreshold)
      trackingRange = this.maxTrackingDistanceThreshold; 
    try {
      if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId()))
        throw new IllegalStateException("Entity is already tracked!"); 
      EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, updateFrequency, sendVelocityUpdates);
      this.trackedEntities.add(entitytrackerentry);
      this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
      entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
      crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
      crashreportcategory.addCrashSectionCallable("Update interval", new Callable<String>() {
            public String call() throws Exception {
              String s = "Once per " + updateFrequency + " ticks";
              if (updateFrequency == Integer.MAX_VALUE)
                s = "Maximum (" + s + ")"; 
              return s;
            }
          });
      entityIn.addEntityCrashInfo(crashreportcategory);
      CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
      ((EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId())).trackedEntity.addEntityCrashInfo(crashreportcategory1);
      try {
        throw new ReportedException(crashreport);
      } catch (ReportedException reportedexception) {
        logger.error("\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
      } 
    } 
  }
  
  public void untrackEntity(Entity entityIn) {
    if (entityIn instanceof EntityPlayerMP) {
      EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
      for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        entitytrackerentry.removeFromTrackedPlayers(entityplayermp); 
    } 
    EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
    if (entitytrackerentry1 != null) {
      this.trackedEntities.remove(entitytrackerentry1);
      entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
    } 
  }
  
  public void updateTrackedEntities() {
    List<EntityPlayerMP> list = Lists.newArrayList();
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);
      if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.trackedEntity instanceof EntityPlayerMP)
        list.add((EntityPlayerMP)entitytrackerentry.trackedEntity); 
    } 
    for (int i = 0; i < list.size(); i++) {
      EntityPlayerMP entityplayermp = list.get(i);
      for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities) {
        if (entitytrackerentry1.trackedEntity != entityplayermp)
          entitytrackerentry1.updatePlayerEntity(entityplayermp); 
      } 
    } 
  }
  
  public void func_180245_a(EntityPlayerMP p_180245_1_) {
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      if (entitytrackerentry.trackedEntity == p_180245_1_) {
        entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
        continue;
      } 
      entitytrackerentry.updatePlayerEntity(p_180245_1_);
    } 
  }
  
  public void sendToAllTrackingEntity(Entity entityIn, Packet p_151247_2_) {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
    if (entitytrackerentry != null)
      entitytrackerentry.sendPacketToTrackedPlayers(p_151247_2_); 
  }
  
  public void func_151248_b(Entity entityIn, Packet p_151248_2_) {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
    if (entitytrackerentry != null)
      entitytrackerentry.func_151261_b(p_151248_2_); 
  }
  
  public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_) {
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
      entitytrackerentry.removeTrackedPlayerSymmetric(p_72787_1_); 
  }
  
  public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_) {
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      if (entitytrackerentry.trackedEntity != p_85172_1_ && entitytrackerentry.trackedEntity.chunkCoordX == p_85172_2_.xPosition && entitytrackerentry.trackedEntity.chunkCoordZ == p_85172_2_.zPosition)
        entitytrackerentry.updatePlayerEntity(p_85172_1_); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */