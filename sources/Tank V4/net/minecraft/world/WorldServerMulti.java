package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer {
   private WorldServer delegate;

   public World init() {
      this.mapStorage = this.delegate.getMapStorage();
      this.worldScoreboard = this.delegate.getScoreboard();
      String var1 = VillageCollection.fileNameForProvider(this.provider);
      VillageCollection var2 = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, var1);
      if (var2 == null) {
         this.villageCollectionObj = new VillageCollection(this);
         this.mapStorage.setData(var1, this.villageCollectionObj);
      } else {
         this.villageCollectionObj = var2;
         this.villageCollectionObj.setWorldsForAll(this);
      }

      return this;
   }

   protected void saveLevel() throws MinecraftException {
   }

   public WorldServerMulti(MinecraftServer var1, ISaveHandler var2, int var3, WorldServer var4, Profiler var5) {
      super(var1, var2, new DerivedWorldInfo(var4.getWorldInfo()), var3, var5);
      this.delegate = var4;
      var4.getWorldBorder().addListener(new IBorderListener(this) {
         final WorldServerMulti this$0;

         public void onDamageAmountChanged(WorldBorder var1, double var2) {
            this.this$0.getWorldBorder().setDamageAmount(var2);
         }

         public void onTransitionStarted(WorldBorder var1, double var2, double var4, long var6) {
            this.this$0.getWorldBorder().setTransition(var2, var4, var6);
         }

         {
            this.this$0 = var1;
         }

         public void onWarningTimeChanged(WorldBorder var1, int var2) {
            this.this$0.getWorldBorder().setWarningTime(var2);
         }

         public void onSizeChanged(WorldBorder var1, double var2) {
            this.this$0.getWorldBorder().setTransition(var2);
         }

         public void onCenterChanged(WorldBorder var1, double var2, double var4) {
            this.this$0.getWorldBorder().setCenter(var2, var4);
         }

         public void onDamageBufferChanged(WorldBorder var1, double var2) {
            this.this$0.getWorldBorder().setDamageBuffer(var2);
         }

         public void onWarningDistanceChanged(WorldBorder var1, int var2) {
            this.this$0.getWorldBorder().setWarningDistance(var2);
         }
      });
   }
}
