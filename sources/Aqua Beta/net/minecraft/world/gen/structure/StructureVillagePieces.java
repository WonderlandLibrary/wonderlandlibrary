package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/*
 * Exception performing whole class analysis ignored.
 */
public class StructureVillagePieces {
    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(House1.class, (String)"ViBH");
        MapGenStructureIO.registerStructureComponent(Field1.class, (String)"ViDF");
        MapGenStructureIO.registerStructureComponent(Field2.class, (String)"ViF");
        MapGenStructureIO.registerStructureComponent(Torch.class, (String)"ViL");
        MapGenStructureIO.registerStructureComponent(Hall.class, (String)"ViPH");
        MapGenStructureIO.registerStructureComponent(House4Garden.class, (String)"ViSH");
        MapGenStructureIO.registerStructureComponent(WoodHut.class, (String)"ViSmH");
        MapGenStructureIO.registerStructureComponent(Church.class, (String)"ViST");
        MapGenStructureIO.registerStructureComponent(House2.class, (String)"ViS");
        MapGenStructureIO.registerStructureComponent(Start.class, (String)"ViStart");
        MapGenStructureIO.registerStructureComponent(Path.class, (String)"ViSR");
        MapGenStructureIO.registerStructureComponent(House3.class, (String)"ViTRH");
        MapGenStructureIO.registerStructureComponent(Well.class, (String)"ViW");
    }

    public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        ArrayList list = Lists.newArrayList();
        list.add((Object)new PieceWeight(House4Garden.class, 4, MathHelper.getRandomIntegerInRange((Random)random, (int)(2 + size), (int)(4 + size * 2))));
        list.add((Object)new PieceWeight(Church.class, 20, MathHelper.getRandomIntegerInRange((Random)random, (int)(0 + size), (int)(1 + size))));
        list.add((Object)new PieceWeight(House1.class, 20, MathHelper.getRandomIntegerInRange((Random)random, (int)(0 + size), (int)(2 + size))));
        list.add((Object)new PieceWeight(WoodHut.class, 3, MathHelper.getRandomIntegerInRange((Random)random, (int)(2 + size), (int)(5 + size * 3))));
        list.add((Object)new PieceWeight(Hall.class, 15, MathHelper.getRandomIntegerInRange((Random)random, (int)(0 + size), (int)(2 + size))));
        list.add((Object)new PieceWeight(Field1.class, 3, MathHelper.getRandomIntegerInRange((Random)random, (int)(1 + size), (int)(4 + size))));
        list.add((Object)new PieceWeight(Field2.class, 3, MathHelper.getRandomIntegerInRange((Random)random, (int)(2 + size), (int)(4 + size * 2))));
        list.add((Object)new PieceWeight(House2.class, 15, MathHelper.getRandomIntegerInRange((Random)random, (int)0, (int)(1 + size))));
        list.add((Object)new PieceWeight(House3.class, 8, MathHelper.getRandomIntegerInRange((Random)random, (int)(0 + size), (int)(3 + size * 2))));
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (((PieceWeight)iterator.next()).villagePiecesLimit != 0) continue;
            iterator.remove();
        }
        return list;
    }

    private static int func_75079_a(List<PieceWeight> p_75079_0_) {
        boolean flag = false;
        int i = 0;
        for (PieceWeight structurevillagepieces$pieceweight : p_75079_0_) {
            if (structurevillagepieces$pieceweight.villagePiecesLimit > 0 && structurevillagepieces$pieceweight.villagePiecesSpawned < structurevillagepieces$pieceweight.villagePiecesLimit) {
                flag = true;
            }
            i += structurevillagepieces$pieceweight.villagePieceWeight;
        }
        return flag ? i : -1;
    }

    private static Village func_176065_a(Start start, PieceWeight weight, List<StructureComponent> p_176065_2_, Random rand, int p_176065_4_, int p_176065_5_, int p_176065_6_, EnumFacing facing, int p_176065_8_) {
        Class oclass = weight.villagePieceClass;
        House4Garden structurevillagepieces$village = null;
        if (oclass == House4Garden.class) {
            structurevillagepieces$village = House4Garden.func_175858_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == Church.class) {
            structurevillagepieces$village = Church.func_175854_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == House1.class) {
            structurevillagepieces$village = House1.func_175850_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == WoodHut.class) {
            structurevillagepieces$village = WoodHut.func_175853_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == Hall.class) {
            structurevillagepieces$village = Hall.func_175857_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == Field1.class) {
            structurevillagepieces$village = Field1.func_175851_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == Field2.class) {
            structurevillagepieces$village = Field2.func_175852_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == House2.class) {
            structurevillagepieces$village = House2.func_175855_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        } else if (oclass == House3.class) {
            structurevillagepieces$village = House3.func_175849_a((Start)start, p_176065_2_, (Random)rand, (int)p_176065_4_, (int)p_176065_5_, (int)p_176065_6_, (EnumFacing)facing, (int)p_176065_8_);
        }
        return structurevillagepieces$village;
    }

    private static Village func_176067_c(Start start, List<StructureComponent> p_176067_1_, Random rand, int p_176067_3_, int p_176067_4_, int p_176067_5_, EnumFacing facing, int p_176067_7_) {
        int i = StructureVillagePieces.func_75079_a((List<PieceWeight>)start.structureVillageWeightedPieceList);
        if (i <= 0) {
            return null;
        }
        int j = 0;
        block0: while (j < 5) {
            ++j;
            int k = rand.nextInt(i);
            for (PieceWeight structurevillagepieces$pieceweight : start.structureVillageWeightedPieceList) {
                if ((k -= structurevillagepieces$pieceweight.villagePieceWeight) >= 0) continue;
                if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePiecesOfType(p_176067_7_) || structurevillagepieces$pieceweight == start.structVillagePieceWeight && start.structureVillageWeightedPieceList.size() > 1) continue block0;
                Village structurevillagepieces$village = StructureVillagePieces.func_176065_a(start, structurevillagepieces$pieceweight, p_176067_1_, rand, p_176067_3_, p_176067_4_, p_176067_5_, facing, p_176067_7_);
                if (structurevillagepieces$village == null) continue;
                ++structurevillagepieces$pieceweight.villagePiecesSpawned;
                start.structVillagePieceWeight = structurevillagepieces$pieceweight;
                if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePieces()) {
                    start.structureVillageWeightedPieceList.remove((Object)structurevillagepieces$pieceweight);
                }
                return structurevillagepieces$village;
            }
        }
        StructureBoundingBox structureboundingbox = Torch.func_175856_a((Start)start, p_176067_1_, (Random)rand, (int)p_176067_3_, (int)p_176067_4_, (int)p_176067_5_, (EnumFacing)facing);
        if (structureboundingbox != null) {
            return new Torch(start, p_176067_7_, rand, structureboundingbox, facing);
        }
        return null;
    }

    private static StructureComponent func_176066_d(Start start, List<StructureComponent> p_176066_1_, Random rand, int p_176066_3_, int p_176066_4_, int p_176066_5_, EnumFacing facing, int p_176066_7_) {
        if (p_176066_7_ > 50) {
            return null;
        }
        if (Math.abs((int)(p_176066_3_ - start.getBoundingBox().minX)) <= 112 && Math.abs((int)(p_176066_5_ - start.getBoundingBox().minZ)) <= 112) {
            Village structurecomponent = StructureVillagePieces.func_176067_c(start, p_176066_1_, rand, p_176066_3_, p_176066_4_, p_176066_5_, facing, p_176066_7_ + 1);
            if (structurecomponent != null) {
                int i1;
                int i = (structurecomponent.boundingBox.minX + structurecomponent.boundingBox.maxX) / 2;
                int j = (structurecomponent.boundingBox.minZ + structurecomponent.boundingBox.maxZ) / 2;
                int k = structurecomponent.boundingBox.maxX - structurecomponent.boundingBox.minX;
                int l = structurecomponent.boundingBox.maxZ - structurecomponent.boundingBox.minZ;
                int n = i1 = k > l ? k : l;
                if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    p_176066_1_.add((Object)structurecomponent);
                    start.field_74932_i.add((Object)structurecomponent);
                    return structurecomponent;
                }
            }
            return null;
        }
        return null;
    }

    private static StructureComponent func_176069_e(Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing facing, int p_176069_7_) {
        if (p_176069_7_ > 3 + start.terrainType) {
            return null;
        }
        if (Math.abs((int)(p_176069_3_ - start.getBoundingBox().minX)) <= 112 && Math.abs((int)(p_176069_5_ - start.getBoundingBox().minZ)) <= 112) {
            StructureBoundingBox structureboundingbox = Path.func_175848_a((Start)start, p_176069_1_, (Random)rand, (int)p_176069_3_, (int)p_176069_4_, (int)p_176069_5_, (EnumFacing)facing);
            if (structureboundingbox != null && structureboundingbox.minY > 10) {
                int i1;
                Path structurecomponent = new Path(start, p_176069_7_, rand, structureboundingbox, facing);
                int i = (structurecomponent.boundingBox.minX + structurecomponent.boundingBox.maxX) / 2;
                int j = (structurecomponent.boundingBox.minZ + structurecomponent.boundingBox.maxZ) / 2;
                int k = structurecomponent.boundingBox.maxX - structurecomponent.boundingBox.minX;
                int l = structurecomponent.boundingBox.maxZ - structurecomponent.boundingBox.minZ;
                int n = i1 = k > l ? k : l;
                if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
                    p_176069_1_.add((Object)structurecomponent);
                    start.field_74930_j.add((Object)structurecomponent);
                    return structurecomponent;
                }
            }
            return null;
        }
        return null;
    }

    static /* synthetic */ StructureComponent access$000(Start x0, List x1, Random x2, int x3, int x4, int x5, EnumFacing x6, int x7) {
        return StructureVillagePieces.func_176069_e(x0, (List<StructureComponent>)x1, x2, x3, x4, x5, x6, x7);
    }

    static /* synthetic */ StructureComponent access$100(Start x0, List x1, Random x2, int x3, int x4, int x5, EnumFacing x6, int x7) {
        return StructureVillagePieces.func_176066_d(x0, (List<StructureComponent>)x1, x2, x3, x4, x5, x6, x7);
    }
}
