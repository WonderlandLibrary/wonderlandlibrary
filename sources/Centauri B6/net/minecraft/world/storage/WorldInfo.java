package net.minecraft.world.storage;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.storage.WorldInfo.1;
import net.minecraft.world.storage.WorldInfo.2;
import net.minecraft.world.storage.WorldInfo.3;
import net.minecraft.world.storage.WorldInfo.4;
import net.minecraft.world.storage.WorldInfo.5;
import net.minecraft.world.storage.WorldInfo.6;
import net.minecraft.world.storage.WorldInfo.7;
import net.minecraft.world.storage.WorldInfo.8;
import net.minecraft.world.storage.WorldInfo.9;

public class WorldInfo {
   public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
   private long randomSeed;
   private WorldType terrainType = WorldType.DEFAULT;
   private String generatorOptions = "";
   private int spawnX;
   private int spawnY;
   private int spawnZ;
   private long totalTime;
   private long worldTime;
   private long lastTimePlayed;
   private long sizeOnDisk;
   private NBTTagCompound playerTag;
   private int dimension;
   private String levelName;
   private int saveVersion;
   private int cleanWeatherTime;
   private boolean raining;
   private int rainTime;
   private boolean thundering;
   private int thunderTime;
   private GameType theGameType;
   private boolean mapFeaturesEnabled;
   private boolean hardcore;
   private boolean allowCommands;
   private boolean initialized;
   private EnumDifficulty difficulty;
   private boolean difficultyLocked;
   private double borderCenterX = 0.0D;
   private double borderCenterZ = 0.0D;
   private double borderSize = 6.0E7D;
   private long borderSizeLerpTime = 0L;
   private double borderSizeLerpTarget = 0.0D;
   private double borderSafeZone = 5.0D;
   private double borderDamagePerBlock = 0.2D;
   private int borderWarningDistance = 5;
   private int borderWarningTime = 15;
   private GameRules theGameRules = new GameRules();

   public WorldInfo(WorldInfo worldInformation) {
      this.randomSeed = worldInformation.randomSeed;
      this.terrainType = worldInformation.terrainType;
      this.generatorOptions = worldInformation.generatorOptions;
      this.theGameType = worldInformation.theGameType;
      this.mapFeaturesEnabled = worldInformation.mapFeaturesEnabled;
      this.spawnX = worldInformation.spawnX;
      this.spawnY = worldInformation.spawnY;
      this.spawnZ = worldInformation.spawnZ;
      this.totalTime = worldInformation.totalTime;
      this.worldTime = worldInformation.worldTime;
      this.lastTimePlayed = worldInformation.lastTimePlayed;
      this.sizeOnDisk = worldInformation.sizeOnDisk;
      this.playerTag = worldInformation.playerTag;
      this.dimension = worldInformation.dimension;
      this.levelName = worldInformation.levelName;
      this.saveVersion = worldInformation.saveVersion;
      this.rainTime = worldInformation.rainTime;
      this.raining = worldInformation.raining;
      this.thunderTime = worldInformation.thunderTime;
      this.thundering = worldInformation.thundering;
      this.hardcore = worldInformation.hardcore;
      this.allowCommands = worldInformation.allowCommands;
      this.initialized = worldInformation.initialized;
      this.theGameRules = worldInformation.theGameRules;
      this.difficulty = worldInformation.difficulty;
      this.difficultyLocked = worldInformation.difficultyLocked;
      this.borderCenterX = worldInformation.borderCenterX;
      this.borderCenterZ = worldInformation.borderCenterZ;
      this.borderSize = worldInformation.borderSize;
      this.borderSizeLerpTime = worldInformation.borderSizeLerpTime;
      this.borderSizeLerpTarget = worldInformation.borderSizeLerpTarget;
      this.borderSafeZone = worldInformation.borderSafeZone;
      this.borderDamagePerBlock = worldInformation.borderDamagePerBlock;
      this.borderWarningTime = worldInformation.borderWarningTime;
      this.borderWarningDistance = worldInformation.borderWarningDistance;
   }

   public WorldInfo(WorldSettings settings, String name) {
      this.populateFromWorldSettings(settings);
      this.levelName = name;
      this.difficulty = DEFAULT_DIFFICULTY;
      this.initialized = false;
   }

   public WorldInfo(NBTTagCompound nbt) {
      this.randomSeed = nbt.getLong("RandomSeed");
      if(nbt.hasKey("generatorName", 8)) {
         String s = nbt.getString("generatorName");
         this.terrainType = WorldType.parseWorldType(s);
         if(this.terrainType == null) {
            this.terrainType = WorldType.DEFAULT;
         } else if(this.terrainType.isVersioned()) {
            int i = 0;
            if(nbt.hasKey("generatorVersion", 99)) {
               i = nbt.getInteger("generatorVersion");
            }

            this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(i);
         }

         if(nbt.hasKey("generatorOptions", 8)) {
            this.generatorOptions = nbt.getString("generatorOptions");
         }
      }

      this.theGameType = GameType.getByID(nbt.getInteger("GameType"));
      if(nbt.hasKey("MapFeatures", 99)) {
         this.mapFeaturesEnabled = nbt.getBoolean("MapFeatures");
      } else {
         this.mapFeaturesEnabled = true;
      }

      this.spawnX = nbt.getInteger("SpawnX");
      this.spawnY = nbt.getInteger("SpawnY");
      this.spawnZ = nbt.getInteger("SpawnZ");
      this.totalTime = nbt.getLong("Time");
      if(nbt.hasKey("DayTime", 99)) {
         this.worldTime = nbt.getLong("DayTime");
      } else {
         this.worldTime = this.totalTime;
      }

      this.lastTimePlayed = nbt.getLong("LastPlayed");
      this.sizeOnDisk = nbt.getLong("SizeOnDisk");
      this.levelName = nbt.getString("LevelName");
      this.saveVersion = nbt.getInteger("version");
      this.cleanWeatherTime = nbt.getInteger("clearWeatherTime");
      this.rainTime = nbt.getInteger("rainTime");
      this.raining = nbt.getBoolean("raining");
      this.thunderTime = nbt.getInteger("thunderTime");
      this.thundering = nbt.getBoolean("thundering");
      this.hardcore = nbt.getBoolean("hardcore");
      if(nbt.hasKey("initialized", 99)) {
         this.initialized = nbt.getBoolean("initialized");
      } else {
         this.initialized = true;
      }

      if(nbt.hasKey("allowCommands", 99)) {
         this.allowCommands = nbt.getBoolean("allowCommands");
      } else {
         this.allowCommands = this.theGameType == GameType.CREATIVE;
      }

      if(nbt.hasKey("Player", 10)) {
         this.playerTag = nbt.getCompoundTag("Player");
         this.dimension = this.playerTag.getInteger("Dimension");
      }

      if(nbt.hasKey("GameRules", 10)) {
         this.theGameRules.readFromNBT(nbt.getCompoundTag("GameRules"));
      }

      if(nbt.hasKey("Difficulty", 99)) {
         this.difficulty = EnumDifficulty.getDifficultyEnum(nbt.getByte("Difficulty"));
      }

      if(nbt.hasKey("DifficultyLocked", 1)) {
         this.difficultyLocked = nbt.getBoolean("DifficultyLocked");
      }

      if(nbt.hasKey("BorderCenterX", 99)) {
         this.borderCenterX = nbt.getDouble("BorderCenterX");
      }

      if(nbt.hasKey("BorderCenterZ", 99)) {
         this.borderCenterZ = nbt.getDouble("BorderCenterZ");
      }

      if(nbt.hasKey("BorderSize", 99)) {
         this.borderSize = nbt.getDouble("BorderSize");
      }

      if(nbt.hasKey("BorderSizeLerpTime", 99)) {
         this.borderSizeLerpTime = nbt.getLong("BorderSizeLerpTime");
      }

      if(nbt.hasKey("BorderSizeLerpTarget", 99)) {
         this.borderSizeLerpTarget = nbt.getDouble("BorderSizeLerpTarget");
      }

      if(nbt.hasKey("BorderSafeZone", 99)) {
         this.borderSafeZone = nbt.getDouble("BorderSafeZone");
      }

      if(nbt.hasKey("BorderDamagePerBlock", 99)) {
         this.borderDamagePerBlock = nbt.getDouble("BorderDamagePerBlock");
      }

      if(nbt.hasKey("BorderWarningBlocks", 99)) {
         this.borderWarningDistance = nbt.getInteger("BorderWarningBlocks");
      }

      if(nbt.hasKey("BorderWarningTime", 99)) {
         this.borderWarningTime = nbt.getInteger("BorderWarningTime");
      }

   }

   protected WorldInfo() {
   }

   // $FF: synthetic method
   static boolean access$100(WorldInfo x0) {
      return x0.mapFeaturesEnabled;
   }

   // $FF: synthetic method
   static String access$200(WorldInfo x0) {
      return x0.generatorOptions;
   }

   // $FF: synthetic method
   static int access$300(WorldInfo x0) {
      return x0.spawnX;
   }

   // $FF: synthetic method
   static WorldType access$000(WorldInfo x0) {
      return x0.terrainType;
   }

   // $FF: synthetic method
   static int access$400(WorldInfo x0) {
      return x0.spawnY;
   }

   // $FF: synthetic method
   static int access$500(WorldInfo x0) {
      return x0.spawnZ;
   }

   // $FF: synthetic method
   static long access$600(WorldInfo x0) {
      return x0.totalTime;
   }

   // $FF: synthetic method
   static long access$700(WorldInfo x0) {
      return x0.worldTime;
   }

   // $FF: synthetic method
   static int access$800(WorldInfo x0) {
      return x0.dimension;
   }

   // $FF: synthetic method
   static boolean access$1100(WorldInfo x0) {
      return x0.raining;
   }

   // $FF: synthetic method
   static int access$1000(WorldInfo x0) {
      return x0.rainTime;
   }

   // $FF: synthetic method
   static int access$1200(WorldInfo x0) {
      return x0.thunderTime;
   }

   // $FF: synthetic method
   static boolean access$1300(WorldInfo x0) {
      return x0.thundering;
   }

   // $FF: synthetic method
   static GameType access$1400(WorldInfo x0) {
      return x0.theGameType;
   }

   // $FF: synthetic method
   static boolean access$1500(WorldInfo x0) {
      return x0.hardcore;
   }

   // $FF: synthetic method
   static boolean access$1600(WorldInfo x0) {
      return x0.allowCommands;
   }

   // $FF: synthetic method
   static int access$900(WorldInfo x0) {
      return x0.saveVersion;
   }

   public long getSeed() {
      return this.randomSeed;
   }

   public boolean isRaining() {
      return this.raining;
   }

   public boolean isThundering() {
      return this.thundering;
   }

   public long getWorldTime() {
      return this.worldTime;
   }

   public void setWorldTime(long time) {
      this.worldTime = time;
   }

   public int getSpawnX() {
      return this.spawnX;
   }

   public int getSpawnZ() {
      return this.spawnZ;
   }

   public void setRaining(boolean isRaining) {
      this.raining = isRaining;
   }

   public void setThundering(boolean thunderingIn) {
      this.thundering = thunderingIn;
   }

   public int getRainTime() {
      return this.rainTime;
   }

   public int getSpawnY() {
      return this.spawnY;
   }

   public int getThunderTime() {
      return this.thunderTime;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public void setThunderTime(int time) {
      this.thunderTime = time;
   }

   public void setRainTime(int time) {
      this.rainTime = time;
   }

   public void addToCrashReport(CrashReportCategory category) {
      category.addCrashSectionCallable("Level seed", new 1(this));
      category.addCrashSectionCallable("Level generator", new 2(this));
      category.addCrashSectionCallable("Level generator options", new 3(this));
      category.addCrashSectionCallable("Level spawn location", new 4(this));
      category.addCrashSectionCallable("Level time", new 5(this));
      category.addCrashSectionCallable("Level dimension", new 6(this));
      category.addCrashSectionCallable("Level storage version", new 7(this));
      category.addCrashSectionCallable("Level weather", new 8(this));
      category.addCrashSectionCallable("Level game mode", new 9(this));
   }

   public void setDifficulty(EnumDifficulty newDifficulty) {
      this.difficulty = newDifficulty;
   }

   public GameType getGameType() {
      return this.theGameType;
   }

   public double getBorderSize() {
      return this.borderSize;
   }

   public void setSpawnY(int y) {
      this.spawnY = y;
   }

   public void getBorderCenterX(double posX) {
      this.borderCenterX = posX;
   }

   public double getBorderCenterX() {
      return this.borderCenterX;
   }

   public void setSpawnZ(int z) {
      this.spawnZ = z;
   }

   public void setSpawnX(int x) {
      this.spawnX = x;
   }

   public void getBorderCenterZ(double posZ) {
      this.borderCenterZ = posZ;
   }

   public double getBorderCenterZ() {
      return this.borderCenterZ;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public void setAllowCommands(boolean allow) {
      this.allowCommands = allow;
   }

   public void setHardcore(boolean hardcoreIn) {
      this.hardcore = hardcoreIn;
   }

   public void setBorderSize(double size) {
      this.borderSize = size;
   }

   public String getWorldName() {
      return this.levelName;
   }

   public NBTTagCompound getNBTTagCompound() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.updateTagCompound(nbttagcompound, this.playerTag);
      return nbttagcompound;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public void setDifficultyLocked(boolean locked) {
      this.difficultyLocked = locked;
   }

   public void setMapFeaturesEnabled(boolean enabled) {
      this.mapFeaturesEnabled = enabled;
   }

   public void setBorderSafeZone(double amount) {
      this.borderSafeZone = amount;
   }

   public void setBorderDamagePerBlock(double damage) {
      this.borderDamagePerBlock = damage;
   }

   public double getBorderLerpTarget() {
      return this.borderSizeLerpTarget;
   }

   public double getBorderDamagePerBlock() {
      return this.borderDamagePerBlock;
   }

   public int getBorderWarningDistance() {
      return this.borderWarningDistance;
   }

   public long getBorderLerpTime() {
      return this.borderSizeLerpTime;
   }

   public int getBorderWarningTime() {
      return this.borderWarningTime;
   }

   public double getBorderSafeZone() {
      return this.borderSafeZone;
   }

   public void setSpawn(BlockPos spawnPoint) {
      this.spawnX = spawnPoint.getX();
      this.spawnY = spawnPoint.getY();
      this.spawnZ = spawnPoint.getZ();
   }

   public void setGameType(GameType type) {
      this.theGameType = type;
   }

   public int getCleanWeatherTime() {
      return this.cleanWeatherTime;
   }

   public long getWorldTotalTime() {
      return this.totalTime;
   }

   public void setServerInitialized(boolean initializedIn) {
      this.initialized = initializedIn;
   }

   public void setCleanWeatherTime(int cleanWeatherTimeIn) {
      this.cleanWeatherTime = cleanWeatherTimeIn;
   }

   public void setWorldTotalTime(long time) {
      this.totalTime = time;
   }

   public GameRules getGameRulesInstance() {
      return this.theGameRules;
   }

   public boolean isHardcoreModeEnabled() {
      return this.hardcore;
   }

   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   public boolean areCommandsAllowed() {
      return this.allowCommands;
   }

   public NBTTagCompound getPlayerNBTTagCompound() {
      return this.playerTag;
   }

   public void setBorderWarningDistance(int amountOfBlocks) {
      this.borderWarningDistance = amountOfBlocks;
   }

   public void setBorderWarningTime(int ticks) {
      this.borderWarningTime = ticks;
   }

   public void setBorderLerpTime(long time) {
      this.borderSizeLerpTime = time;
   }

   public void setBorderLerpTarget(double lerpSize) {
      this.borderSizeLerpTarget = lerpSize;
   }

   public void setWorldName(String worldName) {
      this.levelName = worldName;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public String getGeneratorOptions() {
      return this.generatorOptions;
   }

   private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt) {
      nbt.setLong("RandomSeed", this.randomSeed);
      nbt.setString("generatorName", this.terrainType.getWorldTypeName());
      nbt.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
      nbt.setString("generatorOptions", this.generatorOptions);
      nbt.setInteger("GameType", this.theGameType.getID());
      nbt.setBoolean("MapFeatures", this.mapFeaturesEnabled);
      nbt.setInteger("SpawnX", this.spawnX);
      nbt.setInteger("SpawnY", this.spawnY);
      nbt.setInteger("SpawnZ", this.spawnZ);
      nbt.setLong("Time", this.totalTime);
      nbt.setLong("DayTime", this.worldTime);
      nbt.setLong("SizeOnDisk", this.sizeOnDisk);
      nbt.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
      nbt.setString("LevelName", this.levelName);
      nbt.setInteger("version", this.saveVersion);
      nbt.setInteger("clearWeatherTime", this.cleanWeatherTime);
      nbt.setInteger("rainTime", this.rainTime);
      nbt.setBoolean("raining", this.raining);
      nbt.setInteger("thunderTime", this.thunderTime);
      nbt.setBoolean("thundering", this.thundering);
      nbt.setBoolean("hardcore", this.hardcore);
      nbt.setBoolean("allowCommands", this.allowCommands);
      nbt.setBoolean("initialized", this.initialized);
      nbt.setDouble("BorderCenterX", this.borderCenterX);
      nbt.setDouble("BorderCenterZ", this.borderCenterZ);
      nbt.setDouble("BorderSize", this.borderSize);
      nbt.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
      nbt.setDouble("BorderSafeZone", this.borderSafeZone);
      nbt.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
      nbt.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
      nbt.setDouble("BorderWarningBlocks", (double)this.borderWarningDistance);
      nbt.setDouble("BorderWarningTime", (double)this.borderWarningTime);
      if(this.difficulty != null) {
         nbt.setByte("Difficulty", (byte)this.difficulty.getDifficultyId());
      }

      nbt.setBoolean("DifficultyLocked", this.difficultyLocked);
      nbt.setTag("GameRules", this.theGameRules.writeToNBT());
      if(playerNbt != null) {
         nbt.setTag("Player", playerNbt);
      }

   }

   public long getLastTimePlayed() {
      return this.lastTimePlayed;
   }

   public void populateFromWorldSettings(WorldSettings settings) {
      this.randomSeed = settings.getSeed();
      this.theGameType = settings.getGameType();
      this.mapFeaturesEnabled = settings.isMapFeaturesEnabled();
      this.hardcore = settings.getHardcoreEnabled();
      this.terrainType = settings.getTerrainType();
      this.generatorOptions = settings.getWorldName();
      this.allowCommands = settings.areCommandsAllowed();
   }

   public int getSaveVersion() {
      return this.saveVersion;
   }

   public void setTerrainType(WorldType type) {
      this.terrainType = type;
   }

   public void setSaveVersion(int version) {
      this.saveVersion = version;
   }

   public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.updateTagCompound(nbttagcompound, nbt);
      return nbttagcompound;
   }

   public long getSizeOnDisk() {
      return this.sizeOnDisk;
   }
}
