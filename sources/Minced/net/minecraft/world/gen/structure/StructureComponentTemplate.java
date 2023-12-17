// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public abstract class StructureComponentTemplate extends StructureComponent
{
    private static final PlacementSettings DEFAULT_PLACE_SETTINGS;
    protected Template template;
    protected PlacementSettings placeSettings;
    protected BlockPos templatePosition;
    
    public StructureComponentTemplate() {
        this.placeSettings = StructureComponentTemplate.DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }
    
    public StructureComponentTemplate(final int type) {
        super(type);
        this.placeSettings = StructureComponentTemplate.DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }
    
    protected void setup(final Template templateIn, final BlockPos pos, final PlacementSettings settings) {
        this.template = templateIn;
        this.setCoordBaseMode(EnumFacing.NORTH);
        this.templatePosition = pos;
        this.placeSettings = settings;
        this.setBoundingBoxFromTemplate();
    }
    
    @Override
    protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setInteger("TPX", this.templatePosition.getX());
        tagCompound.setInteger("TPY", this.templatePosition.getY());
        tagCompound.setInteger("TPZ", this.templatePosition.getZ());
    }
    
    @Override
    protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
        this.templatePosition = new BlockPos(tagCompound.getInteger("TPX"), tagCompound.getInteger("TPY"), tagCompound.getInteger("TPZ"));
    }
    
    @Override
    public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
        this.placeSettings.setBoundingBox(structureBoundingBoxIn);
        this.template.addBlocksToWorld(worldIn, this.templatePosition, this.placeSettings, 18);
        final Map<BlockPos, String> map = this.template.getDataBlocks(this.templatePosition, this.placeSettings);
        for (final Map.Entry<BlockPos, String> entry : map.entrySet()) {
            final String s = entry.getValue();
            this.handleDataMarker(s, entry.getKey(), worldIn, randomIn, structureBoundingBoxIn);
        }
        return true;
    }
    
    protected abstract void handleDataMarker(final String p0, final BlockPos p1, final World p2, final Random p3, final StructureBoundingBox p4);
    
    private void setBoundingBoxFromTemplate() {
        final Rotation rotation = this.placeSettings.getRotation();
        final BlockPos blockpos = this.template.transformedSize(rotation);
        final Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new StructureBoundingBox(0, 0, 0, blockpos.getX(), blockpos.getY() - 1, blockpos.getZ());
        switch (rotation) {
            case CLOCKWISE_90: {
                this.boundingBox.offset(-blockpos.getX(), 0, 0);
                break;
            }
            case COUNTERCLOCKWISE_90: {
                this.boundingBox.offset(0, 0, -blockpos.getZ());
                break;
            }
            case CLOCKWISE_180: {
                this.boundingBox.offset(-blockpos.getX(), 0, -blockpos.getZ());
                break;
            }
        }
        switch (mirror) {
            case FRONT_BACK: {
                BlockPos blockpos2 = BlockPos.ORIGIN;
                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos2 = blockpos2.offset(EnumFacing.EAST, blockpos.getX());
                    }
                    else {
                        blockpos2 = blockpos2.offset(EnumFacing.WEST, blockpos.getX());
                    }
                }
                else {
                    blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), blockpos.getZ());
                }
                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            }
            case LEFT_RIGHT: {
                BlockPos blockpos3 = BlockPos.ORIGIN;
                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos3 = blockpos3.offset(EnumFacing.SOUTH, blockpos.getZ());
                    }
                    else {
                        blockpos3 = blockpos3.offset(EnumFacing.NORTH, blockpos.getZ());
                    }
                }
                else {
                    blockpos3 = blockpos3.offset(rotation.rotate(EnumFacing.NORTH), blockpos.getX());
                }
                this.boundingBox.offset(blockpos3.getX(), 0, blockpos3.getZ());
                break;
            }
        }
        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }
    
    @Override
    public void offset(final int x, final int y, final int z) {
        super.offset(x, y, z);
        this.templatePosition = this.templatePosition.add(x, y, z);
    }
    
    static {
        DEFAULT_PLACE_SETTINGS = new PlacementSettings();
    }
}
