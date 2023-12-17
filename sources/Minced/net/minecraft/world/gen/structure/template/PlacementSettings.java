// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure.template;

import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.util.math.ChunkPos;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;

public class PlacementSettings
{
    private Mirror mirror;
    private Rotation rotation;
    private boolean ignoreEntities;
    @Nullable
    private Block replacedBlock;
    @Nullable
    private ChunkPos chunk;
    @Nullable
    private StructureBoundingBox boundingBox;
    private boolean ignoreStructureBlock;
    private float integrity;
    @Nullable
    private Random random;
    @Nullable
    private Long setSeed;
    
    public PlacementSettings() {
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreStructureBlock = true;
        this.integrity = 1.0f;
    }
    
    public PlacementSettings copy() {
        final PlacementSettings placementsettings = new PlacementSettings();
        placementsettings.mirror = this.mirror;
        placementsettings.rotation = this.rotation;
        placementsettings.ignoreEntities = this.ignoreEntities;
        placementsettings.replacedBlock = this.replacedBlock;
        placementsettings.chunk = this.chunk;
        placementsettings.boundingBox = this.boundingBox;
        placementsettings.ignoreStructureBlock = this.ignoreStructureBlock;
        placementsettings.integrity = this.integrity;
        placementsettings.random = this.random;
        placementsettings.setSeed = this.setSeed;
        return placementsettings;
    }
    
    public PlacementSettings setMirror(final Mirror mirrorIn) {
        this.mirror = mirrorIn;
        return this;
    }
    
    public PlacementSettings setRotation(final Rotation rotationIn) {
        this.rotation = rotationIn;
        return this;
    }
    
    public PlacementSettings setIgnoreEntities(final boolean ignoreEntitiesIn) {
        this.ignoreEntities = ignoreEntitiesIn;
        return this;
    }
    
    public PlacementSettings setReplacedBlock(final Block replacedBlockIn) {
        this.replacedBlock = replacedBlockIn;
        return this;
    }
    
    public PlacementSettings setChunk(final ChunkPos chunkPosIn) {
        this.chunk = chunkPosIn;
        return this;
    }
    
    public PlacementSettings setBoundingBox(final StructureBoundingBox boundingBoxIn) {
        this.boundingBox = boundingBoxIn;
        return this;
    }
    
    public PlacementSettings setSeed(@Nullable final Long seedIn) {
        this.setSeed = seedIn;
        return this;
    }
    
    public PlacementSettings setRandom(@Nullable final Random randomIn) {
        this.random = randomIn;
        return this;
    }
    
    public PlacementSettings setIntegrity(final float integrityIn) {
        this.integrity = integrityIn;
        return this;
    }
    
    public Mirror getMirror() {
        return this.mirror;
    }
    
    public PlacementSettings setIgnoreStructureBlock(final boolean ignoreStructureBlockIn) {
        this.ignoreStructureBlock = ignoreStructureBlockIn;
        return this;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public Random getRandom(@Nullable final BlockPos seed) {
        if (this.random != null) {
            return this.random;
        }
        if (this.setSeed != null) {
            return (this.setSeed == 0L) ? new Random(System.currentTimeMillis()) : new Random(this.setSeed);
        }
        if (seed == null) {
            return new Random(System.currentTimeMillis());
        }
        final int i = seed.getX();
        final int j = seed.getZ();
        return new Random(i * i * 4987142 + i * 5947611 + j * j * 4392871L + j * 389711 ^ 0x3AD8025FL);
    }
    
    public float getIntegrity() {
        return this.integrity;
    }
    
    public boolean getIgnoreEntities() {
        return this.ignoreEntities;
    }
    
    @Nullable
    public Block getReplacedBlock() {
        return this.replacedBlock;
    }
    
    @Nullable
    public StructureBoundingBox getBoundingBox() {
        if (this.boundingBox == null && this.chunk != null) {
            this.setBoundingBoxFromChunk();
        }
        return this.boundingBox;
    }
    
    public boolean getIgnoreStructureBlock() {
        return this.ignoreStructureBlock;
    }
    
    void setBoundingBoxFromChunk() {
        this.boundingBox = this.getBoundingBoxFromChunk(this.chunk);
    }
    
    @Nullable
    private StructureBoundingBox getBoundingBoxFromChunk(@Nullable final ChunkPos pos) {
        if (pos == null) {
            return null;
        }
        final int i = pos.x * 16;
        final int j = pos.z * 16;
        return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
    }
}
