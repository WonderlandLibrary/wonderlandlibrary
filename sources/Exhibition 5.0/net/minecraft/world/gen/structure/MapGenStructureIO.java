// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class MapGenStructureIO
{
    private static final Logger logger;
    private static Map field_143040_a;
    private static Map field_143038_b;
    private static Map field_143039_c;
    private static Map field_143037_d;
    private static final String __OBFID = "CL_00000509";
    
    private static void registerStructure(final Class p_143034_0_, final String p_143034_1_) {
        MapGenStructureIO.field_143040_a.put(p_143034_1_, p_143034_0_);
        MapGenStructureIO.field_143038_b.put(p_143034_0_, p_143034_1_);
    }
    
    static void registerStructureComponent(final Class p_143031_0_, final String p_143031_1_) {
        MapGenStructureIO.field_143039_c.put(p_143031_1_, p_143031_0_);
        MapGenStructureIO.field_143037_d.put(p_143031_0_, p_143031_1_);
    }
    
    public static String func_143033_a(final StructureStart p_143033_0_) {
        return MapGenStructureIO.field_143038_b.get(p_143033_0_.getClass());
    }
    
    public static String func_143036_a(final StructureComponent p_143036_0_) {
        return MapGenStructureIO.field_143037_d.get(p_143036_0_.getClass());
    }
    
    public static StructureStart func_143035_a(final NBTTagCompound p_143035_0_, final World worldIn) {
        StructureStart var2 = null;
        try {
            final Class var3 = MapGenStructureIO.field_143040_a.get(p_143035_0_.getString("id"));
            if (var3 != null) {
                var2 = var3.newInstance();
            }
        }
        catch (Exception var4) {
            MapGenStructureIO.logger.warn("Failed Start with id " + p_143035_0_.getString("id"));
            var4.printStackTrace();
        }
        if (var2 != null) {
            var2.func_143020_a(worldIn, p_143035_0_);
        }
        else {
            MapGenStructureIO.logger.warn("Skipping Structure with id " + p_143035_0_.getString("id"));
        }
        return var2;
    }
    
    public static StructureComponent func_143032_b(final NBTTagCompound p_143032_0_, final World worldIn) {
        StructureComponent var2 = null;
        try {
            final Class var3 = MapGenStructureIO.field_143039_c.get(p_143032_0_.getString("id"));
            if (var3 != null) {
                var2 = var3.newInstance();
            }
        }
        catch (Exception var4) {
            MapGenStructureIO.logger.warn("Failed Piece with id " + p_143032_0_.getString("id"));
            var4.printStackTrace();
        }
        if (var2 != null) {
            var2.func_143009_a(worldIn, p_143032_0_);
        }
        else {
            MapGenStructureIO.logger.warn("Skipping Piece with id " + p_143032_0_.getString("id"));
        }
        return var2;
    }
    
    static {
        logger = LogManager.getLogger();
        MapGenStructureIO.field_143040_a = Maps.newHashMap();
        MapGenStructureIO.field_143038_b = Maps.newHashMap();
        MapGenStructureIO.field_143039_c = Maps.newHashMap();
        MapGenStructureIO.field_143037_d = Maps.newHashMap();
        registerStructure(StructureMineshaftStart.class, "Mineshaft");
        registerStructure(MapGenVillage.Start.class, "Village");
        registerStructure(MapGenNetherBridge.Start.class, "Fortress");
        registerStructure(MapGenStronghold.Start.class, "Stronghold");
        registerStructure(MapGenScatteredFeature.Start.class, "Temple");
        registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
        StructureMineshaftPieces.registerStructurePieces();
        StructureVillagePieces.registerVillagePieces();
        StructureNetherBridgePieces.registerNetherFortressPieces();
        StructureStrongholdPieces.registerStrongholdPieces();
        ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
        StructureOceanMonumentPieces.func_175970_a();
    }
}
