/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureEndCityPieces {
    private static final PlacementSettings OVERWRITE = new PlacementSettings().setIgnoreEntities(true);
    private static final PlacementSettings INSERT = new PlacementSettings().setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    private static final IGenerator HOUSE_TOWER_GENERATOR = new IGenerator(){

        @Override
        public void init() {
        }

        @Override
        public boolean func_191086_a(TemplateManager p_191086_1_, int p_191086_2_, CityTemplate p_191086_3_, BlockPos p_191086_4_, List<StructureComponent> p_191086_5_, Random p_191086_6_) {
            if (p_191086_2_ > 8) {
                return false;
            }
            Rotation rotation = p_191086_3_.placeSettings.getRotation();
            CityTemplate structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, p_191086_3_, p_191086_4_, "base_floor", rotation, true));
            int i = p_191086_6_.nextInt(3);
            if (i == 0) {
                StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
            } else if (i == 1) {
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
                StructureEndCityPieces.func_191088_b(p_191086_1_, TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, null, p_191086_5_, p_191086_6_);
            } else if (i == 2) {
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "third_floor_c", rotation, false));
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
                StructureEndCityPieces.func_191088_b(p_191086_1_, TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, null, p_191086_5_, p_191086_6_);
            }
            return true;
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList(new Tuple<Rotation, BlockPos>(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple<Rotation, BlockPos>(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple<Rotation, BlockPos>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple<Rotation, BlockPos>(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6)));
    private static final IGenerator TOWER_GENERATOR = new IGenerator(){

        @Override
        public void init() {
        }

        @Override
        public boolean func_191086_a(TemplateManager p_191086_1_, int p_191086_2_, CityTemplate p_191086_3_, BlockPos p_191086_4_, List<StructureComponent> p_191086_5_, Random p_191086_6_) {
            Rotation rotation = p_191086_3_.placeSettings.getRotation();
            CityTemplate lvt_8_1_ = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, p_191086_3_, new BlockPos(3 + p_191086_6_.nextInt(2), -3, 3 + p_191086_6_.nextInt(2)), "tower_base", rotation, true));
            lvt_8_1_ = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, lvt_8_1_, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
            CityTemplate structureendcitypieces$citytemplate1 = p_191086_6_.nextInt(3) == 0 ? lvt_8_1_ : null;
            int i = 1 + p_191086_6_.nextInt(3);
            for (int j = 0; j < i; ++j) {
                lvt_8_1_ = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, lvt_8_1_, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
                if (j >= i - 1 || !p_191086_6_.nextBoolean()) continue;
                structureendcitypieces$citytemplate1 = lvt_8_1_;
            }
            if (structureendcitypieces$citytemplate1 != null) {
                for (Tuple tuple : TOWER_BRIDGES) {
                    if (!p_191086_6_.nextBoolean()) continue;
                    CityTemplate structureendcitypieces$citytemplate2 = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate1, (BlockPos)tuple.getSecond(), "bridge_end", rotation.add((Rotation)((Object)tuple.getFirst())), true));
                    StructureEndCityPieces.func_191088_b(p_191086_1_, TOWER_BRIDGE_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate2, null, p_191086_5_, p_191086_6_);
                }
                StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, lvt_8_1_, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
            } else {
                if (p_191086_2_ != 7) {
                    return StructureEndCityPieces.func_191088_b(p_191086_1_, FAT_TOWER_GENERATOR, p_191086_2_ + 1, lvt_8_1_, null, p_191086_5_, p_191086_6_);
                }
                StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, lvt_8_1_, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
            }
            return true;
        }
    };
    private static final IGenerator TOWER_BRIDGE_GENERATOR = new IGenerator(){
        public boolean shipCreated;

        @Override
        public void init() {
            this.shipCreated = false;
        }

        @Override
        public boolean func_191086_a(TemplateManager p_191086_1_, int p_191086_2_, CityTemplate p_191086_3_, BlockPos p_191086_4_, List<StructureComponent> p_191086_5_, Random p_191086_6_) {
            Rotation rotation = p_191086_3_.placeSettings.getRotation();
            int i = p_191086_6_.nextInt(4) + 1;
            CityTemplate structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, p_191086_3_, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
            structureendcitypieces$citytemplate.componentType = -1;
            int j = 0;
            for (int k = 0; k < i; ++k) {
                if (p_191086_6_.nextBoolean()) {
                    structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -4), "bridge_piece", rotation, true));
                    j = 0;
                    continue;
                }
                structureendcitypieces$citytemplate = p_191086_6_.nextBoolean() ? StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -4), "bridge_steep_stairs", rotation, true)) : StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, j, -8), "bridge_gentle_stairs", rotation, true));
                j = 4;
            }
            if (!this.shipCreated && p_191086_6_.nextInt(10 - p_191086_2_) == 0) {
                StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-8 + p_191086_6_.nextInt(8), j, -70 + p_191086_6_.nextInt(10)), "ship", rotation, true));
                this.shipCreated = true;
            } else if (!StructureEndCityPieces.func_191088_b(p_191086_1_, HOUSE_TOWER_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate, new BlockPos(-3, j + 1, -11), p_191086_5_, p_191086_6_)) {
                return false;
            }
            structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(4, j, 0), "bridge_end", rotation.add(Rotation.CLOCKWISE_180), true));
            structureendcitypieces$citytemplate.componentType = -1;
            return true;
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList(new Tuple<Rotation, BlockPos>(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple<Rotation, BlockPos>(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple<Rotation, BlockPos>(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple<Rotation, BlockPos>(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12)));
    private static final IGenerator FAT_TOWER_GENERATOR = new IGenerator(){

        @Override
        public void init() {
        }

        @Override
        public boolean func_191086_a(TemplateManager p_191086_1_, int p_191086_2_, CityTemplate p_191086_3_, BlockPos p_191086_4_, List<StructureComponent> p_191086_5_, Random p_191086_6_) {
            Rotation rotation = p_191086_3_.placeSettings.getRotation();
            CityTemplate structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, p_191086_3_, new BlockPos(-3, 4, -3), "fat_tower_base", rotation, true));
            structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, 4, 0), "fat_tower_middle", rotation, true));
            for (int i = 0; i < 2 && p_191086_6_.nextInt(3) != 0; ++i) {
                structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(0, 8, 0), "fat_tower_middle", rotation, true));
                for (Tuple tuple : FAT_TOWER_BRIDGES) {
                    if (!p_191086_6_.nextBoolean()) continue;
                    CityTemplate structureendcitypieces$citytemplate1 = StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, (BlockPos)tuple.getSecond(), "bridge_end", rotation.add((Rotation)((Object)tuple.getFirst())), true));
                    StructureEndCityPieces.func_191088_b(p_191086_1_, TOWER_BRIDGE_GENERATOR, p_191086_2_ + 1, structureendcitypieces$citytemplate1, null, p_191086_5_, p_191086_6_);
                }
            }
            StructureEndCityPieces.func_189935_b(p_191086_5_, StructureEndCityPieces.func_191090_b(p_191086_1_, structureendcitypieces$citytemplate, new BlockPos(-2, 8, -2), "fat_tower_top", rotation, true));
            return true;
        }
    };

    public static void registerPieces() {
        MapGenStructureIO.registerStructureComponent(CityTemplate.class, "ECP");
    }

    private static CityTemplate func_191090_b(TemplateManager p_191090_0_, CityTemplate p_191090_1_, BlockPos p_191090_2_, String p_191090_3_, Rotation p_191090_4_, boolean p_191090_5_) {
        CityTemplate structureendcitypieces$citytemplate = new CityTemplate(p_191090_0_, p_191090_3_, p_191090_1_.templatePosition, p_191090_4_, p_191090_5_);
        BlockPos blockpos = p_191090_1_.template.calculateConnectedPos(p_191090_1_.placeSettings, p_191090_2_, structureendcitypieces$citytemplate.placeSettings, BlockPos.ORIGIN);
        structureendcitypieces$citytemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        return structureendcitypieces$citytemplate;
    }

    public static void func_191087_a(TemplateManager p_191087_0_, BlockPos p_191087_1_, Rotation p_191087_2_, List<StructureComponent> p_191087_3_, Random p_191087_4_) {
        FAT_TOWER_GENERATOR.init();
        HOUSE_TOWER_GENERATOR.init();
        TOWER_BRIDGE_GENERATOR.init();
        TOWER_GENERATOR.init();
        CityTemplate structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191087_3_, new CityTemplate(p_191087_0_, "base_floor", p_191087_1_, p_191087_2_, true));
        structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191087_3_, StructureEndCityPieces.func_191090_b(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 0, -1), "second_floor", p_191087_2_, false));
        structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191087_3_, StructureEndCityPieces.func_191090_b(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 4, -1), "third_floor", p_191087_2_, false));
        structureendcitypieces$citytemplate = StructureEndCityPieces.func_189935_b(p_191087_3_, StructureEndCityPieces.func_191090_b(p_191087_0_, structureendcitypieces$citytemplate, new BlockPos(-1, 8, -1), "third_roof", p_191087_2_, true));
        StructureEndCityPieces.func_191088_b(p_191087_0_, TOWER_GENERATOR, 1, structureendcitypieces$citytemplate, null, p_191087_3_, p_191087_4_);
    }

    private static CityTemplate func_189935_b(List<StructureComponent> p_189935_0_, CityTemplate p_189935_1_) {
        p_189935_0_.add(p_189935_1_);
        return p_189935_1_;
    }

    private static boolean func_191088_b(TemplateManager p_191088_0_, IGenerator p_191088_1_, int p_191088_2_, CityTemplate p_191088_3_, BlockPos p_191088_4_, List<StructureComponent> p_191088_5_, Random p_191088_6_) {
        if (p_191088_2_ > 8) {
            return false;
        }
        ArrayList<StructureComponent> list = Lists.newArrayList();
        if (p_191088_1_.func_191086_a(p_191088_0_, p_191088_2_, p_191088_3_, p_191088_4_, list, p_191088_6_)) {
            boolean flag = false;
            int i = p_191088_6_.nextInt();
            for (StructureComponent structurecomponent : list) {
                structurecomponent.componentType = i;
                StructureComponent structurecomponent1 = StructureComponent.findIntersecting(p_191088_5_, structurecomponent.getBoundingBox());
                if (structurecomponent1 == null || structurecomponent1.componentType == p_191088_3_.componentType) continue;
                flag = true;
                break;
            }
            if (!flag) {
                p_191088_5_.addAll(list);
                return true;
            }
        }
        return false;
    }

    static interface IGenerator {
        public void init();

        public boolean func_191086_a(TemplateManager var1, int var2, CityTemplate var3, BlockPos var4, List<StructureComponent> var5, Random var6);
    }

    public static class CityTemplate
    extends StructureComponentTemplate {
        private String pieceName;
        private Rotation rotation;
        private boolean overwrite;

        public CityTemplate() {
        }

        public CityTemplate(TemplateManager p_i47214_1_, String p_i47214_2_, BlockPos p_i47214_3_, Rotation p_i47214_4_, boolean p_i47214_5_) {
            super(0);
            this.pieceName = p_i47214_2_;
            this.templatePosition = p_i47214_3_;
            this.rotation = p_i47214_4_;
            this.overwrite = p_i47214_5_;
            this.func_191085_a(p_i47214_1_);
        }

        private void func_191085_a(TemplateManager p_191085_1_) {
            Template template = p_191085_1_.getTemplate(null, new ResourceLocation("endcity/" + this.pieceName));
            PlacementSettings placementsettings = (this.overwrite ? OVERWRITE : INSERT).copy().setRotation(this.rotation);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.pieceName);
            tagCompound.setString("Rot", this.rotation.name());
            tagCompound.setBoolean("OW", this.overwrite);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.pieceName = tagCompound.getString("Template");
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.overwrite = tagCompound.getBoolean("OW");
            this.func_191085_a(p_143011_2_);
        }

        @Override
        protected void handleDataMarker(String p_186175_1_, BlockPos p_186175_2_, World p_186175_3_, Random p_186175_4_, StructureBoundingBox p_186175_5_) {
            if (p_186175_1_.startsWith("Chest")) {
                TileEntity tileentity;
                BlockPos blockpos = p_186175_2_.down();
                if (p_186175_5_.isVecInside(blockpos) && (tileentity = p_186175_3_.getTileEntity(blockpos)) instanceof TileEntityChest) {
                    ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, p_186175_4_.nextLong());
                }
            } else if (p_186175_1_.startsWith("Sentry")) {
                EntityShulker entityshulker = new EntityShulker(p_186175_3_);
                entityshulker.setPosition((double)p_186175_2_.getX() + 0.5, (double)p_186175_2_.getY() + 0.5, (double)p_186175_2_.getZ() + 0.5);
                entityshulker.setAttachmentPos(p_186175_2_);
                p_186175_3_.spawnEntityInWorld(entityshulker);
            } else if (p_186175_1_.startsWith("Elytra")) {
                EntityItemFrame entityitemframe = new EntityItemFrame(p_186175_3_, p_186175_2_, this.rotation.rotate(EnumFacing.SOUTH));
                entityitemframe.setDisplayedItem(new ItemStack(Items.ELYTRA));
                p_186175_3_.spawnEntityInWorld(entityitemframe);
            }
        }
    }
}

