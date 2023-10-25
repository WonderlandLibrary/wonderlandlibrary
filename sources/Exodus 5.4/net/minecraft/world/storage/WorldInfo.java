/*
 * Decompiled with CFR 0.152.
 */
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
    private long worldTime;
    private double borderDamagePerBlock = 0.2;
    private long lastTimePlayed;
    private int thunderTime;
    private NBTTagCompound playerTag;
    private GameRules theGameRules;
    private double borderCenterX = 0.0;
    private int spawnX;
    private long randomSeed;
    private boolean mapFeaturesEnabled;
    private double borderCenterZ = 0.0;
    private boolean hardcore;
    private boolean thundering;
    private long borderSizeLerpTime = 0L;
    private WorldType terrainType = WorldType.DEFAULT;
    private long sizeOnDisk;
    private boolean initialized;
    private double borderSafeZone = 5.0;
    private String generatorOptions = "";
    private boolean raining;
    private int borderWarningTime = 15;
    private int borderWarningDistance = 5;
    private boolean allowCommands;
    public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
    private long totalTime;
    private EnumDifficulty difficulty;
    private int spawnZ;
    private boolean difficultyLocked;
    private double borderSize = 6.0E7;
    private int dimension;
    private int saveVersion;
    private WorldSettings.GameType theGameType;
    private double borderSizeLerpTarget = 0.0;
    private int rainTime;
    private int spawnY;
    private String levelName;
    private int cleanWeatherTime;

    public void setBorderSize(double d) {
        this.borderSize = d;
    }

    public void setBorderLerpTarget(double d) {
        this.borderSizeLerpTarget = d;
    }

    public void getBorderCenterX(double d) {
        this.borderCenterX = d;
    }

    public GameRules getGameRulesInstance() {
        return this.theGameRules;
    }

    private void updateTagCompound(NBTTagCompound nBTTagCompound, NBTTagCompound nBTTagCompound2) {
        nBTTagCompound.setLong("RandomSeed", this.randomSeed);
        nBTTagCompound.setString("generatorName", this.terrainType.getWorldTypeName());
        nBTTagCompound.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
        nBTTagCompound.setString("generatorOptions", this.generatorOptions);
        nBTTagCompound.setInteger("GameType", this.theGameType.getID());
        nBTTagCompound.setBoolean("MapFeatures", this.mapFeaturesEnabled);
        nBTTagCompound.setInteger("SpawnX", this.spawnX);
        nBTTagCompound.setInteger("SpawnY", this.spawnY);
        nBTTagCompound.setInteger("SpawnZ", this.spawnZ);
        nBTTagCompound.setLong("Time", this.totalTime);
        nBTTagCompound.setLong("DayTime", this.worldTime);
        nBTTagCompound.setLong("SizeOnDisk", this.sizeOnDisk);
        nBTTagCompound.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
        nBTTagCompound.setString("LevelName", this.levelName);
        nBTTagCompound.setInteger("version", this.saveVersion);
        nBTTagCompound.setInteger("clearWeatherTime", this.cleanWeatherTime);
        nBTTagCompound.setInteger("rainTime", this.rainTime);
        nBTTagCompound.setBoolean("raining", this.raining);
        nBTTagCompound.setInteger("thunderTime", this.thunderTime);
        nBTTagCompound.setBoolean("thundering", this.thundering);
        nBTTagCompound.setBoolean("hardcore", this.hardcore);
        nBTTagCompound.setBoolean("allowCommands", this.allowCommands);
        nBTTagCompound.setBoolean("initialized", this.initialized);
        nBTTagCompound.setDouble("BorderCenterX", this.borderCenterX);
        nBTTagCompound.setDouble("BorderCenterZ", this.borderCenterZ);
        nBTTagCompound.setDouble("BorderSize", this.borderSize);
        nBTTagCompound.setLong("BorderSizeLerpTime", this.borderSizeLerpTime);
        nBTTagCompound.setDouble("BorderSafeZone", this.borderSafeZone);
        nBTTagCompound.setDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
        nBTTagCompound.setDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
        nBTTagCompound.setDouble("BorderWarningBlocks", this.borderWarningDistance);
        nBTTagCompound.setDouble("BorderWarningTime", this.borderWarningTime);
        if (this.difficulty != null) {
            nBTTagCompound.setByte("Difficulty", (byte)this.difficulty.getDifficultyId());
        }
        nBTTagCompound.setBoolean("DifficultyLocked", this.difficultyLocked);
        nBTTagCompound.setTag("GameRules", this.theGameRules.writeToNBT());
        if (nBTTagCompound2 != null) {
            nBTTagCompound.setTag("Player", nBTTagCompound2);
        }
    }

    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }

    public void setSpawnY(int n) {
        this.spawnY = n;
    }

    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.updateTagCompound(nBTTagCompound, this.playerTag);
        return nBTTagCompound;
    }

    public void setThunderTime(int n) {
        this.thunderTime = n;
    }

    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.playerTag;
    }

    public String getGeneratorOptions() {
        return this.generatorOptions;
    }

    public void setBorderLerpTime(long l) {
        this.borderSizeLerpTime = l;
    }

    public WorldInfo(WorldInfo worldInfo) {
        this.theGameRules = new GameRules();
        this.randomSeed = worldInfo.randomSeed;
        this.terrainType = worldInfo.terrainType;
        this.generatorOptions = worldInfo.generatorOptions;
        this.theGameType = worldInfo.theGameType;
        this.mapFeaturesEnabled = worldInfo.mapFeaturesEnabled;
        this.spawnX = worldInfo.spawnX;
        this.spawnY = worldInfo.spawnY;
        this.spawnZ = worldInfo.spawnZ;
        this.totalTime = worldInfo.totalTime;
        this.worldTime = worldInfo.worldTime;
        this.lastTimePlayed = worldInfo.lastTimePlayed;
        this.sizeOnDisk = worldInfo.sizeOnDisk;
        this.playerTag = worldInfo.playerTag;
        this.dimension = worldInfo.dimension;
        this.levelName = worldInfo.levelName;
        this.saveVersion = worldInfo.saveVersion;
        this.rainTime = worldInfo.rainTime;
        this.raining = worldInfo.raining;
        this.thunderTime = worldInfo.thunderTime;
        this.thundering = worldInfo.thundering;
        this.hardcore = worldInfo.hardcore;
        this.allowCommands = worldInfo.allowCommands;
        this.initialized = worldInfo.initialized;
        this.theGameRules = worldInfo.theGameRules;
        this.difficulty = worldInfo.difficulty;
        this.difficultyLocked = worldInfo.difficultyLocked;
        this.borderCenterX = worldInfo.borderCenterX;
        this.borderCenterZ = worldInfo.borderCenterZ;
        this.borderSize = worldInfo.borderSize;
        this.borderSizeLerpTime = worldInfo.borderSizeLerpTime;
        this.borderSizeLerpTarget = worldInfo.borderSizeLerpTarget;
        this.borderSafeZone = worldInfo.borderSafeZone;
        this.borderDamagePerBlock = worldInfo.borderDamagePerBlock;
        this.borderWarningTime = worldInfo.borderWarningTime;
        this.borderWarningDistance = worldInfo.borderWarningDistance;
    }

    public void setSpawnZ(int n) {
        this.spawnZ = n;
    }

    public void setAllowCommands(boolean bl) {
        this.allowCommands = bl;
    }

    public double getBorderSize() {
        return this.borderSize;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public long getLastTimePlayed() {
        return this.lastTimePlayed;
    }

    public void setDifficulty(EnumDifficulty enumDifficulty) {
        this.difficulty = enumDifficulty;
    }

    public WorldType getTerrainType() {
        return this.terrainType;
    }

    public double getBorderLerpTarget() {
        return this.borderSizeLerpTarget;
    }

    public WorldSettings.GameType getGameType() {
        return this.theGameType;
    }

    public long getSizeOnDisk() {
        return this.sizeOnDisk;
    }

    public void setRainTime(int n) {
        this.rainTime = n;
    }

    public void setHardcore(boolean bl) {
        this.hardcore = bl;
    }

    public void setBorderSafeZone(double d) {
        this.borderSafeZone = d;
    }

    public int getBorderWarningTime() {
        return this.borderWarningTime;
    }

    public long getWorldTotalTime() {
        return this.totalTime;
    }

    public int getSaveVersion() {
        return this.saveVersion;
    }

    public double getBorderDamagePerBlock() {
        return this.borderDamagePerBlock;
    }

    public double getBorderSafeZone() {
        return this.borderSafeZone;
    }

    public boolean isHardcoreModeEnabled() {
        return this.hardcore;
    }

    public void setGameType(WorldSettings.GameType gameType) {
        this.theGameType = gameType;
    }

    public WorldInfo(WorldSettings worldSettings, String string) {
        this.theGameRules = new GameRules();
        this.populateFromWorldSettings(worldSettings);
        this.levelName = string;
        this.difficulty = DEFAULT_DIFFICULTY;
        this.initialized = false;
    }

    public void setDifficultyLocked(boolean bl) {
        this.difficultyLocked = bl;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public void setBorderWarningTime(int n) {
        this.borderWarningTime = n;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public void addToCrashReport(CrashReportCategory crashReportCategory) {
        crashReportCategory.addCrashSectionCallable("Level seed", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.valueOf(WorldInfo.this.getSeed());
            }
        });
        crashReportCategory.addCrashSectionCallable("Level generator", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.format("ID %02d - %s, ver %d. Features enabled: %b", WorldInfo.this.terrainType.getWorldTypeID(), WorldInfo.this.terrainType.getWorldTypeName(), WorldInfo.this.terrainType.getGeneratorVersion(), WorldInfo.this.mapFeaturesEnabled);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level generator options", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return WorldInfo.this.generatorOptions;
            }
        });
        crashReportCategory.addCrashSectionCallable("Level spawn location", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level time", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.format("%d game time, %d day time", WorldInfo.this.totalTime, WorldInfo.this.worldTime);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level dimension", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.valueOf(WorldInfo.this.dimension);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level storage version", new Callable<String>(){

            @Override
            public String call() throws Exception {
                String string = "Unknown?";
                try {
                    switch (WorldInfo.this.saveVersion) {
                        case 19132: {
                            string = "McRegion";
                            break;
                        }
                        case 19133: {
                            string = "Anvil";
                        }
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                return String.format("0x%05X - %s", WorldInfo.this.saveVersion, string);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level weather", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", WorldInfo.this.rainTime, WorldInfo.this.raining, WorldInfo.this.thunderTime, WorldInfo.this.thundering);
            }
        });
        crashReportCategory.addCrashSectionCallable("Level game mode", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", WorldInfo.this.theGameType.getName(), WorldInfo.this.theGameType.getID(), WorldInfo.this.hardcore, WorldInfo.this.allowCommands);
            }
        });
    }

    public void setCleanWeatherTime(int n) {
        this.cleanWeatherTime = n;
    }

    public double getBorderCenterZ() {
        return this.borderCenterZ;
    }

    public void setWorldTotalTime(long l) {
        this.totalTime = l;
    }

    public void setRaining(boolean bl) {
        this.raining = bl;
    }

    public double getBorderCenterX() {
        return this.borderCenterX;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public long getSeed() {
        return this.randomSeed;
    }

    protected WorldInfo() {
        this.theGameRules = new GameRules();
    }

    public int getCleanWeatherTime() {
        return this.cleanWeatherTime;
    }

    public String getWorldName() {
        return this.levelName;
    }

    public long getWorldTime() {
        return this.worldTime;
    }

    public WorldInfo(NBTTagCompound nBTTagCompound) {
        this.theGameRules = new GameRules();
        this.randomSeed = nBTTagCompound.getLong("RandomSeed");
        if (nBTTagCompound.hasKey("generatorName", 8)) {
            String string = nBTTagCompound.getString("generatorName");
            this.terrainType = WorldType.parseWorldType(string);
            if (this.terrainType == null) {
                this.terrainType = WorldType.DEFAULT;
            } else if (this.terrainType.isVersioned()) {
                int n = 0;
                if (nBTTagCompound.hasKey("generatorVersion", 99)) {
                    n = nBTTagCompound.getInteger("generatorVersion");
                }
                this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(n);
            }
            if (nBTTagCompound.hasKey("generatorOptions", 8)) {
                this.generatorOptions = nBTTagCompound.getString("generatorOptions");
            }
        }
        this.theGameType = WorldSettings.GameType.getByID(nBTTagCompound.getInteger("GameType"));
        this.mapFeaturesEnabled = nBTTagCompound.hasKey("MapFeatures", 99) ? nBTTagCompound.getBoolean("MapFeatures") : true;
        this.spawnX = nBTTagCompound.getInteger("SpawnX");
        this.spawnY = nBTTagCompound.getInteger("SpawnY");
        this.spawnZ = nBTTagCompound.getInteger("SpawnZ");
        this.totalTime = nBTTagCompound.getLong("Time");
        this.worldTime = nBTTagCompound.hasKey("DayTime", 99) ? nBTTagCompound.getLong("DayTime") : this.totalTime;
        this.lastTimePlayed = nBTTagCompound.getLong("LastPlayed");
        this.sizeOnDisk = nBTTagCompound.getLong("SizeOnDisk");
        this.levelName = nBTTagCompound.getString("LevelName");
        this.saveVersion = nBTTagCompound.getInteger("version");
        this.cleanWeatherTime = nBTTagCompound.getInteger("clearWeatherTime");
        this.rainTime = nBTTagCompound.getInteger("rainTime");
        this.raining = nBTTagCompound.getBoolean("raining");
        this.thunderTime = nBTTagCompound.getInteger("thunderTime");
        this.thundering = nBTTagCompound.getBoolean("thundering");
        this.hardcore = nBTTagCompound.getBoolean("hardcore");
        this.initialized = nBTTagCompound.hasKey("initialized", 99) ? nBTTagCompound.getBoolean("initialized") : true;
        if (nBTTagCompound.hasKey("allowCommands", 99)) {
            this.allowCommands = nBTTagCompound.getBoolean("allowCommands");
        } else {
            boolean bl = this.allowCommands = this.theGameType == WorldSettings.GameType.CREATIVE;
        }
        if (nBTTagCompound.hasKey("Player", 10)) {
            this.playerTag = nBTTagCompound.getCompoundTag("Player");
            this.dimension = this.playerTag.getInteger("Dimension");
        }
        if (nBTTagCompound.hasKey("GameRules", 10)) {
            this.theGameRules.readFromNBT(nBTTagCompound.getCompoundTag("GameRules"));
        }
        if (nBTTagCompound.hasKey("Difficulty", 99)) {
            this.difficulty = EnumDifficulty.getDifficultyEnum(nBTTagCompound.getByte("Difficulty"));
        }
        if (nBTTagCompound.hasKey("DifficultyLocked", 1)) {
            this.difficultyLocked = nBTTagCompound.getBoolean("DifficultyLocked");
        }
        if (nBTTagCompound.hasKey("BorderCenterX", 99)) {
            this.borderCenterX = nBTTagCompound.getDouble("BorderCenterX");
        }
        if (nBTTagCompound.hasKey("BorderCenterZ", 99)) {
            this.borderCenterZ = nBTTagCompound.getDouble("BorderCenterZ");
        }
        if (nBTTagCompound.hasKey("BorderSize", 99)) {
            this.borderSize = nBTTagCompound.getDouble("BorderSize");
        }
        if (nBTTagCompound.hasKey("BorderSizeLerpTime", 99)) {
            this.borderSizeLerpTime = nBTTagCompound.getLong("BorderSizeLerpTime");
        }
        if (nBTTagCompound.hasKey("BorderSizeLerpTarget", 99)) {
            this.borderSizeLerpTarget = nBTTagCompound.getDouble("BorderSizeLerpTarget");
        }
        if (nBTTagCompound.hasKey("BorderSafeZone", 99)) {
            this.borderSafeZone = nBTTagCompound.getDouble("BorderSafeZone");
        }
        if (nBTTagCompound.hasKey("BorderDamagePerBlock", 99)) {
            this.borderDamagePerBlock = nBTTagCompound.getDouble("BorderDamagePerBlock");
        }
        if (nBTTagCompound.hasKey("BorderWarningBlocks", 99)) {
            this.borderWarningDistance = nBTTagCompound.getInteger("BorderWarningBlocks");
        }
        if (nBTTagCompound.hasKey("BorderWarningTime", 99)) {
            this.borderWarningTime = nBTTagCompound.getInteger("BorderWarningTime");
        }
    }

    public void setSpawn(BlockPos blockPos) {
        this.spawnX = blockPos.getX();
        this.spawnY = blockPos.getY();
        this.spawnZ = blockPos.getZ();
    }

    public void setThundering(boolean bl) {
        this.thundering = bl;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public EnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void setWorldName(String string) {
        this.levelName = string;
    }

    public void getBorderCenterZ(double d) {
        this.borderCenterZ = d;
    }

    public void setSpawnX(int n) {
        this.spawnX = n;
    }

    public boolean areCommandsAllowed() {
        return this.allowCommands;
    }

    public void populateFromWorldSettings(WorldSettings worldSettings) {
        this.randomSeed = worldSettings.getSeed();
        this.theGameType = worldSettings.getGameType();
        this.mapFeaturesEnabled = worldSettings.isMapFeaturesEnabled();
        this.hardcore = worldSettings.getHardcoreEnabled();
        this.terrainType = worldSettings.getTerrainType();
        this.generatorOptions = worldSettings.getWorldName();
        this.allowCommands = worldSettings.areCommandsAllowed();
    }

    public int getBorderWarningDistance() {
        return this.borderWarningDistance;
    }

    public void setServerInitialized(boolean bl) {
        this.initialized = bl;
    }

    public NBTTagCompound cloneNBTCompound(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        this.updateTagCompound(nBTTagCompound2, nBTTagCompound);
        return nBTTagCompound2;
    }

    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }

    public void setBorderDamagePerBlock(double d) {
        this.borderDamagePerBlock = d;
    }

    public boolean isRaining() {
        return this.raining;
    }

    public void setWorldTime(long l) {
        this.worldTime = l;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setSaveVersion(int n) {
        this.saveVersion = n;
    }

    public void setBorderWarningDistance(int n) {
        this.borderWarningDistance = n;
    }

    public void setMapFeaturesEnabled(boolean bl) {
        this.mapFeaturesEnabled = bl;
    }

    public long getBorderLerpTime() {
        return this.borderSizeLerpTime;
    }

    public void setTerrainType(WorldType worldType) {
        this.terrainType = worldType;
    }
}

