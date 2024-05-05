package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public class StructureStrongholdPieces {
    private static final PieceWeight[] pieceWeightArray = new PieceWeight[]{new PieceWeight(Straight.class, 40, 0), new PieceWeight(Prison.class, 5, 5), new PieceWeight(LeftTurn.class, 20, 0), new PieceWeight(RightTurn.class, 20, 0), new PieceWeight(RoomCrossing.class, 10, 6), new PieceWeight(StairsStraight.class, 5, 5), new PieceWeight(Stairs.class, 5, 5), new PieceWeight(Crossing.class, 5, 4), new PieceWeight(ChestCorridor.class, 5, 4), new /* Unavailable Anonymous Inner Class!! */, new /* Unavailable Anonymous Inner Class!! */};
    private static List<PieceWeight> structurePieceList;
    private static Class<? extends Stronghold> strongComponentType;
    static int totalWeight;
    private static final Stones strongholdStones;

    public static void registerStrongholdPieces() {
        MapGenStructureIO.registerStructureComponent(ChestCorridor.class, (String)"SHCC");
        MapGenStructureIO.registerStructureComponent(Corridor.class, (String)"SHFC");
        MapGenStructureIO.registerStructureComponent(Crossing.class, (String)"SH5C");
        MapGenStructureIO.registerStructureComponent(LeftTurn.class, (String)"SHLT");
        MapGenStructureIO.registerStructureComponent(Library.class, (String)"SHLi");
        MapGenStructureIO.registerStructureComponent(PortalRoom.class, (String)"SHPR");
        MapGenStructureIO.registerStructureComponent(Prison.class, (String)"SHPH");
        MapGenStructureIO.registerStructureComponent(RightTurn.class, (String)"SHRT");
        MapGenStructureIO.registerStructureComponent(RoomCrossing.class, (String)"SHRC");
        MapGenStructureIO.registerStructureComponent(Stairs.class, (String)"SHSD");
        MapGenStructureIO.registerStructureComponent(Stairs2.class, (String)"SHStart");
        MapGenStructureIO.registerStructureComponent(Straight.class, (String)"SHS");
        MapGenStructureIO.registerStructureComponent(StairsStraight.class, (String)"SHSSD");
    }

    public static void prepareStructurePieces() {
        structurePieceList = Lists.newArrayList();
        for (PieceWeight structurestrongholdpieces$pieceweight : pieceWeightArray) {
            structurestrongholdpieces$pieceweight.instancesSpawned = 0;
            structurePieceList.add((Object)structurestrongholdpieces$pieceweight);
        }
        strongComponentType = null;
    }

    private static boolean canAddStructurePieces() {
        boolean flag = false;
        totalWeight = 0;
        for (PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
            if (structurestrongholdpieces$pieceweight.instancesLimit > 0 && structurestrongholdpieces$pieceweight.instancesSpawned < structurestrongholdpieces$pieceweight.instancesLimit) {
                flag = true;
            }
            totalWeight += structurestrongholdpieces$pieceweight.pieceWeight;
        }
        return flag;
    }

    private static Stronghold func_175954_a(Class<? extends Stronghold> p_175954_0_, List<StructureComponent> p_175954_1_, Random p_175954_2_, int p_175954_3_, int p_175954_4_, int p_175954_5_, EnumFacing p_175954_6_, int p_175954_7_) {
        Straight structurestrongholdpieces$stronghold = null;
        if (p_175954_0_ == Straight.class) {
            structurestrongholdpieces$stronghold = Straight.func_175862_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == Prison.class) {
            structurestrongholdpieces$stronghold = Prison.func_175860_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == LeftTurn.class) {
            structurestrongholdpieces$stronghold = LeftTurn.func_175867_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == RightTurn.class) {
            structurestrongholdpieces$stronghold = RightTurn.func_175867_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == RoomCrossing.class) {
            structurestrongholdpieces$stronghold = RoomCrossing.func_175859_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == StairsStraight.class) {
            structurestrongholdpieces$stronghold = StairsStraight.func_175861_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == Stairs.class) {
            structurestrongholdpieces$stronghold = Stairs.func_175863_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == Crossing.class) {
            structurestrongholdpieces$stronghold = Crossing.func_175866_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == ChestCorridor.class) {
            structurestrongholdpieces$stronghold = ChestCorridor.func_175868_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == Library.class) {
            structurestrongholdpieces$stronghold = Library.func_175864_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        } else if (p_175954_0_ == PortalRoom.class) {
            structurestrongholdpieces$stronghold = PortalRoom.func_175865_a(p_175954_1_, (Random)p_175954_2_, (int)p_175954_3_, (int)p_175954_4_, (int)p_175954_5_, (EnumFacing)p_175954_6_, (int)p_175954_7_);
        }
        return structurestrongholdpieces$stronghold;
    }

    private static Stronghold func_175955_b(Stairs2 p_175955_0_, List<StructureComponent> p_175955_1_, Random p_175955_2_, int p_175955_3_, int p_175955_4_, int p_175955_5_, EnumFacing p_175955_6_, int p_175955_7_) {
        if (!StructureStrongholdPieces.canAddStructurePieces()) {
            return null;
        }
        if (strongComponentType != null) {
            Stronghold structurestrongholdpieces$stronghold = StructureStrongholdPieces.func_175954_a(strongComponentType, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
            strongComponentType = null;
            if (structurestrongholdpieces$stronghold != null) {
                return structurestrongholdpieces$stronghold;
            }
        }
        int j = 0;
        block0: while (j < 5) {
            ++j;
            int i = p_175955_2_.nextInt(totalWeight);
            for (PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
                if ((i -= structurestrongholdpieces$pieceweight.pieceWeight) >= 0) continue;
                if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructuresOfType(p_175955_7_) || structurestrongholdpieces$pieceweight == p_175955_0_.strongholdPieceWeight) continue block0;
                Stronghold structurestrongholdpieces$stronghold1 = StructureStrongholdPieces.func_175954_a((Class<? extends Stronghold>)structurestrongholdpieces$pieceweight.pieceClass, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
                if (structurestrongholdpieces$stronghold1 == null) continue;
                ++structurestrongholdpieces$pieceweight.instancesSpawned;
                p_175955_0_.strongholdPieceWeight = structurestrongholdpieces$pieceweight;
                if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructures()) {
                    structurePieceList.remove((Object)structurestrongholdpieces$pieceweight);
                }
                return structurestrongholdpieces$stronghold1;
            }
        }
        StructureBoundingBox structureboundingbox = Corridor.func_175869_a(p_175955_1_, (Random)p_175955_2_, (int)p_175955_3_, (int)p_175955_4_, (int)p_175955_5_, (EnumFacing)p_175955_6_);
        if (structureboundingbox != null && structureboundingbox.minY > 1) {
            return new Corridor(p_175955_7_, p_175955_2_, structureboundingbox, p_175955_6_);
        }
        return null;
    }

    private static StructureComponent func_175953_c(Stairs2 p_175953_0_, List<StructureComponent> p_175953_1_, Random p_175953_2_, int p_175953_3_, int p_175953_4_, int p_175953_5_, EnumFacing p_175953_6_, int p_175953_7_) {
        if (p_175953_7_ > 50) {
            return null;
        }
        if (Math.abs((int)(p_175953_3_ - p_175953_0_.getBoundingBox().minX)) <= 112 && Math.abs((int)(p_175953_5_ - p_175953_0_.getBoundingBox().minZ)) <= 112) {
            Stronghold structurecomponent = StructureStrongholdPieces.func_175955_b(p_175953_0_, p_175953_1_, p_175953_2_, p_175953_3_, p_175953_4_, p_175953_5_, p_175953_6_, p_175953_7_ + 1);
            if (structurecomponent != null) {
                p_175953_1_.add((Object)structurecomponent);
                p_175953_0_.field_75026_c.add((Object)structurecomponent);
            }
            return structurecomponent;
        }
        return null;
    }

    static /* synthetic */ Stones access$100() {
        return strongholdStones;
    }

    static /* synthetic */ Class access$202(Class x0) {
        strongComponentType = x0;
        return strongComponentType;
    }

    static /* synthetic */ StructureComponent access$300(Stairs2 x0, List x1, Random x2, int x3, int x4, int x5, EnumFacing x6, int x7) {
        return StructureStrongholdPieces.func_175953_c(x0, (List<StructureComponent>)x1, x2, x3, x4, x5, x6, x7);
    }

    static {
        strongholdStones = new Stones(null);
    }
}
