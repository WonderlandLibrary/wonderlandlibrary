/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate
extends StructureComponent {
    private static final PlacementSettings DEFAULT_PLACE_SETTINGS = new PlacementSettings();
    protected Template template;
    protected PlacementSettings placeSettings = DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    protected BlockPos templatePosition;

    public StructureComponentTemplate() {
    }

    public StructureComponentTemplate(int p_i46662_1_) {
        super(p_i46662_1_);
    }

    protected void setup(Template p_186173_1_, BlockPos p_186173_2_, PlacementSettings p_186173_3_) {
        this.template = p_186173_1_;
        this.setCoordBaseMode(EnumFacing.NORTH);
        this.templatePosition = p_186173_2_;
        this.placeSettings = p_186173_3_;
        this.setBoundingBoxFromTemplate();
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("TPX", this.templatePosition.getX());
        tagCompound.setInteger("TPY", this.templatePosition.getY());
        tagCompound.setInteger("TPZ", this.templatePosition.getZ());
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        this.templatePosition = new BlockPos(tagCompound.getInteger("TPX"), tagCompound.getInteger("TPY"), tagCompound.getInteger("TPZ"));
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.placeSettings.setBoundingBox(structureBoundingBoxIn);
        this.template.addBlocksToWorld(worldIn, this.templatePosition, this.placeSettings, 18);
        Map<BlockPos, String> map = this.template.getDataBlocks(this.templatePosition, this.placeSettings);
        for (Map.Entry<BlockPos, String> entry : map.entrySet()) {
            String s = entry.getValue();
            this.handleDataMarker(s, entry.getKey(), worldIn, randomIn, structureBoundingBoxIn);
        }
        return true;
    }

    protected abstract void handleDataMarker(String var1, BlockPos var2, World var3, Random var4, StructureBoundingBox var5);

    private void setBoundingBoxFromTemplate() {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos blockpos = this.template.transformedSize(rotation);
        Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new StructureBoundingBox(0, 0, 0, blockpos.getX(), blockpos.getY() - 1, blockpos.getZ());
        switch (rotation) {
            default: {
                break;
            }
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
            }
        }
        switch (mirror) {
            default: {
                break;
            }
            case FRONT_BACK: {
                BlockPos blockpos2 = BlockPos.ORIGIN;
                blockpos2 = rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90 ? (rotation == Rotation.CLOCKWISE_180 ? blockpos2.offset(EnumFacing.EAST, blockpos.getX()) : blockpos2.offset(EnumFacing.WEST, blockpos.getX())) : blockpos2.offset(rotation.rotate(EnumFacing.WEST), blockpos.getZ());
                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            }
            case LEFT_RIGHT: {
                BlockPos blockpos1 = BlockPos.ORIGIN;
                blockpos1 = rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90 ? (rotation == Rotation.CLOCKWISE_180 ? blockpos1.offset(EnumFacing.SOUTH, blockpos.getZ()) : blockpos1.offset(EnumFacing.NORTH, blockpos.getZ())) : blockpos1.offset(rotation.rotate(EnumFacing.NORTH), blockpos.getX());
                this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
            }
        }
        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }

    @Override
    public void offset(int x, int y, int z) {
        super.offset(x, y, z);
        this.templatePosition = this.templatePosition.add(x, y, z);
    }
}

