package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class WorldInfo {
   private long totalTime;
   private NBTTagCompound playerTag;
   private boolean allowCommands;
   private String levelName;
   private long worldTime;
   private boolean difficultyLocked;
   private double borderCenterX;
   private int spawnZ;
   private boolean mapFeaturesEnabled;
   private double borderSafeZone;
   private long borderSizeLerpTime;
   private long randomSeed;
   private double borderDamagePerBlock;
   private String generatorOptions;
   private GameRules theGameRules;
   private int borderWarningDistance;
   private boolean hardcore;
   private boolean initialized;
   private double borderSizeLerpTarget;
   public static final EnumDifficulty DEFAULT_DIFFICULTY;
   private int borderWarningTime;
   private double borderSize;
   private int saveVersion;
   private WorldSettings.GameType theGameType;
   private int thunderTime;
   private boolean raining;
   private double borderCenterZ;
   private int spawnY;
   private int rainTime;
   private EnumDifficulty difficulty;
   private boolean thundering;
   private WorldType terrainType;
   private long sizeOnDisk;
   private long lastTimePlayed;
   private int spawnX;
   private int dimension;
   private int cleanWeatherTime;

   static int access$9(WorldInfo var0) {
      return var0.saveVersion;
   }

   public int getBorderWarningDistance() {
      return this.borderWarningDistance;
   }

   public int getSpawnY() {
      return this.spawnY;
   }

   static int access$10(WorldInfo var0) {
      return var0.rainTime;
   }

   public String getWorldName() {
      return this.levelName;
   }

   public void setWorldTime(long var1) {
      this.worldTime = var1;
   }

   public String getGeneratorOptions() {
      return this.generatorOptions;
   }

   static boolean access$11(WorldInfo var0) {
      return var0.raining;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public void setAllowCommands(boolean var1) {
      this.allowCommands = var1;
   }

   public void setWorldTotalTime(long var1) {
      this.totalTime = var1;
   }

   public double getBorderCenterZ() {
      return this.borderCenterZ;
   }

   public void addToCrashReport(CrashReportCategory var1) {
      var1.addCrashSectionCallable("Level seed", new Callable(this) {
         final WorldInfo this$0;

         public String call() throws Exception {
            return String.valueOf(this.this$0.getSeed());
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSectionCallable("Level generator", new Callable(this) {
         final WorldInfo this$0;

         public String call() throws Exception {
            return String.format("ID %02d - %s, ver %d. Features enabled: %b", WorldInfo.access$0(this.this$0).getWorldTypeID(), WorldInfo.access$0(this.this$0).getWorldTypeName(), WorldInfo.access$0(this.this$0).getGeneratorVersion(), WorldInfo.access$1(this.this$0));
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSectionCallable("Level generator options", new Callable(this) {
         final WorldInfo this$0;

         public String call() throws Exception {
            return WorldInfo.access$2(this.this$0);
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.addCrashSectionCallable("Level spawn location", new Callable(this) {
         final WorldInfo this$0;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo((double)WorldInfo.access$3(this.this$0), (double)WorldInfo.access$4(this.this$0), (double)WorldInfo.access$5(this.this$0));
         }

         {
            this.this$0 = var1;
         }
      });
      var1.addCrashSectionCallable("Level time", new Callable(this) {
         final WorldInfo this$0;

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            return String.format("%d game time, %d day time", WorldInfo.access$6(this.this$0), WorldInfo.access$7(this.this$0));
         }
      });
      var1.addCrashSectionCallable("Level dimension", new Callable(this) {
         final WorldInfo this$0;

         {
            this.this$0 = var1;
         }

         public String call() throws Exception {
            return String.valueOf(WorldInfo.access$8(this.this$0));
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSectionCallable("Level storage version", new Callable(this) {
         final WorldInfo this$0;

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            String var1 = "Unknown?";

            try {
               switch(WorldInfo.access$9(this.this$0)) {
               case 19132:
                  var1 = "McRegion";
                  break;
               case 19133:
                  var1 = "Anvil";
               }
            } catch (Throwable var4) {
            }

            return String.format("0x%05X - %s", WorldInfo.access$9(this.this$0), var1);
         }
      });
      var1.addCrashSectionCallable("Level weather", new Callable(this) {
         final WorldInfo this$0;

         public String call() throws Exception {
            return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", WorldInfo.access$10(this.this$0), WorldInfo.access$11(this.this$0), WorldInfo.access$12(this.this$0), WorldInfo.access$13(this.this$0));
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSectionCallable("Level game mode", new Callable(this) {
         final WorldInfo this$0;

         public String call() throws Exception {
            return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", WorldInfo.access$14(this.this$0).getName(), WorldInfo.access$14(this.this$0).getID(), WorldInfo.access$15(this.this$0), WorldInfo.access$16(this.this$0));
         }

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }
      });
   }

   public NBTTagCompound getPlayerNBTTagCompound() {
      return this.playerTag;
   }

   public long getSizeOnDisk() {
      return this.sizeOnDisk;
   }

   static long access$7(WorldInfo var0) {
      return var0.worldTime;
   }

   static boolean access$16(WorldInfo var0) {
      return var0.allowCommands;
   }

   public void setBorderSafeZone(double var1) {
      this.borderSafeZone = var1;
   }

   static WorldSettings.GameType access$14(WorldInfo var0) {
      return var0.theGameType;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   static {
      DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
   }

   public void setTerrainType(WorldType var1) {
      this.terrainType = var1;
   }

   public double getBorderDamagePerBlock() {
      return this.borderDamagePerBlock;
   }

   public boolean isHardcoreModeEnabled() {
      return this.hardcore;
   }

   public double getBorderLerpTarget() {
      return this.borderSizeLerpTarget;
   }

   public int getBorderWarningTime() {
      return this.borderWarningTime;
   }

   static boolean access$13(WorldInfo var0) {
      return var0.thundering;
   }

   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   public void setHardcore(boolean var1) {
      this.hardcore = var1;
   }

   static int access$12(WorldInfo var0) {
      return var0.thunderTime;
   }

   public boolean isThundering() {
      return this.thundering;
   }

   public void setSaveVersion(int var1) {
      this.saveVersion = var1;
   }

   static int access$5(WorldInfo var0) {
      return var0.spawnZ;
   }

   public int getRainTime() {
      return this.rainTime;
   }

   static int access$4(WorldInfo var0) {
      return var0.spawnY;
   }

   public double getBorderSize() {
      return this.borderSize;
   }

   public void setSpawnZ(int var1) {
      this.spawnZ = var1;
   }

   public void getBorderCenterX(double var1) {
      this.borderCenterX = var1;
   }

   public void setThundering(boolean var1) {
      this.thundering = var1;
   }

   public WorldInfo(WorldInfo var1) {
      this.terrainType = WorldType.DEFAULT;
      this.generatorOptions = "";
      this.borderCenterX = 0.0D;
      this.borderCenterZ = 0.0D;
      this.borderSize = 6.0E7D;
      this.borderSizeLerpTime = 0L;
      this.borderSizeLerpTarget = 0.0D;
      this.borderSafeZone = 5.0D;
      this.borderDamagePerBlock = 0.2D;
      this.borderWarningDistance = 5;
      this.borderWarningTime = 15;
      this.theGameRules = new GameRules();
      this.randomSeed = var1.randomSeed;
      this.terrainType = var1.terrainType;
      this.generatorOptions = var1.generatorOptions;
      this.theGameType = var1.theGameType;
      this.mapFeaturesEnabled = var1.mapFeaturesEnabled;
      this.spawnX = var1.spawnX;
      this.spawnY = var1.spawnY;
      this.spawnZ = var1.spawnZ;
      this.totalTime = var1.totalTime;
      this.worldTime = var1.worldTime;
      this.lastTimePlayed = var1.lastTimePlayed;
      this.sizeOnDisk = var1.sizeOnDisk;
      this.playerTag = var1.playerTag;
      this.dimension = var1.dimension;
      this.levelName = var1.levelName;
      this.saveVersion = var1.saveVersion;
      this.rainTime = var1.rainTime;
      this.raining = var1.raining;
      this.thunderTime = var1.thunderTime;
      this.thundering = var1.thundering;
      this.hardcore = var1.hardcore;
      this.allowCommands = var1.allowCommands;
      this.initialized = var1.initialized;
      this.theGameRules = var1.theGameRules;
      this.difficulty = var1.difficulty;
      this.difficultyLocked = var1.difficultyLocked;
      this.borderCenterX = var1.borderCenterX;
      this.borderCenterZ = var1.borderCenterZ;
      this.borderSize = var1.borderSize;
      this.borderSizeLerpTime = var1.borderSizeLerpTime;
      this.borderSizeLerpTarget = var1.borderSizeLerpTarget;
      this.borderSafeZone = var1.borderSafeZone;
      this.borderDamagePerBlock = var1.borderDamagePerBlock;
      this.borderWarningTime = var1.borderWarningTime;
      this.borderWarningDistance = var1.borderWarningDistance;
   }

   public int getSpawnZ() {
      return this.spawnZ;
   }

   static WorldType access$0(WorldInfo var0) {
      return var0.terrainType;
   }

   public long getBorderLerpTime() {
      return this.borderSizeLerpTime;
   }

   static long access$6(WorldInfo var0) {
      return var0.totalTime;
   }

   public void setBorderLerpTime(long var1) {
      this.borderSizeLerpTime = var1;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public NBTTagCompound cloneNBTCompound(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      this.updateTagCompound(var2, var1);
      return var2;
   }

   public long getLastTimePlayed() {
      return this.lastTimePlayed;
   }

   public GameRules getGameRulesInstance() {
      return this.theGameRules;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public long getWorldTotalTime() {
      return this.totalTime;
   }

   public void setMapFeaturesEnabled(boolean var1) {
      this.mapFeaturesEnabled = var1;
   }

   public WorldInfo(NBTTagCompound var1) {
      this.terrainType = WorldType.DEFAULT;
      this.generatorOptions = "";
      this.borderCenterX = 0.0D;
      this.borderCenterZ = 0.0D;
      this.borderSize = 6.0E7D;
      this.borderSizeLerpTime = 0L;
      this.borderSizeLerpTarget = 0.0D;
      this.borderSafeZone = 5.0D;
      this.borderDamagePerBlock = 0.2D;
      this.borderWarningDistance = 5;
      this.borderWarningTime = 15;
      this.theGameRules = new GameRules();
      this.randomSeed = var1.getLong("RandomSeed");
      if (var1.hasKey("generatorName", 8)) {
         String var2 = var1.getString("generatorName");
         this.terrainType = WorldType.parseWorldType(var2);
         if (this.terrainType == null) {
            this.terrainType = WorldType.DEFAULT;
         } else if (this.terrainType.isVersioned()) {
            int var3 = 0;
            if (var1.hasKey("generatorVersion", 99)) {
               var3 = var1.getInteger("generatorVersion");
            }

            this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(var3);
         }

         if (var1.hasKey("generatorOptions", 8)) {
            this.generatorOptions = var1.getString("generatorOptions");
         }
      }

      this.theGameType = WorldSettings.GameType.getByID(var1.getInteger("GameType"));
      if (var1.hasKey("MapFeatures", 99)) {
         this.mapFeaturesEnabled = var1.getBoolean("MapFeatures");
      } else {
         this.mapFeaturesEnabled = true;
      }

      this.spawnX = var1.getInteger("SpawnX");
      this.spawnY = var1.getInteger("SpawnY");
      this.spawnZ = var1.getInteger("SpawnZ");
      this.totalTime = var1.getLong("Time");
      if (var1.hasKey("DayTime", 99)) {
         this.worldTime = var1.getLong("DayTime");
      } else {
         this.worldTime = this.totalTime;
      }

      this.lastTimePlayed = var1.getLong("LastPlayed");
      this.sizeOnDisk = var1.getLong("SizeOnDisk");
      this.levelName = var1.getString("LevelName");
      this.saveVersion = var1.getInteger("version");
      this.cleanWeatherTime = var1.getInteger("clearWeatherTime");
      this.rainTime = var1.getInteger("rainTime");
      this.raining = var1.getBoolean("raining");
      this.thunderTime = var1.getInteger("thunderTime");
      this.thundering = var1.getBoolean("thundering");
      this.hardcore = var1.getBoolean("hardcore");
      if (var1.hasKey("initialized", 99)) {
         this.initialized = var1.getBoolean("initialized");
      } else {
         this.initialized = true;
      }

      if (var1.hasKey("allowCommands", 99)) {
         this.allowCommands = var1.getBoolean("allowCommands");
      } else {
         this.allowCommands = this.theGameType == WorldSettings.GameType.CREATIVE;
      }

      if (var1.hasKey("Player", 10)) {
         this.playerTag = var1.getCompoundTag("Player");
         this.dimension = this.playerTag.getInteger("Dimension");
      }

      if (var1.hasKey("GameRules", 10)) {
         this.theGameRules.readFromNBT(var1.getCompoundTag("GameRules"));
      }

      if (var1.hasKey("Difficulty", 99)) {
         this.difficulty = EnumDifficulty.getDifficultyEnum(var1.getByte("Difficulty"));
      }

      if (var1.hasKey("DifficultyLocked", 1)) {
         this.difficultyLocked = var1.getBoolean("DifficultyLocked");
      }

      if (var1.hasKey("BorderCenterX", 99)) {
         this.borderCenterX = var1.getDouble("BorderCenterX");
      }

      if (var1.hasKey("BorderCenterZ", 99)) {
         this.borderCenterZ = var1.getDouble("BorderCenterZ");
      }

      if (var1.hasKey("BorderSize", 99)) {
         this.borderSize = var1.getDouble("BorderSize");
      }

      if (var1.hasKey("BorderSizeLerpTime", 99)) {
         this.borderSizeLerpTime = var1.getLong("BorderSizeLerpTime");
      }

      if (var1.hasKey("BorderSizeLerpTarget", 99)) {
         this.borderSizeLerpTarget = var1.getDouble("BorderSizeLerpTarget");
      }

      if (var1.hasKey("BorderSafeZone", 99)) {
         this.borderSafeZone = var1.getDouble("BorderSafeZone");
      }

      if (var1.hasKey("BorderDamagePerBlock", 99)) {
         this.borderDamagePerBlock = var1.getDouble("BorderDamagePerBlock");
      }

      if (var1.hasKey("BorderWarningBlocks", 99)) {
         this.borderWarningDistance = var1.getInteger("BorderWarningBlocks");
      }

      if (var1.hasKey("BorderWarningTime", 99)) {
         this.borderWarningTime = var1.getInteger("BorderWarningTime");
      }

   }

   public int getSaveVersion() {
      return this.saveVersion;
   }

   public void setDifficultyLocked(boolean var1) {
      this.difficultyLocked = var1;
   }

   public void getBorderCenterZ(double var1) {
      this.borderCenterZ = var1;
   }

   public void setBorderSize(double var1) {
      this.borderSize = var1;
   }

   static String access$2(WorldInfo var0) {
      return var0.generatorOptions;
   }

   public int getSpawnX() {
      return this.spawnX;
   }

   public void setBorderDamagePerBlock(double var1) {
      this.borderDamagePerBlock = var1;
   }

   public void setGameType(WorldSettings.GameType var1) {
      this.theGameType = var1;
   }

   public NBTTagCompound getNBTTagCompound() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.updateTagCompound(var1, this.playerTag);
      return var1;
   }

   public long getSeed() {
      return this.randomSeed;
   }

   public void setBorderWarningDistance(int var1) {
      this.borderWarningDistance = var1;
   }

   public WorldInfo(WorldSettings var1, String var2) {
      this.terrainType = WorldType.DEFAULT;
      this.generatorOptions = "";
      this.borderCenterX = 0.0D;
      this.borderCenterZ = 0.0D;
      this.borderSize = 6.0E7D;
      this.borderSizeLerpTime = 0L;
      this.borderSizeLerpTarget = 0.0D;
      this.borderSafeZone = 5.0D;
      this.borderDamagePerBlock = 0.2D;
      this.borderWarningDistance = 5;
      this.borderWarningTime = 15;
      this.theGameRules = new GameRules();
      this.populateFromWorldSettings(var1);
      this.levelName = var2;
      this.difficulty = DEFAULT_DIFFICULTY;
      this.initialized = false;
   }

   public void setDifficulty(EnumDifficulty var1) {
      this.difficulty = var1;
   }

   public void setWorldName(String var1) {
      this.levelName = var1;
   }

   static int access$3(WorldInfo var0) {
      return var0.spawnX;
   }

   public void setSpawnY(int var1) {
      this.spawnY = var1;
   }

   static int access$8(WorldInfo var0) {
      return var0.dimension;
   }

   public void setServerInitialized(boolean var1) {
      this.initialized = var1;
   }

   public int getThunderTime() {
      return this.thunderTime;
   }

   public WorldSettings.GameType getGameType() {
      return this.theGameType;
   }

   public boolean areCommandsAllowed() {
      return this.allowCommands;
   }

   public void setBorderLerpTarget(double var1) {
      this.borderSizeLerpTarget = var1;
   }

   public void setBorderWarningTime(int var1) {
      this.borderWarningTime = var1;
   }

   static boolean access$15(WorldInfo var0) {
      return var0.hardcore;
   }

   public int getCleanWeatherTime() {
      return this.cleanWeatherTime;
   }

   static boolean access$1(WorldInfo var0) {
      return var0.mapFeaturesEnabled;
   }

   public void setSpawnX(int var1) {
      this.spawnX = var1;
   }

   protected WorldInfo() {
      this.terrainType = WorldType.DEFAULT;
      this.generatorOptions = "";
      this.borderCenterX = 0.0D;
      this.borderCenterZ = 0.0D;
      this.borderSize = 6.0E7D;
      this.borderSizeLerpTime = 0L;
      this.borderSizeLerpTarget = 0.0D;
      this.borderSafeZone = 5.0D;
      this.borderDamagePerBlock = 0.2D;
      this.borderWarningDistance = 5;
      this.borderWarningTime = 15;
      this.theGameRules = new GameRules();
   }

   public void setThunderTime(int var1) {
      this.thunderTime = var1;
   }

   public void setCleanWeatherTime(int var1) {
      this.cleanWeatherTime = var1;
   }

   public void setSpawn(BlockPos var1) {
      this.spawnX = var1.getX();
      this.spawnY = var1.getY();
      this.spawnZ = var1.getZ();
   }

   public void setRainTime(int var1) {
      this.rainTime = var1;
   }

   public void setRaining(boolean var1) {
      this.raining = var1;
   }

   public void populateFromWorldSettings(WorldSettings var1) {
      this.randomSeed = var1.getSeed();
      this.theGameType = var1.getGameType();
      this.mapFeaturesEnabled = var1.isMapFeaturesEnabled();
      this.hardcore = var1.getHardcoreEnabled();
      this.terrainType = var1.getTerrainType();
      this.generatorOptions = var1.getWorldName();
      this.allowCommands = var1.areCommandsAllowed();
   }

   public double getBorderCenterX() {
      return this.borderCenterX;
   }

   public double getBorderSafeZone() {
      return this.borderSafeZone;
   }

   private void updateTagCompound(NBTTagCompound var1, NBTTagCompound var2) {
      var1.setLong("RandomSeed", this.randomSeed);
      var1.setString("generatorName", this.terrainType.getWorldTypeName());
      var1.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
      var1.setString("generatorOptions", this.generatorOptions);
      var1.setInteger("GameType", this.theGameType.getID());
      var1.setBoolean("MapFeatures", this.mapFeaturesEnabled);
      var1.setInteger("SpawnX", this.spawnX);
      var1.setInteger("SpawnY", this.spawnY);
      var1.setInteger("SpawnZ", this.spawnZ);
      var1.setLong("Time", this.totalTime);
      var1.setLong("DayTime", this.worldTime);
      var1.setLong("SizeOnDisk", this.sizeOnDisk);
      var1.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
      var1.setString("LevelName", this.levelName);
      var1.setInteger("version", this.saveVersion);
      var1.setInteger("clearWeatherTime", this.cleanWeatherTime);
      var1.setInteger("rainTime", this.rainTime);
      var1.setBoolean("raining", this.raining);
      var1.setInteger("thunderTime", this.thunderTime);
      var1.setBoolean("thundering", this.thundering);
      var1.setBoolean("hardcore", this.hardcore);
      var1.setBoolean("allowCommands", this.allowCommands);
      var1.setBoolean("initialized", this.initialized);
      var1.setDouble("BorderCenterX", this.borderCenterX);
      var1.setDouble("BorderCenterZ", this.borderCenterZ);
      var1.setDouble("BorderSize", this.borderSize);
      var1.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
      var1.setDouble("BorderSafeZone", this.borderSafeZone);
      var1.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
      var1.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
      var1.setDouble("BorderWarningBlocks", (double)this.borderWarningDistance);
      var1.setDouble("BorderWarningTime", (double)this.borderWarningTime);
      if (this.difficulty != null) {
         var1.setByte("Difficulty", (byte)this.difficulty.getDifficultyId());
      }

      var1.setBoolean("DifficultyLocked", this.difficultyLocked);
      var1.setTag("GameRules", this.theGameRules.writeToNBT());
      if (var2 != null) {
         var1.setTag("Player", var2);
      }

   }

   public long getWorldTime() {
      return this.worldTime;
   }

   public boolean isRaining() {
      return this.raining;
   }
}
