// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure.template;

import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityList;
import java.util.UUID;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.init.Blocks;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3i;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public class Template
{
    private final List<BlockInfo> blocks;
    private final List<EntityInfo> entities;
    private BlockPos size;
    private String author;
    
    public Template() {
        this.blocks = (List<BlockInfo>)Lists.newArrayList();
        this.entities = (List<EntityInfo>)Lists.newArrayList();
        this.size = BlockPos.ORIGIN;
        this.author = "?";
    }
    
    public BlockPos getSize() {
        return this.size;
    }
    
    public void setAuthor(final String authorIn) {
        this.author = authorIn;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public void takeBlocksFromWorld(final World worldIn, final BlockPos startPos, final BlockPos size, final boolean takeEntities, @Nullable final Block toIgnore) {
        if (size.getX() >= 1 && size.getY() >= 1 && size.getZ() >= 1) {
            final BlockPos blockpos = startPos.add(size).add(-1, -1, -1);
            final List<BlockInfo> list = (List<BlockInfo>)Lists.newArrayList();
            final List<BlockInfo> list2 = (List<BlockInfo>)Lists.newArrayList();
            final List<BlockInfo> list3 = (List<BlockInfo>)Lists.newArrayList();
            final BlockPos blockpos2 = new BlockPos(Math.min(startPos.getX(), blockpos.getX()), Math.min(startPos.getY(), blockpos.getY()), Math.min(startPos.getZ(), blockpos.getZ()));
            final BlockPos blockpos3 = new BlockPos(Math.max(startPos.getX(), blockpos.getX()), Math.max(startPos.getY(), blockpos.getY()), Math.max(startPos.getZ(), blockpos.getZ()));
            this.size = size;
            for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos2, blockpos3)) {
                final BlockPos blockpos4 = blockpos$mutableblockpos.subtract(blockpos2);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
                if (toIgnore == null || toIgnore != iblockstate.getBlock()) {
                    final TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);
                    if (tileentity != null) {
                        final NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                        nbttagcompound.removeTag("x");
                        nbttagcompound.removeTag("y");
                        nbttagcompound.removeTag("z");
                        list2.add(new BlockInfo(blockpos4, iblockstate, nbttagcompound));
                    }
                    else if (!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
                        list3.add(new BlockInfo(blockpos4, iblockstate, null));
                    }
                    else {
                        list.add(new BlockInfo(blockpos4, iblockstate, null));
                    }
                }
            }
            this.blocks.clear();
            this.blocks.addAll(list);
            this.blocks.addAll(list2);
            this.blocks.addAll(list3);
            if (takeEntities) {
                this.takeEntitiesFromWorld(worldIn, blockpos2, blockpos3.add(1, 1, 1));
            }
            else {
                this.entities.clear();
            }
        }
    }
    
    private void takeEntitiesFromWorld(final World worldIn, final BlockPos startPos, final BlockPos endPos) {
        final List<Entity> list = worldIn.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, new AxisAlignedBB(startPos, endPos), (com.google.common.base.Predicate<? super Entity>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return !(p_apply_1_ instanceof EntityPlayer);
            }
        });
        this.entities.clear();
        for (final Entity entity : list) {
            final Vec3d vec3d = new Vec3d(entity.posX - startPos.getX(), entity.posY - startPos.getY(), entity.posZ - startPos.getZ());
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            entity.writeToNBTOptional(nbttagcompound);
            BlockPos blockpos;
            if (entity instanceof EntityPainting) {
                blockpos = ((EntityPainting)entity).getHangingPosition().subtract(startPos);
            }
            else {
                blockpos = new BlockPos(vec3d);
            }
            this.entities.add(new EntityInfo(vec3d, blockpos, nbttagcompound));
        }
    }
    
    public Map<BlockPos, String> getDataBlocks(final BlockPos pos, final PlacementSettings placementIn) {
        final Map<BlockPos, String> map = (Map<BlockPos, String>)Maps.newHashMap();
        final StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();
        for (final BlockInfo template$blockinfo : this.blocks) {
            final BlockPos blockpos = transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);
            if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos)) {
                final IBlockState iblockstate = template$blockinfo.blockState;
                if (iblockstate.getBlock() != Blocks.STRUCTURE_BLOCK || template$blockinfo.tileentityData == null) {
                    continue;
                }
                final TileEntityStructure.Mode tileentitystructure$mode = TileEntityStructure.Mode.valueOf(template$blockinfo.tileentityData.getString("mode"));
                if (tileentitystructure$mode != TileEntityStructure.Mode.DATA) {
                    continue;
                }
                map.put(blockpos, template$blockinfo.tileentityData.getString("metadata"));
            }
        }
        return map;
    }
    
    public BlockPos calculateConnectedPos(final PlacementSettings placementIn, final BlockPos p_186262_2_, final PlacementSettings p_186262_3_, final BlockPos p_186262_4_) {
        final BlockPos blockpos = transformedBlockPos(placementIn, p_186262_2_);
        final BlockPos blockpos2 = transformedBlockPos(p_186262_3_, p_186262_4_);
        return blockpos.subtract(blockpos2);
    }
    
    public static BlockPos transformedBlockPos(final PlacementSettings placementIn, final BlockPos pos) {
        return transformedBlockPos(pos, placementIn.getMirror(), placementIn.getRotation());
    }
    
    public void addBlocksToWorldChunk(final World worldIn, final BlockPos pos, final PlacementSettings placementIn) {
        placementIn.setBoundingBoxFromChunk();
        this.addBlocksToWorld(worldIn, pos, placementIn);
    }
    
    public void addBlocksToWorld(final World worldIn, final BlockPos pos, final PlacementSettings placementIn) {
        this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, 2);
    }
    
    public void addBlocksToWorld(final World worldIn, final BlockPos pos, final PlacementSettings placementIn, final int flags) {
        this.addBlocksToWorld(worldIn, pos, new BlockRotationProcessor(pos, placementIn), placementIn, flags);
    }
    
    public void addBlocksToWorld(final World worldIn, final BlockPos pos, @Nullable final ITemplateProcessor templateProcessor, final PlacementSettings placementIn, final int flags) {
        if ((!this.blocks.isEmpty() || (!placementIn.getIgnoreEntities() && !this.entities.isEmpty())) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1) {
            final Block block = placementIn.getReplacedBlock();
            final StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();
            for (final BlockInfo template$blockinfo : this.blocks) {
                final BlockPos blockpos = transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);
                final BlockInfo template$blockinfo2 = (templateProcessor != null) ? templateProcessor.processBlock(worldIn, blockpos, template$blockinfo) : template$blockinfo;
                if (template$blockinfo2 != null) {
                    final Block block2 = template$blockinfo2.blockState.getBlock();
                    if ((block != null && block == block2) || (placementIn.getIgnoreStructureBlock() && block2 == Blocks.STRUCTURE_BLOCK) || (structureboundingbox != null && !structureboundingbox.isVecInside(blockpos))) {
                        continue;
                    }
                    final IBlockState iblockstate = template$blockinfo2.blockState.withMirror(placementIn.getMirror());
                    final IBlockState iblockstate2 = iblockstate.withRotation(placementIn.getRotation());
                    if (template$blockinfo2.tileentityData != null) {
                        final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                        if (tileentity != null) {
                            if (tileentity instanceof IInventory) {
                                ((IInventory)tileentity).clear();
                            }
                            worldIn.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 4);
                        }
                    }
                    if (!worldIn.setBlockState(blockpos, iblockstate2, flags) || template$blockinfo2.tileentityData == null) {
                        continue;
                    }
                    final TileEntity tileentity2 = worldIn.getTileEntity(blockpos);
                    if (tileentity2 == null) {
                        continue;
                    }
                    template$blockinfo2.tileentityData.setInteger("x", blockpos.getX());
                    template$blockinfo2.tileentityData.setInteger("y", blockpos.getY());
                    template$blockinfo2.tileentityData.setInteger("z", blockpos.getZ());
                    tileentity2.readFromNBT(template$blockinfo2.tileentityData);
                    tileentity2.mirror(placementIn.getMirror());
                    tileentity2.rotate(placementIn.getRotation());
                }
            }
            for (final BlockInfo template$blockinfo3 : this.blocks) {
                if (block == null || block != template$blockinfo3.blockState.getBlock()) {
                    final BlockPos blockpos2 = transformedBlockPos(placementIn, template$blockinfo3.pos).add(pos);
                    if (structureboundingbox != null && !structureboundingbox.isVecInside(blockpos2)) {
                        continue;
                    }
                    worldIn.notifyNeighborsRespectDebug(blockpos2, template$blockinfo3.blockState.getBlock(), false);
                    if (template$blockinfo3.tileentityData == null) {
                        continue;
                    }
                    final TileEntity tileentity3 = worldIn.getTileEntity(blockpos2);
                    if (tileentity3 == null) {
                        continue;
                    }
                    tileentity3.markDirty();
                }
            }
            if (!placementIn.getIgnoreEntities()) {
                this.addEntitiesToWorld(worldIn, pos, placementIn.getMirror(), placementIn.getRotation(), structureboundingbox);
            }
        }
    }
    
    private void addEntitiesToWorld(final World worldIn, final BlockPos pos, final Mirror mirrorIn, final Rotation rotationIn, @Nullable final StructureBoundingBox aabb) {
        for (final EntityInfo template$entityinfo : this.entities) {
            final BlockPos blockpos = transformedBlockPos(template$entityinfo.blockPos, mirrorIn, rotationIn).add(pos);
            if (aabb == null || aabb.isVecInside(blockpos)) {
                final NBTTagCompound nbttagcompound = template$entityinfo.entityData;
                final Vec3d vec3d = transformedVec3d(template$entityinfo.pos, mirrorIn, rotationIn);
                final Vec3d vec3d2 = vec3d.add(pos.getX(), pos.getY(), pos.getZ());
                final NBTTagList nbttaglist = new NBTTagList();
                nbttaglist.appendTag(new NBTTagDouble(vec3d2.x));
                nbttaglist.appendTag(new NBTTagDouble(vec3d2.y));
                nbttaglist.appendTag(new NBTTagDouble(vec3d2.z));
                nbttagcompound.setTag("Pos", nbttaglist);
                nbttagcompound.setUniqueId("UUID", UUID.randomUUID());
                Entity entity;
                try {
                    entity = EntityList.createEntityFromNBT(nbttagcompound, worldIn);
                }
                catch (Exception var15) {
                    entity = null;
                }
                if (entity == null) {
                    continue;
                }
                float f = entity.getMirroredYaw(mirrorIn);
                f += entity.rotationYaw - entity.getRotatedYaw(rotationIn);
                entity.setLocationAndAngles(vec3d2.x, vec3d2.y, vec3d2.z, f, entity.rotationPitch);
                worldIn.spawnEntity(entity);
            }
        }
    }
    
    public BlockPos transformedSize(final Rotation rotationIn) {
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());
            }
            default: {
                return this.size;
            }
        }
    }
    
    private static BlockPos transformedBlockPos(final BlockPos pos, final Mirror mirrorIn, final Rotation rotationIn) {
        int i = pos.getX();
        final int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                k = -k;
                break;
            }
            case FRONT_BACK: {
                i = -i;
                break;
            }
            default: {
                flag = false;
                break;
            }
        }
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90: {
                return new BlockPos(k, j, -i);
            }
            case CLOCKWISE_90: {
                return new BlockPos(-k, j, i);
            }
            case CLOCKWISE_180: {
                return new BlockPos(-i, j, -k);
            }
            default: {
                return flag ? new BlockPos(i, j, k) : pos;
            }
        }
    }
    
    private static Vec3d transformedVec3d(final Vec3d vec, final Mirror mirrorIn, final Rotation rotationIn) {
        double d0 = vec.x;
        final double d2 = vec.y;
        double d3 = vec.z;
        boolean flag = true;
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                d3 = 1.0 - d3;
                break;
            }
            case FRONT_BACK: {
                d0 = 1.0 - d0;
                break;
            }
            default: {
                flag = false;
                break;
            }
        }
        switch (rotationIn) {
            case COUNTERCLOCKWISE_90: {
                return new Vec3d(d3, d2, 1.0 - d0);
            }
            case CLOCKWISE_90: {
                return new Vec3d(1.0 - d3, d2, d0);
            }
            case CLOCKWISE_180: {
                return new Vec3d(1.0 - d0, d2, 1.0 - d3);
            }
            default: {
                return flag ? new Vec3d(d0, d2, d3) : vec;
            }
        }
    }
    
    public BlockPos getZeroPositionWithTransform(final BlockPos p_189961_1_, final Mirror p_189961_2_, final Rotation p_189961_3_) {
        return getZeroPositionWithTransform(p_189961_1_, p_189961_2_, p_189961_3_, this.getSize().getX(), this.getSize().getZ());
    }
    
    public static BlockPos getZeroPositionWithTransform(final BlockPos p_191157_0_, final Mirror p_191157_1_, final Rotation p_191157_2_, int p_191157_3_, int p_191157_4_) {
        --p_191157_3_;
        --p_191157_4_;
        final int i = (p_191157_1_ == Mirror.FRONT_BACK) ? p_191157_3_ : 0;
        final int j = (p_191157_1_ == Mirror.LEFT_RIGHT) ? p_191157_4_ : 0;
        BlockPos blockpos = p_191157_0_;
        switch (p_191157_2_) {
            case COUNTERCLOCKWISE_90: {
                blockpos = p_191157_0_.add(j, 0, p_191157_3_ - i);
                break;
            }
            case CLOCKWISE_90: {
                blockpos = p_191157_0_.add(p_191157_4_ - j, 0, i);
                break;
            }
            case CLOCKWISE_180: {
                blockpos = p_191157_0_.add(p_191157_3_ - i, 0, p_191157_4_ - j);
                break;
            }
            case NONE: {
                blockpos = p_191157_0_.add(i, 0, j);
                break;
            }
        }
        return blockpos;
    }
    
    public static void registerFixes(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.STRUCTURE, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (compound.hasKey("entities", 9)) {
                    final NBTTagList nbttaglist = compound.getTagList("entities", 10);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.get(i);
                        if (nbttagcompound.hasKey("nbt", 10)) {
                            nbttagcompound.setTag("nbt", fixer.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("nbt"), versionIn));
                        }
                    }
                }
                if (compound.hasKey("blocks", 9)) {
                    final NBTTagList nbttaglist2 = compound.getTagList("blocks", 10);
                    for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
                        final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttaglist2.get(j);
                        if (nbttagcompound2.hasKey("nbt", 10)) {
                            nbttagcompound2.setTag("nbt", fixer.process(FixTypes.BLOCK_ENTITY, nbttagcompound2.getCompoundTag("nbt"), versionIn));
                        }
                    }
                }
                return compound;
            }
        });
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        final BasicPalette template$basicpalette = new BasicPalette();
        final NBTTagList nbttaglist = new NBTTagList();
        for (final BlockInfo template$blockinfo : this.blocks) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("pos", this.writeInts(template$blockinfo.pos.getX(), template$blockinfo.pos.getY(), template$blockinfo.pos.getZ()));
            nbttagcompound.setInteger("state", template$basicpalette.idFor(template$blockinfo.blockState));
            if (template$blockinfo.tileentityData != null) {
                nbttagcompound.setTag("nbt", template$blockinfo.tileentityData);
            }
            nbttaglist.appendTag(nbttagcompound);
        }
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final EntityInfo template$entityinfo : this.entities) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setTag("pos", this.writeDoubles(template$entityinfo.pos.x, template$entityinfo.pos.y, template$entityinfo.pos.z));
            nbttagcompound2.setTag("blockPos", this.writeInts(template$entityinfo.blockPos.getX(), template$entityinfo.blockPos.getY(), template$entityinfo.blockPos.getZ()));
            if (template$entityinfo.entityData != null) {
                nbttagcompound2.setTag("nbt", template$entityinfo.entityData);
            }
            nbttaglist2.appendTag(nbttagcompound2);
        }
        final NBTTagList nbttaglist3 = new NBTTagList();
        for (final IBlockState iblockstate : template$basicpalette) {
            nbttaglist3.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), iblockstate));
        }
        nbt.setTag("palette", nbttaglist3);
        nbt.setTag("blocks", nbttaglist);
        nbt.setTag("entities", nbttaglist2);
        nbt.setTag("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
        nbt.setString("author", this.author);
        nbt.setInteger("DataVersion", 1343);
        return nbt;
    }
    
    public void read(final NBTTagCompound compound) {
        this.blocks.clear();
        this.entities.clear();
        final NBTTagList nbttaglist = compound.getTagList("size", 3);
        this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
        this.author = compound.getString("author");
        final BasicPalette template$basicpalette = new BasicPalette();
        final NBTTagList nbttaglist2 = compound.getTagList("palette", 10);
        for (int i = 0; i < nbttaglist2.tagCount(); ++i) {
            template$basicpalette.addMapping(NBTUtil.readBlockState(nbttaglist2.getCompoundTagAt(i)), i);
        }
        final NBTTagList nbttaglist3 = compound.getTagList("blocks", 10);
        for (int j = 0; j < nbttaglist3.tagCount(); ++j) {
            final NBTTagCompound nbttagcompound = nbttaglist3.getCompoundTagAt(j);
            final NBTTagList nbttaglist4 = nbttagcompound.getTagList("pos", 3);
            final BlockPos blockpos = new BlockPos(nbttaglist4.getIntAt(0), nbttaglist4.getIntAt(1), nbttaglist4.getIntAt(2));
            final IBlockState iblockstate = template$basicpalette.stateFor(nbttagcompound.getInteger("state"));
            NBTTagCompound nbttagcompound2;
            if (nbttagcompound.hasKey("nbt")) {
                nbttagcompound2 = nbttagcompound.getCompoundTag("nbt");
            }
            else {
                nbttagcompound2 = null;
            }
            this.blocks.add(new BlockInfo(blockpos, iblockstate, nbttagcompound2));
        }
        final NBTTagList nbttaglist5 = compound.getTagList("entities", 10);
        for (int k = 0; k < nbttaglist5.tagCount(); ++k) {
            final NBTTagCompound nbttagcompound3 = nbttaglist5.getCompoundTagAt(k);
            final NBTTagList nbttaglist6 = nbttagcompound3.getTagList("pos", 6);
            final Vec3d vec3d = new Vec3d(nbttaglist6.getDoubleAt(0), nbttaglist6.getDoubleAt(1), nbttaglist6.getDoubleAt(2));
            final NBTTagList nbttaglist7 = nbttagcompound3.getTagList("blockPos", 3);
            final BlockPos blockpos2 = new BlockPos(nbttaglist7.getIntAt(0), nbttaglist7.getIntAt(1), nbttaglist7.getIntAt(2));
            if (nbttagcompound3.hasKey("nbt")) {
                final NBTTagCompound nbttagcompound4 = nbttagcompound3.getCompoundTag("nbt");
                this.entities.add(new EntityInfo(vec3d, blockpos2, nbttagcompound4));
            }
        }
    }
    
    private NBTTagList writeInts(final int... values) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final int i : values) {
            nbttaglist.appendTag(new NBTTagInt(i));
        }
        return nbttaglist;
    }
    
    private NBTTagList writeDoubles(final double... values) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final double d0 : values) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }
    
    static class BasicPalette implements Iterable<IBlockState>
    {
        public static final IBlockState DEFAULT_BLOCK_STATE;
        final ObjectIntIdentityMap<IBlockState> ids;
        private int lastId;
        
        private BasicPalette() {
            this.ids = new ObjectIntIdentityMap<IBlockState>(16);
        }
        
        public int idFor(final IBlockState state) {
            int i = this.ids.get(state);
            if (i == -1) {
                i = this.lastId++;
                this.ids.put(state, i);
            }
            return i;
        }
        
        @Nullable
        public IBlockState stateFor(final int id) {
            final IBlockState iblockstate = this.ids.getByValue(id);
            return (iblockstate == null) ? BasicPalette.DEFAULT_BLOCK_STATE : iblockstate;
        }
        
        @Override
        public Iterator<IBlockState> iterator() {
            return this.ids.iterator();
        }
        
        public void addMapping(final IBlockState p_189956_1_, final int p_189956_2_) {
            this.ids.put(p_189956_1_, p_189956_2_);
        }
        
        static {
            DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
        }
    }
    
    public static class BlockInfo
    {
        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound tileentityData;
        
        public BlockInfo(final BlockPos posIn, final IBlockState stateIn, @Nullable final NBTTagCompound compoundIn) {
            this.pos = posIn;
            this.blockState = stateIn;
            this.tileentityData = compoundIn;
        }
    }
    
    public static class EntityInfo
    {
        public final Vec3d pos;
        public final BlockPos blockPos;
        public final NBTTagCompound entityData;
        
        public EntityInfo(final Vec3d vecIn, final BlockPos posIn, final NBTTagCompound compoundIn) {
            this.pos = vecIn;
            this.blockPos = posIn;
            this.entityData = compoundIn;
        }
    }
}
