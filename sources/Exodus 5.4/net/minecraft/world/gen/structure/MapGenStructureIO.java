/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraft.world.gen.structure.StructureMineshaftStart;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapGenStructureIO {
    private static final Logger logger = LogManager.getLogger();
    private static Map<String, Class<? extends StructureStart>> startNameToClassMap = Maps.newHashMap();
    private static Map<Class<? extends StructureComponent>, String> componentClassToNameMap;
    private static Map<Class<? extends StructureStart>, String> startClassToNameMap;
    private static Map<String, Class<? extends StructureComponent>> componentNameToClassMap;

    public static String getStructureComponentName(StructureComponent structureComponent) {
        return componentClassToNameMap.get(structureComponent.getClass());
    }

    private static void registerStructure(Class<? extends StructureStart> clazz, String string) {
        startNameToClassMap.put(string, clazz);
        startClassToNameMap.put(clazz, string);
    }

    static {
        startClassToNameMap = Maps.newHashMap();
        componentNameToClassMap = Maps.newHashMap();
        componentClassToNameMap = Maps.newHashMap();
        MapGenStructureIO.registerStructure(StructureMineshaftStart.class, "Mineshaft");
        MapGenStructureIO.registerStructure(MapGenVillage.Start.class, "Village");
        MapGenStructureIO.registerStructure(MapGenNetherBridge.Start.class, "Fortress");
        MapGenStructureIO.registerStructure(MapGenStronghold.Start.class, "Stronghold");
        MapGenStructureIO.registerStructure(MapGenScatteredFeature.Start.class, "Temple");
        MapGenStructureIO.registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
        StructureMineshaftPieces.registerStructurePieces();
        StructureVillagePieces.registerVillagePieces();
        StructureNetherBridgePieces.registerNetherFortressPieces();
        StructureStrongholdPieces.registerStrongholdPieces();
        ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
        StructureOceanMonumentPieces.registerOceanMonumentPieces();
    }

    static void registerStructureComponent(Class<? extends StructureComponent> clazz, String string) {
        componentNameToClassMap.put(string, clazz);
        componentClassToNameMap.put(clazz, string);
    }

    public static String getStructureStartName(StructureStart structureStart) {
        return startClassToNameMap.get(structureStart.getClass());
    }

    public static StructureStart getStructureStart(NBTTagCompound nBTTagCompound, World world) {
        StructureStart structureStart = null;
        try {
            Class<? extends StructureStart> clazz = startNameToClassMap.get(nBTTagCompound.getString("id"));
            if (clazz != null) {
                structureStart = clazz.newInstance();
            }
        }
        catch (Exception exception) {
            logger.warn("Failed Start with id " + nBTTagCompound.getString("id"));
            exception.printStackTrace();
        }
        if (structureStart != null) {
            structureStart.readStructureComponentsFromNBT(world, nBTTagCompound);
        } else {
            logger.warn("Skipping Structure with id " + nBTTagCompound.getString("id"));
        }
        return structureStart;
    }

    public static StructureComponent getStructureComponent(NBTTagCompound nBTTagCompound, World world) {
        StructureComponent structureComponent = null;
        try {
            Class<? extends StructureComponent> clazz = componentNameToClassMap.get(nBTTagCompound.getString("id"));
            if (clazz != null) {
                structureComponent = clazz.newInstance();
            }
        }
        catch (Exception exception) {
            logger.warn("Failed Piece with id " + nBTTagCompound.getString("id"));
            exception.printStackTrace();
        }
        if (structureComponent != null) {
            structureComponent.readStructureBaseNBT(world, nBTTagCompound);
        } else {
            logger.warn("Skipping Piece with id " + nBTTagCompound.getString("id"));
        }
        return structureComponent;
    }
}

