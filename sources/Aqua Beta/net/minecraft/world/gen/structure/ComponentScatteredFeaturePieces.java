package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class ComponentScatteredFeaturePieces {
    public static void registerScatteredFeaturePieces() {
        MapGenStructureIO.registerStructureComponent(DesertPyramid.class, (String)"TeDP");
        MapGenStructureIO.registerStructureComponent(JunglePyramid.class, (String)"TeJP");
        MapGenStructureIO.registerStructureComponent(SwampHut.class, (String)"TeSH");
    }
}
