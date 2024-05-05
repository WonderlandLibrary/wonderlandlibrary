package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

/*
 * Exception performing whole class analysis ignored.
 */
public class StructureNetherBridgePieces {
    private static final PieceWeight[] primaryComponents = new PieceWeight[]{new PieceWeight(Straight.class, 30, 0, true), new PieceWeight(Crossing3.class, 10, 4), new PieceWeight(Crossing.class, 10, 4), new PieceWeight(Stairs.class, 10, 3), new PieceWeight(Throne.class, 5, 2), new PieceWeight(Entrance.class, 5, 1)};
    private static final PieceWeight[] secondaryComponents = new PieceWeight[]{new PieceWeight(Corridor5.class, 25, 0, true), new PieceWeight(Crossing2.class, 15, 5), new PieceWeight(Corridor2.class, 5, 10), new PieceWeight(Corridor.class, 5, 10), new PieceWeight(Corridor3.class, 10, 3, true), new PieceWeight(Corridor4.class, 7, 2), new PieceWeight(NetherStalkRoom.class, 5, 2)};

    public static void registerNetherFortressPieces() {
        MapGenStructureIO.registerStructureComponent(Crossing3.class, (String)"NeBCr");
        MapGenStructureIO.registerStructureComponent(End.class, (String)"NeBEF");
        MapGenStructureIO.registerStructureComponent(Straight.class, (String)"NeBS");
        MapGenStructureIO.registerStructureComponent(Corridor3.class, (String)"NeCCS");
        MapGenStructureIO.registerStructureComponent(Corridor4.class, (String)"NeCTB");
        MapGenStructureIO.registerStructureComponent(Entrance.class, (String)"NeCE");
        MapGenStructureIO.registerStructureComponent(Crossing2.class, (String)"NeSCSC");
        MapGenStructureIO.registerStructureComponent(Corridor.class, (String)"NeSCLT");
        MapGenStructureIO.registerStructureComponent(Corridor5.class, (String)"NeSC");
        MapGenStructureIO.registerStructureComponent(Corridor2.class, (String)"NeSCRT");
        MapGenStructureIO.registerStructureComponent(NetherStalkRoom.class, (String)"NeCSR");
        MapGenStructureIO.registerStructureComponent(Throne.class, (String)"NeMT");
        MapGenStructureIO.registerStructureComponent(Crossing.class, (String)"NeRC");
        MapGenStructureIO.registerStructureComponent(Stairs.class, (String)"NeSR");
        MapGenStructureIO.registerStructureComponent(Start.class, (String)"NeStart");
    }

    private static Piece func_175887_b(PieceWeight p_175887_0_, List<StructureComponent> p_175887_1_, Random p_175887_2_, int p_175887_3_, int p_175887_4_, int p_175887_5_, EnumFacing p_175887_6_, int p_175887_7_) {
        Class oclass = p_175887_0_.weightClass;
        Straight structurenetherbridgepieces$piece = null;
        if (oclass == Straight.class) {
            structurenetherbridgepieces$piece = Straight.func_175882_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Crossing3.class) {
            structurenetherbridgepieces$piece = Crossing3.func_175885_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Crossing.class) {
            structurenetherbridgepieces$piece = Crossing.func_175873_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Stairs.class) {
            structurenetherbridgepieces$piece = Stairs.func_175872_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (int)p_175887_7_, (EnumFacing)p_175887_6_);
        } else if (oclass == Throne.class) {
            structurenetherbridgepieces$piece = Throne.func_175874_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (int)p_175887_7_, (EnumFacing)p_175887_6_);
        } else if (oclass == Entrance.class) {
            structurenetherbridgepieces$piece = Entrance.func_175881_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Corridor5.class) {
            structurenetherbridgepieces$piece = Corridor5.func_175877_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Corridor2.class) {
            structurenetherbridgepieces$piece = Corridor2.func_175876_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Corridor.class) {
            structurenetherbridgepieces$piece = Corridor.func_175879_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Corridor3.class) {
            structurenetherbridgepieces$piece = Corridor3.func_175883_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Corridor4.class) {
            structurenetherbridgepieces$piece = Corridor4.func_175880_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == Crossing2.class) {
            structurenetherbridgepieces$piece = Crossing2.func_175878_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        } else if (oclass == NetherStalkRoom.class) {
            structurenetherbridgepieces$piece = NetherStalkRoom.func_175875_a(p_175887_1_, (Random)p_175887_2_, (int)p_175887_3_, (int)p_175887_4_, (int)p_175887_5_, (EnumFacing)p_175887_6_, (int)p_175887_7_);
        }
        return structurenetherbridgepieces$piece;
    }

    static /* synthetic */ Piece access$000(PieceWeight x0, List x1, Random x2, int x3, int x4, int x5, EnumFacing x6, int x7) {
        return StructureNetherBridgePieces.func_175887_b(x0, (List<StructureComponent>)x1, x2, x3, x4, x5, x6, x7);
    }

    static /* synthetic */ PieceWeight[] access$100() {
        return primaryComponents;
    }

    static /* synthetic */ PieceWeight[] access$200() {
        return secondaryComponents;
    }
}
