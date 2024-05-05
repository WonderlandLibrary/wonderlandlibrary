package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public class StructureOceanMonumentPieces {
    public static void registerOceanMonumentPieces() {
        MapGenStructureIO.registerStructureComponent(MonumentBuilding.class, (String)"OMB");
        MapGenStructureIO.registerStructureComponent(MonumentCoreRoom.class, (String)"OMCR");
        MapGenStructureIO.registerStructureComponent(DoubleXRoom.class, (String)"OMDXR");
        MapGenStructureIO.registerStructureComponent(DoubleXYRoom.class, (String)"OMDXYR");
        MapGenStructureIO.registerStructureComponent(DoubleYRoom.class, (String)"OMDYR");
        MapGenStructureIO.registerStructureComponent(DoubleYZRoom.class, (String)"OMDYZR");
        MapGenStructureIO.registerStructureComponent(DoubleZRoom.class, (String)"OMDZR");
        MapGenStructureIO.registerStructureComponent(EntryRoom.class, (String)"OMEntry");
        MapGenStructureIO.registerStructureComponent(Penthouse.class, (String)"OMPenthouse");
        MapGenStructureIO.registerStructureComponent(SimpleRoom.class, (String)"OMSimple");
        MapGenStructureIO.registerStructureComponent(SimpleTopRoom.class, (String)"OMSimpleT");
    }
}
