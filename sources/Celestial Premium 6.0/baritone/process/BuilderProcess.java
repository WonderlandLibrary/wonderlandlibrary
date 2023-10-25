/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 */
package baritone.process;

import baritone.Baritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.pathing.goals.GoalGetToBlock;
import baritone.api.process.IBuilderProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.schematic.FillSchematic;
import baritone.api.schematic.ISchematic;
import baritone.api.schematic.IStaticSchematic;
import baritone.api.schematic.SubstituteSchematic;
import baritone.api.schematic.format.ISchematicFormat;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.RayTraceUtils;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import baritone.api.utils.input.Input;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.MovementHelper;
import baritone.utils.BaritoneProcessHelper;
import baritone.utils.BlockStateInterface;
import baritone.utils.NotificationHelper;
import baritone.utils.PathingCommandContext;
import baritone.utils.schematic.MapArtSchematic;
import baritone.utils.schematic.SchematicSystem;
import baritone.utils.schematic.schematica.SchematicaHelper;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class BuilderProcess
extends BaritoneProcessHelper
implements IBuilderProcess {
    private HashSet<BetterBlockPos> incorrectPositions;
    private LongOpenHashSet observedCompleted;
    private String name;
    private ISchematic realSchematic;
    private ISchematic schematic;
    private Vec3i origin;
    private int ticks;
    private boolean paused;
    private int layer;
    private int numRepeats;
    private List<IBlockState> approxPlaceable;

    public BuilderProcess(Baritone baritone) {
        super(baritone);
    }

    @Override
    public void build(String name, ISchematic schematic, Vec3i origin) {
        this.name = name;
        this.schematic = schematic;
        this.realSchematic = null;
        if (!((Map)Baritone.settings().buildSubstitutes.value).isEmpty()) {
            this.schematic = new SubstituteSchematic(this.schematic, (Map)Baritone.settings().buildSubstitutes.value);
        }
        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();
        if (((Boolean)Baritone.settings().schematicOrientationX.value).booleanValue()) {
            x += schematic.widthX();
        }
        if (((Boolean)Baritone.settings().schematicOrientationY.value).booleanValue()) {
            y += schematic.heightY();
        }
        if (((Boolean)Baritone.settings().schematicOrientationZ.value).booleanValue()) {
            z += schematic.lengthZ();
        }
        this.origin = new Vec3i(x, y, z);
        this.paused = false;
        this.layer = (Integer)Baritone.settings().startAtLayer.value;
        this.numRepeats = 0;
        this.observedCompleted = new LongOpenHashSet();
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public boolean build(String name, File schematic, Vec3i origin) {
        ISchematic parsed;
        Optional<ISchematicFormat> format = SchematicSystem.INSTANCE.getByFile(schematic);
        if (!format.isPresent()) {
            return false;
        }
        try {
            parsed = format.get().parse(new FileInputStream(schematic));
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (((Boolean)Baritone.settings().mapArtMode.value).booleanValue()) {
            parsed = new MapArtSchematic((IStaticSchematic)parsed);
        }
        this.build(name, parsed, origin);
        return true;
    }

    @Override
    public void buildOpenSchematic() {
        if (SchematicaHelper.isSchematicaPresent()) {
            Optional<Tuple<IStaticSchematic, BlockPos>> schematic = SchematicaHelper.getOpenSchematic();
            if (schematic.isPresent()) {
                IStaticSchematic s = schematic.get().getFirst();
                this.build(schematic.get().getFirst().toString(), (Boolean)Baritone.settings().mapArtMode.value != false ? new MapArtSchematic(s) : s, (Vec3i)schematic.get().getSecond());
            } else {
                this.logDirect("No schematic currently open");
            }
        } else {
            this.logDirect("Schematica is not present");
        }
    }

    @Override
    public void clearArea(BlockPos corner1, BlockPos corner2) {
        BlockPos origin = new BlockPos(Math.min(corner1.getX(), corner2.getX()), Math.min(corner1.getY(), corner2.getY()), Math.min(corner1.getZ(), corner2.getZ()));
        int widthX = Math.abs(corner1.getX() - corner2.getX()) + 1;
        int heightY = Math.abs(corner1.getY() - corner2.getY()) + 1;
        int lengthZ = Math.abs(corner1.getZ() - corner2.getZ()) + 1;
        this.build("clear area", new FillSchematic(widthX, heightY, lengthZ, Blocks.AIR.getDefaultState()), (Vec3i)origin);
    }

    @Override
    public List<IBlockState> getApproxPlaceable() {
        return new ArrayList<IBlockState>(this.approxPlaceable);
    }

    @Override
    public boolean isActive() {
        return this.schematic != null;
    }

    public IBlockState placeAt(int x, int y, int z, IBlockState current) {
        if (!this.isActive()) {
            return null;
        }
        if (!this.schematic.inSchematic(x - this.origin.getX(), y - this.origin.getY(), z - this.origin.getZ(), current)) {
            return null;
        }
        IBlockState state = this.schematic.desiredState(x - this.origin.getX(), y - this.origin.getY(), z - this.origin.getZ(), current, this.approxPlaceable);
        if (state.getBlock() == Blocks.AIR) {
            return null;
        }
        return state;
    }

    private Optional<Tuple<BetterBlockPos, Rotation>> toBreakNearPlayer(BuilderCalculationContext bcc) {
        BetterBlockPos center = this.ctx.playerFeet();
        BetterBlockPos pathStart = this.baritone.getPathingBehavior().pathStart();
        for (int dx = -5; dx <= 5; ++dx) {
            int dy;
            int n = dy = (Boolean)Baritone.settings().breakFromAbove.value != false ? -1 : 0;
            while (dy <= 5) {
                for (int dz = -5; dz <= 5; ++dz) {
                    IBlockState curr;
                    IBlockState desired;
                    int x = center.x + dx;
                    int y = center.y + dy;
                    int z = center.z + dz;
                    if (dy == -1 && x == pathStart.x && z == pathStart.z || (desired = bcc.getSchematic(x, y, z, bcc.bsi.get0(x, y, z))) == null || (curr = bcc.bsi.get0(x, y, z)).getBlock() == Blocks.AIR || curr.getBlock() instanceof BlockLiquid || this.valid(curr, desired, false)) continue;
                    BetterBlockPos pos = new BetterBlockPos(x, y, z);
                    Optional<Rotation> rot = RotationUtils.reachable(this.ctx.player(), (BlockPos)pos, this.ctx.playerController().getBlockReachDistance());
                    if (!rot.isPresent()) continue;
                    return Optional.of(new Tuple<BetterBlockPos, Rotation>(pos, rot.get()));
                }
                ++dy;
            }
        }
        return Optional.empty();
    }

    private Optional<Placement> searchForPlaceables(BuilderCalculationContext bcc, List<IBlockState> desirableOnHotbar) {
        BetterBlockPos center = this.ctx.playerFeet();
        for (int dx = -5; dx <= 5; ++dx) {
            for (int dy = -5; dy <= 1; ++dy) {
                for (int dz = -5; dz <= 5; ++dz) {
                    IBlockState curr;
                    int x = center.x + dx;
                    int y = center.y + dy;
                    int z = center.z + dz;
                    IBlockState desired = bcc.getSchematic(x, y, z, bcc.bsi.get0(x, y, z));
                    if (desired == null || !MovementHelper.isReplaceable(x, y, z, curr = bcc.bsi.get0(x, y, z), bcc.bsi) || this.valid(curr, desired, false) || dy == 1 && bcc.bsi.get0(x, y + 1, z).getBlock() == Blocks.AIR) continue;
                    desirableOnHotbar.add(desired);
                    Optional<Placement> opt = this.possibleToPlace(desired, x, y, z, bcc.bsi);
                    if (!opt.isPresent()) continue;
                    return opt;
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Placement> possibleToPlace(IBlockState toPlace, int x, int y, int z, BlockStateInterface bsi) {
        for (EnumFacing against : EnumFacing.values()) {
            BetterBlockPos placeAgainstPos = new BetterBlockPos(x, y, z).offset(against);
            IBlockState placeAgainstState = bsi.get0(placeAgainstPos);
            if (MovementHelper.isReplaceable(placeAgainstPos.x, placeAgainstPos.y, placeAgainstPos.z, placeAgainstState, bsi) || !this.ctx.world().mayPlace(toPlace.getBlock(), new BetterBlockPos(x, y, z), false, against, null)) continue;
            AxisAlignedBB aabb = placeAgainstState.getBoundingBox(this.ctx.world(), placeAgainstPos);
            for (Vec3d placementMultiplier : BuilderProcess.aabbSideMultipliers(against)) {
                OptionalInt hotbar;
                double placeX = (double)placeAgainstPos.x + aabb.minX * placementMultiplier.x + aabb.maxX * (1.0 - placementMultiplier.x);
                double placeY = (double)placeAgainstPos.y + aabb.minY * placementMultiplier.y + aabb.maxY * (1.0 - placementMultiplier.y);
                double placeZ = (double)placeAgainstPos.z + aabb.minZ * placementMultiplier.z + aabb.maxZ * (1.0 - placementMultiplier.z);
                Rotation rot = RotationUtils.calcRotationFromVec3d(RayTraceUtils.inferSneakingEyePosition(this.ctx.player()), new Vec3d(placeX, placeY, placeZ), this.ctx.playerRotations());
                RayTraceResult result = RayTraceUtils.rayTraceTowards(this.ctx.player(), rot, this.ctx.playerController().getBlockReachDistance(), true);
                if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK || !result.getBlockPos().equals(placeAgainstPos) || result.sideHit != against.getOpposite() || !(hotbar = this.hasAnyItemThatWouldPlace(toPlace, result, rot)).isPresent()) continue;
                return Optional.of(new Placement(hotbar.getAsInt(), placeAgainstPos, against.getOpposite(), rot));
            }
        }
        return Optional.empty();
    }

    private OptionalInt hasAnyItemThatWouldPlace(IBlockState desired, RayTraceResult result, Rotation rot) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = this.ctx.player().inventory.mainInventory.get(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemBlock)) continue;
            float originalYaw = this.ctx.player().rotationYaw;
            float originalPitch = this.ctx.player().rotationPitch;
            this.ctx.player().rotationYaw = rot.getYaw();
            this.ctx.player().rotationPitch = rot.getPitch();
            IBlockState wouldBePlaced = ((ItemBlock)stack.getItem()).getBlock().getStateForPlacement(this.ctx.world(), result.getBlockPos().offset(result.sideHit), result.sideHit, (float)result.hitVec.x - (float)result.getBlockPos().getX(), (float)result.hitVec.y - (float)result.getBlockPos().getY(), (float)result.hitVec.z - (float)result.getBlockPos().getZ(), stack.getItem().getMetadata(stack.getMetadata()), this.ctx.player());
            this.ctx.player().rotationYaw = originalYaw;
            this.ctx.player().rotationPitch = originalPitch;
            if (!this.valid(wouldBePlaced, desired, true)) continue;
            return OptionalInt.of(i);
        }
        return OptionalInt.empty();
    }

    private static Vec3d[] aabbSideMultipliers(EnumFacing side) {
        switch (side) {
            case UP: {
                return new Vec3d[]{new Vec3d(0.5, 1.0, 0.5), new Vec3d(0.1, 1.0, 0.5), new Vec3d(0.9, 1.0, 0.5), new Vec3d(0.5, 1.0, 0.1), new Vec3d(0.5, 1.0, 0.9)};
            }
            case DOWN: {
                return new Vec3d[]{new Vec3d(0.5, 0.0, 0.5), new Vec3d(0.1, 0.0, 0.5), new Vec3d(0.9, 0.0, 0.5), new Vec3d(0.5, 0.0, 0.1), new Vec3d(0.5, 0.0, 0.9)};
            }
            case NORTH: 
            case SOUTH: 
            case EAST: 
            case WEST: {
                double x = side.getXOffset() == 0 ? 0.5 : (double)(1 + side.getXOffset()) / 2.0;
                double z = side.getZOffset() == 0 ? 0.5 : (double)(1 + side.getZOffset()) / 2.0;
                return new Vec3d[]{new Vec3d(x, 0.25, z), new Vec3d(x, 0.75, z)};
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel) {
        return this.onTick(calcFailed, isSafeToCancel, 0);
    }

    public PathingCommand onTick(boolean calcFailed, boolean isSafeToCancel, int recursions) {
        Goal goal;
        Optional<Tuple<BetterBlockPos, Rotation>> toBreak;
        BuilderCalculationContext bcc;
        if (recursions > 1000) {
            return new PathingCommand(null, PathingCommandType.SET_GOAL_AND_PATH);
        }
        this.approxPlaceable = this.approxPlaceable(36);
        this.ticks = this.baritone.getInputOverrideHandler().isInputForcedDown(Input.CLICK_LEFT) ? 5 : --this.ticks;
        this.baritone.getInputOverrideHandler().clearAllKeys();
        if (this.paused) {
            return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
        }
        if (((Boolean)Baritone.settings().buildInLayers.value).booleanValue()) {
            int minYInclusive;
            int maxYInclusive;
            if (this.realSchematic == null) {
                this.realSchematic = this.schematic;
            }
            final ISchematic realSchematic = this.realSchematic;
            if (((Boolean)Baritone.settings().layerOrder.value).booleanValue()) {
                maxYInclusive = realSchematic.heightY() - 1;
                minYInclusive = realSchematic.heightY() - this.layer;
            } else {
                maxYInclusive = this.layer - 1;
                minYInclusive = 0;
            }
            this.schematic = new ISchematic(){

                @Override
                public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
                    return realSchematic.desiredState(x, y, z, current, BuilderProcess.this.approxPlaceable);
                }

                @Override
                public boolean inSchematic(int x, int y, int z, IBlockState currentState) {
                    return ISchematic.super.inSchematic(x, y, z, currentState) && y >= minYInclusive && y <= maxYInclusive && realSchematic.inSchematic(x, y, z, currentState);
                }

                @Override
                public void reset() {
                    realSchematic.reset();
                }

                @Override
                public int widthX() {
                    return realSchematic.widthX();
                }

                @Override
                public int heightY() {
                    return realSchematic.heightY();
                }

                @Override
                public int lengthZ() {
                    return realSchematic.lengthZ();
                }
            };
        }
        if (!this.recalc(bcc = new BuilderCalculationContext())) {
            if (((Boolean)Baritone.settings().buildInLayers.value).booleanValue() && this.layer < this.realSchematic.heightY()) {
                this.logDirect("Starting layer " + this.layer);
                ++this.layer;
                return this.onTick(calcFailed, isSafeToCancel, recursions + 1);
            }
            Vec3i repeat = (Vec3i)Baritone.settings().buildRepeat.value;
            int max = (Integer)Baritone.settings().buildRepeatCount.value;
            ++this.numRepeats;
            if (repeat.equals(new Vec3i(0, 0, 0)) || max != -1 && this.numRepeats >= max) {
                this.logDirect("Done building");
                if (((Boolean)Baritone.settings().desktopNotifications.value).booleanValue() && ((Boolean)Baritone.settings().notificationOnBuildFinished.value).booleanValue()) {
                    NotificationHelper.notify("Done building", false);
                }
                this.onLostControl();
                return null;
            }
            this.layer = 0;
            this.origin = new BlockPos(this.origin).add(repeat);
            if (!((Boolean)Baritone.settings().buildRepeatSneaky.value).booleanValue()) {
                this.schematic.reset();
            }
            this.logDirect("Repeating build in vector " + repeat + ", new origin is " + this.origin);
            return this.onTick(calcFailed, isSafeToCancel, recursions + 1);
        }
        if (((Boolean)Baritone.settings().distanceTrim.value).booleanValue()) {
            this.trim();
        }
        if ((toBreak = this.toBreakNearPlayer(bcc)).isPresent() && isSafeToCancel && this.ctx.player().onGround) {
            Rotation rot = toBreak.get().getSecond();
            BetterBlockPos pos = toBreak.get().getFirst();
            this.baritone.getLookBehavior().updateTarget(rot, true);
            MovementHelper.switchToBestToolFor(this.ctx, bcc.get(pos));
            if (this.ctx.player().isSneaking()) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.SNEAK, true);
            }
            if (this.ctx.isLookingAt(pos) || this.ctx.playerRotations().isReallyCloseTo(rot)) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_LEFT, true);
            }
            return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
        }
        ArrayList<IBlockState> desirableOnHotbar = new ArrayList<IBlockState>();
        Optional<Placement> toPlace = this.searchForPlaceables(bcc, desirableOnHotbar);
        if (toPlace.isPresent() && isSafeToCancel && this.ctx.player().onGround && this.ticks <= 0) {
            Rotation rot = toPlace.get().rot;
            this.baritone.getLookBehavior().updateTarget(rot, true);
            this.ctx.player().inventory.currentItem = toPlace.get().hotbarSelection;
            this.baritone.getInputOverrideHandler().setInputForceState(Input.SNEAK, true);
            if (this.ctx.isLookingAt(toPlace.get().placeAgainst) && this.ctx.objectMouseOver().sideHit.equals(toPlace.get().side) || this.ctx.playerRotations().isReallyCloseTo(rot)) {
                this.baritone.getInputOverrideHandler().setInputForceState(Input.CLICK_RIGHT, true);
            }
            return new PathingCommand(null, PathingCommandType.CANCEL_AND_SET_GOAL);
        }
        if (((Boolean)Baritone.settings().allowInventory.value).booleanValue()) {
            ArrayList<Integer> usefulSlots = new ArrayList<Integer>();
            ArrayList<IBlockState> noValidHotbarOption = new ArrayList<IBlockState>();
            block0: for (IBlockState desired : desirableOnHotbar) {
                for (int i = 0; i < 9; ++i) {
                    if (!this.valid(this.approxPlaceable.get(i), desired, true)) continue;
                    usefulSlots.add(i);
                    continue block0;
                }
                noValidHotbarOption.add(desired);
            }
            block2: for (int i = 9; i < 36; ++i) {
                for (IBlockState desired : noValidHotbarOption) {
                    if (!this.valid(this.approxPlaceable.get(i), desired, true)) continue;
                    this.baritone.getInventoryBehavior().attemptToPutOnHotbar(i, usefulSlots::contains);
                    break block2;
                }
            }
        }
        if ((goal = this.assemble(bcc, this.approxPlaceable.subList(0, 9))) == null && (goal = this.assemble(bcc, this.approxPlaceable, true)) == null) {
            if (((Boolean)Baritone.settings().skipFailedLayers.value).booleanValue() && ((Boolean)Baritone.settings().buildInLayers.value).booleanValue() && this.layer < this.realSchematic.heightY()) {
                this.logDirect("Skipping layer that I cannot construct! Layer #" + this.layer);
                ++this.layer;
                return this.onTick(calcFailed, isSafeToCancel, recursions + 1);
            }
            this.logDirect("Unable to do it. Pausing. resume to resume, cancel to cancel");
            this.paused = true;
            return new PathingCommand(null, PathingCommandType.REQUEST_PAUSE);
        }
        return new PathingCommandContext(goal, PathingCommandType.FORCE_REVALIDATE_GOAL_AND_PATH, bcc);
    }

    private boolean recalc(BuilderCalculationContext bcc) {
        if (this.incorrectPositions == null) {
            this.incorrectPositions = new HashSet();
            this.fullRecalc(bcc);
            if (this.incorrectPositions.isEmpty()) {
                return false;
            }
        }
        this.recalcNearby(bcc);
        if (this.incorrectPositions.isEmpty()) {
            this.fullRecalc(bcc);
        }
        return !this.incorrectPositions.isEmpty();
    }

    private void trim() {
        HashSet<BetterBlockPos> copy = new HashSet<BetterBlockPos>(this.incorrectPositions);
        copy.removeIf(pos -> pos.distanceSq(this.ctx.player().posX, this.ctx.player().posY, this.ctx.player().posZ) > 200.0);
        if (!copy.isEmpty()) {
            this.incorrectPositions = copy;
        }
    }

    private void recalcNearby(BuilderCalculationContext bcc) {
        BetterBlockPos center = this.ctx.playerFeet();
        int radius = (Integer)Baritone.settings().builderTickScanRadius.value;
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dy = -radius; dy <= radius; ++dy) {
                for (int dz = -radius; dz <= radius; ++dz) {
                    int x = center.x + dx;
                    int y = center.y + dy;
                    int z = center.z + dz;
                    IBlockState desired = bcc.getSchematic(x, y, z, bcc.bsi.get0(x, y, z));
                    if (desired == null) continue;
                    BetterBlockPos pos = new BetterBlockPos(x, y, z);
                    if (this.valid(bcc.bsi.get0(x, y, z), desired, false)) {
                        this.incorrectPositions.remove(pos);
                        this.observedCompleted.add(BetterBlockPos.longHash(pos));
                        continue;
                    }
                    this.incorrectPositions.add(pos);
                    this.observedCompleted.remove(BetterBlockPos.longHash(pos));
                }
            }
        }
    }

    private void fullRecalc(BuilderCalculationContext bcc) {
        this.incorrectPositions = new HashSet();
        for (int y = 0; y < this.schematic.heightY(); ++y) {
            for (int z = 0; z < this.schematic.lengthZ(); ++z) {
                for (int x = 0; x < this.schematic.widthX(); ++x) {
                    int blockZ;
                    int blockY;
                    int blockX = x + this.origin.getX();
                    IBlockState current = bcc.bsi.get0(blockX, blockY = y + this.origin.getY(), blockZ = z + this.origin.getZ());
                    if (!this.schematic.inSchematic(x, y, z, current)) continue;
                    if (bcc.bsi.worldContainsLoadedChunk(blockX, blockZ)) {
                        if (this.valid(bcc.bsi.get0(blockX, blockY, blockZ), this.schematic.desiredState(x, y, z, current, this.approxPlaceable), false)) {
                            this.observedCompleted.add(BetterBlockPos.longHash(blockX, blockY, blockZ));
                            continue;
                        }
                        this.incorrectPositions.add(new BetterBlockPos(blockX, blockY, blockZ));
                        this.observedCompleted.remove(BetterBlockPos.longHash(blockX, blockY, blockZ));
                        if (this.incorrectPositions.size() <= (Integer)Baritone.settings().incorrectSize.value) continue;
                        return;
                    }
                    if (this.observedCompleted.contains(BetterBlockPos.longHash(blockX, blockY, blockZ)) || ((List)Baritone.settings().buildSkipBlocks.value).contains(this.schematic.desiredState(x, y, z, current, this.approxPlaceable).getBlock())) continue;
                    this.incorrectPositions.add(new BetterBlockPos(blockX, blockY, blockZ));
                    if (this.incorrectPositions.size() <= (Integer)Baritone.settings().incorrectSize.value) continue;
                    return;
                }
            }
        }
    }

    private Goal assemble(BuilderCalculationContext bcc, List<IBlockState> approxPlaceable) {
        return this.assemble(bcc, approxPlaceable, false);
    }

    private Goal assemble(BuilderCalculationContext bcc, List<IBlockState> approxPlaceable, boolean logMissing) {
        ArrayList placeable = new ArrayList();
        ArrayList breakable = new ArrayList();
        ArrayList sourceLiquids = new ArrayList();
        ArrayList flowingLiquids = new ArrayList();
        HashMap missing = new HashMap();
        this.incorrectPositions.forEach(pos -> {
            IBlockState state = bcc.bsi.get0((BlockPos)pos);
            if (state.getBlock() instanceof BlockAir) {
                if (approxPlaceable.contains(bcc.getSchematic(pos.x, pos.y, pos.z, state))) {
                    placeable.add(pos);
                } else {
                    IBlockState desired = bcc.getSchematic(pos.x, pos.y, pos.z, state);
                    missing.put(desired, 1 + missing.getOrDefault(desired, 0));
                }
            } else if (state.getBlock() instanceof BlockLiquid) {
                if (!MovementHelper.possiblyFlowing(state)) {
                    sourceLiquids.add(pos);
                } else {
                    flowingLiquids.add(pos);
                }
            } else {
                breakable.add(pos);
            }
        });
        ArrayList toBreak = new ArrayList();
        breakable.forEach(pos -> toBreak.add(this.breakGoal((BlockPos)pos, bcc)));
        ArrayList toPlace = new ArrayList();
        placeable.forEach(pos -> {
            if (!placeable.contains(pos.down()) && !placeable.contains(pos.down(2))) {
                toPlace.add(this.placementGoal((BlockPos)pos, bcc));
            }
        });
        sourceLiquids.forEach(pos -> toPlace.add(new GoalBlock(pos.up())));
        if (!toPlace.isEmpty()) {
            return new JankyGoalComposite(new GoalComposite(toPlace.toArray(new Goal[0])), new GoalComposite(toBreak.toArray(new Goal[0])));
        }
        if (toBreak.isEmpty()) {
            if (logMissing && !missing.isEmpty()) {
                this.logDirect("Missing materials for at least:");
                this.logDirect(missing.entrySet().stream().map(e -> String.format("%sx %s", e.getValue(), e.getKey())).collect(Collectors.joining("\n")));
            }
            if (logMissing && !flowingLiquids.isEmpty()) {
                this.logDirect("Unreplaceable liquids at at least:");
                this.logDirect(flowingLiquids.stream().map(p -> String.format("%s %s %s", p.x, p.y, p.z)).collect(Collectors.joining("\n")));
            }
            return null;
        }
        return new GoalComposite(toBreak.toArray(new Goal[0]));
    }

    private Goal placementGoal(BlockPos pos, BuilderCalculationContext bcc) {
        if (this.ctx.world().getBlockState(pos).getBlock() != Blocks.AIR) {
            return new GoalPlace(pos);
        }
        boolean allowSameLevel = this.ctx.world().getBlockState(pos.up()).getBlock() != Blocks.AIR;
        IBlockState current = this.ctx.world().getBlockState(pos);
        for (EnumFacing facing : Movement.HORIZONTALS_BUT_ALSO_DOWN_____SO_EVERY_DIRECTION_EXCEPT_UP) {
            if (!MovementHelper.canPlaceAgainst(this.ctx, pos.offset(facing)) || !this.ctx.world().mayPlace(bcc.getSchematic(pos.getX(), pos.getY(), pos.getZ(), current).getBlock(), pos, false, facing, null)) continue;
            return new GoalAdjacent(pos, pos.offset(facing), allowSameLevel);
        }
        return new GoalPlace(pos);
    }

    private Goal breakGoal(BlockPos pos, BuilderCalculationContext bcc) {
        if (((Boolean)Baritone.settings().goalBreakFromAbove.value).booleanValue() && bcc.bsi.get0(pos.up()).getBlock() instanceof BlockAir && bcc.bsi.get0(pos.up(2)).getBlock() instanceof BlockAir) {
            return new JankyGoalComposite(new GoalBreak(pos), new GoalGetToBlock(pos.up()){

                @Override
                public boolean isInGoal(int x, int y, int z) {
                    if (y > this.y || x == this.x && y == this.y && z == this.z) {
                        return false;
                    }
                    return super.isInGoal(x, y, z);
                }
            });
        }
        return new GoalBreak(pos);
    }

    @Override
    public void onLostControl() {
        this.incorrectPositions = null;
        this.name = null;
        this.schematic = null;
        this.realSchematic = null;
        this.layer = (Integer)Baritone.settings().startAtLayer.value;
        this.numRepeats = 0;
        this.paused = false;
        this.observedCompleted = null;
    }

    @Override
    public String displayName0() {
        return this.paused ? "Builder Paused" : "Building " + this.name;
    }

    private List<IBlockState> approxPlaceable(int size) {
        ArrayList<IBlockState> result = new ArrayList<IBlockState>();
        for (int i = 0; i < size; ++i) {
            ItemStack stack = this.ctx.player().inventory.mainInventory.get(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemBlock)) {
                result.add(Blocks.AIR.getDefaultState());
                continue;
            }
            result.add(((ItemBlock)stack.getItem()).getBlock().getStateForPlacement(this.ctx.world(), this.ctx.playerFeet(), EnumFacing.UP, (float)this.ctx.player().posX, (float)this.ctx.player().posY, (float)this.ctx.player().posZ, stack.getItem().getMetadata(stack.getMetadata()), this.ctx.player()));
        }
        return result;
    }

    private boolean valid(IBlockState current, IBlockState desired, boolean itemVerify) {
        if (desired == null) {
            return true;
        }
        if (current.getBlock() instanceof BlockLiquid && ((Boolean)Baritone.settings().okIfWater.value).booleanValue()) {
            return true;
        }
        if (current.getBlock() instanceof BlockAir && ((List)Baritone.settings().okIfAir.value).contains(desired.getBlock())) {
            return true;
        }
        if (desired.getBlock() instanceof BlockAir && ((List)Baritone.settings().buildIgnoreBlocks.value).contains(current.getBlock())) {
            return true;
        }
        if (!(current.getBlock() instanceof BlockAir) && ((Boolean)Baritone.settings().buildIgnoreExisting.value).booleanValue() && !itemVerify) {
            return true;
        }
        if (((List)Baritone.settings().buildSkipBlocks.value).contains(desired.getBlock()) && !itemVerify) {
            return true;
        }
        if (((Map)Baritone.settings().buildValidSubstitutes.value).getOrDefault(desired.getBlock(), Collections.emptyList()).contains(current.getBlock()) && !itemVerify) {
            return true;
        }
        return current.equals(desired);
    }

    public class BuilderCalculationContext
    extends CalculationContext {
        private final List<IBlockState> placeable;
        private final ISchematic schematic;
        private final int originX;
        private final int originY;
        private final int originZ;

        public BuilderCalculationContext() {
            super(BuilderProcess.this.baritone, true);
            this.placeable = BuilderProcess.this.approxPlaceable(9);
            this.schematic = BuilderProcess.this.schematic;
            this.originX = BuilderProcess.this.origin.getX();
            this.originY = BuilderProcess.this.origin.getY();
            this.originZ = BuilderProcess.this.origin.getZ();
            this.jumpPenalty += 10.0;
            this.backtrackCostFavoringCoefficient = 1.0;
        }

        private IBlockState getSchematic(int x, int y, int z, IBlockState current) {
            if (this.schematic.inSchematic(x - this.originX, y - this.originY, z - this.originZ, current)) {
                return this.schematic.desiredState(x - this.originX, y - this.originY, z - this.originZ, current, BuilderProcess.this.approxPlaceable);
            }
            return null;
        }

        @Override
        public double costOfPlacingAt(int x, int y, int z, IBlockState current) {
            if (this.isPossiblyProtected(x, y, z) || !this.worldBorder.canPlaceAt(x, z)) {
                return 1000000.0;
            }
            IBlockState sch = this.getSchematic(x, y, z, current);
            if (sch != null && !((List)Baritone.settings().buildSkipBlocks.value).contains(sch.getBlock())) {
                if (sch.getBlock() == Blocks.AIR) {
                    return this.placeBlockCost * 2.0;
                }
                if (this.placeable.contains(sch)) {
                    return 0.0;
                }
                if (!this.hasThrowaway) {
                    return 1000000.0;
                }
                return this.placeBlockCost * 3.0;
            }
            if (this.hasThrowaway) {
                return this.placeBlockCost;
            }
            return 1000000.0;
        }

        @Override
        public double breakCostMultiplierAt(int x, int y, int z, IBlockState current) {
            if (!this.allowBreak || this.isPossiblyProtected(x, y, z)) {
                return 1000000.0;
            }
            IBlockState sch = this.getSchematic(x, y, z, current);
            if (sch != null && !((List)Baritone.settings().buildSkipBlocks.value).contains(sch.getBlock())) {
                if (sch.getBlock() == Blocks.AIR) {
                    return 1.0;
                }
                if (BuilderProcess.this.valid(this.bsi.get0(x, y, z), sch, false)) {
                    return (Double)Baritone.settings().breakCorrectBlockPenaltyMultiplier.value;
                }
                return 1.0;
            }
            return 1.0;
        }
    }

    public static class GoalPlace
    extends GoalBlock {
        public GoalPlace(BlockPos placeAt) {
            super(placeAt.up());
        }

        @Override
        public double heuristic(int x, int y, int z) {
            return (double)(this.y * 100) + super.heuristic(x, y, z);
        }
    }

    public static class GoalAdjacent
    extends GoalGetToBlock {
        private boolean allowSameLevel;
        private BlockPos no;

        public GoalAdjacent(BlockPos pos, BlockPos no, boolean allowSameLevel) {
            super(pos);
            this.no = no;
            this.allowSameLevel = allowSameLevel;
        }

        @Override
        public boolean isInGoal(int x, int y, int z) {
            if (x == this.x && y == this.y && z == this.z) {
                return false;
            }
            if (x == this.no.getX() && y == this.no.getY() && z == this.no.getZ()) {
                return false;
            }
            if (!this.allowSameLevel && y == this.y - 1) {
                return false;
            }
            if (y < this.y - 1) {
                return false;
            }
            return super.isInGoal(x, y, z);
        }

        @Override
        public double heuristic(int x, int y, int z) {
            return (double)(this.y * 100) + super.heuristic(x, y, z);
        }
    }

    public static class GoalBreak
    extends GoalGetToBlock {
        public GoalBreak(BlockPos pos) {
            super(pos);
        }

        @Override
        public boolean isInGoal(int x, int y, int z) {
            if (y > this.y) {
                return false;
            }
            return super.isInGoal(x, y, z);
        }
    }

    public static class JankyGoalComposite
    implements Goal {
        private final Goal primary;
        private final Goal fallback;

        public JankyGoalComposite(Goal primary, Goal fallback) {
            this.primary = primary;
            this.fallback = fallback;
        }

        @Override
        public boolean isInGoal(int x, int y, int z) {
            return this.primary.isInGoal(x, y, z) || this.fallback.isInGoal(x, y, z);
        }

        @Override
        public double heuristic(int x, int y, int z) {
            return this.primary.heuristic(x, y, z);
        }

        public String toString() {
            return "JankyComposite Primary: " + this.primary + " Fallback: " + this.fallback;
        }
    }

    public static class Placement {
        private final int hotbarSelection;
        private final BlockPos placeAgainst;
        private final EnumFacing side;
        private final Rotation rot;

        public Placement(int hotbarSelection, BlockPos placeAgainst, EnumFacing side, Rotation rot) {
            this.hotbarSelection = hotbarSelection;
            this.placeAgainst = placeAgainst;
            this.side = side;
            this.rot = rot;
        }
    }
}

