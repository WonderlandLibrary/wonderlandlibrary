// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.util.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import java.util.List;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StructureEndCityPieces
{
    private static final PlacementSettings OVERWRITE;
    private static final PlacementSettings INSERT;
    private static final IGenerator HOUSE_TOWER_GENERATOR;
    private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES;
    private static final IGenerator TOWER_GENERATOR;
    private static final IGenerator TOWER_BRIDGE_GENERATOR;
    private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES;
    private static final IGenerator FAT_TOWER_GENERATOR;
    
    public static void registerPieces() {
        MapGenStructureIO.registerStructureComponent(CityTemplate.class, "ECP");
    }
    
    private static CityTemplate addPiece(final TemplateManager p_191090_0_, final CityTemplate p_191090_1_, final BlockPos p_191090_2_, final String p_191090_3_, final Rotation p_191090_4_, final boolean owerwrite) {
        final CityTemplate structureendcitypieces$citytemplate = new CityTemplate(p_191090_0_, p_191090_3_, p_191090_1_.templatePosition, p_191090_4_, owerwrite);
        final BlockPos blockpos = p_191090_1_.template.calculateConnectedPos(p_191090_1_.placeSettings, p_191090_2_, structureendcitypieces$citytemplate.placeSettings, BlockPos.ORIGIN);
        structureendcitypieces$citytemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        return structureendcitypieces$citytemplate;
    }
    
    public static void startHouseTower(final TemplateManager p_191087_0_, final BlockPos p_191087_1_, final Rotation p_191087_2_, final List<StructureComponent> p_191087_3_, final Random p_191087_4_) {
        StructureEndCityPieces.FAT_TOWER_GENERATOR.init();
        StructureEndCityPieces.HOUSE_TOWER_GENERATOR.init();
        StructureEndCityPieces.TOWER_BRIDGE_GENERATOR.init();
        StructureEndCityPieces.TOWER_GENERATOR.init();
        CityTemplate structureendcitypieces$citytemplate = addHelper(p_191087_3_, new CityTemplate(p_191087_0_, "base_floor", p_191087_1_, p_191087_2_, true));
        structureendcitypieces$citytemplate = addHelper(p_191087_3_, addPiece(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor", p_191087_2_, false));
        structureendcitypieces$citytemplate = addHelper(p_191087_3_, addPiece(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "third_floor", p_191087_2_, false));
        structureendcitypieces$citytemplate = addHelper(p_191087_3_, addPiece(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "third_roof", p_191087_2_, true));
        recursiveChildren(p_191087_0_, StructureEndCityPieces.TOWER_GENERATOR, 1, structureendcitypieces$citytemplate, null, p_191087_3_, p_191087_4_);
    }
    
    private static CityTemplate addHelper(final List<StructureComponent> p_189935_0_, final CityTemplate p_189935_1_) {
        p_189935_0_.add(p_189935_1_);
        return p_189935_1_;
    }
    
    private static boolean recursiveChildren(final TemplateManager p_191088_0_, final IGenerator p_191088_1_, final int p_191088_2_, final CityTemplate p_191088_3_, final BlockPos p_191088_4_, final List<StructureComponent> p_191088_5_, final Random p_191088_6_) {
        if (p_191088_2_ > 8) {
            return false;
        }
        final List<StructureComponent> list = (List<StructureComponent>)Lists.newArrayList();
        if (p_191088_1_.generate(p_191088_0_, p_191088_2_, p_191088_3_, p_191088_4_, list, p_191088_6_)) {
            boolean flag = false;
            final int i = p_191088_6_.nextInt();
            for (final StructureComponent structurecomponent : list) {
                structurecomponent.componentType = i;
                final StructureComponent structurecomponent2 = StructureComponent.findIntersecting(p_191088_5_, structurecomponent.getBoundingBox());
                if (structurecomponent2 != null && structurecomponent2.componentType != p_191088_3_.componentType) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                p_191088_5_.addAll(list);
                return true;
            }
        }
        return false;
    }
    
    static {
        OVERWRITE = new PlacementSettings().setIgnoreEntities(true);
        INSERT = new PlacementSettings().setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
        HOUSE_TOWER_GENERATOR = new IGenerator() {
            @Override
            public void init() {
            }
            
            @Override
            public boolean generate(final TemplateManager p_191086_1_, final int p_191086_2_, final CityTemplate p_191086_3_, final BlockPos p_191086_4_, final List<StructureComponent> p_191086_5_, final Random p_191086_6_) {
                if (p_191086_2_ > 8) {
                    return false;
                }
                final Rotation rotation = p_191086_3_.placeSettings.getRotation();
                CityTemplate structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, p_191086_3_, p_191086_4_, "base_floor", rotation, true));
                final int i = p_191086_6_.nextInt(3);
                if (i == 0) {
                    addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
                }
                else if (i == 1) {
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
                    recursiveChildren(p_191086_1_, StructureEndCityPieces.TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, null, p_191086_5_, p_191086_6_);
                }
                else if (i == 2) {
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "third_floor_c", rotation, false));
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
                    recursiveChildren(p_191086_1_, StructureEndCityPieces.TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, null, p_191086_5_, p_191086_6_);
                }
                return true;
            }
        };
        TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple((A)Rotation.NONE, (B)new BlockPos(1, -1, 0)), new Tuple((A)Rotation.CLOCKWISE_90, (B)new BlockPos(6, -1, 1)), new Tuple((A)Rotation.COUNTERCLOCKWISE_90, (B)new BlockPos(0, -1, 5)), new Tuple((A)Rotation.CLOCKWISE_180, (B)new BlockPos(5, -1, 6)) });
        TOWER_GENERATOR = new IGenerator() {
            @Override
            public void init() {
            }
            
            @Override
            public boolean generate(final TemplateManager p_191086_1_, final int p_191086_2_, final CityTemplate p_191086_3_, final BlockPos p_191086_4_, final List<StructureComponent> p_191086_5_, final Random p_191086_6_) {
                final Rotation rotation = p_191086_3_.placeSettings.getRotation();
                CityTemplate lvt_8_1_ = addHelper(p_191086_5_, addPiece(p_191086_1_, p_191086_3_, new BlockPos(3 + p_191086_6_.nextInt(2), -3, 3 + p_191086_6_.nextInt(2)), "tower_base", rotation, true));
                lvt_8_1_ = addHelper(p_191086_5_, addPiece(p_191086_1_, lvt_8_1_, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
                CityTemplate structureendcitypieces$citytemplate1 = (p_191086_6_.nextInt(3) == 0) ? lvt_8_1_ : null;
                for (int i = 1 + p_191086_6_.nextInt(3), j = 0; j < i; ++j) {
                    lvt_8_1_ = addHelper(p_191086_5_, addPiece(p_191086_1_, lvt_8_1_, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
                    if (j < i - 1 && p_191086_6_.nextBoolean()) {
                        structureendcitypieces$citytemplate1 = lvt_8_1_;
                    }
                }
                if (structureendcitypieces$citytemplate1 != null) {
                    for (final Tuple<Rotation, BlockPos> tuple : StructureEndCityPieces.TOWER_BRIDGES) {
                        if (p_191086_6_.nextBoolean()) {
                            final CityTemplate structureendcitypieces$citytemplate2 = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate1, tuple.getSecond(), "bridge_end", rotation.add(tuple.getFirst()), true));
                            recursiveChildren(p_191086_1_, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate2, null, p_191086_5_, p_191086_6_);
                        }
                    }
                    addHelper(p_191086_5_, addPiece(p_191086_1_, lvt_8_1_, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
                }
                else {
                    if (p_191086_2_ != 7) {
                        return recursiveChildren(p_191086_1_, StructureEndCityPieces.FAT_TOWER_GENERATOR, p_191086_2_ + 1, lvt_8_1_, null, p_191086_5_, p_191086_6_);
                    }
                    addHelper(p_191086_5_, addPiece(p_191086_1_, lvt_8_1_, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
                }
                return true;
            }
        };
        TOWER_BRIDGE_GENERATOR = new IGenerator() {
            public boolean shipCreated;
            
            @Override
            public void init() {
                this.shipCreated = false;
            }
            
            @Override
            public boolean generate(final TemplateManager p_191086_1_, final int p_191086_2_, final CityTemplate p_191086_3_, final BlockPos p_191086_4_, final List<StructureComponent> p_191086_5_, final Random p_191086_6_) {
                final Rotation rotation = p_191086_3_.placeSettings.getRotation();
                final int i = p_191086_6_.nextInt(4) + 1;
                CityTemplate structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, p_191086_3_, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
                structureendcitypieces$citytemplate.componentType = -1;
                int j = 0;
                for (int k = 0; k < i; ++k) {
                    if (p_191086_6_.nextBoolean()) {
                        structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -4), "bridge_piece", rotation, true));
                        j = 0;
                    }
                    else {
                        if (p_191086_6_.nextBoolean()) {
                            structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -4), "bridge_steep_stairs", rotation, true));
                        }
                        else {
                            structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -8), "bridge_gentle_stairs", rotation, true));
                        }
                        j = 4;
                    }
                }
                if (!this.shipCreated && p_191086_6_.nextInt(10 - p_191086_2_) == 0) {
                    addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-8 + p_191086_6_.nextInt(8), j, -70 + p_191086_6_.nextInt(10)), "ship", rotation, true));
                    this.shipCreated = true;
                }
                else if (!recursiveChildren(p_191086_1_, StructureEndCityPieces.HOUSE_TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, new BlockPos(-3, j + 1, -11), p_191086_5_, p_191086_6_)) {
                    return false;
                }
                structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(4, j, 0), "bridge_end", rotation.add(Rotation.CLOCKWISE_180), true));
                structureendcitypieces$citytemplate.componentType = -1;
                return true;
            }
        };
        FAT_TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple((A)Rotation.NONE, (B)new BlockPos(4, -1, 0)), new Tuple((A)Rotation.CLOCKWISE_90, (B)new BlockPos(12, -1, 4)), new Tuple((A)Rotation.COUNTERCLOCKWISE_90, (B)new BlockPos(0, -1, 8)), new Tuple((A)Rotation.CLOCKWISE_180, (B)new BlockPos(8, -1, 12)) });
        FAT_TOWER_GENERATOR = new IGenerator() {
            @Override
            public void init() {
            }
            
            @Override
            public boolean generate(final TemplateManager p_191086_1_, final int p_191086_2_, final CityTemplate p_191086_3_, final BlockPos p_191086_4_, final List<StructureComponent> p_191086_5_, final Random p_191086_6_) {
                final Rotation rotation = p_191086_3_.placeSettings.getRotation();
                CityTemplate structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, p_191086_3_, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true));
                structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true));
                for (int i = 0; i < 2 && p_191086_6_.nextInt(3) != 0; ++i) {
                    structureendcitypieces$citytemplate = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true));
                    for (final Tuple<Rotation, BlockPos> tuple : StructureEndCityPieces.FAT_TOWER_BRIDGES) {
                        if (p_191086_6_.nextBoolean()) {
                            final CityTemplate structureendcitypieces$citytemplate2 = addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, tuple.getSecond(), "bridge_end", rotation.add(tuple.getFirst()), true));
                            recursiveChildren(p_191086_1_, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate2, null, p_191086_5_, p_191086_6_);
                        }
                    }
                }
                addHelper(p_191086_5_, addPiece(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true));
                return true;
            }
        };
    }
    
    public static class CityTemplate extends StructureComponentTemplate
    {
        private String pieceName;
        private Rotation rotation;
        private boolean overwrite;
        
        public CityTemplate() {
        }
        
        public CityTemplate(final TemplateManager p_i47214_1_, final String p_i47214_2_, final BlockPos p_i47214_3_, final Rotation p_i47214_4_, final boolean overwriteIn) {
            super(0);
            this.pieceName = p_i47214_2_;
            this.templatePosition = p_i47214_3_;
            this.rotation = p_i47214_4_;
            this.overwrite = overwriteIn;
            this.loadTemplate(p_i47214_1_);
        }
        
        private void loadTemplate(final TemplateManager p_191085_1_) {
            final Template template = p_191085_1_.getTemplate(null, new ResourceLocation("endcity/" + this.pieceName));
            final PlacementSettings placementsettings = (this.overwrite ? StructureEndCityPieces.OVERWRITE : StructureEndCityPieces.INSERT).copy().setRotation(this.rotation);
            this.setup(template, this.templatePosition, placementsettings);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.pieceName);
            tagCompound.setString("Rot", this.rotation.name());
            tagCompound.setBoolean("OW", this.overwrite);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.pieceName = tagCompound.getString("Template");
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.overwrite = tagCompound.getBoolean("OW");
            this.loadTemplate(p_143011_2_);
        }
        
        @Override
        protected void handleDataMarker(final String function, final BlockPos pos, final World worldIn, final Random rand, final StructureBoundingBox sbb) {
            if (function.startsWith("Chest")) {
                final BlockPos blockpos = pos.down();
                if (sbb.isVecInside(blockpos)) {
                    final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                    if (tileentity instanceof TileEntityChest) {
                        ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, rand.nextLong());
                    }
                }
            }
            else if (function.startsWith("Sentry")) {
                final EntityShulker entityshulker = new EntityShulker(worldIn);
                entityshulker.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                entityshulker.setAttachmentPos(pos);
                worldIn.spawnEntity(entityshulker);
            }
            else if (function.startsWith("Elytra")) {
                final EntityItemFrame entityitemframe = new EntityItemFrame(worldIn, pos, this.rotation.rotate(EnumFacing.SOUTH));
                entityitemframe.setDisplayedItem(new ItemStack(Items.ELYTRA));
                worldIn.spawnEntity(entityitemframe);
            }
        }
    }
    
    interface IGenerator
    {
        void init();
        
        boolean generate(final TemplateManager p0, final int p1, final CityTemplate p2, final BlockPos p3, final List<StructureComponent> p4, final Random p5);
    }
}
