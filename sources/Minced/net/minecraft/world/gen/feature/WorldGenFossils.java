// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class WorldGenFossils extends WorldGenerator
{
    private static final ResourceLocation STRUCTURE_SPINE_01;
    private static final ResourceLocation STRUCTURE_SPINE_02;
    private static final ResourceLocation STRUCTURE_SPINE_03;
    private static final ResourceLocation STRUCTURE_SPINE_04;
    private static final ResourceLocation STRUCTURE_SPINE_01_COAL;
    private static final ResourceLocation STRUCTURE_SPINE_02_COAL;
    private static final ResourceLocation STRUCTURE_SPINE_03_COAL;
    private static final ResourceLocation STRUCTURE_SPINE_04_COAL;
    private static final ResourceLocation STRUCTURE_SKULL_01;
    private static final ResourceLocation STRUCTURE_SKULL_02;
    private static final ResourceLocation STRUCTURE_SKULL_03;
    private static final ResourceLocation STRUCTURE_SKULL_04;
    private static final ResourceLocation STRUCTURE_SKULL_01_COAL;
    private static final ResourceLocation STRUCTURE_SKULL_02_COAL;
    private static final ResourceLocation STRUCTURE_SKULL_03_COAL;
    private static final ResourceLocation STRUCTURE_SKULL_04_COAL;
    private static final ResourceLocation[] FOSSILS;
    private static final ResourceLocation[] FOSSILS_COAL;
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final Random random = worldIn.getChunk(position).getRandomWithSeed(987234911L);
        final MinecraftServer minecraftserver = worldIn.getMinecraftServer();
        final Rotation[] arotation = Rotation.values();
        final Rotation rotation = arotation[random.nextInt(arotation.length)];
        final int i = random.nextInt(WorldGenFossils.FOSSILS.length);
        final TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
        final Template template = templatemanager.getTemplate(minecraftserver, WorldGenFossils.FOSSILS[i]);
        final Template template2 = templatemanager.getTemplate(minecraftserver, WorldGenFossils.FOSSILS_COAL[i]);
        final ChunkPos chunkpos = new ChunkPos(position);
        final StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
        final PlacementSettings placementsettings = new PlacementSettings().setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);
        final BlockPos blockpos = template.transformedSize(rotation);
        final int j = random.nextInt(16 - blockpos.getX());
        final int k = random.nextInt(16 - blockpos.getZ());
        int l = 256;
        for (int i2 = 0; i2 < blockpos.getX(); ++i2) {
            for (int j2 = 0; j2 < blockpos.getX(); ++j2) {
                l = Math.min(l, worldIn.getHeight(position.getX() + i2 + j, position.getZ() + j2 + k));
            }
        }
        final int k2 = Math.max(l - 15 - random.nextInt(10), 10);
        final BlockPos blockpos2 = template.getZeroPositionWithTransform(position.add(j, k2, k), Mirror.NONE, rotation);
        placementsettings.setIntegrity(0.9f);
        template.addBlocksToWorld(worldIn, blockpos2, placementsettings, 20);
        placementsettings.setIntegrity(0.1f);
        template2.addBlocksToWorld(worldIn, blockpos2, placementsettings, 20);
        return true;
    }
    
    static {
        STRUCTURE_SPINE_01 = new ResourceLocation("fossils/fossil_spine_01");
        STRUCTURE_SPINE_02 = new ResourceLocation("fossils/fossil_spine_02");
        STRUCTURE_SPINE_03 = new ResourceLocation("fossils/fossil_spine_03");
        STRUCTURE_SPINE_04 = new ResourceLocation("fossils/fossil_spine_04");
        STRUCTURE_SPINE_01_COAL = new ResourceLocation("fossils/fossil_spine_01_coal");
        STRUCTURE_SPINE_02_COAL = new ResourceLocation("fossils/fossil_spine_02_coal");
        STRUCTURE_SPINE_03_COAL = new ResourceLocation("fossils/fossil_spine_03_coal");
        STRUCTURE_SPINE_04_COAL = new ResourceLocation("fossils/fossil_spine_04_coal");
        STRUCTURE_SKULL_01 = new ResourceLocation("fossils/fossil_skull_01");
        STRUCTURE_SKULL_02 = new ResourceLocation("fossils/fossil_skull_02");
        STRUCTURE_SKULL_03 = new ResourceLocation("fossils/fossil_skull_03");
        STRUCTURE_SKULL_04 = new ResourceLocation("fossils/fossil_skull_04");
        STRUCTURE_SKULL_01_COAL = new ResourceLocation("fossils/fossil_skull_01_coal");
        STRUCTURE_SKULL_02_COAL = new ResourceLocation("fossils/fossil_skull_02_coal");
        STRUCTURE_SKULL_03_COAL = new ResourceLocation("fossils/fossil_skull_03_coal");
        STRUCTURE_SKULL_04_COAL = new ResourceLocation("fossils/fossil_skull_04_coal");
        FOSSILS = new ResourceLocation[] { WorldGenFossils.STRUCTURE_SPINE_01, WorldGenFossils.STRUCTURE_SPINE_02, WorldGenFossils.STRUCTURE_SPINE_03, WorldGenFossils.STRUCTURE_SPINE_04, WorldGenFossils.STRUCTURE_SKULL_01, WorldGenFossils.STRUCTURE_SKULL_02, WorldGenFossils.STRUCTURE_SKULL_03, WorldGenFossils.STRUCTURE_SKULL_04 };
        FOSSILS_COAL = new ResourceLocation[] { WorldGenFossils.STRUCTURE_SPINE_01_COAL, WorldGenFossils.STRUCTURE_SPINE_02_COAL, WorldGenFossils.STRUCTURE_SPINE_03_COAL, WorldGenFossils.STRUCTURE_SPINE_04_COAL, WorldGenFossils.STRUCTURE_SKULL_01_COAL, WorldGenFossils.STRUCTURE_SKULL_02_COAL, WorldGenFossils.STRUCTURE_SKULL_03_COAL, WorldGenFossils.STRUCTURE_SKULL_04_COAL };
    }
}
