package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

/*
 * Exception performing whole class analysis ignored.
 */
static abstract class StructureNetherBridgePieces.Piece
extends StructureComponent {
    protected static final List<WeightedRandomChestContent> field_111019_a = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 5), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 15), new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.golden_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 5), new WeightedRandomChestContent(Items.nether_wart, 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 8), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 3), new WeightedRandomChestContent(Item.getItemFromBlock((Block)Blocks.obsidian), 0, 2, 4, 2)});

    public StructureNetherBridgePieces.Piece() {
    }

    protected StructureNetherBridgePieces.Piece(int p_i2054_1_) {
        super(p_i2054_1_);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
    }

    private int getTotalWeight(List<StructureNetherBridgePieces.PieceWeight> p_74960_1_) {
        boolean flag = false;
        int i = 0;
        for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : p_74960_1_) {
            if (structurenetherbridgepieces$pieceweight.field_78824_d > 0 && structurenetherbridgepieces$pieceweight.field_78827_c < structurenetherbridgepieces$pieceweight.field_78824_d) {
                flag = true;
            }
            i += structurenetherbridgepieces$pieceweight.field_78826_b;
        }
        return flag ? i : -1;
    }

    private StructureNetherBridgePieces.Piece func_175871_a(StructureNetherBridgePieces.Start p_175871_1_, List<StructureNetherBridgePieces.PieceWeight> p_175871_2_, List<StructureComponent> p_175871_3_, Random p_175871_4_, int p_175871_5_, int p_175871_6_, int p_175871_7_, EnumFacing p_175871_8_, int p_175871_9_) {
        int i = this.getTotalWeight(p_175871_2_);
        boolean flag = i > 0 && p_175871_9_ <= 30;
        int j = 0;
        block0: while (j < 5 && flag) {
            ++j;
            int k = p_175871_4_.nextInt(i);
            for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : p_175871_2_) {
                if ((k -= structurenetherbridgepieces$pieceweight.field_78826_b) >= 0) continue;
                if (!structurenetherbridgepieces$pieceweight.func_78822_a(p_175871_9_) || structurenetherbridgepieces$pieceweight == p_175871_1_.theNetherBridgePieceWeight && !structurenetherbridgepieces$pieceweight.field_78825_e) continue block0;
                StructureNetherBridgePieces.Piece structurenetherbridgepieces$piece = StructureNetherBridgePieces.access$000((StructureNetherBridgePieces.PieceWeight)structurenetherbridgepieces$pieceweight, p_175871_3_, (Random)p_175871_4_, (int)p_175871_5_, (int)p_175871_6_, (int)p_175871_7_, (EnumFacing)p_175871_8_, (int)p_175871_9_);
                if (structurenetherbridgepieces$piece == null) continue;
                ++structurenetherbridgepieces$pieceweight.field_78827_c;
                p_175871_1_.theNetherBridgePieceWeight = structurenetherbridgepieces$pieceweight;
                if (!structurenetherbridgepieces$pieceweight.func_78823_a()) {
                    p_175871_2_.remove((Object)structurenetherbridgepieces$pieceweight);
                }
                return structurenetherbridgepieces$piece;
            }
        }
        return StructureNetherBridgePieces.End.func_175884_a(p_175871_3_, (Random)p_175871_4_, (int)p_175871_5_, (int)p_175871_6_, (int)p_175871_7_, (EnumFacing)p_175871_8_, (int)p_175871_9_);
    }

    private StructureComponent func_175870_a(StructureNetherBridgePieces.Start p_175870_1_, List<StructureComponent> p_175870_2_, Random p_175870_3_, int p_175870_4_, int p_175870_5_, int p_175870_6_, EnumFacing p_175870_7_, int p_175870_8_, boolean p_175870_9_) {
        if (Math.abs((int)(p_175870_4_ - p_175870_1_.getBoundingBox().minX)) <= 112 && Math.abs((int)(p_175870_6_ - p_175870_1_.getBoundingBox().minZ)) <= 112) {
            StructureNetherBridgePieces.Piece structurecomponent;
            List list = p_175870_1_.primaryWeights;
            if (p_175870_9_) {
                list = p_175870_1_.secondaryWeights;
            }
            if ((structurecomponent = this.func_175871_a(p_175870_1_, (List<StructureNetherBridgePieces.PieceWeight>)list, p_175870_2_, p_175870_3_, p_175870_4_, p_175870_5_, p_175870_6_, p_175870_7_, p_175870_8_ + 1)) != null) {
                p_175870_2_.add((Object)structurecomponent);
                p_175870_1_.field_74967_d.add((Object)structurecomponent);
            }
            return structurecomponent;
        }
        return StructureNetherBridgePieces.End.func_175884_a(p_175870_2_, (Random)p_175870_3_, (int)p_175870_4_, (int)p_175870_5_, (int)p_175870_6_, (EnumFacing)p_175870_7_, (int)p_175870_8_);
    }

    protected StructureComponent getNextComponentNormal(StructureNetherBridgePieces.Start p_74963_1_, List<StructureComponent> p_74963_2_, Random p_74963_3_, int p_74963_4_, int p_74963_5_, boolean p_74963_6_) {
        if (this.coordBaseMode != null) {
            switch (StructureNetherBridgePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    return this.func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType(), p_74963_6_);
                }
                case 2: {
                    return this.func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType(), p_74963_6_);
                }
                case 3: {
                    return this.func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, this.coordBaseMode, this.getComponentType(), p_74963_6_);
                }
                case 4: {
                    return this.func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, this.coordBaseMode, this.getComponentType(), p_74963_6_);
                }
            }
        }
        return null;
    }

    protected StructureComponent getNextComponentX(StructureNetherBridgePieces.Start p_74961_1_, List<StructureComponent> p_74961_2_, Random p_74961_3_, int p_74961_4_, int p_74961_5_, boolean p_74961_6_) {
        if (this.coordBaseMode != null) {
            switch (StructureNetherBridgePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    return this.func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, this.getComponentType(), p_74961_6_);
                }
                case 2: {
                    return this.func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, this.getComponentType(), p_74961_6_);
                }
                case 3: {
                    return this.func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), p_74961_6_);
                }
                case 4: {
                    return this.func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), p_74961_6_);
                }
            }
        }
        return null;
    }

    protected StructureComponent getNextComponentZ(StructureNetherBridgePieces.Start p_74965_1_, List<StructureComponent> p_74965_2_, Random p_74965_3_, int p_74965_4_, int p_74965_5_, boolean p_74965_6_) {
        if (this.coordBaseMode != null) {
            switch (StructureNetherBridgePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                case 1: {
                    return this.func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, this.getComponentType(), p_74965_6_);
                }
                case 2: {
                    return this.func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, this.getComponentType(), p_74965_6_);
                }
                case 3: {
                    return this.func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), p_74965_6_);
                }
                case 4: {
                    return this.func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), p_74965_6_);
                }
            }
        }
        return null;
    }

    protected static boolean isAboveGround(StructureBoundingBox p_74964_0_) {
        return p_74964_0_ != null && p_74964_0_.minY > 10;
    }
}
