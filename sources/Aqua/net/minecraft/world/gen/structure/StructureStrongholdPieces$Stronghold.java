package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
static abstract class StructureStrongholdPieces.Stronghold
extends StructureComponent {
    protected Door field_143013_d = Door.OPENING;

    public StructureStrongholdPieces.Stronghold() {
    }

    protected StructureStrongholdPieces.Stronghold(int p_i2087_1_) {
        super(p_i2087_1_);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("EntryDoor", this.field_143013_d.name());
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        this.field_143013_d = Door.valueOf((String)tagCompound.getString("EntryDoor"));
    }

    protected void placeDoor(World worldIn, Random p_74990_2_, StructureBoundingBox p_74990_3_, Door p_74990_4_, int p_74990_5_, int p_74990_6_, int p_74990_7_) {
        switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door[p_74990_4_.ordinal()]) {
            default: {
                this.fillWithBlocks(worldIn, p_74990_3_, p_74990_5_, p_74990_6_, p_74990_7_, p_74990_5_ + 3 - 1, p_74990_6_ + 3 - 1, p_74990_7_, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                break;
            }
            case 2: {
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.oak_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.oak_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                break;
            }
            case 3: {
                this.setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                break;
            }
            case 4: {
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 4)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ + 1, p_74990_3_);
                this.setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 3)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ - 1, p_74990_3_);
            }
        }
    }

    protected Door getRandomDoor(Random p_74988_1_) {
        int i = p_74988_1_.nextInt(5);
        switch (i) {
            default: {
                return Door.OPENING;
            }
            case 2: {
                return Door.WOOD_DOOR;
            }
            case 3: {
                return Door.GRATES;
            }
            case 4: 
        }
        return Door.IRON_DOOR;
    }

    protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 p_74986_1_, List<StructureComponent> p_74986_2_, Random p_74986_3_, int p_74986_4_, int p_74986_5_) {
        if (this.coordBaseMode != null) {
            switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 4: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74986_1_, p_74986_2_, (Random)p_74986_3_, (int)(this.boundingBox.minX + p_74986_4_), (int)(this.boundingBox.minY + p_74986_5_), (int)(this.boundingBox.minZ - 1), (EnumFacing)this.coordBaseMode, (int)this.getComponentType());
                }
                case 1: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74986_1_, p_74986_2_, (Random)p_74986_3_, (int)(this.boundingBox.minX + p_74986_4_), (int)(this.boundingBox.minY + p_74986_5_), (int)(this.boundingBox.maxZ + 1), (EnumFacing)this.coordBaseMode, (int)this.getComponentType());
                }
                case 2: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74986_1_, p_74986_2_, (Random)p_74986_3_, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + p_74986_5_), (int)(this.boundingBox.minZ + p_74986_4_), (EnumFacing)this.coordBaseMode, (int)this.getComponentType());
                }
                case 3: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74986_1_, p_74986_2_, (Random)p_74986_3_, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + p_74986_5_), (int)(this.boundingBox.minZ + p_74986_4_), (EnumFacing)this.coordBaseMode, (int)this.getComponentType());
                }
            }
        }
        return null;
    }

    protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 p_74989_1_, List<StructureComponent> p_74989_2_, Random p_74989_3_, int p_74989_4_, int p_74989_5_) {
        if (this.coordBaseMode != null) {
            switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 4: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74989_1_, p_74989_2_, (Random)p_74989_3_, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + p_74989_4_), (int)(this.boundingBox.minZ + p_74989_5_), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                }
                case 1: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74989_1_, p_74989_2_, (Random)p_74989_3_, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + p_74989_4_), (int)(this.boundingBox.minZ + p_74989_5_), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
                }
                case 2: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74989_1_, p_74989_2_, (Random)p_74989_3_, (int)(this.boundingBox.minX + p_74989_5_), (int)(this.boundingBox.minY + p_74989_4_), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                }
                case 3: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74989_1_, p_74989_2_, (Random)p_74989_3_, (int)(this.boundingBox.minX + p_74989_5_), (int)(this.boundingBox.minY + p_74989_4_), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
                }
            }
        }
        return null;
    }

    protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 p_74987_1_, List<StructureComponent> p_74987_2_, Random p_74987_3_, int p_74987_4_, int p_74987_5_) {
        if (this.coordBaseMode != null) {
            switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 4: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74987_1_, p_74987_2_, (Random)p_74987_3_, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + p_74987_4_), (int)(this.boundingBox.minZ + p_74987_5_), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                }
                case 1: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74987_1_, p_74987_2_, (Random)p_74987_3_, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + p_74987_4_), (int)(this.boundingBox.minZ + p_74987_5_), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
                }
                case 2: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74987_1_, p_74987_2_, (Random)p_74987_3_, (int)(this.boundingBox.minX + p_74987_5_), (int)(this.boundingBox.minY + p_74987_4_), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                }
                case 3: {
                    return StructureStrongholdPieces.access$300((StructureStrongholdPieces.Stairs2)p_74987_1_, p_74987_2_, (Random)p_74987_3_, (int)(this.boundingBox.minX + p_74987_5_), (int)(this.boundingBox.minY + p_74987_4_), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
                }
            }
        }
        return null;
    }

    protected static boolean canStrongholdGoDeeper(StructureBoundingBox p_74991_0_) {
        return p_74991_0_ != null && p_74991_0_.minY > 10;
    }
}
