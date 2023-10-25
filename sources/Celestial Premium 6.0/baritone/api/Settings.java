/*
 * Decompiled with CFR 0.150.
 */
package baritone.api;

import baritone.api.utils.SettingsUtil;
import baritone.api.utils.TypeUtils;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;

public final class Settings {
    public final Setting<Boolean> allowBreak = new Setting(true);
    public final Setting<Boolean> allowSprint = new Setting(true);
    public final Setting<Boolean> allowPlace = new Setting(true);
    public final Setting<Boolean> allowInventory = new Setting(false);
    public final Setting<Boolean> assumeExternalAutoTool = new Setting(false);
    public final Setting<Boolean> disableAutoTool = new Setting(false);
    public final Setting<Double> blockPlacementPenalty = new Setting(20.0);
    public final Setting<Double> blockBreakAdditionalPenalty = new Setting(2.0);
    public final Setting<Double> jumpPenalty = new Setting(2.0);
    public final Setting<Double> walkOnWaterOnePenalty = new Setting(3.0);
    public final Setting<Boolean> allowWaterBucketFall = new Setting(true);
    public final Setting<Boolean> assumeWalkOnWater = new Setting(false);
    public final Setting<Boolean> assumeWalkOnLava = new Setting(false);
    public final Setting<Boolean> assumeStep = new Setting(false);
    public final Setting<Boolean> assumeSafeWalk = new Setting(false);
    public final Setting<Boolean> allowJumpAt256 = new Setting(false);
    public final Setting<Boolean> allowParkourAscend = new Setting(true);
    public final Setting<Boolean> allowDiagonalDescend = new Setting(false);
    public final Setting<Boolean> allowDiagonalAscend = new Setting(false);
    public final Setting<Boolean> allowDownward = new Setting(true);
    public final Setting<List<Item>> acceptableThrowawayItems = new Setting(new ArrayList<Item>(Arrays.asList(Item.getItemFromBlock(Blocks.DIRT), Item.getItemFromBlock(Blocks.COBBLESTONE), Item.getItemFromBlock(Blocks.NETHERRACK), Item.getItemFromBlock(Blocks.STONE))));
    public final Setting<List<Block>> blocksToAvoid = new Setting(new ArrayList());
    public final Setting<List<Block>> blocksToAvoidBreaking = new Setting(new ArrayList<Block>(Arrays.asList(Blocks.CRAFTING_TABLE, Blocks.FURNACE, Blocks.LIT_FURNACE, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.STANDING_SIGN, Blocks.WALL_SIGN)));
    public final Setting<List<Block>> buildIgnoreBlocks = new Setting(new ArrayList<Block>(Arrays.asList(new Block[0])));
    public final Setting<List<Block>> buildSkipBlocks = new Setting(new ArrayList<Block>(Arrays.asList(new Block[0])));
    public final Setting<Map<Block, List<Block>>> buildValidSubstitutes = new Setting(new HashMap());
    public final Setting<Map<Block, List<Block>>> buildSubstitutes = new Setting(new HashMap());
    public final Setting<List<Block>> okIfAir = new Setting(new ArrayList<Block>(Arrays.asList(new Block[0])));
    public final Setting<Boolean> buildIgnoreExisting = new Setting(false);
    public final Setting<Boolean> avoidUpdatingFallingBlocks = new Setting(true);
    public final Setting<Boolean> allowVines = new Setting(false);
    public final Setting<Boolean> allowWalkOnBottomSlab = new Setting(true);
    public final Setting<Boolean> allowParkour = new Setting(false);
    public final Setting<Boolean> allowParkourPlace = new Setting(false);
    public final Setting<Boolean> considerPotionEffects = new Setting(true);
    public final Setting<Boolean> sprintAscends = new Setting(true);
    public final Setting<Boolean> overshootTraverse = new Setting(true);
    public final Setting<Boolean> pauseMiningForFallingBlocks = new Setting(true);
    public final Setting<Integer> rightClickSpeed = new Setting(4);
    public final Setting<Float> blockReachDistance = new Setting(Float.valueOf(4.5f));
    public final Setting<Double> randomLooking = new Setting(0.01);
    public final Setting<Double> costHeuristic = new Setting(3.563);
    public final Setting<Integer> pathingMaxChunkBorderFetch = new Setting(50);
    public final Setting<Double> backtrackCostFavoringCoefficient = new Setting(0.5);
    public final Setting<Boolean> avoidance = new Setting(false);
    public final Setting<Double> mobSpawnerAvoidanceCoefficient = new Setting(2.0);
    public final Setting<Integer> mobSpawnerAvoidanceRadius = new Setting(16);
    public final Setting<Double> mobAvoidanceCoefficient = new Setting(1.5);
    public final Setting<Integer> mobAvoidanceRadius = new Setting(8);
    public final Setting<Boolean> rightClickContainerOnArrival = new Setting(true);
    public final Setting<Boolean> enterPortal = new Setting(true);
    public final Setting<Boolean> minimumImprovementRepropagation = new Setting(true);
    public final Setting<Boolean> cutoffAtLoadBoundary = new Setting(false);
    public final Setting<Double> maxCostIncrease = new Setting(10.0);
    public final Setting<Integer> costVerificationLookahead = new Setting(5);
    public final Setting<Double> pathCutoffFactor = new Setting(0.9);
    public final Setting<Integer> pathCutoffMinimumLength = new Setting(30);
    public final Setting<Integer> planningTickLookahead = new Setting(150);
    public final Setting<Integer> pathingMapDefaultSize = new Setting(1024);
    public final Setting<Float> pathingMapLoadFactor = new Setting(Float.valueOf(0.75f));
    public final Setting<Integer> maxFallHeightNoWater = new Setting(3);
    public final Setting<Integer> maxFallHeightBucket = new Setting(20);
    public final Setting<Boolean> allowOvershootDiagonalDescend = new Setting(true);
    public final Setting<Boolean> simplifyUnloadedYCoord = new Setting(true);
    public final Setting<Boolean> repackOnAnyBlockChange = new Setting(true);
    public final Setting<Integer> movementTimeoutTicks = new Setting(100);
    public final Setting<Long> primaryTimeoutMS = new Setting(500L);
    public final Setting<Long> failureTimeoutMS = new Setting(2000L);
    public final Setting<Long> planAheadPrimaryTimeoutMS = new Setting(4000L);
    public final Setting<Long> planAheadFailureTimeoutMS = new Setting(5000L);
    public final Setting<Boolean> slowPath = new Setting(false);
    public final Setting<Long> slowPathTimeDelayMS = new Setting(100L);
    public final Setting<Long> slowPathTimeoutMS = new Setting(40000L);
    public final Setting<Boolean> chunkCaching = new Setting(true);
    public final Setting<Boolean> pruneRegionsFromRAM = new Setting(true);
    public final Setting<Boolean> containerMemory = new Setting(false);
    public final Setting<Boolean> backfill = new Setting(false);
    public final Setting<Boolean> logAsToast = new Setting(false);
    public final Setting<Long> toastTimer = new Setting(5000L);
    public final Setting<Boolean> chatDebug = new Setting(false);
    public final Setting<Boolean> chatControl = new Setting(true);
    public final Setting<Boolean> chatControlAnyway = new Setting(false);
    public final Setting<Boolean> renderPath = new Setting(true);
    public final Setting<Boolean> renderPathAsLine = new Setting(false);
    public final Setting<Boolean> renderGoal = new Setting(true);
    public final Setting<Boolean> renderSelectionBoxes = new Setting(true);
    public final Setting<Boolean> renderGoalIgnoreDepth = new Setting(true);
    public final Setting<Boolean> renderGoalXZBeacon = new Setting(false);
    public final Setting<Boolean> renderSelectionBoxesIgnoreDepth = new Setting(true);
    public final Setting<Boolean> renderPathIgnoreDepth = new Setting(true);
    public final Setting<Float> pathRenderLineWidthPixels = new Setting(Float.valueOf(5.0f));
    public final Setting<Float> goalRenderLineWidthPixels = new Setting(Float.valueOf(3.0f));
    public final Setting<Boolean> fadePath = new Setting(false);
    public final Setting<Boolean> freeLook = new Setting(true);
    public final Setting<Boolean> antiCheatCompatibility = new Setting(true);
    public final Setting<Boolean> pathThroughCachedOnly = new Setting(false);
    public final Setting<Boolean> sprintInWater = new Setting(true);
    public final Setting<Boolean> blacklistClosestOnFailure = new Setting(true);
    public final Setting<Boolean> renderCachedChunks = new Setting(false);
    public final Setting<Float> cachedChunksOpacity = new Setting(Float.valueOf(0.5f));
    public final Setting<Boolean> prefixControl = new Setting(true);
    public final Setting<String> prefix = new Setting("#");
    public final Setting<Boolean> shortBaritonePrefix = new Setting(false);
    public final Setting<Boolean> echoCommands = new Setting(true);
    public final Setting<Boolean> censorCoordinates = new Setting(false);
    public final Setting<Boolean> censorRanCommands = new Setting(false);
    public final Setting<Boolean> itemSaver = new Setting(false);
    public final Setting<Boolean> preferSilkTouch = new Setting(false);
    public final Setting<Boolean> walkWhileBreaking = new Setting(true);
    public final Setting<Boolean> splicePath = new Setting(true);
    public final Setting<Integer> maxPathHistoryLength = new Setting(300);
    public final Setting<Integer> pathHistoryCutoffAmount = new Setting(50);
    public final Setting<Integer> mineGoalUpdateInterval = new Setting(5);
    public final Setting<Integer> maxCachedWorldScanCount = new Setting(10);
    public final Setting<Integer> minYLevelWhileMining = new Setting(0);
    public final Setting<Boolean> allowOnlyExposedOres = new Setting(false);
    public final Setting<Integer> allowOnlyExposedOresDistance = new Setting(1);
    public final Setting<Boolean> exploreForBlocks = new Setting(true);
    public final Setting<Integer> worldExploringChunkOffset = new Setting(0);
    public final Setting<Integer> exploreChunkSetMinimumSize = new Setting(10);
    public final Setting<Integer> exploreMaintainY = new Setting(64);
    public final Setting<Boolean> replantCrops = new Setting(true);
    public final Setting<Boolean> replantNetherWart = new Setting(false);
    public final Setting<Boolean> extendCacheOnThreshold = new Setting(false);
    public final Setting<Boolean> buildInLayers = new Setting(false);
    public final Setting<Boolean> layerOrder = new Setting(false);
    public final Setting<Integer> startAtLayer = new Setting(0);
    public final Setting<Boolean> skipFailedLayers = new Setting(false);
    public final Setting<Vec3i> buildRepeat = new Setting(new Vec3i(0, 0, 0));
    public final Setting<Integer> buildRepeatCount = new Setting(-1);
    public final Setting<Boolean> buildRepeatSneaky = new Setting(true);
    public final Setting<Boolean> breakFromAbove = new Setting(false);
    public final Setting<Boolean> goalBreakFromAbove = new Setting(false);
    public final Setting<Boolean> mapArtMode = new Setting(false);
    public final Setting<Boolean> okIfWater = new Setting(false);
    public final Setting<Integer> incorrectSize = new Setting(100);
    public final Setting<Double> breakCorrectBlockPenaltyMultiplier = new Setting(10.0);
    public final Setting<Boolean> schematicOrientationX = new Setting(false);
    public final Setting<Boolean> schematicOrientationY = new Setting(false);
    public final Setting<Boolean> schematicOrientationZ = new Setting(false);
    public final Setting<String> schematicFallbackExtension = new Setting("schematic");
    public final Setting<Integer> builderTickScanRadius = new Setting(5);
    public final Setting<Boolean> mineScanDroppedItems = new Setting(true);
    public final Setting<Long> mineDropLoiterDurationMSThanksLouca = new Setting(250L);
    public final Setting<Boolean> distanceTrim = new Setting(true);
    public final Setting<Boolean> cancelOnGoalInvalidation = new Setting(true);
    public final Setting<Integer> axisHeight = new Setting(120);
    public final Setting<Boolean> disconnectOnArrival = new Setting(false);
    public final Setting<Boolean> legitMine = new Setting(false);
    public final Setting<Integer> legitMineYLevel = new Setting(11);
    public final Setting<Boolean> legitMineIncludeDiagonals = new Setting(false);
    public final Setting<Boolean> forceInternalMining = new Setting(true);
    public final Setting<Boolean> internalMiningAirException = new Setting(true);
    public final Setting<Double> followOffsetDistance = new Setting(0.0);
    public final Setting<Float> followOffsetDirection = new Setting(Float.valueOf(0.0f));
    public final Setting<Integer> followRadius = new Setting(3);
    public final Setting<Boolean> disableCompletionCheck = new Setting(false);
    public final Setting<Long> cachedChunksExpirySeconds = new Setting(-1L);
    public final Setting<Consumer<ITextComponent>> logger = new Setting(Minecraft.getMinecraft().ingameGUI.getChatGUI()::printChatMessage);
    public final Setting<Double> yLevelBoxSize = new Setting(15.0);
    public final Setting<Color> colorCurrentPath = new Setting(Color.RED);
    public final Setting<Color> colorNextPath = new Setting(Color.MAGENTA);
    public final Setting<Color> colorBlocksToBreak = new Setting(Color.RED);
    public final Setting<Color> colorBlocksToPlace = new Setting(Color.GREEN);
    public final Setting<Color> colorBlocksToWalkInto = new Setting(Color.MAGENTA);
    public final Setting<Color> colorBestPathSoFar = new Setting(Color.BLUE);
    public final Setting<Color> colorMostRecentConsidered = new Setting(Color.CYAN);
    public final Setting<Color> colorGoalBox = new Setting(Color.GREEN);
    public final Setting<Color> colorInvertedGoalBox = new Setting(Color.RED);
    public final Setting<Color> colorSelection = new Setting(Color.CYAN);
    public final Setting<Color> colorSelectionPos1 = new Setting(Color.BLACK);
    public final Setting<Color> colorSelectionPos2 = new Setting(Color.ORANGE);
    public final Setting<Float> selectionOpacity = new Setting(Float.valueOf(0.5f));
    public final Setting<Float> selectionLineWidth = new Setting(Float.valueOf(2.0f));
    public final Setting<Boolean> renderSelection = new Setting(true);
    public final Setting<Boolean> renderSelectionIgnoreDepth = new Setting(true);
    public final Setting<Boolean> renderSelectionCorners = new Setting(true);
    public final Setting<Boolean> useSwordToMine = new Setting(true);
    public final Setting<Boolean> desktopNotifications = new Setting(false);
    public final Setting<Boolean> notificationOnPathComplete = new Setting(true);
    public final Setting<Boolean> notificationOnFarmFail = new Setting(true);
    public final Setting<Boolean> notificationOnBuildFinished = new Setting(true);
    public final Setting<Boolean> notificationOnExploreFinished = new Setting(true);
    public final Setting<Boolean> notificationOnMineFail = new Setting(true);
    public final Map<String, Setting<?>> byLowerName;
    public final List<Setting<?>> allSettings;
    public final Map<Setting<?>, Type> settingTypes;

    Settings() {
        Field[] temp = this.getClass().getFields();
        HashMap tmpByName = new HashMap();
        ArrayList tmpAll = new ArrayList();
        HashMap tmpSettingTypes = new HashMap();
        this.byLowerName = Collections.unmodifiableMap(tmpByName);
        this.allSettings = Collections.unmodifiableList(tmpAll);
        this.settingTypes = Collections.unmodifiableMap(tmpSettingTypes);
    }

    public <T> List<Setting<T>> getAllValuesByType(Class<T> cla$$) {
        ArrayList<Setting<T>> result = new ArrayList<Setting<T>>();
        for (Setting<?> setting : this.allSettings) {
            if (!setting.getValueClass().equals(cla$$)) continue;
            result.add(setting);
        }
        return result;
    }

    public final class Setting<T> {
        public T value;
        public final T defaultValue;
        private String name;

        private Setting(T value) {
            if (value == null) {
                throw new IllegalArgumentException("Cannot determine value type class from null");
            }
            this.value = value;
            this.defaultValue = value;
        }

        @Deprecated
        public final T get() {
            return this.value;
        }

        public final String getName() {
            return this.name;
        }

        public Class<T> getValueClass() {
            return TypeUtils.resolveBaseClass(this.getType());
        }

        public String toString() {
            return SettingsUtil.settingToString(this);
        }

        public void reset() {
            this.value = this.defaultValue;
        }

        public final Type getType() {
            return Settings.this.settingTypes.get(this);
        }
    }
}

