package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public class StructureMineshaftPieces {
    private static final List<WeightedRandomChestContent> CHEST_CONTENT_WEIGHT_LIST = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock((Block)Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1)});

    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(Corridor.class, (String)"MSCorridor");
        MapGenStructureIO.registerStructureComponent(Cross.class, (String)"MSCrossing");
        MapGenStructureIO.registerStructureComponent(Room.class, (String)"MSRoom");
        MapGenStructureIO.registerStructureComponent(Stairs.class, (String)"MSStairs");
    }

    private static StructureComponent func_175892_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing, int type) {
        int i = rand.nextInt(100);
        if (i >= 80) {
            StructureBoundingBox structureboundingbox = Cross.func_175813_a(listIn, (Random)rand, (int)x, (int)y, (int)z, (EnumFacing)facing);
            if (structureboundingbox != null) {
                return new Cross(type, rand, structureboundingbox, facing);
            }
        } else if (i >= 70) {
            StructureBoundingBox structureboundingbox1 = Stairs.func_175812_a(listIn, (Random)rand, (int)x, (int)y, (int)z, (EnumFacing)facing);
            if (structureboundingbox1 != null) {
                return new Stairs(type, rand, structureboundingbox1, facing);
            }
        } else {
            StructureBoundingBox structureboundingbox2 = Corridor.func_175814_a(listIn, (Random)rand, (int)x, (int)y, (int)z, (EnumFacing)facing);
            if (structureboundingbox2 != null) {
                return new Corridor(type, rand, structureboundingbox2, facing);
            }
        }
        return null;
    }

    private static StructureComponent func_175890_b(StructureComponent componentIn, List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing, int type) {
        if (type > 8) {
            return null;
        }
        if (Math.abs((int)(x - componentIn.getBoundingBox().minX)) <= 80 && Math.abs((int)(z - componentIn.getBoundingBox().minZ)) <= 80) {
            StructureComponent structurecomponent = StructureMineshaftPieces.func_175892_a(listIn, rand, x, y, z, facing, type + 1);
            if (structurecomponent != null) {
                listIn.add((Object)structurecomponent);
                structurecomponent.buildComponent(componentIn, listIn, rand);
            }
            return structurecomponent;
        }
        return null;
    }

    static /* synthetic */ StructureComponent access$000(StructureComponent x0, List x1, Random x2, int x3, int x4, int x5, EnumFacing x6, int x7) {
        return StructureMineshaftPieces.func_175890_b(x0, (List<StructureComponent>)x1, x2, x3, x4, x5, x6, x7);
    }

    static /* synthetic */ List access$100() {
        return CHEST_CONTENT_WEIGHT_LIST;
    }
}
