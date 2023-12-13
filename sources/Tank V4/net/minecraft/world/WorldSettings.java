package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings {
   private final long seed;
   private String worldName;
   private final WorldSettings.GameType theGameType;
   private final boolean hardcoreEnabled;
   private final boolean mapFeaturesEnabled;
   private boolean commandsAllowed;
   private final WorldType terrainType;
   private boolean bonusChestEnabled;

   public boolean isBonusChestEnabled() {
      return this.bonusChestEnabled;
   }

   public WorldSettings(WorldInfo var1) {
      this(var1.getSeed(), var1.getGameType(), var1.isMapFeaturesEnabled(), var1.isHardcoreModeEnabled(), var1.getTerrainType());
   }

   public WorldSettings setWorldName(String var1) {
      this.worldName = var1;
      return this;
   }

   public long getSeed() {
      return this.seed;
   }

   public boolean getHardcoreEnabled() {
      return this.hardcoreEnabled;
   }

   public WorldSettings(long var1, WorldSettings.GameType var3, boolean var4, boolean var5, WorldType var6) {
      this.worldName = "";
      this.seed = var1;
      this.theGameType = var3;
      this.mapFeaturesEnabled = var4;
      this.hardcoreEnabled = var5;
      this.terrainType = var6;
   }

   public WorldSettings enableBonusChest() {
      this.bonusChestEnabled = true;
      return this;
   }

   public boolean areCommandsAllowed() {
      return this.commandsAllowed;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public WorldSettings enableCommands() {
      this.commandsAllowed = true;
      return this;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public static WorldSettings.GameType getGameTypeById(int var0) {
      return WorldSettings.GameType.getByID(var0);
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public WorldSettings.GameType getGameType() {
      return this.theGameType;
   }

   public static enum GameType {
      int id;
      SPECTATOR(3, "spectator"),
      ADVENTURE(2, "adventure"),
      SURVIVAL(0, "survival");

      String name;
      CREATIVE(1, "creative"),
      NOT_SET(-1, "");

      private static final WorldSettings.GameType[] ENUM$VALUES = new WorldSettings.GameType[]{NOT_SET, SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR};

      private GameType(int var3, String var4) {
         this.id = var3;
         this.name = var4;
      }

      public static WorldSettings.GameType getByName(String var0) {
         WorldSettings.GameType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            WorldSettings.GameType var1 = var4[var2];
            if (var1.name.equals(var0)) {
               return var1;
            }
         }

         return SURVIVAL;
      }

      public boolean isCreative() {
         return this == CREATIVE;
      }

      public void configurePlayerCapabilities(PlayerCapabilities var1) {
         if (this == CREATIVE) {
            var1.allowFlying = true;
            var1.isCreativeMode = true;
            var1.disableDamage = true;
         } else if (this == SPECTATOR) {
            var1.allowFlying = true;
            var1.isCreativeMode = false;
            var1.disableDamage = true;
            var1.isFlying = true;
         } else {
            var1.allowFlying = false;
            var1.isCreativeMode = false;
            var1.disableDamage = false;
            var1.isFlying = false;
         }

         var1.allowEdit = this == false;
      }

      public static WorldSettings.GameType getByID(int var0) {
         WorldSettings.GameType[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            WorldSettings.GameType var1 = var4[var2];
            if (var1.id == var0) {
               return var1;
            }
         }

         return SURVIVAL;
      }

      public int getID() {
         return this.id;
      }

      public boolean isSurvivalOrAdventure() {
         return this == SURVIVAL || this == ADVENTURE;
      }

      public String getName() {
         return this.name;
      }
   }
}
