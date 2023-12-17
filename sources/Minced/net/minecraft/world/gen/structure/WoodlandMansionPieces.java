// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import java.util.Iterator;
import java.util.Collections;
import net.minecraft.util.Tuple;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import java.util.List;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WoodlandMansionPieces
{
    public static void registerWoodlandMansionPieces() {
        MapGenStructureIO.registerStructureComponent(MansionTemplate.class, "WMP");
    }
    
    public static void generateMansion(final TemplateManager p_191152_0_, final BlockPos p_191152_1_, final Rotation p_191152_2_, final List<MansionTemplate> p_191152_3_, final Random p_191152_4_) {
        final Grid woodlandmansionpieces$grid = new Grid(p_191152_4_);
        final Placer woodlandmansionpieces$placer = new Placer(p_191152_0_, p_191152_4_);
        woodlandmansionpieces$placer.createMansion(p_191152_1_, p_191152_2_, p_191152_3_, woodlandmansionpieces$grid);
    }
    
    static class FirstFloor extends RoomCollection
    {
        private FirstFloor() {
        }
        
        @Override
        public String get1x1(final Random p_191104_1_) {
            return "1x1_a" + (p_191104_1_.nextInt(5) + 1);
        }
        
        @Override
        public String get1x1Secret(final Random p_191099_1_) {
            return "1x1_as" + (p_191099_1_.nextInt(4) + 1);
        }
        
        @Override
        public String get1x2SideEntrance(final Random p_191100_1_, final boolean p_191100_2_) {
            return "1x2_a" + (p_191100_1_.nextInt(9) + 1);
        }
        
        @Override
        public String get1x2FrontEntrance(final Random p_191098_1_, final boolean p_191098_2_) {
            return "1x2_b" + (p_191098_1_.nextInt(5) + 1);
        }
        
        @Override
        public String get1x2Secret(final Random p_191102_1_) {
            return "1x2_s" + (p_191102_1_.nextInt(2) + 1);
        }
        
        @Override
        public String get2x2(final Random p_191101_1_) {
            return "2x2_a" + (p_191101_1_.nextInt(4) + 1);
        }
        
        @Override
        public String get2x2Secret(final Random p_191103_1_) {
            return "2x2_s1";
        }
    }
    
    static class Grid
    {
        private final Random random;
        private final SimpleGrid baseGrid;
        private final SimpleGrid thirdFloorGrid;
        private final SimpleGrid[] floorRooms;
        private final int entranceX;
        private final int entranceY;
        
        public Grid(final Random randomIn) {
            this.random = randomIn;
            final int i = 11;
            this.entranceX = 7;
            this.entranceY = 4;
            (this.baseGrid = new SimpleGrid(11, 11, 5)).set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
            this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
            this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
            this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
            this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
            this.baseGrid.set(0, 0, 11, 1, 5);
            this.baseGrid.set(0, 9, 11, 11, 5);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, EnumFacing.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, EnumFacing.WEST, 6);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, EnumFacing.WEST, 3);
            this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, EnumFacing.WEST, 3);
            while (this.cleanEdges(this.baseGrid)) {}
            (this.floorRooms = new SimpleGrid[3])[0] = new SimpleGrid(11, 11, 5);
            this.floorRooms[1] = new SimpleGrid(11, 11, 5);
            this.floorRooms[2] = new SimpleGrid(11, 11, 5);
            this.identifyRooms(this.baseGrid, this.floorRooms[0]);
            this.identifyRooms(this.baseGrid, this.floorRooms[1]);
            this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
            this.thirdFloorGrid = new SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
            this.setupThirdFloor();
            this.identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
        }
        
        public static boolean isHouse(final SimpleGrid p_191109_0_, final int p_191109_1_, final int p_191109_2_) {
            final int i = p_191109_0_.get(p_191109_1_, p_191109_2_);
            return i == 1 || i == 2 || i == 3 || i == 4;
        }
        
        public boolean isRoomId(final SimpleGrid p_191114_1_, final int p_191114_2_, final int p_191114_3_, final int p_191114_4_, final int p_191114_5_) {
            return (this.floorRooms[p_191114_4_].get(p_191114_2_, p_191114_3_) & 0xFFFF) == p_191114_5_;
        }
        
        @Nullable
        public EnumFacing get1x2RoomDirection(final SimpleGrid p_191113_1_, final int p_191113_2_, final int p_191113_3_, final int p_191113_4_, final int p_191113_5_) {
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                if (this.isRoomId(p_191113_1_, p_191113_2_ + enumfacing.getXOffset(), p_191113_3_ + enumfacing.getZOffset(), p_191113_4_, p_191113_5_)) {
                    return enumfacing;
                }
            }
            return null;
        }
        
        private void recursiveCorridor(final SimpleGrid p_191110_1_, final int p_191110_2_, final int p_191110_3_, final EnumFacing p_191110_4_, final int p_191110_5_) {
            if (p_191110_5_ > 0) {
                p_191110_1_.set(p_191110_2_, p_191110_3_, 1);
                p_191110_1_.setIf(p_191110_2_ + p_191110_4_.getXOffset(), p_191110_3_ + p_191110_4_.getZOffset(), 0, 1);
                for (int i = 0; i < 8; ++i) {
                    final EnumFacing enumfacing = EnumFacing.byHorizontalIndex(this.random.nextInt(4));
                    if (enumfacing != p_191110_4_.getOpposite() && (enumfacing != EnumFacing.EAST || !this.random.nextBoolean())) {
                        final int j = p_191110_2_ + p_191110_4_.getXOffset();
                        final int k = p_191110_3_ + p_191110_4_.getZOffset();
                        if (p_191110_1_.get(j + enumfacing.getXOffset(), k + enumfacing.getZOffset()) == 0 && p_191110_1_.get(j + enumfacing.getXOffset() * 2, k + enumfacing.getZOffset() * 2) == 0) {
                            this.recursiveCorridor(p_191110_1_, p_191110_2_ + p_191110_4_.getXOffset() + enumfacing.getXOffset(), p_191110_3_ + p_191110_4_.getZOffset() + enumfacing.getZOffset(), enumfacing, p_191110_5_ - 1);
                            break;
                        }
                    }
                }
                final EnumFacing enumfacing2 = p_191110_4_.rotateY();
                final EnumFacing enumfacing3 = p_191110_4_.rotateYCCW();
                p_191110_1_.setIf(p_191110_2_ + enumfacing2.getXOffset(), p_191110_3_ + enumfacing2.getZOffset(), 0, 2);
                p_191110_1_.setIf(p_191110_2_ + enumfacing3.getXOffset(), p_191110_3_ + enumfacing3.getZOffset(), 0, 2);
                p_191110_1_.setIf(p_191110_2_ + p_191110_4_.getXOffset() + enumfacing2.getXOffset(), p_191110_3_ + p_191110_4_.getZOffset() + enumfacing2.getZOffset(), 0, 2);
                p_191110_1_.setIf(p_191110_2_ + p_191110_4_.getXOffset() + enumfacing3.getXOffset(), p_191110_3_ + p_191110_4_.getZOffset() + enumfacing3.getZOffset(), 0, 2);
                p_191110_1_.setIf(p_191110_2_ + p_191110_4_.getXOffset() * 2, p_191110_3_ + p_191110_4_.getZOffset() * 2, 0, 2);
                p_191110_1_.setIf(p_191110_2_ + enumfacing2.getXOffset() * 2, p_191110_3_ + enumfacing2.getZOffset() * 2, 0, 2);
                p_191110_1_.setIf(p_191110_2_ + enumfacing3.getXOffset() * 2, p_191110_3_ + enumfacing3.getZOffset() * 2, 0, 2);
            }
        }
        
        private boolean cleanEdges(final SimpleGrid p_191111_1_) {
            boolean flag = false;
            for (int i = 0; i < p_191111_1_.height; ++i) {
                for (int j = 0; j < p_191111_1_.width; ++j) {
                    if (p_191111_1_.get(j, i) == 0) {
                        int k = 0;
                        k += (isHouse(p_191111_1_, j + 1, i) ? 1 : 0);
                        k += (isHouse(p_191111_1_, j - 1, i) ? 1 : 0);
                        k += (isHouse(p_191111_1_, j, i + 1) ? 1 : 0);
                        k += (isHouse(p_191111_1_, j, i - 1) ? 1 : 0);
                        if (k >= 3) {
                            p_191111_1_.set(j, i, 2);
                            flag = true;
                        }
                        else if (k == 2) {
                            int l = 0;
                            l += (isHouse(p_191111_1_, j + 1, i + 1) ? 1 : 0);
                            l += (isHouse(p_191111_1_, j - 1, i + 1) ? 1 : 0);
                            l += (isHouse(p_191111_1_, j + 1, i - 1) ? 1 : 0);
                            l += (isHouse(p_191111_1_, j - 1, i - 1) ? 1 : 0);
                            if (l <= 1) {
                                p_191111_1_.set(j, i, 2);
                                flag = true;
                            }
                        }
                    }
                }
            }
            return flag;
        }
        
        private void setupThirdFloor() {
            final List<Tuple<Integer, Integer>> list = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            final SimpleGrid woodlandmansionpieces$simplegrid = this.floorRooms[1];
            for (int i = 0; i < this.thirdFloorGrid.height; ++i) {
                for (int j = 0; j < this.thirdFloorGrid.width; ++j) {
                    final int k = woodlandmansionpieces$simplegrid.get(j, i);
                    final int l = k & 0xF0000;
                    if (l == 131072 && (k & 0x200000) == 0x200000) {
                        list.add(new Tuple<Integer, Integer>(j, i));
                    }
                }
            }
            if (list.isEmpty()) {
                this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
            }
            else {
                final Tuple<Integer, Integer> tuple = list.get(this.random.nextInt(list.size()));
                final int l2 = woodlandmansionpieces$simplegrid.get(tuple.getFirst(), tuple.getSecond());
                woodlandmansionpieces$simplegrid.set(tuple.getFirst(), tuple.getSecond(), l2 | 0x400000);
                final EnumFacing enumfacing1 = this.get1x2RoomDirection(this.baseGrid, tuple.getFirst(), tuple.getSecond(), 1, l2 & 0xFFFF);
                final int i2 = tuple.getFirst() + enumfacing1.getXOffset();
                final int i3 = tuple.getSecond() + enumfacing1.getZOffset();
                for (int j2 = 0; j2 < this.thirdFloorGrid.height; ++j2) {
                    for (int k2 = 0; k2 < this.thirdFloorGrid.width; ++k2) {
                        if (!isHouse(this.baseGrid, k2, j2)) {
                            this.thirdFloorGrid.set(k2, j2, 5);
                        }
                        else if (k2 == tuple.getFirst() && j2 == tuple.getSecond()) {
                            this.thirdFloorGrid.set(k2, j2, 3);
                        }
                        else if (k2 == i2 && j2 == i3) {
                            this.thirdFloorGrid.set(k2, j2, 3);
                            this.floorRooms[2].set(k2, j2, 8388608);
                        }
                    }
                }
                final List<EnumFacing> list2 = (List<EnumFacing>)Lists.newArrayList();
                for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL.facings()) {
                    if (this.thirdFloorGrid.get(i2 + enumfacing2.getXOffset(), i3 + enumfacing2.getZOffset()) == 0) {
                        list2.add(enumfacing2);
                    }
                }
                if (list2.isEmpty()) {
                    this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
                    woodlandmansionpieces$simplegrid.set(tuple.getFirst(), tuple.getSecond(), l2);
                }
                else {
                    final EnumFacing enumfacing3 = list2.get(this.random.nextInt(list2.size()));
                    this.recursiveCorridor(this.thirdFloorGrid, i2 + enumfacing3.getXOffset(), i3 + enumfacing3.getZOffset(), enumfacing3, 4);
                    while (this.cleanEdges(this.thirdFloorGrid)) {}
                }
            }
        }
        
        private void identifyRooms(final SimpleGrid p_191116_1_, final SimpleGrid p_191116_2_) {
            final List<Tuple<Integer, Integer>> list = (List<Tuple<Integer, Integer>>)Lists.newArrayList();
            for (int i = 0; i < p_191116_1_.height; ++i) {
                for (int j = 0; j < p_191116_1_.width; ++j) {
                    if (p_191116_1_.get(j, i) == 2) {
                        list.add(new Tuple<Integer, Integer>(j, i));
                    }
                }
            }
            Collections.shuffle(list, this.random);
            int k3 = 10;
            for (final Tuple<Integer, Integer> tuple : list) {
                final int l = tuple.getFirst();
                final int m = tuple.getSecond();
                if (p_191116_2_.get(l, m) == 0) {
                    int i2 = l;
                    int j2 = l;
                    int k4 = m;
                    int l2 = m;
                    int i3 = 65536;
                    if (p_191116_2_.get(l + 1, m) == 0 && p_191116_2_.get(l, m + 1) == 0 && p_191116_2_.get(l + 1, m + 1) == 0 && p_191116_1_.get(l + 1, m) == 2 && p_191116_1_.get(l, m + 1) == 2 && p_191116_1_.get(l + 1, m + 1) == 2) {
                        j2 = l + 1;
                        l2 = m + 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.get(l - 1, m) == 0 && p_191116_2_.get(l, m + 1) == 0 && p_191116_2_.get(l - 1, m + 1) == 0 && p_191116_1_.get(l - 1, m) == 2 && p_191116_1_.get(l, m + 1) == 2 && p_191116_1_.get(l - 1, m + 1) == 2) {
                        i2 = l - 1;
                        l2 = m + 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.get(l - 1, m) == 0 && p_191116_2_.get(l, m - 1) == 0 && p_191116_2_.get(l - 1, m - 1) == 0 && p_191116_1_.get(l - 1, m) == 2 && p_191116_1_.get(l, m - 1) == 2 && p_191116_1_.get(l - 1, m - 1) == 2) {
                        i2 = l - 1;
                        k4 = m - 1;
                        i3 = 262144;
                    }
                    else if (p_191116_2_.get(l + 1, m) == 0 && p_191116_1_.get(l + 1, m) == 2) {
                        j2 = l + 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.get(l, m + 1) == 0 && p_191116_1_.get(l, m + 1) == 2) {
                        l2 = m + 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.get(l - 1, m) == 0 && p_191116_1_.get(l - 1, m) == 2) {
                        i2 = l - 1;
                        i3 = 131072;
                    }
                    else if (p_191116_2_.get(l, m - 1) == 0 && p_191116_1_.get(l, m - 1) == 2) {
                        k4 = m - 1;
                        i3 = 131072;
                    }
                    int j3 = this.random.nextBoolean() ? i2 : j2;
                    int k5 = this.random.nextBoolean() ? k4 : l2;
                    int l3 = 2097152;
                    if (!p_191116_1_.edgesTo(j3, k5, 1)) {
                        j3 = ((j3 == i2) ? j2 : i2);
                        k5 = ((k5 == k4) ? l2 : k4);
                        if (!p_191116_1_.edgesTo(j3, k5, 1)) {
                            k5 = ((k5 == k4) ? l2 : k4);
                            if (!p_191116_1_.edgesTo(j3, k5, 1)) {
                                j3 = ((j3 == i2) ? j2 : i2);
                                k5 = ((k5 == k4) ? l2 : k4);
                                if (!p_191116_1_.edgesTo(j3, k5, 1)) {
                                    l3 = 0;
                                    j3 = i2;
                                    k5 = k4;
                                }
                            }
                        }
                    }
                    for (int i4 = k4; i4 <= l2; ++i4) {
                        for (int j4 = i2; j4 <= j2; ++j4) {
                            if (j4 == j3 && i4 == k5) {
                                p_191116_2_.set(j4, i4, 0x100000 | l3 | i3 | k3);
                            }
                            else {
                                p_191116_2_.set(j4, i4, i3 | k3);
                            }
                        }
                    }
                    ++k3;
                }
            }
        }
    }
    
    public static class MansionTemplate extends StructureComponentTemplate
    {
        private String templateName;
        private Rotation rotation;
        private Mirror mirror;
        
        public MansionTemplate() {
        }
        
        public MansionTemplate(final TemplateManager p_i47355_1_, final String p_i47355_2_, final BlockPos p_i47355_3_, final Rotation p_i47355_4_) {
            this(p_i47355_1_, p_i47355_2_, p_i47355_3_, p_i47355_4_, Mirror.NONE);
        }
        
        public MansionTemplate(final TemplateManager p_i47356_1_, final String p_i47356_2_, final BlockPos p_i47356_3_, final Rotation p_i47356_4_, final Mirror p_i47356_5_) {
            super(0);
            this.templateName = p_i47356_2_;
            this.templatePosition = p_i47356_3_;
            this.rotation = p_i47356_4_;
            this.mirror = p_i47356_5_;
            this.loadTemplate(p_i47356_1_);
        }
        
        private void loadTemplate(final TemplateManager p_191081_1_) {
            final Template template = p_191081_1_.getTemplate(null, new ResourceLocation("mansion/" + this.templateName));
            final PlacementSettings placementsettings = new PlacementSettings().setIgnoreEntities(true).setRotation(this.rotation).setMirror(this.mirror);
            this.setup(template, this.templatePosition, placementsettings);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.templateName);
            tagCompound.setString("Rot", this.placeSettings.getRotation().name());
            tagCompound.setString("Mi", this.placeSettings.getMirror().name());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound, final TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.templateName = tagCompound.getString("Template");
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.mirror = Mirror.valueOf(tagCompound.getString("Mi"));
            this.loadTemplate(p_143011_2_);
        }
        
        @Override
        protected void handleDataMarker(final String function, final BlockPos pos, final World worldIn, final Random rand, final StructureBoundingBox sbb) {
            if (function.startsWith("Chest")) {
                final Rotation rotation = this.placeSettings.getRotation();
                IBlockState iblockstate = Blocks.CHEST.getDefaultState();
                if ("ChestWest".equals(function)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.WEST));
                }
                else if ("ChestEast".equals(function)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.EAST));
                }
                else if ("ChestSouth".equals(function)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.SOUTH));
                }
                else if ("ChestNorth".equals(function)) {
                    iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockChest.FACING, rotation.rotate(EnumFacing.NORTH));
                }
                this.generateChest(worldIn, sbb, rand, pos, LootTableList.CHESTS_WOODLAND_MANSION, iblockstate);
            }
            else if ("Mage".equals(function)) {
                final EntityEvoker entityevoker = new EntityEvoker(worldIn);
                entityevoker.enablePersistence();
                entityevoker.moveToBlockPosAndAngles(pos, 0.0f, 0.0f);
                worldIn.spawnEntity(entityevoker);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
            else if ("Warrior".equals(function)) {
                final EntityVindicator entityvindicator = new EntityVindicator(worldIn);
                entityvindicator.enablePersistence();
                entityvindicator.moveToBlockPosAndAngles(pos, 0.0f, 0.0f);
                entityvindicator.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvindicator)), null);
                worldIn.spawnEntity(entityvindicator);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
        }
    }
    
    static class PlacementData
    {
        public Rotation rotation;
        public BlockPos position;
        public String wallType;
        
        private PlacementData() {
        }
    }
    
    static class Placer
    {
        private final TemplateManager templateManager;
        private final Random random;
        private int startX;
        private int startY;
        
        public Placer(final TemplateManager p_i47361_1_, final Random p_i47361_2_) {
            this.templateManager = p_i47361_1_;
            this.random = p_i47361_2_;
        }
        
        public void createMansion(final BlockPos p_191125_1_, final Rotation p_191125_2_, final List<MansionTemplate> p_191125_3_, final Grid p_191125_4_) {
            final PlacementData woodlandmansionpieces$placementdata = new PlacementData();
            woodlandmansionpieces$placementdata.position = p_191125_1_;
            woodlandmansionpieces$placementdata.rotation = p_191125_2_;
            woodlandmansionpieces$placementdata.wallType = "wall_flat";
            final PlacementData woodlandmansionpieces$placementdata2 = new PlacementData();
            this.entrance(p_191125_3_, woodlandmansionpieces$placementdata);
            woodlandmansionpieces$placementdata2.position = woodlandmansionpieces$placementdata.position.up(8);
            woodlandmansionpieces$placementdata2.rotation = woodlandmansionpieces$placementdata.rotation;
            woodlandmansionpieces$placementdata2.wallType = "wall_window";
            if (!p_191125_3_.isEmpty()) {}
            final SimpleGrid woodlandmansionpieces$simplegrid = p_191125_4_.baseGrid;
            final SimpleGrid woodlandmansionpieces$simplegrid2 = p_191125_4_.thirdFloorGrid;
            this.startX = p_191125_4_.entranceX + 1;
            this.startY = p_191125_4_.entranceY + 1;
            final int i = p_191125_4_.entranceX + 1;
            final int j = p_191125_4_.entranceY;
            this.traverseOuterWalls(p_191125_3_, woodlandmansionpieces$placementdata, woodlandmansionpieces$simplegrid, EnumFacing.SOUTH, this.startX, this.startY, i, j);
            this.traverseOuterWalls(p_191125_3_, woodlandmansionpieces$placementdata2, woodlandmansionpieces$simplegrid, EnumFacing.SOUTH, this.startX, this.startY, i, j);
            final PlacementData woodlandmansionpieces$placementdata3 = new PlacementData();
            woodlandmansionpieces$placementdata3.position = woodlandmansionpieces$placementdata.position.up(19);
            woodlandmansionpieces$placementdata3.rotation = woodlandmansionpieces$placementdata.rotation;
            woodlandmansionpieces$placementdata3.wallType = "wall_window";
            boolean flag = false;
            for (int k = 0; k < woodlandmansionpieces$simplegrid2.height && !flag; ++k) {
                for (int l = woodlandmansionpieces$simplegrid2.width - 1; l >= 0 && !flag; --l) {
                    if (Grid.isHouse(woodlandmansionpieces$simplegrid2, l, k)) {
                        woodlandmansionpieces$placementdata3.position = woodlandmansionpieces$placementdata3.position.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (k - this.startY) * 8);
                        woodlandmansionpieces$placementdata3.position = woodlandmansionpieces$placementdata3.position.offset(p_191125_2_.rotate(EnumFacing.EAST), (l - this.startX) * 8);
                        this.traverseWallPiece(p_191125_3_, woodlandmansionpieces$placementdata3);
                        this.traverseOuterWalls(p_191125_3_, woodlandmansionpieces$placementdata3, woodlandmansionpieces$simplegrid2, EnumFacing.SOUTH, l, k, l, k);
                        flag = true;
                    }
                }
            }
            this.createRoof(p_191125_3_, p_191125_1_.up(16), p_191125_2_, woodlandmansionpieces$simplegrid, woodlandmansionpieces$simplegrid2);
            this.createRoof(p_191125_3_, p_191125_1_.up(27), p_191125_2_, woodlandmansionpieces$simplegrid2, null);
            if (!p_191125_3_.isEmpty()) {}
            final RoomCollection[] awoodlandmansionpieces$roomcollection = { new FirstFloor(), new SecondFloor(), new ThirdFloor() };
            for (int l2 = 0; l2 < 3; ++l2) {
                final BlockPos blockpos = p_191125_1_.up(8 * l2 + ((l2 == 2) ? 3 : 0));
                final SimpleGrid woodlandmansionpieces$simplegrid3 = p_191125_4_.floorRooms[l2];
                final SimpleGrid woodlandmansionpieces$simplegrid4 = (l2 == 2) ? woodlandmansionpieces$simplegrid2 : woodlandmansionpieces$simplegrid;
                final String s = (l2 == 0) ? "carpet_south" : "carpet_south_2";
                final String s2 = (l2 == 0) ? "carpet_west" : "carpet_west_2";
                for (int i2 = 0; i2 < woodlandmansionpieces$simplegrid4.height; ++i2) {
                    for (int j2 = 0; j2 < woodlandmansionpieces$simplegrid4.width; ++j2) {
                        if (woodlandmansionpieces$simplegrid4.get(j2, i2) == 1) {
                            BlockPos blockpos2 = blockpos.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (i2 - this.startY) * 8);
                            blockpos2 = blockpos2.offset(p_191125_2_.rotate(EnumFacing.EAST), (j2 - this.startX) * 8);
                            p_191125_3_.add(new MansionTemplate(this.templateManager, "corridor_floor", blockpos2, p_191125_2_));
                            if (woodlandmansionpieces$simplegrid4.get(j2, i2 - 1) == 1 || (woodlandmansionpieces$simplegrid3.get(j2, i2 - 1) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.templateManager, "carpet_north", blockpos2.offset(p_191125_2_.rotate(EnumFacing.EAST), 1).up(), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.get(j2 + 1, i2) == 1 || (woodlandmansionpieces$simplegrid3.get(j2 + 1, i2) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.templateManager, "carpet_east", blockpos2.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 1).offset(p_191125_2_.rotate(EnumFacing.EAST), 5).up(), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.get(j2, i2 + 1) == 1 || (woodlandmansionpieces$simplegrid3.get(j2, i2 + 1) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.templateManager, s, blockpos2.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 5).offset(p_191125_2_.rotate(EnumFacing.WEST), 1), p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.get(j2 - 1, i2) == 1 || (woodlandmansionpieces$simplegrid3.get(j2 - 1, i2) & 0x800000) == 0x800000) {
                                p_191125_3_.add(new MansionTemplate(this.templateManager, s2, blockpos2.offset(p_191125_2_.rotate(EnumFacing.WEST), 1).offset(p_191125_2_.rotate(EnumFacing.NORTH), 1), p_191125_2_));
                            }
                        }
                    }
                }
                final String s3 = (l2 == 0) ? "indoors_wall" : "indoors_wall_2";
                final String s4 = (l2 == 0) ? "indoors_door" : "indoors_door_2";
                final List<EnumFacing> list = (List<EnumFacing>)Lists.newArrayList();
                for (int k2 = 0; k2 < woodlandmansionpieces$simplegrid4.height; ++k2) {
                    for (int l3 = 0; l3 < woodlandmansionpieces$simplegrid4.width; ++l3) {
                        boolean flag2 = l2 == 2 && woodlandmansionpieces$simplegrid4.get(l3, k2) == 3;
                        if (woodlandmansionpieces$simplegrid4.get(l3, k2) == 2 || flag2) {
                            final int i3 = woodlandmansionpieces$simplegrid3.get(l3, k2);
                            final int j3 = i3 & 0xF0000;
                            final int k3 = i3 & 0xFFFF;
                            flag2 = (flag2 && (i3 & 0x800000) == 0x800000);
                            list.clear();
                            if ((i3 & 0x200000) == 0x200000) {
                                for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                                    if (woodlandmansionpieces$simplegrid4.get(l3 + enumfacing.getXOffset(), k2 + enumfacing.getZOffset()) == 1) {
                                        list.add(enumfacing);
                                    }
                                }
                            }
                            EnumFacing enumfacing2 = null;
                            if (!list.isEmpty()) {
                                enumfacing2 = list.get(this.random.nextInt(list.size()));
                            }
                            else if ((i3 & 0x100000) == 0x100000) {
                                enumfacing2 = EnumFacing.UP;
                            }
                            BlockPos blockpos3 = blockpos.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 8 + (k2 - this.startY) * 8);
                            blockpos3 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.EAST), -1 + (l3 - this.startX) * 8);
                            if (Grid.isHouse(woodlandmansionpieces$simplegrid4, l3 - 1, k2) && !p_191125_4_.isRoomId(woodlandmansionpieces$simplegrid4, l3 - 1, k2, l2, k3)) {
                                p_191125_3_.add(new MansionTemplate(this.templateManager, (enumfacing2 == EnumFacing.WEST) ? s4 : s3, blockpos3, p_191125_2_));
                            }
                            if (woodlandmansionpieces$simplegrid4.get(l3 + 1, k2) == 1 && !flag2) {
                                final BlockPos blockpos4 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.EAST), 8);
                                p_191125_3_.add(new MansionTemplate(this.templateManager, (enumfacing2 == EnumFacing.EAST) ? s4 : s3, blockpos4, p_191125_2_));
                            }
                            if (Grid.isHouse(woodlandmansionpieces$simplegrid4, l3, k2 + 1) && !p_191125_4_.isRoomId(woodlandmansionpieces$simplegrid4, l3, k2 + 1, l2, k3)) {
                                BlockPos blockpos5 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.SOUTH), 7);
                                blockpos5 = blockpos5.offset(p_191125_2_.rotate(EnumFacing.EAST), 7);
                                p_191125_3_.add(new MansionTemplate(this.templateManager, (enumfacing2 == EnumFacing.SOUTH) ? s4 : s3, blockpos5, p_191125_2_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (woodlandmansionpieces$simplegrid4.get(l3, k2 - 1) == 1 && !flag2) {
                                BlockPos blockpos6 = blockpos3.offset(p_191125_2_.rotate(EnumFacing.NORTH), 1);
                                blockpos6 = blockpos6.offset(p_191125_2_.rotate(EnumFacing.EAST), 7);
                                p_191125_3_.add(new MansionTemplate(this.templateManager, (enumfacing2 == EnumFacing.NORTH) ? s4 : s3, blockpos6, p_191125_2_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (j3 == 65536) {
                                this.addRoom1x1(p_191125_3_, blockpos3, p_191125_2_, enumfacing2, awoodlandmansionpieces$roomcollection[l2]);
                            }
                            else if (j3 == 131072 && enumfacing2 != null) {
                                final EnumFacing enumfacing3 = p_191125_4_.get1x2RoomDirection(woodlandmansionpieces$simplegrid4, l3, k2, l2, k3);
                                final boolean flag3 = (i3 & 0x400000) == 0x400000;
                                this.addRoom1x2(p_191125_3_, blockpos3, p_191125_2_, enumfacing3, enumfacing2, awoodlandmansionpieces$roomcollection[l2], flag3);
                            }
                            else if (j3 == 262144 && enumfacing2 != null && enumfacing2 != EnumFacing.UP) {
                                EnumFacing enumfacing4 = enumfacing2.rotateY();
                                if (!p_191125_4_.isRoomId(woodlandmansionpieces$simplegrid4, l3 + enumfacing4.getXOffset(), k2 + enumfacing4.getZOffset(), l2, k3)) {
                                    enumfacing4 = enumfacing4.getOpposite();
                                }
                                this.addRoom2x2(p_191125_3_, blockpos3, p_191125_2_, enumfacing4, enumfacing2, awoodlandmansionpieces$roomcollection[l2]);
                            }
                            else if (j3 == 262144 && enumfacing2 == EnumFacing.UP) {
                                this.addRoom2x2Secret(p_191125_3_, blockpos3, p_191125_2_, awoodlandmansionpieces$roomcollection[l2]);
                            }
                        }
                    }
                }
            }
        }
        
        private void traverseOuterWalls(final List<MansionTemplate> p_191130_1_, final PlacementData p_191130_2_, final SimpleGrid p_191130_3_, EnumFacing p_191130_4_, final int p_191130_5_, final int p_191130_6_, final int p_191130_7_, final int p_191130_8_) {
            int i = p_191130_5_;
            int j = p_191130_6_;
            final EnumFacing enumfacing = p_191130_4_;
            do {
                if (!Grid.isHouse(p_191130_3_, i + p_191130_4_.getXOffset(), j + p_191130_4_.getZOffset())) {
                    this.traverseTurn(p_191130_1_, p_191130_2_);
                    p_191130_4_ = p_191130_4_.rotateY();
                    if (i == p_191130_7_ && j == p_191130_8_ && enumfacing == p_191130_4_) {
                        continue;
                    }
                    this.traverseWallPiece(p_191130_1_, p_191130_2_);
                }
                else if (Grid.isHouse(p_191130_3_, i + p_191130_4_.getXOffset(), j + p_191130_4_.getZOffset()) && Grid.isHouse(p_191130_3_, i + p_191130_4_.getXOffset() + p_191130_4_.rotateYCCW().getXOffset(), j + p_191130_4_.getZOffset() + p_191130_4_.rotateYCCW().getZOffset())) {
                    this.traverseInnerTurn(p_191130_1_, p_191130_2_);
                    i += p_191130_4_.getXOffset();
                    j += p_191130_4_.getZOffset();
                    p_191130_4_ = p_191130_4_.rotateYCCW();
                }
                else {
                    i += p_191130_4_.getXOffset();
                    j += p_191130_4_.getZOffset();
                    if (i == p_191130_7_ && j == p_191130_8_ && enumfacing == p_191130_4_) {
                        continue;
                    }
                    this.traverseWallPiece(p_191130_1_, p_191130_2_);
                }
            } while (i != p_191130_7_ || j != p_191130_8_ || enumfacing != p_191130_4_);
        }
        
        private void createRoof(final List<MansionTemplate> p_191123_1_, final BlockPos p_191123_2_, final Rotation p_191123_3_, final SimpleGrid p_191123_4_, @Nullable final SimpleGrid p_191123_5_) {
            for (int i = 0; i < p_191123_4_.height; ++i) {
                for (int j = 0; j < p_191123_4_.width; ++j) {
                    BlockPos lvt_8_3_ = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (i - this.startY) * 8);
                    lvt_8_3_ = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), (j - this.startX) * 8);
                    final boolean flag = p_191123_5_ != null && Grid.isHouse(p_191123_5_, j, i);
                    if (Grid.isHouse(p_191123_4_, j, i) && !flag) {
                        p_191123_1_.add(new MansionTemplate(this.templateManager, "roof", lvt_8_3_.up(3), p_191123_3_));
                        if (!Grid.isHouse(p_191123_4_, j + 1, i)) {
                            final BlockPos blockpos1 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_front", blockpos1, p_191123_3_));
                        }
                        if (!Grid.isHouse(p_191123_4_, j - 1, i)) {
                            BlockPos blockpos2 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 0);
                            blockpos2 = blockpos2.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                            p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_front", blockpos2, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                        }
                        if (!Grid.isHouse(p_191123_4_, j, i - 1)) {
                            final BlockPos blockpos3 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                            p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_front", blockpos3, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                        }
                        if (!Grid.isHouse(p_191123_4_, j, i + 1)) {
                            BlockPos blockpos4 = lvt_8_3_.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            blockpos4 = blockpos4.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                            p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_front", blockpos4, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                        }
                    }
                }
            }
            if (p_191123_5_ != null) {
                for (int k = 0; k < p_191123_4_.height; ++k) {
                    for (int i2 = 0; i2 < p_191123_4_.width; ++i2) {
                        BlockPos blockpos5 = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (k - this.startY) * 8);
                        blockpos5 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), (i2 - this.startX) * 8);
                        final boolean flag2 = Grid.isHouse(p_191123_5_, i2, k);
                        if (Grid.isHouse(p_191123_4_, i2, k) && flag2) {
                            if (!Grid.isHouse(p_191123_4_, i2 + 1, k)) {
                                final BlockPos blockpos6 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 7);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall", blockpos6, p_191123_3_));
                            }
                            if (!Grid.isHouse(p_191123_4_, i2 - 1, k)) {
                                BlockPos blockpos7 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                                blockpos7 = blockpos7.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall", blockpos7, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                            if (!Grid.isHouse(p_191123_4_, i2, k - 1)) {
                                BlockPos blockpos8 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 0);
                                blockpos8 = blockpos8.offset(p_191123_3_.rotate(EnumFacing.NORTH), 1);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall", blockpos8, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!Grid.isHouse(p_191123_4_, i2, k + 1)) {
                                BlockPos blockpos9 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                                blockpos9 = blockpos9.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall", blockpos9, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                            if (!Grid.isHouse(p_191123_4_, i2 + 1, k)) {
                                if (!Grid.isHouse(p_191123_4_, i2, k - 1)) {
                                    BlockPos blockpos10 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 7);
                                    blockpos10 = blockpos10.offset(p_191123_3_.rotate(EnumFacing.NORTH), 2);
                                    p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall_corner", blockpos10, p_191123_3_));
                                }
                                if (!Grid.isHouse(p_191123_4_, i2, k + 1)) {
                                    BlockPos blockpos11 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.EAST), 8);
                                    blockpos11 = blockpos11.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 7);
                                    p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall_corner", blockpos11, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                                }
                            }
                            if (!Grid.isHouse(p_191123_4_, i2 - 1, k)) {
                                if (!Grid.isHouse(p_191123_4_, i2, k - 1)) {
                                    BlockPos blockpos12 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 2);
                                    blockpos12 = blockpos12.offset(p_191123_3_.rotate(EnumFacing.NORTH), 1);
                                    p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall_corner", blockpos12, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                                }
                                if (!Grid.isHouse(p_191123_4_, i2, k + 1)) {
                                    BlockPos blockpos13 = blockpos5.offset(p_191123_3_.rotate(EnumFacing.WEST), 1);
                                    blockpos13 = blockpos13.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8);
                                    p_191123_1_.add(new MansionTemplate(this.templateManager, "small_wall_corner", blockpos13, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                                }
                            }
                        }
                    }
                }
            }
            for (int l = 0; l < p_191123_4_.height; ++l) {
                for (int j2 = 0; j2 < p_191123_4_.width; ++j2) {
                    BlockPos blockpos14 = p_191123_2_.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8 + (l - this.startY) * 8);
                    blockpos14 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), (j2 - this.startX) * 8);
                    final boolean flag3 = p_191123_5_ != null && Grid.isHouse(p_191123_5_, j2, l);
                    if (Grid.isHouse(p_191123_4_, j2, l) && !flag3) {
                        if (!Grid.isHouse(p_191123_4_, j2 + 1, l)) {
                            final BlockPos blockpos15 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 6);
                            if (!Grid.isHouse(p_191123_4_, j2, l + 1)) {
                                final BlockPos blockpos16 = blockpos15.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_corner", blockpos16, p_191123_3_));
                            }
                            else if (Grid.isHouse(p_191123_4_, j2 + 1, l + 1)) {
                                final BlockPos blockpos17 = blockpos15.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 5);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_inner_corner", blockpos17, p_191123_3_));
                            }
                            if (!Grid.isHouse(p_191123_4_, j2, l - 1)) {
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_corner", blockpos15, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            else if (Grid.isHouse(p_191123_4_, j2 + 1, l - 1)) {
                                BlockPos blockpos18 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 9);
                                blockpos18 = blockpos18.offset(p_191123_3_.rotate(EnumFacing.NORTH), 2);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_inner_corner", blockpos18, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                        }
                        if (!Grid.isHouse(p_191123_4_, j2 - 1, l)) {
                            BlockPos blockpos19 = blockpos14.offset(p_191123_3_.rotate(EnumFacing.EAST), 0);
                            blockpos19 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 0);
                            if (!Grid.isHouse(p_191123_4_, j2, l + 1)) {
                                final BlockPos blockpos20 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 6);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_corner", blockpos20, p_191123_3_.add(Rotation.CLOCKWISE_90)));
                            }
                            else if (Grid.isHouse(p_191123_4_, j2 - 1, l + 1)) {
                                BlockPos blockpos21 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 8);
                                blockpos21 = blockpos21.offset(p_191123_3_.rotate(EnumFacing.WEST), 3);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_inner_corner", blockpos21, p_191123_3_.add(Rotation.COUNTERCLOCKWISE_90)));
                            }
                            if (!Grid.isHouse(p_191123_4_, j2, l - 1)) {
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_corner", blockpos19, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                            else if (Grid.isHouse(p_191123_4_, j2 - 1, l - 1)) {
                                final BlockPos blockpos22 = blockpos19.offset(p_191123_3_.rotate(EnumFacing.SOUTH), 1);
                                p_191123_1_.add(new MansionTemplate(this.templateManager, "roof_inner_corner", blockpos22, p_191123_3_.add(Rotation.CLOCKWISE_180)));
                            }
                        }
                    }
                }
            }
        }
        
        private void entrance(final List<MansionTemplate> p_191133_1_, final PlacementData p_191133_2_) {
            final EnumFacing enumfacing = p_191133_2_.rotation.rotate(EnumFacing.WEST);
            p_191133_1_.add(new MansionTemplate(this.templateManager, "entrance", p_191133_2_.position.offset(enumfacing, 9), p_191133_2_.rotation));
            p_191133_2_.position = p_191133_2_.position.offset(p_191133_2_.rotation.rotate(EnumFacing.SOUTH), 16);
        }
        
        private void traverseWallPiece(final List<MansionTemplate> p_191131_1_, final PlacementData p_191131_2_) {
            p_191131_1_.add(new MansionTemplate(this.templateManager, p_191131_2_.wallType, p_191131_2_.position.offset(p_191131_2_.rotation.rotate(EnumFacing.EAST), 7), p_191131_2_.rotation));
            p_191131_2_.position = p_191131_2_.position.offset(p_191131_2_.rotation.rotate(EnumFacing.SOUTH), 8);
        }
        
        private void traverseTurn(final List<MansionTemplate> p_191124_1_, final PlacementData p_191124_2_) {
            p_191124_2_.position = p_191124_2_.position.offset(p_191124_2_.rotation.rotate(EnumFacing.SOUTH), -1);
            p_191124_1_.add(new MansionTemplate(this.templateManager, "wall_corner", p_191124_2_.position, p_191124_2_.rotation));
            p_191124_2_.position = p_191124_2_.position.offset(p_191124_2_.rotation.rotate(EnumFacing.SOUTH), -7);
            p_191124_2_.position = p_191124_2_.position.offset(p_191124_2_.rotation.rotate(EnumFacing.WEST), -6);
            p_191124_2_.rotation = p_191124_2_.rotation.add(Rotation.CLOCKWISE_90);
        }
        
        private void traverseInnerTurn(final List<MansionTemplate> p_191126_1_, final PlacementData p_191126_2_) {
            p_191126_2_.position = p_191126_2_.position.offset(p_191126_2_.rotation.rotate(EnumFacing.SOUTH), 6);
            p_191126_2_.position = p_191126_2_.position.offset(p_191126_2_.rotation.rotate(EnumFacing.EAST), 8);
            p_191126_2_.rotation = p_191126_2_.rotation.add(Rotation.COUNTERCLOCKWISE_90);
        }
        
        private void addRoom1x1(final List<MansionTemplate> p_191129_1_, final BlockPos p_191129_2_, final Rotation p_191129_3_, final EnumFacing p_191129_4_, final RoomCollection p_191129_5_) {
            Rotation rotation = Rotation.NONE;
            String s = p_191129_5_.get1x1(this.random);
            if (p_191129_4_ != EnumFacing.EAST) {
                if (p_191129_4_ == EnumFacing.NORTH) {
                    rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
                }
                else if (p_191129_4_ == EnumFacing.WEST) {
                    rotation = rotation.add(Rotation.CLOCKWISE_180);
                }
                else if (p_191129_4_ == EnumFacing.SOUTH) {
                    rotation = rotation.add(Rotation.CLOCKWISE_90);
                }
                else {
                    s = p_191129_5_.get1x1Secret(this.random);
                }
            }
            BlockPos blockpos = Template.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, rotation, 7, 7);
            rotation = rotation.add(p_191129_3_);
            blockpos = blockpos.rotate(p_191129_3_);
            final BlockPos blockpos2 = p_191129_2_.add(blockpos.getX(), 0, blockpos.getZ());
            p_191129_1_.add(new MansionTemplate(this.templateManager, s, blockpos2, rotation));
        }
        
        private void addRoom1x2(final List<MansionTemplate> p_191132_1_, final BlockPos p_191132_2_, final Rotation p_191132_3_, final EnumFacing p_191132_4_, final EnumFacing p_191132_5_, final RoomCollection p_191132_6_, final boolean p_191132_7_) {
            if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.SOUTH) {
                final BlockPos blockpos13 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos13, p_191132_3_));
            }
            else if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos14 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos14 = blockpos14.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos14, p_191132_3_, Mirror.LEFT_RIGHT));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos15 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos15 = blockpos15.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos15, p_191132_3_.add(Rotation.CLOCKWISE_180)));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.SOUTH) {
                final BlockPos blockpos16 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos16, p_191132_3_, Mirror.FRONT_BACK));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos17 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos17, p_191132_3_.add(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.WEST) {
                final BlockPos blockpos18 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos18, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.WEST) {
                BlockPos blockpos19 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos19 = blockpos19.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos19, p_191132_3_.add(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.EAST) {
                BlockPos blockpos20 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos20 = blockpos20.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2SideEntrance(this.random, p_191132_7_), blockpos20, p_191132_3_.add(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.SOUTH && p_191132_4_ == EnumFacing.NORTH) {
                BlockPos blockpos21 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos21 = blockpos21.offset(p_191132_3_.rotate(EnumFacing.NORTH), 8);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2FrontEntrance(this.random, p_191132_7_), blockpos21, p_191132_3_));
            }
            else if (p_191132_5_ == EnumFacing.NORTH && p_191132_4_ == EnumFacing.SOUTH) {
                BlockPos blockpos22 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 7);
                blockpos22 = blockpos22.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 14);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2FrontEntrance(this.random, p_191132_7_), blockpos22, p_191132_3_.add(Rotation.CLOCKWISE_180)));
            }
            else if (p_191132_5_ == EnumFacing.WEST && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos23 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 15);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2FrontEntrance(this.random, p_191132_7_), blockpos23, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.EAST && p_191132_4_ == EnumFacing.WEST) {
                BlockPos blockpos24 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.WEST), 7);
                blockpos24 = blockpos24.offset(p_191132_3_.rotate(EnumFacing.SOUTH), 6);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2FrontEntrance(this.random, p_191132_7_), blockpos24, p_191132_3_.add(Rotation.COUNTERCLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.UP && p_191132_4_ == EnumFacing.EAST) {
                final BlockPos blockpos25 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 15);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2Secret(this.random), blockpos25, p_191132_3_.add(Rotation.CLOCKWISE_90)));
            }
            else if (p_191132_5_ == EnumFacing.UP && p_191132_4_ == EnumFacing.SOUTH) {
                BlockPos blockpos26 = p_191132_2_.offset(p_191132_3_.rotate(EnumFacing.EAST), 1);
                blockpos26 = blockpos26.offset(p_191132_3_.rotate(EnumFacing.NORTH), 0);
                p_191132_1_.add(new MansionTemplate(this.templateManager, p_191132_6_.get1x2Secret(this.random), blockpos26, p_191132_3_));
            }
        }
        
        private void addRoom2x2(final List<MansionTemplate> p_191127_1_, final BlockPos p_191127_2_, final Rotation p_191127_3_, final EnumFacing p_191127_4_, final EnumFacing p_191127_5_, final RoomCollection p_191127_6_) {
            int i = 0;
            int j = 0;
            Rotation rotation = p_191127_3_;
            Mirror mirror = Mirror.NONE;
            if (p_191127_5_ == EnumFacing.EAST && p_191127_4_ == EnumFacing.SOUTH) {
                i = -7;
            }
            else if (p_191127_5_ == EnumFacing.EAST && p_191127_4_ == EnumFacing.NORTH) {
                i = -7;
                j = 6;
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.NORTH && p_191127_4_ == EnumFacing.EAST) {
                i = 1;
                j = 14;
                rotation = p_191127_3_.add(Rotation.COUNTERCLOCKWISE_90);
            }
            else if (p_191127_5_ == EnumFacing.NORTH && p_191127_4_ == EnumFacing.WEST) {
                i = 7;
                j = 14;
                rotation = p_191127_3_.add(Rotation.COUNTERCLOCKWISE_90);
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.SOUTH && p_191127_4_ == EnumFacing.WEST) {
                i = 7;
                j = -8;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_90);
            }
            else if (p_191127_5_ == EnumFacing.SOUTH && p_191127_4_ == EnumFacing.EAST) {
                i = 1;
                j = -8;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_90);
                mirror = Mirror.LEFT_RIGHT;
            }
            else if (p_191127_5_ == EnumFacing.WEST && p_191127_4_ == EnumFacing.NORTH) {
                i = 15;
                j = 6;
                rotation = p_191127_3_.add(Rotation.CLOCKWISE_180);
            }
            else if (p_191127_5_ == EnumFacing.WEST && p_191127_4_ == EnumFacing.SOUTH) {
                i = 15;
                mirror = Mirror.FRONT_BACK;
            }
            BlockPos blockpos = p_191127_2_.offset(p_191127_3_.rotate(EnumFacing.EAST), i);
            blockpos = blockpos.offset(p_191127_3_.rotate(EnumFacing.SOUTH), j);
            p_191127_1_.add(new MansionTemplate(this.templateManager, p_191127_6_.get2x2(this.random), blockpos, rotation, mirror));
        }
        
        private void addRoom2x2Secret(final List<MansionTemplate> p_191128_1_, final BlockPos p_191128_2_, final Rotation p_191128_3_, final RoomCollection p_191128_4_) {
            final BlockPos blockpos = p_191128_2_.offset(p_191128_3_.rotate(EnumFacing.EAST), 1);
            p_191128_1_.add(new MansionTemplate(this.templateManager, p_191128_4_.get2x2Secret(this.random), blockpos, p_191128_3_, Mirror.NONE));
        }
    }
    
    abstract static class RoomCollection
    {
        private RoomCollection() {
        }
        
        public abstract String get1x1(final Random p0);
        
        public abstract String get1x1Secret(final Random p0);
        
        public abstract String get1x2SideEntrance(final Random p0, final boolean p1);
        
        public abstract String get1x2FrontEntrance(final Random p0, final boolean p1);
        
        public abstract String get1x2Secret(final Random p0);
        
        public abstract String get2x2(final Random p0);
        
        public abstract String get2x2Secret(final Random p0);
    }
    
    static class SecondFloor extends RoomCollection
    {
        private SecondFloor() {
        }
        
        @Override
        public String get1x1(final Random p_191104_1_) {
            return "1x1_b" + (p_191104_1_.nextInt(4) + 1);
        }
        
        @Override
        public String get1x1Secret(final Random p_191099_1_) {
            return "1x1_as" + (p_191099_1_.nextInt(4) + 1);
        }
        
        @Override
        public String get1x2SideEntrance(final Random p_191100_1_, final boolean p_191100_2_) {
            return p_191100_2_ ? "1x2_c_stairs" : ("1x2_c" + (p_191100_1_.nextInt(4) + 1));
        }
        
        @Override
        public String get1x2FrontEntrance(final Random p_191098_1_, final boolean p_191098_2_) {
            return p_191098_2_ ? "1x2_d_stairs" : ("1x2_d" + (p_191098_1_.nextInt(5) + 1));
        }
        
        @Override
        public String get1x2Secret(final Random p_191102_1_) {
            return "1x2_se" + (p_191102_1_.nextInt(1) + 1);
        }
        
        @Override
        public String get2x2(final Random p_191101_1_) {
            return "2x2_b" + (p_191101_1_.nextInt(5) + 1);
        }
        
        @Override
        public String get2x2Secret(final Random p_191103_1_) {
            return "2x2_s1";
        }
    }
    
    static class SimpleGrid
    {
        private final int[][] grid;
        private final int width;
        private final int height;
        private final int valueIfOutside;
        
        public SimpleGrid(final int p_i47358_1_, final int p_i47358_2_, final int p_i47358_3_) {
            this.width = p_i47358_1_;
            this.height = p_i47358_2_;
            this.valueIfOutside = p_i47358_3_;
            this.grid = new int[p_i47358_1_][p_i47358_2_];
        }
        
        public void set(final int p_191144_1_, final int p_191144_2_, final int p_191144_3_) {
            if (p_191144_1_ >= 0 && p_191144_1_ < this.width && p_191144_2_ >= 0 && p_191144_2_ < this.height) {
                this.grid[p_191144_1_][p_191144_2_] = p_191144_3_;
            }
        }
        
        public void set(final int p_191142_1_, final int p_191142_2_, final int p_191142_3_, final int p_191142_4_, final int p_191142_5_) {
            for (int i = p_191142_2_; i <= p_191142_4_; ++i) {
                for (int j = p_191142_1_; j <= p_191142_3_; ++j) {
                    this.set(j, i, p_191142_5_);
                }
            }
        }
        
        public int get(final int p_191145_1_, final int p_191145_2_) {
            return (p_191145_1_ >= 0 && p_191145_1_ < this.width && p_191145_2_ >= 0 && p_191145_2_ < this.height) ? this.grid[p_191145_1_][p_191145_2_] : this.valueIfOutside;
        }
        
        public void setIf(final int p_191141_1_, final int p_191141_2_, final int p_191141_3_, final int p_191141_4_) {
            if (this.get(p_191141_1_, p_191141_2_) == p_191141_3_) {
                this.set(p_191141_1_, p_191141_2_, p_191141_4_);
            }
        }
        
        public boolean edgesTo(final int p_191147_1_, final int p_191147_2_, final int p_191147_3_) {
            return this.get(p_191147_1_ - 1, p_191147_2_) == p_191147_3_ || this.get(p_191147_1_ + 1, p_191147_2_) == p_191147_3_ || this.get(p_191147_1_, p_191147_2_ + 1) == p_191147_3_ || this.get(p_191147_1_, p_191147_2_ - 1) == p_191147_3_;
        }
    }
    
    static class ThirdFloor extends SecondFloor
    {
        private ThirdFloor() {
        }
    }
}
