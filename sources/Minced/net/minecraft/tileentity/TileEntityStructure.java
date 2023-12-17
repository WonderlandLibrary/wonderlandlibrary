// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3i;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import java.util.List;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStructure;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;

public class TileEntityStructure extends TileEntity
{
    private String name;
    private String author;
    private String metadata;
    private BlockPos position;
    private BlockPos size;
    private Mirror mirror;
    private Rotation rotation;
    private Mode mode;
    private boolean ignoreEntities;
    private boolean powered;
    private boolean showAir;
    private boolean showBoundingBox;
    private float integrity;
    private long seed;
    
    public TileEntityStructure() {
        this.name = "";
        this.author = "";
        this.metadata = "";
        this.position = new BlockPos(0, 1, 0);
        this.size = BlockPos.ORIGIN;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.mode = Mode.DATA;
        this.ignoreEntities = true;
        this.showBoundingBox = true;
        this.integrity = 1.0f;
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("name", this.name);
        compound.setString("author", this.author);
        compound.setString("metadata", this.metadata);
        compound.setInteger("posX", this.position.getX());
        compound.setInteger("posY", this.position.getY());
        compound.setInteger("posZ", this.position.getZ());
        compound.setInteger("sizeX", this.size.getX());
        compound.setInteger("sizeY", this.size.getY());
        compound.setInteger("sizeZ", this.size.getZ());
        compound.setString("rotation", this.rotation.toString());
        compound.setString("mirror", this.mirror.toString());
        compound.setString("mode", this.mode.toString());
        compound.setBoolean("ignoreEntities", this.ignoreEntities);
        compound.setBoolean("powered", this.powered);
        compound.setBoolean("showair", this.showAir);
        compound.setBoolean("showboundingbox", this.showBoundingBox);
        compound.setFloat("integrity", this.integrity);
        compound.setLong("seed", this.seed);
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setName(compound.getString("name"));
        this.author = compound.getString("author");
        this.metadata = compound.getString("metadata");
        final int i = MathHelper.clamp(compound.getInteger("posX"), -32, 32);
        final int j = MathHelper.clamp(compound.getInteger("posY"), -32, 32);
        final int k = MathHelper.clamp(compound.getInteger("posZ"), -32, 32);
        this.position = new BlockPos(i, j, k);
        final int l = MathHelper.clamp(compound.getInteger("sizeX"), 0, 32);
        final int i2 = MathHelper.clamp(compound.getInteger("sizeY"), 0, 32);
        final int j2 = MathHelper.clamp(compound.getInteger("sizeZ"), 0, 32);
        this.size = new BlockPos(l, i2, j2);
        try {
            this.rotation = Rotation.valueOf(compound.getString("rotation"));
        }
        catch (IllegalArgumentException var11) {
            this.rotation = Rotation.NONE;
        }
        try {
            this.mirror = Mirror.valueOf(compound.getString("mirror"));
        }
        catch (IllegalArgumentException var12) {
            this.mirror = Mirror.NONE;
        }
        try {
            this.mode = Mode.valueOf(compound.getString("mode"));
        }
        catch (IllegalArgumentException var13) {
            this.mode = Mode.DATA;
        }
        this.ignoreEntities = compound.getBoolean("ignoreEntities");
        this.powered = compound.getBoolean("powered");
        this.showAir = compound.getBoolean("showair");
        this.showBoundingBox = compound.getBoolean("showboundingbox");
        if (compound.hasKey("integrity")) {
            this.integrity = compound.getFloat("integrity");
        }
        else {
            this.integrity = 1.0f;
        }
        this.seed = compound.getLong("seed");
        this.updateBlockState();
    }
    
    private void updateBlockState() {
        if (this.world != null) {
            final BlockPos blockpos = this.getPos();
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
                this.world.setBlockState(blockpos, iblockstate.withProperty(BlockStructure.MODE, this.mode), 2);
            }
        }
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 7, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public boolean usedBy(final EntityPlayer player) {
        if (!player.canUseCommandBlock()) {
            return false;
        }
        if (player.getEntityWorld().isRemote) {
            player.openEditStructure(this);
        }
        return true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String nameIn) {
        String s = nameIn;
        for (final char c0 : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS) {
            s = s.replace(c0, '_');
        }
        this.name = s;
    }
    
    public void createdBy(final EntityLivingBase p_189720_1_) {
        if (!StringUtils.isNullOrEmpty(p_189720_1_.getName())) {
            this.author = p_189720_1_.getName();
        }
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setPosition(final BlockPos posIn) {
        this.position = posIn;
    }
    
    public BlockPos getStructureSize() {
        return this.size;
    }
    
    public void setSize(final BlockPos sizeIn) {
        this.size = sizeIn;
    }
    
    public Mirror getMirror() {
        return this.mirror;
    }
    
    public void setMirror(final Mirror mirrorIn) {
        this.mirror = mirrorIn;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final Rotation rotationIn) {
        this.rotation = rotationIn;
    }
    
    public String getMetadata() {
        return this.metadata;
    }
    
    public void setMetadata(final String metadataIn) {
        this.metadata = metadataIn;
    }
    
    public Mode getMode() {
        return this.mode;
    }
    
    public void setMode(final Mode modeIn) {
        this.mode = modeIn;
        final IBlockState iblockstate = this.world.getBlockState(this.getPos());
        if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
            this.world.setBlockState(this.getPos(), iblockstate.withProperty(BlockStructure.MODE, modeIn), 2);
        }
    }
    
    public void nextMode() {
        switch (this.getMode()) {
            case SAVE: {
                this.setMode(Mode.LOAD);
                break;
            }
            case LOAD: {
                this.setMode(Mode.CORNER);
                break;
            }
            case CORNER: {
                this.setMode(Mode.DATA);
                break;
            }
            case DATA: {
                this.setMode(Mode.SAVE);
                break;
            }
        }
    }
    
    public boolean ignoresEntities() {
        return this.ignoreEntities;
    }
    
    public void setIgnoresEntities(final boolean ignoreEntitiesIn) {
        this.ignoreEntities = ignoreEntitiesIn;
    }
    
    public float getIntegrity() {
        return this.integrity;
    }
    
    public void setIntegrity(final float integrityIn) {
        this.integrity = integrityIn;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public void setSeed(final long seedIn) {
        this.seed = seedIn;
    }
    
    public boolean detectSize() {
        if (this.mode != Mode.SAVE) {
            return false;
        }
        final BlockPos blockpos = this.getPos();
        final int i = 80;
        final BlockPos blockpos2 = new BlockPos(blockpos.getX() - 80, 0, blockpos.getZ() - 80);
        final BlockPos blockpos3 = new BlockPos(blockpos.getX() + 80, 255, blockpos.getZ() + 80);
        final List<TileEntityStructure> list = this.getNearbyCornerBlocks(blockpos2, blockpos3);
        final List<TileEntityStructure> list2 = this.filterRelatedCornerBlocks(list);
        if (list2.size() < 1) {
            return false;
        }
        final StructureBoundingBox structureboundingbox = this.calculateEnclosingBoundingBox(blockpos, list2);
        if (structureboundingbox.maxX - structureboundingbox.minX > 1 && structureboundingbox.maxY - structureboundingbox.minY > 1 && structureboundingbox.maxZ - structureboundingbox.minZ > 1) {
            this.position = new BlockPos(structureboundingbox.minX - blockpos.getX() + 1, structureboundingbox.minY - blockpos.getY() + 1, structureboundingbox.minZ - blockpos.getZ() + 1);
            this.size = new BlockPos(structureboundingbox.maxX - structureboundingbox.minX - 1, structureboundingbox.maxY - structureboundingbox.minY - 1, structureboundingbox.maxZ - structureboundingbox.minZ - 1);
            this.markDirty();
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
            return true;
        }
        return false;
    }
    
    private List<TileEntityStructure> filterRelatedCornerBlocks(final List<TileEntityStructure> p_184415_1_) {
        final Iterable<TileEntityStructure> iterable = (Iterable<TileEntityStructure>)Iterables.filter((Iterable)p_184415_1_, (Predicate)new Predicate<TileEntityStructure>() {
            public boolean apply(@Nullable final TileEntityStructure p_apply_1_) {
                return p_apply_1_.mode == Mode.CORNER && TileEntityStructure.this.name.equals(p_apply_1_.name);
            }
        });
        return (List<TileEntityStructure>)Lists.newArrayList((Iterable)iterable);
    }
    
    private List<TileEntityStructure> getNearbyCornerBlocks(final BlockPos p_184418_1_, final BlockPos p_184418_2_) {
        final List<TileEntityStructure> list = (List<TileEntityStructure>)Lists.newArrayList();
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_184418_1_, p_184418_2_)) {
            final IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos);
            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
                final TileEntity tileentity = this.world.getTileEntity(blockpos$mutableblockpos);
                if (tileentity == null || !(tileentity instanceof TileEntityStructure)) {
                    continue;
                }
                list.add((TileEntityStructure)tileentity);
            }
        }
        return list;
    }
    
    private StructureBoundingBox calculateEnclosingBoundingBox(final BlockPos p_184416_1_, final List<TileEntityStructure> p_184416_2_) {
        StructureBoundingBox structureboundingbox;
        if (p_184416_2_.size() > 1) {
            final BlockPos blockpos = p_184416_2_.get(0).getPos();
            structureboundingbox = new StructureBoundingBox(blockpos, blockpos);
        }
        else {
            structureboundingbox = new StructureBoundingBox(p_184416_1_, p_184416_1_);
        }
        for (final TileEntityStructure tileentitystructure : p_184416_2_) {
            final BlockPos blockpos2 = tileentitystructure.getPos();
            if (blockpos2.getX() < structureboundingbox.minX) {
                structureboundingbox.minX = blockpos2.getX();
            }
            else if (blockpos2.getX() > structureboundingbox.maxX) {
                structureboundingbox.maxX = blockpos2.getX();
            }
            if (blockpos2.getY() < structureboundingbox.minY) {
                structureboundingbox.minY = blockpos2.getY();
            }
            else if (blockpos2.getY() > structureboundingbox.maxY) {
                structureboundingbox.maxY = blockpos2.getY();
            }
            if (blockpos2.getZ() < structureboundingbox.minZ) {
                structureboundingbox.minZ = blockpos2.getZ();
            }
            else {
                if (blockpos2.getZ() <= structureboundingbox.maxZ) {
                    continue;
                }
                structureboundingbox.maxZ = blockpos2.getZ();
            }
        }
        return structureboundingbox;
    }
    
    public void writeCoordinates(final ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }
    
    public boolean save() {
        return this.save(true);
    }
    
    public boolean save(final boolean writeToDisk) {
        if (this.mode == Mode.SAVE && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
            final BlockPos blockpos = this.getPos().add(this.position);
            final WorldServer worldserver = (WorldServer)this.world;
            final MinecraftServer minecraftserver = this.world.getMinecraftServer();
            final TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            final Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(this.name));
            template.takeBlocksFromWorld(this.world, blockpos, this.size, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
            template.setAuthor(this.author);
            return !writeToDisk || templatemanager.writeTemplate(minecraftserver, new ResourceLocation(this.name));
        }
        return false;
    }
    
    public boolean load() {
        return this.load(true);
    }
    
    public boolean load(final boolean requireMatchingSize) {
        if (this.mode != Mode.LOAD || this.world.isRemote || StringUtils.isNullOrEmpty(this.name)) {
            return false;
        }
        final BlockPos blockpos = this.getPos();
        final BlockPos blockpos2 = blockpos.add(this.position);
        final WorldServer worldserver = (WorldServer)this.world;
        final MinecraftServer minecraftserver = this.world.getMinecraftServer();
        final TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        final Template template = templatemanager.get(minecraftserver, new ResourceLocation(this.name));
        if (template == null) {
            return false;
        }
        if (!StringUtils.isNullOrEmpty(template.getAuthor())) {
            this.author = template.getAuthor();
        }
        final BlockPos blockpos3 = template.getSize();
        final boolean flag = this.size.equals(blockpos3);
        if (!flag) {
            this.size = blockpos3;
            this.markDirty();
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        }
        if (requireMatchingSize && !flag) {
            return false;
        }
        final PlacementSettings placementsettings = new PlacementSettings().setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk(null).setReplacedBlock(null).setIgnoreStructureBlock(false);
        if (this.integrity < 1.0f) {
            placementsettings.setIntegrity(MathHelper.clamp(this.integrity, 0.0f, 1.0f)).setSeed(this.seed);
        }
        template.addBlocksToWorldChunk(this.world, blockpos2, placementsettings);
        return true;
    }
    
    public void unloadStructure() {
        final WorldServer worldserver = (WorldServer)this.world;
        final TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        templatemanager.remove(new ResourceLocation(this.name));
    }
    
    public boolean isStructureLoadable() {
        if (this.mode == Mode.LOAD && !this.world.isRemote) {
            final WorldServer worldserver = (WorldServer)this.world;
            final MinecraftServer minecraftserver = this.world.getMinecraftServer();
            final TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            return templatemanager.get(minecraftserver, new ResourceLocation(this.name)) != null;
        }
        return false;
    }
    
    public boolean isPowered() {
        return this.powered;
    }
    
    public void setPowered(final boolean poweredIn) {
        this.powered = poweredIn;
    }
    
    public boolean showsAir() {
        return this.showAir;
    }
    
    public void setShowAir(final boolean showAirIn) {
        this.showAir = showAirIn;
    }
    
    public boolean showsBoundingBox() {
        return this.showBoundingBox;
    }
    
    public void setShowBoundingBox(final boolean showBoundingBoxIn) {
        this.showBoundingBox = showBoundingBoxIn;
    }
    
    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("structure_block.hover." + this.mode.modeName, new Object[] { (this.mode == Mode.DATA) ? this.metadata : this.name });
    }
    
    public enum Mode implements IStringSerializable
    {
        SAVE("save", 0), 
        LOAD("load", 1), 
        CORNER("corner", 2), 
        DATA("data", 3);
        
        private static final Mode[] MODES;
        private final String modeName;
        private final int modeId;
        
        private Mode(final String modeNameIn, final int modeIdIn) {
            this.modeName = modeNameIn;
            this.modeId = modeIdIn;
        }
        
        @Override
        public String getName() {
            return this.modeName;
        }
        
        public int getModeId() {
            return this.modeId;
        }
        
        public static Mode getById(final int id) {
            return (id >= 0 && id < Mode.MODES.length) ? Mode.MODES[id] : Mode.MODES[0];
        }
        
        static {
            MODES = new Mode[values().length];
            for (final Mode tileentitystructure$mode : values()) {
                Mode.MODES[tileentitystructure$mode.getModeId()] = tileentitystructure$mode;
            }
        }
    }
}
